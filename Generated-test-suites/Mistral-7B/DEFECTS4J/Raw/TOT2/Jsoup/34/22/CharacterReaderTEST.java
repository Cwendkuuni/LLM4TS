import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 @Test
 public void testConstructor() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertNotNull(reader);
 }

 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertFalse(reader.isEmpty());
 reader.consume();
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals('T', reader.current());
 reader.consume();
 Assert.assertEquals('h', reader.current());
 }

 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("This is a test string");
 String consumed = "";
 while (!reader.isEmpty()) {
 consumed += reader.consume();
 }
 Assert.assertEquals("This is a test", consumed);
 }

 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader("This is a test string");
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('T', reader.current());
 }

 @Test
 public void testAdvance() {
 CharacterReader reader = new CharacterReader("This is a test string");
 reader.advance();
 Assert.assertEquals('h', reader.current());
 }

 @Test
 public void testMark() {
 CharacterReader reader = new CharacterReader("This is a test string");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('T', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals("is a test", reader.consumeAsString());
 }

 @Test
 public void testNextIndexOf() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals(2, reader.nextIndexOf('s'));
 Assert.assertEquals(-1, reader.nextIndexOf('z'));
 }

 @Test
 public void testNextIndexOf_CharSequence() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals(12, reader.nextIndexOf("est string"));
 }

 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals("is a test", reader.consumeTo(' '));
 Assert.assertEquals("test string", reader.consumeToEnd());
 }

 @Test
 public void testConsumeTo_CharSequence() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals("is a test", reader.consumeTo(" is a "));
 Assert.assertEquals("string", reader.consumeToEnd());
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("123abcd123efg456");
 Assert.assertEquals("123", reader.consumeToAny('1', '2'));
 Assert.assertEquals("efg", reader.consumeToAny('e', 'g'));
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals("This is a test string", reader.consumeToEnd());
 }

 @Test(expected = AssertionError.class)
 public void testConsumeLetterSequence_Empty() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertFalse(reader.isEmpty());
 String letterSeq = reader.consumeLetterSequence();
 Assert.assertEquals("", letterSeq);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("This is 123 a test");
 Assert.assertEquals("is 123 a test", reader.consumeToAny(' ', ' '));
 String letterThenDigitSeq = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("is 123", letterThenDigitSeq);
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("This is &amp;0x10hex &amp;0x20hex");
 String hexSeq = reader.consumeHexSequence();
 Assert.assertEquals("10hex 20hex", hexSeq);
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 String digitSeq = reader.consumeDigitSequence();
 Assert.assertEquals("1234567890", digitSeq);
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matches('t'));
 Assert.assertTrue(reader.matches("is"));
 Assert.assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatches_String() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matches("is a test string"));
 Assert.assertFalse(reader.matches("is not a test string"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matchesIgnoreCase("is a test string"));
 Assert.assertTrue(reader.matchesIgnoreCase("Is A Test String"));
 Assert.assertFalse(reader.matchesIgnoreCase("Is not a test string"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matchesAny('t', 's'));
 Assert.assertFalse(reader.matchesAny('z'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matchesLetter());
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matchesDigit());
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matchConsume("is a test string"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matchConsumeIgnoreCase("Is a test string"));
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test Title</title></head><body>Test Content</body></html>");
 Assert.assertTrue(reader.containsIgnoreCase("title"));
 Assert.assertTrue(reader.containsIgnoreCase("TEST TITLE"));
 Assert.assertTrue(reader.containsIgnoreCase("TEST CONTENT"));
 Assert.assertFalse(reader.containsIgnoreCase("footer"));
 }

 @Test
 public void testToString() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals("This is a test string", reader.toString());
 }
}