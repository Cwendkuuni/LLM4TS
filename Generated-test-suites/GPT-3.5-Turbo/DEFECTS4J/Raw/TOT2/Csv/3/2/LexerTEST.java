import static org.junit.Assert.*;
import org.junit.Test;

public class LexerTest {
  
  @Test
  public void testMapNullToDisabled() {
    Lexer lexer = new Lexer(format, in);
    char result = lexer.mapNullToDisabled(null);
    assertEquals('\ufffe', result);
  }
  
  @Test
  public void testGetLineNumber() {
    Lexer lexer = new Lexer(format, in);
    long result = lexer.getLineNumber();
    assertEquals(0, result);
  }
  
  @Test
  public void testReadEscape() {
    Lexer lexer = new Lexer(format, in);
    int result = lexer.readEscape();
    assertEquals(-1, result);
  }
  
  @Test
  public void testTrimTrailingSpaces() {
    Lexer lexer = new Lexer(format, in);
    StringBuilder buffer = new StringBuilder("test   ");
    lexer.trimTrailingSpaces(buffer);
    assertEquals("test", buffer.toString());
  }
  
  @Test
  public void testReadEndOfLine() {
    Lexer lexer = new Lexer(format, in);
    boolean result = lexer.readEndOfLine(LF);
    assertTrue(result);
  }
  
  @Test
  public void testIsWhitespace() {
    Lexer lexer = new Lexer(format, in);
    boolean result = lexer.isWhitespace(TAB);
    assertTrue(result);
  }
  
  @Test
  public void testIsStartOfLine() {
    Lexer lexer = new Lexer(format, in);
    boolean result = lexer.isStartOfLine(UNDEFINED);
    assertTrue(result);
  }
  
  @Test
  public void testIsEndOfFile() {
    Lexer lexer = new Lexer(format, in);
    boolean result = lexer.isEndOfFile(END_OF_STREAM);
    assertTrue(result);
  }
  
  @Test
  public void testIsDelimiter() {
    Lexer lexer = new Lexer(format, in);
    boolean result = lexer.isDelimiter(',');
    assertTrue(result);
  }
  
  @Test
  public void testIsEscape() {
    Lexer lexer = new Lexer(format, in);
    boolean result = lexer.isEscape('\ufffe');
    assertTrue(result);
  }
  
  @Test
  public void testIsQuoteChar() {
    Lexer lexer = new Lexer(format, in);
    boolean result = lexer.isQuoteChar('\ufffe');
    assertTrue(result);
  }
  
  @Test
  public void testIsCommentStart() {
    Lexer lexer = new Lexer(format, in);
    boolean result = lexer.isCommentStart('\ufffe');
    assertTrue(result);
  }
}