
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
    CharacterReader reader = new CharacterReader("test");
    assertFalse(reader.isEmpty());
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
    assertEquals(0, reader.mark);
  }

  @Test
  public void testRewindToMark() {
    CharacterReader reader = new CharacterReader("test");
    reader.advance();
    reader.mark();
    reader.advance();
    reader.rewindToMark();
    assertEquals('e', reader.current());
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
    assertEquals(-1, reader.nextIndexOf('x'));
  }

  @Test
  public void testNextIndexOfCharSequence() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals(1, reader.nextIndexOf("es"));
    assertEquals(-1, reader.nextIndexOf("ex"));
  }

  @Test
  public void testConsumeTo() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("t", reader.consumeTo('e'));
    reader.consume();
    assertEquals("st", reader.consumeTo('x'));
  }

  @Test
  public void testConsumeToAny() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("t", reader.consumeToAny('x', 'e'));
    reader.unconsume();
    assertEquals("te", reader.consumeToAny('x', 's'));
  }

  @Test
  public void testConsumeToAnySorted() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("t", reader.consumeToAnySorted('e', 'x'));
    reader.unconsume();
    assertEquals("ts", reader.consumeToAnySorted('x', 's'));
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
    CharacterReader reader = new CharacterReader("test1");
    assertEquals("test1", reader.consumeLetterThenDigitSequence());
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
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matches('t'));
    assertFalse(reader.matches('s'));
  }

  @Test
  public void testMatchesString() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matches("tes"));
    assertFalse(reader.matches("xyz"));
  }

  @Test
  public void testMatchesIgnoreCase() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchesIgnoreCase("TeS"));
    assertFalse(reader.matchesIgnoreCase("XyZ"));
  }

  @Test
  public void testMatchesAny() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchesAny('t', 's'));
    assertFalse(reader.matchesAny('a', 'b'));
  }

  @Test
  public void testMatchesAnySorted() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchesAnySorted('s', 't'));
    assertFalse(reader.matchesAnySorted('x', 'y'));
  }

  @Test
  public void testMatchesLetter() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchesLetter());
    reader.unconsume();
    assertFalse(reader.matchesLetter());
  }

  @Test
  public void testMatchesDigit() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchesDigit());
    reader.unconsume();
    assertFalse(reader.matchesDigit());
  }

  @Test
  public void testMatchConsume() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchConsume("te"));
    assertFalse(reader.matchConsume("xy"));
  }

  @Test
  public void testMatchConsumeIgnoreCase() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.matchConsumeIgnoreCase("Te"));
    assertFalse(reader.matchConsumeIgnoreCase("Xy"));
  }

  @Test
  public void testContainsIgnoreCase() {
    CharacterReader reader = new CharacterReader("test");
    assertTrue(reader.containsIgnoreCase("ES"));
    assertFalse(reader.containsIgnoreCase("XY"));
  }

  @Test
  public void testToString() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("test", reader.toString());
  }
}
