package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import org.junit.Test;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.StringWriter;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;
import com.google.javascript.rhino.jstype.StaticSourceFile;

public class SourceFileTest {

  @Test
  public void testConstructorWithFileName() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertEquals("test.js", sourceFile.getName());
  }

  @Test
  public void testGetLineOffset() throws IOException {
    SourceFile sourceFile = createSourceFileWithCode("test.js",
        "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}");
    assertEquals(1, sourceFile.getLineOffset(1));
    assertEquals(3, sourceFile.getLineOffset(3));
    assertEquals(5, sourceFile.getLineOffset(5));
    assertEquals(8, sourceFile.getLineOffset(8));

    try {
      sourceFile.getLineOffset(0);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      sourceFile.getLineOffset(10);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testGetNumLines() throws IOException {
    SourceFile sourceFile = createSourceFileWithCode("test.js",
        "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}");
    assertEquals(4, sourceFile.getNumLines());
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    SourceFile sourceFile = createSourceFileWithCode("test.js",
        "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}");
    String code = sourceFile.getCode();
    String[] sourceLines = code.split("\n");
    int[] expectedLineOffsets = {0, 6, 12, 18};
    assertArrayEquals(expectedLineOffsets, sourceFile.lineOffsets);
  }

  @Test
  public void testGetCode() throws IOException {
    SourceFile sourceFile = createSourceFileWithCode("test.js",
        "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}");
    String expectedCode = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}";
    assertEquals(expectedCode, sourceFile.getCode());
  }

  @Test
  public void testGetCodeReader() throws IOException {
    SourceFile sourceFile = createSourceFileWithCode("test.js",
        "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}");
    Reader reader = sourceFile.getCodeReader();
    StringWriter stringWriter = new StringWriter();
    char[] buffer = new char[1024];
    int charsRead = 0;
    while ((charsRead = reader.read(buffer, 0, buffer.length)) > 0) {
      stringWriter.write(buffer, 0, charsRead);
    }
    assertEquals("function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}", stringWriter.toString());
  }

  @Test
  public void testGetOriginalPath() throws IOException {
    SourceFile sourceFile = createSourceFileWithCode("test.js",
        "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}", "original/path/to/test.js");
    assertEquals("original/path/to/test.js", sourceFile.getOriginalPath());
  }

  @Test
  public void testSetOriginalPath() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.setOriginalPath("original/path/to/test.js");
    assertEquals("original/path/to/test.js", sourceFile.getOriginalPath());
  }

  @Test
  public void testClearCachedSource() throws IOException {
    SourceFile sourceFile = createSourceFileWithCode("test.js",
        "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}");
    String expectedCode = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}";
    assertEquals(expectedCode, sourceFile.getCode());
    sourceFile.clearCachedSource();
    assertEquals(null, sourceFile.getCode());
  }

  @Test
  public void testHasSourceInMemory() throws IOException {
    SourceFile sourceFile = createSourceFileWithCode("test.js",
        "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}");
    assertTrue(sourceFile.hasSourceInMemory());

    sourceFile.clearCachedSource();
    assertFalse(sourceFile.hasSourceInMemory());
  }

  @Test
  public void testGetLine() throws IOException {
    SourceFile sourceFile = createSourceFileWithCode("test.js",
        "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}");
    String expectedLine1 = "function test() {\n";
    String expectedLine2 = "  var a = 1;\n";
    String expectedLine3 = "  var b = 2;\n";
    String expectedLine4 = "}";

    assertEquals(expectedLine1, sourceFile.getLine(1));
    assertEquals(expectedLine2, sourceFile.getLine(3));
    assertEquals(expectedLine3, sourceFile.getLine(5));
    assertEquals(expectedLine4, sourceFile.getLine(7));

    try {
      sourceFile.getLine(0);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      sourceFile.getLine(8);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testGetRegion() throws IOException {
    SourceFile sourceFile = createSourceFileWithCode("test.js",
        "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}");

    String expectedRegion1 =
      "function test() {\n" +
      "  var a = 1;\n";
    String expectedRegion2 =
      "  var a = 1;\n" +
      "  var b = 2;\n";

    Region region1 = sourceFile.getRegion(1);
    Region region2 = sourceFile.getRegion(5);

    assertEquals(expectedRegion1, region1.getCode());
    assertEquals(expectedRegion2, region2.getCode());

    try {
      sourceFile.getRegion(0);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      sourceFile.getRegion(8);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testFromFile() throws IOException {
    String testFilePath = "test_from_file.js";
    File testFile = new File(testFilePath);
    Files.write(testFile, "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}", Charsets.UTF_8);

    SourceFile sourceFile = SourceFile.fromFile(testFilePath);
    String expectedCode = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}";
    assertEquals(expectedCode, sourceFile.getCode());

    Files.delete(testFile);
  }

  @Test
  public void testFromCodeWithFileName() throws IOException {
    SourceFile sourceFile = SourceFile.fromCode("test.js",
        "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}");
    String expectedCode = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}";
    assertEquals(expectedCode, sourceFile.getCode());
  }

  @Test
  public void testFromCodeWithFileNameOriginalPath() throws IOException {
    SourceFile sourceFile = SourceFile.fromCode("test.js",
        "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}", "test_original_path.js",
        "original/path/to/test.js");
    String expectedCode = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}";
    assertEquals(expectedCode, sourceFile.getCode());
    assertEquals("original/path/to/test.js", sourceFile.getOriginalPath());
  }

  @Test
  public void testFromInputStream() throws IOException {
    String testCode = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}";
    SourceFile sourceFile = SourceFile.fromInputStream("test.js",
        new ByteArrayInputStream(testCode.getBytes()));
    String expectedCode = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}";
    assertEquals(expectedCode, sourceFile.getCode());
  }

  @Test
  public void testFromReader() throws IOException {
    String testCode = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}";
    SourceFile sourceFile = SourceFile.fromReader("test.js",
        new StringReader(testCode));
    String expectedCode = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}";
    assertEquals(expectedCode, sourceFile.getCode());
  }

  @Test
  public void testFromGenerator() throws IOException {
    String testCode = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}";
    Generator generator = new SourceFile.Generator() {
      @Override
      public String getCode() {
        return testCode;
      }
    };
    SourceFile sourceFile = SourceFile.fromGenerator("test.js", generator);
    String expectedCode = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}";
    assertEquals(expectedCode, sourceFile.getCode());
  }

  @VisibleForTesting
  static SourceFile createSourceFileWithCode(String fileName, String code)
      throws IOException {
    return SourceFile.fromCode(fileName, code);
  }

  static class ByteArrayInputStream extends java.io.InputStream {

    private final byte[] data;
    private int pos = 0;

    ByteArrayInputStream(byte[] data) {
      this.data = data;
    }

    @Override
    public int read() throws IOException {
      if (this.pos < this.data.length) {
        return this.data[this.pos++];
      } else {
        return -1;
      }
    }
  }
}