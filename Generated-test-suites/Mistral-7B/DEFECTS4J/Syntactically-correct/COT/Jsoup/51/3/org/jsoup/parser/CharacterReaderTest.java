package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;

import java.util.Arrays;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setup() {
 char[] input = "abcdefgABCDEFG12345!@#$%&*<>.;:,/?".toCharArray();
 characterReader = new CharacterReader(new String(input));
 }

 @Test
 public void testConstructor() {
 Validate.notNull(characterReader.input);
 assertEquals(characterReader.length, input.length);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(characterReader.isEmpty());
 characterReader.consume();
 assertFalse(characterReader.isEmpty());
 }

 @Test
 public void testCurrent() {
 assertEquals(characterReader.current(), 'a');
 characterReader.advance();
 assertEquals(characterReader.current(), 'b');
 }

 @Test
 public void testConsume() {
 assertEquals(characterReader.consume(), 'a');
 assertEquals(characterReader.current(), 'b');
 characterReader.unconsume();
 assertEquals(characterReader.current(), 'b');
 }

 @Test
 public void testUnconsume() {
 characterReader.advance();
 characterReader.unconsume();
 assertEquals(characterReader.current(), 'a');
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 assertEquals(characterReader.current(), 'b');
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.consume();
 characterReader.rewindToMark();
 assertEquals(characterReader.current(), 'a');
 }

 @Test
 public void testConsumeAsString() {
 assertEquals(characterReader.consumeAsString(), "a");
 }

 @Test
 public void testNextIndexOf() {
 assertEquals(characterReader.nextIndexOf('b'), 1);
 assertEquals(characterReader.nextIndexOf('z'), -1);
 }

 @Test
 public void testNextIndexOfString() {
 assertEquals(characterReader.nextIndexOf("bc"), 1);
 assertEquals(characterReader.nextIndexOf("xyz"), -1);
 }

 @Test
 public void testConsumeTo() {
 assertEquals(characterReader.consumeTo('b').toString(), "a");
 assertEquals(characterReader.consumeTo('b').toString(), "ab");
 assertEquals(characterReader.consumeTo('e').toString(), "abcd");
 }

 @Test
 public void testConsumeToString() {
 assertEquals(characterReader.consumeToString("bc").toString(), "ab");
 assertEquals(characterReader.consumeToString("xyz").toString(), "");
 }

 @Test
 public void testConsumeToAny() {
 assertEquals(characterReader.consumeToAny(new char[]{'b'}).toString(), "a");
 assertEquals(characterReader.consumeToAny(new char[]{'e', 'f'}).toString(), "bcdefg");
 }

 @Test
 public void testConsumeToAnySorted() {
 assertEquals(characterReader.consumeToAnySorted(new char[]{'f', 'e'}).toString(), "bcdefg");
 assertEquals(characterReader.consumeToAnySorted(new char[]{'z'}).toString(), "");
 }

 @Test
 public void testConsumeData() {
 assertEquals(characterReader.consumeData().toString(), "abcdefg");
 }

 @Test
 public void testConsumeTagName() {
 assertEquals(characterReader.consumeTagName().toString(), "abcdefgABCDEFG");
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals(characterReader.consumeToEnd().toString(), "abcdefgABCDEFG12345!");
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals(characterReader.consumeLetterSequence().toString(), "abcdefgABCDEFG");
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 assertEquals(characterReader.consumeLetterThenDigitSequence().toString(), "abcdefg12345");
 }

 @Test
 public void testMatches() {
 assertTrue(characterReader.matches('b'));
 assertFalse(characterReader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(characterReader.matches("bc"));
 assertFalse(characterReader.matches("xyz"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(characterReader.matchesIgnoreCase("BC"));
 assertFalse(characterReader.matchesIgnoreCase("xyz"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(characterReader.matchesAny('b'));
 assertFalse(characterReader.matchesAny('z'));
 }

 @Test
 public void testMatchesAnySorted() {
 assertTrue(characterReader.matchesAnySorted(new char[]{'b'}));
 assertFalse(characterReader.matchesAnySorted(new char[]{'z'}));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 assertFalse(characterReader.matchesLetter('0'));
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(characterReader.matchesDigit());
 assertTrue(characterReader.matchesDigit('1'));
 }

 @Test
 public void testMatchConsume() {
 assertTrue(characterReader.matchConsume("bc"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertTrue(characterReader.matchConsumeIgnoreCase("BC"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(characterReader.containsIgnoreCase("</title>"));
 assertTrue(characterReader.containsIgnoreCase("</style>"));
 assertFalse(characterReader.containsIgnoreCase("</body>"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 characterReader = new CharacterReader(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBoundsException() {
 characterReader.consumeTo(-1);
 }
}