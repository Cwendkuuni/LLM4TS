import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setup() {
 reader = new CharacterReader("This is a test string for CharacterReader.");
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty()); // new instance is empty
 reader.consume();
 assertFalse(reader.isEmpty()); // after consuming a character, it's not empty
 reader.rewindToMark(); // reset to mark
 assertTrue(reader.isEmpty()); // at mark position, it's empty
 }

 @Test
 public void testCurrentAndConsume() {
 assertEquals('T', reader.current()); // first character
 reader.consume();
 assertEquals('h', reader.current()); // next character
 reader.consume();
 assertFalse(reader.matches('T')); // 'T' has been consumed
 }

 @Test
 public void testMarkAndRewind() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('T', reader.current()); // rewinds to previous position
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("is a test", reader.consumeAsString(6)); // consume 6 characters
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndWithNothing() {
 reader.consumeToEnd(); //consume all characters, no more characters to consume
 reader.consumeToEnd(); // attempt to consume again, should throw an exception
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("is a test", reader.consumeLetterSequence());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeDigitSequenceWithNothing() {
 reader.consumeDigitSequence(); //consume all digits, no more digits to consume
 reader.consumeDigitSequence(); // attempt to consume again, should throw an exception
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('t')); // matches first 't'
 assertFalse(reader.matches('a')); // does not match 'a'
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("is")); // matches "is" case insensitively
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny(new char[]{'e', 's'})); // matches either 'e' or 's'
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("test")); // checks presence of "test" case insensitively
 }
}