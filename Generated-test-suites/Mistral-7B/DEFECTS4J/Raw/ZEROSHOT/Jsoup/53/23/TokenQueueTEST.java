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
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());
        queue.advance();
        assertEquals('b', queue.peek());
        queue.consume();
        assertEquals('c', queue.peek());
        queue.consumeTo("bc");
        assertEquals('', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        queue.addFirst("def");
        assertEquals("defa", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("xyz"));
        queue.consume("ab");
        assertFalse(queue.matches("ab"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCDEF");
        assertTrue(queue.matchesCS("AB"));
        assertFalse(queue.matchesCS("xyz"));
        queue.consume('A');
        assertFalse(queue.matchesCS("AB"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("12345");
        assertTrue(queue.matchesAny("1", "2", "3"));
        assertFalse(queue.matchesAny("xyz", "abc"));
        queue.consume('1');
        assertFalse(queue.matchesAny("1", "2", "3"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("ABCDEF");
        assertTrue(queue.matchesAny('A', 'B', 'C'));
        assertFalse(queue.matchesAny('x', 'y', 'z'));
        queue.consume('A');
        assertFalse(queue.matchesAny('A', 'B', 'C'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body>");
        assertTrue(queue.matchesStartTag());
        queue.consume('<');
        queue.consume('h');
        queue.consume('t');
        queue.consume('m');
        queue.consume('l');
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdefghijk");
        queue.matchChomp("ab");
        assertEquals("cdefghijk", queue.toString());
        queue.matchChomp("cd");
        assertEquals("efghijk", queue.toString());
        queue.matchChomp("ef");
        assertEquals("ghijk", queue.toString());
        queue.matchChomp("gh");
        assertEquals("ijk", queue.toString());
        queue.matchChomp("i");
        assertEquals("jk", queue.toString());
        queue.matchChomp("j");
        assertEquals("k", queue.toString());
        queue.matchChomp("k");
        assertEquals("", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        assertTrue(queue.matchesWhitespace());
        queue.consumeWhitespace();
        queue.consumeWhitespace();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesWord());
        queue.consume('a');
        assertTrue(queue.matchesWord());
        queue.consume('b');
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.advance();
        assertEquals('b', queue.peek());
        queue.advance();
        assertEquals('c', queue.peek());
        queue.consume('c');
        queue.advance();
        assertEquals('d', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals('d', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('f', queue.consume());
        assertEquals('', queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeInvalidSequence() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeTo("bc"));
        assertEquals("efg", queue.toString());
        assertEquals("abc", queue.consumeTo("bc"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("ABCDefGhIjK");
        assertEquals("ABCDef", queue.consumeToIgnoreCase("ABCDEF"));
        assertEquals("GhIjK", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("12345");
        assertEquals("12", queue.consumeToAny("1", "2"));
        assertEquals("345", queue.toString());
    }

    @Test
    public void testChompTo() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        assertEquals("four", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        String input = "\\n\\r\\t\\\"\\\\";
        String output = TokenQueue.unescape(input);
        assertEquals("\n\r\t\"\\", output);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeWord());
        assertEquals("def", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body>");
        assertEquals("html", queue.consumeTagName());
        assertEquals("head", queue.consumeTagName());
        assertEquals("title", queue.consumeTagName());
        assertEquals("Test", queue.consumeWord());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("abc div.class1 p.class2 span#id1");
        assertEquals("abc", queue.consumeElementSelector());
        assertEquals("div.class1", queue.consumeElementSelector());
        assertEquals("p.class2", queue.consumeElementSelector());
        assertEquals("span", queue.consumeElementSelector());
        assertEquals("#id1", queue.consumeCssIdentifier());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class1 class2 id1 id2");
        assertEquals("class1", queue.consumeCssIdentifier());
        assertEquals("class2", queue.consumeCssIdentifier());
        assertEquals("id1", queue.consumeCssIdentifier());
        assertEquals("id2", queue.consumeCssIdentifier());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"class1\" id=\"id1\" style=\"color: red;\"");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("style", queue.consumeAttributeKey());
        assertEquals("", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.remainder());
        queue.consume("abc");
        assertEquals("def", queue.remainder());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.toString());
        queue.consume("abc");
        assertEquals("def", queue.toString());
    }
}