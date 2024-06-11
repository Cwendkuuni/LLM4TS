package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

 @Test
 public void testEmptyQueue() {
 TokenQueue queue = new TokenQueue("");
 assertTrue(queue.isEmpty());
 assertEquals("", queue.toString());
 }

 @Test
 public void testAddFirst() {
 TokenQueue queue = new TokenQueue("");
 queue.addFirst('a');
 assertEquals("a", queue.peek().toString());
 assertEquals("a", queue.consume().toString());
 }

 @Test
 public void testAddFirstString() {
 TokenQueue queue = new TokenQueue("");
 queue.addFirst("hello");
 assertEquals("hello", queue.peek());
 assertEquals("hello", queue.consumeToString());
 }

 @Test
 public void testMatches() {
 TokenQueue queue = new TokenQueue("abcd");
 assertTrue(queue.matches("bc"));
 assertFalse(queue.matches("ab"));
 }

 @Test
 public void testMatchesCS() {
 TokenQueue queue = new TokenQueue("ABCD");
 assertTrue(queue.matchesCS("BC"));
 assertFalse(queue.matchesCS("AB"));
 }

 @Test
 public void testMatchesAny() {
 TokenQueue queue = new TokenQueue("123abcd");
 assertTrue(queue.matchesAny("1", "2"));
 assertFalse(queue.matchesAny("4", "5"));
 }

 @Test
 public void testMatchesAnyChars() {
 TokenQueue queue = new TokenQueue("123abcd");
 assertTrue(queue.matchesAny('1', '2'));
 assertFalse(queue.matchesAny('4', '5'));
 }

 @Test
 public void testMatchesStartTag() {
 TokenQueue queue = new TokenQueue("<tag>123<tag2>");
 assertTrue(queue.matchesStartTag());
 assertFalse(queue.matchesStartTag()); // consume one char
 }

 @Test
 public void testMatchChomp() {
 TokenQueue queue = new TokenQueue("abcdabcd");
 assertTrue(queue.matchChomp("ab"));
 assertFalse(queue.matchChomp("cd"));
 }

 @Test
 public void testMatchesWhitespace() {
 TokenQueue queue = new TokenQueue("  abcd ");
 assertTrue(queue.matchesWhitespace());
 assertFalse(queue.matchesWord());
 }

 @Test
 public void testMatchesWord() {
 TokenQueue queue = new TokenQueue("abcd");
 assertTrue(queue.matchesWord());
 assertFalse(queue.matchesWhitespace());
 }

 @Test
 public void testAdvance() {
 TokenQueue queue = new TokenQueue("abcd");
 queue.advance();
 assertEquals('b', queue.peek());
 }

 @Test
 public void testConsume() {
 TokenQueue queue = new TokenQueue("abcd");
 assertEquals('a', queue.consume());
 }

 @Test
 public void testConsumeString() {
 TokenQueue queue = new TokenQueue("abcd");
 assertEquals("ab", queue.consumeToString("ab"));
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeToFailed() {
 TokenQueue queue = new TokenQueue("123456");
 queue.consumeTo("7");
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeToCaseInsensitiveFailed() {
 TokenQueue queue = new TokenQueue("AbCdEf");
 queue.consumeToIgnoreCase("ef");
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeToAnyFailed() {
 TokenQueue queue = new TokenQueue("123abcd");
 queue.consumeToAny("ef");
 }

 @Test
 public void testChompBalanced() {
 TokenQueue queue = new TokenQueue("(1(2)3)4");
 assertEquals("1(2)3", queue.chompBalanced('(', ')').toString());
 }

 @Test
 public void testUnescape() {
 assertEquals("ab\\cd\\ef\\gh", TokenQueue.unescape("ab\\cd\\ef\\gh"));
 }

 @Test
 public void testConsumeWhitespace() {
 TokenQueue queue = new TokenQueue("  abcd ");
 assertTrue(queue.consumeWhitespace());
 }

 @Test
 public void testConsumeWord() {
 TokenQueue queue = new TokenQueue("abcd");
 assertEquals("ab", queue.consumeWord());
 }

 @Test
 public void testConsumeTagName() {
 TokenQueue queue = new TokenQueue("tag tagName tag/tag");
 assertEquals("tagName", queue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 TokenQueue queue = new TokenQueue("tagName | tagName2 tagName3");
 assertEquals("tagName", queue.consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 TokenQueue queue = new TokenQueue("tagName class class1 class2 class3");
 assertEquals("class", queue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 TokenQueue queue = new TokenQueue("tagName attr attr1 attr2 attr3");
 assertEquals("attr1", queue.consumeAttributeKey());
 }

 @Test
 public void testRemainder() {
 TokenQueue queue = new TokenQueue("123abcd");
 assertEquals("cd", queue.remainder());
 }
}