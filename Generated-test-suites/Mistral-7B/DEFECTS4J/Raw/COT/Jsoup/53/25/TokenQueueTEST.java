package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        String data = "sample data";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertNotNull(queue);
        Assert.assertEquals(data, queue.queue);
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        String data = "sample data";
        queue = new TokenQueue(data);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "sample data";
        TokenQueue queue = new TokenQueue(data);
        char c = queue.peek();
        assertEquals(data.charAt(0), c);
        queue.advance();
        c = queue.peek();
        assertEquals(data.charAt(1), c);
        queue.consume();
        c = queue.peek();
        assertEquals(data.charAt(2), c);
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('A');
        assertEquals("A", queue.queue);
        queue.addFirst("test");
        assertEquals("testA", queue.queue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFirstWithNull() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst(null);
    }

    @Test
    public void testMatches() {
        String data = "sample data";
        TokenQueue queue = new TokenQueue(data);
        // simple match
        assertTrue(queue.matches("sa"));
        // match at end
        assertTrue(queue.matches("data"));
        // match in middle
        queue.advance();
        queue.advance();
        assertTrue(queue.matches("mp"));
        // no match
        assertFalse(queue.matches("xx"));
        // match empty
        assertTrue(queue.matches(""));
    }

    @Test
    public void testMatchesCS() {
        String data = "Sample Data";
        TokenQueue queue = new TokenQueue(data);
        // case sensitive match
        assertTrue(queue.matchesCS("Sample"));
        assertFalse(queue.matchesCS("sample"));
    }

    @Test
    public void testMatchesAny() {
        String data = "sample data";
        TokenQueue queue = new TokenQueue(data);
        // simple match
        assertTrue(queue.matchesAny("sa"));
        // match at end
        assertTrue(queue.matchesAny("data"));
        // match in middle
        queue.advance();
        queue.advance();
        assertTrue(queue.matchesAny("mp"));
        // no match
        assertFalse(queue.matchesAny("xx"));
        // test empty match
        assertTrue(queue.matchesAny(""));
        // test multiple matches
        assertTrue(queue.matchesAny("sa", "mp"));
    }

    @Test
    public void testMatchesAnyWithEmptySequence() {
        String data = "sample data";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesAny());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesAnyWithNullSequence() {
        String data = "sample data";
        TokenQueue queue = new TokenQueue(data);
        queue.matchesAny(null);
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " sample data ";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesWhitespace());
        queue.advance();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "sample data";
        TokenQueue queue = new TokenQueue(data);
        assertTrue(queue.matchesWord());
        queue.advance();
        assertTrue(queue.matchesWord());
        queue.advance();
        assertTrue(queue.matchesWord());
        queue.advance();
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "sample data";
        TokenQueue queue = new TokenQueue(data);
        queue.advance();
        assertEquals(data.charAt(1), queue.queue.charAt(queue.pos));
        queue.advance();
        assertEquals(data.charAt(2), queue.queue.charAt(queue.pos));
        queue.advance();
        assertEquals(data.charAt(3), queue.queue.charAt(queue.pos));
        queue.advance();
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testConsume() {
        String data = "sample data";
        TokenQueue queue = new TokenQueue(data);
        String consumed = queue.consume();
        assertEquals(data.charAt(0), consumed.charAt(0));
        assertEquals("ample data", queue.queue);

        consumed = queue.consume();
        assertEquals(data.charAt(1), consumed.charAt(0));
        assertEquals("ample data", queue.queue);

        consumed = queue.consume();
        assertEquals(data.charAt(2), consumed.charAt(0));
        assertEquals("ample data", queue.queue);

        consumed = queue.consume();
        assertEquals(data.charAt(3), consumed.charAt(0));
        assertEquals("ample data", queue.queue);

        consumed = queue.consume();
        assertEquals(data.charAt(4), consumed.charAt(0));
        assertEquals("", queue.queue);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        queue.consume();
    }

    @Test
    public void testConsumeSequence() {
        String data = "sample data";
        TokenQueue queue = new TokenQueue(data);
        String seq = "sa";
        queue.consume(seq);
        assertEquals("ample data", queue.queue);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceMismatch() {
        String data = "sample data";
        TokenQueue queue = new TokenQueue(data);
        String seq = "xx";
        queue.consume(seq);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceTooShort() {
        String data = "sample data";
        TokenQueue queue = new TokenQueue(data);
        String seq = "sa";
        queue.consume(seq.substring(0, seq.length()-1));
    }

    @Test
    public void testConsumeTo() {
        String data = "sample data <test>";
        TokenQueue queue = new TokenQueue(data);
        String seq = "sa";
        String consumed = queue.consumeTo(seq);
        assertEquals("sample ", consumed);
        assertEquals("data <test>", queue.queue);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "Sample Data <Test>";
        TokenQueue queue = new TokenQueue(data);
        String seq = "SA";
        String consumed = queue.consumeToIgnoreCase(seq);
        assertEquals("Sample Data ", consumed);
        assertEquals(" <Test>", queue.queue);
    }

    @Test
    public void testConsumeToAny() {
        String data = "sample data <test1> <test2>";
        TokenQueue queue = new TokenQueue(data);
        String[] seq = {"sa", "te"};
        String consumed = queue.consumeToAny(seq);
        assertEquals("sample ", consumed);
        assertEquals("<test1> <test2>", queue.queue);
    }

    @Test
    public void testChompTo() {
        String data = "sample data <test>";
        TokenQueue queue = new TokenQueue(data);
        String seq = "sa";
        String consumed = queue.chompTo(seq);
        assertEquals("sample ", consumed);
        assertEquals("test", queue.queue);
    }

    @Test
    public void testChompToIgnoreCase() {
        String data = "Sample Data <Test>";
        TokenQueue queue = new TokenQueue(data);
        String seq = "SA";
        String consumed = queue.chompToIgnoreCase(seq);
        assertEquals("Sample Data ", consumed);
        assertEquals(" <Test>", queue.queue);
    }

    @Test
    public void testChompBalanced() {
        String data = "(sample data) (test1) (test2)";
        TokenQueue queue = new TokenQueue(data);
        char open = '(';
        char close = ')';
        String consumed = queue.chompBalanced(open, close);
        assertEquals("(sample data)", consumed);
        assertEquals("(test1) (test2)", queue.queue);
    }

    @Test
    public void testUnescape() {
        String data = "\\(sample \\data\\)";
        String escaped = queue.unescape(data);
        assertEquals("(sample data)", escaped);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = " sample data ";
        TokenQueue queue = new TokenQueue(data);
        boolean consumed = queue.consumeWhitespace();
        assertTrue(consumed);
        assertEquals("sample data", queue.queue);
    }

    @Test
    public void testConsumeWord() {
        String data = "sample data";
        TokenQueue queue = new TokenQueue(data);
        String consumed = queue.consumeWord();
        assertEquals("sample", consumed);
        assertEquals(" data", queue.queue);
    }

    @Test
    public void testConsumeTagName() {
        String data = "<sample data>";
        TokenQueue queue = new TokenQueue(data);
        String consumed = queue.consumeTagName();
        assertEquals("sample", consumed);
        assertEquals(" data>", queue.queue);
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "<sample:data>";
        TokenQueue queue = new TokenQueue(data);
        String consumed = queue.consumeElementSelector();
        assertEquals("sample", consumed);
        assertEquals(":data>", queue.queue);
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "class='sample-identifier'";
        TokenQueue queue = new TokenQueue(data);
        String consumed = queue.consumeCssIdentifier();
        assertEquals("sample-identifier", consumed);
        assertEquals("'", queue.queue);
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "class='sample attribute:key'";
        TokenQueue queue = new TokenQueue(data);
        String consumed = queue.consumeAttributeKey();
        assertEquals("sample attribute:key", consumed);
        assertEquals("'", queue.queue);
    }

    @Test
    public void testRemainder() {
        String data = "sample data";
        TokenQueue queue = new TokenQueue(data);
        String remainder = queue.remainder();
        assertEquals("data", remainder);
        queue.consumeAll();
        assertEquals("", queue.remainder());
    }
}