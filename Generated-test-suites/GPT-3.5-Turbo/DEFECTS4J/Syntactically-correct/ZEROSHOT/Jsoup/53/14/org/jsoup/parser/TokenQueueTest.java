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
        TokenQueue tokenQueue = new TokenQueue("");
        assertEquals(0, tokenQueue.peek());
        tokenQueue = new TokenQueue("test");
        assertEquals('t', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.addFirst('t');
        assertEquals(1, tokenQueue.remainingLength());
        tokenQueue.addFirst("test");
        assertEquals(5, tokenQueue.remainingLength());
    }
    
    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertTrue(tokenQueue.matches("test"));
        assertFalse(tokenQueue.matches("tes"));
        assertTrue(tokenQueue.matchesCS("test"));
        assertFalse(tokenQueue.matchesCS("Tes"));
    }
    
    // Add more test methods to test all methods in the TokenQueue class
    
}