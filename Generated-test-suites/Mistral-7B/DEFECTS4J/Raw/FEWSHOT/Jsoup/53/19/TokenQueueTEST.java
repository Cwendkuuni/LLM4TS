package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tq = new TokenQueue("");
        assertTrue(tq.isEmpty());

        tq = new TokenQueue("abc");
        assertFalse(tq.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tq = new TokenQueue("abc");
        assertEquals('a', tq.peek());

        tq = new TokenQueue("def");
        assertEquals('d', tq.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tq = new TokenQueue("");
        tq.addFirst('x');
        assertEquals('x', tq.peek());

        tq = new TokenQueue("abc");
        tq.addFirst('x');
        assertEquals('x', tq.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue tq = new TokenQueue("abc");
        assertTrue(tq.matches("a"));
        assertTrue(tq.matches("bc"));
        assertFalse(tq.matches("def"));

        tq = new TokenQueue("xyz");
        assertTrue(tq.matches("x"));
        assertTrue(tq.matches("yz"));
        assertFalse(tq.matches("abc"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tq = new TokenQueue("ABC");
        assertTrue(tq.matchesCS("A"));
        assertTrue(tq.matchesCS("BC"));
        assertFalse(tq.matchesCS("def"));

        tq = new TokenQueue("XYZ");
        assertTrue(tq.matchesCS("X"));
        assertTrue(tq.matchesCS("YZ"));
        assertFalse(tq.matchesCS("abc"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tq = new TokenQueue("abc");
        assertTrue(tq.matchesAny("a", "b"));
        assertTrue(tq.matchesAny("b", "c"));
        assertTrue(tq.matchesAny("a", "c"));
        assertFalse(tq.matchesAny("d", "e"));

        tq = new TokenQueue("xyz");
        assertTrue(tq.matchesAny("x", "y", "z"));
        assertFalse(tq.matchesAny("a", "b"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue tq = new TokenQueue("abc");
        assertTrue(tq.matchesAny(new char[]{'a', 'b'}));
        assertTrue(tq.matchesAny(new char[]{'b', 'c'}));
        assertTrue(tq.matchesAny(new char[]{'a', 'c'}));
        assertFalse(tq.matchesAny(new char[]{'d', 'e'}));

        tq = new TokenQueue("xyz");
        assertTrue(tq.matchesAny(new char[]{'x', 'y', 'z'}));
        assertFalse(tq.matchesAny(new char[]{'a', 'b'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tq = new TokenQueue("<x>");
        assertTrue(tq.matchesStartTag());

        tq = new TokenQueue("<y z>");
        assertTrue(tq.matchesStartTag());

        tq = new TokenQueue("abc");
        assertFalse(tq.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tq = new TokenQueue("abcdef");
        assertTrue(tq.matchChomp("ab"));
        assertEquals("cdef", tq.toString());

        tq = new TokenQueue("xyzxyz");
        assertTrue(tq.matchChomp("xy"));
        assertEquals("zxyz", tq.toString());

        tq = new TokenQueue("abc");
        assertFalse(tq.matchChomp("xy"));
        assertEquals("abc", tq.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tq = new TokenQueue(" abc ");
        assertTrue(tq.matchesWhitespace());

        tq = new TokenQueue(" xyz ");
        assertTrue(tq.matchesWhitespace());

        tq = new TokenQueue("123");
        assertFalse(tq.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tq = new TokenQueue("abc");
        assertTrue(tq.matchesWord());

        tq = new TokenQueue("xyz");
        assertTrue(tq.matchesWord());

        tq = new TokenQueue("123");
        assertFalse(tq.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tq = new TokenQueue("abc");
        tq.advance();
        assertEquals('b', tq.peek());

        tq = new TokenQueue("xyz");
        tq.advance();
        assertEquals('y', tq.peek());

        tq = new TokenQueue("123");
        tq.advance();
        assertEquals('2', tq.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue tq = new TokenQueue("abc");
        assertEquals('a', tq.consume());
        assertEquals('b', tq.consume());
        assertEquals('c', tq.consume());
        assertTrue(tq.isEmpty());

        tq = new TokenQueue("xyz");
        assertEquals('x', tq.consume());
        assertEquals('y', tq.consume());
        assertEquals('z', tq.consume());
        assertTrue(tq.isEmpty());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tq = new TokenQueue("abcd");
        assertEquals("ab", tq.consumeTo("ab"));
        assertEquals("cd", tq.toString());

        tq = new TokenQueue("xyzxyz");
        assertEquals("xyz", tq.consumeTo("xyz"));
        assertEquals("", tq.toString());

        tq = new TokenQueue("abc");
        assertEquals("abc", tq.consumeTo("abc"));
        assertTrue(tq.isEmpty());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tq = new TokenQueue("AbCdEf");
        assertEquals("AbCd", tq.consumeToIgnoreCase("abcd"));
        assertEquals("Ef", tq.toString());

        tq = new TokenQueue("XyZxYz");
        assertEquals("XyZ", tq.consumeToIgnoreCase("xyz"));
        assertEquals("", tq.toString());

        tq = new TokenQueue("AbCdEf");
        assertEquals("AbCdEf", tq.consumeToIgnoreCase("AbCdEf"));
        assertTrue(tq.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tq = new TokenQueue("abcdef");
        assertEquals("abc", tq.consumeToAny("ab", "cd"));
        assertEquals("def", tq.toString());

        tq = new TokenQueue("xyzxyz");
        assertEquals("xyz", tq.consumeToAny("xy", "zw"));
        assertEquals("", tq.toString());

        tq = new TokenQueue("abc");
        assertEquals("abc", tq.consumeToAny("ab", "xy"));
        assertTrue(tq.isEmpty());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tq = new TokenQueue("((())()())");
        assertEquals("((()))", tq.chompBalanced('(', ')'));
        assertEquals("()()", tq.toString());

        tq = new TokenQueue("([{}])");
        assertEquals("[{}]", tq.chompBalanced('[', ']'));
        assertEquals("", tq.toString());

        tq = new TokenQueue("(abc)def");
        assertEquals("(abc)", tq.chompBalanced('(', ')'));
        assertEquals("def", tq.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("abc\\def\\ghi", TokenQueue.unescape("abc\\def\\ghi"));
        assertEquals("abcdefghi", TokenQueue.unescape("abcdefghi"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tq = new TokenQueue(" abc def ");
        assertTrue(tq.consumeWhitespace());
        assertEquals("", tq.toString());

        tq = new TokenQueue(" xyz ");
        assertTrue(tq.consumeWhitespace());
        assertEquals("", tq.toString());

        tq = new TokenQueue("123");
        assertFalse(tq.consumeWhitespace());
        assertEquals("123", tq.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tq = new TokenQueue("abc");
        assertEquals("a", tq.consumeWord());
        assertEquals("bc", tq.toString());

        tq = new TokenQueue("xyz");
        assertEquals("x", tq.consumeWord());
        assertEquals("yz", tq.toString());

        tq = new TokenQueue("123");
        assertEquals("", tq.consumeWord());
        assertEquals("123", tq.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tq = new TokenQueue("<tag name>");
        assertEquals("tag", tq.consumeTagName());
        assertEquals(" name>", tq.toString());

        tq = new TokenQueue("<tag nameSpace:attribute>");
        assertEquals("tag", tq.consumeTagName());
        assertEquals(" nameSpace:attribute>", tq.toString());

        tq = new TokenQueue("xyz");
        assertEquals("", tq.consumeTagName());
        assertEquals("xyz", tq.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tq = new TokenQueue("tag[class='class']");
        assertEquals("tag", tq.consumeElementSelector());
        assertEquals("[class='class']", tq.toString());

        tq = new TokenQueue("tag[class='class']|tag2[class='class2']");
        assertEquals("tag", tq.consumeElementSelector());
        assertEquals("|tag2[class='class2']", tq.toString());

        tq = new TokenQueue("xyz");
        assertEquals("", tq.consumeElementSelector());
        assertEquals("xyz", tq.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tq = new TokenQueue("class:identifier");
        assertEquals("identifier", tq.consumeCssIdentifier());
        assertEquals(":class", tq.toString());

        tq = new TokenQueue("class:identifier1 class:identifier2");
        assertEquals("identifier1", tq.consumeCssIdentifier());
        assertEquals(" class:identifier2", tq.toString());

        tq = new TokenQueue("xyz");
        assertEquals("", tq.consumeCssIdentifier());
        assertEquals("xyz", tq.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tq = new TokenQueue("tag [attribute='value']");
        assertEquals("attribute", tq.consumeAttributeKey());
        assertEquals("='value']>", tq.toString());

        tq = new TokenQueue("tag [attribute='value' class='class']");
        assertEquals("attribute", tq.consumeAttributeKey());
        assertEquals("='value' class='class']>", tq.toString());

        tq = new TokenQueue("xyz");
        assertEquals("", tq.consumeAttributeKey());
        assertEquals("xyz", tq.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue tq = new TokenQueue("abcdef");
        assertEquals("def", tq.remainder());
        assertEquals("abc", tq.toString());

        tq = new TokenQueue("xyzxyz");
        assertEquals("", tq.remainder());
        assertEquals("xyzxyz", tq.toString());

        tq = new TokenQueue("abc");
        assertEquals("", tq.remainder());
        assertEquals("abc", tq.toString());
    }
}