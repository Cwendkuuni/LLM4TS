import org.jsoup.parser.TokenQueue;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("Test String");
    }

    @Test
    public void testIsEmpty() {
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals('T', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('A');
        assertEquals('A', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("Test"));
    }

    // Add more test cases for other methods...

    @Test
    public void testConsume() {
        assertEquals('T', tokenQueue.consume());
        assertEquals('e', tokenQueue.consume());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Test", tokenQueue.consumeTo(" "));
    }

    @Test
    public void testChompTo() {
        assertEquals("Tes", tokenQueue.chompTo("t"));
    }

    // Add more test cases for other methods...

}