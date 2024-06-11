import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testTokenQueueEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testTokenQueueNonEmpty() {
        TokenQueue queue = new TokenQueue("hello world");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testTokenQueuePeek() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals('h', queue.peek());
    }

    @Test
    public void testTokenQueueAddFirst() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.addFirst('X');
        assertEquals('X', queue.peek());
        assertEquals('h', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('l', queue.consume());
        assertEquals('l', queue.consume());
        assertEquals('o', queue.consume());
        assertEquals(' ', queue.consume());
        assertEquals('w', queue.consume());
        assertEquals('r', queue.consume());
        assertEquals('d', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testTokenQueueMatches() {
        TokenQueue queue = new TokenQueue("hello world");
        assertTrue(queue.matches("he"));
        assertTrue(queue.matches("ll"));
        assertTrue(queue.matches("llo"));
        assertTrue(queue.matches("world"));
        assertFalse(queue.matches("wo"));
    }

    @Test
    public void testTokenQueueMatchesCaseSensitive() {
        TokenQueue queue = new TokenQueue("Hello World");
        assertFalse(queue.matchesCS("hello"));
        assertTrue(queue.matchesCS("HELLO"));
        assertTrue(queue.matchesCS("World"));
    }

    @Test
    public void testTokenQueueMatchesAny() {
        TokenQueue queue = new TokenQueue("123abcdef");
        assertTrue(queue.matchesAny("1", "2", "3", "a", "b", "c", "d", "e", "f"));
        assertFalse(queue.matchesAny("g"));
    }

    @Test
    public void testTokenQueueMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("123abcdef");
        assertTrue(queue.matchesAny(new char[]{'1', '2', '3', 'a', 'b', 'c', 'd', 'e', 'f'}));
        assertFalse(queue.matchesAny(new char[]{'g'}));
    }

    @Test
    public void testTokenQueueMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head></head><body></body></html>");
        assertTrue(queue.matchesStartTag());
    }

    @Test
    public void testTokenQueueMatchChomp() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body></body></html>");
        assertTrue(queue.matchChomp("title"));
        assertEquals("Test", queue.consumeToIgnoreCase("Test"));
    }

    @Test
    public void testTokenQueueConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   hello world  ");
        assertTrue(queue.consumeWhitespace());
        assertEquals('h', queue.peek());
    }

    @Test
    public void testTokenQueueConsumeWord() {
        TokenQueue queue = new TokenQueue("hello world");
        assertEquals("hello", queue.consumeWord());
        assertEquals("world", queue.consumeWord());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testTokenQueueConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body></body></html>");
        assertEquals("html", queue.consumeTagName());
        assertEquals("head", queue.consumeTagName());
        assertEquals("title", queue.consumeTagName());
        assertEquals("body", queue.consumeTagName());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testTokenQueueConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("<p class=\"red\"></p>");
        assertEquals("p", queue.consumeElementSelector());
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("red", queue.consumeWord());
        assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenQueueConsumeToUnmatchedSequence() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        queue.consumeTo("five");
    }

    @Test
    public void testTokenQueueChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        assertEquals("four", queue.consumeWord());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testTokenQueueUnescape() {
        String input = "\\(one \\(two\\) three\\)";
        String output = TokenQueue.unescape(input);
        assertEquals("(one (two) three)", output);
    }

    @Test
    public void testTokenQueueRemainder() {
        TokenQueue queue = new TokenQueue("hello world");
        String remainder = queue.remainder();
        assertEquals(" world", remainder);
        queue.consumeWord();
        queue.consumeWord();
        remainder = queue.remainder();
        assertTrue(remainder.isEmpty());
    }

}