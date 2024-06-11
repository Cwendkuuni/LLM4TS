package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("data");
        Assert.assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("data");
        Assert.assertEquals('d', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("ata");
        tokenQueue.addFirst('d');
        Assert.assertEquals("data", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("data");
        Assert.assertTrue(tokenQueue.matches("data"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("Data");
        Assert.assertTrue(tokenQueue.matchesCS("Data"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("data");
        Assert.assertTrue(tokenQueue.matchesAny("da", "ta"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue tokenQueue = new TokenQueue("data");
        Assert.assertTrue(tokenQueue.matchesAny('d', 't'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<div>");
        Assert.assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("data");
        tokenQueue.matchChomp("da");
        Assert.assertEquals("ta", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("data");
        Assert.assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("data");
        Assert.assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tokenQueue = new TokenQueue("data");
        tokenQueue.advance();
        Assert.assertEquals("ata", tokenQueue.toString());
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("data");
        Assert.assertEquals('d', tokenQueue.consume());
    }

    @Test
    public void testConsumeSeq() {
        TokenQueue tokenQueue = new TokenQueue("data");
        tokenQueue.consume("da");
        Assert.assertEquals("ta", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("data");
        Assert.assertEquals("da", tokenQueue.consumeTo("ta"));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("data");
        Assert.assertEquals("da", tokenQueue.consumeToIgnoreCase("TA"));
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("data");
        Assert.assertEquals("dat", tokenQueue.consumeToAny("t", "r"));
    }

    @Test
    public void testChompTo() {
        TokenQueue tokenQueue = new TokenQueue("data");
        Assert.assertEquals("da", tokenQueue.chompTo("ta"));
    }

    @Test
    public void testChompToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("data");
        Assert.assertEquals("da", tokenQueue.chompToIgnoreCase("TA"));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(data)");
        Assert.assertEquals("data", tokenQueue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        String escaped = "\\(data\\)";
        Assert.assertEquals("(data)", TokenQueue.unescape(escaped));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   data");
        Assert.assertTrue(tokenQueue.consumeWhitespace());
        Assert.assertEquals("data", tokenQueue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("data");
        Assert.assertEquals("data", tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("div");
        Assert.assertEquals("div", tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("div|span");
        Assert.assertEquals("div|span", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("css-id");
        Assert.assertEquals("css-id", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("attribute-key");
        Assert.assertEquals("attribute-key", tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("data");
        Assert.assertEquals("data", tokenQueue.remainder());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testToString() {
        TokenQueue tokenQueue = new TokenQueue("data");
        Assert.assertEquals("data", tokenQueue.toString());
    }
}