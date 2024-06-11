import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] testInput = {'a', 'b', 'c', 'd', 'e', 'f'};
 String testInputString = new String(testInput);
 reader = new CharacterReader(testInputString);
 }

 @Test
 public void testIsEmpty() {
 assertFalse(reader.isEmpty());
 reader.consume();
 reader.consume();
 reader.consume();
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 reader.consume();
 reader.consume();
 reader.consume();
 assertEquals(3, reader.pos());
 }

 @Test
 public void testCurrent() {
 assertEquals('a', reader.current());
 reader.consume();
 assertEquals('b', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('a', reader.current());
 reader.consume();
 assertEquals('b', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume();
 reader.unconsume();
 assertEquals('b', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.consume();
 reader.advance();
 assertEquals('c', reader.current());
 }

 @Test
 public void testMark() {
 reader.consume();
 reader.consume();
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('c', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 reader.consume();
 reader.consume();
 String consumed = reader.consumeAsString();
 assertEquals("ab", consumed);
 }

 @Test
 public void testConsumeTo() {
 reader.consume();
 reader.consume();
 reader.consume();
 String consumed = reader.consumeTo('d');
 assertEquals("c", consumed.charAt(0));
 assertEquals("d", reader.current());
 }

 @Test
 public void testConsumeToEnd() {
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consumeToEnd();
 assertEquals("", reader.current());
 }

 @Test
 public void testConsumeToAny() {
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consumeToAny('a', 'b');
 assertEquals("c", reader.current());
 }

 @Test
 public void testConsumeLetterSequence() {
 reader.consumeLetterSequence();
 assertEquals("abcd", reader.toString());
 }

 @Test
 public void testConsumeHexSequence() {
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consumeHexSequence();
 assertEquals("ef", reader.toString());
 }

 @Test
 public void testConsumeDigitSequence() {
 reader.consume();
 reader.consume();
 reader.consumeDigitSequence();
 assertEquals("123", reader.toString());
 }

 @Test
 public void testMatches() {
 reader.consume();
 assertTrue(reader.matches('b'));
 assertFalse(reader.matches('a'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 reader.consume();
 assertTrue(reader.matchesIgnoreCase("B"));
 assertFalse(reader.matchesIgnoreCase("A"));
 }

 @Test
 public void testMatchesAny() {
 reader.consume();
 assertTrue(reader.matchesAny('b', 'c'));
 assertFalse(reader.matchesAny('a'));
 }

 @Test
 public void testMatchesLetter() {
 reader.consume();
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchesDigit() {
 reader.consume();
 reader.consume();
 assertTrue(reader.matchesDigit());
 assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchConsume() {
 reader.consume();
 reader.consume();
 String consumed = reader.matchConsume("ab");
 assertEquals("ab", consumed);
 assertEquals("", reader.toString());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 reader.consume();
 reader.consume();
 reader.consume();
 String consumed = reader.matchConsumeIgnoreCase("Ab");
 assertEquals("cd", consumed);
 assertEquals("", reader.toString());
 }

 @Test
 public void testContainsIgnoreCase() {
 reader.consumeLetterSequence();
 reader.consumeHexSequence();
 reader.consumeDigitSequence();
 assertTrue(reader.containsIgnoreCase("abcd ef123"));
 assertFalse(reader.containsIgnoreCase("ABCD EF123 XYZ"));
 }
}