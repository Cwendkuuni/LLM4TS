import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals('a', tokenQueue.peek());
        tokenQueue.advance();
        assertEquals('b', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("world");
        tokenQueue.addFirst('H');
        assertEquals("Hello world", tokenQueue.toString());
        
        tokenQueue = new TokenQueue("ngineering");
        tokenQueue.addFirst("E");
        assertEquals("Engineeering", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertTrue(tokenQueue.matches("abc"));
        assertFalse(tokenQueue.matches("def"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertTrue(tokenQueue.matchesCS("abc"));
        assertFalse(tokenQueue.matchesCS("Abc"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertTrue(tokenQueue.matchesAny("a", "x", "b"));
        assertFalse(tokenQueue.matchesAny("x", "y"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<div>");
        assertTrue(tokenQueue.matchesStartTag());
        tokenQueue = new TokenQueue("</div>");
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("Hello world");
        assertTrue(tokenQueue.matchChomp("Hello"));
        assertEquals(" world", tokenQueue.toString());
        assertFalse(tokenQueue.matchChomp("Hi"));
        assertEquals(" world", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   abc");
        assertTrue(tokenQueue.matchesWhitespace());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("abc123");
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.advance();
        assertEquals('b', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals('a', tokenQueue.consume());
        assertEquals('b', tokenQueue.consume());
        assertEquals('c', tokenQueue.consume());
    }

    @Test
    public void testConsumeString() {
        TokenQueue tokenQueue = new TokenQueue("Hello world");
        tokenQueue.consume("Hello");
        assertEquals(" world", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("Hello world");
        assertEquals("Hello", tokenQueue.consumeTo(" "));
        assertEquals(" world", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("Hello world");
        assertEquals("Hello", tokenQueue.consumeToIgnoreCase("o"));
        assertEquals(" world", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("Hello world");
        assertEquals("Hello", tokenQueue.consumeToAny("o", "l"));
        assertEquals(" world", tokenQueue.toString());
    }

    @Test
    public void testChompTo() {
        TokenQueue tokenQueue = new TokenQueue("Hello world");
        assertEquals("Hello", tokenQueue.chompTo(" "));
        assertEquals("world", tokenQueue.toString());
    }
    
    @Test
    public void testChompToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("Hello world");
        assertEquals("Hello", tokenQueue.chompToIgnoreCase("o"));
        assertEquals("world", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
        assertEquals(" four", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("Hello\\", TokenQueue.unescape("Hello\\\\"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   abc");
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals("abc", tokenQueue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("abc123");
        assertEquals("abc", tokenQueue.consumeWord());
        assertEquals("123", tokenQueue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("div");
        assertEquals("div", tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("div");
        assertEquals("div", tokenQueue.consumeElementSelector());
        tokenQueue = new TokenQueue("ns|div");
        assertEquals("ns|div", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("id123");
        assertEquals("id123", tokenQueue.consumeCssIdentifier());
        tokenQueue = new TokenQueue("class-name");
        assertEquals("class-name", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("data-id");
        assertEquals("data-id", tokenQueue.consumeAttributeKey());
        tokenQueue = new TokenQueue("custom:attr");
        assertEquals("custom:attr", tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("Hello world");
        assertEquals("Hello world", tokenQueue.remainder());
        assertTrue(tokenQueue.isEmpty());
    }
}