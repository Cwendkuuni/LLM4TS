package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link TokenQueue} class.
 */
public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
        assertEquals("", queue.toString());
    }

    @Test
    public void testAddFirstChar() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst('x');
        assertEquals('x', queue.peek());
        assertEquals("xabc", queue.toString());
    }

    @Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst("xyz");
        assertEquals('x', queue.peek());
        assertEquals("xyzabc", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("cd"));
        assertFalse(queue.matches("xy"));
        assertTrue(queue.matches("ef"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCDEFG");
        assertTrue(queue.matchesCS("AB"));
        assertTrue(queue.matchesCS("CD"));
        assertFalse(queue.matchesCS("XY"));
        assertTrue(queue.matchesCS("EF"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matchesAny("ab", "cd"));
        assertFalse(queue.matchesAny("xy", "pq"));
        assertTrue(queue.matchesAny("ef", "ef"));
        assertTrue(queue.matchesAny("g", "g"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matchesAny('a', 'b', 'c'));
        assertFalse(queue.matchesAny('x', 'y', 'z'));
        assertTrue(queue.matchesAny('f', 'f'));
        assertTrue(queue.matchesAny('g', 'g'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchChompInvalidSeq() {
        TokenQueue queue = new TokenQueue("abcdefg");
        queue.matchChomp("xyz");
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdefg");
        queue.matchChomp("ab");
        assertEquals("cdefg", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" abc ");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcdefg");
        queue.advance();
        assertEquals('b', queue.peek());
        queue.advance();
        assertEquals('c', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals('d', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('f', queue.consume());
        assertEquals('g', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeSeque() {
        TokenQueue queue = new TokenQueue("abcd");
        assertEquals("abcd", queue.consumeTo("cd"));
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeSequeIgnoreCase() {
        TokenQueue queue = new TokenQueue("ABCDabcd");
        assertEquals("ABCD", queue.consumeToIgnoreCase("CD"));
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeAny() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals("bcdefg", queue.consumeToAny("ab", "cd"));
        assertEquals("g", queue.consume().toString());
    }

    @Test
    public void testConsumeBalanced() {
        TokenQueue queue = new TokenQueue("((())())");
        assertEquals("((())", queue.chompBalanced('(', ')'));
        assertEquals("()", queue.toString());
    }

    @Test
    public void testUnescape() {
        String escaped = "\\\\\\b\\\\\\n\\\\\\r\\\\\\\\\\\\";
        String unescaped = TokenQueue.unescape(escaped);
        assertEquals("\b\n\r\\", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" abc   def ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertEquals("def", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals("abc", queue.consumeWord());
        assertEquals("defg", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>My Page</title>");
        assertEquals("html", queue.consumeTagName());
        assertEquals("<head>", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#id1.class1.class2");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("#id1.class1.class2", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("id1.class1.class2");
        assertEquals("id1", queue.consumeCssIdentifier());
        assertEquals(".class1.class2", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"class1\" id=\"id1\"");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("=\"class1\" id=\"id1\"", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdefg");
        String remainder = queue.remainder();
        assertEquals("g", queue.consume().toString());
        assertEquals(remainder, queue.toString());
    }
}