
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

        tokenQueue = new TokenQueue("");
        assertEquals((char)0, tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.addFirst('A');
        assertEquals("Atest", tokenQueue.toString());
    }

    // Add more test cases for the remaining methods

}
