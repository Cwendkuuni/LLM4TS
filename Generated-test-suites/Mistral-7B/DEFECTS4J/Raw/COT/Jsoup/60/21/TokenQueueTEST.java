import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        Assert.assertTrue(tokenQueue.isEmpty());

        tokenQueue = new TokenQueue("test");
        Assert.assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertEquals('t', tokenQueue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.peek();
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.addFirst('X');
        Assert.assertEquals('X', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertTrue(tokenQueue.matches("est"));
        Assert.assertFalse(tokenQueue.matches("tes"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("Test");
        Assert.assertTrue(tokenQueue.matchesCS("Test"));
        Assert.assertFalse(tokenQueue.matchesCS("test"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertTrue(tokenQueue.matchesAny("est", "es"));
        Assert.assertFalse(tokenQueue.matchesAny("tes"));

        tokenQueue = new TokenQueue("abcd1234");
        Assert.assertTrue(tokenQueue.matchesAny('a', '1'));
        Assert.assertFalse(tokenQueue.matchesAny('z'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<test>");
        Assert.assertTrue(tokenQueue.matchesStartTag());
        Assert.assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("test<test>");
        Assert.assertTrue(tokenQueue.matchChomp("test"));
        Assert.assertEquals(0, tokenQueue.peek());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("test ");
        Assert.assertTrue(tokenQueue.matchesWhitespace());
        Assert.assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("testWord");
        Assert.assertTrue(tokenQueue.matchesWord());
        Assert.assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.advance();
        Assert.assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertEquals('t', tokenQueue.consume());
        Assert.assertEquals('e', tokenQueue.consume());
        Assert.assertEquals('s', tokenQueue.consume());
        Assert.assertEquals(0, tokenQueue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.consume();
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("test");
        String consumed = tokenQueue.consume("est");
        Assert.assertEquals("est", consumed);
        Assert.assertEquals(0, tokenQueue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptySequence() {
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.consume("");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeLongerSequence() {
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.consume("testest");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("test<test>");
        String consumed = tokenQueue.consumeTo("test");
        Assert.assertEquals("test<", consumed);
        Assert.assertEquals('t', tokenQueue.peek());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("Test<test>");
        String consumed = tokenQueue.consumeToIgnoreCase("test");
        Assert.assertEquals("test<", consumed);
        Assert.assertEquals('t', tokenQueue.peek());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("test<test>");
        String consumed = tokenQueue.consumeToAny("est", "tes");
        Assert.assertEquals("test<", consumed);
        Assert.assertEquals('t', tokenQueue.peek());

        consumed = tokenQueue.consumeToAny("tes", "est");
        Assert.assertEquals("test", consumed);
        Assert.assertEquals(0, tokenQueue.peek());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(test)");
        String consumed = tokenQueue.chompBalanced('(', ')');
        Assert.assertEquals("test", consumed);
        Assert.assertEquals(0, tokenQueue.peek());

        tokenQueue = new TokenQueue("(test(test)test)");
        consumed = tokenQueue.chompBalanced('(', ')');
        Assert.assertEquals("test(test", consumed);
        Assert.assertEquals('t', tokenQueue.peek());
    }

    @Test
    public void testUnescape() {
        String in = "\\(test\\)";
        String out = TokenQueue.unescape(in);
        Assert.assertEquals("(test)", out);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" test ");
        boolean seen = tokenQueue.consumeWhitespace();
        Assert.assertTrue(seen);
        Assert.assertEquals('t', tokenQueue.peek());

        seen = tokenQueue.consumeWhitespace();
        Assert.assertFalse(seen);
        Assert.assertEquals(0, tokenQueue.peek());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("testWord");
        String consumed = tokenQueue.consumeWord();
        Assert.assertEquals("test", consumed);
        Assert.assertEquals('W', tokenQueue.peek());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("<testTagName>");
        String consumed = tokenQueue.consumeTagName();
        Assert.assertEquals("testTagName", consumed);
        Assert.assertEquals(0, tokenQueue.peek());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("div.testClass:hover");
        String consumed = tokenQueue.consumeElementSelector();
        Assert.assertEquals("div.testClass", consumed);
        Assert.assertEquals(":", tokenQueue.peek());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("class:testId");
        String consumed = tokenQueue.consumeCssIdentifier();
        Assert.assertEquals("testId", consumed);
        Assert.assertEquals(':', tokenQueue.peek());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("class='testAttribute'");
        String consumed = tokenQueue.consumeAttributeKey();
        Assert.assertEquals("class", consumed);
        Assert.assertEquals('=', tokenQueue.peek());
    }

    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("test remainingString");
        String remainder = tokenQueue.remainder();
        Assert.assertEquals(" remainingString", remainder);
        Assert.assertEquals(0, tokenQueue.peek());
    }

    @Test
    public void testToString() {
        TokenQueue tokenQueue = new TokenQueue("test");
        Assert.assertEquals("test", tokenQueue.toString());
    }
}