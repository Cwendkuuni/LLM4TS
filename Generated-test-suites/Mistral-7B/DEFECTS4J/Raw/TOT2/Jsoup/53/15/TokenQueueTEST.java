import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testIsNotEmpty() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals('h', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekEmpty() {
        TokenQueue queue = new TokenQueue("");
        queue.peek();
    }

    @Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.addFirst("Test");
        Assert.assertEquals("Testhello world", queue.toString());
    }

    @Test
    public void testAddFirstCharacter() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.addFirst('T');
        Assert.assertEquals("Thello world", queue.toString());
    }

    @Test
    public void testMatchSimple() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matches("ll"));
    }

    @Test
    public void testMatchEmpty() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertFalse(queue.matches("XX"));
    }

    @Test
    public void testMatchCS() {
        TokenQueue queue = new TokenQueue("Hello World");
        Assert.assertTrue(queue.matchesCS("HeLLo"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matchesAny("ll", "wo"));
    }

    @Test
    public void testMatchesAnyEmpty() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertFalse(queue.matchesAny("XX", "OO"));
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("hello <world>");
        Assert.assertTrue(queue.matchChomp("<world>"));
        Assert.assertEquals("hello ", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchChompEmpty() {
        TokenQueue queue = new TokenQueue("hello");
        queue.matchChomp("<world>");
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" hello world ");
        Assert.assertTrue(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertTrue(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.advance();
        Assert.assertEquals('h', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testAdvanceBeyondEnd() {
        TokenQueue queue = new TokenQueue("hello world");
        for (int i = 0; i < queue.remainingLength(); i++) {
            queue.advance();
        }
        queue.advance();
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals('h', queue.consume());
        Assert.assertEquals("ello world", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmpty() {
        TokenQueue queue = new TokenQueue("");
        queue.consume();
    }

    @Test
    public void testConsumeString() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals("hello", queue.consume( "he" ));
        Assert.assertEquals("ld world", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeStringMismatch() {
        TokenQueue queue = new TokenQueue("hello world");
        queue.consume("ll");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeStringQueueEmpty() {
        TokenQueue queue = new TokenQueue("");
        queue.consume("he");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("hello <world>");
        Assert.assertEquals("hello ", queue.consumeTo("he"));
        Assert.assertEquals("<world>", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("Hello <world>");
        Assert.assertEquals("Hello ", queue.consumeToIgnoreCase("Hel"));
        Assert.assertEquals("<world>", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("hello <world> <foo> <bar>");
        Assert.assertEquals("hello <world>", queue.consumeToAny("He", "wo"));
        Assert.assertEquals(" <foo> <bar>", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(hello (world) (foo bar))");
        Assert.assertEquals("hello (world) (foo bar)", queue.chompBalanced('(', ')').toString());
    }

    @Test
    public void testUnescape() {
        TokenQueue queue = new TokenQueue("\\(hello\\ world\\)");
        String unescaped = queue.unescape(queue.toString());
        Assert.assertEquals("(hello world)", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" hello world ");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("helloworld", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals("hello", queue.consumeWord());
        Assert.assertEquals("world", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body></body></html>");
        Assert.assertEquals("html", queue.consumeTagName());
        Assert.assertEquals("<head><title>Test</title></head><body>", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.test:hover");
        Assert.assertEquals("div", queue.consumeElementSelector());
        Assert.assertEquals(".test:hover", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color: red; background: #f00;");
        Assert.assertEquals("color", queue.consumeCssIdentifier());
        Assert.assertEquals(": red;\nbgbackground: #f00;", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"test\" id=\"id1\"");
        Assert.assertEquals("class", queue.consumeAttributeKey());
        Assert.assertEquals("=\"test\" id=\"id1\"", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("hello world");
        Assert.assertEquals("world", queue.remainder());
        Assert.assertEquals("", queue.toString());
    }
}