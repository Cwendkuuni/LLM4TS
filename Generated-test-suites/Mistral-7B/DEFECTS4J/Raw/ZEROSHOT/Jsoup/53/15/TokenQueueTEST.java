import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abcdef");
        char c = queue.peek();
        assertEquals('a', c);
        assertTrue(queue.isEmpty() || queue.matches("a"));
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekEmptyQueueThrowsException() {
        TokenQueue queue = new TokenQueue("");
        queue.peek();
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.addFirst('x');
        assertEquals('x', queue.consume());
        assertEquals('a', queue.consume());
        // remaining characters should be the same
        for (int i = 0; i < 5; i++) {
            assertEquals(queue.consume(), queue.consume());
        }
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matches("a"));
        assertTrue(queue.matches("bc"));
        assertTrue(queue.matches("ef"));
        assertFalse(queue.matches("g"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("AbCdEf");
        assertTrue(queue.matchesCS("A"));
        assertTrue(queue.matchesCS("B"));
        assertTrue(queue.matchesCS("d"));
        assertFalse(queue.matchesCS("g"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesAny("a", "b", "c"));
        assertTrue(queue.matchesAny("a", "e", "f"));
        assertFalse(queue.matchesAny("g", "h", "i"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesAny('a', 'b', 'c'));
        assertTrue(queue.matchesAny('a', 'e', 'f'));
        assertFalse(queue.matchesAny('g', 'h', 'i'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><body>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abc<def>");
        assertTrue(queue.matchChomp("<def>"));
        assertFalse(queue.matches("def"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\r\n abc ");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("word1 Word2 Word3");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.advance();
        assertEquals('b', queue.consume());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        // remaining characters should be the same
        for (int i = 0; i < 3; i++) {
            assertEquals(queue.consume(), queue.consume());
        }
        assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueueThrowsException() {
        TokenQueue queue = new TokenQueue("");
        queue.consume();
    }

    @Test
    public void testConsumeSequence() {
        TokenQueue queue = new TokenQueue("abcdefghijk");
        String sequence = "def";
        String consumed = queue.consumeTo(sequence);
        assertEquals(sequence, consumed);
        assertEquals('g', queue.consume());
        assertEquals('i', queue.consume());
        assertEquals('j', queue.consume());
    }

    @Test
    public void testConsumeSequenceIgnoreCase() {
        TokenQueue queue = new TokenQueue("AbCdEfGhIjK");
        String sequence = "def";
        String consumed = queue.consumeToIgnoreCase(sequence);
        assertEquals(sequence, consumed);
        assertEquals('G', queue.consume());
        assertEquals('H', queue.consume());
        assertEquals('I', queue.consume());
    }

    @Test
    public void testConsumeSequenceAny() {
        TokenQueue queue = new TokenQueue("abcdefghijk");
        String[] sequences = {"def", "ijk"};
        String consumed = queue.consumeToAny(sequences);
        assertEquals("def", consumed);
        assertEquals('g', queue.consume());
        assertEquals('h', queue.consume());
        assertEquals('i', queue.consume());
        assertEquals("ijk", queue.remainder());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((()()))");
        String balanced = queue.chompBalanced('(', ')');
        assertEquals("(()", balanced);
        assertEquals("()))", queue.remainder());
    }

    @Test
    public void testUnescape() {
        String escaped = "\\(\\)";
        String unescaped = TokenQueue.unescape(escaped);
        assertEquals("()", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\r\n abc ");
        assertTrue(queue.consumeWhitespace());
        assertEquals('a', queue.consume());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("word1 Word2 Word3");
        String word = queue.consumeWord();
        assertEquals("Word1", word);
        assertEquals("Word2", queue.consumeWord());
        assertEquals("Word3", queue.consumeWord());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><body>");
        String tagName = queue.consumeTagName();
        assertEquals("html", tagName);
        assertEquals("body", queue.consumeTagName());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class1.class2::before");
        String elementSelector = queue.consumeElementSelector();
        assertEquals("div.class1.class2", elementSelector);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class1 id1 class2 id2");
        String cssIdentifier = queue.consumeCssIdentifier();
        assertEquals("class1", cssIdentifier);
        assertEquals("id2", queue.consumeCssIdentifier());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class='class1' id='id1'");
        String attributeKey = queue.consumeAttributeKey();
        assertEquals("class", attributeKey);
        assertEquals("id", queue.consumeAttributeKey());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        String remainder = queue.remainder();
        assertEquals("def", remainder);
        assertTrue(queue.isEmpty());
    }
}