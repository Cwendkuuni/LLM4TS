package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("abcdefg");
    }

    @Test
    public void testIsEmpty() {
        assertTrue(tokenQueue.isEmpty());
        tokenQueue.addFirst('a');
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals('a', tokenQueue.peek());
        tokenQueue.advance();
        assertEquals('b', tokenQueue.peek());
        tokenQueue.consume();
        assertEquals('c', tokenQueue.peek());
    }

    @Test
    public void testAddFirst_Character() {
        tokenQueue.addFirst('h');
        assertEquals('h', tokenQueue.consume());
        assertEquals('a', tokenQueue.consume());
        assertEquals('b', tokenQueue.consume());
    }

    @Test
    public void testAddFirst_String() {
        tokenQueue.addFirst("xyz");
        assertEquals("xyz", tokenQueue.consumeTo("a"));
        assertEquals("a", tokenQueue.consume());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("abc"));
        assertFalse(tokenQueue.matches("def"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("abc"));
        assertFalse(tokenQueue.matchesCS("ABC"));
    }

    @Test
    public void testMatchesAny_String() {
        assertTrue(tokenQueue.matchesAny("abc", "def", "xyz"));
        assertFalse(tokenQueue.matchesAny("def", "xyz"));
    }

    @Test
    public void testMatchesAny_Char() {
        assertTrue(tokenQueue.matchesAny('a', 'b', 'c'));
        assertFalse(tokenQueue.matchesAny('d', 'e', 'f'));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.addFirst("<");
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.addFirst("x");
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("abc"));
        assertEquals('d', tokenQueue.consume());
    }

    @Test
    public void testMatchesWhitespace() {
        assertFalse(tokenQueue.matchesWhitespace());
        tokenQueue.addFirst(' ');
        assertTrue(tokenQueue.matchesWhitespace());
        tokenQueue.consume();
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.consumeto("c");
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        tokenQueue.advance();
        assertEquals('b', tokenQueue.peek());
        tokenQueue.advance();
        assertEquals('c', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        assertEquals('a', tokenQueue.consume());
        assertEquals('b', tokenQueue.consume());
    }

    @Test
    public void testConsume_String() {
        tokenQueue.consume("ab");
        assertEquals('c', tokenQueue.consume());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("a", tokenQueue.consumeTo("b"));
        assertEquals('b', tokenQueue.consume());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("a", tokenQueue.consumeToIgnoreCase("B"));
        assertEquals('b', tokenQueue.consume());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("a", tokenQueue.consumeToAny('b'));
        assertEquals('b', tokenQueue.consume());
    }

    @Test
    public void testChompTo() {
        assertEquals("a", tokenQueue.chompTo("b"));
        assertEquals('b', tokenQueue.consume());
    }

    @Test
    public void testChompToIgnoreCase() {
        assertEquals("a", tokenQueue.chompToIgnoreCase("B"));
        assertEquals('b', tokenQueue.consume());
    }

    @Test
    public void testChompBalanced() {
        assertEquals("bcde", tokenQueue.chompBalanced('b', 'e'));
        assertEquals('f', tokenQueue.consume());
    }

    @Test
    public void testUnescape() {
        assertEquals("\\", TokenQueue.unescape("\\\\"));
        assertEquals("\"", TokenQueue.unescape("\\\""));
    }

    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
        tokenQueue.addFirst(' ');
        assertTrue(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        assertEquals("abcd", tokenQueue.consumeWord());
        assertEquals('e', tokenQueue.consume());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("abc", tokenQueue.consumeTagName());
        assertEquals('d', tokenQueue.consume());
    }

    @Test
    public void testConsumeElementSelector() {
        assertEquals("abc", tokenQueue.consumeElementSelector());
        assertEquals('d', tokenQueue.consume());
    }

    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("abcd", tokenQueue.consumeCssIdentifier());
        assertEquals('e', tokenQueue.consume());
    }

    @Test
    public void testConsumeAttributeKey() {
        assertEquals("abc", tokenQueue.consumeAttributeKey());
        assertEquals('d', tokenQueue.consume());
    }

    @Test
    public void testRemainder() {
        assertEquals("abcdefg", tokenQueue.remainder());
    }
    
}