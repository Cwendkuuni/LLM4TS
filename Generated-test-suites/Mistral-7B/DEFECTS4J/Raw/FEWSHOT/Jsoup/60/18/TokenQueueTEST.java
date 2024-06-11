package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
        queue.addFirst("foo");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("bar");
        assertEquals('b', queue.peek());
        queue.advance();
        assertEquals('a', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("foo");
        assertEquals("foo", queue.toString());

        queue.addFirst("bar");
        assertEquals("barfoo", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("foobar");
        assertTrue(queue.matches("fo"));
        assertTrue(queue.matches("oo"));
        assertTrue(queue.matches("ba"));
        assertFalse(queue.matches("qux"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("foobar");
        assertTrue(queue.matchesCS("foo"));
        assertTrue(queue.matchesCS("bar"));
        assertFalse(queue.matchesCS("qux"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("foobar");
        assertTrue(queue.matchesAny("foo", "bar"));
        assertFalse(queue.matchesAny("qux"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("foobar");
        assertTrue(queue.matchesAny('f', 'o', 'b', 'a', 'r'));
        assertFalse(queue.matchesAny('q', 'u', 'x'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html>");
        assertTrue(queue.matchesStartTag());
        queue.advance();
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("foobar");
        assertTrue(queue.matchChomp("foo"));
        assertEquals("bar", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" foo\tbar ");
        assertTrue(queue.matchesWhitespace());
        queue.advance();
        assertTrue(queue.matchesWhitespace());
        queue.advance();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("foobar");
        assertTrue(queue.matchesWord());
        queue.advance();
        assertTrue(queue.matchesWord());
        queue.advance();
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("foobar");
        queue.advance();
        assertEquals("oo", queue.toString());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("foobar");
        assertEquals('f', queue.consume());
        assertEquals("oobar", queue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo", queue.consumeTo("foo").toString());
        assertEquals("barbaz", queue.toString());

        assertEquals("bar", queue.consumeTo("bar").toString());
        assertEquals("baz", queue.toString());

        assertEquals("baz", queue.consumeTo("z").toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("FOO<BAR>BAZ");
        assertEquals("FOO", queue.consumeToIgnoreCase("foo").toString());
        assertEquals("BARBAZ", queue.toString());

        assertEquals("BAR", queue.consumeToIgnoreCase("bar").toString());
        assertEquals("BAZ", queue.toString());

        assertEquals("BAZ", queue.consumeToIgnoreCase("z").toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo", queue.consumeToAny("foo", "bar").toString());
        assertEquals("barbaz", queue.toString());

        assertEquals("bar", queue.consumeToAny("foo", "bar").toString());
        assertEquals("baz", queue.toString());

        assertEquals("baz", queue.consumeToAny("z").toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(foo (bar) baz) qux");
        assertEquals("foo (bar) baz", queue.chompBalanced('(', ')').toString());
        assertEquals("qux", queue.toString());

        assertEquals("bar", queue.chompBalanced('(', ')').toString());
        assertEquals("(baz) qux", queue.toString());

        assertEquals("baz", queue.chompBalanced('(', ')').toString());
        assertEquals("qux", queue.toString());

        assertEquals("qux", queue.chompBalanced('(', ')').toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("\\foo\\bar\\baz", TokenQueue.unescape("\\foo\\\\bar\\\\baz\\\\"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" foo\tbar ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("foobar");
        assertEquals("foo", queue.consumeWord().toString());
        assertEquals("bar", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html> <head> <title>Foo</title> </head> <body> <p>Bar</p> </body> </html>");
        assertEquals("html", queue.consumeTagName().toString());
        queue.advance();
        assertEquals("head", queue.consumeTagName().toString());
        queue.advance();
        assertEquals("title", queue.consumeTagName().toString());
        queue.advance();
        assertEquals("p", queue.consumeTagName().toString());
        queue.advance();
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("li.foo > ul.bar > li.baz");
        assertEquals("li", queue.consumeElementSelector().toString());
        queue.advance();
        assertEquals("ul", queue.consumeElementSelector().toString());
        queue.advance();
        assertEquals("li", queue.consumeElementSelector().toString());
        queue.advance();
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color: red; background-color: #f00;");
        assertEquals("color", queue.consumeCssIdentifier().toString());
        queue.advance();
        assertEquals("background-color", queue.consumeCssIdentifier().toString());
        queue.advance();
        assertEquals("red", queue.consumeCssIdentifier().toString());
        queue.advance();
        assertEquals("#f00", queue.consumeCssIdentifier().toString());
        queue.advance();
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"foo bar baz\" id=\"qux\"");
        assertEquals("class", queue.consumeAttributeKey().toString());
        queue.advance();
        assertEquals("id", queue.consumeAttributeKey().toString());
        queue.advance();
        assertEquals("qux", queue.consumeAttributeKey().toString());
        queue.advance();
        assertEquals("", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("qux");
        assertEquals("", queue.remainder().toString());
        queue.addFirst("foo");
        assertEquals("qux", queue.remainder().toString());
    }
}