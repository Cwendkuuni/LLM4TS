package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testTokenQueueCreation() {
        String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Test Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"test-id\">Test Content</div>\n" +
                "</body>\n" +
                "</html>";

        TokenQueue queue = new TokenQueue(data);
        assertFalse("Queue should not be empty", queue.isEmpty());
        assertEquals("Data should match constructed queue", data, queue.toString());
    }

    @Test
    public void testTokenQueueIsEmpty() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        assertTrue("Queue should be empty", queue.isEmpty());
    }

    @Test
    public void testPeekAndConsume() {
        String data = "<html><head><title>Test Title</title></head><body><div id=\"test-id\">Test Content</div></body></html>";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("Peek should match first character in queue", '<', queue.peek());
        consume(queue);
        assertEquals("Consume should match peeked character", '<', queue.peek());
        consume(queue);
        assertEquals("Consume should match second character in queue", 'h', queue.peek());
    }

    @Test
    public void testAddFirst() {
        String data = "Test Data";
        TokenQueue queue = new TokenQueue(data);
        queue.addFirst('[');
        queue.addFirst("Test");
        assertEquals("Added elements should match queue content", "[Test]Test Data", queue.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFirstNull() {
        String data = "Test Data";
        TokenQueue queue = new TokenQueue(data);
        queue.addFirst(null);
    }

    @Test
    public void testMatches() {
        String data = "Test Data";
        TokenQueue queue = new TokenQueue(data);
        assertTrue("Queue should match empty string", queue.matches(""));
        assertTrue("Queue should match itself", queue.matches(data.substring(0, queue.remainingLength())));
        assertFalse("Queue should not match non-matching string", queue.matches("Test"));
        queue.advance();
        assertFalse("Queue should not match empty string after advancing", queue.matches(""));
    }

    @Test
    public void testMatchesCS() {
        String data = "Test Data";
        TokenQueue queue = new TokenQueue(data);
        assertTrue("Queue should match itself case sensitive", queue.matchesCS(data.substring(0, queue.remainingLength())));
        assertFalse("Queue should not match non-matching string case sensitive", queue.matchesCS("Test"));
        queue.advance();
        assertFalse("Queue should not match itself case sensitive after advancing", queue.matchesCS(data.substring(0, queue.remainingLength())));
    }

    @Test
    public void testMatchesAny() {
        String data = "Test Data";
        TokenQueue queue = new TokenQueue(data);
        assertTrue("Queue should match empty array", queue.matchesAny());
        assertTrue("Queue should match itself", queue.matchesAny(data.substring(0, queue.remainingLength()).toCharArray()));
        assertTrue("Queue should match a single character", queue.matchesAny('T', 'e', 's', 't'));
        assertFalse("Queue should not match non-matching characters", queue.matchesAny('F', 'a', 'l', 's', 'e'));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<html><head><title>Test Title</title></head><body><div id=\"test-id\">Test Content</div></body></html>";
        TokenQueue queue = new TokenQueue(data);
        assertTrue("Queue should match '<h'", queue.matchesStartTag());
        assertFalse("Queue should not match '/h'", queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        String data = "<html><head><title>Test Title</title></head><body><div id=\"test-id\">Test Content</div></body></html>";
        TokenQueue queue = new TokenQueue(data);
        assertTrue("Queue should match '<html>' and consume it", queue.matchChomp("<html>"));
        assertFalse("Queue should not match '/html'", queue.matchChomp("/html"));
    }

    @Test
    public void testMatchAny() {
        String data = "Test Data";
        TokenQueue queue = new TokenQueue(data);
        assertTrue("Queue should match any of empty array", queue.matchesAny());
        assertTrue("Queue should match itself", queue.matchesAny(data.substring(0, queue.remainingLength()).toCharArray()));
        assertTrue("Queue should match a single character", queue.matchesAny('T', 'e', 's', 't'));
        assertFalse("Queue should not match non-matching characters", queue.matchesAny('F', 'a', 'l', 's', 'e'));
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " Test Data ";
        TokenQueue queue = new TokenQueue(data);
        assertTrue("Queue should match whitespace", queue.matchesWhitespace());
        assertFalse("Queue should not match non-whitespace", queue.matchesWhitespace());
        queue.advance();
        assertFalse("Queue should not match whitespace after advancing", queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "Test Data";
        TokenQueue queue = new TokenQueue(data);
        assertTrue("Queue should match a word character", queue.matchesWord());
        assertFalse("Queue should not match a non-word character", queue.matchesWord());
        queue.advance();
        assertFalse("Queue should not match a word character after advancing", queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "Test Data";
        TokenQueue queue = new TokenQueue(data);
        queue.advance();
        assertFalse("Queue should not match itself after advancing", queue.matches(data.substring(0, queue.pos)));
        assertFalse("Queue should not match empty string after advancing", queue.matches(""));
        assertEquals("Queue should have advanced one character", 1, queue.remainingLength());
    }

    @Test
    public void testConsume() {
        String data = "Test Data";
        TokenQueue queue = new TokenQueue(data);
        String consumed = queue.consume(2);
        assertEquals("Consumed characters should match the specified length", 2, consumed.length());
        assertEquals("Consumed characters should match the slice of the queue", data.substring(queue.pos - 2, queue.pos), consumed);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        String data = "";
        TokenQueue queue = new TokenQueue(data);
        queue.consume(1);
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeLongerQueueThanSpecified() {
        String data = "Test Data";
        TokenQueue queue = new TokenQueue(data);
        queue.consume(data.length());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNonMatchingSequence() {
        String data = "Test Data";
        TokenQueue queue = new TokenQueue(data);
        queue.consume("NonMatchingSequence");
    }

    @Test
    public void testConsumeTo() {
        String data = "<html><head><title>Test Title</title></head><body><div id=\"test-id\">Test Content</div></body></html>";
        TokenQueue queue = new TokenQueue(data);
        String consumed = queue.consumeTo("body");
        assertEquals("Consumed sequence should match expected", "html<head><title>Test Title</title></head>", queue.toString());
        assertEquals("Consumed data should match expected", "body<div id=\"test-id\">Test Content</div>", consumed);
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "<html><head><title>Test Title</title></head><body><div id=\"test-id\">Test Content</div></body></html>";
        TokenQueue queue = new TokenQueue(data);
        String consumed = queue.consumeToIgnoreCase("BODY");
        assertEquals("Consumed sequence should match expected", "html<head><title>Test Title</title></head>", queue.toString());
        assertEquals("Consumed data should match expected", "body<div id=\"test-id\">Test Content</div>", consumed);
    }

    @Test
    public void testConsumeToAny() {
        String data = "Test Data";
        TokenQueue queue = new TokenQueue(data);
        String consumed = queue.consumeToAny("Test", "Data");
        assertEquals("Consumed data should match expected", "Data", consumed);
        assertEquals("Queue should match remaining data after consuming", "Data", queue.toString().substring(queue.pos));
    }

    @Test
    public void testChompBalanced() {
        String data = "(Test (Data) Test) Data";
        TokenQueue queue = new TokenQueue(data);
        String chomp = queue.chompBalanced('(', ')');
        assertEquals("Balanced string should be chomped", "(Test (Data) Test)", queue.toString());
        assertEquals("Chomped string should match expected", "Test (Data) Test", chomp);
    }

    @Test
    public void testUnescape() {
        String data = "\\Test\\ Data";
        String unescaped = TokenQueue.unescape(data);
        assertEquals("Unescaped string should match expected", "Test Data", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = " Test Data ";
        TokenQueue queue = new TokenQueue(data);
        assertTrue("Queue should consume whitespace", queue.consumeWhitespace());
        assertFalse("Queue should not match whitespace after consuming", queue.matchesWhitespace());
    }

    @Test
    public void testConsumeWord() {
        String data = "Test Data";
        TokenQueue queue = new TokenQueue(data);
        String consumed = queue.consumeWord();
        assertEquals("Consumed word should match expected", "Test", consumed);
        assertFalse("Queue should not match word after consuming", queue.matchesWord());
    }

    @Test
    public void testConsumeTagName() {
        String data = "<html><head><title>Test Title</title></head><body><div id=\"test-id\">Test Content</div></body></html>";
        TokenQueue queue = new TokenQueue(data);
        String consumed = queue.consumeTagName();
        assertEquals("Consumed tag name should match expected", "html", consumed);
        assertEquals("Queue should match remaining data after consuming", "<head><title>Test Title</title></head><body><div id=\"test-id\">Test Content</div>", queue.toString().substring(queue.pos));
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "<html><head><title>Test Title</title><link rel=\"stylesheet\" href=\"styles.css\"/></head><body><div id=\"test-id\" class=\"test-class\">Test Content</div></body></html>";
        TokenQueue queue = new TokenQueue(data);
        String consumed = queue.consumeElementSelector();
        assertEquals("Consumed element selector should match expected", "link", consumed);
        assertEquals("Queue should match remaining data after consuming", "<head><title>Test Title</title><body><div id=\"test-id\" class=\"test-class\">Test Content</div>", queue.toString().substring(queue.pos));
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "<style>\n" +
                ".test-class {\n" +
                " color: red;\n" +
                "}\n" +
                "</style>";
        TokenQueue queue = new TokenQueue(data);
        String consumed = queue.consumeCssIdentifier();
        assertEquals("Consumed CSS identifier should match expected", "test-class", consumed);
        assertEquals("Queue should match remaining data after consuming", "", queue.toString().substring(queue.pos));
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "<html>\n" +
                "<body>\n" +
                "<div id=\"test-id\" class=\"test-class\" title=\"Test Title\">Test Content</div>\n" +
                "</body>\n" +
                "</html>";
        TokenQueue queue = new TokenQueue(data);
        String consumed = queue.consumeAttributeKey();
        assertEquals("Consumed attribute key should match expected", "title", consumed);
        assertEquals("Queue should match remaining data after consuming", "", queue.toString().substring(queue.pos));
    }

    @Test
    public void testRemainder() {
        String data = "Test Data";
        TokenQueue queue = new TokenQueue(data);
        assertEquals("Queue remainder should match expected", "Data", queue.remainder());
        assertEquals("Queue should be empty after consuming remainder", true, queue.isEmpty());
    }
}