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

        queue.addFirst("test");
        assertFalse(queue.isEmpty());

        queue.consume("test");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("foo<bar>");
        assertEquals('f', queue.peek());

        queue.advance();
        assertEquals('o', queue.peek());

        queue.consume("<");
        queue.consume("bar>");
        assertEquals('r', queue.peek());

        queue.consumeAll(new String[]{"bar", "baz"});
        assertEquals('a', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("test");
        assertEquals("test", queue.toString());

        queue.addFirst("foo");
        assertEquals("foostest", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("foo<bar>");
        assertTrue(queue.matches("foo"));
        assertTrue(queue.matches("<bar>"));
        assertFalse(queue.matches("baz"));

        queue.consume("<");
        queue.consume("bar>");
        assertFalse(queue.matches("<bar>"));
        assertTrue(queue.matches("baz"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Foo<BAR>");
        assertTrue(queue.matchesCS("Foo"));
        assertFalse(queue.matchesCS("foo"));
        assertTrue(queue.matchesCS("<BAR>"));
        assertFalse(queue.matchesCS("Bar"));

        queue.consume("Foo");
        queue.consume(">");
        assertTrue(queue.matchesCS("Bar"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");

        assertTrue(queue.matchesAny("f", "o"));
        assertTrue(queue.matchesAny("b", "a", "z"));
        assertFalse(queue.matchesAny("qux"));

        queue.consume("foo");
        assertFalse(queue.matchesAny("f"));
        assertTrue(queue.matchesAny("b", "a", "z"));

        queue.consumeAll(new String[]{"bar", "baz"});
        assertFalse(queue.matchesAny("f", "o"));
        assertFalse(queue.matchesAny("b", "a", "z"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");

        assertTrue(queue.matchesAny('f', 'o'));
        assertTrue(queue.matchesAny('b', 'a', 'z'));
        assertFalse(queue.matchesAny('qux'));

        queue.consume("foo");
        assertFalse(queue.matchesAny('f'));
        assertTrue(queue.matchesAny('b', 'a', 'z'));

        queue.consumeAll(new String[]{"bar", "baz"});
        assertFalse(queue.matchesAny('f', 'o'));
        assertFalse(queue.matchesAny('b', 'a', 'z'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");

        assertFalse(queue.matchesStartTag());

        queue.advance();
        assertTrue(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");

        assertFalse(queue.matchChomp("qux"));

        queue.consume("foo");
        assertFalse(queue.matchChomp("qux"));

        queue.consume("<bar>");
        assertTrue(queue.matchChomp("bar"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" foo\tbar\n\t qux ");

        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());

        queue.consume(" foo\t");
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");

        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());

        queue.consume("foo");
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");

        queue.advance();
        assertEquals('o', queue.peek());

        queue.consume("o");
        queue.advance();
        assertEquals('o', queue.peek());

        queue.consumeAll(new String[]{"bar", "baz"});
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");

        String consumed = queue.consume("foo");
        assertEquals("foo", consumed);
        assertEquals("o<bar>baz", queue.toString());

        consumed = queue.consume("<bar>");
        assertEquals("<bar>", consumed);
        assertEquals("o<baz>", queue.toString());

        consumed = queue.consumeAll(new String[]{"baz"});
        assertEquals("baz", consumed);
        assertEquals("o", queue.toString());

        consumed = queue.consume("o");
        assertEquals("o", consumed);
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");

        String consumed = queue.consumeTo("bar");
        assertEquals("foo<bar>", consumed);
        assertEquals("baz", queue.toString());

        consumed = queue.consumeToIgnoreCase("BAR");
        assertEquals("foo<bar>", consumed);
        assertEquals("baz", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");

        String consumed = queue.consumeToAny("bar", "baz");
        assertEquals("foo<bar>", consumed);
        assertEquals("baz", queue.toString());

        consumed = queue.consumeToAny("qux", "bar", "baz");
        assertEquals("foo", consumed);
        assertEquals("qux<bar>baz", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(foo (bar (baz) qux))");

        String consumed = queue.chompBalanced('(', ')');
        assertEquals("(foo (bar (baz) qux)", consumed);
        assertEquals("", queue.toString());

        consumed = queue.chompBalanced('(', '(');
        assertEquals("(foo", consumed);
        assertEquals("(bar (baz) qux)", queue.toString());

        consumed = queue.chompBalanced('(', '(');
        assertEquals("foo", consumed);
        assertEquals("(bar (baz) qux)", queue.toString());

        consumed = queue.chompBalanced('(', '{');
        assertEquals("", consumed);
        assertEquals("(foo (bar (baz) qux)", queue.toString());

        consumed = queue.chompBalanced('{', '}');
        assertEquals("", consumed);
        assertEquals("(foo (bar (baz) qux)", queue.toString());

        consumed = queue.chompBalanced('[', ']');
        assertEquals("", consumed);
        assertEquals("(foo (bar (baz) qux)", queue.toString());
    }

    @Test
    public void testUnescape() {
        String escaped = "\\(\\)foo\\<bar\\>\\baz\\\\\\";
        String unescaped = TokenQueue.unescape(escaped);
        assertEquals("(foo<bar>baz\\\\", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" foo\tbar\n\t qux ");

        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());

        queue.consume(" foo\t");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");

        String consumed = queue.consumeWord();
        assertEquals("foo", consumed);
        assertEquals("o<bar>baz", queue.toString());

        consumed = queue.consumeWord();
        assertEquals("o", consumed);
        assertEquals("<bar>baz", queue.toString());

        consumed = queue.consumeWord();
        assertEquals("o", consumed);
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");

        String consumed = queue.consumeTagName();
        assertEquals("foo", consumed);
        assertEquals("barbaz", queue.toString());

        consumed = queue.consumeTagName();
        assertEquals("bar", consumed);
        assertEquals("baz", queue.toString());

        consumed = queue.consumeTagName();
        assertEquals("baz", consumed);
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");

        String consumed = queue.consumeElementSelector();
        assertEquals("foo", consumed);
        assertEquals("barbaz", queue.toString());

        consumed = queue.consumeElementSelector();
        assertEquals("bar", consumed);
        assertEquals("baz", queue.toString());

        consumed = queue.consumeElementSelector();
        assertEquals("baz", consumed);
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");

        String consumed = queue.consumeCssIdentifier();
        assertEquals("foo", consumed);
        assertEquals("barbaz", queue.toString());

        consumed = queue.consumeCssIdentifier();
        assertEquals("bar", consumed);
        assertEquals("baz", queue.toString());

        consumed = queue.consumeCssIdentifier();
        assertEquals("baz", consumed);
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("foo<bar class=baz attr1='qux'>baz");

        String consumed = queue.consumeAttributeKey();
        assertEquals("class", consumed);
        assertEquals("baz attr1='qux'>baz", queue.toString());

        consumed = queue.consumeAttributeKey();
        assertEquals("attr1", consumed);
        assertEquals("baz", queue.toString());

        consumed = queue.consumeAttributeKey();
        assertEquals("", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");

        String remainder = queue.remainder();
        assertEquals("baz", remainder);
        assertEquals("foo<bar>", queue.toString());

        remainder = queue.remainder();
        assertEquals("", remainder);
        assertEquals("foo<bar>", queue.toString());
    }
}