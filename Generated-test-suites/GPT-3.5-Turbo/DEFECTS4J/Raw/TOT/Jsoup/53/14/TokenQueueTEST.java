import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("Hello World! 123");
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
        tokenQueue.advance(); // Consume 'H'
        assertFalse(tokenQueue.isEmpty());
        tokenQueue.consume("ello"); // Consume 'ello'
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals('H', tokenQueue.peek());
        tokenQueue.advance(); // Consume 'H'
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('A');
        assertEquals('A', tokenQueue.peek());
        tokenQueue.addFirst("BCD");
        assertEquals('D', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("Hello"));
        assertFalse(tokenQueue.matches("World"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("Hello"));
        assertFalse(tokenQueue.matchesCS("hello"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny("Hello", "W", "123"));
        assertFalse(tokenQueue.matchesAny("W", "123"));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.advance(); // Consume 'H'
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.consume("ello"); // Consume 'ello'
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.advance(); // Consume ' '
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.advance(); // Consume 'W'
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.advance(); // Consume 'o'
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.advance(); // Consume 'r'
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.advance(); // Consume 'l'
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.advance(); // Consume 'd'
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.advance(); // Consume '!'
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.advance(); // Consume ' '
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.advance(); // Consume '1'
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.advance(); // Consume '2'
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.advance(); // Consume '3'
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("Hello"));
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testMatchesWhitespace() {
        assertFalse(tokenQueue.matchesWhitespace());
        tokenQueue.advance(); // Consume 'H'
        assertFalse(tokenQueue.matchesWhitespace());
        tokenQueue.consume("ello"); // Consume 'ello'
        assertTrue(tokenQueue.matchesWhitespace());
        tokenQueue.advance(); // Consume ' '
        assertTrue(tokenQueue.matchesWhitespace());
        tokenQueue.advance(); // Consume 'W'
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance(); // Consume 'H'
        assertFalse(tokenQueue.matchesWord());
        tokenQueue.consume("ello"); // Consume 'ello'
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance(); // Consume ' '
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        assertEquals('H', tokenQueue.peek());
        tokenQueue.advance();
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        assertEquals('H', tokenQueue.consume());
        assertEquals('e', tokenQueue.peek());
        tokenQueue.consume("ello");
        assertEquals(' ', tokenQueue.consume());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hello", tokenQueue.consumeTo(" "));
        assertEquals(' ', tokenQueue.peek());
        assertEquals("World!", tokenQueue.consumeTo("123"));
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testChompTo() {
        assertEquals("Hello ", tokenQueue.chompTo("!"));
        assertEquals('1', tokenQueue.peek());
    }

    @Test
    public void testChompBalanced() {
        String balancedString = "one (two) three";
        assertEquals(balancedString, tokenQueue.chompBalanced('(', ')'));
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testUnescape() {
        String escapedString = "\\\"Hello\\\" World!";
        String unescapedString = "\"Hello\" World!";
        assertEquals(unescapedString, TokenQueue.unescape(escapedString));
    }

    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.matchesWhitespace());
        tokenQueue.consumeWhitespace();
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeWord() {
        assertEquals("Hello", tokenQueue.consumeWord());
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("Hello", tokenQueue.consumeTagName());
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeElementSelector() {
        assertEquals("Hello", tokenQueue.consumeElementSelector());
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("Hello", tokenQueue.consumeCssIdentifier());
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeAttributeKey() {
        assertEquals("Hello", tokenQueue.consumeAttributeKey());
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testRemainder() {
        assertEquals("Hello World! 123", tokenQueue.remainder());
        assertTrue(tokenQueue.isEmpty());
    }

}