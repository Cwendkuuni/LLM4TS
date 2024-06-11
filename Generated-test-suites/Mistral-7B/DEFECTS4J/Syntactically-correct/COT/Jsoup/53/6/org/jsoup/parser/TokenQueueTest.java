package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        String data = "test data";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals(data, queue.queue);
        Assert.assertEquals(0, queue.pos);
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());

        queue = new TokenQueue("abcdef");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertEquals('a', queue.peek());
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddFirstAndConsume() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst("xyz");
        queue.consume();
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertTrue(queue.matches("ab"));
        Assert.assertFalse(queue.matches("efg"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCDef");
        Assert.assertTrue(queue.matchesCS("AB"));
        Assert.assertFalse(queue.matchesCS("efg"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertTrue(queue.matchesAny("a", "b"));
        Assert.assertTrue(queue.matchesAny("b", "c"));
        Assert.assertFalse(queue.matchesAny("g", "h"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertTrue(queue.matchesAny(new char[]{'a', 'b'}));
        Assert.assertTrue(queue.matchesAny(new char[]{'b', 'c'}));
        Assert.assertFalse(queue.matchesAny(new char[]{'g', 'h'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abc>def");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abc<def>");
        Assert.assertTrue(queue.matchChomp("<def>"));
        Assert.assertEquals(0, queue.pos);
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" test data ");
        Assert.assertTrue(queue.matchesWhitespace());
        queue.advance();
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertTrue(queue.matchesWord());
        queue.advance();
        Assert.assertTrue(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.advance();
        Assert.assertEquals('b', queue.consume());
        queue.advance();
        Assert.assertEquals('c', queue.consume());
        queue.advance();
        Assert.assertEquals('d', queue.consume());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.consume());
        Assert.assertEquals('c', queue.consume());
        Assert.assertEquals('d', queue.consume());
        Assert.assertEquals('e', queue.consume());
        Assert.assertEquals('f', queue.consume());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume();
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithUnexpectedSequence() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        String consumed = queue.consumeTo("(two)");
        Assert.assertEquals("one (two)", consumed);
        Assert.assertEquals("three) four", queue.queue.substring(queue.pos));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("(One (Two) Three) Four");
        String consumed = queue.consumeToIgnoreCase("(two)");
        Assert.assertEquals("One (Two)", consumed);
        Assert.assertEquals("Three) Four", queue.queue.substring(queue.pos));
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdefg");
        String consumed = queue.consumeToAny("a", "b", "c");
        Assert.assertEquals("abc", consumed);
        Assert.assertEquals("defg", queue.queue.substring(queue.pos));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        String chomp = queue.chompBalanced('(', ')');
        Assert.assertEquals("one (two) three", chomp);
        Assert.assertEquals(" four", queue.queue.substring(queue.pos));
    }

    @Test
    public void testUnescape() {
        String in = "\\(one \\(two\\) three\\) four";
        String out = TokenQueue.unescape(in);
        Assert.assertEquals("(one (two) three) four", out);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" test data ");
        boolean seen = queue.consumeWhitespace();
        Assert.assertTrue(seen);
        Assert.assertEquals(" test data ", queue.queue.substring(queue.pos));
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcdef");
        String consumed = queue.consumeWord();
        Assert.assertEquals("abc", consumed);
        Assert.assertEquals("def", queue.queue.substring(queue.pos));
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<div class=\"foo bar\"></div>");
        String consumed = queue.consumeTagName();
        Assert.assertEquals("div", consumed);
        Assert.assertEquals("class=\"foo bar\"", queue.queue.substring(queue.pos));
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.foo.bar span.baz");
        String consumed = queue.consumeElementSelector();
        Assert.assertEquals("div.foo.bar", consumed);
        Assert.assertEquals("span.baz", queue.queue.substring(queue.pos));
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("id=\"foo\" class=\"bar baz\"");
        String consumed = queue.consumeCssIdentifier();
        Assert.assertEquals("foo", consumed);
        Assert.assertEquals(" class=\"bar baz\"", queue.queue.substring(queue.pos));
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("id=\"foo\" class=\"bar baz\" data-baz=\"qux\"");
        String consumed = queue.consumeAttributeKey();
        Assert.assertEquals("id", consumed);
        Assert.assertEquals(" class=\"bar baz\" data-baz=\"qux\"", queue.queue.substring(queue.pos));
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        String remainder = queue.remainder();
        Assert.assertEquals("def", remainder);
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertEquals("def", queue.toString());
        Assert.assertTrue(queue.isEmpty());
    }
}