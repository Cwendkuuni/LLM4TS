import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setup() {
 char[] input = {'t', 'e', 'st', ' ', 'i', 's', ' ', 'a', ' ', 'c', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'r', 'e', 'a', 'd', 'e', 'r', '.', ' '};
 this.reader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.advance();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 reader.consume();
 reader.consume();
 assertEquals(2, reader.pos());
 }

 @Test
 public void testCurrent() {
 assertEquals('t', reader.current());
 reader.consume();
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('t', reader.consume());
 reader.consume();
 assertEquals('e', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume();
 reader.unconsume();
 assertEquals('e', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 reader.advance();
 assertEquals('i', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('i', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 String s = reader.consumeAsString();
 assertEquals("is a", s);
 reader.consume();
 reader.consume();
 s = reader.consumeAsString();
 assertEquals("charter", s);
 }

 @Test(expected = AssertionError.class)
 public void testConsumeToNegative() {
 reader.consumeTo(-1);
 fail("Expected AssertionError");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEnd() {
 String s = reader.consumeToEnd();
 assertEquals("charter.", s);
 reader.consume();
 reader.consume();
 s = reader.consumeToEnd();
 assertNull(s);
 }

 @Test
 public void testConsumeLetterSequence() {
 String s = reader.consumeLetterSequence();
 assertEquals("is a", s);
 reader.consume();
 reader.consume();
 s = reader.consumeLetterSequence();
 assertEquals("charter", s);
 }

 @Test(expected = AssertionError.class)
 public void testConsumeHexSequenceEmpty() {
 reader.consumeHexSequence();
 fail("Expected AssertionError");
 }

 @Test(expected = AssertionError.class)
 public void testConsumeHexSequenceInvalid() {
 reader.advance();
 reader.consumeHexSequence();
 fail("Expected AssertionError");
 }

 @Test
 public void testConsumeHexSequence() {
 String s = reader.consumeHexSequence();
 assertEquals("is", s);
 reader.consume();
 reader.consume();
 reader.consume();
 s = reader.consumeHexSequence();
 assertEquals("acter", s);
 }

 @Test(expected = AssertionError.class)
 public void testConsumeDigitSequenceEmpty() {
 reader.consumeDigitSequence();
 fail("Expected AssertionError");
 }

 @Test
 public void testConsumeDigitSequenceValid() {
 String s = reader.consumeDigitSequence();
 assertEquals("is", s);
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 s = reader.consumeDigitSequence();
 assertEquals("acter", s);
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('t'));
 reader.consume();
 assertFalse(reader.matches('t'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches("is"));
 reader.consumeLetterSequence();
 assertFalse(reader.matches("is"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("is"));
 reader.consumeLetterSequence();
 assertFalse(reader.matchesIgnoreCase("is"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny('i', 's'));
 reader.consume();
 assertFalse(reader.matchesAny('i', 's'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 reader.consume();
 assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(reader.matchesDigit());
 reader.consume();
 assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 assertTrue(reader.matchConsume("is"));
 assertEquals(3, reader.pos());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertTrue(reader.matchConsumeIgnoreCase("is"));
 assertEquals(3, reader.pos());
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("is"));
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 assertTrue(reader.containsIgnoreCase("acter"));
 }
}