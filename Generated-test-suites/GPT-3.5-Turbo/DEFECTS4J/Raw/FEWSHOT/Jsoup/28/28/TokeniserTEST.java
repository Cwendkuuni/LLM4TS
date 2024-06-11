
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList errors;
    private CharacterReader reader;

    @Before
    public void setUp() {
        errors = new ParseErrorList();
        reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        assertTrue(tokeniser.read() == null);
    }

    @Test
    public void testEmit() {
        Token tag = new Token.Tag("div");
        tokeniser.emit(tag);
        assertTrue(tokeniser.read() == tag);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals(str, ((Token.Character) token).getData());
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals(Character.toString(c), ((Token.Character) token).getData());
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        reader = new CharacterReader("test");
        tokeniser = new Tokeniser(reader, errors);
        TokeniserState state = TokeniserState.TagName;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
        assertEquals('e', reader.current());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = true;
        assertNull(tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute));
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertTrue(token instanceof Token.StartTag);
        assertEquals("div", ((Token.StartTag) token).tagName);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.data = "test";
        tokeniser.emitCommentPending();

        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Comment);
        assertEquals("test", ((Token.Comment) token).getData());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name = "html";
        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Doctype);
        assertEquals("html", ((Token.Doctype) token).getName());
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "div";

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "div";

        assertEquals("div", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, errors.size());
        assertEquals("Unexpected character 'null' in input state [Data]", errors.get(0).getMessage());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("invalid reference");
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference: invalid reference", errors.get(0).getMessage());
    }

    @Test
    public void testErrorWithMessage() {
        tokeniser.error("error");
        assertEquals(1, errors.size());
        assertEquals("error", errors.get(0).getMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}
