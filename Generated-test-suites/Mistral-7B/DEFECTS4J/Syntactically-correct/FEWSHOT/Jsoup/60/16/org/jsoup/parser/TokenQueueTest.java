package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue(" ");
        assertTrue(queue.isEmpty());
        
        queue = new TokenQueue("abc");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());
        
        queue = new TokenQueue("123");
        assertEquals('1', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        assertEquals("a", queue.toString());
        
        queue.addFirst("ab");
        assertEquals("ab", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matches("foo"));
        assertTrue(queue.matches("bar"));
        assertFalse(queue.matches("baz"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("FOO<BAR>BAZ");
        assertTrue(queue.matchesCS("FOO"));
        assertTrue(queue.matchesCS("BAR"));
        assertFalse(queue.matchesCS("BAZ"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchesAny("foo", "bar"));
        assertFalse(queue.matchesAny("baz", "qux"));
        
        queue = new TokenQueue("1 2 3");
        assertTrue(queue.matchesAny('1', '2', '3'));
        assertFalse(queue.matchesAny('4', '5'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>");
        assertTrue(queue.matchesStartTag());
        
        queue = new TokenQueue("abc<def>");
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchChomp("foo"));
        
        queue = new TokenQueue("123<456>");
        assertTrue(queue.matchChomp("123"));
        
        queue = new TokenQueue("\"foo<bar>baz\"");
        assertTrue(queue.matchChomp("\""));
        
        queue = new TokenQueue("\"foo<bar>baz\"");
        assertFalse(queue.matchChomp("qux"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" foo ");
        assertTrue(queue.matchesWhitespace());
        
        queue = new TokenQueue("abc");
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchesWord());
        
        queue = new TokenQueue("123<456>");
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        assertEquals('b', queue.peek());
        
        queue.advance();
        assertEquals('c', queue.peek());
        
        queue.advance();
        assertNull(queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo", queue.consume(3));
        assertEquals("barbaz", queue.toString());
        
        queue = new TokenQueue("123<456>");
        assertEquals("123", queue.consume(3));
        assertEquals("456", queue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("bar", queue.consumeTo("bar").toString());
        assertEquals("baz", queue.toString());
        
        queue = new TokenQueue("123<456>");
        assertEquals("123", queue.consumeTo("123").toString());
        assertEquals("456", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("Foo<BAR>BAZ");
        assertEquals("BAR", queue.consumeToIgnoreCase("bar").toString());
        assertEquals("BAZ", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo", queue.consumeToAny("foo", "bar").toString());
        assertEquals("baz", queue.toString());
        
        queue = new TokenQueue("1 2 3");
        assertEquals("1", queue.consumeToAny('1', '2').toString());
        assertEquals("2 3", queue.toString());
    }

    @Test
    public void testChompTo() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("bar", queue.chompTo("bar").toString());
        assertEquals("baz", queue.toString());
        
        queue = new TokenQueue("123<456>");
        assertEquals("123", queue.chompTo("123").toString());
        assertEquals("456", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(foo (bar) baz) qux");
        assertEquals("foo (bar) baz", queue.chompBalanced('(', ')').toString());
        assertEquals("qux", queue.toString());
        
        queue = new TokenQueue("(foo (bar) baz) qux");
        assertEquals("foo (bar) baz", queue.chompBalanced('(', ')').toString());
        assertEquals("qux", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("\\foo\\bar\\baz", TokenQueue.unescape("\\\\foo\\\\bar\\\\baz"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" foo ");
        assertTrue(queue.consumeWhitespace());
        
        queue = new TokenQueue("abc");
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo", queue.consumeWord().toString());
        
        queue = new TokenQueue("123<456>");
        assertNull(queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>");
        assertEquals("html", queue.consumeTagName().toString());
        
        queue = new TokenQueue("abc<foo:bar>def");
        assertEquals("foo", queue.consumeTagName().toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("<html><head><link rel=\"stylesheet\" href=\"styles.css\"/>");
        assertEquals("link", queue.consumeElementSelector().toString());
        
        queue = new TokenQueue("<html><head><div id=\"content\" class=\"highlight\"/>");
        assertEquals("div", queue.consumeElementSelector().toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("p.class:hover");
        assertEquals("class", queue.consumeCssIdentifier().toString());
        
        queue = new TokenQueue("p#id:hover");
        assertEquals("id", queue.consumeCssIdentifier().toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("<input type=\"text\" name=\"username\" value=\"john\"/>");
        assertEquals("name", queue.consumeAttributeKey().toString());
        
        queue = new TokenQueue("<input type=\"text\" id=\"username\" value=\"john\"/>");
        assertEquals("id", queue.consumeAttributeKey().toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("baz", queue.remainder().toString());
        
        queue = new TokenQueue("123<456>");
        assertEquals("456", queue.remainder().toString());
    }
}