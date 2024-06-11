
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LexerTest {
  
  private Lexer lexer;
  
  @Before
  public void setUp() {
    lexer = new Lexer(format, in);
  }
  
  @Test
  public void testGetLineNumber() {
    assertEquals(0, lexer.getLineNumber());
  }
  
  @Test
  public void testReadEscape() {
    try {
      assertEquals(UNDEFINED, lexer.readEscape());
    } catch (IOException e) {
      fail("IOException thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testTrimTrailingSpaces() {
    StringBuilder buffer = new StringBuilder("test string  ");
    lexer.trimTrailingSpaces(buffer);
    assertEquals("test string", buffer.toString());
  }
  
  @Test
  public void testReadEndOfLine() {
    try {
      assertFalse(lexer.readEndOfLine('\n'));
      assertFalse(lexer.readEndOfLine('\r'));
    } catch (IOException e) {
      fail("IOException thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testIsWhitespace() {
    assertFalse(lexer.isWhitespace('a'));
    assertTrue(lexer.isWhitespace(' '));
  }
  
  @Test
  public void testIsStartOfLine() {
    assertTrue(lexer.isStartOfLine('\n'));
    assertTrue(lexer.isStartOfLine('\r'));
    assertTrue(lexer.isStartOfLine(UNDEFINED));
    assertFalse(lexer.isStartOfLine('a'));
  }
  
  @Test
  public void testIsEndOfFile() {
    assertTrue(lexer.isEndOfFile(END_OF_STREAM));
    assertFalse(lexer.isEndOfFile('a'));
  }
  
  @Test
  public void testIsDelimiter() {
    assertTrue(lexer.isDelimiter(delimiter));
    assertFalse(lexer.isDelimiter('a'));
  }
  
  @Test
  public void testIsEscape() {
    assertTrue(lexer.isEscape(escape));
    assertFalse(lexer.isEscape('a'));
  }
  
  @Test
  public void testIsQuoteChar() {
    assertTrue(lexer.isQuoteChar(quoteChar));
    assertFalse(lexer.isQuoteChar('a'));
  }
  
  @Test
  public void testIsCommentStart() {
    assertTrue(lexer.isCommentStart(commmentStart));
    assertFalse(lexer.isCommentStart('a'));
  }

}
