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
    CharacterReader reader1 = new CharacterReader("");
    assertTrue(reader1.isEmpty());
    
    CharacterReader reader2 = new CharacterReader("test");
    assertFalse(reader2.isEmpty());
  }
  
  @Test
  public void testCurrent() {
    CharacterReader reader1 = new CharacterReader("test");
    assertEquals('t', reader1.current());
    
    CharacterReader reader2 = new CharacterReader("");
    assertEquals(CharacterReader.EOF, reader2.current());
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
    reader.consume();
    reader.unconsume();
    assertEquals('e', reader.consume());
  }
  
  @Test
  public void testAdvance() {
    CharacterReader reader = new CharacterReader("test");
    reader.advance();
    assertEquals('e', reader.current());
  }
  
  @Test
  public void testMarkAndRewindToMark() {
    CharacterReader reader = new CharacterReader("test");
    reader.mark();
    reader.consume();
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
    assertEquals("st", reader.consumeToEnd());
    assertEquals("", reader.consumeTo('x'));
  }
  
  @Test
  public void testConsumeToAny() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("t", reader.consumeToAny('e', 's', 'x'));
    assertEquals("e", reader.consumeToAny('s', 'x'));
    assertEquals("s", reader.consumeToAny('x'));
    assertEquals("", reader.consumeToAny('x'));
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
  public void testConsumeHexSequence() {
    CharacterReader reader = new CharacterReader("123ABC");
    assertEquals("123ABC", reader.consumeHexSequence());
  }
  
  @Test
  public void testConsumeDigitSequence() {
    CharacterReader reader = new CharacterReader("123ABC");
    assertEquals("123", reader.consumeDigitSequence());
  }
  
  @Test
  public void testMatches() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matches('t'));
    assertFalse(reader.matches('e'));
    
    assertFalse(reader.matches(""));
    assertFalse(reader.matchesIgnoreCase("e"));
  }
  
  @Test
  public void testMatchesIgnoreCase() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchesIgnoreCase("T"));
    assertTrue(reader.matchesIgnoreCase("Te"));
    
    assertFalse(reader.matchesIgnoreCase(""));
    assertFalse(reader.matchesIgnoreCase("x"));
  }
  
  @Test
  public void testMatchesAny() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchesAny('t', 'e'));
    assertFalse(reader.matchesAny('x'));
  }
  
  @Test
  public void testMatchesLetter() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchesLetter());
    
    assertFalse(new CharacterReader("").matchesLetter());
    assertFalse(new CharacterReader("123").matchesLetter());
  }
  
  @Test
  public void testMatchesDigit() {
    CharacterReader reader = new CharacterReader("123");
    assertTrue(reader.matchesDigit());
    
    assertFalse(new CharacterReader("").matchesDigit());
    assertFalse(new CharacterReader("abc").matchesDigit());
  }
  
  @Test
  public void testMatchConsume() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchConsume("test"));
    assertFalse(reader.matchConsume("notmatching"));
  }
  
  @Test
  public void testMatchConsumeIgnoreCase() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchConsumeIgnoreCase("Test"));
    assertFalse(reader.matchConsumeIgnoreCase("NotMatching"));
  }
  
  @Test
  public void testContainsIgnoreCase() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.containsIgnoreCase("te"));
    
    assertFalse(reader.containsIgnoreCase("notpresent"));
  }
  
  @Test
  public void testToString() {
    CharacterReader reader = new CharacterReader("test");
    reader.consume();
    assertEquals("est", reader.toString());
  }
  
}
