
package JSci.maths.symbolic;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.Assert.*;

public class SimpleCharStreamTest {
  
  private SimpleCharStream simpleCharStream;

  @Before
  public void setUp() {
    InputStream inputStream = getClass().getResourceAsStream("test.txt");
    Reader reader = new InputStreamReader(inputStream);
    simpleCharStream = new SimpleCharStream(reader);
  }

  @Test
  public void testBeginToken() throws IOException {
    char expected = 'A';
    assertEquals(expected, simpleCharStream.BeginToken());
  }

  @Test
  public void testReadChar() throws IOException {
    char expected = 'A';
    simpleCharStream.BeginToken();
    assertEquals(expected, simpleCharStream.readChar());
  }

  @Test
  public void testGetColumn() {
    int expected = 0;
    assertEquals(expected, simpleCharStream.getColumn());
  }

  @Test
  public void testGetLine() {
    int expected = 1;
    assertEquals(expected, simpleCharStream.getLine());
  }

  @Test
  public void testGetEndColumn() {
    int expected = 0;
    assertEquals(expected, simpleCharStream.getEndColumn());
  }

  @Test
  public void testGetEndLine() {
    int expected = 1;
    assertEquals(expected, simpleCharStream.getEndLine());
  }

  @Test
  public void testGetBeginColumn() {
    int expected = 0;
    assertEquals(expected, simpleCharStream.getBeginColumn());
  }

  @Test
  public void testGetBeginLine() {
    int expected = 1;
    assertEquals(expected, simpleCharStream.getBeginLine());
  }

  @Test
  public void testBackup() {
    int amount = 5;
    simpleCharStream.backup(amount);
    int expected = -1;
    assertEquals(expected, simpleCharStream.bufpos);
  }

  @Test
  public void testReInit() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.txt");
    simpleCharStream.ReInit(new InputStreamReader(inputStream), 2, 2, 1024);
    int expected = 2;
    assertEquals(expected, simpleCharStream.getLine());
  }

  @Test
  public void testGetImage() throws IOException {
    simpleCharStream.BeginToken();
    String expected = "A";
    assertEquals(expected, simpleCharStream.GetImage());
  }

  @Test
  public void testGetSuffix() {
    char[] expected = {'A'};
    assertArrayEquals(expected, simpleCharStream.GetSuffix(1));
  }

  @Test
  public void testDone() {
    simpleCharStream.Done();
    assertNull(simpleCharStream.buffer);
    assertNull(simpleCharStream.bufline);
    assertNull(simpleCharStream.bufcolumn);
  }

  @Test
  public void testAdjustBeginLineColumn() {
    int newLine = 2;
    int newCol = 2;
    int expectedLine = 2;
    int expectedCol = 2;
    SimpleCharStream.adjustBeginLineColumn(newLine, newCol);
    assertEquals(expectedLine, SimpleCharStream.line);
    assertEquals(expectedCol, SimpleCharStream.column);
  }
}
