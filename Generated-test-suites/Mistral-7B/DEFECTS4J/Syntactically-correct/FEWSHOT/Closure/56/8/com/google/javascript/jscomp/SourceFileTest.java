package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testEmptySourceFile() {
    SourceFile sourceFile = new SourceFile("testEmptySourceFile.js");
    assertNull(sourceFile.getCode());
    assertEquals(0, sourceFile.getNumLines());
  }

  @Test
  public void testSourceFileFromString() throws IOException {
    String code = "function foo() {\n" + "  // This is a test comment.\n" +
        "  return 42;\n" + "}";
    SourceFile sourceFile = new SourceFile("testSourceFileFromString.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    assertEquals(code, sourceFile.getCode());
    assertEquals(3, sourceFile.getNumLines());
  }

  @Test
  public void testSourceFileFromFile() throws IOException {
    String testFilePath = "testSourceFileFromFile.js";
    String code = "function foo() {\n" + "  // This is a test comment.\n" +
        "  return 42;\n" + "}";
    Files.write(new File(testFilePath), code.getBytes());
    try {
      SourceFile sourceFile = SourceFile.fromFile(testFilePath);
      assertEquals(code, sourceFile.getCode());
      assertEquals(3, sourceFile.getNumLines());
    } finally {
      Files.delete(new File(testFilePath));
    }
  }

  @Test
  public void testSourceFileFromInputStream() throws IOException {
    String testFilePath = "testSourceFileFromInputStream.js";
    String code = "function foo() {\n" + "  // This is a test comment.\n" +
        "  return 42;\n" + "}";
    try (FileOutputStream outputStream = new FileOutputStream(testFilePath)) {
      outputStream.write(code.getBytes());
      outputStream.flush();
    }
    try (InputStream inputStream = new FileInputStream(testFilePath)) {
      SourceFile sourceFile = SourceFile.fromInputStream(testFilePath, inputStream);
      assertEquals(code, sourceFile.getCode());
      assertEquals(3, sourceFile.getNumLines());
    } finally {
      Files.delete(new File(testFilePath));
    }
  }

  @Test
  public void testRegion() throws IOException {
    String testFilePath = "testRegion.js";
    String code = "function foo() {\n" +
        "  // This is a test comment.\n" +
        "  var bar = 42;\n" +
        "  function baz() {\n" +
        "    return 13;\n" +
        "  }\n" +
        "}";
    try (FileOutputStream outputStream = new FileOutputStream(testFilePath)) {
      outputStream.write(code.getBytes());
      outputStream.flush();
    }
    SourceFile sourceFile = SourceFile.fromFile(testFilePath);
    Region region = sourceFile.getRegion(5);
    assertTrue(region != null);
    assertEquals("  function baz() {\n" +
        "    return 13;\n" +
        "  }\n", region.getCode());
    assertEquals(3, region.getStartLineNumber());
    assertEquals(5, region.getEndLineNumber());
  }

  @Test
  public void testGetLine() throws IOException {
    String testFilePath = "testGetLine.js";
    String code = "function foo() {\n" +
        "  var bar = 42;\n" +
        "  function baz() {\n" +
        "    return 13;\n" +
        "  }\n" +
        "}";
    try (FileOutputStream outputStream = new FileOutputStream(testFilePath)) {
      outputStream.write(code.getBytes());
      outputStream.flush();
    }
    SourceFile sourceFile = SourceFile.fromFile(testFilePath);
    String line1 = sourceFile.getLine(1);
    String line2 = sourceFile.getLine(2);
    String line3 = sourceFile.getLine(3);
    String line4 = sourceFile.getLine(4);
    assertNull(line1);
    assertEquals("function foo() {\n", line2);
    assertEquals("  var bar = 42;\n", line3);
    assertEquals("  function baz() {\n", line4);
  }

  @Test
  public void testLineOffsets() throws IOException {
    String testFilePath = "testLineOffsets.js";
    String code = "function foo() {\n" +
        "  var bar = 42;\n" +
        "  function baz() {\n" +
        "    return 13;\n" +
        "  }\n" +
        "}";
    try (FileOutputStream outputStream = new FileOutputStream(testFilePath)) {
      outputStream.write(code.getBytes());
      outputStream.flush();
    }
    SourceFile sourceFile = SourceFile.fromFile(testFilePath);
    int[] lineOffsets = sourceFile.getLineOffsets();
    assertEquals(4, lineOffsets.length);
    assertEquals(1, lineOffsets[0]);
    assertEquals(11, lineOffsets[1]);
    assertEquals(36, lineOffsets[2]);
    assertEquals(62, lineOffsets[3]);
  }

  @Test
  public void testSetOriginalPath() throws IOException {
    String testFilePath = "testSetOriginalPath.js";
    String originalPath = "testOriginalPath";
    String code = "function foo() {\n" +
        "  var bar = 42;\n" +
        "  function baz() {\n" +
        "    return 13;\n" +
        "  }\n" +
        "}";
    try (FileOutputStream outputStream = new FileOutputStream(testFilePath)) {
      outputStream.write(code.getBytes());
      outputStream.flush();
    }
    SourceFile sourceFile = SourceFile.fromFile(testFilePath);
    sourceFile.setOriginalPath(originalPath);
    assertEquals(originalPath, sourceFile.getOriginalPath());
  }
}