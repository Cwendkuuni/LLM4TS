import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenQueueTest {
    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("abc DEF 123");
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        Assert.assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('x');
        Assert.assertEquals('x', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(tokenQueue.matches("abc"));
        Assert.assertFalse(tokenQueue.matches("def"));
    }

    @Test
    public void testMatchesCS() {
        Assert.assertTrue(tokenQueue.matchesCS("abc"));
        Assert.assertFalse(tokenQueue.matchesCS("ABC"));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertTrue(tokenQueue.matchesAny("abc", "def"));
        Assert.assertFalse(tokenQueue.matchesAny("DEF", "123"));
    }

    @Test
    public void testMatchesStartTag() {
        Assert.assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.addFirst("<");
        Assert.assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        Assert.assertTrue(tokenQueue.matchChomp("abc"));
        Assert.assertFalse(tokenQueue.matchChomp("def"));
    }

    @Test
    public void testMatchesWhitespace() {
        Assert.assertFalse(tokenQueue.matchesWhitespace());
        tokenQueue.consume();
        Assert.assertTrue(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        Assert.assertTrue(tokenQueue.matchesWord());
        tokenQueue.consume();
        Assert.assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        tokenQueue.advance();
        Assert.assertEquals('b', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        Assert.assertEquals('a', tokenQueue.consume());
        Assert.assertEquals('b', tokenQueue.peek());
    }

    @Test
    public void testConsumeString() {
        tokenQueue.consume("ab");
        Assert.assertEquals('c', tokenQueue.peek());
    }

    @Test
    public void testConsumeTo() {
        Assert.assertEquals("abc", tokenQueue.consumeTo(" "));
        Assert.assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        Assert.assertEquals("abc", tokenQueue.consumeToIgnoreCase(" "));
        Assert.assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeToAny() {
        Assert.assertEquals("abc", tokenQueue.consumeToAny("D", "E", "F"));
        Assert.assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testChompTo() {
        Assert.assertEquals("abc", tokenQueue.chompTo(" "));
        Assert.assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testChompToIgnoreCase() {
        Assert.assertEquals("abc", tokenQueue.chompToIgnoreCase(" "));
        Assert.assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testChompBalanced() {
        Assert.assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
        Assert.assertEquals(" four", tokenQueue.remainder());
    }

    @Test
    public void testUnescape() {
        Assert.assertEquals("foo\\bar", TokenQueue.unescape("foo\\\\bar"));
        Assert.assertEquals("foo\\bar\\", TokenQueue.unescape("foo\\\\bar\\"));
    }

    @Test
    public void testConsumeWhitespace() {
        Assert.assertTrue(tokenQueue.consumeWhitespace());
        Assert.assertFalse(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        Assert.assertEquals("abc", tokenQueue.consumeWord());
        Assert.assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeTagName() {
        tokenQueue = new TokenQueue("tag-name");
        Assert.assertEquals("tag", tokenQueue.consumeTagName());
        Assert.assertEquals('-', tokenQueue.peek());
    }

    @Test
    public void testConsumeElementSelector() {
        tokenQueue = new TokenQueue("element-selector");
        Assert.assertEquals("element", tokenQueue.consumeElementSelector());
        Assert.assertEquals('-', tokenQueue.peek());
    }

    @Test
    public void testConsumeCssIdentifier() {
        Assert.assertEquals("abc", tokenQueue.consumeCssIdentifier());
        Assert.assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeAttributeKey() {
        tokenQueue = new TokenQueue("attr-key");
        Assert.assertEquals("attr", tokenQueue.consumeAttributeKey());
        Assert.assertEquals('-', tokenQueue.peek());
    }

    @Test
    public void testRemainder() {
        Assert.assertEquals("abc DEF 123", tokenQueue.remainder());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("abc DEF 123", tokenQueue.toString());
    }
}