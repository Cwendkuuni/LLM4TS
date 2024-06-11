import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setup() {
 tokenQueue = new TokenQueue("testData");
 }

 @Test
 public void testIsEmpty() {
 assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('t');

 assertEquals('t', tokenQueue.peek());
 }

 @Test(expected = IllegalStateException.class)
 public void testPeekEmptyQueueThrowsException() {
 tokenQueue.peek();
 }

 @Test
 public void testAddFirstString() {
 tokenQueue.addFirst("testString");
 assertEquals("testString", tokenQueue.toString());
 }

 @Test
 public void testAddFirstChar() {
 tokenQueue.addFirst('t');
 assertEquals('t', tokenQueue.peek());
 }

 @Test
 public void testMatches() {
 tokenQueue.addFirst("testString");
 tokenQueue.addFirst("test");

 assertTrue(tokenQueue.matches("test"));
 }

 @Test
 public void testMatchesCS() {
 tokenQueue.addFirst("TestString");
 tokenQueue.addFirst("TEST");

 assertTrue(tokenQueue.matchesCS("TEST"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue.addFirst("abcd");
 tokenQueue.addFirst("ab");
 tokenQueue.addFirst("cd");

 assertTrue(tokenQueue.matchesAny("ab", "cd"));
 }

 @Test
 public void testMatchesAnyChars() {
 tokenQueue.addFirst("abcd");
 tokenQueue.addFirst('a');
 tokenQueue.addFirst('d');

 assertTrue(tokenQueue.matchesAny('a', 'd'));
 }

 @Test
 public void testMatchesStartTag() {
 tokenQueue.addFirst("<test>");

 assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue.addFirst("testString");

 assertTrue(tokenQueue.matchChomp("test"));
 }

 @Test
 public void testMatchesWhitespace() {
 tokenQueue.addFirst(" test ");

 assertTrue(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 tokenQueue.addFirst("testWord");

 assertTrue(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('t');

 tokenQueue.advance();
 tokenQueue.advance();

 assertEquals('s', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 tokenQueue.addFirst('t');
 tokenQueue.addFirst('e');
 tokenQueue.addFirst('s');
 tokenQueue.addFirst('t');

 assertEquals('t', tokenQueue.consume());
 assertEquals('e', tokenQueue.consume());
 assertEquals('s', tokenQueue.consume());
 assertEquals('t', tokenQueue.consume());

 assertTrue(tokenQueue.isEmpty());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeEmptyQueueThrowsException() {
 tokenQueue.consume();
 }

 @Test
 public void testConsumeString() {
 tokenQueue.addFirst("testString");

 assertEquals("test", tokenQueue.consumeTo("est"));
 }

 @Test
 public void testConsumeIgnoreCase() {
 tokenQueue.addFirst("TestString");

 assertEquals("test", tokenQueue.consumeToIgnoreCase("test"));
 }

 @Test
 public void testConsumeToAny() {
 tokenQueue.addFirst("testabcd");

 assertEquals("test", tokenQueue.consumeToAny("test"));
 }

 @Test
 public void testChompBalanced() {
 tokenQueue.addFirst("(test (one (two) three) four) five");

 assertEquals("test (one (two) three)", tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 assertEquals("\\test\\\\string", TokenQueue.unescape("\\test\\\\string"));
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue.addFirst(" test ");

 assertTrue(tokenQueue.consumeWhitespace());
 }

 @Test
 public void testConsumeWord() {
 tokenQueue.addFirst("testWord");

 assertEquals("testWord", tokenQueue.consumeWord());
 }

 @Test
 public void testConsumeTagName() {
 tokenQueue.addFirst("<testTagName>");

 assertEquals("testTagName", tokenQueue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 tokenQueue.addFirst("testDiv p:class(testClass)");

 assertEquals("testDiv", tokenQueue.consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 tokenQueue.addFirst("testId:class(testClass)");

 assertEquals("testId", tokenQueue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 tokenQueue.addFirst("testAttribute key=\"value\"");

 assertEquals("attribute", tokenQueue.consumeAttributeKey());
 }

 @Test
 public void testRemainder() {
 tokenQueue.addFirst("testData remainder");

 assertEquals("remainder", tokenQueue.remainder());
 }
}