package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

/**
 * Test cases for TokenQueue class.
 */
public class TokenQueueTest {

    @Test
    public void testConstructor() {
        String data = "sample data";
        TokenQueue tq = new TokenQueue(data);
        assertNotNull(tq);
        assertEquals(data, tq.queue);
        assertEquals(0, tq.pos);
    }

    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.isEmpty());

        data = "sample data";
        tq = new TokenQueue(data);
        assertFalse(tq.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "sample data";
        TokenQueue tq = new TokenQueue(data);
        assertNull(tq.peek());

        data = "sample dataAB";
        tq = new TokenQueue(data);
        assertEquals('A', tq.peek());
    }

    @Test
    public void testAddFirstString() {
        String data = "sample data";
        TokenQueue tq = new TokenQueue(data);
        tq.addFirst("AB");
        assertEquals("ABsample data", tq.queue);
        assertEquals(0, tq.pos);
    }

    @Test
    public void testAddFirstChar() {
        String data = "sample data";
        TokenQueue tq = new TokenQueue(data);
        tq.addFirst('A');
        assertEquals("Asample data", tq.queue);
        assertEquals(0, tq.pos);
    }

    @Test
    public void testMatches() {
        String data = "sample dataAB";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.matches("AB"));
        assertFalse(tq.matches("CD"));
    }

    @Test
    public void testMatchesCS() {
        String data = "sample dataAB";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.matchesCS("AB"));
        assertFalse(tq.matchesCS("ab"));
    }

    @Test
    public void testMatchesAny() {
        String data = "sample dataAB";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.matchesAny("AB"));
        assertFalse(tq.matchesAny("CD"));
        assertTrue(tq.matchesAny("A", "B"));
        assertFalse(tq.matchesAny("C", "D"));
    }

    @Test
    public void testMatchesAnyChars() {
        String data = "sample dataAB";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.matchesAny('A', 'B'));
        assertFalse(tq.matchesAny('C', 'D'));
        assertTrue(tq.matchesAny('A', 'B', 'C'));
        assertFalse(tq.matchesAny('C', 'D', 'E'));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<sample><data>AB</data></sample>";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.matchesStartTag());
        assertFalse(tq.matchesStartTag()); // consume first "<"
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchChompNullSequence() {
        String data = "sample data";
        TokenQueue tq = new TokenQueue(data);
        tq.matchChomp(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchChompEmptyQueue() {
        String data = "";
        TokenQueue tq = new TokenQueue(data);
        tq.matchChomp("AB");
    }

    @Test
    public void testMatchChomp() {
        String data = "sample dataABAB";
        TokenQueue tq = new TokenQueue(data);
        tq.matchChomp("AB");
        assertEquals("sample data", tq.queue);
        assertEquals(2, tq.pos);
    }

    @Test
    public void testMatchesWhitespace() {
        String data = "sample data   ";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.matchesWhitespace());
        assertFalse(tq.matchesWhitespace()); // consume first whitespace
    }

    @Test
    public void testMatchesWord() {
        String data = "sample dataAB";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.matchesWord());
        assertFalse(tq.matchesWord()); // consume first word character
    }

    @Test
    public void testAdvance() {
        String data = "sample dataAB";
        TokenQueue tq = new TokenQueue(data);
        tq.advance();
        assertEquals('A', tq.queue.charAt(tq.pos));
    }

    @Test
    public void testConsume() {
        String data = "sample dataAB";
        TokenQueue tq = new TokenQueue(data);
        assertEquals('A', tq.consume());
        assertEquals('B', tq.consume());
        assertEquals("sample data", tq.queue);
        assertEquals(2, tq.pos);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        String data = "";
        TokenQueue tq = new TokenQueue(data);
        tq.consume("AB");
    }

    @Test
    public void testConsume() {
        String data = "sample dataAB";
        TokenQueue tq = new TokenQueue(data);
        String consumed = tq.consume("AB");
        assertEquals("sample", consumed);
        assertEquals("data", tq.queue);
        assertEquals(3, tq.pos);
    }

    @Test
    public void testConsumeTo() {
        String data = "sample data<tag>sample text<tag2>";
        TokenQueue tq = new TokenQueue(data);
        String consumed = tq.consumeTo("tag");
        assertEquals("sample data<tag", consumed);
        assertEquals("sample text<tag2>", tq.queue);
        assertEquals(11, tq.pos);

        consumed = tq.consumeToIgnoreCase("TAG");
        assertEquals("sample data<tag", consumed);
        assertEquals("sample text<tag2>", tq.queue);
        assertEquals(11, tq.pos);
    }

    @Test
    public void testConsumeToAny() {
        String data = "sample dataABCDABCD";
        TokenQueue tq = new TokenQueue(data);
        String consumed = tq.consumeToAny("AB");
        assertEquals("sample dataAB", consumed);
        assertEquals("CDABCD", tq.queue);
        assertEquals(4, tq.pos);
    }

    @Test
    public void testChompBalanced() {
        String data = "(sample (data (one (two) three) four) five) six";
        String expected = "sample (data (one (two) three)";
        TokenQueue tq = new TokenQueue(data);
        String result = tq.chompBalanced('(', ')');
        assertEquals(expected, result);
        assertEquals("five) six", tq.queue);
        assertEquals(13, tq.pos);
    }

    @Test
    public void testUnescape() {
        String data = "sample \\data\\AB\\C\\";
        String expected = "sample dataABC";
        String result = TokenQueue.unescape(data);
        assertEquals(expected, result);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = "sample data   ";
        TokenQueue tq = new TokenQueue(data);
        assertTrue(tq.consumeWhitespace());
        assertFalse(tq.consumeWhitespace()); // consume all whitespace
    }

    @Test
    public void testConsumeWord() {
        String data = "sample dataAB";
        TokenQueue tq = new TokenQueue(data);
        String result = tq.consumeWord();
        assertEquals("sample", result);
        assertEquals(" dataAB", tq.queue);
        assertEquals(6, tq.pos);
    }

    @Test
    public void testConsumeTagName() {
        String data = "<sample><tag1>text1</tag1><tag2>text2</tag2></sample>";
        String result = tq.consumeTagName();
        assertEquals("tag1", result);
        assertEquals("<tag2>text2</tag2></sample>", tq.queue);
        assertEquals(18, tq.pos);

        result = tq.consumeTagName();
        assertEquals("tag2", result);
        assertEquals("</sample>", tq.queue);
        assertEquals(21, tq.pos);
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "<sample><div id='id1' class='class1'></div>";
        String result = tq.consumeElementSelector();
        assertEquals("div", result);
        assertEquals("<id='id1' class='class1'>", tq.queue);
        assertEquals(13, tq.pos);

        result = tq.consumeElementSelector();
        assertEquals("id", result);
        assertEquals("<class='class1'>", tq.queue);
        assertEquals(14, tq.pos);

        result = tq.consumeElementSelector();
        assertEquals("class", result);
        assertEquals("", tq.queue);
        assertEquals(15, tq.pos);
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "style='color:red; background-color:blue; border:1px solid black;'";
        String result = tq.consumeCssIdentifier();
        assertEquals("color", result);
        assertEquals(":red; background-color:blue; border:1px solid black;", tq.queue);
        assertEquals(9, tq.pos);

        result = tq.consumeCssIdentifier();
        assertEquals("background-color", result);
        assertEquals("border:1px solid black;", tq.queue);
        assertEquals(20, tq.pos);

        result = tq.consumeCssIdentifier();
        assertEquals("border", result);
        assertEquals("1px solid black;", tq.queue);
        assertEquals(21, tq.pos);
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "<div style='color:red; background-color:blue; border:1px solid black;' class='class1 class2' id='id'>Test</div>";
        String result = tq.consumeAttributeKey();
        assertEquals("style", result);
        assertEquals("class='class1 class2' id='id'>Test</div>", tq.queue);
        assertEquals(6, tq.pos);

        result = tq.consumeAttributeKey();
        assertEquals("class", result);
        assertEquals("id='id'>Test</div>", tq.queue);
        assertEquals(11, tq.pos);

        result = tq.consumeAttributeKey();
        assertEquals("id", result);
        assertEquals("", tq.queue);
        assertEquals(12, tq.pos);
    }

    @Test
    public void testRemainder() {
        String data = "sample dataAB";
        TokenQueue tq = new TokenQueue(data);
        String result = tq.remainder();
        assertEquals("AB", result);
        assertEquals("sample data", tq.queue);
        assertEquals(3, tq.pos);
    }
}