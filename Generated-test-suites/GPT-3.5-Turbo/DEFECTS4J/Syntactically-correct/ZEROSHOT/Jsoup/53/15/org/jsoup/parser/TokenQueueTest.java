package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertEquals('t', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.addFirst('A');
        Assert.assertEquals("Atest", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertTrue(tokenQueue.matches("test"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertTrue(tokenQueue.matchesCS("test"));
    }

    @Test
    public void testMatchesAnyString() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertTrue(tokenQueue.matchesAny("test"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertTrue(tokenQueue.matchesAny('t'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<tag>");
        Assert.assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertTrue(tokenQueue.matchChomp("test"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" ");
        Assert.assertTrue(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.advance();
        Assert.assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertEquals('t', tokenQueue.consume());
    }

    @Test
    public void testConsumeString() {
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.consume("te");
        Assert.assertEquals("st", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertEquals("", tokenQueue.consumeTo("a"));
        Assert.assertEquals("test", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertEquals("", tokenQueue.consumeToIgnoreCase("a"));
        Assert.assertEquals("test", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAnyString() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertEquals("t", tokenQueue.consumeToAny("c"));
        Assert.assertEquals("est", tokenQueue.toString());
    }

    @Test
    public void testChompTo() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertEquals("test", tokenQueue.chompTo("a"));
        Assert.assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testChompToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertEquals("test", tokenQueue.chompToIgnoreCase("a"));
        Assert.assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        Assert.assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
        Assert.assertEquals(" four", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        Assert.assertEquals("\\", TokenQueue.unescape("\\\\"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("  ");
        Assert.assertTrue(tokenQueue.consumeWhitespace());
        Assert.assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertEquals("test", tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertEquals("test", tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertEquals("test", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertEquals("test", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertEquals("test", tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertEquals("test", tokenQueue.remainder());

    }
}