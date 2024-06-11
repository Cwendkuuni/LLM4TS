package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 private CharacterReader reader;
 private char[] input = new char[]{'a', 'b', 'c'};

 @Before
 public void setUp() {
 reader = new CharacterReader(new String(input));
 }

 @Test
 public void testConstructorWithString() {
 CharacterReader reader = new CharacterReader(new String(input));
 assertNotNull(reader);
 }

 @Test
 public void testConstructorWithReader() {
 CharacterReader reader = new CharacterReader(new StringReader(new String(input)));
 assertNotNull(reader);
 }

 @Test
 public void testConstructorWithReaderAndSize() {
 CharacterReader reader = new CharacterReader(new StringReader(new String(input)), input.length);
 assertNotNull(reader);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 reader.consume();
 assertEquals(1, reader.pos());
 reader.consume();
 reader.consume();
 assertEquals(3, reader.pos());
 }

 @Test
 public void testConsume() {
 assertEquals('a', reader.current());
 reader.consume();
 assertEquals('b', reader.current());
 reader.consume();
 assertEquals('c', reader.current());
 reader.consume();
 assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test(expected = UncheckedIOException.class)
 public void testIOException() throws IOException {
 reader = new CharacterReader(new StringReader("Invalid Input"));
 reader.consume();
 }

 @Test
 public void testBufferUp() {
 reader.consume();
 reader.consume();
 reader.bufferUp();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testAdvance() {
 reader.consume();
 reader.advance();
 assertEquals('b', reader.current());
 }

 @Test
 public void testMark() {
 reader.consume();
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testNextIndexOf() {
 assertEquals(1, reader.nextIndexOf('b'));
 assertEquals(-1, reader.nextIndexOf('d'));
 }

 @Test
 public void testNextIndexOfSequence() {
 assertEquals(1, reader.nextIndexOf("ab"));
 assertEquals(-1, reader.nextIndexOf("cd"));
 }

 @Test
 public void testConsumeTo() {
 String str = reader.consumeTo('b');
 assertEquals("a", str);
 }

 @Test
 public void testConsumeToEnd() {
 String str = reader.consumeToEnd();
 assertEquals("abc", str);
 }

 @Test
 public void testConsumeLetterSequence() {
 String str = reader.consumeLetterSequence();
 assertEquals("abc", str);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 String str = reader.consumeLetterThenDigitSequence();
 assertEquals("ab123", str);
 }

 @Test
 public void testConsumeHexSequence() {
 String str = reader.consumeHexSequence();
 assertEquals("123456", str);
 }

 @Test
 public void testConsumeDigitSequence() {
 String str = reader.consumeDigitSequence();
 assertEquals("123456", str);
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('a'));
 assertFalse(reader.matches('b'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches("ab"));
 assertFalse(reader.matches("cd"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("ab"));
 assertFalse(reader.matchesIgnoreCase("CD"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny('a', 'b'));
 assertFalse(reader.matchesAny('c', 'a'));
 }

 @Test
 public void testMatchesAnySorted() {
 assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
 assertFalse(reader.matchesAnySorted(new char[]{'b', 'a'}));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(reader.matchesDigit());
 assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 reader.consume();
 String str = reader.matchConsume("ab");
 assertEquals("ab", str);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 reader.consume();
 String str = reader.matchConsumeIgnoreCase("Ab");
 assertEquals("ab", str);
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("ab"));
 assertFalse(reader.containsIgnoreCase("cd"));
 }

 @Test
 public void testToString() {
 String str = reader.toString();
 assertEquals("abc", str);
 }

 @After
 public void tearDown() {
 reader = null;
 }
}