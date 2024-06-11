package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.TokenQueue.ESC;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        assertEquals(data, queue.queue);
        assertEquals(0, queue.pos);
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        String data = "abcdef";
        queue = new TokenQueue(data);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        assertEquals('a', queue.peek());
        assertEquals('a', queue.consume());
        assertEquals('b', queue.peek());
    }

    @Test
    public void testAddFirst() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        queue.addFirst('x');
        assertEquals('x', queue.consume());
        assertEquals('a', queue.consume());

        queue.addFirst("xyz");
        assertEquals('x', queue.consume());
        assertEquals('y', queue.consume());
        assertEquals('z', queue.consume());
        assertEquals('a', queue.consume());
    }

    @Test
    public void testMatches() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);

        assertTrue(queue.matches(""));
        assertTrue(queue.matches("a"));
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("abc"));
        assertTrue(queue.matches("def"));
        assertTrue(queue.matches("abcdef"));

        assertFalse(queue.matches("pqr"));
        assertFalse(queue.matches("xyz"));
    }

    @Test
    public void testMatchesCS() {
        String data = "abcABCdef";
        TokenQueue queue = new TokenQueue(data);

        assertTrue(queue.matchesCS(""));
        assertTrue(queue.matchesCS("a"));
        assertTrue(queue.matchesCS("ABC"));
        assertTrue(queue.matchesCS("abc"));
        assertTrue(queue.matchesCS("def"));
        assertTrue(queue.matchesCS("abcABCdef"));

        assertFalse(queue.matchesCS("pqr"));
        assertFalse(queue.matchesCS("xyz"));
    }

    @Test
    public void testMatchesAny() {
        String data = "123abcDef456";
        TokenQueue queue = new TokenQueue(data);

        assertTrue(queue.matchesAny("1", "2", "3"));
        assertTrue(queue.matchesAny("1", "2"));
        assertTrue(queue.matchesAny("1", "a", "b"));
        assertTrue(queue.matchesAny("1", "a"));
        assertTrue(queue.matchesAny("a", "b", "c"));
        assertTrue(queue.matchesAny("a", "b"));
        assertTrue(queue.matchesAny("A", "B", "C", "d", "e", "F"));
        assertFalse(queue.matchesAny("x"));
        assertFalse(queue.matchesAny("pqr"));
    }

    @Test
    public void testMatchesAnyChars() {
        String data = "123abcDef456";
        TokenQueue queue = new TokenQueue(data);

        assertTrue(queue.matchesAny(new char[]{'1', '2', '3'}));
        assertTrue(queue.matchesAny(new char[]{'1', '2'}));
        assertTrue(queue.matchesAny(new char[]{'1', 'a'}));
        assertTrue(queue.matchesAny(new char[]{'1'}));
        assertTrue(queue.matchesAny(new char[]{'a', 'b'}));
        assertTrue(queue.matchesAny(new char[]{'a'}));
        assertTrue(queue.matchesAny(new char[]{'A', 'B', 'C', 'd', 'e', 'F'}));
        assertFalse(queue.matchesAny(new char[]{'x'}));
        assertFalse(queue.matchesAny(new char[]{'p', 'q', 'r'}));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<tag1><tag2><tag3>";
        TokenQueue queue = new TokenQueue(data);

        assertTrue(queue.matchesStartTag());
        assertTrue(queue.matchesStartTag());
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchChompWithEmptyQueue() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        queue.matchChomp("");
    }

    @Test
    public void testMatchChomp() {
        String data = "<tag1><tag2><tag3>xyzdef";
        TokenQueue queue = new TokenQueue(data);

        assertEquals("tag1>", queue.consumeTo("tag1>"));
        assertEquals("tag2>", queue.consumeTo("tag2>"));
        assertEquals("tag3>", queue.consumeTo("tag3>"));
        assertEquals("xyz", queue.consumeTo("xyz"));
        assertEquals("def", queue.consumeTo("def"));
        assertTrue(queue.isEmpty());

        assertEquals("xyz", queue.matchChomp("xyz"));
        assertEquals("def", queue.matchChomp("def"));
        assertFalse(queue.matchChomp(""));
        assertFalse(queue.matchChomp("pqr"));
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " \t\r\n";
        TokenQueue queue = new TokenQueue(data);

        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchesWord() {
        String data = "xyz123abcDef";
        TokenQueue queue = new TokenQueue(data);

        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testAdvance() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);

        queue.advance();
        assertEquals('a', queue.consume());

        queue.advance();
        assertEquals('b', queue.consume());

        queue.advance();
        assertEquals('c', queue.consume());

        queue.advance();
        assertEquals('d', queue.consume());

        queue.advance();
        assertEquals('e', queue.consume());

        queue.advance();
        assertEquals('f', queue.consume());
    }

    @Test
    public void testConsume() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);

        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals('d', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('f', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeInvalidSequence() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        queue.consume("xyz");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSeqNotLongEnough() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        queue.consume("abc");
        queue.consume("def");
        queue.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        String data = "<tag1><tag2><tag3>xyzdef";
        TokenQueue queue = new TokenQueue(data);

        assertEquals("tag1>", queue.consumeTo("tag1>"));
        assertEquals("xyz", queue.consumeTo("xyz"));
        assertEquals("def", queue.consumeTo("def"));
        assertTrue(queue.isEmpty());

        assertEquals("xyz", queue.consumeToIgnoreCase("xyz"));
        assertEquals("def", queue.consumeToIgnoreCase("def"));
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        String data = "abcxyz123def";
        TokenQueue queue = new TokenQueue(data);

        assertEquals("abc", queue.consumeToAny("a", "b", "c"));
        assertEquals("xyz", queue.consumeToAny("x", "y", "z"));
        assertEquals("123", queue.consumeToAny("1", "2", "3"));
        assertEquals("def", queue.consumeToAny("d", "e", "f"));
        assertTrue(queue.isEmpty());

        assertEquals("xyz", queue.consumeToAny("x", "y", "z", "a", "b", "c"));
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testChompBalanced() {
        String data = "((((hello((world)))))))";
        TokenQueue queue = new TokenQueue(data);

        assertEquals("hello((world)", queue.chompBalanced('(', ')'));
        assertEquals("hello((world)", queue.chompBalanced('(', '('));
        assertEquals("hello((world", queue.chompBalanced('(', '{'));
        assertEquals("hello((world", queue.chompBalanced('(', '"'));

        assertEquals("((hello((world))", queue.chompBalanced(')', '('));
        assertEquals("((hello((world))", queue.chompBalanced(')', ')'));
        assertEquals("((hello((world))", queue.chompBalanced(')', '{'));
        assertEquals("((hello((world))", queue.chompBalanced(')', '"'));

        assertEquals("hello((world)", queue.chompBalanced(')', ')'));
        assertEquals("hello((world)", queue.chompBalanced(')', ']'));
        assertEquals("hello((world)", queue.chompBalanced(')', '}'));
        assertEquals("hello((world", queue.chompBalanced(')', '\"'));

        assertEquals("", queue.chompBalanced(')', '('));
        assertEquals("", queue.chompBalanced(')', ')'));
        assertEquals("", queue.chompBalanced(')', '{'));
        assertEquals("", queue.chompBalanced(')', '\''));

        assertTrue(queue.isEmpty());
    }

    @Test
    public void testUnescape() {
        String data = "\\\\abc\\def\\\\\\\\";
        String expected = "abcdef\\";
        assertEquals(expected, TokenQueue.unescape(data));
    }

    @Test
    public void testConsumeWhitespace() {
        String data = "   abcdef   ";
        TokenQueue queue = new TokenQueue(data);

        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeWord() {
        String data = "xyz123abcDef";
        TokenQueue queue = new TokenQueue(data);

        assertEquals("xyz", queue.consumeWord());
        assertEquals("123", queue.consumeWord());
        assertEquals("abc", queue.consumeWord());
        assertEquals("Def", queue.consumeWord());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        String data = "<tag1><tag2>xyz<tag3>";
        TokenQueue queue = new TokenQueue(data);

        assertEquals("tag1", queue.consumeTagName());
        assertEquals("tag2", queue.consumeTagName());
        assertEquals("xyz", queue.consumeWord());
        assertEquals("tag3", queue.consumeTagName());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "<tag1 tagName=\"tag2\" class=\"class1 class2\">xyz</tag1>";
        TokenQueue queue = new TokenQueue(data);

        assertEquals("tag1", queue.consumeElementSelector());
        assertEquals("tag2", queue.consumeTagName());
        assertEquals("class1", queue.consumeCssIdentifier());
        assertEquals("class2", queue.consumeCssIdentifier());
        assertEquals("xyz", queue.consumeWord());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "tag1 class=\"class1 class2 id=\"id1 id2\"";
        TokenQueue queue = new TokenQueue(data);

        assertEquals("class1", queue.consumeCssIdentifier());
        assertEquals("class2", queue.consumeCssIdentifier());
        assertEquals("id1", queue.consumeCssIdentifier());
        assertEquals("id2", queue.consumeCssIdentifier());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "tag1 tagName=\"tag2\" tagName2=\"tag3\"";
        TokenQueue queue = new TokenQueue(data);

        assertEquals("tagName", queue.consumeAttributeKey());
        assertEquals("tagName2", queue.consumeAttributeKey());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemainder() {
        String data = "abcxyz123def";
        TokenQueue queue = new TokenQueue(data);

        assertEquals("def", queue.remainder());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testToString() {
        String data = "abcxyz123def";
        TokenQueue queue = new TokenQueue(data);

        assertEquals("def", queue.toString());
        assertTrue(queue.isEmpty());
    }
}