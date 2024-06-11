package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue.addFirst("a");
        assertFalse(queue.isEmpty());

        queue.consume();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());
        queue.advance();
        assertEquals('b', queue.peek());
        queue.consume();
        assertEquals('c', queue.peek());
        queue.consume();
        assertNull(queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst("x");
        assertEquals("xabc", queue.toString());

        queue.addFirst("y");
        assertEquals("yxabc", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matches("a"));
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("b"));
        assertFalse(queue.matches("c"));
        assertFalse(queue.matches("d"));

        queue.advance();
        assertTrue(queue.matches("b"));
        assertFalse(queue.matches("a"));

        queue.consume();
        assertTrue(queue.matches("c"));
        assertFalse(queue.matches("b"));

        queue.consume();
        assertNull(queue.matches("c"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABC");
        assertTrue(queue.matchesCS("A"));
        assertTrue(queue.matchesCS("Ab"));
        assertFalse(queue.matchesCS("B"));
        assertFalse(queue.matchesCS("C"));
        assertFalse(queue.matchesCS("d"));

        queue.advance();
        assertTrue(queue.matchesCS("B"));
        assertFalse(queue.matchesCS("A"));

        queue.consume();
        assertTrue(queue.matchesCS("C"));
        assertFalse(queue.matchesCS("B"));

        queue.consume();
        assertNull(queue.matchesCS("C"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny("a", "b", "c"));
        assertTrue(queue.matchesAny("ab", "bc", "ac"));
        assertTrue(queue.matchesAny("abc"));
        assertFalse(queue.matchesAny("d"));
        assertFalse(queue.matchesAny("def"));

        queue.advance();
        assertTrue(queue.matchesAny("b", "c"));
        assertFalse(queue.matchesAny("a"));

        queue.consume();
        assertTrue(queue.matchesAny("c"));
        assertFalse(queue.matchesAny("b"));

        queue.consume();
        assertNull(queue.matchesAny("c"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny(new char[]{'a', 'b', 'c'}));
        assertTrue(queue.matchesAny(new char[]{'a', 'b', 'd'}));
        assertFalse(queue.matchesAny(new char[]{'e'}));

        queue.advance();
        assertTrue(queue.matchesAny(new char[]{'b', 'c'}));
        assertFalse(queue.matchesAny(new char[]{'a'}));

        queue.consume();
        assertTrue(queue.matchesAny(new char[]{'c'}));
        assertFalse(queue.matchesAny(new char[]{'b'}));

        queue.consume();
        assertNull(queue.matchesAny(new char[]{'c'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<ab>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
        queue.advance();
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchChomp("ab"));
        assertEquals("cdef", queue.toString());

        assertTrue(queue.matchChomp("cd"));
        assertEquals("ef", queue.toString());

        assertTrue(queue.matchChomp("ef"));
        assertEquals("", queue.toString());

        assertFalse(queue.matchChomp("xyz"));
        assertEquals("ef", queue.toString());

        queue.consume();
        assertTrue(queue.matchChomp("ef"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \tab\t ");
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        queue.consumeWhitespace();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());

        queue.consumeWord();
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        assertEquals("b", queue.peek());
        queue.consume();
        assertEquals("c", queue.peek());
        queue.consume();
        assertNull(queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consume();
        assertEquals("bc", queue.toString());
        queue.consume();
        assertEquals("c", queue.toString());
        queue.consume();
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeTo("ab"));
        assertEquals("def", queue.toString());

        assertEquals("cdef", queue.consumeTo("cd"));
        assertEquals("", queue.toString());

        assertEquals("ef", queue.consumeTo("ef"));
        assertEquals("", queue.toString());

        assertEquals("def", queue.consumeTo("xyz"));
        assertEquals("def", queue.toString());

        queue.consume();
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("AbCdEf");
        assertEquals("AbC", queue.consumeToIgnoreCase("Ab"));
        assertEquals("dEf", queue.toString());

        assertEquals("cD", queue.consumeToIgnoreCase("CD"));
        assertEquals("Ef", queue.toString());

        assertEquals("ef", queue.consumeToIgnoreCase("ef"));
        assertEquals("", queue.toString());

        assertEquals("dEf", queue.consumeToIgnoreCase("xyZ"));
        assertEquals("dEf", queue.toString());

        queue.consume();
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeToAny("ab", "cd"));
        assertEquals("def", queue.toString());

        assertEquals("bcd", queue.consumeToAny("cd", "ef"));
        assertEquals("ef", queue.toString());

        assertEquals("ef", queue.consumeToAny("ef", "xyz"));
        assertEquals("", queue.toString());

        queue.consume();
        assertEquals("", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(a(b)c)d");
        assertEquals("a(b)c", queue.chompBalanced('(', ')'));
        assertEquals("d", queue.toString());

        assertEquals("(a(b)c)", queue.chompBalanced(')', '('));
        assertEquals("", queue.toString());

        assertEquals("d", queue.chompBalanced('(', '('));
        assertEquals("", queue.toString());

        assertEquals("", queue.chompBalanced(')', ')'));
        assertEquals("", queue.toString());

        assertEquals("", queue.chompBalanced('x', 'y'));
        assertEquals("", queue.toString());

        queue.consume();
        assertEquals("", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("", TokenQueue.unescape(""));
        assertEquals("abc", TokenQueue.unescape("abc"));
        assertEquals("\\abc", TokenQueue.unescape("\\abc"));
        assertEquals("\\\\", TokenQueue.unescape("\\\\"));
        assertEquals("\\ab\\cd\\ef", TokenQueue.unescape("\\ab\\cd\\ef"));

        TokenQueue queue = new TokenQueue("\\abc\\def\\/ghi\\\\jkl\\(mno\\)pqr");
        assertEquals("abcdef/ghijkl(mno)pqr", queue.toString());
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" \tab\t ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertEquals("abc", queue.consumeWord());
        assertEquals("123", queue.toString());

        assertEquals("123", queue.consumeWord());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("divid id=\"class1 class2\"");
        assertEquals("div", queue.consumeTagName());
        assertEquals("id id=\"class1 class2\"", queue.toString());

        assertEquals("id", queue.consumeTagName());
        assertEquals("class1 class2", queue.toString());

        assertEquals("class1", queue.consumeTagName());
        assertEquals("class2", queue.toString());

        assertEquals("class2", queue.consumeTagName());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#id1.class1.class2 > p.class3");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("#id1.class1.class2 > p.class3", queue.toString());

        assertEquals("#id1", queue.consumeElementSelector());
        assertEquals(".class1.class2 > p.class3", queue.toString());

        assertEquals(".class1", queue.consumeElementSelector());
        assertEquals(".class2 > p.class3", queue.toString());

        assertEquals(".class2", queue.consumeElementSelector());
        assertEquals(" > p.class3", queue.toString());

        assertEquals(" > p.class3", queue.consumeElementSelector());
        assertEquals("p.class3", queue.toString());

        assertEquals("p", queue.consumeElementSelector());
        assertEquals(".class3", queue.toString());

        assertEquals(".class3", queue.consumeElementSelector());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class1 class2 id1 id2");
        assertEquals("class1", queue.consumeCssIdentifier());
        assertEquals("class2 id1 id2", queue.toString());

        assertEquals("class2", queue.consumeCssIdentifier());
        assertEquals("id1 id2", queue.toString());

        assertEquals("id1", queue.consumeCssIdentifier());
        assertEquals("id2", queue.toString());

        assertEquals("id2", queue.consumeCssIdentifier());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"class1 class2\" id=\"id1\" style=\"color: red\"");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("=\"class1 class2\" id=\"id1\" style=\"color: red\"", queue.toString());

        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("=\"id1\" style=\"color: red\"", queue.toString());

        assertEquals("style", queue.consumeAttributeKey());
        assertEquals("=\"color: red\"", queue.toString());

        assertEquals("color", queue.consumeAttributeKey());
        assertEquals(": red}", queue.toString());

        assertEquals("red", queue.consumeAttributeKey());
        assertEquals("}", queue.toString());

        assertEquals("}", queue.consumeAttributeKey());
        assertEquals("", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.remainder());

        queue.consume();
        assertEquals("ef", queue.remainder());

        queue.consume();
        assertEquals("", queue.remainder());

        queue.consumeAll();
        assertEquals("", queue.remainder());
    }
}