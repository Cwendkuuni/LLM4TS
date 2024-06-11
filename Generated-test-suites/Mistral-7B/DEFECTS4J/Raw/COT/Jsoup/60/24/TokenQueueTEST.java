package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;

import java.util.Arrays;

/**
 * Test class for TokenQueue.
 */
public class TokenQueueTest {

    @org.junit.Before
    public void setUp() {
        // Initialize any state here that needs to be constant for all tests
    }

    @org.junit.Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
        
        queue = new TokenQueue("abc");
        assertFalse(queue.isEmpty());
    }

    @org.junit.Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());
        queue.advance();
        assertEquals('b', queue.peek());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testAddFirstStringWithEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("xyz");
    }

    @org.junit.Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst("xyz");
        assertEquals("xyzabc", queue.toString());
    }

    @org.junit.Test
    public void testAddFirstCharacter() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst('x');
        assertEquals("xyzabc", queue.toString());
    }

    @org.junit.Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcxyz");
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("xy"));
        assertFalse(queue.matches("cd"));
        assertTrue(queue.matches("abcd"));
    }

    @org.junit.Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCxyz");
        assertTrue(queue.matchesCS("AB"));
        assertFalse(queue.matchesCS("xy"));
        assertFalse(queue.matchesCS("cd"));
        assertFalse(queue.matchesCS("ABCD"));
    }

    @org.junit.Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcxyz");
        assertTrue(queue.matchesAny("ab", "cd"));
        assertFalse(queue.matchesAny("xy", "cd"));
        assertTrue(queue.matchesAny("ab", "xy"));
        assertFalse(queue.matchesAny("cd", "xy"));
        assertFalse(queue.matchesAny("ab", "ef"));
    }

    @org.junit.Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abcxyz");
        assertTrue(queue.matchesAny(new char[]{'a', 'b'}));
        assertFalse(queue.matchesAny(new char[]{'x', 'y'}));
        assertTrue(queue.matchesAny(new char[]{'a', 'x'}));
        assertFalse(queue.matchesAny(new char[]{'y', 'x'}));
        assertFalse(queue.matchesAny(new char[]{'a', 'z'}));
    }

    @org.junit.Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<x><y>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @org.junit.Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcxyz");
        assertTrue(queue.matchChomp("ab"));
        assertFalse(queue.matchChomp("xy"));
        assertFalse(queue.matchChomp("cd"));
        assertTrue(queue.matchChomp("abcd"));
        assertEquals("cdxyz", queue.toString());
    }

    @org.junit.Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\r\n abc\u0020");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @org.junit.Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcxyz");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @org.junit.Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        assertEquals('b', queue.peek());
        queue.advance();
        assertEquals('c', queue.peek());
    }

    @org.junit.Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals("", queue.toString());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testConsumeWithEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume("xyz");
    }

    @org.junit.Test
    public void testConsumeString() {
        TokenQueue queue = new TokenQueue("abcxyz");
        String consumed = queue.consume("ab");
        assertEquals("cdxyz", queue.toString());
        assertEquals("ab", consumed);
    }

    @org.junit.Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')').toString());
        assertEquals("four", queue.toString());
    }

    @org.junit.Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((()())())");
        assertEquals("((())", queue.chompBalanced('(', ')').toString());
        assertEquals("()))", queue.toString());
    }

    @org.junit.Test
    public void testUnescape() {
        assertEquals("\\abc\\xyz", TokenQueue.unescape("\\abc\\xyz"));
    }

    @org.junit.Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("abc   def");
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
        assertEquals("def", queue.toString());
    }

    @org.junit.Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcxyz");
        assertEquals("abc", queue.consumeWord().toString());
        assertEquals("xyz", queue.toString());
    }

    @org.junit.Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<x><y>");
        assertEquals("x", queue.consumeTagName().toString());
        assertEquals("y", queue.toString());
    }

    @org.junit.Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class1.class2:hover");
        assertEquals("div", queue.consumeElementSelector().toString());
        assertEquals(".class1.class2:hover", queue.toString());
    }

    @org.junit.Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color:red; background-color:#fff");
        assertEquals("color", queue.consumeCssIdentifier().toString());
        assertEquals(":red; background-color:#fff", queue.toString());
    }

    @org.junit.Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"class1 class2\" id=\"id1\"");
        assertEquals("class", queue.consumeAttributeKey().toString());
        assertEquals("=\"class1 class2\" id=\"id1\"", queue.toString());
    }

    @org.junit.Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcxyz");
        assertEquals("xyz", queue.remainder());
        assertEquals("", queue.toString());
    }

    @org.junit.Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("abcxyz");
        assertEquals("xyz", queue.toString());
    }
}