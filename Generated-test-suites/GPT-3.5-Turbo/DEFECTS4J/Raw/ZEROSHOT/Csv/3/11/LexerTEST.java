import static org.junit.Assert.*;
import org.junit.Test;

public class LexerTest {
  
  @Test
  public void testGetLineNumber() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    
    long lineNumber = lexer.getLineNumber();
    
    assertEquals(0, lineNumber);
  }
  
  @Test
  public void testReadEscape() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    
    int escape = lexer.readEscape();
    
    assertEquals(-1, escape);
  }
  
  @Test
  public void testTrimTrailingSpaces() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    
    StringBuilder buffer = new StringBuilder("test   ");
    lexer.trimTrailingSpaces(buffer);
    
    assertEquals("test", buffer.toString());
  }
  
  @Test
  public void testReadEndOfLine() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    
    boolean endOfLine = lexer.readEndOfLine(10);
    
    assertTrue(endOfLine);
  }
  
  @Test
  public void testNextToken() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    
    Token token = lexer.nextToken(new Token());
    
    assertNotNull(token);
  }
  
  @Test
  public void testIsWhitespace() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    
    boolean whitespace = lexer.isWhitespace(32);
    
    assertTrue(whitespace);
  }
  
  @Test
  public void testIsStartOfLine() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    
    boolean startOfLine = lexer.isStartOfLine(10);
    
    assertTrue(startOfLine);
  }
  
  @Test
  public void testIsEndOfFile() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    
    boolean endOfFile = lexer.isEndOfFile(-1);
    
    assertTrue(endOfFile);
  }
  
  @Test
  public void testIsDelimiter() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    
    boolean delimiter = lexer.isDelimiter(44);
    
    assertTrue(delimiter);
  }
  
  @Test
  public void testIsEscape() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    
    boolean escape = lexer.isEscape(92);
    
    assertTrue(escape);
  }
  
  @Test
  public void testIsQuoteChar() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    
    boolean quoteChar = lexer.isQuoteChar(34);
    
    assertTrue(quoteChar);
  }
  
  @Test
  public void testIsCommentStart() {
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    Lexer lexer = new Lexer(format, in);
    
    boolean commentStart = lexer.isCommentStart(35);
    
    assertTrue(commentStart);
  }
}