import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testTokenQueueEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testTokenQueueAddFirstString() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        String expected = "hello";
        tokenQueue.addFirst("hello");
        assertEquals(expected, tokenQueue.peek());
    }

    @Test
    public void testTokenQueueAddFirstCharacter() {
        TokenQueue tokenQueue = new TokenQueue("hello world");;
        char expected = 'h';
        tokenQueue.addFirst('h');
        assertEquals(expected, tokenQueue.peek());
    }

    @Test
    public void testTokenQueueMatches() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        String expected = "hello";
        assertTrue(tokenQueue.matches(expected));
        assertEquals(expected, tokenQueue.consumeTo(expected));
    }

    @Test
    public void testTokenQueueMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("HELLO WORLD");
        String expected = "HELLO";
        assertTrue(tokenQueue.matchesCS(expected));
        assertEquals(expected, tokenQueue.consumeToIgnoreCase(expected));
    }

    @Test
    public void testTokenQueueMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("123abcd");
        String[] expected = {"1", "2", "3", "a", "b", "c"};
        for (String s : expected) {
            assertTrue(tokenQueue.matchesAny(s));
            assertEquals(s, tokenQueue.consumeToAny(s));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenQueueMatchesFailure() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        String unexpected = "howdy";
        tokenQueue.consumeTo(unexpected);
    }

    @Test
    public void testTokenQueueChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        String expected = "one (two) three";
        assertEquals(expected, tokenQueue.chompBalanced('(', ')').replaceAll("\\\\", ""));
    }

    @Test
    public void testTokenQueueUnescape() {
        TokenQueue tokenQueue = new TokenQueue("\\(one \\(two\\) three\\) four");
        String expected = "(one (two) three)";
        assertEquals(expected, TokenQueue.unescape(tokenQueue.consumeToAny("\\(", "\\)").toString()));
    }

    @Test
    public void testTokenQueueConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   hello world   ");
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals("hello world", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        String expected = "hello";
        assertEquals(expected, tokenQueue.consumeWord());
        assertEquals("world", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("<html><head><title>Test</title></head><body>Hello World</body></html>");
        String expected = "html";
        assertEquals(expected, tokenQueue.consumeTagName());
        assertEquals("<head><title>Test</title></head><body>Hello World</body></html>", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("<div#test-id.test-class>Content</div>");
        String expected = "div";
        assertEquals(expected, tokenQueue.consumeElementSelector());
        assertEquals("#test-id.test-class", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("color: red; border: 1px solid black;");
        String expected = "color";
        assertEquals(expected, tokenQueue.consumeCssIdentifier());
        assertEquals("border: 1px solid black;", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("class=\"test test-class\" id=\"test-id\"");
        String expected = "class";
        assertEquals(expected, tokenQueue.consumeAttributeKey());
        assertEquals("id=\"test-id\"", tokenQueue.toString());
    }
}