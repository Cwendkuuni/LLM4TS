package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("");
    }

    @Test
    public void testIsEmpty() {
        assertTrue(tokenQueue.isEmpty());
        tokenQueue.addFirst("abc");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals(0, tokenQueue.peek());

        tokenQueue.addFirst("abc");
        assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst("abc");
        assertEquals("abc", tokenQueue.toString());

        tokenQueue.addFirst('d');
        assertEquals("dabc", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        assertFalse(tokenQueue.matches("abc"));

        tokenQueue.addFirst("abc");
        assertTrue(tokenQueue.matches("abc"));
    }

    @Test
    public void testMatchesCS() {
        assertFalse(tokenQueue.matchesCS("abc"));

        tokenQueue.addFirst("abc");
        assertTrue(tokenQueue.matchesCS("abc"));
    }

    @Test
    public void testMatchesAnyStringArray() {
        assertFalse(tokenQueue.matchesAny("abc", "def"));

        tokenQueue.addFirst("abc");
        assertTrue(tokenQueue.matchesAny("abc", "def"));
    }

    @Test
    public void testMatchesAnyCharArray() {
        assertFalse(tokenQueue.matchesAny('a', 'b', 'c'));

        tokenQueue.addFirst("abc");
        assertTrue(tokenQueue.matchesAny('a', 'b', 'c'));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());

        tokenQueue.addFirst("<a");
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertFalse(tokenQueue.matchChomp("abc"));

        tokenQueue.addFirst("abc");
        assertTrue(tokenQueue.matchChomp("abc"));
    }

    @Test
    public void testMatchesWhitespace() {
        assertFalse(tokenQueue.matchesWhitespace());

        tokenQueue.addFirst(" ");
        assertTrue(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        assertFalse(tokenQueue.matchesWord());

        tokenQueue.addFirst("a");
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        tokenQueue.advance();
        assertEquals("", tokenQueue.toString());

        tokenQueue.addFirst("abc");
        tokenQueue.advance();
        assertEquals("bc", tokenQueue.toString());
    }

    @Test
    public void testConsume() {
        assertEquals(0, tokenQueue.consume());

        tokenQueue.addFirst("abc");
        assertEquals('a', tokenQueue.consume());
    }

    @Test
    public void testConsumeString() {
        tokenQueue.consume("abc");
        assertEquals("", tokenQueue.toString());

        tokenQueue.addFirst("abc");
        tokenQueue.consume("bc");
        assertEquals("a", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("", tokenQueue.consumeTo("abc"));

        tokenQueue.addFirst("abcdefgh");
        assertEquals("abcdef", tokenQueue.consumeTo("gh"));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("", tokenQueue.consumeToIgnoreCase("abc"));

        tokenQueue.addFirst("ABCDEFGH");
        assertEquals("abc", tokenQueue.consumeToIgnoreCase("def"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("", tokenQueue.consumeToAny("abc", "def"));

        tokenQueue.addFirst("abcdefgh");
        assertEquals("abcd", tokenQueue.consumeToAny("efg", "hij"));
    }

    @Test
    public void testChompTo() {
        assertEquals("", tokenQueue.chompTo("abc"));

        tokenQueue.addFirst("abcdefgh");
        assertEquals("abcdef", tokenQueue.chompTo("gh"));
    }

    @Test
    public void testChompToIgnoreCase() {
        assertEquals("", tokenQueue.chompToIgnoreCase("abc"));

        tokenQueue.addFirst("ABCDEFGH");
        assertEquals("abc", tokenQueue.chompToIgnoreCase("def"));
    }

    @Test
    public void testChompBalanced() {
        assertEquals("", tokenQueue.chompBalanced('(', ')'));

        tokenQueue.addFirst("(one (two) three) four");
        assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        assertEquals("\\", TokenQueue.unescape("\\\\"));
        assertEquals("abc", TokenQueue.unescape("abc"));
    }

    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());

        tokenQueue.addFirst(" ");
        assertTrue(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        assertEquals("", tokenQueue.consumeWord());

        tokenQueue.addFirst("abc");
        assertEquals("abc", tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("", tokenQueue.consumeTagName());

        tokenQueue.addFirst("abc:");
        assertEquals("abc", tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        assertEquals("", tokenQueue.consumeElementSelector());

        tokenQueue.addFirst("abc:");
        assertEquals("abc", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("", tokenQueue.consumeCssIdentifier());

        tokenQueue.addFirst("abc_");
        assertEquals("abc_", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        assertEquals("", tokenQueue.consumeAttributeKey());

        tokenQueue.addFirst("abc-");
        assertEquals("abc-", tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        assertEquals("", tokenQueue.remainder());

        tokenQueue.addFirst("abc");
        assertEquals("abc", tokenQueue.remainder());
    }
}