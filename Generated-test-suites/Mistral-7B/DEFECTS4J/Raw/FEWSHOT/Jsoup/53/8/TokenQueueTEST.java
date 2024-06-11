package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

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
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.peek());

        queue.addFirst("XYZ");
        assertEquals('X', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("foo");
        assertEquals("foo", queue.consumeTo(null));

        queue.addFirst("XYZ");
        assertEquals("XYZ", queue.consume(null));

        queue.addFirst("123");
        assertEquals("123", queue.consume(null));
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("ef"));
        assertTrue(queue.matches("cd")); // false expected

        queue.consume("ab");
        assertFalse(queue.matches("ab"));
        assertFalse(queue.matches("cd"));
        assertTrue(queue.matches("ef"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertFalse(queue.matchesCS("Ab"));
        assertTrue(queue.matchesCS("ef"));
        assertFalse(queue.matchesCS("cd"));

        queue.consume("ab");
        assertFalse(queue.matchesCS("Ab"));
        assertFalse(queue.matchesCS("cd"));
        assertTrue(queue.matchesCS("ef"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesAny("ab", "cd")); // false expected
        assertTrue(queue.matchesAny("ef", "cd"));
        assertTrue(queue.matchesAny("ef", "ef"));

        queue.consume("ab");
        assertFalse(queue.matchesAny("ab", "cd"));
        assertTrue(queue.matchesAny("ef", "ef"));
        assertTrue(queue.matchesAny("ef", "cd"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesAny('a', 'd')); // false expected
        assertTrue(queue.matchesAny('e', 'd'));
        assertTrue(queue.matchesAny('e', 'e'));

        queue.consume("ab");
        assertFalse(queue.matchesAny('a', 'd'));
        assertTrue(queue.matchesAny('e', 'e'));
        assertTrue(queue.matchesAny('e', 'd'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>");
        assertTrue(queue.matchesStartTag());

        queue.consume("<html>");
        assertFalse(queue.matchesStartTag());

        queue.consume("<head>");
        assertFalse(queue.matchesStartTag());

        queue.consume("<title>");
        assertTrue(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchChomp("ab"));
        assertEquals("cdef", queue.toString());

        assertTrue(queue.matchChomp("ef"));
        assertEquals("c", queue.toString());

        assertFalse(queue.matchChomp("cd"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   foo   ");
        assertTrue(queue.matchesWhitespace());

        queue.consume("   ");
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("foo bar baz");
        assertTrue(queue.matchesWord());
        assertEquals("bar baz", queue.consumeWord());

        queue.consume("foo");
        assertTrue(queue.matchesWord());
        assertEquals("bar baz", queue.consumeWord());

        queue.consume("baz");
        assertFalse(queue.matchesWord());
        assertEquals("", queue.consumeWord());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeLongerThanQueue() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consume("abcdef");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeTo("def"));
        assertEquals("ef", queue.toString());

        assertEquals("abc", queue.consumeTo("abcd"));
        assertEquals("ef", queue.toString());

        assertEquals("ef", queue.consumeTo("xyz"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("AbCdEf");
        assertEquals("AbC", queue.consumeToIgnoreCase("DeF"));
        assertEquals("dEf", queue.toString());

        assertEquals("AbC", queue.consumeToIgnoreCase("AbCdEf"));
        assertEquals("", queue.toString());

        assertEquals("", queue.consumeToIgnoreCase("xyz"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeToAny("def", "xyz"));
        assertEquals("def", queue.toString());

        assertEquals("abc", queue.consumeToAny("abcdef", "xyz"));
        assertEquals("", queue.toString());

        assertEquals("", queue.consumeToAny("xyz", "xyz"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((())())");
        assertEquals("((())", queue.chompBalanced('(', ')'));
        assertEquals("()()", queue.toString());


        queue = new TokenQueue("((())())");
        assertEquals("((())", queue.chompBalanced('(', '('));
        assertEquals("()()", queue.toString());

        queue = new TokenQueue("((())())");
        assertEquals("((())", queue.chompBalanced(')', '('));
        assertEquals("()()", queue.toString());

        queue = new TokenQueue("((())())");
        assertEquals("((())", queue.chompBalanced('(', '('));
        assertEquals("()()", queue.toString());

        queue = new TokenQueue("((())())");
        assertEquals("((())", queue.chompBalanced(')', '('));
        assertEquals("()()", queue.toString());

        queue = new TokenQueue("((())())");
        assertEquals("((())", queue.chompBalanced('(', ')'));
        assertEquals("()()", queue.toString());

        queue = new TokenQueue("((())())");
        assertEquals("((())", queue.chompBalanced('(', ')'));
        assertEquals("()()", queue.toString());

        queue = new TokenQueue("((())())");
        assertEquals("((())", queue.chompBalanced('(', ')'));
        assertEquals("()()", queue.toString());

        queue = new TokenQueue("(())((()))");
        assertEquals("(())", queue.chompBalanced('(', ')'));
        assertEquals("((()))", queue.toString());

        queue = new TokenQueue("(())((()))");
        assertEquals("((()))", queue.chompBalanced('(', ')'));
        assertEquals("()()", queue.toString());

        queue = new TokenQueue("(())((()))");
        assertEquals("((()))", queue.chompBalanced(')', '('));
        assertEquals("()()", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("", TokenQueue.unescape(""));
        assertEquals("\\abc", TokenQueue.unescape("\\abc"));
        assertEquals("foo\\bar", TokenQueue.unescape("foo\\bar"));
        assertEquals("foo\\bar\\baz", TokenQueue.unescape("foo\\bar\\baz"));
        assertEquals("foo\\\\bar\\\\baz", TokenQueue.unescape("foo\\\\bar\\\\baz"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   foo   ");
        assertTrue(queue.consumeWhitespace());
        assertEquals("foo", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("foo bar baz");
        assertEquals("foo", queue.consumeWord());
        assertEquals("bar baz", queue.toString());

        assertEquals("bar", queue.consumeWord());
        assertEquals("baz", queue.toString());

        assertEquals("baz", queue.consumeWord());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("foo<tag name=bar>baz");
        assertEquals("foo", queue.consumeTagName());
        assertEquals("tag name=bar>baz", queue.toString());

        assertEquals("tag name=bar", queue.consumeTagName());
        assertEquals(">baz", queue.toString());

        assertEquals(">", queue.consumeTagName());
        assertEquals("baz", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#foo.class:hover > ul:last-child");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("#foo.class:hover > ul:last-child", queue.toString());

        assertEquals("#foo.class", queue.consumeElementSelector());
        assertEquals(":hover > ul:last-child", queue.toString());

        assertEquals(":", queue.consumeElementSelector());
        assertEquals("hover > ul:last-child", queue.toString());

        assertEquals("hover", queue.consumeTagName());
        assertEquals("> ul:last-child", queue.toString());

        assertEquals(">", queue.consumeTagName());
        assertEquals("ul:last-child", queue.toString());

        assertEquals("ul", queue.consumeTagName());
        assertEquals(":last-child", queue.toString());

        assertEquals(":", queue.consumeElementSelector());
        assertEquals("last-child", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color: #fooBarBaz;");
        assertEquals("color:", queue.consumeCssIdentifier());
        assertEquals("#fooBarBaz;", queue.toString());

        assertEquals("#fooBarBaz", queue.consumeCssIdentifier());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"foo bar baz\"");
        assertEquals("class=\"", queue.consumeAttributeKey());
        assertEquals("\">foo bar baz<", queue.toString());

        assertEquals("\">foo", queue.consumeTagName());
        assertEquals(" bar baz<", queue.toString());

        assertEquals(" bar", queue.consumeTagName());
        assertEquals(" baz<", queue.toString());

        assertEquals(" baz", queue.consumeTagName());
        assertEquals("", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.remainder());

        queue = new TokenQueue("xyz");
        assertEquals("", queue.remainder());

        queue = new TokenQueue("xyz");
        queue.consume("xyz");
        assertEquals("", queue.remainder());

        queue = new TokenQueue("abcdef");
        queue.consume("ab");
        queue.consume("cd");
        assertEquals("ef", queue.remainder());
    }
}