package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * TokenQueue test cases.
 */
public class TokenQueueTest {

    @Test
    public void testConstructor() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals(data, tokenQueue.queue);
    }

    @Test
    public void testIsEmpty() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.isEmpty());

        data = "testdata";
        tokenQueue = new TokenQueue(data);
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals(data.charAt(0), tokenQueue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekEmpty() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.peek();
    }

    @Test
    public void testAddFirst() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.addFirst('X');
        assertEquals('X', tokenQueue.peek());
        
        tokenQueue.addFirst("test");
        assertEquals("test", tokenQueue.toString());
        
        tokenQueue.addFirst("123");
        assertEquals("123test", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "est";
        assertTrue(tokenQueue.matches(seq));
        
        seq = "test";
        assertTrue(tokenQueue.matches(seq));
        
        seq = "testdata";
        assertTrue(tokenQueue.matches(seq));
        
        seq = "data";
        assertFalse(tokenQueue.matches(seq));
    }

    @Test
    public void testMatchesCS() {
        String data = "TestData";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "Test";
        assertTrue(tokenQueue.matchesCS(seq));
        
        seq = "Data";
        assertFalse(tokenQueue.matchesCS(seq));
    }

    @Test
    public void testMatchesAny() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        String[] seq = {"test", "data"};
        assertTrue(tokenQueue.matchesAny(seq));
        
        seq = new String[]{"test", "data1"};
        assertFalse(tokenQueue.matchesAny(seq));
    }

    @Test
    public void testMatchesAnyChar() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        char[] seq = {'t', 'd'};
        assertTrue(tokenQueue.matchesAny(seq));
        
        seq = new char[]{'t', 'e'};
        assertFalse(tokenQueue.matchesAny(seq));
    }

    @Test
    public void testMatchesStartTag() {
        String data = "<test>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesStartTag());

        data = "test<";
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "est";
        tokenQueue.matchChomp(seq);
        assertEquals("testdata".substring(seq.length()), tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        String data = " test data ";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWhitespace());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.advance();
        assertEquals('t', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals('t', tokenQueue.consume());
        assertEquals("estdata", tokenQueue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmpty() {
        String data = "";
        TokenQueue tokenQueue = new TokenQueue(data);
        tokenQueue.consume();
    }

    @Test
    public void testConsumeSequence() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "est";
        tokenQueue.consume(seq);
        assertEquals("testdata".substring(seq.length()), tokenQueue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeSequenceMismatch() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "tes";
        tokenQueue.consume(seq);
    }

    @Test
    public void testConsumeSequenceNotLongEnough()
    {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "test";
        tokenQueue.consume(seq);
        assertEquals("data", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "est";
        assertEquals("test", tokenQueue.consumeTo(seq).toString());
        assertEquals("data", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String data = "TESTDATA";
        TokenQueue tokenQueue = new TokenQueue(data);
        String seq = "test";
        assertEquals("Test", tokenQueue.consumeToIgnoreCase(seq).toString());
        assertEquals("DATA", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        String[] seq = {"test", "data"};
        assertEquals("test", tokenQueue.consumeToAny(seq).toString());
        assertEquals("data", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        String data = "(test(one (two) three) four)";
        TokenQueue tokenQueue = new TokenQueue(data);
        char open = '(';
        char close = ')';
        assertEquals("testone (two) three", tokenQueue.chompBalanced(open, close).toString());
    }

    @Test
    public void testChompBalancedInvalidOpen() {
        String data = "(test(one (two) three) four)";
        TokenQueue tokenQueue = new TokenQueue(data);
        char open = '{';
        char close = ')';
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testChompBalancedInvalidClose() {
        String data = "(test(one (two) three) four)";
        TokenQueue tokenQueue = new TokenQueue(data);
        char open = '(';
        char close = '}';
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testChompBalancedQuotedOpen() {
        String data = "(test(one ('(two)') three) four)";
        TokenQueue tokenQueue = new TokenQueue(data);
        char open = '(';
        char close = ')';
        assertEquals("testone ('(two)') three", tokenQueue.chompBalanced(open, close).toString());
    }

    @Test
    public void testChompBalancedQuotedClose() {
        String data = "(test(one (two)' three) four)";
        TokenQueue tokenQueue = new TokenQueue(data);
        char open = '(';
        char close = ')';
        assertEquals("testone (two) three", tokenQueue.chompBalanced(open, close).toString());
    }

    @Test
    public void testChompBalancedEscapedOpen() {
        String data = "(test(one (\\(two\\)) three) four)";
        TokenQueue tokenQueue = new TokenQueue(data);
        char open = '(';
        char close = ')';
        assertEquals("testone (\\(two\\)) three", tokenQueue.chompBalanced(open, close).toString());
    }

    @Test
    public void testChompBalancedEscapedClose() {
        String data = "(test(one (two)\\) three) four)";
        TokenQueue tokenQueue = new TokenQueue(data);
        char open = '(';
        char close = ')';
        assertEquals("testone (two) three", tokenQueue.chompBalanced(open, close).toString());
    }

    @Test
    public void testUnescape() {
        String data = "\\(test\\)";
        String input = tokenQueue.unescape(data);
        assertEquals("(test)", input);
    }

    @Test
    public void testConsumeWhitespace() {
        String data = " test data ";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals("testdata", tokenQueue.toString());
    }

    @Test
    public void testConsumeWord() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("test", tokenQueue.consumeWord().toString());
        assertEquals("data", tokenQueue.toString());
    }

    @Test
    public void testConsumeTagName() {
        String data = "<testtag>";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("testtag", tokenQueue.consumeTagName().toString());
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        String data = "test:tag test2:tag2 test3-tag3";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("test:tag", tokenQueue.consumeElementSelector().toString());
        assertEquals("test2:tag2 test3-tag3", tokenQueue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String data = "test-identifier test2-identifier test3_identifier";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("test-identifier", tokenQueue.consumeCssIdentifier().toString());
        assertEquals("test2-identifier test3_identifier", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        String data = "test-attribute test2-attribute test3-attribute class test3-class";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("test-attribute", tokenQueue.consumeAttributeKey().toString());
        assertEquals("test2-attribute test3-class", tokenQueue.toString());
    }

    @Test
    public void testRemainder() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("data", tokenQueue.remainder().toString());
    }

    @Test
    public void testToString() {
        String data = "testdata";
        TokenQueue tokenQueue = new TokenQueue(data);
        assertEquals("data", tokenQueue.toString());
    }

}