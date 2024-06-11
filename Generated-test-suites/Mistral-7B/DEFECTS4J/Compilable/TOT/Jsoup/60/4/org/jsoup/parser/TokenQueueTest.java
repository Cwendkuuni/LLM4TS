package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testCreateAndisEmpty() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals("Hello World", queue.toString());
    }

    @Test
    public void testMatchesAndPeek() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertTrue(queue.matches("He"));
        Assert.assertEquals('H', queue.peek());
    }

    @Test
    public void testMatchesFail() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertFalse(queue.matches("Xyz"));
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("Hello World");
        queue.addFirst('!');
        Assert.assertEquals("!Hello World", queue.toString());
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertTrue(queue.matchesCS("He"));
        Assert.assertFalse(queue.matchesCS("Xyz"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertTrue(queue.matchesAny("He", "Hi"));
        Assert.assertFalse(queue.matchesAny("Xyz"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertTrue(queue.matchesAny(new char[]{'H', 'W'}));
        Assert.assertTrue(queue.matchesAny(new char[]{'W', 'l'}));
        Assert.assertFalse(queue.matchesAny(new char[]{'X', 'y'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
        Assert.assertTrue(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("Hello <world> World");
        Assert.assertTrue(queue.matchChomp("<world>"));
        Assert.assertEquals("Hello World", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" Hello World ");
        Assert.assertTrue(queue.matchesWhitespace());
        queue.consumeWhitespace();
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertTrue(queue.matchesWord());
        queue.advance();
        Assert.assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvanceAndConsume() {
        TokenQueue queue = new TokenQueue("Hello World");
        queue.advance();
        Assert.assertEquals('H', queue.consume());
        Assert.assertEquals('e', queue.consume());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeFail() {
        TokenQueue queue = new TokenQueue("Hello World");
        queue.consume("Xyz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("Hello <world> World");
        String result = queue.consumeTo("wor");
        Assert.assertEquals("Hello <world> ", queue.toString());
        Assert.assertEquals("World", result);
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        String result = queue.chompBalanced('(', ')');
        Assert.assertEquals("one (two) three", result);
        Assert.assertEquals(" four", queue.toString());
    }

    @Test
    public void testUnescape() {
        String input = "\\\\a\\\\b\\\\c";
        String expected = "abc";
        String result = TokenQueue.unescape(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("Hello World   ");
        queue.consumeWhitespace();
        Assert.assertEquals("Hello World", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("Hello World");
        String result = queue.consumeWord();
        Assert.assertEquals("World", result);
        Assert.assertEquals("Hello", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
        String result = queue.consumeTagName();
        Assert.assertEquals("title", result);
        Assert.assertEquals("<html><head></head>", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class p#id span.class > ul > li > a");
        String result = queue.consumeElementSelector();
        Assert.assertEquals("ul", result);
        Assert.assertEquals("div.class p#id span.class > li > a", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("background-color:#ff0000;");
        String result = queue.consumeCssIdentifier();
        Assert.assertEquals("#ff0000", result);
        Assert.assertEquals("background-color:;", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"class1 class2\" id=\"id1\"");
        String result = queue.consumeAttributeKey();
        Assert.assertEquals("class", result);
        Assert.assertEquals("id=\"id1\"", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("Hello World");
        String result = queue.remainder();
        Assert.assertEquals("", result);
        Assert.assertEquals("", queue.toString());

        queue = new TokenQueue("Hello World");
        result = queue.remainder();
        Assert.assertEquals("World", result);
        Assert.assertEquals("Hello", queue.toString());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertEquals("World", queue.toString());
    }
}