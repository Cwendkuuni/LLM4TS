import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 @Test
 public void testIsEmpty() {
 TokenQueue tq = new TokenQueue("");
 Assert.assertTrue(tq.isEmpty());
 }

 @Test
 public void testPeekAndAddFirst() {
 TokenQueue tq = new TokenQueue("hello world");
 Assert.assertEquals('h', tq.peek());
 tq.addFirst('!');
 Assert.assertEquals('!', tq.peek());
 }

 @Test
 public void testMatches() {
 TokenQueue tq = new TokenQueue("hello world");
 Assert.assertTrue(tq.matches("hello"));
 Assert.assertFalse(tq.matches("world"));
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumerSequencesThatDoNotMatch() {
 TokenQueue tq = new TokenQueue("hello world");
 tq.consume("world");
 }

 @Test
 public void testConsumeWhitespace() {
 TokenQueue tq = new TokenQueue(" hello world ");
 boolean seen = tq.consumeWhitespace();
 Assert.assertTrue(seen);
 }

 @Test
 public void testConsumeWord() {
 TokenQueue tq = new TokenQueue("hello world");
 String word = tq.consumeWord();
 Assert.assertEquals("hello", word);
 }

 @Test
 public void testConsumeTagName() {
 TokenQueue tq = new TokenQueue("<html><head><title>Test</title></head><body></body></html>");
 String tagName = tq.consumeTagName();
 Assert.assertEquals("html", tagName);
 }

 @Test
 public void testConsumeElementSelector() {
 TokenQueue tq = new TokenQueue("div#test p span.class");
 String elementSelector = tq.consumeElementSelector();
 Assert.assertEquals("div", elementSelector);
 }

 @Test
 public void testConsumeCssIdentifier() {
 TokenQueue tq = new TokenQueue("id:test class:class");
 String cssIdentifier = tq.consumeCssIdentifier();
 Assert.assertEquals("test", cssIdentifier);
 }

 @Test
 public void testConsumeAttributeKey() {
 TokenQueue tq = new TokenQueue("class:class name:name");
 String attributeKey = tq.consumeAttributeKey();
 Assert.assertEquals("class", attributeKey);
 }

 @Test
 public void testChompBalanced() {
 TokenQueue tq = new TokenQueue("(hello (world) (hello world))");
 String expected = "hello (world)";
 String actual = tq.chompBalanced('(', ')');
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeTo() {
 TokenQueue tq = new TokenQueue("hello (world) (hello world)");
 String expected = "hello (world)";
 String actual = tq.consumeTo("(");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToAny() {
 TokenQueue tq = new TokenQueue("hello world hello (world) world");
 String expected = "hello world";
 String actual = tq.consumeToAny("hello", "world");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testChompBalancedInvalidInput() {
 TokenQueue tq = new TokenQueue("(hello world)");
 // expected IllegalArgumentException
 tq.chompBalanced(')','(');
 }

 @Test
 public void testUnescapedString() {
 TokenQueue tq = new TokenQueue("\\(hello\\ world\\)");
 String expected = "(hello world)";
 String actual = new TokenQueue().unescape(tq.consumeTo("\\(").toString());
 Assert.assertEquals(expected, actual);
 }
}