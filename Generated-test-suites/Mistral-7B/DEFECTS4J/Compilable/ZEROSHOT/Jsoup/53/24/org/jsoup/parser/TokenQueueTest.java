package org.jsoup.parser;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 @Test
 public void testTokenQueueIsEmpty() {
 TokenQueue tokenQueue = new TokenQueue("");
 Assert.assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testTokenQueueIsNotEmpty() {
 TokenQueue tokenQueue = new TokenQueue("abcdef");
 Assert.assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 TokenQueue tokenQueue = new TokenQueue("abcdef");
 Assert.assertEquals('a', tokenQueue.peek());
 }

 @Test(expected = IllegalStateException.class)
 public void testPeekWhenEmpty() {
 TokenQueue tokenQueue = new TokenQueue("");
 tokenQueue.peek();
 }

 @Test
 public void testAddFirst() {
 TokenQueue tokenQueue = new TokenQueue("abcdef");
 tokenQueue.addFirst('x');
 Assert.assertEquals('x', tokenQueue.peek());
 }

 @Test
 public void testMatches() {
 TokenQueue tokenQueue = new TokenQueue("abcdef");
 Assert.assertTrue(tokenQueue.matches("ab"));
 }

 @Test
 public void testMatchesCS() {
 TokenQueue tokenQueue = new TokenQueue("abcABCef");
 Assert.assertTrue(tokenQueue.matchesCS("ab"));
 }

 @Test
 public void testMatchesAny() {
 TokenQueue tokenQueue = new TokenQueue("abcABCef");
 Assert.assertTrue(tokenQueue.matchesAny("ab", "ef"));
 }

 @Test
 public void testMatchesAnyWithChar() {
 TokenQueue tokenQueue = new TokenQueue("abcABCef");
 Assert.assertTrue(tokenQueue.matchesAny('a', 'e'));
 }

 @Test
 public void testMatchesStartTag() {
 TokenQueue tokenQueue = new TokenQueue("<html><head><title>");
 Assert.assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 TokenQueue tokenQueue = new TokenQueue("abc<def>ef");
 Assert.assertTrue(tokenQueue.matchChomp("<def>"));
 }

 @Test
 public void testMatchesWhitespace() {
 TokenQueue tokenQueue = new TokenQueue(" abc ");
 Assert.assertTrue(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 TokenQueue tokenQueue = new TokenQueue("abcABCef");
 Assert.assertTrue(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 TokenQueue tokenQueue = new TokenQueue("abcABCef");
 tokenQueue.advance();
 Assert.assertEquals('b', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 TokenQueue tokenQueue = new TokenQueue("abcABCef");
 Assert.assertEquals('a', tokenQueue.consume());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeWhenEmpty() {
 TokenQueue tokenQueue = new TokenQueue("");
 tokenQueue.consume();
 }

 @Test
 public void testConsumeSequence() {
 TokenQueue tokenQueue = new TokenQueue("abc<def>ef");
 String sequence = "<def>";
 tokenQueue.consume(sequence);
 Assert.assertEquals("ef", tokenQueue.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeSequenceEmptyQueue() {
 TokenQueue tokenQueue = new TokenQueue("");
 String sequence = "abc";
 tokenQueue.consume(sequence);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeSequenceDoesNotMatch() {
 TokenQueue tokenQueue = new TokenQueue("abc<def>ef");
 String sequence = "xyz";
 tokenQueue.consume(sequence);
 }

 @Test
 public void testConsumeTo() {
 TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
 String sequence = "one (two) three";
 String result = tokenQueue.consumeTo(sequence);
 Assert.assertEquals(result, sequence);
 Assert.assertEquals(" four", tokenQueue.toString());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 TokenQueue tokenQueue = new TokenQueue("(One (Two) Three) Four");
 String sequence = "one (two) three";
 String result = tokenQueue.consumeToIgnoreCase(sequence);
 Assert.assertEquals(result, sequence);
 Assert.assertEquals(" Four", tokenQueue.toString());
 }

 @Test
 public void testConsumeToAny() {
 TokenQueue tokenQueue = new TokenQueue("(one (two) three) for");
 String[] sequences = {"one", "for"};
 String result = tokenQueue.consumeToAny(sequences);
 Assert.assertEquals(result, "for");
 Assert.assertEquals("(one (two) three)", tokenQueue.toString());
 }

 @Test
 public void testChompBalanced() {
 TokenQueue tokenQueue = new TokenQueue("(one (two) three) for");
 char open = '(';
 char close = ')';
 String result = tokenQueue.chompBalanced(open, close);
 Assert.assertEquals(result, "one (two) three");
 Assert.assertEquals(" for", tokenQueue.toString());
 }

 @Test
 public void testUnescape() {
 String escapedString = "\\(one \\(two\\) three\\) for";
 String unescapedString = TokenQueue.unescape(escapedString);
 Assert.assertEquals(unescapedString, "(one (two) three) for");
 }

 @Test
 public void testConsumeWhitespace() {
 TokenQueue tokenQueue = new TokenQueue(" abc ");
 boolean result = tokenQueue.consumeWhitespace();
 Assert.assertTrue(result);
 Assert.assertEquals("", tokenQueue.toString());
 }

 @Test
 public void testConsumeWord() {
 TokenQueue tokenQueue = new TokenQueue("abcABCef");
 String result = tokenQueue.consumeWord();
 Assert.assertEquals(result, "abc");
 Assert.assertEquals("ABCef", tokenQueue.toString());
 }

 @Test
 public void testConsumeTagName() {
 TokenQueue tokenQueue = new TokenQueue("<html><head><title>My Title</title>");
 String result = tokenQueue.consumeTagName();
 Assert.assertEquals(result, "title");
 Assert.assertEquals("My Title", tokenQueue.toString());
 }

 @Test
 public void testConsumeElementSelector() {
 TokenQueue tokenQueue = new TokenQueue("div.class1.class2#id1.class3");
 String result = tokenQueue.consumeElementSelector();
 Assert.assertEquals(result, "div.class1");
 Assert.assertEquals(".class2#id1.class3", tokenQueue.toString());
 }

 @Test
 public void testConsumeCssIdentifier() {
 TokenQueue tokenQueue = new TokenQueue("color: red; background-color: #ff0000;");
 String result = tokenQueue.consumeCssIdentifier();
 Assert.assertEquals(result, "color");
 Assert.assertEquals(" red; background-color: #ff0000", tokenQueue.toString());
 }

 @Test
 public void testConsumeAttributeKey() {
 TokenQueue tokenQueue = new TokenQueue("class=\"class1 class2\" id=\"id1\"");
 String result = tokenQueue.consumeAttributeKey();
 Assert.assertEquals(result, "class");
 Assert.assertEquals("=\"class1 class2\" id=\"id1\"", tokenQueue.toString());
 }

 @Test
 public void testRemainder() {
 TokenQueue tokenQueue = new TokenQueue("abcABCef");
 String result = tokenQueue.remainder();
 Assert.assertEquals(result, "ef");
 Assert.assertEquals("", tokenQueue.toString());
 }

 @Test
 public void testToString() {
 TokenQueue tokenQueue = new TokenQueue("abcABCef");
 String result = tokenQueue.toString();
 Assert.assertEquals(result, "ef");
 }
}