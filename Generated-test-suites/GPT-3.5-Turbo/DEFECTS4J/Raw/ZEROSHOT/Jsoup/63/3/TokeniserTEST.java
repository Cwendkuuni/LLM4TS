import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        CharacterReader reader = Mockito.mock(CharacterReader.class);
        ParseErrorList errors = Mockito.mock(ParseErrorList.class);
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        // TODO: Implement test cases for the read() method
    }

    @Test
    public void testEmit() {
        // TODO: Implement test cases for the emit() method
    }

    @Test
    public void testEmitString() {
        // TODO: Implement test cases for the emit(String str) method
    }

    @Test
    public void testEmitCharArray() {
        // TODO: Implement test cases for the emit(char[] chars) method
    }

    @Test
    public void testEmitInt() {
        // TODO: Implement test cases for the emit(int[] codepoints) method
    }

    @Test
    public void testEmitChar() {
        // TODO: Implement test cases for the emit(char c) method
    }

    @Test
    public void testGetState() {
        // TODO: Implement test cases for the getState() method
    }

    @Test
    public void testTransition() {
        // TODO: Implement test cases for the transition() method
    }

    @Test
    public void testAdvanceTransition() {
        // TODO: Implement test cases for the advanceTransition() method
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // TODO: Implement test cases for the acknowledgeSelfClosingFlag() method
    }

    @Test
    public void testConsumeCharacterReference() {
        // TODO: Implement test cases for the consumeCharacterReference() method
    }

    @Test
    public void testCreateTagPending() {
        // TODO: Implement test cases for the createTagPending() method
    }

    @Test
    public void testEmitTagPending() {
        // TODO: Implement test cases for the emitTagPending() method
    }

    @Test
    public void testCreateCommentPending() {
        // TODO: Implement test cases for the createCommentPending() method
    }

    @Test
    public void testEmitCommentPending() {
        // TODO: Implement test cases for the emitCommentPending() method
    }

    @Test
    public void testCreateDoctypePending() {
        // TODO: Implement test cases for the createDoctypePending() method
    }

    @Test
    public void testEmitDoctypePending() {
        // TODO: Implement test cases for the emitDoctypePending() method
    }

    @Test
    public void testCreateTempBuffer() {
        // TODO: Implement test cases for the createTempBuffer() method
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // TODO: Implement test cases for the isAppropriateEndTagToken() method
    }

    @Test
    public void testAppropriateEndTagName() {
        // TODO: Implement test cases for the appropriateEndTagName() method
    }

    @Test
    public void testErrorTokeniserState() {
        // TODO: Implement test cases for the error(TokeniserState state) method
    }

    @Test
    public void testEofErrorTokeniserState() {
        // TODO: Implement test cases for the eofError(TokeniserState state) method
    }

    @Test
    public void testCharacterReferenceError() {
        // TODO: Implement test cases for the characterReferenceError() method
    }

    @Test
    public void testError() {
        // TODO: Implement test cases for the error() method
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // TODO: Implement test cases for the currentNodeInHtmlNS() method
    }

    @Test
    public void testUnescapeEntities() {
        // TODO: Implement test cases for the unescapeEntities() method
    }
}