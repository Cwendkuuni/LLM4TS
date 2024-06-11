import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 // Test for constructor
 @Test
 public void testTokenQueueConstruction() {
 TokenQueue queue = new TokenQueue("Hello World");
 Assert.assertFalse(queue.isEmpty());
 }

 // Test for isEmpty method
 @Test
 public void testIsEmpty() {
 TokenQueue queue = new TokenQueue("");
 Assert.assertTrue(queue.isEmpty());

 TokenQueue queue1 = new TokenQueue("Hello World");
 Assert.assertFalse(queue1.isEmpty());
 }

 // Test for remainingLength method
 @Test
 public void testRemainingLength() {
 TokenQueue queue = new TokenQueue("Hello World");
 Assert.assertEquals(11, queue.remainingLength());
 }

 // Test for peek method
 @Test
 public void testPeek() {
 TokenQueue queue = new TokenQueue("Hello World");
 Assert.assertEquals('H', queue.peek());
 }

 // Test for addFirst method with String
 @Test
 public void testAddFirstString() {
 TokenQueue queue = new TokenQueue("Hello World");
 queue.addFirst("ABC");
 Assert.assertEquals("ABCHello World", queue.toString());
 }

 // Test for addFirst method with Character
 @Test
 public void testAddFirstCharacter() {
 TokenQueue queue = new TokenQueue("Hello World");
 queue.addFirst('A');
 Assert.assertEquals("AHello World", queue.toString());
 }

 // Test for matches method with String
 @Test
 public void testMatchesString() {
 TokenQueue queue = new TokenQueue("Hello World");
 Assert.assertTrue(queue.matches("Hello"));
 }

 // Test for matches method with String case insensitive
 @Test
 public void testMatchesStringIgnoreCase() {
 TokenQueue queue = new TokenQueue("Hello World");
 Assert.assertTrue(queue.matchesIgnoreCase("hello"));
 }

 // Test for matchesAny method with String
 @Test
 public void testMatchesAnyString() {
 TokenQueue queue = new TokenQueue("Hello World");
 Assert.assertTrue(queue.matchesAny("Hi", "World"));
 }

 // Test for matchesAny method with Character
 @Test
 public void testMatchesAnyCharacter() {
 TokenQueue queue = new TokenQueue("Hello World");
 Assert.assertTrue(queue.matchesAny('H', 'W'));
 }

 // Test for matchesStartTag method
 @Test
 public void testMatchesStartTag() {
 TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head></html>");
 Assert.assertTrue(queue.matchesStartTag());
 }

 // Test for matchChomp method with String
 @Test
 public void testMatchChompString() {
 TokenQueue queue = new TokenQueue("Hello World<html><head><title>Test</title></head></html>");
 Assert.assertTrue(queue.matchChomp("Hello World"));
 }

 // Test for matchesWhitespace method
 @Test
 public void testMatchesWhitespace() {
 TokenQueue queue = new TokenQueue(" Hello World ");
 Assert.assertTrue(queue.matchesWhitespace());
 }

 // Test for matchesWord method
 @Test
 public void testMatchesWord() {
 TokenQueue queue = new TokenQueue("Hello World");
 Assert.assertTrue(queue.matchesWord());
 }

 // Test for advance method
 @Test
 public void testAdvance() {
 TokenQueue queue = new TokenQueue("Hello World");
 queue.advance();
 Assert.assertEquals('H', queue.peek());
 }

 // Test for consume method with String
 @Test
 public void testConsumeString() {
 TokenQueue queue = new TokenQueue("Hello World");
 queue.consume("Hello");
 Assert.assertEquals("World", queue.toString());
 }

 // Test for consumeTo method with String
 @Test
 public void testConsumeToString() {
 TokenQueue queue = new TokenQueue("Hello World<html><head><title>Test</title></head></html>");
 Assert.assertEquals("World<html><head><title>Test</title></head></html>", queue.consumeTo("World"));
 }

 // Test for consumeToIgnoreCase method with String
 @Test
 public void testConsumeToIgnoreCaseString() {
 TokenQueue queue = new TokenQueue("Hello World<html><head><title>Test</title></head></html>");
 Assert.assertEquals("World<html><head><title>Test</title></head></html>", queue.consumeToIgnoreCase("world"));
 }

 // Test for consumeToAny method with String
 @Test
 public void testConsumeToAnyString() {
 TokenQueue queue = new TokenQueue("Hello World<html><head><title>Test</title></head></html>");
 Assert.assertEquals("World<html><head><title>Test</title></head></html>", queue.consumeToAny("World", "html"));
 }

 // Test for chompBalanced method with String
 @Test
 public void testChompBalancedString() {
 TokenQueue queue = new TokenQueue("(hello (world) world) (javascript:alert('Test'))");
 Assert.assertEquals("hello (world) world", queue.chompBalanced('(', ')'));
 }

 // Test for unescape method with String
 @Test
 public void testUnescapeString() {
 String str = "\\(hello \\(world\\) world\\) \\(javascript:alert('Test')\\)";
 String result = TokenQueue.unescape(str);
 Assert.assertEquals("(hello (world) world) (javascript:alert('Test'))", result);
 }

 // Test for consumeWhitespace method
 @Test
 public void testConsumeWhitespace() {
 TokenQueue queue = new TokenQueue(" Hello World   ");
 boolean consumed = queue.consumeWhitespace();
 Assert.assertTrue(consumed);
 }

 // Test for consumeWord method
 @Test
 public void testConsumeWord() {
 TokenQueue queue = new TokenQueue("Hello World");
 String consumed = queue.consumeWord();
 Assert.assertEquals("Hello", consumed);
 }

 // Test for consumeTagName method
 @Test
 public void testConsumeTagName() {
 TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head></html>");
 String consumed = queue.consumeTagName();
 Assert.assertEquals("html", consumed);
 }

 // Test for consumeElementSelector method
 @Test
 public void testConsumeElementSelector() {
 TokenQueue queue = new TokenQueue("html div p#id class1.class2");
 String consumed = queue.consumeElementSelector();
 Assert.assertEquals("html", consumed);
 }

 // Test for consumeCssIdentifier method
 @Test
 public void testConsumeCssIdentifier() {
 TokenQueue queue = new TokenQueue("class1.class2 id id class1");
 String consumed = queue.consumeCssIdentifier();
 Assert.assertEquals("class1", consumed);
 }

 // Test for consumeAttributeKey method
 @Test
 public void testConsumeAttributeKey() {
 TokenQueue queue = new TokenQueue("class1 class2 id id class1 class1 class1 class1 class1 class1");
 String consumed = queue.consumeAttributeKey();
 Assert.assertEquals("class1", consumed);
 }

 // Test for remainder method
 @Test
 public void testRemainder() {
 TokenQueue queue = new TokenQueue("Hello World");
 String remainder = queue.remainder();
 Assert.assertEquals("World", remainder);
 }
}