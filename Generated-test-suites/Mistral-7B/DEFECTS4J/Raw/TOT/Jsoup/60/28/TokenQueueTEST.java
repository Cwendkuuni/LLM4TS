package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the TokenQueue class.
 */
public class TokenQueueTest {

    /**
     * Test constructor with empty input string.
     */
    @Test
    public void testEmptyInput() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
    }

    /**
     * Test constructor with valid input string.
     */
    @Test
    public void testValidInput() {
        TokenQueue queue = new TokenQueue("abc123def");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals('a', queue.peek());
        queue.advance();
        Assert.assertEquals('b', queue.peek());
        queue.addFirst('x');
        Assert.assertEquals('x', queue.peek());
    }

    /**
     * Test matches method with valid input.
     */
    @Test
    public void testMatchesMethod() {
        TokenQueue queue = new TokenQueue("abc123def");
        Assert.assertFalse(queue.matches("xyz"));
        Assert.assertTrue(queue.matches("ab"));
        Assert.assertTrue(queue.matches("cd"));
        queue.advance();
        Assert.assertTrue(queue.matches("ef"));
    }

    /**
     * Test matchesAny method with valid input.
     */
    @Test
    public void testMatchesAnyMethod() {
        TokenQueue queue = new TokenQueue("abc123def");
        Assert.assertTrue(queue.matchesAny("ab", "cd"));
        Assert.assertFalse(queue.matchesAny("xyz"));
        queue.addFirst('x');
        Assert.assertTrue(queue.matchesAny("ab", "cd", "x"));
    }

    /**
     * Test matchesStartTag method with valid input.
     */
    @Test
    public void testMatchesStartTagMethod() {
        TokenQueue queue = new TokenQueue(" <html>\n<head>\n <title>\n <body>\n");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag());
        Assert.assertTrue(queue.matchesStartTag());
        queue.advance();
        queue.advance();
        Assert.assertTrue(queue.matchesStartTag());
    }

    /**
     * Test matchChomp method with valid input.
     */
    @Test
    public void testMatchChompMethod() {
        TokenQueue queue = new TokenQueue("<html>\n<head>\n<title>\n<body>\n");
        Assert.assertFalse(queue.matchChomp("html"));
        Assert.assertTrue(queue.matchChomp("head"));
        Assert.assertTrue(queue.matchChomp("title"));
        Assert.assertTrue(queue.matchChomp("body"));
        Assert.assertFalse(queue.matchChomp("body"));
    }

    /**
     * Test consume method with valid input.
     */
    @Test
    public void testConsumeMethod() {
        TokenQueue queue = new TokenQueue("abc123def");
        String consumed = queue.consume(2);
        Assert.assertEquals("ab", consumed);
        Assert.assertEquals('c', queue.peek());
        Assert.assertTrue(queue.matches("def"));
    }

    /**
     * Test chompBalanced method with valid input.
     */
    @Test
    public void testChompBalancedMethod() {
        TokenQueue queue = new TokenQueue("(abc)def((x)(y))");
        String balance = queue.chompBalanced('(', ')');
        Assert.assertEquals("abcdef", queue.toString());
        Assert.assertEquals("(x)(y)", balance);
    }

    /**
     * Test unescape method with valid input.
     */
    @Test
    public void testUnescapeMethod() {
        Assert.assertEquals("abc\\def", TokenQueue.unescape("abc\\def"));
        Assert.assertEquals("abc\\def\\\\", TokenQueue.unescape("abc\\def\\\\"));
    }
}