import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 assertEquals("", reader.toString());
 }

 @Test
 public void testReaderWithSingleCharacter() {
 CharacterReader reader = new CharacterReader("A");
 assertFalse(reader.isEmpty());
 assertEquals("A", reader.consumeAsString());
 assertEquals('A', reader.current());
 }

 @Test
 public void testReaderWithMultipleCharacters() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertFalse(reader.isEmpty());
 assertEquals("Hello", reader.consumeTo(' '));
 assertEquals(' ', reader.current());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 try {
 CharacterReader reader = new CharacterReader(null);
 } catch (IllegalArgumentException e) {
 assertEquals("Input cannot be null", e.getMessage());
 }
 }

 @Test
 public void testNextIndexOfChar() {
 CharacterReader reader = new CharacterReader("kitten");
 assertEquals(2, reader.nextIndexOf('t'));
 assertEquals(-1, reader.nextIndexOf('z'));
 }

 @Test
 public void testNextIndexOfString() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals(6, reader.nextIndexOf("World"));
 assertEquals(-1, reader.nextIndexOf("Hello"));
 }

 @Test
 public void testConsumeToChar() {
 CharacterReader reader = new CharacterReader("kitten");
 assertEquals("itt", reader.consumeTo('t'));
 assertEquals('n', reader.current());
 }

 @Test
 public void testConsumeToString() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals("Hello", reader.consumeTo(" World"));
 assertEquals(' ', reader.current());
 }

 @Test
 public void testConsumeToAnyChar() {
 CharacterReader reader = new CharacterReader("kitten");
 assertEquals("itten", reader.consumeToAny('n', 't'));
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals("World", reader.consumeToEnd());
 assertEquals(EOF, reader.current());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals("Hello", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("Hello1234 World");
 assertEquals("Hello1234", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testMatchesChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matches('h'));
 }

 @Test
 public void testMatchesString() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matches("ll"));
 }

 @Test
 public void testMatchesIgnoreCaseString() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesIgnoreCase("LL"));
 }

 @Test
 public void testMatchesAnyChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesAny('l', 'o'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("Hello123 World");
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals('H', reader.current());
 }
}