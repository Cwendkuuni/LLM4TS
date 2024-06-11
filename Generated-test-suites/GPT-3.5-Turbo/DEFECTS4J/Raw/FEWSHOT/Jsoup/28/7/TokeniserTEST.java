package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Readers the input stream into tokens.
 */
class TokeniserTest {
    @Test
    public void testRead() {
        // Test case for read method goes here
    }
    
    @Test
    public void testEmit() {
        // Test case for emit method goes here
    }
    
    @Test
    public void testEmitString() {
        // Test case for emit(String str) method goes here
    }
    
    @Test
    public void testEmitChar() {
        // Test case for emit(char c) method goes here
    }
    
    @Test
    public void testGetState() {
        // Test case for getState() method goes here
    }
    
    @Test
    public void testTransition() {
        // Test case for transition(TokeniserState state) method goes here
    }
    
    @Test
    public void testAdvanceTransition() {
        // Test case for advanceTransition(TokeniserState state) method goes here
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Test case for acknowledgeSelfClosingFlag() method goes here
    }
    
    @Test
    public void testConsumeCharacterReference() {
        // Test case for consumeCharacterReference(Character additionalAllowedCharacter, boolean inAttribute) method goes here
    }
    
    @Test
    public void testCreateTagPending() {
        // Test case for createTagPending(boolean start) method goes here
    }
    
    @Test
    public void testEmitTagPending() {
        // Test case for emitTagPending() method goes here
    }
    
    @Test
    public void testCreateCommentPending() {
        // Test case for createCommentPending() method goes here
    }
    
    @Test
    public void testEmitCommentPending() {
        // Test case for emitCommentPending() method goes here
    }
    
    @Test
    public void testCreateDoctypePending() {
        // Test case for createDoctypePending() method goes here
    }
    
    @Test
    public void testEmitDoctypePending() {
        // Test case for emitDoctypePending() method goes here
    }
    
    @Test
    public void testCreateTempBuffer() {
        // Test case for createTempBuffer() method goes here
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        // Test case for isAppropriateEndTagToken() method goes here
    }
    
    @Test
    public void testAppropriateEndTagName() {
        // Test case for appropriateEndTagName() method goes here
    }
    
    @Test
    public void testError() {
        // Test case for error(TokeniserState state) method goes here
    }
    
    @Test
    public void testEofError() {
        // Test case for eofError(TokeniserState state) method goes here
    }
    
    @Test
    private void testCharacterReferenceError() {
        // Test case for characterReferenceError(String message) method goes here
    }
    
    @Test
    private void testError() {
        // Test case for error(String errorMsg) method goes here
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        // Test case for currentNodeInHtmlNS() method goes here
    }
}