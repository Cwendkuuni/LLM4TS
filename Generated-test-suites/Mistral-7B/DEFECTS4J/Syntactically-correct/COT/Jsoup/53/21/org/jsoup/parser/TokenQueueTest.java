package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;

import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for TokenQueue.
 */
public class TokenQueueTest {

    @Test
    public void testConstructor() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals(data, tokenQueue.queue);
        assertEquals(0, tokenQueue.pos);
    }

    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.isEmpty());
        
        data = "abc";
        tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals('a', tokenQueue.peek());
        assertEquals('a', tokenQueue.consume());
        assertEquals('b', tokenQueue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddFirst() {
        String data = "abc";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("xyz");
    }

    @Test
    public void testMatches() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matches("ab"));
        assertFalse(tokenQueue.matches("pqr"));
        assertFalse(tokenQueue.matches(""));
    }

    @Test
    public void testMatchesCS() {
        String data = "ABCDEF";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesCS("AB"));
        assertFalse(tokenQueue.matchesCS("pqr"));
        assertFalse(tokenQueue.matchesCS(""));
    }

    @Test
    public void testMatchesAny() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesAny("ab", "cd"));
        assertFalse(tokenQueue.matchesAny("pq", "cd"));
        assertFalse(tokenQueue.matchesAny(""));
    }

    @Test
    public void testMatchesAnyWithSingleChar() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesAny('a', 'c'));
        assertFalse(tokenQueue.matchesAny('p', 'c'));
        assertFalse(tokenQueue.matchesAny(''));
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " \t\nabcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWhitespace());
        assertTrue(tokenQueue.advance());
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "012345abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWord());
        assertFalse(tokenQueue.matchesWord());
        assertFalse(tokenQueue.matchesWord());
        tokenQueue.advance();
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.advance();
        assertEquals('b', tokenQueue.peek());
        tokenQueue.advance();
        assertEquals('c', tokenQueue.peek());
        tokenQueue.advance();
        assertEquals('d', tokenQueue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume();
    }

    @Test
    public void testConsume() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals('a', tokenQueue.consume());
        assertEquals('b', tokenQueue.consume());
        assertEquals('c', tokenQueue.consume());
        assertEquals('d', tokenQueue.consume());
        assertEquals('e', tokenQueue.consume());
        assertEquals('f', tokenQueue.consume());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeSubstring() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        String consumed = tokenQueue.consume(String.substring(0, 3));
        assertEquals("abc", consumed);
        assertEquals('d', tokenQueue.consume());
        assertEquals('e', tokenQueue.consume());
        assertEquals('f', tokenQueue.consume());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNotMatched() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume("xyz");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSubstringNotMatched() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume(String.substring(3, 5));
    }

    @Test
    public void testChompTo() {
        String data = "abc (def) ghi (jkl) mno";
        TokenQueue tokenQueue = new TokenQueue(data);
        String consumed = tokenQueue.chompBalanced('(', ')');
        assertEquals("abc (def)", consumed);
        assertEquals("ghi (jkl) mno", tokenQueue.toString());
    }

    @Test
    public void testChompBalancedEmpty() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("", tokenQueue.chompBalanced('(', ')'));
    }

    @Test
    public void testChompBalancedUnmatched() {
        String data = "abc(def";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("", tokenQueue.chompBalanced('(', ')'));
        assertEquals(data, tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        String data = "abc(def)ghi(jkl)mno";
        TokenQueue tokenQueue = new TokenQueue(data);
        String consumed = tokenQueue.chompBalanced('(', ')');
        assertEquals("abc(def)ghi", consumed);
        assertEquals("(jkl)mno", tokenQueue.toString());
    }

    @Test
    public void testChompBalancedNested() {
        String data = "abc(def(ghi(ijk)l)m)no";
        TokenQueue tokenQueue = new TokenQueue(data);
        String consumed = tokenQueue.chompBalanced('(', ')');
        assertEquals("abc(def(ghi(ijk)", consumed);
        assertEquals("(l)m)no", tokenQueue.toString());
    }

    @Test
    public void testChompBalancedEscaped() {
        String data = "abc(\\(def\\)ghi(jkl)mno";
        TokenQueue tokenQueue = new TokenQueue(data);
        String consumed = tokenQueue.chompBalanced('(', ')');
        assertEquals("abc(\\(def\\)ghi", consumed);
        assertEquals("(jkl)mno", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        String data = "\\abc\\def\\";
        String unescaped = TokenQueue.unescape(data);
        assertEquals("abcdef", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = " \t\n abc def";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.consumeWhitespace());
        assertTrue(tokenQueue.consumeWhitespace());
        assertTrue(tokenQueue.consumeWhitespace());
        assertFalse(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        String data = "012345abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        String consumed = tokenQueue.consumeWord();
        assertEquals("0", consumed);
        consumed = tokenQueue.consumeWord();
        assertEquals("1", consumed);
        consumed = tokenQueue.consumeWord();
        assertEquals("2", consumed);
        consumed = tokenQueue.consumeWord();
        assertEquals("3", consumed);
        consumed = tokenQueue.consumeWord();
        assertEquals("a", consumed);
        consumed = tokenQueue.consumeWord();
        assertEquals("b", consumed);
        consumed = tokenQueue.consumeWord();
        assertEquals("c", consumed);
        consumed = tokenQueue.consumeWord();
        assertEquals("d", consumed);
        consumed = tokenQueue.consumeWord();
        assertEquals("e", consumed);
        consumed = tokenQueue.consumeWord();
        assertEquals("f", consumed);
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        String data = "div id='firstClass' class='anotherClass'";
        TokenQueue tokenQueue = new TokenQueue(data);
        String consumed = tokenQueue.consumeTagName();
        assertEquals("div", consumed);
        consumed = tokenQueue.consumeTagName();
        assertEquals("id", consumed);
        assertEquals('=', tokenQueue.peek());
        consumed = tokenQueue.consume(); // consume '='
        consumed = tokenQueue.consume(); // consume ' '
        consumed = tokenQueue.consume(); // consume 'f'
        consumed = tokenQueue.consume(); // consume 'i'
        consumed = tokenQueue.consume(); // consume 'r'
        consumed = tokenQueue.consume(); // consume 's'
        consumed = tokenQueue.consume(); // consume 't'
        consumed = tokenQueue.consume(); // consume 'C'
        consumed = tokenQueue.consume(); // consume 'l'
        consumed = tokenQueue.consume(); // consume 'a'
        consumed = tokenQueue.consume(); // consume 's'
        consumed = tokenQueue.consume(); // consume 's'
        consumed = tokenQueue.consume(); // consume ' '
        consumed = tokenQueue.consume(); // consume 'a'
        consumed = tokenQueue.consume(); // consume 'n'
        consumed = tokenQueue.consume(); // consume 'o'
        consumed = tokenQueue.consume(); // consume 'therClass'
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "div#firstClass.anotherClass input[type='text']";
        TokenQueue tokenQueue = new TokenQueue(data);
        String consumed = tokenQueue.consumeElementSelector();
        assertEquals("div", consumed);
        consumed = tokenQueue.consumeElementSelector();
        assertEquals("#firstClass", consumed);
        consumed = tokenQueue.consumeElementSelector();
        assertEquals(".anotherClass", consumed);
        consumed = tokenQueue.consumeElementSelector();
        assertEquals("input", consumed);
        consumed = tokenQueue.consumeElementSelector();
        assertEquals("[type='text']", consumed);
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "div#firstClass.anotherClass input[type='text'] { color: red; }";
        TokenQueue tokenQueue = new TokenQueue(data);
        String consumed = tokenQueue.consumeCssIdentifier();
        assertEquals("color", consumed);
        consumed = tokenQueue.consumeCssIdentifier();
        assertEquals("red", consumed);
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "div#firstClass.anotherClass input[type='text']";
        TokenQueue tokenQueue = new TokenQueue(data);
        String consumed = tokenQueue.consumeAttributeKey();
        assertEquals("type", consumed);
        consumed = tokenQueue.consumeAttributeKey();
        assertEquals("text", consumed);
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testRemainder() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        String remainder = tokenQueue.remainder();
        assertEquals("def", remainder);
        assertEquals('e', tokenQueue.peek());
        assertEquals('f', tokenQueue.consume());
        assertEquals("", tokenQueue.remainder());
        tokenQueue.addFirst("xyz");
        remainder = tokenQueue.remainder();
        assertEquals("xyzdef", remainder);
    }

    @Test
    public void testToString() {
        String data = "abcdef";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals(data, tokenQueue.toString());
        tokenQueue.consume();
        tokenQueue.consume();
        assertEquals("cdef", tokenQueue.toString());
    }
}