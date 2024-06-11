package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testNewInstance() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abcdef", queue.queue);
        assertEquals(0, queue.pos);
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
        queue.addFirst("a");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.peek());

        queue.advance();
        assertEquals('b', queue.peek());

        queue.addFirst("xyz");
        assertEquals('x', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst('x');
        assertEquals("xabc", queue.queue);
        assertEquals('x', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("123abc");
        assertTrue(queue.matches("1"));
        assertTrue(queue.matches("2"));
        assertTrue(queue.matches("3"));
        assertTrue(queue.matches("a"));
        assertTrue(queue.matches("b"));
        assertFalse(queue.matches("c"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCabc");
        assertTrue(queue.matchesCS("A"));
        assertTrue(queue.matchesCS("B"));
        assertTrue(queue.matchesCS("C"));
        assertTrue(queue.matchesCS("a"));
        assertFalse(queue.matchesCS("b"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("123abc");
        assertTrue(queue.matchesAny("1", "2", "3"));
        assertTrue(queue.matchesAny("1", "a"));
        assertTrue(queue.matchesAny("3", "b"));
        assertFalse(queue.matchesAny("4"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny(new char[]{'a', 'b'}));
        assertFalse(queue.matchesAny(new char[]{'c', 'd'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<tag attr=\"value\">");
        assertTrue(queue.matchesStartTag());

        queue.advance();
        assertEquals('<', queue.peek());
        assertTrue(queue.matchesStartTag());

        queue.advance();
        assertEquals('t', queue.peek());
        assertFalse(queue.matchesStartTag());

        queue.addFirst("<");
        assertTrue(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("123abc");
        queue.matchChomp("1");
        assertEquals("23abc", queue.queue);
        assertEquals('2', queue.peek());

        queue.matchChomp("a");
        assertEquals("23bc", queue.queue);
        assertEquals('2', queue.peek());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" abc  ");
        assertTrue(queue.matchesWhitespace());
        queue.advance();
        assertTrue(queue.matchesWhitespace());
        queue.advance();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesWord());
        queue.advance();
        assertTrue(queue.matchesWord());
        queue.advance();
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.advance();
        assertEquals('b', queue.peek());

        queue.advance();
        assertEquals('c', queue.peek());

        queue.addFirst("xyz");
        queue.advance();
        assertEquals('x', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcd");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals('d', queue.consume());
        assertNull(queue.consume());
    }

    @Test
    public void testConsumeCS() {
        TokenQueue queue = new TokenQueue("ABCDabc");
        assertEquals('A', queue.consumeCS());
        assertEquals('B', queue.consumeCS());
        assertEquals('C', queue.consumeCS());
        assertEquals('a', queue.consumeCS());
        assertNull(queue.consumeCS());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeMismatch() {
        TokenQueue queue = new TokenQueue("123");
        queue.consume("abc");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdefghijk");
        assertEquals("abc", queue.consumeTo("abc"));
        assertEquals("def", queue.consumeTo("def"));
        assertEquals("ijk", queue.consumeTo("ijk"));
        assertEquals("", queue.consumeTo("xyz"));

        queue.addFirst("xyz");
        assertEquals("xyz", queue.consumeTo("xyz"));
        assertEquals("", queue.consumeTo("xyz"));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("ABCDEFGHIJK");
        assertEquals("ABC", queue.consumeToIgnoreCase("AbC"));
        assertEquals("DEF", queue.consumeToIgnoreCase("Def"));
        assertEquals("IJK", queue.consumeToIgnoreCase("Ijk"));
        assertEquals("", queue.consumeToIgnoreCase("XYZ"));

        queue.addFirst("XYZ");
        assertEquals("XYZ", queue.consumeToIgnoreCase("XYZ"));
        assertEquals("", queue.consumeToIgnoreCase("XYZ"));
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("123abc");
        assertEquals("123", queue.consumeToAny("1", "2", "3"));
        assertEquals("abc", queue.consumeToAny("a", "b", "c"));
        assertEquals("", queue.consumeToAny("1", "m", "c"));
    }

    @Test
    public void testChompTo() {
        TokenQueue queue = new TokenQueue("<tag attr=\"value\">content</tag>");
        assertEquals("tag attr=\"value\"", queue.chompTo("tag"));
        assertEquals("attr=\"value\"", queue.chompTo("attr"));
        assertEquals("value", queue.chompTo("value"));
        assertEquals("content", queue.chompTo("content"));
        assertEquals("", queue.chompTo("nonexistent"));
    }

    @Test
    public void testChompToIgnoreCase() {
        TokenQueue queue = new TokenQueue("<TAG attr=\"VALUE\">content</TAG>");
        assertEquals("TAG attr=\"VALUE\"", queue.chompToIgnoreCase("tag"));
        assertEquals("attr=\"VALUE\"", queue.chompToIgnoreCase("attr"));
        assertEquals("VALUE", queue.chompToIgnoreCase("value"));
        assertEquals("content", queue.chompToIgnoreCase("content"));
        assertEquals("", queue.chompToIgnoreCase("NONEEXISTENT"));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(abc (def) ghi) jkl");
        assertEquals("abc (def) ghi", queue.chompBalanced('(', ')'));
        assertEquals("abc (def)", queue.chompBalanced('(', ')'));
        assertEquals("abc def", queue.chompBalanced('(', ')'));
        assertEquals("(def)", queue.chompBalanced('(', ')'));
        assertEquals("", queue.chompBalanced('(', ')'));

        queue.addFirst("(");
        assertEquals("(abc (def) ghi", queue.chompBalanced('(', ')'));
        assertEquals("abc (def) ghi", queue.chompBalanced('(', ')'));
        assertEquals("", queue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        assertEquals("abc\\def\\ghi", TokenQueue.unescape("abc\\def\\ghi"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" abc  ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("a", queue.consumeWord());
        assertEquals("b", queue.consumeWord());
        assertEquals("c", queue.consumeWord());
        assertEquals("d", queue.consumeWord());
        assertEquals("e", queue.consumeWord());
        assertEquals("", queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<tag attr=\"value\">");
        assertEquals("tag", queue.consumeTagName());
        assertEquals("tag", queue.consumeTagName());
        assertEquals("", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#id.class:hover");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("id", queue.consumeElementSelector());
        assertEquals("class", queue.consumeElementSelector());
        assertEquals(":hover", queue.consumeElementSelector());
        assertEquals("", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("#id.class1.class2");
        assertEquals("id", queue.consumeCssIdentifier());
        assertEquals("class1", queue.consumeCssIdentifier());
        assertEquals("class2", queue.consumeCssIdentifier());
        assertEquals("", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("name=\"value\"");
        assertEquals("name", queue.consumeAttributeKey());
        assertEquals("", queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.remainder());
        assertEquals("", queue.remainder());
    }
}