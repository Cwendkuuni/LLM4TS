import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    
    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());

        TokenQueue tokenQueue2 = new TokenQueue("test");
        assertFalse(tokenQueue2.isEmpty());
    }
    
    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertEquals('t', tokenQueue.peek());

        TokenQueue emptyTokenQueue = new TokenQueue("");
        assertEquals(0, emptyTokenQueue.peek());
    }
    
    // Add more test cases for other methods
    
}