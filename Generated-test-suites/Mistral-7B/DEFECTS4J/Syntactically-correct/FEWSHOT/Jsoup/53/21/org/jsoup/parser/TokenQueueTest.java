package org.jsoup.parser;

import org.junit. Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenQueueTest {

 @Test
 public void testIsEmpty() {
 TokenQueue queue = new TokenQueue("");
 assertTrue(queue.isEmpty());
 }

 @Test
 public void testNotEmpty() {
 TokenQueue queue = new TokenQueue("abc");
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testPeek() {
 TokenQueue queue = new TokenQueue("abc");
 assertEquals('a', queue.peek());
 }

 @Test
 public void testPeekEmpty() {
 TokenQueue queue = new TokenQueue("");
 assertEquals((char) 0, queue.peek());
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
 TokenQueue queue = new TokenQueue("abcd");
 assertTrue(queue.matches("ab"));
 assertTrue(queue.matches("cd"));
 assertFalse(queue.matches("ef"));
 }

 @Test
 public void testMatchesCS() {
 TokenQueue queue = new TokenQueue("ABCD");
 assertTrue(queue.matchesCS("ab"));
 assertFalse(queue.matchesCS("cd"));
 assertFalse(queue.matchesCS("ef"));
 }

 @Test
 public void testMatchesAny() {
 TokenQueue queue = new TokenQueue("abcd");
 assertTrue(queue.matchesAny("ab", "cd"));
 assertTrue(queue.matchesAny("ad"));
 assertFalse(queue.matchesAny("ef"));
 }

 @Test
 public void testMatchesAnyChars() {
 TokenQueue queue = new TokenQueue("abcd");
 assertTrue(queue.matchesAny('a', 'b', 'c', 'd'));
 }

 @Test
 public void testMatchesStartTag() {
 TokenQueue queue = new TokenQueue("<abc>");
 assertTrue(queue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 TokenQueue queue = new TokenQueue("abcdef");
 assertTrue(queue.matchChomp("ab"));
 assertEquals("cdef", queue.toString());
 }

 @Test
 public void testMatchesWhitespace() {
 TokenQueue queue = new TokenQueue(" abc");
 assertTrue(queue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 TokenQueue queue = new TokenQueue("abcdef");
 assertTrue(queue.matchesWord());
 }

 @Test
 public void testAdvance() {
 TokenQueue queue = new TokenQueue("abc");
 queue.advance();
 assertEquals('b', queue.peek());
 }

 @Test
 public void testConsume() {
 TokenQueue queue = new TokenQueue("abc");
 assertEquals('a', queue.consume());
 assertEquals('b', queue.peek());
 }

 @Test
 public void testConsumeEmpty() {
 TokenQueue queue = new TokenQueue("");
 assertNull(queue.consume());
 }

 @Test
 public void testConsumeSequence() {
 TokenQueue queue = new TokenQueue("abcdef");
 assertEquals("ab", queue.consume(2));
 assertEquals("cdef", queue.toString());
 }

 @Test
 public void testConsumeTo() {
 TokenQueue queue = new TokenQueue("abcdefghij");
 assertEquals("bcdefghi", queue.consumeTo("ghi"));
 assertEquals("j", queue.peek());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 TokenQueue queue = new TokenQueue("AbCdEfGhIjKlMnOpQrStUvWxYz");
 assertEquals("AbCdEf", queue.consumeToIgnoreCase("def"));
 assertEquals("GhIjKlMnOpQrStUvWxY", queue.toString());
 }

 @Test
 public void testConsumeToAny() {
 TokenQueue queue = new TokenQueue("abcdefghij");
 assertEquals("bcd", queue.consumeToAny("ab", "cd"));
 assertEquals("efghij", queue.toString());
 }

 @Test
 public void testChompBalanced() {
 TokenQueue queue = new TokenQueue("((())())((()))");
 assertEquals("((()))((()))", queue.chompBalanced('(', ')'));
 assertEquals("", queue.toString());
 }

 @Test
 public void testUnescape() {
 assertEquals("\\\\abc\\\\def\\\\ghi", TokenQueue.unescape("\\abc\\def\\\\ghi"));
 }

 @Test
 public void testConsumeWhitespace() {
 TokenQueue queue = new TokenQueue(" abc\t def\n ghi ");
 assertTrue(queue.consumeWhitespace());
 }

 @Test
 public void testConsumeWord() {
 TokenQueue queue = new TokenQueue("abcdefghij");
 assertEquals("word", queue.consumeWord());
 }

 @Test
 public void testConsumeTagName() {
 TokenQueue queue = new TokenQueue("abc<tag>def");
 assertEquals("tag", queue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 TokenQueue queue = new TokenQueue("abc<tag name|attr name>def");
 assertEquals("tag name", queue.consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 TokenQueue queue = new TokenQueue("abc#identifier def");
 assertEquals("identifier", queue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 TokenQueue queue = new TokenQueue("abc[attribute key] def");
 assertEquals("attribute key", queue.consumeAttributeKey());
 }

 @Test
 public void testRemainder() {
 TokenQueue queue = new TokenQueue("abcdef");
 assertEquals("def", queue.remainder());
 }

}