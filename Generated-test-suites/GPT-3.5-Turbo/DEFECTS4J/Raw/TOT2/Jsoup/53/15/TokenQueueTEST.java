import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("test");
    }

    @Test
    public void testIsEmpty() {
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals('t', queue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst("ing");
        assertEquals("ingtest", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("test"));
        assertFalse(tokenQueue.matches("ing"));
    }
}