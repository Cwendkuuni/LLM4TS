import org.jsoup.parser.CharacterReader;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 Validate.notNull(reader);
 reader = new CharacterReader(new StringReader("test"));
 }

 @Test
 public void testCharacterReaderConstructorWithStringInput() {
 CharacterReader reader = new CharacterReader("testString");
 assertEquals("testString", reader.toString());
 }

 @Test
 public void testCharacterReaderConstructorWithReaderInput() {
 StringReader stringReader = new StringReader("testReader");
 CharacterReader reader = new CharacterReader(stringReader);
 assertEquals("testReader", reader.toString());
 }

 @Test
 public void testCharacterReaderConstructorWithReaderInputAndSize() {
 StringReader stringReader = new StringReader("testReader");
 CharacterReader reader = new CharacterReader(stringReader, 10);
 assertEquals(10, reader.charBuf.length);
 }

 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 reader.advance();
 reader.advance();
 int currentPosition = reader.pos();
 assertEquals(2, currentPosition);
 }

 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 assertFalse(reader.isEmpty());
 reader.consume();
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 reader.advance();
 assertEquals('t', reader.current());
 }

 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 reader.consume();
 assertEquals('t', reader.current());
 }

 @Test(expected = UncheckedIOException.class)
 public void testIOExceptionOnBufferUp() throws IOException {
 CharacterReader reader = new CharacterReader(new StringReader("test"), 2) {
 @Override
 void bufferUp() throws IOException {
 throw new IOException();
 }
 };
 reader.consume();
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('t', reader.current());
 }

 @Test
 public void testNextIndexOf() {
 CharacterReader reader = new CharacterReader(new StringReader("testtest"));
 assertEquals(4, reader.nextIndexOf('t'));
 }

 @Test
 public void testNextIndexOfWithEmptyString() {
 CharacterReader reader = new CharacterReader(new StringReader("testtest"));
 assertEquals(-1, reader.nextIndexOf(""));
 }

 @Test
 public void testNextIndexOfWithCharSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("testtest"));
 assertEquals(4, reader.nextIndexOf("st"));
 }

 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 String result = reader.consumeTo('s');
 assertEquals("test", result);
 }

 @Test
 public void testConsumeToEmpty() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 String result = reader.consumeTo('s');
 assertEquals("", result);
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 String result = reader.consumeToEnd();
 assertEquals("test", result);
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("testLetterSequence"));
 String result = reader.consumeLetterSequence();
 assertEquals("LetterSequence", result);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("testLetterThenDigitSequence"));
 String result = reader.consumeLetterThenDigitSequence();
 assertEquals("LetterThenDigitSequence", result);
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("testHexSequence"));
 String result = reader.consumeHexSequence();
 assertEquals("HexSequence", result);
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("testDigitSequence"));
 String result = reader.consumeDigitSequence();
 assertEquals("DigitSequence", result);
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 assertTrue(reader.matches('t'));
 }

 @Test
 public void testMatchesString() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 assertTrue(reader.matches("t"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 assertTrue(reader.matchesIgnoreCase("t"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 assertTrue(reader.matchesAny('t'));
 }

 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 assertTrue(reader.matchesAnySorted(new char[]{'t'}));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 String result = reader.matchConsume("t");
 assertEquals("t", result);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 String result = reader.matchConsumeIgnoreCase("t");
 assertEquals("t", result);
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("test<title>test</title>test"));
 assertTrue(reader.containsIgnoreCase("<title>"));
 }

}