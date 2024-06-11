package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertMatchesRegularExpression;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testNewInstance() {
        TokenQueue testQueue = new TokenQueue("test");
        assertEquals("test", testQueue.queue);
        assertEquals(0, testQueue.pos);
        assertFalse(testQueue.isEmpty());
    }

    @Test
    public void testIsEmpty() {
        TokenQueue testQueue = new TokenQueue("");
        assertTrue(testQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue testQueue = new TokenQueue("test");
        assertEquals('t', testQueue.peek());
        TokenQueue testQueue2 = new TokenQueue("test");
        testQueue2.advance();
        assertEquals('e', testQueue2.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue testQueue = new TokenQueue("test");
        testQueue.addFirst('a');
        assertEquals('a', testQueue.queue.charAt(0));
        assertEquals('t', testQueue.queue.charAt(1));
        assertEquals('e', testQueue.queue.charAt(2));
        assertEquals('s', testQueue.queue.charAt(3));
        assertEquals('t', testQueue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue testQueue = new TokenQueue("test");
        assertTrue(testQueue.matches("t"));
        assertTrue(testQueue.matches("es"));
        assertFalse(testQueue.matches("a"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue testQueue = new TokenQueue("test");
        assertTrue(testQueue.matchesCS("t"));
        assertFalse(testQueue.matchesCS("T"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue testQueue = new TokenQueue("test");
        assertTrue(testQueue.matchesAny("t", "s"));
        assertFalse(testQueue.matchesAny("x", "s"));
        assertFalse(testQueue.matchesAny("x", "x"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue testQueue = new TokenQueue("test");
        assertTrue(testQueue.matchesAny(new char[]{'t', 's'}));
        assertFalse(testQueue.matchesAny(new char[]{'x', 's'}));
        assertFalse(testQueue.matchesAny(new char[]{'x', 'x'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue testQueue = new TokenQueue("<test>");
        assertTrue(testQueue.matchesStartTag());
        TokenQueue testQueue2 = new TokenQueue("test");
        assertFalse(testQueue2.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue testQueue = new TokenQueue("test<test>");
        assertTrue(testQueue.matchChomp("test"));
        assertEquals(">", testQueue.queue.substring(0, testQueue.pos));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue testQueue = new TokenQueue(" test ");
        assertTrue(testQueue.matchesWhitespace());
        TokenQueue testQueue2 = new TokenQueue("test");
        assertFalse(testQueue2.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue testQueue = new TokenQueue("test");
        assertTrue(testQueue.matchesWord());
        TokenQueue testQueue2 = new TokenQueue("123");
        assertFalse(testQueue2.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue testQueue = new TokenQueue("test");
        testQueue.advance();
        assertEquals('e', testQueue.queue.charAt(0));
        assertEquals('s', testQueue.queue.charAt(1));
        assertEquals('t', testQueue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue testQueue = new TokenQueue("test");
        assertEquals('t', testQueue.consume());
        assertEquals('e', testQueue.consume());
        assertEquals('s', testQueue.consume());
        assertEquals('', testQueue.consume());
        assertTrue(testQueue.isEmpty());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue testQueue = new TokenQueue("test<test>");
        assertEquals("test", testQueue.consumeTo("test"));
        assertEquals(">", testQueue.queue.substring(0, testQueue.pos));
        assertEquals("", testQueue.consumeTo(""));
        assertEquals("", testQueue.consumeTo("test"));
        assertTrue(testQueue.isEmpty());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue testQueue = new TokenQueue("TeSt<TeSt>");
        assertEquals("test", testQueue.consumeToIgnoreCase("test"));
        assertEquals(">", testQueue.queue.substring(0, testQueue.pos));
        assertEquals("", testQueue.consumeToIgnoreCase(""));
        assertEquals("", testQueue.consumeToIgnoreCase("Test"));
        assertTrue(testQueue.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue testQueue = new TokenQueue("test<test>");
        assertEquals("test", testQueue.consumeToAny("test"));
        assertEquals(">", testQueue.queue.substring(0, testQueue.pos));
        assertEquals("", testQueue.consumeToAny(""));
        assertEquals("", testQueue.consumeToAny("test2"));
        assertTrue(testQueue.isEmpty());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue testQueue = new TokenQueue("(test)");
        assertEquals("test", testQueue.chompBalanced('(', ')'));
        assertEquals("", testQueue.queue.substring(0, testQueue.pos));
        TokenQueue testQueue2 = new TokenQueue("(test(test)test)");
        assertEquals("test(test)", testQueue2.chompBalanced('(', ')'));
        assertEquals("test", testQueue2.chompBalanced('(', 't'));
        assertEquals("testtest", testQueue2.queue.substring(0, testQueue2.pos));
    }

    @Test
    public void testUnescape() {
        assertEquals("\\\\test", TokenQueue.unescape("test\\\\test"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue testQueue = new TokenQueue(" test ");
        assertTrue(testQueue.consumeWhitespace());
        assertEquals("", testQueue.consumeWhitespace());
        assertEquals("", testQueue.consumeWhitespace());
        assertTrue(testQueue.isEmpty());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue testQueue = new TokenQueue("testword");
        assertEquals("test", testQueue.consumeWord());
        assertEquals("word", testQueue.consumeWord());
        assertEquals("", testQueue.consumeWord());
        assertTrue(testQueue.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue testQueue = new TokenQueue("test<tag>");
        assertEquals("tag", testQueue.consumeTagName());
        assertEquals("", testQueue.queue.substring(0, testQueue.pos));
        assertTrue(testQueue.isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue testQueue = new TokenQueue("test<tag:class>");
        assertEquals("tag:class", testQueue.consumeElementSelector());
        assertEquals("", testQueue.queue.substring(0, testQueue.pos));
        assertTrue(testQueue.isEmpty());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue testQueue = new TokenQueue("test#ident");
        assertEquals("ident", testQueue.consumeCssIdentifier());
        assertEquals("", testQueue.queue.substring(0, testQueue.pos));
        assertTrue(testQueue.isEmpty());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue testQueue = new TokenQueue("test attribute=\"key\"");
        assertEquals("key", testQueue.consumeAttributeKey());
        assertEquals("", testQueue.queue.substring(0, testQueue.pos));
        assertTrue(testQueue.isEmpty());
    }

    @Test
    public void testRemainder() {
        TokenQueue testQueue = new TokenQueue("test");
        assertEquals("", testQueue.remainder());
        TokenQueue testQueue2 = new TokenQueue("testremainder");
        assertEquals("remainder", testQueue2.remainder());
    }

    @Test
    public void testIllegalStateExceptionWhenConsumingUnmatchedSequence() {
        TokenQueue testQueue = new TokenQueue("test");
        String expectedSequence = "test2";
        assertThrows(IllegalStateException.class, () -> testQueue.consume(expectedSequence));
    }

    @Test
    public void testIllegalStateExceptionWhenConsumingSequenceLongerThanQueue() {
        TokenQueue testQueue = new TokenQueue("test");
        String expectedSequence = "testtesttesttest";
        assertThrows(IllegalStateException.class, () -> testQueue.consume(expectedSequence));
    }

    @Test
    public void testIllegalStateExceptionWhenConsumingUnmatchedCharacter() {
        TokenQueue testQueue = new TokenQueue("test");
        char expectedCharacter = 'x';
        assertThrows(IllegalStateException.class, () -> testQueue.consume(Character.toString(expectedCharacter)));
    }
}