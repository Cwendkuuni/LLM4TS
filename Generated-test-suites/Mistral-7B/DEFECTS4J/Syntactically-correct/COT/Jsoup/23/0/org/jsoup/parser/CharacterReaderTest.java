package org.jsoup.parser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.CharacterReader.EOF;

import org.junit.Test;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] testInput = {'t', 'e', 's', 't', ' ', 'i', 's', ' ', 'a', ' ', 't', 'e', 's', 't', '\n'};
 reader = new CharacterReader(new String(testInput));
 }

 @Test
 public void testConstructor() {
 CharacterReader reader = new CharacterReader("test");
 assertEquals("test", reader.input);
 assertEquals(6, reader.length);
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 new CharacterReader(null);
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(reader.isEmpty());
 reader.consume();
 reader.consume();
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 assertEquals('t', reader.current());
 reader.consume();
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('t', reader.consume());
 assertEquals('e', reader.current());
 reader.consume();
 assertEquals('i', reader.current());
 reader.consume();
 assertEquals(' ', reader.current());
 reader.consume();
 assertEquals('a', reader.current());
 reader.consume();
 assertEquals(' ', reader.current());
 reader.consume();
 assertEquals('t', reader.current());
 reader.consume();
 reader.consume();
 assertEquals('\n', reader.current());
 assertEquals(EOF, reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume();
 reader.unconsume();
 assertEquals('i', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('e', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('t', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("is a", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals(" is ", reader.consumeTo(' '));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("test\n", reader.consumeToEnd());
 }

 @Test
 public void testConsumeToAny() {
 assertEquals(" is ", reader.consumeToAny(' ', 's'));
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("test", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("test", reader.consumeHexSequence()); // This method seems to be identical to consumeLetterSequence()
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("123", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertFalse(reader.matches('a'));
 assertTrue(reader.matches('t'));
 reader.consume();
 assertFalse(reader.matches('t'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches("is"));
 reader.consume();
 reader.consume();
 assertFalse(reader.matches("is"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("is"));
 reader.consume();
 reader.consume();
 assertTrue(reader.matchesIgnoreCase("is"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny('s', 't'));
 assertFalse(reader.matchesAny('x'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 reader.consume();
 assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(reader.matchesDigit());
 reader.consume();
 reader.consume();
 reader.consume();
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 assertEquals("is", reader.matchConsume("is"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("is", reader.matchConsumeIgnoreCase("is"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("is"));
 reader.consume();
 reader.consume();
 assertTrue(reader.containsIgnoreCase("is"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndWithEmptyInput() {
 new CharacterReader("").consumeToEnd();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToWithNegativeIndex() {
 new CharacterReader("test").consumeTo(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToWithNullSeq() {
 new CharacterReader("test").consumeTo(null);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testConsumeToAnyWithEmptyInput() {
 new CharacterReader("").consumeToAny((char[]) null);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testConsumeToAnyWithNullSeqElement() {
 new CharacterReader("test").consumeToAny(new char[] { 'a', null });
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndWithMarkBeyondEnd() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 reader.consumeToEnd();
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeToEndWithMarkAtStart() {
 reader.mark();
 reader.consumeToEnd();
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testConsumeToWithEmptyInputAndEmptySeq() {
 new CharacterReader("").consumeTo("");
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testConsumeToWithEmptyInputAndNonEmptySeq() {
 new CharacterReader("").consumeTo("a");
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testConsumeToWithNonEmptyInputAndEmptySeq() {
 new CharacterReader("test").consumeTo("");
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testConsumeToWithNonEmptyInputAndNonEmptySeqButNotPresent() {
 new CharacterReader("test").consumeTo("x");
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testConsumeToWithNonEmptyInputAndNonEmptySeqButNotAtStart() {
 reader.consume();
 new CharacterReader("test").consumeTo("s");
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testConsumeToAnyWithEmptyInput() {
 new CharacterReader("").consumeToAny();
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testConsumeToAnyWithEmptySeq() {
 new CharacterReader("test").consumeToAny();
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testConsumeToAnyWithNonEmptyInputAndEmptySeq() {
 new CharacterReader("test").consumeToAny("");
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testConsumeToAnyWithNonEmptyInputAndNonEmptySeqButNotPresent() {
 reader.consume();
 reader.consume();
 new CharacterReader("test").consumeToAny("s");
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testConsumeToAnyWithNonEmptyInputAndNonEmptySeqButNotAtStart() {
 reader.consume();
 reader.consume();
 reader.consume();
 new CharacterReader("test").consumeToAny("t");
 }

 @Test
 public void testToString() {
 assertEquals("test\n", reader.toString());
 }
}