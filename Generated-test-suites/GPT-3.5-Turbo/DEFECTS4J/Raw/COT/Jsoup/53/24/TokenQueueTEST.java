import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("data");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("data");
        assertEquals('d', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("data");
        queue.addFirst('c');
        assertEquals('c', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("data");
        assertTrue(queue.matches("da"));
        assertFalse(queue.matches("at"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("data");
        assertTrue(queue.matchesCS("da"));
        assertFalse(queue.matchesCS("DA"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("data");
        assertTrue(queue.matchesAny("at", "da"));
        assertFalse(queue.matchesAny("AT", "DA"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<tag>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("data");
        assertTrue(queue.matchChomp("da"));
        assertFalse(queue.matchChomp("at"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" ");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("a1");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("data");
        queue.advance();
        assertEquals('a', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("data");
        assertEquals('d', queue.consume());
        assertEquals('a', queue.peek());
    }

    @Test
    public void testConsumeString() {
        TokenQueue queue = new TokenQueue("data");
        queue.consume("da");
        assertEquals('t', queue.peek());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("one two three");
        assertEquals("one", queue.consumeTo(" "));
        assertEquals(" ", queue.peek());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("one TWO three");
        assertEquals("one ", queue.consumeToIgnoreCase("two"));
        assertEquals("two", queue.peek());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("one-two-three");
        assertEquals("one", queue.consumeToAny("-"));
        assertEquals("-", queue.peek());
    }

    @Test
    public void testChompTo() {
        TokenQueue queue = new TokenQueue("one two three");
        assertEquals("one ", queue.chompTo(" "));
        assertEquals("two three", queue.peek());
    }

    @Test
    public void testChompToIgnoreCase() {
        TokenQueue queue = new TokenQueue("one TWO three");
        assertEquals("one ", queue.chompToIgnoreCase("two"));
        assertEquals("two three", queue.peek());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("(one (two) three)", queue.chompBalanced('(', ')'));
        assertEquals(" ", queue.peek());
    }

    @Test
    public void testUnescape() {
        String escaped = "\\\"one\\\" \\\\'two\\'";
        String unescaped = TokenQueue.unescape(escaped);
        assertEquals("\"one\" 'two'", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   data");
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
        assertEquals('d', queue.peek());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("text 123");
        assertEquals("text", queue.consumeWord());
        assertEquals(' ', queue.peek());
    }
    
    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("tag:foo");
        assertEquals("tag", queue.consumeTagName());
        assertEquals(':', queue.peek());
    }
    
    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("tag|foo");
        assertEquals("tag", queue.consumeElementSelector());
        assertEquals('|', queue.peek());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("identifier-ab1");
        assertEquals("identifier-ab1", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("attr:key");
        assertEquals("attr:key", queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("data");
        assertEquals("data", queue.remainder());
        assertTrue(queue.isEmpty());
    }
    
    // Add additional test cases for edge cases and exception handling scenarios here

}

