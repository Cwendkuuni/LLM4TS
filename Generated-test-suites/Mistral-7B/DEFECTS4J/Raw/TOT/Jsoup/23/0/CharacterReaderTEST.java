import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 @Test
 void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 Assertions.assertTrue(reader.isEmpty());
 }

 @Test
 void testConsumeSingleCharacter() {
 CharacterReader reader = new CharacterReader("abc");
 Assertions.assertEquals('a', reader.consume());
 }

 @Test
 void testConsumeMultipleCharacters() {
 CharacterReader reader = new CharacterReader("abcd");
 Assertions.assertEquals('a', reader.consume());
 Assertions.assertEquals('b', reader.consume());
 Assertions.assertEquals('c', reader.consume());
 Assertions.assertEquals('d', reader.consume());
 Assertions.assertTrue(reader.isEmpty());
 }

 @Test
 void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abc");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assertions.assertEquals('a', reader.current());
 }

 @Test
 void testConsumeSubstring() {
 CharacterReader reader = new CharacterReader("abcdef");
 String substring = reader.consumeTo('d');
 Assertions.assertEquals("abc", substring);
 }

 @Test
 void testConsumeString() {
 CharacterReader reader = new CharacterReader("abcdef");
 String substring = reader.consumeAsString();
 Assertions.assertEquals("abc", substring);
 }

 @Test
 void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 String substring = reader.consumeToEnd();
 Assertions.assertEquals("def", substring);
 }

 @Test
 void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abCdEfGhijklMnOpQrSTuvWxYz");
 String sequence = reader.consumeLetterSequence();
 Assertions.assertEquals("abC", sequence);
 }

 @Test
 void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("0123456789ABCDEFabcdef");
 String sequence = reader.consumeHexSequence();
 Assertions.assertEquals("0123", sequence);
 }

 @Test
 void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("0123456789");
 String sequence = reader.consumeDigitSequence();
 Assertions.assertEquals("0123", sequence);
 }

 @Test
 void testMatchesSingleCharacter() {
 CharacterReader reader = new CharacterReader("abc");
 Assertions.assertTrue(reader.matches('a'));
 }

 @Test
 void testMatchesMultipleCharacters() {
 CharacterReader reader = new CharacterReader("abc");
 Assertions.assertTrue(reader.matches("ab"));
 }

 @Test
 void testMatchesString() {
 CharacterReader reader = new CharacterReader("abcabc");
 Assertions.assertTrue(reader.matches("abc"));
 }

 @Test
 void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("AbCdEfGhIjKlMnOpQrStUvWxYz");
 Assertions.assertTrue(reader.matchesIgnoreCase("ab"));
 }

 @Test
 void testMatchesAnySingleCharacter() {
 CharacterReader reader = new CharacterReader("abcd");
 Assertions.assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
 }

 @Test
 void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcABC");
 Assertions.assertTrue(reader.matchesLetter());
 }

 @Test
 void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("0123456789");
 Assertions.assertTrue(reader.matchesDigit());
 }

 @Test
 void testMatchConsumeString() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 Assertions.assertTrue(reader.matchConsume("ab"));
 }

 @Test
 void testMatchConsumeIgnoreCaseString() {
 CharacterReader reader = new CharacterReader("AbCdEfGhIjKlMnOpQrStUvWxYz");
 Assertions.assertTrue(reader.matchConsumeIgnoreCase("ab"));
 }

 @Test
 void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body>Content</body></html>");
 Assertions.assertTrue(reader.containsIgnoreCase("title"));
 }
}