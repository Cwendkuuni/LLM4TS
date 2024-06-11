package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private CharacterReader reader;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        reader = new CharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @After
    public void tearDown() {
        tokeniser = null;
        reader = null;
        errors = null;
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();

        assertNull(token);
        assertEquals(0, errors.size());
    }

    @Test
    public void testEmit() {
        String str = "Test String";
        Token token = new Token.Character();
        tokeniser.emit(str);

        token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(str, token.toString());
        assertEquals(0, errors.size());
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        Token token = new Token.Character();
        tokeniser.emit(c);

        token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(String.valueOf(c), token.toString());
        assertEquals(0, errors.size());
    }

    @Test
    public void testEmitCharArray() {
        char[] chars = {'a', 'b', 'c'};
        Token token = new Token.Character();
        tokeniser.emit(chars);

        token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(String.valueOf(chars), token.toString());
        assertEquals(0, errors.size());
    }

    @Test
    public void testEmitIntArrayOfCodepoints() {
        int[] codepoints = {65, 66, 67};
        Token token = new Token.Character();
        tokeniser.emit(codepoints);

        token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("ABC", token.toString());
        assertEquals(0, errors.size());
    }

    @Test
    public void testConsumeCharacterReference() {
        String str = "Test&Reference;";
        reader = new CharacterReader(str);
        tokeniser = new Tokeniser(reader, errors);

        int[] codepoint = tokeniser.consumeCharacterReference(null, false);

        assertNotNull(codepoint);
        assertEquals(1, codepoint.length);
        assertEquals((int)'&', codepoint[0]);
        assertEquals(0, errors.size());
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);

        assertNotNull(tag);
        assertEquals(start, tag.start);
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tokeniser.tagPending.name = "tag";
        tokeniser.state = TokeniserState.TagName;

        tokeniser.emitTagPending();
        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("tag", ((Token.StartTag)token).tagName);
        assertEquals(0, errors.size());
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();

        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.commentPending.data = "comment";

        tokeniser.emitCommentPending();
        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals("comment", token.toString());
        assertEquals(0, errors.size());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();

        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.doctypePending.name = "doctype";

        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Doctype, token.type);
        assertEquals("doctype", token.toString());
        assertEquals(0, errors.size());
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();

        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = "startTag";
        tokeniser.tagPending = tokeniser.createTagPending(false);
        tokeniser.tagPending.name = "endTag";

        boolean result = tokeniser.isAppropriateEndTagToken();

        assertFalse(result);
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = "startTag";

        String result = tokeniser.appropriateEndTagName();

        assertEquals("startTag", result);
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);

        assertEquals(1, errors.size());
        assertEquals("Unexpected character '' in input state [Data]", errors.get(0).toString());
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);

        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).toString());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference: invalid message";
        tokeniser.characterReferenceError(message);

        assertEquals(1, errors.size());
        assertEquals("Invalid character reference: invalid message", errors.get(0).toString());
    }

    @Test
    public void testErrorWithMsg() {
        String errorMsg = "Error message";
        tokeniser.error(errorMsg);

        assertEquals(1, errors.size());
        assertEquals("Error at 0: Error message", errors.get(0).toString());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();

        assertTrue(result);
    }
}