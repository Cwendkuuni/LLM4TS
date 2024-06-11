package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

/**
 * Test class for TokenQueue.
 */
public class TokenQueueTest {

    @Test
    public void testConstructor() {
        String data = "data";
        TokenQueue queue = new TokenQueue(data);
        assertEquals(data, queue.queue);
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("data");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abcd");
        assertEquals('a', queue.peek());
        queue.advance();
        assertEquals('b', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekWhenEmpty() {
        TokenQueue queue = new TokenQueue("");
        queue.peek();
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        assertEquals('a', queue.consume());
        assertEquals(0, queue.remainingLength());

        queue.addFirst("ab");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals(0, queue.remainingLength());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("ef"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCDEF");
        assertTrue(queue.matchesCS("Ab"));
        assertFalse(queue.matchesCS("ef"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("1234abcd");
        assertTrue(queue.matchesAny("12", "3d"));
        assertFalse(queue.matchesAny("ef"));
    }

    @Test
    public void testMatchesAnyWithSingleCharacter() {
        TokenQueue queue = new TokenQueue("1234abcd");
        assertTrue(queue.matchesAny('1'));
        assertTrue(queue.matchesAny('3'));
        assertTrue(queue.matchesAny('d'));
        assertFalse(queue.matchesAny('e'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<ab>cd");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdabcd");
        assertTrue(queue.matchChomp("ab"));
        assertEquals(5, queue.remainingLength());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("  abc  def ");
        assertTrue(queue.matchesWhitespace());
        queue.advance();
        assertTrue(queue.matchesWhitespace());
        queue.advance();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcd123efg");
        assertTrue(queue.matchesWord());
        queue.advance();
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals('d', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('f', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeSequence() {
        TokenQueue queue = new TokenQueue("abcdabcd");
        String consumed = queue.consume(
                "ab");
        assertEquals("cd", consumed);
        assertEquals(5, queue.remainingLength());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNonMatchedSequence() {
        TokenQueue queue = new TokenQueue("abcdabcd");
        queue.consume("ef");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("((one (two) three) four)");
        String consumed = queue.consumeTo("one (");
        assertEquals("(one (two) three)", consumed);
        assertEquals(" four)", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("((one (two) three) four)");
        String consumed = queue.consumeToIgnoreCase("One (TWO)");
        assertEquals("(one (two) three)", consumed);
        assertEquals(" four)", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("((one (two) three) four)");
        String consumed = queue.consumeToAny("one", "THREE");
        assertEquals("(one (two)", consumed);
        assertEquals(" three) four)", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((one (two) three) four)");
        String consumed = queue.chompBalanced('(', ')');
        assertEquals("one (two) three", consumed);
        assertEquals(" four)", queue.toString());
    }

    @Test
    public void testUnescape() {
        String escaped = "\\(one \\(two\\) three\\) four";
        String unescaped = TokenQueue.unescape(escaped);
        assertEquals("(one (two) three) four", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("  abc  def ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcd123efg");
        String consumed = queue.consumeWord();
        assertEquals("abcd", consumed);
        assertEquals("123efg", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<tag name='value' attr='value'>content</tag>");
        String consumed = queue.consumeTagName();
        assertEquals("tag", consumed);
        assertEquals(" name='value' attr='value'>content", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("<tag name='value' class='class' id='id'>content</tag>");
        String consumed = queue.consumeElementSelector();
        assertEquals("tag", consumed);
        assertEquals("<tag name='value' class='class' id='id'>content", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("id='class' class='id'");
        String consumed = queue.consumeCssIdentifier();
        assertEquals("class", consumed);
        assertEquals("id='id' class='id'", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("id='class' class='id'");
        String consumed = queue.consumeAttributeKey();
        assertEquals("id", consumed);
        assertEquals("class='id'", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("((one (two) three) four)");
        String remainder = queue.remainder();
        assertEquals(" four)", remainder);
        assertTrue(queue.isEmpty());
    }
}