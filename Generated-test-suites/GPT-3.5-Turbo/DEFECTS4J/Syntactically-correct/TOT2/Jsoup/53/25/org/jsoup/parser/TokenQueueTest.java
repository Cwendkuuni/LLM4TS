import static org.junit.Assert.*;
import org.junit.Test;

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
    
    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertTrue(tokenQueue.matches("te"));
    }
    
    // Add more test cases for other methods
    
    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
    }
    
    @Test
    public void testUnescape() {
        String escapedString = "\\\"test\\\"";
        assertEquals("\"test\"", TokenQueue.unescape(escapedString));
    }
    
    // Merge all test cases here
    
}