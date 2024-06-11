import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setUp() {
 char[] inputChars = {'t', 'e', 's', 't', ' ', 's', 't', 'r', 'i', 'n', 'g', '\n'};
 String input = new String(inputChars);
 characterReader = new CharacterReader(input);
 }

 @Test
 public void testConstructor() {
 CharacterReader characterReader = new CharacterReader(null);
 Assert.assertNull(characterReader.input);
 characterReader = new CharacterReader("");
 Assert.assertEquals("", characterReader.input);
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, characterReader.pos());
 characterReader.advance();
 Assert.assertEquals(1, characterReader.pos());
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(characterReader.isEmpty());
 characterReader.consumeToEnd();
 Assert.assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('t', characterReader.current());
 characterReader.advance();
 Assert.assertEquals('e', characterReader.current());
 }

 @Test
 public void testConsume() {
 Assert.assertEquals('t', characterReader.consume());
 Assert.assertEquals('e', characterReader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 characterReader.consumeToEnd();
 characterReader.consume();
 }

 @Test
 public void testUnconsume() {
 characterReader.consume();
 characterReader.unconsume();
 Assert.assertEquals('t', characterReader.current());
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 Assert.assertEquals('e', characterReader.current());
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.consume();
 characterReader.rewindToMark();
 Assert.assertEquals('t', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 String expected = "str";
 String actual = characterReader.consumeAsString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeTo() {
 String expected = "ing";
 String actual = characterReader.consumeTo('n');
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToEnd() {
 String expected = "string";
 String actual = characterReader.consumeToEnd();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "test";
 String actual = characterReader.consumeLetterSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "testing";
 String actual = characterReader.consumeHexSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "12345";
 String actual = characterReader.consumeDigitSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(characterReader.matches('s'));
 Assert.assertFalse(characterReader.matches('r'));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(characterReader.matches("string"));
 Assert.assertFalse(characterReader.matches("test"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(characterReader.matchesIgnoreCase("String"));
 Assert.assertFalse(characterReader.matchesIgnoreCase("test"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader characterReader = new CharacterReader("abracadabra");
 Assert.assertTrue(characterReader.matchesAny('a', 'b', 'r'));
 Assert.assertFalse(characterReader.matchesAny('c', 'b', 'r'));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(characterReader.matchesLetter());
 characterReader.consumeLetterSequence();
 Assert.assertFalse(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertFalse(characterReader.matchesDigit());
 characterReader.consumeDigitSequence();
 Assert.assertTrue(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String expected = "string";
 characterReader.advance();
 characterReader.advance();
 characterReader.matchConsume("ing");
 Assert.assertEquals(expected, characterReader.input.substring(characterReader.pos));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String expected = "String";
 characterReader.advance();
 characterReader.advance();
 characterReader.matchConsumeIgnoreCase("ING");
 Assert.assertEquals(expected, characterReader.input.substring(characterReader.pos));
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(characterReader.containsIgnoreCase("string"));
 Assert.assertFalse(characterReader.containsIgnoreCase("testing"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToNull() {
 characterReader.consumeTo(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToEmptyString() {
 characterReader.consumeTo("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToArrayNull() {
 characterReader.consumeToAny(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToArrayEmpty() {
 characterReader.consumeToAny(new char[0]);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndPastEnd() {
 characterReader.consumeToEnd();
 characterReader.consumeToEnd();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndPastMiddle() {
 characterReader.consumeToEnd();
 characterReader.consumeToEnd();
 characterReader.consumeToEnd();
 characterReader.consumeToEnd();
 characterReader.consumeToEnd();
 characterReader.consumeToEnd();
 characterReader.consumeToEnd();
 characterReader.consumeToEnd();
 characterReader.consumeToEnd();
 characterReader.consumeToEnd();
 characterReader.consumeToEnd();
 characterReader.consumeToEnd();
 characterReader.consumeToEnd();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToNullCharacter() {
 characterReader.consumeTo((char) 0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToNonExistentCharacter() {
 characterReader.consumeTo((char) 'z');
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToSequenceEmpty() {
 characterReader.consumeToAny();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToSequenceMismatchFirst() {
 characterReader.consumeToAny('x', 'b');
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToSequenceMismatchLast() {
 characterReader.consumeToAny('a', 'z');
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToSequenceMismatchMiddle() {
 characterReader.consumeToAny('a', 'c', 'z');
 }

 @Test
 public void testConsumeToSequenceMatch() {
 String expected = "string";
 characterReader.consumeToAny('s', 't', 'r', 'i', 'n', 'g');
 Assert.assertEquals(expected, characterReader.input.substring(characterReader.pos));
 }
}