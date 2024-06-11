package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

  private CharacterReader reader;

  @Before
  public void setUp() {
     reader = new CharacterReader("This is a test string.");
     assertNotNull(reader);
  }

  @Test
  public void testPos() {
     assertEquals(0, reader.pos());
  }

  @Test
  public void testIsEmpty() {
     assertFalse(reader.isEmpty());
  }

  @Test
  public void testCurrent() {
     assertEquals('T', reader.current());
  }

  @Test
  public void testConsume() {
     assertEquals('T', reader.consume());
     assertEquals('h', reader.consume());
     assertEquals('i', reader.consume());
     assertEquals('s', reader.consume());
  }

  @Test
  public void testUnconsume() {
     reader.consume();
     reader.unconsume();
     assertEquals('T', reader.current());
  }

  @Test
  public void testAdvance() {
     reader.advance();
     assertEquals('h', reader.current());
  }

  @Test
  public void testMark() {
     reader.consume();
     reader.mark();
     assertEquals('h', reader.current());
  }

  @Test
  public void testRewindToMark() {
     reader.consume();
     reader.mark();
     reader.consume();
     reader.rewindToMark();
     assertEquals('h', reader.current());
  }

  @Test
  public void testConsumeAsString() {
     assertEquals("T", reader.consumeAsString());
     assertEquals("h", reader.consumeAsString());
     assertEquals("i", reader.consumeAsString());
     assertEquals("s", reader.consumeAsString());
  }

  @Test
  public void testConsumeTo() {
     assertEquals("This", reader.consumeTo(' '));
     assertEquals(" is a", reader.consumeTo(' '));
     assertEquals(" test", reader.consumeTo(' '));
     assertEquals(" string.", reader.consumeTo(' '));
  }

  @Test
  public void testConsumeToAny() {
     assertEquals("This", reader.consumeToAny(' ', '.'));
     assertEquals(" is", reader.consumeToAny(' ', '.'));
     assertEquals(" a", reader.consumeToAny(' ', '.'));
     assertEquals(" test", reader.consumeToAny(' ', '.'));
     assertEquals(" string", reader.consumeToAny(' ', '.'));
     assertEquals(".", reader.consumeToAny(' ', '.'));
  }

  @Test
  public void testConsumeToEnd() {
     assertEquals("This is a test string.", reader.consumeToEnd());
     assertEquals("", reader.consumeToEnd());
  }

  @Test
  public void testConsumeLetterSequence() {
     assertEquals("Th", reader.consumeLetterSequence());
     assertEquals("is", reader.consumeLetterSequence());
     assertEquals("is", reader.consumeLetterSequence());
     assertEquals("a", reader.consumeLetterSequence());
     assertEquals("test", reader.consumeLetterSequence());
     assertEquals("string", reader.consumeLetterSequence());
     assertEquals("", reader.consumeLetterSequence());
  }

  @Test
  public void testConsumeHexSequence() {
     assertEquals("", reader.consumeHexSequence());
     assertEquals("", reader.consumeHexSequence());
     assertEquals("", reader.consumeHexSequence());
     assertEquals("", reader.consumeHexSequence());
  }

  @Test
  public void testConsumeDigitSequence() {
     assertEquals("", reader.consumeDigitSequence());
     assertEquals("", reader.consumeDigitSequence());
     assertEquals("", reader.consumeDigitSequence());
     assertEquals("", reader.consumeDigitSequence());
  }

  @Test
  public void testMatches() {
     assertTrue(reader.matches('T'));
     assertFalse(reader.matches('t'));
     assertFalse(reader.matches(' '));
  }

  @Test
  public void testMatchesWithSeq() {
     assertTrue(reader.matches("This"));
     assertFalse(reader.matches("this"));
     assertFalse(reader.matches(" "));
  }

  @Test
  public void testMatchesIgnoreCase() {
     assertTrue(reader.matchesIgnoreCase("This"));
     assertTrue(reader.matchesIgnoreCase("this"));
     assertFalse(reader.matchesIgnoreCase(" "));
  }

  @Test
  public void testMatchesAny() {
     assertTrue(reader.matchesAny('T', 't'));
     assertFalse(reader.matchesAny(' '));
  }

  @Test
  public void testMatchesLetter() {
     assertTrue(reader.matchesLetter());
     reader.consume();
     assertTrue(reader.matchesLetter());
     reader.consume();
     assertFalse(reader.matchesLetter());
  }

  @Test
  public void testMatchesDigit() {
     assertFalse(reader.matchesDigit());
     reader.consume();
     assertFalse(reader.matchesDigit());
     reader.consume();
     assertFalse(reader.matchesDigit());
  }

  @Test
  public void testMatchConsume() {
     assertTrue(reader.matchConsume("This"));
     assertFalse(reader.matchConsume("this"));
  }

  @Test
  public void testMatchConsumeIgnoreCase() {
     assertTrue(reader.matchConsumeIgnoreCase("This"));
     assertTrue(reader.matchConsumeIgnoreCase("this"));
  }

  @Test
  public void testContainsIgnoreCase() {
     assertTrue(reader.containsIgnoreCase("test"));
     assertFalse(reader.containsIgnoreCase("TEST"));
  }

  @Test
  public void testToString() {
     assertEquals("This is a test string.", reader.toString());
     reader.consume();
     assertEquals("his is a test string.", reader.toString());
  }
}