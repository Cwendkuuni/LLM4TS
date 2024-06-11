package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class TokeniserTest {

    @Mock
    private CharacterReader reader;

    @Mock
    private ParseErrorList errors;

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        // TODO: Implement test for read() method
    }

    @Test
    public void testEmit() {
        // TODO: Implement test for emit(Token) method
    }

    @Test
    public void testEmit_String() {
        // TODO: Implement test for emit(String) method
    }

    @Test
    public void testEmit_char() {
        // TODO: Implement test for emit(char) method
    }

    @Test
    public void testGetState() {
        // TODO: Implement test for getState() method
    }

    @Test
    public void testTransition() {
        // TODO: Implement test for transition(TokeniserState) method
    }

    @Test
    public void testAdvanceTransition() {
        // TODO: Implement test for advanceTransition(TokeniserState) method
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // TODO: Implement test for acknowledgeSelfClosingFlag() method
    }

    @Test
    public void testConsumeCharacterReference() {
        // TODO: Implement test for consumeCharacterReference(Character, boolean) method
    }

    @Test
    public void testCreateTagPending() {
        // TODO: Implement test for createTagPending(boolean) method
    }

    @Test
    public void testEmitTagPending() {
        // TODO: Implement test for emitTagPending() method
    }

    @Test
    public void testCreateCommentPending() {
        // TODO: Implement test for createCommentPending() method
    }

    @Test
    public void testEmitCommentPending() {
        // TODO: Implement test for emitCommentPending() method
    }

    @Test
    public void testCreateDoctypePending() {
        // TODO: Implement test for createDoctypePending() method
    }

    @Test
    public void testEmitDoctypePending() {
        // TODO: Implement test for emitDoctypePending() method
    }

    @Test
    public void testCreateTempBuffer() {
        // TODO: Implement test for createTempBuffer() method
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // TODO: Implement test for isAppropriateEndTagToken() method
    }

    @Test
    public void testAppropriateEndTagName() {
        // TODO: Implement test for appropriateEndTagName() method
    }

    @Test
    public void testError_State() {
        // TODO: Implement test for error(TokeniserState) method
    }

    @Test
    public void testEofError() {
        // TODO: Implement test for eofError(TokeniserState) method
    }

    @Test
    public void testCharacterReferenceError() {
        // TODO: Implement test for characterReferenceError(String) method
    }

    @Test
    public void testError_String() {
        // TODO: Implement test for error(String) method
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // TODO: Implement test for currentNodeInHtmlNS() method
    }
}