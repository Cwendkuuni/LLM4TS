import org.jsoup.parser.TokenQueue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setUp() {
 tokenQueue = new TokenQueue("test<tag testClass=\"testClass\" id=\"testId\" data-test=\"dataTest\"/>");
 }

 @After
 public void tearDown() {
 tokenQueue = null;
 }

 @Test
 public void testIsEmpty() {
 assertTrue(tokenQueue.isEmpty());
 tokenQueue.consume('t');
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 assertEquals('t', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue.addFirst('a');
 assertEquals('a', tokenQueue.peek());
 }

 @Test
 public void testMatches() {
 assertTrue(tokenQueue.matches("test"));
 assertFalse(tokenQueue.matches("other"));
 }

 @Test
 public void testMatchesCS() {
 assertTrue(tokenQueue.matchesCS("testClass"));
 assertFalse(tokenQueue.matchesCS("otherClass"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(tokenQueue.matchesAny("test", "testClass"));
 }

 @Test
 public void testMatchesAnyChar() {
 assertTrue(tokenQueue.matchesAny(new char[]{'t', 'e'}));
 }

 @Test
 public void testMatchesStartTag() {
 assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 assertTrue(tokenQueue.matchChomp("test"));
 }

 @Test
 public void testMatchesWhitespace() {
 assertTrue(tokenQueue.matchesWhitespace());
 tokenQueue.consumeWhitespace();
 assertFalse(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 assertTrue(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 tokenQueue.advance();
 assertEquals('e', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 assertEquals('t', tokenQueue.consume());
 }

 @Test
 public void testConsumeString() {
 tokenQueue.consume("testClass");
 assertEquals("id=\"testId\" data-test=\"dataTest\"", tokenQueue.toString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("testClass=\"testClass\" id=\"", tokenQueue.consumeTo("class"));
 }

 @Test
 public void testConsumeToIgnoreCase() {
 assertEquals("testClass=\"testClass\" id=\"", tokenQueue.consumeToIgnoreCase("class"));
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("id=\"testId\" data-test=\"dataTest\"", tokenQueue.consumeToAny("id", "data-test"));
 }

 @Test
 public void testChompBalanced() {
 assertEquals("test testClass", tokenQueue.chompBalanced('(', ')'));
 }

 @Test(expected = IllegalStateException.class)
 public void testChompBalancedUnbalanced() {
 tokenQueue.chompBalanced('(', '}');
 }
}