import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abcdefg");
        char peek = queue.peek();
        assertEquals('a', peek);
        assertTrue(queue.isEmpty() || queue.peek() == 'a');
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abcdefg");
        queue.addFirst('X');
        assertEquals('X', queue.peek());
        assertEquals("Xabcdefg", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("xyz"));
        assertFalse(queue.matches(""));
        queue.advance();
        assertFalse(queue.matches("ab"));
        assertTrue(queue.matches("cd"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCDEFG");
        assertTrue(queue.matchesCS("ab")); // case sensitive test
        assertFalse(queue.matchesCS("XYZ"));
        assertFalse(queue.matchesCS(""));
        queue.advance();
        assertFalse(queue.matchesCS("ab"));
        assertTrue(queue.matchesCS("CD"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matchesAny("ab", "cd"));
        assertFalse(queue.matchesAny("xyz", "ab"));
        assertFalse(queue.matchesAny("", ""));
        queue.advance();
        assertFalse(queue.matchesAny("ab", "cd"));
        assertTrue(queue.matchesAny("ef", "ef"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matchesAny('a', 'b', 'c'));
        assertFalse(queue.matchesAny('x', 'a', 'b'));
        assertFalse(queue.matchesAny('', 'a', 'b'));
        queue.advance();
        assertFalse(queue.matchesAny('a', 'b', 'c'));
        assertTrue(queue.matchesAny('f', 'f', 'f'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag()); // consume first character
        queue.advance();
        assertFalse(queue.matchesStartTag()); // consume <
        assertTrue(queue.matchesStartTag()); // consume h
    }

    @Test(expected = AssertionError.class)
    public void testMatchChompEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.matchChomp("xyz");
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcxyzdef");
        assertTrue(queue.matchChomp("abc"));
        assertEquals("def", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" abc    def ");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace()); // consume whitespace
        queue.advance();
        assertFalse(queue.matchesWhitespace());
        assertTrue(queue.matchesWord());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcd123efghIJK");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord()); // consume word character
        queue.advance();
        assertFalse(queue.matchesWord());
        assertTrue(queue.matchesWhitespace());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcd123efghIJK");
        queue.advance();
        assertEquals('b', queue.peek());
        queue.advance();
        assertEquals('c', queue.peek());
        // empty queue
        queue.advance();
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdefg");
        char consumed = queue.consume();
        assertEquals('a', consumed);
        assertEquals("bcdefg", queue.toString());
    }

    @Test
    public void testConsumeString() {
        TokenQueue queue = new TokenQueue("abcdefg");
        String consumed = queue.consume("ab");
        assertEquals("cd
efg", queue.toString());
        assertEquals("ab", consumed);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeStringMismatch() {
        TokenQueue queue = new TokenQueue("abcdefg");
        queue.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        String consumed = queue.consumeTo("(one (two) ");
        assertEquals("three) four", queue.toString());
        assertEquals("(one (two)", queue.toString());
        assertEquals("one (two)", consumed);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        String consumed = queue.consumeToIgnoreCase("(one (two)");
        assertEquals("three) four", queue.toString());
        assertEquals("(one (two)", queue.toString());
        assertEquals("one (two)", consumed);
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("123<html><head><title>Test</title></head><body>");
        String consumed = queue.consumeToAny("1", "<", "h", "t", "t", "l");
        assertEquals("</title></head><body>", queue.toString());
        assertEquals("123<html><head><title>Test</title>", queue.toString());
        assertEquals("123", consumed);
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        String balanced = queue.chompBalanced('(', ')');
        assertEquals("one (two) three", balanced);
        assertEquals(" four", queue.toString());
    }

    @Test
    public void testUnescape() {
        String escaped = "\\(one \\(two\\) three\\) four";
        String unescaped = TokenQueue.unescape(escaped);
        assertEquals("(one (two) three) four", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" abc    def ");
        boolean seen = queue.consumeWhitespace();
        assertTrue(seen);
        assertEquals("def", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcd123efghIJK");
        String word = queue.consumeWord();
        assertEquals("abcd", queue.toString());
        assertEquals("abcd", word);
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body>");
        String tagName = queue.consumeTagName();
        assertEquals("html", tagName);
        assertEquals("<head><title>Test</title>", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body><div class=\"test\">");
        String elementSelector = queue.consumeElementSelector();
        assertEquals("div", elementSelector);
        assertEquals("<class=\"test\">", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class=\"test\" id=\"id123\"");
        String cssIdentifier = queue.consumeCssIdentifier();
        assertEquals("test", cssIdentifier);
        assertEquals(" id=\"id123\"", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"test\" id=\"id123\" data-test=\"test123\"");
        String attributeKey = queue.consumeAttributeKey();
        assertEquals("class", attributeKey);
        assertEquals(" id=\"id123\" data-test=\"test123\"", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdefg");
        String remainder = queue.remainder();
        assertEquals("efg", remainder);
        assertEquals("abc", queue.toString());
    }
}