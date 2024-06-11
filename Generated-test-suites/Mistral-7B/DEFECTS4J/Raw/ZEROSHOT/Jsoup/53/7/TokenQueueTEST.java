import org.junit.Test;
import org.jsoup.parser.TokenQueue;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testNotEmpty() {
        TokenQueue queue = new TokenQueue("abc");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.peek() == 'a');
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekAfterConsume() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consume();
        queue.peek();
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        queue.addFirst("123");
        assertEquals("123a", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matches("a"));
        assertTrue(queue.matches("bc"));
        assertFalse(queue.matches("123"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesCS("a"));
        assertFalse(queue.matchesCS("bc"));
        assertFalse(queue.matchesCS("ABC"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abc123");
        assertTrue(queue.matchesAny("a", "1"));
        assertTrue(queue.matchesAny("a", "b", "1"));
        assertFalse(queue.matchesAny("x"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abc123");
        assertTrue(queue.matchesAny(new char[]{'a', '1'}));
        assertFalse(queue.matchesAny(new char[]{'x'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abc>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abc<def>");
        assertTrue(queue.matchChomp("abc"));
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\r\n abc");
        assertTrue(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        assertEquals('b', queue.consume());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeAfterEmpty() {
        TokenQueue queue = new TokenQueue("");
        queue.consume();
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consume("ab");
        assertEquals('c', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeAfterEnd() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consumeTo("xyz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeTo("def"));
        assertEquals("def", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("AbCdEf");
        assertEquals("AbC", queue.consumeToIgnoreCase("def"));
        assertEquals("dEf", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcd1234ef");
        assertEquals("abcd", queue.consumeToAny("123", "ef"));
        assertEquals("1234ef", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(foo (bar baz) qux) quux");
        assertEquals("foo (bar baz)", queue.chompBalanced('(', ')'));
        assertEquals("qux quux", queue.toString());
    }

    @Test
    public void testUnescape() {
        String input = "\\(foo\\) \\(bar\\(baz\\)\\) qux\\\\quux";
        String expected = "(\\(foo\\)\\) (\\(bar\\(baz\\)\\)) qux\\quux";
        String output = TokenQueue.unescape(input);
        assertEquals(expected, output);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\r\n abc");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertEquals("abc", queue.consumeWord());
        assertEquals("123", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<tag name=\"value\" attr=\"value2\">content</tag>");
        assertEquals("tag", queue.consumeTagName());
        assertEquals("name=\"value\" attr=\"value2\"", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("<div class=\"class1 class2\" id=\"id1\">content</div>");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("class1 class2 id=\"id1\"", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class1 class2 id1 attribute:value");
        assertEquals("class1", queue.consumeCssIdentifier());
        assertEquals("class2 id1 attribute:value", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class1 class2 id1 attribute:value attr1=\"value1\" attr2=\"value2\"");
        assertEquals("attribute", queue.consumeAttributeKey());
        assertEquals("value:value attr1=\"value1\" attr2=\"value2\"", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("12345");
        assertEquals("5", queue.remainder());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("12345");
        assertEquals("5", queue.toString());
    }
}