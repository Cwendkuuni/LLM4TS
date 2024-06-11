import org.jsoup.parser.CharacterReader;
import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Test
 public void testCharacterReaderConstructionWithInputReader() {
 reader = new CharacterReader(new StringReader("Hello World"));
 Validate.notNull(reader);
 }

 @Test
 public void testCharacterReaderConstructionWithInputString() {
 reader = new CharacterReader(new StringReader("Hello World"));
 Validate.notNull(reader);
 }

 @Test
 public void testCharacterReaderPos() {
 reader = new CharacterReader(new StringReader("Hello World"));
 reader.advance();
 reader.advance();
 int pos = reader.pos();
 Assert.assertEquals(2, pos);
 }

 @Test
 public void testCharacterReaderIsEmpty() {
 reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertFalse(reader.isEmpty());
 reader.consume();
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderCurrent() {
 reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals('H', reader.current());
 reader.consume();
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testCharacterReaderConsume() {
 reader = new CharacterReader(new StringReader("Hello World"));
 char c = reader.consume();
 Assert.assertEquals('H', c);
 }

 @Test
 public void testCharacterReaderUnconsume() {
 reader = new CharacterReader(new StringReader("Hello World"));
 reader.consume();
 reader.unconsume();
 char c = reader.current();
 Assert.assertEquals('H', c);
 }

 @Test
 public void testCharacterReaderAdvance() {
 reader = new CharacterReader(new StringReader("Hello World"));
 reader.advance();
 reader.advance();
 reader.advance();
 int pos = reader.pos();
 Assert.assertEquals(3, pos);
 }

 @Test
 public void testCharacterReaderMark() {
 reader = new CharacterReader(new StringReader("Hello World"));
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 char c = reader.current();
 Assert.assertEquals('H', c);
 }

 @Test
 public void testCharacterReaderNextIndexOf() {
 reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals(7, reader.nextIndexOf('l'));
 Assert.assertEquals(-1, reader.nextIndexOf('Z'));
 }

 @Test
 public void testCharacterReaderNextIndexOfWithCharSequence() {
 reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals(5, reader.nextIndexOf("ll"));
 Assert.assertEquals(-1, reader.nextIndexOf("ZZ"));
 }

 @Test(expected = UncheckedIOException.class)
 public void testCharacterReaderIOException() {
 reader = new CharacterReader(new StringReader("Invalid Input"));
 reader.bufferUp();
 }

 @Test
 public void testCharacterReaderConsumeTo() {
 reader = new CharacterReader(new StringReader("Hello World"));
 String str = reader.consumeTo('l');
 Assert.assertEquals("Hello", str);
 }

 @Test
 public void testCharacterReaderConsumeToEnd() {
 reader = new CharacterReader(new StringReader("Hello World"));
 String str = reader.consumeToEnd();
 Assert.assertEquals("Hello World", str);
 }

 @Test
 public void testCharacterReaderConsumeLetterSequence() {
 reader = new CharacterReader(new StringReader("HelloWorld"));
 String str = reader.consumeLetterSequence();
 Assert.assertEquals("Hello", str);
 }

 @Test
 public void testCharacterReaderConsumeLetterThenDigitSequence() {
 reader = new CharacterReader(new StringReader("Hello1234World"));
 String str = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("Hello1234", str);
 }

 @Test
 public void testCharacterReaderConsumeHexSequence() {
 reader = new CharacterReader(new StringReader("0x1A"));
 String str = reader.consumeHexSequence();
 Assert.assertEquals("0x1A", str);
 }

 @Test
 public void testCharacterReaderConsumeDigitSequence() {
 reader = new CharacterReader(new StringReader("1234567890"));
 String str = reader.consumeDigitSequence();
 Assert.assertEquals("1234567890", str);
 }

 @Test
 public void testCharacterReaderMatches() {
 reader = new CharacterReader(new StringReader("HelloWorld"));
 Assert.assertTrue(reader.matches('l'));
 Assert.assertTrue(reader.matches("World"));
 Assert.assertFalse(reader.matches('x'));
 Assert.assertFalse(reader.matches("Hello"));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCase() {
 reader = new CharacterReader(new StringReader("HelloWorld"));
 Assert.assertTrue(reader.matchesIgnoreCase("World"));
 Assert.assertFalse(reader.matchesIgnoreCase("HELLO"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 reader = new CharacterReader(new StringReader("HelloWorld"));
 Assert.assertTrue(reader.matchesAny('l', 'w'));
 Assert.assertFalse(reader.matchesAny('x', 'w'));
 }

 @Test
 public void testCharacterReaderMatchesAnySorted() {
 reader = new CharacterReader(new StringReader("HelloWorld"));
 char[] arr = {'l', 'w'};
 Assert.assertTrue(reader.matchesAnySorted(arr));
 Assert.assertFalse(reader.matchesAnySorted(new char[]{'x', 'w'}));
 }

 @Test
 public void testCharacterReaderMatchesLetter() {
 reader = new CharacterReader(new StringReader("HelloWorld"));
 Assert.assertTrue(reader.matchesLetter());
 reader.advance();
 Assert.assertTrue(reader.matchesLetter());
 reader.advance();
 Assert.assertFalse(reader.matchesLetter());
 }

 @Test
 public void testCharacterReaderMatchesDigit() {
 reader = new CharacterReader(new StringReader("1234567890"));
 Assert.assertTrue(reader.matchesDigit());
 reader.consume();
 Assert.assertTrue(reader.matchesDigit());
 reader.consume();
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testCharacterReaderMatchConsume() {
 reader = new CharacterReader(new StringReader("HelloWorld"));
 Assert.assertTrue(reader.matchConsume("World"));
 }

 @Test
 public void testCharacterReaderMatchConsumeIgnoreCase() {
 reader = new CharacterReader(new StringReader("HELLOWorld"));
 Assert.assertTrue(reader.matchConsumeIgnoreCase("World"));
 }

 @Test
 public void testCharacterReaderContainsIgnoreCase() {
 reader = new CharacterReader(new StringReader("<html><title>Hello World</title>"));
 Assert.assertTrue(reader.containsIgnoreCase("/title"));
 Assert.assertTrue(reader.containsIgnoreCase("world"));
 }
}