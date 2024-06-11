import org.jsoup.parser.TokenQueue;
import org.junit.Assert;
import org.junit.Test;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Test
 public void testTokenQueueConstructor() {
 tokenQueue = new TokenQueue("testString");
 Assert.assertNotNull(tokenQueue);
 }

 @Test
 public void testIsEmpty() {
 tokenQueue = new TokenQueue("testString");
 Assert.assertFalse(tokenQueue.isEmpty());
 tokenQueue.consume('t');
 Assert.assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 tokenQueue = new TokenQueue("testString");
 Assert.assertEquals('t', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue = new TokenQueue("testString");
 tokenQueue.addFirst('a');
 Assert.assertEquals('a', tokenQueue.consume());
 }

 @Test
 public void testMatches() {
 tokenQueue = new TokenQueue("testString");
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('S');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('r');
 tokenQueue.addFirst('i');
 tokenQueue.addFirst('n');
 tokenQueue.addFirst('g');

 Assert.assertTrue(tokenQueue.matches("testing"));
 }

 @Test
 public void testMatchesCS() {
 tokenQueue = new TokenQueue("testString");
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('S');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('r');
 tokenQueue.addFirst('i');
 tokenQueue.addFirst('n');
 tokenQueue.addFirst('g');

 Assert.assertFalse(tokenQueue.matchesCS("Testing"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue = new TokenQueue("testString");
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('S');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('r');
 tokenQueue.addFirst('i');
 tokenQueue.addFirst('n');
 tokenQueue.addFirst('g');

 Assert.assertTrue(tokenQueue.matchesAny("test", "ing"));
 }

 @Test
 public void testMatchesStartTag() {
 tokenQueue = new TokenQueue("<test>");
 Assert.assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue = new TokenQueue("testString");
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('i');
 tokenQueue.addFirst('n');
 tokenQueue.addFirst('g');

 Assert.assertTrue(tokenQueue.matchChomp("testing"));
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue = new TokenQueue("test\t string");
 Assert.assertTrue(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue = new TokenQueue("testWord string");
 Assert.assertTrue(tokenQueue.matchesWord());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeWithEmptyQueue() {
 tokenQueue = new TokenQueue("");
 tokenQueue.consume("test");
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeSequenceWithLongerSequenceThanAvailable() {
 tokenQueue = new TokenQueue("testString");
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');

 Assert.assertThrows(IllegalStateException.class, () -> tokenQueue.consume("testString"));
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeSequenceWithMismatchingSequence() {
 tokenQueue = new TokenQueue("testString");
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst('s');

 Assert.assertThrows(IllegalStateException.class, () -> tokenQueue.consume("testStringTest"));
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeSequenceWithSequenceStartingAfterCurrentPosition() {
 tokenQueue = new TokenQueue("testString");
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('t');

 Assert.assertThrows(IllegalStateException.class, () -> tokenQueue.consume("String"));
 }

 @Test
 public void testChompBalanced() {
 tokenQueue = new TokenQueue("((())())");
 Assert.assertEquals("((())", tokenQueue.chompBalanced('(', ')'));
 Assert.assertEquals("", tokenQueue.chompBalanced('(', '}'));
 }

 @Test
 public void testUnescape() {
 Assert.assertEquals("ab\\bc", TokenQueue.unescape("ab\\bc"));
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue = new TokenQueue(" test string ");
 Assert.assertTrue(tokenQueue.consumeWhitespace());
 }

 @Test
 public void testConsumeWord() {
 tokenQueue = new TokenQueue("testWord string");
 Assert.assertEquals("Word", tokenQueue.consumeWord());
 }

 @Test
 public void testConsumeTagName() {
 tokenQueue = new TokenQueue("<testTag>");
 Assert.assertEquals("testTag", tokenQueue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 tokenQueue = new TokenQueue("test|tagName");
 Assert.assertEquals("test", tokenQueue.consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 tokenQueue = new TokenQueue("id:testId");
 Assert.assertEquals("testId", tokenQueue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 tokenQueue = new TokenQueue("attribute:key");
 Assert.assertEquals("key", tokenQueue.consumeAttributeKey());
 }

 @Test
 public void testRemainder() {
 tokenQueue = new TokenQueue("testString");
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('i');
 tokenQueue.addFirst('n');
 tokenQueue.addFirst('g');

 String remainder = tokenQueue.remainder();
 Assert.assertEquals("", remainder);
 }
}