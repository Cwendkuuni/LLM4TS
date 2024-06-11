package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

  @Test
  public void testPos() {
    CharacterReader reader = new CharacterReader("Test String");
    assertEquals(0, reader.pos());
  }

  @Test
  public void testIsEmpty() {
    CharacterReader reader = new CharacterReader("");
    assertTrue(reader.isEmpty());
  }

  @Test
  public void testCurrent() {
    CharacterReader reader = new CharacterReader("Test String");
    assertEquals('T', reader.current());
  }

  @Test
  public void testConsume() {
    CharacterReader reader = new CharacterReader("Test String");
    assertEquals('T', reader.consume());
    assertEquals('e', reader.consume());
  }

  @Test
  public void testUnconsume() {
    CharacterReader reader = new CharacterReader("Test String");
    reader.consume();
    reader.unconsume();
    assertEquals('T', reader.current());
  }

  @Test
  public void testAdvance() {
    CharacterReader reader = new CharacterReader("Test String");
    reader.advance();
    assertEquals('e', reader.current());
  }

  @Test
  public void testMark() {
    CharacterReader reader = new CharacterReader("Test String");
    reader.advance();
    reader.mark();
    reader.advance();
    assertEquals('s', reader.current());
    reader.rewindToMark();
    assertEquals('e', reader.current());
  }

  @Test
  public void testConsumeAsString() {
    CharacterReader reader = new CharacterReader("Test");
    assertEquals("T", reader.consumeAsString());
  }

  @Test
  public void testConsumeTo() {
    CharacterReader reader = new CharacterReader("Test String");
    assertEquals("Test ", reader.consumeTo(' '));
    assertEquals("S", reader.consumeTo(' '));
  }

  @Test
  public void testConsumeToAny() {
    CharacterReader reader = new CharacterReader("Test String");
    assertEquals("Test ", reader.consumeToAny(' ', 'g'));
    assertEquals("Strin", reader.consumeToAny(' ', 'g'));
  }

  @Test
  public void testConsumeToEnd() {
    CharacterReader reader = new CharacterReader("Test String");
    assertEquals("Test String", reader.consumeToEnd());
  }

  @Test
  public void testConsumeLetterSequence() {
    CharacterReader reader = new CharacterReader("Test String");
    assertEquals("Test", reader.consumeLetterSequence());
  }

  @Test
  public void testConsumeHexSequence() {
    CharacterReader reader = new CharacterReader("ABC123");
    assertEquals("ABC123", reader.consumeHexSequence());
  }

  @Test
  public void testConsumeDigitSequence() {
    CharacterReader reader = new CharacterReader("12345");
    assertEquals("12345", reader.consumeDigitSequence());
  }

  @Test
  public void testMatches() {
    CharacterReader reader = new CharacterReader("Test String");
    assertTrue(reader.matches('T'));
    assertFalse(reader.matches('t'));
  }

  @Test
  public void testMatchesIgnoreCase() {
    CharacterReader reader = new CharacterReader("Test String");
    assertTrue(reader.matchesIgnoreCase("t"));
    assertFalse(reader.matchesIgnoreCase("x"));
  }

  @Test
  public void testMatchesAny() {
    CharacterReader reader = new CharacterReader("Test String");
    assertTrue(reader.matchesAny('T', 'e'));
    assertFalse(reader.matchesAny('x', 'y'));
  }

  @Test
  public void testMatchesLetter() {
    CharacterReader reader = new CharacterReader("Test String");
    assertTrue(reader.matchesLetter());
    assertFalse(reader.matchesLetter());
  }

  @Test
  public void testMatchesDigit() {
    CharacterReader reader = new CharacterReader("Test String");
    assertFalse(reader.matchesDigit());
    reader.advance();
    assertFalse(reader.matchesDigit());
    reader.advance();
    assertFalse(reader.matchesDigit());
    reader.advance();
    assertFalse(reader.matchesDigit());
    reader.advance();
    assertFalse(reader.matchesDigit());
    reader.advance();
    assertFalse(reader.matchesDigit());
    reader.advance();
    assertTrue(reader.matchesDigit());
  }

  @Test
  public void testMatchConsume() {
    CharacterReader reader = new CharacterReader("Test String");
    assertTrue(reader.matchConsume("Test"));
    assertFalse(reader.matchConsume("Test"));
  }

  @Test
  public void testMatchConsumeIgnoreCase() {
    CharacterReader reader = new CharacterReader("Test String");
    assertTrue(reader.matchConsumeIgnoreCase("test"));
    assertFalse(reader.matchConsumeIgnoreCase("test"));
  }

  @Test
  public void testContainsIgnoreCase() {
    CharacterReader reader = new CharacterReader("Test String");
    assertTrue(reader.containsIgnoreCase("test"));
    assertTrue(reader.containsIgnoreCase("string"));
    assertFalse(reader.containsIgnoreCase("test2"));
  }

  @Test
  public void testToString() {
    CharacterReader reader = new CharacterReader("Test String");
    assertEquals("Test String", reader.toString());
  }

}