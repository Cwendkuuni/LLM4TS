package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

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
  }
  
  @Test
  public void testUnconsume() {
    CharacterReader reader = new CharacterReader("test");
    reader.consume();
    reader.unconsume();
    assertEquals('t', reader.current());
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
    reader.advance();
    reader.mark();
    assertEquals(1, reader.pos());
    reader.advance();
    assertEquals(2, reader.pos());
    reader.rewindToMark();
    assertEquals(1, reader.pos());
  }
  
  @Test
  public void testConsumeAsString() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("t", reader.consumeAsString());
  }
  
  @Test
  public void testNextIndexOf() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals(2, reader.nextIndexOf('s'));
  }
  
  @Test
  public void testNextIndexOfCharSequence() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals(2, reader.nextIndexOf("st"));
  }
  
  @Test
  public void testConsumeTo() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("te", reader.consumeTo('s'));
  }
  
  @Test
  public void testConsumeToAny() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("t", reader.consumeToAny('s'));
  }
  
  @Test
  public void testConsumeToAnySorted() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("t", reader.consumeToAnySorted('s'));
  }
  
  @Test
  public void testConsumeData() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("test", reader.consumeData());
  }
  
  @Test
  public void testConsumeTagName() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("test", reader.consumeTagName());
  }
  
  @Test
  public void testConsumeToEnd() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("test", reader.consumeToEnd());
  }
  
  @Test
  public void testConsumeLetterSequence() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("test", reader.consumeLetterSequence());
  }
  
  @Test
  public void testConsumeLetterThenDigitSequence() {
    CharacterReader reader = new CharacterReader("test123");
    assertEquals("test123", reader.consumeLetterThenDigitSequence());
  }
  
  @Test
  public void testConsumeHexSequence() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("test", reader.consumeHexSequence());
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
  }
  
  @Test
  public void testMatchesString() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matches("test"));
  }
  
  @Test
  public void testMatchesIgnoreCase() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchesIgnoreCase("TEST"));
  }
  
  @Test
  public void testMatchesAny() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchesAny('t'));
  }
  
  @Test
  public void testMatchesAnySorted() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchesAnySorted('t'));
  }
  
  @Test
  public void testMatchesLetter() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchesLetter());
  }
  
  @Test
  public void testMatchesDigit() {
    CharacterReader reader = new CharacterReader("123");
    assertTrue(reader.matchesDigit());
  }
  
  @Test
  public void testMatchConsume() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchConsume("te"));
  }
  
  @Test
  public void testMatchConsumeIgnoreCase() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchConsumeIgnoreCase("TE"));
  }
  
  @Test
  public void testContainsIgnoreCase() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.containsIgnoreCase("TEST"));
  }
  
  @Test
  public void testToString() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("test", reader.toString());
  }
}