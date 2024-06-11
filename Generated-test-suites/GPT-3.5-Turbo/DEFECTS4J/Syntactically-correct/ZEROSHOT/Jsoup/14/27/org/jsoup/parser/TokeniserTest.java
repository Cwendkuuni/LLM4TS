import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TokeniserTest {

    private CharacterReader reader;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        reader = mock(CharacterReader.class);
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead() {
        // TODO: implement test
    }

    @Test
    public void testEmit() {
        // TODO: implement test
    }

    @Test
    public void testEmitString() {
        // TODO: implement test
    }

    @Test
    public void testEmitChar() {
        // TODO: implement test
    }

    @Test
    public void testGetState() {
        // TODO: implement test
    }

    @Test
    public void testTransition() {
        // TODO: implement test
    }

    @Test
    public void testAdvanceTransition() {
        // TODO: implement test
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // TODO: implement test
    }

    @Test
    public void testConsumeCharacterReference() {
        // TODO: implement test
    }

    @Test
    public void testCreateTagPending() {
        // TODO: implement test
    }

    @Test
    public void testEmitTagPending() {
        // TODO: implement test
    }

    @Test
    public void testCreateCommentPending() {
        // TODO: implement test
    }

    @Test
    public void testEmitCommentPending() {
        // TODO: implement test
    }

    @Test
    public void testCreateDoctypePending() {
        // TODO: implement test
    }

    @Test
    public void testEmitDoctypePending() {
        // TODO: implement test
    }

    @Test
    public void testCreateTempBuffer() {
        // TODO: implement test
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // TODO: implement test
    }

    @Test
    public void testIsTrackErrors() {
        // TODO: implement test
    }

    @Test
    public void testSetTrackErrors() {
        // TODO: implement test
    }

    @Test
    public void testErrorTokeniserState() {
        // TODO: implement test
    }

    @Test
    public void testEofErrorTokeniserState() {
        // TODO: implement test
    }

    @Test
    public void testCharacterReferenceError() {
        // TODO: implement test
    }

    @Test
    public void testErrorString() {
        // TODO: implement test
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // TODO: implement test
    }

}