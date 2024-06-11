package org.jsoup.parser;

import org.junit.Assert; import org.junit.Test;
import org.jsoup.helper.StringUtil;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        Assert.assertFalse(tokenQueue.isEmpty());
        Assert.assertEquals("hello world", tokenQueue.toString());
    }

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());

        tokenQueue = new TokenQueue("hello world");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        Assert.assertEquals('h', tokenQueue.peek());
        Assert.assertEquals('h', tokenQueue.consume());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekOnEmptyQueue() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.peek();
    }

    @Test
    public void testAddFirstCharacter() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        tokenQueue.addFirst('X');
        Assert.assertEquals("Xhello world", tokenQueue.toString());
        Assert.assertEquals('X', tokenQueue.consume());
    }

    @Test
    public void testAddFirstString() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        tokenQueue.addFirst("XYZ");
        Assert.assertEquals("XYZhello world", tokenQueue.toString());
        Assert.assertEquals('X', tokenQueue.consume());
        Assert.assertEquals('Y', tokenQueue.consume());
        Assert.assertEquals('Z', tokenQueue.consume());
        Assert.assertEquals('h', tokenQueue.consume());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertTrue(tokenQueue.matches("hello"));
        assertTrue(tokenQueue.matches("world"));
        assertFalse(tokenQueue.matches("foo"));
        assertFalse(tokenQueue.matches(""));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("Hello World");
        assertTrue(tokenQueue.matchesCS("Hello"));
        assertTrue(tokenQueue.matchesCS("World"));
        assertFalse(tokenQueue.matchesCS("foo"));
        assertFalse(tokenQueue.matchesCS(""));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertTrue(tokenQueue.matchesAny("hello", "world"));
        assertFalse(tokenQueue.matchesAny("foo"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertTrue(tokenQueue.matchesAny('h', 'w'));
        assertFalse(tokenQueue.matchesAny('f'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<html>");
        assertTrue(tokenQueue.matchesStartTag());
        assertFalse(tokenQueue.matchesStartTag()); // consume first character
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("hello <world>");
        assertTrue(tokenQueue.matchChomp("hello"));
        assertTrue(tokenQueue.isEmpty());
        assertTrue(tokenQueue.matchesStartTag());
        assertTrue(tokenQueue.matchChomp("world"));
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   hello world  ");
        assertTrue(tokenQueue.matchesWhitespace());
        tokenQueue.consumeWhitespace();
        assertFalse(tokenQueue.matchesWhitespace());
        Assert.assertEquals('h', tokenQueue.consume());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertTrue(tokenQueue.matchesWord());
        Assert.assertEquals('h', tokenQueue.consume());
        assertTrue(tokenQueue.matchesWord());
        Assert.assertEquals('w', tokenQueue.consume());
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        tokenQueue.advance();
        Assert.assertEquals('h', tokenQueue.consume());
        tokenQueue.advance();
        Assert.assertEquals('e', tokenQueue.consume());
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        Assert.assertEquals('h', tokenQueue.consume());
        Assert.assertEquals('e', tokenQueue.consume());
        Assert.assertEquals('l', tokenQueue.consume());
        Assert.assertEquals('l', tokenQueue.consume());
        Assert.assertEquals('o', tokenQueue.consume());
        Assert.assertEquals(' ', tokenQueue.consume());
        Assert.assertEquals('w', tokenQueue.consume());
        Assert.assertEquals('o', tokenQueue.consume());
        Assert.assertEquals('r', tokenQueue.consume());
        Assert.assertEquals('l', tokenQueue.consume());
        Assert.assertEquals('d', tokenQueue.consume());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeWithNullSequence() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        tokenQueue.consume(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithNonMatchedSequence() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        tokenQueue.consume("foo");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithSequenceShorterThanQueue() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        tokenQueue.consume("hel");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("(hello (world))");
        Assert.assertEquals("hello (world)", tokenQueue.consumeTo("hello "));
        Assert.assertEquals("(world)", tokenQueue.toString());
        Assert.assertEquals("(world)", tokenQueue.consumeTo(""));
        Assert.assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("(Hello (World))");
        Assert.assertEquals("Hello (World)", tokenQueue.consumeToIgnoreCase("HELLO "));
        Assert.assertEquals("(World)", tokenQueue.toString());
        Assert.assertEquals("(World)", tokenQueue.consumeToIgnoreCase(""));
        Assert.assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        Assert.assertEquals("hello", tokenQueue.consumeToAny("hello", "world"));
        Assert.assertEquals("world", tokenQueue.toString());
        Assert.assertEquals("world", tokenQueue.consumeToAny(""));
        Assert.assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(hello (world))");
        Assert.assertEquals("hello (world)", tokenQueue.toString());
        Assert.assertEquals("hello", tokenQueue.chompBalanced('(', ')'));
        Assert.assertEquals("(world)", tokenQueue.toString());
        Assert.assertEquals("", tokenQueue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        String input = "\\\\hello\\ world";
        String output = TokenQueue.unescape(input);
        Assert.assertEquals("hello world", output);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   hello world   ");
        assertTrue(tokenQueue.consumeWhitespace());
        assertTrue(tokenQueue.consumeWhitespace());
        Assert.assertEquals('h', tokenQueue.consume());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        String consumedWord = tokenQueue.consumeWord();
        Assert.assertEquals("hello", consumedWord);
        Assert.assertEquals('w', tokenQueue.consume());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("<html><body><div id='myDiv'>Hello World</div></body></html>");
        String consumedTagName = tokenQueue.consumeTagName();
        Assert.assertEquals("html", consumedTagName);
        Assert.assertEquals("body", tokenQueue.consumeTagName());
        Assert.assertEquals("div", tokenQueue.consumeTagName());
        Assert.assertEquals("myDiv", tokenQueue.consumeCssIdentifier());
        Assert.assertEquals("Hello World", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("<html><body><div id='myDiv' class='myClass'>Hello World</div></body></html>");
        String consumedElementSelector = tokenQueue.consumeElementSelector();
        Assert.assertEquals("div.myClass", consumedElementSelector);
        Assert.assertEquals("Hello World", tokenQueue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("<html><body><div id='myDiv' class='myClass'>Hello World</div></body></html>");
        String consumedCssIdentifier = tokenQueue.consumeCssIdentifier();
        Assert.assertEquals("myDiv", consumedCssIdentifier);
        Assert.assertEquals("myClass", tokenQueue.consumeCssIdentifier());
        Assert.assertEquals("Hello World", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("<html><body><div id='myDiv' class='myClass' style='color:red' data-test='test'>Hello World</div></body></html>");
        String consumedAttributeKey = tokenQueue.consumeAttributeKey();
        Assert.assertEquals("style", consumedAttributeKey);
        Assert.assertEquals("color", tokenQueue.consumeAttributeKey());
        Assert.assertEquals("data-test", tokenQueue.consumeAttributeKey());
        Assert.assertEquals("test", tokenQueue.consumeAttributeKey());
        Assert.assertEquals("Hello World", tokenQueue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        String remainder = tokenQueue.remainder();
        Assert.assertEquals("world", remainder);
        Assert.assertEquals("", tokenQueue.toString());
    }
}