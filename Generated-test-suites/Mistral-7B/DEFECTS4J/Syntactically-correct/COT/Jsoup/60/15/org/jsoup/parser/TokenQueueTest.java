package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test cases for {@link TokenQueue} class.
 */
public class TokenQueueTest {

    @Test
    public void testTokenQueueConstruction() {
        String data = "abcd1234efgh";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals(data, tokenQueue.queue);
    }

    @Test(expected = NullPointerException.class)
    public void testTokenQueueWithNullData() {
        new TokenQueue(null);
    }

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());

        String data = "abcd1234efgh";
        tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "abcd1234efgh";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertEquals('a', tokenQueue.peek());
        assertEquals('a', tokenQueue.consume());
        assertEquals('b', tokenQueue.peek());
    }

    @Test
    public void testPeekAndConsume() {
        String data = "abcd1234efgh";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertEquals('a', tokenQueue.peek());
        assertEquals('a', tokenQueue.consume());
        assertEquals('b', tokenQueue.peek());
        assertEquals('b', tokenQueue.consume());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.addFirst('a');
        assertEquals("a", tokenQueue.queue.substring(0, 1));
        assertEquals('a', tokenQueue.peek());
        assertEquals('a', tokenQueue.consume());

        tokenQueue.addFirst("xyz");
        assertEquals("xyz", tokenQueue.queue.substring(0, 3));
    }

    @Test
    public void testMatches() {
        String data = "abcd1234efgh";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertTrue(tokenQueue.matches("ab"));
        assertTrue(tokenQueue.matches("cd"));
        assertFalse(tokenQueue.matches("pq"));
    }

    @Test
    public void testMatchesCS() {
        String data = "ABCD1234efgh";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertTrue(tokenQueue.matchesCS("AB"));
        assertTrue(tokenQueue.matchesCS("CD"));
        assertFalse(tokenQueue.matchesCS("PQ"));
    }

    @Test
    public void testMatchesAny() {
        String data = "abcd1234efgh";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertTrue(tokenQueue.matchesAny("ab", "cd"));
        assertFalse(tokenQueue.matchesAny("pq", "xy"));
    }

    @Test
    public void testMatchesAnyChar() {
        String data = "abcd1234efgh";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertTrue(tokenQueue.matchesAny('a', 'b', 'c'));
        assertFalse(tokenQueue.matchesAny('p', 'q', 'z'));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<ab cd ef >gh<i j k>";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertTrue(tokenQueue.matchesStartTag());
        assertFalse(tokenQueue.matchesStartTag()); // consume '<'
        assertFalse(tokenQueue.matchesStartTag()); // consume 'a'
        assertTrue(tokenQueue.matchesStartTag()); // consume 'b'
        assertFalse(tokenQueue.matchesStartTag()); // consume ' '
        assertFalse(tokenQueue.matchesStartTag()); // consume 'c'
        assertFalse(tokenQueue.matchesStartTag()); // consume 'd'
        assertFalse(tokenQueue.matchesStartTag()); // consume 'e'
        assertFalse(tokenQueue.matchesStartTag()); // consume '>'
        assertFalse(tokenQueue.matchesStartTag()); // consume '<'
        assertFalse(tokenQueue.matchesStartTag()); // consume 'i'
    }

    @Test
    public void testMatchChomp() {
        String data = "abcd1234efgh";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertTrue(tokenQueue.matchChomp("ab"));
        assertEquals("cd1234efgh", tokenQueue.queue);

        assertFalse(tokenQueue.matchChomp("pq"));
        assertEquals("cd1234efgh", tokenQueue.queue);
    }

    @Test
    public void testMatchesWhitespace() {
        String data = "   abcd1234efgh   ";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertTrue(tokenQueue.matchesWhitespace());
        assertFalse(tokenQueue.matchesWhitespace()); // consume space
        assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testMatchesWord() {
        String data = " abcd1234efgh ";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertTrue(tokenQueue.matchesWord());
        assertFalse(tokenQueue.matchesWord()); // consume ' '
        assertEquals('a', tokenQueue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithNoMatch() {
        String data = "abcd1234efgh";
        TokenQueue tokenQueue = new TokenQueue(data);

        tokenQueue.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        String data = "abcd1234efgh<xyz>";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertEquals("xyz", tokenQueue.consumeTo("xyz"));
        assertEquals("", tokenQueue.queue);

        assertEquals("efgh<", tokenQueue.consumeTo("ef"));
        assertEquals("gh<", tokenQueue.queue);

        assertEquals("gh<", tokenQueue.consumeToIgnoreCase("gh"));
        assertEquals("", tokenQueue.queue);
    }

    @Test
    public void testConsumeToAny() {
        String data = "abcd1234efgh<xyz>";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertEquals("xyz", tokenQueue.consumeToAny("xy", "zx"));
        assertEquals("", tokenQueue.queue);

        assertEquals("efgh<", tokenQueue.consumeToAny("ef", "gh"));
        assertEquals("", tokenQueue.queue);
    }

    @Test
    public void testChompBalanced() {
        String data = "((((a((b)c))))d)";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertEquals("a((b)c)", tokenQueue.chompBalanced('(', ')'));
        assertEquals("d)", tokenQueue.queue);
    }

    @Test
    public void testUnescape() {
        String data = "\\abc\\def\\";
        String expected = "abcdef\\";

        assertEquals(expected, TokenQueue.unescape(data));
    }

    @Test
    public void testConsumeWhitespace() {
        String data = "   abcd1234efgh   ";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertTrue(tokenQueue.consumeWhitespace());
        assertFalse(tokenQueue.consumeWhitespace()); // consume space
        assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testConsumeWord() {
        String data = " abcd1234efgh ";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertEquals("ab", tokenQueue.consumeWord());
        assertEquals("cd1234efgh", tokenQueue.queue);
    }

    @Test
    public void testConsumeTagName() {
        String data = "<tag name attr='value' other='value'>content</tag>";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertEquals("tag", tokenQueue.consumeTagName());
        assertEquals("name attr='value' other='value'>content", tokenQueue.queue);

        assertEquals("other", tokenQueue.consumeTagName());
        assertEquals("content", tokenQueue.queue);
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "<tag name|namespace attr='value' other='value'>content</tag>";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertEquals("tag", tokenQueue.consumeElementSelector());
        assertEquals("name|namespace attr='value' other='value'>content", tokenQueue.queue);

        assertEquals("other", tokenQueue.consumeElementSelector());
        assertEquals("content", tokenQueue.queue);
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "tag { color: #abcdef; border: 1px solid #ghijk; }";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertEquals("color", tokenQueue.consumeCssIdentifier());
        assertEquals("#abcdef; border: 1px solid #ghijk; ", tokenQueue.queue);

        assertEquals("border", tokenQueue.consumeCssIdentifier());
        assertEquals("1px solid #ghijk; ", tokenQueue.queue);
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "<tag name attr='value' other='value' />";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertEquals("attr", tokenQueue.consumeAttributeKey());
        assertEquals("value", tokenQueue.queue);

        assertEquals("other", tokenQueue.consumeAttributeKey());
        assertEquals("", tokenQueue.queue);
    }

    @Test
    public void testRemainder() {
        String data = "abcd1234efgh";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertEquals("", tokenQueue.remainder());

        tokenQueue.consume("ab");
        assertEquals("cd1234efgh", tokenQueue.queue);
        assertEquals("cd1234efgh", tokenQueue.remainder());

        tokenQueue.consumeTo("ef");
        assertEquals("gh", tokenQueue.queue);
        assertEquals("gh", tokenQueue.remainder());
    }
}