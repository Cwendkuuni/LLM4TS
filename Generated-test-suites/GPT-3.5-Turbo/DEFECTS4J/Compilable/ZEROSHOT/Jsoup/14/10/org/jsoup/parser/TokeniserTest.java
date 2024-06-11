package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead() {
        // Write your test cases for the read() method here
        
    }

    @Test
    public void testEmit() {
        // Write your test cases for the emit(Token token) method here
        
    }

    @Test
    public void testEmitString() {
        // Write your test cases for the emit(String str) method here
        
    }

    @Test
    public void testEmitChar() {
        // Write your test cases for the emit(char c) method here
        
    }

    @Test
    public void testGetState() {
        // Write your test cases for the getState() method here
        
    }

    @Test
    public void testTransition() {
        // Write your test cases for the transition(TokeniserState state) method here
        
    }

    @Test
    public void testAdvanceTransition() {
        // Write your test cases for the advanceTransition(TokeniserState state) method here
        
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Write your test cases for the acknowledgeSelfClosingFlag() method here
        
    }

    @Test
    public void testConsumeCharacterReference() {
        // Write your test cases for the consumeCharacterReference(Character additionalAllowedCharacter, boolean inAttribute) method here
        
    }

    @Test
    public void testCreateTagPending() {
        // Write your test cases for the createTagPending(boolean start) method here
        
    }

    @Test
    public void testEmitTagPending() {
        // Write your test cases for the emitTagPending() method here
        
    }

    @Test
    public void testCreateCommentPending() {
        // Write your test cases for the createCommentPending() method here
        
    }

    @Test
    public void testEmitCommentPending() {
        // Write your test cases for the emitCommentPending() method here
        
    }

    @Test
    public void testCreateDoctypePending() {
        // Write your test cases for the createDoctypePending() method here
        
    }

    @Test
    public void testEmitDoctypePending() {
        // Write your test cases for the emitDoctypePending() method here
        
    }

    @Test
    public void testCreateTempBuffer() {
        // Write your test cases for the createTempBuffer() method here
        
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Write your test cases for the isAppropriateEndTagToken() method here
        
    }

    @Test
    public void testIsTrackErrors() {
        // Write your test cases for the isTrackErrors() method here
        
    }

    @Test
    public void testSetTrackErrors() {
        // Write your test cases for the setTrackErrors(boolean trackErrors) method here
        
    }

    @Test
    public void testError() {
        // Write your test cases for the error(TokeniserState state) method here
        
    }

    @Test
    public void testEofError() {
        // Write your test cases for the eofError(TokeniserState state) method here
        
    }

    @Test
    public void testCharacterReferenceError() {
        // Write your test cases for the characterReferenceError() method here
        
    }

    @Test
    public void testErrorString() {
        // Write your test cases for the error(String errorMsg) method here
        
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // Write your test cases for the currentNodeInHtmlNS() method here
        
    }
}