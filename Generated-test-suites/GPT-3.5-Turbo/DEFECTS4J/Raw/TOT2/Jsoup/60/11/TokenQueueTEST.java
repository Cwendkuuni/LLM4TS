import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;

public class TokenQueueTest {
    
    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertEquals('t', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("world");
        tokenQueue.addFirst('H');
        assertEquals("Hello world", tokenQueue.toString());
    }

    // Add more test cases for each method

    // Edge cases and exception handling scenarios

    @Test
    public void testMatchChompEdgeCase() {
        TokenQueue tokenQueue = new TokenQueue("1234");
        assertTrue(tokenQueue.matchChomp("12"));
        assertEquals("34", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKeyException() {
        TokenQueue tokenQueue = new TokenQueue("invalid:key");
        try {
            tokenQueue.consumeAttributeKey();
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // Pass
        }
    }

    // Complete test file

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("Hello, World!");
        assertEquals("Hello", tokenQueue.consumeTo(", "));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(((test)))");
        assertEquals("((test))", tokenQueue.chompBalanced('(', ')'));
    }

    // Add more test cases

}