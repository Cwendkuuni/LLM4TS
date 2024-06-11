package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenQueueTest {
    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("example");
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        Assert.assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst("1");
        Assert.assertEquals('1', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(tokenQueue.matches("example"));
    }

    @Test
    public void testMatchesCS() {
        Assert.assertTrue(tokenQueue.matchesCS("example"));
    }

    @Test
    public void testMatchesAnyString() {
        Assert.assertTrue(tokenQueue.matchesAny("example", "another"));
    }

    @Test
    public void testMatchesAnyChar() {
        Assert.assertTrue(tokenQueue.matchesAny('e', 'x'));
    }

    @Test
    public void testMatchesStartTag() {
        Assert.assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        Assert.assertTrue(tokenQueue.matchChomp("example"));
    }

    @Test
    public void testMatchesWhitespace() {
        Assert.assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        Assert.assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        tokenQueue.advance();
        Assert.assertEquals('x', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        Assert.assertEquals('e', tokenQueue.consume());
    }

    @Test
    public void testConsumeString() {
        tokenQueue.consume("example");
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeTo() {
        Assert.assertEquals("", tokenQueue.consumeTo("unknown"));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        Assert.assertEquals("", tokenQueue.consumeToIgnoreCase("unknown"));
    }

    @Test
    public void testConsumeToAny() {
        Assert.assertEquals("", tokenQueue.consumeToAny("unknown", "another"));
    }

    @Test
    public void testChompTo() {
        Assert.assertEquals("", tokenQueue.chompTo("unknown"));
    }

    @Test
    public void testChompToIgnoreCase() {
        Assert.assertEquals("", tokenQueue.chompToIgnoreCase("unknown"));
    }

    @Test
    public void testChompBalanced() {
        Assert.assertEquals("", tokenQueue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        Assert.assertEquals("example", TokenQueue.unescape("example"));
    }

    @Test
    public void testConsumeWhitespace() {
        Assert.assertFalse(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        Assert.assertEquals("example", tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        Assert.assertEquals("example", tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        Assert.assertEquals("example", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        Assert.assertEquals("example", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        Assert.assertEquals("example", tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        Assert.assertEquals("example", tokenQueue.remainder());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("example", tokenQueue.toString());
    }
}