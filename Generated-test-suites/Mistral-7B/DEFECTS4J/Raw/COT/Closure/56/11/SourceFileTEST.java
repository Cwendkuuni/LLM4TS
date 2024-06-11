package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;

public class SourceFileTest {
  private SourceFile sourceFile;
  private Generator generatorMock;

  @Before
  public void setup() {
    generatorMock = mock(Generator.class);
    when(generatorMock.getCode()).thenReturn("testCode");
    sourceFile = new SourceFile("testFileName") {
      @Override
      public SourceFile fromGenerator(String fileName, Generator generator) {
        return new Generated(fileName, generator);
      }
    };
    sourceFile.setGenerator(generatorMock);
  }

  @Test
  public void testConstructor() throws IOException {
    sourceFile = new SourceFile("testFileName");
    assertEquals("testFileName", sourceFile.getName());
    assertNull(sourceFile.getCode());
  }

  @Test
  public void testGetLine() throws IOException {
    String expectedCode = "testLine1\ntestLine2\ntestLine3";
    sourceFile.setCode(expectedCode);
    String line1 = sourceFile.getLine(1);
    String expectedLine1 = "testLine1";
    assertEquals(expectedLine1, line1);

    String line3 = sourceFile.getLine(3);
    String expectedLine3 = "testLine3";
    assertEquals(expectedLine3, line3);

    String line5 = sourceFile.getLine(5);
    assertNull(line5);
  }

  @Test
  public void testGetRegion() throws IOException {
    String expectedCode = "testLine1\ntestLine2\ntestLine3\ntestLine4";
    sourceFile.setCode(expectedCode);
    Region region = sourceFile.getRegion(3);
    String expectedRegion = "testLine3\ntestLine4";
    assertEquals(expectedRegion, region.getCode());
  }

  @Test
  public void testFromFile() throws IOException {
    File file = new File("testFilePath");
    SourceFile sourceFile = SourceFile.fromFile("testFilePath", Charsets.UTF_8);
    assertEquals("testFilePath", sourceFile.getName());
    assertSame(file, sourceFile.getOriginalPath());
  }

  @Test
  public void testFromCode() throws IOException {
    String code = "testCode";
    SourceFile sourceFile = SourceFile.fromCode("testFileName", code);
    assertEquals("testFileName", sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
  }

  @Test
  public void testFromInputStream() throws IOException {
    InputStream inputStream = new FileInputStream(new File("testFilePath"));
    SourceFile sourceFile = SourceFile.fromInputStream("testFilePath", inputStream);
    assertEquals("testFilePath", sourceFile.getName());
    assertSame(inputStream, sourceFile.getGenerator());
  }

  @Test
  public void testFromReader() throws IOException {
    Reader reader = new StringReader("testReaderCode");
    SourceFile sourceFile = SourceFile.fromReader("testFilePath", reader);
    assertEquals("testFilePath", sourceFile.getName());
    assertEquals("testReaderCode", sourceFile.getCode());
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    String code = "testLine1\ntestLine2\ntestLine3";
    sourceFile.setCode(code);
    int[] lineOffsets = sourceFile.getLineOffsets();
    int[] expectedLineOffsets = {0, 8, 17};
    assertArrayEquals(expectedLineOffsets, lineOffsets);
  }
}