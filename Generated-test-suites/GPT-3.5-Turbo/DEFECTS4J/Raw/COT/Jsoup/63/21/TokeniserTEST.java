package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    
    private Tokeniser tokeniser;
    
    @Before
    public void setup() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }
    
    @Test
    public void testRead() {
        // Test case for read() method
        Token token = tokeniser.read();
        assertNull(token);
    }
    
    @Test
    public void testEmit() {
        // Test case for emit(Token token) method
        Token token = tokeniser.read();
        tokeniser.emit(token);
        
        assertEquals(token, tokeniser.read());
    }
    
    @Test
    public void testEmitString() {
        // Test case for emit(String str) method
        String str = "Hello, World!";
        tokeniser.emit(str);
        
        assertEquals(str, tokeniser.read().toString());
    }
    
    @Test
    public void testEmitCharArray() {
        // Test case for emit(char[] chars) method
        char[] chars = {'H', 'e', 'l', 'l', 'o'};
        tokeniser.emit(chars);
        
        assertEquals("Hello", tokeniser.read().toString());
    }
    
    @Test
    public void testEmitIntArr() {
        // Test case for emit(int[] codepoints) method
        int[] codepoints = {72, 101, 108, 108, 111};
        tokeniser.emit(codepoints);
        
        assertEquals("Hello", tokeniser.read().toString());
    }
    
    @Test
    public void testEmitChar() {
        // Test case for emit(char c) method
        char c = 'H';
        tokeniser.emit(c);
        
        assertEquals("H", tokeniser.read().toString());
    }
    
    @Test
    public void testGetState() {
        // Test case for getState() method
        TokeniserState state = tokeniser.getState();
        
        assertEquals(TokeniserState.Data, state);
    }
    
    @Test
    public void testTransition() {
        // Test case for transition(TokeniserState state) method
        TokeniserState state = TokeniserState.AttributeValue_doubleQuoted;
        tokeniser.transition(state);
        
        assertEquals(state, tokeniser.getState());
    }
    
    @Test
    public void testAdvanceTransition() {
        // Test case for advanceTransition(TokeniserState state) method
        TokeniserState state = TokeniserState.AttributeValue_doubleQuoted;
        tokeniser.advanceTransition(state);
        
        assertEquals(state, tokeniser.getState());
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Test case for acknowledgeSelfClosingFlag() method
        tokeniser.acknowledgeSelfClosingFlag();
        
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
    
    @Test
    public void testConsumeCharacterReference() {
        // Test case for consumeCharacterReference(Character additionalAllowedCharacter, boolean inAttribute) method
        int[] codeRef = tokeniser.consumeCharacterReference(null, true);
        
        assertNull(codeRef);
    }
    
    @Test
    public void testCreateTagPending() {
        // Test case for createTagPending(boolean start) method
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        
        assertNotNull(tag);
    }
    
    @Test
    public void testEmitTagPending() {
        // Test case for emitTagPending() method
        tokeniser.emitTagPending();
        
        assertNotNull(tokeniser.read());
    }
    
    @Test
    public void testCreateCommentPending() {
        // Test case for createCommentPending() method
        tokeniser.createCommentPending();
        
        assertNotNull(tokeniser.commentPending);
    }
    
    @Test
    public void testEmitCommentPending() {
        // Test case for emitCommentPending() method
        tokeniser.emitCommentPending();
        
        assertNotNull(tokeniser.read());
    }
    
    @Test
    public void testCreateDoctypePending() {
        // Test case for createDoctypePending() method
        tokeniser.createDoctypePending();
        
        assertNotNull(tokeniser.doctypePending);
    }
    
    @Test
    public void testEmitDoctypePending() {
        // Test case for emitDoctypePending() method
        tokeniser.emitDoctypePending();
        
        assertNotNull(tokeniser.read());
    }
    
    @Test
    public void testCreateTempBuffer() {
        // Test case for createTempBuffer() method
        tokeniser.createTempBuffer();
        
        assertNotNull(tokeniser.dataBuffer);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        // Test case for isAppropriateEndTagToken() method
        boolean result = tokeniser.isAppropriateEndTagToken();
        
        assertFalse(result);
    }
    
    @Test
    public void testAppropriateEndTagName() {
        // Test case for appropriateEndTagName() method
        String tagName = tokeniser.appropriateEndTagName();
        
        assertNull(tagName);
    }
    
    @Test
    public void testError() {
        // Test case for error(TokeniserState state) method
        TokeniserState state = TokeniserState.Attributes;
        tokeniser.error(state);
        
        assertFalse(tokeniser.errors.isEmpty());
    }
    
    @Test
    public void testEofError() {
        // Test case for eofError(TokeniserState state) method
        TokeniserState state = TokeniserState.Attributes;
        tokeniser.eofError(state);
        
        assertFalse(tokeniser.errors.isEmpty());
    }
    
    @Test
    public void testCharacterReferenceError() {
        // Test case for characterReferenceError(String message) method
        String errorMsg = "Invalid character reference";
        tokeniser.characterReferenceError(errorMsg);
        
        assertFalse(tokeniser.errors.isEmpty());
    }
    
    @Test
    public void testErrorString() {
        // Test case for error(String errorMsg) method
        String errorMsg = "Error occurred";
        tokeniser.error(errorMsg);
        
        assertFalse(tokeniser.errors.isEmpty());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        // Test case for currentNodeInHtmlNS() method
        boolean result = tokeniser.currentNodeInHtmlNS();
        
        assertTrue(result);
    }
    
    @Test
    public void testUnescapeEntities() {
        // Test case for unescapeEntities(boolean inAttribute) method
        boolean inAttribute = true;
        String unescapedStr = tokeniser.unescapeEntities(inAttribute);
        
        assertNotNull(unescapedStr);
    }
}