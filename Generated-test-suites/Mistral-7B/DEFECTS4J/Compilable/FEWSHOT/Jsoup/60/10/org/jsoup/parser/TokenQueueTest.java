package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue.addFirst("a");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.peek());
        assertEquals('a', queue.consume());
        assertEquals('b', queue.peek());
        assertEquals('b', queue.consume());
        // empty queue
        assertEquals(0, queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("hello");
        assertEquals("hello", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdabcd");
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("cd"));
        assertFalse(queue.matches("ef"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABcdABcd");
        assertTrue(queue.matchesCS("AB"));
        assertTrue(queue.matchesCS("CD"));
        assertFalse(queue.matchesCS("ef"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdabcd");
        assertTrue(queue.matchesAny("ab", "cd"));
        assertFalse(queue.matchesAny("ef"));

        queue = new TokenQueue("abc\"def");
        assertTrue(queue.matchesAny("a", "b", "c", "d", "e", "f", "\""));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><body>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("html<body>");
        assertTrue(queue.matchChomp("html"));
        assertEquals("body", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\r\nhello world\t");
        assertTrue(queue.matchesWhitespace());
        queue.advance();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.advance();
        assertEquals('b', queue.peek());
        queue.advance();
        assertEquals('c', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceNotMatching() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("<html><body><p>Hello World</p></body></html>");
        assertEquals("html", queue.consumeTo("html"));
        assertEquals("body", queue.consumeTo("body"));
        assertEquals("p", queue.consumeTo("p"));
        assertEquals("Hello World", queue.consumeTo("</p>"));
        assertEquals("</body>", queue.consumeTo("</body>"));
        assertEquals("</html>", queue.consumeTo("</html>"));
        assertEquals("", queue.consumeTo(""));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("<HTML><BODY><P>Hello World</P></BODY></HTML>");
        assertEquals("HTML", queue.consumeToIgnoreCase("html"));
        assertEquals("BODY", queue.consumeToIgnoreCase("Body"));
        assertEquals("P", queue.consumeToIgnoreCase("p"));
        assertEquals("Hello World", queue.consumeToIgnoreCase("HELLO WORLD"));
        assertEquals("</BODY>", queue.consumeToIgnoreCase("</body>"));
        assertEquals("</HTML>", queue.consumeToIgnoreCase("</html>"));
        assertEquals("", queue.consumeToIgnoreCase(""));
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdefghijklmno");
        assertEquals("abc", queue.consumeToAny("a", "b", "c"));
        assertEquals("def", queue.consumeToAny("d", "e", "f"));
        assertEquals("ijklmno", queue.consumeToAny());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(()){}");
        assertEquals("(())", queue.chompBalanced('(', ')'));
        assertEquals("{}", queue.toString());
    }

    @Test
    public void testUnescape() {
        String input = "&lt;html&gt;<body&gt;<p&gt;Hello &amp; World!&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";
        String output = TokenQueue.unescape(input);
        assertEquals("<html><body><p>Hello & World!</p></body></html>", output);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\r\nfoo bar \t baz\r\n");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("foobar");
        assertEquals("foo", queue.consumeWord());
        assertEquals("bar", queue.consumeWord());
        assertEquals("", queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><body><p class=\"foo\">Hi</p></body></html>");
        assertEquals("html", queue.consumeTagName());
        assertEquals("body", queue.consumeTagName());
        assertEquals("p", queue.consumeTagName());
        assertEquals("class", queue.consumeTagName());
        assertEquals("foo", queue.consumeTagName());
        assertEquals("", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("<html><body><p class=\"foo bar baz\"></p></body></html>");
        assertEquals("html", queue.consumeElementSelector());
        assertEquals("body", queue.consumeElementSelector());
        assertEquals("p", queue.consumeElementSelector());
        assertEquals("foo bar baz", queue.consumeElementSelector());
        assertEquals("", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class=\"foo bar baz\" id=\"qux quux\"");
        assertEquals("foo", queue.consumeCssIdentifier());
        assertEquals("bar", queue.consumeCssIdentifier());
        assertEquals("baz", queue.consumeCssIdentifier());
        assertEquals("qux", queue.consumeCssIdentifier());
        assertEquals("quux", queue.consumeCssIdentifier());
        assertEquals("", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("<p class=\"foo bar baz\" id=\"qux quux\" data-foo=\"bar baz\"data-bar=\"qux quux\"></p>");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("foo", queue.consumeAttributeKey());
        assertEquals("bar", queue.consumeAttributeKey());
        assertEquals("baz", queue.consumeAttributeKey());
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("qux", queue.consumeAttributeKey());
        assertEquals("quux", queue.consumeAttributeKey());
        assertEquals("data-foo", queue.consumeAttributeKey());
        assertEquals("data-bar", queue.consumeAttributeKey());
        assertEquals("", queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals("g", queue.remainder().charAt(0));
    }
}