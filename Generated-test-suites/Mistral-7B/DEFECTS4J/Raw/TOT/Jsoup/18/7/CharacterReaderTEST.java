package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testEmptyCharacterReader() {
 CharacterReader reader = new CharacterReader("");

 Assert.assertTrue(reader.isEmpty());
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testBasicConsumption() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertEquals('a', reader.current());
 Assert.assertEquals('a', reader.consume());

 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testBasicConsumptionEndOfString() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertEquals('f', reader.consume());
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testMark() {
 CharacterReader reader = new CharacterReader("ababc");

 reader.mark();
 reader.consume();
 Assert.assertEquals('a', reader.current());
 Assert.assertEquals('a', reader.consume());
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testNegativePosition() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.pos = -1;
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 CharacterReader reader = new CharacterReader(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyInput() {
 CharacterReader reader = new CharacterReader("");
 }

 @Test
 public void testConsumeSequence() {
 CharacterReader reader = new CharacterReader("abcdabcd");

 Assert.assertEquals("ab", reader.consumeTo('b'));
 Assert.assertEquals("cd", reader.consumeTo('d'));
 Assert.assertEquals("abcd", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("ABCabc123DEF");

 Assert.assertEquals("ABC", reader.consumeLetterSequence());
 Assert.assertEquals("abc", reader.consumeLetterSequence());
 Assert.assertEquals("DEF", reader.consumeLetterSequence());
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("abcdabcd");

 Assert.assertTrue(reader.matches('a'));
 Assert.assertTrue(reader.matches("ab"));
 Assert.assertFalse(reader.matches('e'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcdABCD");

 Assert.assertTrue(reader.matchesIgnoreCase("ab"));
 Assert.assertTrue(reader.matchesIgnoreCase("CD"));
 Assert.assertFalse(reader.matchesIgnoreCase("E"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("012301ABCdef0123");

 Assert.assertTrue(reader.matchesAny('0', '3'));
 Assert.assertFalse(reader.matchesAny('G'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdABCD");

 Assert.assertTrue(reader.matchesLetter());
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesLetter('5'));
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("012301ABCdef0123");

 Assert.assertTrue(reader.matchesDigit());
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit('F'));
 }

 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader("abcdabcd");

 Assert.assertTrue(reader.matchConsume("ab"));
 Assert.assertEquals('c', reader.current());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("abABcdabABcd");

 Assert.assertTrue(reader.matchConsumeIgnoreCase("ab"));
 Assert.assertEquals('A', reader.current());
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("abABcdabABcd<title></title>");

 Assert.assertTrue(reader.containsIgnoreCase("title"));
 Assert.assertFalse(reader.containsIgnoreCase("ABC"));
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("0x1a2b3c4d0x1e2f3g");

 Assert.assertEquals("1a2b", reader.consumeHexSequence());
 Assert.assertEquals("3c4d", reader.consumeHexSequence());
 Assert.assertEquals("g", reader.current());
 }
}