package org.jsoup.parser; // Make sure the package name matches the given class

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.ParseErrorList;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Test class for Tokeniser class
 */
public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("<html><body><script>function test() { alert('Hello World!'); }</script></body></html>".getBytes());
        CharacterReader reader = new InputStreamReader(inputStream);
        this.errors = new ParseErrorList();
        this.tokeniser = new Tokeniser(reader, this.errors);
    }

    

    // Test constructor
    @Test
    public void testConstructor() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        assertNull(tokeniser.reader);
        assertNull(tokeniser.errors);
    }

    // Test read() method with no character found
    @Test
    public void testReadNoCharacter() {
        tokeniser.read();
        assertEquals(Token.TokenType.Eof, tokeniser.getState().getTokenType());
    }

    // Test read() method with character found
    @Test
    public void testReadCharacter() throws IOException {
        char character = (char) tokeniser.reader.current(); // Assuming character exists
        Token token = tokeniser.read();
        Token expectedToken = new Token.Character();
        expectedToken.data(Character.toString(character));
        assertEquals(expectedToken, token);
    }

    // Test emit() method with character token
    @Test
    public void testEmitCharacterToken() {
        char character = 'a';
        tokeniser.emit(new Token.Character().data(Character.toString(character)));
        assertEquals(Token.TokenType.Character, tokeniser.getState().getTokenType());
        assertTrue(tokeniser.emitPending instanceof Token.Character);
    }

    // Test emit() method with start tag token
    @Test
    public void testEmitStartTagToken() {
        String tagName = "div";
        Token startTagToken = new Token.StartTag().setTagName(tagName);
        tokeniser.emit(startTagToken);
        assertEquals(Token.TokenType.StartTag, tokeniser.getState().getTokenType());
        assertTrue(tokeniser.emitPending instanceof Token.StartTag);
        assertEquals(tagName, ((Token.StartTag) tokeniser.emitPending).tagName());
    }

    // Test emit() method with end tag token
    @Test
    public void testEmitEndTagToken() {
        String tagName = "div";
        Token endTagToken = new Token.EndTag().setTagName(tagName);
        tokeniser.emit(tagName);
        tokeniser.emit(endTagToken);
        assertEquals(Token.TokenType.EndTag, tokeniser.getState().getTokenType());
        assertTrue(tokeniser.emitPending instanceof Token.EndTag);
        assertEquals(tagName, ((Token.EndTag) tokeniser.emitPending).tagName());
    }

    // Test emit() method with string token
    @Test
    public void testEmitStringToken() {
        String stringToEmit = "Hello World!";
        tokeniser.emit(stringToEmit);
        assertEquals(Token.TokenType.Text, tokeniser.getState().getTokenType());
        assertTrue(tokeniser.emitPending instanceof Token.Character);
        assertEquals(stringToEmit, ((Token.Character) tokeniser.emitPending).data());
    }

    // Test emit() method with doctype token
    @Test
    public void testEmitDoctypeToken() {
        String doctypeName = "html";
        String publicId = "HTML5";
        String systemId = "";
        Token doctypeToken = new Token.Doctype().setName(doctypeName).setPublicId(publicId).setSystemId(systemId);
        tokeniser.emit(doctypeToken);
        assertEquals(Token.TokenType.Doctype, tokeniser.getState().getTokenType());
        assertTrue(tokeniser.emitPending instanceof Token.Doctype);
        assertEquals(doctypeName, ((Token.Doctype) tokeniser.emitPending).getName());
        assertEquals(publicId, ((Token.Doctype) tokeniser.emitPending).getPublicId());
        assertEquals(systemId, ((Token.Doctype) tokeniser.emitPending).getSystemId());
    }

    // Test emit() method with comment token
    @Test
    public void testEmitCommentToken() {
        String commentText = "This is a comment.";
        tokeniser.emit(new Token.Comment().setText(commentText));
        assertEquals(Token.TokenType.Comment, tokeniser.getState().getTokenType());
        assertTrue(tokeniser.emitPending instanceof Token.Comment);
        assertEquals(commentText, ((Token.Comment) tokeniser.emitPending).getText());
    }

    // Test getState() method
    @Test
    public void testGetState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    // Test transition() method
    @Test
    public void testTransition() {
        TokeniserState newState = TokeniserState.Script;
        tokeniser.transition(newState);
        assertEquals(newState, tokeniser.getState());
    }

    // Test acknowledgeSelfClosingFlag() method
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    // Test consumeCharacterReference() method with valid numeric reference
    @Test
    public void testConsumeCharacterReferenceNumeric() throws IOException {
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        assertEquals(1, codepoints.length);
        assertEquals('\u00A9', codepoints[0]); // copyright symbol
    }

    // Test consumeCharacterReference() method with invalid numeric reference
    @Test
    public void testConsumeCharacterReferenceInvalidNumeric() throws IOException {
        tokeniser.consumeCharacterReference((char) 0xFFFD, false);
        assertEquals(TokeniserState.ErrorRecovery, tokeniser.getState());
        assertTrue(tokeniser.errors.size() > 0);
    }

    // Test consumeCharacterReference() method with valid named reference
    @Test
    public void testConsumeCharacterReferenceNamed() throws IOException {
        int[] codepoints = tokeniser.consumeCharacterReference("ampersand", false);
        assertEquals(1, codepoints.length);
        assertEquals('&', codepoints[0]);
    }

    // Test consumeCharacterReference() method with invalid named reference
    @Test
    public void testConsumeCharacterReferenceInvalidNamed() throws IOException {
        tokeniser.consumeCharacterReference("invalidEntity", false);
        assertEquals(TokeniserState.ErrorRecovery, tokeniser.getState());
        assertTrue(tokeniser.errors.size() > 0);
    }

    // Test createTagPending() method with start tag
    @Test
    public void testCreateTagPendingStartTag() {
        String tagName = "div";
        TokeniserState state = TokeniserState.Data;
        Token.StartTag startTag = tokeniser.createTagPending(true);
        tokeniser.transition(state);
        tokeniser.emitTagPending();
        assertEquals(tagName, tokeniser.appropriateEndTagName());
    }

    // Test createTagPending() method with end tag
    @Test
    public void testCreateTagPendingEndTag() {
        String tagName = "div";
        tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        assertEquals(tagName, tokeniser.appropriateEndTagName());
    }

    // Test error() method with ParseErrorList error handling
    @Test
    public void testErrorParseErrorList() {
        String errorMessage = "This is a test error message.";
        tokeniser.error(errorMessage);
        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals(errorMessage, error.getMessage());
        assertEquals(tokeniser.reader.pos(), error.getErrorPos());
    }

    // Test eofError() method
    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", error.getMessage());
        assertEquals(tokeniser.reader.pos(), error.getErrorPos());
    }

    // Test characterReferenceError() method
    @Test
    public void testCharacterReferenceError() {
        String errorMessage = "Invalid character reference: Invalid reference.";
        tokeniser.characterReferenceError(errorMessage);
        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals(errorMessage, error.getMessage());
        assertEquals(tokeniser.reader.pos(), error.getErrorPos());
    }

    // Test error() method with custom error message
    @Test
    public void testErrorCustomMessage() {
        String errorMessage = "This is a custom error message.";
        tokeniser.error(errorMessage);
        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals(errorMessage, error.getMessage());
        assertEquals(tokeniser.reader.pos(), error.getErrorPos());
    }

    // Test isAppropriateEndTagToken() method
    @Test
    public void testIsAppropriateEndTagToken() {
        String startTagName = "div";
        tokeniser.emit(new Token.StartTag().setTagName(startTagName));
        assertTrue(tokeniser.isAppropriateEndTagToken());
        assertEquals(startTagName, tokeniser.appropriateEndTagName());

        tokeniser.transition(TokeniserState.Data);

        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    // Test unescapeEntities() method
    @Test
    public void testUnescapeEntities() {
        String input = "&amp; Hello &lt;World&gt;! &quot; &apos; &egrave; &#128; &#129;";
        String output = tokeniser.unescapeEntities(false);
        assertEquals("&amp; Hello &lt;World&gt;! &quot; &apos; &egrave; &#x7f;&#x80;", output);

        output = tokeniser.unescapeEntities(true);
        assertEquals("&amp;Hello&lt;World&gt;!&quot;&apos;&egrave; &#128;&#129;", output);
    }

    
}