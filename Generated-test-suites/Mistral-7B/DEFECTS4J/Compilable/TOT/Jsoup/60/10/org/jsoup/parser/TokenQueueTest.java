package org.jsoup.parser;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());

        queue.addFirst("test");
        Assert.assertFalse(queue.isEmpty());
        
        queue.consume("test");
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testPeekAndConsume() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals('h', queue.peek());
        Assert.assertEquals('h', queue.consume());
        Assert.assertEquals('e', queue.peek());
        
        queue.addFirst('x');
        Assert.assertEquals('x', queue.peek());
        Assert.assertEquals('x', queue.consume());
        
        Assert.assertEquals('e', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("test");
        Assert.assertEquals("test", queue.toString());

        queue.addFirst("another");
        Assert.assertEquals("anothertest", queue.toString());
    }

    @Test
    public void testMatch() {
        TokenQueue queue = new TokenQueue("123<tag>456");
        Assert.assertTrue(queue.matches("12"));
        Assert.assertTrue(queue.matches("tag"));
        Assert.assertFalse(queue.matches("789"));
    }

    @Test
    public void testMatchCS() {
        TokenQueue queue = new TokenQueue("123<Tag>456");
        Assert.assertTrue(queue.matchesCS("tag"));
        Assert.assertFalse(queue.matchesCS("TAG"));
    }

    @Test
    public void testMatchAny() {
        TokenQueue queue = new TokenQueue("123<tag>456");
        Assert.assertTrue(queue.matchesAny("1", "2", "3", "4", "5", "6"));
        Assert.assertFalse(queue.matchesAny("7", "8", "9"));
        
        Assert.assertTrue(queue.matchesAny("t", "a", "g"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("123<tag>456");
        Assert.assertTrue(queue.matchChomp("12"));
        Assert.assertEquals("tag456", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   hello world   ");
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWhitespace());

        queue.consumeWhitespace();
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord());

        queue.advance();
        Assert.assertTrue(queue.matchesWord());
    }

    @Test
    public void testAdvanceAndConsume() {
        TokenQueue queue = new TokenQueue("123<tag>456");
        Assert.assertEquals('1', queue.peek());
        queue.advance();
        Assert.assertEquals('2', queue.peek());
        Assert.assertEquals('2', queue.consume());
        
        Assert.assertEquals('3', queue.peek());
        Assert.assertEquals("tag456", queue.consumeToIgnoreCase("tag"));
        Assert.assertEquals("456", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        Assert.assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        Assert.assertEquals(" four", queue.toString());
    }

    @Test
    public void testUnescape() {
        String in = "\\\\hello\\\\world\\\\\\\\";
        String out = TokenQueue.unescape(in);
        Assert.assertEquals("hello world\\\\", out);
    }
}