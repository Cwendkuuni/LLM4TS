package org.jsoup.parser;
import org.jsoup.parser.TokenQueue;
import org.junit.Assert;
import org.junit.Test;

public class TokenQueueTest {

 @Test
 public void testCreateTokenQueue() {
 TokenQueue tokenQueue = new TokenQueue("example");
 Assert.assertNotNull(tokenQueue);
 }

 @Test
 public void testIsEmpty() {
 TokenQueue tokenQueue = new TokenQueue("example");
 Assert.assertFalse(tokenQueue.isEmpty());
 TokenQueue emptyTokenQueue = new TokenQueue("");
 Assert.assertTrue(emptyTokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 TokenQueue tokenQueue = new TokenQueue("example");
 Assert.assertEquals('e', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 TokenQueue tokenQueue = new TokenQueue("example");
 tokenQueue.addFirst('a');
 Assert.assertEquals('a', tokenQueue.peek());
 }

 @Test
 public void testMatches() {
 TokenQueue tokenQueue = new TokenQueue("example");
 Assert.assertTrue(tokenQueue.matches("xp"));
 Assert.assertFalse(tokenQueue.matches("ample"));
 }

 @Test
 public void testMatchesCS() {
 TokenQueue tokenQueue = new TokenQueue("example");
 Assert.assertTrue(tokenQueue.matchesCS("xp"));
 Assert.assertFalse(tokenQueue.matchesCS("ample"));
 }

 @Test
 public void testMatchesAny() {
 TokenQueue tokenQueue = new TokenQueue("example");
 Assert.assertTrue(tokenQueue.matchesAny("xp", "am"));
 Assert.assertFalse(tokenQueue.matchesAny("zx"));
 }

 @Test
 public void testMatchesAnyChar() {
 TokenQueue tokenQueue = new TokenQueue("example");
 Assert.assertTrue(tokenQueue.matchesAny('x', 'p'));
 Assert.assertFalse(tokenQueue.matchesAny('z'));
 }

 @Test
 public void testMatchesStartTag() {
 TokenQueue tokenQueue = new TokenQueue("<example>");
 Assert.assertTrue(tokenQueue.matchesStartTag());
 TokenQueue notStartTagTokenQueue = new TokenQueue("example");
 Assert.assertFalse(notStartTagTokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 TokenQueue tokenQueue = new TokenQueue("example<tag>");
 Assert.assertTrue(tokenQueue.matchChomp("example"));
 Assert.assertEquals('t', tokenQueue.peek());
 }

 @Test
 public void testMatchesWhitespace() {
 TokenQueue tokenQueue = new TokenQueue(" example");
 Assert.assertTrue(tokenQueue.matchesWhitespace());
 TokenQueue noWhitespaceTokenQueue = new TokenQueue("example");
 Assert.assertFalse(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 TokenQueue tokenQueue = new TokenQueue("example");
 Assert.assertTrue(tokenQueue.matchesWord());
 TokenQueue noWordTokenQueue = new TokenQueue("example123");
 Assert.assertFalse(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 TokenQueue tokenQueue = new TokenQueue("example");
 tokenQueue.advance();
 Assert.assertEquals('e', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 TokenQueue tokenQueue = new TokenQueue("example");
 Assert.assertEquals('e', tokenQueue.consume());
 Assert.assertEquals('p', tokenQueue.consume());
 Assert.assertEquals('x', tokenQueue.consume());
 Assert.assertEquals(' ', tokenQueue.consume());
 Assert.assertEquals('\u0000', tokenQueue.consume());
 }

 @Test
 public void testConsumeString() {
 TokenQueue tokenQueue = new TokenQueue("example");
 Assert.assertEquals("example", tokenQueue.consumeTo("example"));
 }

 @Test
 public void testConsumeToIgnoreCase() {
 TokenQueue tokenQueue = new TokenQueue("Example:exAmple");
 Assert.assertEquals("example", tokenQueue.consumeToIgnoreCase("example"));
 }

 @Test
 public void testConsumeToAny() {
 TokenQueue tokenQueue = new TokenQueue("example:exAmpLe:123:456");
 Assert.assertEquals("example", tokenQueue.consumeToAny("example"));
 }

 @Test
 public void testChompBalanced() {
 TokenQueue tokenQueue = new TokenQueue("(example (hello) world) hello");
 Assert.assertEquals("example (hello)", tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 String input = "\\(example\\)";
 String expected = "example";
 String output = TokenQueue.unescape(input);
 Assert.assertEquals(expected, output);
 }

 @Test
 public void testConsumeWhitespace() {
 TokenQueue tokenQueue = new TokenQueue(" example ");
 Assert.assertTrue(tokenQueue.consumeWhitespace());
 }

 @Test
 public void testConsumeWord() {
 TokenQueue tokenQueue = new TokenQueue("example");
 Assert.assertEquals("example", tokenQueue.consumeWord());
 }

 @Test
 public void testConsumeTagName() {
 TokenQueue tokenQueue = new TokenQueue("<tag name>");
 Assert.assertEquals("tag name", tokenQueue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 TokenQueue tokenQueue = new TokenQueue("<tag name:class>");
 Assert.assertEquals("tag name", tokenQueue.consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 TokenQueue tokenQueue = new TokenQueue("class:example:id:idExample");
 Assert.assertEquals("example", tokenQueue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 TokenQueue tokenQueue = new TokenQueue("class:example attribute:value");
 Assert.assertEquals("attribute", tokenQueue.consumeAttributeKey());
 }

 @Test
 public void testRemainder() {
 TokenQueue tokenQueue = new TokenQueue("example");
 Assert.assertEquals("", tokenQueue.remainder());
 TokenQueue remainderTokenQueue = new TokenQueue("example:remainder");
 Assert.assertEquals("remainder", tokenQueue.remainder());
 }
}