package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testNewInstance() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.isEmpty());
        assertEquals("hello world", queue.queue);
        assertEquals(0, queue.pos);
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.isEmpty());
        queue.advance();
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals('h', queue.peek());
        assertEquals('h', queue.consume());
        assertEquals('e', queue.peek());
        queue.advance();
        queue.advance();
        assertEquals('l', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.addFirst('a');
        assertEquals('a', queue.consume());
        assertEquals('h', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('l', queue.consume());
        assertEquals('o', queue.consume());
        assertEquals(' ', queue.consume());
        assertEquals('w', queue.consume());
        assertEquals('r', queue.consume());
        assertEquals('d', queue.consume());
        assertEquals(' ', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.matches("h"));
        assertTrue(queue.matches("h"));
        assertTrue(queue.matches("e"));
        assertTrue(queue.matches("l"));
        assertTrue(queue.matches("o"));
        assertTrue(queue.matches("w"));
        assertTrue(queue.matches("r"));
        assertTrue(queue.matches("d"));
        assertFalse(queue.matches("x"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Hello World");
        assertTrue(queue.matchesCS("H"));
        assertTrue(queue.matchesCS("H"));
        assertTrue(queue.matchesCS("e"));
        assertTrue(queue.matchesCS("l"));
        assertTrue(queue.matchesCS("o"));
        assertTrue(queue.matchesCS("W"));
        assertTrue(queue.matchesCS("w"));
        assertTrue(queue.matchesCS("r"));
        assertTrue(queue.matchesCS("d"));
        assertFalse(queue.matchesCS("x"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.matchesAny("h", "e"));
        assertTrue(queue.matchesAny("h", "l"));
        assertTrue(queue.matchesAny("h", "e", "l"));
        assertFalse(queue.matchesAny("x", "e"));
        assertFalse(queue.matchesAny("x", "x"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.matchesAny(new char[]{'h', 'e'}));
        assertTrue(queue.matchesAny(new char[]{'h', 'l'}));
        assertTrue(queue.matchesAny(new char[]{'h', 'e', 'l'}));
        assertFalse(queue.matchesAny(new char[]{'x', 'e'}));
        assertFalse(queue.matchesAny(new char[]{'x', 'x'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head></head><body></body></html>");
        assertTrue(queue.matchesStartTag());
        queue.advance();
        assertTrue(queue.matchesStartTag());
        queue.advance();
        queue.advance();
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("h<e>llo w<or>ld");
        assertTrue(queue.matchChomp("h"));
        assertEquals("e", queue.consume());
        assertTrue(queue.matchChomp("l"));
        assertEquals("l", queue.consume());
        assertEquals("o", queue.consume());
        assertTrue(queue.matchChomp("w"));
        assertEquals("o", queue.consume());
        assertEquals("r", queue.consume());
        assertFalse(queue.matchChomp("x"));
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" hello world ");
        assertTrue(queue.matchesWhitespace());
        assertEquals(' ', queue.consume());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.matchesWord());
        assertEquals("h", queue.consume());
        assertTrue(queue.matchesWord());
        assertEquals("e", queue.consume());
        assertTrue(queue.matchesWord());
        assertEquals("l", queue.consume());
        assertTrue(queue.matchesWord());
        assertEquals("o", queue.consume());
        assertTrue(queue.matchesWord());
        assertEquals("w", queue.consume());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.advance();
        assertEquals('e', queue.consume());
        queue.advance();
        assertEquals('l', queue.consume());
        queue.advance();
        assertEquals('o', queue.consume());
        queue.advance();
        assertEquals(' ', queue.consume());
        queue.advance();
        assertEquals('w', queue.consume());
        queue.advance();
        assertEquals('r', queue.consume());
        queue.advance();
        assertEquals('d', queue.consume());
        queue.advance();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals('h', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('l', queue.consume());
        assertEquals('o', queue.consume());
        assertEquals(' ', queue.consume());
        assertEquals('w', queue.consume());
        assertEquals('r', queue.consume());
        assertEquals('d', queue.consume());
        assertNull(queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("(<a>hello <b>world</b>");
        assertEquals("(<a>", queue.consumeTo("("));
        assertEquals("<a>", queue.consume());
        assertEquals("hello ", queue.consumeTo(" "));
        assertEquals("hello ", queue.consumeToIgnoreCase(" hello "));
        assertEquals("world", queue.consumeTo("</b>"));
        assertEquals("b>", queue.consume());
        assertEquals(">", queue.consume());
        assertNull(queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("(<a>hello <b>world</b>");
        assertEquals("(<a>", queue.consumeToAny("(", "<"));
        assertEquals("<a>", queue.consume());
        assertEquals("hello ", queue.consumeToAny(" ", " "));
        assertEquals("hello ", queue.consumeToAny(" hello ", " hello "));
        assertEquals("world", queue.consumeToAny("</b>", "</b>"));
        assertEquals("b>", queue.consume());
        assertEquals(">", queue.consume());
        assertNull(queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(<a>hello <b>world</b></a>");
        assertEquals("(<a>hello ", queue.chompBalanced('(', ')'));
        assertEquals("<a>hello ", queue.consumeTo(" "));
        assertEquals("hello ", queue.consumeToIgnoreCase(" hello "));
        assertEquals("world", queue.chompBalanced('b', 'b'));
        assertEquals("</a>", queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testUnescape() {
        String input = "\\(<a>hello\\ <b>world\\</b>\\)";
        assertEquals("(<a>hello <b>world</b>)", TokenQueue.unescape(input));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   hello world   ");
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals("h", queue.consumeWord());
        assertEquals("e", queue.consumeWord());
        assertEquals("l", queue.consumeWord());
        assertEquals("o", queue.consumeWord());
        assertEquals("w", queue.consumeWord());
        assertEquals("r", queue.consumeWord());
        assertEquals("d", queue.consumeWord());
        assertNull(queue.consumeWord());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head></head><body></body></html>");
        assertEquals("html", queue.consumeTagName());
        assertEquals("head", queue.consumeTagName());
        assertEquals("body", queue.consumeTagName());
        assertNull(queue.consumeTagName());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class p#id span.class");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("class", queue.consume());
        assertEquals("p", queue.consume());
        assertEquals("#id", queue.consume());
        assertEquals("span", queue.consumeElementSelector());
        assertEquals("class", queue.consume());
        assertNull(queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("background-color:red; color:blue; border:1px solid black; margin:1em; padding:1em;");
        assertEquals("background-color", queue.consumeCssIdentifier());
        assertEquals("color", queue.consume());
        assertEquals("border", queue.consume());
        assertEquals("margin", queue.consume());
        assertEquals("padding", queue.consume());
        assertNull(queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"class1 class2\" id=\"id1\" data-test=\"testValue\"");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("id", queue.consume());
        assertEquals("data-test", queue.consumeAttributeKey());
        assertNull(queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals(" world", queue.remainder());
        assertEquals("", queue.remainder());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals(" world", queue.toString());
        assertTrue(queue.isEmpty());
    }
}