import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setUp() {
 tokenQueue = new TokenQueue("some data for testing");
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(tokenQueue.isEmpty());
 tokenQueue.advance();
 Assert.assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 tokenQueue.addFirst('a');
 Assert.assertEquals('a', tokenQueue.peek());
 }

 @Test(expected = IllegalStateException.class)
 public void testPeekAfterConsume() {
 tokenQueue.addFirst('a');
 tokenQueue.consume();
 tokenQueue.peek();
 }

 @Test
 public void testAddFirst() {
 tokenQueue.addFirst('a');
 Assert.assertEquals('a', tokenQueue.peek());
 }

 @Test
 public void testAddFirstString() {
 tokenQueue.addFirst("abc");
 Assert.assertEquals('a', tokenQueue.peek());
 }

 @Test
 public void testMatches() {
 tokenQueue.addFirst("abcdef");
 Assert.assertTrue(tokenQueue.matches("ab"));
 Assert.assertFalse(tokenQueue.matches("xyz"));
 }

 @Test
 public void testMatchesCS() {
 tokenQueue.addFirst("abcdef");
 Assert.assertTrue(tokenQueue.matchesCS("ab"));
 Assert.assertFalse(tokenQueue.matchesCS("xyz"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue.addFirst("abcdef");
 Assert.assertTrue(tokenQueue.matchesAny("ab", "xy"));
 Assert.assertFalse(tokenQueue.matchesAny("pqr"));
 }

 @Test
 public void testMatchesAnyChars() {
 tokenQueue.addFirst("abcdef");
 Assert.assertTrue(tokenQueue.matchesAny('a', 'b'));
 Assert.assertFalse(tokenQueue.matchesAny('x', 'y'));
 }

 @Test
 public void testMatchesStartTag() {
 tokenQueue.addFirst("<abc>");
 Assert.assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue.addFirst("abcdef");
 Assert.assertEquals("ab", tokenQueue.matchChomp("ab"));
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue.addFirst(" abc ");
 Assert.assertTrue(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue.addFirst("abcdef");
 Assert.assertTrue(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 tokenQueue.addFirst("abcdef");
 tokenQueue.advance();
 Assert.assertEquals('b', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 tokenQueue.addFirst("abcdef");
 Assert.assertEquals('a', tokenQueue.consume());
 Assert.assertEquals('b', tokenQueue.peek());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeEmptyQueue() {
 tokenQueue.consume();
 }

 @Test
 public void testConsumeTo() {
 tokenQueue.addFirst("abcdef");
 Assert.assertEquals("ab", tokenQueue.consumeTo("ab"));
 }

 @Test
 public void testConsumeToIgnoreCase() {
 tokenQueue.addFirst("ABCDEF");
 Assert.assertEquals("ab", tokenQueue.consumeToIgnoreCase("ab"));
 }

 @Test
 public void testConsumeToAny() {
 tokenQueue.addFirst("abcdef");
 Assert.assertEquals("def", tokenQueue.consumeToAny("xy", "abdef"));
 }

 @Test
 public void testChompBalanced() {
 tokenQueue.addFirst("((())())");
 Assert.assertEquals("((())", tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 Assert.assertEquals("ab\\cd\\ef\\gh\\ij", TokenQueue.unescape("ab\\cd\\ef\\gh\\ij"));
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue.addFirst(" abc def ");
 Assert.assertTrue(tokenQueue.consumeWhitespace());
 }

 @Test
 public void testConsumeWord() {
 tokenQueue.addFirst("abcdef");
 Assert.assertEquals("abc", tokenQueue.consumeWord());
 }

 @Test
 public void testConsumeTagName() {
 tokenQueue.addFirst("some-tag-name");
 Assert.assertEquals("some-tag-name", tokenQueue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 tokenQueue.addFirst("div.class:hover");
 Assert.assertEquals("div.class", tokenQueue.consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 tokenQueue.addFirst("class-identifier");
 Assert.assertEquals("class-identifier", tokenQueue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 tokenQueue.addFirst("attr-key");
 Assert.assertEquals("attr-key", tokenQueue.consumeAttributeKey());
 }

 @Test
 public void testRemainder() {
 tokenQueue.addFirst("some data for testing");
 String remainder = tokenQueue.remainder();
 Assert.assertEquals(" data for testing", remainder);
 }

 @Test
 public void testToString() {
 tokenQueue.addFirst("some data for testing");
 String toString = tokenQueue.toString();
 Assert.assertEquals(" data for testing", toString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTokenQueueWithNullData() {
 new TokenQueue(null);
 }
}