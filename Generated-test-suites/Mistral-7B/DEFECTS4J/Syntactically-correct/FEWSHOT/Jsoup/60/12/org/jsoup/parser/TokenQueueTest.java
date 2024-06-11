package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testNewInstance() {
        String data = "abcdef";
        TokenQueue tq = new TokenQueue(data);
        assertEquals(data, tq.queue);
        assertEquals(0, tq.pos);
        assertEquals(5, tq.remainingLength());
    }

    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.isEmpty());
        data = "abc";
        tq = new TokenQueue(data);
        assertFalse(tq.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "abcdef";
        TokenQueue tq = new TokenQueue(data);
        assertEquals('a', tq.peek());
        assertEquals(5, tq.remainingLength());
    }

    @Test
    public void testAddFirst() {
        String data = "abc";
        TokenQueue tq = new TokenQueue(data);
        tq.addFirst('Z');
        assertEquals('Z', tq.peek());
        assertEquals(6, tq.remainingLength());
    }

    @Test
    public void testMatches() {
        String data = "abcde";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.matches("a"));
        assertTrue(tq.matches("ab"));
        assertTrue(tq.matches("bcde"));
        assertFalse(tq.matches("xyz"));
        assertFalse(tq.matches(""));
    }

    @Test
    public void testMatchesCS() {
        String data = "AbCdE";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.matchesCS("A"));
        assertTrue(tq.matchesCS("Ab"));
        assertTrue(tq.matchesCS("bcde"));
        assertFalse(tq.matchesCS("xyz"));
        assertFalse(tq.matchesCS(""));
    }

    @Test
    public void testMatchesAny() {
        String data = "123abcd";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.matchesAny("1", "2", "3", "a", "b", "c", "d"));
        assertFalse(tq.matchesAny("e"));
        assertFalse(tq.matchesAny("xyz"));
    }

    @Test
    public void testMatchesAnyChar() {
        String data = "abcd";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.matchesAny('a', 'b', 'c', 'd'));
        assertFalse(tq.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<x>";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.matchesStartTag());
        assertFalse(tq.matchesStartTag()); // consume first character
    }

    @Test
    public void testMatchChomp() {
        String data = "abcdef";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.matchChomp("ab"));
        assertEquals(3, tq.remainingLength());
        assertFalse(tq.matchChomp("xyz"));
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " \t\n\rabcd";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.matchesWhitespace());
        assertTrue(tq.matchesWhitespace());
        assertTrue(tq.matchesWhitespace());
        assertTrue(tq.matchesWhitespace());
        assertFalse(tq.matchesWhitespace());
        assertEquals(4, tq.remainingLength());
    }

    @Test
    public void testMatchesWord() {
        String data = "abcd123ef";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.matchesWord());
        assertTrue(tq.matchesWord());
        assertTrue(tq.matchesWord());
        assertTrue(tq.matchesWord());
        assertTrue(tq.matchesWord());
        assertFalse(tq.matchesWord());
        assertEquals(1, tq.remainingLength());
    }

    @Test
    public void testAdvance() {
        String data = "abcd";
        TokenQueue tq = new TokenQueue(data);
        tq.advance();
        assertEquals('b', tq.peek());
        tq.advance();
        assertEquals('c', tq.peek());
    }

    @Test
    public void testConsume() {
        String data = "abcdef";
        TokenQueue tq = new TokenQueue(data);
        assertEquals('a', tq.consume());
        assertEquals('b', tq.consume());
        assertEquals('c', tq.consume());
        assertEquals('d', tq.consume());
        assertEquals('e', tq.consume());
        assertEquals('f', tq.consume());
        assertTrue(tq.isEmpty());
    }

    @Test
    public void testConsumeString() {
        String data = "abcd";
        TokenQueue tq = new TokenQueue(data);
        String expected = "ab";
        tq.consume(expected);
        assertEquals(2, tq.remainingLength());
        assertEquals("cd", tq.queue.substring(tq.pos));
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeMismatch() {
        String data = "abcd";
        TokenQueue tq = new TokenQueue(data);
        tq.consume("ef");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeTruncated() {
        String data = "abcd";
        TokenQueue tq = new TokenQueue(data);
        tq.consume("ab");
        tq.consume("ab");
        tq.consume("cd");
        tq.consume("ef"); // should be IllegalStateException
    }

    @Test
    public void testConsumeTo() {
        String data = "<html><head><title>Test</title></head><body>";
        TokenQueue tq = new TokenQueue(data);
        String expected = "<html><head>";
        String result = tq.consumeTo(expected);
        assertEquals(expected, result);
        assertEquals("</title></head><body>", tq.queue.substring(tq.pos));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "<HTML><HEAD><TITLE>Test</TITLE></HEAD><BODY>";
        TokenQueue tq = new TokenQueue(data);
        String expected = "<html><head>";
        String result = tq.consumeToIgnoreCase(expected);
        assertEquals(expected, result);
        assertEquals("</title></head><BODY>", tq.queue.substring(tq.pos));
    }

    @Test
    public void testConsumeToAny() {
        String data = "abcdefghijk";
        TokenQueue tq = new TokenQueue(data);
        String result = tq.consumeToAny("a", "b", "c");
        assertEquals("abc", result);
        assertEquals("defghijk", tq.queue.substring(tq.pos));
    }

    @Test
    public void testChompTo() {
        String data = "<html><head><title>Test</title></head><body>";
        TokenQueue tq = new TokenQueue(data);
        String expected = "<html><head>";
        String result = tq.chompTo(expected);
        assertEquals(expected, result);
        assertEquals("</title></head><body>", tq.queue.substring(tq.pos));
    }

    @Test
    public void testChompToIgnoreCase() {
        String data = "<HTML><HEAD><TITLE>Test</TITLE></HEAD><BODY>";
        TokenQueue tq = new TokenQueue(data);
        String expected = "<html><head>";
        String result = tq.chompToIgnoreCase(expected);
        assertEquals(expected, result);
        assertEquals("</title></head><BODY>", tq.queue.substring(tq.pos));
    }

    @Test
    public void testChompBalanced() {
        String data = "(abc(def)ghi)jkl";
        TokenQueue tq = new TokenQueue(data);
        String result = tq.chompBalanced('(', ')');
        assertEquals("abc(def)ghi", result);
        assertEquals("jkl", tq.queue.substring(tq.pos));
    }

    @Test
    public void testUnescape() {
        String data = "\\(ab\\)cd\\ef\\";
        String result = TokenQueue.unescape(data);
        assertEquals("(ab)cdef", result);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = "   abc   def";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.consumeWhitespace());
        assertTrue(tq.consumeWhitespace());
        assertTrue(tq.consumeWhitespace());
        assertTrue(tq.consumeWhitespace());
        assertTrue(tq.consumeWhitespace());
        assertEquals("abc", tq.queue.substring(tq.pos));
    }

    @Test
    public void testConsumeWord() {
        String data = "abcd_ef_123";
        TokenQueue tq = new TokenQueue(data);
        String result = tq.consumeWord();
        assertEquals("abcd", result);
        assertEquals("ef_123", tq.queue.substring(tq.pos));
    }

    @Test
    public void testConsumeTagName() {
        String data = "<div id='foo'>content</div>";
        TokenQueue tq = new TokenQueue(data);
        String result = tq.consumeTagName();
        assertEquals("div", result);
        assertEquals(" id='foo'>content", tq.queue.substring(tq.pos));
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "div.class:hover";
        TokenQueue tq = new TokenQueue(data);
        String result = tq.consumeElementSelector();
        assertEquals("div.class", result);
        assertEquals(":hover", tq.queue.substring(tq.pos));
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = ".class1 .class2 #id1";
        TokenQueue tq = new TokenQueue(data);
        String result = tq.consumeCssIdentifier();
        assertEquals(".class1", result);
        assertEquals(".class2 #id1", tq.queue.substring(tq.pos));
        result = tq.consumeCssIdentifier();
        assertEquals(".class2", result);
        assertEquals("#id1", tq.queue.substring(tq.pos));
        result = tq.consumeCssIdentifier();
        assertEquals("#id1", result);
        assertTrue(tq.isEmpty());
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "class='value' id='id'";
        TokenQueue tq = new TokenQueue(data);
        String result = tq.consumeAttributeKey();
        assertEquals("class", result);
        assertEquals(" id='id'", tq.queue.substring(tq.pos));
        result = tq.consumeAttributeKey();
        assertEquals("id", result);
        assertTrue(tq.isEmpty());
    }

    @Test
    public void testRemainder() {
        String data = "abcdef";
        TokenQueue tq = new TokenQueue(data);
        String result = tq.remainder();
        assertEquals("def", result);
        assertTrue(tq.isEmpty());
    }

    @Test
    public void testToString() {
        String data = "abcdef";
        TokenQueue tq = new TokenQueue(data);
        assertEquals("def", tq.toString());
        assertTrue(tq.isEmpty());
    }
}