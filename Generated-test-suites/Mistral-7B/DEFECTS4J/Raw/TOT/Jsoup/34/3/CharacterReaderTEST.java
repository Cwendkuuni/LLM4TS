import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 assertEquals("", reader.toString());
 }

 @Test
 public void testSingleCharacterReader() {
 CharacterReader reader = new CharacterReader("abc");
 assertEquals('a', reader.current());
 assertEquals("a", reader.consumeAsString());
 assertEquals('b', reader.current());
 }

 @Test
 public void testMulticharacterReader() {
 CharacterReader reader = new CharacterReader("abcd");
 assertEquals('a', reader.current());
 assertEquals("a", reader.consumeAsString());
 assertEquals('b', reader.current());
 assertEquals("ab", reader.consumeTo('b').toString());
 assertEquals('c', reader.current());
 }

 @Test
 public void testScanForCharSequence() {
 CharacterReader reader = new CharacterReader("ababab");
 assertEquals(5, reader.nextIndexOf("aba".toCharArray()));
 }

 @Test(expected = java.lang.IndexOutOfBoundsException.class)
 public void testScanBeyondEndOfString() {
 CharacterReader reader = new CharacterReader("abc");
 reader.pos = reader.length;
 reader.nextIndexOf('a');
 }

 @Test
 public void testScanForEmptySequence() {
 CharacterReader reader = new CharacterReader("abc");
 assertEquals(-1, reader.nextIndexOf(new String("").toCharArray()));
 }

 @Test
 public void testScanForSingleCharacter() {
 CharacterReader reader = new CharacterReader("ababab");
 assertEquals(0, reader.nextIndexOf('a'));
 }

 @Test
 public void testScanForMultipleCharacters() {
 CharacterReader reader = new CharacterReader("ababab");
 assertEquals(2, reader.nextIndexOf("ba".toCharArray()));
 }

 @Test
 public void testScanForMiddleOfSequence() {
 CharacterReader reader = new CharacterReader("ababab");
 assertEquals(3, reader.nextIndexOf("aba".toCharArray(), 1));
 }

 @Test
 public void testScanForCharSequenceAtEnd() {
 CharacterReader reader = new CharacterReader("ababab");
 assertEquals(6, reader.nextIndexOf("ba".toCharArray(), 4));
 }

 @Test
 public void testScanForSurrogateChar() {
 CharacterReader reader = new CharacterReader("👋");
 assertEquals(-1, reader.nextIndexOf('\uD83C'.toCharArray()));
 }

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testScanForNullCharSequence() {
 CharacterReader reader = new CharacterReader("ab");
 reader.nextIndexOf(null);
 }

 @Test
 public void testConsumeSequence() {
 CharacterReader reader = new CharacterReader("ababab");
 assertEquals("aba", reader.consumeTo('b').toString());
 }

 @Test
 public void testConsumeSequenceWithEmptyString() {
 CharacterReader reader = new CharacterReader("ababab");
 assertEquals("", reader.consumeTo('').toString());
 }

 @Test
 public void testConsumeSequenceWithEndOfString() {
 CharacterReader reader = new CharacterReader("ab");
 assertEquals("ab", reader.consumeToEnd().toString());
 }

 @Test
 public void testConsumeSingleLetterSequence() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
 assertEquals("a", reader.consumeLetterSequence().toString());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abc123def456");
 assertEquals("ad", reader.consumeLetterThenDigitSequence().toString());
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz0123456789ABCDEF");
 assertEquals("abc", reader.consumeHexSequence().toString());
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 assertEquals("123", reader.consumeDigitSequence().toString());
 }

 @Test
 public void testMatchesSingleCharacter() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matches('d'));
 assertFalse(reader.matches('e'));
 }

 @Test
 public void testMatchesCharSequence() {
 CharacterReader reader = new CharacterReader("ababab");
 assertTrue(reader.matches("ba".toCharArray()));
 assertFalse(reader.matches("ca".toCharArray()));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("abABaBa");
 assertTrue(reader.matchesIgnoreCase("ba".toCharArray()));
 }

 @Test
 public void testMatchesAnyCharacter() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matchesAny('c', 'd'));
 assertFalse(reader.matchesAny('e'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("0123456789");
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsumeCharSequence() {
 CharacterReader reader = new CharacterReader("ababab");
 assertTrue(reader.matchConsume("ba".toCharArray()));
 }

 @Test
 public void testMatchConsumeCharSequenceIgnoreCase() {
 CharacterReader reader = new CharacterReader("abABaBa");
 assertTrue(reader.matchConsumeIgnoreCase("ba".toCharArray()));
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("abABaBa<html><title>Test</title></html>");
 assertTrue(reader.containsIgnoreCase("/title>"));
 }
}