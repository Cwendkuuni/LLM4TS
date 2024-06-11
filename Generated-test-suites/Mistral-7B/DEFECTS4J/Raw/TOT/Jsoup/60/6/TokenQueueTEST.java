import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue.addFirst("test");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.peek());
        assertEquals('a', queue.consume());
        assertEquals('b', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddFirstAndConsumeEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("test");
        queue.consume();
        queue.addFirst("test2");
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("cd"));
        assertFalse(queue.matches("xy"));

        queue.advance();
        assertFalse(queue.matches("ab"));
        assertTrue(queue.matches("cd"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesAny("ab", "cd"));
        assertTrue(queue.matchesAny("ef", "xy"));
        assertFalse(queue.matchesAny("xy", "ef"));

        queue.advance();
        assertTrue(queue.matchesAny("cd", "ef"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abcdef   ");
        assertTrue(queue.matchesWhitespace());
        queue.advance();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc def ghi jkl mno pqr stu vwxyz");
        assertTrue(queue.matchesWord());
        queue.advance();
        assertTrue(queue.matchesWord());
        queue.advance();
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals('d', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('f', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeString() {
        TokenQueue queue = new TokenQueue("abcd abcd");
        String consumed = queue.consume("ab");
        assertEquals("cd cd", consumed);

        queue.advance();
        queue.advance();
        consumed = queue.consume("cd");
        assertEquals("ef ef", consumed);
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(())()");
        String consumed = queue.chompBalanced('(', ')');
        assertEquals("()()", consumed);
        assertEquals("()", queue.toString());

        queue = new TokenQueue("(ab)(cd)ef");
        consumed = queue.chompBalanced('(', ')');
        assertEquals("abcd", consumed);
        assertEquals("ef", queue.toString());

        queue = new TokenQueue("(abc)(def)ghi");
        consumed = queue.chompBalanced('(', ')');
        assertEquals("abcdef", consumed);
        assertEquals("ghi", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        String remainder = queue.remainder();
        assertEquals("def", remainder);

        queue = new TokenQueue("(abc)(def)ghi");
        remainder = queue.remainder();
        assertEquals("ghi", remainder);
    }
}