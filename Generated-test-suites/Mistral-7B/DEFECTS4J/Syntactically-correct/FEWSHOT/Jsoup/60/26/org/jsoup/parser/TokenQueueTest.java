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
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst("1");
        String expected = "1abc";
        assertEquals(expected, queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matches("a"));
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("b"));
        assertFalse(queue.matches("c"));
        assertFalse(queue.matches("d"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABC");
        assertTrue(queue.matchesCS("A"));
        assertTrue(queue.matchesCS("AB"));
        assertFalse(queue.matchesCS("B"));
        assertFalse(queue.matchesCS("C"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny("a", "b"));
        assertTrue(queue.matchesAny("b", "c"));
        assertTrue(queue.matchesAny("a", "c"));
        assertFalse(queue.matchesAny("d"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny('a', 'b'));
        assertTrue(queue.matchesAny('b', 'c'));
        assertTrue(queue.matchesAny('a', 'c'));
        assertFalse(queue.matchesAny('d'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<ab>cd");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdefg");;
        String expected = "bcdefg";
        queue.matchChomp("ab");
        assertEquals(expected, queue.toString());

        queue = new TokenQueue("/html><head><title>Hello</title>");
        queue.matchChomp("/html");
        expected = "<head><title>Hello</title>";
        assertEquals(expected, queue.toString());

        queue = new TokenQueue("\"abc\\def\"");
        queue.matchChomp("\"abc\\");
        expected = "def\"";
        assertEquals(expected, queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        assertTrue(queue.matchesWhitespace());
        queue.advance();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        assertEquals('b', queue.peek());

        queue.advance();
        assertEquals('c', queue.peek());

        queue = new TokenQueue("123");
        queue.advance();
        queue.advance();
        queue.advance();
        assertNull(queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertNull(queue.peek());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("123<ab>cd<ef>ghi");
        assertEquals("ab", queue.consumeTo("ab"));
        assertEquals("ef", queue.consumeTo("ef"));
        assertEquals("ghi", queue.consumeToIgnoreCase("Ghi"));
        assertEquals("", queue.consumeToIgnoreCase("XYZ"));
        assertEquals("", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToMismatch() {
        TokenQueue queue = new TokenQueue("123<ab>cd<ef>ghi");
        queue.consumeTo("pqr");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToShort() {   
        TokenQueue queue = new TokenQueue("123<ab>cd<ef>ghi");
        queue.consumeTo(1); 
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("123<ab>cd<ef>ghi");
        assertEquals("ab", queue.consumeToAny("ab", "ef"));
        assertEquals("ef", queue.consumeToAny("ef", "pqr"));
        assertEquals("cd", queue.consumeToAny("cd", "ghi"));
        assertEquals("", queue.consumeToAny("pqr", "xyz"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(ab)cd(ef)ghi");
        assertEquals("abcd", queue.chompBalanced('(', ')').toString());
        assertEquals("efghi", queue.toString());

        queue = new TokenQueue("[abc]def[ef]ghi");
        assertEquals("abcdef", queue.chompBalanced('[', ']').toString());
        assertEquals("efghi", queue.toString());

        queue = new TokenQueue("'abc''def'ghi");
        assertEquals("abcdef", queue.chompBalanced('\'', '\'').toString());
        assertEquals("ghi", queue.toString());

        queue = new TokenQueue("\"abc\\def\"ghi");
        assertEquals("abcdef", queue.chompBalanced('"', "\"").toString());
        assertEquals("ghi", queue.toString());

        queue = new TokenQueue("<ab>cd<ef>ghi");
        assertEquals("abcd", queue.chompBalanced('<', '>').toString());
        assertEquals("efghi", queue.toString());

        queue = new TokenQueue("<ab>cd<ef>ghi</ef>");
        assertEquals("abcd", queue.chompBalanced('<', '>').toString());
        assertEquals("", queue.toString());

        queue = new TokenQueue("<ab>cd<ef>ghi</ef><ef>");
        assertEquals("abcd", queue.chompBalanced('<', '>').toString());
        assertEquals("ef", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("abc\\def\\\\g\\hi", TokenQueue.unescape("abc\\def\\\\g\\hi"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   ab   cd   ");
        assertTrue(queue.consumeWhitespace());
        assertEquals('a', queue.peek());

        queue = new TokenQueue("   ");
        assertTrue(queue.consumeWhitespace());
        assertNull(queue.peek());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("ab cd ef");
        assertEquals("ab", queue.consumeWord());
        assertEquals("cd", queue.consumeWord());
        assertEquals("ef", queue.consumeWord());
        assertNull(queue.peek());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("tag1 tag2 tag3");
        assertEquals("tag1", queue.consumeTagName());
        assertEquals("tag2", queue.consumeTagName());
        assertEquals("tag3", queue.consumeTagName());
        assertNull(queue.peek());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("tag1#id1 tag2.class1 tag3.class1.class2");
        assertEquals("tag1#id1", queue.consumeElementSelector());
        assertEquals("tag2.class1", queue.consumeElementSelector());
        assertEquals("tag3.class1.class2", queue.consumeElementSelector());
        assertNull(queue.peek());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("id1 class1 class2 id3");
        assertEquals("id1", queue.consumeCssIdentifier());
        assertEquals("class1", queue.consumeCssIdentifier());
        assertEquals("class2", queue.consumeCssIdentifier());
        assertEquals("id3", queue.consumeCssIdentifier());
        assertNull(queue.peek());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"class1\" id=\"id1\" style=\"style1\"");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("style", queue.consumeAttributeKey());
        assertNull(queue.peek());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("ab cd ef");
        assertEquals("ef", queue.remainder().toString());
        assertEquals("", queue.toString());
    }
}