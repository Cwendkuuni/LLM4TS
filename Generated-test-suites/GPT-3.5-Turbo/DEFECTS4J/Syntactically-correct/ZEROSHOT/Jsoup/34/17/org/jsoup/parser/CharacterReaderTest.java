import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
  
  @Test
  public void testPos() {
    CharacterReader cr = new CharacterReader("test");
    assertEquals(0, cr.pos());
  }
  
  @Test
  public void testIsEmpty() {
    CharacterReader cr = new CharacterReader("");
    assertTrue(cr.isEmpty());
  }
  
  @Test
  public void testIsEmptyNotEmpty() {
    CharacterReader cr = new CharacterReader("test");
    assertFalse(cr.isEmpty());
  }
  
  @Test
  public void testCurrent() {
    CharacterReader cr = new CharacterReader("test");
    assertEquals('t', cr.current());
  }
  
  @Test
  public void testConsume() {
    CharacterReader cr = new CharacterReader("test");
    assertEquals('t', cr.consume());
  }
  
  @Test
  public void testUnconsume() {
    CharacterReader cr = new CharacterReader("test");
    cr.consume();
    cr.unconsume();
    assertEquals('t', cr.current());
  }
  
  @Test
  public void testAdvance() {
    CharacterReader cr = new CharacterReader("test");
    cr.advance();
    assertEquals('e', cr.current());
  }
  
  @Test
  public void testMark() {
    CharacterReader cr = new CharacterReader("test");
    cr.mark();
    assertEquals(0, cr.pos());
  }
  
  @Test
  public void testRewindToMark() {
    CharacterReader cr = new CharacterReader("test");
    cr.mark();
    cr.advance();
    cr.rewindToMark();
    assertEquals('t', cr.current());
  }
  
  @Test
  public void testConsumeAsString() {
    CharacterReader cr = new CharacterReader("test");
    assertEquals("t", cr.consumeAsString());
  }
  
  @Test
  public void testNextIndexOf() {
    CharacterReader cr = new CharacterReader("test");
    assertEquals(1, cr.nextIndexOf('e'));
  }
  
  @Test
  public void testNextIndexOfNotFound() {
    CharacterReader cr = new CharacterReader("test");
    assertEquals(-1, cr.nextIndexOf('a'));
  }
  
  @Test
  public void testNextIndexOfSequence() {
    CharacterReader cr = new CharacterReader("test");
    assertEquals(1, cr.nextIndexOf("es"));
  }
  
  @Test
  public void testNextIndexOfSequenceNotFound() {
    CharacterReader cr = new CharacterReader("test");
    assertEquals(-1, cr.nextIndexOf("abc"));
  }
  
  @Test
  public void testConsumeTo() {
    CharacterReader cr = new CharacterReader("test");
    assertEquals("t", cr.consumeTo('e'));
  }
  
  @Test
  public void testConsumeToNotFound() {
    CharacterReader cr = new CharacterReader("test");
    assertEquals("test", cr.consumeTo('a'));
  }
  
  @Test
  public void testConsumeToSequence() {
    CharacterReader cr = new CharacterReader("test");
    assertEquals("t", cr.consumeTo("es"));
  }
  
  @Test
  public void testConsumeToSequenceNotFound() {
    CharacterReader cr = new CharacterReader("test");
    assertEquals("test", cr.consumeTo("abc"));
  }
  
  @Test
  public void testConsumeToAny() {
    CharacterReader cr = new CharacterReader("test");
    assertEquals("t", cr.consumeToAny('e', 's'));
  }
  
  @Test
  public void testConsumeToAnyNotFound() {
    CharacterReader cr = new CharacterReader("test");
    assertEquals("", cr.consumeToAny('a', 'b'));
  }
  
  @Test
  public void testConsumeToEnd() {
    CharacterReader cr = new CharacterReader("test");
    assertEquals("test", cr.consumeToEnd());
    assertTrue(cr.isEmpty());
  }
  
  @Test
  public void testConsumeLetterSequence() {
    CharacterReader cr = new CharacterReader("test");
    assertEquals("test", cr.consumeLetterSequence());
  }
  
  @Test
  public void testConsumeLetterSequenceEmpty() {
    CharacterReader cr = new CharacterReader("123");
    assertEquals("", cr.consumeLetterSequence());
  }
  
  @Test
  public void testConsumeLetterThenDigitSequence() {
    CharacterReader cr = new CharacterReader("t123est");
    assertEquals("t123", cr.consumeLetterThenDigitSequence());
  }
  
  @Test
  public void testConsumeLetterThenDigitSequenceEmpty() {
    CharacterReader cr = new CharacterReader("123");
    assertEquals("", cr.consumeLetterThenDigitSequence());
  }
  
  @Test
  public void testConsumeHexSequence() {
    CharacterReader cr = new CharacterReader("t123est");
    assertEquals("t123", cr.consumeHexSequence());
  }
  
  @Test
  public void testConsumeHexSequenceEmpty() {
    CharacterReader cr = new CharacterReader("abc");
    assertEquals("", cr.consumeHexSequence());
  }
  
  @Test
  public void testConsumeDigitSequence() {
    CharacterReader cr = new CharacterReader("t123est");
    assertEquals("123", cr.consumeDigitSequence());
  }
  
  @Test
  public void testConsumeDigitSequenceEmpty() {
    CharacterReader cr = new CharacterReader("abc");
    assertEquals("", cr.consumeDigitSequence());
  }
  
  @Test
  public void testMatches() {
    CharacterReader cr = new CharacterReader("test");
    assertTrue(cr.matches('t'));
  }
  
  @Test
  public void testMatchesFalse() {
    CharacterReader cr = new CharacterReader("test");
    assertFalse(cr.matches('a'));
  }
  
  @Test
  public void testMatchesString() {
    CharacterReader cr = new CharacterReader("test");
    assertTrue(cr.matches("test"));
  }
  
  @Test
  public void testMatchesStringFalse() {
    CharacterReader cr = new CharacterReader("test");
    assertFalse(cr.matches("abc"));
  }
  
  @Test
  public void testMatchesIgnoreCase() {
    CharacterReader cr = new CharacterReader("test");
    assertTrue(cr.matchesIgnoreCase("Test"));
  }
  
  @Test
  public void testMatchesIgnoreCaseFalse() {
    CharacterReader cr = new CharacterReader("test");
    assertFalse(cr.matchesIgnoreCase("Abc"));
  }
  
  @Test
  public void testMatchesAny() {
    CharacterReader cr = new CharacterReader("test");
    assertTrue(cr.matchesAny('t', 'a'));
  }
  
  @Test
  public void testMatchesAnyFalse() {
    CharacterReader cr = new CharacterReader("test");
    assertFalse(cr.matchesAny('a', 'b'));
  }
  
  @Test
  public void testMatchesLetter() {
    CharacterReader cr = new CharacterReader("test");
    assertTrue(cr.matchesLetter());
  }
  
  @Test
  public void testMatchesLetterFalse() {
    CharacterReader cr = new CharacterReader("123");
    assertFalse(cr.matchesLetter());
  }
  
  @Test
  public void testMatchesDigit() {
    CharacterReader cr = new CharacterReader("123");
    assertTrue(cr.matchesDigit());
  }
  
  @Test
  public void testMatchesDigitFalse() {
    CharacterReader cr = new CharacterReader("test");
    assertFalse(cr.matchesDigit());
  }
  
  @Test
  public void testMatchConsume() {
    CharacterReader cr = new CharacterReader("test");
    assertTrue(cr.matchConsume("test"));
    assertTrue(cr.isEmpty());
  }
  
  @Test
  public void testMatchConsumeFalse() {
    CharacterReader cr = new CharacterReader("test");
    assertFalse(cr.matchConsume("abc"));
    assertEquals('t', cr.current());
  }
  
  @Test
  public void testMatchConsumeIgnoreCase() {
    CharacterReader cr = new CharacterReader("test");
    assertTrue(cr.matchConsumeIgnoreCase("Test"));
    assertTrue(cr.isEmpty());
  }
  
  @Test
  public void testMatchConsumeIgnoreCaseFalse() {
    CharacterReader cr = new CharacterReader("test");
    assertFalse(cr.matchConsumeIgnoreCase("Abc"));
    assertEquals('t', cr.current());
  }
  
  @Test
  public void testContainsIgnoreCase() {
    CharacterReader cr = new CharacterReader("test");
    assertTrue(cr.containsIgnoreCase("te"));
  }
  
  @Test
  public void testContainsIgnoreCaseFalse() {
    CharacterReader cr = new CharacterReader("test");
    assertFalse(cr.containsIgnoreCase("ab"));
  }
  
  @Test
  public void testToString() {
    CharacterReader cr = new CharacterReader("test");
    assertEquals("test", cr.toString());
  }
  
}