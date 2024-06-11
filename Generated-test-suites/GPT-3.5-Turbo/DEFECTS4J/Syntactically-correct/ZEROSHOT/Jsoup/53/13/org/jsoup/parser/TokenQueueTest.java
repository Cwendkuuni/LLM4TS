import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("example");
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('X');
        assertEquals("Xexample", tokenQueue.toString());

        tokenQueue.addFirst("Y");
        assertEquals("YXexample", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("example"));
        assertFalse(tokenQueue.matches("Example"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("example"));
        assertFalse(tokenQueue.matchesCS("Example"));
    }

    @Test
    public void testMatchesAnyStringArray() {
        assertTrue(tokenQueue.matchesAny("example", "Example"));
        assertFalse(tokenQueue.matchesAny("Example"));
    }

    @Test
    public void testMatchesAnyCharArray() {
        assertFalse(tokenQueue.matchesAny('A'));
        assertTrue(tokenQueue.matchesAny('A', 'e'));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
        TokenQueue tokenQueue1 = new TokenQueue("<example");
        assertTrue(tokenQueue1.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("exam"));
        assertEquals("ple", tokenQueue.toString());

        assertFalse(tokenQueue.matchChomp("Exam"));
        assertEquals("ple", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        assertFalse(tokenQueue.matchesWhitespace());
        TokenQueue tokenQueue1 = new TokenQueue(" ");
        assertTrue(tokenQueue1.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        assertTrue(tokenQueue.matchesWord());
        TokenQueue tokenQueue1 = new TokenQueue(" ");
        assertFalse(tokenQueue1.matchesWord());
    }

    @Test
    public void testAdvance() {
        tokenQueue.advance();
        assertEquals("xample", tokenQueue.toString());

        tokenQueue.advance();
        assertEquals("ample", tokenQueue.toString());
    }

    @Test
    public void testConsume() {
        assertEquals('e', tokenQueue.consume());
        assertEquals("xample", tokenQueue.toString());
    }

    @Test
    public void testConsumeString() {
        tokenQueue.consume("exam");
        assertEquals("ple", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("exam", tokenQueue.consumeTo("ple"));
        assertEquals("ple", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("exam", tokenQueue.consumeToIgnoreCase("PLE"));
        assertEquals("ple", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("ex", tokenQueue.consumeToAny("a", "p"));
        assertEquals("ample", tokenQueue.toString());
    }

    @Test
    public void testChompTo() {
        assertEquals("ex", tokenQueue.chompTo("am"));
        assertEquals("ple", tokenQueue.toString());
    }

    @Test
    public void testChompToIgnoreCase() {
        assertEquals("ex", tokenQueue.chompToIgnoreCase("AM"));
        assertEquals("ple", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue1 = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", tokenQueue1.chompBalanced('(', ')'));
        assertEquals(" four", tokenQueue1.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("\\", TokenQueue.unescape("\\\\"));
        assertEquals("'", TokenQueue.unescape("\\'"));
    }

    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
        TokenQueue tokenQueue1 = new TokenQueue("   ");
        assertTrue(tokenQueue1.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        assertEquals("example", tokenQueue.consumeWord());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("example", tokenQueue.consumeTagName());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        assertEquals("example", tokenQueue.consumeElementSelector());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("example", tokenQueue.consumeCssIdentifier());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeAttributeKey() {
        assertEquals("example", tokenQueue.consumeAttributeKey());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testRemainder() {
        assertEquals("example", tokenQueue.remainder());
        assertTrue(tokenQueue.isEmpty());
    }
}