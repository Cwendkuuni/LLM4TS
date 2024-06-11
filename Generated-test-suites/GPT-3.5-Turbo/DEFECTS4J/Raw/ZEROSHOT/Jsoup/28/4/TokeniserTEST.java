package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TokeniserTest {

    @Mock
    private CharacterReader mockCharacterReader;

    @Mock
    private ParseErrorList mockParseErrorList;

    @InjectMocks
    private Tokeniser tokeniser;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRead() {
        // TODO: Implement test cases for read() method in Tokeniser class
    }

    @Test
    public void testEmit() {
        // TODO: Implement test cases for emit(Token) method in Tokeniser class
    }

    @Test
    public void testEmitString() {
        // TODO: Implement test cases for emit(String) method in Tokeniser class
    }

    @Test
    public void testEmitChar() {
        // TODO: Implement test cases for emit(char) method in Tokeniser class
    }

    @Test
    public void testGetState() {
        // TODO: Implement test cases for getState() method in Tokeniser class
    }

    @Test
    public void testTransition() {
        // TODO: Implement test cases for transition(TokeniserState) method in Tokeniser class
    }

    @Test
    public void testAdvanceTransition() {
        // TODO: Implement test cases for advanceTransition(TokeniserState) method in Tokeniser class
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // TODO: Implement test cases for acknowledgeSelfClosingFlag() method in Tokeniser class
    }

    @Test
    public void testConsumeCharacterReference() {
        // TODO: Implement test cases for consumeCharacterReference(Character, boolean) method in Tokeniser class
    }

    @Test
    public void testCreateTagPending() {
        // TODO: Implement test cases for createTagPending(boolean) method in Tokeniser class
    }

    @Test
    public void testEmitTagPending() {
        // TODO: Implement test cases for emitTagPending() method in Tokeniser class
    }

    @Test
    public void testCreateCommentPending() {
        // TODO: Implement test cases for createCommentPending() method in Tokeniser class
    }

    @Test
    public void testEmitCommentPending() {
        // TODO: Implement test cases for emitCommentPending() method in Tokeniser class
    }

    @Test
    public void testCreateDoctypePending() {
        // TODO: Implement test cases for createDoctypePending() method in Tokeniser class
    }

    @Test
    public void testEmitDoctypePending() {
        // TODO: Implement test cases for emitDoctypePending() method in Tokeniser class
    }

    @Test
    public void testCreateTempBuffer() {
        // TODO: Implement test cases for createTempBuffer() method in Tokeniser class
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // TODO: Implement test cases for isAppropriateEndTagToken() method in Tokeniser class
    }

    @Test
    public void testAppropriateEndTagName() {
        // TODO: Implement test cases for appropriateEndTagName() method in Tokeniser class
    }

    @Test
    public void testErrorTokeniserState() {
        // TODO: Implement test cases for error(TokeniserState) method in Tokeniser class
    }

    @Test
    public void testEofError() {
        // TODO: Implement test cases for eofError(TokeniserState) method in Tokeniser class
    }

    @Test
    public void testCharacterReferenceError() {
        // TODO: Implement test cases for characterReferenceError(String) method in Tokeniser class
    }

    @Test
    public void testError() {
        // TODO: Implement test cases for error(String) method in Tokeniser class
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // TODO: Implement test cases for currentNodeInHtmlNS() method in Tokeniser class
    }

    @Test
    public void testConsumeReaderAndUnescapeEntities() {
        // TODO: Implement test cases for consumeReaderAndUnescapeEntities(boolean) method in Tokeniser class
    }
}