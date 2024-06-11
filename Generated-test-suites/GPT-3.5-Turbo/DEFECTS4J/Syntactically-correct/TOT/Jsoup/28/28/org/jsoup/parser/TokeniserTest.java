package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead_returnsToken() {
        // Initialize
        CharacterReader reader = new CharacterReader("test string");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // Test
        Token token = tokeniser.read();

        // Verify
        assertNotNull(token);
    }

    @Test
    public void testRead_returnsCharacterToken() {
        // Initialize
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // Test
        Token token = tokeniser.read();

        // Verify
        assertNotNull(token);
        assertTrue(token instanceof Token.Character);
        assertEquals("test", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit_setIsEmitPendingTrue() {
        // Initialize
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = new Token.Character("test");

        // Test
        tokeniser.emit(token);

        // Verify
        assertTrue(tokeniser.isEmitPending);
        assertEquals(token, tokeniser.emitPending);
    }

    @Test
    public void testEmit_setsIsEmitPendingTrueForStartTagToken() {
        // Initialize
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token.StartTag startTag = new Token.StartTag();

        // Test
        tokeniser.emit(startTag);

        // Verify
        assertTrue(tokeniser.isEmitPending);
        assertEquals(startTag, tokeniser.emitPending);
    }

    @Test
    public void testEmit_setsIsEmitPendingTrueForEndTagToken() {
        // Initialize
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token.EndTag endTag = new Token.EndTag();

        // Test
        tokeniser.emit(endTag);

        // Verify
        assertTrue(tokeniser.isEmitPending);
        assertEquals(endTag, tokeniser.emitPending);
    }

    @Test
    public void testEmit_setsIsEmitPendingTrueForCharacterToken() {
        // Initialize
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token.Character character = new Token.Character("test");

        // Test
        tokeniser.emit(character);

        // Verify
        assertTrue(tokeniser.isEmitPending);
        assertEquals(character, tokeniser.emitPending);
    }

    @Test
    public void testEmitTagPending_setsTagPendingStateToFinalised() {
        // Initialize
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Tokeniser.Token.Tag tagPending = tokeniser.createTagPending(true);

        // Test
        tokeniser.emitTagPending();

        // Verify
        assertTrue(tagPending.isTagFinalised());
    }

    @Test
    public void testCreateCommentPending_setsCommentPendingStateToNewComment() {
        // Initialize
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // Test
        tokeniser.createCommentPending();

        // Verify
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending_returnsCommentPending() {
        // Initialize
        CharacterReader reader = new CharacterReader("comment");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.createCommentPending();

        // Test
        tokeniser.emitCommentPending();

        // Verify
        assertNotNull(tokeniser.commentPending);
        assertEquals("comment", tokeniser.commentPending.getData());
    }

    @Test
    public void testCreateDoctypePending_setsDoctypePendingStateToNewDoctype() {
        // Initialize
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // Test
        tokeniser.createDoctypePending();

        // Verify
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending_returnsDoctypePending() {
        // Initialize
        CharacterReader reader = new CharacterReader("doctype");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.createDoctypePending();

        // Test
        tokeniser.emitDoctypePending();

        // Verify
        assertNotNull(tokeniser.doctypePending);
        assertEquals("doctype", tokeniser.doctypePending.getData());
    }
}