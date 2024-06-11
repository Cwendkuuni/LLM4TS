import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Test
 public void testTokenQueueCreation() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testIsEmpty() {
 tokenQueue = new TokenQueue("");
 Assert.assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertEquals('h', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue = new TokenQueue("hello world");
 tokenQueue.addFirst('!');
 Assert.assertEquals('!', tokenQueue.peek());
 }

 @Test
 public void testMatches() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertTrue(tokenQueue.matches("hello"));
 }

 @Test
 public void testMatchesCS() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertFalse(tokenQueue.matchesCS("world"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertTrue(tokenQueue.matchesAny("l", "o"));
 }

 @Test
 public void testMatchesAnyChar() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertTrue(tokenQueue.matchesAny(new char[]{'l', 'o'}));
 }

 @Test
 public void testMatchesStartTag() {
 tokenQueue = new TokenQueue("<html><head><title>");
 Assert.assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertTrue(tokenQueue.matchChomp("hello"));
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue = new TokenQueue("   hello world   ");
 Assert.assertTrue(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertTrue(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 tokenQueue = new TokenQueue("hello world");
 tokenQueue.advance();
 Assert.assertEquals('h', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertEquals('h', tokenQueue.consume());
 Assert.assertEquals('e', tokenQueue.peek());
 }

 @Test
 public void testConsumeString() {
 tokenQueue = new TokenQueue("hello world");
 tokenQueue.consume("hello");
 Assert.assertEquals(' ', tokenQueue.peek());
 }

 @Test
 public void testConsumeTo() {
 tokenQueue = new TokenQueue("(one (two) three) four");
 Assert.assertEquals("one (two) three", tokenQueue.consumeTo("("));
 Assert.assertEquals(" four", tokenQueue.toString());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 tokenQueue = new TokenQueue("(one (two) three) four");
 Assert.assertEquals("one (two) three", tokenQueue.consumeToIgnoreCase("("));
 Assert.assertEquals(" four", tokenQueue.toString());
 }

 @Test
 public void testConsumeToAny() {
 tokenQueue = new TokenQueue("(one (two) three) four");
 Assert.assertEquals("one (two) three", tokenQueue.consumeToAny("(", ")", "three"));
 Assert.assertEquals(" four", tokenQueue.toString());
 }

 @Test
 public void testConsumeBalanced() {
 tokenQueue = new TokenQueue("(one (two) three) four");
 Assert.assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
 Assert.assertEquals(" four", tokenQueue.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeUnmatchedSequence() {
 tokenQueue = new TokenQueue("(one (two) three) four");
 tokenQueue.consume("five");
 }

 @Test
 public void testUnescape() {
 Assert.assertEquals("\\ab\\bc\\cd\\ef", TokenQueue.unescape("\\ab\\bc\\cd\\ef"));
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue = new TokenQueue("   hello world   ");
 Assert.assertTrue(tokenQueue.consumeWhitespace());
 Assert.assertEquals('h', tokenQueue.peek());
 }

 @Test
 public void testConsumeWord() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertEquals("hello", tokenQueue.consumeWord());
 Assert.assertEquals(" world", tokenQueue.toString());
 }

 @Test
 public void testConsumeTagName() {
 tokenQueue = new TokenQueue("<html><head><title>My page</title></head>");
 Assert.assertEquals("title", tokenQueue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 tokenQueue = new TokenQueue("div.class p.anotherClass#identifier");
 Assert.assertEquals("div", tokenQueue.consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 tokenQueue = new TokenQueue("id:identifier class:class");
 Assert.assertEquals("identifier", tokenQueue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 tokenQueue = new TokenQueue("name:value class:class attr:attr");
 Assert.assertEquals("name", tokenQueue.consumeAttributeKey());
 }

 @Test
 public void testRemainder() {
 tokenQueue = new TokenQueue("(one (two) three) four");
 tokenQueue.consumeTo(')');
 Assert.assertEquals("", tokenQueue.remainder());
 }
}