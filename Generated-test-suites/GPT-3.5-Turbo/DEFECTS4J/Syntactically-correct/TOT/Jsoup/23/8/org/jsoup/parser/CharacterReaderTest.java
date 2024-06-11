import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
  
  private CharacterReader characterReader;
  
  @Before
  public void setUp() {
    characterReader = new CharacterReader("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
  }
  
  @Test
  public void testPos() {
    assertEquals(0, characterReader.pos());
    
    characterReader.consume();
    
    assertEquals(1, characterReader.pos());
  }
  
  @Test
  public void testIsEmpty() {
    assertFalse(characterReader.isEmpty());
    
    for (int i = 0; i < characterReader.length; i++) {
      characterReader.consume();
    }
    
    assertTrue(characterReader.isEmpty());
  }
  
  @Test
  public void testCurrent() {
    assertEquals('L', characterReader.current());
    
    characterReader.consume();
    
    assertEquals('o', characterReader.current());
  }
  
  @Test
  public void testConsume() {
    assertEquals('L', characterReader.consume());
    
    assertEquals('o', characterReader.consume());
    
    // Consume all characters
    for (int i = 0; i < characterReader.length; i++) {
      characterReader.consume();
    }
    
    assertEquals(CharacterReader.EOF, characterReader.consume());
  }
  
  @Test
  public void testUnconsume() {
    characterReader.consume();
    characterReader.unconsume();
    
    assertEquals('L', characterReader.current());
  }
  
  @Test
  public void testAdvance() {
    characterReader.advance();
    
    assertEquals('o', characterReader.current());
  }
  
  @Test
  public void testMark() {
    characterReader.mark();
    
    characterReader.consume();
    
    assertEquals('o', characterReader.current());
  }
  
  @Test
  public void testRewindToMark() {
    characterReader.mark();
    characterReader.consume();
    
    characterReader.rewindToMark();
    
    assertEquals('L', characterReader.current());
  }
  
  @Test
  public void testConsumeAsString() {
    assertEquals("L", characterReader.consumeAsString());
    
    assertEquals("o", characterReader.consumeAsString());
    
    // Consume all characters
    for (int i = 0; i < characterReader.length-2; i++) {
      characterReader.consume();
    }
    
    assertEquals("t", characterReader.consumeAsString());
  }
  
  @Test
  public void testConsumeTo() {
    assertEquals("Lorem ipsum ", characterReader.consumeTo("dolor"));
    
    assertEquals("d", characterReader.consumeTo(' '));
    
    // Consume all characters
    for (int i = 0; i < characterReader.length-10; i++) {
      characterReader.consume();
    }
    
    assertEquals("amet, ", characterReader.consumeTo("consectetur"));
  }
  
  @Test
  public void testConsumeToAny() {
    assertEquals("Lorem", characterReader.consumeToAny(' ', ','));
    
    assertEquals(" ", characterReader.consumeToAny('.', '!'));
    
    // Consume all characters
    for (int i = 0; i < characterReader.length-8; i++) {
      characterReader.consume();
    }
    
    assertEquals("con", characterReader.consumeToAny('l', 's'));
  }
  
  @Test
  public void testConsumeToEnd() {
    assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", characterReader.consumeToEnd());
    
    // Consume all characters
    for (int i = 0; i < characterReader.length; i++) {
      characterReader.consume();
    }
    
    assertEquals("", characterReader.consumeToEnd());
  }
  
  @Test
  public void testConsumeLetterSequence() {
    assertEquals("Lorem", characterReader.consumeLetterSequence());
    
    // Consume all letters
    for (int i = 0; i < characterReader.length-10; i++) {
      characterReader.consume();
    }
    
    assertEquals("sit", characterReader.consumeLetterSequence());
  }
  
  @Test
  public void testConsumeHexSequence() {
    characterReader = new CharacterReader("12F3G5");
    
    assertEquals("12F3", characterReader.consumeHexSequence());
    
    assertEquals("G", characterReader.consumeHexSequence());
  }
  
  @Test
  public void testConsumeDigitSequence() {
    characterReader = new CharacterReader("123a56");
    
    assertEquals("123", characterReader.consumeDigitSequence());
    
    assertEquals("a", characterReader.consumeDigitSequence());
  }
  
  @Test
  public void testMatches() {
    assertTrue(characterReader.matches('L'));
    
    characterReader.consume();
    
    assertFalse(characterReader.matches('L'));
  }
  
  @Test
  public void testMatchesIgnoreCase() {
    assertTrue(characterReader.matchesIgnoreCase('L'));
    
    characterReader.consume();
    
    assertTrue(characterReader.matchesIgnoreCase('o'));
  }
  
  @Test
  public void testMatchesAny() {
    assertTrue(characterReader.matchesAny('L', 'o'));
    
    characterReader.consume();
    
    assertFalse(characterReader.matchesAny('L', 'o'));
  }
  
  @Test
  public void testMatchesLetter() {
    assertTrue(characterReader.matchesLetter());
    
    characterReader.consume();
    
    assertFalse(characterReader.matchesLetter());
  }
  
  @Test
  public void testMatchesDigit() {
    assertFalse(characterReader.matchesDigit());
    
    characterReader.consume();
    
    assertTrue(characterReader.matchesDigit());
  }
  
  @Test
  public void testMatchConsume() {
    assertTrue(characterReader.matchConsume("Lorem"));
    
    assertFalse(characterReader.matchConsume("Loren"));
  }
  
  @Test
  public void testMatchConsumeIgnoreCase() {
    assertTrue(characterReader.matchConsumeIgnoreCase("Lorem"));
    
    assertTrue(characterReader.matchConsumeIgnoreCase("ipsum"));
  }
  
  @Test
  public void testContainsIgnoreCase() {
    assertFalse(characterReader.containsIgnoreCase("Sit"));
    
    assertTrue(characterReader.containsIgnoreCase("sit"));
  }
  
  @Test
  public void testToString() {
    assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", characterReader.toString());
    
    // Consume all characters
    for (int i = 0; i < characterReader.length; i++) {
      characterReader.consume();
    }
    
    assertEquals("", characterReader.toString());
  }
}