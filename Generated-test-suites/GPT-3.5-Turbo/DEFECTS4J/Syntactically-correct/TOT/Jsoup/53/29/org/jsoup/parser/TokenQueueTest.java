
package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("hello world");
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals('h', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst("!");
        assertEquals('!', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("hello"));
        assertFalse(tokenQueue.matches("world"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("hello"));
        assertFalse(tokenQueue.matchesCS("Hello"));
    }

    @Test
    public void testMatchesAnyString() {
        assertTrue(tokenQueue.matchesAny("hello", "world"));
        assertFalse(tokenQueue.matchesAny("Hello", "World"));
    }

    @Test
    public void testMatchesAnyChar() {
        assertTrue(tokenQueue.matchesAny('h', 'w'));
        assertFalse(tokenQueue.matchesAny('H', 'W'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tagQueue = new TokenQueue("<div>");
        assertTrue(tagQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("hello"));
        assertEquals(" world", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue whitespaceQueue = new TokenQueue("     ");
        assertTrue(whitespaceQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue wordQueue = new TokenQueue("hello123");
        assertTrue(wordQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        tokenQueue.advance();
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        assertEquals('h', tokenQueue.consume());
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testConsumeSeq() {
        tokenQueue.consume("hello");
        assertEquals(" world", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("hello", tokenQueue.consumeTo(" "));
        assertEquals(" ", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("hello", tokenQueue.consumeToIgnoreCase(" "));
        assertEquals(" ", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("hello", tokenQueue.consumeToAny(" ", "!"));
        assertEquals(" ", tokenQueue.toString());
    }

    @Test
    public void testChompTo() {
        assertEquals("hello", tokenQueue.chompTo(" "));
        assertEquals("world", tokenQueue.toString());
    }

    @Test
    public void testChompToIgnoreCase() {
        assertEquals("hello", tokenQueue.chompToIgnoreCase(" "));
        assertEquals("world", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue balancedQueue = new TokenQueue("(one (two) three) four");
        assertEquals("(one (two) three)", balancedQueue.chompBalanced('(', ')'));
        assertEquals(" four", balancedQueue.toString());
    }

    @Test
    public void testUnescape() {
        String escapedString = "Hello\\, world";
        String unescapedString = TokenQueue.unescape(escapedString);
        assertEquals("Hello, world", unescapedString);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue whitespaceQueue = new TokenQueue("     ");
        assertTrue(whitespaceQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue wordQueue = new TokenQueue("hello123");
        assertEquals("hello123", wordQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tagQueue = new TokenQueue("div");
        assertEquals("div", tagQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue elementQueue = new TokenQueue("div");
        assertEquals("div", elementQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue cssQueue = new TokenQueue("id");
        assertEquals("id", cssQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue attributeQueue = new TokenQueue("data-id");
        assertEquals("data-id", attributeQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        String remainder = tokenQueue.remainder();
        assertEquals("hello world", remainder);
        assertTrue(tokenQueue.isEmpty());
    }
}

