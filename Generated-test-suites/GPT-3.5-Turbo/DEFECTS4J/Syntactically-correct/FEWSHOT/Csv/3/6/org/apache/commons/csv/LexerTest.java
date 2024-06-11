package org.apache.commons.csv;

import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTest {

  @Test
  public void testConstructor() {
    // Test constructor with null format
    try {
      new Lexer(null, null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }

    // Test constructor with null input stream
    try {
      new Lexer(CSVFormat.DEFAULT, null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }

  @Test
  public void testGetLineNumber() {
    // Test getLineNumber() method
    ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
    Lexer lexer = new Lexer(CSVFormat.DEFAULT, reader);
    assertEquals(0, lexer.getLineNumber());

    // Test getLineNumber() method after reading a line
    reader.setLineNumber(5);
    assertEquals(5, lexer.getLineNumber());
  }

  @Test
  public void testMapNullToDisabled() {
    // Test mapNullToDisabled() method with null character
    Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
    assertEquals((char) 0xfffe, lexer.mapNullToDisabled(null));

    // Test mapNullToDisabled() method with non-null character
    assertEquals('a', lexer.mapNullToDisabled('a'));
  }

  @Test
  public void testReadEscape() throws IOException {
    // Test readEscape() method with valid escape sequence
    ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\\r"));
    Lexer lexer = new Lexer(CSVFormat.DEFAULT, reader);
    assertEquals('\r', lexer.readEscape());

    // Test readEscape() method with invalid escape sequence
    reader = new ExtendedBufferedReader(new StringReader("\\x"));
    lexer = new Lexer(CSVFormat.DEFAULT, reader);
    assertEquals(Lexer.END_OF_STREAM, lexer.readEscape());

    // Test readEscape() method with escape character at end of stream
    reader = new ExtendedBufferedReader(new StringReader("\\"));
    lexer = new Lexer(CSVFormat.DEFAULT, reader);
    try {
      lexer.readEscape();
      fail("Expected IOException");
    } catch (IOException e) {
      // Expected exception
    }
  }

  @Test
  public void testTrimTrailingSpaces() {
    // Test trimTrailingSpaces() method with no trailing spaces
    StringBuilder buffer = new StringBuilder("abc");
    Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
    lexer.trimTrailingSpaces(buffer);
    assertEquals("abc", buffer.toString());

    // Test trimTrailingSpaces() method with trailing spaces
    buffer = new StringBuilder("abc   ");
    lexer.trimTrailingSpaces(buffer);
    assertEquals("abc", buffer.toString());

    // Test trimTrailingSpaces() method with all spaces
    buffer = new StringBuilder("   ");
    lexer.trimTrailingSpaces(buffer);
    assertEquals("", buffer.toString());
  }

  @Test
  public void testReadEndOfLine() throws IOException {
    // Test readEndOfLine() method with LF character
    ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\n"));
    Lexer lexer = new Lexer(CSVFormat.DEFAULT, reader);
    assertTrue(lexer.readEndOfLine(reader.read()));

    // Test readEndOfLine() method with LF character after CR character
    reader = new ExtendedBufferedReader(new StringReader("\r\n"));
    lexer = new Lexer(CSVFormat.DEFAULT, reader);
    assertTrue(lexer.readEndOfLine(reader.read()));

    // Test readEndOfLine() method with CR character
    reader = new ExtendedBufferedReader(new StringReader("\r"));
    lexer = new Lexer(CSVFormat.DEFAULT, reader);
    assertTrue(lexer.readEndOfLine(reader.read()));
  }

  @Test
  public void testIsWhitespace() {
    // Test isWhitespace() method with whitespace character
    Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
    assertTrue(lexer.isWhitespace(' '));

    // Test isWhitespace() method with non-whitespace character
    assertFalse(lexer.isWhitespace('a'));
  }

  @Test
  public void testIsStartOfLine() {
    // Test isStartOfLine() method with start of line character
    Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
    assertTrue(lexer.isStartOfLine('\n'));

    // Test isStartOfLine() method with non-start of line character
    assertFalse(lexer.isStartOfLine('a'));
  }

  @Test
  public void testIsEndOfFile() {
    // Test isEndOfFile() method with end of file character
    Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
    assertTrue(lexer.isEndOfFile(-1));

    // Test isEndOfFile() method with non-end of file character
    assertFalse(lexer.isEndOfFile('a'));
  }

  @Test
  public void testIsDelimiter() {
    // Test isDelimiter() method with delimiter character
    Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
    assertTrue(lexer.isDelimiter(','));

    // Test isDelimiter() method with non-delimiter character
    assertFalse(lexer.isDelimiter('a'));
  }

  @Test
  public void testIsEscape() {
    // Test isEscape() method with escape character
    Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
    assertTrue(lexer.isEscape('\\'));

    // Test isEscape() method with non-escape character
    assertFalse(lexer.isEscape('a'));
  }

  @Test
  public void testIsQuoteChar() {
    // Test isQuoteChar() method with quote character
    Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
    assertTrue(lexer.isQuoteChar('"'));

    // Test isQuoteChar() method with non-quote character
    assertFalse(lexer.isQuoteChar('a'));
  }

  @Test
  public void testIsCommentStart() {
    // Test isCommentStart() method with comment start character
    Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
    assertTrue(lexer.isCommentStart('#'));

    // Test isCommentStart() method with non-comment start character
    assertFalse(lexer.isCommentStart('a'));
  }

  // TODO: Add test cases for each method in Lexer class

}