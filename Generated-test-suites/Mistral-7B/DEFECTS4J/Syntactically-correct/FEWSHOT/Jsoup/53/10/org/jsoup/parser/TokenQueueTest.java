package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testTokenQueueIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testTokenQueueIsNotEmpty() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testTokenQueuePeek() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testTokenQueuePeekEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertEquals((char) 0, tokenQueue.peek());
    }

    @Test
    public void testTokenQueueAddFirstString() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.addFirst("def");
        assertEquals("defabc", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueAddFirstCharacter() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.addFirst('d');
        assertEquals("dabc", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueMatches() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertTrue(tokenQueue.matches("a"));
        assertTrue(tokenQueue.matches("bc"));
        assertTrue(tokenQueue.matches("ab"));
        assertFalse(tokenQueue.matches("xyz"));
    }

    @Test
    public void testTokenQueueMatchesCaseSensitive() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertFalse(tokenQueue.matchesCS("a")); // false, as 'a' in the queue is lowercase
        assertTrue(tokenQueue.matchesCS("Ab")); // true, as 'A' in the expected sequence is uppercase
        assertFalse(tokenQueue.matchesCS("AC")); // false, as 'C' is not in the queue
    }

    @Test
    public void testTokenQueueMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertTrue(tokenQueue.matchesAny("a", "b")); // true, as both 'a' and 'b' are in the queue
        assertTrue(tokenQueue.matchesAny("b", "c")); // true, as 'b' is in the queue
        assertFalse(tokenQueue.matchesAny("x", "y")); // false, as neither 'x' nor 'y' are in the queue
    }

    @Test
    public void testTokenQueueMatchesAnyChar() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertTrue(tokenQueue.matchesAny('a', 'b')); // true, as both 'a' and 'b' are in the queue
        assertTrue(tokenQueue.matchesAny('b', 'c')); // true, as 'b' is in the queue
        assertFalse(tokenQueue.matchesAny('x', 'y')); // false, as neither 'x' nor 'y' are in the queue
    }

    @Test
    public void testTokenQueueMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<html>");
        assertTrue(tokenQueue.matchesStartTag()); // true, as the queue starts with '<' and the next character is a letter
    }

    @Test
    public void testTokenQueueMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        tokenQueue.matchChomp("ab"); // consume "ab" from the queue and advance the pointer
        assertEquals("cdef", tokenQueue.toString());
        assertTrue(tokenQueue.matches("c")); // 'c' is still in the queue
    }

    @Test
    public void testTokenQueueMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" \t\r\n abc");
        assertTrue(tokenQueue.matchesWhitespace()); // true, as the first character in the queue is a whitespace character
    }

    @Test
    public void testTokenQueueMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("abc123");
        assertTrue(tokenQueue.matchesWord()); // true, as the first character in the queue is a letter or digit
        assertFalse(tokenQueue.matchesWord()); // false, as the queue is now empty
    }

    @Test
    public void testTokenQueueAdvance() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.advance();
        assertEquals("b", tokenQueue.peek().toString());
    }

    @Test
    public void testTokenQueueConsume() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.consume();
        assertEquals("b", tokenQueue.peek().toString());
        assertEquals("bc", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeString() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.consume("ab");
        assertEquals("c", tokenQueue.peek().toString());
        assertEquals("c", tokenQueue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenQueueConsumeUnexpectedSequence() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.consume("xyz");
    }

    @Test
    public void testTokenQueueConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("abcxyz");
        assertEquals("abc", tokenQueue.consumeTo("ab").toString());
        assertEquals("xyz", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("ABCxyz");
        assertEquals("ABC", tokenQueue.consumeToIgnoreCase("ab").toString());
        assertEquals("xyz", tokenQueue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenQueueConsumeToInvalidSequence() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.consumeTo("xyz");
    }

    @Test
    public void testTokenQueueConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("abcxyz");
        assertEquals("abc", tokenQueue.consumeToAny("ab", "cd").toString());
        assertEquals("xyz", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeToAnySingle() {
        TokenQueue tokenQueue = new TokenQueue("abcxyz");
        assertEquals("abc", tokenQueue.consumeToAny("ab").toString());
        assertEquals("xyz", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(abc)(def)ghi");
        assertEquals("abc(def)", tokenQueue.chompBalanced('(', ')').toString());
        assertEquals("ghi", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueUnescape() {
        TokenQueue tokenQueue = new TokenQueue("\\abc\\def\\");
        assertEquals("abcdef\\", TokenQueue.unescape(tokenQueue.toString()));
    }

    @Test
    public void testTokenQueueConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" \t\r\n abc");
        assertTrue(tokenQueue.consumeWhitespace()); // consume all whitespace characters from the queue
        assertEquals("abc", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("abc123");
        assertEquals("abc", tokenQueue.consumeWord().toString());
        assertEquals("123", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("<html>");
        assertEquals("html", tokenQueue.consumeTagName().toString());
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("div#myClass:hover");
        assertEquals("div", tokenQueue.consumeElementSelector().toString());
        assertEquals("#myClass:hover", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("class:myClass");
        assertEquals("class", tokenQueue.consumeCssIdentifier().toString());
        assertEquals("myClass", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("style=\"background-color: red\"");
        assertEquals("style", tokenQueue.consumeAttributeKey().toString());
        assertEquals("background-color: red", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueRemainder() {
        TokenQueue tokenQueue = new TokenQueue("abcxyz");
        assertEquals("xyz", tokenQueue.remainder().toString());
        assertEquals("", tokenQueue.toString());
    }
}