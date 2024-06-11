import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        Assert.assertTrue(tokenQueue.isEmpty());

        tokenQueue.addFirst("test");
        Assert.assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testAddFirstString() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.addFirst("test");
        Assert.assertEquals("test", tokenQueue.peek());
        Assert.assertEquals("test", tokenQueue.consume().toString());
    }

    @Test
    public void testAddFirstCharacter() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.addFirst('t');
        Assert.assertEquals('t', tokenQueue.peek());
        Assert.assertEquals('t', tokenQueue.consume());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullData() {
        new TokenQueue(null);
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("testtesttest");
        Assert.assertTrue(tokenQueue.matches("test"));
        Assert.assertFalse(tokenQueue.matches("tes"));
        Assert.assertFalse(tokenQueue.matches("testtes"));
        Assert.assertFalse(tokenQueue.matches("testtesttesttest"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("TestTestTest");
        Assert.assertTrue(tokenQueue.matchesCS("Test"));
        Assert.assertFalse(tokenQueue.matchesCS("tes"));
        Assert.assertFalse(tokenQueue.matchesCS("TestTest"));
        Assert.assertFalse(tokenQueue.matchesCS("TestTestTest"));
    }

    @Test
    public void testMatchesAnyString() {
        TokenQueue tokenQueue = new TokenQueue("testtesttesttestesttest");
        Assert.assertTrue(tokenQueue.matchesAny("test", "tes"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue tokenQueue = new TokenQueue("testtesttesttestesttest");
        Assert.assertTrue(tokenQueue.matchesAny('t', 's'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<test>test</test>");
        Assert.assertTrue(tokenQueue.matchesStartTag());
        Assert.assertFalse(tokenQueue.matchesStartTag()); // consume first char
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchChompNullSequence() {
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.matchChomp(null);
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("test<test>test");
        Assert.assertTrue(tokenQueue.matchChomp("<test>"));
        Assert.assertEquals("test", tokenQueue.peek());
        Assert.assertEquals("test", tokenQueue.consume().toString());
    }

    @Test
    public void testMatchChompEmptyQueue() {
        TokenQueue tokenQueue = new TokenQueue("");
        Assert.assertFalse(tokenQueue.matchChomp("test"));
        Assert.assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   test   ");
        Assert.assertTrue(tokenQueue.matchesWhitespace());
        Assert.assertFalse(tokenQueue.matchesWord());
        tokenQueue.advance();
        Assert.assertFalse(tokenQueue.matchesWhitespace());
        Assert.assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("testtesttesttest");
        Assert.assertTrue(tokenQueue.matchesWord());
        Assert.assertFalse(tokenQueue.matchesWhitespace());
        tokenQueue.advance();
        Assert.assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance();
        Assert.assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance();
        Assert.assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance();
        Assert.assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tokenQueue = new TokenQueue("testtesttesttest");
        Assert.assertEquals('t', tokenQueue.consume());
        Assert.assertTrue(tokenQueue.matchesWord());
        Assert.assertEquals('e', tokenQueue.consume());
        Assert.assertEquals('s', tokenQueue.consume());
        Assert.assertEquals('t', tokenQueue.consume());
        Assert.assertEquals(' ', tokenQueue.consume());
        Assert.assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testConsumeString() {
        TokenQueue tokenQueue = new TokenQueue("testtesttesttest");
        Assert.assertEquals("test", tokenQueue.consumeTo("test"));
        Assert.assertEquals("testtest", tokenQueue.consumeTo("test"));
        Assert.assertEquals("testtesttest", tokenQueue.consumeTo("test"));
        Assert.assertEquals("testtesttesttest", tokenQueue.consumeTo("test"));
        Assert.assertEquals("testtesttesttest", tokenQueue.consumeTo("test"));
        Assert.assertEquals("testtesttesttest", tokenQueue.consumeTo("test"));
        Assert.assertEquals("", tokenQueue.consumeTo("test"));
    }

    @Test
    public void testConsumeStringIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("TestTestTestTest");
        Assert.assertEquals("Test", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTestTestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTestTestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTestTestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTestTestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("", tokenQueue.consumeToIgnoreCase("Test"));
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("testtesttesttesttesttest");
        Assert.assertEquals("test", tokenQueue.consumeToAny("test", "tes"));
        Assert.assertEquals("testtest", tokenQueue.consumeToAny("test", "tes"));
        Assert.assertEquals("testtesttest", tokenQueue.consumeToAny("test", "tes"));
        Assert.assertEquals("testtesttesttesttesttest", tokenQueue.consumeToAny("test", "tes"));
        Assert.assertEquals("testtesttesttesttesttest", tokenQueue.consumeToAny("test", "tes"));
        Assert.assertEquals("testtesttesttesttesttest", tokenQueue.consumeToAny("test", "tes"));
        Assert.assertEquals("testtesttesttesttesttest", tokenQueue.consumeToAny("test", "tes"));
        Assert.assertEquals("", tokenQueue.consumeToAny("test", "tes"));
        Assert.assertEquals("test", tokenQueue.consumeToAny("test", "tes", "tester"));
        Assert.assertEquals("testtest", tokenQueue.consumeToAny("test", "tes", "tester"));
        Assert.assertEquals("testtesttest", tokenQueue.consumeToAny("test", "tes", "tester"));
        Assert.assertEquals("testtesttesttesttesttest", tokenQueue.consumeToAny("test", "tes", "tester"));
        Assert.assertEquals("testtesttesttesttesttest", tokenQueue.consumeToAny("test", "tes", "tester"));
        Assert.assertEquals("testtesttesttesttesttest", tokenQueue.consumeToAny("test", "tes", "tester"));
        Assert.assertEquals("testtesttesttesttesttest", tokenQueue.consumeToAny("test", "tes", "tester"));
        Assert.assertEquals("", tokenQueue.consumeToAny("test", "tes", "tester"));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("TestTestTestTestTestTest");
        Assert.assertEquals("Test", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTestTestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTestTestTestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTestTestTestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTestTestTestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTestTestTestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("Test", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTestTestTestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTestTestTestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTestTestTestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("TestTestTestTestTest", tokenQueue.consumeToIgnoreCase("Test"));
        Assert.assertEquals("", tokenQueue.consumeToIgnoreCase("Test"));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(test)(test)(test)");
        Assert.assertEquals("test", tokenQueue.chompBalanced('(', ')').toString());
        Assert.assertEquals("test(test)", tokenQueue.toString());

        tokenQueue.advance();
        Assert.assertEquals("test(test)", tokenQueue.toString());
        Assert.assertEquals("test", tokenQueue.consume().toString());

        tokenQueue.advance();
        Assert.assertEquals("test", tokenQueue.toString());
        Assert.assertEquals("test", tokenQueue.consume().toString());
    }

    @Test
    public void testUnescape() {
        String in = "\\\\test\\\\\\\\test\\\\\\\\test\\\\\\\\test\\\\";
        String out = TokenQueue.unescape(in);
        Assert.assertEquals("testtesttesttesttest", out);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   test   ");
        Assert.assertTrue(tokenQueue.consumeWhitespace());
        Assert.assertEquals("test", tokenQueue.consume().toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("testtesttesttesttesttest");
        Assert.assertEquals("test", tokenQueue.consumeWord());
        Assert.assertEquals("testtest", tokenQueue.consumeWord());
        Assert.assertEquals("testtesttest", tokenQueue.consumeWord());
        Assert.assertEquals("testtesttesttest", tokenQueue.consumeWord());
        Assert.assertEquals("testtesttesttesttest", tokenQueue.consumeWord());
        Assert.assertEquals("testtesttesttesttesttest", tokenQueue.consumeWord());
        Assert.assertEquals("", tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("<test><test><test><test></test></test></test>");
        Assert.assertEquals("test", tokenQueue.consumeTagName());
        Assert.assertEquals("test", tokenQueue.consumeTagName());
        Assert.assertEquals("test", tokenQueue.consumeTagName());
        Assert.assertEquals("test", tokenQueue.consumeTagName());
        Assert.assertEquals("", tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("test test:test test.test test-test test|test test.test:test");
        Assert.assertEquals("test", tokenQueue.consumeElementSelector());
        Assert.assertEquals("test", tokenQueue.consumeElementSelector());
        Assert.assertEquals("test.test", tokenQueue.consumeElementSelector());
        Assert.assertEquals("test-test", tokenQueue.consumeElementSelector());
        Assert.assertEquals("test|test", tokenQueue.consumeElementSelector());
        Assert.assertEquals("test.test:test", tokenQueue.consumeElementSelector());
        Assert.assertEquals("", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("test test-test test_test test-test_test test--test test--test--");
        Assert.assertEquals("test", tokenQueue.consumeCssIdentifier());
        Assert.assertEquals("test-test", tokenQueue.consumeCssIdentifier());
        Assert.assertEquals("test_test", tokenQueue.consumeCssIdentifier());
        Assert.assertEquals("test-test_test", tokenQueue.consumeCssIdentifier());
        Assert.assertEquals("test--test", tokenQueue.consumeCssIdentifier());
        Assert.assertEquals("test--test--", tokenQueue.consumeCssIdentifier());
        Assert.assertEquals("", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("class='test' id='test' class='test-test' id='test-test'");
        Assert.assertEquals("class", tokenQueue.consumeAttributeKey());
        Assert.assertEquals("test", tokenQueue.consumeAttributeKey());
        Assert.assertEquals("id", tokenQueue.consumeAttributeKey());
        Assert.assertEquals("test-test", tokenQueue.consumeAttributeKey());
        Assert.assertEquals("", tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("testtesttesttesttesttest");
        Assert.assertEquals("testtesttesttesttesttest", tokenQueue.toString());
        Assert.assertEquals("testtesttesttesttest", tokenQueue.remainder().toString());
    }

    @Test
    public void testToString() {
        TokenQueue tokenQueue = new TokenQueue("testtesttesttesttesttest");
        Assert.assertEquals("testtesttesttesttesttest", tokenQueue.toString());
        Assert.assertEquals("testtesttesttesttest", tokenQueue.consumeToIgnoreCase("test").toString());
        Assert.assertEquals("testtesttesttesttesttest", tokenQueue.toString());
    }
}