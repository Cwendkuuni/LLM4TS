import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("Test");
        assertEquals('T', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("Test");
        tokenQueue.addFirst('A');
        assertEquals('A', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("Test");
        assertTrue(tokenQueue.matches("Test"));
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("Testing");
        assertTrue(tokenQueue.matchChomp("Tes"));
        assertEquals('t', tokenQueue.peek());
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("Testing");
        assertTrue(tokenQueue.matchesAny('e', 's'));
    }

    // Continue with test cases for other methods...

}