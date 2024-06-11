package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

/**
 * Tests for the TokenQueue class.
 */
public class TokenQueueTest {

    @Test
    public void testTokenQueueCreation() {
        String data = "abcdefg";
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
        String data = "abcdefg";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals('a', tokenQueue.peek());
        assertEquals(0, tokenQueue.pos);

        tokenQueue.advance();
        assertEquals('a', tokenQueue.peek());
        assertEquals(1, tokenQueue.pos);
    }

    @Test
    public void testAddFirst() {
        String data = "abcdefg";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.matches("x"));

        tokenQueue.addFirst("x");
        tokenQueue.advance();
        assertTrue(tokenQueue.matches("x"));
        assertEquals("xabcdefg", tokenQueue.queue);
        assertEquals(1, tokenQueue.pos);
    }

    @Test
    public void testMatches() {
        String data = "abcdefg";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertFalse(tokenQueue.matches("x"));
        assertFalse(tokenQueue.matches("ab"));
        assertTrue(tokenQueue.matches("a"));
        assertTrue(tokenQueue.matches("abc"));
        assertTrue(tokenQueue.matches("defg"));
    }

    @Test
    public void testMatchesCS() {
        String data = "abcABCdefg";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertFalse(tokenQueue.matchesCS("x"));
        assertFalse(tokenQueue.matchesCS("ab"));
        assertTrue(tokenQueue.matchesCS("a"));
        assertTrue(tokenQueue.matchesCS("abc"));
        assertTrue(tokenQueue.matchesCS("ABC"));
        assertFalse(tokenQueue.matchesCS("DEFg"));
    }

    @Test
    public void testMatchesAny() {
        String data = "abcdefg";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertFalse(tokenQueue.matchesAny("x", "y"));
        assertFalse(tokenQueue.matchesAny("xy"));
        assertTrue(tokenQueue.matchesAny("a", "a"));
        assertTrue(tokenQueue.matchesAny("ab", "c"));
        assertTrue(tokenQueue.matchesAny("defg", "g"));
    }

    @Test
    public void testMatchesAny_char() {
        String data = "abcdefg";
        TokenQueue tokenQueue = new TokenQueue(data);

        assertFalse(tokenQueue.matchesAny('x', 'y'));
        assertFalse(tokenQueue.matchesAny('xy'));
        assertTrue(tokenQueue.matchesAny('a', 'a'));
        assertTrue(tokenQueue.matchesAny('a', 'b'));
        assertTrue(tokenQueue.matchesAny('d', 'g'));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<abcdefg>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesStartTag());
        assertEquals(1, tokenQueue.pos);
    }

    @Test
    public void testMatchChomp() {
        String data = "<abcdefg>";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.advance();
        assertTrue(tokenQueue.matchChomp("<"));
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " \t\r\n abc ";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWhitespace());
        assertEquals(1, tokenQueue.pos);
    }

    @Test
    public void testMatchesWord() {
        String data = "abcdefg";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWord());
        assertEquals(1, tokenQueue.pos);
    }

    @Test
    public void testAdvance() {
        String data = "abcdefg";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.advance();
        assertEquals('a', tokenQueue.peek());
        assertEquals(1, tokenQueue.pos);
    }

    @Test
    public void testConsume() {
        String data = "abcdefg";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals('a', tokenQueue.consume());
        assertEquals('b', tokenQueue.peek());
        assertEquals(2, tokenQueue.pos);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsume_unexpectedSequence() {
        String data = "abcdefg";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        String data = "abcdefg";
        TokenQueue tokenQueue = new TokenQueue(data);

        String consumed = tokenQueue.consumeTo("de");
        assertEquals("ab", consumed);
        assertEquals("fg", tokenQueue.queue.substring(tokenQueue.pos));
        assertEquals(4, tokenQueue.pos);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "AbCdEfG";
        TokenQueue tokenQueue = new TokenQueue(data);

        String consumed = tokenQueue.consumeToIgnoreCase("De");
        assertEquals("AbCd", consumed);
        assertEquals("EfG", tokenQueue.queue.substring(tokenQueue.pos));
        assertEquals(6, tokenQueue.pos);
    }

    @Test
    public void testConsumeToAny() {
        String data = "abcdefg";
        TokenQueue tokenQueue = new TokenQueue(data);

        String consumed = tokenQueue.consumeToAny("ab", "cd");
        assertEquals("cd", consumed);
        assertEquals("efg", tokenQueue.queue.substring(tokenQueue.pos));
        assertEquals(4, tokenQueue.pos);
    }

    @Test
    public void testChompBalanced() {
        String data = "({)}";
        TokenQueue tokenQueue = new TokenQueue(data);

        String result = tokenQueue.chompBalanced('{', '}');
        assertEquals("({)}", result);
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testUnescape() {
        String data = "\\abc\\def\\";
        String result = TokenQueue.unescape(data);
        assertEquals("abcdef", result);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = " \t\r\n abc ";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals("abc", tokenQueue.queue.substring(tokenQueue.pos));
        assertEquals(4, tokenQueue.pos);
    }

    @Test
    public void testConsumeWord() {
        String data = "abcdefg";
        TokenQueue tokenQueue = new TokenQueue(data);
        String consumed = tokenQueue.consumeWord();
        assertEquals("abc", consumed);
        assertEquals("defg", tokenQueue.queue.substring(tokenQueue.pos));
        assertEquals(4, tokenQueue.pos);
    }

    @Test
    public void testConsumeTagName() {
        String data = "<tag1 name=\"tag2\"/>";
        TokenQueue tokenQueue = new TokenQueue(data);
        String consumed = tokenQueue.consumeTagName();
        assertEquals("tag1", consumed);
        assertEquals("<name=\"tag2\"/>", tokenQueue.queue.substring(tokenQueue.pos));
        assertEquals(8, tokenQueue.pos);
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "div#id1.class1.class2";
        TokenQueue tokenQueue = new TokenQueue(data);
        String consumed = tokenQueue.consumeElementSelector();
        assertEquals("div", consumed);
        assertEquals("#id1.class1.class2", tokenQueue.queue.substring(tokenQueue.pos));
        assertEquals(9, tokenQueue.pos);
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "id1.class1.class2";
        TokenQueue tokenQueue = new TokenQueue(data);
        String consumed = tokenQueue.consumeCssIdentifier();
        assertEquals("id1", consumed);
        assertEquals(".class1.class2", tokenQueue.queue.substring(tokenQueue.pos));
        assertEquals(7, tokenQueue.pos);
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "name=\"value\"";
        TokenQueue tokenQueue = new TokenQueue(data);
        String consumed = tokenQueue.consumeAttributeKey();
        assertEquals("name", consumed);
        assertEquals("=\"value\"", tokenQueue.queue.substring(tokenQueue.pos));
        assertEquals(6, tokenQueue.pos);
    }

    @Test
    public void testRemainder() {
        String data = "abcdefg";
        TokenQueue tokenQueue = new TokenQueue(data);
        String remainder = tokenQueue.remainder();
        assertEquals("defg", remainder);
        assertEquals(0, tokenQueue.pos);
    }
}