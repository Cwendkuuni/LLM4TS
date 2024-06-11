import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Test
 public void testConstructor() {
 tokenQueue = new TokenQueue("Test data");
 Assert.assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testIsEmpty() {
 tokenQueue = new TokenQueue("");
 Assert.assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 tokenQueue = new TokenQueue("Test data");
 Assert.assertEquals('T', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue = new TokenQueue("Test data");
 tokenQueue.addFirst('A');
 tokenQueue.addFirst("Test");
 Assert.assertEquals("TestA", tokenQueue.consumeWord());
 }

 @Test
 public void testMatches() {
 tokenQueue = new TokenQueue("TestTest");
 Assert.assertTrue(tokenQueue.matches("Test"));
 }

 @Test
 public void testMatchesCS() {
 tokenQueue = new TokenQueue("TestTest");
 Assert.assertFalse(tokenQueue.matchesCS("test"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue = new TokenQueue("TestTestTest");
 Assert.assertTrue(tokenQueue.matchesAny("est", "es"));
 }

 @Test
 public void testMatchesAnyChar() {
 tokenQueue = new TokenQueue("TestTestTest");
 Assert.assertTrue(tokenQueue.matchesAny('s', 't'));
 }

 @Test
 public void testMatchesStartTag() {
 tokenQueue = new TokenQueue("<Test>Test<Test>");
 Assert.assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue = new TokenQueue("Test<Test>Test>");
 Assert.assertTrue(tokenQueue.matchChomp("<Test>"));
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue = new TokenQueue(" Test Test ");
 Assert.assertTrue(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue = new TokenQueue("TestTestTest");
 Assert.assertTrue(tokenQueue.matchesWord());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 tokenQueue.consume("Test");
 }

 @Test
 public void testConsumeTo() {
 tokenQueue = new TokenQueue("TestTestTestTestTest");
 Assert.assertEquals("TestTest", tokenQueue.consumeTo("Test"));
 }

 @Test
 public void testConsumeToIgnoreCase() {
 tokenQueue = new TokenQueue("TestTestTesttestTestTest");
 Assert.assertEquals("TestTesttest", tokenQueue.consumeToIgnoreCase("TestTesttest"));
 }

 @Test
 public void testConsumeToAny() {
 tokenQueue = new TokenQueue("TestTestTestTestTestTestTestTestTestTestTest");
 Assert.assertEquals("TestTest", tokenQueue.consumeToAny("TestTest"));
 }

 @Test
 public void testChompBalanced() {
 tokenQueue = new TokenQueue("(Test)(Test)Test");
 Assert.assertEquals("Test(Test)", tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 Assert.assertEquals("abc\\def", TokenQueue.unescape("abc\\def"));
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue = new TokenQueue(" Test Test ");
 Assert.assertTrue(tokenQueue.consumeWhitespace());
 }

 @Test
 public void testConsumeWord() {
 tokenQueue = new TokenQueue("TestTestTest");
 Assert.assertEquals("Test", tokenQueue.consumeWord());
 Assert.assertEquals("Test", tokenQueue.consumeWord());
 Assert.assertEquals("Test", tokenQueue.consumeWord());
 Assert.assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testConsumeTagName() {
 tokenQueue = new TokenQueue("<TestTagName>TestTagName</TestTagName>");
 Assert.assertEquals("TestTagName", tokenQueue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 tokenQueue = new TokenQueue("div p.class:hover");
 Assert.assertEquals("div", tokenQueue.consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 tokenQueue = new TokenQueue("class:test-identifier-test");
 Assert.assertEquals("test-identifier", tokenQueue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 tokenQueue = new TokenQueue("id=\"test-attribute-key\" class=\"test-class\"");
 Assert.assertEquals("test-attribute", tokenQueue.consumeAttributeKey());
 }

 @Test
 public void testRemainder() {
 tokenQueue = new TokenQueue("TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest");
 String remainder = tokenQueue.remainder();
 Assert.assertEquals("TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest", remainder);
 }
}