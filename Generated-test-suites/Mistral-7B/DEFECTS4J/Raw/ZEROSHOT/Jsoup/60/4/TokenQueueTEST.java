import org.junit.Test;
import org.junit.Assert;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testTokenQueueCreation() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertFalse(tokenQueue.isEmpty());
        Assert.assertEquals(data, tokenQueue.toString());
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
        Assert.assertEquals(data.charAt(0), tokenQueue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekEmptyQueue() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.peek();
    }

    @Test
    public void testAddFirst() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst('A');
        Assert.assertEquals('A', tokenQueue.consume());
    }

    @Test
    public void testMatches() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matches("test"));
        Assert.assertFalse(tokenQueue.matches("Test"));
    }

    @Test
    public void testMatchesCS() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchesCS("test"));
        Assert.assertFalse(tokenQueue.matchesCS("Test"));
    }

    @Test
    public void testMatchesAny() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchesAny("test", "Test"));
        Assert.assertTrue(tokenQueue.matchesAny("test", "data"));
        Assert.assertFalse(tokenQueue.matchesAny("Test", "test"));
    }

    @Test
    public void testMatchesAnyChars() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchesAny('t', 'd'));
        Assert.assertTrue(tokenQueue.matchesAny('T', 'D'));
        Assert.assertFalse(tokenQueue.matchesAny('A', 't'));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<test>";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchesStartTag());

        data = "test";
        tokenQueue = new TokenQueue(data);
        Assert.assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        String data = "<test>test</test>";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchChomp("test"));
        Assert.assertEquals("</test>", tokenQueue.toString());

        data = "<test>test</test>";
        tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchChomp("Test")); // case insensitive
        Assert.assertEquals("</test>", tokenQueue.toString());

        data = "<test>test</test>";
        tokenQueue = new TokenQueue(data);
        Assert.assertFalse(tokenQueue.matchChomp("Test2"));
        Assert.assertEquals("<test>test</test>", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " test ";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchesWhitespace());
        Assert.assertTrue(tokenQueue.consumeWhitespace());
        Assert.assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "testWord";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.matchesWord());
        Assert.assertEquals("Word", tokenQueue.consumeWord());
        Assert.assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testConsume() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals('t', tokenQueue.consume());
        Assert.assertEquals('e', tokenQueue.consume());
        Assert.assertEquals('s', tokenQueue.consume());
        Assert.assertEquals('t', tokenQueue.consume());
        Assert.assertEquals('D', tokenQueue.consume());
        Assert.assertEquals('a', tokenQueue.consume());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume();
    }

    @Test
    public void testConsumeSequence() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("test", tokenQueue.consumeTo("test"));
        Assert.assertEquals("Data", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "Test Data";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("test", tokenQueue.consumeToIgnoreCase("test"));
        Assert.assertEquals("Data", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        String data = "testAnyData";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("test", tokenQueue.consumeToAny("test", "Test"));
        Assert.assertEquals("AnyData", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAnyChars() {
        String data = "testAnyData";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("test", tokenQueue.consumeToAny('t', 'd'));
        Assert.assertEquals("AnyData", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        String data = "((test)(test)))";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("test(test)", tokenQueue.chompBalanced('(', ')'));
        Assert.assertEquals("))", tokenQueue.toString());

        data = "((test)(test)))";
        tokenQueue = new TokenQueue(data);
        Assert.assertEquals("test(test)", tokenQueue.chompBalanced('(', '(')); // wrong closing bracket
        Assert.assertEquals("((test)(test)))", tokenQueue.toString());

        data = "((test)(test)))";
        tokenQueue = new TokenQueue(data);
        Assert.assertEquals("test(test)", tokenQueue.chompBalanced(')', '(')); // wrong opening bracket
        Assert.assertEquals("((test)(test)))", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        String data = "\\test\\";
        String unescaped = TokenQueue.unescape(data);
        Assert.assertEquals("test", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = " test  data  ";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertTrue(tokenQueue.consumeWhitespace());
        Assert.assertEquals("test", tokenQueue.consumeWord());
        Assert.assertTrue(tokenQueue.consumeWhitespace());
        Assert.assertEquals("data", tokenQueue.consumeWord());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeWord() {
        String data = "testWord";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("test", tokenQueue.consumeWord());
        Assert.assertEquals("Word", tokenQueue.consumeWord());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        String data = "<testTagName>";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("testTagName", tokenQueue.consumeTagName());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "testElement:name";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("testElement:name", tokenQueue.consumeElementSelector());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "testId testId2";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("testId", tokenQueue.consumeCssIdentifier());
        Assert.assertEquals("testId2", tokenQueue.consumeCssIdentifier());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "testName testClass testName2";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("testName", tokenQueue.consumeAttributeKey());
        Assert.assertEquals("testClass", tokenQueue.consumeAttributeKey());
        Assert.assertEquals("testName2", tokenQueue.consumeAttributeKey());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testRemainder() {
        String data = "testData";
        TokenQueue tokenQueue = new TokenQueue(data);
        Assert.assertEquals("Data", tokenQueue.remainder());
        Assert.assertTrue(tokenQueue.isEmpty());
    }
}