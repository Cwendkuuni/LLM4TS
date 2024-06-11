package com.google.javascript.jscomp.tests;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

/**
 * Tests for the {@link SourceFile} class.
 */
public class SourceFileTest {

  @Test
  public void testBasicUsage() throws IOException {
    String fileName = "TestFile.js";
    Charset charset = Charsets.UTF_8;
    Generator generator = () -> "function test() { return 42; }";

    SourceFile sourceFile = new SourceFile(fileName);
    sourceFile.setGenerator(generator);

    assertEquals("function test() { return 42; }", sourceFile.getCode());
    assertEquals("TestFile.js", sourceFile.getName());
    assertFalse(sourceFile.isExtern());
    assertEquals(1, sourceFile.getNumLines());

    assertEquals(0, sourceFile.getLineOffset(1));
    assertEquals("function test() { return 42; }", sourceFile.getLine(1));

    assertEquals(new SimpleRegion(1, 1, "function test() { return 42; }\n"),
        sourceFile.getRegion(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullFileName() {
    new SourceFile(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyFileName() {
    new SourceFile("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeLineNumber() {
    SourceFile sourceFile = new SourceFile("TestFile.js");
    sourceFile.setCode("function test() { return 42; }");
    sourceFile.setLineOffsets(new int[] { 0, 10, 20, 30 });

    int lineNumber = -1;
    sourceFile.getLineOffset(lineNumber);
  }

  @Test
  public void testFromFile() throws IOException {
    String fileName = "TestFile.js";
    File file = new File(fileName);
    Charset charset = Charsets.UTF_8;

    SourceFile sourceFile = SourceFile.fromFile(fileName, charset);
    assertEquals(fileName, sourceFile.getName());
    assertTrue(sourceFile.hasSourceInMemory());

    String code = sourceFile.getCode();
    assertEquals("function test() { return 42; }", code);

    // Test line and region queries
    assertEquals(0, sourceFile.getLineOffset(1));
    assertEquals("function test() { return 42; }", sourceFile.getLine(1));
    assertEquals(
        new SimpleRegion(1, 1, "function test() { return 42; }\n"),
        sourceFile.getRegion(1));
  }

  @Test
  public void testFromFileWithOriginalPath() throws IOException {
    String fileName = "TestFile.js";
    File file = new File(fileName);
    Charset charset = Charsets.UTF_8;

    SourceFile sourceFile = SourceFile.fromFile(fileName, file.getAbsolutePath());
    assertEquals("TestFile.js", sourceFile.getName());
    assertEquals(file.getAbsolutePath(), sourceFile.getOriginalPath());
    assertTrue(sourceFile.hasSourceInMemory());

    String code = sourceFile.getCode();
    assertEquals("function test() { return 42; }", code);

    // Test line and region queries
    assertEquals(0, sourceFile.getLineOffset(1));
    assertEquals("function test() { return 42; }", sourceFile.getLine(1));
    assertEquals(
        new SimpleRegion(1, 1, "function test() { return 42; }\n"),
        sourceFile.getRegion(1));
  }

  @Test
  public void testFromCode() throws IOException {
    String fileName = "TestFile.js";
    String code = "function test() { return 42; }";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    assertEquals(fileName, sourceFile.getName());
    assertFalse(sourceFile.hasSourceInMemory());

    assertEquals("function test() { return 42; }", sourceFile.getCode());

    // Test line and region queries
    assertEquals(0, sourceFile.getLineOffset(1));
    assertEquals("function test() { return 42; }", sourceFile.getLine(1));
    assertEquals(
        new SimpleRegion(1, 1, "function test() { return 42; }\n"),
        sourceFile.getRegion(1));
  }

  @Test
  public void testFromCodeWithOriginalPath() throws IOException {
    String fileName = "TestFile.js";
    String code = "function test() { return 42; }";
    String originalPath = "/path/to/TestFile.js";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code, originalPath);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(originalPath, sourceFile.getOriginalPath());
    assertFalse(sourceFile.hasSourceInMemory());

    assertEquals("function test() { return 42; }", sourceFile.getCode());

    // Test line and region queries
    assertEquals(0, sourceFile.getLineOffset(1));
    assertEquals("function test() { return 42; }", sourceFile.getLine(1));
    assertEquals(
        new SimpleRegion(1, 1, "function test() { return 42; }\n"),
        sourceFile.getRegion(1));
  }

  @Test
  public void testFromInputStream() throws IOException {
    String fileName = "TestFile.js";
    String code = "function test() { return 42; }";
    File file = new File(fileName);
    InputStream inputStream = new ByteArrayInputStream(code.getBytes());
    Charset charset = Charsets.UTF_8;

    SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream, charset);
    assertEquals(fileName, sourceFile.getName());
    assertFalse(sourceFile.hasSourceInMemory());

    assertEquals("function test() { return 42; }", sourceFile.getCode());

    // Test line and region queries
    assertEquals(0, sourceFile.getLineOffset(1));
    assertEquals("function test() { return 42; }", sourceFile.getLine(1));
    assertEquals(
        new SimpleRegion(1, 1, "function test() { return 42; }\n"),
        sourceFile.getRegion(1));

    // Test clearing the cache
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
  }

  @Test
  public void testFromReader() throws IOException {
    String fileName = "TestFile.js";
    String code = "function test() { return 42; }";
    File file = new File(fileName);
    Reader reader = new StringReader(code);
    Charset charset = Charsets.UTF_8;

    SourceFile sourceFile = SourceFile.fromReader(fileName, reader, charset);
    assertEquals(fileName, sourceFile.getName());
    assertFalse(sourceFile.hasSourceInMemory());

    assertEquals("function test() { return 42; }", sourceFile.getCode());

    // Test line and region queries
    assertEquals(0, sourceFile.getLineOffset(1));
    assertEquals("function test() { return 42; }", sourceFile.getLine(1));
    assertEquals(
        new SimpleRegion(1, 1, "function test() { return 42; }\n"),
        sourceFile.getRegion(1));

    // Test clearing the cache
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
  }

  @Test
  public void testGeneratedSource() throws IOException {
    String fileName = "TestFile.js";
    Generator generator = () -> "function test() { return 42; }";

    SourceFile sourceFile = new SourceFile(fileName) {
      @Override
      public Generator getGenerator() {
        return generator;
      }
    };

    assertEquals("function test() { return 42; }", sourceFile.getCode());

    // Test line and region queries
    assertEquals(0, sourceFile.getLineOffset(1));
    assertEquals("function test() { return 42; }", sourceFile.getLine(1));
    assertEquals(
        new SimpleRegion(1, 1, "function test() { return 42; }\n"),
        sourceFile.getRegion(1));

    // Test clearing the cache
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String fileName = "TestFile.js";
    String code = "function test() { return 42; }";
    File file = new File(fileName);

    SourceFile sourceFile = SourceFile.fromFile(fileName);
    sourceFile.setCode(code);

    assertEquals("function test() { return 42; }", sourceFile.getCode());

    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
  }

  @Test
  public void testSetOriginalPath() throws IOException {
    String fileName = "TestFile.js";
    File file = new File(fileName);

    SourceFile sourceFile = SourceFile.fromFile(fileName);
    sourceFile.setOriginalPath("/path/to/TestFile.js");

    assertEquals("/path/to/TestFile.js", sourceFile.getOriginalPath());
  }

  @Test
  public void testSetOriginalPathNull() throws IOException {
    String fileName = "TestFile.js";
    File file = new File(fileName);

    SourceFile sourceFile = SourceFile.fromFile(fileName);
    sourceFile.setOriginalPath(null);

    assertNull(sourceFile.getOriginalPath());
  }

  @Test
  public void testSetOriginalPathEmptyString() throws IOException {
    String fileName = "TestFile.js";
    File file = new File(fileName);

    SourceFile sourceFile = SourceFile.fromFile(fileName);
    sourceFile.setOriginalPath("");

    assertEquals("", sourceFile.getOriginalPath());
  }
}