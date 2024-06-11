
package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {
    
    private Tokeniser tokeniser;
    
    @Before
    public void setup() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }

    @Test
    public void testRead() {
        tokeniser.emit("abc");
        Token result = tokeniser.read();
        assertEquals(Token.TokenType.Character, result.type);
        assertEquals("abc", ((Token.Character) result).getData());
    }

    @Test
    public void testEmitToken() {
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.emit(startTag);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(startTag, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        String str = "abc";
        tokeniser.emit(str);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(c, tokeniser.charBuffer.charAt(0));
    }

    @Test
    public void testGetState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }
    
    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.TagName);
        assertEquals(TokeniserState.TagName, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.BeforeAttributeName);
        assertEquals(TokeniserState.BeforeAttributeName, tokeniser.getState());
        assertTrue(tokeniser.reader.isEmpty());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.selfClosingFlagAcknowledged = false;
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        tokeniser.reader = new CharacterReader("&amp;");
        Character result = tokeniser.consumeCharacterReference(null, false);
        assertEquals('&', result.charValue());
    }
    
    @Test
    public void testCreateTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        assertTrue(tag instanceof Token.StartTag);
        assertNotNull(tokeniser.tagPending);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertTrue(tokeniser.isEmitPending);
        assertTrue(tokeniser.emitPending instanceof Token.StartTag);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertTrue(tokeniser.isEmitPending);
        assertTrue(tokeniser.emitPending instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertTrue(tokeniser.isEmitPending);
        assertTrue(tokeniser.emitPending instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.tagPending = new Token.EndTag();
        assertFalse(tokeniser.isAppropriateEndTagToken());
        
        tokeniser.tagPending.tagName = "tag";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "tag";
        assertEquals("tag", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError1() {
        tokeniser.error(TokeniserState.DOCTYPE_PUBLIC_IDENTIFIER_MISMATCH);  
    }

    @Test
    public void testError2() {
        tokeniser.error("error");  
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.BeforeAttributeName);
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("error");
    }

    @Test
    public void testErrorCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

}

