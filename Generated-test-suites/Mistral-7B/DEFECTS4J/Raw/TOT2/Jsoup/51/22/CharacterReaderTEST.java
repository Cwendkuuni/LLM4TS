package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 // Test for isEmpty method
 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 reader = new CharacterReader("Test");
 assertFalse(reader.isEmpty());
 }

 // Test for pos method
 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader("Test");
 assertEquals(0, reader.pos());
 reader.consume();
 reader.consume();
 reader.advance();
 reader.mark();
 reader.rewindToMark();
 assertEquals(0, reader.pos());
 reader.advance();
 assertEquals(1, reader.pos());
 }

 // Test for current and consume methods
 @Test
 public void testCurrentAndConsume() {
 CharacterReader reader = new CharacterReader("Test");
 assertEquals('T', reader.current());
 reader.consume();
 assertEquals('e', reader.current());
 reader.consume();
 reader.consume();
 assertEquals('s', reader.current());
 reader.consume();
 assertFalse(reader.matches('t'));
 assertEquals(EOF, reader.current());
 }

 // Test for unconsume method
 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader("Test");
 reader.consume();
 reader.consume();
 reader.unconsume();
 assertEquals('e', reader.current());
 reader.advance();
 reader.rewindToMark();
 reader.consume();
 assertEquals('t', reader.current());
 }

 // Test for mark and rewindToMark methods
 @Test
 public void testMarkAndRewindToMark() {
 CharacterReader reader = new CharacterReader("Test");
 reader.mark();
 reader.consume();
 reader.consume();
 reader.rewindToMark();
 assertEquals('T', reader.current());
 reader.consume();
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('e', reader.current());
 }

 // Test for consumeAsString method
 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("Test");
 assertEquals("Test", reader.consumeToEnd());
 reader.mark();
 reader.advance();
 assertEquals("e", reader.consumeAsString());
 reader.rewindToMark();
 }

 // Test for nextIndexOf method with a single character
 @Test
 public void testNextIndexOfSingleChar() {
 CharacterReader reader = new CharacterReader("Test");
 assertEquals(1, reader.nextIndexOf('e'));
 reader = new CharacterReader("Tester");
 assertEquals(1, reader.nextIndexOf('e'));
 reader = new CharacterReader("Testest");
 assertEquals(2, reader.nextIndexOf('e'));
 reader = new CharacterReader("");
 assertEquals(-1, reader.nextIndexOf('e'));
 }

 // Test for nextIndexOf method with a sequence of characters
 @Test
 public void testNextIndexOfSequence() {
 CharacterReader reader = new CharacterReader("TestTest");
 assertEquals(2, reader.nextIndexOf("est"));
 reader = new CharacterReader("TestTestTest");
 assertEquals(3, reader.nextIndexOf("est"));
 reader = new CharacterReader("TestTestTestTest");
 assertEquals(4, reader.nextIndexOf("est"));
 reader = new CharacterReader("");
 assertEquals(-1, reader.nextIndexOf("est"));
 }

 // Test for consumeTo method with a single character
 @Test
 public void testConsumeToSingleChar() {
 CharacterReader reader = new CharacterReader("TestTest");
 assertEquals("est", reader.consumeTo('e'));
 reader = new CharacterReader("TestTestTest");
 assertEquals("estTest", reader.consumeTo('e'));
 reader = new CharacterReader("TestTestTestTest");
 assertEquals("estTestTest", reader.consumeTo('e'));
 reader = new CharacterReader("");
 assertEquals("", reader.consumeTo('e'));
 }

 // Test for consumeTo method with a sequence of characters
 @Test
 public void testConsumeToSequence() {
 CharacterReader reader = new CharacterReader("TestTestTestTest");
 assertEquals("estTestTest", reader.consumeTo("est"));
 reader = new CharacterReader("TestTestTestTestTest");
 assertEquals("estTestTestTest", reader.consumeTo("est"));
 reader = new CharacterReader("TestTestTestTestTestTest");
 assertEquals("estTestTestTestTest", reader.consumeTo("est"));
 reader = new CharacterReader("");
 assertEquals("", reader.consumeTo("est"));
 }

 // Test for consumeToAny method with a single character
 @Test
 public void testConsumeToAnySingleChar() {
 CharacterReader reader = new CharacterReader("123abc");
 assertEquals("1", reader.consumeToAny('1'));
 reader = new CharacterReader("123abc");
 assertEquals("123", reader.consumeToAny('1', '2', '3'));
 reader = new CharacterReader("123abc");
 assertEquals("123", reader.consumeToAnySorted('1', '3', '2'));
 reader = new CharacterReader("abc123");
 assertEquals("", reader.consumeToAny('1'));
 reader = new CharacterReader("abc123");
 assertEquals("", reader.consumeToAnySorted('3', '1'));
 reader = new CharacterReader("");
 assertEquals("", reader.consumeToAny('1'));
 }

 // Test for consumeToAny method with a sequence of characters
 @Test
 public void testConsumeToAnySequence() {
 CharacterReader reader = new CharacterReader("123abc123");
 assertEquals("123", reader.consumeToAny('1', '2', '3'));
 reader = new CharacterReader("123abc123");
 assertEquals("123abc", reader.consumeToAnySorted('1', '2', '3', 'a', 'b', 'c'));
 reader = new CharacterReader("abc123abc123");
 assertEquals("abc", reader.consumeToAny('a', 'b', 'c'));
 reader = new CharacterReader("abc123abc123");
 assertEquals("abc123", reader.consumeToAnySorted('a', 'b', 'c', '1', '2'));
 reader = new CharacterReader("");
 assertEquals("", reader.consumeToAny('1'));
 reader = new CharacterReader("");
 assertEquals("", reader.consumeToAnySorted('1', '2'));
 }

 // Test for consumeData method
 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("&<123");
 assertEquals("&<123", reader.consumeData());
 reader = new CharacterReader("&<12345");
 assertEquals("&<123", reader.consumeData());
 reader = new CharacterReader("&<abc123");
 assertEquals("", reader.consumeData());
 reader = new CharacterReader("");
 assertEquals("", reader.consumeData());
 }

 // Test for consumeTagName method
 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("<html>");
 assertEquals("html", reader.consumeTagName());
 reader = new CharacterReader("<title>");
 assertEquals("title", reader.consumeTagName());
 reader = new CharacterReader("<style>");
 assertEquals("style", reader.consumeTagName());
 reader = new CharacterReader("<");
 assertEquals("", reader.consumeTagName());
 reader = new CharacterReader("");
 assertEquals("", reader.consumeTagName());
 }

 // Test for consumeToEnd method
 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("TestTestTestTest");
 assertEquals("TestTestTestTest", reader.consumeToEnd());
 reader = new CharacterReader("TestTestTestTestTest");
 assertEquals("TestTestTestTestTest", reader.consumeToEnd());
 reader = new CharacterReader("");
 assertEquals("", reader.consumeToEnd());
 }

 // Test for consumeLetterSequence method
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("TestTestTestTestTest");
 assertEquals("TestTestTestTest", reader.consumeLetterSequence());
 reader = new CharacterReader("TestTestTestTestTestTest");
 assertEquals("TestTestTestTestTest", reader.consumeLetterSequence());
 reader = new CharacterReader("TestTestTestTestTestTestTest");
 assertEquals("TestTestTestTestTest", reader.consumeLetterSequence());
 reader = new CharacterReader("");
 assertEquals("", reader.consumeLetterSequence());
 }

 // Test for consumeLetterThenDigitSequence method
 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("Test123Test456");
 assertEquals("Test123Test", reader.consumeLetterThenDigitSequence());
 reader = new CharacterReader("Test123Test456Test789");
 assertEquals("Test123Test456Test", reader.consumeLetterThenDigitSequence());
 reader = new CharacterReader("Test123Test456Test789Test1011");
 assertEquals("Test123Test456Test789Test1011", reader.consumeLetterThenDigitSequence());
 reader = new CharacterReader("");
 assertEquals("", reader.consumeLetterThenDigitSequence());
 }

 // Test for consumeHexSequence method
 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("Test#%&/1F2A3E4M5N6P7QR8B9C10D11E12F");
 assertEquals("Test#%&/1F2A3E4M5N6P7QR8B9C10D11E12F", reader.consumeHexSequence());
 reader = new CharacterReader("Test#%&/1F2A3E4M5N6P7QR8B9C10D11E12F#%&/1F2A3E4M5N6P7QR8B9C10D11E12F");
 assertEquals("Test#%&/1F2A3E4M5N6P7QR8B9C10D11E12F#%&/1F2A3E4M5N6P7QR8B9C10D11E12F", reader.consumeHexSequence());
 reader = new CharacterReader("");
 assertEquals("", reader.consumeHexSequence());
 }

 // Test for consumeDigitSequence method
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 assertEquals("1234567890", reader.consumeDigitSequence());
 reader = new CharacterReader("12345678901234567890");
 assertEquals("12345678901234567890", reader.consumeDigitSequence());
 reader = new CharacterReader("12345678901234567890123456");
 assertEquals("12345678901234567890123456", reader.consumeDigitSequence());
 reader = new CharacterReader("");
 assertEquals("", reader.consumeDigitSequence());
 }

 // Test for matches method
 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("TestTest");
 assertTrue(reader.matches('e'));
 assertTrue(reader.matches("es"));
 assertFalse(reader.matches('t'));
 assertFalse(reader.matches("esd"));
 }

 // Test for matches method with ignore case
 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("TestTest");
 assertTrue(reader.matchesIgnoreCase("es"));
 assertTrue(reader.matchesIgnoreCase("Test"));
 assertFalse(reader.matchesIgnoreCase("t"));
 assertFalse(reader.matchesIgnoreCase("esd"));
 }

 // Test for matchesAny method
 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("TestTestTestTestTestTestTestTestTest");
 assertTrue(reader.matchesAny('e'));
 assertTrue(reader.matchesAny('t'));
 assertTrue(reader.matchesAny('s'));
 assertFalse(reader.matchesAny('d'));
 }

 // Test for matchesAny method with sorted characters
 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader("TestTestTestTestTestTestTestTestTest");
 assertTrue(reader.matchesAnySorted('e', 's', 't'));
 assertTrue(reader.matchesAnySorted('s', 't', 'e'));
 assertFalse(reader.matchesAnySorted('d', 'e', 's', 't'));
 }

 // Test for matchesLetter method
 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("TestTestTestTestTestTestTestTestTest");
 assertTrue(reader.matchesLetter());
 assertTrue(reader.matchesLetter());
 assertTrue(reader.matchesLetter());
 assertTrue(reader.matchesLetter());
 assertTrue(reader.matchesLetter());
 assertTrue(reader.matchesLetter());
 assertTrue(reader.matchesLetter());
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter());
 }

 // Test for matchesDigit method
 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("1234567890");
 assertTrue(reader.matchesDigit());
 assertTrue(reader.matchesDigit());
 assertTrue(reader.matchesDigit());
 assertTrue(reader.matchesDigit());
 assertTrue(reader.matchesDigit());
 assertTrue(reader.matchesDigit());
 assertTrue(reader.matchesDigit());
 assertTrue(reader.matchesDigit());
 assertFalse(reader.matchesDigit());
 }

 // Test for matchConsume method
 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader("TestTestTestTestTestTestTestTestTest");
 assertTrue(reader.matchConsume("es"));
 assertTrue(reader.matchConsume("Test"));
 assertFalse(reader.matchConsume("t"));
 assertFalse(reader.matchConsume("esd"));
 }

 // Test for matchConsume method with ignore case
 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("TestTestTestTestTestTestTestTestTest");
 assertTrue(reader.matchConsumeIgnoreCase("es"));
 assertTrue(reader.matchConsumeIgnoreCase("Test"));
 assertFalse(reader.matchConsumeIgnoreCase("t"));
 assertFalse(reader.matchConsumeIgnoreCase("esd"));
 }

 // Test for containsIgnoreCase method
 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body><p>Test Test</p></body></html>");
 assertTrue(reader.containsIgnoreCase("/title>"));
 assertTrue(reader.containsIgnoreCase("/p>"));
 assertFalse(reader.containsIgnoreCase("/body>"));
 }
}