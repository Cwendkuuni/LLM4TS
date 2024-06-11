package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for TokenQueue.
 */
public class TokenQueueTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        TokenQueue queue = new TokenQueue("Hello World!");
        assertFalse(queue.isEmpty());
        assertEquals(13, queue.remainingLength());
    }

    /**
     * Test isEmpty method.
     */
    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue.addFirst('H');
        assertFalse(queue.isEmpty());
    }

    /**
     * Test peek method.
     */
    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("Hello World!");
        assertEquals('H', queue.peek());
        assertEquals('H', consume(queue));
        assertEquals('e', queue.peek());
    }

    /**
     * Test addFirst method.
     */
    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("Hello World!");
        queue.addFirst('X');
        assertEquals('X', queue.peek());
        assertEquals('X', consume(queue));
        assertEquals('H', queue.peek());
    }

    /**
     * Test matches method.
     */
    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("Hello World!");
        assertTrue(queue.matches("ll"));
        assertTrue(queue.matches("Hello"));
        assertFalse(queue.matches("World"));
        assertFalse(queue.matches("llo"));
    }

    /**
     * Test matchesAny method.
     */
    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("Hello World!");
        assertTrue(queue.matchesAny("ll", "lo"));
        assertFalse(queue.matchesAny("World"));
        assertFalse(queue.matchesAny("llo", "World"));
    }

    /**
     * Test matchesCS method.
     */
    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Hello World!");
        assertTrue(queue.matchesCS("He"));
        assertFalse(queue.matchesCS("World"));
        assertFalse(queue.matchesCS("Heo"));
    }

    /**
     * Test matchesStartTag method.
     */
    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head></head><body>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    /**
     * Test matchChomp method.
     */
    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdefg");;
        assertTrue(queue.matchChomp("ab"));
        assertEquals("cdefg", queue.toString());

        assertTrue(queue.matchChomp("efg"));
        assertEquals("", queue.toString());
    }

    /**
     * Test matchesWhitespace method.
     */
    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   Hello World!");
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    /**
     * Test matchesWord method.
     */
    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("Hello World!");
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
        assertFalse(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    /**
     * Test consume method.
     */
    private char consume(TokenQueue queue) {
        char result = queue.consume();
        queue.advance();
        return result;
    }

    /**
     * Test consume method with different inputs.
     */
    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("Hello World!");
        assertEquals('H', consume(queue));
        assertEquals('e', consume(queue));
        assertEquals('l', consume(queue));
        assertEquals('l', consume(queue));
        assertEquals('o', consume(queue));
        assertEquals(' ', consume(queue));
        assertEquals('W', consume(queue));
        assertEquals('o', consume(queue));
        assertEquals('r', consume(queue));
        assertEquals('l', consume(queue));
        assertEquals('d', consume(queue));
        assertTrue(queue.isEmpty());
    }

    /**
     * Test consume method with different inputs - empty queue.
     */
    @Test
    public void testConsumeEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertEquals(0, consume(queue));
        assertTrue(queue.isEmpty());
    }

    /**
     * Test consume method with different inputs - negative length queue.
     */
    @Test(expected = IllegalStateException.class)
    public void testConsumeNegativeLength() {
        TokenQueue queue = new TokenQueue("Hello World!");
        queue.consume(15);
    }

    /**
     * Test consume method with different inputs - non-matching sequence.
     */
    @Test(expected = IllegalStateException.class)
    public void testConsumeNonMatching() {
        TokenQueue queue = new TokenQueue("Hello World!");
        queue.consume("abcd");
    }

    /**
     * Test consumeTo method.
     */
    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("Hello World!");
        assertEquals("Hello", queue.consumeTo("llo"));
        assertEquals("World", queue.toString());

        assertEquals("World", queue.consumeTo("d"));
        assertEquals("", queue.toString());
    }

    /**
     * Test consumeTo method with different inputs - empty queue.
     */
    @Test
    public void testConsumeToEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertEquals("", queue.consumeTo(""));
        assertTrue(queue.isEmpty());
    }

    /**
     * Test consumeTo method with different inputs - negative length queue.
     */
    @Test(expected = IllegalStateException.class)
    public void testConsumeToNegativeLength() {
        TokenQueue queue = new TokenQueue("Hello World!");
        queue.consumeTo(-1);
    }

    /**
     * Test consumeTo method with different inputs - non-matching sequence.
     */
    @Test(expected = IllegalStateException.class)
    public void testConsumeToNonMatching() {
        TokenQueue queue = new TokenQueue("Hello World!");
        queue.consumeTo("abcd");
    }

    /**
     * Test consumeToIgnoreCase method.
     */
    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("Hello World!");
        assertEquals("Hello", queue.consumeToIgnoreCase("hello"));
        assertEquals("World", queue.toString());

        assertEquals("World", queue.consumeToIgnoreCase("World"));
        assertEquals("", queue.toString());
    }

    /**
     * Test consumeToAny method.
     */
    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("Hello World!");
        assertEquals("llo", queue.consumeToAny("llo", "World"));
        assertEquals("World", queue.toString());

        assertEquals("World", queue.consumeToAny("World"));
        assertEquals("", queue.toString());
    }

    /**
     * Test consumeToAny method with different inputs - empty queue.
     */
    @Test
    public void testConsumeToAnyEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertEquals("", queue.consumeToAny());
        assertTrue(queue.isEmpty());
    }

    /**
     * Test consumeToAny method with different inputs - non-matching sequence.
     */
    @Test(expected = IllegalStateException.class)
    public void testConsumeToAnyNonMatching() {
        TokenQueue queue = new TokenQueue("Hello World!");
        queue.consumeToAny("abcd");
    }

    /**
     * Test chompBalanced method.
     */
    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(())()");
        assertEquals("(())", queue.chompBalanced('(', ')'));
        assertEquals("()", queue.toString());

        assertEquals("()", queue.chompBalanced('(', ')'));
        assertEquals("", queue.toString());

        assertEquals("()", queue.chompBalanced('(', '('));
        assertEquals("()", queue.toString());

        assertEquals("()", queue.chompBalanced(')', ')'));
        assertEquals("()", queue.toString());

        assertEquals("()", queue.chompBalanced(')', '('));
        assertEquals("()", queue.toString());
    }

    /**
     * Test chompBalanced method with different inputs - empty queue.
     */
    @Test
    public void testChompBalancedEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertEquals("", queue.chompBalanced('(', ')'));
        assertTrue(queue.isEmpty());
    }

    /**
     * Test chompBalanced method with different inputs - unbalanced openers.
     */
    @Test(expected = IllegalStateException.class)
    public void testChompBalancedUnbalancedOpeners() {
        TokenQueue queue = new TokenQueue("((");
        queue.chompBalanced('(', ')');
    }

    /**
     * Test chompBalanced method with different inputs - unbalanced closers.
     */
    @Test(expected = IllegalStateException.class)
    public void testChompBalancedUnbalancedClosers() {
        TokenQueue queue = new TokenQueue("())");
        queue.chompBalanced('(', ')');
    }

    /**
     * Test unescape method.
     */
    @Test
    public void testUnescape() {
        assertEquals("abc\\def\\ghi", TokenQueue.unescape("abc\\def\\ghi"));
        assertEquals("abcd\\ef\\ghi", TokenQueue.unescape("abc\\def\\ghi"));
        assertEquals("abcd\\ef\\ghi", TokenQueue.unescape("abcd\\ef\\ghi"));
    }

    /**
     * Test consumeWhitespace method.
     */
    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   Hello World!");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    /**
     * Test consumeWord method.
     */
    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("Hello World!");
        assertEquals("Hello", queue.consumeWord());
        assertEquals("World", queue.toString());

        assertEquals("World", queue.consumeWord());
        assertEquals("", queue.toString());
    }

    /**
     * Test consumeTagName method.
     */
    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body>");
        assertEquals("html", queue.consumeTagName());
        assertEquals("head", queue.consumeTagName());
        assertEquals("title", queue.consumeTagName());
        assertEquals("Test", queue.toString());

        assertEquals("Test", queue.consumeTagName());
        assertEquals("", queue.toString());
    }

    /**
     * Test consumeElementSelector method.
     */
    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class:hover");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals(".class:hover", queue.toString());

        assertEquals(".class:hover", queue.consumeElementSelector());
        assertEquals("", queue.toString());
    }

    /**
     * Test consumeCssIdentifier method.
     */
    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color: #FF0000; background-color: #00FF00;");
        assertEquals("color", queue.consumeCssIdentifier());
        assertEquals("#FF0000", queue.toString());

        assertEquals("#FF0000", queue.consumeCssIdentifier());
        assertEquals("background-color", queue.toString());

        assertEquals("background-color", queue.consumeCssIdentifier());
        assertEquals("#00FF00", queue.toString());

        assertEquals("#00FF00", queue.consumeCssIdentifier());
        assertEquals("", queue.toString());
    }

    /**
     * Test consumeAttributeKey method.
     */
    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("data-test=\"value\" class=\"class\" id=\"id\"");
        assertEquals("data-test", queue.consumeAttributeKey());
        assertEquals("value", queue.toString());

        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("id", queue.toString());

        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("", queue.toString());
    }
}