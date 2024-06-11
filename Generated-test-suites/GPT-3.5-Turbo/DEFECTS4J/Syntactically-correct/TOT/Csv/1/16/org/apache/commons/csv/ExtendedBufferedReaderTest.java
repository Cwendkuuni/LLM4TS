import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.junit.Before;
import org.junit.Test;

public class ExtendedBufferedReaderTest {

  private ExtendedBufferedReader reader;
  private BufferedReader mockedBufferedReader;

  @Before
  public void setUp() {
    Reader reader = mock(Reader.class);
    mockedBufferedReader = mock(BufferedReader.class);
    when(mockedBufferedReader.read()).thenReturn(10, 20, 30);
    when(mockedBufferedReader.read(any(char[].class), anyInt(), anyInt())).thenReturn(5, 8);
    when(mockedBufferedReader.readLine()).thenReturn("line1", "line2");
    this.reader = new ExtendedBufferedReader(mockedBufferedReader);
  }

  @Test
  public void testRead() throws IOException {
    assertEquals(10, reader.read());
    assertEquals(20, reader.read());
    assertEquals(30, reader.read());
    verify(mockedBufferedReader, times(3)).read();
  }

  @Test
  public void testReadAgain() throws IOException {
    reader.read();
    assertEquals(10, reader.readAgain());
    verifyZeroInteractions(mockedBufferedReader);
  }

  @Test
  public void testRead_withCharArray() throws IOException {
    char[] buf = new char[10];
    assertEquals(5, reader.read(buf, 0, 10));
    assertEquals(8, reader.read(buf, 0, 10));
    verify(mockedBufferedReader, times(2)).read(any(char[].class), anyInt(), anyInt());
  }

  @Test
  public void testReadLine() throws IOException {
    assertEquals("line1", reader.readLine());
    assertEquals("line2", reader.readLine());
    assertNull(reader.readLine());
    verify(mockedBufferedReader, times(3)).readLine();
  }

  @Test
  public void testLookAhead() throws IOException {
    when(mockedBufferedReader.read()).thenReturn(100);
    assertEquals(100, reader.lookAhead());
    verify(mockedBufferedReader, times(2)).mark(1);
    verify(mockedBufferedReader).reset();
  }

  @Test
  public void testGetLineNumber() {
    assertEquals(0, reader.getLineNumber());
  }

}