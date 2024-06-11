package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the TokenQueue class.
 */
public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
        Assert.assertFalse(queue.matches("abc"));
        Assert.assertFalse(queue.matchesCS("ABC"));
        Assert.assertFalse(queue.matchesAny("abc", "def"));
        Assert.assertFalse(queue.matchesAny(new char[]{'a', 'b'}));
        Assert.assertFalse(queue.matchesStartTag());
        Assert.assertFalse(queue.matchChomp("abc"));
        Assert.assertFalse(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWord());
    }

    @Test
    public void testSimpleAddAndConsume() {
        TokenQueue queue = new TokenQueue("abc123");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals('a', queue.peek());
        queue.addFirst('X');
        Assert.assertEquals('X', queue.peek());
        queue.consume();
        Assert.assertEquals('a', queue.peek());
        queue.consume('a');
        queue.consume('b');
        queue.consume('1');
        queue.consume('2');
        queue.consume('3');
        Assert.assertEquals('c', queue.peek());
        queue.consume();
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc123");
        Assert.assertFalse(queue.matches("xyz"));
        Assert.assertTrue(queue.matches("ab"));
        Assert.assertTrue(queue.matchesCS("Ab"));
        Assert.assertTrue(queue.matchesAny("ab", "cd"));
        Assert.assertTrue(queue.matchesAny(new char[]{'a', 'b', 'c'}));
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertTrue(queue.matchChomp("ab"));
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchesWord());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidConsume() {
        TokenQueue queue = new TokenQueue("abc123");
        queue.consume("def");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abc(def)123");
        String consumed = queue.consumeTo("(");
        Assert.assertEquals("abc", consumed);
        Assert.assertEquals('(', queue.peek());
        String consumed1 = queue.consumeTo(")");
        Assert.assertEquals("def", consumed1);
        Assert.assertEquals('1', queue.peek());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("AbCdEfG(hIjK)lMnOpQr");
        String consumed = queue.consumeToIgnoreCase("AbCd");
        Assert.assertEquals("AbCd", consumed);
        Assert.assertEquals('E', queue.peek());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcDEFgHiJklMnOpQrStUvWxYz");
        String consumed = queue.consumeToAny("ef", "ghi");
        Assert.assertEquals("DEFgHi", consumed);
        Assert.assertEquals('J', queue.peek());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("({a (b)c})d");
        String consumed = queue.chompBalanced('{', '}');
        Assert.assertEquals("(a (b)c)", consumed);
        Assert.assertEquals('d', queue.peek());
    }

    @Test
    public void testUnescape() {
        String escaped = "\\(\\)(\\w+)\\:\\/(\\w+)\\(([^)]+)\\)";
        String unescaped = TokenQueue.unescape(escaped);
        Assert.assertEquals("(*)(word+)://(group)", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        boolean seen = queue.consumeWhitespace();
        Assert.assertTrue(seen);
        Assert.assertEquals('a', queue.peek());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("word1 word2 word3");
        String word = queue.consumeWord();
        Assert.assertEquals("word1", word);
        Assert.assertEquals(' ', queue.peek());
        word = queue.consumeWord();
        Assert.assertEquals("word2", word);
        Assert.assertEquals(' ', queue.peek());
        word = queue.consumeWord();
        Assert.assertEquals("word3", word);
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("div class=\"class1\" id=\"id1\"");
        String tagName = queue.consumeTagName();
        Assert.assertEquals("div", tagName);
        Assert.assertEquals(' ', queue.peek());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class1.class2[attr=\"value\"]");
        String selector = queue.consumeElementSelector();
        Assert.assertEquals("div.class1", selector);
        Assert.assertEquals('.', queue.peek());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class:class1 id:id1");
        String identifier = queue.consumeCssIdentifier();
        Assert.assertEquals("class1", identifier);
        Assert.assertEquals(' ', queue.peek());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class:class1 id:id1 attr:attr1");
        String key = queue.consumeAttributeKey();
        Assert.assertEquals("attr", key);
        Assert.assertEquals(':', queue.peek());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abc123");
        String remainder = queue.remainder();
        Assert.assertEquals("123", remainder);
        Assert.assertTrue(queue.isEmpty());
    }
}