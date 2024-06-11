package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

/**
 * Test class for TokenQueue.
 */
public class TokenQueueTest {

    @Test
    public void testConstructor() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        // No additional assertions needed as constructor doesn't have return type or throws exception
    }

    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.isEmpty());

        data = "example data";
        tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        data = data.charAt(0) + "";
        assertEquals(data, tokenQueue.peek());

        tokenQueue.advance();
        assertEquals("", tokenQueue.peek());
    }

    @Test
    public void testAddFirstString() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        String newData = "new data";
        tokenQueue.addFirst(newData);
        assertEquals(newData + data, tokenQueue.toString());
    }

    @Test
    public void testAddFirstCharacter() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        char newChar = 'a';
        tokenQueue.addFirst(newChar);
        assertEquals(newChar + data, tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "example";
        assertTrue(tokenQueue.matches(seq));
        tokenQueue.advance();
        assertFalse(tokenQueue.matches(seq));
    }

    @Test
    public void testMatchesCS() {
        String data = "Example Data";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "Example";
        assertTrue(tokenQueue.matchesCS(seq));
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesCS(seq));
    }

    @Test
    public void testMatchesAnyString() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        String[] seq = {"example", "data"};
        assertTrue(tokenQueue.matchesAny(seq));
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesAny(seq));
    }

    @Test
    public void testMatchesAnyCharacter() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        char[] seq = {'e', 'd'};
        assertTrue(tokenQueue.matchesAny(seq));
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesAny(seq));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<example>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesStartTag());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchChompWithNoMatch() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "nonexistent";
        tokenQueue.matchChomp(seq);
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWhitespace());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.advance();
        assertEquals(1, tokenQueue.remainingLength());
    }

    @Test
    public void testConsume() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        char c = tokenQueue.consume();
        assertEquals(data.charAt(0), c);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithEmptyQueue() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume();
    }

    @Test
    public void testConsumeString() {
        String data = "example data";
        String seq = "example";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume(seq);
        assertEquals("data", tokenQueue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithNoMatch() {
        String data = "example data";
        String seq = "nonexistent";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume(seq);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithInsufficientData() {
        String data = "example";
        String seq = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume(seq);
    }

    @Test
    public void testConsumeTo() {
        String data = "example data";
        String seq = "example";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeTo(seq);
        assertEquals("example", result);
        assertEquals(" data", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "Example DATA";
        String seq = "example";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeToIgnoreCase(seq);
        assertEquals("example", result);
        assertEquals(" DATA", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        String data = "example data";
        String[] seq = {"example", "data"};
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeToAny(seq);
        assertTrue(StringUtil.isNotEmpty(result));
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testChompTo() {
        String data = "<example>data";
        String seq = "<example>";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.chompTo(seq);
        assertEquals("data", result);
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testChompToIgnoreCase() {
        String data = "<Example>data";
        String seq = "<example>";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.chompToIgnoreCase(seq);
        assertEquals("data", result);
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        String data = "<example>(one (two) three) four";
        char open = '<';
        char close = ')';
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.chompBalanced(open, close);
        assertEquals("one (two) three", result);
        assertEquals(" four", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        String data = "\\\\example\\\\";
        String result = TokenQueue.unescape(data);
        assertEquals("example", result);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = " example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        boolean result = tokenQueue.consumeWhitespace();
        assertTrue(result);
        assertEquals("example data", tokenQueue.toString());
    }

    @Test
    public void testConsumeWord() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeWord();
        assertEquals("example", result);
        assertEquals(" data", tokenQueue.toString());
    }

    @Test
    public void testConsumeTagName() {
        String data = "<exampleTagName>";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeTagName();
        assertEquals("exampleTagName", result);
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "exampleTagName:~foo, barClass, _class";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeElementSelector();
        assertEquals("exampleTagName", result);
        assertEquals(":~foo, barClass, _class", tokenQueue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "exampleId classId _classId";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeCssIdentifier();
        assertEquals("exampleId", result);
        assertEquals(" classId _classId", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "name=\"exampleAttribute\" class=\"exampleClass\" id=\"exampleId\"";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeAttributeKey();
        assertEquals("name", result);
        assertEquals("\"exampleAttribute\" class=\"exampleClass\" id=\"exampleId\"", tokenQueue.toString());
    }

    @Test
    public void testRemainder() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.remainder();
        assertEquals("data", result);
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testToString() {
        String data = "example data";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("data", tokenQueue.toString());
    }
}