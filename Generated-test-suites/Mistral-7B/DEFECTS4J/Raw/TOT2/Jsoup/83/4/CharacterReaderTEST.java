import static org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 // Test constructor with valid Reader input
 @Test
 public void testCharacterReaderWithValidReaderInput() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertEquals("Hello World", characterReader.toString());
 }

 // Test constructor with null Reader input
 @Test(expected = NullPointerException.class)
 public void testCharacterReaderWithNullReaderInput() {
 new CharacterReader(null);
 }

 // Test constructor with non-markSupported Reader input
 @Test(expected = IllegalArgumentException.class)
 public void testCharacterReaderWithNonMarkSupportedReaderInput() {
 new CharacterReader(new StringBufferReader("")); // StringBufferReader doesn't support mark()
 }

 // Test pos() method
 @Test
 public void testCharacterReaderPosMethod() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertEquals(0, characterReader.pos());
 characterReader.advance();
 assertEquals(1, characterReader.pos());
 }

 // Test isEmpty() method
 @Test
 public void testCharacterReaderIsEmptyMethod() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertFalse(characterReader.isEmpty());
 characterReader.consumeToEnd();
 assertTrue(characterReader.isEmpty());
 }

 // Test consume() method
 @Test
 public void testCharacterReaderConsumeMethod() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertEquals('H', characterReader.consume());
 assertEquals('e', characterReader.consume());
 // Test EOF
 assertEquals(CharacterReader.EOF, characterReader.consume());
 }

 // Test mark() and rewindToMark() methods
 @Test
 public void testCharacterReaderMarkAndRewindToMark() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 characterReader.mark();
 characterReader.advance();
 characterReader.rewindToMark();
 assertEquals('H', characterReader.current());
 }

 // Test nextIndexOf() method with valid char input
 @Test
 public void testCharacterReaderNextIndexOfWithValidCharInput() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertEquals(6, characterReader.nextIndexOf('W'));
 }

 // Test nextIndexOf() method with invalid char input
 @Test
 public void testCharacterReaderNextIndexOfWithInvalidCharInput() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertEquals(-1, characterReader.nextIndexOf('X'));
 }

 // Test consumeTo() method with valid char input
 @Test
 public void testCharacterReaderConsumeToWithValidCharInput() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertEquals("llo", characterReader.consumeTo('l'));
 }

 // Test consumeTo() method with EOF
 @Test
 public void testCharacterReaderConsumeToWithEOF() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 characterReader.consumeToEnd();
 assertEquals("", characterReader.consumeTo('X'));
 }

 // Test consumeTo() method with invalid char input
 @Test
 public void testCharacterReaderConsumeToWithInvalidCharInput() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertEquals("llo", characterReader.consumeTo('l'));
 assertEquals("", characterReader.consumeTo('X'));
 }

 // Test consumeToAny() method with valid char input
 @Test
 public void testCharacterReaderConsumeToAnyWithValidCharInput() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertEquals("ll", characterReader.consumeToAny(new char[]{'l', 'o'}));
 }

 // Test consumeToAny() method with invalid char input
 @Test
 public void testCharacterReaderConsumeToAnyWithInvalidCharInput() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertEquals("", characterReader.consumeToAny(new char[]{'X'}));
 }

 // Test consumeToEnd() method
 @Test
 public void testCharacterReaderConsumeToEnd() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertEquals("Hello World", characterReader.consumeToEnd());
 }

 // Test consumeLetterSequence() method
 @Test
 public void testCharacterReaderConsumeLetterSequence() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertEquals("ello", characterReader.consumeLetterSequence());
 }

 // Test consumeLetterThenDigitSequence() method
 @Test
 public void testCharacterReaderConsumeLetterThenDigitSequence() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello123World"));
 assertEquals("ello123", characterReader.consumeLetterThenDigitSequence());
 }

 // Test consumeHexSequence() method
 @Test
 public void testCharacterReaderConsumeHexSequence() {
 CharacterReader characterReader = new CharacterReader(new StringReader("123ABCdef456"));
 assertEquals("123ABCdef", characterReader.consumeHexSequence());
 }

 // Test consumeDigitSequence() method
 @Test
 public void testCharacterReaderConsumeDigitSequence() {
 CharacterReader characterReader = new CharacterReader(new StringReader("1234567890"));
 assertEquals("1234567890", characterReader.consumeDigitSequence());
 }

 // Test matches() method with valid char input
 @Test
 public void testCharacterReaderMatchesWithValidCharInput() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertTrue(characterReader.matches('l'));
 assertTrue(characterReader.matches("ll"));
 }

 // Test matches() method with invalid char input
 @Test
 public void testCharacterReaderMatchesWithInvalidCharInput() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertFalse(characterReader.matches('X'));
 assertFalse(characterReader.matches("XX"));
 }

 // Test matchesIgnoreCase() method with valid char input
 @Test
 public void testCharacterReaderMatchesIgnoreCaseWithValidCharInput() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertTrue(characterReader.matchesIgnoreCase("l"));
 assertTrue(characterReader.matchesIgnoreCase("LL"));
 }

 // Test matchesIgnoreCase() method with invalid char input
 @Test
 public void testCharacterReaderMatchesIgnoreCaseWithInvalidCharInput() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertFalse(characterReader.matchesIgnoreCase("X"));
 assertFalse(characterReader.matchesIgnoreCase("XX"));
 }

 // Test matchesAny() method with valid char input
 @Test
 public void testCharacterReaderMatchesAnyWithValidCharInput() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertTrue(characterReader.matchesAny(new char[]{'l', 'o'}));
 assertTrue(characterReader.matchesAny(new char[]{'l', 'o', 'W'}));
 }

 // Test matchesAny() method with invalid char input
 @Test
 public void testCharacterReaderMatchesAnyWithInvalidCharInput() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertFalse(characterReader.matchesAny(new char[]{'X'}));
 assertFalse(characterReader.matchesAny(new char[]{'X', 'X'}));
 }

 // Test matchesLetter() method
 @Test
 public void testCharacterReaderMatchesLetter() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertTrue(characterReader.matchesLetter());
 }

 // Test matchesDigit() method
 @Test
 public void testCharacterReaderMatchesDigit() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertTrue(characterReader.matchesDigit());
 }

 // Test matchConsume() method with valid String input
 @Test
 public void testCharacterReaderMatchConsumeWithValidStringInput() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertTrue(characterReader.matchConsume("ll"));
 assertEquals("ll", characterReader.toString());
 }

 // Test matchConsume() method with invalid String input
 @Test
 public void testCharacterReaderMatchConsumeWithInvalidStringInput() {
 CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
 assertFalse(characterReader.matchConsume("XX"));
 }

 // Test containsIgnoreCase() method with valid String input
 @Test
 public void testCharacterReaderContainsIgnoreCaseWithValidStringInput() {
 CharacterReader characterReader = new CharacterReader(new StringReader("<html><title>Test</title></html>"));
 assertTrue(characterReader.containsIgnoreCase("/title"));
 }

 // Test containsIgnoreCase() method with invalid String input
 @Test
 public void testCharacterReaderContainsIgnoreCaseWithInvalidStringInput() {
 CharacterReader characterReader = new CharacterReader(new StringReader("<html><title>Test</title></html>"));
 assertFalse(characterReader.containsIgnoreCase("/body"));
 }
}