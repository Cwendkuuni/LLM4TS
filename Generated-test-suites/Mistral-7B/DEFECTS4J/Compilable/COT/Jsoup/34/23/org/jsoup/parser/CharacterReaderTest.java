package org.jsoup.parser;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private static final String TEST_INPUT = "This is a test <html><head><title>Test Title</title></head><body>";

 @Test
 public void testCharacterReaderCreation() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 Assert.assertFalse("Input should not be null", reader.isEmpty());
 }

 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 reader.consume();
 reader.consume();
 Assert.assertEquals("Position should be 2", 2, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertTrue("Reader should be empty", reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertEquals("Current character should be a", 'a', reader.current());
 reader.consume();
 Assert.assertEquals("Current character should be b", 'b', reader.current());
 }

 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("abc");
 String consumed = reader.consumeTo('b');
 Assert.assertEquals("Consumed should be ac", "ac", consumed);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 CharacterReader reader = new CharacterReader("abc");
 reader.consumeToEnd();
 reader.consume();
 }

 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader("abc");
 reader.consume();
 reader.unconsume();
 Assert.assertEquals("Position should be unchanged", 0, reader.pos());
 }

 @Test
 public void testAdvance() {
 CharacterReader reader = new CharacterReader("abc");
 reader.advance();
 Assert.assertEquals("Position should be 1", 1, reader.pos());
 }

 @Test
 public void testMark() {
 CharacterReader reader = new CharacterReader("abc");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals("Position should be unchanged", 0, reader.pos());
 }

 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertEquals("Consumed string should be a", "a", reader.consumeAsString());
 }

 @Test
 public void testNextIndexOf() {
 CharacterReader reader = new CharacterReader("abcdefg");
 Assert.assertEquals("Index of d is 3", 3, reader.nextIndexOf('d'));
 Assert.assertEquals("Index of z is -1", -1, reader.nextIndexOf('z'));
 }

 @Test
 public void testNextIndexOfSequence() {
 CharacterReader reader = new CharacterReader("abcdefghij");
 String sequence = "def";
 Assert.assertEquals("Index of sequence def is 3", 3, reader.nextIndexOf(sequence));
 }

 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader("abcdefghij");
 String target = "def";
 String consumed = reader.consumeTo(target);
 Assert.assertEquals("Consumed should be def", "def", consumed);
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("1234567890");
 char[] targets = {'1', '5', '9'};
 String consumed = reader.consumeToAny(targets);
 Assert.assertEquals("Consumed should be 15 characters", 15, consumed.length());
 Assert.assertTrue("Consumed should contain all targets", containsAll(consumed.toCharArray(), targets));
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcdefghij");
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("Consumed should be whole input", "abcdefghij", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abc123def");
 String expected = "abc";
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("Consumed should be letter sequence", expected, consumed);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abc123def");
 String expected = "abc123";
 String consumed = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("Consumed should be letter then digit sequence", expected, consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("1234abcdE5F6");
 String expected = "1234abcdE5F6";
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("Consumed should be hex sequence", expected, consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 String expected = "1234567890";
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals("Consumed should be digit sequence", expected, consumed);
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("abcdefghij");
 Assert.assertTrue("Matches a", reader.matches('a'));
 Assert.assertTrue("Matches h", reader.matches('h'));
 Assert.assertFalse("Does not match z", reader.matches('z'));
 }

 @Test
 public void testMatchesSequence() {
 CharacterReader reader = new CharacterReader("abcdefghij");
 String sequence = "def";
 Assert.assertTrue("Matches sequence def", reader.matches(sequence));
 Assert.assertFalse("Does not match sequence ghi", reader.matches("ghi"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcdefghij");
 String sequence = "Def";
 Assert.assertTrue("Matches sequence Def ignored case", reader.matchesIgnoreCase(sequence));
 Assert.assertFalse("Does not match sequence Ghi ignored case", reader.matchesIgnoreCase("Ghi"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("abc123def");
 char[] targets = {'1', 'a', 'd'};
 Assert.assertTrue("Matches any target 1", reader.matchesAny(targets[0]));
 Assert.assertTrue("Matches any target a", reader.matchesAny(targets[1]));
 Assert.assertTrue("Matches any target d", reader.matchesAny(targets[2]));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdefghij");
 Assert.assertTrue("Is a letter", reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("1234567890");
 Assert.assertTrue("Is a digit", reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader("abcdefghij");
 String sequence = "def";
 Assert.assertTrue("Matches sequence def and consumes", reader.matchConsume(sequence));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcDEFGhIj");
 String sequence = "DEF";
 Assert.assertTrue("Matches sequence DEF ignored case and consumes", reader.matchConsumeIgnoreCase(sequence));
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader(" <html><head><title>Test Title</title></head><body>");
 String sequence = "</title>";
 Assert.assertTrue("Contains sequence </title> ignored case", reader.containsIgnoreCase(sequence));
 }

 private boolean containsAll(char[] arr1, char[] arr2) {
 for (char a : arr2) {
 if (!contains(arr1, a)) {
 return false;
 }
 }
 return true;
 }

 private boolean contains(char[] arr, char c) {
 for (char a : arr) {
 if (a == c) {
 return true;
 }
 }
 return false;
 }

}