package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

 private TokenQueue queue;

 @Before
 public void setUp() {
 queue = new TokenQueue("abcdef<tag>123<tag2>456");
 }

 @Test
 public void testIsEmpty() {
 assertTrue(queue.isEmpty());
 queue.addFirst("a");
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testPeek() {
 assertEquals('a', queue.peek());
 queue.advance();
 assertEquals('b', queue.peek());
 }

 @Test
 public void testAddFirst() {
 queue.addFirst("x");
 assertEquals("x" + queue.toString().substring(1), queue.toString());
 }

 @Test
 public void testMatches() {
 assertTrue(queue.matches("ab"));
 assertFalse(queue.matches("ef"));
 assertFalse(queue.matches("xyz"));
 }

 @Test
 public void testMatchesCS() {
 assertTrue(queue.matchesCS("ab"));
 assertFalse(queue.matchesCS("ef"));
 assertFalse(queue.matchesCS("xyz"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(queue.matchesAny("ab", "ef"));
 assertFalse(queue.matchesAny("xyz", "ab"));
 }

 @Test
 public void testMatchesAnyChar() {
 assertTrue(queue.matchesAny('a', 'b'));
 assertFalse(queue.matchesAny('x', 'b'));
 }

 @Test
 public void testMatchesStartTag() {
 assertTrue(queue.matchesStartTag());
 assertFalse(queue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 assertTrue(queue.matchChomp("tag"));
 assertFalse(queue.matchChomp("tagg"));
 }

 @Test
 public void testMatchesWhitespace() {
 assertTrue(queue.matchesWhitespace());
 assertFalse(queue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 assertTrue(queue.matchesWord());
 assertFalse(queue.matchesWord());
 }

 @Test
 public void testAdvance() {
 queue.advance();
 assertEquals('b', queue.peek());
 }

 @Test
 public void testConsume() {
 assertEquals('a', queue.consume());
 assertEquals("bcdef<tag>123<tag2>456", queue.toString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("tag", queue.consumeTo("tag"));
 assertEquals("123<tag2>456", queue.consumeTo("tag2"));
 assertEquals("bcdef<tag>123", queue.consumeTo("tag2", "tag"));
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeToWithInvalidSequence() {
 queue.consumeTo("tag2");
 }

 @Test
 public void testChompBalanced() {
 assertEquals("one (two) three", queue.chompBalanced('(', ')'));
 assertEquals("", queue.chompBalanced('(', '('));
 }

 @Test
 public void testUnescape() {
 assertEquals("abcdef<tag>123<tag2>456", TokenQueue.unescape(queue.toString()));
 }
}