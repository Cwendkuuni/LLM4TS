package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setUp() {
 String data = "abc<def>ghi<jkl>mno";
 tokenQueue = new TokenQueue(data);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(tokenQueue.isEmpty());
 tokenQueue.addFirst("a");
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 assertEquals('a', tokenQueue.peek());
 tokenQueue.addFirst('b');
 assertEquals('b', tokenQueue.peek());
 tokenQueue.consume();
 assertEquals('b', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue.addFirst("def");
 assertEquals("defabc<def>ghi<jkl>mno", tokenQueue.toString());
 }

 @Test
 public void testMatches() {
 assertTrue(tokenQueue.matches("abc"));
 tokenQueue.addFirst("def");
 assertFalse(tokenQueue.matches("abc"));
 tokenQueue.addFirst("abc");
 assertTrue(tokenQueue.matches("abc"));
 }

 @Test
 public void testMatchesCS() {
 assertTrue(tokenQueue.matchesCS("abc"));
 tokenQueue.addFirst("DEF");
 assertFalse(tokenQueue.matchesCS("abc"));
 tokenQueue.addFirst("abc");
 assertTrue(tokenQueue.matchesCS("abc"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue.addFirst("def");
 assertTrue(tokenQueue.matchesAny("ef", "ab"));
 assertFalse(tokenQueue.matchesAny("xyz"));
 }

 @Test
 public void testMatchesAnyChars() {
 tokenQueue.addFirst("def");
 assertTrue(tokenQueue.matchesAny(new char[]{'e', 'f'}));
 assertFalse(tokenQueue.matchesAny(new char[]{'x', 'y'}));
 }

 @Test
 public void testMatchesStartTag() {
 assertTrue(tokenQueue.matchesStartTag());
 tokenQueue.consume();
 assertFalse(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue.addFirst("abc<def>");
 assertTrue(tokenQueue.matchChomp("abc"));
 assertEquals("def<def>ghi<jkl>mno", tokenQueue.toString());
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue.addFirst(" abc ");
 assertTrue(tokenQueue.matchesWhitespace());
 tokenQueue.consume();
 assertFalse(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue.addFirst("abcdef");
 assertTrue(tokenQueue.matchesWord());
 tokenQueue.consume();
 assertFalse(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 tokenQueue.addFirst("abcdef");
 tokenQueue.advance();
 assertEquals('b', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 tokenQueue.addFirst("abcdef");
 assertEquals('a', tokenQueue.consume());
 assertEquals('b', tokenQueue.peek());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeFailMatch() {
 tokenQueue.addFirst("abcdef");
 tokenQueue.consume("xyz");
 }

 @Test
 public void testConsumeTo() {
 tokenQueue.addFirst("abc<def>ghi<jkl>mno");
 assertEquals("def", tokenQueue.consumeTo("def"));
 assertEquals("ghi<jkl>mno", tokenQueue.toString());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 tokenQueue.addFirst("abc<Def>ghi<jkl>mno");
 assertEquals("Def", tokenQueue.consumeToIgnoreCase("DEF"));
 assertEquals("ghi<jkl>mno", tokenQueue.toString());
 }

 @Test
 public void testConsumeToAny() {
 tokenQueue.addFirst("abc<def>ghi<jkl>mno");
 assertEquals("def", tokenQueue.consumeToAny("ef", "ab"));
 assertEquals("ghi<jkl>mno", tokenQueue.toString());
 }

 @Test
 public void testChompBalanced() {
 tokenQueue.addFirst("(abc(def)(ghi))(jkl)mno");
 assertEquals("abc(def)(ghi)", tokenQueue.chompBalanced('(', ')'));
 assertEquals("jklmno", tokenQueue.toString());
 }

 @Test
 public void testUnescape() {
 assertEquals("abc\\def\\ghi\\jkl\\mno", TokenQueue.unescape("abc\\def\\ghi\\jkl\\mno"));
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue.addFirst("  abc  def  ");
 assertTrue(tokenQueue.consumeWhitespace());
 assertEquals("abcdef", tokenQueue.toString());
 }

 @Test
 public void testConsumeWord() {
 tokenQueue.addFirst("abcdefghij");
 assertEquals("word", tokenQueue.consumeWord());
 assertEquals("defghij", tokenQueue.toString());
 }

 @Test
 public void testConsumeTagName() {
 tokenQueue.addFirst("abc<def>ghi<jkl>mno");
 assertEquals("def", tokenQueue.consumeTagName());
 assertEquals("ghi<jkl>mno", tokenQueue.toString());
 }

 @Test
 public void testConsumeElementSelector() {
 tokenQueue.addFirst("abc<def>ghi<jkl>mno");
 assertEquals("def", tokenQueue.consumeElementSelector());
 assertEquals("ghi<jkl>mno", tokenQueue.toString());
 }

 @Test
 public void testConsumeCssIdentifier() {
 tokenQueue.addFirst("abc<def>ghi<jkl>mno");
 assertEquals("def", tokenQueue.consumeCssIdentifier());
 assertEquals("ghi<jkl>mno", tokenQueue.toString());
 }

 @Test
 public void testConsumeAttributeKey() {
 tokenQueue.addFirst("abc<def>ghi<jkl>mno");
 assertEquals("def", tokenQueue.consumeAttributeKey());
 assertEquals("ghi<jkl>mno", tokenQueue.toString());
 }

 @Test
 public void testRemainder() {
 tokenQueue.addFirst("abc<def>ghi<jkl>mno");
 assertEquals("mno", tokenQueue.remainder());
 }
}