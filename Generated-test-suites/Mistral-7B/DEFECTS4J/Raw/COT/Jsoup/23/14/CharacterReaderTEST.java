package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private String input;
 private CharacterReader reader;

 @Before
 public void setUp() {
 input = "This is a test string with various characters, numbers, and sequences.";
 reader = new CharacterReader(input);
 }

 // Test for basic functionality of public methods

 @Test
 public void testPos() {
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('T', reader.current());
 reader.consume();
 Assert.assertEquals('h', reader.current());
 }

 @Test
 public void testConsume() {
 Assert.assertEquals('T', reader.consume());
 Assert.assertEquals('h', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('T', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 Assert.assertEquals('h', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 Assert.assertEquals(reader.pos(), reader.mark());
 }

 @Test
 public void testRewindToMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals('T', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 String consumed = reader.consumeAsString();
 Assert.assertEquals("is a test string with various characters, numbers, and sequences.", consumed);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndWithEmptyInput() {
 String consumed = reader.consumeToEnd();
 }

 @Test
 public void testConsumeTo() {
 String consumed = reader.consumeTo('s');
 Assert.assertEquals("is a test string with various characters, numbers, and sequences.", input.substring(0, reader.pos()));
 Assert.assertEquals("a test string with various characters, numbers, and sequences.", consumed);
 }

 @Test
 public void testConsumeToEnd() {
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("string with various characters, numbers, and sequences.", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 String sequence = reader.consumeLetterSequence();
 Assert.assertEquals("is", sequence);
 }

 @Test
 public void testConsumeHexSequence() {
 String sequence = reader.consumeHexSequence();
 Assert.assertEquals("is", sequence); // Assuming that the consumed hex sequence is represented as a string of hexadecimal values in the test environment
 }

 @Test
 public void testConsumeDigitSequence() {
 String sequence = reader.consumeDigitSequence();
 Assert.assertEquals("1", sequence);
 }

 // Test for edge cases and exception handling scenarios

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumePastEndOfInput() {
 reader.consumeToEnd();
 reader.consume();
 String consumed = reader.consumeToEnd();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToNegativeIndex() {
 reader.consumeTo(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToNull() {
 reader.consumeTo(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToEmptyString() {
 reader.consumeTo("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToSequenceLongerThanInput() {
 reader.consumeTo("longerthaninput");
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(reader.matches('i'));
 Assert.assertFalse(reader.matches('.'));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(reader.matches("is"));
 Assert.assertFalse(reader.matches("and"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(reader.matchesIgnoreCase("is"));
 Assert.assertTrue(reader.matchesIgnoreCase("Is"));
 Assert.assertFalse(reader.matchesIgnoreCase("and"));
 }

 @Test
 public void testMatchesAny() {
 Assert.assertTrue(reader.matchesAny('i', 's'));
 Assert.assertTrue(reader.matchesAny('i', 'I'));
 Assert.assertFalse(reader.matchesAny('a', 's'));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesLetter('4'));
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit('a'));
 }

 @Test
 public void testMatchConsume() {
 String sequence = reader.matchConsume("is");
 Assert.assertEquals("is", sequence);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String sequence = reader.matchConsumeIgnoreCase("IS");
 Assert.assertEquals("is", sequence);
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(reader.containsIgnoreCase("test"));
 Assert.assertFalse(reader.containsIgnoreCase("AND"));
 }

 @Test
 public void testToString() {
 Assert.assertEquals("string with various characters, numbers, and sequences.", reader.toString());
 }

}