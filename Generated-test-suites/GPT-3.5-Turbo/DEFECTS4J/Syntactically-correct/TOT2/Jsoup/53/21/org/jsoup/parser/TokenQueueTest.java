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
        TokenQueue tokenQueue = new TokenQueue("example");
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("world");
        tokenQueue.addFirst('h');
        assertEquals("hello world", tokenQueue.toString());
    }

    // Add more individual test cases for the remaining methods as per the functionalities they provide
    // Test edge cases, exception handling scenarios, typical cases, etc.

    // Test case for matches()
    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("HelloWorld");
        assertTrue(tokenQueue.matches("Hello"));
    }

    // Test case for matchesAny() - typical case
    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertTrue(tokenQueue.matchesAny('a', 'b', 'c'));
    }

    // Test case for matchesAny() - edge case
    @Test
    public void testMatchesAnyEdgeCase() {
        TokenQueue tokenQueue = new TokenQueue("123");
        assertTrue(tokenQueue.matchesAny('1', '2', '3'));
    }

    // Test case for consume()
    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("hello");
        char firstChar = tokenQueue.consume();
        assertEquals('h', firstChar);
    }

    // Test case for consumeTo()
    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        String consumed = tokenQueue.consumeTo(" ");
        assertEquals("hello", consumed);
    }

    // Test case for consumeToIgnoreCase()
    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("Hello World");
        String consumed = tokenQueue.consumeToIgnoreCase("world");
        assertEquals("Hello ", consumed);
    }

    // Test case for chompBalanced()
    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        String balanced = tokenQueue.chompBalanced('(', ')');
        assertEquals("(one (two) three", balanced);
    }

    // Add more test cases for the remaining methods

}