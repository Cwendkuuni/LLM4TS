package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

 private TokenQueue queue;
 private static final String TEST_DATA = "abcdef<x>123<abc>456";

 @Before
 public void setUp() {
 queue = new TokenQueue(TEST_DATA);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(queue.isEmpty());

 queue.addFirst('a');
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testPeek() {
 assertEquals('a', queue.peek());

 queue.addFirst('b');
 assertEquals('b', queue.peek());
 }

 @Test
 public void testAddFirst() {
 queue.addFirst("abcd");
 assertEquals("abcd", queue.toString());
 }

 @Test
 public void testMatches() {
 assertTrue(queue.matches("a"));
 assertFalse(queue.matches("z"));

 queue.advance();
 assertTrue(queue.matches("b"));
 }

 @Test
 public void testMatchesCS() {
 assertFalse(queue.matchesCS("aB")); // case sensitive
 assertFalse(queue.matchesCS("Ab")); // case sensitive

 assertTrue(queue.matchesCS("a")); // case sensitive
 assertTrue(queue.matchesCS("A")); // case sensitive
 }

 @Test
 public void testMatchesAny() {
 assertTrue(queue.matchesAny("a", "b"));
 assertTrue(queue.matchesAny("b", "a"));

 assertTrue(queue.matchesAny("a", "b", "c"));
 assertFalse(queue.matchesAny("z", "b", "c"));
 }

 @Test
 public void testMatchesAnyChar() {
 assertTrue(queue.matchesAny('a', 'b'));
 assertTrue(queue.matchesAny('b', 'a'));

 assertTrue(queue.matchesAny('a', 'b', 'c'));
 assertFalse(queue.matchesAny('z', 'b', 'c'));
 }

 @Test
 public void testMatchesStartTag() {
 assertTrue(queue.matchesStartTag());
 assertFalse(queue.matchesStartTag()); // no tag name
 }

 @Test
 public void testMatchChomp() {
 assertEquals("x", queue.matchChomp("<x>"));
 assertFalse(queue.matches("x")); // no match
 }

 @Test
 public void testMatchesWhitespace() {
 assertTrue(queue.matchesWhitespace());
 assertFalse(queue.matchesWhitespace()); // no whitespace
 }

 @Test
 public void testMatchesWord() {
 assertTrue(queue.matchesWord());
 assertFalse(queue.matchesWord()); // non-word character
 }

 @Test
 public void testAdvance() {
 queue.advance();
 assertEquals('b', queue.peek());

 queue.advance();
 assertEquals('c', queue.peek());
 }

 @Test
 public void testConsume() {
 assertEquals('a', queue.consume());
 assertEquals('b', queue.consume());

 assertEquals("cdef<", queue.toString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("abc", queue.consumeTo("bc"));
 assertEquals("def<", queue.toString());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 assertEquals("abc", queue.consumeToIgnoreCase("Bc")); // case insensitive match
 assertEquals("def<", queue.toString());
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("abc", queue.consumeToAny("a", "b", "c"));
 assertEquals("def<", queue.toString());
 }

 @Test
 public void testChompBalanced() {
 assertEquals("abcdef", queue.chompBalanced('(', ')'));
 assertEquals("", queue.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testChompBalancedInvalid() {
 queue.chompBalanced('(', '}'); // unbalanced close bracket
 }

 @Test
 public void testUnescape() {
 assertEquals("abcdef<x>123<abc>456", TokenQueue.unescape(TEST_DATA));
 }

 @Test
 public void testConsumeWhitespace() {
 assertTrue(queue.consumeWhitespace());
 assertFalse(queue.consumeWhitespace()); // no more whitespace
 }

 @Test
 public void testConsumeWord() {
 assertEquals("def", queue.consumeWord());
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testConsumeTagName() {
 assertEquals("x", queue.consumeTagName());
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testConsumeElementSelector() {
 assertEquals("abc", queue.consumeElementSelector());
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testConsumeCssIdentifier() {
 assertEquals("abc", queue.consumeCssIdentifier());
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testConsumeAttributeKey() {
 assertEquals("class", queue.consumeAttributeKey());
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testRemainder() {
 assertEquals("", queue.remainder()); // empty queue
 assertEquals("456", queue.remainder());
 }
}