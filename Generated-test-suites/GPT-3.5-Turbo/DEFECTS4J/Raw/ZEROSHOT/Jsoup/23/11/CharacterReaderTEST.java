import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
  
  private CharacterReader characterReader;
  
  @Before
  public void setup() {
    characterReader = new CharacterReader("This is a test");
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
    assertEquals('T', characterReader.current());
  }
  
  @Test
  public void testConsume() {
    assertEquals('T', characterReader.consume());
    assertEquals('h', characterReader.current());
    assertFalse(characterReader.isEmpty());
  }
  
  @Test
  public void testUnconsume() {
    characterReader.consume();
    characterReader.unconsume();
    assertEquals('T', characterReader.current());
  }
  
  @Test
  public void testAdvance() {
    characterReader.advance();
    assertEquals('h', characterReader.current());
  }
  
  @Test
  public void testMark() {
    characterReader.mark();
    assertEquals(0, characterReader.pos());
    assertEquals(0, characterReader.mark);
  }
  
  @Test
  public void testRewindToMark() {
    characterReader.advance();
    characterReader.mark();
    characterReader.rewindToMark();
    assertEquals(0, characterReader.pos());
  }
  
  @Test
  public void testConsumeAsString() {
    assertEquals("T", characterReader.consumeAsString());
    assertEquals('h', characterReader.current());
    assertFalse(characterReader.isEmpty());
  }
  
  @Test
  public void testConsumeTo() {
    assertEquals("This", characterReader.consumeTo(' '));
    assertEquals(' ', characterReader.current());
    assertFalse(characterReader.isEmpty());
  }
  
  @Test
  public void testConsumeToAny() {
    assertEquals("This", characterReader.consumeToAny(' ', 'a'));
    assertEquals(' ', characterReader.current());
    assertFalse(characterReader.isEmpty());
  }
  
  @Test
  public void testConsumeToEnd() {
    assertEquals("This is a test", characterReader.consumeToEnd());
    assertTrue(characterReader.isEmpty());
  }
  
  @Test
  public void testConsumeLetterSequence() {
    assertEquals("This", characterReader.consumeLetterSequence());
    assertFalse(characterReader.isEmpty());
  }
  
  @Test
  public void testConsumeHexSequence() {
    assertEquals("", characterReader.consumeHexSequence());
    assertFalse(characterReader.isEmpty());
  }
  
  @Test
  public void testConsumeDigitSequence() {
    assertEquals("", characterReader.consumeDigitSequence());
    assertFalse(characterReader.isEmpty());
  }
  
  @Test
  public void testMatches() {
    assertTrue(characterReader.matches('T'));
    assertFalse(characterReader.matches('F'));
  }
  
  @Test
  public void testMatchesIgnoreCase() {
    assertTrue(characterReader.matchesIgnoreCase("t"));
    assertFalse(characterReader.matchesIgnoreCase("f"));
  }
  
  @Test
  public void testMatchesAny() {
    assertTrue(characterReader.matchesAny('T', 'F'));
    assertFalse(characterReader.matchesAny('F', 'E'));
  }
  
  @Test
  public void testMatchesLetter() {
    assertTrue(characterReader.matchesLetter());
    assertFalse(characterReader.matchesLetter());
  }
  
  @Test
  public void testMatchesDigit() {
    assertFalse(characterReader.matchesDigit());
  }
  
  @Test
  public void testMatchConsume() {
    assertTrue(characterReader.matchConsume("This"));
    assertFalse(characterReader.matchConsume("is"));
  }
  
  @Test
  public void testMatchConsumeIgnoreCase() {
    assertTrue(characterReader.matchConsumeIgnoreCase("t"));
    assertFalse(characterReader.matchConsumeIgnoreCase("i"));
  }
  
  @Test
  public void testContainsIgnoreCase() {
    assertTrue(characterReader.containsIgnoreCase("is"));
    assertFalse(characterReader.containsIgnoreCase("not"));
  }
  
  @Test
  public void testToString() {
    assertEquals("This is a test", characterReader.toString());
  }
}