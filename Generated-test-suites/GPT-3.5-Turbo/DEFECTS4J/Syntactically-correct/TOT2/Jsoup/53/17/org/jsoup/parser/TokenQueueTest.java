import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    
    // Test cases for TokenQueue isEmpty method
    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
        
        tokenQueue = new TokenQueue("data");
        assertFalse(tokenQueue.isEmpty());
    }
    
    // Test cases for TokenQueue peek method
    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals('a', tokenQueue.peek());
        
        tokenQueue.consume();
        assertEquals('b', tokenQueue.peek());
    }
    
    // Further test cases for other methods in TokenQueue class can be added here

}