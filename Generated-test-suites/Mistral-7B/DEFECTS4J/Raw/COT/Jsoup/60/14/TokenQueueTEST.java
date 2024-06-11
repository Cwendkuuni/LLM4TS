package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.TokenQueue.*; // for convenience

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        TokenQueue tq = new TokenQueue("hello world");
        Assert.assertFalse(tq.isEmpty());
        Assert.assertEquals("hewllo world", tq.queue);
        Assert.assertEquals(0, tq.pos);
    }

    @Test
    public void testIsEmpty() {
        TokenQueue tq = new TokenQueue("");
        Assert.assertTrue(tq.isEmpty());

        tq = new TokenQueue("abc");
        Assert.assertFalse(tq.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tq = new TokenQueue("abcdef");
        Assert.assertEquals('a', tq.peek());
        tq.advance();
        Assert.assertEquals('b', tq.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekOnEmptyQueue() {
        TokenQueue tq = new TokenQueue("");
        tq.peek();
    }

    @Test
    public void testAddFirst() {
        TokenQueue tq = new TokenQueue("abc");
        tq.addFirst('x');
        Assert.assertEquals("xabc", tq.queue);
        Assert.assertEquals(0, tq.pos);

        tq.addFirst("def");
        Assert.assertEquals("defxabc", tq.queue);
        Assert.assertEquals("def", tq.consume(2).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFirstWithNull() {
        TokenQueue tq = new TokenQueue("abc");
        tq.addFirst(null);
    }

    @Test
    public void testMatches() {
        TokenQueue tq = new TokenQueue("abcdef");
        Assert.assertTrue(tq.matches("bc"));
        Assert.assertFalse(tq.matches("xyz"));

        tq.addFirst("ab");
        Assert.assertTrue(tq.matches("ab"));
        Assert.assertFalse(tq.matches("xyz"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tq = new TokenQueue("ABcDEF");
        Assert.assertTrue(tq.matchesCS("BC"));
        Assert.assertFalse(tq.matchesCS("xyz"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tq = new TokenQueue("abcdef");
        Assert.assertTrue(tq.matchesAny("ab", "ef"));
        Assert.assertFalse(tq.matchesAny("xyz"));

        tq.addFirst("ab");
        Assert.assertTrue(tq.matchesAny("ab", "ef"));
        Assert.assertFalse(tq.matchesAny("xyz"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue tq = new TokenQueue("abcdef");
        Assert.assertTrue(tq.matchesAny('a', 'f'));
        Assert.assertFalse(tq.matchesAny('x', 'y'));

        tq.addFirst("ab");
        Assert.assertTrue(tq.matchesAny('a', 'f'));
        Assert.assertFalse(tq.matchesAny('x', 'y'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tq = new TokenQueue("<tag attr=\"value\" >content</tag>");
        Assert.assertTrue(tq.matchesStartTag());

        tq.advance();
        Assert.assertTrue(tq.matchesStartTag());

        tq.matchChomp("<tag");
        Assert.assertFalse(tq.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tq = new TokenQueue("<tag attr=\"value\" >content</tag>");
        Assert.assertTrue(tq.matchChomp("<tag"));
        Assert.assertEquals("attr=\"value\" >content</tag>", tq.queue);
        Assert.assertEquals(5, tq.pos);

        tq = new TokenQueue("abcdef");
        Assert.assertFalse(tq.matchChomp("xyz"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tq = new TokenQueue(" abc   def ");
        Assert.assertTrue(tq.matchesWhitespace());
        tq.advance();
        Assert.assertFalse(tq.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tq = new TokenQueue("abcdef");
        Assert.assertTrue(tq.matchesWord());
        Assert.assertTrue(tq.matchesWord());
        Assert.assertFalse(tq.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tq = new TokenQueue("abcdef");
        Assert.assertEquals('a', tq.consume());
        Assert.assertEquals('b', tq.consume());
        Assert.assertEquals('c', tq.consume());
        Assert.assertEquals('d', tq.consume());
        Assert.assertEquals('e', tq.consume());
        Assert.assertEquals('f', tq.consume());
        Assert.assertTrue(tq.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeOnEmptyQueue() {
        TokenQueue tq = new TokenQueue("");
        tq.consume();
    }

    @Test
    public void testConsume() {
        TokenQueue tq = new TokenQueue("abcdef");
        String s = tq.consume(3);
        Assert.assertEquals("cde", s);
        Assert.assertEquals('f', tq.consume());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeWithNegativeLength() {
        TokenQueue tq = new TokenQueue("abcdef");
        tq.consume(-1);
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tq = new TokenQueue("abcdefg");
        String s = tq.consumeTo("def");
        Assert.assertEquals("def", s);
        Assert.assertEquals("g", tq.consume());
        Assert.assertEquals("", tq.consumeTo("xyz"));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tq = new TokenQueue("AbCdEfG");
        String s = tq.consumeToIgnoreCase("def");
        Assert.assertEquals("def", s);
        Assert.assertEquals("G", tq.consume());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tq = new TokenQueue("abcdef");
        String s = tq.consumeToAny("xy", "ef");
        Assert.assertEquals("ef", s);
        Assert.assertEquals("", tq.consumeToAny("xyz"));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tq = new TokenQueue("(())((()))");
        String s = tq.chompBalanced('(', ')');
        Assert.assertEquals("()()", s);
        Assert.assertEquals(")", tq.consume());
        Assert.assertEquals(")", tq.consume());
        Assert.assertEquals(")", tq.consume());
        Assert.assertEquals("", tq.consumeTo("xyz"));

        tq = new TokenQueue("(<tag attr=\"value\" >content<tag2 attr=\"value2\" >content2</tag2></tag>");
        s = tq.chompBalanced('<', '>');
        Assert.assertEquals("<tag attr=\"value\" >content<tag2 attr=\"value2\" >content2</tag2>", tq.queue);
        Assert.assertEquals("", tq.consumeTo("xyz"));
    }

    @Test
    public void testUnescape() {
        String s = TokenQueue.unescape("\\(foo\\)bar\\b\\t\\n\\r\\f\\\\");
        Assert.assertEquals("(foo)bar\b\t\n\r\f\\", s);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tq = new TokenQueue("   abc   def  ");
        Assert.assertTrue(tq.consumeWhitespace());
        Assert.assertEquals("abc", tq.queue);
        Assert.assertEquals(3, tq.pos);

        tq = new TokenQueue("   abc   def  ");
        Assert.assertTrue(tq.consumeWhitespace());
        Assert.assertTrue(tq.consumeWhitespace());
        Assert.assertEquals("abc", tq.queue);
        Assert.assertEquals(0, tq.pos);

        tq = new TokenQueue("   abc   def  ");
        Assert.assertFalse(tq.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tq = new TokenQueue("abc123def");
        String s = tq.consumeWord();
        Assert.assertEquals("abc", s);
        Assert.assertEquals("123def", tq.queue);
        Assert.assertEquals(3, tq.pos);

        tq = new TokenQueue("abc123def");
        s = tq.consumeWord();
        Assert.assertEquals("123", s);
        Assert.assertEquals("def", tq.queue);
        Assert.assertEquals(3, tq.pos);

        tq = new TokenQueue("abc123def");
        s = tq.consumeWord();
        Assert.assertEquals("def", s);
        Assert.assertEquals("", tq.queue);
        Assert.assertEquals(6, tq.pos);

        tq = new TokenQueue("abc123def");
        s = tq.consumeWord();
        Assert.assertEquals("", s);
        Assert.assertEquals("", tq.queue);
        Assert.assertEquals(6, tq.pos);
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tq = new TokenQueue("<tag attr=\"value\" >content</tag>");
        String s = tq.consumeTagName();
        Assert.assertEquals("tag", s);
        Assert.assertEquals("attr=\"value\" >content</tag>", tq.queue);
        Assert.assertEquals(7, tq.pos);

        tq = new TokenQueue("class:my-class id:my-id attr:my-attr");
        s = tq.consumeTagName();
        Assert.assertEquals("class", s);
        Assert.assertEquals(":my-class id:my-id attr:my-attr", tq.queue);
        Assert.assertEquals(5, tq.pos);

        tq = new TokenQueue("class:my-class id:my-id attr:my-attr");
        s = tq.consumeTagName();
        Assert.assertEquals("id", s);
        Assert.assertEquals(":my-id attr:my-attr", tq.queue);
        Assert.assertEquals(5, tq.pos);

        tq = new TokenQueue("class:my-class id:my-id attr:my-attr");
        s = tq.consumeTagName();
        Assert.assertEquals("attr", s);
        Assert.assertEquals(":my-attr", tq.queue);
        Assert.assertEquals(4, tq.pos);

        tq = new TokenQueue("class:my-class id:my-id attr:my-attr");
        s = tq.consumeTagName();
        Assert.assertEquals("", s);
        Assert.assertEquals("", tq.queue);
        Assert.assertEquals(4, tq.pos);
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tq = new TokenQueue("tag[class=\"my-class\" id=\"my-id\"]");
        String s = tq.consumeElementSelector();
        Assert.assertEquals("tag", s);
        Assert.assertEquals("[class=\"my-class\" id=\"my-id\"]", tq.queue);
        Assert.assertEquals(6, tq.pos);

        tq = new TokenQueue("tag[class=\"my-class\" id=\"my-id\"]");
        s = tq.consumeElementSelector();
        Assert.assertEquals("class", s);
        Assert.assertEquals("[\"my-class\" id=\"my-id\"]", tq.queue);
        Assert.assertEquals(5, tq.pos);

        tq = new TokenQueue("tag[class=\"my-class\" id=\"my-id\"]");
        s = tq.consumeElementSelector();
        Assert.assertEquals("id", s);
        Assert.assertEquals("\"my-id\"", tq.queue);
        Assert.assertEquals(4, tq.pos);

        tq = new TokenQueue("tag[class=\"my-class\" id=\"my-id\"]");
        s = tq.consumeElementSelector();
        Assert.assertEquals("", s);
        Assert.assertEquals("", tq.queue);
        Assert.assertEquals(4, tq.pos);
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tq = new TokenQueue("tag { color: red; background: #ff0000; }");
        String s = tq.consumeCssIdentifier();
        Assert.assertEquals("color", s);
        Assert.assertEquals(": red; background: #ff0000; ", tq.queue);
        Assert.assertEquals(5, tq.pos);

        tq = new TokenQueue("tag { color: red; background: #ff0000; }");
        s = tq.consumeCssIdentifier();
        Assert.assertEquals("background", s);
        Assert.assertEquals(": #ff0000; ", tq.queue);
        Assert.assertEquals(4, tq.pos);

        tq = new TokenQueue("tag { color: red; background: #ff0000; }");
        s = tq.consumeCssIdentifier();
        Assert.assertEquals("", s);
        Assert.assertEquals("", tq.queue);
        Assert.assertEquals(4, tq.pos);

        tq = new TokenQueue("tag { color: red; background: #ff0000; }");
        s = tq.consumeCssIdentifier();
        Assert.assertEquals("", s);
        Assert.assertEquals("", tq.queue);
        Assert.assertEquals(0, tq.pos);
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tq = new TokenQueue("<tag attr=\"value\" >content</tag>");
        String s = tq.consumeAttributeKey();
        Assert.assertEquals("attr", s);
        Assert.assertEquals("value\" >content</tag>", tq.queue);
        Assert.assertEquals(7, tq.pos);
    }

    @Test
    public void testRemainder() {
        TokenQueue tq = new TokenQueue("abcdefg");
        String s = tq.remainder();
        Assert.assertEquals("g", s);
        Assert.assertEquals("", tq.queue);
        Assert.assertEquals(7, tq.pos);

        tq = new TokenQueue("abcdefg");
        s = tq.remainder();
        Assert.assertEquals("", s);
        Assert.assertEquals("", tq.queue);
        Assert.assertEquals(7, tq.pos);
    }
}