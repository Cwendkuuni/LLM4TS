package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TokenQueueTest {

    private String queue;
    private TokenQueue tokenQueue;

    public TokenQueueTest(String queue) {
        this.queue = queue;
    }

    @Parameterized.Parameters(name = "{index}: queue={0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"", new TokenQueue("")},
                new Object[]{"abc", new TokenQueue("abc")},
                new Object[]{"<div id=\"test\">abc</div>", new TokenQueue("<div id=\"test\">abc</div>")},
                new Object[]{"<div id=\"test\">abc<def>", new TokenQueue("<div id=\"test\">abc<def>")},
                new Object[]{"<div id=\"test\">abc<def>", new TokenQueue("<div id=\"test\">abc")},
                new Object[]{"<div id=\"test\">abc<def>", new TokenQueue("<div id=\"test\">def")},
                new Object[]{"<div id=\"test\">abc<def>", new TokenQueue("")},
        });
    }

    @Test
    public void testConstructor() {
        tokenQueue = new TokenQueue(queue);
        assertEquals(queue, tokenQueue.queue);
        assertEquals(0, tokenQueue.pos);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(tokenQueue.isEmpty());
        tokenQueue.addFirst("a");
        assertFalse(tokenQueue.isEmpty());
        tokenQueue.advance();
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        tokenQueue.addFirst('a');
        assertEquals('a', tokenQueue.peek());
        tokenQueue.advance();
        assertNull(tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('a');
        assertEquals(new char[]{'a'}, tokenQueue.toCharArray());
        tokenQueue.addFirst('b');
        assertEquals(new char[]{'a', 'b'}, tokenQueue.toCharArray());
        tokenQueue.addFirst("abc");
        assertEquals(new char[]{'a', 'b', 'a', 'b', 'c'}, tokenQueue.toCharArray());
    }

    @Test
    public void testMatches() {
        tokenQueue.addFirst("abc");
        assertTrue(tokenQueue.matches("abc"));
        assertTrue(tokenQueue.matches("ab"));
        assertFalse(tokenQueue.matches("ac"));
        assertFalse(tokenQueue.matches("def"));
        assertFalse(tokenQueue.matches(""));
    }

    @Test
    public void testMatchesCS() {
        tokenQueue.addFirst("AbC");
        assertTrue(tokenQueue.matchesCS("AbC"));
        assertTrue(tokenQueue.matchesCS("Ab"));
        assertFalse(tokenQueue.matchesCS("Ac"));
        assertFalse(tokenQueue.matchesCS("Def"));
        assertFalse(tokenQueue.matchesCS(""));
    }

    @Test
    public void testMatchesAny() {
        tokenQueue.addFirst("abcdef");
        assertTrue(tokenQueue.matchesAny("ab", "cd"));
        assertTrue(tokenQueue.matchesAny("bc", "ef"));
        assertTrue(tokenQueue.matchesAny("ac", "ef"));
        assertFalse(tokenQueue.matchesAny("abcd"));
        assertFalse(tokenQueue.matchesAny("xyz"));
    }

    @Test
    public void testMatchesAnyChar() {
        tokenQueue.addFirst("abcdef");
        assertTrue(tokenQueue.matchesAny(new char[]{'a', 'b'}));
        assertTrue(tokenQueue.matchesAny(new char[]{'c', 'f'}));
        assertFalse(tokenQueue.matchesAny(new char[]{'x', 'y'}));
    }

    @Test
    public void testMatchesStartTag() {
        tokenQueue.addFirst("<div>");
        assertTrue(tokenQueue.matchesStartTag());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        tokenQueue.addFirst("abc");
        tokenQueue.advance();
        assertTrue(tokenQueue.matchChomp("bc"));
        tokenQueue.advance();
        assertTrue(tokenQueue.isEmpty());
        tokenQueue.addFirst("abcdef");
        tokenQueue.consume("abc");
        assertTrue(tokenQueue.matchChomp("def"));
    }

    @Test
    public void testMatchesWhitespace() {
        tokenQueue.addFirst("   ");
        assertTrue(tokenQueue.matchesWhitespace());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        tokenQueue.addFirst("abc");
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        tokenQueue.addFirst("abc");
        tokenQueue.advance();
        assertEquals('b', tokenQueue.consume());
        assertEquals('c', tokenQueue.consume());
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testConsume() {
        tokenQueue.addFirst("abc");
        assertEquals('a', tokenQueue.consume());
        assertEquals('b', tokenQueue.consume());
        assertEquals('c', tokenQueue.consume());
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeTo() {
        tokenQueue.addFirst("abc");
        tokenQueue.consumeTo("bc");
        assertEquals("a", tokenQueue.consume());
        assertEquals("c", tokenQueue.consume());
        assertTrue(tokenQueue.isEmpty());

        tokenQueue.addFirst("abcdef");
        tokenQueue.consumeTo("bcde");
        assertEquals("a", tokenQueue.consume());
        assertEquals("f", tokenQueue.consume());
        assertEquals("", tokenQueue.consume());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        tokenQueue.addFirst("AbCdEf");
        tokenQueue.consumeToIgnoreCase("bcDE");
        assertEquals("Ab", tokenQueue.consume());
        assertEquals("f", tokenQueue.consume());
        assertEquals("", tokenQueue.consume());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        tokenQueue.addFirst("abcdef");
        tokenQueue.consumeToAny("ab", "cd");
        assertEquals("a", tokenQueue.consume());
        assertEquals("d", tokenQueue.consume());
        assertEquals("", tokenQueue.consume());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testChompBalanced() {
        tokenQueue.addFirst("(abc)def");
        assertEquals("abc", tokenQueue.chompBalanced('(', ')'));
        assertEquals("def", tokenQueue.toString());

        tokenQueue.addFirst("(abc)def");
        tokenQueue.consume("(abc");
        assertEquals("def", tokenQueue.chompBalanced('(', ')'));
        assertEquals("", tokenQueue.toString());

        tokenQueue.addFirst("(abc)def");
        tokenQueue.consume("(abc");
        tokenQueue.consume("def");
        assertEquals("", tokenQueue.toString());

        tokenQueue.addFirst("(abc)def");
        tokenQueue.consume("(abc");
        tokenQueue.consumeTo("def");
        assertEquals("", tokenQueue.toString());

        tokenQueue.addFirst("(abc)def");
        tokenQueue.consumeToIgnoreCase("def");
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("abc\\def", TokenQueue.unescape("abc\\def"));
        assertEquals("abc\\\\def", TokenQueue.unescape("abc\\\\def"));
    }

    @Test
    public void testConsumeWhitespace() {
        tokenQueue.addFirst("   abc   ");
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals("abc", tokenQueue.toString());
    }

    @Test
    public void testConsumeWord() {
        tokenQueue.addFirst("abc abc");
        assertEquals("abc", tokenQueue.consumeWord());
        assertEquals("abc", tokenQueue.toString());
    }

    @Test
    public void testConsumeTagName() {
        tokenQueue.addFirst("div id=\"test\" class=\"test\"");
        assertEquals("div", tokenQueue.consumeTagName());
        assertEquals("id=\"test\" class=\"test\"", tokenQueue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        tokenQueue.addFirst("div#test.class1.class2");
        assertEquals("div", tokenQueue.consumeElementSelector());
        assertEquals("#test.class1.class2", tokenQueue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        tokenQueue.addFirst("div#test.class1.test-class");
        assertEquals("test", tokenQueue.consumeCssIdentifier());
        assertEquals("#test.class1.test-class", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        tokenQueue.addFirst("div id=\"test\" class=\"test class1\" data-test=\"value\"");
        assertEquals("class", tokenQueue.consumeAttributeKey());
        assertEquals("id=\"test\" class=\"test class1\" data-test=\"value\"", tokenQueue.toString());
    }

    @Test
    public void testRemainder() {
        tokenQueue.addFirst("abcdef");
        assertEquals("def", tokenQueue.remainder());
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testToString() {
        tokenQueue.addFirst("abcdef");
        assertEquals("def", tokenQueue.toString());
        assertEquals("def", tokenQueue.remainder());
        tokenQueue.addFirst("ghijk");
        assertEquals("abcdefghijk", tokenQueue.toString());
    }
}