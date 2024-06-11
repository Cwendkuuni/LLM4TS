import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private ByteArrayInputStream inputStream;
    private List<ParseError> errors;

    @Before
    public void setup() {
        inputStream = new ByteArrayInputStream("<html><head><title>Test</title></head><body><script type='text/javascript'></script></body></html>".getBytes());
        tokeniser = new Tokeniser(new CharacterReader(inputStream));
        errors = new ArrayList<>();
        tokeniser.setTrackErrors(true);
    }

    @Test
    public void testRead_Basic() {
        Token token;
        for (int i = 0; i < 7; i++) {
            token = tokeniser.read();
            assertEquals("Unexpected token type: " + token.type, Token.TokenType.values()[i], token.type);
            if (token.type == Token.TokenType.StartTag) {
                assertEquals("Unexpected tag name: " + token.tagName, "html", token.tagName);
            } else if (token.type == Token.TokenType.EndTag) {
                assertEquals("Unexpected end tag name: " + token.tagName, "/html", token.tagName);
            }
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testRead_Error() {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read(); // should throw an exception since there is no next token
    }

    @Test
    public void testEmit_Character() {
        tokeniser.emit(new Token.Character('a'));
        Token token = tokeniser.read();
        assertEquals("Unexpected token type: " + token.type, Token.TokenType.Character, token.type);
        assertEquals("Unexpected character: " + token.data, "a", token.data);
    }

    @Test
    public void testEmit_StartTag() {
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.read();
        tokeniser.emit(new Token.StartTag("html"));
        tokeniser.emit(new Token.Character(' '));
        tokeniser.emit(new Token.Character('>'));
        Token token = tokeniser.read();
        assertEquals("Unexpected token type: " + token.type, Token.TokenType.StartTag, token.type);
        assertEquals("Unexpected tag name: " + token.tagName, "html", token.tagName);
    }

    @Test
    public void testEmit_EndTag() {
        tokeniser.transition(TokeniserState.Data);
        tokeniser.read(); // read <html>
        tokeniser.read(); // read <head>
        tokeniser.read(); // read <title>
        tokeniser.transition(TokeniserState.EndTag);
        tokeniser.read(); // read </title>
        tokeniser.emit(new Token.EndTag("/title"));
        tokeniser.emit(new Token.Character('>'));
        Token token = tokeniser.read();
        assertEquals("Unexpected token type: " + token.type, Token.TokenType.EndTag, token.type);
        assertEquals("Unexpected end tag name: " + token.tagName, "/title", token.tagName);
    }

    @Test
    public void testEmit_Comment() {
        tokeniser.transition(TokeniserState.Comment);
        tokeniser.read(); // read <!
        tokeniser.emit(new Token.Comment("-- Test"));
        tokeniser.emit(new Token.Character('>'));
        Token token = tokeniser.read();
        assertEquals("Unexpected token type: " + token.type, Token.TokenType.Comment, token.type);
        assertEquals("Unexpected comment: " + token.data, "-- Test", token.data);
    }

    @Test
    public void testEmit_Doctype() {
        tokeniser.transition(TokeniserState.Doctype);
        tokeniser.read(); // read <!DOCTYPE
        tokeniser.read(); // read html
        tokeniser.read(); // read [
        tokeniser.emit(new Token.Doctype());
        tokeniser.emit(new Token.Character('>'));
        Token token = tokeniser.read();
        assertEquals("Unexpected token type: " + token.type, Token.TokenType.EOF, token.type);
    }

    @Test
    public void testConsumeCharacterReference_Basic() {
        tokeniser.transition(TokeniserState.Data);
        tokeniser.read(); // read <
        tokeniser.advanceTransition(TokeniserState.CharacterReference);
        CharacterReference characterReference = tokeniser.consumeCharacterReference(null, false);
        assertEquals("Unexpected character reference: " + characterReference, "&amp;", characterReference.toString());
        tokeniser.emit(characterReference);
        tokeniser.emit(new Token.Character('>'));
        Token token = tokeniser.read();
        assertEquals("Unexpected token type: " + token.type, Token.TokenType.EndTag, token.type);
        assertEquals("Unexpected end tag name: " + token.tagName, "/html", token.tagName);
    }

    @Test
    public void testConsumeCharacterReference_Invalid() {
        tokeniser.transition(TokeniserState.Data);
        tokeniser.read(); // read <
        tokeniser.advanceTransition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference('>', false);
        assertTrue("Expected character reference error, but no error was thrown.", errors.size() > 0);
        ParseError error = errors.get(0);
        assertEquals("Invalid character reference", error.getMessage());
        assertEquals(1, error.getLineNumber());
        assertEquals(7, error.getColumnNumber());
        assertEquals(TokeniserState.Data.toString(), error.getState().toString());
    }

    @Test
    public void testConsumeCharacterReference_Named() {
        tokeniser.transition(TokeniserState.Data);
        tokeniser.read(); // read <
        tokeniser.advanceTransition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference("amp", false);
        assertEquals("Unexpected character reference: " + tokeniser.read().data, "&amp;", tokeniser.read().data);
        tokeniser.emit(tokeniser.read());
        tokeniser.emit(new Token.Character('>'));
        Token token = tokeniser.read();
        assertEquals("Unexpected token type: " + token.type, Token.TokenType.EndTag, token.type);
        assertEquals("Unexpected end tag name: " + token.tagName, "/html", token.tagName);
    }

    @Test
    public void testConsumeCharacterReference_InvalidNamed() {
        tokeniser.transition(TokeniserState.Data);
        tokeniser.read(); // read <
        tokeniser.advanceTransition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference("xamp", false);
        assertTrue("Expected character reference error, but no error was thrown.", errors.size() > 0);
        ParseError error = errors.get(0);
        assertEquals("Invalid character reference", error.getMessage());
        assertEquals(1, error.getLineNumber());
        assertEquals(11, error.getColumnNumber());
        assertEquals(TokeniserState.Data.toString(), error.getState().toString());
    }

    @Test
    public void testConsumeCharacterReference_NamedWithAttribute() {
        tokeniser.transition(TokeniserState.Data); // read <html
        tokeniser.read();
        tokeniser.transition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference("amp", true);
        assertFalse("Expected error since character references are not allowed in attributes.", errors.isEmpty());
        ParseError error = errors.get(0);
        assertEquals("Attributes incorrectly present on character reference", error.getMessage());
        assertEquals(1, error.getLineNumber());
        assertEquals(10, error.getColumnNumber());
        assertEquals(TokeniserState.Data.toString(), error.getState().toString());
    }

    @Test
    public void testCreateTagPending_Start() {
        Token.StartTag tagPending = tokeniser.createTagPending(true);
        assertEquals("Unexpected tag name: " + tagPending.tagName, "", tagPending.tagName);
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertEquals("Unexpected token type: " + token.type, Token.TokenType.StartTag, token.type);
        assertEquals("Unexpected tag name: " + token.tagName, "", token.tagName);
    }

    @Test
    public void testCreateTagPending_End() {
        Token.EndTag tagPending = tokeniser.createTagPending(false);
        assertEquals("Unexpected tag name: " + tagPending.tagName, "", tagPending.tagName);
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertEquals("Unexpected token type: " + token.type, Token.TokenType.EndTag, token.type);
        assertEquals("Unexpected end tag name: " + token.tagName, "", token.tagName);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emit(new Token.StartTag("html"));
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emit(new Token.StartTag("body"));
        tokeniser.transition(TokeniserState.EndTag);
        tokeniser.emit(new Token.EndTag("/body"));
        tokeniser.emit(new Token.EndTag("/html"));
        assertTrue("Expected appropriate end tag token: " + tokeniser.isAppropriateEndTagToken(), tokeniser.isAppropriateEndTagToken());

        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emit(new Token.StartTag("html"));
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emit(new Token.StartTag("body"));
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emit(new Token.StartTag("div"));
        tokeniser.transition(TokeniserState.EndTag);
        tokeniser.emit(new Token.EndTag("/div"));
        tokeniser.transition(TokeniserState.EndTag);
        tokeniser.emit(new Token.EndTag("/body"));
        tokeniser.emit(new Token.EndTag("/html"));
        assertFalse("Expected inappropriate end tag token: " + tokeniser.isAppropriateEndTagToken(), tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testErrorHandling_UnexpectedCharacter() {
        tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("&".getBytes())));
        tokeniser.setTrackErrors(true);
        tokeniser.read();
        tokeniser.error(TokeniserState.Data);
        assertTrue("Expected error, but no error was thrown.", errors.size() > 0);
        ParseError error = errors.get(0);
        assertEquals("Invalid character reference", error.getMessage());
        assertEquals(1, error.getLineNumber());
        assertEquals(1, error.getColumnNumber());
        assertEquals(TokeniserState.Data.toString(), error.getState().toString());
    }

    @Test
    public void testErrorHandling_EOF() {
        tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())));
        tokeniser.setTrackErrors(true);
        tokeniser.read();
        tokeniser.read();
        tokeniser.transition(TokeniserState.Data);
        tokeniser.eofError(TokeniserState.Data);
        assertTrue("Expected error, but no error was thrown.", errors.size() > 0);
        ParseError error = errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF)", error.getMessage());
        assertEquals(1, error.getLineNumber());
        assertEquals(1, error.getColumnNumber());
        assertEquals(TokeniserState.Data.toString(), error.getState().toString());
    }

    @Test
    public void testErrorHandling_CharacterReference() {
        tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<p id='foo'>&#x1234;".getBytes())));
        tokeniser.setTrackErrors(true);
        tokeniser.read();
        tokeniser.read();
        tokeniser.transition(TokeniserState.CharacterReference);
        tokeniser.error(TokeniserState.CharacterReference);
        assertTrue("Expected error, but no error was thrown.", errors.size() > 0);
        ParseError error = errors.get(0);
        assertEquals("Invalid character reference", error.getMessage());
        assertEquals(1, error.getLineNumber());
        assertEquals(6, error.getColumnNumber());
        assertEquals(TokeniserState.CharacterReference.toString(), error.getState().toString());
    }
}