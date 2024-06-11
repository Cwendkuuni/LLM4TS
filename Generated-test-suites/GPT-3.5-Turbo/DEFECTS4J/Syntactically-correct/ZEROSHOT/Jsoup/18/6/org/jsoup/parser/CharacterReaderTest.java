package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
  
  private CharacterReader characterReader;
  
  @Before
  public void setUp() {
    String input = "This is a test input";
    characterReader = new CharacterReader(input);
  }
  
  @Test
  public void testPos() {
    int expected = 0;
    int actual = characterReader.pos();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsEmpty() {
    boolean expected = false;
    boolean actual = characterReader.isEmpty();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testCurrent() {
    char expected = 'T';
    char actual = characterReader.current();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testConsume() {
    char expected = 'T';
    char actual = characterReader.consume();
    assertEquals(expected, actual);
    
    expected = 'h';
    actual = characterReader.consume();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testUnconsume() {
    char expected = 'h';
    characterReader.unconsume();
    char actual = characterReader.current();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testAdvance() {
    char expected = 'i';
    characterReader.advance();
    char actual = characterReader.current();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testMark() {
    characterReader.mark();
    int expected = 0;
    int actual = characterReader.pos();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testRewindToMark() {
    characterReader.rewindToMark();
    int expected = 0;
    int actual = characterReader.pos();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testConsumeAsString() {
    String expected = "T";
    String actual = characterReader.consumeAsString();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testConsumeTo() {
    String expected = "This ";
    String actual = characterReader.consumeTo("is");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testConsumeToAny() {
    String expected = "This ";
    String actual = characterReader.consumeToAny('a', 's');
    assertEquals(expected, actual);
  }
  
  @Test
  public void testConsumeToEnd() {
    String expected = "This is a test input";
    String actual = characterReader.consumeToEnd();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testConsumeLetterSequence() {
    String expected = "This";
    String actual = characterReader.consumeLetterSequence();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testConsumeHexSequence() {
    String expected = "This";
    String actual = characterReader.consumeHexSequence();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testConsumeDigitSequence() {
    String expected = "";
    String actual = characterReader.consumeDigitSequence();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testMatches() {
    boolean expected = false;
    boolean actual = characterReader.matches('z');
    assertEquals(expected, actual);
  }
  
  @Test
  public void testMatchesAny() {
    boolean expected = false;
    boolean actual = characterReader.matchesAny('a', 'b');
    assertEquals(expected, actual);
  }
  
  @Test
  public void testMatchesLetter() {
    boolean expected = true;
    boolean actual = characterReader.matchesLetter();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testMatchesDigit() {
    boolean expected = false;
    boolean actual = characterReader.matchesDigit();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testMatchConsume() {
    boolean expected = true;
    boolean actual = characterReader.matchConsume("This");
    assertEquals(expected, actual);
    
    expected = false;
    actual = characterReader.matchConsume("is");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testMatchConsumeIgnoreCase() {
    boolean expected = true;
    boolean actual = characterReader.matchConsumeIgnoreCase("this");
    assertEquals(expected, actual);
    
    expected = false;
    actual = characterReader.matchConsumeIgnoreCase("is");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testContainsIgnoreCase() {
    boolean expected = true;
    boolean actual = characterReader.containsIgnoreCase("his");
    assertEquals(expected, actual);
    
    expected = false;
    actual = characterReader.containsIgnoreCase("hello");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testToString() {
    String expected = "This is a test input";
    String actual = characterReader.toString();
    assertEquals(expected, actual);
  }
  
}