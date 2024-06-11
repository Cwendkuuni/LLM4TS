import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setUp() {
 char[] testInput = {'t', 'e', 's', 't', '_', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'R', 'e', 'a', 'd', 'e', 'r', '.', ' '};
 this.characterReader = new CharacterReader(new String(testInput));
 }

 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader("test");
 int pos = reader.pos();
 assertEquals(0, pos);
 }

 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader("test");
 assertEquals('t', reader.current());
 }

 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("test");
 assertEquals('t', reader.consume());
 assertEquals('e', reader.current());
 }

 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader("test");
 reader.consume();
 reader.unconsume();
 assertEquals('t', reader.current());
 }

 @Test
 public void testAdvance() {
 CharacterReader reader = new CharacterReader("test");
 reader.advance();
 assertEquals('e', reader.current());
 }

 @Test
 public void testMark() {
 CharacterReader reader = new CharacterReader("test");
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals('t', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("test");
 assertEquals("t", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader("testCharReader");
 String expected = "CharReader";
 String result = reader.consumeTo(expected.charAt(0));
 assertEquals(expected.substring(0, 1), result);
 result = reader.consumeTo(expected);
 assertEquals(expected, reader.toString());
 }

 @Test
 public void testConsumeToString() {
 CharacterReader reader = new CharacterReader("testCharacterReader");
 String expected = "CharacterReader";
 String result = reader.consumeTo(expected.charAt(0));
 assertEquals(expected.substring(0, 1), result);
 result = reader.consumeTo(expected);
 assertEquals(expected, reader.toString());
 }

 @Test
 public void testConsumeToAnySingleCharacter() {
 CharacterReader reader = new CharacterReader("testCharacterReader");
 String result = reader.consumeToAny('r');
 assertEquals("testCharacter", result);
 }

 @Test
 public void testConsumeToAnyMultipleCharacters() {
 CharacterReader reader = new CharacterReader("testCharacterReaderForTest");
 String result = reader.consumeToAny('t', 'r');
 assertEquals("testCharacterFor", result);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("");
 String result = reader.consumeToEnd();
 Assert.fail("Expected an IndexOutOfBoundsException but no exception was thrown.");
 try {
 assertEquals("", result);
 } catch (IndexOutOfBoundsException e) {
 // Expected exception, test passed
 }
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("testCharacterReaderForTest");
 String expected = "testCharacterFor";
 String result = reader.consumeLetterSequence();
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeLetterSequenceWithEmptyInput() {
 CharacterReader reader = new CharacterReader("");
 String result = reader.consumeLetterSequence();
 Assert.fail("Expected an IllegalArgumentException but no exception was thrown.");
 try {
 assertEquals("", result);
 } catch (IllegalArgumentException e) {
 // Expected exception, test passed
 }
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("test#0x1234abcd");
 String expected = "0x1234abcd";
 String result = reader.consumeHexSequence();
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeHexSequenceWithEmptyInput() {
 CharacterReader reader = new CharacterReader("");
 String result = reader.consumeHexSequence();
 Assert.fail("Expected an IllegalArgumentException but no exception was thrown.");
 try {
 assertEquals("", result);
 } catch (IllegalArgumentException e) {
 // Expected exception, test passed
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeDigitSequenceWithInputContainingNonDigits() {
 CharacterReader reader = new CharacterReader("test1_2a3b4c");
 String result = reader.consumeDigitSequence();
 Assert.fail("Expected an IllegalArgumentException but no exception was thrown.");
 try {
 assertEquals("", result);
 } catch (IllegalArgumentException e) {
 // Expected exception, test passed
 }
 }

 @Test
 public void testConsumeDigitSequenceWithInputContainingOnlyDigits() {
 CharacterReader reader = new CharacterReader("test12345");
 String expected = "12345";
 String result = reader.consumeDigitSequence();
 assertEquals(expected, result);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testMatches() {
 CharacterReader reader = new CharacterReader("test");
 boolean result = reader.matches('z');
 Assert.fail("Expected an IndexOutOfBoundsException but no exception was thrown.");
 try {
 assertEquals(false, result);
 } catch (IndexOutOfBoundsException e) {
 // Expected exception, test passed
 }
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("Test String");
 boolean result = reader.matchesIgnoreCase("string");
 assertTrue(result);
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("testCharacterReaderForTest");
 boolean result = reader.matchesAny('t', 'r');
 assertTrue(result);
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("testCharacterReaderForTest");
 boolean result = reader.matchesLetter();
 assertTrue(result);
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("test12345");
 boolean result = reader.matchesDigit();
 assertTrue(result);
 }

 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader("testCharacterReaderForTest");
 String expected = "testCharacterFor";
 boolean result = reader.matchConsume("testCharacterFor");
 assertTrue(result);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("testCharacterReaderForTest");
 String expected = "testCharacterFor";
 boolean result = reader.matchConsumeIgnoreCase("Test Character For");
 assertTrue(result);
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("testCharacterReaderFor<title>Test<title/>");
 boolean result = reader.containsIgnoreCase("<title>");
 assertTrue(result);
 }

 @Test
 public void testToString() {
 CharacterReader reader = new CharacterReader("testCharacterReaderFor<title>Test<title/>");
 String expected = "TestCharacterReaderFor<title>Test<title/>";
 String result = reader.toString();
 assertEquals(expected, result);
 }
}