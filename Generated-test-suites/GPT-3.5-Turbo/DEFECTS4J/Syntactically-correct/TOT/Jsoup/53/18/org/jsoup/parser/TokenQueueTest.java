import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("Hello");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("Hello");
        assertEquals('H', queue.peek());

        queue.advance();
        assertEquals('e', queue.peek());

        queue = new TokenQueue("");
        assertEquals(0, queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("Hello");
        queue.addFirst('!');
        assertEquals("!Hello", queue.toString());

        queue = new TokenQueue("");
        queue.addFirst('!');
        assertEquals("!", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("Hello");
        assertTrue(queue.matches("Hello"));

        queue.advance();
        assertFalse(queue.matches("Hello"));

        assertFalse(queue.matches("hello"));
    }

    // Repeat similar tests for other methods

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("Hello, World!");
        String consumed = queue.consumeTo(",");
        assertEquals("Hello", consumed);
        assertEquals(", World!", queue.toString());

        consumed = queue.consumeTo("!");
        assertEquals(", World", consumed);
        assertEquals("!", queue.toString());

        consumed = queue.consumeTo("?");
        assertEquals("!", consumed);
        assertEquals("", queue.toString());
    }

    @Test
    public void testChompTo() {
        TokenQueue queue = new TokenQueue("Hello, World!");
        String chomped = queue.chompTo(",");
        assertEquals("Hello", chomped);
        assertEquals(" World!", queue.toString());

        chomped = queue.chompTo("!");
        assertEquals(" World", chomped);
        assertEquals("!", queue.toString());

        chomped = queue.chompTo("?");
        assertEquals("", chomped);
        assertEquals("", queue.toString());
    }

    // Repeat similar tests for other methods

}