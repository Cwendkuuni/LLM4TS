package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testTokenQueueIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testTokenQueueIsNotEmpty() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        assertEquals('a', tokenQueue.peek());
        assertEquals('a', tokenQueue.consume());
        assertEquals('b', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.addFirst('a');
        assertEquals('a', tokenQueue.consume());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        assertTrue(tokenQueue.matches("bc"));
        assertTrue(tokenQueue.matches("ef"));
        assertFalse(tokenQueue.matches("ab"));
        assertFalse(tokenQueue.matches("g"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("AbCdEf");
        assertTrue(tokenQueue.matchesCS("Ab"));
        assertTrue(tokenQueue.matchesCS("Cd"));
        assertFalse(tokenQueue.matchesCS("AbX"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("abcdefg");
        assertTrue(tokenQueue.matchesAny("ab", "cd"));
        assertTrue(tokenQueue.matchesAny("bc", "efg"));
        assertFalse(tokenQueue.matchesAny("h"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   abc   def  ");
        assertTrue(tokenQueue.matchesWhitespace());
        tokenQueue.advance();
        tokenQueue.advance();
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance();
        assertTrue(tokenQueue.matchesWord());
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        assertEquals('a', tokenQueue.consume());
        assertEquals('b', tokenQueue.consume());
        assertEquals('c', tokenQueue.consume());
        assertEquals('d', tokenQueue.consume());
        assertEquals('e', tokenQueue.consume());
        assertEquals('f', tokenQueue.consume());
        assertNull(tokenQueue.consume());
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        assertEquals('a', tokenQueue.consume());
        assertEquals('b', tokenQueue.consume());
        assertEquals('c', tokenQueue.consume());
        assertEquals('d', tokenQueue.consume());
        assertEquals('e', tokenQueue.consume());
        assertEquals('f', tokenQueue.consume());
        assertNull(tokenQueue.consume());
    }

    @Test
    public void testConsumeCS() {
        TokenQueue tokenQueue = new TokenQueue("AbCdEf");
        String consumed = tokenQueue.consumeCS("Ab");
        assertEquals("Ab", consumed);
        String consumed2 = tokenQueue.consumeCS("Cd");
        assertEquals("Cd", consumed2);
        String consumed3 = tokenQueueQueue.consumeCS("Xy");
        assertNull(consumed3);
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        String consumed = tokenQueue.chompBalanced('(', ')');
        assertEquals("one (two) three", consumed);
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        String consumed = tokenQueue.chompBalanced('(', ')');
        assertEquals("one (two) three", consumed);

        tokenQueue = new TokenQueue("(one {two} three) four");
        String consumed2 = tokenQueue.chompBalanced('{', '}');
        assertEquals("one {two} three", consumed2);

        tokenQueue = new TokenQueue("(one <two> three) four");
        String consumed3 = tokenQueue.chompBalanced('<', '>');
        assertEquals("one <two> three", consumed3);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        String consumed = tokenQueue.consumeToIgnoreCase("onE");
        assertEquals("(one (two) three)", consumed);
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("(one <two> three) four <five> six <seven> eight");
        String consumed = tokenQueue.consumeToAny("two", "seven");
        assertEquals("(one <two> three) four <five> six", consumed);
    }

    @Test
    public void testChompTo() {
        TokenQueue tokenQueue = new TokenQueue("one two three four");
        String consumed = tokenQueue.chompTo("two");
        assertEquals("one", consumed);
    }

    @Test
    public void testChompToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("One Two Three Four");
        String consumed = tokenQueue.chompToIgnoreCase("Two");
        assertEquals("One", consumed);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   one  two   three   four   ");
        assertTrue(tokenQueue.consumeWhitespace());
        tokenQueue.advance();
        tokenQueue.advance();
        tokenQueue.advance();
        assertFalse(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("one two three four");
        String consumed = tokenQueue.consumeWord();
        assertEquals("one", consumed);
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("<div class='test' id='test'>Content</div>");
        String consumed = tokenQueue.consumeTagName();
        assertEquals("div", consumed);
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("div.test > p.class2:first-child {color: red;}");
        String consumed = tokenQueue.consumeElementSelector();
        assertEquals("div.test", consumed);
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("div.test > p.class2.test2:first-child {color: red;}");
        String consumed = tokenQueue.consumeCssIdentifier();
        assertEquals("test2", consumed);
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("<a href='https://www.google.com'>Google</a>");
        String consumed = tokenQueue.consumeAttributeKey();
        assertEquals("href", consumed);
    }
}