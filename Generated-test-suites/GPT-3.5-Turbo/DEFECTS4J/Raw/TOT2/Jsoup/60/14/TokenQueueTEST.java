import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());

        tokenQueue = new TokenQueue("Test");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("Testing");
        assertEquals('T', tokenQueue.peek());

        tokenQueue = new TokenQueue("");
        assertEquals(0, tokenQueue.peek());
    }

    // Add more test methods here for other public methods in TokenQueue class

    // Edge cases and exception handling scenarios can be tested in separate test methods

}