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
        TokenQueue tokenQueue = new TokenQueue("test");
        assertEquals('t', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("world");
        tokenQueue.addFirst('h');
        assertEquals('h', tokenQueue.peek());
    }
    
    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("example");
        assertTrue(tokenQueue.matches("exam"));
    }
    
    // Add more test cases for the remaining public methods
    
    // Edge cases and exception handling scenarios
    
    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("testing");
        assertTrue(tokenQueue.matchChomp("test"));
        assertEquals('i', tokenQueue.peek());
    }
    
    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
    }
    
    // Complete JUnit test file
    
    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   abc");
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals('a', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue(" abc123");
        assertEquals("abc", tokenQueue.consumeWord());
    }
    
    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("tag-name");
        assertEquals("tag", tokenQueue.consumeTagName());
    }
    
    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("element|name");
        assertEquals("element", tokenQueue.consumeElementSelector());
    }

    // Add more test cases for the remaining methods
    
}