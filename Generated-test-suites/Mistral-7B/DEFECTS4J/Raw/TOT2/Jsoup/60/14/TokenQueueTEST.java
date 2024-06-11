import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals("Hello World".charAt(0), queue.peek());
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
        queue.addFirst("Test");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abcdef");
        Assert.assertEquals('a', queue.peek());
        queue.advance();
        Assert.assertEquals('b', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("Hello");
        queue.addFirst("World");
        Assert.assertEquals("WorldHello", queue.toString());
        Assert.assertEquals('W', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertTrue(queue.matches("He"));
        Assert.assertTrue(queue.matches("llo"));
        Assert.assertFalse(queue.matches("ll"));
        queue.advance();
        Assert.assertFalse(queue.matches("He"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertTrue(queue.matchesCS("He"));
        Assert.assertFalse(queue.matchesCS("ll"));
        queue.advance();
        Assert.assertFalse(queue.matchesCS("He"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("Test123Test");
        Assert.assertTrue(queue.matchesAny("Test", "1"));
        Assert.assertTrue(queue.matchesAny("Test", "3"));
        Assert.assertFalse(queue.matchesAny("Test1"));
        queue.advance();
        Assert.assertFalse(queue.matchesAny("Test", "1"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("Test123Test");
        Assert.assertTrue(queue.matchesAny(new char[]{'1', '3'}));
        queue.advance();
        Assert.assertFalse(queue.matchesAny(new char[]{'1', '3'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
        Assert.assertTrue(queue.matchesStartTag());
        queue.advance();
        Assert.assertTrue(queue.matchesStartTag());
        queue.advance();
        Assert.assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
        Assert.assertTrue(queue.matchChomp("<title>"));
        Assert.assertEquals("Test</title>", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \tHello World");
        Assert.assertTrue(queue.matchesWhitespace());
        queue.advance();
        Assert.assertTrue(queue.matchesWhitespace());
        queue.advance();
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeInvalidSequence() {
        TokenQueue queue = new TokenQueue("TestTest");
        queue.consume("Test1");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("Hello World <html>");
        String consumed = queue.consumeTo("World");
        Assert.assertEquals("Hello World", queue.toString());
        Assert.assertEquals("World", consumed);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("Hello World <html>");
        String consumed = queue.consumeToIgnoreCase("world");
        Assert.assertEquals("Hello World <html>", queue.toString());
        Assert.assertEquals("World", consumed);
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("Test123Test");
        String consumed = queue.consumeToAny("1", "3");
        Assert.assertEquals("Test", consumed);
        Assert.assertEquals("Test2", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(())((()))");
        String consumed = queue.chompBalanced('(', ')');
        Assert.assertEquals("()()", queue.toString());
        Assert.assertEquals("(())", consumed);
    }

    @Test
    public void testUnescape() {
        String input = "\\<script\\>alert('XSS')\\;</script>";
        String output = TokenQueue.unescape(input);
        Assert.assertEquals("<script>alert('XSS')</script>", output);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("\t Hello World \n");
        boolean consumed = queue.consumeWhitespace();
        Assert.assertTrue(consumed);
        Assert.assertEquals("Hello World", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("Hello World");
        String consumed = queue.consumeWord();
        Assert.assertEquals("Hello", consumed);
        Assert.assertEquals("World", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head>");
        String consumed = queue.consumeTagName();
        Assert.assertEquals("html", consumed);
        Assert.assertEquals("<head><title>Test</title></head>", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class p#id span.class:hover");
        String consumed = queue.consumeElementSelector();
        Assert.assertEquals("div", consumed);
        Assert.assertEquals(".class p#id span.class:hover", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("id:example class:test");
        String consumed = queue.consumeCssIdentifier();
        Assert.assertEquals("id", consumed);
        Assert.assertEquals("example", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class='class1 class2' id='example'");
        String consumed = queue.consumeAttributeKey();
        Assert.assertEquals("class", consumed);
        Assert.assertEquals("'class1 class2'", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("Hello World");
        String remainder = queue.remainder();
        Assert.assertEquals("World", remainder);
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertEquals("World", queue.toString());
    }
}