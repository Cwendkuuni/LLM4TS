package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @SuppressWarnings("deprecation")
 @Before
 public void setup() {
 tokenQueue = new TokenQueue("abcdef<xyz>123ABC");
 }

 @Test
 public void testIsEmpty() {
 TokenQueue emptyQueue = new TokenQueue("");
 assertTrue(emptyQueue.isEmpty());

 tokenQueue = new TokenQueue(" ");
 assertTrue(tokenQueue.isEmpty());

 tokenQueue = new TokenQueue("abc");
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 assertEquals('a', tokenQueue.peek());
 tokenQueue.consume();
 assertEquals('b', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue.addFirst('x');
 assertEquals('x', tokenQueue.peek());
 }

 @Test
 public void testMatches() {
 assertTrue(tokenQueue.matches("ab"));
 tokenQueue.consume();
 tokenQueue.consume();
 assertFalse(tokenQueue.matches("ab"));
 }

 @Test
 public void testMatchesCS() {
 assertTrue(tokenQueue.matchesCS("AB"));
 assertFalse(tokenQueue.matchesCS("ab"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue.addFirst('[');
 assertTrue(tokenQueue.matchesAny("[]"));
 tokenQueue.consume();
 assertFalse(tokenQueue.matchesAny("[]"));
 }

 @Test
 public void testMatchesAnyChar() {
 tokenQueue.addFirst('1');
 assertTrue(tokenQueue.matchesAny(new char[]{'1'}));
 tokenQueue.consume();
 assertFalse(tokenQueue.matchesAny(new char[]{'1'}));
 }

 @Test
 public void testMatchesStartTag() {
 assertTrue(tokenQueue.matchesStartTag());
 tokenQueue.consume();
 assertFalse(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue.addFirst('<');
 tokenQueue.addFirst('x');
 assertTrue(tokenQueue.matchChomp("xyz>"));
 assertEquals("xyz", tokenQueue.remainder());
 }

 @Test
 public void testMatchesWhitespace() {
 assertTrue(tokenQueue.matchesWhitespace());
 tokenQueue.consumeWhitespace();
 assertTrue(tokenQueue.matchesWhitespace());
 tokenQueue.consume();
 assertFalse(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 assertTrue(tokenQueue.matchesWord());
 tokenQueue.consumeWord();
 assertFalse(tokenQueue.matchesWord());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeWithNoMatch() {
 tokenQueue.consume("xyz");
 }

 @Test
 public void testConsumeTo() {
 assertEquals("abc", tokenQueue.consumeTo("bc"));
 assertEquals("def", tokenQueue.consumeTo("ef"));
 assertEquals("ABC", tokenQueue.consumeTo("ABC"));
 assertEquals("123", tokenQueue.consumeTo("123"));
 }

 @Test
 public void testConsumeToIgnoreCase() {
 assertEquals("abc", tokenQueue.consumeToIgnoreCase("bc"));
 assertEquals("def", tokenQueue.consumeToIgnoreCase("ef"));
 assertEquals("ABC", tokenQueue.consumeToIgnoreCase("ABC"));
 assertEquals("123", tokenQueue.consumeToIgnoreCase("123"));
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("xyz", tokenQueue.consumeToAny("xy", "zx"));
 }

 @Test
 public void testChompBalanced() {
 assertEquals("abc (def)", tokenQueue.chompBalanced('(', ')'));
 tokenQueue.consume();
 assertEquals("abc (def", tokenQueue.chompBalanced('(', ')'));
 tokenQueue.consume();
 assertEquals("def", tokenQueue.chompBalanced(')', ')'));
 tokenQueue.consume();
 assertEquals("", tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 assertEquals("abc\\def", TokenQueue.unescape("abc\\def"));
 }

 @Test
 public void testConsumeWhitespace() {
 assertTrue(tokenQueue.consumeWhitespace());
 tokenQueue.consume();
 assertFalse(tokenQueue.consumeWhitespace());
 }

 @Test
 public void testConsumeWord() {
 assertEquals("abc", tokenQueue.consumeWord());
 tokenQueue.consumeWord();
 assertEquals("def", tokenQueue.consumeWord());
 }

 @Test
 public void testConsumeTagName() {
 assertEquals("tagName", tokenQueue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 assertEquals("tagName|namespace", tokenQueue.consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 assertEquals("identifier", tokenQueue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 assertEquals("attributeKey", tokenQueue.consumeAttributeKey());
 }

 @Test
 public void testRemainder() {
 assertEquals("", tokenQueue.remainder());
 }

 @Test
 public void testToString() {
 assertEquals("", tokenQueue.toString());
 }
}