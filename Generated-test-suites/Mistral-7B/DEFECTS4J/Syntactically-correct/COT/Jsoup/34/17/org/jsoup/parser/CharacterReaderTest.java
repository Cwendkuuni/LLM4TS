import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setup() {
 char[] input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
 reader = new CharacterReader(new String(input));
 }

 @Test
 public void testConstructor() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertEquals(-1, reader.pos());
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testConsume() {
 char c = reader.consume();
 Assert.assertEquals('a', c);
 Assert.assertEquals(1, reader.pos());
 }

 @Test
 public void testConsumeEnd() {
 String data = reader.consumeToEnd();
 Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", data);
 Assert.assertEquals(-1, reader.pos());
 }

 @Test
 public void testNextIndexOf() {
 // basic test
 Assert.assertEquals(1, reader.nextIndexOf('a'));
 // edge case: pos is at the end of the input
 Assert.assertEquals(-1, reader.nextIndexOf('z'));
 // exception handling: input is empty
 reader = new CharacterReader("");
 Assert.assertEquals(-1, reader.nextIndexOf('a'));
 }

 @Test
 public void testNextIndexOfWithCharSequence() {
 // basic test
 String seq = "abc";
 Assert.assertEquals(3, reader.nextIndexOf(seq));
 // edge case: sequence not found
 Assert.assertEquals(-1, reader.nextIndexOf("xyz"));
 // edge case: pos is at the end of the input
 Assert.assertEquals(-1, reader.nextIndexOf("abc"));
 // edge case: input is empty
 reader = new CharacterReader("");
 Assert.assertEquals(-1, reader.nextIndexOf("abc"));
 }

 @Test
 public void testConsumeTo() {
 String data = reader.consumeTo('e');
 Assert.assertEquals("abcd", data);
 Assert.assertEquals(4, reader.pos());
 }

 @Test
 public void testConsumeToEnd() {
 String data = reader.consumeToEnd();
 Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", data);
 Assert.assertEquals(-1, reader.pos());
 }

 @Test
 public void testConsumeToAny() {
 String data = reader.consumeToAny('a', 'b', 'c');
 Assert.assertEquals("abca", data);
 Assert.assertEquals(4, reader.pos());
 }

 @Test(expected = AssertionError.class)
 public void testConsumeToAnyWithEmptyInput() {
 reader = new CharacterReader("");
 reader.consumeToAny('a');
 }

 @Test(expected = AssertionError.class)
 public void testConsumeToAnyWithNullInput() {
 reader.consumeToAny(null);
 }

 @Test
 public void testConsumeLetterSequence() {
 String data = reader.consumeLetterSequence();
 Assert.assertEquals("abcdef", data);
 Assert.assertEquals(6, reader.pos());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 String data = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("ab1cd", data);
 Assert.assertEquals(5, reader.pos());
 }

 @Test
 public void testConsumeHexSequence() {
 String data = reader.consumeHexSequence();
 Assert.assertEquals("abcdef", data);
 Assert.assertEquals(6, reader.pos());
 }

 @Test
 public void testConsumeDigitSequence() {
 String data = reader.consumeDigitSequence();
 Assert.assertEquals("12345", data);
 Assert.assertEquals(5, reader.pos());
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(reader.matches('a'));
 Assert.assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 String seq = "abc";
 Assert.assertTrue(reader.matches(seq));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String seq = "ABC";
 Assert.assertTrue(reader.matchesIgnoreCase(seq));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = {'a', 'b'};
 Assert.assertTrue(reader.matchesAny(seq));
 }

 @Test
 public void testMatcheConsume() {
 String seq = "abc";
 reader.matchConsume(seq);
 Assert.assertEquals(3, reader.pos());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String seq = "AbC";
 reader.matchConsumeIgnoreCase(seq);
 Assert.assertEquals(3, reader.pos());
 }

 @Test
 public void testContainsIgnoreCase() {
 String seq = "abc";
 Assert.assertTrue(reader.containsIgnoreCase(seq));
 }

 @Test
 public void testMarkUnconsume() {
 reader.mark();
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testRewindToMark() {
 reader.mark();
 reader.consume();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 reader.advance();
 Assert.assertEquals('c', reader.current());
 }
}