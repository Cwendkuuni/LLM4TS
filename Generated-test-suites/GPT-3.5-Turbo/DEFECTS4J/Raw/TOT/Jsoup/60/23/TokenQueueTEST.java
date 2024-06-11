import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("Hello world!");
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        char expected = 'H';
        char actual = tokenQueue.peek();
        assertEquals(expected, actual);
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('T');
        char expected = 'T';
        char actual = tokenQueue.peek();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("Hello"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("Hello"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny("Hello", "World"));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("Hello"));
    }

    @Test
    public void testMatchesWhitespace() {
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        tokenQueue.advance();
        char expected = 'e';
        char actual = tokenQueue.peek();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsume() {
        char expected = 'H';
        char actual = tokenQueue.consume();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeString() {
        tokenQueue.consume("Hello");
        char expected = ' ';
        char actual = tokenQueue.peek();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo() {
        String expected = "Hello";
        String actual = tokenQueue.consumeTo(" ");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String expected = "Hello";
        String actual = tokenQueue.consumeToIgnoreCase(" ");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        String expected = "Hello";
        String actual = tokenQueue.consumeToAny(" ", "!");
        assertEquals(expected, actual);
    }

    @Test
    public void testChompTo() {
        String expected = "Hello";
        String actual = tokenQueue.chompTo(" ");
        assertEquals(expected, actual);
    }

    @Test
    public void testChompToIgnoreCase() {
        String expected = "Hello";
        String actual = tokenQueue.chompToIgnoreCase(" ");
        assertEquals(expected, actual);
    }

    @Test
    public void testChompBalanced() {
        String expected = "(Hello)";
        String actual = tokenQueue.chompBalanced('(', ')');
        assertEquals(expected, actual);
    }

    @Test
    public void testUnescape() {
        String expected = "\\";
        String actual = TokenQueue.unescape("\\\\");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeWhitespace() {
        boolean actual = tokenQueue.consumeWhitespace();
        assertFalse(actual);
    }

    @Test
    public void testConsumeWord() {
        String expected = "Hello";
        String actual = tokenQueue.consumeWord();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTagName() {
        String expected = "Hello";
        String actual = tokenQueue.consumeTagName();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeElementSelector() {
        String expected = "Hello";
        String actual = tokenQueue.consumeElementSelector();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeCssIdentifier() {
        String expected = "Hello";
        String actual = tokenQueue.consumeCssIdentifier();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeAttributeKey() {
        String expected = "Hello";
        String actual = tokenQueue.consumeAttributeKey();
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        String expected = "Hello world!";
        String actual = tokenQueue.remainder();
        assertEquals(expected, actual);
    }
}