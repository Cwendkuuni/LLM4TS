import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setup() {
 String data = "example<tag>exampleTagName exampleTag example1 example2 example3 <exampleTag2>exampleTag2Name</exampleTag2>";
 tokenQueue = new TokenQueue(data);
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 Assert.assertEquals('e', tokenQueue.peek());
 tokenQueue.advance();
 Assert.assertEquals('x', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue.addFirst('A');
 Assert.assertEquals('A', tokenQueue.peek());
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(tokenQueue.matches("example"));
 Assert.assertTrue(tokenQueue.matches("tag"));
 Assert.assertTrue(tokenQueue.matches("exampleTagName"));
 Assert.assertFalse(tokenQueue.matches("noneOfThese"));
 }

 @Test
 public void testMatchesCS() {
 Assert.assertTrue(tokenQueue.matchesCS("tag"));
 Assert.assertFalse(tokenQueue.matchesCS("Tag"));
 }

 @Test
 public void testMatchesAny() {
 Assert.assertTrue(tokenQueue.matchesAny("tag", "name"));
 Assert.assertFalse(tokenQueue.matchesAny("noneOfThese"));
 }

 @Test
 public void testMatchesAnyChars() {
 Assert.assertTrue(tokenQueue.matchesAny('e', 'x'));
 Assert.assertFalse(tokenQueue.matchesAny('a', 'z'));
 }

 @Test
 public void testMatchesStartTag() {
 Assert.assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 Assert.assertTrue(tokenQueue.matchChomp("exampleTagName"));
 }

 @Test
 public void testMatchesWhitespace() {
 Assert.assertTrue(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 Assert.assertTrue(tokenQueue.matchesWord());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeWithNoMatch() {
 tokenQueue.consume("noneOfThese");
 }

 @Test
 public void testConsumeTo() {
 Assert.assertEquals("exampleTagName", tokenQueue.consumeTo("exampleTagName"));
 }

 @Test
 public void testConsumeToIgnoreCase() {
 Assert.assertEquals("exampleTagName", tokenQueue.consumeToIgnoreCase("exampleTagName"));
 }

 @Test
 public void testConsumeToAny() {
 Assert.assertEquals("example", tokenQueue.consumeToAny("example", "tag"));
 }

 @Test
 public void testChompBalanced() {
 Assert.assertEquals("exampleTag example1 example2 example3 ", tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 Assert.assertEquals("example\\(one \\(two\\)\\ three\\) four", TokenQueue.unescape("example\\(one \\(two\\)\\ three\\) four"));
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue.advance();
 tokenQueue.advance();
 tokenQueue.advance();
 tokenQueue.consumeWhitespace();
 Assert.assertTrue(tokenQueue.consumeWhitespace());
 }

 @Test
 public void testConsumeWord() {
 Assert.assertEquals("exampleTag", tokenQueue.consumeWord());
 }

 @Test
 public void testConsumeTagName() {
 Assert.assertEquals("tag", tokenQueue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 Assert.assertEquals("tag", tokenQueue.consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 Assert.assertEquals("exampleTagName", tokenQueue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 Assert.assertEquals("attributeKey", tokenQueue.consumeAttributeKey());
 }

 @Test
 public void testRemainder() {
 Assert.assertEquals("four", tokenQueue.remainder());
 }

 @Test
 public void testToString() {
 Assert.assertEquals("four", tokenQueue.toString());
 }
}