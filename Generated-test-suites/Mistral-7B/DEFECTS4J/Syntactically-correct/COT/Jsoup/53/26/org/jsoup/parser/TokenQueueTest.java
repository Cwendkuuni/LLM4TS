import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setUp() {
 tokenQueue = new TokenQueue("abcdefg<ab>cd<ef>ghi");
 }

 @Test
 public void testIsEmpty() {
 TokenQueue emptyQueue = new TokenQueue("");
 assertTrue(emptyQueue.isEmpty());
 tokenQueue.advance();
 assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
 assertEquals('a', tokenQueue.peek());
 assertEquals('a', tokenQueue.consume());
 }

 @Test
 public void testAddFirst() {
 tokenQueue.addFirst('z');
 assertEquals('z', tokenQueue.consume());
 }

 @Test
 public void testMatches() {
 assertTrue(tokenQueue.matches("ab"));
 assertFalse(tokenQueue.matches("cd"));
 }

 @Test
 public void testMatchesCS() {
 assertFalse(tokenQueue.matchesCS("ab"));
 assertTrue(tokenQueue.matchesCS("<ab>"));
 }

 @Test
 public void testMatchesAny() {
 String[] seq = {"a", "b", "c"};
 assertTrue(tokenQueue.matchesAny(seq));
 assertFalse(tokenQueue.matchesAny(new String[]{"d", "e"}));
 }

 @Test
 public void testMatchesAnyChars() {
 char[] seq = {'a', 'b', 'c'};
 assertTrue(tokenQueue.matchesAny(seq));
 assertFalse(tokenQueue.matchesAny(new char[]{'d', 'e'}));
 }

 @Test
 public void testMatchesStartTag() {
 assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 tokenQueue.advance();
 tokenQueue.advance();
 tokenQueue.matchChomp("ab");
 assertEquals("cd<ef>", tokenQueue.toString());
 }

 @Test
 public void testMatchesWhitespace() {
 assertTrue(tokenQueue.matchesWhitespace());
 tokenQueue.consumeWhitespace();
 tokenQueue.consumeWhitespace();
 assertFalse(tokenQueue.matchesWhitespace());
 }

 @Test
 public void testMatchesWord() {
 assertTrue(tokenQueue.matchesWord());
 tokenQueue.advance();
 assertFalse(tokenQueue.matchesWord());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeInvalidSequence() {
 tokenQueue.consume("ef");
 }

 @Test
 public void testConsumeTo() {
 assertEquals("ab<ef>", tokenQueue.consumeTo("ab<ef>"));
 assertEquals("cd<ef>ghi", tokenQueue.toString());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 assertEquals("ef", tokenQueue.consumeToIgnoreCase("ef"));
 assertEquals("cd<ef>ghi", tokenQueue.toString());
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("cd", tokenQueue.consumeToAny("ab", "cd"));
 assertEquals("ghi", tokenQueue.toString());
 }

 @Test
 public void testChompBalanced() {
 assertEquals("abcdef", tokenQueue.chompBalanced('(', ')'));
 assertEquals("", tokenQueue.toString());
 }

 @Test
 public void testUnescape() {
 assertEquals("abcdef\\(abcdef\\)", TokenQueue.unescape("abcdef\\(abcdef\\)\\"));
 }

 @Test
 public void testConsumeWhitespace() {
 tokenQueue.advance();
 tokenQueue.advance();
 tokenQueue.consumeWhitespace();
 tokenQueue.consumeWhitespace();
 assertEquals("cd<ef>ghi", tokenQueue.toString());
 }

 @Test
 public void testConsumeWord() {
 assertEquals("ef", tokenQueue.consumeWord());
 assertEquals("cd<ef>ghi", tokenQueue.toString());
 }

 @Test
 public void testConsumeTagName() {
 assertEquals("ef", tokenQueue.consumeTagName());
 assertEquals("cd<ef>ghi", tokenQueue.toString());
 }

 @Test
 public void testConsumeElementSelector() {
 assertEquals("ef", tokenQueue.consumeElementSelector());
 assertEquals("cd<ef>ghi", tokenQueue.toString());
 }

 @Test
 public void testConsumeCssIdentifier() {
 assertEquals("ef", tokenQueue.consumeCssIdentifier());
 assertEquals("cd<ef>ghi", tokenQueue.toString());
 }

 @Test
 public void testConsumeAttributeKey() {
 assertEquals("ef", tokenQueue.consumeAttributeKey());
 assertEquals("cd<ef>ghi", tokenQueue.toString());
 }

 @Test
 public void testRemainder() {
 assertEquals("ghi", tokenQueue.remainder());
 }
}