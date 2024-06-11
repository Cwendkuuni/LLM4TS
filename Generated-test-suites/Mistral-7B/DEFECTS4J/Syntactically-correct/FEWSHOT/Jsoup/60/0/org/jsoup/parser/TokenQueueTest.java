package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;

import java.util.Arrays;

public class TokenQueueTest {

    @Test
    public void testTokenQueueIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testTokenQueueIsNotEmpty() {
        TokenQueue queue = new TokenQueue("abc");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());
    }

    @Test
    public void testPeekEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertEquals('', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        assertEquals('a', queue.peek());
    }

    @Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("abc");
        assertEquals('a', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("cd"));
        assertFalse(queue.matches("xy"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("AbCdEf");
        assertTrue(queue.matchesCS("Ab"));
        assertFalse(queue.matchesCS("cd"));
        assertFalse(queue.matchesCS("xy"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdefghijkl");
        assertTrue(queue.matchesAny("bc", 'd'));
        assertFalse(queue.matchesAny("xy"));
        assertFalse(queue.matchesAny("pq"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("abcdefghijkl");
        assertTrue(queue.matchesAny(new char[]{'b', 'c'}));
        assertFalse(queue.matchesAny(new char[]{'x', 'y'}));
        assertFalse(queue.matchesAny(new char[]{'p', 'q'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abc>def");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdefghijkl<abc>def");
        String seq = "abc";
        assertTrue(queue.matchChomp(seq));
        assertEquals("defghijkl", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc123def");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
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
        assertEquals('b', queue.peek());
    }

    @Test
    public void testConsumeCS() {
        TokenQueue queue = new TokenQueue("AbCdEf");
        assertEquals('A', queue.consumeCS());
        assertEquals('B', queue.peekCS());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdefghijkl<abc>def");
        String seq = "abc";
        assertEquals("defghijkl", queue.consumeTo(seq).toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("ABCDEFghijKL<AbC>def");
        String seq = "AbC";
        assertEquals("defghijKL", queue.consumeToIgnoreCase(seq).toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdefghijkl<abc>def");
        String[] seqs = new String[]{"bcd", "xy", "abc"};
        assertEquals("defghijkl", queue.consumeToAny(seqs).toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testChompTo() {
        TokenQueue queue = new TokenQueue("abcdefghijkl<abc>def");
        String seq = "abc";
        assertEquals("defghijkl", queue.chompTo(seq).toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testChompToIgnoreCase() {
        TokenQueue queue = new TokenQueue("ABCDEFghijKL<AbC>def");
        String seq = "AbC";
        assertEquals("defghijKL", queue.chompToIgnoreCase(seq).toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(abc)def(xyz)pqr");
        assertEquals("abcdef", queue.chompBalanced('(', ')').toString());
        assertEquals("xyzpqr", queue.toString());
    }

    @Test
    public void testUnescape() {
        String in = "\\abc\\def\\\\";
        assertEquals("abcdef\\", TokenQueue.unescape(in));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc");
        assertTrue(queue.consumeWhitespace());
        assertEquals('a', queue.peek());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abc123def");
        assertEquals("abc", queue.consumeWord().toString());
        assertEquals("123def", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<tag name='abc' />");
        assertEquals("tag", queue.consumeTagName().toString());
        assertEquals(" name='abc' />", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class1.class2:hover");
        assertEquals("div", queue.consumeElementSelector().toString());
        assertEquals(".class1.class2:hover", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("#class1 .class2:hover");
        assertEquals("class1", queue.consumeCssIdentifier().toString());
        assertEquals(".class2:hover", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class='red' href='/index.html'");
        assertEquals("class", queue.consumeAttributeKey().toString());
        assertEquals("href='/index.html'", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdefghijkl<abc>def");
        assertEquals("", queue.remainder().toString());
        queue.addFirst("xyz");
        assertEquals("xyz", queue.remainder().toString());
        assertEquals("abcdefghijkl<abc>def", queue.toString());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("abcdefghijkl<abc>def");
        assertEquals("defghijkl", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToWithMismatchedSequence() {
        TokenQueue queue = new TokenQueue("(abc)def");
        String seq = ")";
        queue.consumeTo(seq);
    }

    @Test
    public void testAddFirstWithNull() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst(null);
        assertEquals("", queue.toString());
    }
}