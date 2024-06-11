import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CodeConsumerTest {

    private CodeConsumer codeConsumer;

    @Before
    public void setUp() {
        codeConsumer = new CodeConsumerImpl();
    }

    @Test
    public void testStartSourceMapping() {
        Node node = new Node();
        codeConsumer.startSourceMapping(node);
        // Assert that the source mapping is properly started
        assertTrue(codeConsumer.statementNeedsEnded);
    }

    @Test
    public void testEndSourceMapping() {
        Node node = new Node();
        codeConsumer.endSourceMapping(node);
        // Assert that the source mapping is properly ended
        assertFalse(codeConsumer.statementNeedsEnded);
    }

    @Test
    public void testContinueProcessing() {
        // Test if continueProcessing() returns true
        assertTrue(codeConsumer.continueProcessing());
    }

    @Test
    public void testGetLastChar() {
        codeConsumer.append("abc");
        // Fetch the last character appended
        char lastChar = codeConsumer.getLastChar();
        // Assert that the last character is 'c'
        assertEquals('c', lastChar);
    }

    @Test
    public void testAddIdentifier() {
        codeConsumer.addIdentifier("identifier");
        // Assert that the identifier is added properly
        assertEquals("identifier", codeConsumer.getLastChar());
    }

    /* Add more test cases for other methods */

}