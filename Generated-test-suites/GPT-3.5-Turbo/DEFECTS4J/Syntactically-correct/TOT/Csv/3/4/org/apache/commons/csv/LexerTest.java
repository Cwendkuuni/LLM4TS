import org.junit.Test;

public class LexerTest {

  // Testing the constructor
  @Test
  public void testLexerConstructor() {
    // Create a dummy CSVFormat and ExtendedBufferedReader
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();

    // Create a new Lexer instance
    Lexer lexer = new Lexer(format, in);

    // Assertions
    assertNotNull(lexer);
    assertEquals(format, lexer.format);
    assertEquals(in, lexer.in);
    assertEquals(format.getDelimiter(), lexer.delimiter);
    assertEquals(format.getEscape(), lexer.escape);
    assertEquals(format.getQuoteChar(), lexer.quoteChar);
    assertEquals(format.getCommentStart(), lexer.commmentStart);
    assertEquals(format.getIgnoreSurroundingSpaces(), lexer.ignoreSurroundingSpaces);
    assertEquals(format.getIgnoreEmptyLines(), lexer.ignoreEmptyLines);
  }

  // Testing the getLineNumber() method
  @Test
  public void testGetLineNumber() {
    // Create a dummy CSVFormat and ExtendedBufferedReader
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();
    in.setLineNumber(10);

    // Create a new Lexer instance
    Lexer lexer = new Lexer(format, in);

    // Call the getLineNumber() method
    long lineNumber = lexer.getLineNumber();

    // Assertion
    assertEquals(10, lineNumber);
  }

  // Testing the mapNullToDisabled() method
  @Test
  public void testMapNullToDisabled() {
    // Create a dummy CSVFormat and ExtendedBufferedReader
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();

    // Create a new Lexer instance
    Lexer lexer = new Lexer(format, in);

    // Call the mapNullToDisabled() method with null character
    char disabledChar = lexer.mapNullToDisabled(null);

    // Assertion
    assertEquals(Lexer.DISABLED, disabledChar);

    // Call the mapNullToDisabled() method with non-null character
    char charValue = 'a';
    disabledChar = lexer.mapNullToDisabled(charValue);

    // Assertion
    assertEquals(charValue, disabledChar);
  }

  // Testing the readEscape() method
  @Test
  public void testReadEscape() {
    // Create a dummy CSVFormat and ExtendedBufferedReader
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();

    // Create a new Lexer instance
    Lexer lexer = new Lexer(format, in);

    // Call the readEscape() method with valid escape characters (CR, LF, TAB, BACKSPACE)
    int escapedChar = lexer.readEscape('r');
    // Assertion
    assertEquals(Lexer.CR, escapedChar);

    escapedChar = lexer.readEscape('n');
    // Assertion
    assertEquals(Lexer.LF, escapedChar);

    escapedChar = lexer.readEscape('t');
    // Assertion
    assertEquals(Lexer.TAB, escapedChar);

    escapedChar = lexer.readEscape('b');
    // Assertion
    assertEquals(Lexer.BACKSPACE, escapedChar);

    // Call the readEscape() method with invalid escape character
    try {
      lexer.readEscape('x');
      fail("IOException expected");
    } catch (IOException e) {
      // Assertion
      assertEquals("EOF whilst processing escape sequence", e.getMessage());
    }
  }

  // Testing the trimTrailingSpaces() method
  @Test
  public void testTrimTrailingSpaces() {
    // Create a dummy CSVFormat and ExtendedBufferedReader
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();

    // Create a new Lexer instance
    Lexer lexer = new Lexer(format, in);

    // Create a StringBuffer with trailing spaces
    StringBuilder buffer = new StringBuilder("Hello World    ");

    // Call the trimTrailingSpaces() method
    lexer.trimTrailingSpaces(buffer);

    // Assertion
    assertEquals("Hello World", buffer.toString());

    // Create a StringBuffer without trailing spaces
    buffer = new StringBuilder("Hello World");

    // Call the trimTrailingSpaces() method
    lexer.trimTrailingSpaces(buffer);

    // Assertion
    assertEquals("Hello World", buffer.toString());
  }

  // Testing the readEndOfLine() method
  @Test
  public void testReadEndOfLine() {
    // Create a dummy CSVFormat and ExtendedBufferedReader
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();

    // Create a new Lexer instance
    Lexer lexer = new Lexer(format, in);

    // Call the readEndOfLine() method with LF character
    boolean isEndOfLine = lexer.readEndOfLine(Lexer.LF);

    // Assertion
    assertTrue(isEndOfLine);

    // Call the readEndOfLine() method with CR character
    isEndOfLine = lexer.readEndOfLine(Lexer.CR);

    // Assertion
    assertTrue(isEndOfLine);

    // Reset the in stream
    in.reset();

    // Call the readEndOfLine() method with CR and LF characters
    isEndOfLine = lexer.readEndOfLine(Lexer.CR);

    // Assertion
    assertTrue(isEndOfLine);
  }

  // Testing the isWhitespace() method
  @Test
  public void testIsWhitespace() {
    // Create a dummy CSVFormat and ExtendedBufferedReader
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();

    // Create a new Lexer instance
    Lexer lexer = new Lexer(format, in);

    // Call the isWhitespace() method with a whitespace character
    boolean isWhitespace = lexer.isWhitespace(' ');

    // Assertion
    assertTrue(isWhitespace);

    // Call the isWhitespace() method with a non-whitespace character
    isWhitespace = lexer.isWhitespace('a');

    // Assertion
    assertFalse(isWhitespace);
  }

  // Testing the isStartOfLine() method
  @Test
  public void testIsStartOfLine() {
    // Create a dummy CSVFormat and ExtendedBufferedReader
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();

    // Create a new Lexer instance
    Lexer lexer = new Lexer(format, in);

    // Call the isStartOfLine() method with a line start character
    boolean isStartOfLine = lexer.isStartOfLine(Lexer.CR);

    // Assertion
    assertTrue(isStartOfLine);

    // Call the isStartOfLine() method with a non-line start character
    isStartOfLine = lexer.isStartOfLine('a');

    // Assertion
    assertFalse(isStartOfLine);
  }

  // Testing the isEndOfFile() method
  @Test
  public void testIsEndOfFile() {
    // Create a dummy CSVFormat and ExtendedBufferedReader
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();

    // Create a new Lexer instance
    Lexer lexer = new Lexer(format, in);

    // Call the isEndOfFile() method with the end of stream character
    boolean isEndOfFile = lexer.isEndOfFile(Lexer.END_OF_STREAM);

    // Assertion
    assertTrue(isEndOfFile);

    // Call the isEndOfFile() method with a non-end of stream character
    isEndOfFile = lexer.isEndOfFile('a');

    // Assertion
    assertFalse(isEndOfFile);
  }

  // Testing the isDelimiter() method
  @Test
  public void testIsDelimiter() {
    // Create a dummy CSVFormat and ExtendedBufferedReader
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();

    // Create a new Lexer instance
    Lexer lexer = new Lexer(format, in);

    // Call the isDelimiter() method with the delimiter character
    boolean isDelimiter = lexer.isDelimiter(Lexer.DEFAULT_DELIMITER);

    // Assertion
    assertTrue(isDelimiter);

    // Call the isDelimiter() method with a non-delimiter character
    isDelimiter = lexer.isDelimiter('a');

    // Assertion
    assertFalse(isDelimiter);
  }

  // Testing the isEscape() method
  @Test
  public void testIsEscape() {
    // Create a dummy CSVFormat and ExtendedBufferedReader
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();

    // Create a new Lexer instance
    Lexer lexer = new Lexer(format, in);

    // Call the isEscape() method with the escape character
    boolean isEscape = lexer.isEscape(Lexer.DEFAULT_ESCAPE);

    // Assertion
    assertTrue(isEscape);

    // Call the isEscape() method with a non-escape character
    isEscape = lexer.isEscape('a');

    // Assertion
    assertFalse(isEscape);
  }

  // Testing the isQuoteChar() method
  @Test
  public void testIsQuoteChar() {
    // Create a dummy CSVFormat and ExtendedBufferedReader
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();

    // Create a new Lexer instance
    Lexer lexer = new Lexer(format, in);

    // Call the isQuoteChar() method with the quote character
    boolean isQuoteChar = lexer.isQuoteChar(Lexer.DEFAULT_QUOTE_CHAR);

    // Assertion
    assertTrue(isQuoteChar);

    // Call the isQuoteChar() method with a non-quote character
    isQuoteChar = lexer.isQuoteChar('a');

    // Assertion
    assertFalse(isQuoteChar);
  }

  // Testing the isCommentStart() method
  @Test
  public void testIsCommentStart() {
    // Create a dummy CSVFormat and ExtendedBufferedReader
    CSVFormat format = CSVFormat.DEFAULT;
    ExtendedBufferedReader in = new ExtendedBufferedReader();

    // Create a new Lexer instance
    Lexer lexer = new Lexer(format, in);

    // Call the isCommentStart() method with the comment start character
    boolean isCommentStart = lexer.isCommentStart(Lexer.DEFAULT_COMMENT_START);

    // Assertion
    assertTrue(isCommentStart);

    // Call the isCommentStart() method with a non-comment start character
    isCommentStart = lexer.isCommentStart('a');

    // Assertion
    assertFalse(isCommentStart);
  }

  // TODO: Implement test cases for the nextToken() method

}