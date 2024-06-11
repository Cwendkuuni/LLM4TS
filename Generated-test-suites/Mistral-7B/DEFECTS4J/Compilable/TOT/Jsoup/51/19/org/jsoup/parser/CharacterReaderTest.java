package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderEmpty() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderSingleCharacter() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertEquals('a', reader.current());
 reader.consume();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testCharacterReaderMultipleCharacters() {
 CharacterReader reader = new CharacterReader("abcd");
 String expected = reader.consumeTo('c');
 Assert.assertEquals("ab", expected);
 }

 @Test
 public void testCharacterReaderScanString() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 String expected = reader.consumeTo("abcd");
 Assert.assertEquals("abcdabcd", reader.toString());
 Assert.assertEquals("abcd", expected);
 }

 @Test
 public void testCharacterReaderScanCharSequence() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 String expected = reader.consumeTo("cd");
 Assert.assertEquals("abcdabcd", reader.toString());
 Assert.assertEquals("cd", expected);
 }

 @Test
 public void testCharacterReaderScanAnyCharacter() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
 String expected = reader.consumeToAny('a', 'e', 'i');
 Assert.assertEquals("aei", expected);
 }

 @Test
 public void testCharacterReaderScanAnyCharacterSorted() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
 String expected = reader.consumeToAnySorted('a', 'e', 'i');
 Assert.assertEquals("aei", expected);
 }

 @Test
 public void testCharacterReaderScanData() {
 CharacterReader reader = new CharacterReader("&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;/head&gt;&lt;body&gt;&lt;/body&gt;");
 String expected = reader.consumeData();
 Assert.assertEquals("&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;", expected);
 }

 @Test
 public void testCharacterReaderScanTagName() {
 CharacterReader reader = new CharacterReader("&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;/head&gt;&lt;body&gt;&lt;/body&gt;");
 String expected = reader.consumeTagName();
 Assert.assertEquals("title", expected);
 }

 @Test
 public void testCharacterReaderScanToEnd() {
 CharacterReader reader = new CharacterReader("&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;/head&gt;&lt;body&gt;&lt;/body&gt;");
 String expected = reader.consumeToEnd();
 Assert.assertEquals("&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;/head&gt;&lt;body&gt;&lt;/body&gt;", expected);
 }

 @Test
 public void testCharacterReaderScanLetterSequence() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
 String expected = reader.consumeLetterSequence();
 Assert.assertEquals("abcdef", expected);
 }

 @Test
 public void testCharacterReaderScanLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abc123def456");
 String expected = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("abc123", expected);
 }

 @Test
 public void testCharacterReaderScanHexSequence() {
 CharacterReader reader = new CharacterReader("&#x61;&#x62;&#x63;");
 String expected = reader.consumeHexSequence();
 Assert.assertEquals("abc", expected);
 }

 @Test
 public void testCharacterReaderScanDigitSequence() {
 CharacterReader reader = new CharacterReader("12345678901234567890");
 String expected = reader.consumeDigitSequence();
 Assert.assertEquals("123456", expected);
 }

 @Test(expected = IllegalStateException.class)
 public void testCharacterReaderMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testCharacterReaderConsumePastEnd() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.consumeToEnd();
 reader.consume();
 }
}