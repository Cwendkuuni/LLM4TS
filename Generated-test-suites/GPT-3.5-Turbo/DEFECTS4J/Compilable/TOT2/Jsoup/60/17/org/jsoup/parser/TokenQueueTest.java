package org.jsoup.parser;
import org.junit.Before;
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
        assertFalse(tokenQueue.isEmpty());
        tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        assertEquals('t', tokenQueue.peek());
        tokenQueue.consume();
        assertEquals('e', tokenQueue.peek());
        tokenQueue = new TokenQueue("");
        assertEquals(0, tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('A');
        assertEquals('A', tokenQueue.peek());
        
        tokenQueue.addFirst("XYZ");
        assertEquals('X', tokenQueue.peek());
    }
    
    // More test cases for other methods
    
    // Edge cases and exception handling scenarios test cases
    
    @Test
    public void testConsumeTo() {
        tokenQueue = new TokenQueue("one.two.three");
        assertEquals("one.", tokenQueue.consumeTo("."));
        assertEquals(".two.three", tokenQueue.toString());
        
        tokenQueue = new TokenQueue("one.two.three");
        assertEquals("one.two.three", tokenQueue.consumeTo(" "));
        assertEquals("", tokenQueue.toString());
        
        tokenQueue = new TokenQueue("one.two.three");
        try {
            tokenQueue.consumeTo(",");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
    }
    
    // Merge all the individual test cases into a complete JUnit 4 test file
    
    // Complete TokenQueueTest.java file with all test cases
    
}