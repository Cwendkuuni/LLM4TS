package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

  @Test
  public void testPos() {
    CharacterReader reader = new CharacterReader("hello");
    assertEquals(0, reader.pos());
  }
  
  @Test
  public void testIsEmpty() {
    CharacterReader reader = new CharacterReader("");
    assertTrue(reader.isEmpty());
  }
  
  @Test
  public void testCurrent() {
    CharacterReader reader = new CharacterReader("hello");
    assertEquals('h', reader.current());
  }
  
  @Test
  public void testConsume() {
    CharacterReader reader = new CharacterReader("hello");
    assertEquals('h', reader.consume());
    assertEquals('e', reader.consume());
  }
  
  @Test
  public void testUnconsume() {
    CharacterReader reader = new CharacterReader("hello");
    assertEquals('h', reader.consume());
    reader.unconsume();
    assertEquals('h', reader.current());
  }
  
  @Test
  public void testAdvance() {
    CharacterReader reader = new CharacterReader("hello");
    reader.advance();
    assertEquals('e', reader.current());
  }
  
  @Test
  public void testMark() {
    CharacterReader reader = new CharacterReader("hello");
    reader.mark();
    assertEquals(0, reader.pos());
  }
  
  @Test
  public void testRewindToMark() {
    CharacterReader reader = new CharacterReader("hello");
    reader.mark();
    reader.consume();
    reader.rewindToMark();
    assertEquals(0, reader.pos());
  }
  
  @Test
  public void testConsumeAsString() {
    CharacterReader reader = new CharacterReader("hello");
    assertEquals("h", reader.consumeAsString());
    assertEquals("e", reader.consumeAsString());
  }
  
  @Test
  public void testNextIndexOf() {
    CharacterReader reader = new CharacterReader("hello");
    assertEquals(1, reader.nextIndexOf('e'));
    assertEquals(-1, reader.nextIndexOf('z'));
  }
  
  @Test
  public void testNextIndexOfSeq() {
    CharacterReader reader = new CharacterReader("hello");
    assertEquals(1, reader.nextIndexOf("ell"));
    assertEquals(-1, reader.nextIndexOf("zoo"));
  }
  
  @Test
  public void testConsumeTo() {
    CharacterReader reader = new CharacterReader("hello");
    assertEquals("h", reader.consumeTo('e'));
    assertEquals("ell", reader.consumeTo('o'));
  }
  
  @Test
  public void testConsumeToSeq() {
    CharacterReader reader = new CharacterReader("hello");
    assertEquals("h", reader.consumeTo("e"));
    assertEquals("ell", reader.consumeTo("o"));
  }
  
  @Test
  public void testConsumeToAny() {
    CharacterReader reader = new CharacterReader("hello");
    assertEquals("", reader.consumeToAny('e', 'o'));
    assertEquals("h", reader.consumeToAny('o'));
  }
  
  @Test
  public void testConsumeToEnd() {
    CharacterReader reader = new CharacterReader("hello");
    assertEquals("hello", reader.consumeToEnd());
    assertTrue(reader.isEmpty());
  }
  
  @Test
  public void testConsumeLetterSequence() {
    CharacterReader reader = new CharacterReader("hello");
    assertEquals("hello", reader.consumeLetterSequence());
  }
  
  @Test
  public void testConsumeLetterThenDigitSequence() {
    CharacterReader reader = new CharacterReader("hello1");
    assertEquals("hello1", reader.consumeLetterThenDigitSequence());
  }
  
  @Test
  public void testConsumeHexSequence() {
    CharacterReader reader = new CharacterReader("123abc");
    assertEquals("123abc", reader.consumeHexSequence());
  }
  
  @Test
  public void testConsumeDigitSequence() {
    CharacterReader reader = new CharacterReader("123abc");
    assertEquals("123", reader.consumeDigitSequence());
  }
  
  @Test
  public void testMatches() {
    CharacterReader reader = new CharacterReader("hello");
    assertTrue(reader.matches('h'));
    assertFalse(reader.matches('e'));
  }
  
  @Test
  public void testMatchesSeq() {
    CharacterReader reader = new CharacterReader("hello");
    assertTrue(reader.matches("h"));
    assertFalse(reader.matches("e"));
  }
  
  @Test
  public void testMatchesIgnoreCase() {
    CharacterReader reader = new CharacterReader("hello");
    assertTrue(reader.matchesIgnoreCase("H"));
    assertFalse(reader.matchesIgnoreCase("E"));
  }
  
  @Test
  public void testMatchesAny() {
    CharacterReader reader = new CharacterReader("hello");
    assertTrue(reader.matchesAny('h', 'e'));
    assertFalse(reader.matchesAny('e'));
  }
  
  @Test
  public void testMatchesLetter() {
    CharacterReader reader = new CharacterReader("hello");
    assertTrue(reader.matchesLetter());
    assertFalse(reader.matchesLetter());
  }
  
  @Test
  public void testMatchesDigit() {
    CharacterReader reader = new CharacterReader("hello");
    assertFalse(reader.matchesDigit());
    reader.advance();
    assertTrue(reader.matchesDigit());
  }
  
  @Test
  public void testMatchConsume() {
    CharacterReader reader = new CharacterReader("hello");
    assertTrue(reader.matchConsume("hel"));
    assertFalse(reader.matchConsume("no"));
  }
  
  @Test
  public void testMatchConsumeIgnoreCase() {
    CharacterReader reader = new CharacterReader("hello");
    assertTrue(reader.matchConsumeIgnoreCase("HEL"));
    assertFalse(reader.matchConsumeIgnoreCase("NO"));
  }
  
  @Test
  public void testContainsIgnoreCase() {
    CharacterReader reader = new CharacterReader("hello");
    assertTrue(reader.containsIgnoreCase("EL"));
    assertFalse(reader.containsIgnoreCase("NO"));
  }
  
  @Test
  public void testToString() {
    CharacterReader reader = new CharacterReader("hello");
    assertEquals("hello", reader.toString());
  }
  
}