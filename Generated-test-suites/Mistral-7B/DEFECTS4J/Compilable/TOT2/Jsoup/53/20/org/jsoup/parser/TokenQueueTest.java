package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokenQueueTest {

 private TokenQueue queue;

 @Test
 public void testConstructor() {
 queue = new TokenQueue("Hello World");
 Assert.assertFalse(queue.isEmpty());
 Assert.assertEquals("Hello World", queue.toString());
 }

 @Test
 public void testIsEmpty() {
 queue = new TokenQueue("");
 Assert.assertTrue(queue.isEmpty());
 queue = new TokenQueue("Hello World");
 Assert.assertFalse(queue.isEmpty());
 }

 @Test
 public void testPeek() {
 queue = new TokenQueue("Hello World");
 Assert.assertEquals('H', queue.peek());
 }

 @Test(expected = IllegalStateException.class)
 public void testPeekWhenEmpty() {
 queue = new TokenQueue("");
 queue.peek();
 }

 @Test
 public void testAddFirst() {
 queue = new TokenQueue("Hello World");
 queue.addFirst('X');
 Assert.assertEquals('X', queue.peek());
 Assert.assertEquals("XHello World", queue.toString());
 }

 @Test
 public void testMatches() {
 queue = new TokenQueue("Hello World");
 Assert.assertTrue(queue.matches("He"));
 Assert.assertTrue(queue.matches("ll"));
 Assert.assertTrue(queue.matches("World"));
 Assert.assertFalse(queue.matches("Hi"));
 }

 @Test
 public void testMatchesCS() {
 queue = new TokenQueue("Hello World");
 Assert.assertFalse(queue.matchesCS("He")); // case sensitive
 Assert.assertTrue(queue.matchesCS("Hello"));
 Assert.assertTrue(queue.matchesCS("World"));
 }

 @Test
 public void testMatchesAny() {
 queue = new TokenQueue("Hello World");
 Assert.assertTrue(queue.matchesAny("He", "ll"));
 Assert.assertTrue(queue.matchesAny("World"));
 Assert.assertFalse(queue.matchesAny("Hi"));
 }

 @Test
 public void testMatchesAnyChar() {
 queue = new TokenQueue("Hello World");
 Assert.assertTrue(queue.matchesAny(new char[]{'H', 'l'}));
 Assert.assertTrue(queue.matchesAny(new char[]{'l', 'd'}));
 Assert.assertTrue(queue.matchesAny(new char[]{'W', 'r'}));
 }

 @Test
 public void testMatchesStartTag() {
 queue = new TokenQueue("<html>");
 Assert.assertTrue(queue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 queue = new TokenQueue("Hello <world>");
 Assert.assertTrue(queue.matchChomp("Hello "));
 Assert.assertTrue(queue.isEmpty());
 }

 @Test
 public void testMatchesWhitespace() {
 queue = new TokenQueue(" Hello World ");
 Assert.assertTrue(queue.matchesWhitespace());
 queue.advance();
 Assert.assertFalse(queue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 queue = new TokenQueue("Hello World");
 Assert.assertTrue(queue.matchesWord());
 queue.advance();
 Assert.assertTrue(queue.matchesWord());
 }

 @Test
 public void testAdvance() {
 queue = new TokenQueue("Hello World");
 queue.advance();
 Assert.assertEquals('H', queue.peek());
 }

 @Test
 public void testConsume() {
 queue = new TokenQueue("Hello World");
 Assert.assertEquals('H', queue.consume());
 Assert.assertEquals("ello World", queue.toString());
 }

 @Test
 public void testConsumeString() {
 queue = new TokenQueue("Hello World");
 queue.consume("Hello");
 Assert.assertEquals("World", queue.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeNonMatchedString() {
 queue = new TokenQueue("Hello World");
 queue.consume("Hi");
 }

 @Test
 public void testConsumeTo() {
 queue = new TokenQueue("Hello World");queue.consumeTo("ll");
 Assert.assertEquals("World", queue.toString());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 queue = new TokenQueue("Hello World");queue.consumeToIgnoreCase("hi");
 Assert.assertEquals("World", queue.toString());
 }

 @Test
 public void testConsumeToAny() {
 queue = new TokenQueue("Hello World");queue.consumeToAny("hi", "ll");
 Assert.assertEquals("World", queue.toString());
 }

 @Test
 public void testConsumeToAnyWithMatch() {
 queue = new TokenQueue("Hello<tag> World");queue.consumeToAny("tag");
 Assert.assertEquals(" World", queue.toString());
 }

 @Test
 public void testChompBalanced() {
 queue = new TokenQueue("(hello (world) world) world");
 Assert.assertEquals("hello (world) world", queue.chompBalanced('(', ')').toString());
 Assert.assertEquals("world world", queue.toString());
 }

 @Test
 public void testChompBalancedWithInvalidOpen() {
 queue = new TokenQueue("[hello (world) world]");
 Assert.assertEquals("hello (world) world", queue.toString());
 }

 @Test
 public void testChompBalancedWithInvalidClose() {
 queue = new TokenQueue("(hello [world] world) world");
 Assert.assertEquals("(hello [world] world) world", queue.toString());
 }

 @Test
 public void testChompBalancedWithEscapedOpen() {
 queue = new TokenQueue("(hello (\\(world) world) world)");
 Assert.assertEquals("hello (world) world", queue.chompBalanced('(', ')').toString());
 Assert.assertEquals("\\(world) world world", queue.toString());
 }

 @Test
 public void testChompBalancedWithEscapedClose() {
 queue = new TokenQueue("(hello (world)\\) world)");
 Assert.assertEquals("hello (world)", queue.toString());
 }

 @Test
 public void testUnescape() {
 queue = new TokenQueue("\\(hello \\(world\\) world\\)");
 Assert.assertEquals("(hello (world) world)", TokenQueue.unescape(queue.toString()));
 }

 @Test
 public void testConsumeWhitespace() {
 queue = new TokenQueue(" Hello World ");
 Assert.assertTrue(queue.consumeWhitespace());
 Assert.assertEquals("Hello World", queue.toString());
 }

 @Test
 public void testConsumeWord() {
 queue = new TokenQueue("Hello World");
 Assert.assertEquals("He", queue.consumeWord());
 Assert.assertEquals("ll", queue.consumeWord());
 Assert.assertEquals("World", queue.toString());
 }

 @Test
 public void testConsumeTagName() {
 queue = new TokenQueue("<html><body><tag foo=\"bar\"></tag></body></html>");
 Assert.assertEquals("tag", queue.consumeTagName());
 Assert.assertEquals(" foo=\"bar\"", queue.toString());
 }

 @Test
 public void testConsumeElementSelector() {
 queue = new TokenQueue("[html] div p.class #id > span:first-child");
 Assert.assertEquals("div", queue.consumeElementSelector());
 Assert.assertEquals(" p.class #id > span:first-child", queue.toString());
 }

 @Test
 public void testConsumeCssIdentifier() {
 queue = new TokenQueue("class:foo id:bar");
 Assert.assertEquals("class", queue.consumeCssIdentifier());
 Assert.assertEquals("foo", queue.toString());
 }

 @Test
 public void testConsumeAttributeKey() {
 queue = new TokenQueue("[foo='bar']");
 Assert.assertEquals("foo", queue.consumeAttributeKey());
 Assert.assertEquals("=bar", queue.toString());
 }

 @Test
 public void testRemainder() {
 queue = new TokenQueue("Hello World");
 Assert.assertEquals("World", queue.remainder());
 Assert.assertEquals("", queue.toString());
 }

 @Test
 public void testToString() {
 queue = new TokenQueue("Hello World");
 Assert.assertEquals("World", queue.toString());
 }
}