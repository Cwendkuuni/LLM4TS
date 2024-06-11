import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

  private CharacterReader characterReader;

  @Before
  public void setUp() {
    characterReader = new CharacterReader("test string");
  }

  @Test
  public void testPos() {
    assertEquals(0, characterReader.pos());
  }

  @Test
  public void testIsEmpty() {
    assertFalse(characterReader.isEmpty());
  }

  @Test
  public void testCurrent() {
    assertEquals('t', characterReader.current());
  }

  @Test
  public void testConsume() {
    char consumed = characterReader.consume();
    assertEquals('t', consumed);
    assertEquals(1, characterReader.pos());
  }

  @Test
  public void testUnconsume() {
    characterReader.unconsume();
    assertEquals(0, characterReader.pos());
  }

  @Test
  public void testAdvance() {
    characterReader.advance();
    assertEquals(1, characterReader.pos());
  }

  @Test
  public void testMark() {
    characterReader.mark();
    assertEquals(0, characterReader.mark);
  }

  @Test
  public void testRewindToMark() {
    characterReader.rewindToMark();
    assertEquals(0, characterReader.pos());
  }

  @Test
  public void testConsumeAsString() {
    String consumed = characterReader.consumeAsString();
    assertEquals("t", consumed);
    assertEquals(1, characterReader.pos());
  }

  @Test
  public void testNextIndexOf() {
    int offset = characterReader.nextIndexOf('s');
    assertEquals(2, offset);
  }

  @Test
  public void testNextIndexOf2() {
    int offset = characterReader.nextIndexOf("str");
    assertEquals(2, offset);
  }

  @Test
  public void testConsumeTo() {
    String consumed = characterReader.consumeTo('s');
    assertEquals("te", consumed);
    assertEquals(3, characterReader.pos());
  }

  @Test
  public void testConsumeTo2() {
    String consumed = characterReader.consumeTo("st");
    assertEquals("te", consumed);
    assertEquals(2, characterReader.pos());
  }

  @Test
  public void testConsumeToAny() {
    String consumed = characterReader.consumeToAny('s', ' ');
    assertEquals("te", consumed);
    assertEquals(2, characterReader.pos());
  }

  @Test
  public void testConsumeToEnd() {
    String consumed = characterReader.consumeToEnd();
    assertEquals("test string", consumed);
    assertEquals(11, characterReader.pos());
  }

  @Test
  public void testConsumeLetterSequence() {
    characterReader.consume();
    String consumed = characterReader.consumeLetterSequence();
    assertEquals("est", consumed);
    assertEquals(6, characterReader.pos());
  }

  @Test
  public void testConsumeLetterThenDigitSequence() {
    characterReader.consume();
    String consumed = characterReader.consumeLetterThenDigitSequence();
    assertEquals("est", consumed);
    assertEquals(6, characterReader.pos());
  }

  @Test
  public void testConsumeHexSequence() {
    String consumed = characterReader.consumeHexSequence();
    assertEquals("", consumed);
  }

  @Test
  public void testConsumeDigitSequence() {
    String consumed = characterReader.consumeDigitSequence();
    assertEquals("", consumed);
  }

  @Test
  public void testMatches() {
    assertTrue(characterReader.matches('t'));
  }

  @Test
  public void testMatches2() {
    assertFalse(characterReader.matches('s'));
  }

  @Test
  public void testMatchesIgnoreCase() {
    assertTrue(characterReader.matchesIgnoreCase("T"));
  }

  @Test
  public void testMatchesIgnoreCase2() {
    assertFalse(characterReader.matchesIgnoreCase("S"));
  }

  @Test
  public void testMatchesAny() {
    assertTrue(characterReader.matchesAny('t'));
  }

  @Test
  public void testMatchesAny2() {
    assertFalse(characterReader.matchesAny('s'));
  }

  @Test
  public void testMatchesLetter() {
    assertTrue(characterReader.matchesLetter());
  }

  @Test
  public void testMatchesLetter2() {
    assertFalse(characterReader.matchesLetter());
  }

  @Test
  public void testMatchesDigit() {
    assertTrue(characterReader.matchesDigit());
  }

  @Test
  public void testMatchesDigit2() {
    assertFalse(characterReader.matchesDigit());
  }

  @Test
  public void testMatchConsume() {
    assertTrue(characterReader.matchConsume("te"));
    assertEquals(2, characterReader.pos());
  }

  @Test
  public void testMatchConsumeIgnoreCase() {
    assertTrue(characterReader.matchConsumeIgnoreCase("TE"));
    assertEquals(2, characterReader.pos());
  }

  @Test
  public void testContainsIgnoreCase() {
    assertTrue(characterReader.containsIgnoreCase("S"));
  }

  @Test
  public void testToString() {
    assertEquals("test string", characterReader.toString());
  }
}