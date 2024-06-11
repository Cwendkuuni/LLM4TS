import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.CharacterReaderTest;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Test
 public void testCharacterReaderInstance() {
 reader = new CharacterReader(new StringReader("Test string"));
 Assert.assertNotNull(reader);
 }

 @Test
 public void testCharacterReaderConstructorWithMaxBufferLen() {
 reader = new CharacterReader(new StringReader("Test string"), CharacterReader.maxBufferLen);
 Assert.assertArrayEquals(CharacterReader.maxBufferLen, reader.charBuf.length);
 }

 @Test
 public void testCharacterReaderConstructorWithReader() {
 reader = new CharacterReader(new StringReader("Test string"));
 Assert.assertEquals("Test string", reader.toString());
 }

 @Test
 public void testBufferUp() {
 reader = new CharacterReader(new StringReader("Test string Test string Test string Test string"));
 reader.bufferUp();
 Assert.assertEquals(CharacterReader.maxBufferLen, reader.bufLength);
 }

 @Test
 public void testEmpty() {
 reader = new CharacterReader(new StringReader("Test string"));
 Assert.assertFalse(reader.isEmpty());
 reader.bufferUp();
 reader.consume();
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testPos() {
 reader = new CharacterReader(new StringReader("Test string Test string Test string Test string"));
 reader.bufferUp();
 Assert.assertEquals(0, reader.pos());
 reader.advance();
 reader.advance();
 reader.advance();
 reader.advance();
 reader.advance();
 Assert.assertEquals(5, reader.pos());
 }

 @Test
 public void testConsume() {
 reader = new CharacterReader(new StringReader("Test string Test string Test string Test string"));
 reader.bufferUp();
 Assert.assertEquals('T', reader.consume());
 Assert.assertEquals('e', reader.consume());
 Assert.assertEquals('st', reader.consume(2));
 }

 @Test(expected = UncheckedIOException.class)
 public void testIOException() {
 reader = new CharacterReader(new StringReader("Test string") {
 @Override
 public int read(char[] cbuf, int off, int len) throws IOException {
 throw new IOException();
 }
 });
 reader.bufferUp();
 reader.consume(); // should not reach this point
 }

 @Test
 public void testNextIndexOf() {
 reader = new CharacterReader(new StringReader("Test string Test string Test string Test string"));
 reader.bufferUp();
 Assert.assertEquals(0, reader.nextIndexOf('T'));
 Assert.assertEquals(4, reader.nextIndexOf('s'));
 Assert.assertEquals(-1, reader.nextIndexOf('Z'));
 }

 @Test
 public void testNextIndexOfWithCharSequence() {
 reader = new CharacterReader(new StringReader("Test string Test string Test string Test string"));
 reader.bufferUp();
 Assert.assertEquals(0, reader.nextIndexOf("Test"));
 Assert.assertEquals(13, reader.nextIndexOf("string"));
 Assert.assertEquals(-1, reader.nextIndexOf("Zzzz"));
 }

 @Test
 public void testConsumeTo() {
 reader = new CharacterReader(new StringReader("Test string Test string Test string Test string"));
 reader.bufferUp();
 Assert.assertEquals("Test", reader.consumeTo(' '));
 Assert.assertEquals("ing", reader.consumeTo(' '));
 Assert.assertEquals("Test string Test string", reader.consumeToEnd());
 }

 @Test
 public void testConsumeToAny() {
 reader = new CharacterReader(new StringReader("Test string Test string Test string Test string"));
 reader.bufferUp();
 Assert.assertEquals("Test", reader.consumeToAny(new char[]{' '}));
 Assert.assertEquals("stringTest", reader.consumeToAnySorted(new char[]{'s', 't'}));
 }

 @Test
 public void testConsumeData() {
 reader = new CharacterReader(new StringReader("Test string Test string Test string Test string"));
 reader.bufferUp();
 Assert.assertEquals("Test", reader.consumeData());
 }

 @Test
 public void testConsumeTagName() {
 reader = new CharacterReader(new StringReader("Test\tstring Test\n\rstring Test\f string Test <title>Title</title> Test <style>Style</style> Test <script>Script</script> string"));
 reader.bufferUp();
 Assert.assertEquals("Test\t", reader.consumeTagName());
 Assert.assertEquals("title", reader.consumeTagName());
 Assert.assertEquals("Style", reader.consumeTagName());
 Assert.assertEquals("Script", reader.consumeTagName());
 Assert.assertEquals("string", reader.consumeTagName());
 }

 @Test
 public void testConsumeLetterSequence() {
 reader = new CharacterReader(new StringReader("Test string Test string Test string Test string"));
 reader.bufferUp();
 Assert.assertEquals("Test", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 reader = new CharacterReader(new StringReader("Test123 string Test456 Test789 string"));
 reader.bufferUp();
 Assert.assertEquals("Test123", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testMatch() {
 reader = new CharacterReader(new StringReader("Test string Test string Test string Test string"));
 reader.bufferUp();
 Assert.assertTrue(reader.matches('e'));
 Assert.assertTrue(reader.matches("es"));
 Assert.assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchIgnoreCase() {
 reader = new CharacterReader(new StringReader("Test string Test string Test string Test string"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesIgnoreCase("es"));
 Assert.assertFalse(reader.matchesIgnoreCase("Z"));
 }

 @Test
 public void testMatchesAny() {
 reader = new CharacterReader(new StringReader("Test string Test string Test string Test string"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesAny(new char[]{'e', 's'}));
 Assert.assertFalse(reader.matchesAny(new char[]{'z'}));
 }

 @Test
 public void testMatchesAnySorted() {
 reader = new CharacterReader(new StringReader("Test string Test string Test string Test string"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesAnySorted(new char[]{'s', 'e'}));
 Assert.assertFalse(reader.matchesAnySorted(new char[]{'z'}));
 }

 @Test
 public void testMatchesLetter() {
 reader = new CharacterReader(new StringReader("Test string Test string Test string Test string"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesLetter('9'));
 }

 @Test
 public void testMatchesDigit() {
 reader = new CharacterReader(new StringReader("Test123 string Test456 Test789 string"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit('s'));
 }

 @Test
 public void testToString() {
 reader = new CharacterReader(new StringReader("Test string Test string Test string Test string"));
 reader.bufferUp();
 Assert.assertEquals("Test string Test string Test string Test string", reader.toString());
 }

 @Test
 public void testCacheString() {
 reader = new CharacterReader(new StringReader("Test string Test string Test string Test string"));
 reader.bufferUp();
 String str1 = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.bufLength - reader.bufPos);
 String str2 = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.bufLength - reader.bufPos);
 Assert.assertEquals(str1, str2);
 }
}