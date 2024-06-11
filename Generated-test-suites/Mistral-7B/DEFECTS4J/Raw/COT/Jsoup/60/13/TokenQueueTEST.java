import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

 private TokenQueue tokenQueue;

 @Before
 public void setUp() {
 tokenQueue = new TokenQueue("abcdef<xyz>123");
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(tokenQueue.isEmpty());
 }

 @Test
 public void testPeek() {
char expected = 'a';
 char actual = tokenQueue.peek();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testAddFirst() {
 tokenQueue.addFirst('g');
 tokenQueue.addFirst("hij");
 char expected = 'g';
 char actual = tokenQueue.peek();
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullData() {
 // Given
 TokenQueue tokenQueue = null;

 // When
 // Then
 Assert.assertThrows(IllegalArgumentException.class, () -> tokenQueue = new TokenQueue(null));
 }

 @Test
 public void testMatches() {
 String expected = "xyz";
 String actual = tokenQueue.matches("xyz") ? tokenQueue.consume(3) : "";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMatchesCS() {
 String expected = "<xyz>";
 String actual = tokenQueue.matchesCS("xyz") ? tokenQueue.consume(3) : "";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMatchesAny() {
 String[] expected = {"xyz", "123"};
 String[] actual = tokenQueue.matchesAny("xyz", "123") ? new String[2] : new String[0];
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testMatchesStartTag() {
 Assert.assertTrue(tokenQueue.matchesStartTag());
 }

 @Test
 public void testMatchChomp() {
 String expected = "xyz";
 String actual = tokenQueue.matchChomp("xyz");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMatchesWhitespace() {
 char expected = ' ';
 char actual = tokenQueue.matchesWhitespace() ? tokenQueue.consume() : 0;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMatchesWord() {
 char expected = 'd';
 char actual = tokenQueue.matchesWord() ? tokenQueue.consume() : 0;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testAdvance() {
 tokenQueue.advance();
 char expected = 'b';
 char actual = tokenQueue.peek();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsume() {
 char expected = 'b';
 char actual = tokenQueue.consume();
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeWithUnmatchedSequence() {
 // Given
 String seq = "xyz";

 // When
 // Then
 tokenQueue.consume(seq);
 }

 @Test
 public void testConsumeTo() {
 String expected = "xyz";
 String actual = tokenQueue.consumeTo("xyz");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testChompBalanced() {
 char open = '<';
 char close = '>';
 String expected = "abcdef";
 String actual = tokenQueue.chompBalanced(open, close);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testUnescape() {
 String input = "\\\\xyz";
 String expected = "xyz";
 String actual = TokenQueue.unescape(input);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeWhitespace() {
 boolean seen = false;
 tokenQueue.consumeWhitespace();
 tokenQueue.consumeWhitespace();
 tokenQueue.consumeWhitespace();
 seen = tokenQueue.consumeWhitespace();
 Assert.assertTrue(seen);
 }

 @Test
 public void testConsumeWord() {
 String expected = "def";
 String actual = tokenQueue.consumeWord();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeTagName() {
 String expected = "xyz";
 String actual = tokenQueue.consumeTagName();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeElementSelector() {
 String expected = "xyz";
 String actual = tokenQueue.consumeElementSelector();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeCssIdentifier() {
 String expected = "123";
 String actual = tokenQueue.consumeCssIdentifier();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeAttributeKey() {
 String expected = "class";
 String actual = tokenQueue.consumeAttributeKey();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testRemainder() {
 String expected = "";
 String actual = tokenQueue.remainder();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
 String expected = "";
 String actual = tokenQueue.toString();
 Assert.assertEquals(expected, actual);
 }
}