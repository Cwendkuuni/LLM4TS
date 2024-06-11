package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertTrue("Empty reader should be empty", reader.isEmpty());
 Assert.assertEquals("", reader.toString());
 }

 @Test
 public void testReaderWithSingleCharacter() {
 CharacterReader reader = new CharacterReader("A");
 Assert.assertFalse("Reader with single character should not be empty", reader.isEmpty());
 Assert.assertEquals("A", reader.consumeAsString());
 }

 @Test
 public void testReaderConsumeMultipleCharacters() {
 CharacterReader reader = new CharacterReader("Hello World");
 String expected = "Hello";
 int offset = reader.nextIndexOf(expected.charAt(0));
 String consumed = reader.consumeTo(expected.charAt(0));
 Assert.assertEquals(expected, consumed);
 Assert.assertEquals(offset + expected.length(), reader.pos());
 }

 @Test
 public void testReaderConsumeSequence() {
 CharacterReader reader = new CharacterReader("Hello World");
 String expected = "World";
 int offset = reader.nextIndexOf(expected);
 String consumed = reader.consumeTo(expected);
 Assert.assertEquals(expected, consumed);
 Assert.assertEquals(offset + expected.length(), reader.pos());
 }

 @Test
 public void testReaderConsumeAnyCharacters() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
 String expected = "abc";
 String consumed = reader.consumeToAny(expected.toCharArray());
 Assert.assertEquals(expected, consumed);
 }

 @Test
 public void testReaderConsumeAnySortedCharacters() {
 CharacterReader reader = new CharacterReader("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890");
 char[] expected = {'A', 'B', 'C'};
 String consumed = reader.consumeToAnySorted(expected);
 Assert.assertEquals(Arrays.toString(expected), consumed);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReaderWithNullInput() {
 CharacterReader reader = new CharacterReader(null);
 }
}