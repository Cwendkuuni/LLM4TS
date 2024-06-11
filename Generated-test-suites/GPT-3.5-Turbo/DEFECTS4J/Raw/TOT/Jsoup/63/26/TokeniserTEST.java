package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";

        tokeniser.emit(startTag);

        Token result = tokeniser.read();

        assertNotNull(result);
        assertEquals(startTag, result);
    }

    @Test
    public void testEmitString() {
        String str = "Test String";

        tokeniser.emit(str);

        Token result = tokeniser.read();

        assertNotNull(result);
        assertEquals(str, result.toString());
    }

    @Test
    public void testEmitCharArray() {
        char[] chars = {'T', 'e', 's', 't'};

        tokeniser.emit(chars);

        Token result = tokeniser.read();

        assertNotNull(result);
        assertEquals("Test", result.toString());
    }

    @Test
    public void testEmitIntCodepoints() {
        int[] codepoints = {84, 101, 115, 116};

        tokeniser.emit(codepoints);

        Token result = tokeniser.read();

        assertNotNull(result);
        assertEquals("Test", result.toString());
    }

    @Test
    public void testEmitChar() {
        char c = 'T';

        tokeniser.emit(c);

        Token result = tokeniser.read();

        assertNotNull(result);
        assertEquals("T", result.toString());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();

        assertNotNull(state);
        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.TagName);

        TokeniserState state = tokeniser.getState();

        assertNotNull(state);
        assertEquals(TokeniserState.TagName, state);
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.TagName);

        TokeniserState state = tokeniser.getState();
        CharacterReader reader = tokeniser.reader;

        assertNotNull(state);
        assertEquals(TokeniserState.TagName, state);
        assertEquals(1, reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        tokeniser.reader = new CharacterReader("&#65;");

        int[] result = tokeniser.consumeCharacterReference(null, false);

        assertNotNull(result);
        assertEquals(65, result[0]);
    }

    @Test
    public void testConsumeCharacterReferenceAdditionalAllowed() {
        tokeniser.reader = new CharacterReader("&#66;");

        int[] result = tokeniser.consumeCharacterReference('B', false);

        assertNull(result);
    }

    @Test
    public void testConsumeCharacterReferenceNotCharRefCharsSorted() {
        tokeniser.reader = new CharacterReader("<p>&");

        int[] result = tokeniser.consumeCharacterReference(null, false);

        assertNull(result);
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);

        assertNotNull(tokeniser.tagPending);
        assertTrue(tokeniser.tagPending instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.StartTag("div");

        tokeniser.emitTagPending();

        Token result = tokeniser.read();

        assertNotNull(result);
        assertTrue(result instanceof Token.StartTag);
        assertEquals("div", ((Token.StartTag) result).tagName);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();

        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.commentPending = new Token.Comment("Test comment");

        tokeniser.emitCommentPending();

        Token result = tokeniser.read();

        assertNotNull(result);
        assertTrue(result instanceof Token.Comment);
        assertEquals("Test comment", result.toString());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();

        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.doctypePending = new Token.Doctype("html", "test", "test");

        tokeniser.emitDoctypePending();

        Token result = tokeniser.read();

        assertNotNull(result);
        assertTrue(result instanceof Token.Doctype);
        assertEquals("html", ((Token.Doctype) result).name);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();

        assertEquals("", tokeniser.dataBuffer.toString());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = "p";
        tokeniser.tagPending = new Token.EndTag("p");

        boolean result = tokeniser.isAppropriateEndTagToken();

        assertTrue(result);
    }

    @Test
    public void testIsAppropriateEndTagTokenFalse() {
        tokeniser.lastStartTag = "p";
        tokeniser.tagPending = new Token.EndTag("div");

        boolean result = tokeniser.isAppropriateEndTagToken();

        assertFalse(result);
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = "p";

        String result = tokeniser.appropriateEndTagName();

        assertEquals("p", result);
    }

    @Test
    public void testErrorTokeniserState() {
        tokeniser.error(TokeniserState.Data);

        ParseErrorList errorList = tokeniser.errors;
        ParseError error = errorList.get(0);

        assertEquals(1, errorList.size());
        assertEquals("Unexpected character '',ReaderPos=1,Count=0 in input state [Data]", error.toString());
    }

    @Test
    public void testEofErrorTokeniserState() {
        tokeniser.eofError(TokeniserState.TagName);

        ParseErrorList errorList = tokeniser.errors;
        ParseError error = errorList.get(0);

        assertEquals(1, errorList.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [TagName]", error.toString());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Invalid character reference");

        ParseErrorList errorList = tokeniser.errors;
        ParseError error = errorList.get(0);

        assertEquals(1, errorList.size());
        assertEquals("Invalid character reference: Invalid character reference", error.toString());
    }

    @Test
    public void testError() {
        tokeniser.error("Test error");

        ParseErrorList errorList = tokeniser.errors;
        ParseError error = errorList.get(0);

        assertEquals(1, errorList.size());
        assertEquals("Test error", error.toString());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();
        assertTrue(result);
    }

    @Test
    public void testUnescapeEntities() {
        tokeniser.reader = new CharacterReader("Test &amp;");

        String result = tokeniser.unescapeEntities(false);

        assertEquals("Test &", result);
    }
}