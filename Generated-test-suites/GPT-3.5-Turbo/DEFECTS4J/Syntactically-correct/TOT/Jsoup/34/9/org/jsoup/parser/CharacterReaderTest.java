import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

  @Test
  public void testPos() {
    CharacterReader reader = new CharacterReader("Test");
    assertEquals(0, reader.pos());
  }

  @Test
  public void testIsEmpty() {
    CharacterReader reader = new CharacterReader("Test");
    assertFalse(reader.isEmpty());
  }

  @Test
  public void testCurrent() {
    CharacterReader reader = new CharacterReader("Test");
    assertEquals('T', reader.current());
  }

  @Test
  public void testConsume() {
    CharacterReader reader = new CharacterReader("Test");
    assertEquals('T', reader.consume());
    assertEquals('e', reader.consume());
  }

  @Test
  public void testUnconsume() {
    CharacterReader reader = new CharacterReader("Test");
    reader.consume();
    reader.unconsume();
    assertEquals('T', reader.consume());
  }

  @Test
  public void testAdvance() {
    CharacterReader reader = new CharacterReader("Test");
    assertEquals('e', reader.advance());
  }

  @Test
  public void testMarkAndRewindToMark() {
    CharacterReader reader = new CharacterReader("Test");
    reader.mark();
    reader.advance();
    reader.rewindToMark();
    assertEquals('T', reader.current());
  }

  @Test
  public void testConsumeAsString() {
    CharacterReader reader = new CharacterReader("Test");
    assertEquals("T", reader.consumeAsString());
  }

  @Test
  public void testNextIndexOf_char() {
    CharacterReader reader = new CharacterReader("Test");
    assertEquals(1, reader.nextIndexOf('e'));
  }

  @Test
  public void testNextIndexOf_char_notFound() {
    CharacterReader reader = new CharacterReader("Test");
    assertEquals(-1, reader.nextIndexOf('z'));
  }

  @Test
  public void testNextIndexOf_CharSequence() {
    CharacterReader reader = new CharacterReader("Test");
    assertEquals(1, reader.nextIndexOf("es"));
  }

  @Test
  public void testNextIndexOf_CharSequence_notFound() {
    CharacterReader reader = new CharacterReader("Test");
    assertEquals(-1, reader.nextIndexOf("xy"));
  }

  @Test
  public void testConsumeTo_char() {
    CharacterReader reader = new CharacterReader("Test");
    assertEquals("", reader.consumeTo('T'));
    assertEquals("T", reader.consumeTo('S'));
  }

  @Test
  public void testConsumeTo_String() {
    CharacterReader reader = new CharacterReader("Test");
    assertEquals("", reader.consumeTo("Test"));
    assertEquals("Test", reader.consumeTo("x"));
  }

  @Test
  public void testConsumeToAny() {
    CharacterReader reader = new CharacterReader("Test");
    assertEquals("", reader.consumeToAny('T', 'e'));
    assertEquals("Test", reader.consumeToAny('x'));
  }

  @Test
  public void testConsumeToEnd() {
    CharacterReader reader = new CharacterReader("Test");
    assertEquals("Test", reader.consumeToEnd());
  }

  @Test
  public void testConsumeLetterSequence() {
    CharacterReader reader = new CharacterReader("Test");
    assertEquals("Test", reader.consumeLetterSequence());
  }

  @Test
  public void testConsumeLetterThenDigitSequence() {
    CharacterReader reader = new CharacterReader("Test123");
    assertEquals("Test", reader.consumeLetterThenDigitSequence());
  }

  @Test
  public void testConsumeHexSequence() {
    CharacterReader reader = new CharacterReader("AEFF");
    assertEquals("AEFF", reader.consumeHexSequence());
  }

  @Test
  public void testConsumeDigitSequence() {
    CharacterReader reader = new CharacterReader("123");
    assertEquals("123", reader.consumeDigitSequence());
  }

  @Test
  public void testMatches_char_true() {
    CharacterReader reader = new CharacterReader("Test");
    assertTrue(reader.matches('T'));
  }

  @Test
  public void testMatches_char_false() {
    CharacterReader reader = new CharacterReader("Test");
    assertFalse(reader.matches('S'));
  }

  @Test
  public void testMatches_String_true() {
    CharacterReader reader = new CharacterReader("Test");
    assertTrue(reader.matches("Test"));
  }

  @Test
  public void testMatches_String_false() {
    CharacterReader reader = new CharacterReader("Test");
    assertFalse(reader.matches("x"));
  }

  @Test
  public void testMatchesIgnoreCase_String_true() {
    CharacterReader reader = new CharacterReader("Test");
    assertTrue(reader.matchesIgnoreCase("test"));
  }

  @Test
  public void testMatchesIgnoreCase_String_false() {
    CharacterReader reader = new CharacterReader("Test");
    assertFalse(reader.matchesIgnoreCase("x"));
  }

  @Test
  public void testMatchesAny_true() {
    CharacterReader reader = new CharacterReader("Test");
    assertTrue(reader.matchesAny('x', 'T'));
  }

  @Test
  public void testMatchesAny_false() {
    CharacterReader reader = new CharacterReader("Test");
    assertFalse(reader.matchesAny('x', 'S'));
  }

  @Test
  public void testMatchesLetter_true() {
    CharacterReader reader = new CharacterReader("Test");
    assertTrue(reader.matchesLetter());
  }

  @Test
  public void testMatchesLetter_false() {
    CharacterReader reader = new CharacterReader("123");
    assertFalse(reader.matchesLetter());
  }

  @Test
  public void testMatchesDigit_true() {
    CharacterReader reader = new CharacterReader("123");
    assertTrue(reader.matchesDigit());
  }

  @Test
  public void testMatchesDigit_false() {
    CharacterReader reader = new CharacterReader("Test");
    assertFalse(reader.matchesDigit());
  }

  @Test
  public void testMatchConsume_String_true() {
    CharacterReader reader = new CharacterReader("Test");
    assertTrue(reader.matchConsume("Test"));
    assertEquals('T', reader.current());
  }

  @Test
  public void testMatchConsume_String_false() {
    CharacterReader reader = new CharacterReader("Test");
    assertFalse(reader.matchConsume("x"));
    assertEquals('T', reader.current());
  }

  @Test
  public void testMatchConsumeIgnoreCase_String_true() {
    CharacterReader reader = new CharacterReader("Test");
    assertTrue(reader.matchConsumeIgnoreCase("test"));
    assertEquals('T', reader.current());
  }

  @Test
  public void testMatchConsumeIgnoreCase_String_false() {
    CharacterReader reader = new CharacterReader("Test");
    assertFalse(reader.matchConsumeIgnoreCase("x"));
    assertEquals('T', reader.current());
  }

  @Test
  public void testContainsIgnoreCase_true() {
    CharacterReader reader = new CharacterReader("Test");
    assertTrue(reader.containsIgnoreCase("TeSt"));
  }

  @Test
  public void testContainsIgnoreCase_false() {
    CharacterReader reader = new CharacterReader("Test");
    assertFalse(reader.containsIgnoreCase("x"));
  }

  @Test
  public void testToString() {
    CharacterReader reader = new CharacterReader("Test");
    assertEquals("Test", reader.toString());
  }
}