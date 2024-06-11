package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class SourceFileTest {
  // Given

  private SourceFile sourceFile;
  private Generator generator;
  private File file;

  @Before
  public void setUp() {
    generator = mock(Generator.class);
    when(generator.getCode()).thenReturn("mockCode");
    sourceFile = new SourceFile("testFileName") {
      @Override
      public SourceFile fromGenerator(String fileName, Generator generator) {
        return new Generated(fileName, generator);
      }
    };
    sourceFile.setGenerator(generator);

    file = new File("testFilePath");
  }

  @Test
  public void testGetFileName() {
    assertEquals("testFileName", sourceFile.getName());
  }

  @Test
  public void testIsExtern() {
    assertFalse(sourceFile.isExtern());
  }

  @Test
  public void testSetIsExtern() {
    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testGetCode() throws IOException {
    assertEquals("mockCode", sourceFile.getCode());
  }

  @Test
  public void testGetCodeReader() throws IOException {
    StringReader reader = new StringReader("mockCode");
    assertEquals(reader, sourceFile.getCodeReader());
  }

  @Test
  public void testGetOriginalPath() {
    assertNull(sourceFile.getOriginalPath());
  }

  @Test
  public void testSetOriginalPath() {
    sourceFile.setOriginalPath("testFilePath");
    assertEquals("testFilePath", sourceFile.getOriginalPath());
  }

  @Test
  public void testClearCachedSource() {
    String code = "cachedCode";
    sourceFile.setCode(code);
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
  }

  @Test
  public void testHasSourceInMemory() {
    String code = "inMemoryCode";
    sourceFile.setCode(code);
    assertTrue(sourceFile.hasSourceInMemory());
  }

  @Test
  public void testConstructorWithFileName() {
    SourceFile sourceFile = new SourceFile("testFileName");
    assertEquals("testFileName", sourceFile.getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithEmptyFileName() {
    new SourceFile("");
  }

  @Test
  public void testGetLine() {
    String code = "line1: print('Hello');\nline2: print('World');\n";
    sourceFile = new OnDisk(file, Charsets.UTF_8) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    assertEquals("print('Hello');", sourceFile.getLine(1));
    assertEquals("print('World');", sourceFile.getLine(2));
    assertNull(sourceFile.getLine(3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineWithNegativeLineNumber() {
    sourceFile.getLine(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineWithLineNumberGreaterThanNumberOfLines() {
    sourceFile.getLine(5);
  }

  @Test
  public void testGetRegion() {
    String code = "line1: print('Hello');\nline2: print('World');\n";
    sourceFile = new OnDisk(file, Charsets.UTF_8) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    Region region = sourceFile.getRegion(2);
    assertEquals(2, region.getStartLineNumber());
    assertEquals(2, region.getEndLineNumber());
    assertEquals(" print('World');", region.getCode());

    region = sourceFile.getRegion(1);
    assertEquals(1, region.getStartLineNumber());
    assertEquals(2, region.getEndLineNumber());
    assertEquals("print('Hello');\n", region.getCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRegionWithNegativeLineNumber() {
    sourceFile.getRegion(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRegionWithLineNumberGreaterThanNumberOfLines() {
    sourceFile.getRegion(5);
  }

  @Test
  public void testGetLineOffset() {
    String code = "line1: print('Hello');\nline2: print('World');\n";
    sourceFile = new OnDisk(file, Charsets.UTF_8) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    assertEquals(0, sourceFile.getLineOffset(1));
    assertEquals(13, sourceFile.getLineOffset(2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetWithNegativeLineNumber() {
    sourceFile.getLineOffset(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetWithLineNumberGreaterThanNumberOfLines() {
    sourceFile.getLineOffset(5);
  }

  @Test
  public void testGetNumLines() {
    String code = "line1: print('Hello');\nline2: print('World');\nline3: print('JavaScript');";
    sourceFile = new OnDisk(file, Charsets.UTF_8) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    assertEquals(3, sourceFile.getNumLines());
  }

  @Test
  public void testToString() {
    assertEquals("testFileName", sourceFile.toString());
  }

  @Test
  public void testFromFile() throws IOException {
    String fileContent = "function test() {\n print('Hello World');\n}";
    when(generator.getCode()).thenReturn("");

    File testFile = new File("testFile.js");
    Files.write(testFile, fileContent.getBytes(), Charsets.UTF_8);
    SourceFile sourceFile = SourceFile.fromFile(testFile.getPath(), Charsets.UTF_8);
    assertEquals("testFile.js", sourceFile.getName());
    assertEquals(fileContent, sourceFile.getCode());
    Files.delete(testFile);
  }

  @Test
  public void testFromFileWithOriginalPath() throws IOException {
    String fileContent = "function test() {\n print('Hello World');\n}";
    when(generator.getCode()).thenReturn("");

    File testFile = new File("testFile.js");
    Files.write(testFile, fileContent.getBytes(), Charsets.UTF_8);
    SourceFile sourceFile = SourceFile.fromFile(testFile.getPath(), "testFolder/", fileContent);
    assertEquals("testFolder/testFile.js", sourceFile.getOriginalPath());
    assertEquals("testFile.js", sourceFile.getName());
    assertEquals(fileContent, sourceFile.getCode());
    Files.delete(testFile);
  }

  @Test
  public void testFromCode() {
    SourceFile sourceFile = SourceFile.fromCode("testFileName", "function test() {\n print('Hello World');\n}");
    assertEquals("testFileName", sourceFile.getName());
    assertEquals("function test() {\n print('Hello World');\n}", sourceFile.getCode());
  }

  @Test
  public void testFromCodeWithOriginalPath() {
    SourceFile sourceFile = SourceFile.fromCode("testFileName", "function test() {\n print('Hello World');\n}", "testFolder/");
    assertEquals("testFolder/testFileName", sourceFile.getOriginalPath());
    assertEquals("function test() {\n print('Hello World');\n}", sourceFile.getCode());
  }

  @Test
  public void testFromInputStream() throws IOException {
    String inputContent = "function test() {\n print('Hello World');\n}";
    InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(Charsets.UTF_8));
    SourceFile sourceFile = SourceFile.fromInputStream("testFileName", inputStream, Charsets.UTF_8);
    assertEquals("testFileName", sourceFile.getName());
    assertEquals(inputContent, sourceFile.getCode());
  }

  @Test
  public void testFromInputStreamWithOriginalPath() throws IOException {
    String inputContent = "function test() {\n print('Hello World');\n}";
    InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(Charsets.UTF_8));
    SourceFile sourceFile = SourceFile.fromInputStream("testFileName", inputStream, "testFolder/", Charsets.UTF_8);
    assertEquals("testFolder/testFileName", sourceFile.getOriginalPath());
    assertEquals(inputContent, sourceFile.getCode());
  }

  @Test
  public void testFromReader() throws IOException {
    String readerContent = "function test() {\n print('Hello World');\n}";
    Reader reader = new StringReader(readerContent);
    SourceFile sourceFile = SourceFile.fromReader("testFileName", reader, Charsets.UTF_8);
    assertEquals("testFileName", sourceFile.getName());
    assertEquals(readerContent, sourceFile.getCode());
  }

  @Test
  public void testFromGenerator() {
    when(generator.getCode()).thenReturn("function test() {\n print('Hello World');\n}");
    SourceFile sourceFile = SourceFile.fromGenerator("testFileName", generator);
    assertEquals("testFileName", sourceFile.getName());
    assertEquals("function test() {\n print('Hello World');\n}", sourceFile.getCode());
  }
}

class ByteArrayInputStream extends InputStream {
  private byte[] bytes;

  ByteArrayInputStream(byte[] bytes) {
    this.bytes = bytes;
  }

  @Override
  public int read() throws IOException {
    if (bytes.length > 0) {
      byte b = bytes[0];
      bytes = new byte[bytes.length - 1];
      return b;
    } else {
      return -1;
    }
  }

  @Override
  public int available() throws IOException {
    return bytes.length;
  }

  @Override
  public void close() throws IOException {
  }

  @Override
  public void mark(int readlimit) {
  }

  @Override
  public boolean markSupported() {
    return false;
  }

  @Override
  public synchronized long skip(long n) throws IOException {
    long toSkip = Math.min(n, bytes.length);
    byte[] newBytes = new byte[bytes.length - (int) toSkip];
    System.arraycopy(bytes, (int) toSkip, newBytes, 0, newBytes.length);
    bytes = newBytes;
    return n;
  }
}