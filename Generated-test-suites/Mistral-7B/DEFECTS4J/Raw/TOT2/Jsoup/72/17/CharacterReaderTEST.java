import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Test
 public void testCharacterReaderConstructors() {
 reader = new CharacterReader(new StringReader("test string"));
 Assert.assertEquals("test string", reader.toString());
 reader = new CharacterReader(new StringReader(""), 10);
 Assert.assertEquals("", reader.toString());
 reader = new CharacterReader(new StringReader("test string"), 10);
 Assert.assertEquals("test string", reader.toString());
 reader = new CharacterReader(new StringReader("test string"), 100);
 Assert.assertEquals("test string", reader.toString());
 reader = new CharacterReader(new StringReader("test string"), -1);
 Assert.assertEquals("test string", reader.toString());
 reader = new CharacterReader(new StringReader("test string"), 512);
 Assert.assertEquals("test string", reader.toString());
 }

 @Test
 public void testConstructorWithIOException() {
 try {
 reader = new CharacterReader(new StringReader("test string"), -100);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testReaderPositionBeforeCreation() {
 reader = new CharacterReader(new StringReader("test string"));
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testReaderIsEmptyBeforeCreation() {
 reader = new CharacterReader(new StringReader("test string"));
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testReaderCurrentBeforeCreation() {
 reader = new CharacterReader(new StringReader("test string"));
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testReaderConsumeBeforeCreation() {
 reader = new CharacterReader(new StringReader("test string"));
 Assert.assertEquals(CharacterReader.EOF, reader.consume());
 }

 @Test
 public void testReaderAdvanceBeforeCreation() {
 reader = new CharacterReader(new StringReader("test string"));
 reader.advance();
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testReaderMarkBeforeCreation() {
 reader = new CharacterReader(new StringReader("test string"));
 reader.mark();
 reader.rewindToMark();
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testReaderNextIndexOfWithSingleCharacter() {
 reader = new CharacterReader(new StringReader("testtest"));
 Assert.assertEquals(1, reader.nextIndexOf('t'));
 }

 @Test
 public void testReaderNextIndexOfWithMultipleCharacters() {
 reader = new CharacterReader(new StringReader("testtest"));
 Assert.assertEquals(5, reader.nextIndexOf("est"));
 }

 @Test
 public void testReaderNextIndexOfWithEmptySequence() {
 reader = new CharacterReader(new StringReader("testtest"));
 Assert.assertEquals(-1, reader.nextIndexOf(""));
 }

 @Test
 public void testReaderConsumeToSingleCharacter() {
 reader = new CharacterReader(new StringReader("testtest"));
 String result = reader.consumeTo('t');
 Assert.assertEquals("est", result);
 }

 @Test
 public void testReaderConsumeToMultipleCharacters() {
 reader = new CharacterReader(new StringReader("testtest"));
 String result = reader.consumeTo("est");
 Assert.assertEquals("test", result);
 }

 @Test
 public void testReaderConsumeToEnd() {
 reader = new CharacterReader(new StringReader("testtest"));
 String result = reader.consumeToEnd();
 Assert.assertEquals("testtest", result);
 }

 @Test
 public void testReaderConsumeToAnyWithSingleCharacter() {
 reader = new CharacterReader(new StringReader("testtest"));
 String result = reader.consumeToAny('t');
 Assert.assertEquals("esttest", result);
 }

 @Test
 public void testReaderConsumeToAnyWithMultipleCharacters() {
 reader = new CharacterReader(new StringReader("testtest"));
 String result = reader.consumeToAny(new char[]{'e', 's'});
 Assert.assertEquals("test", result);
 }

 @Test
 public void testReaderConsumeToAnySortedWithSingleCharacter() {
 reader = new CharacterReader(new StringReader("testtest"));
 String result = reader.consumeToAnySorted('t');
 Assert.assertEquals("esttest", result);
 }

 @Test
 public void testReaderConsumeToAnySortedWithMultipleCharacters() {
 reader = new CharacterReader(new StringReader("testtest"));
 String result = reader.consumeToAnySorted(new char[]{'e', 's'});
 Assert.assertEquals("test", result);
 }

 @Test
 public void testReaderConsumeData() {
 reader = new CharacterReader(new StringReader("&<test>"));
 String result = reader.consumeData();
 Assert.assertEquals("&<test>", result);
 }

 @Test
 public void testReaderConsumeTagName() {
 reader = new CharacterReader(new StringReader("<test>"));
 String result = reader.consumeTagName();
 Assert.assertEquals("test", result);
 }

 @Test
 public void testReaderConsumeToEnd() {
 reader = new CharacterReader(new StringReader("test"));
 String result = reader.consumeToEnd();
 Assert.assertEquals("test", result);
 }

 @Test
 public void testReaderConsumeLetterSequence() {
 reader = new CharacterReader(new StringReader("testtesttest"));
 String result = reader.consumeLetterSequence();
 Assert.assertEquals("testtest", result);
 }

 @Test
 public void testReaderConsumeLetterThenDigitSequence() {
 reader = new CharacterReader(new StringReader("test123test456"));
 String result = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("test123test", result);
 }

 @Test
 public void testReaderConsumeHexSequence() {
 reader = new CharacterReader(new StringReader("&x01&x02&x03"));
 String result = reader.consumeHexSequence();
 Assert.assertEquals("&x01&x02&x03", result);
 }

 @Test
 public void testReaderConsumeDigitSequence() {
 reader = new CharacterReader(new StringReader("1234567890"));
 String result = reader.consumeDigitSequence();
 Assert.assertEquals("1234567890", result);
 }

 @Test
 public void testReaderMatchesWithSingleCharacter() {
 reader = new CharacterReader(new StringReader("testtest"));
 Assert.assertTrue(reader.matches('t'));
 }

 @Test
 public void testReaderMatchesWithMultipleCharacters() {
 reader = new CharacterReader(new StringReader("testtest"));
 Assert.assertTrue(reader.matches("es"));
 }

 @Test
 public void testReaderMatchesIgnoreCaseWithSingleCharacter() {
 reader = new CharacterReader(new StringReader("testtest"));
 Assert.assertTrue(reader.matchesIgnoreCase("t"));
 }

 @Test
 public void testReaderMatchesIgnoreCaseWithMultipleCharacters() {
 reader = new CharacterReader(new StringReader("testtest"));
 Assert.assertTrue(reader.matchesIgnoreCase("es"));
 }

 @Test
 public void testReaderMatchesAnyWithSingleCharacter() {
 reader = new CharacterReader(new StringReader("testtest"));
 Assert.assertTrue(reader.matchesAny('t'));
 }

 @Test
 public void testReaderMatchesAnyWithMultipleCharacters() {
 reader = new CharacterReader(new StringReader("testtest"));
 Assert.assertTrue(reader.matchesAny(new char[]{'t', 'e'}));
 }

 @Test
 public void testReaderMatchesLetter() {
 reader = new CharacterReader(new StringReader("testtest"));
 Assert.assertTrue(reader.matchesLetter());
 }

 @Test
 public void testReaderMatchesDigit() {
 reader = new CharacterReader(new StringReader("123456"));
 Assert.assertTrue(reader.matchesDigit());
 }

 @Test
 public void testReaderMatchConsumeWithSingleCharacter() {
 reader = new CharacterReader(new StringReader("testtest"));
 Assert.assertTrue(reader.matchConsume('t'));
 }

 @Test
 public void testReaderMatchConsumeIgnoreCaseWithSingleCharacter() {
 reader = new CharacterReader(new StringReader("testtest"));
 Assert.assertTrue(reader.matchConsumeIgnoreCase("t"));
 }

 @Test
 public void testReaderContainsIgnoreCaseWithSingleCharacter() {
 reader = new CharacterReader(new StringReader("testtest"));
 Assert.assertTrue(reader.containsIgnoreCase("t"));
 }

 @Test
 public void testReaderContainsIgnoreCaseWithMultipleCharacters() {
 reader = new CharacterReader(new StringReader("testtest"));
 Assert.assertTrue(reader.containsIgnoreCase("es"));
 }

 @Test
 public void testReaderToString() {
 reader = new CharacterReader(new StringReader("testtest"));
 Assert.assertEquals("testtest", reader.toString());
 }
}