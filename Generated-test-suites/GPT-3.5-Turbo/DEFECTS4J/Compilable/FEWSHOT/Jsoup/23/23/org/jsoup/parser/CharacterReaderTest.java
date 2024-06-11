package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {
  
  @Test
  public void testPos() {
    CharacterReader reader = new CharacterReader("");
    assertEquals(0, reader.pos());
    
    reader.advance();
    assertEquals(1, reader.pos());
  }
  
  @Test
  public void testIsEmpty() {
    CharacterReader reader = new CharacterReader("");
    assertTrue(reader.isEmpty());
    
    reader.advance();
    assertTrue(reader.isEmpty());
    
    reader = new CharacterReader("test");
    assertFalse(reader.isEmpty());
    
    reader.consume();
    assertFalse(reader.isEmpty());
  }
  
  @Test
  public void testCurrent() {
    CharacterReader reader = new CharacterReader("");
    assertEquals('\uffff', reader.current());
    
    reader.advance();
    assertEquals('\uffff', reader.current());
    
    reader = new CharacterReader("test");
    assertEquals('t', reader.current());
    
    reader.consume();
    assertEquals('e', reader.current());
  }
  
  @Test
  public void testConsume() {
    CharacterReader reader = new CharacterReader("");
    assertEquals('\uffff', reader.consume());
    
    reader.advance();
    assertEquals('\uffff', reader.consume());
    
    reader = new CharacterReader("test");
    
    for (int i = 0; i < 4; i++) {
      assertEquals("test".charAt(i), reader.consume());
    }
    
    assertEquals('\uffff', reader.consume());
  }
  
  @Test
  public void testUnconsume() {
    CharacterReader reader = new CharacterReader("test");
    
    reader.consume();
    reader.unconsume();
    assertEquals('t', reader.current());
    
    reader.unconsume();
    assertEquals('\uffff', reader.current());
  }
  
  @Test
  public void testAdvance() {
    CharacterReader reader = new CharacterReader("test");
    
    reader.advance();
    assertEquals('e', reader.current());
    
    reader.advance();
    assertEquals('s', reader.current());
    
    reader.advance();
    assertEquals('t', reader.current());
    
    reader.advance();
    assertEquals('\uffff', reader.current());
  }
  
  @Test
  public void testMark() {
    CharacterReader reader = new CharacterReader("test");
    
    reader.mark();
    reader.advance();
    reader.advance();
    
    reader.rewindToMark();
    assertEquals('t', reader.current());
  }
  
  @Test
  public void testConsumeAsString() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("t", reader.consumeAsString());
    
    reader.mark();
    assertEquals("e", reader.consumeAsString());
    
    reader.unconsume();
    assertEquals("e", reader.consumeAsString());
  }
  
  @Test
  public void testConsumeTo_char() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("", reader.consumeTo('.'));
    
    reader = new CharacterReader("test.test");
    assertEquals("test", reader.consumeTo('.'));
    
    reader = new CharacterReader("test.test");
    assertEquals("test.", reader.consumeTo('t'));
  }
  
  @Test
  public void testConsumeTo_String() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("", reader.consumeTo("."));
    
    reader = new CharacterReader("test.test");
    assertEquals("test", reader.consumeTo("."));
    
    reader = new CharacterReader("test.test");
    assertEquals("test.", reader.consumeTo("t"));
  }
  
  @Test
  public void testConsumeToAny() {
    CharacterReader reader = new CharacterReader("test.test");
    assertEquals("", reader.consumeToAny('.'));
    
    reader = new CharacterReader("test.test");
    assertEquals("test", reader.consumeToAny('.', '/'));
    
    reader = new CharacterReader("foo/bar/test");
    assertEquals("foo/bar", reader.consumeToAny('.', '/'));
  }
  
  @Test
  public void testConsumeToEnd() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("test", reader.consumeToEnd());
    
    reader = new CharacterReader("test.test");
    assertEquals("test.test", reader.consumeToEnd());
  }
  
  @Test
  public void testConsumeLetterSequence() {
    CharacterReader reader = new CharacterReader("test123");
    assertEquals("test", reader.consumeLetterSequence());
    
    reader = new CharacterReader("123test");
    assertEquals("", reader.consumeLetterSequence());
  }
  
  @Test
  public void testConsumeHexSequence() {
    CharacterReader reader = new CharacterReader("1234abcdef");
    assertEquals("1234", reader.consumeHexSequence());
    
    reader = new CharacterReader("abcdef1234");
    assertEquals("", reader.consumeHexSequence());
  }
  
  @Test
  public void testConsumeDigitSequence() {
    CharacterReader reader = new CharacterReader("123abc");
    assertEquals("123", reader.consumeDigitSequence());
    
    reader = new CharacterReader("abc123");
    assertEquals("", reader.consumeDigitSequence());
  }
  
  @Test
  public void testMatches_char() {
    CharacterReader reader = new CharacterReader("test");
    assertFalse(reader.matches('.'));
    
    reader.consume();
    assertTrue(reader.matches('.'));
  }
  
  @Test
  public void testMatches_String() {
    CharacterReader reader = new CharacterReader("test");
    assertFalse(reader.matches("."));
    
    reader.consume();
    assertTrue(reader.matches("."));
  }
  
  @Test
  public void testMatchesIgnoreCase() {
    CharacterReader reader = new CharacterReader("test");
    assertFalse(reader.matchesIgnoreCase("."));
    
    reader.consume();
    assertTrue(reader.matchesIgnoreCase("."));
  }
  
  @Test
  public void testMatchesAny() {
    CharacterReader reader = new CharacterReader("test");
    assertFalse(reader.matchesAny('.'));
    
    reader.consume();
    assertTrue(reader.matchesAny('.'));
  }
  
  @Test
  public void testMatchesLetter() {
    CharacterReader reader = new CharacterReader("test");
    assertFalse(reader.matchesLetter());
    
    reader.consume();
    assertTrue(reader.matchesLetter());
  }
  
  @Test
  public void testMatchesDigit() {
    CharacterReader reader = new CharacterReader("test");
    assertFalse(reader.matchesDigit());
    
    reader.consume();
    assertFalse(reader.matchesDigit());
  }
  
  @Test
  public void testMatchConsume() {
    CharacterReader reader = new CharacterReader("test");
    assertFalse(reader.matchConsume("."));
    
    reader.consume();
    assertTrue(reader.matchConsume("."));
    
    assertFalse(reader.matchConsume("."));
  }
  
  @Test
  public void testMatchConsumeIgnoreCase() {
    CharacterReader reader = new CharacterReader("test");
    assertFalse(reader.matchConsumeIgnoreCase("."));
    
    reader.consume();
    assertFalse(reader.matchConsumeIgnoreCase("."));
    
    reader.unconsume();
    assertTrue(reader.matchConsumeIgnoreCase("."));
  }
  
  @Test
  public void testContainsIgnoreCase() {
    CharacterReader reader = new CharacterReader("test");
    assertFalse(reader.containsIgnoreCase("<title>"));
    
    reader = new CharacterReader("</title>");
    assertTrue(reader.containsIgnoreCase("<title>"));
    
    reader = new CharacterReader("te</tItLe>st");
    assertTrue(reader.containsIgnoreCase("<title>"));
  }
  
  @Test
  public void testToString() {
    CharacterReader reader = new CharacterReader("test");
    assertEquals("test", reader.toString());
    
    reader.consume();
    assertEquals("est", reader.toString());
  }
}