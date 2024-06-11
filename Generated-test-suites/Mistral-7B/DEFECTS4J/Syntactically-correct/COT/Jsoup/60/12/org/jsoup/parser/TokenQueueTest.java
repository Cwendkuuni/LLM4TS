package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Test
 public void testConstructor() {
 tokenQueue = new TokenQueue("test string");
 assertEquals("test string", tokenQueue.queue);
 }

 @Test
 public void testIsEmpty() {
 tokenQueue = new TokenQueue("test string");
 assertFalse(tokenQueue.isEmpty());
 tokenQueue.advance();
 assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 tokenQueue = new TokenQueue("test string");
 assertEquals('t', tokenQueue.peek());
 tokenQueue.advance();
 assertEquals('e', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue = new TokenQueue("test string");
 tokenQueue.addFirst('A');
 assertEquals('A', tokenQueue.peek());
 }

 @Test
 public void testMatches() {
 tokenQueue = new TokenQueue("test string");
 tokenQueue.advance();
 assertTrue(tokenQueue.matches("est"));
 }

 @Test
 public void testMatchesCS() {
 tokenQueue = new TokenQueue("test String");
 tokenQueue.advance();
 assertTrue(tokenQueue.matchesCS("est"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue = new TokenQueue("test string");
 tokenQueue.advance();
 tokenQueue.advance();
 assertTrue(tokenQueue.matchesAny("es", "t"));
 }

 @Test
 public void testMatchesAnyChar() {
 tokenQueue = new TokenQueue("test string");
 tokenQueue.advance();
 tokenQueue.advance();
 assertTrue(tokenQueue.matchesAny(new char[]{'s', 't'}));
 }

 @Test
 public void testMatchesStartTag() {
 tokenQueue = new TokenQueue("<test>");
 assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue = new TokenQueue("test<string>");
 assertFalse(tokenQueue.matchChomp("test"));
 assertTrue(tokenQueue.matchChomp("string"));
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue = new TokenQueue(" test string");
 assertTrue(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue = new TokenQueue("testWord");
 assertTrue(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 tokenQueue = new TokenQueue("test string");
 tokenQueue.advance();
 tokenQueue.advance();
 assertEquals('s', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 tokenQueue = new TokenQueue("test string");
 assertEquals('t', tokenQueue.consume());
 assertEquals('e', tokenQueue.consume());
 }

 @Test
 public void testConsumeTo() {
 tokenQueue = new TokenQueue("test<string>");
 assertEquals("test", tokenQueue.consumeTo("string"));
 }

 @Test
 public void testConsumeToIgnoreCase() {
 tokenQueue = new TokenQueue("test<STRING>");
 assertEquals("test", tokenQueue.consumeToIgnoreCase("string"));
 }

 @Test
 public void testConsumeToAny() {
 tokenQueue = new TokenQueue("test<string1>test<string2>");
 assertEquals("string1", tokenQueue.consumeToAny("string1", "string2"));
 }

 @Test
 public void testChompBalanced() {
 tokenQueue = new TokenQueue("(test (one (two) three) four)");
 assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeToWithNoMatch() {
 tokenQueue = new TokenQueue("test string");
 tokenQueue.consumeTo("test2");
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeToWithInsufficientData() {
 tokenQueue = new TokenQueue("test string");
 tokenQueue.consume(new String[]{"test"});
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeToAnyWithNoMatch() {
 tokenQueue = new TokenQueue("test string");
 tokenQueue.consumeToAny(new String[]{"test2", "test3"});
 }
}