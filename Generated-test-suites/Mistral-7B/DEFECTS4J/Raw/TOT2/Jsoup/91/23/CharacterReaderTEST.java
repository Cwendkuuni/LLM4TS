import org.jsoup.parser.CharacterReader;
import org.jsoup.UncheckedIOException;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderInstance() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World!"));
 Assert.assertEquals("Hello, World!", reader.toString());
 }

 @Test
 public void testCharacterReaderEmptyReader() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderNonEmptyReader() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World!"));
 reader.bufferUp();
 Assert.assertFalse(reader.isEmpty());
 }

 @Test(expected = UncheckedIOException.class)
 public void testCharacterReaderIOException() {
 CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(new byte[0]))) {
 @Override
 public void close() throws IOException {
 super.close();
 throw new IOException("Testing IOException");
 }
 };
 reader.bufferUp();
 reader.consume();
 }

 @Test
 public void testCharacterReaderCurrentChar() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World!"));
 reader.bufferUp();
 reader.advance();
 Assert.assertEquals('H', reader.current());
 }

 @Test
 public void testCharacterReaderConsume() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World!"));
 reader.bufferUp();
 Assert.assertEquals('H', reader.consume());
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testCharacterReaderConsumeTo() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World!"));
 reader.bufferUp();
 String consumed = reader.consumeTo('l');
 Assert.assertEquals("ello, ", consumed);
 }

 @Test
 public void testCharacterReaderConsumeToEnd() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World!"));
 reader.bufferUp();
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("Hello, World!", consumed);
 }

 @Test
 public void testCharacterReaderConsumeToAny() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World! <html>"));
 reader.bufferUp();
 String consumed = reader.consumeToAny(',');
 Assert.assertEquals("Hello, World! <html>", reader.toString());
 Assert.assertEquals("Hello, World!", consumed);
 }

 @Test
 public void testCharacterReaderConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World! <html>"));
 reader.bufferUp();
 String consumed = reader.consumeToAnySorted(new char[]{'<', 'h'});
 Assert.assertEquals("Hello, World!", reader.toString());
 Assert.assertEquals("Hello, World!", consumed);
 }

 @Test
 public void testCharacterReaderConsumeData() {
 CharacterReader reader = new CharacterReader(new StringReader("&lt;html&gt;"));
 reader.bufferUp();
 String consumed = reader.consumeData();
 Assert.assertEquals("<html>", consumed);
 }

 @Test
 public void testCharacterReaderConsumeTagName() {
 CharacterReader reader = new CharacterReader(new StringReader("<html>"));
 reader.bufferUp();
 String consumed = reader.consumeTagName();
 Assert.assertEquals("html", consumed);
 }

 @Test
 public void testCharacterReaderNextIndexOf() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World! <html>"));
 reader.bufferUp();
 int index = reader.nextIndexOf('l');
 Assert.assertEquals(5, index);
 int index2 = reader.nextIndexOf("<html>");
 Assert.assertEquals(11, index2);
 }

 @Test
 public void testCharacterReaderNextIndexOfEmpty() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 reader.bufferUp();
 int index = reader.nextIndexOf('a');
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testCharacterReaderNextIndexOfSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World! <html>"));
 reader.bufferUp();
 String seq = "html";
 int index = reader.nextIndexOf(seq);
 Assert.assertEquals(11, index);
 }

 @Test
 public void testCharacterReaderNextIndexOfEmptySequence() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World! <html>"));
 reader.bufferUp();
 String seq = "";
 int index = reader.nextIndexOf(seq);
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testCharacterReaderMatches() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World!"));
 reader.bufferUp();
 Assert.assertTrue(reader.matches('o'));
 Assert.assertTrue(reader.matches("ll"));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World!"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesIgnoreCase("ll"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World!"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesAny('o', 'l'));
 Assert.assertTrue(reader.matchesAny(new char[]{'l', 'o'}));
 }

 @Test
 public void testCharacterReaderMatchesAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World!"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'o'}));
 }

 @Test
 public void testCharacterReaderMatchesLetter() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World!"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesLetter());
 }

 @Test
 public void testCharacterReaderMatchesDigit() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World!"));
 reader.bufferUp();
 Assert.assertTrue(reader.matchesDigit());
 }

 @Test
 public void testCharacterReaderMark() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World!"));
 reader.bufferUp();
 reader.mark();
 reader.unconsume();
 reader.rewindToMark();
 Assert.assertEquals('H', reader.current());
 }

 @Test
 public void testCharacterReaderRewindToMarkInvalid() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello, World!"));
 reader.bufferUp();
 reader.mark();
 reader.unconsume();
 reader.rewindToMark();
 reader.consume();
 Assert.assertEquals('e', reader.current());
 }
}