package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 // Empty reader should return EOF at the beginning
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testSimpleConsume() {
 CharacterReader reader = new CharacterReader("abcdef");
 // Consume 'a' and assert current char is 'b'
 reader.consume();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testConsumeSequence() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 // Consume 'abcd' and assert current char is 'a'
 reader.consume(4);
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 // Consume entire input and assert current char is EOF
 reader.consumeToEnd();
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 // Consume 'abcd'
 reader.consume(4);
 // Mark position and consume next character
 reader.mark();
 reader.consume();
 // Rewind to mark and assert current character is 'd'
 reader.rewindToMark();
 Assert.assertEquals('d', reader.current());
 }

 @Test
 public void testNextIndexOf() {
 CharacterReader reader = new CharacterReader("abcdefabcdef");
 // Next index of 'e' should be 5
 Assert.assertEquals(5, reader.nextIndexOf('e'));
 // Next index of sequence "def" should be 5
 Assert.assertEquals(5, reader.nextIndexOf("def"));
 // Next index of unknown character should be -1
 Assert.assertEquals(-1, reader.nextIndexOf('g'));
 }

 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader("abcdefabcdef");
 // Consume up to 'e' and assert consumed string is "abcde"
 String consumed = reader.consumeTo('e');
 Assert.assertEquals("abcde", consumed);
 // Consume up to end and assert consumed string is "abcdefabcdef"
 consumed = reader.consumeToEnd();
 Assert.assertEquals("abcdefabcdef", consumed);
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("1234abcxyz1234");
 // Consume up to any of the given characters and assert consumed string is "1234"
 String consumed = reader.consumeToAny('1', '2', '3', '4');
 Assert.assertEquals("1234", consumed);
 }

 @Test
 public void testConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader("abcd123ef456g");
 // Consume up to any of the given characters in sorted order and assert consumed string is "abcd"
 String consumed = reader.consumeToAnySorted('a', 'b', 'c', 'd');
 Assert.assertEquals("abcd", consumed);
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("&lt;abc&amp;def&gt;");
 // Consume data until first special character and assert consumed string is "<abc"
 String consumed = reader.consumeData();
 Assert.assertEquals("<abc", consumed);
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("&lt;tag name=&quot;attribute&quot;&gt;");
 // Consume tag name and assert consumed string is "tag"
 String consumed = reader.consumeTagName();
 Assert.assertEquals("tag", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcdef");
 // Consume letter sequence and assert consumed string is "abcdef"
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("abcdef", consumed);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abc123def456");
 // Consume letter then digit sequence and assert consumed string is "abc123def456"
 String consumed = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("abc123def456", consumed);
 }

 @Test
 public void testConsumeHexSequence() { // Assuming this class can handle hex sequences properly
 CharacterReader reader = new CharacterReader("&#x61;&#x61;&#x61;");
 // Consume hex sequence and assert consumed string is "aaa"
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("aaa", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 // Consume digit sequence and assert consumed string is "1234567890"
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals("1234567890", consumed);
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("abcdef");
 // Match 'e' and assert true
 Assert.assertTrue(reader.matches('e'));
 // Match unknown character and assert false
 Assert.assertFalse(reader.matches('g'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("ABCDEF");
 // Match 'e' ignoring case and assert true
 Assert.assertTrue(reader.matchesIgnoreCase("e"));
 // Match unknown character ignoring case and assert false
 Assert.assertFalse(reader.matchesIgnoreCase("g"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 // Match 'a' and assert true
 Assert.assertTrue(reader.matchesAny('a'));
 // Match unknown character and assert false
 Assert.assertFalse(reader.matchesAny('g'));
 }

 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 // Match 'a' and assert true
 Assert.assertTrue(reader.matchesAnySorted('a'));
 // Match unknown character and assert false
 Assert.assertFalse(reader.matchesAnySorted('g'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 // Match 'a' and assert true
 Assert.assertTrue(reader.matchesLetter());
 // Match unknown character and assert false
 Assert.assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("1234567890");
 // Match '1' and assert true
 Assert.assertTrue(reader.matchesDigit());
 // Match unknown character and assert false
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 // Match "abcd" and consume it
 reader.matchConsume("abcd");
 Assert.assertFalse(reader.matches("abcd")); // Should no longer match "abcd"
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("ABCDABCD");
 // Match "ABCD" ignoring case and consume it
 reader.matchConsumeIgnoreCase("abcd");
 Assert.assertFalse(reader.matchesIgnoreCase("ABCD")); // Should no longer match "ABCD" ignoring case
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("&lt;title&gt;Hello World&lt;/title&gt;");
 // Check for presence of "/title>" ignoring case and assert true
 Assert.assertTrue(reader.containsIgnoreCase("/title>"));
 // Check for presence of "World" ignoring case and assert true
 Assert.assertTrue(reader.containsIgnoreCase("world"));
 // Check for presence of unknown sequence ignoring case and assert false
 Assert.assertFalse(reader.containsIgnoreCase("XXX"));
 }
}