import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Test
 public void testConstructor() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertFalse(tokenQueue.isEmpty());
 Assert.assertEquals("hello world", tokenQueue.toString());
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
 Assert.assertEquals("!hello world", tokenQueue.toString());
 }

 @Test
 public void testMatches() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertTrue(tokenQueue.matches("ll"));
 }

 @Test
 public void testMatchesCS() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertFalse(tokenQueue.matchesCS("HeLLo"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertTrue(tokenQueue.matchesAny("ll", 'o'));
 }

 @Test
 public void testMatchesAnyWithEmptyArray() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertFalse(tokenQueue.matchesAny(new String[]{}));
 }

 @Test
 public void testMatchesStartTag() {
 tokenQueue = new TokenQueue("<html>");
 Assert.assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue = new TokenQueue("hello <world>");
 Assert.assertTrue(tokenQueue.matchChomp("hello <"));
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue = new TokenQueue(" hello world ");
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
 Assert.assertEquals("ello world", tokenQueue.toString());
 }

 @Test
 public void testConsumeString() {
 tokenQueue = new TokenQueue("hello world");
 tokenQueue.consume("ll");
 Assert.assertEquals("ello world", tokenQueue.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeNotMatched() {
 tokenQueue = new TokenQueue("hello world");
 tokenQueue.consume("xx");
 }

 @Test
 public void testConsumeTo() {
 tokenQueue = new TokenQueue("hello <world>");
 Assert.assertEquals("hello <", tokenQueue.consumeTo("hello <"));
 Assert.assertEquals("<world>", tokenQueue.toString());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 tokenQueue = new TokenQueue("Hello <world>");
 Assert.assertEquals("Hello <", tokenQueue.consumeToIgnoreCase("HELLO <"));
 Assert.assertEquals("<world>", tokenQueue.toString());
 }

 @Test
 public void testConsumeToAny() {
 tokenQueue = new TokenQueue("hello <world> <foo>");
 Assert.assertEquals("hello <", tokenQueue.consumeToAny("hello <"));
 Assert.assertEquals("<world> <foo>", tokenQueue.toString());
 }

 @Test
 public void testConsumeToAnyEmptyArray() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertEquals("hello world", tokenQueue.consumeToAny(new String[]{}));
 }

 @Test
 public void testChompBalanced() {
 tokenQueue = new TokenQueue("(one (two) three) four");
 Assert.assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testChompBalancedEmpty() {
 tokenQueue = new TokenQueue("()");
 Assert.assertEquals("", tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testChompBalancedUnbalanced() {
 tokenQueue = new TokenQueue("(one (two) three");
 Assert.assertEquals("(one (two) three", tokenQueue.toString());
 }

 @Test
 public void testChompBalancedEscaped() {
 tokenQueue = new TokenQueue("(one \\(two\\) three) four");
 Assert.assertEquals("one \\(two\\) three", tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 String escaped = "\\(one \\(two\\) three\\) four";
 String unescaped = TokenQueue.unescape(escaped);
 Assert.assertEquals("(one (two) three) four", unescaped);
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue = new TokenQueue(" hello world ");
 Assert.assertTrue(tokenQueue.consumeWhitespace());
 Assert.assertEquals("helloworld", tokenQueue.toString());
 }

 @Test
 public void testConsumeWord() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertEquals("hello", tokenQueue.consumeWord());
 Assert.assertEquals(" world", tokenQueue.toString());
 }

 @Test
 public void testConsumeTagName() {
 tokenQueue = new TokenQueue("<html>");
 Assert.assertEquals("html", tokenQueue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 tokenQueue = new TokenQueue("div.red:hover");
 Assert.assertEquals("div.red", tokenQueue.consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 tokenQueue = new TokenQueue("class:exampleId");
 Assert.assertEquals("class", tokenQueue.consumeCssIdentifier());
 Assert.assertEquals("exampleId", tokenQueue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 tokenQueue = new TokenQueue("class=\"example\" id=\"idExample\"");
 Assert.assertEquals("class", tokenQueue.consumeAttributeKey());
 Assert.assertEquals("id", tokenQueue.consumeAttributeKey());
 }

 @Test
 public void testRemainder() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertEquals(" world", tokenQueue.remainder());
 }

 @Test
 public void testToString() {
 tokenQueue = new TokenQueue("hello world");
 Assert.assertEquals(" world", tokenQueue.toString());
 }
}