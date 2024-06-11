package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        tokeniser = new Tokeniser(null, null);
    }
    
    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token();
        tokeniser.emit(token);
        assertTrue(tokeniser.isEmitPending());
        assertEquals(token, tokeniser.getEmitPending());
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        assertEquals(str, tokeniser.getCharBuffer().toString());
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        assertEquals(String.valueOf(c), tokeniser.getCharBuffer().toString());
    }

    @Test
    public void testGetState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.advanceTransition(state);
        assertTrue(tokeniser.isEmitPending());
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.isSelfClosingFlagAcknowledged());
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader(null); // provide valid input for reader here
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = true;
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);

        assertNotNull(result);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull(tag);
        assertEquals(start, tag.isStartTag());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.Tag();
        tokeniser.tagPending.tagName = "p";

        tokeniser.emitTagPending();
        
        assertTrue(tokeniser.isEmitPending());
        assertTrue(tokeniser.getEmitPending() instanceof Token.Tag);
        assertEquals("p", ((Token.Tag)tokeniser.getEmitPending()).tagName);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.commentPending = new Token.Comment();
        tokeniser.commentPending.data = "test";

        tokeniser.emitCommentPending();
        
        assertTrue(tokeniser.isEmitPending());
        assertTrue(tokeniser.getEmitPending() instanceof Token.Comment);
        assertEquals("test", ((Token.Comment)tokeniser.getEmitPending()).data);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.doctypePending = new Token.Doctype();
        tokeniser.doctypePending.name = "html";

        tokeniser.emitDoctypePending();
        
        assertTrue(tokeniser.isEmitPending());
        assertTrue(tokeniser.getEmitPending() instanceof Token.Doctype);
        assertEquals("html", ((Token.Doctype)tokeniser.getEmitPending()).name);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.tagPending = new Token.Tag();
        tokeniser.tagPending.tagName = "p";
        
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "p";

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "p";

        assertEquals("p", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.error(state);
        // validate the error message in ParseErrorList
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.eofError(state);
        // validate the error message in ParseErrorList
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference";
        tokeniser.characterReferenceError(message);
        // validate the error message in ParseErrorList
    }

    @Test
    public void testErrorWithMessage() {
        String errorMsg = "Error message";
        tokeniser.error(errorMsg);
        // validate the error message in ParseErrorList
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}