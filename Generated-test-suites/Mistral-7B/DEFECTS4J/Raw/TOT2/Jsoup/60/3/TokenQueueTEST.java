import org.junit.Test;
import static org.junit.Assert.*;

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

        queue.consume();
        assertEquals('b', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst('x');
        assertEquals('x', queue.peek());

        queue.consume();
        assertEquals('a', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("def"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABC");
        assertTrue(queue.matchesCS("AB"));
        assertFalse(queue.matchesCS("def"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny("ab", "cd"));
        assertFalse(queue.matchesAny("ef"));
    }

    @Test
    public void testMatchesAnyWithChars() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny('a', 'b'));
        assertFalse(queue.matchesAny('d'));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc");
        assertTrue(queue.matchesWhitespace());

        queue.consumeWhitespace();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord("123"));
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithEmptySequence() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consume("xyz");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithSequenceLongerThanQueue() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consume("def");
        queue.consume("xyz");
        queue.consume("123"); // queue is now empty
        queue.consume("ghi"); // should throw an exception
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        assertEquals(" four", queue.remainder());
    }
}