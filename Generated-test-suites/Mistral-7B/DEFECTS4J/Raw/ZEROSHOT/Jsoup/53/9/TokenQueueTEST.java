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

        queue.advance();
        assertEquals('b', queue.peek());

        queue.consume();
        assertEquals('c', queue.peek());

        queue.consumeTo("bc");
        assertEquals('', queue.peek());

        queue.addFirst("xyz");
        assertEquals('x', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst("xyz");
        assertEquals("xyzabc", queue.toString());

        queue.addFirst(5);
        assertEquals("5xyzabc", queue.toString());

        queue.addFirst(true);
        assertEquals("true5xyzabc", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matches("a"));
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("b"));
        assertFalse(queue.matches("c"));
        assertFalse(queue.matches("d"));

        queue.consume();
        assertFalse(queue.matches("a"));
        assertTrue(queue.matches("b"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("AbC");
        assertTrue(queue.matchesCS("A"));
        assertTrue(queue.matchesCS("Ab"));
        assertFalse(queue.matchesCS("B"));
        assertFalse(queue.matchesCS("C"));
        assertFalse(queue.matchesCS("d"));

        queue.consume();
        assertFalse(queue.matchesCS("A"));
        assertTrue(queue.matchesCS("B"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny("a", "b"));
        assertTrue(queue.matchesAny("a", "c"));
        assertTrue(queue.matchesAny("a", "d"));
        assertTrue(queue.matchesAny("b", "c"));
        assertTrue(queue.matchesAny("b", "d"));
        assertTrue(queue.matchesAny("c", "d"));
        assertFalse(queue.matchesAny("e", "f"));

        queue = new TokenQueue("xyz");
        assertTrue(queue.matchesAny("x", "y", "z"));
        assertTrue(queue.matchesAny("x", "w", "z"));
        assertFalse(queue.matchesAny("a", "b", "c"));
    }

    @Test
    public void testMatchesAnyWithChars() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny('a', 'b'));
        assertTrue(queue.matchesAny('a', 'c'));
        assertTrue(queue.matchesAny('a', 'd'));
        assertTrue(queue.matchesAny('b', 'c'));
        assertTrue(queue.matchesAny('b', 'd'));
        assertTrue(queue.matchesAny('c', 'd'));
        assertFalse(queue.matchesAny('e', 'f'));

        queue = new TokenQueue("xyz");
        assertTrue(queue.matchesAny('x', 'y', 'z'));
        assertTrue(queue.matchesAny('x', 'w', 'z'));
        assertFalse(queue.matchesAny('a', 'b', 'c'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>");
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
        TokenQueue queue = new TokenQueue("   abc   ");
        assertTrue(queue.matchesWhitespace());

        queue.consumeWhitespace();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertTrue(queue.matchesWord());

        queue.consume();
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        assertEquals('b', queue.peek());

        queue.advance();
        assertEquals('c', queue.peek());

        queue.consume();
        assertEquals('c', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.consume());

        assertEquals('b', queue.consume());

        assertEquals('c', queue.consume());

        assertEquals("", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume("xyz");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithWrongSequence() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("12345");
        assertEquals("2345", queue.consumeTo("345"));
        assertEquals("", queue.toString());

        queue = new TokenQueue("abcdef");
        assertEquals("bcdef", queue.consumeTo("cdef"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("AbCdEfG");
        assertEquals("AbCd", queue.consumeToIgnoreCase("AbCd"));
        assertEquals("EfG", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToIgnoreCaseWithWrongSequence() {
        TokenQueue queue = new TokenQueue("AbCdEfG");
        queue.consumeToIgnoreCase("AbCe");
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("bcdef", queue.consumeToAny("cdef"));
        assertEquals("a", queue.peek());
    }

    @Test
    public void testChompTo() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        assertEquals("four", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        assertEquals("four", queue.toString());

        queue = new TokenQueue("([a-z]*)");
        assertEquals("", queue.chompBalanced('[', ']'));
        assertEquals("([a-z]*)", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("", TokenQueue.unescape(""));
        assertEquals("\\\\", TokenQueue.unescape("\\\\"));
        assertEquals("abc\\def\\ghi", TokenQueue.unescape("abc\\def\\ghi"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        assertTrue(queue.consumeWhitespace());
        assertEquals("abc", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abc123xyz");
        assertEquals("abc", queue.consumeWord());
        assertEquals("123xyz", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
        assertEquals("title", queue.consumeTagName());
        assertEquals("Test", queue.consumeWord());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("li.selected:first-child");
        assertEquals("li", queue.consumeElementSelector());
        assertEquals("selected", queue.consumeCssIdentifier());
        assertEquals(":first-child", queue.consumeTagName());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class:selected id:test");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("selected", queue.consumeCssIdentifier());
        assertEquals(" id", queue.consumeAttributeKey());
        assertEquals("test", queue.consumeCssIdentifier());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class:selected id:test style:color:red");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("selected", queue.consumeCssIdentifier());
        assertEquals(" id", queue.consumeAttributeKey());
        assertEquals("test", queue.consumeCssIdentifier());
        assertEquals(" style:", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.remainder());
        assertEquals("abc", queue.toString());

        queue = new TokenQueue("");
        assertEquals("", queue.toString());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals("abc", queue.toString());

        queue = new TokenQueue("def");
        assertEquals("def", queue.toString());
    }
}