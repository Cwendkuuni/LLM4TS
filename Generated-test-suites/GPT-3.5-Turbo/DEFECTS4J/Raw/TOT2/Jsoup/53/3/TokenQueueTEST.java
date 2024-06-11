import org.jsoup.parser.TokenQueue;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    
    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
        
        tokenQueue = new TokenQueue("test");
        assertFalse(tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertEquals('t', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("world");
        tokenQueue.addFirst('h');
        assertEquals("hello world", tokenQueue.toString());
    }
    
    // Additional test cases for other methods can be added here
    
}