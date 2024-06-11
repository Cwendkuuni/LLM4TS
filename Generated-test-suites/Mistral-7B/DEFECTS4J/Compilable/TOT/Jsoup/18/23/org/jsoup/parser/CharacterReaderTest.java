package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 // Test for isEmpty method
 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertTrue(reader.isEmpty());

 CharacterReader reader1 = new CharacterReader("abc");
 Assert.assertFalse(reader1.isEmpty());
 }

 // Test for pos and current methods with valid input
 @Test
 public void testPosAndCurrent() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertEquals(0, reader.pos());
 Assert.assertEquals('a', reader.current());

 reader.consume();
 Assert.assertEquals(1, reader.pos());
 Assert.assertEquals('b', reader.current());
 }

 // Test for consume method with valid input
 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("abcdef");

 reader.consume();
 Assert.assertEquals('b', reader.current());
 Assert.assertEquals(1, reader.pos());
 }

 // Test for unconsume method with valid input
 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader("abcdef");

 reader.consume();
 reader.unconsume();
 Assert.assertEquals('a', reader.current());
 Assert.assertEquals(0, reader.pos());
 }

 // Test for mark and rewindToMark methods with valid input
 @Test
 public void testMarkAndRewindToMark() {
 CharacterReader reader = new CharacterReader("abcdef");

 reader.mark();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 Assert.assertEquals(0, reader.pos());
 }

 // Test for consumeAsString method with valid input
 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertEquals("abc", reader.consumeAsString());
 Assert.assertEquals(3, reader.pos());
 }

 // Test for consumeTo method with valid input
 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader("abcdefabc");

 Assert.assertEquals("def", reader.consumeTo('f'));
 Assert.assertEquals(6, reader.pos());
 }

 // Test for consumeTo method with invalid input
 @Test(expected = AssertionError.class)
 public void testConsumeToInvalidInput() {
 CharacterReader reader = new CharacterReader("abcdefabc");

 reader.consumeTo('z');
 }

 // Test for consumeToEnd method
 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertEquals("defabc", reader.consumeToEnd());
 Assert.assertEquals(6, reader.pos());
 }

 // Test for consumeLetterSequence method with valid input
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcdefghijk");

 Assert.assertEquals("abc", reader.consumeLetterSequence());
 Assert.assertEquals(3, reader.pos());
 }

 // Test for consumeHexSequence method with valid input
 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("0x1a2b3c");

 Assert.assertEquals("1a2b", reader.consumeHexSequence());
 Assert.assertEquals(3, reader.pos());
 }

 // Test for consumeDigitSequence method with valid input
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");

 Assert.assertEquals("123", reader.consumeDigitSequence());
 Assert.assertEquals(3, reader.pos());
 }

 // Test for matchConsume method with valid input
 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader("abcabc");

 Assert.assertTrue(reader.matchConsume("bc"));
 Assert.assertEquals(2, reader.pos());
 }

 // Test for matchConsumeIgnoreCase method with valid input
 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("ABCABC");

 Assert.assertTrue(reader.matchConsumeIgnoreCase("bc"));
 Assert.assertEquals(2, reader.pos());
 }

 // Test for containsIgnoreCase method with valid input
 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body>");

 Assert.assertTrue(reader.containsIgnoreCase("/title"));
 Assert.assertTrue(reader.containsIgnoreCase("/body"));
 }

 // Test for matches and matchesIgnoreCase methods with valid input
 @Test
 public void testMatchesAndMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcabc");

 Assert.assertTrue(reader.matches('b'));
 Assert.assertTrue(reader.matches("bc"));
 Assert.assertTrue(reader.matchesIgnoreCase("AbC"));
 }

 // Test for matchesAny method with valid input
 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("012345");

 Assert.assertTrue(reader.matchesAny('0', '1', '3', '4'));
 Assert.assertTrue(reader.matchesAny('0', '1', '2', '3', '4', '5'));
 }

 // Test for matchesLetter method with valid input
 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertTrue(reader.matchesLetter());
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesLetter());
 }

 // Test for matchesDigit method with valid input
 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("123456");

 Assert.assertTrue(reader.matchesDigit());
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit());
 }
}