package org.apache.commons.csv;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

  private ExtendedBufferedReader reader;

  @Before
  public void setUp() throws IOException {
    Reader r = new StringReader("This is a test\nwith multiple lines\n");
    reader = new ExtendedBufferedReader(r);
  }

  @Test
  public void testRead() throws IOException {
    int firstChar = reader.read();
    assertEquals('T', firstChar);
    int secondChar = reader.read();
    assertEquals('h', secondChar);
    int lastChar = reader.readAgain();
    assertEquals('h', lastChar);
  }

  @Test
  public void testReadLine() throws IOException {
    String firstLine = reader.readLine();
    assertEquals("This is a test", firstLine);
    int lineNumber = reader.getLineNumber();
    assertEquals(1, lineNumber);
    String secondLine = reader.readLine();
    assertEquals("with multiple lines", secondLine);
    lineNumber = reader.getLineNumber();
    assertEquals(2, lineNumber);
  }

  @Test
  public void testReadBuffer() throws IOException {
    char[] buffer = new char[10];
    int charsRead = reader.read(buffer, 0, 10);
    assertEquals(10, charsRead);
    assertEquals('T', buffer[0]);
    assertEquals('i', buffer[1]);

    buffer = new char[20];
    charsRead = reader.read(buffer, 0, 20);
    assertEquals(19, charsRead);
    assertEquals('w', buffer[0]);
    assertEquals('i', buffer[1]);
    assertEquals('\n', buffer[10]);
    assertEquals('l', buffer[11]);
    assertEquals('i', buffer[12]);
    assertEquals('n', buffer[13]);
    assertEquals('e', buffer[14]);
    assertEquals('s', buffer[15]);
    assertEquals('\n', buffer[19]);
  }

  @Test
  public void testLookAhead() throws IOException {
    int nextChar = reader.lookAhead();
    assertEquals('T', nextChar);
    reader.read(); // consume the next char
    nextChar = reader.lookAhead();
    assertEquals('h', nextChar);
  }

  @Test
  public void testLineNumber() throws IOException {
    int lineNumber = reader.getLineNumber();
    assertEquals(0, lineNumber);
    reader.readLine();
    lineNumber = reader.getLineNumber();
    assertEquals(1, lineNumber);
    reader.readLine();
    lineNumber = reader.getLineNumber();
    assertEquals(2, lineNumber);
  }

  @Test
  public void testEndOfStream() throws IOException {
    int lastChar = reader.read();
    while (lastChar != ExtendedBufferedReader.END_OF_STREAM) {
      lastChar = reader.read();
    }
    assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
  }

}