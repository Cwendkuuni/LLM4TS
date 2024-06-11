import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());
    }

    // Add test cases for all public methods

    @Test
    public void testChompTo() {
        TokenQueue queue = new TokenQueue("<div>content</div>");
        assertEquals("content", queue.chompTo("div>"));
    }

    @Test
    public void testUnescape() {
        assertEquals("escaped\\string", TokenQueue.unescape("escaped\\\\string"));
    }

    // Add more test cases for edge cases and exceptions

}