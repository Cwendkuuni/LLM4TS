import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertEquals('t', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("world");
        tokenQueue.addFirst("Hello, ");
        assertEquals("Hello, world", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("Example");
        assertTrue(tokenQueue.matches("Example"));
    }

    // Additional test cases for other methods in TokenQueue class can be added here

    @Test
    public void testExceptionHandling() {
        TokenQueue tokenQueue = new TokenQueue("Test");
        
        try {
            tokenQueue.consume("Exam"); // Expected to throw IllegalStateException
            fail("consume method did not throw expected exception");
        } catch (IllegalStateException e) {
            // Expected exception caught
        }
    }
}