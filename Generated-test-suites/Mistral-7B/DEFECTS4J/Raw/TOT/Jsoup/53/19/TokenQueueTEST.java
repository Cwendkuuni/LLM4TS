package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the TokenQueue class.
 */
public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());

        queue.addFirst("hello");
        Assert.assertFalse(queue.isEmpty());

        queue.consume("hello");
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals('h', queue.peek());
        queue.advance();
        Assert.assertEquals('e', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("hello");
        queue.addFirst("world");
        Assert.assertEquals("worldhello", queue.toString());

        queue.consume("world");
        Assert.assertEquals("hello", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matches("ll"));
        Assert.assertTrue(queue.matches("or"));
        Assert.assertFalse(queue.matches("llo"));
        Assert.assertFalse(queue.matches("dll"));

        queue.advance();
        Assert.assertFalse(queue.matches("or"));
        Assert.assertTrue(queue.matches("ld"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertTrue(queue.matchesCS("Hello"));
        Assert.assertFalse(queue.matchesCS("hello"));
        Assert.assertFalse(queue.matchesCS("World"));
        Assert.assertFalse(queue.matchesCS("world"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matchesAny("ll", "or"));
        Assert.assertTrue(queue.matchesAny("lo", "d"));
        Assert.assertFalse(queue.matchesAny("ld", "or"));

        queue.advance();
        Assert.assertTrue(queue.matchesAny("ld", "l"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matchesAny('l', 'o', 'r'));
        Assert.assertTrue(queue.matchesAny('l', 'd'));
        Assert.assertFalse(queue.matchesAny('d', 'r'));

        queue.advance();
        Assert.assertTrue(queue.matchesAny('l', 'd'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><body>");
        Assert.assertTrue(queue.matchesStartTag());

        queue.consume("<html>");
        Assert.assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("hello <world>");
        Assert.assertTrue(queue.matchChomp("<world>"));
        Assert.assertEquals("hello ", queue.toString());

        queue.consume("hello ");
        Assert.assertFalse(queue.matches("<world>"));
        Assert.assertFalse(queue.matchChomp("<world>"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   hello world   ");
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.matchesWhitespace());

        queue.consume("   ");
        Assert.assertFalse(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord());

        queue.consume("he");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.advance();
        Assert.assertEquals("e", queue.toString());

        queue.consume("e");
        queue.advance();
        Assert.assertEquals("l", queue.toString());

        queue.consume("l");
        queue.advance();
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals("hello", queue.consume(6, "world"));
        Assert.assertEquals("world", queue.toString());

        queue.consume("world");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("hello <world>");
        Assert.assertEquals("hello ", queue.consumeTo(" "));
        Assert.assertEquals("world", queue.toString());

        queue.consume("world");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("HELLO <world>");
        Assert.assertEquals("HELLO ", queue.consumeToIgnoreCase(" "));
        Assert.assertEquals("world", queue.toString());

        queue.consume("world");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("hello world <foo bar>");
        Assert.assertEquals("hello world", queue.consumeToAny(" foo", " bar"));
        Assert.assertEquals("<foo bar>", queue.toString());

        queue.consume("<foo bar>");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(hello (world) three) four");
        Assert.assertEquals("hello (world) three", queue.chompBalanced('(', ')'));
        Assert.assertEquals("four", queue.toString());

        queue.consume("(hello (world) three) four");
        Assert.assertEquals("four", queue.toString());
    }

    @Test
    public void testUnescape() {
        Assert.assertEquals("abc\\def\\ghi", TokenQueue.unescape("abc\\def\\ghi"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   hello world   ");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("hello world", queue.toString());

        queue.consume("hello world");
        Assert.assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals("hello", queue.consumeWord());
        Assert.assertEquals("world", queue.toString());

        queue.consume("hello");
        Assert.assertEquals("world", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><body><tag name='value'></tag>");
        Assert.assertEquals("tag", queue.consumeTagName());
        Assert.assertEquals("name='value'", queue.toString());

        queue.consume("name='value'");
        Assert.assertEquals(">", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("<html><body><tag name='value' class='class1 class2'></tag>");
        Assert.assertEquals("tag", queue.consumeElementSelector());
        Assert.assertEquals("name='value' class='class1 class2'", queue.toString());

        queue.consume("name='value' class='class1 class2'");
        Assert.assertEquals(">", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("id=\"class1 class2_class3\" class=\"class4 class5 class6\"");
        Assert.assertEquals("class1", queue.consumeCssIdentifier());
        Assert.assertEquals("class2_class3 class=\"class4 class5 class6\"", queue.toString());

        queue.consume("class2_class3 class=\"class4 class5 class6\"");
        Assert.assertEquals("class4", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("<html><body id='id' class='class' attr='attrValue'></body>");
        Assert.assertEquals("id", queue.consumeAttributeKey());
        Assert.assertEquals("class='class' attr='attrValue'", queue.toString());

        queue.consume("class='class' attr='attrValue'");
        Assert.assertEquals("attr", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals("world", queue.remainder());
        Assert.assertEquals("", queue.toString());

        queue.consume("hello");
        Assert.assertEquals("world", queue.remainder());
        Assert.assertEquals("", queue.toString());
    }
}