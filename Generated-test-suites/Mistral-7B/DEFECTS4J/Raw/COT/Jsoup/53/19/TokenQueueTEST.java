package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.TokenQueue;

import org.junit.Test;

/**
 * TokenQueueTest.java
 */
public class TokenQueueTest {

    @Test
    public void testTokenQueueConstructor() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        assertEquals(data, queue.queue);
        assertEquals(0, queue.pos);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.isEmpty());

        data = "abcdef";
        queue = new TokenQueue(data);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeekAndConsume() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        assertEquals('a', queue.peek());
        assertEquals('a', queue.consume());
        assertEquals('b', queue.peek());
        assertEquals('b', queue.consume());
        // test empty
        data = "";
        queue = new TokenQueue(data);
        assertEquals(0, queue.peek());
        assertEquals(0, queue.consume());
    }

    @Test
    public void testAddFirst() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        queue.addFirst('x');
        assertEquals('x', queue.queue.charAt(0));
        assertEquals('a', queue.queue.charAt(1));
        assertEquals(1, queue.pos);

        queue.addFirst("xyz");
        assertEquals("xyz", queue.queue.substring(0, 3));
        assertEquals('a', queue.queue.charAt(3));
        assertEquals(3, queue.pos);
    }

    @Test
    public void testMatches() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        // true
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("ef"));

        // false
        assertFalse(queue.matches("cd"));
        assertFalse(queue.matches("gh"));

        // empty
        String emptyData = "";
        TokenQueue emptyQueue = new TokenQueue(emptyData);
        assertFalse(emptyQueue.matches("cd"));
    }

    @Test
    public void testMatchesCS() {
        String data = "abcABCdef";
        TokenQueue queue = new TokenQueue(data);
        // true
        assertTrue(queue.matchesCS("ab"));
        assertTrue(queue.matchesCS("EF"));

        // false
        assertFalse(queue.matchesCS("cd"));
        assertFalse(queue.matchesCS("gh"));
    }

    @Test
    public void testMatchesAny() {
        String data = "abcABCdef";
        TokenQueue queue = new TokenQueue(data);
        // true
        assertTrue(queue.matchesAny("ab", "ef"));
        assertTrue(queue.matchesAny("EF", "cd"));

        // false
        assertFalse(queue.matchesAny("gh", "jk"));
    }

    @Test
    public void testMatchesAnyChar() {
        String data = "abcABCdef";
        TokenQueue queue = new TokenQueue(data);
        // true
        assertTrue(queue.matchesAny(new char[]{'a', 'b'}));
        assertTrue(queue.matchesAny(new char[]{'E', 'F'}));

        // false
        assertFalse(queue.matchesAny(new char[]{'g', 'h'}));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<abc><def><ghi>";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesStartTag()); // "<a"
        assertTrue(queue.matchesStartTag()); // "<d"
        assertTrue(queue.matchesStartTag()); // "<g"
        assertFalse(queue.matchesStartTag()); // ">"
    }

    @Test
    public void testMatchChomp() {
        String data = "abc<def>";
        TokenQueue queue = new TokenQueue(data);
        // true
        assertTrue(queue.matchChomp("ab"));
        assertEquals("c<def>", queue.queue);
        assertEquals(3, queue.pos);

        // false
        assertFalse(queue.matchChomp("cd"));
        assertEquals("abc<def>", queue.queue);
        assertEquals(0, queue.pos);
    }

    @Test
    public void testMatchesWhitespace() {
        String data = "   abcdef   ";
        TokenQueue queue = new TokenQueue(data);
        // true
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());

        // false
        assertFalse(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "abc123xyz";
        TokenQueue queue = new TokenQueue(data);
        // true
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());

        // false
        assertFalse(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithEmptyQueue() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        queue.consume("xyz");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithMismatchedSequence() {
        String data = "abc<def>";
        TokenQueue queue = new TokenQueue(data);
        queue.consume("ef");
        queue.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        String data = "abc<def>ghijk";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("def", queue.consumeTo("def"));
        assertEquals("ghijk", queue.queue);
        assertEquals(6, queue.pos);

        assertEquals("defghi", queue.consumeTo("defghi"));
        assertEquals("jk", queue.queue);
        assertEquals(8, queue.pos);

        assertEquals("", queue.consumeTo("xyz"));
        assertEquals("", queue.queue);
        assertEquals(8, queue.pos);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "ABC<def>ghijk";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("def", queue.consumeToIgnoreCase("def"));
        assertEquals("ghijk", queue.queue);
        assertEquals(6, queue.pos);

        assertEquals("DEFGHI", queue.consumeToIgnoreCase("DefGhi"));
        assertEquals("jk", queue.queue);
        assertEquals(11, queue.pos);

        assertEquals("", queue.consumeToIgnoreCase("xyz"));
        assertEquals("", queue.queue);
        assertEquals(11, queue.pos);
    }

    @Test
    public void testConsumeToAny() {
        String data = "<abc:def ghi:jkl mno:pqr>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("def", queue.consumeToAny("def", "ghi"));
        assertEquals(":jkl mno:pqr", queue.queue);
        assertEquals(7, queue.pos);

        assertEquals("pqr", queue.consumeToAny("pqr", "mno"));
        assertEquals("", queue.queue);
        assertEquals(11, queue.pos);

        assertEquals("", queue.consumeToAny("xyz", "xyz"));
        assertEquals("", queue.queue);
        assertEquals(11, queue.pos);
    }

    @Test
    public void testChompBalanced() {
        String data = "(abc)<def>(ghi)jkl>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("abc", queue.chompBalanced('(', ')'));
        assertEquals("(def)(ghi)jkl>", queue.queue);
        assertEquals(7, queue.pos);

        assertEquals("def", queue.chompBalanced('(', ')'));
        assertEquals("(ghi)jkl>", queue.queue);
        assertEquals(9, queue.pos);

        assertEquals("ghi", queue.chompBalanced('(', ')'));
        assertEquals("jkl>", queue.queue);
        assertEquals(11, queue.pos);

        assertEquals("", queue.chompBalanced('(', ')'));
        assertEquals("jkl>", queue.queue);
        assertEquals(11, queue.pos);
    }

    @Test
    public void testUnescape() {
        String data = "abc\\def\\\\";
        String result = TokenQueue.unescape(data);
        assertEquals("abcdef\\", result);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = "   abcdef   ";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.consumeWhitespace());
        assertEquals("abcdef", queue.queue);
        assertEquals(6, queue.pos);

        assertTrue(queue.consumeWhitespace());
        assertEquals("abcdef", queue.queue);
        assertEquals(6, queue.pos);

        assertFalse(queue.consumeWhitespace());
        assertEquals("abcdef", queue.queue);
        assertEquals(6, queue.pos);
    }

    @Test
    public void testConsumeWord() {
        String data = "abc123xyz";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("abc", queue.consumeWord());
        assertEquals("123xyz", queue.queue);
        assertEquals(3, queue.pos);

        assertEquals("xyz", queue.consumeWord());
        assertEquals("", queue.queue);
        assertEquals(6, queue.pos);

        assertEquals("", queue.consumeWord());
        assertEquals("", queue.queue);
        assertEquals(6, queue.pos);
    }

    @Test
    public void testConsumeTagName() {
        String data = "<abc:def ghi:jkl mno:pqr>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("abc", queue.consumeTagName());
        assertEquals(":def ghi:jkl mno:pqr", queue.queue);
        assertEquals(6, queue.pos);

        assertEquals("def", queue.consumeTagName());
        assertEquals(":ghi jkl mno:pqr", queue.queue);
        assertEquals(10, queue.pos);

        assertEquals("jkl", queue.consumeTagName());
        assertEquals("mno:pqr", queue.queue);
        assertEquals(13, queue.pos);

        assertEquals("", queue.consumeTagName());
        assertEquals("mno:pqr", queue.queue);
        assertEquals(13, queue.pos);
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "<abc:def ghi:jkl mno:pqr>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("abc", queue.consumeElementSelector());
        assertEquals(":def ghi:jkl mno:pqr", queue.queue);
        assertEquals(6, queue.pos);

        assertEquals("def", queue.consumeElementSelector());
        assertEquals(":ghi jkl mno:pqr", queue.queue);
        assertEquals(10, queue.pos);

        assertEquals("jkl", queue.consumeElementSelector());
        assertEquals("mno:pqr", queue.queue);
        assertEquals(13, queue.pos);

        assertEquals("", queue.consumeElementSelector());
        assertEquals("mno:pqr", queue.queue);
        assertEquals(13, queue.pos);
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "id:abc123 class:xyz-def";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("abc", queue.consumeCssIdentifier());
        assertEquals("123 class:xyz-def", queue.queue);
        assertEquals(5, queue.pos);

        assertEquals("xyz-def", queue.consumeCssIdentifier());
        assertEquals("", queue.queue);
        assertEquals(10, queue.pos);

        assertEquals("", queue.consumeCssIdentifier());
        assertEquals("", queue.queue);
        assertEquals(10, queue.pos);
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "id:abc123 class:xyz-def style:color:red";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals(":abc123 class:xyz-def style:color:red", queue.queue);
        assertEquals(4, queue.pos);

        assertEquals("class", queue.consumeAttributeKey());
        assertEquals(":xyz-def style:color:red", queue.queue);
        assertEquals(9, queue.pos);

        assertEquals("style", queue.consumeAttributeKey());
        assertEquals(":color:red", queue.queue);
        assertEquals(12, queue.pos);

        assertEquals("color", queue.consumeAttributeKey());
        assertEquals(":red", queue.queue);
        assertEquals(13, queue.pos);

        assertEquals("", queue.consumeAttributeKey());
        assertEquals(":red", queue.queue);
        assertEquals(13, queue.pos);
    }

    @Test
    public void testRemainder() {
        String data = "abcdefghij";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("ij", queue.remainder());
        assertEquals("abcdef", queue.queue);
        assertEquals(8, queue.pos);

        assertEquals("", queue.remainder());
        assertEquals("", queue.queue);
        assertEquals(8, queue.pos);
    }

    @Test
    public void testToString() {
        String data = "abcdefghij";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("ij", queue.toString());
        assertEquals("abcdef", queue.queue);
        assertEquals(8, queue.pos);

        assertEquals("", queue.toString());
        assertEquals("", queue.queue);
        assertEquals(8, queue.pos);
    }
}