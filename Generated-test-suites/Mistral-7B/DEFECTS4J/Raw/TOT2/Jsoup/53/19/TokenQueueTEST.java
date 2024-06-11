package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for TokenQueue class.
 */
public class TokenQueueTest {

    @Test
    public void testConstructor() {
        String data = "This is a test";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("This is a test", queue.queue);
        assertEquals(0, queue.pos);
    }

    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.isEmpty());

        data = "This is a test";
        queue = new TokenQueue(data);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "This is a test";
        TokenQueue queue = new TokenQueue(data);
        assertEquals('T', queue.peek());
    }

    @Test
    public void testAddFirst() {
        String data = "This is a test";
        TokenQueue queue = new TokenQueue(data);
        queue.addFirst('X');
        assertEquals('X', queue.peek());

        queue.addFirst("hello");
        assertEquals("helloT", queue.toString());
    }

    @Test
    public void testMatches() {
        String data = "This is a test";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matches("is"));
        assertFalse(queue.matches("isn't"));
    }

    @Test
    public void testMatchesCS() {
        String data = "This is a test";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesCS("Test"));
        assertFalse(queue.matchesCS("test"));
    }

    @Test
    public void testMatchesAny() {
        String data = "This is a test";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesAny("is", "at"));
        assertFalse(queue.matchesAny("isn't", "at"));
    }

    @Test
    public void testMatchesAnyChar() {
        String data = "This is a test";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesAny(new char[]{'t', 's'}));
        assertFalse(queue.matchesAny(new char[]{'x', 's'}));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<html><head><title>Test</title></head><body>";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag()); // consume first <
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchChompWithNoMatch() {
        String data = "This is a test";
        TokenQueue queue = new TokenQueue(data);
        queue.matchChomp("notmatch");
    }

    @Test
    public void testMatchesWhitespace() {
        String data = "   This is a test   ";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace()); // consume whitespace
    }

    @Test
    public void testMatchesWord() {
        String data = "This is a test";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord()); // consume non-word
    }

    @Test
    public void testAdvance() {
        String data = "This is a test";
        TokenQueue queue = new TokenQueue(data);
        queue.advance();
        assertEquals('T', queue.peek());
    }

    @Test
    public void testConsume() {
        String data = "This is a test";
        TokenQueue queue = new TokenQueue(data);
        assertEquals('T', queue.consume());
        assertEquals('h', queue.consume());
        assertEquals('i', queue.consume());
        assertEquals('s', queue.consume());
        assertFalse(queue.isEmpty());
        assertEquals(' ', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeSequence() {
        String data = "<html><head><title>Test</title></head><body>";
        TokenQueue queue = new TokenQueue(data);
        String expected = "<html><head>";
        String actual = queue.consumeTo("head");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeSequenceIgnoreCase() {
        String data = "<html><head><title>Test</title></head><body>";
        TokenQueue queue = new TokenQueue(data);
        String expected = "<html><head>";
        String actual = queue.consumeToIgnoreCase("head");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceNoMatch() {
        String data = "This is a test";
        TokenQueue queue = new TokenQueue(data);
        queue.consumeTo("notmatch");
    }

    @Test
    public void testConsumeSequencePartialMatch() {
        String data = "This is a test";
        TokenQueue queue = new TokenQueue(data);
        String expected = "is a test";
        String actual = queue.consumeTo("test");
        assertEquals(expected, actual);
    }

    @Test
    public void testChompBalanced() {
        String data = "(one (two) three) four";
        TokenQueue queue = new TokenQueue(data);
        String expected = "one (two) three";
        String actual = queue.chompBalanced('(', ')');
        assertEquals(expected, actual);
    }

    @Test
    public void testUnescape() {
        String data = "This is a \\test\\ with \\escapes\\";
        String expected = "This is a test with escapes";
        String actual = TokenQueue.unescape(data);
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = "   This is a test   ";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace()); // consume all whitespace
    }

    @Test
    public void testConsumeWord() {
        String data = "This is a test";
        TokenQueue queue = new TokenQueue(data);
        String expected = "is a test";
        String actual = queue.consumeWord();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTagName() {
        String data = "<html><head><title>Test</title></head><body>";
        TokenQueue queue = new TokenQueue(data);
        String expected = "html";
        String actual = queue.consumeTagName();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "<html><head><title>Test</title><link rel=\"stylesheet\" href=\"style.css\"/></head><body>";
        TokenQueue queue = new TokenQueue(data);
        String expected = "link";
        String actual = queue.consumeElementSelector();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "id=\"myId\" class=\"myClass\"";
        TokenQueue queue = new TokenQueue(data);
        String expected = "myId";
        String actual = queue.consumeCssIdentifier();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "id=\"myId\" class=\"myClass\"";
        TokenQueue queue = new TokenQueue(data);
        String expected = "id";
        String actual = queue.consumeAttributeKey();
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        String data = "This is a test";
        TokenQueue queue = new TokenQueue(data);
        String expected = "test";
        String actual = queue.remainder();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String data = "This is a test";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("test", queue.toString());
    }
}