package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokenQueueTest {

    @Test
    public void testNewInstance() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abcdef", queue.queue);
        assertEquals(0, queue.pos);
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("abc");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.peek());

        queue.consume();
        assertEquals('b', queue.peek());

        queue.consumeTo("def");
        assertEquals('e', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        assertEquals("a", queue.queue.substring(0, 1));

        queue.addFirst("abc");
        assertEquals("abc", queue.queue.substring(0, 3));
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("cd"));
        assertFalse(queue.matches("xyz"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCDef");
        assertTrue(queue.matchesCS("Ab"));
        assertFalse(queue.matchesCS("xyz"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("12345");
        assertTrue(queue.matchesAny('1', '2', '3'));
        assertFalse(queue.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesAny('a', 'b', 'c'));
        assertFalse(queue.matchesAny('x', 'y', 'z'));

        queue = new TokenQueue("\\");
        assertTrue(queue.matchesAny('\\'));
        assertFalse(queue.matchesAny('a', 'b', 'c'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abc");
        assertTrue(queue.matchesStartTag());

        queue = new TokenQueue("abcd");
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matchChomp("ab"));
        assertEquals("cdefg", queue.queue);

        assertTrue(queue.matchChomp("cd"));
        assertEquals("efg", queue.queue);

        assertFalse(queue.matchChomp("xyz"));
        assertEquals("efg", queue.queue);
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        assertTrue(queue.matchesWhitespace());

        queue.consumeWhitespace();
        assertEquals('a', queue.peek());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("12345");
        assertTrue(queue.matchesWord());

        queue.consumeWord();
        assertEquals("2345", queue.queue);
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.advance();
        assertEquals('b', queue.peek());

        queue.consume();
        queue.advance();
        assertEquals('c', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.consume());
        assertEquals("bcdef", queue.queue);

        queue = new TokenQueue("12345");
        assertEquals('1', queue.consume());
        assertEquals("2345", queue.queue);
    }

    @Test
    public void testConsumeString() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.consume("ab");
        assertEquals("cdef", queue.queue);

        queue = new TokenQueue("12345");
        queue.consume("12");
        assertEquals("345", queue.queue);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeUnmatchedSequence() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeTo("def"));
        assertEquals("", queue.queue);

        queue = new TokenQueue("12345");
        assertEquals("123", queue.consumeTo("45"));
        assertEquals("", queue.queue);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("ABCDEF");
        assertEquals("ABC", queue.consumeToIgnoreCase("def"));
        assertEquals("", queue.queue);

        queue = new TokenQueue("xyzABCDEF");
        assertEquals("xyzABC", queue.consumeToIgnoreCase("def"));
        assertEquals("EF", queue.queue);
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abcdef", queue.consumeToAny(""));

        assertEquals("ab", queue.consumeToAny("abcd"));
        assertEquals("def", queue.queue);

        assertEquals("ef", queue.consumeToAny("efg"));
        assertEquals("", queue.queue);
    }

    @Test
    public void testChompTo() {
        TokenQueue queue = new TokenQueue("(abc)(def)ghi");
        assertEquals("(abc)", queue.chompBalanced('(', ')'));
        assertEquals("(def)ghi", queue.queue);

        queue = new TokenQueue("(xyz)");
        assertEquals("(xyz)", queue.chompBalanced('(', ')'));
        assertEquals("", queue.queue);
    }

    @Test
    public void testChompBalancedWithQuotes() {
        TokenQueue queue = new TokenQueue("\"abc\"(def)\"ghi\"");
        assertEquals("\"abc\"", queue.chompBalanced('"', '"'));
        assertEquals("(def)\"ghi\"", queue.queue);

        queue = new TokenQueue("\"(xyz)\"");
        assertEquals("\"(xyz)\"", queue.chompBalanced('"', '"'));
        assertEquals("", queue.queue);
    }

    @Test
    public void testChompBalancedWithEscapedQuotes() {
        TokenQueue queue = new TokenQueue("\"abc\\\"(def)\"ghi\"");
        assertEquals("\"abc\\\"", queue.chompBalanced('"', '"'));
        assertEquals("(def)\"ghi\"", queue.queue);

        queue = new TokenQueue("\\\"(xyz)\\\"");
        assertEquals("\\\"(xyz)\\\"", queue.chompBalanced('\\', '\\'));
        assertEquals("", queue.queue);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        assertTrue(queue.consumeWhitespace());
        assertEquals('a', queue.peek());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("12345");
        assertEquals("2345", queue.consumeWord());
        assertEquals("", queue.queue);
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<tag name>attr=\"value\"");
        assertEquals("tag name", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#id.class1.class2");
        assertEquals("div", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color: #id123;");
        assertEquals("#id123", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"class1 class2\"");
        assertEquals("class", queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.remainder());

        queue = new TokenQueue("12345");
        assertEquals("5", queue.remainder());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.toString());

        queue = new TokenQueue("12345");
        assertEquals("5", queue.toString());
    }
}