package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

/**
 * Test class for TokenQueue.
 */
public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setup() {
 tokenQueue = new TokenQueue("test<tag>test test<tag2>test test<tag3>test");
 }

 @Test
 public void testIsEmpty() {
 TokenQueue emptyTokenQueue = new TokenQueue("");
 assertTrue(emptyTokenQueue.isEmpty());
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 assertEquals('t', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue.addFirst('X');
 assertEquals('X', tokenQueue.peek());
 }

 @Test
 public void testMatches() {
 assertTrue(tokenQueue.matches("test"));
 }

 @Test
 public void testMatchesCS() {
 assertTrue(tokenQueue.matchesCS("tag"));
 }

 @Test
 public void testMatchesAny() {
 String[] seq = {"test", "tag"};
 assertTrue(tokenQueue.matchesAny(seq));
 }

 @Test
 public void testMatchesAnyChar() {
 char[] seq = {'t', 'a'};
 assertTrue(tokenQueue.matchesAny(seq));
 }

 @Test
 public void testMatchesStartTag() {
 assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 String seq = "test";
 assertTrue(tokenQueue.matchChomp(seq));
 assertFalse(tokenQueue.matches(seq));
 }

 @Test
 public void testMatchesWhitespace() {
 assertTrue(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 assertTrue(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 tokenQueue.advance();
 assertEquals('e', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 assertEquals('t', tokenQueue.consume());
 }

 @Test
 public void testConsumeSequence() {
 String seq = "test";
 tokenQueue.consume(seq);
 assertEquals("test", tokenQueue.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeNoMatch() {
 tokenQueue.consume("tag");
 fail();
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeNotLongEnough() {
 tokenQueue.consume("tagtest");
 fail();
 }

 @Test
 public void testConsumeTo() {
 String seq = "test";
 String consumed = tokenQueue.consumeTo(seq);
 assertEquals("test", consumed);
 assertEquals("test", tokenQueue.toString());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 String seq = "Test";
 String consumed = tokenQueue.consumeToIgnoreCase(seq);
 assertEquals("test", consumed);
 assertEquals("test", tokenQueue.toString());
 }

 @Test
 public void testConsumeToAny() {
 String[] seq = {"test", "tag"};
 String consumed = tokenQueue.consumeToAny(seq);
 assertEquals("test", consumed);
 assertEquals("tag", tokenQueue.peek());
 }

 @Test
 public void testChompTo() {
 String seq = "test";
 String consumed = tokenQueue.chompTo(seq);
 assertEquals("test", consumed);
 assertEquals("tag", tokenQueue.peek());
 }

 @Test
 public void testChompToIgnoreCase() {
 String seq = "Test";
 String consumed = tokenQueue.chompToIgnoreCase(seq);
 assertEquals("test", consumed);
 assertEquals("tag", tokenQueue.peek());
 }

 @Test
 public void testChompBalanced() {
 char open = '<';
 char close = '>';
 String consumed = tokenQueue.chompBalanced(open, close);
 assertEquals("test<tag>", consumed);
 assertEquals("test", tokenQueue.peek());
 }

 @Test
 public void testUnescape() {
 String escaped = "\\<test\\>";
 String unescaped = TokenQueue.unescape(escaped);
 assertEquals("test", unescaped);
 }

 @Test
 public void testConsumeWhitespace() {
 assertTrue(tokenQueue.consumeWhitespace());
 assertFalse(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testConsumeWord() {
 String consumed = tokenQueue.consumeWord();
 assertEquals("tag", consumed);
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testConsumeTagName() {
 String consumed = tokenQueue.consumeTagName();
 assertEquals("tag", consumed);
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testConsumeElementSelector() {
 String consumed = tokenQueue.consumeElementSelector();
 assertEquals("tag", consumed);
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testConsumeCssIdentifier() {
 String consumed = tokenQueue.consumeCssIdentifier();
 assertEquals("tag", consumed);
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testConsumeAttributeKey() {
 String consumed = tokenQueue.consumeAttributeKey();
 assertEquals("tag", consumed);
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testRemainder() {
 String remainder = tokenQueue.remainder();
 assertEquals("", remainder);
 tokenQueue.addFirst('X');
 remainder = tokenQueue.remainder();
 assertEquals("X", remainder);
 }

 @Test
 public void testToString() {
 assertEquals("tag", tokenQueue.toString());
 }
}