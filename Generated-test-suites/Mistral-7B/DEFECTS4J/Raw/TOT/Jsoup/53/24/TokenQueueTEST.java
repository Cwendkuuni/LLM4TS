package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testEmptyTokenQueue() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
        Assert.assertFalse(queue.matches("abc"));
        Assert.assertFalse(queue.matchesCS("ABC"));
        Assert.assertFalse(queue.matchesAny("ab", 'a'));
        Assert.assertFalse(queue.matchesStartTag());
        Assert.assertFalse(queue.matchChomp("ab"));
        Assert.assertFalse(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWord());
        Assert.assertFalse(queue.advance());
        Assert.assertEquals(0, queue.consume());
        Assert.assertThrows(IllegalArgumentException.class, () -> queue.consume("abc"));
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testBasicTokenQueue() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals('a', queue.peek());
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.peek());
        Assert.assertEquals('b', queue.consume());
        Assert.assertEquals('c', queue.peek());
        Assert.assertEquals('c', queue.consume());
        Assert.assertEquals('d', queue.peek());
        Assert.assertEquals('d', queue.consume());
        Assert.assertEquals('e', queue.peek());
        Assert.assertEquals('e', queue.consume());
        Assert.assertEquals('f', queue.peek());
        Assert.assertEquals('f', queue.consume());
        Assert.assertTrue(queue.isEmpty());
        Assert.assertFalse(queue.matches("abc"));
        Assert.assertFalse(queue.matchesCS("ABC"));
        Assert.assertFalse(queue.matchesAny("ab", 'a'));
        Assert.assertFalse(queue.matchesAny("cd", 'd'));
        Assert.assertFalse(queue.matchesStartTag());
        Assert.assertFalse(queue.matchChomp("ab"));
        Assert.assertFalse(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWord());
        Assert.assertFalse(queue.advance());
        Assert.assertEquals(0, queue.consume());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testAdvanceAndConsume() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertEquals('a', queue.peek());
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.peek());
        queue.advance();
        Assert.assertEquals('b', queue.peek());
        Assert.assertEquals('b', queue.consume());
        Assert.assertEquals('c', queue.peek());
        Assert.assertEquals('c', queue.consume());
        Assert.assertEquals('d', queue.peek());
        Assert.assertEquals('d', queue.consume());
        Assert.assertEquals('e', queue.peek());
        Assert.assertEquals('e', queue.consume());
        Assert.assertEquals('f', queue.peek());
        Assert.assertEquals('f', queue.consume());
        Assert.assertTrue(queue.isEmpty());
        Assert.assertFalse(queue.matches("abc"));
        Assert.assertFalse(queue.matchesCS("ABC"));
        Assert.assertFalse(queue.matchesAny("ab", 'a'));
        Assert.assertFalse(queue.matchesAny("cd", 'd'));
        Assert.assertFalse(queue.matchesStartTag());
        Assert.assertFalse(queue.matchChomp("ab"));
        Assert.assertFalse(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWord());
        Assert.assertFalse(queue.advance());
        Assert.assertEquals(0, queue.consume());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertFalse(queue.matches("xyz"));
        Assert.assertFalse(queue.matchesCS("XYZ"));
        Assert.assertFalse(queue.matchesAny("xy", 'x'));
        Assert.assertFalse(queue.matchesAny("cd", 'd'));
        Assert.assertFalse(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWord());
        Assert.assertFalse(queue.matchesAny(new String[]{"ab", "cd"}, 'a'));
        queue.addFirst("abc");
        Assert.assertTrue(queue.matches("abc"));
        Assert.assertTrue(queue.matchesCS("ABC"));
        queue.addFirst("def");
        Assert.assertTrue(queue.matchesAny("abc", "def"));
        queue.addFirst("xyz");
        Assert.assertFalse(queue.matches("abc"));
        Assert.assertFalse(queue.matchesCS("ABC"));
        Assert.assertFalse(queue.matchesAny("abc", "def"));
        Assert.assertTrue(queue.matches("xyz"));
        Assert.assertTrue(queue.matchesCS("XYZ"));
        Assert.assertTrue(queue.matchesAny("xyz", "xyz"));
    }

    @Test
    public void testChomp() {
        TokenQueue queue = new TokenQueue("(abc)def");
        Assert.assertFalse(queue.matches("xyz"));
        Assert.assertFalse(queue.matchesCS("XYZ"));
        Assert.assertFalse(queue.matchesAny("xy", 'x'));
        Assert.assertFalse(queue.matchesAny("cd", 'd'));
        Assert.assertFalse(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWord());
        Assert.assertFalse(queue.matchChomp("ab"));
        Assert.assertTrue(queue.matchesBalanced('(', ')'));
        Assert.assertEquals("(abc)def", queue.toString());
        queue.matchChomp("(");
        Assert.assertEquals("abc)def", queue.toString());
        queue.matchChomp(")def");
        Assert.assertEquals("abc", queue.toString());
        queue.addFirst("xyz");
        Assert.assertEquals("xyzabc", queue.toString());
        queue.matchChomp("xyz");
        Assert.assertEquals("abc", queue.toString());
        queue.addFirst("(x)yz");
        Assert.assertEquals("(x)yzabc", queue.toString());
        queue.matchChomp("(");
        Assert.assertEquals("x)yzabc", queue.toString());
        queue.matchChomp("x)yz");
        Assert.assertEquals("yzabc", queue.toString());
        queue.matchChomp("yz");
        Assert.assertEquals("zabc", queue.toString());
        queue.matchChomp("z");
        Assert.assertEquals("abc", queue.toString());
        queue.matchChomp("a");
        Assert.assertEquals("bc", queue.toString());
        queue.matchChomp("b");
        Assert.assertEquals("c", queue.toString());
        queue.matchChomp("c");
        Assert.assertEquals("", queue.toString());
        Assert.assertTrue(queue.isEmpty());
        Assert.assertFalse(queue.matches("xyz"));
        Assert.assertFalse(queue.matchesCS("XYZ"));
        Assert.assertFalse(queue.matchesAny("xy", 'x'));
        Assert.assertFalse(queue.matchesAny("cd", 'd'));
        Assert.assertFalse(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWord());
        Assert.assertFalse(queue.advance());
        Assert.assertEquals(0, queue.consume());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeInvalidSequence() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.consume("xyz");
    }

    @Test
    public void testConsumeValidSequence() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.consume("abc");
        Assert.assertEquals("def", queue.toString());
        queue.consume("def");
        Assert.assertTrue(queue.isEmpty());
    }
}