package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.helper.StringUtil.isWhitespace;
import static org.jsoup.helper.Validate.notNull;

/**
 * Unit tests for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    @Test
    public void testNewInstance() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        assertEquals(data, queue.queue);
        assertEquals(0, queue.pos);
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
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        assertEquals('a', queue.peek());
        assertEquals('a', queue.consume());

        data = "xyz";
        queue = new TokenQueue(data);
        assertEquals(0, queue.peek());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testAddFirst() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        queue.addFirst('x');
        assertEquals('x', queue.consume());
        assertEquals('a', queue.consume());

        data = "";
        queue = new TokenQueue(data);
        queue.addFirst("hello");
        assertEquals('h', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('l', queue.consume());
        assertEquals('l', queue.consume());
        assertEquals('o', queue.consume());
        assertEquals(0, queue.consume());
    }

    @Test
    public void testMatches() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matches("bc"));
        assertTrue(queue.matchesIgnoreCase("bc"));
        assertFalse(queue.matches("ef"));
        assertFalse(queue.matchesIgnoreCase("ef"));

        data = "xyz";
        queue = new TokenQueue(data);
        assertFalse(queue.matches("xyw"));
        assertFalse(queue.matchesIgnoreCase("xyw"));
        assertFalse(queue.matches("xyz"));
        assertTrue(queue.matchesIgnoreCase("xyz"));
        assertFalse(queue.matches("x"));
        assertFalse(queue.matches("y"));
        assertFalse(queue.matches("z"));
    }

    @Test
    public void testMatchesCS() {
        String data = "HelloWorld";
        TokenQueue queue = new TokenQueue(data);
        assertFalse(queue.matchesCS("hello"));
        assertFalse(queue.matchesCS("Hello"));
        assertTrue(queue.matchesCS("HelloWorld"));
        assertTrue(queue.matchesCS("HelloWorld"));
    }

    @Test
    public void testMatchesAny() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesAny("bc", "ef"));
        assertTrue(queue.matchesAny(new String[]{"bc", "ef"}));
        assertTrue(queue.matchesAny("ef", "bc"));
        assertTrue(queue.matchesAny(new String[]{"ef", "bc"}));
        assertFalse(queue.matchesAny("xy"));
        assertFalse(queue.matchesAny(new String[]{"xy"}));

        data = "xyz";
        queue = new TokenQueue(data);
        assertTrue(queue.matchesAny("x", "y", "z"));
        assertTrue(queue.matchesAny(new String[]{"x", "y", "z"}));
        assertTrue(queue.matchesAny("y", "x", "z"));
        assertTrue(queue.matchesAny(new String[]{"y", "x", "z"}));
        assertFalse(queue.matchesAny("w"));
        assertFalse(queue.matchesAny(new String[]{"w"}));
    }

    @Test
    public void testMatchesAnyChar() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesAny(new Character[]{'b', 'e'}));
        assertTrue(queue.matchesAny(new Character[]{'e', 'b'}));
        assertFalse(queue.matchesAny(new Character[]{'x'}));

        data = "xyz";
        queue = new TokenQueue(data);
        assertTrue(queue.matchesAny(new Character[]{'x', 'y', 'z'}));
        assertTrue(queue.matchesAny(new Character[]{'y', 'x', 'z'}));
        assertFalse(queue.matchesAny(new Character[]{'w'}));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<html><head><title>Test</title></head><body>";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        String data = "<p id='foo'>Hello World</p>";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchChomp("id='"));
        assertEquals("foo>", queue.consumeToIgnoreCase("foo>"));
        assertTrue(queue.isEmpty());

        data = "abcdefg";
        queue = new TokenQueue(data);
        assertFalse(queue.matchChomp("xyz"));
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testMatchesWhitespace() {
        String data = "   abc   def   ";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesWord());
        assertTrue(queue.consumeWord().equals("a"));
        assertTrue(queue.matchesWord());
        assertTrue(queue.consumeWord().equals("d"));
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        queue.advance();
        assertEquals('a', queue.consume());
        queue.advance();
        assertEquals('b', queue.consume());
        queue.advance();
        queue.advance();
        assertEquals('e', queue.consume());
        assertEquals(0, queue.consume());
    }

    @Test
    public void testConsume() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals('d', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('f', queue.consume());
        assertEquals('g', queue.consume());
        assertEquals(0, queue.consume());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNoMatch() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        queue.consume("xyz");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNoLength() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        queue.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        String data = "<p id='foo'>Hello World</p>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("p id='", queue.consumeTo("id'"));
        assertEquals("foo>", queue.consumeToIgnoreCase("foo>"));
        assertEquals("World", queue.consumeToIgnoreCase("World"));
        assertTrue(queue.isEmpty());

        data = "abcdefg";
        queue = new TokenQueue(data);
        assertEquals("", queue.consumeTo("xyz"));
        assertEquals("g", queue.consume());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "<p id='foo'>Hello World</p>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("p id='foo'", queue.consumeToIgnoreCase("p id='foo'"));
        assertEquals("Hello World", queue.consumeToIgnoreCase("hello world"));
        assertTrue(queue.isEmpty());

        data = "abcdefg";
        queue = new TokenQueue(data);
        assertEquals("g", queue.consumeToIgnoreCase("g"));
        assertEquals("", queue.consume());
    }

    @Test
    public void testConsumeToAny() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("bc", queue.consumeToAny("bc", "ef"));
        assertEquals("ef", queue.consumeToAny("ef", "bc"));
        assertEquals("", queue.consume());

        data = "xyz";
        queue = new TokenQueue(data);
        assertEquals("x", queue.consumeToAny("x", "y", "z"));
        assertEquals("", queue.consume());
    }

    @Test
    public void testChompBalanced() {
        String data = "((())())((()))";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("((())", queue.chompBalanced('(', ')'));
        assertEquals("((()))", queue.chompBalanced('(', ')'));
        assertEquals("((()))", queue.chompBalanced('(', ')'));
        assertEquals("()", queue.chompBalanced('(', ')'));
        assertEquals("", queue.chompBalanced('(', ')'));

        data = "()";
        queue = new TokenQueue(data);
        assertEquals("", queue.chompBalanced('(', ')'));

        data = "(abc)";
        queue = new TokenQueue(data);
        assertEquals("abc", queue.chompBalanced('(', ')'));

        data = "((a)(b))";
        queue = new TokenQueue(data);
        assertEquals("(a)(b)", queue.chompBalanced('(', ')'));

        data = "(<html><head></head><body></body></html>";
        queue = new TokenQueue(data);
        assertEquals("<html><head></head><body>", queue.chompBalanced('<', '>'));

        data = "(<html><head><title>Test</title></head><body>Hello World</body></html>";
        queue = new TokenQueue(data);
        assertEquals("<head><title>Test</title>", queue.chompBalanced('<', '>'));
    }

    @Test
    public void testUnescape() {
        String data = "\\abc\\def\\\\";
        String expected = "abcdef\\";
        String result = TokenQueue.unescape(data);
        assertEquals(expected, result);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = "   abc   def   ";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("a", queue.consumeWord());
        assertEquals("d", queue.consumeWord());
        assertFalse(queue.consumeWord().equals(""));
    }

    @Test
    public void testConsumeTagName() {
        String data = "<p id='foo'>Hello World</p>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("p", queue.consumeTagName());
        assertEquals("p", queue.consumeTagName());
        assertEquals("", queue.consumeTagName());

        data = "<html><head><title>Test</title></head><body><p id='foo'>Hello World</p></body></html>";
        queue = new TokenQueue(data);
        assertEquals("html", queue.consumeTagName());
        assertEquals("head", queue.consumeTagName());
        assertEquals("title", queue.consumeTagName());
        assertEquals("p", queue.consumeTagName());
        assertEquals("", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "<p class='foo bar baz'>Hello World</p>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("p", queue.consumeElementSelector());
        assertEquals("p.foo", queue.consumeElementSelector());
        assertEquals("p.bar", queue.consumeElementSelector());
        assertEquals("p.baz", queue.consumeElementSelector());
        assertEquals("", queue.consumeElementSelector());

        data = "<p id='foo class='bar baz'><span class='foo bar'>Hello World</span></p>";
        queue = new TokenQueue(data);
        assertEquals("p", queue.consumeElementSelector());
        assertEquals("p#foo", queue.consumeElementSelector());
        assertEquals("span", queue.consumeElementSelector());
        assertEquals("span.foo", queue.consumeElementSelector());
        assertEquals("span.bar", queue.consumeElementSelector());
        assertEquals("", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = ".foo.bar.baz { color: red; }";
        TokenQueue queue = new TokenQueue(data);
        assertEquals(".foo", queue.consumeCssIdentifier());
        assertEquals(".bar", queue.consumeCssIdentifier());
        assertEquals(".baz", queue.consumeCssIdentifier());
        assertEquals("", queue.consumeCssIdentifier());

        data = "div#foo.bar:hover { color: blue; }";
        queue = new TokenQueue(data);
        assertEquals("div#foo", queue.consumeCssIdentifier());
        assertEquals("div#foo.bar", queue.consumeCssIdentifier());
        assertEquals("div#foo.bar:hover", queue.consumeCssIdentifier());
        assertEquals("", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "<p id='foo' class='bar baz' style='color: red;'>Hello World</p>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("id='foo'", queue.consume("id='"));
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("class='bar baz'", queue.consume("class='"));
        assertEquals("style", queue.consumeAttributeKey());
        assertEquals("style='color: red;'", queue.consume("style='"));
        assertEquals("", queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        String data = "<p id='foo'>Hello World</p>";
        TokenQueue queue = new TokenQueue(data);
        String expected = ">";
        String result = queue.remainder();
        assertEquals(expected, result);

        data = "abcdefg";
        queue = new TokenQueue(data);
        expected = "";
        result = queue.remainder();
        assertEquals(expected, result);
    }

    @Test
    public void testToString() {
        String data = "abcdefg";
        TokenQueue queue = new TokenQueue(data);
        String expected = "g";
        String result = queue.toString();
        assertEquals(expected, result);

        data = "";
        queue = new TokenQueue(data);
        expected = "";
        result = queue.toString();
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testNewInstanceNullData() {
        new TokenQueue(null);
    }
}