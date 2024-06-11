package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderEmpty() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 assertTrue(reader.isEmpty());
 assertEquals(0, reader.pos());
 }

 @Test
 public void testCharacterReaderConsumeSingleCharacter() {
 CharacterReader reader = new CharacterReader(new StringReader("abc"));
 assertEquals('a', reader.current());
 assertEquals('a', reader.consume());
 }

 @Test
 public void testCharacterReaderConsumeMultipleCharacters() {
 CharacterReader reader = new CharacterReader(new StringReader("abc"));
 assertEquals('a', reader.current());
 assertEquals('a', reader.consume());
 assertEquals('b', reader.current());
 assertEquals('b', reader.consume());
 assertEquals('c', reader.current());
 }

 @Test(expected = UncheckedIOException.class)
 public void testCharacterReaderIOException() {
 CharacterReader reader = new CharacterReader(new StringReader("abc") {
 @Override
 public int read(char[] cbuf, int off, int len) throws IOException {
 throw new IOException("Test IOException");
 }
 });
 reader.consume();
 }

 @Test
 public void testCharacterReaderMarkAndRewind() {
 CharacterReader reader = new CharacterReader(new StringReader("abc"));
 reader.mark();
 reader.advance();
 assertEquals('a', reader.current());
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testCharacterReaderNextIndexOf() {
 CharacterReader reader = new CharacterReader(new StringReader("hello world"));
 assertEquals(5, reader.nextIndexOf(' '));
 assertEquals(6, reader.nextIndexOf('d'));
 }

 @Test
 public void testCharacterReaderConsumeTo() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdefghij"));
 assertEquals("abc", reader.consumeTo('d'));
 assertEquals("efg", reader.consumeTo('g'));
 assertEquals("hij", reader.consumeToEnd());
 }

 @Test
 public void testCharacterReaderConsumeToEnd() {
 CharacterReader reader = new CharacterReader(new StringReader("abc"));
 assertEquals("abc", reader.consumeToEnd());
 }

 @Test
 public void testCharacterReaderConsumeToAny() {
 CharacterReader reader = new CharacterReader(new StringReader("123abcd123abcd123"));
 assertEquals("123", reader.consumeToAny(',', '.'));
 assertEquals("abcd", reader.consumeToAny(new char[]{'a', 'b', 'c', 'd'}));
 }

 @Test
 public void testCharacterReaderConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("123abcd123abcd123"));
 assertEquals("123", reader.consumeToAnySorted(new char[]{',', '.'}));
 }

 @Test
 public void testCharacterReaderConsumeData() {
 CharacterReader reader = new CharacterReader(new StringReader("&lt;script src=\"script.js\"&gt;"));
 assertEquals("<script src=\"script.js\"", reader.consumeData());
 }

 @Test
 public void testCharacterReaderConsumeTagName() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test Title</title></head>"));
 assertEquals("html", reader.consumeTagName());
 assertEquals("head", reader.consumeTagName());
 assertEquals("title", reader.consumeTagName());
 }

 @Test
 public void testCharacterReaderContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test Title</title></head>"));
 assertTrue(reader.containsIgnoreCase("/title"));
 assertTrue(reader.containsIgnoreCase("/HEAD"));
 }
}