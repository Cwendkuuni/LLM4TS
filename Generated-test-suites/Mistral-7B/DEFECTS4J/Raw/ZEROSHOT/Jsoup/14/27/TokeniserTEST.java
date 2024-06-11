import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class TokeniserTest {

    private CharacterReader reader;
    private Tokeniser tokeniser;

    @BeforeEach
    public void setUp() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>".getBytes());
        reader = new InputStreamReader(inputStream);
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testReadNormalCharacter() {
        Token token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.Character, token.type);
        Assertions.assertEquals("h", token.data);
    }

    @Test
    public void testReadStartTag() {
        Token token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.StartTag, token.type);
        Assertions.assertEquals("html", token.tagName);

        token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.StartTag, token.type);
        Assertions.assertEquals("head", token.tagName);
    }

    @Test
    public void testReadEndTag() {
        Token token = tokeniser.read();
        token = tokeniser.read();
        token = tokeniser.read();
        Token endTag = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.EndTag, endTag.type);
        Assertions.assertEquals("html", endTag.tagName);
    }

    @Test
    public void testReadMultipleEndTags() {
        Token token = tokeniser.read();
        token = tokeniser.read();
        token = tokeniser.read();
        token = tokeniser.read();
        token = tokeniser.read();
        token = tokeniser.read();
        token = tokeniser.read();
        token = tokeniser.read();
        Token endTag1 = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.EndTag, endTag1.type);
        Assertions.assertEquals("body", endTag1.tagName);
        Token endTag2 = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.EndTag, endTag2.type);
        Assertions.assertEquals("html", endTag2.tagName);
    }

    @Test
    public void testReadSelfClosingTag() {
        tokeniser.transition(TokeniserState.SelfClosingStartTag);
        Token token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.StartTag, token.type);
        Assertions.assertTrue(token.selfClosing);
        Assertions.assertEquals("img", token.tagName);

        token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.Character, token.type);
        Assertions.assertEquals("\"", token.data);

        token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.Character, token.type);
        Assertions.assertEquals("src", token.data);

        token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.Character, token.type);
        Assertions.assertEquals("=", token.data);

        token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.Character, token.type);
        Assertions.assertEquals("\"", token.data);

        token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.Character, token.type);
        Assertions.assertEquals("/", token.data);

        token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.Character, token.type);
        Assertions.assertEquals("img", token.data);

        token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.EndTag, token.type);
        Assertions.assertEquals("img", token.tagName);
    }

    @Test
    public void testReadComment() {
        tokeniser.transition(TokeniserState.Comment);
        Token token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.Comment, token.type);
        Assertions.assertEquals("<!--", token.data);

        token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.Character, token.type);
        Assertions.assertEquals("-", token.data);

        token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.Character, token.type);
        Assertions.assertEquals("-->", token.data);

        token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.Character, token.type);
        Assertions.assertEquals(">", token.data);
    }

    @Test
    public void testReadDoctype() {
        tokeniser.transition(TokeniserState.Doctype);
        tokeniser.emit(new Doctype("html", null, null));
        Token token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.Doctype, token.type);
        Assertions.assertEquals("html", token.data);
    }

    @Test
    public void testErrorHandling() {
        tokeniser.setTrackErrors(false);
        tokeniser.transition(TokeniserState.Data);
        tokeniser.error(TokeniserState.Data);
        Token token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.Character, token.type);
        Assertions.assertEquals("", token.data);
        Assertions.assertEmpty(tokeniser.errors);
    }

    @Test
    public void testErrorHandlingWithTracking() {
        List<ParseError> errors = new ArrayList<>();
        tokeniser.setTrackErrors(true);
        tokeniser.transition(TokeniserState.Data);
        tokeniser.error(TokeniserState.Data);
        Token token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.Character, token.type);
        Assertions.assertEquals("", token.data);
        errors.addAll(tokeniser.errors);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Unexpected character in input", errors.get(0).message);
    }

    @Test
    public void testCharacterReference() {
        tokeniser.transition(TokeniserState.Data);
        Character character = tokeniser.consumeCharacterReference(null, false);
        Assertions.assertEquals(Token.TokenType.Character, tokeniser.read().type);
        Assertions.assertEquals("&", tokeniser.read().data);
        Assertions.assertEquals(Character.AMPERSAND, character);

        character = tokeniser.consumeCharacterReference('#', false);
        Assertions.assertEquals(Token.TokenType.Character, tokeniser.read().type);
        Assertions.assertEquals("1", tokeniser.read().data);
        Assertions.assertEquals(Character.DIGIT_0, character);

        character = tokeniser.consumeCharacterReference("nbsp", false);
        Assertions.assertEquals(Token.TokenType.Character, tokeniser.read().type);
        Assertions.assertEquals(" ", tokeniser.read().data);
        Assertions.assertEquals(Character.NON_BREAKING_SPACE, character);

        character = tokeniser.consumeCharacterReference("nbsp", true);
        Assertions.assertEquals(Token.TokenType.Character, tokeniser.read().type);
        Assertions.assertEquals("n", tokeniser.read().data);
        Assertions.assertEquals(Character.NON_BREAKING_SPACE, character);
        Assertions.assertEquals("b", tokeniser.read().data);
        Assertions.assertEquals("p", tokeniser.read().data);
    }
}