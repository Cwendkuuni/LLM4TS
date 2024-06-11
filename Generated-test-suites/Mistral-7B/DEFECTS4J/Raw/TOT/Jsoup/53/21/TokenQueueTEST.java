package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the TokenQueue class.
 */
public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testBasicQueueOperations() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);

        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals("abcdefg", queue.toString());

        Assert.assertEquals('a', queue.peek());
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.peek());
        Assert.assertEquals('b', queue.consume());

        queue.addFirst('x');
        Assert.assertEquals('x', queue.consume());
        Assert.assertEquals('b', queue.peek());

        queue.addFirst("ab");
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.consume());
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('b', queue.consume());

        Assert.assertEquals("efg", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdefg");
        Assert.assertFalse(queue.matches("h"));
        Assert.assertTrue(queue.matches("a"));
        Assert.assertTrue(queue.matches("ab"));
        Assert.assertTrue(queue.matches("bc"));
        Assert.assertTrue(queue.matches("defg"));
        Assert.assertTrue(queue.matches(""));

        queue.addFirst("xyz");
        Assert.assertTrue(queue.matches("xyz"));
        Assert.assertTrue(queue.matches(""));
    }

    @Test
    public void testMatchesCaseSensitive() {
        TokenQueue queue = new TokenQueue("AbCdEfG");
        Assert.assertFalse(queue.matchesCS("h"));
        Assert.assertTrue(queue.matchesCS("A"));
        Assert.assertTrue(queue.matchesCS("Ab"));
        Assert.assertTrue(queue.matchesCS("BC"));
        Assert.assertTrue(queue.matchesCS("DefG"));
        Assert.assertTrue(queue.matchesCS(""));

        queue.addFirst("XYZ");
        Assert.assertTrue(queue.matchesCS("XYZ"));
        Assert.assertTrue(queue.matchesCS(""));
    }

    @Test
    public void testMatchesAnyCharacter() {
        TokenQueue queue = new TokenQueue("abcdefg");
        Assert.assertTrue(queue.matchesAny('a', 'b', 'c'));
        Assert.assertTrue(queue.matchesAny('a', 'x', 'g'));
        Assert.assertTrue(queue.matchesAny('z', 'z', 'z'));
        Assert.assertFalse(queue.matchesAny('h', 'i', 'j'));
    }

    @Test
    public void testMatchesAnyString() {
        TokenQueue queue = new TokenQueue("abcdefg");
        Assert.assertTrue(queue.matchesAny("ab", "cd", "efg"));
        Assert.assertTrue(queue.matchesAny("", "xy", "gz"));
        Assert.assertFalse(queue.matchesAny("hij", "ijk", "lmn"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body>");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdefg");
        Assert.assertFalse(queue.matchChomp(""));
        Assert.assertFalse(queue.matchChomp("h"));
        Assert.assertTrue(queue.matchChomp("ab"));
        Assert.assertEquals("cdefg", queue.toString());

        queue.addFirst("xyz");
        Assert.assertFalse(queue.matchChomp(""));
        Assert.assertFalse(queue.matchChomp("h"));
        Assert.assertFalse(queue.matchChomp("ab"));
        Assert.assertEquals("xyzcdefg", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   def   ");
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWhitespace());
        Assert.assertEquals("abc def", queue.toString());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcdefg");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord());
        Assert.assertEquals("defg", queue.toString());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcdefg");
        queue.advance();
        queue.advance();
        queue.advance();
        Assert.assertEquals('d', queue.peek());
        Assert.assertEquals('d', queue.consume());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceMissing() {
        TokenQueue queue = new TokenQueue("abcdefg");
        queue.consume("xyz");
    }

    @Test
    public void testConsumeSequenceMatching() {
        TokenQueue queue = new TokenQueue("abcxyzdefg");
        queue.consume("xyz");
        Assert.assertEquals("defg", queue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("(abc)(defg)hij");
        Assert.assertEquals("(abc)", queue.consumeTo("("));
        Assert.assertEquals("(defg)hij", queue.toString());

        Assert.assertEquals("(defg)", queue.consumeTo(")"));
        Assert.assertEquals("hij", queue.toString());

        Assert.assertEquals("hij", queue.consumeTo("ij"));
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("(AbCdEfG)(HiJkLmN)OpQrS");
        Assert.assertEquals("(AbCdEfG)", queue.consumeToIgnoreCase("("));
        Assert.assertEquals("(HiJkLmN)OpQrS", queue.toString());

        Assert.assertEquals("(HiJkLmN)", queue.consumeToIgnoreCase(")"));
        Assert.assertEquals("OpQrS", queue.toString());

        Assert.assertEquals("OpQrS", queue.consumeToIgnoreCase("op"));
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("(abc)(defg)hij");
        Assert.assertEquals("(abc)", queue.consumeToAny("(", ")"));
        Assert.assertEquals("(defg)hij", queue.toString());

        Assert.assertEquals("(defg)", queue.consumeToAny(")", "("));
        Assert.assertEquals("hij", queue.toString());

        Assert.assertEquals("hij", queue.consumeToAny("ij", "kl"));
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((((((a))))))");
        Assert.assertEquals("a", queue.chompBalanced('(', ')'));
        Assert.assertEquals("((((((a))))))", queue.toString());

        Assert.assertEquals("((((a))))", queue.chompBalanced('(', ')'));
        Assert.assertEquals("(((a))", queue.toString());

        Assert.assertEquals("(((a))", queue.chompBalanced('(', ')'));
        Assert.assertEquals("(a)", queue.toString());

        Assert.assertEquals("(a)", queue.chompBalanced('(', ')'));
        Assert.assertEquals("", queue.toString());

        Assert.assertEquals("", queue.chompBalanced('(', ')'));
        Assert.assertEquals("", queue.toString());
    }
}