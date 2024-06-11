import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals("Hello World", queue.toString());
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());

        queue = new TokenQueue("ABC");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("ABCDEF");
        Assert.assertEquals('A', queue.peek());
        queue.advance();
        Assert.assertEquals('B', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddFirstStringEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("ABC");
    }

    @Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("DEF");
        queue.addFirst("ABC");
        Assert.assertEquals("ABCDEF", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("ABCDEF");
        Assert.assertTrue(queue.matches("AB"));
        Assert.assertFalse(queue.matches("EFG"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCDEF");
        Assert.assertTrue(queue.matchesCS("AB"));
        Assert.assertFalse(queue.matchesCS("efg"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("ABCDEF");
        Assert.assertTrue(queue.matchesAny("AB", "CD"));
        Assert.assertFalse(queue.matchesAny("GHI"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("ABCDEF");
        Assert.assertTrue(queue.matchesAny(new char[]{'A', 'B'}));
        Assert.assertFalse(queue.matchesAny(new char[]{'G', 'H'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
        Assert.assertTrue(queue.matchChomp("<html>"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" ABC   ");
        Assert.assertTrue(queue.matchesWhitespace());
        queue.consumeWhitespace();
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("ABC ABC ABC");
        Assert.assertTrue(queue.matchesWord());
        queue.advance();
        Assert.assertTrue(queue.matchesWord());
        queue.advance();
        Assert.assertTrue(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("ABC ABC ABC");
        queue.advance();
        queue.advance();
        queue.advance();
        Assert.assertEquals("BC ABC", queue.toString());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("ABC ABC ABC");
        Assert.assertEquals('A', queue.consume());
        Assert.assertEquals("BC ABC", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume("ABC");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("ABC DEF GHI JKL");
        Assert.assertEquals("DEF", queue.consumeTo("DEF"));
        Assert.assertEquals("GHI JKL", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("ABC DEF GHI JKL");
        Assert.assertEquals("DEF", queue.consumeToIgnoreCase("def"));
        Assert.assertEquals("GHI JKL", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("ABCDEFGHIJKL");
        Assert.assertEquals("DEFGHI", queue.consumeToAny("ABC", "MNO"));
        Assert.assertEquals("JKL", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(())");
        Assert.assertEquals("()", queue.chompBalanced('(', ')'));
        Assert.assertEquals("", queue.toString());

        queue = new TokenQueue("(<html>(<head>(<title>Test</title>)</head>)</html>");
        Assert.assertEquals("<html>(<head>(<title>Test</title>)", queue.chompBalanced('<', '>'));
    }

    @Test
    public void testUnescape() {
        Assert.assertEquals("\\ab\\bc\\cd\\ef\\fg", TokenQueue.unescape("\\ab\\bc\\cd\\ef\\fg"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("ABC ABC ABC   ");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("ABC ABC", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("ABC ABC ABC");
        Assert.assertEquals("ABC", queue.consumeWord());
        Assert.assertEquals("AB ABC", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
        Assert.assertEquals("title", queue.consumeTagName());
        Assert.assertEquals("<html><head></head>", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class1.class2 div.class3 p#id1");
        Assert.assertEquals("div.class1.class2", queue.consumeElementSelector());
        Assert.assertEquals("div.class3 p#id1", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class1 class2 id1 id2 class3");
        Assert.assertEquals("class1", queue.consumeCssIdentifier());
        Assert.assertEquals("class2 id2 class3", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class='class1' id='id1' style='color:red'");
        Assert.assertEquals("class", queue.consumeAttributeKey());
        Assert.assertEquals("id='id1' style='color:red'", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("ABC DEF GHI JKL");
        Assert.assertEquals("JKL", queue.remainder());
        queue.consumeTo("DEF");
        Assert.assertEquals("GHI JKL", queue.toString());
        Assert.assertEquals("JKL", queue.remainder());
    }
}