import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Error;
import org.jsoup.parser.Token.StartTag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private CharacterReader characterReader;
    private ParseErrorList errorList;

    @Before
    public void setup() {
        characterReader = new CharacterReader(new ByteArrayInputStream("<html><head></head><body></body>".getBytes()));
        errorList = new ParseErrorList();
        tokeniser = new Tokeniser(characterReader, errorList);
    }

    @Test
    public void testRead_DataState_ReturnsCharacterToken() {
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("html", token.data);
    }

    @Test
    public void testRead_DataState_BuffersMultipleCharacters() {
        tokeniser.emit("html");
        tokeniser.read();
        tokeniser.emit(" <head>");
        tokeniser.read();
        tokeniser.emit(" ");
        tokeniser.read();

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("e", token.data);
    }

    @Test
    public void testRead_ScriptState_ReturnsScriptToken() {
        tokeniser.transition(TokeniserState.Script);
        tokeniser.read();
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("script", token.tagName);
    }

    @Test(expected = AssertionError.class)
    public void testRead_ScriptState_ThrowsErrorOnNonScriptTag() {
        tokeniser.transition(TokeniserState.Script);
        tokeniser.read();
        tokeniser.read(); // consume "<head>"
        tokeniser.read(); // consume ">"
        tokeniser.read(); // consume "h"
        tokeniser.read(); // consume "t"
        tokeniser.read(); // consume "m"
        tokeniser.read(); // consume "l"

        tokeniser.read(); // consume " "
        tokeniser.read(); // consume "/"
        tokeniser.read(); // consume "h"
        tokeniser.read(); // consume "d"
        tokeniser.read(); // consume ">"

        tokeniser.read(); // consume ">"

        tokeniser.read(); // consume "b"
        tokeniser.read(); // consume "o"
        tokeniser.read(); // consume "d"
        tokeniser.read(); // consume "y"

        tokeniser.read(); // consume " "
        tokeniser.read(); // consume "/"
        tokeniser.read(); // consume "b"
        tokeniser.read(); // consume "o"
        tokeniser.read(); // consume "d"
        tokeniser.read(); // consume "y"

        tokeniser.read(); // consume ">"

        tokeniser.read(); // consume "e"

        tokeniser.read(); // consume " "
        tokeniser.read(); // consume "/"
        tokeniser.read(); // consume "h"
        tokeniser.read(); // consume "1"
        tokeniser.read(); // consume " "
        tokeniser.read(); // consume ">"

        tokeniser.read(); // consume ">"

        tokeniser.read(); // consume "a"
        tokeniser.read(); // consume " "
        tokeniser.read(); // consume "/"
        tokeniser.read(); // consume "a"
        tokeniser.read(); // consume ">"

        tokeniser.read(); // consume ">"

        tokeniser.read(); // consume "p"
        tokeniser.read(); // consume " "
        tokeniser.read(); // consume "/"
        tokeniser.read(); // consume "p"
        tokeniser.read(); // consume ">"

        tokeniser.read(); // consume ">"

        tokeniser.read(); // consume "s"
        tokeniser.read(); // consume "c"
        tokeniser.read(); // consume "r"
        tokeniser.read(); // consume "i"
        tokeniser.read(); // consume "p"
        tokeniser.read(); // consume "t"
        tokeniser.read(); // consume ">"

        tokeniser.read(); // consume ">"

        tokeniser.read(); // consume "t"
        tokeniser.read(); // consume "h"
        tokeniser.read(); // consume "e"
        tokeniser.read(); // consume " "
        tokeniser.read(); // consume "/"
        tokeniser.read(); // consume "t"
        tokeniser.read(); // consume "h"
        tokeniser.read(); // consume "e"
        tokeniser.read(); // consume ">"

        tokeniser.read(); // consume ">"

        tokeniser.read(); // consume "h"
        tokeniser.read(); // consume "t"
        tokeniser.read(); // consume "m"
        tokeniser.read(); // consume "l"
        tokeniser.read(); // consume ">"

        // Should throw an AssertionError due to unexpected token
        tokeniser.read();
    }

    @Test
    public void testEmit_CharacterToken() {
        tokeniser.emit(new Character('h'));
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("h", token.data);
    }

    @Test
    public void testEmit_StartTagToken() {
        tokeniser.emit(new StartTag("html"));
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.tagName);
    }

    @Test
    public void testEmit_EndTagToken() {
        tokeniser.emitStartTag("html");
        tokeniser.emit(">");
        tokeniser.emit(" <body>");
        tokeniser.emit("</body>");
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("body", token.tagName);
    }

    @Test
    public void testEmit_CommentToken() {
        tokeniser.emitComment("This is a comment.");
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Comment, token.type);
        Assert.assertEquals("This is a comment.", token.data);
    }

    @Test
    public void testEmit_DoctypeToken() {
        tokeniser.createDoctypePending();
        tokeniser.emit("DOCTYPE");
        tokeniser.emit(" html");
        tokeniser.emit(" PUBLIC");
        tokeniser.emit(" \"-//W3C//DTD HTML 4.01//EN\"");
        tokeniser.emit(" [");
        tokeniser.emit("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" [");
        tokeniser.emit("]");
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Doctype, token.type);
        Assert.assertEquals("html", token.publicId);
        Assert.assertEquals("html", token.systemId);
    }

    @Test(expected = AssertionError.class)
    public void testTransition_InvalidStateTransition() {
        tokeniser.transition(TokeniserState.Script);
        tokeniser.transition(TokeniserState.ScriptData);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference_ValidCharacterReference() {
        tokeniser.consumeCharacterReference('a');
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("a", token.data);
    }

    @Test
    public void testConsumeCharacterReference_InvalidCharacterReference() {
        tokeniser.consumeCharacterReference('&');
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Error, token.type);
        Assert.assertEquals(Error.Type.CharacterReferenceError, token.errorType);
        Assert.assertEquals("invalid named referenece '&'", token.errorMessage);
    }

    @Test
    public void testConsumeCharacterReference_InvalidCharacterReferenceWithSemicolon() {
        tokeniser.consumeCharacterReference("&amp;");
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("&amp;", token.data);
    }

    @Test
    public void testCreateTagPending_StartTag() {
        tokeniser.createTagPending(true);
        Token tagPending = tokeniser.tagPending;
        Assert.assertNotNull(tagPending);
        Assert.assertEquals(Tag.START_TAG, tagPending.tagType);
    }

    @Test
    public void testCreateTagPending_EndTag() {
        tokeniser.createTagPending(false);
        Token tagPending = tokeniser.tagPending;
        Assert.assertNotNull(tagPending);
        Assert.assertEquals(Tag.END_TAG, tagPending.tagType);
    }

    @Test
    public void testEmitTagPending_StartTag() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("html", token.tagName);
    }

    @Test
    public void testEmitTagPending_EndTag() {
        tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        tokeniser.read(); // consume "<body>"
        tokeniser.read(); // consume ">"
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("body", token.tagName);
    }

    @Test(expected = AssertionError.class)
    public void testEmitTagPending_IncorrectEndTag() {
        tokeniser.createTagPending(true);
        tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        tokeniser.read(); // consume "<html>"
        tokeniser.read(); // consume ">"
        tokeniser.read(); // consume "<body>"
        tokeniser.read(); // consume ">"
        tokeniser.emitTagPending();

        tokeniser.read(); // consume "</body>"
        Token token = tokeniser.read();

        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.EndTag, token.type);
        Assert.assertEquals("html", token.tagName); // should be "body"
    }

    @Test
    public void testError_InvalidCharacter() {
        tokeniser.error(TokeniserState.Data);
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Error, token.type);
        Assert.assertEquals(Error.Type.InvalidCharacter, token.errorType);
    }

    @Test
    public void testError_UnexpectedEndOfFile() {
        tokeniser.eofError(TokeniserState.Data);
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Error, token.type);
        Assert.assertEquals(Error.Type.EndOfFileError, token.errorType);
    }

    @Test
    public void testCharacterReferenceError_MissingSemicolon() {
        tokeniser.characterReferenceError("numeric reference with no semicolon");
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Error, token.type);
        Assert.assertEquals(Error.Type.CharacterReferenceError, token.errorType);
        Assert.assertEquals("missing semicolon", token.errorMessage);
    }

    @Test
    public void testCharacterReferenceError_NamedReference() {
        tokeniser.characterReferenceError("invalid named referenece '&'");
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Error, token.type);
        Assert.assertEquals(Error.Type.CharacterReferenceError, token.errorType);
        Assert.assertEquals("invalid named referenece '&'", token.errorMessage);
    }

    @Test
    public void testCharacterReferenceError_NamedReferenceWithSemicolon() {
        tokeniser.characterReferenceError("invalid named referenece '&amp;'");
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("&amp;", token.data);
    }
}