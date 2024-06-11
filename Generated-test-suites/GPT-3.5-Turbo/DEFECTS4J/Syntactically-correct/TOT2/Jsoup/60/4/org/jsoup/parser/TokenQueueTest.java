import org.junit.jupiter.api.Test;

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
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.addFirst('a');
        assertEquals("atest", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertTrue(tokenQueue.matches("test"));
        assertFalse(tokenQueue.matches("123"));
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertTrue(tokenQueue.matchChomp("te"));
        assertEquals("st", tokenQueue.toString());
    }

    // Add more test cases for the remaining methods

}