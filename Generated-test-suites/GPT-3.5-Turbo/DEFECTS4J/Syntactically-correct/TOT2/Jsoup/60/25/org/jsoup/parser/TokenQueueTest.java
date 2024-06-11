import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    
    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
        assertFalse(tokenQueue.remainingLength() == 0);
    }
    
    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertEquals('t', tokenQueue.peek());
        tokenQueue.consume();
        assertEquals('e', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("world");
        tokenQueue.addFirst('H');
        assertEquals("Hello, world", tokenQueue.toString());
    }
    
    // Additional test cases for other methods should be included here
}