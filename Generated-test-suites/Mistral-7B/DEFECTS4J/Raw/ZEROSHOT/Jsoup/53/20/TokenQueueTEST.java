import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("abc");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.peek();
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst('d');
        queue.addFirst("def");
        assertEquals('d', queue.peek());
        assertEquals("defabc", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("cd"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABC");
        assertTrue(queue.matchesCS("AB"));
        assertFalse(queue.matchesCS("cd"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny('a', 'b'));
        assertFalse(queue.matchesAny('x', 'y'));
    }

    @Test
    public void testMatchesAnyStringArray() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny("ab", "cd"));
        assertFalse(queue.matchesAny("x", "y"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<tag name>");
        assertTrue(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.matchChomp("ab");
        assertEquals("cdef", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" abc");
        assertTrue(queue.matchesWhitespace());
        queue.consumeWhitespace();
        assertEquals("", queue.toString());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesWord());
        queue.advance();
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        assertEquals('b', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume();
    }

    @Test
    public void testConsumeString() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.consume("ab");
        assertEquals("cdef", queue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeTo("ab"));
        assertEquals("def", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("ABCdef");
        assertEquals("ABC", queue.consumeToIgnoreCase("AbC"));
        assertEquals("def", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeToAny("ab", "cd"));
        assertEquals("def", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(())()");
        assertEquals("(())", queue.chompBalanced('(', ')'));
        assertEquals("()", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("ab\\cd\\ef\\gh\\ij", TokenQueue.unescape("ab\\cd\\ef\\gh\\ij\\"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        assertTrue(queue.consumeWhitespace());
        assertEquals("abc", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals("abc", queue.consumeWord());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<tag name>");
        assertEquals("tag name", queue.consumeTagName());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("tag name|_class");
        assertEquals("tag name", queue.consumeElementSelector());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("id:class_identifier");
        assertEquals("identifier", queue.consumeCssIdentifier());
        assertEquals("id:class", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("name=\"attribute\"");
        assertEquals("attribute", queue.consumeAttributeKey());
        assertEquals("name=\"\"", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.remainder());
    }
}