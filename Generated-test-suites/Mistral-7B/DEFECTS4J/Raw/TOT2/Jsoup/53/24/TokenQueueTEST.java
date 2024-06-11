import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        TokenQueue queue = new TokenQueue("Hello World!");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals("Hello", queue.consumeWord());
        Assert.assertEquals("World!", queue.consumeToIgnoreCase("world"));
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());

        queue = new TokenQueue("Hello");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("Hello World!");
        Assert.assertEquals('H', queue.peek());
        queue.advance();
        Assert.assertEquals('e', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddFirstStringNull() {
        TokenQueue queue = new TokenQueue("Hello World!");
        queue.addFirst(null);
    }

    @Test
    public void testAddFirstString() {
        TokenQueue queue = new TokenQueue("Hello World!");
        queue.addFirst("!Hello");
        Assert.assertEquals("!HelloHello World!", queue.toString());
    }

    @Test
    public void testAddFirstCharacter() {
        TokenQueue queue = new TokenQueue("Hello World!");
        queue.addFirst('!');
        Assert.assertEquals("!Hello World!", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("Hello World!");
        Assert.assertTrue(queue.matches("ll"));
        Assert.assertFalse(queue.matches("oo"));
        Assert.assertTrue(queue.matchesIgnoreCase("Hello"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Hello World!");
        Assert.assertTrue(queue.matchesCS("Hello"));
        Assert.assertFalse(queue.matchesCS("World"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("Hello World!");
        Assert.assertTrue(queue.matchesAny("l", "l"));
        Assert.assertTrue(queue.matchesAny("l", "o"));
        Assert.assertFalse(queue.matchesAny("x", "o"));
    }

    @Test
    public void testMatchesAnyCharArray() {
        TokenQueue queue = new TokenQueue("Hello World!");
        Assert.assertTrue(queue.matchesAny(new char[]{'l', 'l'}));
        Assert.assertTrue(queue.matchesAny(new char[]{'l', 'o'}));
        Assert.assertFalse(queue.matchesAny(new char[]{'x', 'o'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>");
        Assert.assertTrue(queue.matchesStartTag());
        Assert.assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("Hello <world> World!");
        Assert.assertTrue(queue.matchChomp("<world>"));
        Assert.assertEquals("World!", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" Hello  World!");
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertFalse(queue.matchesWhitespace());
        queue.consumeWhitespace();
        Assert.assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("Hello World!");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("Hello World!");
        queue.advance();
        Assert.assertEquals('e', queue.consume());
        queue.advance();
        Assert.assertEquals('l', queue.consume());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("Hello World!");
        Assert.assertEquals('H', queue.consume());
        Assert.assertEquals('e', queue.consume());
        Assert.assertEquals('l', queue.consume());
        Assert.assertEquals('l', queue.consume());
        Assert.assertEquals('o', queue.consume());
        Assert.assertEquals(' ', queue.consume());
        Assert.assertEquals('W', queue.consume());
        Assert.assertEquals('o', queue.consume());
        Assert.assertEquals('r', queue.consume());
        Assert.assertEquals('l', queue.consume());
        Assert.assertEquals('d', queue.consume());
        Assert.assertEquals('!', queue.consume());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeInvalidSequence() {
        TokenQueue queue = new TokenQueue("Hello World!");
        queue.consume("World");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("Hello World!");
        Assert.assertEquals("Hello", queue.consumeTo("ello"));
        Assert.assertEquals("World!", queue.toString());

        queue = new TokenQueue("Hello World!");
        queue.consumeToIgnoreCase("HeLLo");
        Assert.assertEquals("HeLLoWorld!", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("Hello World! Hello World!");
        Assert.assertEquals("Hello", queue.consumeToAny("ello", "hello"));
        Assert.assertEquals("World! World!", queue.toString());
    }

    @Test
    public void testConsumeToAnyIgnoreCase() {
        TokenQueue queue = new TokenQueue("Hello World! Hello World!");
        Assert.assertEquals("Hello", queue.consumeToAnyIgnoreCase("HELLO", "hELLO"));
        Assert.assertEquals("World! World!", queue.toString());
    }

    @Test
    public void testConsumeBalanced() {
        TokenQueue queue = new TokenQueue("(())[]{}");
        Assert.assertEquals("(())", queue.chompBalanced('(', ')' ));
        Assert.assertEquals("[]{}", queue.toString());

        queue = new TokenQueue("(())[]{} <script> <script> ");
        Assert.assertEquals("(<script>", queue.chompBalanced('<script>', '</script>' ));
        Assert.assertEquals("<script> <script>", queue.toString());

        queue = new TokenQueue("(())[]{} <script><script>");
        Assert.assertEquals("(<script>", queue.chompBalanced('<script>', '</script>' ));
        Assert.assertEquals("<script><script>", queue.toString());
    }

    @Test
    public void testUnescape() {
        String in = "\\((\\(foo\\)\\))\\";
        Assert.assertEquals("(foo)", TokenQueue.unescape(in));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   Hello World!");
        Assert.assertTrue(queue.consumeWhitespace());
        Assert.assertEquals("Hello", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("Hello World!");
        Assert.assertEquals("Hello", queue.consumeWord());
        Assert.assertEquals("World!", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>Hello World!");
        Assert.assertEquals("html", queue.consumeTagName());
        Assert.assertEquals("<head><title>Hello World!", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#myDiv.myClass:hover");
        Assert.assertEquals("div", queue.consumeElementSelector());
        Assert.assertEquals("#myDiv.myClass:hover", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("background-color: red;");
        Assert.assertEquals("background-color", queue.consumeCssIdentifier());
        Assert.assertEquals(": red", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"red\"");
        Assert.assertEquals("class", queue.consumeAttributeKey());
        Assert.assertEquals("=\"red\"", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("Hello World!");
        Assert.assertEquals("World!", queue.remainder());
        queue.consumeTo("World");
        Assert.assertEquals("", queue.remainder());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertEquals("", queue.toString());

        queue = new TokenQueue("Hello World!");
        Assert.assertEquals("Hello World!", queue.toString());

        queue.consumeTo("World");
        Assert.assertEquals("", queue.toString());
    }
}