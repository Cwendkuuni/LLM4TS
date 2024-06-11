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

        tq.consume();
        assertEquals('b', tq.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tq = new TokenQueue("");
        tq.addFirst('X');
        assertEquals('X', tq.peek());

        tq.addFirst("xyz");
        assertEquals("xyz", tq.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tq = new TokenQueue("abc");
        assertTrue(tq.matches("a"));
        assertTrue(tq.matches("b"));
        assertTrue(tq.matches("c"));
        assertFalse(tq.matches("d"));

        tq.consume();
        assertFalse(tq.matches("a"));
        assertTrue(tq.matches("b"));
        assertTrue(tq.matches("c"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tq = new TokenQueue("ABC");
        assertTrue(tq.matchesCS("A"));
        assertTrue(tq.matchesCS("B"));
        assertTrue(tq.matchesCS("C"));
        assertFalse(tq.matchesCS("d"));

        tq.consume();
        assertFalse(tq.matchesCS("A"));
        assertTrue(tq.matchesCS("B"));
        assertTrue(tq.matchesCS("C"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tq = new TokenQueue("abc");
        assertTrue(tq.matchesAny("a", "b"));
        assertTrue(tq.matchesAny("b", "a"));
        assertTrue(tq.matchesAny("a", "c"));
        assertTrue(tq.matchesAny("c", "a"));
        assertFalse(tq.matchesAny("d"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue tq = new TokenQueue("abc");
        assertTrue(tq.matchesAny(new char[]{'a', 'b'}));
        assertTrue(tq.matchesAny(new char[]{'b', 'a'}));
        assertTrue(tq.matchesAny(new char[]{'a', 'c'}));
        assertTrue(tq.matchesAny(new char[]{'c', 'a'}));
        assertFalse(tq.matchesAny(new char[]{'d'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tq = new TokenQueue("<tag>");
        assertTrue(tq.matchesStartTag());

        tq.consume();
        assertFalse(tq.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tq = new TokenQueue("abc<xyz>def");
        assertEquals("xyz", tq.matchChomp("xyz"));
        assertEquals("def", tq.toString());

        tq = new TokenQueue("abc<xyz>def");
        assertEquals("xyz", tq.matchChomp("XYZ")); // case insensitive
        assertEquals("def", tq.toString());

        tq = new TokenQueue("abc<xyz>def");
        assertEquals("def", tq.matchChomp("xyz")); // doesn't match, nothing consumed
        assertEquals("def", tq.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tq = new TokenQueue("abc<xyz>def");
        assertEquals("xyz", tq.consumeTo("xyz"));
        assertEquals("def", tq.toString());

        tq = new TokenQueue("abc<xyz>def");
        assertEquals("xyz", tq.consumeToIgnoreCase("XYZ")); // case insensitive
        assertEquals("def", tq.toString());

        tq = new TokenQueue("abc<xyz>def");
        assertEquals("def", tq.consumeTo("xyz")); // doesn't match, nothing consumed
        assertEquals("def", tq.toString());

        tq = new TokenQueue("abc<xyz>def");
        assertEquals("abc", tq.consumeToIgnoreCase(new String[]{"xyz", "ABC"})); // matches any
        assertEquals("def", tq.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tq = new TokenQueue("abc<xyz>def");
        assertEquals("xyz", tq.consumeToAny(new String[]{"xyz"}));
        assertEquals("def", tq.toString());

        tq = new TokenQueue("abc<xyz>def");
        assertEquals("xyz", tq.consumeToAny(new String[]{"XYZ"})); // case insensitive
        assertEquals("def", tq.toString());

        tq = new TokenQueue("abc<xyz>def");
        assertEquals("def", tq.consumeToAny(new String[]{"abc", "xyz"})); // matches any
        assertEquals("", tq.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tq = new TokenQueue("(abc (def (ghi) jkl)) mno");
        assertEquals("abc (def (ghi) jkl)", tq.chompBalanced('(', ')'));
        assertEquals("mno", tq.toString());

        tq = new TokenQueue("(abc (def (ghi) jkl)) mno");
        assertEquals("abc (def (ghi) jkl)", tq.chompBalanced('(', '(')); // unbalanced
        assertEquals("(mno)", tq.toString());

        tq = new TokenQueue("(abc (def (ghi) jkl)) mno");
        assertEquals("mno", tq.toString()); // no match

        tq = new TokenQueue("(abc (def (ghi) jkl)) mno");
        assertEquals("(abc (def (ghi) jkl)", tq.consumeTo("(abc (def (ghi) jkl)"));
        assertEquals("mno", tq.toString());
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tq = new TokenQueue(" foo  bar ");
        assertTrue(tq.consumeWhitespace());
        assertEquals("", tq.toString());

        tq = new TokenQueue(" foo  bar ");
        assertTrue(tq.consumeWhitespace());
        assertEquals("", tq.toString());

        tq = new TokenQueue(" foo  bar ");
        assertFalse(tq.consumeWhitespace());
        assertEquals("bar", tq.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tq = new TokenQueue("foo bar baz");
        assertEquals("foo", tq.consumeWord());
        assertEquals("bar baz", tq.toString());

        tq = new TokenQueue("foo bar baz");
        assertEquals("bar", tq.consumeWord());
        assertEquals("baz", tq.toString());

        tq = new TokenQueue("foo bar baz");
        assertEquals("baz", tq.consumeWord());
        assertEquals("", tq.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tq = new TokenQueue("tag1 tag2 tag3");
        assertEquals("tag1", tq.consumeTagName());
        assertEquals("tag2 tag3", tq.toString());

        tq = new TokenQueue("tag1 tag2 tag3");
        assertEquals("tag2", tq.consumeTagName());
        assertEquals("tag3", tq.toString());

        tq = new TokenQueue("tag1 tag2 tag3");
        assertEquals("tag3", tq.consumeTagName());
        assertEquals("", tq.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tq = new TokenQueue("tag1.class1 tag2.class2 tag3.class3");
        assertEquals("tag1.class1", tq.consumeElementSelector());
        assertEquals("tag2.class2 tag3.class3", tq.toString());

        tq = new TokenQueue("tag1.class1 tag2.class2 tag3.class3");
        assertEquals("tag2.class2", tq.consumeElementSelector());
        assertEquals("tag3.class3", tq.toString());

        tq = new TokenQueue("tag1.class1 tag2.class2 tag3.class3");
        assertEquals("tag3.class3", tq.consumeElementSelector());
        assertEquals("", tq.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tq = new TokenQueue("id1 class1 id2 class2 id3");
        assertEquals("id1", tq.consumeCssIdentifier());
        assertEquals("class1 id2 class2 id3", tq.toString());

        tq = new TokenQueue("id1 class1 id2 class2 id3");
        assertEquals("id2", tq.consumeCssIdentifier());
        assertEquals("class1 id3", tq.toString());

        tq = new TokenQueue("id1 class1 id2 class2 id3");
        assertEquals("id3", tq.consumeCssIdentifier());
        assertEquals("", tq.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tq = new TokenQueue("name1 class1 name2 class2 name3");
        assertEquals("name1", tq.consumeAttributeKey());
        assertEquals("class1 name2 class2 name3", tq.toString());

        tq = new TokenQueue("name1 class1 name2 class2 name3");
        assertEquals("name2", tq.consumeAttributeKey());
        assertEquals("class1 name3", tq.toString());

        tq = new TokenQueue("name1 class1 name2 class2 name3");
        assertEquals("name3", tq.consumeAttributeKey());
        assertEquals("", tq.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue tq = new TokenQueue("abcdef");
        assertEquals("def", tq.remainder());
        assertEquals("abc", tq.toString());

        tq = new TokenQueue("abcdef");
        assertEquals("", tq.remainder());
        assertEquals("", tq.toString());
    }
}