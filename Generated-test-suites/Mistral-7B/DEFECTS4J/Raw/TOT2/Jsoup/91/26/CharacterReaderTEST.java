import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 String input = "This is a test for CharacterReader.";
 reader = new CharacterReader(new StringReader(input));
 }

 @Test
 public void testConstructorWithStringReader() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 assertNotNull(reader);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullInput() {
 CharacterReader reader = new CharacterReader(null);
 assertThrows(NullPointerException.class, () -> reader);
 }

 @Test
 public void testConstructorWithReaderAndInt() {
 CharacterReader reader = new CharacterReader(new StringReader(""), 10);
 assertNotNull(reader);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullInputAndSize() {
 CharacterReader reader = new CharacterReader(null, 10);
 assertThrows(NullPointerException.class, () -> reader);
 }

 @Test
 public void testIsEmpty() {
 assertFalse(reader.isEmpty());
 reader.bufferUp();
 reader.bufferUp();
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testIsEmptyNoBufferUp() {
 reader.bufferUp();
 assertTrue(reader.isEmptyNoBufferUp());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 }

 @Test
 public void testPosAfterReadingCharacters() {
 reader.consume();
 reader.consume();
 reader.consume();
 assertEquals(3, reader.pos());
 }

 @Test
 public void testConsume() {
 assertEquals('T', reader.consume());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeOutOfBound() {
 reader.bufferUp();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume(); // read last character
 reader.consume(); // out of bound
 assertThrows(IllegalArgumentException.class, () -> reader.consume());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.unconsume();
 assertEquals('T', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('h', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 assertEquals(-1, reader.bufMark);
 }

 @Test
 public void testMarkAndRewind() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('T', reader.current());
 }

 @Test(expected = UncheckedIOException.class)
 public void testBufferUpThrowsIOException() {
 CharacterReader reader = new CharacterReader(new StringReader(""), 1);
 try {
 reader.bufferUp();
 } catch (IOException e) {
 // expected
 }
 assertThrows(UncheckedIOException.class, reader::bufferUp);
 }

 @Test
 public void testNextIndexOf() {
 assertEquals(7, reader.nextIndexOf('s'));
 }

 @Test
 public void testNextIndexOfWithEmptyInput() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 assertEquals(-1, reader.nextIndexOf('s'));
 }

 @Test
 public void testNextIndexOfWithNullInput() {
 CharacterReader reader = new CharacterReader(null);
 assertThrows(NullPointerException.class, () -> reader.nextIndexOf('s'));
 }

 @Test
 public void testNextIndexOfWithCharSequence() {
 String testString = "testString";
 assertEquals(testString.length(), reader.nextIndexOf(testString));
 }

 @Test
 public void testNextIndexOfWithEmptyCharSequence() {
 assertEquals(-1, reader.nextIndexOf(new String("")));
 }

 @Test
 public void testConsumeTo() {
 String expected = "is a test for CharacterReader.";
 String result = reader.consumeTo(' ');
 assertEquals(expected, result);
 }

 @Test
 public void testConsumeToEmpty() {
 assertEquals("", reader.consumeTo(' '));
 }

 @Test
 public void testConsumeToAny() {
 char[] testChars = {'<', '/', '>'};
 String result = reader.consumeToAny(testChars);
 assertEquals("", result);
 }

 @Test
 public void testConsumeToAnyWithSingleChar() {
 char testChar = '>';
 String result = reader.consumeToAny(testChar);
 assertEquals("", result);
 }

 @Test
 public void testConsumeToAnyWithMultipleChars() {
 char[] testChars = {'<', '/', '>'};
 String input = "<html><head></head><body></body></html>";
 CharacterReader reader = new CharacterReader(new StringReader(input));
 String result = reader.consumeToAny(testChars);
 assertEquals("</html>", result);
 }

 @Test
 public void testConsumeData() {
 String result = reader.consumeData();
 assertEquals("&, <, null", result);
 }

 @Test
 public void testConsumeTagName() {
 String result = reader.consumeTagName();
 assertEquals("", result);
 }

 @Test
 public void testConsumeToEnd() {
 String result = reader.consumeToEnd();
 assertEquals("This is a test for CharacterReader.", result);
 }

 @Test
 public void testConsumeLetterSequence() {
 String result = reader.consumeLetterSequence();
 assertEquals("is a test for CharacterReader.", result);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 String result = reader.consumeLetterThenDigitSequence();
 assertEquals("is a test for CharacterReader.", result);
 }

 @Test
 public void testConsumeHexSequence() {
 String result = reader.consumeHexSequence();
 assertEquals("is a test for CharacterReader.", result);
 }

 @Test
 public void testConsumeDigitSequence() {
 String result = reader.consumeDigitSequence();
 assertEquals("is a test for CharacterReader.", result);
 }

 @Test
 public void testMatches() {
 reader.bufferUp();
 reader.consume();
 assertTrue(reader.matches('h'));
 }

 @Test
 public void testMatchesWithEmptyInput() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 assertFalse(reader.matches('s'));
 }

 @Test
 public void testMatchesWithNullInput() {
 CharacterReader reader = new CharacterReader(null);
 assertThrows(NullPointerException.class, () -> reader.matches('s'));
 }

 @Test
 public void testMatchesString() {
 reader.bufferUp();
 reader.consume();
 reader.consume();
 assertTrue(reader.matches("is"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 reader.bufferUp();
 reader.consume();
 reader.consume();
 assertTrue(reader.matchesIgnoreCase("Is"));
 }

 @Test
 public void testMatchesAny() {
 reader.bufferUp();
 reader.consume();
 reader.consume();
 assertTrue(reader.matchesAny('h', 'i'));
 }

 @Test
 public void testMatchesAnySorted() {
 reader.bufferUp();
 reader.consume();
 reader.consume();
 assertTrue(reader.matchesAnySorted(new char[]{'h', 'i'}));
 }

 @Test
 public void testMatchesLetter() {
 reader.bufferUp();
 reader.consume();
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 reader.bufferUp();
 reader.consume();
 reader.consume();
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String testString = "test";
 reader.bufferUp();
 reader.consume();
 reader.consume();
 assertTrue(reader.matchConsume(testString));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String testString = "Test";
 reader.bufferUp();
 reader.consume();
 reader.consume();
 assertTrue(reader.matchConsumeIgnoreCase(testString));
 }

 @Test
 public void testContainsIgnoreCase() {
 reader.bufferUp();
 reader.consume();
 reader.consume();
 reader.consume();
 assertTrue(reader.containsIgnoreCase("test"));
 }
}