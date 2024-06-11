package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LexerTest {
  
  @Test
  public void testGetLineNumber() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    assertEquals(0, lexer.getLineNumber());
  }
  
  @Test
  public void testReadEscape() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    try {
      int result = lexer.readEscape();
      assertEquals(-1, result);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testTrimTrailingSpaces() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    StringBuilder buffer = new StringBuilder();
    buffer.append("test   ");
    lexer.trimTrailingSpaces(buffer);
    assertEquals("test", buffer.toString());
  }
  
  @Test
  public void testReadEndOfLine() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    try {
      boolean result = lexer.readEndOfLine("\n");
      assertEquals(true, result);
      
      result = lexer.readEndOfLine("\r\n");
      assertEquals(true, result);
      
      result = lexer.readEndOfLine("\r");
      assertEquals(true, result);
      
      result = lexer.readEndOfLine("test");
      assertEquals(false, result);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testIsWhitespace() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    boolean result = lexer.isWhitespace(' ');
    assertEquals(true, result);
    
    result = lexer.isWhitespace('\t');
    assertEquals(true, result);
    
    result = lexer.isWhitespace('\n');
    assertEquals(false, result);
    
    result = lexer.isWhitespace('\r');
    assertEquals(false, result);
    
    result = lexer.isWhitespace('t');
    assertEquals(false, result);
  }
  
  @Test
  public void testIsStartOfLine() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    boolean result = lexer.isStartOfLine('\n');
    assertEquals(true, result);
    
    result = lexer.isStartOfLine('\r');
    assertEquals(true, result);
    
    result = lexer.isStartOfLine('t');
    assertEquals(false, result);
  }
  
  @Test
  public void testIsEndOfFile() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    boolean result = lexer.isEndOfFile(-1);
    assertEquals(true, result);
    
    result = lexer.isEndOfFile(0);
    assertEquals(false, result);
  }
  
  @Test
  public void testIsDelimiter() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    boolean result = lexer.isDelimiter(',');
    assertEquals(true, result);
    
    result = lexer.isDelimiter(';');
    assertEquals(false, result);
  }
  
  @Test
  public void testIsEscape() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    boolean result = lexer.isEscape('\\');
    assertEquals(true, result);
    
    result = lexer.isEscape('/');
    assertEquals(false, result);
  }
  
  @Test
  public void testIsQuoteChar() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    boolean result = lexer.isQuoteChar('\"');
    assertEquals(true, result);
    
    result = lexer.isQuoteChar('\'');
    assertEquals(false, result);
  }
  
  @Test
  public void testIsCommentStart() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    boolean result = lexer.isCommentStart('#');
    assertEquals(true, result);
    
    result = lexer.isCommentStart('!');
    assertEquals(false, result);
  }
  
}