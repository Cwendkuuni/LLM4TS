package org.jsoup.parser;
import org.junit.Test;
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
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst('d');
        assertEquals("dabc", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matches("abc"));
        assertFalse(queue.matches("bcd"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesCS("a"));
        assertFalse(queue.matchesCS("A"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny("a", "b"));
        assertFalse(queue.matchesAny("d", "e"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<p>");
        assertTrue(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchChomp("ab"));
        assertEquals("c", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" ");
        assertTrue(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesWord());
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
        assertEquals("bc", queue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals("ab", queue.consumeTo("c"));
        assertEquals("c", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals("ab", queue.consumeToIgnoreCase("C"));
        assertEquals("c", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals("a", queue.consumeToAny("b", "c"));
        assertEquals("bc", queue.toString());
    }

    @Test
    public void testChompTo() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals("a", queue.chompTo("b"));
        assertEquals("c", queue.toString());
    }

    @Test
    public void testChompToIgnoreCase() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals("a", queue.chompToIgnoreCase("B"));
        assertEquals("c", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        assertEquals(" four", queue.toString());
    }

    @Test
    public void testUnescape() {
        String escaped = "\\\"";
        assertEquals("\"", TokenQueue.unescape(escaped));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("  ");
        assertTrue(queue.consumeWhitespace());
        assertEquals(" ", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertEquals("abc123", queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("a-b_c:");
        assertEquals("a-b_c:", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("a-b_c|");
        assertEquals("a-b_c|", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("a-b_c:");
        assertEquals("a-b_c:", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("a-b_c:");
        assertEquals("a-b_c:", queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals("abc", queue.remainder());
        assertTrue(queue.isEmpty());
    }
}
