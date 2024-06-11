package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokenQueueTest {

 // Test for constructor
 @Test
 public void testTokenQueueConstructor() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 Assert.assertFalse(tokenQueue.isEmpty());
 }

 // Test for isEmpty method
 @Test
 public void testIsEmpty() {
 TokenQueue tokenQueue = new TokenQueue("");
 Assert.assertTrue(tokenQueue.isEmpty());
 }

 // Test for peek method
 @Test
 public void testPeek() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 Assert.assertEquals('H', tokenQueue.peek());
 }

 // Test for addFirst method
 @Test
 public void testAddFirst() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 tokenQueue.addFirst('A');
 Assert.assertEquals('A', tokenQueue.peek());
 Assert.assertEquals("AHello World", tokenQueue.toString());
 }

 // Test for matches method (case insensitive)
 @Test
 public void testMatchesCS() {
 TokenQueue tokenQueue = new TokenQueue("<html lang=\"en\"><head><title>Test</title></head>");
 Assert.assertTrue(tokenQueue.matches("html"));
 }

 // Test for matches method (case sensitive)
 @Test
 public void testMatchesCSFalse() {
 TokenQueue tokenQueue = new TokenQueue("<html lang=\"en\"><head><title>Test</title></head>");
 Assert.assertFalse(tokenQueue.matchesCS("HTML"));
 }

 // Test for matchesAny method (case insensitive)
 @Test
 public void testMatchesAny() {
 TokenQueue tokenQueue = new TokenQueue("<html lang=\"en\"><head><title>Test</title></head>");
 Assert.assertTrue(tokenQueue.matchesAny("html", "head"));
 }

 // Test for matchesAny method (case sensitive)
 @Test
 public void testMatchesAnyCS() {
 TokenQueue tokenQueue = new TokenQueue("<html lang=\"en\"><head><title>Test</title></head>");
 Assert.assertFalse(tokenQueue.matchesAnyCS("HTML", "head"));
 }

 // Test for matchesStartTag method
 @Test
 public void testMatchesStartTag() {
 TokenQueue tokenQueue = new TokenQueue("<html lang=\"en\"><head><title>Test</title></head>");
 Assert.assertTrue(tokenQueue.matchesStartTag());
 }

 // Test for matchChomp method
 @Test
 public void testMatchChomp() {
 TokenQueue tokenQueue = new TokenQueue("<html lang=\"en\"><head><title>Test</title></head>");
 Assert.assertTrue(tokenQueue.matchChomp("html"));
 }

 // Test for matchesWhitespace method
 @Test
 public void testMatchesWhitespace() {
 TokenQueue tokenQueue = new TokenQueue("  Hello World   ");
 Assert.assertTrue(tokenQueue.matchesWhitespace());
 }

 // Test for matchesWord method
 @Test
 public void testMatchesWord() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 Assert.assertTrue(tokenQueue.matchesWord());
 }

 // Test for advance method
 @Test
 public void testAdvance() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 tokenQueue.advance();
 Assert.assertEquals('H', tokenQueue.peek());
 }

 // Test for consume method
 @Test
 public void testConsume() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 Assert.assertEquals('H', tokenQueue.consume());
 Assert.assertEquals("ello World", tokenQueue.toString());
 }

 // Test for consume method with sequence
 @Test
 public void testConsumeSequence() {
 TokenQueue tokenQueue = new TokenQueue("<html lang=\"en\"><head><title>Test</title></head>");
 tokenQueue.consume("html");
 Assert.assertEquals("head<title>Test</title>", tokenQueue.toString());
 }

 // Test for consumeTo method
 @Test
 public void testConsumeTo() {
 TokenQueue tokenQueue = new TokenQueue("Hello WorldXXXHello World");
 String consumed = tokenQueue.consumeTo("World");
 Assert.assertEquals("Hello World", consumed);
 Assert.assertEquals("XXXHello World", tokenQueue.toString());
 }

 // Test for consumeToIgnoreCase method
 @Test
 public void testConsumeToIgnoreCase() {
 TokenQueue tokenQueue = new TokenQueue("Hello WorldXXXHello World");
 String consumed = tokenQueue.consumeToIgnoreCase("world");
 Assert.assertEquals("Hello World", consumed);
 Assert.assertEquals("XXXHello World", tokenQueue.toString());
 }

 // Test for consumeToAny method
 @Test
 public void testConsumeToAny() {
 TokenQueue tokenQueue = new TokenQueue("Hello World<![CDATA[script]]>Hello World");
 String consumed = tokenQueue.consumeToAny("script");
 Assert.assertEquals("<![CDATA[script]]>", consumed);
 Assert.assertEquals("Hello WorldHello World", tokenQueue.toString());
 }

 // Test for consumeToAny method with empty sequence
 @Test (expected = IllegalStateException.class)
 public void testConsumeToAnyEmptySequence() {
 TokenQueue tokenQueue = new TokenQueue("Hello World<![CDATA[script]]>Hello World");
 tokenQueue.consumeToAny(new String[0]);
 }

 // Test for chompBalanced method
 @Test
 public void testChompBalanced() {
 TokenQueue tokenQueue = new TokenQueue("(hello (world) (world))");
 String result = tokenQueue.chompBalanced('(', ')');
 Assert.assertEquals("hello (world) (world)", result);
 }

 // Test for consumeWhitespace method
 @Test
 public void testConsumeWhitespace() {
 TokenQueue tokenQueue = new TokenQueue(" Hello World ");
 boolean consumed = tokenQueue.consumeWhitespace();
 Assert.assertTrue(consumed);
 Assert.assertEquals("Hello World", tokenQueue.toString());
 }

 // Test for consumeWord method
 @Test
 public void testConsumeWord() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 String consumed = tokenQueue.consumeWord();
 Assert.assertEquals("Hello", consumed);
 Assert.assertEquals("World", tokenQueue.toString());
 }

 // Test for consumeTagName method
 @Test
 public void testConsumeTagName() {
 TokenQueue tokenQueue = new TokenQueue("<html lang=\"en\"><head><title>Test</title></head>");
 String consumed = tokenQueue.consumeTagName();
 Assert.assertEquals("html", consumed);
 Assert.assertEquals("<head><title>Test</title></head>", tokenQueue.toString());
 }

 // Test for consumeElementSelector method
 @Test
 public void testConsumeElementSelector() {
 TokenQueue tokenQueue = new TokenQueue("div#id.class div.class p#id p.class div.class");
 String consumed = tokenQueue.consumeElementSelector();
 Assert.assertEquals("div", consumed);
 Assert.assertEquals("id.class div.class p#id p.class div.class", tokenQueue.toString());
 }

 // Test for consumeCssIdentifier method
 @Test
 public void testConsumeCssIdentifier() {
 TokenQueue tokenQueue = new TokenQueue("color: #red;");
 String consumed = tokenQueue.consumeCssIdentifier();
 Assert.assertEquals("color", consumed);
 Assert.assertEquals(": #red", tokenQueue.toString());
 }

 // Test for consumeAttributeKey method
 @Test
 public void testConsumeAttributeKey() {
 TokenQueue tokenQueue = new TokenQueue("class=\"class1 class2\"");
 String consumed = tokenQueue.consumeAttributeKey();
 Assert.assertEquals("class", consumed);
 Assert.assertEquals("=\"class1 class2\"", tokenQueue.toString());
 }

 // Test for remainder method
 @Test
 public void testRemainder() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 String remainder = tokenQueue.remainder();
 Assert.assertEquals("", remainder);
 Assert.assertEquals("", tokenQueue.toString());
 }

 // Test for remainder method with data
 @Test
 public void testRemainderWithData() {
 TokenQueue tokenQueue = new TokenQueue("Hello World");
 String remainder = tokenQueue.remainder();
 Assert.assertEquals("World", remainder);
 Assert.assertEquals("Hello", tokenQueue.toString());
 }
}