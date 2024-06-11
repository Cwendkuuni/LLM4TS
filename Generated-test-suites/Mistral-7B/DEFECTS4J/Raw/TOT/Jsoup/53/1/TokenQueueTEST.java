package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testPeekAndConsumeSingleCharacter() {
        TokenQueue queue = new TokenQueue("abc");
        Assert.assertEquals('a', queue.peek());
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.peek());
        Assert.assertEquals('b', queue.consume());
        Assert.assertEquals('c', queue.peek());
        Assert.assertEquals('c', queue.consume());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testAddFirstAndConsume() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst('x');
        queue.addFirst("def");
        Assert.assertEquals('x', queue.peek());
        Assert.assertEquals('x', queue.consume());
        Assert.assertEquals('d', queue.peek());
        Assert.assertEquals('d', queue.consume());
        Assert.assertEquals('e', queue.peek());
        Assert.assertEquals('e', queue.consume());
        Assert.assertEquals('f', queue.peek());
        Assert.assertEquals('f', queue.consume());
        Assert.assertEquals('a', queue.peek());
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.peek());
        Assert.assertEquals('b', queue.consume());
        Assert.assertEquals('c', queue.peek());
        Assert.assertEquals('c', queue.consume());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcd");
        Assert.assertTrue(queue.matches("ab"));
        Assert.assertTrue(queue.matches("abcd"));
        Assert.assertFalse(queue.matches("ef"));
        Assert.assertFalse(queue.matches("xy"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABcd");
        Assert.assertTrue(queue.matchesCS("AB"));
        Assert.assertTrue(queue.matchesCS("ABcd"));
        Assert.assertFalse(queue.matchesCS("ef"));
        Assert.assertFalse(queue.matchesCS("xy"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcd");
        Assert.assertTrue(queue.matchesAny("ab", "cd"));
        Assert.assertTrue(queue.matchesAny("cd", "ab"));
        Assert.assertTrue(queue.matchesAny("ab", "cd", "ef", "xy"));
        Assert.assertFalse(queue.matchesAny("ef", "xy"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abcd");
        Assert.assertTrue(queue.matchesAny(new char[]{'a', 'b'}));
        Assert.assertTrue(queue.matchesAny(new char[]{'d', 'c'}));
        Assert.assertTrue(queue.matchesAny(new char[]{'a', 'b', 'd', 'c'}));
        Assert.assertFalse(queue.matchesAny(new char[]{'e', 'f'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><body>");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag()); // consumed '<'
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("<html><body><div id=\"test\">hello world</div>");
        Assert.assertTrue(queue.matchChomp("div"));
        Assert.assertEquals(" id=\"test\">hello world", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   ");
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcd");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord()); // consumed 'd'
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchChompFail() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.matchChomp("xyz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        String consumed = queue.consumeTo("(one (two) three)");
        Assert.assertEquals("(one (two) three)", consumed);
        Assert.assertEquals(" four", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("(One (Two) Three) Four");
        String consumed = queue.consumeToIgnoreCase("(one (two) three)");
        Assert.assertEquals("(one (two) three)", consumed);
        Assert.assertEquals(" Four", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToFail() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.consumeTo("xyz");
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        String balanced = queue.chompBalanced('(', ')');
        Assert.assertEquals("one (two) three", balanced);
        Assert.assertEquals(" four", queue.toString());
    }

    @Test
    public void testChompBalancedFail() {
        TokenQueue queue = new TokenQueue("(one (two three) four) five");
        String balanced = queue.chompBalanced('(', '}');
        Assert.assertEquals("", balanced);
        Assert.assertEquals("(one (two three) four) five", queue.toString());
    }

    @Test
    public void testChompBalancedWithEscaped() {
        TokenQueue queue = new TokenQueue("(one ((two) three) four) five");
        String balanced = queue.chompBalanced('(', '}');
        Assert.assertEquals("one ((two) three)", balanced);
        Assert.assertEquals(" four) five", queue.toString());
    }

    @Test
    public void testUnescape() {
        String escaped = "\\(one \\((two)\\) three\\) four";
        String unescaped = TokenQueue.unescape(escaped);
        Assert.assertEquals("(one ((two) three) four)", unescaped);
    }
}