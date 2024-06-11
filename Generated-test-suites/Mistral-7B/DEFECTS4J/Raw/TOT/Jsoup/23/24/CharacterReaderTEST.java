import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 String input = "This is a test string for CharacterReader.";
 reader = new CharacterReader(input);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrentAndConsume() {
 assertEquals('T', reader.current());
 assertEquals('T', reader.consume());
 }

 @Test
 public void testConsumeAsString() {
 String expected = "is a test string for CharacterReader.";
 assertEquals(expected, reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 String expected = "test string for CharacterReader.";
 assertEquals(expected, reader.consumeTo(' '));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndWithMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 reader.consumeToEnd();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEnd() {
 reader.consumeToEnd();
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "is a test string for CharacterReader.";
 assertEquals(expected, reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "is a test string for CharacterReader.";
 reader.consume(0x20); // skip whitespace
 reader.consume(0x31); // 1
 reader.consume(0x32); // 2
 reader.consume(0x33); // 3
 String hex = reader.consumeHexSequence();
 assertEquals("123", hex);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeDigitSequenceWithMark() {
 reader.mark();
 reader.consume();
 reader.consume();
 reader.rewindToMark();
 reader.consumeDigitSequence();
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "1234";
 assertEquals(expected, reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertFalse(reader.matches('x'));
 assertTrue(reader.matches('T'));
 reader.consume();
 assertFalse(reader.matches('T'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("is"));
 }

 @Test
 public void testMatchesAny() {
 String seq = "is a t";
 assertTrue(reader.matchesAny(seq.toCharArray()));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String seq = "is a";
 reader.consume(0x20); // skip whitespace
 reader.consume('i');
 reader.consume('s');
 reader.consume(' ');
 assertTrue(reader.matchConsume(seq));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String seq = "is a";
 reader.consume(0x20); // skip whitespace
 reader.consume('i');
 reader.consume('s');
 reader.consume(' ');
 reader.consume('A');
 reader.consume(' ');
 assertTrue(reader.matchConsumeIgnoreCase(seq));
 }

 @Test
 public void testContainsIgnoreCase() {
 String seq = "is a test";
 assertTrue(reader.containsIgnoreCase(seq));
 }

 @Test
 public void testToString() {
 String expected = "string for CharacterReader.";
 assertEquals(expected, reader.toString());
 }
}