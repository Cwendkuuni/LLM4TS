package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

/**
 * Unit tests for {@link TokenQueue} class.
 */
public class TokenQueueTest {

    @Test
    public void testTokenQueueEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testTokenQueueNonEmpty() {
        TokenQueue queue = new TokenQueue("abc123");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc123");
        assertEquals('a', queue.peek());
        assertEquals('a', queue.consume());
        assertEquals('b', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchNotFound() {
        TokenQueue queue = new TokenQueue("abc123");
        queue.consume(3); // consume first three characters
        queue.matchChomp("xyz");
    }

    @Test
    public void testMatchSingleCharacter() {
        TokenQueue queue = new TokenQueue("abc123");
        queue.matchChomp('a');
        assertEquals(2, queue.remainingLength());
        assertEquals('b', queue.peek());
    }

    @Test
    public void testMatchSingleCharacterIgnoreCase() {
        TokenQueue queue = new TokenQueue("AbC123");
        queue.matchChomp('a');
        assertEquals(2, queue.remainingLength());
        assertEquals('B', queue.peek());
    }

    @Test
    public void testMatchMultipleCharacters() {
        TokenQueue queue = new TokenQueue("abc123");
        queue.matchChomp("ab");
        assertEquals(1, queue.remainingLength());
        assertEquals('1', queue.peek());
    }

    @Test
    public void testMatchWhitespace() {
        TokenQueue queue = new TokenQueue(" abc123 ");
        queue.consumeWhitespace();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchWord() {
        TokenQueue queue = new TokenQueue("abc123");
        queue.consumeWord();
        assertEquals('2', queue.peek());
    }

    @Test
    public void testMatchWordIgnoreCase() {
        TokenQueue queue = new TokenQueue("AbC123");
        queue.consumeWord();
        assertEquals('2', queue.peek());
    }

    @Test
    public void testMatchAnySingleCharacter() {
        TokenQueue queue = new TokenQueue("abc123");
        queue.matchesAny('a');
        assertEquals('b', queue.peek());
        queue.consume();
        queue.matchesAny('b');
        assertEquals('1', queue.peek());
    }

    @Test
    public void testMatchAnyMultipleCharacters() {
        TokenQueue queue = new TokenQueue("abc123");
        queue.matchesAny(new char[]{'a', 'b'});
        assertEquals('c', queue.peek());
    }

    @Test
    public void testMatchStartTag() {
        TokenQueue queue = new TokenQueue("<html><head></head><body></body></html>");
        assertTrue(queue.matchesStartTag());
        assertEquals('<', queue.consume());
        assertEquals('h', queue.consume());
        assertEquals('t', queue.consume());
        assertEquals('m', queue.consume());
        assertEquals('l', queue.consume());
        assertEquals('>', queue.consume());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        assertEquals("four", queue.remainder());
    }

    @Test
    public void testUnescape() {
        String escapedString = "\\/\\\\\\";
        String unescapedString = TokenQueue.unescape(escapedString);
        assertEquals("/\\\\", unescapedString);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc123   ");
        assertTrue(queue.consumeWhitespace());
        assertEquals('a', queue.peek());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertEquals("bc123", queue.consumeWord());
        assertEquals("", queue.remainder());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<p class=\"foo\"></p>");
        assertEquals("p", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#foo.bar:hover");
        assertEquals("div", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class:foo id:bar");
        assertEquals("foo", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"foo\" id=\"bar\"");
        assertEquals("class", queue.consumeAttributeKey());
    }

    @Test
    public void testConsumeRemainder() {
        TokenQueue queue = new TokenQueue("divid");
        assertEquals("divid", queue.remainder());
        queue.consumeTo("div");
        assertEquals("", queue.remainder());
    }
}