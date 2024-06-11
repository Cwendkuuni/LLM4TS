package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 assertFalse(reader.matches('a'));
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testSimpleReader() {
 CharacterReader reader = new CharacterReader("abc");
 assertEquals("a", reader.consumeAsString());
 assertEquals('a', reader.current());
 assertEquals('b', reader.consume());
 assertEquals("bc", reader.consumeTo("c"));
 assertEquals("abc", reader.consumeToEnd());
 }

 @Test
 public void testScanChar() {
 CharacterReader reader = new CharacterReader("abcdef");
 int index = reader.nextIndexOf('b');
 assertEquals(1, index);
 index = reader.nextIndexOf('e');
 assertEquals(4, index);
 index = reader.nextIndexOf('z');
 assertEquals(-1, index);
 }

 @Test
 public void testScanString() {
 CharacterReader reader = new CharacterReader("abcdef");
 int index = reader.nextIndexOf("def");
 assertEquals(3, index);
 index = reader.nextIndexOf("abc");
 assertEquals(-1, index);
 }

 @Test
 public void testConsumeSequence() {
 CharacterReader reader = new CharacterReader("&<><>");
 assertEquals("<", reader.consumeTo('<').trim());
 assertEquals("><>", reader.consumeTo(">").trim());
 assertEquals("&<><>", reader.consumeToEnd().trim());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("123abcd");
 assertEquals("abc", reader.consumeLetterSequence().trim());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("123abcd123");
 assertEquals("abc123", reader.consumeLetterThenDigitSequence().trim());
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("0x123A");
 assertEquals("123A", reader.consumeHexSequence().trim());
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("12345");
 assertEquals("45", reader.consumeDigitSequence().trim());
 }

 @Test
 public void testScanSpecialChars() {
 CharacterReader reader = new CharacterReader("\t\n\r\f ");
 assertEquals("\t", reader.consumeTo('\t').trim());
 assertEquals("\n\r\f ", reader.consumeToEnd().trim());
 }

 @Test
 public void testScanStringIgnoreCase() {
 CharacterReader reader = new CharacterReader("<TITLE></title>");
 assertTrue(reader.containsIgnoreCase("/title"));
 }
}