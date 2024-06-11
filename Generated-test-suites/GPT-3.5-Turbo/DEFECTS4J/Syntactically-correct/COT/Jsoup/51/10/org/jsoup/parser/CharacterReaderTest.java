import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
  
  @Test
  public void testPos() {
    CharacterReader characterReader = new CharacterReader("test");
    assertEquals(0, characterReader.pos());
  }
  
  @Test
  public void testIsEmpty() {
    CharacterReader characterReader = new CharacterReader("");
    assertTrue(characterReader.isEmpty());
  }
  
  @Test
  public void testCurrent() {
    CharacterReader characterReader = new CharacterReader("test");
    assertEquals('t', characterReader.current());
  }
  
  @Test
  public void testConsume() {
    CharacterReader characterReader = new CharacterReader("test");
    assertEquals('t', characterReader.consume());
  }
  
  @Test
  public void testUnconsume() {
    CharacterReader characterReader = new CharacterReader("test");
    characterReader.consume();
    characterReader.unconsume();
    assertEquals('t', characterReader.current());
  }
  
  @Test
  public void testAdvance() {
    CharacterReader characterReader = new CharacterReader("test");
    characterReader.advance();
    assertEquals('e', characterReader.current());
  }
  
  @Test
  public void testMark() {
    CharacterReader characterReader = new CharacterReader("test");
    characterReader.mark();
    characterReader.advance();
    assertEquals('e', characterReader.current());
  }
  
  @Test
  public void testRewindToMark() {
    CharacterReader characterReader = new CharacterReader("test");
    characterReader.mark();
    characterReader.advance();
    characterReader.rewindToMark();
    assertEquals('t', characterReader.current());
  }
  
  @Test
  public void testConsumeAsString() {
    CharacterReader characterReader = new CharacterReader("test");
    assertEquals("t", characterReader.consumeAsString());
  }
  
  @Test
  public void testNextIndexOf() {
    CharacterReader characterReader = new CharacterReader("test");
    assertEquals(1, characterReader.nextIndexOf('e'));
  }
  
  @Test
  public void testNextIndexOfSequence() {
    CharacterReader characterReader = new CharacterReader("test");
    assertEquals(1, characterReader.nextIndexOf("es"));
  }
  
  @Test
  public void testConsumeTo() {
    CharacterReader characterReader = new CharacterReader("test");
    assertEquals("t", characterReader.consumeTo('e'));
  }
  
  @Test
  public void testConsumeToSequence() {
    CharacterReader characterReader = new CharacterReader("test");
    assertEquals("t", characterReader.consumeTo("es"));
  }
  
  @Test
  public void testConsumeToAny() {
    CharacterReader characterReader = new CharacterReader("test");
    assertEquals("t", characterReader.consumeToAny('e'));
  }
  
  @Test
  public void testConsumeToAnySorted() {
    CharacterReader characterReader = new CharacterReader("test");
    assertEquals("est", characterReader.consumeToAnySorted('s'));
  }
  
  @Test
  public void testConsumeData() {
    CharacterReader characterReader = new CharacterReader("test");
    assertEquals("test", characterReader.consumeData());
  }
  
  @Test
  public void testConsumeTagName() {
    CharacterReader characterReader = new CharacterReader("test");
    assertEquals("test", characterReader.consumeTagName());
  }
  
  @Test
  public void testConsumeToEnd() {
    CharacterReader characterReader = new CharacterReader("test");
    assertEquals("test", characterReader.consumeToEnd());
  }
  
  @Test
  public void testConsumeLetterSequence() {
    CharacterReader characterReader = new CharacterReader("test123");
    assertEquals("test", characterReader.consumeLetterSequence());
  }
  
  @Test
  public void testConsumeLetterThenDigitSequence() {
    CharacterReader characterReader = new CharacterReader("test123");
    assertEquals("test123", characterReader.consumeLetterThenDigitSequence());
  }
  
  @Test
  public void testConsumeHexSequence() {
    CharacterReader characterReader = new CharacterReader("test123");
    assertEquals("123", characterReader.consumeHexSequence());
  }
  
  @Test
  public void testConsumeDigitSequence() {
    CharacterReader characterReader = new CharacterReader("test123");
    assertEquals("123", characterReader.consumeDigitSequence());
  }
  
  @Test
  public void testMatches() {
    CharacterReader characterReader = new CharacterReader("test");
    assertTrue(characterReader.matches('t'));
  }
  
  @Test
  public void testMatchesSequence() {
    CharacterReader characterReader = new CharacterReader("test");
    assertTrue(characterReader.matches("te"));
  }
  
  @Test
  public void testMatchesIgnoreCase() {
    CharacterReader characterReader = new CharacterReader("tEst");
    assertTrue(characterReader.matchesIgnoreCase("Te"));
  }
  
  @Test
  public void testMatchesAny() {
    CharacterReader characterReader = new CharacterReader("test");
    assertTrue(characterReader.matchesAny('t'));
  }
  
  @Test
  public void testMatchesAnySorted() {
    CharacterReader characterReader = new CharacterReader("test");
    assertTrue(characterReader.matchesAnySorted('e', 's', 't'));
  }
  
  @Test
  public void testMatchesLetter() {
    CharacterReader characterReader = new CharacterReader("test");
    assertTrue(characterReader.matchesLetter());
  }
  
  @Test
  public void testMatchesDigit() {
    CharacterReader characterReader = new CharacterReader("123");
    assertTrue(characterReader.matchesDigit());
  }
  
  @Test
  public void testMatchConsume() {
    CharacterReader characterReader = new CharacterReader("test");
    assertTrue(characterReader.matchConsume("te"));
    assertEquals('s', characterReader.current());
  }
  
  @Test
  public void testMatchConsumeIgnoreCase() {
    CharacterReader characterReader = new CharacterReader("tEst");
    assertTrue(characterReader.matchConsumeIgnoreCase("Te"));
    assertEquals('s', characterReader.current());
  }
  
  @Test
  public void testContainsIgnoreCase() {
    CharacterReader characterReader = new CharacterReader("tEst");
    assertTrue(characterReader.containsIgnoreCase("es"));
  }
  
  @Test
  public void testToString() {
    CharacterReader characterReader = new CharacterReader("test");
    assertEquals("test", characterReader.toString());
  }
  
}