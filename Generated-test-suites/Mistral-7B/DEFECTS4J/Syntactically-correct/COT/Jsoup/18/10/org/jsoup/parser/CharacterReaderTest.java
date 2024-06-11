import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private String input;
 private CharacterReader reader;

 @Before
 public void setup() {
 input = "This is a test string for CharacterReader.";
 reader = new CharacterReader(input);
 }

 @Test
 public void testConstructor() {
 assertNotNull(reader);
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('T', reader.current());
 reader.consume();
 Assert.assertEquals('h', reader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeAfterEnd() {
 reader.consume();
 reader.consume(); // consume all characters
 reader.consume(); // should throw an exception
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('T', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 Assert.assertEquals('h', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('T', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 String consumed = reader.consumeAsString();
 Assert.assertEquals("is a test string for CharacterReader.", consumed);
 }

 @Test
 public void testConsumeTo() {
 String consumed = reader.consumeTo('s');
 Assert.assertEquals("is a test string for CharacterReader.", consumed);
 consumed = reader.consumeTo('t');
 Assert.assertEquals("is a test string for CharacterReader.", consumed);
 consumed = reader.consumeTo('r');
 Assert.assertEquals("is a test string for CharacterReader.", consumed);
 consumed = reader.consumeTo('.');
 Assert.assertEquals("for CharacterReader.", consumed);
 consumed = reader.consumeToEnd();
 Assert.assertEquals("", consumed);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToInvalidChar() {
 reader.consumeTo('z'); // 'z' is not present in the input
 }

 @Test
 public void testConsumeToString() {
 String consumed = reader.consumeTo("string");
 Assert.assertEquals("is a test for CharacterReader.", consumed);
 consumed = reader.consumeTo("Reader.");
 Assert.assertEquals("", consumed);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToInvalidString() {
 reader.consumeTo("zString"); // "zString" is not present in the input
 }

 @Test
 public void testConsumeToAny() {
 String consumed = reader.consumeToAny('s', 't');
 Assert.assertEquals("is a test for CharacterReader.", consumed);
 consumed = reader.consumeToAny('a', 'b', 'c');
 Assert.assertEquals("", consumed);
 }

 @Test
 public void testConsumeToEnd() {
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("is", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("is", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals("1", consumed);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(reader.matches('t'));
 Assert.assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(reader.matches("is"));
 Assert.assertFalse(reader.matches("isn't"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(reader.matchesIgnoreCase("is"));
 Assert.assertTrue(reader.matchesIgnoreCase("IS"));
 Assert.assertFalse(reader.matchesIgnoreCase("isn't"));
 }

 @Test
 public void testMatchesAny() {
 Assert.assertTrue(reader.matchesAny('i', 's'));
 Assert.assertFalse(reader.matchesAny('z'));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String consumed = reader.matchConsume("is");
 Assert.assertEquals("is", consumed);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String consumed = reader.matchConsumeIgnoreCase("Is");
 Assert.assertEquals("is", consumed);
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(reader.containsIgnoreCase("title"));
 Assert.assertTrue(reader.containsIgnoreCase("Style"));
 Assert.assertFalse(reader.containsIgnoreCase("Ztitle"));
 }

 @Test
 public void testToString() {
 Assert.assertEquals("for CharacterReader.", reader.toString());
 }

}