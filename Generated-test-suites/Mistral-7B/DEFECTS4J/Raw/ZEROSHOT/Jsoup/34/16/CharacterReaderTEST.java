import org.jsoup.parser.CharacterReader;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderEmpty() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 assertEquals("", reader.toString());
 }

 @Test
 public void testCharacterReaderSingleCharacter() {
 CharacterReader reader = new CharacterReader("A");
 assertFalse(reader.isEmpty());
 assertEquals("A", reader.consumeAsString());
 }

 @Test
 public void testCharacterReaderMultipleCharacters() {
 CharacterReader reader = new CharacterReader("ABC");
 assertFalse(reader.isEmpty());
 assertEquals("A", reader.consumeAsString());
 assertEquals("B", reader.consumeAsString());
 assertEquals("C", reader.consumeAsString());
 assertEquals("", reader.consumeToEnd());
 }

 @Test
 public void testCharacterReaderMarkAndRewind() {
 CharacterReader reader = new CharacterReader("ABC");
 reader.mark();
 assertEquals("A", reader.consumeAsString());
 reader.rewindToMark();
 assertEquals("A", reader.consumeAsString());
 }

 @Test
 public void testCharacterReaderConsumeTo() {
 CharacterReader reader = new CharacterReader("ABCDEF");
 assertEquals("B", reader.consumeTo('B').toString());
 assertEquals("CDEF", reader.consumeTo('D').toString());
 assertEquals("EF", reader.consumeTo('E').toString());
 assertEquals("", reader.consumeTo('G').toString());
 }

 @Test
 public void testCharacterReaderConsumeToSequence() {
 CharacterReader reader = new CharacterReader("ABABC");
 assertEquals("BA", reader.consumeTo("BA").toString());
 }

 @Test
 public void testCharacterReaderConsumeToAny() {
 CharacterReader reader = new CharacterReader("ABCDEF");
 assertEquals("B", reader.consumeToAny('B').toString());
 assertEquals("DEF", reader.consumeToAny('D', 'F').toString());
 }

 @Test
 public void testCharacterReaderNextIndexOf() {
 CharacterReader reader = new CharacterReader("ABABC");
 assertEquals(1, reader.nextIndexOf('B'));
 assertEquals(4, reader.nextIndexOf("AB"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharacterReaderNullInput() {
 new CharacterReader(null);
 }

 @Test
 public void testCharacterReaderConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcd123efghijk");
 assertEquals("abcdefghij", reader.consumeLetterSequence().toString());
 }

 @Test
 public void testCharacterReaderConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abcd123efghijk");
 assertEquals("abcd123efg", reader.consumeLetterThenDigitSequence().toString());
 }

 @Test
 public void testCharacterReaderConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("abcd%61%63%68%65%2d%63%61%66%65");
 assertEquals("abcdfhe-cai", reader.consumeHexSequence().toString());
 }

 @Test
 public void testCharacterReaderConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 assertEquals("0", reader.consumeDigitSequence().toString());
 assertEquals("9", reader.consumeDigitSequence().toString());
 }

 @Test
 public void testCharacterReaderMatches() {
 CharacterReader reader = new CharacterReader("ABCABC");
 assertTrue(reader.matches('A'));
 assertTrue(reader.matches("AB"));
 assertFalse(reader.matches('D'));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("ABCABC");
 assertTrue(reader.matchesIgnoreCase("AB"));
 assertFalse(reader.matchesIgnoreCase("d"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 CharacterReader reader = new CharacterReader("ABCABC");
 assertTrue(reader.matchesAny('A', 'B'));
 assertFalse(reader.matchesAny('D'));
 }

 @Test
 public void testCharacterReaderMatchesLetter() {
 CharacterReader reader = new CharacterReader("ABCABC");
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('5'));
 }

 @Test
 public void testCharacterReaderMatchesDigit() {
 CharacterReader reader = new CharacterReader("123ABC456");
 assertTrue(reader.matchesDigit());
 assertFalse(reader.matchesDigit('A'));
 }

 @Test
 public void testCharacterReaderMatchConsume() {
 CharacterReader reader = new CharacterReader("ABCABC");
 assertTrue(reader.matchConsume("AB"));
 }

 @Test
 public void testCharacterReaderMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("ABCABC");
 assertTrue(reader.matchConsumeIgnoreCase("ab"));
 }

 @Test
 public void testCharacterReaderContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head></head><body></body></html>");
 assertTrue(reader.containsIgnoreCase("HEAD"));
 }
}