package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(), new ParseErrorList());
    }

    @Test
    public void testRead() {
        // Test read method
        Token token = tokeniser.read();
        assertNotNull(token);  
    }

    @Test
    public void testEmit() {
        // Test emit method
        Token token = tokeniser.read();
        assertNull(token);
        tokeniser.emit(new Token.StartTag());
        token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testEmitString() {
        // Test emitString method
        Token token = tokeniser.read();
        assertNull(token);
        tokeniser.emit("test");
        token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testEmitCharArray() {
        // Test emitCharArray method
        Token token = tokeniser.read();
        assertNull(token);
        tokeniser.emit(new char[]{'t', 'e', 's', 't'});
        token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testEmitInt() {
        // Test emitInt method
        Token token = tokeniser.read();
        assertNull(token);
        tokeniser.emit(65);
        token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testGetState() {
        // Test getState method
        TokeniserState state = tokeniser.getState();
        assertNotNull(state);
    }

    @Test
    public void testTransition() {
        // Test transition method
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
        tokeniser.transition(TokeniserState.TagName);
        state = tokeniser.getState();
        assertEquals(TokeniserState.TagName, state);
    }

    @Test
    public void testAdvanceTransition() {
        // Test advanceTransition method
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
        tokeniser.advanceTransition(TokeniserState.TagName);
        state = tokeniser.getState();
        assertEquals(TokeniserState.TagName, state);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Test acknowledgeSelfClosingFlag method
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testConsumeCharacterReference() {
        // Test consumeCharacterReference method
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        assertNull(codepoints);
    }

    @Test
    public void testCreateTagPending() {
        // Test createTagPending method
        Token.Tag tag = tokeniser.createTagPending(true);
        assertNotNull(tag);
    }

    @Test
    public void testEmitTagPending() {
        // Test emitTagPending method
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testCreateCommentPending() {
        // Test createCommentPending method
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        // Test emitCommentPending method
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertEquals(Token.TokenType.Comment, tokeniser.commentPending.type);
    }

    @Test
    public void testCreateDoctypePending() {
        // Test createDoctypePending method
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        // Test emitDoctypePending method
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertEquals(Token.TokenType.Doctype, tokeniser.doctypePending.type);
    }

    @Test
    public void testCreateTempBuffer() {
        // Test createTempBuffer method
        tokeniser.createTempBuffer();
        assertEquals(0, tokeniser.dataBuffer.length());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Test isAppropriateEndTagToken method
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        // Test appropriateEndTagName method
        assertNull(tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        // Test error method
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testEofError() {
        // Test eofError method
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        // Test characterReferenceError method
        tokeniser.characterReferenceError("Invalid character reference");
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testUnescapeEntities() {
        // Test unescapeEntities method
        String unescapedString = tokeniser.unescapeEntities(false);
        assertNotNull(unescapedString);
        assertEquals("", unescapedString);
    }

}