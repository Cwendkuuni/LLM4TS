import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TokeniserTest {

    private Tokeniser createTokeniser(String input) {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        return new Tokeniser(new CharReader(in));
    }

    @Test
    public void testTokeniserConstructor() {
        Tokeniser tokeniser = new Tokeniser(null);
        Assert.assertNotNull(tokeniser);
    }

    @Test
    public void testRead_NormalCharacter() {
        String input = "abc";
        Tokeniser tokeniser = createTokeniser(input);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("a", token.asCharacter().toString());
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("b", token.asCharacter().toString());
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("c", token.asCharacter().toString());
    }

    @Test
    public void testRead_EndOfInput() {
        String input = "abc";
        Tokeniser tokeniser = createTokeniser(input);
        Token token = tokeniser.read(); // read a, b, and c
        Assert.assertFalse(tokeniser.isEmitPending());
        Assert.assertNull(token);
    }

    @Test
    public void testRead_SelfClosingFlag() {
        String input = "<img src='image.png' />";
        Tokeniser tokeniser = createTokeniser(input);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("img", token.asStartTag().tagName);
        Assert.assertTrue(token.asStartTag().selfClosing);
        tokeniser.emit(token);
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("img", token.asEndTag().tagName);
    }

    @Test
    public void testRead_CharacterReference() {
        String input = "&apos;";
        Tokeniser tokeniser = createTokeniser(input);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals('\'' , token.asCharacter().charValue());
    }

    @Test(expected = AssertionError.class)
    public void testRead_InvalidCharacterReference() {
        String input = "&invalid";
        Tokeniser tokeniser = createTokeniser(input);
        tokeniser.read();
    }

    @Test
    public void testRead_NamedCharacterReference() {
        String input = "&nbsp;";
        Tokeniser tokeniser = createTokeniser(input);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals('\u00A0' , token.asCharacter().charValue());
    }

    @Test
    public void testRead_UnmatchedEndTag() {
        String input = "<div><p>Hello<span>";
        Tokeniser tokeniser = createTokeniser(input);
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read(); // expect error here

        List<ParseError> errors = tokeniser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Attributes incorrectly present on end tag", errors.get(0).message);
    }

    @Test
    public void testRead_EmptyInput() {
        String input = "";
        Tokeniser tokeniser = createTokeniser(input);
        Token token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testRead_Whitespace() {
        String input = "\t \n \f\r ";
        Tokeniser tokeniser = createTokeniser(input);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("\t", token.asCharacter().toString());
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals(" ", token.asCharacter().toString());
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("\n", token.asCharacter().toString());
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("\f", token.asCharacter().toString());
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("\r", token.asCharacter().toString());
        token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals(" ", token.asCharacter().toString());
    }

    @Test
    public void testEmit() {
        String input = "abc";
        Tokeniser tokeniser = createTokeniser(input);
        Character c1 = tokeniser.read();
        tokeniser.emit(c1);
        Character c2 = tokeniser.read();
        tokeniser.emit(c2);
        Character c3 = tokeniser.read();
        tokeniser.emit(c3);

        List<Token> tokens = tokeniser.getTokens();
        Assert.assertEquals(3, tokens.size());
        Assert.assertEquals(Token.TokenType.Character, tokens.get(0).type);
        Assert.assertEquals("a", tokens.get(0).asCharacter().toString());
        Assert.assertEquals(Token.TokenType.Character, tokens.get(1).type);
        Assert.assertEquals("b", tokens.get(1).asCharacter().toString());
        Assert.assertEquals(Token.TokenType.Character, tokens.get(2).type);
        Assert.assertEquals("c", tokens.get(2).asCharacter().toString());
    }

    @Test
    public void testEmit_EndTag() {
        String input = "<div><p><span>";
        Tokeniser tokeniser = createTokeniser(input);
        Token startTagDiv = tokeniser.read();
        Token startTagP = tokeniser.read();
        Token startTagSpan = tokeniser.read();

        tokeniser.emit(startTagDiv);
        tokeniser.emit(startTagP);
        tokeniser.emitStartTag("p"); // should not emit corresponding end tag
        tokeniser.emit(startTagSpan);

        Token endTagSpan = tokeniser.read();
        tokeniser.emit(endTagSpan);

        List<Token> tokens = tokeniser.getTokens();
        Assert.assertEquals(4, tokens.size());
        Assert.assertEquals(Token.TokenType.StartTag, tokens.get(0).type);
        Assert.assertEquals("div", tokens.get(0).asStartTag().tagName);
        Assert.assertEquals(Token.TokenType.StartTag, tokens.get(1).type);
        Assert.assertEquals("p", tokens.get(2).asStartTag().tagName);
        Assert.assertEquals(Token.TokenType.EndTag, tokens.get(3).type);
        Assert.assertEquals("span", tokens.get(3).asEndTag().tagName);
    }

    @Test
    public void testEmit_StartTag() {
        String input = "<div><p><span>";
        Tokeniser tokeniser = createTokeniser(input);

        tokeniser.emitStartTag("p");
        Token endTagP = tokeniser.read();

        List<Token> tokens = tokeniser.getTokens();
        Assert.assertEquals(3, tokens.size());
        Assert.assertEquals(Token.TokenType.StartTag, tokens.get(0).type);
        Assert.assertEquals("div", tokens.get(0).asStartTag().tagName);
        Assert.assertEquals(Token.TokenType.StartTag, tokens.get(1).type);
        Assert.assertEquals("p", tokens.get(1).asStartTag().tagName);
        Assert.assertEquals(Token.TokenType.EndTag, tokens.get(2).type);
        Assert.assertEquals("p", tokens.get(2).asEndTag().tagName);
    }

    @Test
    public void testEmit_Comment() {
        String input = "<!DOCTYPE html><!-- This is a comment -->";
        Tokeniser tokeniser = createTokeniser(input);

        Token doctype = tokeniser.read();
        tokeniser.emit(doctype);

        Token comment = tokeniser.read();
        tokeniser.emitComment(comment);

        List<Token> tokens = tokeniser.getTokens();
        Assert.assertEquals(2, tokens.size());
        Assert.assertEquals(Token.TokenType.Doctype, tokens.get(0).type);
        Assert.assertEquals(null, tokens.get(0).asDoctype());
        Assert.assertEquals(Token.TokenType.Comment, tokens.get(1).type);
        Assert.assertEquals("This is a comment", tokens.get(1).asComment().data);
    }

    @Test
    public void testEmit_Doctype() {
        String input = "<!DOCTYPE html>";
        Tokeniser tokeniser = createTokeniser(input);

        Token doctype = tokeniser.read();
        tokeniser.emitDoctype(doctype);

        List<Token> tokens = tokeniser.getTokens();
        Assert.assertEquals(1, tokens.size());
        Assert.assertEquals(Token.TokenType.Doctype, tokens.get(0).type);
        Assert.assertEquals("html", tokens.get(0).asDoctype().name);
    }

    @Test
    public void testTransition() {
        String input = "<html>";
        Tokeniser tokeniser = createTokeniser(input);
        TokeniserState initialState = tokeniser.getState();

        tokeniser.transition(TokeniserState.S_IN_HTML);
        TokeniserState newState = tokeniser.getState();

        Assert.assertEquals(TokeniserState.S_IN_HTML, newState);
        Assert.assertNotEquals(initialState, newState);
    }

    @Test
    public void testGetState() {
        String input = "<html>";
        Tokeniser tokeniser = createTokeniser(input);

        Assert.assertEquals(TokeniserState.S_AT_INIT, tokeniser.getState());
    }

    @Test
    public void testAcceptSelfClosingFlag() {
        String input = "<input type='text' />";
        Tokeniser tokeniser = createTokeniser(input);

        tokeniser.acceptSelfClosingFlag();
        Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test(expected = AssertionError.class)
    public void testAcceptSelfClosingFlag_AlreadyAcknowledged() {
        String input = "<input type='text' />";
        Tokeniser tokeniser = createTokeniser(input);

        tokeniser.acceptSelfClosingFlag();
        tokeniser.acceptSelfClosingFlag();
    }

    @Test
    public void testGetErrors() {
        String input = "&invalid&<";
        Tokeniser tokeniser = createTokeniser(input);

        List<ParseError> errors = tokeniser.getErrors();
        Assert.assertEquals(2, errors.size());

        ParseError characterReferenceError = errors.get(0);
        Assert.assertEquals("Invalid character reference", characterReferenceError.message);
        Assert.assertEquals(input.length() - 1, characterReferenceError.line);
        Assert.assertEquals(CharacterReference.invalidCharacter, characterReferenceError.character);

        ParseError unexpectedCharacterError = errors.get(1);
        Assert.assertEquals("Unexpected character in input", unexpectedCharacterError.message);
        Assert.assertEquals(input.length() - 2, unexpectedCharacterError.line);
        Assert.assertEquals('<', unexpectedCharacterError.character);
    }

    @Test
    public void testGetTokens() {
        String input = "<html><body><h1>Hello, World!</h1></body></html>";
        Tokeniser tokeniser = createTokeniser(input);
        List<Token> tokens = tokeniser.getTokens();

        Assert.assertEquals(14, tokens.size());
        Assert.assertEquals(Token.TokenType.Doctype, tokens.get(0).type);
        Assert.assertEquals(null, tokens.get(0).asDoctype());
        Assert.assertEquals(Token.TokenType.StartTag, tokens.get(1).type);
        Assert.assertEquals("html", tokens.get(1).asStartTag().tagName);
        Assert.assertEquals(Token.TokenType.StartTag, tokens.get(2).type);
        Assert.assertEquals("head", tokens.get(2).asStartTag().tagName);
        Assert.assertEquals(Token.TokenType.EndTag, tokens.get(3).type);
        Assert.assertEquals("head", tokens.get(3).asEndTag().tagName);
        Assert.assertEquals(Token.TokenType.StartTag, tokens.get(4).type);
        Assert.assertEquals("body", tokens.get(4).asStartTag().tagName);
        Assert.assertEquals(Token.TokenType.Character, tokens.get(5).type);
        Assert.assertEquals(" ", tokens.get(5).asCharacter().toString());
        Assert.assertEquals(Token.TokenType.Character, tokens.get(6).type);
        Assert.assertEquals("H", tokens.get(6).asCharacter().toString());
        Assert.assertEquals(Token.TokenType.Character, tokens.get(7).type);
        Assert.assertEquals("1", tokens.get(7).asCharacter().toString());
        Assert.assertEquals(Token.TokenType.Character, tokens.get(8).type);
        Assert.assertEquals("!", tokens.get(8).asCharacter().toString());
        Assert.assertEquals(Token.TokenType.Character, tokens.get(9).type);
        Assert.assertEquals(" ", tokens.get(9).asCharacter().toString());
        Assert.assertEquals(Token.TokenType.Character, tokens.get(10).type);
        Assert.assertEquals("H", tokens.get(10).asCharacter().toString());
        Assert.assertEquals(Token.TokenType.Character, tokens.get(11).type);
        Assert.assertEquals("e", tokens.get(11).asCharacter().toString());
        Assert.assertEquals(Token.TokenType.Character, tokens.get(12).type);
        Assert.assertEquals("l", tokens.get(12).asCharacter().toString());
        Assert.assertEquals(Token.TokenType.Character, tokens.get(13).type);
        Assert.assertEquals("l", tokens.get(13).asCharacter().toString());
        Assert.assertEquals(Token.TokenType.Character, tokens.get(14).type);
        Assert.assertEquals("o", tokens.get(14).asCharacter().toString());
    }
}