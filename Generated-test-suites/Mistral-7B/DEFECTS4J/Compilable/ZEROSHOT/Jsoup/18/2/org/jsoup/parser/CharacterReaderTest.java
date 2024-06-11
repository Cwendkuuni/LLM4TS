package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setup() {
 char[] testInput = {'t', 'e', 's', 't', '\u0000'}; // null character for EOF test
 this.reader = new CharacterReader(new String(testInput));
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 assertEquals('t', reader.current());
 reader.advance();
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('t', reader.consume());
 assertEquals('e', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.unconsume();
 assertEquals('t', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('e', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals('t', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("st", reader.consumeAsString());
 assertEquals("", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("es", reader.consumeTo('s'));
 assertEquals("", reader.consumeTo('x'));
 }

 @Test
 public void testConsumeToString() {
 assertEquals("es", reader.consumeTo("s"));
 assertEquals("", reader.consumeTo("x"));
 }

 @Test
 public void testConsumeToAny() {
 char[] seq = {'s', 's'};
 assertEquals("es", reader.consumeToAny(seq));
 assertEquals("", reader.consumeToAny(new char[]{'x'}));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("test\u0000", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("est", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 char[] hexSeq = {'4', '1', '7', '0'};
 assertEquals("4170", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("123", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('t'));
 assertFalse(reader.matches('x'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches("st"));
 assertFalse(reader.matches("tx"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("st"));
 assertFalse(reader.matchesIgnoreCase("tx"));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = {'s', 's'};
 assertTrue(reader.matchesAny(seq));
 assertFalse(reader.matchesAny(new char[]{'x'}));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(reader.matchesLetter());
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 assertEquals("es", reader.matchConsume("s"));
 assertFalse(reader.matchConsume("x"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("es", reader.matchConsumeIgnoreCase("s"));
 assertFalse(reader.matchConsumeIgnoreCase("x"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("es"));
 assertFalse(reader.containsIgnoreCase("tx"));
 }
}