import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        TokenQueue tokenQueue = new TokenQueue("test string");
        Assert.assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("test string");
        tokenQueue.consume('t');
        tokenQueue.consume('e');
        tokenQueue.consume('s');
        tokenQueue.consume('t');
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("test string");
        Assert.assertEquals('t', tokenQueue.peek());
        tokenQueue.consume('t');
        Assert.assertEquals('e', tokenQueue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddFirstWithNullSeq() {
        TokenQueue tokenQueue = new TokenQueue("test string");
        tokenQueue.addFirst(null);
    }

    @Test
    public void testAddFirstString() {
        TokenQueue tokenQueue = new TokenQueue("test string");
        tokenQueue.addFirst("abc");
        Assert.assertEquals("abctest string", tokenQueue.toString());
    }

    @Test
    public void testAddFirstCharacter() {
        TokenQueue tokenQueue = new TokenQueue("test string");
        tokenQueue.addFirst('a');
        Assert.assertEquals("atest string", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("test string");
        Assert.assertTrue(tokenQueue.matches("est"));
        tokenQueue.consume('t');
        Assert.assertTrue(tokenQueue.matches("est"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("Test String");
        Assert.assertTrue(tokenQueue.matchesCS("Test"));
        tokenQueue.consume('T');
        Assert.assertFalse(tokenQueue.matchesCS("Test"));
    }

    @Test
    public void testMatchesAnyString() {
        TokenQueue tokenQueue = new TokenQueue("test string");
        Assert.assertTrue(tokenQueue.matchesAny("est", "tes"));
        tokenQueue.consume('t');
        Assert.assertTrue(tokenQueue.matchesAny("es", "tes"));
    }

    @Test
    public void testMatchesAnyCharacter() {
        TokenQueue tokenQueue = new TokenQueue("test string");
        Assert.assertTrue(tokenQueue.matchesAny('s', 't'));
        tokenQueue.consume('t');
        Assert.assertTrue(tokenQueue.matchesAny('s', 'e'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<test>");
        Assert.assertTrue(tokenQueue.matchesStartTag());
        tokenQueue.consume('<');
        Assert.assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("test string<test>");
        Assert.assertTrue(tokenQueue.matchChomp("test"));
        Assert.assertEquals("test<", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" test string");
        Assert.assertTrue(tokenQueue.matchesWhitespace());
        tokenQueue.consume(' ');
        Assert.assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("testWord");
        Assert.assertTrue(tokenQueue.matchesWord());
        tokenQueue.consume('t');
        Assert.assertTrue(tokenQueue.matchesWord());
    }

    @Test(expected = IllegalStateException.class)
    public void testAdvanceWhenEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.advance();
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("test string");
        Assert.assertEquals('t', tokenQueue.consume());
        Assert.assertEquals('e', tokenQueue.consume());
        Assert.assertEquals('s', tokenQueue.consume());
        Assert.assertEquals('t', tokenQueue.consume());
        Assert.assertEquals(' ', tokenQueue.consume());
        Assert.assertTrue(tokenQueue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithEmptyQueue() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.consume("test");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithMismatchedSequence() {
        TokenQueue tokenQueue = new TokenQueue("test<test>");
        tokenQueue.consume("test");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("test <test>");
        String consumed = tokenQueue.consumeTo("test");
        Assert.assertEquals("test", consumed);
        Assert.assertEquals(" <test>", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("Test tesT <test>");
        String consumed = tokenQueue.consumeToIgnoreCase("test");
        Assert.assertEquals("test", consumed);
        Assert.assertEquals(" Test tesT <", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("test string<test>");
        String consumed = tokenQueue.consumeToAny("est", "tes");
        Assert.assertEquals("est", consumed);
        Assert.assertEquals("test <", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(test (test) test) test");
        String chompBalanced = tokenQueue.chompBalanced('(', ')');
        Assert.assertEquals("(test (test) test)", chompBalanced);
        Assert.assertEquals(" test", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        String in = "\\(test\\)";
        String out = TokenQueue.unescape(in);
        Assert.assertEquals("(test)", out);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" test string");
        boolean seen = tokenQueue.consumeWhitespace();
        Assert.assertTrue(seen);
        Assert.assertEquals("est string", tokenQueue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("testWord");
        String consumed = tokenQueue.consumeWord();
        Assert.assertEquals("Word", consumed);
        Assert.assertEquals("test", tokenQueue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("testTagName");
        String consumed = tokenQueue.consumeTagName();
        Assert.assertEquals("TagName", consumed);
        Assert.assertEquals("test", tokenQueue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("testElementSelector");
        String consumed = tokenQueue.consumeElementSelector();
        Assert.assertEquals("ElementSelector", consumed);
        Assert.assertEquals("test", tokenQueue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("testCssIdentifier");
        String consumed = tokenQueue.consumeCssIdentifier();
        Assert.assertEquals("CssIdentifier", consumed);
        Assert.assertEquals("test", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("testAttributeKey");
        String consumed = tokenQueue.consumeAttributeKey();
        Assert.assertEquals("AttributeKey", consumed);
        Assert.assertEquals("test", tokenQueue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("test string");
        String remainder = tokenQueue.remainder();
        Assert.assertEquals(" string", remainder);
        Assert.assertEquals("test", tokenQueue.toString());
    }
}