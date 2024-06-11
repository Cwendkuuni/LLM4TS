package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Test
 public void testConstructor() {
 tokenQueue = new TokenQueue("abcdef");
 assertNotNull(tokenQueue);
 }

 @Test
 public void testIsEmpty() {
 tokenQueue = new TokenQueue("");
 assertTrue(tokenQueue.isEmpty());
 tokenQueue = new TokenQueue("abcdef");
 assertFalse(tokenQueue.isEmpty());
 tokenQueue.advance();
 tokenQueue.consume();
 tokenQueue.consume();
 assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 tokenQueue = new TokenQueue("abcdef");
 assertEquals('a', tokenQueue.peek());
 tokenQueue.advance();
 assertEquals('b', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue = new TokenQueue("abcdef");
 tokenQueue.addFirst('g');
 assertEquals('g', tokenQueue.peek());
 tokenQueue.addFirst("hij");
 assertEquals('h', tokenQueue.peek());
 }

 @Test
 public void testMatches() {
 tokenQueue = new TokenQueue("abcdef");
 assertTrue(tokenQueue.matches("ab"));
 assertFalse(tokenQueue.matches("efg"));
 tokenQueue.consume();
 tokenQueue.consume();
 assertTrue(tokenQueue.matches("ef"));
 }

 @Test
 public void testMatchesCS() {
 tokenQueue = new TokenQueue("ABCDEF");
 assertTrue(tokenQueue.matchesCS("ab"));
 assertFalse(tokenQueue.matchesCS("efg"));
 tokenQueue.consume();
 tokenQueue.consume();
 assertTrue(tokenQueue.matchesCS("ef"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue = new TokenQueue("abcdef");
 tokenQueue.matchesAny("abcd", 'e', "ef");
 assertFalse(tokenQueue.isEmpty());
 assertEquals('a', tokenQueue.peek());
 tokenQueue.advance();
 assertEquals('b', tokenQueue.peek());
 tokenQueue.advance();
 assertEquals('c', tokenQueue.peek());
 tokenQueue.advance();
 assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testMatchesStartTag() {
 tokenQueue = new TokenQueue("<abcdef>");
 assertTrue(tokenQueue.matchesStartTag());
 tokenQueue.consume();
 assertFalse(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue = new TokenQueue("ab<cd>ef");
 assertTrue(tokenQueue.matchChomp("ab"));
 assertEquals('c', tokenQueue.peek());
 assertTrue(tokenQueue.matchChomp("cd"));
 assertEquals('e', tokenQueue.peek());
 assertFalse(tokenQueue.matchChomp("ef"));
 assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue = new TokenQueue(" \t\n abcdef ");
 assertTrue(tokenQueue.matchesWhitespace());
 tokenQueue.consumeWhitespace();
 tokenQueue.consumeWhitespace();
 tokenQueue.consumeWhitespace();
 assertFalse(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue = new TokenQueue("abcdEf");
 assertTrue(tokenQueue.matchesWord());
 tokenQueue.consume();
 assertFalse(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 tokenQueue = new TokenQueue("abcdef");
 tokenQueue.advance();
 assertEquals('b', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 tokenQueue = new TokenQueue("abcdef");
 assertEquals('a', tokenQueue.consume());
 assertEquals('b', tokenQueue.peek());
 }

 @Test
 public void testConsumeCS() {
 tokenQueue = new TokenQueue("ABCDEF");
 assertEquals('A', tokenQueue.consumeCS());
 assertEquals('B', tokenQueue.peekCS());
 }

 @Test
 public void testConsumeTo() {
 tokenQueue = new TokenQueue("(one (two) three) four");
 assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')').toString());
 assertEquals(" four", tokenQueue.remainder());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeToInvalidSequence() {
 tokenQueue = new TokenQueue("(one (two) three) four");
 tokenQueue.consume("five");
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeToEmptyQueue() {
 tokenQueue = new TokenQueue("");
 tokenQueue.consumeTo("abc");
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeToUnknownSequence() {
 tokenQueue = new TokenQueue("(one (two) three) four");
 tokenQueue.consumeTo("xyz");
 }

 @Test
 public void testConsumeToAny() {
 tokenQueue = new TokenQueue("abxyzcdab");
 assertEquals("ab", tokenQueue.consumeToAny("ab").toString());
 assertEquals("xyz", tokenQueue.consumeToAny("cd", "ef").toString());
 assertEquals("cd", tokenQueue.consumeToAny("cd").toString());
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testConsumeToAnyEmptyQueue() {
 tokenQueue = new TokenQueue("");
 assertEquals("", tokenQueue.consumeToAny("ab").toString());
 }

 @Test
 public void testConsumeToAnyUnknownSequence() {
 tokenQueue = new TokenQueue("abxyzcdab");
 assertEquals("", tokenQueue.consumeToAny("pqr").toString());
 }
}