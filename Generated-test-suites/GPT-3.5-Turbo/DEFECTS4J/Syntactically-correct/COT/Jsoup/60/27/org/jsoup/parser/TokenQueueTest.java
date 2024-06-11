import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("Hello World");
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals('H', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('!');
        assertEquals("!", tokenQueue.toString());
        tokenQueue.addFirst("Hi");
        assertEquals("Hi!", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("Hello"));
        assertFalse(tokenQueue.matches("Hello World!"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("Hello"));
        assertFalse(tokenQueue.matchesCS("hello"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny("World", "Goodbye"));
        assertFalse(tokenQueue.matchesAny("World1", "Goodbye"));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("Hello"));
        assertFalse(tokenQueue.matchChomp("Hello World!"));
    }

    @Test
    public void testMatchesWhitespace() {
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        tokenQueue.advance();
        assertEquals("ello World", tokenQueue.toString());
    }

    @Test
    public void testConsume() {
        assertEquals('H', tokenQueue.consume());
        assertEquals("ello World", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hello ", tokenQueue.consumeTo(" "));
        assertEquals("World", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("Hello W", tokenQueue.consumeToIgnoreCase("oR"));
        assertEquals("orld", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Hello", tokenQueue.consumeToAny("oR"));
        assertEquals(" World", tokenQueue.toString());
    }

    @Test
    public void testChompTo() {
        assertEquals("Hello W", tokenQueue.chompTo("oR"));
        assertEquals("orld", tokenQueue.toString());
    }

    @Test
    public void testChompToIgnoreCase() {
        assertEquals("Hello W", tokenQueue.chompToIgnoreCase("oR"));
        assertEquals("orld", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        assertEquals("ello (Worl", tokenQueue.chompBalanced('(', ')'));
        assertEquals("d", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("\\\\\\'", TokenQueue.unescape("\\'"));
    }

    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        assertEquals("Hello", tokenQueue.consumeWord());
        assertEquals(" World", tokenQueue.toString());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("Hello", tokenQueue.consumeTagName());
        assertEquals(" World", tokenQueue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        assertEquals("Hello", tokenQueue.consumeElementSelector());
        assertEquals(" World", tokenQueue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("Hello", tokenQueue.consumeCssIdentifier());
        assertEquals(" World", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        assertEquals("Hello", tokenQueue.consumeAttributeKey());
        assertEquals(" World", tokenQueue.toString());
    }

    @Test
    public void testRemainder() {
        assertEquals("Hello World", tokenQueue.remainder());
        assertTrue(tokenQueue.isEmpty());
    }
}