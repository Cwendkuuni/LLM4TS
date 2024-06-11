import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("abc");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abcd");
        assertEquals('a', queue.peek());
        assertEquals('a', queue.consume());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekAfterConsumeAll() {
        TokenQueue queue = new TokenQueue("abcd");
        queue.consume();
        queue.consume();
        queue.consume();
        queue.consume();
        queue.peek();
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        queue.addFirst("bc");
        assertEquals("bcac", queue.toString());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals('a', queue.consume());
        assertEquals('', queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("xyz"));
        assertFalse(queue.matches("abcd"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCabcDEFG");
        assertTrue(queue.matchesCS("AB"));
        assertFalse(queue.matchesCS("xyz"));
        assertFalse(queue.matchesCS("AbCd"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcabc");
        assertTrue(queue.matchesAny("ab", "cd"));
        assertFalse(queue.matchesAny("xy", "z"));
        assertFalse(queue.matchesAny("abcd"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny('a', 'b', 'c'));
        assertFalse(queue.matchesAny('x', 'y'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><body>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag()); // consume first character
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdefg");
        queue.matchChomp("ab");
        assertEquals("cdefg", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\n\r abc");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace()); // consume whitespace character
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcd1234");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord()); // consume word character
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        assertEquals('b', queue.consume());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEmpty(queue);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeAfterEmpty() {
        TokenQueue queue = new TokenQueue("");
        queue.consume();
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        String consumed = queue.consume(

);
        assertEquals("c", queue.toString());
        assertEquals("ab", consumed);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeAfterNotMatch() {
        TokenQueue queue = new TokenQueue("abc");
        queue.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        String consumed = queue.consumeTo("(one (two) three)");
        assertEquals("one (two) three", consumed);
        assertEquals("four", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("(One (TWO) Three) Four");
        String consumed = queue.consumeToIgnoreCase("(one (two) three)");
        assertEquals("One (TWO) Three", consumed);
        assertEquals("Four", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdefg");
        String consumed = queue.consumeToAny("b", "c");
        assertEquals("acdefg", queue.toString());
        assertEquals("ab", consumed);
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        String consumed = queue.chompBalanced('(', ')');
        assertEquals("one (two) three", consumed);
        assertEquals("four", queue.toString());
    }

    @Test
    public void testUnescape() {
        String input = "\\(one \\(two\\) three\\) four";
        String output = TokenQueue.unescape(input);
        String expected = "(one (two) three) four";
        assertEquals(expected, output);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" \t\n\r abc");
        assertTrue(queue.consumeWhitespace());
        assertEquals("abc", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcd1234");
        String consumed = queue.consumeWord();
        assertEquals("abcd", queue.toString());
        assertEquals("1234", consumed);
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><body>");
        String consumed = queue.consumeTagName();
        assertEquals("html", consumed);
        assertEquals("<body>", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.class:hover");
        String consumed = queue.consumeElementSelector();
        assertEquals("div", consumed);
        assertEquals(".class:hover", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("#id class name");
        String consumed = queue.consumeCssIdentifier();
        assertEquals("#id", consumed);
        assertEquals(" class name", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("id='value' class='class' name='name'");
        String consumed = queue.consumeAttributeKey();
        assertEquals("id", consumed);
        assertEquals("value class='class' name='name'", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcd1234");
        String remainder = queue.remainder();
        assertEquals("1234", remainder);
        assertEquals("abcd", queue.toString());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("abcd1234");
        assertEquals("abcd1234", queue.toString());
    }
}