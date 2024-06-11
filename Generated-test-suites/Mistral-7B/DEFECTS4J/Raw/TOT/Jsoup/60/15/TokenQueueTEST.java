package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
        Assert.assertFalse(queue.matches("hello"));
        Assert.assertFalse(queue.matchesCS("Hello"));
        Assert.assertFalse(queue.matchesAny("hello", "world"));
        Assert.assertFalse(queue.matchesStartTag());
        Assert.assertFalse(queue.matchChomp("hello"));
        Assert.assertFalse(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWord());
        queue.advance();
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testBasicQueue() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals('h', queue.peek());
        Assert.assertTrue(queue.matches("hello"));
        Assert.assertTrue(queue.matchesCS("Hello"));
        Assert.assertTrue(queue.matchesAny("hello", "world"));
        Assert.assertFalse(queue.matchesAny("goodbye"));
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchChomp("goodbye"));
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals('h', queue.consume());
        Assert.assertEquals('e', queue.consume());
        Assert.assertEquals('l', queue.consume());
        Assert.assertEquals('l', queue.consume());
        Assert.assertEquals('o', queue.consume());
        Assert.assertEquals(' ', queue.consume());
        Assert.assertEquals('w', queue.consume());
        Assert.assertEquals('o', queue.consume());
        Assert.assertEquals('r', queue.consume());
        Assert.assertEquals('l', queue.consume());
        Assert.assertEquals('d', queue.consume());
        Assert.assertEquals(' ', queue.consume());
        Assert.assertEquals('\u0000', queue.consume());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testMismatchedConsume() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.consume("world");
    }

    @Test
    public void testAdvanceAndConsume() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals('h', queue.peek());
        queue.advance();
        Assert.assertEquals('e', queue.peek());
        queue.consume();
        queue.consume();
        Assert.assertEquals('l', queue.peek());
        queue.consume();
        queue.consume();
        Assert.assertEquals('o', queue.peek());
        queue.consume();
        queue.consume();
        Assert.assertEquals(' ', queue.peek());
        queue.consume();
        Assert.assertEquals('\u0000', queue.peek());
        queue.consume();
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matchesAny(new char[]{'h', 'w'}));
        Assert.assertFalse(queue.matchesAny(new char[]{'g', 'w'}));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(hello (world) (hello) world)");
        Assert.assertEquals("hello (world)", queue.chompBalanced('(', ')'));
        Assert.assertEquals("(hello)", queue.chompBalanced('(', ')'));
        Assert.assertEquals("hello world", queue.chompBalanced('(', ')'));
        Assert.assertEquals("", queue.chompBalanced('(', ')'));
        queue.consume('(');
        queue.consume('h');
        queue.consume('e');
        queue.consume('l');
        queue.consume('o');
        queue.consume('(');
        queue.consume('w');
        queue.consume('o');
        queue.consume('r');
        queue.consume('l');
        queue.consume('d');
        queue.consume(')');
        Assert.assertEquals("", queue.chompBalanced('(', ')'));
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testUnescape() {
        String input = "\\(hello \\(world\\) world\\)";
        String expected = "\\(hello (world) world\\)";
        String output = TokenQueue.unescape(input);
        Assert.assertEquals(expected, output);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" hello world ");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals('h', queue.peek());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals("hello", queue.consumeWord());
        Assert.assertEquals("world", queue.consumeWord());
        Assert.assertEquals("\u0000", queue.peek());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body></body></html>");
        Assert.assertEquals("html", queue.consumeTagName());
        Assert.assertEquals("head", queue.consumeTagName());
        Assert.assertEquals("title", queue.consumeTagName());
        Assert.assertEquals("Test", queue.consumeElementSelector());
        Assert.assertEquals("body", queue.consumeTagName());
        Assert.assertEquals("\u0000", queue.peek());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#class:hover > p#id > a[title]");
        Assert.assertEquals("div", queue.consumeElementSelector());
        Assert.assertEquals("class", queue.consumeAttributeKey());
        Assert.assertEquals(":hover", queue.consume());
        Assert.assertEquals(">", queue.consume());
        Assert.assertEquals("p", queue.consumeElementSelector());
        Assert.assertEquals("id", queue.consumeAttributeKey());
        Assert.assertEquals("id", queue.consume());
        Assert.assertEquals(">", queue.consume());
        Assert.assertEquals("a", queue.consumeElementSelector());
        Assert.assertEquals("[title]", queue.consume());
        Assert.assertEquals("\u0000", queue.peek());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class:hover id id class class");
        Assert.assertEquals("class", queue.consumeCssIdentifier());
        Assert.assertEquals("hover", queue.consumeCssIdentifier());
        Assert.assertEquals("id", queue.consumeCssIdentifier());
        Assert.assertEquals("id", queue.consumeCssIdentifier());
        Assert.assertEquals("class", queue.consumeCssIdentifier());
        Assert.assertEquals("class", queue.consumeCssIdentifier());
        Assert.assertEquals("\u0000", queue.peek());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class:hover id for class class");
        Assert.assertEquals("class", queue.consumeAttributeKey());
        Assert.assertEquals("hover", queue.consume());
        Assert.assertEquals("id", queue.consumeAttributeKey());
        Assert.assertEquals("for", queue.consumeAttributeKey());
        Assert.assertEquals("class", queue.consumeAttributeKey());
        Assert.assertEquals("\u0000", queue.peek());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("class:hover id for class class id");
        String expected = "id";
        String output = queue.remainder();
        Assert.assertEquals(expected, output);
        Assert.assertEquals("\u0000", queue.peek());
        Assert.assertTrue(queue.isEmpty());
    }
}