import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 // Test for isEmpty method
 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 reader = new CharacterReader("abc");
 assertFalse(reader.isEmpty());
 }

 // Test for pos method
 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader("abc");
 assertEquals(0, reader.pos());
 reader.consume();
 reader.consume();
 assertEquals(2, reader.pos());
 }

 // Test for current method
 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader("abcd");
 assertEquals('a', reader.current());
 reader.consume();
 assertEquals('b', reader.current());
 reader.consume();
 assertEquals('c', reader.current());
 reader.consume();
 assertEquals(CharacterReader.EOF, reader.current());
 }

 // Test for consume method
 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("abcde");
 assertEquals('a', reader.consume());
 assertEquals('b', reader.consume());
 assertEquals('c', reader.consume());
 assertEquals('d', reader.consume());
 assertEquals('e', reader.consume());
 assertEquals(CharacterReader.EOF, reader.consume());
 }

 // Test for unconsume method
 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.consume();
 reader.consume();
 reader.unconsume();
 assertEquals('b', reader.current());
 }

 // Test for advance method
 @Test
 public void testAdvance() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.advance();
 assertEquals('b', reader.current());
 }

 // Test for mark method
 @Test
 public void testMark() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 // Test for consumeAsString method
 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("abcd");
 assertEquals("", reader.consumeAsString());
 reader.consume();
 reader.consume();
 assertEquals("cd", reader.consumeAsString());
 }

 // Test for nextIndexOf method with char
 @Test
 public void testNextIndexOfWithChar() {
 CharacterReader reader = new CharacterReader("abccdabcdabccd");
 assertEquals(1, reader.nextIndexOf('c'));
 assertEquals(-1, reader.nextIndexOf('z'));
 }

 // Test for nextIndexOf method with CharSequence
 @Test
 public void testNextIndexOfWithCharSequence() {
 CharacterReader reader = new CharacterReader("abccdabcdabccd");
 assertEquals(3, reader.nextIndexOf("cc"));
 assertEquals(-1, reader.nextIndexOf("zz"));
 }

 // Test for consumeTo method with char
 @Test
 public void testConsumeToWithChar() {
 CharacterReader reader = new CharacterReader("abccdabccdabccd");
 assertEquals("b", reader.consumeTo('c').toString());
 assertEquals("abccdabccd", reader.toString());
 }

 // Test for consumeTo method with CharSequence
 @Test
 public void testConsumeToWithCharSequence() {
 CharacterReader reader = new CharacterReader("abccdabccdabccd");
 assertEquals("cc", reader.consumeTo("cc").toString());
 assertEquals("ab", reader.toString());
 }

 // Test for consumeToAny method
 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
 assertEquals("cd", reader.consumeToAny('c', 'd').toString());
 assertEquals("abcdabcd", reader.toString());
 }

 // Test for consumeToEnd method
 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 assertEquals("abcdabcd", reader.toString());
 assertEquals("abcdabcd", reader.consumeToEnd().toString());
 }

 // Test for consumeLetterSequence method
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
 assertEquals("abc", reader.consumeLetterSequence().toString());
 }

 // Test for consumeLetterThenDigitSequence method
 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abc123def456ghi789");
 assertEquals("abc123", reader.consumeLetterThenDigitSequence().toString());
 }

 // Test for consumeHexSequence method
 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("0123456789ABCDEFabcdef");
 assertEquals("01", reader.consumeHexSequence().toString());
 }

 // Test for consumeDigitSequence method
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 assertEquals("1234567890", reader.consumeDigitSequence().toString());
 }

 // Test for matches method with char
 @Test
 public void testMatchesWithChar() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matches('a'));
 assertTrue(reader.matches('d'));
 assertFalse(reader.matches('e'));
 }

 // Test for matches method with String
 @Test
 public void testMatchesWithString() {
 CharacterReader reader = new CharacterReader("abcdabc");
 assertTrue(reader.matches("ab"));
 assertFalse(reader.matches("ef"));
 }

 // Test for matchesIgnoreCase method with String
 @Test
 public void testMatchesIgnoreCaseWithString() {
 CharacterReader reader = new CharacterReader("abcdABCdEf");
 assertTrue(reader.matchesIgnoreCase("AB"));
 assertTrue(reader.matchesIgnoreCase("ef"));
 assertFalse(reader.matchesIgnoreCase("PQ"));
 }

 // Test for matchesAny method with char
 @Test
 public void testMatchesAnyWithChar() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matchesAny('a', 'b'));
 assertFalse(reader.matchesAny('e', 'f'));
 }

 // Test for matchesLetter method
 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matchesLetter());
 }

 // Test for matchesDigit method
 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("1234");
 assertTrue(reader.matchesDigit());
 }

 // Test for matchConsume method with String
 @Test
 public void testMatchConsumeWithString() {
 CharacterReader reader = new CharacterReader("abcdabc");
 assertTrue(reader.matchConsume("ab"));
 }

 // Test for matchConsumeIgnoreCase method with String
 @Test
 public void testMatchConsumeIgnoreCaseWithString() {
 CharacterReader reader = new CharacterReader("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
 assertTrue(reader.matchConsumeIgnoreCase("ef"));
 }

 // Test for containsIgnoreCase method with String
 @Test
 public void testContainsIgnoreCaseWithString() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
 assertTrue(reader.containsIgnoreCase("ef"));
 }

 // Test for toString method
 @Test
 public void testToString() {
 CharacterReader reader = new CharacterReader("abcd");
 assertEquals("abcd", reader.toString());
 }
}