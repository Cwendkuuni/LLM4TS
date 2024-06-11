import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setup() {
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test</title></head><body></body></html>".getBytes());
        tokeniser = new Tokeniser(new CharacterReader(inputStream), new ParseErrorList());
        errors = tokeniser.errors;
    }

    @Test
    public void testRead_StateData_Character_ExpectCharacter() {
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Character character = (Character) token;
        Assert.assertEquals('<', character.getValue());
    }

    @Test
    public void testRead_StateData_CharacterRef_ExpectCharacter() {
        tokeniser.emit("&amp;");
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.advanceTransition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference(null, false);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Character character = (Character) token;
        Assert.assertEquals('&', character.getValue());
    }

    @Test(expected = AssertionError.class)
    public void testRead_StateData_CharacterRef_ExpectEndTag() {
        tokeniser.emit("&amp;");
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.advanceTransition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference("a", false);
        tokeniser.transition(TokeniserState.EndTag);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.fail("Expected error when reading character reference and expecting end tag");
    }

    @Test(expected = AssertionError.class)
    public void testRead_StateData_SelfClosingTag_ExpectCharacter() {
        tokeniser.emit("<img src=\"test.jpg\" />");
        tokeniser.transition(TokeniserState.SelfClosingTag);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.fail("Expected error when reading self-closing tag and expecting character");
    }

    @Test
    public void testRead_StateData_Script_ExpectScript() {
        tokeniser.emit("<script type=\"text/javascript\">");
        tokeniser.transition(TokeniserState.Script);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Script, token.type);
    }

    @Test
    public void testRead_StateData_Comment_ExpectComment() {
        tokeniser.emit("<!-- Test -->");
        tokeniser.transition(TokeniserState.Comment);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Comment, token.type);
    }

    @Test
    public void testRead_StateData_EndTag_ExpectEndTag() {
        tokeniser.emit("<html>");
        tokeniser.emit("<head>");
        tokeniser.emit("</head>");

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("head", token.value);
    }

    @Test
    public void testRead_StateData_EndTag_ExpectTag() {
        tokeniser.emit("<html>");
        tokeniser.emit("<head>");
        tokeniser.transition(TokeniserState.StartTag);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("head", token.value);
    }

    @Test
    public void testRead_StateData_EndTag_ExpectDoctype() {
        tokeniser.emit("<!DOCTYPE html>");
        tokeniser.transition(TokeniserState.Doctype);

        tokeniser.emit("<html>");
        tokeniser.transition(TokeniserState.StartTag);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("html", token.value);
    }

    @Test
    public void testRead_StateData_Error_ExpectCharacter() {
        tokeniser.error(TokeniserState.Data);

        Token token = tokeniser.read();
        Assert.fail("Expected error when reading in error state");
    }

    @Test
    public void testRead_StateData_Eof_ExpectCharacter() {
        tokeniser.emit("<html>");
        tokeniser.transition(TokeniserState.Data);
        tokeniser.eofError(TokeniserState.Data);

        Token token = tokeniser.read();
        Assert.fail("Expected error when reading at end of file");
    }

    @Test
    public void testRead_StateCharacterReference_Character_ExpectCharacterReference() {
        tokeniser.emit("&amp;");
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.advanceTransition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference(null, false);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.CharacterReference, token.type);
        CharacterReference characterReference = (CharacterReference) token;
        Assert.assertEquals('&', characterReference.getValue());
    }

    @Test
    public void testRead_StateCharacterReference_Character_ExpectCharacter() {
        tokeniser.emit("&amp;");
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.advanceTransition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference(null, false);

        tokeniser.emit("&a");
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.advanceTransition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference("a", false);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Character character = (Character) token;
        Assert.assertEquals('a', character.getValue());
    }

    @Test
    public void testRead_StateCharacterReference_Character_ExpectEndTag() {
        tokeniser.emit("&amp;");
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.advanceTransition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference(null, false);

        tokeniser.transition(TokeniserState.EndTag);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.fail("Expected error when reading character reference and expecting end tag");
    }

    @Test
    public void testRead_StateCharacterReference_Character_ExpectCharacterReference() {
        tokeniser.emit("&amp;");
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.advanceTransition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference(null, false);

        tokeniser.emit("&a");
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.advanceTransition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference("a", false);

        tokeniser.emit("&b");
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.advanceTransition(TokeniserState.CharacterReference);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.CharacterReference, token.type);
        CharacterReference characterReference = (CharacterReference) token;
        Assert.assertEquals('b', characterReference.getValue());
    }

    @Test
    public void testRead_StateScript_Script_ExpectScript() {
        tokeniser.emit("<script type=\"text/javascript\">");
        tokeniser.transition(TokeniserState.Script);

        tokeniser.emit("document.write('<p>Test</p>');");

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Script, token.type);
    }

    @Test
    public void testRead_StateComment_Comment_ExpectComment() {
        tokeniser.emit("<!-- Test -->");
        tokeniser.transition(TokeniserState.Comment);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Comment, token.type);
    }

    @Test
    public void testRead_StateEndTag_EndTag_ExpectEndTag() {
        tokeniser.emit("<html>");
        tokeniser.emit("<head>");
        tokeniser.transition(TokeniserState.EndTag);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("head", token.value);
    }

    @Test
    public void testRead_StateEndTag_EndTag_ExpectTag() {
        tokeniser.emit("<html>");
        tokeniser.emit("<head>");
        tokeniser.transition(TokeniserState.EndTag);
        tokeniser.transition(TokeniserState.StartTag);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("head", token.value);
    }

    @Test
    public void testRead_StateEndTag_EndTag_ExpectDoctype() {
        tokeniser.emit("<!DOCTYPE html>");
        tokeniser.transition(TokeniserState.Doctype);

        tokeniser.emit("<html>");
        tokeniser.transition(TokeniserState.StartTag);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("html", token.value);
    }

    @Test
    public void testRead_StateError_Error_ExpectCharacter() {
        tokeniser.error(TokeniserState.Data);

        tokeniser.emit("&amp;");
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.advanceTransition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference(null, false);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Character character = (Character) token;
        Assert.assertEquals('&', character.getValue());
    }

    @Test
    public void testRead_StateError_Error_ExpectEndTag() {
        tokeniser.error(TokeniserState.Data);

        tokeniser.emit("<html>");
        tokeniser.transition(TokeniserState.EndTag);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("html", token.value);
    }

    @Test
    public void testRead_StateEof_Eof_ExpectCharacter() {
        tokeniser.emit("<html>");
        tokeniser.transition(TokeniserState.Data);
        tokeniser.eofError(TokeniserState.Data);

        tokeniser.emit("&amp;");
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.advanceTransition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference(null, false);

        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Character character = (Character) token;
        Assert.assertEquals('&', character.getValue());
    }
}