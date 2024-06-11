import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(null, null);
    }

    @Test
    public void testRead() {
        // Create test case here
    }

    @Test
    public void testEmit() {
        // Create test case here
    }

    @Test
    public void testEmitString() {
        // Create test case here
    }

    @Test
    public void testEmitCharArray() {
        // Create test case here
    }

    @Test
    public void testEmitIntArray() {
        // Create test case here
    }

    @Test
    public void testEmitChar() {
        // Create test case here
    }

    @Test
    public void testGetState() {
        // Create test case here
    }

    @Test
    public void testTransition() {
        // Create test case here
    }

    @Test
    public void testAdvanceTransition() {
        // Create test case here
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Create test case here
    }

    @Test
    public void testConsumeCharacterReference() {
        // Create test case here
    }

    @Test
    public void testCreateTagPending() {
        // Create test case here
    }

    @Test
    public void testEmitTagPending() {
        // Create test case here
    }

    @Test
    public void testCreateCommentPending() {
        // Create test case here
    }

    @Test
    public void testEmitCommentPending() {
        // Create test case here
    }

    @Test
    public void testCreateDoctypePending() {
        // Create test case here
    }

    @Test
    public void testEmitDoctypePending() {
        // Create test case here
    }

    @Test
    public void testCreateTempBuffer() {
        // Create test case here
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Create test case here
    }

    @Test
    public void testAppropriateEndTagName() {
        // Create test case here
    }

    @Test
    public void testErrorState() {
        // Create test case here
    }

    @Test
    public void testEofError() {
        // Create test case here
    }

    @Test
    public void testCharacterReferenceError() {
        // Create test case here
    }

    @Test
    public void testError() {
        // Create test case here
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // Create test case here
    }

    @Test
    public void testUnescapeEntities() {
        // Create test case here
    }
}