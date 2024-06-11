package org.jsoup.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @BeforeEach
    void setUp() {
        String queue = "This is a test string";
        tokenQueue = new TokenQueue(queue);
    }

    @Test
    void testIsEmpty() {
        TokenQueue emptyQueue = new TokenQueue("");
        assertTrue(emptyQueue.isEmpty());
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    void testPeek() {
        assertEquals('T', tokenQueue.peek());
        // Test empty queue
        TokenQueue emptyQueue = new TokenQueue("");
        assertEquals(0, emptyQueue.peek());
    }

    @Test
    void testAddFirst() {
        tokenQueue.addFirst('A');
        assertEquals('A', tokenQueue.peek());
        tokenQueue.addFirst("First ");
        assertEquals('F', tokenQueue.peek());
        assertEquals("First This is a test string", tokenQueue.toString());
    }

    @Test
    void testMatches() {
        assertTrue(tokenQueue.matches("This"));
        assertFalse(tokenQueue.matches("this"));
    }

    @Test
    void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("This"));
        assertFalse(tokenQueue.matchesCS("this"));
    }

    @Test
    void testMatchesAnyString() {
        assertTrue(tokenQueue.matchesAny("This", "That", "Test"));
        assertFalse(tokenQueue.matchesAny("this", "that", "test"));
    }

    @Test
    void testMatchesAnyChar() {
        assertTrue(tokenQueue.matchesAny('T', 't'));
        assertFalse(tokenQueue.matchesAny('A', 'B'));
    }

    @Test
    void testMatchesStartTag() {
        String startTag = "<test>";
        tokenQueue.addFirst(startTag);
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("This is"));
    }

    @Test
    void testMatchesWhitespace() {
        String whitespace = " \t\n";
        tokenQueue.addFirst(whitespace);
        assertTrue(tokenQueue.matchesWhitespace());
    }

    @Test
    void testMatchesWord() {
        String word = "Testing123";
        tokenQueue.addFirst(word);
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    void testAdvance() {
        tokenQueue.advance();
        assertEquals('h', tokenQueue.peek());
    }

    @Test
    void testConsume() {
        assertEquals('T', tokenQueue.consume());
        assertEquals("his is a test string", tokenQueue.toString());
    }

    @Test
    void testConsumeSequence() {
        tokenQueue.consume("This");
        assertEquals(" is a test string", tokenQueue.toString());
    }

    @Test
    void testConsumeTo() {
        String seq = "is a";
        assertEquals("This ", tokenQueue.consumeTo(seq));
        assertEquals(seq + " test string", tokenQueue.toString());
    }

    @Test
    void testConsumeToIgnoreCase() {
        String seq = "THIS A";
        assertEquals("This is ", tokenQueue.consumeToIgnoreCase(seq));
        assertEquals(seq.toLowerCase() + " test string", tokenQueue.toString());
    }

    @Test
    void testConsumeToAny() {
        String seq1 = "i";
        String seq2 = "j";
        assertEquals("Th", tokenQueue.consumeToAny(seq1, seq2));
        assertEquals("is is a test string", tokenQueue.toString());
    }

    @Test
    void testChompTo() {
        String seq = "is a";
        assertEquals("This ", tokenQueue.chompTo(seq));
        assertEquals(" test string", tokenQueue.toString());
    }

    @Test
    void testChompToIgnoreCase() {
        String seq = "THIS A";
        assertEquals("This is ", tokenQueue.chompToIgnoreCase(seq));
        assertEquals(" test string", tokenQueue.toString());
    }

    @Test
    void testChompBalanced() {
        String open = "(";
        String close = ")";
        tokenQueue.addFirst("(one (two) three) four");
        assertEquals("(one (two) three)", tokenQueue.chompBalanced(open.charAt(0), close.charAt(0)));
    }

    @Test
    void testUnescape() {
        String escaped = "\\(one \\)two\\) three";
        assertEquals("(one )two) three", TokenQueue.unescape(escaped));
    }

    @Test
    void testConsumeWhitespace() {
        tokenQueue.addFirst("     ");
        assertTrue(tokenQueue.consumeWhitespace());
    }

    @Test
    void testConsumeWord() {
        String word = "Testing123";
        tokenQueue.addFirst(word);
        assertEquals(word, tokenQueue.consumeWord());
    }

    @Test
    void testConsumeTagName() {
        String tagName = "div";
        tokenQueue.addFirst(tagName);
        assertEquals(tagName, tokenQueue.consumeTagName());
    }

    @Test
    void testConsumeElementSelector() {
        String elementSelector = "div|test";
        tokenQueue.addFirst(elementSelector);
        assertEquals(elementSelector, tokenQueue.consumeElementSelector());
    }

    @Test
    void testConsumeCssIdentifier() {
        String cssIdentifier = "test-id";
        tokenQueue.addFirst(cssIdentifier);
        assertEquals(cssIdentifier, tokenQueue.consumeCssIdentifier());
    }

    @Test
    void testConsumeAttributeKey() {
        String attributeKey = "data-name";
        tokenQueue.addFirst(attributeKey);
        assertEquals(attributeKey, tokenQueue.consumeAttributeKey());
    }

    @Test
    void testRemainder() {
        assertEquals("This is a test string", tokenQueue.remainder());
        assertTrue(tokenQueue.isEmpty());
    }

}