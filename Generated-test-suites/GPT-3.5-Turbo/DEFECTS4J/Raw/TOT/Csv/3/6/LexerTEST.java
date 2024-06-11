package org.apache.commons.csv;

import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

  @Test
  public void testGetLineNumber() {
    ExtendedBufferedReader reader = new ExtendedBufferedReader(null);
    Lexer lexer = new Lexer(null, reader);
    assertEquals(0, lexer.getLineNumber());
  }

  @Test
  public void testMapNullToDisabled() {
    CSVFormat format = new CSVFormat();
    format.setDelimiter(null);
    Lexer lexer = new Lexer(format, null);
    assertEquals(65534, lexer.mapNullToDisabled(null));
  }

  @Test
  public void testReadEscape() {
    ExtendedBufferedReader reader = new ExtendedBufferedReader(null);
    Lexer lexer = new Lexer(null, reader);

    try {
      lexer.readEscape();
    } catch (IOException e) {
      assertEquals("EOF whilst processing escape sequence", e.getMessage());
    }

    reader.setLastChar('c');
    try {
      int result = lexer.readEscape();
      assertEquals('c', result);
    } catch (IOException e) {
      fail("Exception should not have been thrown");
    }
  }

  @Test
  public void testTrimTrailingSpaces() {
    StringBuilder buffer = new StringBuilder();
    buffer.append(" test. ");
    Lexer lexer = new Lexer(null, null);
    lexer.trimTrailingSpaces(buffer);
    assertEquals(" test.", buffer.toString());
  }

  @Test
  public void testReadEndOfLine() {
    ExtendedBufferedReader reader = new ExtendedBufferedReader(null);
    Lexer lexer = new Lexer(null, reader);

    try {
      assertTrue(lexer.readEndOfLine(Constants.CR));
    } catch (IOException e) {
      fail("Exception should not have been thrown");
    }

    reader.setLastChar(Constants.LF);
    try {
      assertTrue(lexer.readEndOfLine(Constants.CR));
    } catch (IOException e) {
      fail("Exception should not have been thrown");
    }

    reader.setLastChar('a');
    try {
      assertFalse(lexer.readEndOfLine('a'));
    } catch (IOException e) {
      fail("Exception should not have been thrown");
    }
  }

  @Test
  public void testIsWhitespace() {
    Lexer lexer = new Lexer(null, null);

    assertTrue(lexer.isWhitespace(' '));
    assertTrue(lexer.isWhitespace('\t'));
    assertFalse(lexer.isWhitespace(','));
  }

  @Test
  public void testIsStartOfLine() {
    Lexer lexer = new Lexer(null, null);

    assertTrue(lexer.isStartOfLine(Constants.CR));
    assertTrue(lexer.isStartOfLine(Constants.LF));
    assertTrue(lexer.isStartOfLine(Constants.UNDEFINED));
    assertFalse(lexer.isStartOfLine('a'));
  }

  @Test
  public void testIsEndOfFile() {
    Lexer lexer = new Lexer(null, null);

    assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
    assertFalse(lexer.isEndOfFile('a'));
  }

  @Test
  public void testIsDelimiter() {
    CSVFormat format = new CSVFormat();
    format.setDelimiter(';');
    Lexer lexer = new Lexer(format, null);

    assertTrue(lexer.isDelimiter(';'));
    assertFalse(lexer.isDelimiter(','));
  }

  @Test
  public void testIsEscape() {
    CSVFormat format = new CSVFormat();
    format.setEscape('\n');
    Lexer lexer = new Lexer(format, null);

    assertTrue(lexer.isEscape('\n'));
    assertFalse(lexer.isEscape('\t'));
  }

  @Test
  public void testIsQuoteChar() {
    CSVFormat format = new CSVFormat();
    format.setQuoteChar('|');
    Lexer lexer = new Lexer(format, null);

    assertTrue(lexer.isQuoteChar('|'));
    assertFalse(lexer.isQuoteChar('"'));
  }

  @Test
  public void testIsCommentStart() {
    CSVFormat format = new CSVFormat();
    format.setCommentStart("//");
    Lexer lexer = new Lexer(format, null);

    assertTrue(lexer.isCommentStart('/'));
    assertFalse(lexer.isCommentStart('*'));
  }

}