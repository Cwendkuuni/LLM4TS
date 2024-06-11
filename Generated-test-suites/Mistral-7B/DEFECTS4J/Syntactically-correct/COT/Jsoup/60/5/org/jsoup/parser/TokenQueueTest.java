package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setUp() {
 char[] data = {'<', 'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'd', ' ', 'd', 'a', 't', 'a', ' ', 'i', 's', ' ', 'a', ' ', 'T', 'o', 'k', 'e', 'n', 'Q', 'u', 'e', 'u', 'e', '(', ')', ' ', 'a', ' ', 's', 't', 'r', 'i', 'n', 'g', ' ', 'o', 'f', ' ', 'T', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 't', 'e', 's', 't', ' ', 'f', 'o', 'r', ' ', 'a', ' ', 'n', 'u', 'l', 'l', ' ', 'T', 'e', 's', 't', ' ', 'f', 'i', 'l', 'e', ' ', 'd', 'o', 'n', 'e', '.', '.'};
 tokenQueue = new TokenQueue(new String(data));
 }

 @Test
 public void testIsEmpty() {
 TokenQueue emptyQueue = new TokenQueue("");
 assertTrue(emptyQueue.isEmpty());
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 assertEquals('<', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue.addFirst("newData");
 assertEquals("newData" + tokenQueue.peek(), "newData<");
 }

 @Test
 public void testMatches() {
 tokenQueue.addFirst("hello world data is a TokenQueue(test)");
 assertTrue(tokenQueue.matches("hello"));
 assertTrue(tokenQueue.matches("data"));
 assertTrue(tokenQueue.matches("TokenQueue(test)"));
 assertFalse(tokenQueue.matches("isTest"));
 }

 @Test
 public void testMatchesCS() {
 tokenQueue.addFirst("Hello World Data Is A TokenQueue(test)");
 assertFalse(tokenQueue.matchesCS("hello"));
 assertTrue(tokenQueue.matchesCS("Hello"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue.addFirst("Hello World Data Is A TokenQueue(test)");
 assertTrue(tokenQueue.matchesAny("Hello", "World", "Data"));
 }

 @Test
 public void testMatchesAnyWithEmptyArray() {
 tokenQueue.addFirst("Hello World Data Is A TokenQueue(test)");
 assertFalse(tokenQueue.matchesAny(new String[]{}));
 }

 @Test
 public void testMatchesAnyWithNull() {
 tokenQueue.addFirst("Hello World Data Is A TokenQueue(test)");
 assertFalse(tokenQueue.matchesAny(null));
 }

 @Test
 public void testMatchesStartTag() {
 tokenQueue.addFirst("<tagName>");
 assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue.addFirst("Hello World Data Is A <tagName> TokenQueue(test)");
 tokenQueue.matchChomp("Hello World Data Is A ");
 assertEquals("<tagName> TokenQueue(test)", tokenQueue.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testMatchChompWithNoMatch() {
 tokenQueue.addFirst("Hello World Data Is A TokenQueue(test)");
 tokenQueue.matchChomp("notAMatch");
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue.addFirst(" Hello World Data Is A TokenQueue(test) ");
 assertTrue(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue.addFirst("Hello World Data Is A TokenQueue(test)");
 assertTrue(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 tokenQueue.addFirst("Hello World Data Is A TokenQueue(test)");
 tokenQueue.advance();
 assertEquals('H', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 tokenQueue.addFirst("Hello World Data Is A TokenQueue(test)");
 assertEquals('H', tokenQueue.consume());
 assertEquals('e', tokenQueue.peek());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeWithEmptyQueue() {
 tokenQueue.consume();
 }

 @Test
 public void testConsumeSequence() {
 tokenQueue.addFirst("Hello World Data Is A TokenQueue(test)");
 tokenQueue.consume("Hello World");
 assertEquals("Data Is A TokenQueue(test)", tokenQueue.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeSequenceWithNoMatch() {
 tokenQueue.addFirst("Hello World Data Is A TokenQueue(test)");
 tokenQueue.consume("NoMatch");
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeSequenceWithNotEnoughData() {
 tokenQueue.addFirst("Hello World Data Is A TokenQueue(test)");
 tokenQueue.consume("Hello World Data");
 }

 @Test
 public void testConsumeTo() {
 tokenQueue.addFirst("Hello World Data Is A <tagName> TokenQueue(test)");
 assertEquals("Hello World", tokenQueue.consumeTo("Hello World"));
 assertEquals("<tagName> TokenQueue(test)", tokenQueue.toString());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 tokenQueue.addFirst("Hello World Data Is A <tagName> TokenQueue(test)");
 assertEquals("Hello World", tokenQueue.consumeToIgnoreCase("Hello world"));
 assertEquals("<tagName> TokenQueue(test)", tokenQueue.toString());
 }

 @Test
 public void testConsumeToAny() {
 tokenQueue.addFirst("Hello World Data Is A <tagName> TokenQueue(test)");
 assertEquals("Hello World", tokenQueue.consumeToAny("Hello World", "Data"));
 assertEquals("<tagName> TokenQueue(test)", tokenQueue.toString());
 }

 @Test
 public void testChompBalanced() {
 tokenQueue.addFirst("(one (two) three) four");
 assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
 assertEquals(" four", tokenQueue.toString());
 }

 @Test
 public void testChompBalancedWithQuotes() {
 tokenQueue.addFirst("\"one (\"two\" three) three\" four");
 assertEquals("one \"two\" three", tokenQueue.chompBalanced('\"', "\""));
 assertEquals(" three\" four", tokenQueue.toString());
 }

 @Test
 public void testChompBalancedWithEscapes() {
 tokenQueue.addFirst("\\(one \\(two\\) three\\) four");
 assertEquals("one \\(two\\) three", tokenQueue.chompBalanced('\\(', '\\)'));
 assertEquals(" four", tokenQueue.toString());
 }

 @Test
 public void testUnescape() {
 assertEquals("\\(one \\(two\\) three\\) four", TokenQueue.unescape("\\(one \\(two\\) three\\) four"));
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue.addFirst(" Hello World Data Is A TokenQueue(test) ");
 assertTrue(tokenQueue.consumeWhitespace());
 assertEquals("Hello World Data Is A TokenQueue(test)", tokenQueue.toString());
 }

 @Test
 public void testConsumeWord() {
 tokenQueue.addFirst("Hello World Data Is A TokenQueue(test)");
 assertEquals("Word", tokenQueue.consumeWord());
 assertEquals("Data Is A TokenQueue(test)", tokenQueue.toString());
 }

 @Test
 public void testConsumeTagName() {
 tokenQueue.addFirst("<tagName attribute='value'>content</tagName>");
 assertEquals("tagName", tokenQueue.consumeTagName());
 assertEquals(" attribute='value'>content", tokenQueue.toString());
 }

 @Test
 public void testConsumeElementSelector() {
 tokenQueue.addFirst("div.class p#id > ul > li > a[href]");
 assertEquals("div.class", tokenQueue.consumeElementSelector());
 assertEquals("p#id > ul > li > a[href]", tokenQueue.toString());
 }

 @Test
 public void testConsumeCssIdentifier() {
 tokenQueue.addFirst("class:hover:active");
 assertEquals("class", tokenQueue.consumeCssIdentifier());
 assertEquals(":hover:active", tokenQueue.toString());
 }

 @Test
 public void testConsumeAttributeKey() {
 tokenQueue.addFirst("class='class1 class2' id='id1' style='color:red;'");
 assertEquals("class", tokenQueue.consumeAttributeKey());
 assertEquals("'class2' id='id1' style='color:red;'", tokenQueue.toString());
 }

 @Test
 public void testRemainder() {
 tokenQueue.addFirst("Hello World Data Is A TokenQueue(test)");
 assertEquals("Data Is A TokenQueue(test)", tokenQueue.toString());
 assertEquals("Data Is A TokenQueue(test)", tokenQueue.remainder());
 }
}