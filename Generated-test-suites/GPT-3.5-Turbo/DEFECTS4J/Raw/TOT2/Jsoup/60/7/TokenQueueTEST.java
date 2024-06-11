import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertEquals('t', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.addFirst('a');
        assertEquals("atest", tokenQueue.toString());
    }

    // Additional test cases for other public methods should be added here

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertTrue(tokenQueue.matchChomp("test"));
        assertEquals(0, tokenQueue.remainingLength());
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertEquals('t', tokenQueue.consume());
        assertEquals('e', tokenQueue.consume());
        assertEquals('s', tokenQueue.consume());
        assertEquals('t', tokenQueue.consume());
    }

    // Additional test cases for edge cases and exceptions handling should be added here

}