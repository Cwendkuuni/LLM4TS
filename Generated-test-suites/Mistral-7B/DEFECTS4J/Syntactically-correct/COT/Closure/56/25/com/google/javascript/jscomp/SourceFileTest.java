package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SourceFileTest {

  @Test
  public void testSourceFileConstructor() {
    SourceFile sf = new SourceFile("test.js");
    assertEquals("test.js", sf.getName());
  }

  @Test
  public void testGetLineOffset() throws IOException {
    String sourceCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    SourceFile testSourceFile = new Preloaded("test.js", sourceCode);

    int numLines = testSourceFile.getNumLines();
    for (int i = 1; i <= numLines; i++) {
      int lineOffset = testSourceFile.getLineOffset(i);
      assertEquals(i * 2 - 1, lineOffset);
    }
  }

  @Test
  public void testGetLineOffsetWithInvalidLineNumber() throws IOException {
    String sourceCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    SourceFile testSourceFile = new Preloaded("test.js", sourceCode);
    assertThrows(IllegalArgumentException.class, () -> testSourceFile.getLineOffset(-1));
  }

  @Test
  public void testGetRegion() throws IOException {
    String sourceCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    SourceFile testSourceFile = new Preloaded("test.js", sourceCode);

    int lineNumber = 2;
    Region region = testSourceFile.getRegion(lineNumber);
    assertEquals("var y = 2;\n", region.getCode());
  }

  @Test
  public void testGetRegionWithInvalidLineNumber() throws IOException {
    String sourceCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    SourceFile testSourceFile = new Preloaded("test.js", sourceCode);

    int lineNumber = 0;
    assertNull(testSourceFile.getRegion(lineNumber));
  }

  @Test
  public void testFromFile() throws IOException {
    String filePath = "test.js";
    String sourceCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    File testFile = new File(filePath);
    Files.write(testFile, sourceCode.getBytes(Charsets.UTF_8));

    SourceFile testSourceFile = SourceFile.fromFile(filePath, Charsets.UTF_8);
    assertEquals("test.js", testSourceFile.getName());

    Files.delete(testFile);
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String sourceCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    SourceFile testSourceFile = new Preloaded("test.js", sourceCode);

    String newSourceCode = "function test() {\n" +
        "  var x = 2;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    testSourceFile.clearCachedSource();
    assertEquals(newSourceCode, testSourceFile.getCodeNoCache());
  }

  @Test
  public void testGetCodeWithIOException() throws IOException {
    String sourceCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    SourceFile testSourceFile = new Preloaded("test.js", sourceCode);

    IOException ioException = new IOException("Test IOException");
    when(testSourceFile.getCodeReader()).thenThrow(ioException);
    assertThrows(IOException.class, () -> testSourceFile.getCode());
  }

  @Test
  public void testFromInputStream() throws IOException {
    String sourceCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    InputStream inputStream = new ByteArrayInputStream(sourceCode.getBytes(Charsets.UTF_8));

    SourceFile testSourceFile = SourceFile.fromInputStream("test.js", inputStream);
    assertEquals("test.js", testSourceFile.getName());
    assertEquals(sourceCode, testSourceFile.getCode());
  }

  @Test
  public void testGetLineWithEndOfFile() throws IOException {
    String sourceCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    SourceFile testSourceFile = new Preloaded("test.js", sourceCode);

    int lineNumber = testSourceFile.getNumLines() + 1;
    assertNull(testSourceFile.getLine(lineNumber));
  }

  @Test
  public void testGetRegionWithEndOfFile() throws IOException {
    String sourceCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    SourceFile testSourceFile = new Preloaded("test.js", sourceCode);

    int lineNumber = testSourceFile.getNumLines() + 1;
    assertNull(testSourceFile.getRegion(lineNumber));
  }
}