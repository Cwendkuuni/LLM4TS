package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("test string");
    }

    @Test
    public void testIsEmpty() {
        boolean isEmpty = tokenQueue.isEmpty();
        assertFalse(isEmpty);
    }

    @Test
    public void testPeek() {
        char peek = tokenQueue.peek();
        assertEquals('t', peek);
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('a');
        char peek = tokenQueue.peek();
        assertEquals('a', peek);
    }

    @Test
    public void testAddFirstString() {
        tokenQueue.addFirst("add ");
        char peek = tokenQueue.peek();
        assertEquals('a', peek);
    }

    @Test
    public void testMatches() {
        boolean matches = tokenQueue.matches("test");
        assertTrue(matches);
    }

    @Test
    public void testMatchesCS() {
        boolean matchesCS = tokenQueue.matchesCS("Test");
        assertFalse(matchesCS);
    }

    @Test
    public void testMatchesAny() {
        boolean matchesAny = tokenQueue.matchesAny("test", "abc");
        assertTrue(matchesAny);
    }

    @Test
    public void testMatchChomp() {
        boolean matchChomp = tokenQueue.matchChomp("test");
        assertTrue(matchChomp);
    }

    @Test
    public void testMatchesWhitespace() {
        boolean matchesWhitespace = tokenQueue.matchesWhitespace();
        assertFalse(matchesWhitespace);
    }

    @Test
    public void testMatchesWord() {
        boolean matchesWord = tokenQueue.matchesWord();
        assertTrue(matchesWord);
    }

    @Test
    public void testAdvance() {
        tokenQueue.advance();
        char peek = tokenQueue.peek();
        assertEquals('e', peek);
    }

    @Test
    public void testConsume() {
        char consume = tokenQueue.consume();
        assertEquals('t', consume);
    }

    @Test
    public void testConsumeSeq() {
        tokenQueue.consume("test");
        char peek = tokenQueue.peek();
        assertEquals(' ', peek);
    }

    @Test
    public void testConsumeTo() {
        String consumeTo = tokenQueue.consumeTo(" ");
        assertEquals("test", consumeTo);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String consumeToIgnoreCase = tokenQueue.consumeToIgnoreCase("TE");
        assertEquals("test ", consumeToIgnoreCase);
    }

    @Test
    public void testConsumeToAny() {
        String consumeToAny = tokenQueue.consumeToAny("s", " ");
        assertEquals("te", consumeToAny);
    }

    @Test
    public void testChompTo() {
        String chompTo = tokenQueue.chompTo("t");
        assertEquals("", chompTo);
    }

    @Test
    public void testChompToIgnoreCase() {
        String chompToIgnoreCase = tokenQueue.chompToIgnoreCase("TE");
        assertEquals("", chompToIgnoreCase);
    }

    @Test
    public void testChompBalanced() {
        String chompBalanced = tokenQueue.chompBalanced('t', 's');
        assertEquals("es", chompBalanced);
    }

    @Test
    public void testUnescape() {
        String unescape = TokenQueue.unescape("\\\\");
        assertEquals("\\", unescape);
    }

    @Test
    public void testConsumeWhitespace() {
        boolean consumeWhitespace = tokenQueue.consumeWhitespace();
        assertFalse(consumeWhitespace);
    }

    @Test
    public void testConsumeWord() {
        String consumeWord = tokenQueue.consumeWord();
        assertEquals("test", consumeWord);
    }

    @Test
    public void testConsumeTagName() {
        String consumeTagName = tokenQueue.consumeTagName();
        assertEquals("test", consumeTagName);
    }

    @Test
    public void testConsumeElementSelector() {
        String consumeElementSelector = tokenQueue.consumeElementSelector();
        assertEquals("test", consumeElementSelector);
    }

    @Test
    public void testConsumeCssIdentifier() {
        String consumeCssIdentifier = tokenQueue.consumeCssIdentifier();
        assertEquals("test", consumeCssIdentifier);
    }

    @Test
    public void testConsumeAttributeKey() {
        String consumeAttributeKey = tokenQueue.consumeAttributeKey();
        assertEquals("test", consumeAttributeKey);
    }

    @Test
    public void testRemainder() {
        String remainder = tokenQueue.remainder();
        assertEquals("t string", remainder);
    }

    @Test
    public void testToString() {
        String toString = tokenQueue.toString();
        assertEquals("test string", toString);
    }
}