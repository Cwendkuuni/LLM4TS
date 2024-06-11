package org.jsoup.parser.test;

import static org.junit.Assert.*;

import org.jsoup.parser.TokenQueue;

import org.junit.Test;

/**
 * Test class for TokenQueue.
 */
public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setUp() {
 String data = "abcdef<xyz>";
 tokenQueue = new TokenQueue(data);
 }

 @Test
 public void testIsEmpty() {
 TokenQueue emptyQueue = new TokenQueue("");
 assertTrue(emptyQueue.isEmpty());
 tokenQueue = new TokenQueue("abcdef<xyz>");
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 assertEquals('a', tokenQueue.peek());
 }

 @Test(expected = IllegalStateException.class)
 public void testPeekEmptyQueue() {
 TokenQueue emptyQueue = new TokenQueue("");
 emptyQueue.peek();
 }

 @Test
 public void testAddFirst() {
 tokenQueue.addFirst('z');
 assertEquals('z', tokenQueue.peek());
 }

 @Test
 public void testAddFirstString() {
 tokenQueue.addFirst("abc");
 assertEquals('a', tokenQueue.peek());
 }

 @Test
 public void testMatches() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('b');
 tokenQueue.addFirst('c');
 assertTrue(tokenQueue.matches("abc"));
 }

 @Test
 public void testMatchesCS() {
 tokenQueue.addFirst('A');
 tokenQueue.addFirst('b');
 tokenQueue.addFirst('c');
 assertFalse(tokenQueue.matchesCS("abc"));
 tokenQueue.addFirst('A');
 assertTrue(tokenQueue.matchesCS("Abc"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('b');
 tokenQueue.addFirst('c');
 tokenQueue.addFirst('d');
 assertTrue(tokenQueue.matchesAny("abcd"));
 }

 @Test
 public void testMatchesAnyChar() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('b');
 tokenQueue.addFirst('c');
 tokenQueue.addFirst('d');
 assertTrue(tokenQueue.matchesAny(new char[]{'a', 'b'}));
 }

 @Test
 public void testMatchesStartTag() {
 tokenQueue.addFirst('<');
 tokenQueue.addFirst('x');
 assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue.addFirst("abcdef<xyz>");
 assertTrue(tokenQueue.matchChomp("xyz"));
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue.addFirst(' ');
 assertTrue(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue.addFirst('a');
 assertTrue(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('b');
 tokenQueue.advance();
 assertEquals('b', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('b');
 assertEquals('a', tokenQueue.consume());
 assertEquals('b', tokenQueue.peek());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeEmptyQueue() {
 TokenQueue emptyQueue = new TokenQueue("");
 emptyQueue.consume();
 }

 @Test
 public void testConsumeTo() {
 tokenQueue.addFirst("abcdef<xyz>");
 assertEquals("abc", tokenQueue.consumeTo("bc"));
 assertEquals("def<xyz>", tokenQueue.toString());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 tokenQueue.addFirst("ABCDEF<xyz>");
 assertEquals("Abc", tokenQueue.consumeToIgnoreCase("bc"));
 assertEquals("DEF<xyz>", tokenQueue.toString());
 }

 @Test
 public void testConsumeToAny() {
 tokenQueue.addFirst("abcdef<xyz>");
 assertEquals("xyz", tokenQueue.consumeToAny("xy", "z"));
 assertEquals("abcdef", tokenQueue.toString());
 }

 @Test
 public void testChompBalanced() {
 tokenQueue.addFirst("(one (two) three) four");
 assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
 assertEquals(" four", tokenQueue.toString());
 }

 @Test
 public void testUnescape() {
 String escaped = "\\(\\)one\\ \\(two\\ \\)three\\";
 assertEquals("(one (two) three)", TokenQueue.unescape(escaped));
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue.addFirst(" \t\r\n abc ");
 assertTrue(tokenQueue.consumeWhitespace());
 assertEquals("abc", tokenQueue.toString());
 }

 @Test
 public void testConsumeWord() {
 tokenQueue.addFirst("abcd efgh ijkl");
 assertEquals("abcd", tokenQueue.consumeWord());
 assertEquals("efgh ijkl", tokenQueue.toString());
 }

 @Test
 public void testConsumeTagName() {
 tokenQueue.addFirst("tagName attributeName=\"value\" tagName attributeName2=\"value2\"");
 assertEquals("tagName", tokenQueue.consumeTagName());
 assertEquals("attributeName=\"value\" tagName attributeName2=\"value2\"", tokenQueue.toString());
 }

 @Test
 public void testConsumeElementSelector() {
 tokenQueue.addFirst("div.class1 span.class2:hover");
 assertEquals("div.class1", tokenQueue.consumeElementSelector());
 assertEquals("span.class2:hover", tokenQueue.toString());
 }

 @Test
 public void testConsumeCssIdentifier() {
 tokenQueue.addFirst("id1.class1 class2_id2");
 assertEquals("id1", tokenQueue.consumeCssIdentifier());
 assertEquals("class1 class2_id2", tokenQueue.toString());
 }

 @Test
 public void testConsumeAttributeKey() {
 tokenQueue.addFirst("name=\"value\" name2=\"value2\" class=\"class1\"");
 assertEquals("name", tokenQueue.consumeAttributeKey());
 assertEquals("name2=\"value2\" class=\"class1\"", tokenQueue.toString());
 }

 @Test
 public void testRemainder() {
 tokenQueue.addFirst("abcdef<xyz>");
 assertEquals("def<xyz>", tokenQueue.remainder());
 }

 @Test
 public void testToString() {
 tokenQueue.addFirst("abcdef<xyz>");
 assertEquals("def<xyz>", tokenQueue.toString().substring(tokenQueue.pos));
 }
}