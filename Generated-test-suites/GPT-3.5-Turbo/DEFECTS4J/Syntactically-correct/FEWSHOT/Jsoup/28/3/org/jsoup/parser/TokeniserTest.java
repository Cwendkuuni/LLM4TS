package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);

        tokeniser.emit("test");
        token = tokeniser.read();
        assertEquals("Character", token.getClass().getSimpleName());

        tokeniser.emit(new Token.StartTag());
        token = tokeniser.read();
        assertEquals("StartTag", token.getClass().getSimpleName());
    }

    @Test
    public void testEmit() {
        tokeniser.emit(new Token.StartTag());
        assertEquals("StartTag", tokeniser.emitPending.getClass().getSimpleName());

        tokeniser.emit(new Token.Character("test"));
        assertEquals("Character", tokeniser.emitPending.getClass().getSimpleName());
    }

    @Test
    public void testEmitString() {
        tokeniser.emit("test");
        assertEquals("test", tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        tokeniser.emit('t');
        assertEquals("t", tokeniser.charBuffer.toString());
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.BeforeAttributeName;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.AttributeValue_doubleQuoted;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
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
        
        tokeniser.reader = new CharacterReader("&#x43;");
        char c = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertEquals('C', c);
        
        tokeniser.reader = new CharacterReader("&amp;");
        c = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertEquals('&', c);
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        assertEquals("StartTag", tokeniser.tagPending.getClass().getSimpleName());

        tokeniser.createTagPending(false);
        assertEquals("EndTag", tokeniser.tagPending.getClass().getSimpleName());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertEquals("StartTag", tokeniser.emitPending.getClass().getSimpleName());
        
        tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        assertEquals("EndTag", tokeniser.emitPending.getClass().getSimpleName());
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertEquals("Comment", tokeniser.commentPending.getClass().getSimpleName());
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.commentPending = new Token.Comment("test");
        tokeniser.emitCommentPending();
        assertEquals("Comment", tokeniser.emitPending.getClass().getSimpleName());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertEquals("Doctype", tokeniser.doctypePending.getClass().getSimpleName());
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.doctypePending = new Token.Doctype("test");
        tokeniser.emitDoctypePending();
        assertEquals("Doctype", tokeniser.emitPending.getClass().getSimpleName());
    }

    @Test
    public void testCreateTempBuffer() {
        assertNull(tokeniser.dataBuffer);
        tokeniser.createTempBuffer();
        assertEquals("", tokeniser.dataBuffer.toString());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        assertFalse(tokeniser.isAppropriateEndTagToken());

        tokeniser.lastStartTag = new Token.StartTag("test");
        tokeniser.tagPending = new Token.EndTag("test");
        assertTrue(tokeniser.isAppropriateEndTagToken());
        
        tokeniser.tagPending = new Token.EndTag();
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        assertNull(tokeniser.appropriateEndTagName());

        tokeniser.lastStartTag = new Token.StartTag("test");
        assertEquals("test", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());

        tokeniser.error("Error");
        assertEquals(2, tokeniser.errors.size());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Error");
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}