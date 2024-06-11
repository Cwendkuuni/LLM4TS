package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testIsNotEmptyQueue() {
        TokenQueue queue = new TokenQueue("abc");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekOnEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.peek();
    }

    @Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst("def");
        assertEquals("defabc", queue.toString());
    }

    @Test
    public void testAddFirstCharacter() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst('d');
        assertEquals("dabc", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matches("bc"));
    }

    @Test
    public void testMatchesNot() {
        TokenQueue queue = new TokenQueue("abc");
        assertFalse(queue.matches("xyz"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABC");
        assertTrue(queue.matchesCS("AB"));
    }

    @Test
    public void testMatchesCSNot() {
        TokenQueue queue = new TokenQueue("ABC");
        assertFalse(queue.matchesCS("XYZ"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny("bc", "cd"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny('b', 'c'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abc>");
        assertTrue(queue.matchesStartTag());
    }

    @Test
    public void testMatchesStartTagNot() {
        TokenQueue queue = new TokenQueue("123");
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcabc");
        assertTrue(queue.matchChomp("abc"));
        assertEquals("abc", queue.toString());
    }

    @Test
    public void testMatchChompNot() {
        TokenQueue queue = new TokenQueue("abcabc");
        assertFalse(queue.matchChomp("xyz"));
        assertEquals("abcabc", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        assertTrue(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWhitespaceNot() {
        TokenQueue queue = new TokenQueue("abc");
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertTrue(queue.matchesWord());
    }

    @Test
    public void testMatchesWordNot() {
        TokenQueue queue = new TokenQueue("123");
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        assertEquals('b', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeOnEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume();
    }

    @Test
    public void testConsumeString() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.consume("def");
        assertEquals("abc", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNotMatchedSequence() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consume("xyz");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNotLongEnoughSequence() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consume("def");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeTo("def"));
        assertEquals("ef", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("ABCDEF");
        assertEquals("ABC", queue.consumeToIgnoreCase("DEF"));
        assertEquals("EF", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcxyz");
        assertEquals("xyz", queue.consumeToAny("abc", "def"));
        assertEquals("abc", queue.toString());
    }

    @Test
    public void testChompTo() {
        TokenQueue queue = new TokenQueue("((a(b)c)d)e");
        assertEquals("a(b)c", queue.chompBalanced('(', ')'));
        assertEquals("d)e", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((a(b)c)d)e");
        assertEquals("a(b)c", queue.chompBalanced('(', ')'));
        assertEquals("d)e", queue.toString());
    }

    @Test
    public void testUnescape() {
        String in = "\\(a\\)";
        String out = TokenQueue.unescape(in);
        assertEquals("(a)", out);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        assertTrue(queue.consumeWhitespace());
        assertEquals("abc", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertEquals("abc", queue.consumeWord());
        assertEquals("123", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<tag name>");
        assertEquals("tag", queue.consumeTagName());
        assertEquals(" name>", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("tag:name|class");
        assertEquals("tag", queue.consumeElementSelector());
        assertEquals(":name|class", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("id:identifier class:class");
        assertEquals("id", queue.consumeCssIdentifier());
        assertEquals("identifier class:class", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("name=value class:class");
        assertEquals("name", queue.consumeAttributeKey());
        assertEquals("=value class:class", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.remainder());
        assertEquals("abc", queue.toString());
    }

}