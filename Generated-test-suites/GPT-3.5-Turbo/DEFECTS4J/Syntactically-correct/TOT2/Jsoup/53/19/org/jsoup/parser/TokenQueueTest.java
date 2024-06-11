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
        TokenQueue tokenQueue = new TokenQueue("example");
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("world");
        tokenQueue.addFirst("hello ");
        assertEquals("hello world", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("example");
        assertTrue(tokenQueue.matches("exam"));
    }

    // Additional test cases for other methods omitted for brevity
    
}