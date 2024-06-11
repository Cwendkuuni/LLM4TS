package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testNewInstance() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals("hello world", queue.queue);
        assertEquals(0, queue.pos);
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("hello");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals('h', queue.peek());
        assertEquals('h', queue.consume());

        queue = new TokenQueue("hello world");
        assertNull(queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.addFirst('x');
        assertEquals('x', queue.consume());
        assertEquals('h', queue.consume());

        queue = new TokenQueue("hello world");
        queue.addFirst("hello");
        assertEquals("hello", queue.consumeToIgnoreCase("hello"));
        assertEquals('w', queue.consume());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.matches("ho"));
        assertTrue(queue.matches("ll"));
        assertTrue(queue.matchesIgnoreCase("he"));
        assertFalse(queue.matches("llo"));

        queue = new TokenQueue("");
        assertFalse(queue.matches(""));
        assertFalse(queue.matches("foo"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.matchesAny("ho", "ll"));
        assertFalse(queue.matchesAny("llo"));
        assertTrue(queue.matchesAny('w', 'd'));

        queue = new TokenQueue("123");
        assertTrue(queue.matchesAny("1", "2", "3"));
        assertFalse(queue.matchesAny("4"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>");
        assertTrue(queue.matchesStartTag());
        assertEquals("<html>", queue.consumeToIgnoreCase("<html>"));
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("<html><head><title>foo<bar>");
        assertTrue(queue.matchChomp("title"));
        assertEquals("<bar>", queue.consumeToIgnoreCase("<bar>"));

        queue = new TokenQueue("<html><head><title>foo<bar>");
        assertFalse(queue.matchChomp("title2"));
        assertEquals("<html><head><title>foo<bar>", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   hello world   ");
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.matchesWord());
        assertEquals("world", queue.consumeWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals('h', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('l', queue.consume());
        assertEquals(' ', queue.consume());
        assertEquals('w', queue.consume());
        assertEquals('o', queue.consume());
        assertEquals('r', queue.consume());
        assertEquals('l', queue.consume());
        assertEquals('d', queue.consume());
        assertNull(queue.consume());
    }

    @Test
    public void testConsumeAny() {
        TokenQueue queue = new TokenQueue("12345");
        assertEquals("1", queue.consumeToAny("1", "2", "3", "4", "5"));
        assertEquals("5", queue.consumeToAny("1", "2", "3", "4", "5"));
        assertNull(queue.consumeToAny("6"));
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo", queue.consumeToIgnoreCase("foo"));
        assertEquals("<bar>", queue.consumeToIgnoreCase("<bar>"));
        assertEquals("baz", queue.consumeToIgnoreCase("baz"));
        assertNull(queue.consumeToIgnoreCase("quux"));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(foo (bar (baz)))");
        assertEquals("foo (bar (baz)", queue.chompBalanced('(', ')'));

        queue = new TokenQueue("(foo (bar (baz)))");
        assertEquals("foo", queue.chompBalanced('(', 'f'));
        assertEquals("(bar (baz)", queue.toString());

        queue = new TokenQueue("(foo (bar (baz)))");
        assertEquals("(bar (baz)", queue.chompBalanced('(', 'b'));
        assertEquals("baz)", queue.toString());

        queue = new TokenQueue("(foo (bar (baz)))");
        assertEquals("baz)", queue.chompBalanced(')', 'z'));
        assertEquals(")", queue.toString());

        queue = new TokenQueue("(foo (bar (baz)))");
        assertEquals(")", queue.chompBalanced(')', ')'));
        assertEquals("", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("foo\\bar\\baz", TokenQueue.unescape("foo\\\\bar\\\\baz"));
        assertEquals("foo\\bar\\baz", TokenQueue.unescape("foo\\bar\\baz"));
        assertEquals("foo\\bar\\baz", TokenQueue.unescape("foo\\bar\\baz"));
    }
}