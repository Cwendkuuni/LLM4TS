package org.jsoup.parser;

import org.junit.*;
import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("world");
        queue.addFirst('h');
        assertEquals("hello world", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("world");
        assertTrue(queue.matches("world"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Hello");
        assertTrue(queue.matchesCS("Hello"));
    }

    @Test
    public void testMatchesAny_Array() {
        TokenQueue queue = new TokenQueue("world");
        assertTrue(queue.matchesAny("hello", "world"));
    }

    @Test
    public void testMatchesAny_Char() {
        TokenQueue queue = new TokenQueue("hello");
        assertTrue(queue.matchesAny('h', 'z'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<h1>");
        assertTrue(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("hello");
        assertTrue(queue.matchChomp("hello"));
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" ");
        assertTrue(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("hello");
        assertTrue(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("hello");
        queue.advance();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("hello");
        assertEquals('h', queue.consume());
        assertEquals("ello", queue.toString());
    }

    @Test
    public void testConsume_String() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.consume("hello");
        assertEquals(" world", queue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals("hello", queue.consumeTo(" "));
        assertEquals(" ", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals("hello", queue.consumeToIgnoreCase(" "));
        assertEquals(" ", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals("hello", queue.consumeToAny(" "));
        assertEquals(" ", queue.toString());
    }

    @Test
    public void testChompTo() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals("hello", queue.chompTo(" "));
        assertEquals(" world", queue.toString());
    }

    @Test
    public void testChompToIgnoreCase() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals("hello", queue.chompToIgnoreCase(" "));
        assertEquals(" world", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        assertEquals(" four", queue.toString());
    }

    @Test
    public void testUnescape() {
        String escapedString = "\\(one \\(two\\) three\\) four";
        String unescapedString = TokenQueue.unescape(escapedString);
        assertEquals("(one (two) three) four", unescapedString);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   hello");
        assertTrue(queue.consumeWhitespace());
        assertEquals("hello", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals("hello", queue.consumeWord());
        assertEquals(" world", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("div class");
        assertEquals("div", queue.consumeTagName());
        assertEquals(" class", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div|myElem");
        assertEquals("div|myElem", queue.consumeElementSelector());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("-class_name");
        assertEquals("-class_name", queue.consumeCssIdentifier());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("data-*");
        assertEquals("data-*", queue.consumeAttributeKey());
        assertEquals("", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("hello");
        assertEquals("hello", queue.remainder());
        assertTrue(queue.isEmpty());
    }
}