package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for TokenQueue.
 */
public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst('d');
        Assert.assertEquals("dabc", queue.toString());
        Assert.assertEquals('d', queue.peek());
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.consume());
        Assert.assertEquals('c', queue.consume());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst("def");
        Assert.assertEquals("defabc", queue.toString());
        Assert.assertEquals('d', queue.peek());
        Assert.assertEquals('e', queue.consume());
        Assert.assertEquals('f', queue.consume());
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.consume());
        Assert.assertEquals('c', queue.consume());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc123");
        Assert.assertTrue(queue.matches("ab"));
        Assert.assertTrue(queue.matches("12"));
        Assert.assertFalse(queue.matches("xyz"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABC123");
        Assert.assertTrue(queue.matchesCS("AB"));
        Assert.assertFalse(queue.matchesCS("xyz"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abc123");
        Assert.assertTrue(queue.matchesAny("ab", "12"));
        Assert.assertFalse(queue.matchesAny("xyz"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("abc123");
        Assert.assertTrue(queue.matchesAny('a', '1', 'c'));
        Assert.assertFalse(queue.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abc123");
        Assert.assertTrue(queue.matchChomp("ab"));
        Assert.assertEquals("123", queue.toString());
        Assert.assertFalse(queue.matchChomp("xyz"));
        Assert.assertEquals("123", queue.toString());
    }

    @Test
    public void testMatchChompWhitespace() {
        TokenQueue queue = new TokenQueue(" abc\t123 ");
        Assert.assertTrue(queue.matchChomp(" "));
        Assert.assertEquals("abc\t123", queue.toString());
        Assert.assertFalse(queue.matchChomp("ab"));
        Assert.assertEquals("123", queue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abc123");
        String consumed = queue.consumeTo("ab");
        Assert.assertEquals("ab", consumed);
        Assert.assertEquals("123", queue.toString());
        consumed = queue.consumeTo("xyz");
        Assert.assertEquals("", consumed);
        Assert.assertEquals("123", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("AbC123");
        String consumed = queue.consumeToIgnoreCase("Ab");
        Assert.assertEquals("Ab", consumed);
        Assert.assertEquals("C123", queue.toString());
        consumed = queue.consumeToIgnoreCase("ab");
        Assert.assertEquals("", consumed);
        Assert.assertEquals("C123", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abc123");
        String consumed = queue.consumeToAny("ab", "12");
        Assert.assertEquals("ab", consumed);
        Assert.assertEquals("123", queue.toString());
        consumed = queue.consumeToAny("xyz");
        Assert.assertEquals("", consumed);
        Assert.assertEquals("123", queue.toString());
    }

    @Test
    public void testConsumeToAnyChars() {
        TokenQueue queue = new TokenQueue("abc123");
        String consumed = queue.consumeToAny('a', '1', 'c');
        Assert.assertEquals("a", consumed);
        Assert.assertEquals("bc123", queue.toString());
        consumed = queue.consumeToAny('x', 'y', 'z');
        Assert.assertEquals("", consumed);
        Assert.assertEquals("bc123", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToError() {
        TokenQueue queue = new TokenQueue("abc123");
        queue.consumeTo("xyz");
        queue.consumeTo("xyz");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToAnyError() {
        TokenQueue queue = new TokenQueue("abc123");
        queue.consumeToAny("xyz", "xyz");
    }
}