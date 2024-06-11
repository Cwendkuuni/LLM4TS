import org.junit.*;
import static org.junit.Assert.*;

public class LexerTest {
  
  @Test
  public void testGetLineNumber() {
    Lexer lexer = new Lexer(format, in);
    long lineNumber = lexer.getLineNumber();
    assertEquals(0, lineNumber);
  }

  @Test
  public void testReadEscape() throws IOException {
    Lexer lexer = new Lexer(format, in);
    int escapedChar = lexer.readEscape();
    assertEquals(UNDEFINED, escapedChar); 
  }

  @Test
  public void testTrimTrailingSpaces() {
    Lexer lexer = new Lexer(format, in);
    StringBuilder buffer = new StringBuilder("hello  ");
    lexer.trimTrailingSpaces(buffer);
    assertEquals("hello", buffer.toString());
  }

  @Test
  public void testReadEndOfLine() throws IOException {
    Lexer lexer = new Lexer(format, in);
    boolean endOfLine = lexer.readEndOfLine(LF);
    assertTrue(endOfLine);
  }

  @Test
  public void testIsWhitespace() {
    Lexer lexer = new Lexer(format, in);
    boolean isWhitespace = lexer.isWhitespace(' ');
    assertTrue(isWhitespace);
  }

  @Test
  public void testIsStartOfLine() {
    Lexer lexer = new Lexer(format, in);
    boolean isStartOfLine = lexer.isStartOfLine(LF);
    assertTrue(isStartOfLine);
  }

  @Test
  public void testIsEndOfFile() {
    Lexer lexer = new Lexer(format, in);
    boolean isEndOfFile = lexer.isEndOfFile(END_OF_STREAM);
    assertTrue(isEndOfFile);
  }

  @Test
  public void testIsDelimiter() {
    Lexer lexer = new Lexer(format, in);
    boolean isDelimiter = lexer.isDelimiter(',');
    assertTrue(isDelimiter);
  }

  @Test
  public void testIsEscape() {
    Lexer lexer = new Lexer(format, in);
    boolean isEscape = lexer.isEscape('\\');
    assertTrue(isEscape);
  }

  @Test
  public void testIsQuoteChar() {
    Lexer lexer = new Lexer(format, in);
    boolean isQuoteChar = lexer.isQuoteChar('"');
    assertTrue(isQuoteChar);
  }

  @Test
  public void testIsCommentStart() {
    Lexer lexer = new Lexer(format, in);
    boolean isCommentStart = lexer.isCommentStart('#');
    assertTrue(isCommentStart);
  }
}