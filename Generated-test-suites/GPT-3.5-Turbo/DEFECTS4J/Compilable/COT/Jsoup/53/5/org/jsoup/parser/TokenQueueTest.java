package org.jsoup.parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {
    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("test");
    }

    @After
    public void tearDown() {
        tokenQueue = null;
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals('t', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('c');
        assertEquals("ctest", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("test"));
        assertFalse(tokenQueue.matches("tst"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("test"));
        assertFalse(tokenQueue.matchesCS("TeSt"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny("test", "tst"));
        assertFalse(tokenQueue.matchesAny("tst", "abc"));
    }

    @Test
    public void testMatchesAnyChar() {
        assertTrue(tokenQueue.matchesAny('t', 'a', 'b'));
        assertFalse(tokenQueue.matchesAny('a', 'b', 'c'));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("test"));
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        tokenQueue.advance();
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        char c = tokenQueue.consume();
        assertEquals('t', c);
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testConsumeString() {
        tokenQueue.consume("tes");
        assertEquals("t", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        String consumed = tokenQueue.consumeTo("s");
        assertEquals("te", consumed);
        assertEquals("t", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String consumed = tokenQueue.consumeToIgnoreCase("S");
        assertEquals("te", consumed);
        assertEquals("t", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        String consumed = tokenQueue.consumeToAny("a", "e");
        assertEquals("t", consumed);
        assertEquals("st", tokenQueue.toString());
    }

    @Test
    public void testChompTo() {
        String data = tokenQueue.chompTo("s");
        assertEquals("te", data);
        assertEquals("t", tokenQueue.toString());
    }

    @Test
    public void testChompToIgnoreCase() {
        String data = tokenQueue.chompToIgnoreCase("S");
        assertEquals("te", data);
        assertEquals("t", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        String balanced = tokenQueue.chompBalanced('(', ')');
        assertEquals("", balanced);
    }

    @Test
    public void testUnescape() {
        String unescaped = TokenQueue.unescape("t\\est");
        assertEquals("test", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        boolean consumed = tokenQueue.consumeWhitespace();
        assertFalse(consumed);
    }

    @Test
    public void testConsumeWord() {
        String word = tokenQueue.consumeWord();
        assertEquals("test", word);
    }

    @Test
    public void testConsumeTagName() {
        String tagName = tokenQueue.consumeTagName();
        assertEquals("test", tagName);
    }

    @Test
    public void testConsumeElementSelector() {
        String elementSelector = tokenQueue.consumeElementSelector();
        assertEquals("test", elementSelector);
    }

    @Test
    public void testConsumeCssIdentifier() {
        String cssIdentifier = tokenQueue.consumeCssIdentifier();
        assertEquals("test", cssIdentifier);
    }

    @Test
    public void testConsumeAttributeKey() {
        String attributeKey = tokenQueue.consumeAttributeKey();
        assertEquals("test", attributeKey);
    }

    @Test
    public void testRemainder() {
        String remainder = tokenQueue.remainder();
        assertEquals("test", remainder);
        assertTrue(tokenQueue.isEmpty());
    }
}