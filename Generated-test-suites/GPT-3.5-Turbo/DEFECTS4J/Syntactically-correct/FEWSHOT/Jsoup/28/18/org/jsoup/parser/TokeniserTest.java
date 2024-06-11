
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.CharacterReader;
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {
    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        errors = new ParseErrorList(0);
        tokeniser = new Tokeniser(new CharacterReader(""), errors);
    }
    
    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);

        token = tokeniser.read();
        assertNull(token);

        Token.Character characterToken = new Token.Character("test");
        tokeniser.emit(characterToken);
        token = tokeniser.read();
        assertEquals(characterToken, token);
    }

    @Test
    public void testEmit() {
        assertNull(tokeniser.emitPending);

        Token.Character characterToken = new Token.Character("test");
        tokeniser.emit(characterToken);
        assertEquals(characterToken, tokeniser.emitPending);
    }

    @Test
    public void testEmit_String() {
        assertNull(tokeniser.charBuffer.toString());

        String str = "test";
        tokeniser.emit(str);
        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        assertNull(tokeniser.charBuffer.toString());

        char c = 't';
        tokeniser.emit(c);
        assertEquals(String.valueOf(c), tokeniser.charBuffer.toString());
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.RCData;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.RCData;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertEquals(true, tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tagPending = tokeniser.createTagPending(start);
        assertEquals(start, tagPending.isStartTag());
    }

    @Test
    public void testEmitTagPending() {
        assertNull(tokeniser.tagPending);

        boolean start = true;
        tokeniser.createTagPending(start);
        tokeniser.emitTagPending();

        assertEquals(tokeniser.tagPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() {
        assertNull(tokeniser.commentPending);

        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        assertNull(tokeniser.commentPending);

        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertEquals(tokeniser.commentPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        assertNull(tokeniser.doctypePending);

        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        assertNull(tokeniser.doctypePending);

        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertEquals(tokeniser.doctypePending, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        assertNull(tokeniser.dataBuffer);

        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        assertNull(tokeniser.lastStartTag);

        boolean isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
        assertFalse(isAppropriateEndTagToken);

        String tagName = "test";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = tagName;
        isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
        assertFalse(isAppropriateEndTagToken);

        tokeniser.tagPending = new Token.StartTag();
        tokeniser.tagPending.tagName = tagName;
        isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
        assertTrue(isAppropriateEndTagToken);
    }

    @Test
    public void testAppropriateEndTagName() {
        assertNull(tokeniser.lastStartTag);

        String tagName = "test";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = tagName;
        String appropriateEndTagName = tokeniser.appropriateEndTagName();
        assertEquals(tagName, appropriateEndTagName);
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.RCData);
        assertEquals(1, errors.size());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.RCData);
        assertEquals(1, errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("error");
        assertEquals(1, errors.size());
    }

    @Test
    public void testError_String() {
        tokeniser.error("error");
        assertEquals(1, errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean isCurrentNodeInHtmlNS = tokeniser.currentNodeInHtmlNS();
        assertTrue(isCurrentNodeInHtmlNS);
    }
}
