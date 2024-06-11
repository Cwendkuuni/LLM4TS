package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
        Assert.assertFalse(queue.matches("test"));
    }

    @Test
    public void testPeekAndAddFirst() {
        TokenQueue queue = new TokenQueue("test");
        Assert.assertEquals('t', queue.peek());
        queue.addFirst('T');
        Assert.assertEquals('T', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("testtest");
        Assert.assertTrue(queue.matches("test"));
        Assert.assertFalse(queue.matches("fail"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("TestTesT");
        Assert.assertTrue(queue.matchesCS("Test"));
        Assert.assertFalse(queue.matchesCS("test"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("testtestTestTesT");
        Assert.assertTrue(queue.matchesAny("test", "Test"));
        Assert.assertFalse(queue.matchesAny("fail"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("testtestTestTesT");
        Assert.assertTrue(queue.matchesAny('t', 'T'));
        Assert.assertFalse(queue.matchesAny('f'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<test>");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag()); // already consumed
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("testtestTestTesT");
        Assert.assertTrue(queue.matchChomp("test"));
        Assert.assertEquals("TestTesT", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" test test ");
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("testtestTestTesT");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.consumeWord().equals("test"));
        Assert.assertFalse(queue.matchesWord());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeFail() {
        TokenQueue queue = new TokenQueue("test");
        queue.consume("fail");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("testtestTestTesT");
        Assert.assertEquals("testtest", queue.consumeTo("test"));
        Assert.assertEquals("TestTesT", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("TestTesT testtest TestTesT");;
        Assert.assertEquals("testtest", queue.consumeToIgnoreCase("test"));
        Assert.assertEquals("Test", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("testTest test test test test test");
        Assert.assertEquals("testTest", queue.consumeToAny("test", "Test"));
        Assert.assertEquals(" test test test", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(test (one (two) three) four)");
        Assert.assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        Assert.assertEquals("four", queue.toString());
    }

    @Test
    public void testUnescape() {
        String input = "\\\\test\\\\\\test\\\\\\test\\";
        Assert.assertEquals("testtesttest", TokenQueue.unescape(input));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" \t test \t ");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertFalse(queue.consumeWhitespace());
        Assert.assertEquals("test", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("testtestTestTesT");
        Assert.assertEquals("test", queue.consumeWord());
        Assert.assertEquals("testTest", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<testTag>");
        Assert.assertEquals("testTag", queue.consumeTagName());
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("test:tagTest");
        Assert.assertEquals("test", queue.consumeElementSelector());
        Assert.assertEquals("tagTest", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("test-IDtest-ID");
        Assert.assertEquals("test-ID", queue.consumeCssIdentifier());
        Assert.assertEquals("test-IDtest-ID", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("test-class:classTest test-for:forTest");
        Assert.assertEquals("test-class", queue.consumeAttributeKey());
        Assert.assertEquals("classTest test-for", queue.toString());
    }

    @Test
    public void testConsumeRemainder() {
        TokenQueue queue = new TokenQueue("test test test test test test");
        String remainder = queue.remainder();
        Assert.assertEquals("test test test test test", queue.toString());
        Assert.assertEquals("test", queue.consumeWord());
        Assert.assertEquals(remainder, queue.remainder());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("test test test test test test");
        Assert.assertEquals("test test test test test test", queue.toString());
    }
}