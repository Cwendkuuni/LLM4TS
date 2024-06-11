package org.jsoup.parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.addFirst('z');
        assertEquals("zabc", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertTrue(tokenQueue.matches("abc"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertTrue(tokenQueue.matchesCS("abc"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertTrue(tokenQueue.matchesAny("ab", "bc"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<tag>");
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertTrue(tokenQueue.matchChomp("ab"));
        assertEquals("c", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" ");
        assertTrue(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("a");
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.advance();
        assertEquals("bc", tokenQueue.toString());
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals('a', tokenQueue.consume());
        assertEquals("bc", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("ab c");
        assertEquals("ab", tokenQueue.consumeTo(" "));
        assertEquals(" ", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("AB C");
        assertEquals("AB", tokenQueue.consumeToIgnoreCase("c"));
        assertEquals("c", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals("ab", tokenQueue.consumeToAny("c"));
        assertEquals("c", tokenQueue.toString());
    }

    @Test
    public void testChompTo() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals("ab", tokenQueue.chompTo("c"));
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testChompToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("AB C");
        assertEquals("AB", tokenQueue.chompToIgnoreCase("c"));
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
        assertEquals(" four", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        String escaped = "\\\"hello\\\"";
        String unescaped = TokenQueue.unescape(escaped);
        assertEquals("\"hello\"", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("  abc");
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals("abc", tokenQueue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("abc123");
        assertEquals("abc123", tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("html");
        assertEquals("html", tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("div|p");
        assertEquals("div|p", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("example123");
        assertEquals("example123", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("data-key");
        assertEquals("data-key", tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals("abc", tokenQueue.remainder());
        assertTrue(tokenQueue.toString().isEmpty());
    }

    @Test
    public void testToString() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals("abc", tokenQueue.toString());
    }
}