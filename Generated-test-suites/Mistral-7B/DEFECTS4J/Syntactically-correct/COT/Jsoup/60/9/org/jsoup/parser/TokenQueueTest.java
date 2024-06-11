import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setup() {
 tokenQueue = new TokenQueue("abcdef<xyz>123<abc>");
 }

 @Test
 public void testIsEmpty() {
 TokenQueue emptyQueue = new TokenQueue("");
 assertTrue(emptyQueue.isEmpty());
 tokenQueue = new TokenQueue("abc");
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 assertEquals('a', tokenQueue.peek());
 tokenQueue.advance();
 assertEquals('b', tokenQueue.peek());
 }

 @Test(expected = IllegalStateException.class)
 public void testPeekEmptyQueue() {
 TokenQueue emptyQueue = new TokenQueue("");
 tokenQueue = emptyQueue;
 tokenQueue.peek();
 }

 @Test
 public void testAddFirstString() {
 tokenQueue.addFirst("xyz");
 assertEquals("xyzabcdef<xyz>123<abc>", tokenQueue.toString());
 }

 @Test
 public void testAddFirstCharacter() {
 tokenQueue.addFirst('w');
 assertEquals("wabcdef<xyz>123<abc>", tokenQueue.toString());
 }

 @Test
 public void testMatches() {
 assertTrue(tokenQueue.matches("abc"));
 tokenQueue.advance();
 tokenQueue.advance();
 assertFalse(tokenQueue.matches("abc"));
 }

 @Test
 public void testMatchesCS() {
 tokenQueue.advance();
 tokenQueue.advance();
 assertTrue(tokenQueue.matchesCS("ef"));
 }

 @Test
 public void testMatchesAny() {
 tokenQueue.advance();
 tokenQueue.advance();
 tokenQueue.advance();
 assertTrue(tokenQueue.matchesAny("ef", "xyz"));
 }

 @Test
 public void testMatchesAnyCharArray() {
 tokenQueue.advance();
 tokenQueue.advance();
 tokenQueue.advance();
 assertTrue(tokenQueue.matchesAny(new char[]{'e', 'f'}));
 }

 @Test
 public void testMatchesStartTag() {
 assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue.addFirst("xyz");
 tokenQueue.consume("xyz");
 assertTrue(tokenQueue.matchChomp("xyz"));
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
 tokenQueue.advance();
 assertFalse(tokenQueue.matchesWord());
 }

 @Test
 public void testConsume() {
 assertEquals('a', tokenQueue.consume());
 assertEquals('b', tokenQueue.consume());
 tokenQueue.advance();
 assertEquals('c', tokenQueue.consume());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeEmptyQueue() {
 tokenQueue = new TokenQueue("");
 tokenQueue.consume();
 }

 @Test
 public void testConsumeString() {
 tokenQueue.consume("xyz");
 assertEquals("abcdef<abc>", tokenQueue.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeStringNoMatch() {
 tokenQueue.consume("efg");
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeStringShortQueue() {
 tokenQueue = new TokenQueue("xyz");
 tokenQueue.consume("xy");
 }

 @Test
 public void testConsumeTo() {
 assertEquals("abc", tokenQueue.consumeTo("abc"));
 assertEquals("def<abc>", tokenQueue.toString());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 assertEquals("ef<abc>", tokenQueue.toString());
 assertEquals("def", tokenQueue.consumeToIgnoreCase("def"));
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("xyz", tokenQueue.consumeToAny("xy", "z"));
 assertEquals("def<abc>", tokenQueue.toString());
 }

 @Test
 public void testChompBalanced() {
 assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testChompBalancedEmpty() {
 assertEquals("", tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testChompBalancedUnmatched() {
 assertEquals("", tokenQueue.chompBalanced('(', '}'));
 }

 @Test
 public void testChompBalancedQuoted() {
 assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
 tokenQueue.addFirst("'");
 assertEquals("one '(two) three'", tokenQueue.toString());
 assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testChompBalancedEscaped() {
 assertEquals("one (two) three", tokenQueue.toString());
 tokenQueue.addFirst("\\(");
 tokenQueue.addFirst("abc");
 tokenQueue.addFirst("\\)");
 assertEquals("abc, one (two) three", tokenQueue.toString());
 assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
 }

 @Test
 public void testUnescape() {
 assertEquals("abc\\def\\\\", TokenQueue.unescape("abc\\def\\\\\\"));
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue.addFirst(" ");
 tokenQueue.consumeWhitespace();
 tokenQueue.addFirst(" x");
 assertTrue(tokenQueue.matchesWhitespace());
 tokenQueue.consumeWhitespace();
 assertFalse(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testConsumeWord() {
 assertEquals("abc", tokenQueue.consumeWord());
 }

 @Test
 public void testConsumeTagName() {
 assertEquals("abc", tokenQueue.consumeTagName());
 }

 @Test
 public void testConsumeElementSelector() {
 assertEquals("abc", tokenQueue.consumeElementSelector());
 }

 @Test
 public void testConsumeCssIdentifier() {
 assertEquals("abc", tokenQueue.consumeCssIdentifier());
 }

 @Test
 public void testConsumeAttributeKey() {
 assertEquals("abc", tokenQueue.consumeAttributeKey());
 }

 @Test
 public void testRemainder() {
 assertEquals("", tokenQueue.remainder());
 tokenQueue.addFirst("xyz");
 assertEquals("xyz", tokenQueue.remainder());
 }
}