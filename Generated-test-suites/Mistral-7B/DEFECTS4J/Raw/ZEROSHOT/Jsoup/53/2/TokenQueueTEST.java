import org.jsoup.parser.TokenQueue;
import org.junit.Assert;
import org.junit.Test;

public class TokenQueueTest {

 @Test
 public void testTokenQueue_constructor() {
 TokenQueue tokenQueue = new TokenQueue("abcdef");
 Assert.assertFalse(tokenQueue.isEmpty());
 Assert.assertEquals("abcdef", tokenQueue.toString());
 }

 @Test
 public void testTokenQueue_isEmpty() {
 TokenQueue tokenQueue = new TokenQueue("");
 Assert.assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testTokenQueue_remainingLength() {
 TokenQueue tokenQueue = new TokenQueue("abcdef");
 Assert.assertEquals(5, tokenQueue.remainingLength());
 }

 @Test
 public void testTokenQueue_peek() {
 TokenQueue tokenQueue = new TokenQueue("abcdef");
 Assert.assertEquals('a', tokenQueue.peek());
 }

 @Test
 public void testTokenQueue_addFirst() {
 TokenQueue tokenQueue = new TokenQueue("abcdef");
 tokenQueue.addFirst('x');
 Assert.assertEquals("xabcdef", tokenQueue.toString());
 }

 @Test
 public void testTokenQueue_matches() {
 TokenQueue tokenQueue = new TokenQueue("abcdef");
 Assert.assertTrue(tokenQueue.matches("ab"));
 }

 @Test
 public void testTokenQueue_matchesCS() {
 TokenQueue tokenQueue = new TokenQueue("abcDEF");
 Assert.assertTrue(tokenQueue.matchesCS("ab"));
 }

 @Test
 public void testTokenQueue_matchesAny() {
 TokenQueue tokenQueue = new TokenQueue("abcdefghijklmnopqrstuvwxyz");
 Assert.assertTrue(tokenQueue.matchesAny("xyz"));
 }

 @Test
 public void testTokenQueue_matchesAny_char() {
 TokenQueue tokenQueue = new TokenQueue("abcdefghijklmnopqrstuvwxyz");
 Assert.assertTrue(tokenQueue.matchesAny('x'));
 }

 @Test
 public void testTokenQueue_matchesStartTag() {
 TokenQueue tokenQueue = new TokenQueue("<tag>");
 Assert.assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testTokenQueue_matchChomp() {
 TokenQueue tokenQueue = new TokenQueue("abc<tag>def");
 Assert.assertTrue(tokenQueue.matchChomp("<tag>"));
 }

 @Test
 public void testTokenQueue_consumeTo() {
 TokenQueue tokenQueue = new TokenQueue("abc<tag>def");
 Assert.assertEquals("abc<tag>", tokenQueue.consumeTo("<tag>"));
 }

 @Test
 public void testTokenQueue_chompBalanced() {
 TokenQueue tokenQueue = new TokenQueue("(())");
 Assert.assertEquals("()", tokenQueue.chompBalanced('(', ')'));
 }

 @Test(expected = IllegalStateException.class)
 public void testTokenQueue_consumeTo_notFound() {
 TokenQueue tokenQueue = new TokenQueue("abc<tag>def");
 tokenQueue.consumeTo("<TAG>");
 }

 @Test
 public void testTokenQueue_consumeWhitespace() {
 TokenQueue tokenQueue = new TokenQueue(" abc def ");
 Assert.assertTrue(tokenQueue.consumeWhitespace());
 Assert.assertEquals("def", tokenQueue.toString());
 }

 @Test
 public void testTokenQueue_consumeWord() {
 TokenQueue tokenQueue = new TokenQueue("abc def ghij");
 Assert.assertEquals("def", tokenQueue.consumeWord());
 Assert.assertEquals("ghij", tokenQueue.toString());
 }

 @Test
 public void testTokenQueue_consumeTagName() {
 TokenQueue tokenQueue = new TokenQueue("<tag name>");
 Assert.assertEquals("tag name", tokenQueue.consumeTagName());
 }

 @Test
 public void testTokenQueue_consumeElementSelector() {
 TokenQueue tokenQueue = new TokenQueue("tag name | class-name");
 Assert.assertEquals("tag name", tokenQueue.consumeElementSelector());
 }

 @Test
 public void testTokenQueue_consumeCssIdentifier() {
 TokenQueue tokenQueue = new TokenQueue("id name class-name");
 Assert.assertEquals("id name", tokenQueue.consumeCssIdentifier());
 }

 @Test
 public void testTokenQueue_consumeAttributeKey() {
 TokenQueue tokenQueue = new TokenQueue("name=\"value\" class=\"class-name\"");
 Assert.assertEquals("name", tokenQueue.consumeAttributeKey());
 }

 @Test
 public void testTokenQueue_remainder() {
 TokenQueue tokenQueue = new TokenQueue("abcdefghijklmnopqrstuvwxyz");
 String remainder = tokenQueue.remainder();
 Assert.assertEquals("remainder", remainder);
 }

 @Test
 public void testTokenQueue_toString() {
 TokenQueue tokenQueue = new TokenQueue("abcdef");
 Assert.assertEquals("def", tokenQueue.toString());
 }
}