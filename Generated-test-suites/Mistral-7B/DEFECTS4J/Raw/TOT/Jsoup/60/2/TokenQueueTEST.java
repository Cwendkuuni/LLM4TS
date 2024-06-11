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
        TokenQueue queue = new TokenQueue("abcd");
        Assert.assertEquals('a', queue.peek());
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.peek());
        Assert.assertEquals('b', queue.consume());
        Assert.assertEquals('c', queue.peek());
        Assert.assertEquals('c', queue.consume());
        Assert.assertEquals('d', queue.peek());
        Assert.assertEquals('d', queue.consume());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abcd");
        queue.addFirst('x');
        Assert.assertEquals('x', queue.peek());
        Assert.assertEquals('x', queue.consume());
        Assert.assertEquals('a', queue.peek());
        Assert.assertEquals('a', queue.consume());
        // ... rest of the test case
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdabc");
        Assert.assertTrue(queue.matches("bc"));
        Assert.assertFalse(queue.matches("ef"));
        // ... rest of the test case
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCDABC");
        Assert.assertTrue(queue.matchesCS("BC"));
        Assert.assertFalse(queue.matchesCS("ef"));
        // ... rest of the test case
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdabc");
        Assert.assertTrue(queue.matchesAny("bc", "ef"));
        Assert.assertTrue(queue.matchesAny("ef", "bc"));
        Assert.assertFalse(queue.matchesAny("xy", "ef"));
        // ... rest of the test case
    }

    @Test
    public void testMatchesAnyWithSingleCharacter() {
        TokenQueue queue = new TokenQueue("abcdabc");
        Assert.assertTrue(queue.matchesAny('b'));
        Assert.assertTrue(queue.matchesAny('c'));
        Assert.assertFalse(queue.matchesAny('x'));
        // ... rest of the test case
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abcd>");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag()); // consume '<'
        // ... rest of the test case
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdabc");
        Assert.assertTrue(queue.matchChomp("bc"));
        Assert.assertEquals("cdabc", queue.toString());
        // ... rest of the test case
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abcd   ");
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWhitespace()); // consume whitespace
        // ... rest of the test case
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcdabc");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord()); // consume word character
        // ... rest of the test case
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcdabc");
        queue.advance();
        Assert.assertEquals('b', queue.peek());
        queue.advance();
        Assert.assertEquals('c', queue.peek());
        // ... rest of the test case
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdabc");
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.consume());
        // ... rest of the test case
    }

    @Test
    public void testConsumeString() {
        TokenQueue queue = new TokenQueue("abcdabc");
        String seq = "bc";
        queue.consume(seq);
        Assert.assertEquals("cdabc", queue.toString());
        // ... rest of the test case
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        String seq = ")";
        String data = queue.consumeTo(seq);
        Assert.assertEquals("one (two) three", data);
        Assert.assertEquals(" four", queue.toString());
        // ... rest of the test case
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        String data = queue.chompBalanced('(', ')');
        Assert.assertEquals("one (two) three", data);
        Assert.assertEquals(" four", queue.toString());
        // ... rest of the test case
    }
}