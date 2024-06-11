package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());

        tokenQueue = new TokenQueue("abc");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals('a', tokenQueue.peek());

        tokenQueue.advance();
        assertEquals('b', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.addFirst('a');
        assertEquals('a', tokenQueue.peek());

        tokenQueue.addFirst("123");
        assertEquals("123", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        assertTrue(tokenQueue.matches("ab"));
        assertFalse(tokenQueue.matches("ef"));

        tokenQueue.advance();
        assertTrue(tokenQueue.matches("cd"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("ABCDEF");
        assertTrue(tokenQueue.matchesCS("Ab"));
        assertFalse(tokenQueue.matchesCS("ef"));

        tokenQueue.advance();
        assertTrue(tokenQueue.matchesCS("CD"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        assertTrue(tokenQueue.matchesAny("ab", "cd"));
        assertFalse(tokenQueue.matchesAny("ef", "gh"));

        tokenQueue.advance();
        assertTrue(tokenQueue.matchesAny("bc", "ef"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<html><head><title>Test</title></head><body>");
        assertTrue(tokenQueue.matchesStartTag());
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("abcdef<html><head><title>Test</title></head><body>");
        tokenQueue.matchChomp("abc");
        assertEquals("def<html><head><title>Test</title></head><body>", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   hello world   ");
        assertTrue(tokenQueue.matchesWhitespace());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertTrue(tokenQueue.matchesWord());
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        tokenQueue.advance();
        assertEquals('b', tokenQueue.peek());

        tokenQueue.advance();
        assertEquals('c', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        assertEquals('a', tokenQueue.consume());
        assertEquals('b', tokenQueue.peek());

        tokenQueue.consume("def");
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("abcdef<html><head><title>Test</title></head><body>");
        assertEquals("abcdef<", tokenQueue.consumeTo("abcdef<").toString());
        assertEquals("<html><head><title>Test</title></head><body>", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("ABCDEF<html><head><title>Test</title></head><body>");
        assertEquals("ABCDEF<", tokenQueue.consumeToIgnoreCase("ABCDEF<").toString());
        assertEquals("<html><head><title>Test</title></head><body>", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("abcdef<html><head><title>Test</title></head><body>");
        assertEquals("abc", tokenQueue.consumeToAny("ab", "cd").toString());
        assertEquals("def<html><head><title>Test</title></head><body>", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(())((()))");
        assertEquals("(())", tokenQueue.chompBalanced('(', ')').toString());
        assertEquals("((()))", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("abc\\def\\ghi\\jkl\\mno\\pqr", TokenQueue.unescape("abc\\def\\ghi\\\\jkl\\mno\\pqr"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   hello world   ");
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals("hello world", tokenQueue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertEquals("hello", tokenQueue.consumeWord());
        assertEquals("world", tokenQueue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("<html><head><title>Test</title></head><body>");
        assertEquals("html", tokenQueue.consumeTagName());
        assertEquals("<head><title>Test</title></head><body>", tokenQueue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("html div.class p#id span.class2");
        assertEquals("html", tokenQueue.consumeElementSelector());
        assertEquals("div.class p#id span.class2", tokenQueue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("id class id2 class2 id3 class3");
        assertEquals("id", tokenQueue.consumeCssIdentifier());
        assertEquals("class id2 class2 id3 class3", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("class='class1' data-custom='customValue' class2='class2' id='id1'");
        assertEquals("class", tokenQueue.consumeAttributeKey());
        assertEquals("='class1' data-custom='customValue' class2='class2' id='id1'", tokenQueue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("abcdef<html><head><title>Test</title></head><body>");
        assertEquals("html<head><title>Test</title></head><body>", tokenQueue.remainder().toString());
        assertEquals("", tokenQueue.toString());
    }
}