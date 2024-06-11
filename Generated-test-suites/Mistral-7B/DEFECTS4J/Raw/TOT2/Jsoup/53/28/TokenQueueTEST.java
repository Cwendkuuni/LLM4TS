import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        TokenQueue tq = new TokenQueue("hello world");
        Assert.assertFalse(tq.isEmpty());
        Assert.assertEquals("hello world", tq.toString());
    }

    @Test
    public void testIsEmpty() {
        TokenQueue tq = new TokenQueue("");
        Assert.assertTrue(tq.isEmpty());

        tq = new TokenQueue("abc");
        Assert.assertFalse(tq.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tq = new TokenQueue("abcdef");
        Assert.assertEquals('a', tq.peek());
        tq.advance();
        Assert.assertEquals('b', tq.peek());

        tq = new TokenQueue("");
        Assert.assertEquals((char) 0, tq.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tq = new TokenQueue("abc");
        tq.addFirst('x');
        Assert.assertEquals("xabc", tq.toString());

        tq = new TokenQueue("123");
        tq.addFirst("456");
        Assert.assertEquals("456123", tq.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tq = new TokenQueue("hello world");
        Assert.assertTrue(tq.matches("hello"));
        Assert.assertTrue(tq.matches("world"));
        Assert.assertFalse(tq.matches("abc"));

        tq = new TokenQueue("");
        Assert.assertFalse(tq.matches(""));
        Assert.assertFalse(tq.matches("abc"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tq = new TokenQueue("Hello World");
        Assert.assertTrue(tq.matchesCS("Hello"));
        Assert.assertFalse(tq.matchesCS("world"));
        Assert.assertFalse(tq.matchesCS("Hello World"));

        tq = new TokenQueue("123");
        Assert.assertFalse(tq.matchesCS("123"));
        Assert.assertFalse(tq.matchesCS("456"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tq = new TokenQueue("hello world");
        Assert.assertTrue(tq.matchesAny("hello", "world"));
        Assert.assertFalse(tq.matchesAny("abc", "123"));

        tq = new TokenQueue("");
        Assert.assertFalse(tq.matchesAny("", ""));
        Assert.assertFalse(tq.matchesAny("abc", "123"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue tq = new TokenQueue("hello world");
        Assert.assertTrue(tq.matchesAny(new char[]{'h', 'e'}));
        Assert.assertTrue(tq.matchesAny(new char[]{'w', 'r'}));
        Assert.assertFalse(tq.matchesAny(new char[]{'a', 'b'}));

        tq = new TokenQueue("");
        Assert.assertFalse(tq.matchesAny(new char[]{}));
        Assert.assertFalse(tq.matchesAny(new char[]{'a', 'b'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tq = new TokenQueue("<html><head><title>Test</title></head><body>");
        Assert.assertTrue(tq.matchesStartTag());
        Assert.assertFalse(tq.matchesStartTag());

        tq = new TokenQueue("");
        Assert.assertFalse(tq.matchesStartTag());
        Assert.assertFalse(tq.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tq = new TokenQueue("hello world");
        Assert.assertTrue(tq.matchChomp("hello"));
        Assert.assertEquals("world", tq.toString());

        tq = new TokenQueue("hello world");
        Assert.assertFalse(tq.matchChomp("world"));
        Assert.assertEquals("hello world", tq.toString());

        tq = new TokenQueue("");
        Assert.assertFalse(tq.matchChomp(""));
        Assert.assertEquals("", tq.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tq = new TokenQueue(" hello world ");
        Assert.assertTrue(tq.matchesWhitespace());
        Assert.assertTrue(tq.matchesWhitespace());
        Assert.assertTrue(tq.matchesWhitespace());
        Assert.assertFalse(tq.matchesWhitespace());

        tq = new TokenQueue("hello world");
        Assert.assertFalse(tq.matchesWhitespace());
        Assert.assertFalse(tq.matchesWhitespace());
        Assert.assertFalse(tq.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tq = new TokenQueue("hello world");
        Assert.assertTrue(tq.matchesWord());
        Assert.assertTrue(tq.matchesWord());
        Assert.assertFalse(tq.matchesWord());

        tq = new TokenQueue("123");
        Assert.assertFalse(tq.matchesWord());
        Assert.assertFalse(tq.matchesWord());
        Assert.assertFalse(tq.matchesWord());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithEmptyQueue() {
        TokenQueue tq = new TokenQueue("");
        tq.consume("hello");
    }

    @Test
    public void testConsume() {
        TokenQueue tq = new TokenQueue("hello world");
        String consumed = tq.consume("hello");
        Assert.assertEquals("world", tq.toString());
        Assert.assertEquals("hello", consumed);

        consumed = tq.consume("world");
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("world", consumed);

        consumed = tq.consume("xyz");
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tq = new TokenQueue("hello world");
        String consumed = tq.consumeTo("hello");
        Assert.assertEquals("world", tq.toString());
        Assert.assertEquals("hello", consumed);

        consumed = tq.consumeTo("world");
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("world", consumed);

        consumed = tq.consumeTo("xyz");
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tq = new TokenQueue("Hello World");
        String consumed = tq.consumeToIgnoreCase("hello");
        Assert.assertEquals("World", tq.toString());
        Assert.assertEquals("hello", consumed);

        consumed = tq.consumeToIgnoreCase("world");
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("World", consumed);

        consumed = tq.consumeToIgnoreCase("XYZ");
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tq = new TokenQueue("hello world");
        String consumed = tq.consumeToAny("hello", "world");
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("hello world", consumed);

        consumed = tq.consumeToAny("abc", "xyz");
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testConsumeToAnyChar() {
        TokenQueue tq = new TokenQueue("hello world");
        String consumed = tq.consumeToAny(new char[]{'h', 'e'});
        Assert.assertEquals("world", tq.toString());
        Assert.assertEquals("hello world", consumed);

        consumed = tq.consumeToAny(new char[]{'a', 'b'});
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tq = new TokenQueue("(hello (world) (foo))");
        String consumed = tq.chompBalanced('(', ')');
        Assert.assertEquals("hello (world) ", tq.toString());
        Assert.assertEquals("(hello (world)", consumed);

        consumed = tq.chompBalanced('(', ')');
        Assert.assertEquals("hello world ", tq.toString());
        Assert.assertEquals("hello (world", consumed);

        consumed = tq.chompBalanced('(', ')');
        Assert.assertEquals("hello ", tq.toString());
        Assert.assertEquals("hello world", consumed);

        consumed = tq.chompBalanced('(', ')');
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", consumed);

        consumed = tq.chompBalanced('[', ']');
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", consumed);

        consumed = tq.chompBalanced('{', '}');
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", consumed);

        consumed = tq.chompBalanced('"', '"');
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", consumed);

        consumed = tq.chompBalanced('\\', '\\');
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testUnescape() {
        String escaped = "\\(hello\\ world\\)";
        String unescaped = TokenQueue.unescape(escaped);
        Assert.assertEquals("(hello world)", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tq = new TokenQueue(" hello world ");
        boolean consumed = tq.consumeWhitespace();
        Assert.assertEquals("hello world", tq.toString());
        Assert.assertTrue(consumed);

        consumed = tq.consumeWhitespace();
        Assert.assertEquals("hello world", tq.toString());
        Assert.assertTrue(consumed);

        consumed = tq.consumeWhitespace();
        Assert.assertEquals("hello world", tq.toString());
        Assert.assertFalse(consumed);

        consumed = tq.consumeWhitespace();
        Assert.assertEquals("hello world", tq.toString());
        Assert.assertFalse(consumed);
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tq = new TokenQueue("hello world");
        String consumed = tq.consumeWord();
        Assert.assertEquals("hello world", tq.toString());
        Assert.assertEquals("hello", consumed);

        consumed = tq.consumeWord();
        Assert.assertEquals(" world", tq.toString());
        Assert.assertEquals("world", consumed);

        consumed = tq.consumeWord();
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tq = new TokenQueue("<html><head><title>Test</title></head><body>");
        String consumed = tq.consumeTagName();
        Assert.assertEquals("<html><head><title>Test</title></head><body>", tq.toString());
        Assert.assertEquals("html", consumed);

        consumed = tq.consumeTagName();
        Assert.assertEquals("<head><title>Test</title></head><body>", tq.toString());
        Assert.assertEquals("head", consumed);

        consumed = tq.consumeTagName();
        Assert.assertEquals("<title>Test</title></head><body>", tq.toString());
        Assert.assertEquals("title", consumed);

        consumed = tq.consumeTagName();
        Assert.assertEquals("</head><body>", tq.toString());
        Assert.assertEquals("body", consumed);

        consumed = tq.consumeTagName();
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tq = new TokenQueue("<div id=\"foo\" class=\"bar\"></div>");
        String consumed = tq.consumeElementSelector();
        Assert.assertEquals("<div id=\"foo\" class=\"bar\">", tq.toString());
        Assert.assertEquals("div", consumed);

        consumed = tq.consumeElementSelector();
        Assert.assertEquals("id=\"foo\" class=\"bar\"", tq.toString());
        Assert.assertEquals("foo", consumed);

        consumed = tq.consumeElementSelector();
        Assert.assertEquals("class=\"bar\"", tq.toString());
        Assert.assertEquals("bar", consumed);

        consumed = tq.consumeElementSelector();
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tq = new TokenQueue("id:foo class:bar");
        String consumed = tq.consumeCssIdentifier();
        Assert.assertEquals("id:foo class:bar", tq.toString());
        Assert.assertEquals("foo", consumed);

        consumed = tq.consumeCssIdentifier();
        Assert.assertEquals("class:bar", tq.toString());
        Assert.assertEquals("bar", consumed);

        consumed = tq.consumeCssIdentifier();
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", consumed);

        consumed = tq.consumeCssIdentifier();
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tq = new TokenQueue("class=\"foo bar\" id=\"baz\"");
        String consumed = tq.consumeAttributeKey();
        Assert.assertEquals("class=\"foo bar\" id=\"baz\"", tq.toString());
        Assert.assertEquals("class", consumed);

        consumed = tq.consumeAttributeKey();
        Assert.assertEquals("id", consumed);

        consumed = tq.consumeAttributeKey();
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", consumed);

        consumed = tq.consumeAttributeKey();
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testRemainder() {
        TokenQueue tq = new TokenQueue("hello world");
        String remainder = tq.remainder();
        Assert.assertEquals("world", tq.toString());
        Assert.assertEquals("world", remainder);

        remainder = tq.remainder();
        Assert.assertEquals("", tq.toString());
        Assert.assertEquals("", remainder);
    }
}