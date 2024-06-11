package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setup() {
        CharBuffer charBuffer = CharBuffer.wrap("");
        CharacterReader characterReader = new CharacterReader(charBuffer);
        ParseErrorList parseErrorList = new ParseErrorList();
        tokeniser = new Tokeniser(characterReader, parseErrorList);
    }

    @Test
    public void testRead_EmptyInput() {
        Token token = tokeniser.read();

        assertNull(token);
    }

    @Test
    public void testRead_SelfClosingFlagNotAcknowledged_Error() {
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.emit(new Token.StartTag("img"));
        tokeniser.emit(new Token.EndTag());

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Error, token.type);
        assertEquals("Self closing flag not acknowledged", token.errorMessage);
        assertNull(token.data);
    }

    @Test
    public void testRead_EndTagWithAttributes_Error() {
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.emit(new Token.EndTag("div").attributes("id=\"test\""));

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Error, token.type);
        assertEquals("Attributes incorrectly present on end tag", token.errorMessage);
        assertNull(token.data);
    }

    @Test
    public void testEmit_WithCharacters() {
        tokeniser.emit("Hello World!");

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("Hello World!", token.data);
    }

    @Test
    public void testEmit_WithCharacterArray() {
        char[] chars = "Test".toCharArray();
        tokeniser.emit(chars);

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("Test", token.data);
    }

    @Test
    public void testEmit_WithCodepointsArray() {
        int[] codepoints = new int[]{72, 101, 108, 108, 111};
        tokeniser.emit(codepoints);

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("Hello", token.data);
    }

    @Test
    public void testEmit_WithCharacter() {
        tokeniser.emit('a');

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.data);
    }

    @Test
    public void testEmitTagPending_StartTag() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.attributes("id=\"test\"");
        tokeniser.tagPending.selfClosing = true;

        tokeniser.emitTagPending();

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("test", token.tagName);
        assertEquals("id=\"test\"", token.attributes);
        assertTrue(((Token.StartTag)token).selfClosing);
    }

    @Test
    public void testEmitTagPending_EndTag() {
        tokeniser.createTagPending(false);
        tokeniser.tagPending.attributes("class=\"test\"");

        tokeniser.emitTagPending();

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("test", token.tagName);
        assertNull(token.attributes);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.data("Test comment");

        tokeniser.emitCommentPending();

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals("Test comment", token.data);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name("html");
        tokeniser.doctypePending.publicIdentifier("publicId");
        tokeniser.doctypePending.systemIdentifier("systemId");

        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Doctype, token.type);
        assertEquals("html", token.name);
        assertEquals("publicId", token.publicIdentifier);
        assertEquals("systemId", token.systemIdentifier);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();

        assertEquals(0, tokeniser.dataBuffer.length());
    }

    @Test
    public void testIsAppropriateEndTagToken_True() {
        tokeniser.lastStartTag = "div";
        tokeniser.tagPending.name("div");

        boolean isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();

        assertTrue(isAppropriateEndTagToken);
    }

    @Test
    public void testIsAppropriateEndTagToken_False() {
        tokeniser.lastStartTag = "div";
        tokeniser.tagPending.name("p");

        boolean isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();

        assertFalse(isAppropriateEndTagToken);
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = "div";

        String appropriateEndTagName = tokeniser.appropriateEndTagName();

        assertEquals("div", appropriateEndTagName);
    }

    @Test
    public void testAppropriateEndTagName_NoLastStartTag() {
        String appropriateEndTagName = tokeniser.appropriateEndTagName();

        assertNull(appropriateEndTagName);
    }

    @Test
    public void testError_State() {
        tokeniser.error(TokeniserState.Data);

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Error, token.type);
        assertEquals("Unexpected character '' in input state [Data]", token.errorMessage);
        assertNull(token.data);
    }

    @Test
    public void testError_State_WithMessage() {
        tokeniser.error("Unexpected error occurred");

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Error, token.type);
        assertEquals("Unexpected error occurred", token.errorMessage);
        assertNull(token.data);
    }
    
    @Test
    public void testError_State_WithNullMessage() {
        tokeniser.error(null);

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Error, token.type);
        assertNull(token.errorMessage);
        assertNull(token.data);
    }

    @Test
    public void testEofError_State() {
        tokeniser.eofError(TokeniserState.Data);

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Error, token.type);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", token.errorMessage);
        assertNull(token.data);
    }

    @Test
    public void testEofError_State_WithMessage() {
        tokeniser.eofError(TokeniserState.Data);

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Error, token.type);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", token.errorMessage);
        assertNull(token.data);
    }
    
    @Test
    public void testEofError_State_WithNullMessage() {
        tokeniser.eofError(null);
        
        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Error, token.type);
        assertNull(token.errorMessage);
        assertNull(token.data);
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean currentNodeInHtmlNS = tokeniser.currentNodeInHtmlNS();

        assertTrue(currentNodeInHtmlNS);
    }

    @Test
    public void testUnescapeEntities_WithoutEntities() {
        String result = tokeniser.unescapeEntities(true);

        assertEquals("", result);
    }

    @Test
    public void testUnescapeEntities_WithEntities() {
        tokeniser.emit("Hello &amp; World!");

        String result = tokeniser.unescapeEntities(true);

        assertEquals("Hello & World!", result);
    }
}