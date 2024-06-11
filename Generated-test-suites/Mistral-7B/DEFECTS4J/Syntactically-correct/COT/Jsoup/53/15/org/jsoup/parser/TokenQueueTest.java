import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setup() {
 tokenQueue = new TokenQueue("testData");
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(tokenQueue.isEmpty());
 tokenQueue.consume("testData".charAt(0));
 Assert.assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 Assert.assertEquals('t', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue.addFirst('X');
 Assert.assertEquals('X', tokenQueue.consume());
 Assert.assertEquals('t', tokenQueue.peek());
 }

 @Test
 public void testMatches() {
 tokenQueue.addFirst("testData".toCharArray());
 Assert.assertTrue(tokenQueue.matches("es"));
 Assert.assertFalse(tokenQueue.matches("te"));
 }

 @Test
 public void testMatchesCS() {
 tokenQueue.addFirst("testData".toCharArray());
 Assert.assertTrue(tokenQueue.matchesCS("es"));
 Assert.assertFalse(tokenQueue.matchesCS("te"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue.addFirst("testData".toCharArray());
 Assert.assertTrue(tokenQueue.matchesAny("es", "te"));
 Assert.assertFalse(tokenQueue.matchesAny("fa"));
 }

 @Test
 public void testMatchesAnyChar() {
 tokenQueue.addFirst("testData".toCharArray());
 Assert.assertTrue(tokenQueue.matchesAny(new char[]{'s', 't'}));
 Assert.assertFalse(tokenQueue.matchesAny(new char[]{'f', 'a'}));
 }

 @Test
 public void testMatchesStartTag() {
 Assert.assertFalse(tokenQueue.matchesStartTag());
 tokenQueue.addFirst("<test>");
 Assert.assertTrue(tokenQueue.matchesStartTag());
 }

 @Test(expected = IllegalStateException.class)
 public void testMatchChompWithEmptyQueue() {
 tokenQueue.matchChomp("test");
 }

 @Test
 public void testMatchChomp() {
 tokenQueue.addFirst("testData".toCharArray());
 Assert.assertTrue(tokenQueue.matches("es"));
 Assert.assertTrue(tokenQueue.matchChomp("es"));
 Assert.assertEquals('t', tokenQueue.peek());
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue.addFirst(" testData ".toCharArray());
 Assert.assertTrue(tokenQueue.matchesWhitespace());
 tokenQueue.consumeWhitespace();
 Assert.assertFalse(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue.addFirst("testData".toCharArray());
 Assert.assertTrue(tokenQueue.matchesWord());
 tokenQueue.consumeWord();
 Assert.assertTrue(tokenQueue.matchesWord());
 }

 @Test
 public void testConsume() {
 Assert.assertEquals('t', tokenQueue.consume());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeWithEmptyQueue() {
 tokenQueue.consume();
 }

 @Test
 public void testConsumeString() {
 tokenQueue.addFirst("testData".toCharArray());
 tokenQueue.consume("es");
 Assert.assertEquals('t', tokenQueue.peek());
 }

 @Test
 public void testConsumeTo() {
 tokenQueue.addFirst("testData".toCharArray());
 Assert.assertEquals("est", tokenQueue.consumeTo("est"));
 Assert.assertEquals("Data", tokenQueue.consumeToIgnoreCase("Data"));
 }

 @Test
 public void testConsumeToAny() {
 tokenQueue.addFirst("testData".toCharArray());
 Assert.assertEquals("es", tokenQueue.consumeToAny("es", "te"));
 }

 @Test
 public void testChompBalanced() {
 tokenQueue.addFirst("(test)(test)test");
 Assert.assertEquals("(test)(test)", tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 Assert.assertEquals("ab\\cd\\ef", TokenQueue.unescape("ab\\cd\\ef"));
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue.addFirst(" testData ".toCharArray());
 Assert.assertTrue(tokenQueue.consumeWhitespace());
 }

 @Test
 public void testConsumeWord() {
 Assert.assertEquals("test", tokenQueue.consumeWord());
 }

 @Test
 public void testConsumeTagName() {
 Assert.assertEquals("test", tokenQueue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 Assert.assertEquals("test", tokenQueue.consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 Assert.assertEquals("identifier", tokenQueue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 Assert.assertEquals("attribute", tokenQueue.consumeAttributeKey());
 }

 @Test
 public void testRemainder() {
 Assert.assertEquals("remainder", tokenQueue.remainder());
 }
}