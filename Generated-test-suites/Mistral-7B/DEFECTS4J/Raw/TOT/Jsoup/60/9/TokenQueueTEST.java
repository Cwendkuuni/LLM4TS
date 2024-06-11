package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TokenQueueTest class.
 */
public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
        assertFalse(queue.matches("abc"));
        assertFalse(queue.matchesCS("ABC"));
        assertFalse(queue.matchesAny("ab", "cd"));
        assertFalse(queue.matchesAny('x', 'y'));
        assertFalse(queue.matchesStartTag());
        assertFalse(queue.matchChomp("abc"));
        assertFalse(queue.matchesWhitespace());
        assertFalse(queue.matchesWord());
        assertFalse(queue.advance());
        assertEquals(0, queue.consume());
        assertFalse(queue.consume("abc"));
        assertEquals("", queue.consumeTo(""));
        assertEquals("", queue.consumeToIgnoreCase(""));
        assertEquals("", queue.consumeToAny(""));
        assertEquals("", queue.chompTo(""));
        assertEquals("", queue.chompToIgnoreCase(""));
        assertEquals("", queue.chompBalanced('(', ')'));
        assertEquals("", queue.unescape(""));
        assertFalse(queue.consumeWhitespace());
        assertEquals("", queue.consumeWord());
        assertEquals("", queue.consumeTagName());
        assertEquals("", queue.consumeElementSelector());
        assertEquals("", queue.consumeCssIdentifier());
        assertEquals("", queue.consumeAttributeKey());
        assertEquals("", queue.remainder());
        assertEquals("", queue.toString());
    }

    @Test
    public void testBasicQueue() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertFalse(queue.isEmpty());
        assertEquals('a', queue.peek());
        assertFalse(queue.matches("xyz"));
        assertTrue(queue.matches("abc"));
        assertTrue(queue.matchesCS("ABC"));
        assertTrue(queue.matchesAny("ab", "cd"));
        assertTrue(queue.matchesAny('a', 'e'));
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchChomp("xyz"));
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWord());
        assertTrue(queue.advance());
        assertEquals('b', queue.consume());
        assertTrue(queue.consumes("abc", "abc"));
        assertEquals("defg", queue.consumeTo(""));
        assertEquals("defg", queue.consumeToIgnoreCase(""));
        assertEquals("defg", queue.consumeToAny(""));
        assertEquals("abc", queue.chompTo("abc"));
        assertEquals("defg", queue.chompToIgnoreCase("defg"));
        assertEquals("abc", queue.chompBalanced('(', ')'));
        assertEquals("a\\bcdefg", queue.unescape("a\\bcdefg"));
        assertTrue(queue.consumeWhitespace());
        assertEquals("def", queue.consumeWord());
        assertEquals("tagName", queue.consumeTagName());
        assertEquals("tagName|class", queue.consumeElementSelector());
        assertEquals("identifier", queue.consumeCssIdentifier());
        assertEquals("attributeKey", queue.consumeAttributeKey());
        assertEquals("g", queue.remainder().charAt(0));
        assertEquals("g", queue.remainder().substring(0, 1));
        assertEquals("g", queue.toString().substring(0, 1));
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidConsume() {
        TokenQueue queue = new TokenQueue("abcdefg");
        queue.consume("xyz");
    }

    @Test
    public void testConsumeToMatch() {
        TokenQueue queue = new TokenQueue("abcdefg<tagName attr=\"value\" id=\"id\">content</tagName>");
        assertEquals("tagName", queue.consumeTo("tagName").trim());
        assertEquals("attr=\"value\" id=\"id\">", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdefg<tagName attr=\"value\" id=\"id\">content<script>alert('xss')</script></tagName>");
        assertEquals("script", queue.consumeToAny("script", "tagName").trim());
        assertEquals("alert('xss')</tagName>", queue.toString());
    }
}