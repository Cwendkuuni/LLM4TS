package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 tokenQueue = new TokenQueue("abcdefg<xyz>1234_abcd:efghijk-lmnop");
 }

 // Test for isEmpty() method
 @Test
 public void testIsEmpty() {
 assertFalse(tokenQueue.isEmpty());
 tokenQueue.consumeAll();
 assertTrue(tokenQueue.isEmpty());
 }

 // Test for remainingLength() method
 @Test
 public void testRemainingLength() {
 assertEquals(10, tokenQueue.remainingLength());
 assertEquals(0, tokenQueue.remainingLength());
 }

 // Test for peek() method
 @Test
 public void testPeek() {
 assertEquals('a', tokenQueue.peek());
 assertEquals('x', tokenQueue.peek(6));
 }

 // Test for addFirst() method
 @Test
 public void testAddFirst() {
 tokenQueue.addFirst('i');
 assertEquals('i', tokenQueue.consume().toString());
 }

 // Test for matches() method
 @Test
 public void testMatches() {
 assertTrue(tokenQueue.matches("ab"));
 assertTrue(tokenQueue.matches("def"));
 assertFalse(tokenQueue.matches("xyz"));
 assertFalse(tokenQueue.matches("123"));
 }

 // Test for matchesCS() method
 @Test
 public void testMatchesCS() {
 assertTrue(tokenQueue.matchesCS("xyz"));
 assertFalse(tokenQueue.matchesCS("efg"));
 }

 // Test for matchesAny() method
 @Test
 public void testMatchesAny() {
 assertTrue(tokenQueue.matchesAny("abcd"));
 assertFalse(tokenQueue.matchesAny("mno"));
 }

 // Test for matchesAny(char[]) method
 @Test
 public void testMatchesAnyChars() {
 assertTrue(tokenQueue.matchesAny(new char[]{'a', 'b'}));
 assertFalse(tokenQueue.matchesAny(new char[]{'m', 'n'}));
 }

 // Test for matchesStartTag() method
 @Test
 public void testMatchesStartTag() {
 assertTrue(tokenQueue.matchesStartTag());
 assertFalse(tokenQueue.matchesStartTag());
 }

 // Test for matchChomp() method
 @Test
 public void testMatchChomp() {
 tokenQueue.addFirst("abc");
 assertTrue(tokenQueue.matchChomp("abc"));
 }

 // Test for matchesWhitespace() method
 @Test
 public void testMatchesWhitespace() {
 assertTrue(tokenQueue.matchesWhitespace());
 assertFalse(tokenQueue.matchesWhitespace());
 }

 // Test for matchesWord() method
 @Test
 public void testMatchesWord() {
 assertTrue(tokenQueue.matchesWord());
 assertFalse(tokenQueue.matchesWord());
 }

 // Test for advance() method
 @Test
 public void testAdvance() {
 tokenQueue.advance();
 assertEquals('b', tokenQueue.consume().toString());
 }

 // Test for consume() method
 @Test
 public void testConsume() {
 assertEquals('a', tokenQueue.consume().toString());
 }

 // Test for consume(String) method
 @Test
 public void testConsumeString() {
 tokenQueue.addFirst("abcd");
 tokenQueue.consume("ab");
 assertEquals("cd", tokenQueue.toString());
 }

 // Test for consumeTo() method
 @Test
 public void testConsumeTo() {
 assertEquals("abc", tokenQueue.consumeTo("abc"));
 assertEquals("defghijk-lmnop", tokenQueue.toString());
 }

 // Test for consumeToIgnoreCase() method
 @Test
 public void testConsumeToIgnoreCase() {
 assertEquals("xyz", tokenQueue.consumeToIgnoreCase("XYZ"));
 }

 // Test for consumeToAny(String[]) method
 @Test
 public void testConsumeToAnyStrings() {
 tokenQueue.consumeToAny(new String[]{"def", "ghijk"});
 assertEquals("lmnop", tokenQueue.toString());
 }

 // Test for consumeToAny(char[]) method
 @Test
 public void testConsumeToAnyChars() {
 tokenQueue.consumeToAny(new char[]{'g', 'h'});
 assertEquals("ijklmnop", tokenQueue.toString());
 }

 // Test for chompBalanced(char, char) method
 @Test
 public void testChompBalanced() {
 assertEquals("abcdefg", tokenQueue.chompBalanced('(', ')'));
 }

 // Test for unescape(String) method
 @Test
 public void testUnescape() {
 assertEquals("\\(one \\(two\\) three\\)", TokenQueue.unescape("(one (two) three)\\"));
 }

 // Test for consumeWhitespace() method
 @Test
 public void testConsumeWhitespace() {
 assertTrue(tokenQueue.consumeWhitespace());
 assertFalse(tokenQueue.consumeWhitespace());
 }

 // Test for consumeWord() method
 @Test
 public void testConsumeWord() {
 assertEquals("abcd", tokenQueue.consumeWord());
 }

 // Test for consumeTagName() method
 @Test
 public void testConsumeTagName() { TestconsumeTagName(); }

 // Test for consumeElementSelector() method
 @Test
 public void testConsumeElementSelector() { TestconsumeElementSelector(); }

 // Test for consumeCssIdentifier() method
 @Test
 public void testConsumeCssIdentifier() { TestconsumeCssIdentifier(); }

 // Test for consumeAttributeKey() method
 @Test
 public void testConsumeAttributeKey() { TestconsumeAttributeKey(); }

 // Test for remainder() method
 @Test
 public void testRemainder() {
 assertEquals("ijklmnop", tokenQueue.remainder());
 }

 // Test for toString() method
 @Test
 public void testToString() {
 assertEquals("ijklmnop", tokenQueue.toString());
 }

}