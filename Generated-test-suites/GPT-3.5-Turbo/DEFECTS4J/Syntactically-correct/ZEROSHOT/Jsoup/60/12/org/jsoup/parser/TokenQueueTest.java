import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenQueueTest {

    private TokenQueue queue;

    @Before
    public void setup() {
        queue = new TokenQueue("test");
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        Assert.assertEquals('t', queue.peek());
    }

    @Test
    public void testAddFirst() {
        queue.addFirst('s');
        Assert.assertEquals("stest", queue.toString());
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(queue.matches("test"));
    }

    @Test
    public void testMatchesCS() {
        Assert.assertTrue(queue.matchesCS("test"));
    }

    @Test
    public void testMatchesAnyString() {
        Assert.assertTrue(queue.matchesAny("test"));
    }

    @Test
    public void testMatchesAnyChars() {
        Assert.assertTrue(queue.matchesAny('t'));
    }

    @Test
    public void testMatchesStartTag() {
        Assert.assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        Assert.assertTrue(queue.matchChomp("test"));
    }

    @Test
    public void testMatchesWhitespace() {
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        Assert.assertTrue(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        queue.advance();
        Assert.assertEquals("est", queue.toString());
    }

    @Test
    public void testConsume() {
        Assert.assertEquals('t', queue.consume());
    }

    @Test
    public void testConsumeSeq() {
        queue.consume("te");
        Assert.assertEquals("st", queue.toString());
    }

    @Test
    public void testConsumeTo() {
        Assert.assertEquals("t", queue.consumeTo("e"));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        Assert.assertEquals("t", queue.consumeToIgnoreCase("E"));
    }

    @Test
    public void testConsumeToAny() {
        Assert.assertEquals("t", queue.consumeToAny("e"));
    }

    @Test
    public void testChompTo() {
        Assert.assertEquals("t", queue.chompTo("e"));
    }

    @Test
    public void testChompToIgnoreCase() {
        Assert.assertEquals("t", queue.chompToIgnoreCase("E"));
    }

    @Test
    public void testChompBalanced() {
        Assert.assertEquals("t", queue.chompBalanced('t', 's'));
    }

    @Test
    public void testUnescape() {
        Assert.assertEquals("test", TokenQueue.unescape("t\\test"));
    }

    @Test
    public void testConsumeWhitespace() {
        Assert.assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        Assert.assertEquals("test", queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        Assert.assertEquals("test", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        Assert.assertEquals("test", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        Assert.assertEquals("test", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        Assert.assertEquals("test", queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        Assert.assertEquals("test", queue.remainder());
    }
}