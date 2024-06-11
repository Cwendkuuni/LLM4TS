package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for {@link TokenQueue}
 */
public class TokenQueueTest {

    @Test
    public void testNewInstance() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertEquals(data, queue.queue);
        assertEquals(0, queue.pos);
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        String data = "abcdefg";
        queue = new TokenQueue(data);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertEquals('a', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.peek();
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        assertEquals("a", queue.queue.substring(0, 1));
        assertEquals('a', queue.peek());
    }

    @Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("ab");
        assertEquals("ab", queue.queue.substring(0, 2));
        assertEquals('a', queue.peek());
    }

    @Test
    public void testMatches() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matches("a"));
        assertTrue(queue.matches("bc"));
        assertFalse(queue.matches("xyz"));
    }

    @Test
    public void testMatchesCS() {
        String data = "ABCdefg";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesCS("A"));
        assertFalse(queue.matchesCS("xyz"));
    }

    @Test
    public void testMatchesAny() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesAny("a", "b"));
        assertTrue(queue.matchesAny("b", "a"));
        assertFalse(queue.matchesAny("xyz"));
    }

    @Test
    public void testMatchesAnyChars() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesAny('a', 'b'));
        assertTrue(queue.matchesAny('b', 'a'));
        assertFalse(queue.matchesAny('x', 'y'));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<tag attr=\"value\"";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        String data = "<tag attr=\"value\"";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchChomp("<tag"));
    }

    @Test
    public void testMatchesWhitespace() {
        String data = "   abc   def";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "abc def ghi";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        queue.advance();
        assertEquals('b', queue.peek());
    }

    @Test
    public void testConsume() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertEquals('a', queue.consume());
        assertEquals('b', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        queue.consume();
    }

    @Test
    public void testConsumeString() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("abc", queue.consume("abc"));
        assertEquals('d', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeMismatchSequence() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);        queue.consume("xyz");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceNotLongEnough() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);        queue.consume("ab");
        queue.consume("cd");
    }

    @Test
    public void testConsumeTo() {
        String data = "<tag attr=\"value\"";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("tag attr=\"", queue.consumeTo("tag attr="));
        assertEquals("value\"", queue.queue.substring(queue.pos));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "<TAG attr=\"value\"";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("TAG attr=", queue.consumeToIgnoreCase("TAG attr="));
        assertEquals("value\"", queue.queue.substring(queue.pos));
    }

    @Test
    public void testConsumeToAny() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("abc", queue.consumeToAny("ab", "cd"));
        assertEquals("defg", queue.queue.substring(queue.pos));
    }

    @Test
    public void testChompBalanced() {
        String data = "(one (two) three) four";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        assertEquals(" four", queue.queue.substring(queue.pos));
    }

    @Test
    public void testUnescape() {
        String data = "\\(one \\(two\\) three\\) four";
        String unescaped = TokenQueue.unescape(data);
        assertEquals("(one (two) three) four", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = "   abc   def";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testConsumeWord() {
        String data = "abc def ghi";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("abc", queue.consumeWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testConsumeTagName() {
        String data = "<tag attr=\"value\"";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("tag", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "<tag:namespace|attr=\"value\"";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("tag:namespace", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = ".class:attrName";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("class", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "attrName=\"value\"";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("attrName", queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("defg", queue.remainder());
    }

    @Test
    public void testToString() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("defg", queue.toString());
    }
}