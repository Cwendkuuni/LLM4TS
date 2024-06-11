package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 // Test for isEmpty method
 @Test
 public void testisEmpty() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertTrue(reader.isEmpty());
 reader = new CharacterReader("abc");
 Assert.assertFalse(reader.isEmpty());
 }

 // Test for pos method
 @Test
 public void testpos() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertEquals(0, reader.pos());
 reader.consume();
 Assert.assertEquals(1, reader.pos());
 reader.consume(2);
 Assert.assertEquals(3, reader.pos());
 }

 // Test for current method
 @Test
 public void testcurrent() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertEquals('a', reader.current());
 reader.consume();
 Assert.assertEquals('b', reader.current());
 reader.consume();
 Assert.assertEquals('c', reader.current());
 reader.consume(); // EOF
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 }

 // Test for consume method
 @Test
 public void testconsume() {
 CharacterReader reader = new CharacterReader("abc");
 reader.consume();
 Assert.assertEquals('b', reader.current());
 reader.consume();
 Assert.assertEquals('c', reader.current());
 reader.consume(); // EOF
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 }

 // Test for unconsume method
 @Test
 public void testunconsume() {
 CharacterReader reader = new CharacterReader("abc");
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('a', reader.current());
 }

 // Test for advance method
 @Test
 public void testadvance() {
 CharacterReader reader = new CharacterReader("abc");
 reader.advance();
 Assert.assertEquals('b', reader.current());
 reader.advance();
 Assert.assertEquals('c', reader.current());
 reader.advance(); // EOF
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 }

 // Test for mark method
 @Test
 public void testmark() {
 CharacterReader reader = new CharacterReader("abc");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 // Test for rewindToMark method
 @Test
 public void testrewindToMark() {
 CharacterReader reader = new CharacterReader("abc");
 reader.consume();
 reader.consume();
 reader.mark();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 // Test for consumeAsString method
 @Test
 public void testconsumeAsString() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertEquals("a", reader.consumeAsString());
 reader.consume();
 Assert.assertEquals("bc", reader.consumeAsString());
 reader.consume(); // EOF
 Assert.assertEquals("", reader.consumeAsString());
 }

 // Test for nextIndexOf method with single char
 @Test
 public void testnextIndexOfSingleChar() {
 CharacterReader reader = new CharacterReader("abcabc");
 Assert.assertEquals(3, reader.nextIndexOf('c'));
 reader.consume();
 reader.consume();
 Assert.assertEquals(-1, reader.nextIndexOf('c'));
 }

 // Test for nextIndexOf method with CharSequence
 @Test
 public void testnextIndexOfCharSequence() {
 CharacterReader reader = new CharacterReader("abcabc");
 Assert.assertEquals(3, reader.nextIndexOf("bc"));
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertEquals(-1, reader.nextIndexOf("bc"));
 }

 // Test for consumeTo method with single char
 @Test
 public void testconsumeToSingleChar() {
 CharacterReader reader = new CharacterReader("abcabc");
 Assert.assertEquals("c", reader.consumeTo('c').toString());
 reader.consume();
 reader.consume();
 Assert.assertEquals("", reader.consumeTo('c').toString());
 }

 // Test for consumeTo method with CharSequence
 @Test
 public void testconsumeToCharSequence() {
 CharacterReader reader = new CharacterReader("abcabc");
 Assert.assertEquals("bc", reader.consumeTo("bc").toString());
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertEquals("", reader.consumeTo("bc").toString());
 }

 // Test for consumeToAny method with single char
 @Test
 public void testconsumeToAnySingleChar() {
 CharacterReader reader = new CharacterReader("abc123");
 Assert.assertEquals("1", reader.consumeToAny('1').toString());
 reader.consume();
 reader.consume();
 Assert.assertEquals("", reader.consumeToAny('1').toString());
 }

 // Test for consumeToAny method with char array
 @Test
 public void testconsumeToAnyCharArray() {
 CharacterReader reader = new CharacterReader("abc123");
 Assert.assertEquals("123", reader.consumeToAny(new char[]{'1', '2', '3'}).toString());
 reader.consume();
 reader.consume();
 Assert.assertEquals("", reader.consumeToAny(new char[]{'1', '2', '3'}).toString());
 }

 // Test for consumeToAnySorted method with char array
 @Test
 public void testconsumeToAnySortedCharArray() {
 CharacterReader reader = new CharacterReader("abcd2efg");
 Assert.assertEquals("2efg", reader.consumeToAnySorted(new char[]{'2', 'e', 'f', 'g'}).toString());
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertEquals("", reader.consumeToAnySorted(new char[]{'2', 'e', 'f', 'g'}).toString());
 }

 // Test for consumeData method
 @Test
 public void testconsumeData() {
 CharacterReader reader = new CharacterReader("&<abc");
 Assert.assertEquals("&<", reader.consumeData().toString());
 reader.consume();
 reader.consume();
 Assert.assertEquals("abc", reader.consumeData().toString());
 reader.consume(); // EOF
 Assert.assertEquals("", reader.consumeData().toString());
 }

 // Test for consumeTagName method
 @Test
 public void testconsumeTagName() {
 CharacterReader reader = new CharacterReader("<tab> <title>Hello</title>");
 Assert.assertEquals("<tab>", reader.consumeTagName().toString());
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertEquals("title", reader.consumeTagName().toString());
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertEquals("Hello", reader.consumeTagName().toString());
 reader.consume(); // EOF
 Assert.assertEquals("", reader.consumeTagName().toString());
 }

 // Test for consumeToEnd method
 @Test
 public void testconsumeToEnd() {
 CharacterReader reader = new CharacterReader("<tab> <title>Hello</title>");
 Assert.assertEquals("<tab><title>Hello</title>", reader.consumeToEnd().toString());
 reader.consume(); // EOF
 Assert.assertEquals("", reader.consumeToEnd().toString());
 }

 // Test for consumeLetterSequence method
 @Test
 public void testconsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("ABC");
 Assert.assertEquals("C", reader.consumeLetterSequence().toString());
 reader.consume();
 Assert.assertEquals("B", reader.consumeLetterSequence().toString());
 reader.consume();
 Assert.assertEquals("A", reader.consumeLetterSequence().toString());
 reader.consume(); // EOF
 Assert.assertEquals("", reader.consumeLetterSequence().toString());
 }

 // Test for consumeLetterThenDigitSequence method
 @Test
 public void testconsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("ab123");
 Assert.assertEquals("23", reader.consumeLetterThenDigitSequence().toString());
 reader.consume();
 reader.consume();
 Assert.assertEquals("a", reader.consumeLetterThenDigitSequence().toString());
 reader.consume(); // EOF
 Assert.assertEquals("", reader.consumeLetterThenDigitSequence().toString());
 }

 // Test for consumeHexSequence method
 @Test
 public void testconsumeHexSequence() {
 CharacterReader reader = new CharacterReader("&x0F");
 Assert.assertEquals("F", reader.consumeHexSequence().toString());
 reader.consume();
 reader.consume();
 Assert.assertEquals("", reader.consumeHexSequence().toString());
 }

 // Test for consumeDigitSequence method
 @Test
 public void testconsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("123");
 Assert.assertEquals("3", reader.consumeDigitSequence().toString());
 reader.consume();
 Assert.assertEquals("2", reader.consumeDigitSequence().toString());
 reader.consume(); // EOF
 Assert.assertEquals("", reader.consumeDigitSequence().toString());
 }

 // Test for matches method
 @Test
 public void testmatches() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertTrue(reader.matches('a'));
 Assert.assertTrue(reader.matches("a"));
 reader.consume();
 Assert.assertFalse(reader.matches('a'));
 Assert.assertFalse(reader.matches("a"));
 }

 // Test for matchesIgnoreCase method
 @Test
 public void testmatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertTrue(reader.matchesIgnoreCase("a"));
 Assert.assertTrue(reader.matchesIgnoreCase("A"));
 reader.consume();
 Assert.assertFalse(reader.matchesIgnoreCase("a"));
 Assert.assertFalse(reader.matchesIgnoreCase("A"));
 }

 // Test for matchesAny method
 @Test
 public void testmatchesAny() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertTrue(reader.matchesAny('a', 'b', 'c'));
 Assert.assertTrue(reader.matchesAny("a", "b", "c"));
 reader.consume();
 Assert.assertFalse(reader.matchesAny('a', 'b', 'c'));
 Assert.assertFalse(reader.matchesAny("a", "b", "c"));
 }

 // Test for matchesAnySorted method
 @Test
 public void testmatchesAnySorted() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c'}));
 reader.consume();
 Assert.assertFalse(reader.matchesAnySorted(new char[]{'a', 'b', 'c'}));
 }

 // Test for matchesLetter method
 @Test
 public void testmatchesLetter() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertTrue(reader.matchesLetter());
 reader.consume();
 reader.consume();
 Assert.assertFalse(reader.matchesLetter());
 }

 // Test for matchesDigit method
 @Test
 public void testmatchesDigit() {
 CharacterReader reader = new CharacterReader("123");
 Assert.assertTrue(reader.matchesDigit());
 reader.consume();
 reader.consume(); // EOF
 Assert.assertFalse(reader.matchesDigit());
 }

 // Test for matchConsume method
 @Test
 public void testmatchConsume() {
 CharacterReader reader = new CharacterReader("abcabc");
 Assert.assertTrue(reader.matchConsume("bc"));
 }

 // Test for matchConsumeIgnoreCase method
 @Test
 public void testmatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("AbCaBc");
 Assert.assertTrue(reader.matchConsumeIgnoreCase("bc"));
 }

 // Test for containsIgnoreCase method
 @Test
 public void testcontainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<tab><title>Hello</title></tab>");
 Assert.assertTrue(reader.containsIgnoreCase("/title/"));
 Assert.assertTrue(reader.containsIgnoreCase("TITLE"));
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertFalse(reader.containsIgnoreCase("/title/"));
 Assert.assertFalse(reader.containsIgnoreCase("TITLE"));
 }

 // Test for toString method
 @Test
 public void testtoString() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertEquals("abc", reader.toString());
 }
}