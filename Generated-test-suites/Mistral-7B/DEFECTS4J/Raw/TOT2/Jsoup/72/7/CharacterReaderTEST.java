import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.CharacterReader.TokeniserState;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderConstruction() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 assertEquals("Hello World", reader.toString());
 }

 @Test
 public void testEmptyCharacterReader() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testConsumeSingleCharacter() {
 CharacterReader reader = new CharacterReader(new StringReader("ABC"));
 assertEquals('A', reader.consume());
 }

 @Test(expected = UncheckedIOException.class)
 public void testIOExceptionWhenBufferUpFails() {
 CharacterReader reader = new CharacterReader(new StringReader("A") {
 @Override
 public int read(char[] c, int off, int length) throws IOException {
 throw new IOException();
 }
 });
 reader.consume();
 }

 @Test
 public void testBufferUp() {
 CharacterReader reader = new CharacterReader(new StringReader("12345"));
 reader.advance();
 reader.advance();
 reader.advance();
 reader.bufferUp();
 assertEquals("123", reader.consumeTo(TokeniserState.digit));
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
 String result = reader.consumeToEnd();
 assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", result);
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("abc123ABC"));
 String result = reader.consumeLetterSequence();
 assertEquals("abc", result);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("abc123"));
 String result = reader.consumeLetterThenDigitSequence();
 assertEquals("abc123", result);
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("0x1a"));
 String result = reader.consumeHexSequence();
 assertEquals("1a", result);
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("12345"));
 String result = reader.consumeDigitSequence();
 assertEquals("12345", result);
 }

 @Test
 public void testMatchesSingleCharacter() {
 CharacterReader reader = new CharacterReader(new StringReader("ABC"));
 assertTrue(reader.matches('A'));
 }

 @Test
 public void testMatchesString() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
 assertTrue(reader.matches("ABC"));
 }

 @Test
 public void testMatchesIgnoreCaseString() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
 assertTrue(reader.matchesIgnoreCase("abc"));
 }

 @Test
 public void testMatchesAnySingleCharacter() {
 CharacterReader reader = new CharacterReader(new StringReader("ABC"));
 assertTrue(reader.matchesAny('A'));
 }

 @Test
 public void testMatchesAnyMultipleCharacters() {
 CharacterReader reader = new CharacterReader(new StringReader("ABC"));
 assertTrue(reader.matchesAny(new char[]{'A', 'B'}));
 }

 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("ABC"));
 assertTrue(reader.matchesAnySorted(new char[]{'B', 'A'}));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader(new StringReader("ABC"));
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader(new StringReader("123"));
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsumeSingleCharacter() {
 CharacterReader reader = new CharacterReader(new StringReader("ABC"));
 assertTrue(reader.matchConsume('A'));
 }

 @Test
 public void testMatchConsumeString() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
 assertTrue(reader.matchConsume("AB"));
 }

 @Test
 public void testMatchConsumeIgnoreCaseString() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
 assertTrue(reader.matchConsumeIgnoreCase("ab"));
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head></html>"));
 assertTrue(reader.containsIgnoreCase("title"));
 }

 @Test
 public void testToString() {
 CharacterReader reader = new CharacterReader(new StringReader("ABC"));
 assertEquals("ABC", reader.toString());
 }
}