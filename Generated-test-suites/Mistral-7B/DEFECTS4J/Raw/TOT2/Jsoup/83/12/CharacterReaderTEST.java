import org.jsoup.parser.CharacterReader;
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Test
 public void testCharacterReaderConstruction() {
 reader = new CharacterReader(new StringReader("Hello World"), maxBufferLen);
 Validate.notNull(reader);
 Validate.isInstanceOf(CharacterReader.class, reader);
 }

 @Test
 public void testCharacterReaderReaderConstruction() {
 reader = new CharacterReader(new StringReader("Hello World"));
 Validate.notNull(reader);
 Validate.isInstanceOf(CharacterReader.class, reader);
 }

 @Test
 public void testCharacterReaderStringConstruction() {
 reader = new CharacterReader(new StringReader("Hello World"), "Hello World".length());
 Validate.notNull(reader);
 Validate.isInstanceOf(CharacterReader.class, reader);
 }

 @Test
 public void testCharacterReaderEmpty() {
 reader = new CharacterReader(new StringReader(""));
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderNonEmpty() {
 reader = new CharacterReader(new StringReader("Hello World"));
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderCurrent() {
 reader = new CharacterReader(new StringReader("Hello World"));
 assertEquals('H', reader.current());
 }

 @Test(expected = UncheckedIOException.class)
 public void testCharacterReaderIOException() {
 reader = new CharacterReader(new StringReader("") {
 @Override
 public int read(char[] c, int off, int len) throws IOException {
 throw new IOException();
 }
 });
 reader.bufferUp();
 }

 @Test
 public void testCharacterReaderConsumeLetterSequence() {
 reader = new CharacterReader(new StringReader("abc123"));
 assertEquals("abc", reader.consumeLetterSequence());
 }

 @Test
 public void testCharacterReaderConsumeLetterThenDigitSequence() {
 reader = new CharacterReader(new StringReader("abc123"));
 assertEquals("abc123", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testCharacterReaderConsumeHexSequence() {
 reader = new CharacterReader(new StringReader("1234abcd1f"));
 assertEquals("1234", reader.consumeHexSequence());
 }

 @Test
 public void testCharacterReaderConsumeDigitSequence() {
 reader = new CharacterReader(new StringReader("1234abcd1f"));
 assertEquals("1234", reader.consumeDigitSequence());
 }

 @Test
 public void testCharacterReaderMatches() {
 reader = new CharacterReader(new StringReader("abracadabra"));
 assertTrue(reader.matches('a'));
 assertTrue(reader.matches("ab"));
 assertTrue(reader.matches("cad"));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCase() {
 reader = new CharacterReader(new StringReader("AbRaCaDaBrA"));
 assertTrue(reader.matchesIgnoreCase("ab"));
 assertTrue(reader.matchesIgnoreCase("cad"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 reader = new CharacterReader(new StringReader("1234abcd1f"));
 assertTrue(reader.matchesAny('1'));
 assertTrue(reader.matchesAny('2'));
 assertTrue(reader.matchesAny('3'));
 assertTrue(reader.matchesAny('4'));
 assertTrue(reader.matchesAny('a'));
 assertTrue(reader.matchesAny('b'));
 assertTrue(reader.matchesAny('c'));
 assertTrue(reader.matchesAny('d'));
 }

 @Test
 public void testCharacterReaderMatchesAnySorted() {
 reader = new CharacterReader(new StringReader("1234abcd1f"));
 assertTrue(reader.matchesAnySorted(new char[]{'1', '2', '3', '4', 'a', 'b', 'c', 'd'}));
 }

 @Test
 public void testCharacterReaderConsumeTagName() {
 reader = new CharacterReader(new StringReader("<abcdef>"));
 assertEquals("<", reader.consumeTagName());
 }

 @Test
 public void testCharacterReaderConsumeData() {
 reader = new CharacterReader(new StringReader("&lt;"));
 assertEquals("&lt;", reader.consumeData());
 }

 @Test
 public void testCharacterReaderConsumeTo() {
 reader = new CharacterReader(new StringReader("Hello World"));
 assertEquals("Hello", reader.consumeTo(' '));
 assertEquals("World", reader.consumeTo('W'));
 }

 @Test
 public void testCharacterReaderConsumeToAny() {
 reader = new CharacterReader(new StringReader("1234567890"));
 assertEquals("123", reader.consumeToAny('3'));
 assertEquals("567", reader.consumeToAny('7'));
 }

 @Test
 public void testCharacterReaderConsumeToAnySorted() {
 reader = new CharacterReader(new StringReader("1234567890"));
 assertEquals("123", reader.consumeToAnySorted(new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'}));
 }

 @Test
 public void testCharacterReaderConsumeToEnd() {
 reader = new CharacterReader(new StringReader("Hello World"));
 assertEquals("Hello World", reader.consumeToEnd());
 }

}