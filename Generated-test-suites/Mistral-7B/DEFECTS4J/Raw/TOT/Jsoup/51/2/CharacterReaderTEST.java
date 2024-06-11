package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testSimpleReader() {
 CharacterReader reader = new CharacterReader("abcdef");
 assertFalse(reader.isEmpty());
 assertSame('a', reader.current());
 assertSame('a', reader.consume());
 }

 @Test
 public void testMarkUnconsume() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertSame('a', reader.current());
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 String result = reader.consumeToEnd();
 assertEquals("abcdef", result);
 }

 @Test
 public void testConsumeToCharacter() {
 CharacterReader reader = new CharacterReader("abcdef");
 String result = reader.consumeTo('e');
 assertEquals("abcd", result);
 }

 @Test
 public void testConsumeToSequence() {
 CharacterReader reader = new CharacterReader("abcdef");
 String result = reader.consumeTo("def");
 assertEquals("abc", result);
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
 String result = reader.consumeToAny('a');
 assertEquals("abcdabcdabcdabcd", reader.toString());
 result = reader.consumeToAny('d');
 assertEquals("abcdabcdabcd", result);
 }

 @Test
 public void testConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
 String result = reader.consumeToAnySorted(new char[] {'c', 'd'});
 assertEquals("abcdabcd", result);
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("abc&defg<hijkl>mno");
 String result = reader.consumeData();
 assertEquals("abc", result);
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("  <tag name=\"attr\" value=\"value\">content</tag>");
 String result = reader.consumeTagName();
 assertEquals("tag", result);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumePastEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.consumeToEnd();
 reader.consume();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 try {
 CharacterReader reader = new CharacterReader(null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLengthInput() {
 try {
 CharacterReader reader = new CharacterReader("");
 reader.consume(5);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testStringCache() {
 CharacterReader reader = new CharacterReader("abcabcabc");
 String result1 = reader.consumeTo('b');
 String result2 = reader.consumeTo('b');
 String result3 = reader.consumeTo('b');
 assertEquals(result1, result2);
 assertEquals(result1, result3);
 }
}