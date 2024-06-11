package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

/**
 * Unit tests for {@link TokenQueue}.
 */
public class TokenQueueTest {

    @Test
    public void testConstructor() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertEquals("hello world", tokenQueue.queue);
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
        assertEquals('h', tokenQueue.peek());
        assertEquals('h', tokenQueue.consume());
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        tokenQueue.addFirst('!');
        assertEquals('!', tokenQueue.peek());
        assertEquals('!', tokenQueue.consume());
        assertEquals('h', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertTrue(tokenQueue.matches("ll"));
        assertTrue(tokenQueue.matches("wo"));
        assertFalse(tokenQueue.matches("xx"));

        tokenQueue.advance();
        assertTrue(tokenQueue.matches("ll"));
        assertFalse(tokenQueue.matches("wo"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesNegative() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.matches(-1);
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("Hello World");
        assertTrue(tokenQueue.matchesCS("HELLO"));
        assertFalse(tokenQueue.matchesCS("world"));

        tokenQueue.advance();
        assertFalse(tokenQueue.matchesCS("HELLO"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertTrue(tokenQueue.matchesAny("ll", "wo"));
        assertFalse(tokenQueue.matchesAny("xx"));
        assertFalse(tokenQueue.matchesAny("lll"));

        tokenQueue.advance();
        assertTrue(tokenQueue.matchesAny("ll", "wo"));
        assertFalse(tokenQueue.matchesAny("xx"));
        assertFalse(tokenQueue.matchesAny("lll"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertTrue(tokenQueue.matchesAny('l', 'o'));
        assertFalse(tokenQueue.matchesAny('x'));
        assertFalse(tokenQueue.matchesAny('l'));

        tokenQueue.advance();
        assertTrue(tokenQueue.matchesAny('l', 'o'));
        assertFalse(tokenQueue.matchesAny('x'));
        assertFalse(tokenQueue.matchesAny('l'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<html><head><title>");
        assertTrue(tokenQueue.matchesStartTag());
        assertFalse(tokenQueue.matchesStartTag());

        tokenQueue.advance();
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("hello world<html><head><title>");
        assertTrue(tokenQueue.matchChomp("html"));
        assertEquals("head<title>", tokenQueue.queue);

        tokenQueue.advance();
        assertTrue(tokenQueue.matchChomp("head"));
        assertEquals("title>", tokenQueue.queue);

        tokenQueue.advance();
        assertFalse(tokenQueue.matchChomp("title"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   hello world  ");
        assertTrue(tokenQueue.matchesWhitespace());
        assertTrue(tokenQueue.consumeWhitespace());
        assertTrue(tokenQueue.matchesWhitespace());

        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertTrue(tokenQueue.matchesWord());
        assertTrue(tokenQueue.consumeWord().equals("hello"));

        tokenQueue.advance();
        assertTrue(tokenQueue.matchesWord());
        assertTrue(tokenQueue.consumeWord().equals("world"));

        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        tokenQueue.advance();
        assertEquals('e', tokenQueue.peek());

        tokenQueue.advance();
        assertEquals('w', tokenQueue.peek());

        tokenQueue.advance();
        assertNull(tokenQueue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNegative() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.consume(-1);
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertEquals("hell", tokenQueue.consume(3));
        assertEquals("olo world", tokenQueue.queue);

        tokenQueue.advance();
        assertEquals("olo world", tokenQueue.queue);

        tokenQueue.consume("world");
        assertEquals("", tokenQueue.queue);
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
        assertEquals("four", tokenQueue.queue);

        tokenQueue.advance();
        assertEquals("four", tokenQueue.queue);
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));

        tokenQueue.advance();
        assertEquals("four", tokenQueue.queue);

        tokenQueue.addFirst('(');
        assertEquals("(one (two) three", tokenQueue.queue);
        assertEquals("one", tokenQueue.chompBalanced('(', ')'));
        assertEquals("(two) three", tokenQueue.queue);

        tokenQueue.advance();
        assertEquals("(two) three", tokenQueue.queue);
        assertEquals("two", tokenQueue.chompBalanced('(', ')'));
        assertEquals("three", tokenQueue.queue);
    }

    @Test
    public void testUnescape() {
        assertEquals("\\r\\n\\t\\\\", TokenQueue.unescape("\\r\\n\\t\\\\"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   hello world  ");
        assertTrue(tokenQueue.consumeWhitespace());
        assertTrue(tokenQueue.consumeWhitespace());
        assertFalse(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertEquals("hello", tokenQueue.consumeWord());
        assertEquals("world", tokenQueue.consumeWord());
        assertNull(tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("<html><head><title>");
        assertEquals("html", tokenQueue.consumeTagName());
        assertEquals("head", tokenQueue.consumeTagName());
        assertEquals("title", tokenQueue.consumeTagName());
        assertNull(tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("div#id.class:hover");
        assertEquals("div", tokenQueue.consumeElementSelector());
        assertEquals("id", tokenQueue.consumeElementSelector());
        assertEquals("class", tokenQueue.consumeElementSelector());
        assertEquals(":hover", tokenQueue.consumeElementSelector());
        assertNull(tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("id: id1 id: id2 class: class1 class: class2");
        assertEquals("id1", tokenQueue.consumeCssIdentifier());
        assertEquals("class1", tokenQueue.consumeCssIdentifier());
        assertEquals("id2", tokenQueue.consumeCssIdentifier());
        assertNull(tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("class: class1 class: class2 style: style1 style: style2");
        assertEquals("class1", tokenQueue.consumeAttributeKey());
        assertEquals("style1", tokenQueue.consumeAttributeKey());
        assertEquals("class2", tokenQueue.consumeAttributeKey());
        assertEquals("style2", tokenQueue.consumeAttributeKey());
        assertNull(tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("remainder");
        assertEquals("", tokenQueue.remainder());

        tokenQueue.addFirst('r');
        assertEquals("r", tokenQueue.remainder());

        tokenQueue.addFirst('e');
        assertEquals("re", tokenQueue.remainder());

        tokenQueue.addFirst('m');
        assertEquals("rem", tokenQueue.remainder());

        tokenQueue.addFirst('a');
        assertEquals("remainder", tokenQueue.remainder());
    }

    @Test
    public void testToString() {
        TokenQueue tokenQueue = new TokenQueue("remainder");
        assertEquals("remainder", tokenQueue.toString());

        tokenQueue.addFirst('r');
        assertEquals("rremainder", tokenQueue.toString());

        tokenQueue.addFirst('e');
        assertEquals("eremainder", tokenQueue.toString());

        tokenQueue.addFirst('m');
        assertEquals("remmainder", tokenQueue.toString());

        tokenQueue.addFirst('a');
        assertEquals("remainder", tokenQueue.toString());
    }
}