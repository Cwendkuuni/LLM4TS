package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Test;

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
    CharacterReader reader = new CharacterReader("abc");
    assertEquals('a', reader.current());
  }
  
  @Test
  public void testConsume() {
    CharacterReader reader = new CharacterReader("abc");
    assertEquals('a', reader.consume());
    assertEquals('b', reader.consume());
  }
  
  @Test
  public void testUnconsume() {
    CharacterReader reader = new CharacterReader("abc");
    reader.consume();
    reader.unconsume();
    assertEquals('a', reader.current());
  }
  
  @Test
  public void testAdvance() {
    CharacterReader reader = new CharacterReader("abc");
    reader.advance();
    assertEquals('b', reader.current());
  }
  
  @Test
  public void testMark() {
    CharacterReader reader = new CharacterReader("abc");
    reader.mark();
    reader.advance();
    assertEquals('b', reader.current());
  }
  
  @Test
  public void testRewindToMark() {
    CharacterReader reader = new CharacterReader("abc");
    reader.mark();
    reader.advance();
    reader.rewindToMark();
    assertEquals('a', reader.current());
  }
  
  @Test
  public void testConsumeAsString() {
    CharacterReader reader = new CharacterReader("abc");
    assertEquals("a", reader.consumeAsString());
  }
  
  @Test
  public void testConsumeTo() {
    CharacterReader reader = new CharacterReader("hello,world");
    assertEquals("hello", reader.consumeTo(','));
    assertEquals("world", reader.consumeTo(','));
  }
  
  @Test
  public void testConsumeToAny() {
    CharacterReader reader = new CharacterReader("abc123");
    assertEquals("abc", reader.consumeToAny('1', '2', '3', '4', '5'));
    assertEquals("123", reader.consumeToAny('6', '7', '8', '9', '0'));
  }
  
  @Test
  public void testConsumeToEnd() {
    CharacterReader reader = new CharacterReader("hello");
    assertEquals("hello", reader.consumeToEnd());
  }
  
  @Test
  public void testConsumeLetterSequence() {
    CharacterReader reader = new CharacterReader("abc123");
    assertEquals("abc", reader.consumeLetterSequence());
    assertEquals("123", reader.consumeDigitSequence());
  }
  
  @Test
  public void testConsumeHexSequence() {
    CharacterReader reader = new CharacterReader("a1A9");
    assertEquals("a1A9", reader.consumeHexSequence());
  }
  
  @Test
  public void testConsumeDigitSequence() {
    CharacterReader reader = new CharacterReader("123abc");
    assertEquals("123", reader.consumeDigitSequence());
    assertEquals("abc", reader.consumeLetterSequence());
  }
  
  @Test
  public void testMatches() {
    CharacterReader reader = new CharacterReader("abc");
    assertTrue(reader.matches('a'));
    assertFalse(reader.matches('b'));
  }
  
  @Test
  public void testMatchesIgnoreCase() {
    CharacterReader reader = new CharacterReader("AbC");
    assertTrue(reader.matchesIgnoreCase("abc"));
    assertFalse(reader.matchesIgnoreCase("def"));
  }
  
  @Test
  public void testMatchesAny() {
    CharacterReader reader = new CharacterReader("abc");
    assertTrue(reader.matchesAny('a', 'b'));
    assertFalse(reader.matchesAny('c', 'd'));
  }
  
  @Test
  public void testMatchesLetter() {
    CharacterReader reader = new CharacterReader("a1");
    assertTrue(reader.matchesLetter());
    assertFalse(reader.matchesDigit());
  }
  
  @Test
  public void testMatchesDigit() {
    CharacterReader reader = new CharacterReader("1a");
    assertTrue(reader.matchesDigit());
    assertFalse(reader.matchesLetter());
  }
  
  @Test
  public void testMatchConsume() {
    CharacterReader reader = new CharacterReader("abc");
    assertTrue(reader.matchConsume("ab"));
    assertFalse(reader.matchConsume("cd"));
  }
  
  @Test
  public void testMatchConsumeIgnoreCase() {
    CharacterReader reader = new CharacterReader("abc");
    assertTrue(reader.matchConsumeIgnoreCase("AB"));
    assertFalse(reader.matchConsumeIgnoreCase("CD"));
  }
  
  @Test
  public void testContainsIgnoreCase() {
    CharacterReader reader = new CharacterReader("abc");
    assertTrue(reader.containsIgnoreCase("AB"));
    assertFalse(reader.containsIgnoreCase("DE"));
  }
  
  @Test
  public void testToString() {
    CharacterReader reader = new CharacterReader("abc");
    reader.consume();
    assertEquals("bc", reader.toString());
  }
}