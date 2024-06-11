import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Test
 public void testCharacterReaderConstructor() {
 char[] expectedInput = {'t', 'e', 's', 't', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'R', 'e', 'a', 'd', 'e', 'r'};
 CharacterReader characterReader = new CharacterReader(new String(expectedInput));

 Assert.assertEquals(expectedInput.length, characterReader.length());
 Assert.assertEquals(0, characterReader.pos());
 Assert.assertFalse(characterReader.isEmpty());
 }

 @Test
 public void testCurrentAndConsume() {
 characterReader = new CharacterReader("test");

 Assert.assertEquals('t', characterReader.current());
 Assert.assertEquals('t', characterReader.consume());
 Assert.assertEquals('e', characterReader.current());
 Assert.assertEquals(1, characterReader.pos());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumePastEnd() {
 characterReader = new CharacterReader("test");
 characterReader.consumeToEnd();
 characterReader.consume();
 }

 @Test
 public void testConsumeString() {
 characterReader = new CharacterReader("testString");

 String expectedConsumedString = "String";
 String consumedString = characterReader.consumeTo(expectedConsumedString.charAt(0));

 Assert.assertEquals(expectedConsumedString, consumedString);
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testConsumeToEndOfEmptyString() {
 characterReader = new CharacterReader("");
 characterReader.consumeToEnd();
 }

 @Test
 public void testConsumeLetterSequence() {
 characterReader = new CharacterReader("testLetterSequence");
 String consumedLetterSequence = characterReader.consumeLetterSequence();
 Assert.assertEquals("test", consumedLetterSequence);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeLetterSequencePastEnd() {
 characterReader = new CharacterReader("testLetterSequence");
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 }

 @Test
 public void testConsumeHexSequence() {
 characterReader = new CharacterReader("testHexSequence");
 String consumedHexSequence = characterReader.consumeHexSequence();
 Assert.assertEquals("sequence", consumedHexSequence);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeHexSequencePastEnd() {
 characterReader = new CharacterReader("testHexSequence");
 characterReader.consumeHexSequence();
 characterReader.consumeHexSequence();
 }

 @Test
 public void testConsumeDigitSequence() {
 characterReader = new CharacterReader("test12345DigitSequence");
 String consumedDigitSequence = characterReader.consumeDigitSequence();
 Assert.assertEquals("5", consumedDigitSequence);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeDigitSequencePastEnd() {
 characterReader = new CharacterReader("test12345DigitSequence");
 characterReader.consumeDigitSequence();
 characterReader.consumeDigitSequence();
 }

 @Test
 public void testMatches() {
 characterReader = new CharacterReader("testMatches");

 Assert.assertTrue(characterReader.matches('t'));
 Assert.assertTrue(characterReader.matches("es"));
 Assert.assertFalse(characterReader.matches(' '));
 }

 @Test
 public void testMatchesString() {
 characterReader = new CharacterReader("testMatchesString");

 String expectedSeq = "es";
 Assert.assertTrue(characterReader.matches(expectedSeq));
 }

 @Test
 public void testMatchesIgnoreCase() {
 characterReader = new CharacterReader("testMatchesIgnoreCase");

 String expectedSeq = "ES";
 Assert.assertTrue(characterReader.matchesIgnoreCase(expectedSeq));
 }

 @Test
 public void testMatchesAny() {
 characterReader = new CharacterReader("testMatchesAny");

 char[] expectedSeq = {'a', 'r'};
 Assert.assertTrue(characterReader.matchesAny(expectedSeq));
 }

 @Test
 public void testMatchesLetter() {
 characterReader = new CharacterReader("testMatchesLetter");

 Assert.assertTrue(characterReader.matchesLetter());
 Assert.assertFalse(characterReader.matchesDigit());
 }

 @Test
 public void testMatchesDigit() {
 characterReader = new CharacterReader("testMatchesDigit");

 Assert.assertFalse(characterReader.matchesLetter());
 Assert.assertTrue(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 characterReader = new CharacterReader("testMatchConsumeString");
 String expectedSeq = "String";
 String consumedString = characterReader.matchConsume(expectedSeq);
 Assert.assertEquals(expectedSeq, consumedString);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 characterReader = new CharacterReader("testMatchConsumeIgnoreCase");
 String expectedSeq = "String";
 String consumedString = characterReader.matchConsumeIgnoreCase(expectedSeq);
 Assert.assertEquals(expectedSeq, consumedString);
 }

 @Test
 public void testContainsIgnoreCase() {
 characterReader = new CharacterReader("testContainsIgnoreCase<title>");
 String expectedSeq = "<title>";
 Assert.assertTrue(characterReader.containsIgnoreCase(expectedSeq));
 }

 @Test
 public void testToString() {
 characterReader = new CharacterReader("testToString");
 String expectedString = "ng";
 String currentString = characterReader.toString();
 Assert.assertEquals(expectedString, currentString);
 }
}