package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("example");
        assertEquals('e', queue.peek());
    }

    @Test
    public void testAddFirstCharacter() {
        TokenQueue queue = new TokenQueue("ample");
        queue.addFirst('ex');
        assertEquals("example", queue.toString());
    }

    @Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("ample");
        queue.addFirst("ex");
        assertEquals("example", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("example");
        assertTrue(queue.matches("example"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Example");
        assertTrue(queue.matchesCS("Example"));
    }

    @Test
    public void testMatchesAnyStringArray() {
        TokenQueue queue = new TokenQueue("example");
        assertTrue(queue.matchesAny("ex", "am"));
    }

    @Test
    public void testMatchesAnyCharArray() {
        TokenQueue queue = new TokenQueue("example");
        assertTrue(queue.matchesAny('e', 'a'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<div>");
        assertTrue(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("example");
        assertTrue(queue.matchChomp("exam"));
        assertEquals("ple", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" example");
        assertTrue(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("example");
        assertTrue(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("example");
        queue.advance();
        assertEquals('x', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("example");
        assertEquals('e', queue.consume());
        assertEquals("xample", queue.toString());
    }

    @Test
    public void testConsumeString() {
        TokenQueue queue = new TokenQueue("example");
        queue.consume("exam");
        assertEquals("ple", queue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("example");
        assertEquals("exam", queue.consumeTo("pl"));
        assertEquals("ple", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("Example");
        assertEquals("Exam", queue.consumeToIgnoreCase("p"));
        assertEquals("ple", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("example");
        assertEquals("ex", queue.consumeToAny("am", "pl"));
        assertEquals("ample", queue.toString());
    }

    @Test
    public void testChompTo() {
        TokenQueue queue = new TokenQueue("example");
        assertEquals("exam", queue.chompTo("pl"));
        assertEquals("ple", queue.toString());
    }

    @Test
    public void testChompToIgnoreCase() {
        TokenQueue queue = new TokenQueue("Example");
        assertEquals("Exam", queue.chompToIgnoreCase("p"));
        assertEquals("ple", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        assertEquals(" four", queue.toString());
    }

    @Test
    public void testUnescape() {
        String escapedString = "This is an \\escaped\\ string.";
        String unescapedString = "This is an escaped string.";
        assertEquals(unescapedString, TokenQueue.unescape(escapedString));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   ");
        assertTrue(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("example123");
        assertEquals("example123", queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("example");
        assertEquals("example", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("example");
        assertEquals("example", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("example123");
        assertEquals("example123", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("example123");
        assertEquals("example123", queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("example");
        assertEquals("example", queue.remainder());
        assertTrue(queue.isEmpty());
    }
}