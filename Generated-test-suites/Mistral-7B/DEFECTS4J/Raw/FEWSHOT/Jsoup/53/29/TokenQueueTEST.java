package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;

public class TokenQueueTest {

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
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abcd");
        queue.addFirst('x');
        assertEquals('x', queue.peek());

        queue.addFirst('y');
        assertEquals('y', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("cdef"));
        assertFalse(queue.matches("xyz"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCdef");
        assertTrue(queue.matchesCS("ab"));
        assertTrue(queue.matchesCS("CDEF"));
        assertFalse(queue.matchesCS("xyz"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesAny("ab", "cd"));
        assertFalse(queue.matchesAny("xyz"));

        queue.advance();
        assertTrue(queue.matchesAny("bc", "ef"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesAny('a', 'b', 'c'));
        assertFalse(queue.matchesAny('x', 'y', 'z'));

        queue.advance();
        assertTrue(queue.matchesAny('b', 'd', 'e'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<ab cd>ef");
        assertTrue(queue.matchesStartTag());

        queue.advance();
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matchChomp("ab"));
        assertTrue(queue.matchChomp("cd"));
        assertFalse(queue.matchChomp("xyz"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   def ");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcd efgh");
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcd efgh");
        queue.advance();
        assertEquals('b', queue.peek());

        queue.advance();
        assertEquals('c', queue.peek());

        queue.advance();
        assertEquals('e', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcd efgh");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('e', queue.consume());

        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals("ab", queue.consumeTo("ab"));
        assertEquals("cd", queue.consumeTo("cd"));
        assertEquals("efg", queue.consumeTo("xyz"));
        assertEquals("", queue.consumeTo("xyz"));

        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("AbCdEfG");
        assertEquals("ab", queue.consumeToIgnoreCase("ab"));
        assertEquals("cd", queue.consumeToIgnoreCase("cd"));
        assertEquals("efg", queue.consumeToIgnoreCase("XYZ"));
        assertEquals("", queue.consumeToIgnoreCase("xyz"));

        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals("ab", queue.consumeToAny("ab", "cd"));
        assertEquals("cd", queue.consumeToAny("cd", "ef"));
        assertEquals("efg", queue.consumeToAny("xyz"));
        assertEquals("", queue.consumeToAny("xyz"));

        assertTrue(queue.isEmpty());
    }

    @Test
    public void testChompTo() {
        TokenQueue queue = new TokenQueue("(<ab>(cd)efg>)");
        assertEquals("(<ab>(cd)", queue.chompBalanced('(', ')').toString());
        assertEquals("efg>", queue.chompBalanced(')', '(').toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(<ab>(cd)efg>)");
        assertEquals("(<ab>(cd", queue.chompBalanced('(', ')'));
        assertEquals("efg>", queue.chompBalanced(')', '('));
    }

    @Test
    public void testUnescape() {
        assertEquals("\\r\\n\\t\\b\\f\\\\\\\\", TokenQueue.unescape("\\r\\n\\t\\b\\f\\\\\\\\"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   def ");
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcd efgh");
        assertEquals("ab", queue.consumeWord());
        assertEquals("cd", queue.consumeWord());
        assertEquals("", queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue(" <ab cd>ef ");
        assertEquals("ab", queue.consumeTagName());
        assertEquals("cd", queue.consumeTagName());
        assertEquals("", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue(" <ab:cd>ef ");
        assertEquals("ab", queue.consumeElementSelector());
        assertEquals("cd", queue.consumeElementSelector());
        assertEquals("", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue(" class='id:ab'");
        assertEquals("id", queue.consumeCssIdentifier());
        assertEquals("ab", queue.consumeCssIdentifier());
        assertEquals("", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue(" class='key:ab'");
        assertEquals("key", queue.consumeAttributeKey());
        assertEquals("ab", queue.consumeAttributeKey());
        assertEquals("", queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcd efgh");
        assertEquals("efgh", queue.remainder());

        queue.consume("ab");
        assertEquals("cd efgh", queue.remainder());

        queue.consumeTo("cd");
        assertEquals("efg", queue.remainder());

        queue.consumeToIgnoreCase("efg");
        assertEquals("", queue.remainder());

        queue.consumeToAny("xyz");
        assertEquals("", queue.remainder());
    }
}