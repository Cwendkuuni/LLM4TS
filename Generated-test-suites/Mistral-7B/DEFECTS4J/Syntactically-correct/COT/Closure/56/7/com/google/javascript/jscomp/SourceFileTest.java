package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class SourceFileTest {

  private SourceFile sourceFile;
  private Generator generatorMock;

  @Before
  public void setup() {
    generatorMock = mock(Generator.class);
    when(generatorMock.getCode()).thenReturn("mockedCode");
    sourceFile = new Generated("testFileName", generatorMock);
  }

  @Test
  public void testGetName() {
    assertEquals("testFileName", sourceFile.getName());
  }

  @Test
  public void testIsExtern() {
    assertFalse(sourceFile.isExtern());
  }

  @Test
  public void testGetCode() throws IOException {
    assertEquals("mockedCode", sourceFile.getCode());
  }

  @Test
  public void testGetCodeReader() throws IOException {
    StringReader readerMock = mock(StringReader.class);
    when(readerMock.read(new char[1])).thenReturn((char) 0);
    when(generatorMock.getCodeReader()).thenReturn(readerMock);

    Reader reader = sourceFile.getCodeReader();
    int ch = reader.read();

    assertEquals(-1, ch);
  }

  @Test(expected = IOException.class)
  public void testGetCodeWithIOException() throws IOException {
    when(generatorMock.getCode()).thenThrow(new IOException());
    sourceFile.getCode();
  }

  @Test
  public void testSetOriginalPath() {
    sourceFile.setOriginalPath("testOriginalPath");
    assertEquals("testOriginalPath", sourceFile.getOriginalPath());
  }

  @Test
  public void testGetOriginalPath() {
    assertNull(sourceFile.getOriginalPath());
  }

  @Test
  public void testClearCachedSource() {
    sourceFile.setCode("testCode");
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
  }

  @Test
  public void testHasSourceInMemory() {
    sourceFile.setCode("testCode");
    assertTrue(sourceFile.hasSourceInMemory());
  }

  @Test
  public void testGetLine() {
    String code = "line1\nline2\nline3";
    when(generatorMock.getCode()).thenReturn(code);
    sourceFile = new Generated("testFileName", generatorMock);

    String line1 = sourceFile.getLine(1);
    assertEquals("line1", line1);

    String line2 = sourceFile.getLine(2);
    assertEquals("line2", line2);

    String line3 = sourceFile.getLine(3);
    assertEquals("line3", line3);

    String line4 = sourceFile.getLine(4);
    assertNull(line4);
  }

  @Test
  public void testGetRegion() {
    String code = "line1\nline2\nline3";
    when(generatorMock.getCode()).thenReturn(code);
    sourceFile = new Generated("testFileName", generatorMock);

    Region region = sourceFile.getRegion(3);
    assertSame(3, region.getLineNumber());
    assertEquals("line3", region.getSource());

    Region region2 = sourceFile.getRegion(2);
    assertSame(2, region2.getLineNumber());
    assertEquals("line2", region2.getSource());

    Region region3 = sourceFile.getRegion(1);
    assertSame(1, region3.getLineNumber());
    assertEquals("line1", region3.getSource());

    Region region4 = sourceFile.getRegion(5);
    assertNull(region4);
  }
}