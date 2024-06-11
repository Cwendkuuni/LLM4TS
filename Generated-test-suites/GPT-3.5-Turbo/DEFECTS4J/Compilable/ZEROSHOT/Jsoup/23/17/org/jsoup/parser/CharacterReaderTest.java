package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {
  
  @Test
  public void testPos() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals(0, reader.pos());
  }
  
  @Test
  public void testIsEmpty() {
    CharacterReader reader = new CharacterReader("");
    assertTrue(reader.isEmpty());
  }
  
  @Test
  public void testCurrent() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals('t', reader.current());
  }
  
  @Test
  public void testConsume() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals('t', reader.consume());
    assertEquals('e', reader.consume());
    assertEquals('s', reader.consume());
    assertEquals('t', reader.consume());
    assertEquals(CharacterReader.EOF, reader.consume());
  }
  
  @Test
  public void testUnconsume() {
    CharacterReader reader = new CharacterReader("test");
    reader.consume();
    reader.unconsume();
    assertEquals('t', reader.consume());
  }
  
  @Test
  public void testAdvance() {
    CharacterReader reader = new CharacterReader("test");
    reader.advance();
    assertEquals('e', reader.current());
  }
  
  @Test
  public void testMark() {
    CharacterReader reader = new CharacterReader("test");
    reader.mark();
    reader.advance();
    assertEquals('e', reader.current());
  }
  
  @Test
  public void testRewindToMark() {
    CharacterReader reader = new CharacterReader("test");
    reader.mark();
    reader.advance();
    reader.rewindToMark();
    assertEquals('t', reader.current());
  }
  
  @Test
  public void testConsumeAsString() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("t", reader.consumeAsString());
  }
  
  @Test
  public void testConsumeTo() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("te", reader.consumeTo('s'));
  }
  
  @Test
  public void testConsumeToAny() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("t", reader.consumeToAny('e', 's'));
  }
  
  @Test
  public void testConsumeToEnd() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("test", reader.consumeToEnd());
  }
  
  @Test
  public void testConsumeLetterSequence() {
    CharacterReader reader = new CharacterReader("Test");
    assertEquals("Test", reader.consumeLetterSequence());
  }
  
  @Test
  public void testConsumeHexSequence() {
    CharacterReader reader = new CharacterReader("A1B2");
    assertEquals("A1B2", reader.consumeHexSequence());
  }
  
  @Test
  public void testConsumeDigitSequence() {
    CharacterReader reader = new CharacterReader("123");
    assertEquals("123", reader.consumeDigitSequence());
  }
  
  @Test
  public void testMatches() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matches('t'));
    assertFalse(reader.matches('s'));
  }
  
  @Test
  public void testMatchesIgnoreCase() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchesIgnoreCase("Test"));
    assertFalse(reader.matchesIgnoreCase("Test2"));
  }
  
  @Test
  public void testMatchesAny() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchesAny('t', 's'));
    assertFalse(reader.matchesAny('a', 'b'));
  }
  
  @Test
  public void testMatchesLetter() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchesLetter());
    assertFalse(reader.matchesLetter());
  }
  
  @Test
  public void testMatchesDigit() {
    CharacterReader reader = new CharacterReader("test");
    assertFalse(reader.matchesDigit());
    reader.consume();
    assertTrue(reader.matchesDigit());
  }
  
  @Test
  public void testMatchConsume() {
    CharacterReader reader = new CharacterReader("test");
    reader.matchConsume("t");
    assertFalse(reader.matchConsume("e"));
  }
  
  @Test
  public void testMatchConsumeIgnoreCase() {
    CharacterReader reader = new CharacterReader("test");
    reader.matchConsumeIgnoreCase("Te");
    assertFalse(reader.matchConsumeIgnoreCase("st"));
  }
  
  @Test
  public void testContainsIgnoreCase() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.containsIgnoreCase("Te"));
    assertFalse(reader.containsIgnoreCase("AB"));
  }
  
  @Test
  public void testToString() {
    CharacterReader reader = new CharacterReader("test");
    reader.consume();
    assertEquals("est", reader.toString());
  }
}