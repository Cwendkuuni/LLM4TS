package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("testing");
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
        tokenQueue.addFirst('!');
        assertEquals("!testing", tokenQueue.toString());
    }

    @Test
    public void testAddFirstString() {
        tokenQueue.addFirst("hello");
        assertEquals("hellotesting", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("test"));
        assertFalse(tokenQueue.matches("not"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("TeSt"));
        assertFalse(tokenQueue.matchesCS("TESt"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny("test", "ing"));
        assertFalse(tokenQueue.matchesAny("not", "found"));
    }

    @Test
    public void testMatchesAnyChars() {
        assertTrue(tokenQueue.matchesAny('t', 'i'));
        assertFalse(tokenQueue.matchesAny('f', 'o'));
    }

    @Test
    public void testMatchesStartTag() {
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("test"));
        assertFalse(tokenQueue.matchChomp("not"));
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
        assertEquals("esting", tokenQueue.toString());
    }

    @Test
    public void testConsume() {
        assertEquals('t', tokenQueue.consume());
        assertEquals("esting", tokenQueue.toString());
    }

    @Test
    public void testConsumeString() {
        tokenQueue.consume("test");
        assertEquals("ing", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("t", tokenQueue.consumeTo("e"));
        assertEquals("ing", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("t", tokenQueue.consumeToIgnoreCase("E"));
        assertEquals("ing", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("test", tokenQueue.consumeToAny("oki", "ing"));
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testChompTo() {
        assertEquals("tes", tokenQueue.chompTo("t"));
        assertEquals("ing", tokenQueue.toString());
    }

    @Test
    public void testChompToIgnoreCase() {
        assertEquals("tes", tokenQueue.chompToIgnoreCase("T"));
        assertEquals("ing", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
        assertEquals(" four", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("\\hello", TokenQueue.unescape("\\\\hello"));
    }

    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        assertEquals("testing", tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("testing", tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        assertEquals("testing", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("testing", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        assertEquals("testing", tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        assertEquals("testing", tokenQueue.remainder());
        assertTrue(tokenQueue.isEmpty());
    }

}