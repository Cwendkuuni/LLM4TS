import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testTokenQueueConstructor() {
        String data = "exampleData";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals(data, tokenQueue.queue);
    }

    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.isEmpty());

        data = "testData";
        tokenQueue = new TokenQueue(data);
        Assert.assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals('t', tokenQueue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekEmptyQueue() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.peek();
    }

    @Test
    public void testAddFirstChar() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst('a');
        Assert.assertEquals("a", tokenQueue.queue.substring(0, 1));
    }

    @Test
    public void testAddFirstString() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst("abc");
        Assert.assertEquals("abc", tokenQueue.queue.substring(0, 3));
    }

    @Test
    public void testMatches() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "test";
        Assert.assertTrue(tokenQueue.matches(seq));
    }

    @Test
    public void testMatchesCS() {
        String data = "TestData";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "Test";
        Assert.assertTrue(tokenQueue.matchesCS(seq));
    }

    @Test
    public void testMatchesAny() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        String[] seq = {"t", "s"};
        Assert.assertTrue(tokenQueue.matchesAny(seq));

        TokenQueue tokenQueue2 = new TokenQueue("dataWithoutTS");
        Assert.assertFalse(tokenQueue2.matchesAny(seq));
    }

    @Test
    public void testMatchesAnyChars() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        char[] seq = {'t', 's'};
        Assert.assertTrue(tokenQueue.matchesAny(seq));

        TokenQueue tokenQueue2 = new TokenQueue("dataWithoutTS");
        Assert.assertFalse(tokenQueue2.matchesAny(seq));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<test>";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "test";
        Assert.assertTrue(tokenQueue.matchChomp(seq));
        Assert.assertEquals("Data", tokenQueue.queue);
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " testData ";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "testWord";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.advance();
        Assert.assertEquals('t', tokenQueue.queue.charAt(tokenQueue.pos));
    }

    @Test
    public void testConsume() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        char consumed = tokenQueue.consume();
        Assert.assertEquals('t', consumed);
        Assert.assertEquals("estData", tokenQueue.queue);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume();
    }

    @Test
    public void testConsumeString() {
        String data = "testData";
        String seq = "test";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume(seq);
        Assert.assertEquals("Data", tokenQueue.queue);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeStringMismatch() {
        String data = "testData";
        String seq = "Test";
        TokenQueue tokenQueue = new TokenQueue(data);        tokenQueue.consume(seq);
    }

    @Test
    public void testConsumeTo() {
        String data = "testData";
        String seq = "test";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeTo(seq);
        Assert.assertEquals("test", result);
        Assert.assertEquals("Data", tokenQueue.queue);
    }

    @Test
    public void testConsumeToEmptyQueue() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeTo("test");
        Assert.assertEquals("", result);
        Assert.assertEquals("", tokenQueue.queue);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "TestData";
        String seq = "test";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeToIgnoreCase(seq);
        Assert.assertEquals("Test", result);
        Assert.assertEquals("Data", tokenQueue.queue);
    }

    @Test
    public void testConsumeToAny() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        String[] seq = {"t", "s"};
        String result = tokenQueue.consumeToAny(seq);
        Assert.assertEquals("test", result);
        Assert.assertEquals("Data", tokenQueue.queue);
    }

    @Test
    public void testChompTo() {
        String data = "testData";
        String seq = "test";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.chompTo(seq);
        Assert.assertEquals("test", result);
        Assert.assertEquals("Data", tokenQueue.queue);
    }

    @Test
    public void testChompToIgnoreCase() {
        String data = "TestData";
        String seq = "test";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.chompToIgnoreCase(seq);
        Assert.assertEquals("Test", result);
        Assert.assertEquals("Data", tokenQueue.queue);
    }

    @Test
    public void testChompBalanced() {
        String data = "(test)";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.chompBalanced('(', ')');
        Assert.assertEquals("test", result);
        Assert.assertEquals(")", tokenQueue.queue);
    }

    @Test
    public void testUnescape() {
        String data = "\\test";
        String result = TokenQueue.unescape(data);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = " testData ";
        TokenQueue tokenQueue = new TokenQueue(data);
        boolean result = tokenQueue.consumeWhitespace();
        Assert.assertTrue(result);
        Assert.assertEquals("testData", tokenQueue.queue);
    }

    @Test
    public void testConsumeWord() {
        String data = "testWord";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeWord();
        Assert.assertEquals("testWord", result);
        Assert.assertEquals("", tokenQueue.queue);
    }

    @Test
    public void testConsumeTagName() {
        String data = "<testTagName>";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeTagName();
        Assert.assertEquals("testTagName", result);
        Assert.assertEquals("", tokenQueue.queue);
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "testTagName|class";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeElementSelector();
        Assert.assertEquals("testTagName", result);
        Assert.assertEquals("|class", tokenQueue.queue);
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "#testIdentifier";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeCssIdentifier();
        Assert.assertEquals("testIdentifier", result);
        Assert.assertEquals("", tokenQueue.queue);
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "data data-test-attribute";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.consumeAttributeKey();
        Assert.assertEquals("data", result);
        Assert.assertEquals(" data-test-attribute", tokenQueue.queue);
    }

    @Test
    public void testRemainder() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        String result = tokenQueue.remainder();
        Assert.assertEquals("testData", result);
        Assert.assertEquals("", tokenQueue.queue);
    }
}