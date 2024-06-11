package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testNewInstance() {
        String data = "some data";
        TokenQueue queue = new TokenQueue(data);
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals(data, queue.toString());
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        Assert.assertTrue(queue.isEmpty());

        String data = "some data";
        queue = new TokenQueue(data);
        Assert.assertFalse(queue.isEmpty());
        queue.consume('s');
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("some data");
        Assert.assertEquals('s', queue.peek());
        Assert.assertEquals('s', queue.consume());
        Assert.assertEquals('o', queue.peek());
        Assert.assertEquals('o', queue.consume());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWhenEmpty() {
        TokenQueue queue = new TokenQueue("");
        queue.consume();
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('', queue.toString());

        queue = new TokenQueue("some data");
        queue.addFirst('a');
        Assert.assertEquals('a', queue.consume());
        Assert.assertEquals('s', queue.consume());
        Assert.assertEquals('o', queue.consume());
        Assert.assertEquals('m', queue.consume());
        Assert.assertEquals('e', queue.consume());
        Assert.assertEquals(' ', queue.consume());
        Assert.assertEquals('d', queue.consume());
        Assert.assertEquals('a', queue.consume());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("some data");
        Assert.assertFalse(queue.matches("not data"));
        Assert.assertTrue(queue.matches("some"));
        Assert.assertTrue(queue.matches("data"));
        Assert.assertTrue(queue.matches(""));
        queue.consume('s');
        queue.consume('o');
        Assert.assertFalse(queue.matches("some"));
        Assert.assertTrue(queue.matches("data"));
        queue.consume('d');
        queue.consume(' ');
        Assert.assertFalse(queue.matches("data"));
        Assert.assertTrue(queue.matches(""));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("some data");
        Assert.assertFalse(queue.matchesCS("not data"));
        Assert.assertTrue(queue.matchesCS("some"));
        Assert.assertTrue(queue.matchesCS("data"));
        queue.consume('s');
        queue.consume('o');
        Assert.assertFalse(queue.matchesCS("some"));
        Assert.assertTrue(queue.matchesCS("data"));
        queue.consume('d');
        queue.consume(' ');
        Assert.assertFalse(queue.matchesCS("data"));
        Assert.assertTrue(queue.matchesCS(""));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("some data");
        Assert.assertFalse(queue.matchesAny("not data", "not data2"));
        Assert.assertTrue(queue.matchesAny("some", "data"));
        Assert.assertTrue(queue.matchesAny("data", "some", "data"));
        queue.consume('s');
        queue.consume('o');
        Assert.assertTrue(queue.matchesAny("data", "some", "data"));
        Assert.assertFalse(queue.matchesAny("some", "data"));
        queue.consume('d');
        queue.consume(' ');
        Assert.assertFalse(queue.matchesAny("data", "some", "data"));
        Assert.assertTrue(queue.matchesAny("", ""));
    }

    @Test
    public void testMatchesAnyWithChars() {
        TokenQueue queue = new TokenQueue("some data");
        Assert.assertFalse(queue.matchesAny(new String[]{"not data", "not data2"}, new char[]{'a', 'b'}));
        Assert.assertTrue(queue.matchesAny(new String[]{"some", "data"}, new char[]{'s', 'o'}));
        Assert.assertTrue(queue.matchesAny(new String[]{"data", "some", "data"}, new char[]{'d', ' ', ' '}));
        queue.consume('s');
        queue.consume('o');
        Assert.assertTrue(queue.matchesAny(new String[]{"data", "some", "data"}, new char[]{'d', ' ', ' '}));
        Assert.assertFalse(queue.matchesAny(new String[]{"some", "data"}, new char[]{'s', 'o'}));
        queue.consume('d');
        queue.consume(' ');
        Assert.assertFalse(queue.matchesAny(new String[]{"data", "some", "data"}, new char[]{'d', ' ', ' '}));
        Assert.assertTrue(queue.matchesAny(new String[]{"", ""}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><body>");
        Assert.assertTrue(queue.matchesStartTag());
        queue.consume('<'); queue.consume('h'); queue.consume('t'); queue.consume('m'); queue.consume('l');
        Assert.assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("<html><body>");
        Assert.assertTrue(queue.matchChomp("<html>"));
        Assert.assertEquals("", queue.toString());

        queue = new TokenQueue("some <data> data");
        Assert.assertTrue(queue.matchChomp("some "));
        Assert.assertEquals(" <data> data", queue.toString());
        Assert.assertTrue(queue.matchChomp("data"));
        Assert.assertEquals("", queue.toString());

        queue = new TokenQueue("some <data> data");
        Assert.assertFalse(queue.matchChomp("not data"));
        Assert.assertEquals("some <data> data", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("some data");
        Assert.assertFalse(queue.matchesWhitespace());
        Assert.assertEquals('s', queue.consume());
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertEquals(' ', queue.consume());
        Assert.assertTrue(queue.matchesWhitespace());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("some data");
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals('s', queue.consume());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals('o', queue.consume());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals('m', queue.consume());
        Assert.assertTrue(queue.matchesWord());
        Assert.assertEquals('e', queue.consume());
        Assert.assertFalse(queue.matchesWord());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("some data");
        queue.advance();
        Assert.assertEquals('s', queue.peek());
        queue.advance();
        Assert.assertEquals('o', queue.peek());
        queue.advance();
        Assert.assertEquals('m', queue.peek());
        queue.advance();
        Assert.assertEquals('e', queue.peek());
        queue.advance();
        Assert.assertEquals('', queue.toString());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("some data");
        Assert.assertEquals('s', queue.consume());
        Assert.assertEquals('o', queue.consume());
        Assert.assertEquals('m', queue.consume());
        Assert.assertEquals('e', queue.consume());
        Assert.assertEquals('', queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume();
    }

    @Test
    public void testConsumeSequence() {
        TokenQueue queue = new TokenQueue("some <data> data");
        String sequence = "some <";
        queue.consume(sequence);
        Assert.assertEquals("data> data", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptySequence() {
        TokenQueue queue = new TokenQueue("some <data> data");
        queue.consume("");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeMismatchingSequence() {
        TokenQueue queue = new TokenQueue("some <data> data");
        queue.consume("data>");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("some <data> data");
        String sequence = "data";
        String result = queue.consumeTo(sequence);
        Assert.assertEquals("", queue.toString());
        Assert.assertEquals("data", result);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("SOME <Data> Data");
        String sequence = "data";
        String result = queue.consumeToIgnoreCase(sequence);
        Assert.assertEquals("", queue.toString());
        Assert.assertEquals("Data", queue.toString());
        Assert.assertEquals("data", result);
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("some <data> data");
        String[] sequences = {"data", "Data"};
        String result = queue.consumeToAny(sequences);
        Assert.assertEquals("", queue.toString());
        Assert.assertEquals("data", result);
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(some (data) data) data");
        Assert.assertEquals("some (data) data", queue.chompBalanced('(', ')').toString());
        Assert.assertEquals("data", queue.toString());

        queue = new TokenQueue("((some (data) data) data) data");
        Assert.assertEquals("some (data) data", queue.chompBalanced('(', ')').toString());
        Assert.assertEquals("data data", queue.toString());

        queue = new TokenQueue("(((some (data) data) data) data) data");
        Assert.assertEquals("some (data) data", queue.chompBalanced('(', ')').toString());
        Assert.assertEquals("data data data", queue.toString());

        queue = new TokenQueue("(((some (data) data) data) data) data");
        Assert.assertEquals("some (data) data", queue.chompBalanced('(', ')').toString());
        Assert.assertEquals("data data data data", queue.toString());
    }

    @Test
    public void testUnescape() {
        String escaped = "\\\\some\\\\data";
        String unescaped = TokenQueue.unescape(escaped);
        Assert.assertEquals("some data", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" some data ");
        boolean result = queue.consumeWhitespace();
        Assert.assertEquals("data", queue.toString());
        Assert.assertTrue(result);

        queue = new TokenQueue(" some data ");
        result = queue.consumeWhitespace();
        Assert.assertEquals("data", queue.toString());
        Assert.assertTrue(result);

        queue = new TokenQueue(" some data ");
        result = queue.consumeWhitespace();
        Assert.assertEquals("data", queue.toString());
        Assert.assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("some word");
        String result = queue.consumeWord();
        Assert.assertEquals("some", result);
        Assert.assertEquals("word", queue.toString());

        queue = new TokenQueue("some word");
        result = queue.consumeWord();
        Assert.assertEquals("word", result);
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("tag name");
        String result = queue.consumeTagName();
        Assert.assertEquals("tag", result);
        Assert.assertEquals(" name", queue.toString());

        queue = new TokenQueue("tag name");
        result = queue.consumeTagName();
        Assert.assertEquals("name", result);
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("tag name:selector");
        String result = queue.consumeElementSelector();
        Assert.assertEquals("tag name", result);
        Assert.assertEquals(":selector", queue.toString());

        queue = new TokenQueue("tag name:selector");
        result = queue.consumeElementSelector();
        Assert.assertEquals("selector", result);
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("ID idClass classID");
        String result = queue.consumeCssIdentifier();
        Assert.assertEquals("ID", result);
        Assert.assertEquals(" idClass classID", queue.toString());

        queue = new TokenQueue("ID idClass classID");
        result = queue.consumeCssIdentifier();
        Assert.assertEquals("idClass", result);
        Assert.assertEquals(" classID", queue.toString());

        queue = new TokenQueue("ID idClass classID");
        result = queue.consumeCssIdentifier();
        Assert.assertEquals("classID", result);
        Assert.assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("attribute:value attribute value attribute:value");
        String result = queue.consumeAttributeKey();
        Assert.assertEquals("attribute", result);
        Assert.assertEquals(":value attribute value attribute:value", queue.toString());

        queue = new TokenQueue("attribute:value attribute value attribute:value");
        result = queue.consumeAttributeKey();
        Assert.assertEquals("value", result);
        Assert.assertEquals(" attribute attribute:value", queue.toString());

        queue = new TokenQueue("attribute:value attribute value attribute:value");
        result = queue.consumeAttributeKey();
        Assert.assertEquals("attribute:value", result);
        Assert.assertEquals(" value", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("some data");
        String result = queue.remainder();
        Assert.assertEquals("data", queue.toString());
        Assert.assertEquals(result, queue.toString());

        queue = new TokenQueue("some data");
        result = queue.consume('s');
        result = queue.consume('o');
        result = queue.consume('m');
        result = queue.consume('e');
        result = queue.consume(' ');
        result = queue.consume('d');
        result = queue.consume('a');
        Assert.assertEquals("", queue.toString());
        Assert.assertEquals(result, queue.toString());
    }
}