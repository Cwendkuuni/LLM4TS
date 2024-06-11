import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("test string");
    }

    @After
    public void tearDown() {
        tokenQueue = null;
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals('t', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('a');
        assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("test"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("test"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny("test", "string"));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("test"));
        assertEquals(" string", tokenQueue.toString());
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
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        assertEquals('t', tokenQueue.consume());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("test ", tokenQueue.consumeTo(" "));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("test ", tokenQueue.consumeToIgnoreCase(" "));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("test", tokenQueue.consumeToAny(" ", "s"));
    }

    @Test
    public void testChompTo() {
        assertEquals("test", tokenQueue.chompTo(" "));
    }

    @Test
    public void testChompToIgnoreCase() {
        assertEquals("test", tokenQueue.chompToIgnoreCase(" "));
    }

    @Test
    public void testChompBalanced() {
        assertEquals("(one (two) three)", tokenQueue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        assertEquals("\\n", TokenQueue.unescape("\\\\n"));
    }

    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        assertEquals("test", tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("test", tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        assertEquals("test", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("test", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        assertEquals("test", tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        assertEquals("test string", tokenQueue.remainder());
        assertTrue(tokenQueue.isEmpty());
    }

}