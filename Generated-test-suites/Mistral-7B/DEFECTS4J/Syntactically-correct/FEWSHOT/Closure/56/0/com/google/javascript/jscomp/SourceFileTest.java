package com.google.javascript.jscomp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

public class SourceFileTest {
  @Test
  public void testCreateSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertEquals("test.js", sourceFile.getName());
    assertEquals(1, sourceFile.getLineOffset(1));
    assertNull(sourceFile.getCode());
  }

  @Test
  public void testGetCodeFromFile() throws IOException {
    String expectedCode = Files.toString(new File("path/to/file.js"), Charsets.UTF_8);
    SourceFile sourceFile = new SourceFile("file.js");
    sourceFile.setCode(expectedCode);
    assertEquals(expectedCode, sourceFile.getCode());
  }

  @Test
  public void testGetCodeFromString() {
    String expectedCode = "function test() { return 42; }";
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.setCode(expectedCode);
    assertEquals(expectedCode, sourceFile.getCode());
  }

  @Test
  public void testGetRegion() {
    String expectedCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  var z = x + y;\n" +
        "  return z;\n" +
        "}";
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.setCode(expectedCode);

    int lineNumber = 3;
    Region region = sourceFile.getRegion(lineNumber);
    String expectedRegionCode = "  var y = 2;\n" +
        "  var z = x + y;\n";
    assertEquals(lineNumber, region.getStartLineNumber());
    assertEquals(lineNumber + 1, region.getEndLineNumber());
    assertEquals(expectedRegionCode, region.getCode());
  }

  @Test
  public void testLineOffsets() throws IOException {
    String expectedCode = "function test() {\n" +
        "  return 42;\n" +
        "}";
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.setCode(expectedCode);

    int[] lineOffsets = sourceFile.getLineOffsets();
    assertArrayEquals(new int[] { 0, 10, 12 }, lineOffsets);
  }

  @Test
  public void testSetOriginalPath() {
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.setOriginalPath("/path/to/original/file.js");
    assertEquals("/path/to/original/file.js", sourceFile.getOriginalPath());
  }

  @Test
  public void testClearCachedSource() {
    String expectedCode = "function test() {\n" +
        "  return 42;\n" +
        "}";
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.setCode(expectedCode);

    String newCode = "function test() {\n" +
        "  return 43;\n" +
        "}";
    sourceFile.setCode(newCode);
    assertEquals(newCode, sourceFile.getCode());

    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
  }

  @Test
  public void testHasSourceInMemory() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertFalse(sourceFile.hasSourceInMemory());

    String expectedCode = "function test() {\n" +
        "  return 42;\n" +
        "}";
    sourceFile.setCode(expectedCode);
    assertTrue(sourceFile.hasSourceInMemory());
  }

  @Test
  public void testToString() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertEquals("test.js", sourceFile.toString());
  }

  @Test
  public void testGetLine() {
    String expectedCode = "function test() {\n" +
        "  return 42;\n" +
        "}";
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.setCode(expectedCode);

    int lineNumber = 1;
    String expectedLine = "function test() {\n";
    String line = sourceFile.getLine(lineNumber);
    assertEquals(expectedLine, line);
  }

  @Test
  public void testGetLineNull() {
    SourceFile sourceFile = new SourceFile("test.js");
    String line = sourceFile.getLine(100);
    assertNull(line);
  }

  @Test
  public void testGetRegionNull() {
    SourceFile sourceFile = new SourceFile("test.js");
    Region region = sourceFile.getRegion(100);
    assertNull(region);
  }
}