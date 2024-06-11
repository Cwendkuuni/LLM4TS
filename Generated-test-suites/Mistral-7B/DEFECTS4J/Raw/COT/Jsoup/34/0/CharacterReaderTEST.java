package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setup() {
 char[] input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
 reader = new CharacterReader(new String(input));
 }

 @Test
 public void testConstructor() {
 assertNotNull(reader);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 // test empty case
 assertEquals(CharacterReader.EOF, reader.current());
 reader.consume();
 // test normal case
 assertEquals('a', reader.current());
 reader.consume();
 // test end case
 assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testConsume() {
 // test empty case
 assertEquals(CharacterReader.EOF, reader.consume());
 // test normal case
 assertEquals('a', reader.current());
 reader.consume();
 assertEquals('b', reader.current());
 // test end case
 assertEquals(CharacterReader.EOF, reader.current());
 reader.consume();
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
 reader.advance();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 reader.consume(2);
 assertEquals("ab", reader.consumeAsString());
 }

 @Test
 public void testNextIndexOf() {
 // test char
 assertEquals(1, reader.nextIndexOf('a'));
 assertEquals(-1, reader.nextIndexOf('z'));
 // test string
 assertEquals(10, reader.nextIndexOf("j"));
 assertEquals(-1, reader.nextIndexOf("zx"));
 }

 @Test
 public void testConsumeTo() {
 String expected = "abcd";
 assertEquals(expected, reader.consumeTo('e'));
 }

 @Test(expected = AssertionError.class)
 public void testConsumeToNegative() {
 reader.consumeTo(-1);
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("abcdefghijklmnopqrstuvwxyz", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 assertEquals("abc123def456", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("abcdef0123456789ABCDEF", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("0123456789", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('a'));
 assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches("ab"));
 assertFalse(reader.matches("cd"));
 }

 @Test
 public void testMatchesIgnoreCaseString() {
 assertTrue(reader.matchesIgnoreCase("Ab"));
 assertFalse(reader.matchesIgnoreCase("cd"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny('a', 'b'));
 assertFalse(reader.matchesAny('z', 'b'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('0'));
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(reader.matchesDigit());
 assertFalse(reader.matchesDigit('a'));
 }

 @Test
 public void testMatchConsume() {
 String expected = "defghijklmnopqrstuvwxyz";
 reader.matchConsume("ab");
 assertEquals(expected, reader.toString());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String expected = "DEFGHIJKLMNOPQRSTUVWXYZ";
 reader.matchConsumeIgnoreCase("Ab");
 assertEquals(expected, reader.toString());
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("abcd"));
 assertFalse(reader.containsIgnoreCase("xyz"));
 }

 @Test
 public void testToString() {
 assertEquals("efghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", reader.toString());
 }
}