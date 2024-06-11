import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setUp() {
 tokenQueue = new TokenQueue("This is a test string for TokenQueue.");
 }

 @Test
 public void testIsEmpty() {
 assertTrue(tokenQueue.isEmpty());
 tokenQueue.consume('T');
 assertFalse(tokenQueue.isEmpty());
 tokenQueue.consumeToIgnoreCase("is");
 assertTrue(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 assertEquals('T', tokenQueue.peek());
 tokenQueue.consume('T');
 assertEquals('h', tokenQueue.peek());
 }

 @Test
 public void testAddFirst() {
 tokenQueue.addFirst('X');
 assertEquals('X', tokenQueue.peek());
 tokenQueue.consume();
 assertEquals('h', tokenQueue.peek());
 }

 @Test
 public void testMatches() {
 assertTrue(tokenQueue.matches("is"));
 tokenQueue.consumeToIgnoreCase("is");
 assertFalse(tokenQueue.matches("is"));
 }

 @Test
 public void testMatchesCS() {
 assertTrue(tokenQueue.matchesCS("is"));
 tokenQueue.consumeTo("is");
 assertFalse(tokenQueue.matchesCS("is"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue.consumeToIgnoreCase("is");
 assertFalse(tokenQueue.matchesAny("is"));
 tokenQueue.addFirst('a');
 assertTrue(tokenQueue.matchesAny("at"));
 }

 @Test
 public void testMatchesWhitespace() {
 assertFalse(tokenQueue.matchesWhitespace());
 tokenQueue.consume(' ');
 assertTrue(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 assertFalse(tokenQueue.matchesWord());
 tokenQueue.consume('h');
 assertTrue(tokenQueue.matchesWord());
 }

 @Test
 public void testAdvance() {
 tokenQueue.advance();
 assertEquals('h', tokenQueue.peek());
 }

 @Test
 public void testConsume() {
 assertEquals('h', tokenQueue.consume());
 assertEquals('i', tokenQueue.peek());
 }

 @Test
 public void testConsumeCS() {
 tokenQueue.consumeToIgnoreCase("is");
 tokenQueue.consumeToIgnoreCase("a ");
 tokenQueue.consumeCS("test");
 assertEquals('s', tokenQueue.peek());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("is a test", tokenQueue.consumeTo(" is"));
 assertEquals("string for TokenQueue.", tokenQueue.consumeTo("Queue."));
 assertNull(tokenQueue.consumeTo("not present"));
 }

 @Test
 public void testConsumeToAny() {
 tokenQueue.consumeToIgnoreCase("is a test");
 tokenQueue.consumeToAny("Queue.", "for", "String");
 assertEquals("Token", tokenQueue.consumeToIgnoreCase("Token"));
 }

 @Test
 public void testChompBalanced() {
 assertEquals("This", tokenQueue.chompBalanced('(', ')'));
 tokenQueue.addFirst('(');
 assertEquals("is a test", tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 assertEquals("This is a test string for TokenQueue.", TokenQueue.unescape(tokenQueue.remainder()));
 }
}