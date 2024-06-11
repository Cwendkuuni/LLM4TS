import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.CharacterReader.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderConstructorWithReader() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals("Hello World", reader.consumeToEnd());
 }

 @Test
 public void testCharacterReaderConstructorWithString() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals("Hello World", reader.consumeToEnd());
 }

 @Test
 public void testBufferUp() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFABCDEF"));
 reader.bufferUp();
 Assert.assertEquals(10, reader.bufLength);
 }

 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFABCDEF"));
 reader.bufferUp();
 reader.advance();
 reader.advance();
 Assert.assertEquals(3, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFABCDEF"));
 reader.bufferUp();
 Assert.assertFalse(reader.isEmpty());
 reader.consume();
 reader.consume();
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFABCDEF"));
 reader.bufferUp();
 Assert.assertEquals('A', reader.consume());
 Assert.assertEquals('B', reader.consume());
 }

 @Test(expected = UncheckedIOException.class)
 public void testUncheckedIOException() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"), 1);
 reader.bufferUp();
 reader.advance();
 reader.reader.close();
 reader.consume();
 }

 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFABCDEF"));
 reader.bufferUp();
 Assert.assertEquals("ABCDEF", reader.consumeTo('F'));
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFABCDEF"));
 reader.bufferUp();
 Assert.assertEquals("ABCDEFABCDEF", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
 reader.bufferUp();
 Assert.assertEquals("abcdef", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("abc123def456"));
 reader.bufferUp();
 Assert.assertEquals("abc123", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("0x123456789ABCDEF"));
 reader.bufferUp();
 Assert.assertEquals("123456", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
 reader.bufferUp();
 Assert.assertEquals("123456", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFABCDEF"));
 reader.bufferUp();
 Assert.assertTrue(reader.matches('A'));
 Assert.assertTrue(reader.matches("AB"));
 Assert.assertFalse(reader.matches('Z'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFABCDEF"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesIgnoreCase("ab"));
 Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
 Assert.assertFalse(reader.matchesIgnoreCase("ZX"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFABCDEF"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesAny('A', 'B', 'C'));
 Assert.assertFalse(reader.matchesAny('Z', 'A', 'B'));
 }

 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFABCDEF"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesAnySorted(new char[]{'A', 'B', 'C'}));
 Assert.assertFalse(reader.matchesAnySorted(new char[]{'Z', 'A', 'B'}));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFABCDEF"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFABCDEF"));
 reader.bufferUp();
 reader.mark();
 Assert.assertEquals("AB", reader.consumeTo('B'));
 reader.rewindToMark();
 Assert.assertEquals("AB", reader.matchConsume("AB"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFABCDEF"));
 reader.bufferUp();
 reader.mark();
 Assert.assertEquals("AB", reader.consumeTo('B'));
 reader.rewindToMark();
 Assert.assertEquals("ab", reader.matchConsumeIgnoreCase("Ab"));
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><title>Test Title</title>"));
 reader.bufferUp();
 Assert.assertTrue(reader.containsIgnoreCase("/title"));
 }
}