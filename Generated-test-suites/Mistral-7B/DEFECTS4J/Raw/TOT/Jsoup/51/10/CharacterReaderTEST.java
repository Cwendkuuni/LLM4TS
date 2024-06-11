package org.jsoup.parser;

import org.junit.Assert; import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testConsumeOneCharacter() {
 CharacterReader reader = new CharacterReader("test");
 char expected = 't';
 char actual = reader.consume();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeMultipleCharacters() {
 CharacterReader reader = new CharacterReader("test");
 String expected = "est";
 String actual = reader.consume(3).toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("test");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 char expected = 't';
 char actual = reader.current();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeString() {
 CharacterReader reader = new CharacterReader("test");
 String expected = "est";
 String actual = reader.consumeAsString(2);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEndOfInput() {
 CharacterReader reader = new CharacterReader("test");
 reader.consumeTo('x');
 }

 @Test
 public void testNextIndexOfChar() {
 CharacterReader reader = new CharacterReader("testtest");
 int expected = 3;
 int actual = reader.nextIndexOf('t');
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testNextIndexOfString() {
 CharacterReader reader = new CharacterReader("testtesttest");
 int expected = 6;
 int actual = reader.nextIndexOf("est");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToChar() {
 CharacterReader reader = new CharacterReader("testtesttest");
 String expected = "est";
 String actual = reader.consumeTo('t').toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToString() {
 CharacterReader reader = new CharacterReader("testtesttest");
 String expected = "esttest";
 String actual = reader.consumeTo("est").toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToAnyChar() {
 CharacterReader reader = new CharacterReader("testtesttest123");
 String expected = "esttest1";
 String actual = reader.consumeToAny('1').toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToAnySortedChars() {
 CharacterReader reader = new CharacterReader("testtesttest231");
 String expected = "esttest2";
 String actual = reader.consumeToAnySorted(new char[]{'2'}).toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("&lt;test&gt;");
 String expected = "&lt;test&gt;";
 String actual = reader.consumeData().toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("<test>");
 String expected = "test";
 String actual = reader.consumeTagName().toString();
 Assert.assertEquals(expected, actual);
 }
}