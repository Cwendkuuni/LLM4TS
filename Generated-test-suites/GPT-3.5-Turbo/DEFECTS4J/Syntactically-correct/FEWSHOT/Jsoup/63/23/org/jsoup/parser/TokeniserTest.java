package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setup() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        tagPending.name("tag");
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.StartTag);
        assertEquals("tag", ((Token.StartTag) token).tagName());
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Token.Comment commentPending = tokeniser.commentPending;
        assertFalse(commentPending.isBlank());
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.data("comment");
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Comment);
        assertEquals("comment", ((Token.Comment) token).getData());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Token.Doctype doctypePending = tokeniser.doctypePending;
        assertEquals("", doctypePending.getName());
        assertEquals("", doctypePending.getPublicIdentifier());
        assertEquals("", doctypePending.getSystemIdentifier());
        assertFalse(doctypePending.forceQuirks());
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.forceQuirks(true);
        tokeniser.doctypePending.name("doctype");
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Doctype);
        assertEquals("doctype", ((Token.Doctype) token).getName());
        assertTrue(((Token.Doctype) token).forceQuirks());
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        StringBuilder dataBuffer = tokeniser.dataBuffer;
        assertEquals("", dataBuffer.toString());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = "tag";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagTokenFalse() {
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = "tag";
        assertEquals("tag", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testAppropriateEndTagNameNull() {
        assertNull(tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        ParseErrorList errors = tokeniser.errors;
        errors.add(new ParseError(0, "Error"));
        assertEquals(1, errors.size());
        assertEquals("Error", errors.get(0).getErrorMessage());
    }

    @Test
    public void testEofError() {
        ParseErrorList errors = tokeniser.errors;
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        ParseErrorList errors = tokeniser.errors;
        tokeniser.characterReferenceError("Invalid character reference");
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference: Invalid character reference", errors.get(0).getErrorMessage());
    }

    @Test
    public void testErrorWithState() {
        ParseErrorList errors = tokeniser.errors;
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, errors.size());
        assertEquals("Unexpected character '' in input state [Data]", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

    @Test
    public void testUnescapeEntities() {
        String unescapedString = tokeniser.unescapeEntities(false);
        assertEquals("", unescapedString);
    }
}