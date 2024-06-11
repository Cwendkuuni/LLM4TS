package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

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
      assertEquals('e', reader.current());
   }
   
   @Test
   public void testUnconsume() {
      CharacterReader reader = new CharacterReader("Test");
      reader.consume();
      reader.unconsume();
      assertEquals('T', reader.current());
   }
   
   @Test
   public void testAdvance() {
      CharacterReader reader = new CharacterReader("Test");
      reader.advance();
      assertEquals('e', reader.current());
   }
   
   @Test
   public void testMark() {
      CharacterReader reader = new CharacterReader("Test");
      reader.advance();
      reader.mark();
      assertEquals(1, reader.pos());
   }
   
   @Test
   public void testRewindToMark() {
      CharacterReader reader = new CharacterReader("Test");
      reader.advance();
      reader.mark();
      reader.advance();
      reader.rewindToMark();
      assertEquals(1, reader.pos());
   }
   
   @Test
   public void testConsumeAsString() {
      CharacterReader reader = new CharacterReader("Test");
      assertEquals("T", reader.consumeAsString());
   }
   
   @Test
   public void testNextIndexOf() {
      CharacterReader reader = new CharacterReader("Test");
      assertEquals(1, reader.nextIndexOf('e'));
   }
   
   @Test
   public void testNextIndexOfCharSequence() {
      CharacterReader reader = new CharacterReader("Test");
      assertEquals(1, reader.nextIndexOf("es"));
   }
   
   @Test
   public void testConsumeTo() {
      CharacterReader reader = new CharacterReader("Test");
      assertEquals("T", reader.consumeTo('e'));
   }
   
   @Test
   public void testConsumeToSeq() {
      CharacterReader reader = new CharacterReader("Test");
      assertEquals("T", reader.consumeTo("es"));
   }
   
   @Test
   public void testConsumeToAny() {
      CharacterReader reader = new CharacterReader("Test");
      assertEquals("T", reader.consumeToAny('e'));
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
      CharacterReader reader = new CharacterReader("Test");
      assertEquals("Test", reader.consumeLetterThenDigitSequence());
   }
   
   @Test
   public void testConsumeHexSequence() {
      CharacterReader reader = new CharacterReader("0123456789ABCDEF");
      assertEquals("0123456789ABCDEF", reader.consumeHexSequence());
   }
   
   @Test
   public void testConsumeDigitSequence() {
      CharacterReader reader = new CharacterReader("12345");
      assertEquals("12345", reader.consumeDigitSequence());
   }
   
   @Test
   public void testMatches() {
      CharacterReader reader = new CharacterReader("Test");
      assertTrue(reader.matches('T'));
      assertFalse(reader.matches('e'));
   }
   
   @Test
   public void testMatchesSeq() {
      CharacterReader reader = new CharacterReader("Test");
      assertTrue(reader.matches("Test"));
      assertFalse(reader.matches("Tes"));
   }
   
   @Test
   public void testMatchesIgnoreCase() {
      CharacterReader reader = new CharacterReader("Test");
      assertTrue(reader.matchesIgnoreCase("test"));
      assertFalse(reader.matchesIgnoreCase("tEst"));
   }
   
   @Test
   public void testMatchesAny() {
      CharacterReader reader = new CharacterReader("Test");
      assertTrue(reader.matchesAny('T', 'F'));
      assertFalse(reader.matchesAny('e', 'F'));
   }
   
   @Test
   public void testMatchesLetter() {
      CharacterReader reader = new CharacterReader("Test");
      assertFalse(reader.matchesLetter());
   }
   
   @Test
   public void testMatchesDigit() {
      CharacterReader reader = new CharacterReader("Test");
      assertFalse(reader.matchesDigit());
   }
   
   @Test
   public void testMatchConsume() {
      CharacterReader reader = new CharacterReader("Test");
      assertTrue(reader.matchConsume("Tes"));
      assertFalse(reader.matchConsume("es"));
   }
   
   @Test
   public void testMatchConsumeIgnoreCase() {
      CharacterReader reader = new CharacterReader("Test");
      assertTrue(reader.matchConsumeIgnoreCase("test"));
      assertFalse(reader.matchConsumeIgnoreCase("tEst"));
   }
   
   @Test
   public void testContainsIgnoreCase() {
      CharacterReader reader = new CharacterReader("Test");
      assertTrue(reader.containsIgnoreCase("test"));
      assertFalse(reader.containsIgnoreCase("tEst"));
   }
   
   @Test
   public void testToString() {
      CharacterReader reader = new CharacterReader("Test");
      assertEquals("Test", reader.toString());
   }
}