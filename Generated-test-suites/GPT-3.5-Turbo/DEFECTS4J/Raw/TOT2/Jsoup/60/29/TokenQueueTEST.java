package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertFalse(tokenQueue.isEmpty());
        tokenQueue.consume();
        Assert.assertFalse(tokenQueue.isEmpty());
        tokenQueue.consume();
        tokenQueue.consume();
        tokenQueue.consume();
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("peek");
        Assert.assertEquals('p', tokenQueue.peek());
        tokenQueue.consume();
        Assert.assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.addFirst("first");
        Assert.assertEquals("firsttest", tokenQueue.toString());
    }

    // Add more test cases for other methods mentioned in the TokenQueue class

}