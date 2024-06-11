package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 // Test case for isEmpty() method
 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 reader = new CharacterReader("Hello World");
 assertFalse(reader.isEmpty());
 }

 // Test case for pos() method
 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals(0, reader.pos());
 reader.consume();
 reader.consume();
 assertEquals(2, reader.pos());
 }

 // Test case for current() method
 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals('H', reader.current());
 reader.consume();
 assertEquals('e', reader.current());
 }

 // Test case for consume() method
 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.consume();
 assertEquals('e', reader.current());
 reader.consume();
 assertEquals('l', reader.current());
 }

 // Test case for unconsume() method
 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.consume();
 reader.consume();
 reader.unconsume();
 assertEquals('e', reader.current());
 }

 // Test case for advance() method
 @Test
 public void testAdvance() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.advance();
 reader.advance();
 assertEquals('l', reader.current());
 }

 // Test case for mark() method
 @Test
 public void testMark() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.mark();
 reader.consume();
 reader.consume();
 reader.rewindToMark();
 assertEquals('H', reader.current());
 }

 // Test case for consumeAsString() method
 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals("He", reader.consumeAsString());
 reader.consume();
 assertEquals("llo", reader.consumeAsString());
 }

 // Test case for nextIndexOf() method with char
 @Test
 public void testNextIndexOfChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals(6, reader.nextIndexOf('l'));
 assertEquals(-1, reader.nextIndexOf('z'));
 }

 // Test case for nextIndexOf() method with CharSequence
 @Test
 public void testNextIndexOfCharSequence() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals(7, reader.nextIndexOf("llo"));
 assertEquals(-1, reader.nextIndexOf("zzz"));
 }

 // Test case for consumeTo() method with char
 @Test
 public void testConsumeToChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals("Hello", reader.consumeTo(' '));
 }

 // Test case for consumeTo() method with CharSequence
 @Test
 public void testConsumeToCharSequence() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals("Hello World", reader.consumeTo("World"));
 }

 // Test case for consumeToAny() method
 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("HelloWorld123&<");
 assertEquals("World123", reader.consumeToAny('1', '2', '3'));
 }

 // Test case for consumeToAnySorted() method
 @Test
 public void testConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader("1HelloWorld23");
 assertEquals("1Hello", reader.consumeToAnySorted('1', 'H', 'o'));
 }

 // Test case for consumeData() method
 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("& < abc & def & ghi");
 assertEquals("& < abc", reader.consumeData());
 }

 // Test case for consumeTagName() method
 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("<title>Hello World</title>");
 assertEquals("title", reader.consumeTagName());
 }

 // Test case for consumeToEnd() method
 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals("World", reader.consumeToEnd());
 }

 // Test case for consumeLetterSequence() method
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("HelloWorld");
 assertEquals("Hello", reader.consumeLetterSequence());
 }

 // Test case for consumeLetterThenDigitSequence() method
 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("Hello123World");
 assertEquals("Hello123", reader.consumeLetterThenDigitSequence());
 }

 // Test case for consumeHexSequence() method
 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("&x0F");
 assertEquals("xF", reader.consumeHexSequence());
 }

 // Test case for consumeDigitSequence() method
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("12345");
 assertEquals("5", reader.consumeDigitSequence());
 }

 // Test case for matches() method with char
 @Test
 public void testMatchesChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matches('l'));
 assertFalse(reader.matches('z'));
 }

 // Test case for matches() method with String
 @Test
 public void testMatchesString() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matches("ll"));
 assertFalse(reader.matches("zzz"));
 }

 // Test case for matchesIgnoreCase() method with String
 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesIgnoreCase("ll"));
 assertFalse(reader.matchesIgnoreCase("ZZZ"));
 }

 // Test case for matchesAny() method with char
 @Test
 public void testMatchesAnyChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesAny('l'));
 assertFalse(reader.matchesAny('z'));
 }

 // Test case for matchesAnySorted() method with char array
 @Test
 public void testMatchesAnySortedCharArray() {
 CharacterReader reader = new CharacterReader("Hello123World");
 assertTrue(reader.matchesAnySorted(new char[]{'1', '2'}));
 assertFalse(reader.matchesAnySorted(new char[]{'1', 'z'}));
 }

 // Test case for matchesLetter() method
 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesDigit());
 }

 // Test case for matchesDigit() method
 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertFalse(reader.matchesLetter());
 assertTrue(reader.matchesDigit());
 }

 // Test case for matchConsume() method with String
 @Test
 public void testMatchConsumeString() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchConsume("ll"));
 }

 // Test case for matchConsumeIgnoreCase() method with String
 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchConsumeIgnoreCase("ll"));
 }

 // Test case for containsIgnoreCase() method with String
 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<title>Hello World</title>");
 assertTrue(reader.containsIgnoreCase("title"));
 assertTrue(reader.containsIgnoreCase("world"));
 }
}