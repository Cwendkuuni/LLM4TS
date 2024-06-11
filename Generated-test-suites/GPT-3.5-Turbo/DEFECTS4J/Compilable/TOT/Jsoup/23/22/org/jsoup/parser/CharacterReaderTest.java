package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

  @Test
  public void testPos() {
    CharacterReader reader = new CharacterReader("abcdef");
    assertEquals(0, reader.pos());
    reader.consume();
    assertEquals(1, reader.pos());
  }

  @Test
  public void testIsEmpty() {
    CharacterReader reader = new CharacterReader("");
    assertTrue(reader.isEmpty());
    reader.consume();
    assertTrue(reader.isEmpty());
  }

  @Test
  public void testCurrent() {
    CharacterReader reader = new CharacterReader("abc");
    assertEquals('a', reader.current());
    reader.consume();
    assertEquals('b', reader.current());
    reader.consume();
    assertEquals('c', reader.current());
    reader.consume();
    assertEquals(CharacterReader.EOF, reader.current());
  }

  @Test
  public void testConsume() {
    CharacterReader reader = new CharacterReader("abc");
    assertEquals('a', reader.consume());
    assertEquals('b', reader.consume());
    assertEquals('c', reader.consume());
    assertEquals(CharacterReader.EOF, reader.consume());
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
    reader.advance();
    reader.mark();
    reader.advance();
    assertEquals('c', reader.current());
  }

  @Test
  public void testRewindToMark() {
    CharacterReader reader = new CharacterReader("abc");
    reader.advance();
    reader.mark();
    reader.advance();
    reader.rewindToMark();
    assertEquals('b', reader.current());
  }

  @Test
  public void testConsumeAsString() {
    CharacterReader reader = new CharacterReader("abc");
    assertEquals("a", reader.consumeAsString());
    assertEquals("b", reader.consumeAsString());
    assertEquals("c", reader.consumeAsString());
  }

  @Test
  public void testConsumeTo() {
    CharacterReader reader = new CharacterReader("abc");
    assertEquals("ab", reader.consumeTo('c'));
    assertEquals("", reader.consumeTo('d'));
  }

  @Test
  public void testConsumeToAny() {
    CharacterReader reader = new CharacterReader("abc");
    assertEquals("a", reader.consumeToAny('b', 'c'));
    assertEquals("b", reader.consumeToAny('c'));
    assertEquals("", reader.consumeToAny('d'));
  }

  @Test
  public void testConsumeToEnd() {
    CharacterReader reader = new CharacterReader("abc");
    assertEquals("abc", reader.consumeToEnd());
    assertEquals("", reader.consumeToEnd());
  }

  @Test
  public void testConsumeLetterSequence() {
    CharacterReader reader = new CharacterReader("abc123");
    assertEquals("abc", reader.consumeLetterSequence());
    assertEquals("", reader.consumeLetterSequence());
  }

  @Test
  public void testConsumeHexSequence() {
    CharacterReader reader = new CharacterReader("abc123");
    assertEquals("", reader.consumeHexSequence());
    reader.advance();
    assertEquals("", reader.consumeHexSequence());
    reader.advance();
    assertEquals("", reader.consumeHexSequence());
    reader.advance();
    assertEquals("123", reader.consumeHexSequence());
  }

  @Test
  public void testConsumeDigitSequence() {
    CharacterReader reader = new CharacterReader("123abc");
    assertEquals("123", reader.consumeDigitSequence());
    assertEquals("", reader.consumeDigitSequence());
  }

  @Test
  public void testMatches() {
    CharacterReader reader = new CharacterReader("abc");
    assertTrue(reader.matches('a'));
    assertFalse(reader.matches('b'));
  }

  @Test
  public void testMatchesIgnoreCase() {
    CharacterReader reader = new CharacterReader("Abc");
    assertTrue(reader.matchesIgnoreCase("aBc"));
    assertFalse(reader.matchesIgnoreCase("bcd"));
  }

  @Test
  public void testMatchesAny() {
    CharacterReader reader = new CharacterReader("abc");
    assertTrue(reader.matchesAny('a', 'b'));
    assertFalse(reader.matchesAny('d', 'e'));
  }

  @Test
  public void testMatchesLetter() {
    CharacterReader reader = new CharacterReader("abc");
    assertTrue(reader.matchesLetter());
    reader.advance();
    assertFalse(reader.matchesLetter());
  }

  @Test
  public void testMatchesDigit() {
    CharacterReader reader = new CharacterReader("123");
    assertTrue(reader.matchesDigit());
    reader.advance();
    assertFalse(reader.matchesDigit());
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
    assertFalse(reader.matchConsumeIgnoreCase("cd"));
  }

  @Test
  public void testContainsIgnoreCase() {
    CharacterReader reader = new CharacterReader("abc");
    assertTrue(reader.containsIgnoreCase("b"));
    assertFalse(reader.containsIgnoreCase("d"));
  }
}