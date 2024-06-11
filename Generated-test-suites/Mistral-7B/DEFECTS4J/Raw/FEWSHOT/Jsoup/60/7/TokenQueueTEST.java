package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testTokenQueueInstance() {
        String data = "abc<xyz>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals(data, queue.queue);
        assertEquals(0, queue.pos);
        assertEquals(3, queue.remainingLength());
    }

    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.isEmpty());

        data = "abc";
        queue = new TokenQueue(data);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "abc<xyz>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals('a', queue.peek());
    }

    @Test
    public void testPeekEmpty() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        assertEquals(0, queue.peek());
    }

    @Test
    public void testAddFirst() {
        String data = "abc<xyz>";
        TokenQueue queue = new TokenQueue(data);
        queue.addFirst('x');
        assertEquals(4, queue.remainingLength());
        assertEquals('x', queue.queue.charAt(0));
        assertEquals('a', queue.queue.charAt(1));

        queue.addFirst("def");
        assertEquals(7, queue.remainingLength());
        assertEquals("def", queue.queue.substring(0, 3));
    }

    @Test
    public void testMatches() {
        String data = "abc<xyz>";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matches("a"));
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("abxyz"));
        assertFalse(queue.matches("xyz"));
        assertFalse(queue.matches("xy"));
    }

    @Test
    public void testMatchesCS() {
        String data = "abc<XYZ>";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesCS("AB"));
        assertTrue(queue.matchesCS("ABXYZ"));
        assertFalse(queue.matchesCS("xy"));
    }

    @Test
    public void testMatchesAny() {
        String data = "abc<xyz>";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesAny("a", "b"));
        assertTrue(queue.matchesAny("a", "b", "xy"));
        assertFalse(queue.matchesAny("xy", "z"));

        // Same test with an empty array as a parameter
        assertTrue(queue.matchesAny());
    }

    @Test
    public void testMatchesAnyChars() {
        String data = "abc<xyz>";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesAny('a', 'b'));
        assertTrue(queue.matchesAny('a', 'b', 'x'));
        assertFalse(queue.matchesAny('x', 'z'));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<xyz>";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesStartTag());

        data = "abc<xyz>";
        queue = new TokenQueue(data);
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        String data = "abc<xyz>";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchChomp("xyz"));
        assertEquals(0, queue.remainingLength());
    }

    @Test
    public void testMatchChompEmpty() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        assertFalse(queue.matchChomp(""));
        assertEquals(0, queue.remainingLength());
    }

    @Test
    public void testMatchesWhitespace() {
        String data = "   abc<xyz>";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesWhitespace());

        data = "abc<xyz>";
        queue = new TokenQueue(data);
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "abc<xyz>";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesWord());

        data = "123<xyz>";
        queue = new TokenQueue(data);
        assertTrue(queue.matchesWord());

        data = "   abc<xyz>";
        queue = new TokenQueue(data);
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "abc<xyz>";
        TokenQueue queue = new TokenQueue(data);
        queue.advance();
        assertEquals('b', queue.queue.charAt(0));
        assertEquals(1, queue.pos);
        assertEquals(2, queue.remainingLength());

        queue.advance();
        assertEquals('x', queue.queue.charAt(0));
        assertEquals(2, queue.pos);
        assertEquals(1, queue.remainingLength());
    }

    @Test
    public void testConsume() {
        String data = "abc<xyz>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals('a', queue.consume());
        assertEquals('b', queue.queue.charAt(0));
        assertEquals(1, queue.pos);
        assertEquals(2, queue.remainingLength());

        queue.consume("xy");
        assertEquals('z', queue.queue.charAt(0));
        assertEquals(0, queue.remainingLength());
    }

    @Test
    public void testConsumeTo() {
        String data = "abc<xyz>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("xy", queue.consumeTo("xy"));
        assertEquals("z", queue.queue.charAt(0));
        assertEquals(0, queue.remainingLength());

        assertEquals("xyz", queue.consumeTo("xyz"));
        assertEquals("", queue.queue);
        assertEquals(0, queue.remainingLength());

        assertEquals("xyz", queue.consumeTo("xyz", "z"));
        assertEquals("", queue.queue);
        assertEquals(0, queue.remainingLength());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "ABC<xyz>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("xy", queue.consumeToIgnoreCase("xy"));
        assertEquals("z", queue.queue.charAt(0));
        assertEquals(0, queue.remainingLength());

        assertEquals("xyz", queue.consumeToIgnoreCase("xyz"));
        assertEquals("", queue.queue);
        assertEquals(0, queue.remainingLength());

        assertEquals("xyz", queue.consumeToIgnoreCase("xyz", "Z"));
        assertEquals("", queue.queue);
        assertEquals(0, queue.remainingLength());
    }

    @Test
    public void testConsumeToAny() {
        String data = "abc<xyz>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("xy", queue.consumeToAny("xy"));
        assertEquals("z", queue.queue.charAt(0));
        assertEquals(0, queue.remainingLength());

        assertEquals("xyz", queue.consumeToAny("xyz"));
        assertEquals("", queue.queue);
        assertEquals(0, queue.remainingLength());

        assertEquals("xyz", queue.consumeToAny("xy", "z"));
        assertEquals("", queue.queue);
        assertEquals(0, queue.remainingLength());
    }

    @Test
    public void testChompBalanced() {
        String data = "('(ab)cd'))";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("(ab)cd", queue.chompBalanced('(', ')'));
        assertEquals("", queue.queue);
        assertEquals(0, queue.remainingLength());

        assertEquals("(abc)", queue.chompBalanced('(', ')'));
        assertEquals("", queue.queue);
        assertEquals(0, queue.remainingLength());

        assertEquals("(abc)", queue.chompBalanced('(', ')'));
        assertEquals("", queue.queue);
        assertEquals(0, queue.remainingLength());

        assertEquals("(xy)xyz", queue.chompBalanced('(', ')'));
        assertEquals("xyz", queue.queue);
        assertEquals(3, queue.remainingLength());

        assertEquals("xyz(xy)", queue.chompBalanced('(', ')'));
        assertEquals("xy", queue.queue);
        assertEquals(1, queue.remainingLength());
    }

    @Test
    public void testUnescape() {
        String data = "\\\\abc\\\\def\\\\\\\\";
        String expected = "abcdef\\";
        assertEquals(expected, TokenQueue.unescape(data));
    }

    @Test
    public void testConsumeWhitespace() {
        String data = "   abc<xyz>   ";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.consumeWhitespace());

        assertEquals('a', queue.queue.charAt(0));
        assertEquals(5, queue.remainingLength());
    }

    @Test
    public void testConsumeWord() {
        String data = "abc<xyz>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("abc", queue.consumeWord());
        assertEquals("xyz", queue.queue);
        assertEquals(3, queue.remainingLength());
    }

    @Test
    public void testConsumeTagName() {
        String data = "<div class='class1 class2'>Content</div>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("div", queue.consumeTagName());
        assertEquals("class='class1 class2'>Content", queue.queue);
        assertEquals(16, queue.remainingLength());

        assertEquals("div", queue.consumeTagName());
        assertEquals("", queue.queue);
        assertEquals(0, queue.remainingLength());
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "div#id class1.class2 div > p:first-child";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("id class1.class2 div > p:first-child", queue.queue);
        assertEquals(21, queue.remainingLength());

        assertEquals("div", queue.consumeElementSelector());
        assertEquals("id class1.class2", queue.queue);
        assertEquals(9, queue.remainingLength());

        assertEquals("p:first-child", queue.consumeElementSelector());
        assertEquals("", queue.queue);
        assertEquals(0, queue.remainingLength());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "div#class1.class2.class3";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("class1", queue.consumeCssIdentifier());
        assertEquals("class2.class3", queue.queue);
        assertEquals(11, queue.remainingLength());

        assertEquals("class2", queue.consumeCssIdentifier());
        assertEquals("class3", queue.queue);
        assertEquals(6, queue.remainingLength());

        assertEquals("class3", queue.consumeCssIdentifier());
        assertEquals("", queue.queue);
        assertEquals(0, queue.remainingLength());
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "<div id='id' class='class1 class2' data-test='test'></div>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("class='class1 class2' data-test='test'", queue.queue);
        assertEquals(19, queue.remainingLength());

        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("data-test='test'", queue.queue);
        assertEquals(11, queue.remainingLength());

        assertEquals("data-test", queue.consumeAttributeKey());
        assertEquals("", queue.queue);
        assertEquals(0, queue.remainingLength());
    }

    @Test
    public void testRemainder() {
        String data = "abc<xyz>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("xyz", queue.remainder());
        assertEquals("", queue.queue);
        assertEquals(0, queue.remainingLength());

        data = "abc<xyzdef>";
        queue = new TokenQueue(data);
        assertEquals("def", queue.remainder());
        assertEquals("abc<xyz", queue.queue);
        assertEquals(5, queue.remainingLength());
    }
}