import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    private TokenQueue tokenQueue;

    @Test
    public void testIsEmpty() {
        tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
        
        tokenQueue = new TokenQueue("data");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        tokenQueue = new TokenQueue("data");
        assertEquals('d', tokenQueue.peek());
        
        tokenQueue = new TokenQueue("");
        assertEquals(0, tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue = new TokenQueue("data");
        tokenQueue.addFirst('a');
        assertEquals("adata", tokenQueue.toString());
        
        tokenQueue.addFirst("test");
        assertEquals("testdata", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        tokenQueue = new TokenQueue("data");
        assertTrue(tokenQueue.matches("dat"));
        assertFalse(tokenQueue.matches("test"));
    }

    // Additional test cases for other TokenQueue methods should be added here

}