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
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        queue.addFirst("def");
        assertEquals("defa", queue.toString());

        queue.advance();
        assertEquals('d', queue.peek());
    }

    @Test(expected = NullPointerException.class)
    public void testAddFirstNull() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst(null);
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("def"));
        assertFalse(queue.matches("xyz"));

        queue.advance();
        assertTrue(queue.matches("ef"));
        assertFalse(queue.matches("ab"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCdef");
        assertTrue(queue.matchesCS("AB"));
        assertTrue(queue.matchesCS("def"));
        assertFalse(queue.matchesCS("xyz"));

        queue.advance();
        assertTrue(queue.matchesCS("ef"));
        assertFalse(queue.matchesCS("AB"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesAny("ab", "ef"));
        assertFalse(queue.matchesAny("xyz", "pqr"));

        queue.advance();
        assertTrue(queue.matchesAny("ef", "ef"));
        assertFalse(queue.matchesAny("ab", "ef"));
    }

    @Test
    public void testMatchesAnyCharArray() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesAny(new char[]{'a', 'e'}));
        assertFalse(queue.matchesAny(new char[]{'x', 'y'}));

        queue.advance();
        assertTrue(queue.matchesAny(new char[]{'e', 'f'}));
        assertFalse(queue.matchesAny(new char[]{'a', 'e'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abcdef>");
        assertTrue(queue.matchesStartTag());

        queue.advance();
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abc<def>");
        assertTrue(queue.matchChomp("abc"));
        assertEquals("def", queue.toString());

        queue = new TokenQueue("xyz<abcdef>");
        assertFalse(queue.matchChomp("abc"));
        assertEquals("xyz<abcdef>", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc  ");
        assertTrue(queue.matchesWhitespace());

        queue.advance();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc def");
        assertTrue(queue.matchesWord());

        queue.advance();
        assertTrue(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.advance();
        assertEquals('b', queue.peek());

        queue.advance();
        assertEquals('d', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.peek());

        queue.advance();
        assertEquals('d', queue.consume());
        assertEquals("ef", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume();
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNotMatchedSequence() {
        TokenQueue queue = new TokenQueue("xyz<abcdef>");
        queue.consume("abc");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.consumeTo("("));
        assertEquals(" four", queue.toString());

        queue = new TokenQueue("((pq)r)s");
        assertEquals("(pq)", queue.consumeTo("("));
        assertEquals("r)s", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.consumeToIgnoreCase("("));
        assertEquals(" four", queue.toString());

        queue = new TokenQueue("((PQ)R)S");
        assertEquals("(PQ)", queue.consumeToIgnoreCase("("));
        assertEquals("R)S", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.consumeToAny("(", "three"));
        assertEquals(" four", queue.toString());

        queue = new TokenQueue("((pq)r)s");
        assertEquals("(pq)", queue.consumeToAny("(", "r"));
        assertEquals("s)", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        assertEquals(" four", queue.toString());

        queue = new TokenQueue("(one (two (three) four) five) six");
        assertEquals("one (two (three) four)", queue.chompBalanced('(', ')'));
        assertEquals(" five) six", queue.toString());
    }

    @Test
    public void testUnescape() {
        String in = "\\(one \\(|two\\)|three\\) four";
        String out = TokenQueue.unescape(in);
        assertEquals("(one (two) three) four", out);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        assertTrue(queue.consumeWhitespace());
        assertEquals("abc", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abc def ghi jkl");
        assertEquals("abc", queue.consumeWord());
        assertEquals("def ghi jkl", queue.toString());

        queue = new TokenQueue("abc def ghi jkl");
        assertEquals("def", queue.consumeWord());
        assertEquals("ghi jkl", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("div id='id1' class='class1'");
        assertEquals("div", queue.consumeTagName());
        assertEquals(" id='id1' class='class1'", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#id1.class1");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("#id1.class1", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("id:id1 class:class1");
        assertEquals("id1", queue.consumeCssIdentifier());
        assertEquals("class:class1", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("id='id1' class='class1'");
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("='id1' class='class1'", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.remainder());
        assertEquals("", queue.toString());
    }
}