package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

/**
 * Unit tests for the SourceFile class.
 */
public class SourceFileTest {

  @Test
  public void testSourceFileConstructor() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertEquals("test.js", sourceFile.getName());
    assertNull(sourceFile.getOriginalPath());
  }

  @Test
  public void testGetLineOffset() throws IOException {
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";

    SourceFile sourceFile = new Preloaded("test.js", code);
    int lineNumber = 2;
    int lineOffset = sourceFile.getLineOffset(lineNumber);
    assertEquals(26, lineOffset);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetInvalidLineNumber() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "");
    int lineNumber = -1;
    sourceFile.getLineOffset(lineNumber);
  }

  @Test
  public void testGetCode() throws IOException {
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";

    SourceFile sourceFile = new Preloaded("test.js", code);
    String actualCode = sourceFile.getCode();
    assertEquals(code, actualCode);
  }

  @Test
  public void testGetCodeReader() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "");
    Reader reader = sourceFile.getCodeReader();
    String actualCode = CharStreams.toString(reader);
    assertEquals("", actualCode);
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";

    SourceFile sourceFile = new Preloaded("test.js", code);
    int numLines = sourceFile.getNumLines();
    assertEquals(3, numLines);
  }

  @Test
  public void testGetRegion() throws IOException {
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";

    SourceFile sourceFile = new Preloaded("test.js", code);
    Region region = sourceFile.getRegion(3);
    String expectedRegion = "return x;\n";
    assertEquals(expectedRegion, region.getCode());
  }

  @Test
  public void testFromFile() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";

    SourceFile sourceFile = SourceFile.fromFile(fileName, Charsets.UTF_8);
    String actualCode = sourceFile.getCode();
    assertEquals(code, actualCode);
  }

  @Test
  public void testFromCode() {
    String fileName = "test.js";
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    String actualCode = sourceFile.getCode();
    assertEquals(code, actualCode);
  }

  @Test
  public void testFromInputStream() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";

    InputStream inputStream = new ByteArrayInputStream(code.getBytes());
    SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream);
    String actualCode = sourceFile.getCode();
    assertEquals(code, actualCode);
  }

  @Test
  public void testFromReader() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";

    Reader reader = new StringReader(code);
    SourceFile sourceFile = SourceFile.fromReader(fileName, reader);
    String actualCode = sourceFile.getCode();
    assertEquals(code, actualCode);
  }

  @Test
  public void testFromGenerator() {
    String fileName = "test.js";
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";

    Generator generator = () -> code;
    SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);
    String actualCode = sourceFile.getCode();
    assertEquals(code, actualCode);
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";

    SourceFile sourceFile = SourceFile.fromFile(fileName, Charsets.UTF_8);
    String originalCode = sourceFile.getCode();
    sourceFile.clearCachedSource();
    String actualCode = sourceFile.getCode();
    assertNotEquals(originalCode, actualCode);
  }
}