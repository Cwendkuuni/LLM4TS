import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.parser.CharacterReader;
import java.io.StringReader;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderConstructors() {
 CharacterReader reader = new CharacterReader(new StringReader("hello world"));
 CharacterReader readerWithSize = new CharacterReader(new StringReader("abcdef"), 5);
 CharacterReader readerWithNullInput = new CharacterReader(null);

 Assertions.assertNotNull(reader);
 Assertions.assertEquals(11, reader.charBuf.length);
 Assertions.assertEquals("hello world", reader.toString());

 Assertions.assertNotNull(readerWithSize);
 Assertions.assertEquals(5, readerWithSize.charBuf.length);
 Assertions.assertEquals("abcdef", readerWithSize.toString());

 Assertions.assertNull(readerWithNullInput);
 }

 @Test
 public void testBufferUp() {
 CharacterReader reader = new CharacterReader(new StringReader("hello world"));
 reader.bufferUp();

 Assertions.assertEquals(11, reader.pos());
 Assertions.assertEquals(0, reader.isEmpty());
 Assertions.assertEquals('h', reader.current());
 Assertions.assertEquals('h', reader.consume());
 Assertions.assertEquals(10, reader.pos());
 Assertions.assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrentAndConsume() {
 CharacterReader reader = new CharacterReader(new StringReader("hello world"));

 Assertions.assertEquals('h', reader.current());
 Assertions.assertEquals('h', reader.consume());

 Assertions.assertEquals('e', reader.current());
 Assertions.assertEquals('e', reader.consume());

 Assertions.assertEquals('l', reader.current());
 Assertions.assertEquals('l', reader.consume());

 Assertions.assertEquals(' ', reader.current());
 Assertions.assertFalse(reader.isEmpty());

 Assertions.assertEquals(EOF, reader.consume());
 Assertions.assertTrue(reader.isEmpty());
 }

 @Test
 public void testAdvance() {
 CharacterReader reader = new CharacterReader(new StringReader("hello world"));

 reader.advance();
 reader.advance();
 reader.advance();
 reader.advance();
 reader.advance();

 Assertions.assertEquals('w', reader.current());
 Assertions.assertEquals('w', reader.consume());
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader(new StringReader("hello world"));

 reader.mark();
 reader.advance();
 reader.advance();
 reader.rewindToMark();

 Assertions.assertEquals('h', reader.current());
 Assertions.assertEquals('h', reader.consume());
 }

 @Test
 public void testNextIndexOf() {
 CharacterReader reader = new CharacterReader(new StringReader("hello world"));

 Assertions.assertEquals(6, reader.nextIndexOf('l'));
 Assertions.assertEquals(7, reader.nextIndexOf('o'));
 Assertions.assertEquals(-1, reader.nextIndexOf('z'));
 }

 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader(new StringReader("hello world"));

 Assertions.assertEquals("ello", reader.consumeTo('l'));
 Assertions.assertEquals("ld", reader.consumeTo('d'));
 Assertions.assertEquals("ld", reader.consumeTo('d'));
 Assertions.assertEquals(EOF, reader.consumeTo('z'));
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader(new StringReader("hello world"));

 Assertions.assertEquals("ello", reader.consumeToAny(new char[]{'l'}));
 Assertions.assertEquals("ld", reader.consumeToAny(new char[]{'d'}));
 Assertions.assertEquals("ld", reader.consumeToAny(new char[]{'d'}));
 Assertions.assertEquals(EOF, reader.consumeToAny(new char[]{'z'}));
 }

 @Test
 public void testConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("hello world"));

 Assertions.assertEquals("ello", reader.consumeToAnySorted(new char[]{'l'}));
 Assertions.assertEquals("ld", reader.consumeToAnySorted(new char[]{'d'}));
 Assertions.assertEquals("ld", reader.consumeToAnySorted(new char[]{'d'}));
 Assertions.assertEquals(EOF, reader.consumeToAnySorted(new char[]{'z'}));
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader(new StringReader("hello & world"));

 Assertions.assertEquals("hello ", reader.consumeData());
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader(new StringReader("<h1>Hello World</h1>"));

 Assertions.assertEquals("h1", reader.consumeTagName());
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader(new StringReader("hello world"));

 Assertions.assertEquals("world", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));

 Assertions.assertEquals("abcdef", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("abc123def456"));

 Assertions.assertEquals("abc123def456", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("&x27; &x61; &x6D; &x61; &x69; &x66; &x6F; &x72; &x6D; &x3B;"));

 Assertions.assertEquals("&#x27; &#x61; &#x6D; &#x61; &#x69; &#x66; &#x6F; &#x72; &#x6D; &#x3B;", reader.toString());
 Assertions.assertEquals("&#x27; &#x61; &#x6D; &#x61; &#x69; &#x66; &#x6F; &#x72; &#x6D; &#x3B;", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("1234567890"));

 Assertions.assertEquals("1234567890", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader(new StringReader("hello world"));

 Assertions.assertTrue(reader.matches('h'));
 Assertions.assertTrue(reader.matches('e'));
 Assertions.assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("hello world"));

 Assertions.assertTrue(reader.matchesIgnoreCase("HELLO"));
 Assertions.assertTrue(reader.matchesIgnoreCase("Hello"));
 Assertions.assertFalse(reader.matchesIgnoreCase("Zzzz"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader(new StringReader("hello world"));

 Assertions.assertTrue(reader.matchesAny(new char[]{'h', 'e'}));
 Assertions.assertTrue(reader.matchesAny(new char[]{'h', 'e', 'l'}));
 Assertions.assertFalse(reader.matchesAny(new char[]{'z', 'a'}));
 }

 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("hello world"));

 Assertions.assertTrue(reader.matchesAnySorted(new char[]{'h', 'e', 'l'}));
 Assertions.assertTrue(reader.matchesAnySorted(new char[]{'h', 'e'}));
 Assertions.assertFalse(reader.matchesAnySorted(new char[]{'z', 'a'}));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader(new StringReader("hello world"));

 Assertions.assertTrue(reader.matchesLetter());
 Assertions.assertTrue(reader.matchesLetter());
 Assertions.assertTrue(reader.matchesLetter());
 Assertions.assertTrue(reader.matchesLetter());
 Assertions.assertTrue(reader.matchesLetter());
 Assertions.assertTrue(reader.matchesLetter());
 Assertions.assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader(new StringReader("0123456789"));

 Assertions.assertFalse(reader.matchesDigit());
 Assertions.assertFalse(reader.matchesDigit());
 Assertions.assertTrue(reader.matchesDigit());
 Assertions.assertTrue(reader.matchesDigit());
 Assertions.assertTrue(reader.matchesDigit());
 Assertions.assertTrue(reader.matchesDigit());
 Assertions.assertTrue(reader.matchesDigit());
 Assertions.assertTrue(reader.matchesDigit());
 Assertions.assertTrue(reader.matchesDigit());
 Assertions.assertTrue(reader.matchesDigit());
 Assertions.assertTrue(reader.matchesDigit());
 }

}