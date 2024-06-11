import static org.junit.Assert.*;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertEquals('H', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("World");
        tokenQueue.addFirst('H');
        assertEquals("HelloWorld", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertTrue(tokenQueue.matches("Hello"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertTrue(tokenQueue.matchesCS("Hello"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("ab");
        assertTrue(tokenQueue.matchesAny('a', 'b'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<a");
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertTrue(tokenQueue.matchChomp("Hell"));
        assertEquals("o", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" ");
        assertTrue(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("a");
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        tokenQueue.advance();
        assertEquals("ello", tokenQueue.toString());
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertEquals('H', tokenQueue.consume());
        assertEquals("ello", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("Hello,World");
        assertEquals("Hello", tokenQueue.consumeTo(","));
        assertEquals(",World", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("Hello,World");
        assertEquals("ell", tokenQueue.consumeToIgnoreCase("L"));
        assertEquals("o,World", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("Hello,World");
        assertEquals("Hello", tokenQueue.consumeToAny(","));
        assertEquals(",World", tokenQueue.toString());
    }

    @Test
    public void testChompTo() {
        TokenQueue tokenQueue = new TokenQueue("Hello,World");
        assertEquals("Hello", tokenQueue.chompTo(","));
        assertEquals("World", tokenQueue.toString());
    }

    @Test
    public void testChompToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("Hello,World");
        assertEquals("Hello", tokenQueue.chompToIgnoreCase(","));
        assertEquals("World", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
        assertEquals(" four", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        String escapedString = "\\\"Hello\\\"";
        String unescapedString = TokenQueue.unescape(escapedString);
        assertEquals("\"Hello\"", unescapedString);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   Hello");
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals("Hello", tokenQueue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("Hello4");
        assertEquals("Hello4", tokenQueue.consumeWord());
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("div");
        assertEquals("div", tokenQueue.consumeTagName());
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("div");
        assertEquals("div", tokenQueue.consumeElementSelector());
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("hello-world");
        assertEquals("hello-world", tokenQueue.consumeCssIdentifier());
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("data-key");
        assertEquals("data-key", tokenQueue.consumeAttributeKey());
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertEquals("Hello", tokenQueue.remainder());
        assertEquals("", tokenQueue.toString());
    }

}