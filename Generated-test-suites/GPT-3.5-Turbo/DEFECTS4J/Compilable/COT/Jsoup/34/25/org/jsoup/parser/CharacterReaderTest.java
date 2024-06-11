package org.jsoup.parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

  @Test
  public void testPos() {
    CharacterReader reader = new CharacterReader("abc");
    assertEquals(0, reader.pos());
    reader.consume();
    assertEquals(1, reader.pos());
  }

  @Test
  public void testIsEmpty() {
    CharacterReader reader = new CharacterReader("");
    assertTrue(reader.isEmpty());
    reader = new CharacterReader("abc");
    assertFalse(reader.isEmpty());
  }

  @Test
  public void testCurrent() {
    CharacterReader reader = new CharacterReader("abc");
    assertEquals('a', reader.current());
    reader.consume();
    assertEquals('b', reader.current());
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
    reader.consume();
    assertEquals('b', reader.current());
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
    reader.consume();
    assertEquals('b', reader.current());
    reader.rewindToMark();
    assertEquals('a', reader.current());
  }

  @Test
  public void testConsumeAsString() {
    CharacterReader reader = new CharacterReader("abc");
    assertEquals("a", reader.consumeAsString());
    assertEquals("b", reader.consumeAsString());
  }

  @Test
  public void testNextIndexOf() {
    CharacterReader reader = new CharacterReader("abc");
    assertEquals(1, reader.nextIndexOf('b'));
    assertEquals(2, reader.nextIndexOf('c'));
    assertEquals(-1, reader.nextIndexOf('d'));
  }

  @Test
  public void testNextIndexOfCharSequence() {
    CharacterReader reader = new CharacterReader("abc");
    assertEquals(1, reader.nextIndexOf("bc"));
    assertEquals(-1, reader.nextIndexOf("cd"));
  }

  @Test
  public void testConsumeTo() {
    CharacterReader reader = new CharacterReader("abc");
    assertEquals("ab", reader.consumeTo('c'));
    assertEquals("c", reader.consumeTo('d'));
  }

  @Test
  public void testConsumeToAny() {
    CharacterReader reader = new CharacterReader("abc");
    assertEquals("a", reader.consumeToAny('b', 'c'));
    assertEquals("b", reader.consumeToAny('d'));
  }

  @Test
  public void testConsumeToEnd() {
    CharacterReader reader = new CharacterReader("abc");
    assertEquals("abc", reader.consumeToEnd());
  }

  @Test
  public void testConsumeLetterSequence() {
    CharacterReader reader = new CharacterReader("abc123");
    assertEquals("abc", reader.consumeLetterSequence());
    assertEquals("123", reader.consumeLetterSequence());
  }

  @Test
  public void testConsumeLetterThenDigitSequence() {
    CharacterReader reader = new CharacterReader("abc123");
    assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    reader.unconsume();
    reader.unconsume();
    assertEquals("abc", reader.consumeLetterThenDigitSequence());
  }

  @Test
  public void testConsumeHexSequence() {
    CharacterReader reader = new CharacterReader("abc123");
    assertEquals("abc", reader.consumeHexSequence());
    assertEquals("123", reader.consumeHexSequence());
  }

  @Test
  public void testConsumeDigitSequence() {
    CharacterReader reader = new CharacterReader("abc123");
    assertEquals("123", reader.consumeDigitSequence());
    assertEquals("abc", reader.consumeDigitSequence());
  }

  @Test
  public void testMatches() {
    CharacterReader reader = new CharacterReader("abc");
    assertTrue(reader.matches('a'));
    assertFalse(reader.matches('b'));
  }

  @Test
  public void testMatchesIgnoreCase() {
    CharacterReader reader = new CharacterReader("abc");
    assertTrue(reader.matchesIgnoreCase("A"));
    assertFalse(reader.matchesIgnoreCase("B"));
  }

  @Test
  public void testMatchesAny() {
    CharacterReader reader = new CharacterReader("abc");
    assertTrue(reader.matchesAny('a', 'b'));
    assertFalse(reader.matchesAny('c', 'd'));
  }

  @Test
  public void testMatchesLetter() {
    CharacterReader reader = new CharacterReader("abc123");
    assertTrue(reader.matchesLetter());
    reader.consumeLetterSequence();
    assertFalse(reader.matchesLetter());
  }

  @Test
  public void testMatchesDigit() {
    CharacterReader reader = new CharacterReader("abc123");
    assertFalse(reader.matchesDigit());
    reader.consumeLetterSequence();
    assertTrue(reader.matchesDigit());
  }

  @Test
  public void testMatchConsume() {
    CharacterReader reader = new CharacterReader("abc");
    assertTrue(reader.matchConsume("ab"));
    assertFalse(reader.matchConsume("ab"));
  }

  @Test
  public void testMatchConsumeIgnoreCase() {
    CharacterReader reader = new CharacterReader("abc");
    assertTrue(reader.matchConsumeIgnoreCase("AB"));
    assertFalse(reader.matchConsumeIgnoreCase("AB"));
  }

  @Test
  public void testContainsIgnoreCase() {
    CharacterReader reader = new CharacterReader("abc");
    assertTrue(reader.containsIgnoreCase("a"));
    assertFalse(reader.containsIgnoreCase("d"));
  }

  @Test
  public void testToString() {
    CharacterReader reader = new CharacterReader("abc");
    assertEquals("abc", reader.toString());
  }
}