import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        String data = "test data";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertEquals("test data", queue.queue);
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());

        queue = new TokenQueue("test data");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("test data");
        Assert.assertEquals('t', queue.peek());
        queue.advance();
        Assert.assertEquals('e', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("test data");
        queue.addFirst('X');
        Assert.assertEquals('X', queue.queue.charAt(0));
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("test data");
        Assert.assertTrue(queue.matches("test"));
        Assert.assertFalse(queue.matches("data"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Test Data");
        Assert.assertTrue(queue.matchesCS("Test"));
        Assert.assertFalse(queue.matchesCS("Data"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("test data");
        Assert.assertTrue(queue.matchesAny("est", "data"));
        Assert.assertFalse(queue.matchesAny("tes"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("test data");
        Assert.assertTrue(queue.matchesAny(new char[]{'e', 'd'}));
        Assert.assertFalse(queue.matchesAny(new char[]{'z'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body>");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag());
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchChompWithNoMatch() {
        TokenQueue queue = new TokenQueue("test data");
        queue.matchChomp("Test");
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" test data ");
        Assert.assertTrue(queue.matchesWhitespace());
        queue.advance();
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("test word data");
        Assert.assertTrue(queue.matchesWord());
        queue.advance();
        Assert.assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("test data");
        queue.advance();
        Assert.assertEquals('e', queue.queue.charAt(0));
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("test data");
        Assert.assertEquals('t', queue.consume());
        Assert.assertEquals('e', queue.consume());
        Assert.assertEquals('', queue.consume());
    }

    @Test
    public void testConsumeString() {
        TokenQueue queue = new TokenQueue("test data");
        Assert.assertEquals("test", queue.consume(3));
        Assert.assertEquals("data", queue.consume(4));
        Assert.assertEquals("", queue.consume(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithNoMatch() {
        TokenQueue queue = new TokenQueue("test data");
        queue.consume("Test");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeToNoMatch() {
        TokenQueue queue = new TokenQueue("test data");
        queue.consumeTo("Test");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("test test data");
        String consumed = queue.consumeTo("test");
        Assert.assertEquals("test", consumed);
        Assert.assertEquals(" data", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("Test Test data");
        String consumed = queue.consumeToIgnoreCase("test");
        Assert.assertEquals("Test", consumed);
        Assert.assertEquals(" Test data", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("test data");
        String consumed = queue.consumeToAny("est", "data");
        Assert.assertEquals("test", consumed);
        Assert.assertEquals(" data", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(test)(data)");
        String consumed = queue.chompBalanced('(', ')');
        Assert.assertEquals("test(data)", queue.toString());
        Assert.assertEquals("test", consumed);
    }

    @Test
    public void testUnescape() {
        TokenQueue queue = new TokenQueue("\\(test\\)(\\data\\)");
        String consumed = queue.chompBalanced('(', ')').toString();
        Assert.assertEquals("test(data)", queue.toString());
        Assert.assertEquals("\\(test\\)(\\data\\)", consumed);
        Assert.assertEquals("test(data)", TokenQueue.unescape(consumed));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" test data ");
        boolean consumed = queue.consumeWhitespace();
        Assert.assertTrue(consumed);
        Assert.assertEquals("test data", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("test word data");
        String consumed = queue.consumeWord();
        Assert.assertEquals("test", consumed);
        Assert.assertEquals(" word data", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body>");
        String consumed = queue.consumeTagName();
        Assert.assertEquals("html", consumed);
        Assert.assertEquals("<head><title>Test</title>", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("html:div p .class1 #id1");
        String consumed = queue.consumeElementSelector();
        Assert.assertEquals("html", consumed);
        Assert.assertEquals("div p .class1 #id1", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class:class1 id:id1");
        String consumed = queue.consumeCssIdentifier();
        Assert.assertEquals("class1", consumed);
        Assert.assertEquals(" id:id1", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class:class1 style:background-color:red");
        String consumed = queue.consumeAttributeKey();
        Assert.assertEquals("class", consumed);
        Assert.assertEquals(" style:background-color:red", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("test data");
        String remained = queue.remainder();
        Assert.assertEquals(" data", remained);
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("test data");
        Assert.assertEquals(" data", queue.toString());
    }
}