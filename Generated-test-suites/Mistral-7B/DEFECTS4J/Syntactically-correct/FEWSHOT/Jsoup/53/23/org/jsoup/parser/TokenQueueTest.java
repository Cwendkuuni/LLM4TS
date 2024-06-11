package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;

import java.util.Arrays;

public class TokenQueueTest {

    @Test
    public void testTokenQueueIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testTokenQueueIsNotEmpty() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeekCharacter() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals('a', tokenQueue.peek());
        assertEquals('a', tokenQueue.consume());
    }

    @Test
    public void testPeekString() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        assertEquals("abc", tokenQueue.matches("abc") ? tokenQueue.consumeTo("abc") : "");
        assertEquals("def", tokenQueue.matches("def") ? tokenQueue.consumeTo("def") : "");
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("abcdefghijk");
        assertTrue(tokenQueue.matches("abc"));
        assertTrue(tokenQueue.matches("abcdef"));
        assertTrue(tokenQueue.matches("def"));
        assertFalse(tokenQueue.matches("xyz"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("ABCabcDEF");
        assertTrue(tokenQueue.matchesCS("ABC"));
        assertTrue(tokenQueue.matchesCS("abc"));
        assertFalse(tokenQueue.matchesCS("xyz"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("ABCabcDEF");
        assertTrue(tokenQueue.matchesAny("ABC", "abc"));
        assertTrue(tokenQueue.matchesAny("ABC", "xyz"));
        assertFalse(tokenQueue.matchesAny("xyz", "xyz"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue tokenQueue = new TokenQueue("ABCabcDEF");
        assertTrue(tokenQueue.matchesAny('A', 'B', 'C'));
        assertTrue(tokenQueue.matchesAny('a', 'b', 'c'));
        assertFalse(tokenQueue.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<html><body>");
        assertTrue(tokenQueue.matchesStartTag());
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("abc<def>ghi");
        assertTrue(tokenQueue.matchChomp("abc"));
        assertEquals("def", tokenQueue.consumeTo("def"));
        assertTrue(tokenQueue.matchChomp("ghi"));
        assertEquals("", tokenQueue.consumeToIgnoreCase(""));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(abc (def (ghi)) jkl) mno");
        assertEquals("abc (def (ghi))", tokenQueue.chompBalanced('(', ')'));
        assertEquals("jkl mno", tokenQueue.consumeToIgnoreCase(""));
    }

    @Test
    public void testUnescape() {
        assertEquals("ab\\cde\\f", TokenQueue.unescape("ab\\cde\\f"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   abc   ");
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals("abc", tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("abc def ghi jkl");
        assertEquals("abc", tokenQueue.consumeWord());
        assertEquals("def", tokenQueue.consumeWord());
        assertEquals("ghi", tokenQueue.consumeWord());
        assertEquals("jkl", tokenQueue.consumeToIgnoreCase(""));
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("<html><body><p>Hello World</p></body></html>");
        assertEquals("html", tokenQueue.consumeTagName());
        assertEquals("body", tokenQueue.consumeTagName());
        assertEquals("p", tokenQueue.consumeTagName());
        assertEquals("", tokenQueue.consumeToIgnoreCase(""));
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("div#id.class p.class span.class");
        assertEquals("div", tokenQueue.consumeElementSelector());
        assertEquals("p", tokenQueue.consumeElementSelector());
        assertEquals("span", tokenQueue.consumeElementSelector());
        assertEquals("", tokenQueue.consumeToIgnoreCase(""));

        TokenQueue tokenQueue2 = new TokenQueue("#id.class p.class span.class div");
        assertEquals("div", tokenQueue2.consumeElementSelector());
        assertEquals("", tokenQueue2.consumeToIgnoreCase(""));
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("color: red; -webkit-background-size: cover; font-size: 16px;");
        assertEquals("color", tokenQueue.consumeCssIdentifier());
        assertEquals("font-size", tokenQueue.consumeCssIdentifier());
        assertEquals("", tokenQueue.consumeToIgnoreCase(""));
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("id=\"id\" class=\"class\" style=\"color: red;\" data-test=\"test\"");
        assertEquals("id", tokenQueue.consumeAttributeKey());
        assertEquals("class", tokenQueue.consumeAttributeKey());
        assertEquals("style", tokenQueue.consumeAttributeKey());
        assertEquals("data-test", tokenQueue.consumeAttributeKey());
        assertEquals("", tokenQueue.consumeToIgnoreCase(""));
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertEquals("abcdef", tokenQueue.consumeToAny("abc", "def"));
        assertEquals("xyz", tokenQueue.consumeToAny("xyz"));
        assertEquals("", tokenQueue.consumeToIgnoreCase(""));
    }

    @Test
    public void testChompTo() {
        TokenQueue tokenQueue = new TokenQueue("abc<def>ghi jkl<mno>pqr");
        assertEquals("abc<def>", tokenQueue.chompTo("abc<def>"));
        assertEquals("ghi jkl<mno>pqr", tokenQueue.consumeToIgnoreCase(""));
    }

    @Test
    public void testChompToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("ABC<DEF>GHI jkl<mNo>pqR");
        assertEquals("ABC<DEF>", tokenQueue.chompToIgnoreCase("ABC<DEF>"));
        assertEquals("GHI jkl<mNo>pqR", tokenQueue.consumeToIgnoreCase(""));
    }
}