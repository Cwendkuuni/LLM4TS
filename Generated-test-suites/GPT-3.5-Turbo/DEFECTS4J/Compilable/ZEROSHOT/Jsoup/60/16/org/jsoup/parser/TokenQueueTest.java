package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("example string");
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testAddFirstString() {
        tokenQueue.addFirst("new string");
        assertEquals("new stringexample string", tokenQueue.toString());
    }

    @Test
    public void testAddFirstChar() {
        tokenQueue.addFirst('n');
        assertEquals("nexample string", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("example"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("example"));
    }

    @Test
    public void testMatchesAnyString() {
        assertTrue(tokenQueue.matchesAny("ex", "amp"));
    }

    @Test
    public void testMatchesAnyChar() {
        assertTrue(tokenQueue.matchesAny('e', 'a'));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("example"));
        assertEquals(" string", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        tokenQueue.advance();
        assertEquals('x', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        char consumed = tokenQueue.consume();
        assertEquals('e', consumed);
        assertEquals("xample string", tokenQueue.toString());
    }

    @Test
    public void testConsumeString() {
        tokenQueue.consume("example");
        assertEquals(" string", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        String consumed = tokenQueue.consumeTo("str");
        assertEquals("example ", consumed);
        assertEquals("ing", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String consumed = tokenQueue.consumeToIgnoreCase("ExAm");
        assertEquals("example ", consumed);
        assertEquals("ple string", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        String consumed = tokenQueue.consumeToAny("exa", "str");
        assertEquals("mple ", consumed);
        assertEquals(" string", tokenQueue.toString());
    }

    @Test
    public void testChompTo() {
        String chomped = tokenQueue.chompTo("ing");
        assertEquals("example str", chomped);
        assertEquals(" string", tokenQueue.toString());
    }

    @Test
    public void testChompToIgnoreCase() {
        String chomped = tokenQueue.chompToIgnoreCase("ExAm");
        assertEquals("example str", chomped);
        assertEquals(" string", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        String chomped = tokenQueue.chompBalanced('(', ')');
        assertEquals("", chomped);
        assertEquals("example string", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        String unescaped = TokenQueue.unescape("example\\ string");
        assertEquals("example string", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
        assertEquals("example string", tokenQueue.toString());
    }

    @Test
    public void testConsumeWord() {
        String consumed = tokenQueue.consumeWord();
        assertEquals("example", consumed);
        assertEquals(" string", tokenQueue.toString());
    }

    @Test
    public void testConsumeTagName() {
        String consumed = tokenQueue.consumeTagName();
        assertEquals("example", consumed);
        assertEquals(" string", tokenQueue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        String consumed = tokenQueue.consumeElementSelector();
        assertEquals("example", consumed);
        assertEquals(" string", tokenQueue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String consumed = tokenQueue.consumeCssIdentifier();
        assertEquals("example", consumed);
        assertEquals(" string", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        String consumed = tokenQueue.consumeAttributeKey();
        assertEquals("example", consumed);
        assertEquals(" string", tokenQueue.toString());
    }

    @Test
    public void testRemainder() {
        String remainder = tokenQueue.remainder();
        assertEquals("example string", remainder);
        assertTrue(tokenQueue.isEmpty());
    }

}