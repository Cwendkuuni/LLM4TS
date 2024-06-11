package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

public class CharacterReaderTest {

 @Test
 public void testCharacterReader_ReaderInput() throws IOException {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 assertEquals(0, reader.pos());
 assertEquals(EOF, reader.current());
 assertEquals(EOF, reader.consume());
 assertEquals(0, reader.pos());
 }

 @Test
 public void testCharacterReader_StringInput() {
 CharacterReader reader = new CharacterReader("test");
 assertEquals(0, reader.pos());
 assertEquals(EOF, reader.current());
 assertEquals(EOF, reader.consume());
 assertEquals(0, reader.pos());
 }

 @Test
 public void testBufferUp() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 reader.bufferUp();
 reader.advance();
 reader.advance();
 assertEquals('t', reader.current());
 }

 @Test(expected = UncheckedIOException.class)
 public void testIOException() throws IOException {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 reader.bufferUp();
 reader.reader.close();
 reader.consume();
 }

 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 assertFalse(reader.isEmpty());
 reader.consume();
 reader.consume();
 reader.consumeTo(EOF);
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 reader.bufferUp();
 reader.advance();
 reader.advance();
 assertEquals('t', reader.current());
 reader.consume();
 assertEquals(EOF, reader.current());
 }

 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 reader.bufferUp();
 reader.advance();
 reader.advance();
 assertEquals('t', reader.current());
 char c = reader.consume();
 assertEquals('e', reader.current());
 assertEquals(3, reader.pos());
 }

 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 reader.bufferUp();
 reader.advance();
 reader.advance();
 reader.consume();
 reader.unconsume();
 assertEquals('t', reader.current());
 }

 @Test
 public void testAdvance() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 reader.bufferUp();
 reader.advance();
 reader.advance();
 assertEquals('t', reader.current());
 reader.advance();
 assertEquals(EOF, reader.current());
 }

 @Test
 public void testMark() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 reader.bufferUp();
 reader.advance();
 reader.advance();
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('t', reader.current());
 }

 @Test
 public void testNextIndexOf() {
 CharacterReader reader = new CharacterReader(new StringReader("abracadabra"));
 assertEquals(5, reader.nextIndexOf('a'));
 assertEquals(6, reader.nextIndexOf('b'));
 assertEquals(7, reader.nextIndexOf('r'));
 assertEquals(-1, reader.nextIndexOf('x'));
 }

 @Test
 public void testNextIndexOf_CharSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("abracadabra"));
 assertEquals(4, reader.nextIndexOf("ra"));
 }

 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader(new StringReader("abracadabra"));
 String result = reader.consumeTo('a');
 assertEquals("", result);
 result = reader.consumeTo('r');
 assertEquals("a", result);
 result = reader.consumeTo('d');
 assertEquals("a", result);
 result = reader.consumeTo('b');
 assertEquals("a", result);
 result = reader.consumeTo('r');
 assertEquals("a", result);
 result = reader.consumeTo('a');
 assertEquals("abra", result);
 result = reader.consumeToEnd();
 assertEquals("abracadabra", result);
 }

 @Test
 public void testConsumeTo_CharSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("abracadabra"));
 String result = reader.consumeTo("abra");
 assertEquals("", result);
 result = reader.consumeTo("ra");
 assertEquals("abra", result);
 result = reader.consumeToEnd();
 assertEquals("abracadabra", result);
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 assertEquals("", reader.consumeToAny(new char[]{}));
 assertEquals("test", reader.consumeToAny(new char[]{'t', 's'}));
 assertEquals("est", reader.consumeToAny(new char[]{'s', 't'}));
 assertEquals("est", reader.consumeToAny(new char[]{'e', 't'}));
 assertEquals("est", reader.consumeToAny(new char[]{'e', 's'}));
 assertEquals("test", reader.consumeToAny(new char[]{'s', 't', 'e'}));
 }

 @Test
 public void testConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 assertEquals("", reader.consumeToAnySorted(new char[]{}));
 assertEquals("test", reader.consumeToAnySorted(new char[]{'s', 't'}));
 assertEquals("est", reader.consumeToAnySorted(new char[]{'t', 's'}));
 assertEquals("est", reader.consumeToAnySorted(new char[]{'e', 's'}));
 assertEquals("est", reader.consumeToAnySorted(new char[]{'e', 't'}));
 assertEquals("test", reader.consumeToAnySorted(new char[]{'s', 't', 'e'}));
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader(new StringReader("&lt;test&gt;"));
 assertEquals("&lt;", reader.consumeData());
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
 assertEquals("<html>", reader.consumeTagName());
 reader.consumeToAny(new char[]{'<', 't', 'i', 't', 'l'});
 assertEquals("</head>", reader.consumeTagName());
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
 assertEquals("<html><head><title>Test</title></head>", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("letters"));
 assertEquals("letters", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("123letters123"));
 assertEquals("123letters123", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("0x1234"));
 assertEquals("0x1234", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("123456"));
 assertEquals("123456", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 assertTrue(reader.matches('t'));
 assertTrue(reader.matches("te"));
 assertFalse(reader.matches('s'));
 assertFalse(reader.matches("es"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 assertTrue(reader.matchesIgnoreCase("te"));
 assertTrue(reader.matchesIgnoreCase("test"));
 assertFalse(reader.matchesIgnoreCase("s"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 assertTrue(reader.matchesAny(new char[]{'t', 'e'}));
 assertTrue(reader.matchesAny(new char[]{'s', 't', 'e'}));
 assertTrue(reader.matchesAny(new char[]{'e', 't', 's'}));
 assertFalse(reader.matchesAny(new char[]{'x'}));
 }

 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 assertTrue(reader.matchesAnySorted(new char[]{'s', 't', 'e'}));
 assertTrue(reader.matchesAnySorted(new char[]{'e', 't', 's'}));
 assertTrue(reader.matchesAnySorted(new char[]{'t', 'e', 's'}));
 assertFalse(reader.matchesAnySorted(new char[]{'x'}));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter(Character.MIN_VALUE));
 assertFalse(reader.matchesLetter(Character.MAX_VALUE));
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader(new StringReader("test123"));
 assertTrue(reader.matchesDigit());
 assertFalse(reader.matchesDigit(Character.MIN_VALUE));
 assertFalse(reader.matchesDigit(Character.MAX_VALUE));
 }

 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 String result = reader.matchConsume("te");
 assertEquals("te", result);
 result = reader.matchConsume("es");
 assertEquals("es", result);
 result = reader.matchConsume("s");
 assertEquals("s", result);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 String result = reader.matchConsumeIgnoreCase("Te");
 assertEquals("te", result);
 result = reader.matchConsumeIgnoreCase("Es");
 assertEquals("es", result);
 result = reader.matchConsumeIgnoreCase("S");
 assertEquals("s", result);
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
 assertTrue(reader.containsIgnoreCase("/title"));
 assertTrue(reader.containsIgnoreCase("title"));
 assertFalse(reader.containsIgnoreCase("/head"));
 }
}