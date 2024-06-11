package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

 @Test
 public void testEmptyQueue() {
 TokenQueue queue = new TokenQueue("");
 assertTrue(queue.isEmpty());
 }

 @Test
 public void testIsEmptyAfterConsumption() {
 TokenQueue queue = new TokenQueue("hello world");
 String consumed = queue.consume(5); // consume first 5 characters
 assertTrue(queue.isEmpty());
 assertEquals("hello ", queue.toString());
 }

 @Test
 public void testPeekAndConsumeFirstCharacter() {
 TokenQueue queue = new TokenQueue("hello world");
 assertEquals('h', queue.peek());
 assertEquals('h', queue.consume());
 }

 @Test
 public void testAddFirstCharacter() {
 TokenQueue queue = new TokenQueue("hello world");
 queue.addFirst('X');
 assertEquals('X', queue.consume());
 }

 @Test
 public void testAddFirstString() {
 TokenQueue queue = new TokenQueue("hello world");
 queue.addFirst("abc");
 assertEquals('a', queue.consume());
 assertEquals('b', queue.consume());
 assertEquals('c', queue.consume());
 }

 @Test
 public void testMatches() {
 TokenQueue queue = new TokenQueue("hello world");
 assertTrue(queue.matches("ell"));
 }

 @Test
 public void testMatchesCS() {
 TokenQueue queue = new TokenQueue("HELLO WORLD");
 assertTrue(queue.matchesCS("HELLO"));
 }

 @Test
 public void testMatchesAny() {
 TokenQueue queue = new TokenQueue("hello world");
 assertTrue(queue.matchesAny("el", "orl"));
 }

 @Test
 public void testMatchesStartTag() {
 TokenQueue queue = new TokenQueue("<html><body>");
 assertTrue(queue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 TokenQueue queue = new TokenQueue("hello <world>");
 assertTrue(queue.matchChomp("<world>"));
 }

 @Test
 public void testMatchesWhitespace() {
 TokenQueue queue = new TokenQueue(" hello world ");
 assertTrue(queue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 TokenQueue queue = new TokenQueue("hello world");
 assertTrue(queue.matchesWord());
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidConsumption() {
 TokenQueue queue = new TokenQueue("hello world");
 queue.consume("abc"); // should not accept a String argument
 }

 @Test
 public void testConsumeToIgnoreCase() {
 TokenQueue queue = new TokenQueue("abcDEFghiJKlMnOPQRST<abc>xyz");
 assertEquals("abc", queue.consumeToIgnoreCase("ABC"));
 }

 @Test
 public void testConsumeToAny() {
 TokenQueue queue = new TokenQueue("abcDEFghiJKlMnOPQRST<abc>xyz");
 assertEquals("abcDEF", queue.consumeToAny("abc", "DEF"));
 }

 @Test
 public void testChompBalanced() {
 TokenQueue queue = new TokenQueue("(one (two) three) four");
 assertEquals("one (two) three", queue.chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 assertEquals("\\<abc\\>", TokenQueue.unescape("\\<abc\\\\>"));
 }

 @Test
 public void testConsumeWhitespace() {
 TokenQueue queue = new TokenQueue(" hello world ");
 assertTrue(queue.consumeWhitespace());
 }

 @Test
 public void testConsumeWord() {
 TokenQueue queue = new TokenQueue("hello world");
 assertEquals("world", queue.consumeWord());
 }

 @Test
 public void testConsumeTagName() {
 TokenQueue queue = new TokenQueue("<p class=\"test\">This is a test.</p>");
 assertEquals("p", queue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() { {TokenQueue queue = new TokenQueue("div.class p.test"); assertEquals("div.class", queue.consumeElementSelector());}}

 @Test
 public void testConsumeCssIdentifier() {
 TokenQueue queue = new TokenQueue("id=\"test-id\" class=\"test\"");
 assertEquals("test-id", queue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 TokenQueue queue = new TokenQueue("class=\"test\" id=\"test-id\"");
 assertEquals("class", queue.consumeAttributeKey());
 }
}