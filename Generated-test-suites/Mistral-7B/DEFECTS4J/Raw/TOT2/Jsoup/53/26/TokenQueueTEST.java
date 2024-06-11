import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        String data = "sample data";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("sample data", queue.queue);
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("sample data");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("sample data");
        assertEquals('s', queue.peek());
        queue.advance();
        assertEquals('a', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.peek();
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        queue.addFirst("sample");
        assertEquals("samplea", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("sample data");
        assertTrue(queue.matches("sa"));
        assertTrue(queue.matches("sample data"));
        assertFalse(queue.matches("sd"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Sample Data");
        assertTrue(queue.matchesCS("SA"));
        assertFalse(queue.matchesCS("sd"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("sample data");
        assertTrue(queue.matchesAny("sa", "ample"));
        assertFalse(queue.matchesAny("sd"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("sample data");
        assertTrue(queue.matchesAny(new char[]{'s', 'a'}));
        assertFalse(queue.matchesAny(new char[]{'s', 'd'}));
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("sample data");
        assertTrue(queue.matchChomp("sa"));
        assertEquals("ample data", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("sample data");
        assertTrue(queue.matchesWhitespace());
        queue.advance();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("sample data");
        assertTrue(queue.matchesWord());
        queue.advance();
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("sample data");
        queue.advance();
        assertEquals('a', queue.consume());
        assertEquals('s', queue.consume());
        assertEquals('p', queue.consume());
        assertEquals('l', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals(' ', queue.consume());
        assertEquals('d', queue.consume());
        assertEquals('a', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume();
    }

    @Test
    public void testConsumeString() {
        TokenQueue queue = new TokenQueue("sample data");
        String consumed = queue.consume("sa");
        assertEquals("ample data", queue.toString());
        assertEquals("sa", consumed);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeMismatchedString() {
        TokenQueue queue = new TokenQueue("sample data");
        queue.consume("sd");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("sample data");
        String consumed = queue.consumeTo("sa");
        assertEquals("ample data", queue.toString());
        assertEquals("sample", consumed);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("Sample Data");
        String consumed = queue.consumeToIgnoreCase("Sa");
        assertEquals("ample data", queue.toString());
        assertEquals("Sample", consumed);
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("sample data");
        String consumed = queue.consumeToAny("sa", "ample");
        assertEquals("ample data", queue.toString());
        assertEquals("sample", consumed);
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(sample (data) three) four");
        String balanced = queue.chompBalanced('(', ')');
        assertEquals("sample (data) three", queue.toString());
        assertEquals("(sample (data) three)", balanced);
    }

    @Test
    public void testChompBalancedEmpty() {
        TokenQueue queue = new TokenQueue("");
        String balanced = queue.chompBalanced('(', ')');
        assertTrue(queue.isEmpty());
        assertEquals("", balanced);
    }

    @Test
    public void testChompBalancedUnmatched() {
        TokenQueue queue = new TokenQueue("(sample (data) three) four");
        String balanced = queue.chompBalanced('(', '}');
        assertEquals("(sample (data) three) four", queue.toString());
        assertEquals("", balanced);
    }

    @Test
    public void testUnescape() {
        String in = "\\(sample \\(data\\) three\\) four";
        String out = TokenQueue.unescape(in);
        assertEquals("(sample (data) three) four", out);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" sample data ");
        boolean seen = queue.consumeWhitespace();
        assertEquals("sample data", queue.toString());
        assertTrue(seen);
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("sample data");
        String word = queue.consumeWord();
        assertEquals("sample", queue.toString());
        assertEquals("sample", word);
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<div class='sample' id='data'>three</div>");
        String tagName = queue.consumeTagName();
        assertEquals("<div class='sample' id='data'>three</div>", queue.toString());
        assertEquals("div", tagName);
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.sample:hover");
        String selector = queue.consumeElementSelector();
        assertEquals("div.sample:hover", queue.toString());
        assertEquals("div", selector);
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color: red; background-color: #f00");
        String identifier = queue.consumeCssIdentifier();
        assertEquals("color: red; background-color: #f00", queue.toString());
        assertEquals("color", identifier);
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("id='sample' class='data'");
        String key = queue.consumeAttributeKey();
        assertEquals("id='sample' class='data'", queue.toString());
        assertEquals("id", key);
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("sample data");
        String remainder = queue.remainder();
        assertEquals("sample data", queue.toString());
        assertEquals("data", remainder);
    }
}