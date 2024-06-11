import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.StringUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenQueueTest {

 private TokenQueue queue;

 @Before
 public void setUp() {
 queue = new TokenQueue("abcdef<xyz>1234_AbCdEf");
 }

 @Test
 public void testIsEmpty() {
 assertTrue(queue.isEmpty());
 queue.addFirst('a');
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testPeek() {
 assertEquals('a', queue.peek());
 assertEquals('a', queue.consume());
 }

 @Test
 public void testAddFirst() {
 queue.addFirst('x');
 assertEquals('x', queue.peek());
 queue.addFirst('y');
 assertEquals('y', queue.peek());
 assertEquals('y', queue.consume());
 assertEquals('x', queue.consume());
 }

 @Test
 public void testMatches() {
 assertTrue(queue.matches("a"));
 assertTrue(queue.matches("ab"));
 assertFalse(queue.matches("c"));
 }

 @Test
 public void testMatchesCS() {
 assertTrue(queue.matchesCS("A"));
 assertFalse(queue.matchesCS("b"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(queue.matchesAny("a", "b"));
 assertFalse(queue.matchesAny("c", "d"));
 }

 @Test
 public void testMatchesAnyChar() {
 assertTrue(queue.matchesAny('a', 'b'));
 assertFalse(queue.matchesAny('c', 'd'));
 }

 @Test
 public void testMatchesStartTag() {
 assertFalse(queue.matchesStartTag());
 queue.addFirst('<');
 assertTrue(queue.matchesStartTag());
 queue.consume();
 assertFalse(queue.matchesStartTag());
 queue.addFirst('x');
 assertFalse(queue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 assertEquals("ab", queue.matchChomp("ab"));
 assertEquals("1234", queue.matchChomp("1234"));
 }

 @Test
 public void testMatchesWhitespace() {
 assertTrue(queue.matchesWhitespace());
 queue.advance();
 assertFalse(queue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 assertTrue(queue.matchesWord());
 queue.consume();
 assertFalse(queue.matchesWord());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeEmptyQueue() {
 queue.consume("xyz");
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeLongerThanQueue() {
 queue.consume("xyz123456789");
 }

 @Test
 public void testConsumeTo() {
 assertEquals("ab", queue.consumeTo("ab"));
 assertEquals("1234", queue.consumeTo("1234"));
 assertEquals("AbCdEf", queue.consumeTo(""));
 }

 @Test
 public void testConsumeToIgnoreCase() {
 assertEquals("ab", queue.consumeToIgnoreCase("ab"));
 assertEquals("1234", queue.consumeToIgnoreCase("1234"));
 assertEquals("AbCdEf", queue.consumeToIgnoreCase(""));
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("ab", queue.consumeToAny("a", "b"));
 assertEquals("123", queue.consumeToAny("1", "2", "3"));
 }

 @Test
 public void testChompBalanced() {
 assertEquals("abcdef", queue.chompBalanced('(', ')'));
 assertEquals("(one (two) three)", queue.chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 assertEquals("abcdef", TokenQueue.unescape("\\(abcdef\\)"));
 }

 @Test
 public void testConsumeWhitespace() {
 assertTrue(queue.consumeWhitespace());
 assertFalse(queue.consumeWhitespace());
 }

 @Test
 public void testConsumeWord() {
 assertEquals("AbCdEf", queue.consumeWord());
 assertEquals("", queue.consumeWord());
 }

 @Test
 public void testConsumeTagName() {
 assertEquals("tagName", queue.consumeTagName());
 assertEquals("", queue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 assertEquals("tagName|namespace", queue.consumeElementSelector());
 assertEquals("", queue.consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 assertEquals("identifier", queue.consumeCssIdentifier());
 assertEquals("", queue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 assertEquals("attributeKey", queue.consumeAttributeKey());
 assertEquals("", queue.consumeAttributeKey());
 }

 @Test
 public void testRemainder() {
 assertEquals("", queue.remainder());
 }

 @Test
 public void testToString() {
 assertEquals("", queue.toString());
 }
}