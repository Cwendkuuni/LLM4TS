package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokeniserTest {
    @Test
    public void testRead() {
        // Test case 1
        CharacterReader reader1 = new CharacterReader("test");
        Tokeniser tokeniser1 = new Tokeniser(reader1);
        Token expectedToken1 = new Token.Character("test");
        assertEquals(expectedToken1, tokeniser1.read());

        // Test case 2
        CharacterReader reader2 = new CharacterReader("");
        Tokeniser tokeniser2 = new Tokeniser(reader2);
        assertNull(tokeniser2.read());

        // Test case 3
        CharacterReader reader3 = new CharacterReader("<html>");
        Tokeniser tokeniser3 = new Tokeniser(reader3);
        Token expectedToken3 = new Token.StartTag("html");
        assertEquals(expectedToken3, tokeniser3.read());

        // Test case 4
        CharacterReader reader4 = new CharacterReader("</html>");
        Tokeniser tokeniser4 = new Tokeniser(reader4);
        Token expectedToken4 = new Token.EndTag("html");
        assertEquals(expectedToken4, tokeniser4.read());
    }

    @Test
    public void testEmit() {
        // Test case 1
        CharacterReader reader1 = new CharacterReader("test");
        Tokeniser tokeniser1 = new Tokeniser(reader1);
        Token expectedToken1 = new Token.Character("test");
        tokeniser1.emit(expectedToken1);
        assertEquals(expectedToken1, tokeniser1.read());

        // Test case 2
        CharacterReader reader2 = new CharacterReader("<html>");
        Tokeniser tokeniser2 = new Tokeniser(reader2);
        Token expectedToken2 = new Token.StartTag("html");
        tokeniser2.emit(expectedToken2);
        assertEquals(expectedToken2, tokeniser2.read());
    }

    @Test
    public void testCreateTagPending() {
        // Test case 1
        CharacterReader reader1 = new CharacterReader("");
        Tokeniser tokeniser1 = new Tokeniser(reader1);
        Token.Tag expectedTagPending1 = new Token.StartTag();
        assertEquals(expectedTagPending1, tokeniser1.createTagPending(true));

        // Test case 2
        CharacterReader reader2 = new CharacterReader("");
        Tokeniser tokeniser2 = new Tokeniser(reader2);
        Token.Tag expectedTagPending2 = new Token.EndTag();
        assertEquals(expectedTagPending2, tokeniser2.createTagPending(false));
    }

    @Test
    public void testEmitTagPending() {
        CharacterReader reader = new CharacterReader("<html>");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token.Tag tagPending = tokeniser.createTagPending(true);
        tagPending.tagName = "html";
        tokeniser.emitTagPending();
        Token.StartTag expectedStartTag = new Token.StartTag("html");
        assertEquals(expectedStartTag, tokeniser.read());
    }

    @Test
    public void testCreateCommentPending() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.createCommentPending();
        Token.Comment expectedCommentPending = new Token.Comment();
        assertEquals(expectedCommentPending, tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.createCommentPending();
        tokeniser.commentPending.data = "Test comment";
        tokeniser.emitCommentPending();
        Token.Comment expectedComment = new Token.Comment("Test comment");
        assertEquals(expectedComment, tokeniser.read());
    }

    @Test
    public void testCreateDoctypePending() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.createDoctypePending();
        Token.Doctype expectedDoctypePending = new Token.Doctype();
        assertEquals(expectedDoctypePending, tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name = "html";
        tokeniser.emitDoctypePending();
        Token.Doctype expectedDoctype = new Token.Doctype("html");
        assertEquals(expectedDoctype, tokeniser.read());
    }

    @Test
    public void testCreateTempBuffer() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.createTempBuffer();
        StringBuilder expectedDataBuffer = new StringBuilder();
        assertEquals(expectedDataBuffer, tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "html";
        tokeniser.lastStartTag = new Token.StartTag("html");
        assertEquals(true, tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        assertEquals(true, tokeniser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.setTrackErrors(false);
        assertEquals(false, tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.error(TokeniserState.Data);
        ParseError expectedError = new ParseError("Unexpected character in input", reader.current(), TokeniserState.Data, reader.pos());
        assertEquals(expectedError, tokeniser.errors.get(0));
    }

    @Test
    public void testEofError() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.eofError(TokeniserState.Data);
        ParseError expectedError = new ParseError("Unexpectedly reached end of file (EOF)", TokeniserState.Data, reader.pos());
        assertEquals(expectedError, tokeniser.errors.get(0));
    }

    @Test
    public void testCharacterReferenceError() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.characterReferenceError();
        ParseError expectedError = new ParseError("Invalid character reference", reader.pos());
        assertEquals(expectedError, tokeniser.errors.get(0));
    }

    @Test
    public void testErrorWithMessage() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.error("Test error");
        ParseError expectedError = new ParseError("Test error", reader.pos());
        assertEquals(expectedError, tokeniser.errors.get(0));
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        assertEquals(true, tokeniser.currentNodeInHtmlNS());
    }
}