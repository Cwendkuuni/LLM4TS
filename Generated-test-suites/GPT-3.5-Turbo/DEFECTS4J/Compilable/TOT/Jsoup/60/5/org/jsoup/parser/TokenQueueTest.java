package org.jsoup.parser;
import org.jsoup.parser.TokenQueue;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("example string");
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('x');
        assertEquals('x', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("example"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("example"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny("example", "string"));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("example"));
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
        assertEquals('x', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        assertEquals('e', tokenQueue.consume());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("exam", tokenQueue.consumeTo("ple"));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("exam", tokenQueue.consumeToIgnoreCase("PLE"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("ex", tokenQueue.consumeToAny("amp", "le"));
    }

    @Test
    public void testChompTo() {
        assertEquals("exam", tokenQueue.chompTo("ple"));
    }

    @Test
    public void testChompToIgnoreCase() {
        assertEquals("exam", tokenQueue.chompToIgnoreCase("PLE"));
    }

    @Test
    public void testChompBalanced() {
        assertEquals("xample s", tokenQueue.chompBalanced('e', 'g'));
    }

    @Test
    public void testUnescape() {
        assertEquals("example string", TokenQueue.unescape("example string"));
    }

    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        assertEquals("example", tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("example", tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        assertEquals("example", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("example", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        assertEquals("example", tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        assertEquals("example string", tokenQueue.remainder());
        assertTrue(tokenQueue.isEmpty());
    }

}