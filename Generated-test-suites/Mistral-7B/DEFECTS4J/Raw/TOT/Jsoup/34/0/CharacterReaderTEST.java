import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 @Test
 public void testEmptyCharacterReader() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 assertNull(reader.current());
 }

 @Test
 public void testCharacterReaderWithSingleCharacter() {
 CharacterReader reader = new CharacterReader("a");
 assertEquals('a', reader.current());
 assertFalse(reader.isEmpty());
 reader.consume();
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderWithMultipleCharacters() {
 CharacterReader reader = new CharacterReader("abc");
 assertEquals('a', reader.current());
 assertFalse(reader.isEmpty());
 reader.consume();
 reader.consume();
 assertEquals('c', reader.current());
 }

 @Test
 public void testCharacterReaderMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testCharacterReaderConsumeSequence() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.consumeTo('c');
 assertEquals("ab", consumed);
 }

 @Test
 public void testCharacterReaderConsumeString() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.consumeTo("def");
 assertEquals("abcdef", consumed);
 }

 @Test
 public void testCharacterReaderConsumeAny() {
 CharacterReader reader = new CharacterReader("123abcd45ef");
 String consumed = reader.consumeToAny('3', 'd');
 assertEquals("12", consumed);
 }

 @Test
 public void testCharacterReaderConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.consumeToEnd();
 assertEquals("abcdef", consumed);
 }

 @Test
 public void testCharacterReaderConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcdeFgHiJkLm");
 String consumed = reader.consumeLetterSequence();
 assertEquals("abcde", consumed);
 }

 @Test
 public void testCharacterReaderConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abc123def456ghi");
 String consumed = reader.consumeLetterThenDigitSequence();
 assertEquals("abc123", consumed);
 }

 @Test
 public void testCharacterReaderConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("0x1234abcdEfGhIjKlM");
 String consumed = reader.consumeHexSequence();
 assertEquals("0x1234", consumed);
 }

 @Test
 public void testCharacterReaderConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 String consumed = reader.consumeDigitSequence();
 assertEquals("1234567890", consumed);
 }

 @Test
 public void testCharacterReaderMatches() {
 CharacterReader reader = new CharacterReader("abc");
 assertTrue(reader.matches('a'));
 assertFalse(reader.matches('b'));
 }

 @Test
 public void testCharacterReaderMatchesString() {
 CharacterReader reader = new CharacterReader("abc");
 assertTrue(reader.matches("ab"));
 assertFalse(reader.matches("cd"));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("ABC");
 assertTrue(reader.matchesIgnoreCase("ab")); // case insensitive
 assertFalse(reader.matchesIgnoreCase("CD"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 CharacterReader reader = new CharacterReader("abc");
 assertTrue(reader.matchesAny('a', 'b'));
 assertFalse(reader.matchesAny('c', 'd'));
 }

 @Test
 public void testCharacterReaderMatchesLetter() {
 CharacterReader reader = new CharacterReader("abc");
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testCharacterReaderMatchesDigit() {
 CharacterReader reader = new CharacterReader("123");
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testCharacterReaderMatchConsume() {
 CharacterReader reader = new CharacterReader("abc");
 String consumed = reader.matchConsume("ab");
 assertEquals("ab", consumed);
 }

 @Test
 public void testCharacterReaderMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("AbC");
 String consumed = reader.matchConsumeIgnoreCase("ab"); // case insensitive
 assertEquals("ab", consumed);
 }

 @Test
 public void testCharacterReaderContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("1<title>Test</title>2");
 assertTrue(reader.containsIgnoreCase("title"));
 }

 @Test
 public void testCharacterReaderNullInput() {
 assertThrows(NullPointerException.class, () -> new CharacterReader(null));
 }
}