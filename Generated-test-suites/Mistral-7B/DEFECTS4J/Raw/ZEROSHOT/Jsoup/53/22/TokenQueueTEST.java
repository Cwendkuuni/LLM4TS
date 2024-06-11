import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());

        tokenQueue = new TokenQueue("hello");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertEquals('h', tokenQueue.peek());
        tokenQueue.advance();
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.addFirst('a');
        tokenQueue.addFirst("abc");
        assertEquals("abc", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertTrue(tokenQueue.matches("he"));
        assertTrue(tokenQueue.matches("ll"));
        assertFalse(tokenQueue.matches("o"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertFalse(tokenQueue.matchesCS("He"));
        assertTrue(tokenQueue.matchesCS("ll"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        tokenQueue.addFirst('a');
        assertTrue(tokenQueue.matchesAny("a", "b"));
        tokenQueue.addFirst("abc");
        assertTrue(tokenQueue.matchesAny("a", "b", "c"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        tokenQueue.addFirst('h');
        assertTrue(tokenQueue.matchesAny(new char[]{'h', 'l'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<html><body>");
        assertTrue(tokenQueue.matchesStartTag());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("1 <div class=\"class1 class2\">2");
        tokenQueue.advance();
        tokenQueue.advance();
        tokenQueue.matchChomp("div");
        assertEquals(" class1 class2>", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   ");
        assertTrue(tokenQueue.matchesWhitespace());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance();
        assertTrue(tokenQueue.matchesWord());
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchChompWithUnmatchingSeq() {
        TokenQueue tokenQueue = new TokenQueue("1 <div class=\"class1 class2\">2");
        tokenQueue.matchChomp("span");
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        assertEquals('h', tokenQueue.consume());
        assertEquals('e', tokenQueue.consume());
        assertEquals('l', tokenQueue.consume());
        assertEquals('l', tokenQueue.consume());
        assertEquals(' ', tokenQueue.consume());
        assertEquals('w', tokenQueue.consume());
        assertEquals('o', tokenQueue.consume());
        assertEquals('r', tokenQueue.consume());
        assertEquals('l', tokenQueue.consume());
        assertEquals('d', tokenQueue.consume());
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testConsumeSequence() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        tokenQueue.consume("hel");
        assertEquals(" lo world", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        String consumed = tokenQueue.consumeTo("(one (two) three)");
        assertEquals("one (two) three", consumed);
        assertEquals(" four", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        String consumed = tokenQueue.consumeToIgnoreCase("One (TWO) three");
        assertEquals("one (two) three", consumed);
        assertEquals(" four", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        String consumed = tokenQueue.consumeToAny("one", "three");
        assertEquals("one", consumed);
        assertEquals(" (two) three) four", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        String consumed = tokenQueue.chompBalanced('(', ')');
        assertEquals("one (two) three", consumed);
        assertEquals(" four", tokenQueue.toString());
    }

    @Test
    public void testUnescape() {
        String escapedString = "\\(one \\(two\\) three\\) four";
        String unescapedString = TokenQueue.unescape(escapedString);
        assertEquals("(one (two) three) four", unescapedString);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   ");
        boolean hasConsumedWhitespace = tokenQueue.consumeWhitespace();
        assertTrue(hasConsumedWhitespace);
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        String consumed = tokenQueue.consumeWord();
        assertEquals("hello", consumed);
        assertEquals(" world", tokenQueue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("<html><body>");
        String consumed = tokenQueue.consumeTagName();
        assertEquals("html", consumed);
        assertEquals("<body>", tokenQueue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("html:div.class1.class2");
        String consumed = tokenQueue.consumeElementSelector();
        assertEquals("html:div", consumed);
        assertEquals(".class1.class2", tokenQueue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("class:class1.class2");
        String consumed = tokenQueue.consumeCssIdentifier();
        assertEquals("class1", consumed);
        assertEquals(".class2", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("id=\"myid\" class=\"class1 class2\"");
        String consumed = tokenQueue.consumeAttributeKey();
        assertEquals("id", consumed);
        assertEquals("class=\"class1 class2\"", tokenQueue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        String remainder = tokenQueue.remainder();
        assertEquals(" world", remainder);
        assertTrue(tokenQueue.isEmpty());
    }
}