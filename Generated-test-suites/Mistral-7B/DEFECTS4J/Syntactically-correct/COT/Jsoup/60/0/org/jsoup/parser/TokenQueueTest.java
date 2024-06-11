package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for TokenQueue.
 */
public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        String data = "abcdef<xyz>12345<ABC>";
        tokenQueue = new TokenQueue(data);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testTokenQueueConstructor() {
        TokenQueue tokenQueue = new TokenQueue("data");
        assertNotNull(tokenQueue.queue);
    }

    /**
     * Test isEmpty method.
     */
    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());

        for (int i = 0; i < tokenQueue.remainingLength(); i++) {
            tokenQueue.consume();
        }

        assertTrue(tokenQueue.isEmpty());
    }

    /**
     * Test peek method.
     */
    @Test
    public void testPeek() {
        assertEquals(tokenQueue.peek(), 'a');
    }

    /**
     * Test addFirst method with char.
     */
    @Test
    public void testAddFirstChar() {
        tokenQueue.addFirst('z');
        assertEquals(tokenQueue.peek(), 'z');
    }

    /**
     * Test addFirst method with String.
     */
    @Test
    public void testAddFirstString() {
        tokenQueue.addFirst("efg");
        assertEquals(tokenQueue.peek(), 'e');
    }

    /**
     * Test matches method.
     */
    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("ab"));
        assertFalse(tokenQueue.matches("xy"));
    }

    /**
     * Test matchesCS method.
     */
    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("ab"));
        assertFalse(tokenQueue.matchesCS("XY"));
    }

    /**
     * Test matchesAny method with String array.
     */
    @Test
    public void testMatchesAnyString() {
        assertTrue(tokenQueue.matchesAny("ab", "xy"));
        assertFalse(tokenQueue.matchesAny("ef", "xy"));
    }

    /**
     * Test matchesAny method with char array.
     */
    @Test
    public void testMatchesAnyChar() {
        assertTrue(tokenQueue.matchesAny('a', 'b'));
        assertFalse(tokenQueue.matchesAny('e', 'b'));
    }

    /**
     * Test matchesStartTag method.
     */
    @Test
    public void testMatchesStartTag() {
        tokenQueue.advance();
        assertTrue(tokenQueue.matchesStartTag());
    }

    /**
     * Test matchChomp method.
     */
    @Test
    public void testMatchChomp() {
        tokenQueue.advance();
        tokenQueue.advance();
        tokenQueue.matchChomp("<xyz>");
        assertEquals(tokenQueue.peek(), '1');
    }

    /**
     * Test matchesWhitespace method.
     */
    @Test
    public void testMatchesWhitespace() {
        tokenQueue.advance();
        assertTrue(tokenQueue.matchesWhitespace());
    }

    /**
     * Test matchesWord method.
     */
    @Test
    public void testMatchesWord() {
        tokenQueue.advance();
        assertTrue(tokenQueue.matchesWord());
    }

    /**
     * Test advance method.
     */
    @Test
    public void testAdvance() {
        tokenQueue.advance();
        assertEquals(tokenQueue.peek(), 'b');
    }

    /**
     * Test consume method.
     */
    @Test
    public void testConsume() {
        char consumed = tokenQueue.consume();
        assertEquals(consumed, 'a');
        assertEquals(tokenQueue.peek(), 'b');
    }

    /**
     * Test consume method with String.
     */
    @Test
    public void testConsumeString() {
        String consumed = tokenQueue.consume("ef");
        assertEquals(consumed, "ef");
        assertEquals(tokenQueue.peek(), 'g');
    }

    /**
     * Test consumeTo method.
     */
    @Test
    public void testConsumeTo() {
        String consumed = tokenQueue.consumeTo("xy");
        assertEquals(consumed, "xyz");
        assertEquals(tokenQueue.peek(), 'A');
    }

    /**
     * Test consumeToIgnoreCase method.
     */
    @Test
    public void testConsumeToIgnoreCase() {
        String consumed = tokenQueue.consumeToIgnoreCase("XY");
        assertEquals(consumed, "ABC");
        assertEquals(tokenQueue.peek(), '1');
    }

    /**
     * Test consumeToAny method with String array.
     */
    @Test
    public void testConsumeToAnyString() {
        String consumed = tokenQueue.consumeToAny("ef", "AB");
        assertEquals(consumed, "ef");
        assertEquals(tokenQueue.peek(), '1');
    }

    /**
     * Test chompBalanced method.
     */
    @Test
    public void testChompBalanced() {
        String consumed = tokenQueue.chompBalanced('(', ')');
        assertEquals(consumed, "abc");
        assertEquals(tokenQueue.peek(), '1');
    }

    /**
     * Test unescape method.
     */
    @Test
    public void testUnescape() {
        String unescaped = TokenQueue.unescape("\\(abc\\)def\\\\");
        assertEquals(unescaped, "abcdef");
    }

    /**
     * Test consumeWhitespace method.
     */
    @Test
    public void testConsumeWhitespace() {
        tokenQueue.consumeWhitespace();
        assertTrue(tokenQueue.matches("g"));
    }

    /**
     * Test consumeWord method.
     */
    @Test
    public void testConsumeWord() {
        String consumed = tokenQueue.consumeWord();
        assertEquals(consumed, "efg");
        assertEquals(tokenQueue.peek(), 'A');
    }

    /**
     * Test consumeTagName method.
     */
    @Test
    public void testConsumeTagName() {
        String consumed = tokenQueue.consumeTagName();
        assertEquals(consumed, "ABC");
        assertEquals(tokenQueue.peek(), '1');
    }

    /**
     * Test consumeElementSelector method.
     */
    @Test
    public void testConsumeElementSelector() {
        String consumed = tokenQueue.consumeElementSelector();
        assertEquals(consumed, "ABC\\*:\\*");
        assertEquals(tokenQueue.peek(), '1');
    }

    /**
     * Test consumeCssIdentifier method.
     */
    @Test
    public void testConsumeCssIdentifier() {
        String consumed = tokenQueue.consumeCssIdentifier();
        assertEquals(consumed, "ID");
        assertEquals(tokenQueue.peek(), '1');
    }

    /**
     * Test consumeAttributeKey method.
     */
    @Test
    public void testConsumeAttributeKey() {
        String consumed = tokenQueue.consumeAttributeKey();
        assertEquals(consumed, "attribute");
        assertEquals(tokenQueue.peek(), '1');
    }

    /**
     * Test remainder method.
     */
    @Test
    public void testRemainder() {
        String remainder = tokenQueue.remainder();
        assertEquals(remainder, "12345");
        assertEquals(tokenQueue.isEmpty(), true);
    }
}