import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testCreateQueue() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        assertEquals(data, queue.queue);
        assertEquals(0, queue.pos);
    }

    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.isEmpty());

        data = "abcdef";
        queue = new TokenQueue(data);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        assertEquals('a', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekAfterConsumeAll() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        queue.consume();
        queue.consume();
        queue.consume();
        queue.peek();
    }

    @Test
    public void testAddFirst() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        queue.addFirst('x');
        assertEquals('x', queue.queue.charAt(0));
        assertEquals('a', queue.queue.charAt(1));
    }

    @Test
    public void testMatches() {
        String data = "abcdefghijklmno";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("pq"));
    }

    @Test
    public void testMatchesCS() {
        String data = "ABCDEFGHIJKLMNOPQRST";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesCS("AB"));
        assertFalse(queue.matchesCS("ef"));
    }

    @Test
    public void testMatchesAny() {
        String data = "abcdefghijklmno";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesAny("ab", "cd"));
        assertTrue(queue.matchesAny("ef", "gh", "ij"));
        assertFalse(queue.matchesAny("pq"));
    }

    @Test
    public void testMatchesAnyChar() {
        String data = "abcdefghijklmno";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesAny('a', 'b'));
        assertTrue(queue.matchesAny('e', 'f', 'g'));
        assertFalse(queue.matchesAny('p'));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<ab><cd><ef>";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        String data = "<ab>cd<ef>";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchChomp("ab"));
        assertEquals("cd<ef>", queue.queue);

        assertTrue(queue.matchChomp("cd"));
        assertEquals("<ef>", queue.queue);

        assertFalse(queue.matchChomp("xy"));
        assertEquals("<ef>", queue.queue);
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " \t\r\n abcdefghijklmno";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "abcdefghijklmno";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        queue.advance();
        assertEquals('b', queue.queue.charAt(0));
        assertEquals('c', queue.queue.charAt(1));
    }

    @Test
    public void testConsume() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals('d', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('f', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeAfterEmpty() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        queue.consume();
    }

    @Test
    public void testConsumeSequence() {
        String data = "abcdefghijklmno<ab>cd<ef>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("ab", queue.consumeTo("ab"));
        assertEquals("cd<ef>", queue.queue);

        assertEquals("cd", queue.consumeTo("cd"));
        assertEquals("<ef>", queue.queue);

        assertEquals("", queue.consumeTo("xy"));
        assertEquals("<ef>", queue.queue);
    }

    @Test
    public void testConsumeSequenceIgnoreCase() {
        String data = "ABCDEFGHIJKLMNO<ab>CD<EF>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("ab", queue.consumeToIgnoreCase("ab"));
        assertEquals("CD<EF>", queue.queue);

        assertEquals("cd", queue.consumeToIgnoreCase("cd"));
        assertEquals("<EF>", queue.queue);

        assertEquals("", queue.consumeToIgnoreCase("XY"));
        assertEquals("<EF>", queue.queue);
    }

    @Test
    public void testConsumeSequenceAny() {
        String data = "abcdefghijklmno<ab>cd<ef>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("ab", queue.consumeToAny("ab", "cd"));
        assertEquals("cd<ef>", queue.queue);

        assertEquals("cd", queue.consumeToAny("cd"));
        assertEquals("<ef>", queue.queue);

        assertEquals("", queue.consumeToAny("xy"));
        assertEquals("<ef>", queue.queue);
    }

    @Test
    public void testChompBalanced() {
        String data = "()()()()()";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("()()()()()", queue.chompBalanced('(', ')'));
        assertEquals("", queue.queue);

        assertEquals("(())", queue.chompBalanced('(', ')'));
        assertEquals("()()", queue.queue);

        assertEquals("()()()()()", queue.chompBalanced('{', '}'));
        assertEquals("()()()()()", queue.queue);

        assertEquals("(())", queue.chompBalanced('{', '}'));
        assertEquals("()()", queue.queue);

        assertEquals("abc(def)ghij", queue.chompBalanced('(', ')'));
        assertEquals("abcdefghij", queue.queue);
    }

    @Test
    public void testUnescape() {
        String data = "\\\\\\\\a\\bc\\de\\ff\\n\\r\\tt\\b\\f";
        String expected = "\\abcdeff\n\r\t\b\f";
        String result = TokenQueue.unescape(data);
        assertEquals(expected, result);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = " \t\r\n abcdefghijklmno";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        String data = "abcdefghijklmno";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("abc", queue.consumeWord());
        assertEquals("def", queue.consumeWord());
        assertEquals("ghij", queue.consumeWord());
        assertEquals("", queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        String data = "<ab><cd><ef>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("ab", queue.consumeTagName());
        assertEquals("cd", queue.consumeTagName());
        assertEquals("ef", queue.consumeTagName());
        assertEquals("", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "div.class1 div.class2#id1 div.class3";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("class1", queue.consumeElementSelector());
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("class2", queue.consumeElementSelector());
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("class3", queue.consumeElementSelector());
        assertEquals("", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "color: #ff0000; background: url(image.jpg);";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("color", queue.consumeCssIdentifier());
        assertEquals("#ff0000", queue.consumeCssIdentifier());
        assertEquals("background", queue.consumeCssIdentifier());
        assertEquals("url(image.jpg)", queue.consumeCssIdentifier());
        assertEquals("", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "class='class1' id='id1' style='color: red;'";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("class1", queue.consumeAttributeKey());
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("id1", queue.consumeAttributeKey());
        assertEquals("style", queue.consumeAttributeKey());
        assertEquals("", queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        String data = "abcdef";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("def", queue.remainder());
        assertEquals("", queue.queue);
    }
}