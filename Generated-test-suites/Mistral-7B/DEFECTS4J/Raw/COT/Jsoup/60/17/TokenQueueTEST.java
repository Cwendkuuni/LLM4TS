package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setup() {
 char[] charArray = {'<', 't', 'o', 'k', 'e', 'n', 'Q', 'u', 'e', 'u', 'e', '(', '.', ')'};
 String stringData = new String(charArray);
 tokenQueue = new TokenQueue(stringData);
 }

 @Test
 public void testIsEmpty() {
 tokenQueue = new TokenQueue("");
 assertTrue(tokenQueue.isEmpty());
 tokenQueue = new TokenQueue("abc");
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 tokenQueue = new TokenQueue("<tokenQueue>");
 assertTrue(tokenQueue.isEmpty());
 tokenQueue = new TokenQueue("abctokenQueue");
 assertEquals('a', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue = new TokenQueue("abc");
 tokenQueue.addFirst('x');
 assertEquals("xabc", tokenQueue.toString());
 }

 @Test
 public void testMatches() {
 tokenQueue = new TokenQueue("abctokenQueue");
 assertTrue(tokenQueue.matches("abctokenQueue"));
 tokenQueue = new TokenQueue("abcd");
 assertFalse(tokenQueue.matches("abctokenQueue"));
 }

 @Test
 public void testMatchesCS() {
 tokenQueue = new TokenQueue("abctokenQueue");
 assertTrue(tokenQueue.matchesCS("abctokenQueue"));
 tokenQueue = new TokenQueue("ABCtokenQUEE");
 assertFalse(tokenQueue.matchesCS("abctokenQueue"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue = new TokenQueue("abctokenQueue");
 assertTrue(tokenQueue.matchesAny("ab", "ctokenQueue"));
 tokenQueue = new TokenQueue("abcd");
 assertFalse(tokenQueue.matchesAny("ab", "ctokenQueue"));
 tokenQueue = new TokenQueue("abctokenQueue");
 assertTrue(tokenQueue.matchesAny('a', 'b'));
 }

 @Test
 public void testMatchesStartTag() {
 tokenQueue = new TokenQueue("abctokenQueue");
 assertFalse(tokenQueue.matchesStartTag());
 tokenQueue = new TokenQueue("<a b>");
 assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue = new TokenQueue("abctokenQueue");
 assertFalse(tokenQueue.matchChomp("abctokenQueue"));
 tokenQueue = new TokenQueue("abctokenQueueabctokenQueue");
 tokenQueue.matchChomp("abctokenQueue");
 assertEquals("tokenQueue", tokenQueue.toString());
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue = new TokenQueue(" abctokenQueue ");
 assertTrue(tokenQueue.matchesWhitespace());
 tokenQueue = new TokenQueue("abctokenQueue");
 assertFalse(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue = new TokenQueue("abctokenQueue");
 assertTrue(tokenQueue.matchesWord());
 tokenQueue = new TokenQueue("123abctokenQueue");
 assertTrue(tokenQueue.matchesWord());
 tokenQueue = new TokenQueue("abctokenQueue123");
 assertTrue(tokenQueue.matchesWord());
 tokenQueue = new TokenQueue("abctokenQueue");
 assertFalse(tokenQueue.matchesWord());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeWithEmptyQueue() {
 tokenQueue.consume("abctokenQueue");
 }

 @Test
 public void testConsume() {
 tokenQueue = new TokenQueue("abctokenQueue");
 assertEquals("ctokenQueue", tokenQueue.consume("ab"));
 assertEquals("tokenQueue", tokenQueue.toString());
 }

 @Test
 public void testConsumeTo() {
 tokenQueue = new TokenQueue("(abctokenQueue)abcd");
 assertEquals("abctokenQue", tokenQueue.consumeTo("abctokenQueue"));
 assertEquals("cd", tokenQueue.toString());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 tokenQueue = new TokenQueue("(AbCtOkenQuEe)abcd");
 assertEquals("AbCtOkenQuEe", tokenQueue.consumeToIgnoreCase("abctokenqueue"));
 assertEquals("cd", tokenQueue.toString());
 }

 @Test
 public void testConsumeToAny() {
 tokenQueue = new TokenQueue("abctokenQueue123");
 assertEquals("tokenQueue123", tokenQueue.consumeToAny("token", "queue"));
 assertEquals("", tokenQueue.toString());
 }

 @Test
 public void testChompBalanced() {
 tokenQueue = new TokenQueue("(abctokenQueue(efg)hijk)lmnop");
 assertEquals("abctokenQueue(efg)hijk", tokenQueue.chompBalanced('(', ')').toString());
 assertEquals("lmnop", tokenQueue.toString());
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue = new TokenQueue(" abctokenQueue ");
 assertTrue(tokenQueue.consumeWhitespace());
 assertEquals("abctokenQueue", tokenQueue.toString());
 }

 @Test
 public void testConsumeWord() {
 tokenQueue = new TokenQueue("abctokenQueue");
 assertEquals("token", tokenQueue.consumeWord());
 assertEquals("Queue", tokenQueue.toString());
 }

 @Test
 public void testConsumeTagName() {
 tokenQueue = new TokenQueue("<tagName attr=\"value\" />");
 assertEquals("tagName", tokenQueue.consumeTagName());
 assertEquals("", tokenQueue.toString());
 }

 @Test
 public void testConsumeElementSelector() {
 tokenQueue = new TokenQueue("div.class:hover");
 assertEquals("div", tokenQueue.consumeElementSelector());
 assertEquals("class:hover", tokenQueue.toString());
 }

 @Test
 public void testConsumeCssIdentifier() {
 tokenQueue = new TokenQueue(".class");
 assertEquals("class", tokenQueue.consumeCssIdentifier());
 assertEquals("", tokenQueue.toString());
 }

 @Test
 public void testConsumeAttributeKey() {
 tokenQueue = new TokenQueue("data-attr=\"value\"");
 assertEquals("data-attr", tokenQueue.consumeAttributeKey());
 assertEquals("value", tokenQueue.toString());
 }

 @Test
 public void testRemainder() {
 tokenQueue = new TokenQueue("abctokenQueue");
 assertEquals("", tokenQueue.remainder());
 tokenQueue = new TokenQueue("abctokenQueue123");
 assertEquals("123", tokenQueue.remainder());
 }

 @Test
 public void testToString() {
 tokenQueue = new TokenQueue("abctokenQueue");
 assertEquals("abctokenQueue", tokenQueue.toString());
 }
}