import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] testInput = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
 String testInputString = new String(testInput);
 reader = new CharacterReader(testInputString);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 reader.consume();
 assertEquals(1, reader.pos());
 }

 @Test
 public void testCurrent() {
 assertEquals('a', reader.current());
 reader.consume();
 assertEquals('b', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('a', reader.consume());
 assertEquals('b', reader.current());
 reader.unconsume();
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
 reader.advance();
 assertEquals('b', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("", reader.consumeAsString());
 reader.consume('a');
 reader.consume('b');
 assertEquals("ab", reader.consumeAsString());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndWithEmptyString() {
 reader.consumeToEnd();
 }

 @Test
 public void testConsumeTo() {
 assertEquals("", reader.consumeTo((char) 0));
 reader.consume('a');
 reader.consume('b');
 reader.consumeTo('c');
 assertEquals("bc", reader.toString());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToWithInvalidChar() {
 reader.consumeTo((char) 0);
 }

 @Test
 public void testConsumeToSequence() {
 reader.consume('a');
 reader.consume('b');
 reader.consumeTo("cd");
 assertEquals("abcd", reader.toString());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToSequenceWithInvalidSequence() {
 reader.consumeTo("cd");
 }

 @Test
 public void testConsumeToAny() {
 reader.consume('a');
 reader.consume('b');
 reader.consumeToAny('c', 'd');
 assertEquals("abcd", reader.toString());
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("", reader.consumeToEnd());
 reader.consume('a');
 reader.consume('b');
 reader.consumeToEnd();
 assertEquals("", reader.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToEndWithInvalidArgument() {
 reader.consumeToEnd((String) null);
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('a'));
 assertFalse(reader.matches('b'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches("ab"));
 assertFalse(reader.matches("cd"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("Ab"));
 assertFalse(reader.matchesIgnoreCase("Cd"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny('a', 'b'));
 assertFalse(reader.matchesAny('c', 'd'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('0'));
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(reader.matchesDigit());
 assertTrue(reader.matchesDigit('1'));
 }

 @Test
 public void testMatchConsume() {
 reader.consume('a');
 assertTrue(reader.matchConsume("ab"));
 assertEquals("bc", reader.toString());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 reader.consume('A');
 assertTrue(reader.matchConsumeIgnoreCase("Ab"));
 assertEquals("bc", reader.toString());
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("ab"));
 assertFalse(reader.containsIgnoreCase("CD"));
 }

 @Test(expected = NullPointerException.class)
 public void testContainsIgnoreCaseWithNullSequence() {
 reader.containsIgnoreCase(null);
 }
}