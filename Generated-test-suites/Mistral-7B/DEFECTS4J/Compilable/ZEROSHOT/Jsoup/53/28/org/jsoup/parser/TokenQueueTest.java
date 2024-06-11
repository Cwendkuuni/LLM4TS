package org.jsoup.parser;
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "hello world";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals('h', queue.peek());
    }

    @Test
    public void testAddFirst() {
        String data = "hello world";
        TokenQueue queue = new TokenQueue(data);
        queue.addFirst('A');
        Assert.assertEquals('A', queue.consume());
        Assert.assertEquals('h', queue.peek());
    }

    @Test
    public void testMatches() {
        String data = "hello world";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.matches("he"));
        Assert.assertFalse(queue.matches("we"));
    }

    @Test
    public void testMatchesCS() {
        String data = "hello world";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.matchesCS("He"));
        Assert.assertFalse(queue.matchesCS("we"));
    }

    @Test
    public void testMatchesAny() {
        String data = "hello world";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.matchesAny("he", "Hi"));
        Assert.assertFalse(queue.matchesAny("we", "Hi"));
    }

    @Test
    public void testMatchesAnyChar() {
        String data = "hello world";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.matchesAny(new char[]{'h', 'H'}));
        Assert.assertFalse(queue.matchesAny(new char[]{'w'}));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<html><head><title>Test</title></head><body>";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.matchChomp("ab"));
        Assert.assertEquals('c', queue.peek());
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " hello world ";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "hello world";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "hello world";
        TokenQueue queue = new TokenQueue(data);
        queue.advance();
        Assert.assertEquals('h', queue.peek());
    }

    @Test
    public void testConsume() {
        String data = "hello world";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals('h', queue.consume());
        Assert.assertEquals('e', queue.peek());
    }

    @Test
    public void testConsumeTo() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("abc", queue.consumeTo("bc"));
        Assert.assertEquals('f', queue.peek());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "ABCdefg";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("ABc", queue.consumeToIgnoreCase("AbC"));
        Assert.assertEquals('d', queue.peek());
    }

    @Test
    public void testConsumeToAny() {
        String data = "hello world";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("hello", queue.consumeToAny("he", "lo"));
        Assert.assertEquals(" world", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        String data = "(hello (world)!))";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("hello (world)", queue.chompBalanced('(', ')'));
        Assert.assertEquals("!)", queue.toString());
    }

    @Test
    public void testUnescape() {
        String data = "\\(hello \\(world\\)!\\)";
        Assert.assertEquals("(hello (world)!)", TokenQueue.unescape(data));
    }

    @Test
    public void testConsumeWhitespace() {
        String data = " hello world ";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("hello", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        String data = "hello world";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("hello", queue.consumeWord());
        Assert.assertEquals(" world", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        String data = "<html><head><title>Test</title></head><body>";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("html", queue.consumeTagName());
        Assert.assertEquals("<head", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "div.class:hover";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("div", queue.consumeElementSelector());
        Assert.assertEquals(".class:hover", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "color: red;";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("color", queue.consumeCssIdentifier());
        Assert.assertEquals(": red", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "class=\"red\"";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("class", queue.consumeAttributeKey());
        Assert.assertEquals("=\"red\"", queue.toString());
    }

    @Test
    public void testRemainder() {
        String data = "hello world";
        TokenQueue queue = new TokenQueue(data);
        String remainder = queue.remainder();
        Assert.assertEquals(" world", remainder);
        Assert.assertEquals("", queue.toString());
    }
}