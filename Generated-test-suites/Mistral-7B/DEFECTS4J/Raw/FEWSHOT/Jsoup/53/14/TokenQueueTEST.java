package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testTokenQueue_isEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testTokenQueue_isNotEmpty() {
        TokenQueue queue = new TokenQueue("abc");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testTokenQueue_matches() {
        TokenQueue queue = new TokenQueue("abcabc");
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("cd"));
        assertEquals('c', queue.consume());
        assertFalse(queue.matches("ab"));
    }

    @Test
    public void testTokenQueue_matchesCS() {
        TokenQueue queue = new TokenQueue("AbCaB");
        assertTrue(queue.matchesCS("ab"));
        assertFalse(queue.matchesCS("cd"));
        assertEquals('A', queue.consume());
        assertFalse(queue.matchesCS("Ab"));
    }

    @Test
    public void testTokenQueue_matchesAny() {
        TokenQueue queue = new TokenQueue("12345");
        assertTrue(queue.matchesAny("1", "2", "3", "4", "5"));
        assertFalse(queue.matchesAny("6"));
        assertEquals('1', queue.consume());
        assertFalse(queue.matchesAny("12"));
    }

    @Test
    public void testTokenQueue_matchesAny_char() {
        TokenQueue queue = new TokenQueue("xyzxyz");
        assertTrue(queue.matchesAny('x', 'y', 'z'));
        assertFalse(queue.matchesAny('a'));
        assertEquals('x', queue.consume());
        assertFalse(queue.matchesAny("xy"));
    }

    @Test
    public void testTokenQueue_matchesStartTag() {
        TokenQueue queue = new TokenQueue("<tag>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
        assertEquals('<', queue.consume());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testTokenQueue_matchChomp() {
        TokenQueue queue = new TokenQueue("abab");
        assertTrue(queue.matchChomp("ab"));
        assertEquals("", queue.toString());
        assertFalse(queue.matches("ab"));
    }

    @Test
    public void testTokenQueue_matchChomp_empty() {
        TokenQueue queue = new TokenQueue("");
        assertFalse(queue.matchChomp("ab"));
        assertEquals("", queue.toString());
        assertFalse(queue.matches("ab"));
    }

    @Test
    public void testTokenQueue_matchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\r\n\f ");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
        assertEquals(' ', queue.consume());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testTokenQueue_matchesWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
        assertEquals('a', queue.consume());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testTokenQueue_addFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('x');
        assertEquals("x", queue.toString());
        assertEquals('x', queue.peek());
        assertEquals('x', queue.consume());
        assertEquals("", queue.toString());
    }

    @Test
    public void testTokenQueue_addFirst_string() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("abc");
        assertEquals("abc", queue.toString());
        assertEquals('a', queue.peek());
        assertEquals('a', queue.consume());
        assertEquals("bc", queue.toString());
    }

    @Test
    public void testTokenQueue_consumeTagName() {
        TokenQueue queue = new TokenQueue("<tag>");
        assertEquals("tag", queue.consumeTagName());
        assertEquals("", queue.toString());
    }

    @Test
    public void testTokenQueue_consumeTagName_empty() {
        TokenQueue queue = new TokenQueue("");
        assertEquals("", queue.consumeTagName());
        assertEquals("", queue.toString());
    }

    @Test
    public void testTokenQueue_consumeTagName_space() {
        TokenQueue queue = new TokenQueue(" <tag> ");
        assertEquals("tag", queue.consumeTagName());
        assertEquals("", queue.toString());
    }

    @Test
    public void testTokenQueue_consumeTagName_notTagName() {
        TokenQueue queue = new TokenQueue("<tag> class='class' id='id'>");
        assertEquals("tag", queue.consumeTagName());
        assertEquals("", queue.toString());
        assertEquals("class", queue.consumeTagName());
        assertEquals("id", queue.consumeTagName());
        assertEquals("", queue.toString());
    }

    @Test
    public void testTokenQueue_consumeElementSelector() {
        TokenQueue queue = new TokenQueue("tag[class='class']");
        assertEquals("tag", queue.consumeElementSelector());
        assertEquals("[class='class']", queue.toString());
    }

    @Test
    public void testTokenQueue_consumeElementSelector_empty() {
        TokenQueue queue = new TokenQueue("");
        assertEquals("", queue.consumeElementSelector());
        assertEquals("", queue.toString());
    }

    @Test
    public void testTokenQueue_consumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class:class class class class class class");
        assertEquals("class", queue.consumeCssIdentifier());
        assertEquals("class class class class class class", queue.toString());
    }

    @Test
    public void testTokenQueue_consumeCssIdentifier_empty() {
        TokenQueue queue = new TokenQueue("");
        assertEquals("", queue.consumeCssIdentifier());
        assertEquals("", queue.toString());
    }

    @Test
    public void testTokenQueue_consumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class='class' id='id' class class class class class class");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("id='id' class class class class class class", queue.toString());
    }

    @Test
    public void testTokenQueue_consumeAttributeKey_empty() {
        TokenQueue queue = new TokenQueue("");
        assertEquals("", queue.consumeAttributeKey());
        assertEquals("", queue.toString());
    }

    @Test
    public void testTokenQueue_remainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.remainder());
        assertEquals("abc", queue.toString());
    }

    @Test
    public void testTokenQueue_toString() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals("abc", queue.toString());
    }

    @Test
    public void testTokenQueue_consumeWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\r\n\f ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
        assertEquals("", queue.toString());
    }

    @Test
    public void testTokenQueue_consumeWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertEquals("abc", queue.consumeWord());
        assertEquals("123", queue.toString());
    }

    @Test
    public void testTokenQueue_consumeWord_empty() {
        TokenQueue queue = new TokenQueue("");
        assertEquals("", queue.consumeWord());
        assertEquals("", queue.toString());
    }
}