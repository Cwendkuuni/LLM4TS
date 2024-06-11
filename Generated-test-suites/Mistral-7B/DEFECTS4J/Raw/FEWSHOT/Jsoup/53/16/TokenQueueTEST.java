package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;

import java.util.Arrays;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue.addFirst("abc");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());

        queue.advance();
        assertEquals('b', queue.peek());

        queue.consume();
        assertEquals('c', queue.peek());

        queue.consumeTo("bc");
        assertNull(queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("abc");
        assertEquals("abc", queue.consume());
        assertEquals("", queue.consume());

        queue.addFirst("def");
        assertEquals("def", queue.consume());
        assertEquals("abc", queue.consume());
        assertEquals("", queue.consume());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matches("a"));
        assertTrue(queue.matches("bc"));
        assertFalse(queue.matches("xyz"));
        assertFalse(queue.matches(""));

        queue.consume();
        assertFalse(queue.matches("a"));
        assertTrue(queue.matches("bc"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesCS("a"));
        assertTrue(queue.matchesCS("bc"));
        assertFalse(queue.matchesCS("ABC"));
        assertFalse(queue.matchesCS("xyz"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny("a", "b", "c"));
        assertTrue(queue.matchesAny("bc", "a", "c"));
        assertFalse(queue.matchesAny("xyz", "a", "b", "c"));
        assertFalse(queue.matchesAny(""));

        queue.consume();
        assertFalse(queue.matchesAny("a", "b", "c"));
        assertTrue(queue.matchesAny("bc", "a", "c"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny(new Character[]{'a', 'b', 'c'}));
        assertTrue(queue.matchesAny(new Character[]{'b', 'c', 'a'}));
        assertFalse(queue.matchesAny(new Character[]{'x', 'y', 'z'}));
        assertFalse(queue.matchesAny(new Character[]{}));

        queue.consume();
        assertFalse(queue.matchesAny(new Character[]{'a', 'b', 'c'}));
        assertTrue(queue.matchesAny(new Character[]{'b', 'c', 'a'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abc>");
        assertTrue(queue.matchesStartTag());

        queue.consume();
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdefg<xyz>");
        assertTrue(queue.matchChomp("abc"));
        assertEquals("defg<xyz>", queue.toString());

        assertTrue(queue.matchChomp("defg"));
        assertEquals("<xyz>", queue.toString());

        assertTrue(queue.matchChomp("<xyz>"));
        assertEquals("", queue.toString());

        assertFalse(queue.matchChomp("abcd"));
        assertEquals("defg<xyz>", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" abc ");
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.isEmpty());

        queue.addFirst("def");
        assertFalse(queue.matchesWhitespace());
        assertFalse(queue.consumeWhitespace());
        assertEquals("def", queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matchesWord());
        assertEquals("a", queue.consumeWord());
        assertEquals("b", queue.consumeWord());
        assertEquals("d", queue.consumeWord());
        assertEquals("", queue.consumeWord());

        queue.addFirst("efg");
        assertEquals("e", queue.consumeWord());
        assertEquals("f", queue.consumeWord());
        assertEquals("g", queue.consumeWord());
        assertEquals("", queue.consumeWord());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals("a", queue.consume());
        assertEquals("b", queue.consume());
        assertEquals("c", queue.consume());
        assertEquals("d", queue.consume());
        assertEquals("e", queue.consume());
        assertEquals("f", queue.consume());
        assertEquals("g", queue.consume());
        assertEquals("", queue.consume());

        queue.addFirst("hij");
        assertEquals("h", queue.consume());
        assertEquals("i", queue.consume());
        assertEquals("j", queue.consume());
        assertEquals("", queue.consume());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')').toString());
        assertEquals("four", queue.consume().toString());

        queue.addFirst("xyz");
        assertEquals("xyz", queue.consume().toString());
        assertEquals("(one (two) three)", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')').toString());
        assertEquals("four", queue.consume().toString());

        assertEquals("(one (two) three)", queue.consumeToIgnoreCase("onetwothree").toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')').toString());
        assertEquals("four", queue.consume().toString());

        assertEquals("four", queue.consumeToAny("one", "two", "three", "four").toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')').toString());
        assertEquals("four", queue.consume().toString());

        assertEquals("(one (two) three)", queue.toString());
        assertEquals("one (two) three", queue.chompBalanced('(', ')').toString());
        assertEquals("", queue.toString());

        assertEquals("(one (two) three)", queue.toString());
        assertEquals("(one (two)", queue.consumeTo("(two) three").toString());
        assertEquals("three)", queue.toString());

        assertEquals("(one (two) three)", queue.toString());
        assertEquals("(one ", queue.consumeTo("(two) three", "one ").toString());
        assertEquals("two) three", queue.toString());
    }

    @Test
    public void testUnescape() {
        String input = "\\(one \\(two\\) three\\) four";
        String output = TokenQueue.unescape(input);
        assertEquals("(one (two) three) four", output);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" abc \t def");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.isEmpty());

        queue.addFirst("ghij");
        assertFalse(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
        assertEquals("ghij", queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals("a", queue.consumeWord());
        assertEquals("b", queue.consumeWord());
        assertEquals("d", queue.consumeWord());
        assertEquals("", queue.consumeWord());

        queue.addFirst("efg");
        assertEquals("e", queue.consumeWord());
        assertEquals("f", queue.consumeWord());
        assertEquals("g", queue.consumeWord());
        assertEquals("", queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<p class=\"foo bar\">content</p>");
        assertEquals("p", queue.consumeTagName());
        assertEquals("class=\"foo bar\"", queue.toString());

        assertEquals("class=\"foo bar\"", queue.consumeTo("class").toString());
        assertEquals(">", queue.toString());

        assertEquals("class=\"foo bar\"", queue.consumeToIgnoreCase("CLASS Bar foo"));
        assertEquals(">", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#header div.navigation ul li.menu a");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("#header div.navigation ul li.menu a", queue.toString());

        assertEquals("#header div.navigation ul li.menu a", queue.consumeToAny("#header", "UL", "LI", ".menu", "A").toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("div#header .nav.bar.foo.class");
        assertEquals("nav", queue.consumeCssIdentifier());
        assertEquals(".bar.foo.class", queue.toString());

        assertEquals(".bar.foo.class", queue.consumeToIgnoreCase(".Nav .BAR .foo CLASS"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("div id=\"header\" class=\"nav bar foo\" style=\"color: red\" title=\"Tooltip text\"");
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("class=\"nav bar foo\" style=\"color: red\" title=\"Tooltip text\"", queue.toString());

        assertEquals("class=\"nav bar foo\" style=\"color: red\" title=\"Tooltip text\"", queue.consumeToAny("class", "ID", "STYLE", "TITLE"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals("g", queue.remainder().charAt(0));
        assertEquals("", queue.toString());

        queue.addFirst("hij");
        assertEquals("hij", queue.remainder().toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals("abcdefg", queue.toString());

        queue.addFirst("hij");
        assertEquals("hijabcdefg", queue.toString());

        queue.consumeTo("abc");
        assertEquals("defg", queue.toString());
    }
}