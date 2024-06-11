import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        String data = "some data";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals(data, tokenQueue.queue);
    }

    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.isEmpty());

        data = "some data";
        tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertEquals('h', tokenQueue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekWhenEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.peek();
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("data");
        tokenQueue.addFirst('a');
        assertEquals('a', tokenQueue.queue.charAt(0));

        tokenQueue.addFirst("test");
        assertEquals("testdata", tokenQueue.queue);
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("some data");
        assertTrue(tokenQueue.matches("some"));
        assertTrue(tokenQueue.matches("data"));
        assertFalse(tokenQueue.matches("test"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("Some Data");
        assertTrue(tokenQueue.matchesCS("Some"));
        assertFalse(tokenQueue.matchesCS("some"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("123aBc");
        assertTrue(tokenQueue.matchesAny("1", "3", "a", "B"));
        assertFalse(tokenQueue.matchesAny("d"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue tokenQueue = new TokenQueue("abcd");
        assertTrue(tokenQueue.matchesAny(new char[]{'a', 'b', 'c'}));
        assertFalse(tokenQueue.matchesAny(new char[]{'e'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<html>");
        assertTrue(tokenQueue.matchesStartTag());

        tokenQueue = new TokenQueue(" <html>");
        assertFalse(tokenQueue.matchesStartTag());

        tokenQueue = new TokenQueue("abc<html>");
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("some data some more data");
        assertTrue(tokenQueue.matchChomp("some"));
        assertEquals(" more data", tokenQueue.queue);

        assertFalse(tokenQueue.matchChomp("test"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" some data ");
        assertTrue(tokenQueue.matchesWhitespace());

        tokenQueue = new TokenQueue("not whitespace");
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertTrue(tokenQueue.matchesWord());
        assertEquals('h', tokenQueue.consume().charAt(0));
    }

    @Test
    public void testAdvance() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        tokenQueue.advance();
        assertEquals('e', tokenQueue.consume().charAt(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWhenEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.consume();
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertEquals('h', tokenQueue.consume());
        assertEquals("ello world", tokenQueue.queue);
    }

    @Test
    public void testConsumeString() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        String consumed = tokenQueue.consume("hello");
        assertEquals(" world", tokenQueue.queue);
        assertEquals("hello", consumed);
    }

    @Test(expected = IllegalStateException.class)
    public
    void testConsumeStringWhenNoMatchFound() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        tokenQueue.consume("test");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        String consumed = tokenQueue.consumeTo("(one (two)");
        assertEquals("(one (two)", tokenQueue.queue);
        assertEquals("three) four", consumed);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        String consumed = tokenQueue.consumeToIgnoreCase("(one (two)");
        assertEquals("(one (two)", tokenQueue.queue);
        assertEquals("three) four", consumed);
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        String balanced = tokenQueue.chompBalanced('(', ')');
        assertEquals("one (two) three", balanced);
        assertEquals(" four", tokenQueue.queue);
    }

    @Test
    public void testUnescape() {
        String escaped = "\\(one \\(two\\) three\\) four";
        String unescaped = TokenQueue.unescape(escaped);
        assertEquals("(one (two) three) four", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" some data ");
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals("data", tokenQueue.queue);
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        String word = tokenQueue.consumeWord();
        assertEquals("hello", word);
        assertEquals(" world", tokenQueue.queue);
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("<html>");
        String tagName = tokenQueue.consumeTagName();
        assertEquals("html", tagName);
        assertEquals("", tokenQueue.queue);
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("div.class:hover");
        String elementSelector = tokenQueue.consumeElementSelector();
        assertEquals("div.class", elementSelector);
        assertEquals(":hover", tokenQueue.queue);
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("color: red;");
        String cssIdentifier = tokenQueue.consumeCssIdentifier();
        assertEquals("color", cssIdentifier);
        assertEquals(": red", tokenQueue.queue);
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("id=\"myId\" class=\"myClass\"");
        String attributeKey = tokenQueue.consumeAttributeKey();
        assertEquals("id", attributeKey);
        assertEquals(" class=\"myClass\"", tokenQueue.queue);
    }

    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        String remainder = tokenQueue.remainder();
        assertEquals(" world", remainder);
        assertEquals("", tokenQueue.queue);
    }
}