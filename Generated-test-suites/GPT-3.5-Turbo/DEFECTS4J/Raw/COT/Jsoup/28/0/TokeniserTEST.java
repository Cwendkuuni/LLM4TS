import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        reader = mock(CharacterReader.class);
        errors = mock(ParseErrorList.class);
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        // Test case for read() method
        // TODO: Add steps to test functionality of read() method
    }

    @Test
    public void testEmit() {
        // Test case for emit(Token token) method
        // TODO: Add steps to test functionality of emit(Token token) method
    }

    @Test
    public void testEmitString() {
        // Test case for emit(String str) method
        // TODO: Add steps to test functionality of emit(String str) method
    }

    @Test
    public void testEmitChar() {
        // Test case for emit(char c) method
        // TODO: Add steps to test functionality of emit(char c) method
    }

    @Test
    public void testGetState() {
        // Test case for getState() method
        // TODO: Add steps to test functionality of getState() method
    }

    @Test
    public void testTransition() {
        // Test case for transition(TokeniserState state) method
        // TODO: Add steps to test functionality of transition(TokeniserState state) method
    }

    @Test
    public void testAdvanceTransition() {
        // Test case for advanceTransition(TokeniserState state) method
        // TODO: Add steps to test functionality of advanceTransition(TokeniserState state) method
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Test case for acknowledgeSelfClosingFlag() method
        // TODO: Add steps to test functionality of acknowledgeSelfClosingFlag() method
    }

    @Test
    public void testConsumeCharacterReference() {
        // Test case for consumeCharacterReference(Character additionalAllowedCharacter, boolean inAttribute) method
        // TODO: Add steps to test functionality of consumeCharacterReference(Character additionalAllowedCharacter, boolean inAttribute) method
    }

    @Test
    public void testCreateTagPending() {
        // Test case for createTagPending(boolean start) method
        // TODO: Add steps to test functionality of createTagPending(boolean start) method
    }

    @Test
    public void testEmitTagPending() {
        // Test case for emitTagPending() method
        // TODO: Add steps to test functionality of emitTagPending() method
    }

    @Test
    public void testCreateCommentPending() {
        // Test case for createCommentPending() method
        // TODO: Add steps to test functionality of createCommentPending() method
    }

    @Test
    public void testEmitCommentPending() {
        // Test case for emitCommentPending() method
        // TODO: Add steps to test functionality of emitCommentPending() method
    }

    @Test
    public void testCreateDoctypePending() {
        // Test case for createDoctypePending() method
        // TODO: Add steps to test functionality of createDoctypePending() method
    }

    @Test
    public void testEmitDoctypePending() {
        // Test case for emitDoctypePending() method
        // TODO: Add steps to test functionality of emitDoctypePending() method
    }

    @Test
    public void testCreateTempBuffer() {
        // Test case for createTempBuffer() method
        // TODO: Add steps to test functionality of createTempBuffer() method
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Test case for isAppropriateEndTagToken() method
        // TODO: Add steps to test functionality of isAppropriateEndTagToken() method
    }

    @Test
    public void testAppropriateEndTagName() {
        // Test case for appropriateEndTagName() method
        // TODO: Add steps to test functionality of appropriateEndTagName() method
    }

    @Test
    public void testError() {
        // Test case for error(TokeniserState state) method
        // TODO: Add steps to test functionality of error(TokeniserState state) method
    }

    @Test
    public void testEofError() {
        // Test case for eofError(TokeniserState state) method
        // TODO: Add steps to test functionality of eofError(TokeniserState state) method
    }

    @Test
    public void testCharacterReferenceError() {
        // Test case for characterReferenceError(String message) method
        // TODO: Add steps to test functionality of characterReferenceError(String message) method
    }

    @Test
    public void testErrorString() {
        // Test case for error(String errorMsg) method
        // TODO: Add steps to test functionality of error(String errorMsg) method
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // Test case for currentNodeInHtmlNS() method
        // TODO: Add steps to test functionality of currentNodeInHtmlNS() method
    }

    @Test
    public void testUtilityMethod() {
        // Test case for utility method consume reader and unescape entities found within
        // TODO: Add steps to test functionality of utility method
    }
}