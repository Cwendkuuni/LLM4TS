import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
        
        queue = new TokenQueue("sample data");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("data");
        assertEquals('d', queue.peek());
    }
    
    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("data");
        queue.addFirst('c');
        assertEquals("cdata", queue.toString());
    }
    
    // Additional test cases for other methods can be added as needed

}