package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setup() {
        tokenQueue = new TokenQueue("");
    }

    @Test
    public void testIsEmpty() {
        assertTrue(tokenQueue.isEmpty());
        tokenQueue.addFirst("test");
        assertFalse(tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        assertEquals(0, tokenQueue.peek());
        tokenQueue.addFirst("test");
        assertEquals('t', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst("test");
        assertEquals("test", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        assertFalse(tokenQueue.matches("test"));
        tokenQueue.addFirst("test");
        assertTrue(tokenQueue.matches("test"));
    }

    @Test
    public void testMatchesCS() {
        assertFalse(tokenQueue.matchesCS("test"));
        tokenQueue.addFirst("test");
        assertTrue(tokenQueue.matchesCS("test"));
    }

    @Test
    public void testMatchesAny() {
        assertFalse(tokenQueue.matchesAny("test", "test2"));
        tokenQueue.addFirst("test");
        assertTrue(tokenQueue.matchesAny("test", "test2"));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.addFirst("<div>");
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertFalse(tokenQueue.matchChomp("test"));
        tokenQueue.addFirst("test");
        assertTrue(tokenQueue.matchChomp("test"));
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        assertFalse(tokenQueue.matchesWhitespace());
        tokenQueue.addFirst(" ");
        assertTrue(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        assertFalse(tokenQueue.matchesWord());
        tokenQueue.addFirst("test");
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        tokenQueue.advance();
        assertEquals("", tokenQueue.toString());
        tokenQueue.addFirst("test");
        tokenQueue.advance();
        assertEquals("est", tokenQueue.toString());
    }

    @Test
    public void testConsume() {
        assertEquals(0, tokenQueue.consume());
        tokenQueue.addFirst("test");
        assertEquals('t', tokenQueue.consume());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("", tokenQueue.consumeTo("test"));
        tokenQueue.addFirst("test");
        assertEquals("", tokenQueue.consumeTo("test"));
        tokenQueue.addFirst("prefixtestsuffix");
        assertEquals("prefix", tokenQueue.consumeTo("test"));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("", tokenQueue.consumeToIgnoreCase("test"));
        tokenQueue.addFirst("test");
        assertEquals("", tokenQueue.consumeToIgnoreCase("test"));
        tokenQueue.addFirst("prefixTESTsuffix");
        assertEquals("prefix", tokenQueue.consumeToIgnoreCase("test"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("", tokenQueue.consumeToAny("test", "test2"));
        tokenQueue.addFirst("prefix2suffix");
        assertEquals("prefix", tokenQueue.consumeToAny("test", "test2"));
    }

    @Test
    public void testChompTo() {
        assertEquals("", tokenQueue.chompTo("test"));
        tokenQueue.addFirst("test");
        assertEquals("", tokenQueue.chompTo("test"));
        tokenQueue.addFirst("prefixtestsuffix");
        assertEquals("prefix", tokenQueue.chompTo("test"));
    }

    @Test
    public void testChompToIgnoreCase() {
        assertEquals("", tokenQueue.chompToIgnoreCase("test"));
        tokenQueue.addFirst("test");
        assertEquals("", tokenQueue.chompToIgnoreCase("test"));
        tokenQueue.addFirst("prefixTESTsuffix");
        assertEquals("prefix", tokenQueue.chompToIgnoreCase("test"));
    }

    @Test
    public void testChompBalanced() {
        assertEquals("", tokenQueue.chompBalanced('(', ')'));
        tokenQueue.addFirst("(one (two) three) four");
        assertEquals("(one (two) three)", tokenQueue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        assertEquals("", TokenQueue.unescape(""));
        assertEquals("\\q", TokenQueue.unescape("\\\\q"));
    }

    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
        tokenQueue.addFirst("   ");
        assertTrue(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        assertEquals("", tokenQueue.consumeWord());
        tokenQueue.addFirst("test");
        assertEquals("test", tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("", tokenQueue.consumeTagName());
        tokenQueue.addFirst("div");
        assertEquals("div", tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        assertEquals("", tokenQueue.consumeElementSelector());
        tokenQueue.addFirst("*|div");
        assertEquals("*|div", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("", tokenQueue.consumeCssIdentifier());
        tokenQueue.addFirst("identifier");
        assertEquals("identifier", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        assertEquals("", tokenQueue.consumeAttributeKey());
        tokenQueue.addFirst("key");
        assertEquals("key", tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        assertEquals("", tokenQueue.remainder());
        tokenQueue.addFirst("remainder");
        assertEquals("remainder", tokenQueue.remainder());
    }

}