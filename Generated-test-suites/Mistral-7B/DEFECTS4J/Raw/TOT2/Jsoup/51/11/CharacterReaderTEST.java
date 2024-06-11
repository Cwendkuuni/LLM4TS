package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 // Test for the constructor
 @Test
 public void testCharacterReaderConstructor() {
 CharacterReader reader = new CharacterReader("Test String");
 Assert.assertNotNull(reader);
 }

 // Test for the pos() method
 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader("Test String");
 reader.consume(10);
 Assert.assertEquals(10, reader.pos());
 }

 // Test for the isEmpty() method
 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("Test String");
 Assert.assertFalse(reader.isEmpty());
 reader.consumeToEnd();
 Assert.assertTrue(reader.isEmpty());
 }

 // Test for the current() and consume() methods
 @Test
 public void testCurrentAndConsume() {
 CharacterReader reader = new CharacterReader("Test String");
 Assert.assertEquals('T', reader.current());
 Assert.assertEquals('T', reader.consume());
 }

 // Test for the unconsume() method
 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader("Test String");
 reader.consume(1);
 reader.unconsume();
 Assert.assertEquals('T', reader.current());
 }

 // Test for the mark() and rewindToMark() methods
 @Test
 public void testMarkAndRewindToMark() {
 CharacterReader reader = new CharacterReader("Test String");
 reader.mark();
 reader.consume(1);
 reader.rewindToMark();
 Assert.assertEquals('T', reader.current());
 }

 // Test for the consumeAsString() method
 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("Test String");
 String str = reader.consumeAsString();
 Assert.assertEquals("String", str);
 }

 // Test for the nextIndexOf() method with a single character
 @Test
 public void testNextIndexOfSingleChar() {
 CharacterReader reader = new CharacterReader("TestString");
 Assert.assertEquals(4, reader.nextIndexOf('T'));
 }

 // Test for the nextIndexOf() method with a multi-character sequence
 @Test
 public void testNextIndexOfMultiCharSeq() {
 CharacterReader reader = new CharacterReader("TestStringTest");
 Assert.assertEquals(6, reader.nextIndexOf("Test"));
 }

 // Test for the consumeTo() method with a single character
 @Test
 public void testConsumeToSingleChar() {
 CharacterReader reader = new CharacterReader("TestString");
 String str = reader.consumeTo('T');
 Assert.assertEquals("String", reader.toString());
 Assert.assertEquals("T", str);
 }

 // Test for the consumeTo() method with a multi-character sequence
 @Test
 public void testConsumeToMultiCharSeq() {
 CharacterReader reader = new CharacterReader("TestStringTest");
 String str = reader.consumeTo("Test");
 Assert.assertEquals("StringTest", reader.toString());
 Assert.assertEquals("Test", str);
 }

 // Test for the consumeToAny() method
 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("TestABC");
 String str = reader.consumeToAny('A', 'B');
 Assert.assertEquals("TestBC", reader.toString());
 Assert.assertEquals("A", str);
 }

 // Test for the consumeToAnySorted() method
 @Test
 public void testConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader("TestABC");
 String str = reader.consumeToAnySorted(new char[]{'B', 'C'});
 Assert.assertEquals("TestBC", reader.toString());
 Assert.assertEquals("B", str);
 }

 // Test for the consumeData() method
 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("&< Test");
 String str = reader.consumeData();
 Assert.assertEquals("&< ", str);
 }

 // Test for the consumeTagName() method
 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("<html>");
 String str = reader.consumeTagName();
 Assert.assertEquals("html", str);
 }

 // Test for the consumeToEnd() method
 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("TestString");
 String str = reader.consumeToEnd();
 Assert.assertEquals("", reader.toString());
 Assert.assertEquals("TestString", str);
 }

 // Test for the consumeLetterSequence() method
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("TestLetterSequence");
 String str = reader.consumeLetterSequence();
 Assert.assertEquals("LetterSequence", reader.toString());
 Assert.assertEquals("LetterSequence", str);
 }

 // Test for the consumeLetterThenDigitSequence() method
 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("TestLetterThenDigitSequence123");
 String str = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("LetterThenDigitSequence", reader.toString());
 Assert.assertEquals("LetterThenDigitSequence", str);
 }

 // Test for the consumeHexSequence() method
 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("TestHexSequence&lt;#&x61;x64;x64;x72;x65;x73;x73;x3E;");
 String str = reader.consumeHexSequence();
 Assert.assertEquals("TestHexSequence<#adversary>", reader.toString());
 Assert.assertEquals("#adversary", str);
 }

 // Test for the consumeDigitSequence() method
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("TestDigitSequence123456");
 String str = reader.consumeDigitSequence();
 Assert.assertEquals("TestDigitSequence", reader.toString());
 Assert.assertEquals("123456", str);
 }

 // Test for the matches() method with a single character
 @Test
 public void testMatchesSingleChar() {
 CharacterReader reader = new CharacterReader("TestString");
 Assert.assertTrue(reader.matches('T'));
 }

 // Test for the matches() method with a multi-character sequence
 @Test
 public void testMatchesMultiCharSeq() {
 CharacterReader reader = new CharacterReader("TestTestString");
 Assert.assertTrue(reader.matches("Test"));
 }

 // Test for the matchesIgnoreCase() method with a single character
 @Test
 public void testMatchesIgnoreCaseSingleChar() {
 CharacterReader reader = new CharacterReader("TestString");
 Assert.assertTrue(reader.matchesIgnoreCase("t"));
 }

 // Test for the matchesIgnoreCase() method with a multi-character sequence
 @Test
 public void testMatchesIgnoreCaseMultiCharSeq() {
 CharacterReader reader = new CharacterReader("TestTestString");
 Assert.assertTrue(reader.matchesIgnoreCase("Test"));
 }

 // Test for the matchesAny() method with a single character
 @Test
 public void testMatchesAnySingleChar() {
 CharacterReader reader = new CharacterReader("TestStringA");
 Assert.assertTrue(reader.matchesAny('A'));
 }

 // Test for the matchesAny() method with multiple characters
 @Test
 public void testMatchesAnyMultiChars() {
 CharacterReader reader = new CharacterReader("TestStringAB");
 Assert.assertTrue(reader.matchesAny('A', 'B'));
 }

 // Test for the matchesLetter() method
 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("TestString");
 Assert.assertTrue(reader.matchesLetter());
 }

 // Test for the matchesDigit() method
 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("TestString123");
 Assert.assertTrue(reader.matchesDigit());
 }

 // Test for the matchConsume() method with a single character
 @Test
 public void testMatchConsumeSingleChar() {
 CharacterReader reader = new CharacterReader("TestStringT");
 String str = reader.matchConsume("T");
 Assert.assertEquals("String", reader.toString());
 Assert.assertEquals("T", str);
 }

 // Test for the matchConsume() method with a multi-character sequence
 @Test
 public void testMatchConsumeMultiCharSeq() {
 CharacterReader reader = new CharacterReader("TestTestStringTest");
 String str = reader.matchConsume("Test");
 Assert.assertEquals("StringTest", reader.toString());
 Assert.assertEquals("Test", str);
 }

 // Test for the containsIgnoreCase() method
 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title><!-- Test --></title></head>");
 Assert.assertTrue(reader.containsIgnoreCase("/title"));
 Assert.assertTrue(reader.containsIgnoreCase("Title"));
 }

 // Test for the toString() method
 @Test
 public void testToString() {
 CharacterReader reader = new CharacterReader("TestString");
 Assert.assertEquals("TestString", reader.toString());
 }

 // Test for the cacheString() method
 @Test
 public void testCacheString() {
 CharacterReader reader = new CharacterReader("TestStringTestStringTestString");
 String str1 = reader.cacheString(0, 10);
 String str2 = reader.cacheString(11, 10);
 String str3 = reader.cacheString(21, 10);
 Assert.assertEquals("TestString", reader.toString());
 Assert.assertEquals("TestString", str1);
 Assert.assertEquals("StringTest", str2);
 Assert.assertEquals("StringString", str3);
 }
}