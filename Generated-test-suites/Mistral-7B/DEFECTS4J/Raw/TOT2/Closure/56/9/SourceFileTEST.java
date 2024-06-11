package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.javascript.jscomp.SourceFile;

/**
 * Test class for SourceFile.
 */
public class SourceFileTest {

  /**
   * Test constructor with valid fileName.
   */
  @Test
  public void testConstructorWithValidFileName() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertEquals("test.js", sourceFile.getName());
  }

  /**
   * Test getLine method with valid line number.
   */
  @Test
  public void testGetLineWithValidLineNumber() throws IOException {
    String testCode = "function testFunction() {\n" +
        "  var x = 1;\n" +
        "}\n";
    SourceFile sourceFile = new Preloaded("test.js", testCode);
    String expected = "function testFunction() {\n" +
        "  var x = 1;\n" +
        "}";
    String result = sourceFile.getLine(1);
    assertEquals(expected, result);
  }

  /**
   * Test getLine method with invalid line number.
   */
  @Test
  public void testGetLineWithInvalidLineNumber() throws IOException {
    String testCode = "function testFunction() {\n" +
        "  var x = 1;\n" +
        "}\n";
    SourceFile sourceFile = new Preloaded("test.js", testCode);
    String expectedException = "Expected line number between 1 and " +
        sourceFile.getNumLines() + "\nActual: 0";
    try {
      sourceFile.getLine(0);
    } catch (IllegalArgumentException e) {
      assertEquals(expectedException, e.getMessage());
    }
  }

  /**
   * Test getLineOffset method with valid line number.
   */
  @Test
  public void testGetLineOffsetWithValidLineNumber() throws IOException {
    String testCode = "function testFunction() {\n" +
        "  var x = 1;\n" +
        "}\n";
    SourceFile sourceFile = new Preloaded("test.js", testCode);
    int expected = 1;
    int result = sourceFile.getLineOffset(1);
    assertEquals(expected, result);
  }

  /**
   * Test getLineOffset method with invalid line number.
   */
  @Test
  public void testGetLineOffsetWithInvalidLineNumber() throws IOException {
    String testCode = "function testFunction() {\n" +
        "  var x = 1;\n" +
        "}\n";
    SourceFile sourceFile = new Preloaded("test.js", testCode);
    int expectedException = 1;
    int result = sourceFile.getLineOffset(expectedException);
    assertEquals(-1, result);
  }

  /**
   * Test getLine method with empty code.
   */
  @Test
  public void testGetLineWithEmptyCode() throws IOException {
    SourceFile sourceFile = new OnDisk("test.js");
    String expected = "";
    String result = sourceFile.getLine(1);
    assertEquals(expected, result);
  }

  /**
   * Test getRegion method with valid line number.
   */
  @Test
  public void testGetRegionWithValidLineNumber() throws IOException {
    String testCode = "function testFunction1() {\n" +
        "  var x = 1;\n" +
        "}\n\n" +
        "function testFunction2() {\n" +
        "  var y = 2;\n" +
        "}\n";
    SourceFile sourceFile = new Preloaded("test.js", testCode);
    String expected = "function testFunction1() {\n" +
        "  var x = 1;\n" +
        "}\n";
    Region result = sourceFile.getRegion(1);
    assertEquals(expected.getStartLine(), result.getStartLine());
    assertEquals(expected.getEndLine(), result.getEndLine());
    assertEquals(expected, result.getCode());
  }

  /**
   * Test getRegion method with invalid line number.
   */
  @Test
  public void testGetRegionWithInvalidLineNumber() throws IOException {
    String testCode = "function testFunction1() {\n" +
        "  var x = 1;\n" +
        "}\n\n" +
        "function testFunction2() {\n" +
        "  var y = 2;\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", testCode);
    String expectedException = "Expected line number between 1 and " +
        sourceFile.getNumLines() + "\nActual: 0";
    try {
      sourceFile.getRegion(0);
    } catch (IllegalArgumentException e) {
      assertEquals(expectedException, e.getMessage());
    }
  }

  /**
   * Test getRegion method with empty code.
   */
  @Test
  public void testGetRegionWithEmptyCode() throws IOException {
    SourceFile sourceFile = new OnDisk("test.js");
    Region expected = null;
    Region result = sourceFile.getRegion(1);
    assertEquals(expected, result);
  }

  /**
   * Test isExtern method with true value.
   */
  @Test
  public void testIsExternWithTrueValue() {
    SourceFile sourceFile = new Preloaded("test.js", "");
    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  /**
   * Test isExtern method with false value.
   */
  @Test
  public void testIsExternWithFalseValue() {
    SourceFile sourceFile = new Preloaded("test.js", "");
    assertFalse(sourceFile.isExtern());
  }

  /**
   * Test clearCachedSource method with preloaded code.
   */
  @Test
  public void testClearCachedSourceWithPreloadedCode() throws IOException {
    String testCode = "function testFunction() {\n" +
        "  var x = 1;\n" +
        "}\n";
    SourceFile sourceFile = new Preloaded("test.js", testCode);
    String expected = "function testFunction() {\n" +
        "  var x = 1;\n" +
        "}";
    String previousCode = sourceFile.getCode();
    assertEquals(expected, previousCode);
    sourceFile.clearCachedSource();
    String newCode = sourceFile.getCode();
    assertNotSame(previousCode, newCode);
  }

  /**
   * Test clearCachedSource method with on disk code.
   */
  @Test
  public void testClearCachedSourceWithOnDiskCode() throws IOException {
    String testCode = "function testFunction() {\n" +
        "  var x = 1;\n" +
        "}";
    SourceFile sourceFile = new OnDisk("test.js");
    String expected = "function testFunction() {\n" +
        "  var x = 1;\n" +
        "}";
    String previousCode = sourceFile.getCode();
    assertEquals(expected, previousCode);
    sourceFile.clearCachedSource();
    String newCode = sourceFile.getCode();
    assertNotSame(previousCode, newCode);
  }

  /**
   * Test toString method.
   */
  @Test
  public void testToString() {
    SourceFile sourceFile = new Preloaded("test.js", "");
    assertEquals("test.js", sourceFile.toString());
  }

  /**
   * Test fromFile method with valid file path.
   */
  @Test
  public void testFromFileWithValidFilePath() throws IOException {
    String testFilePath = "/test/test.js";
    SourceFile sourceFile = SourceFile.fromFile(testFilePath);
    assertEquals("test.js", sourceFile.getName());
    try (Reader reader = Files.newReader(new File(testFilePath),
        Charsets.UTF_8)) {
      String expectedCode = CharStreams.toString(reader);
      String actualCode = sourceFile.getCode();
      assertEquals(expectedCode, actualCode);
    }
  }

  /**
   * Test fromFile method with invalid file path.
   */
  @Test(expected = FileNotFoundException.class)
  public void testFromFileWithInvalidFilePath() throws IOException {
    String testFilePath = "/test/nonexistentfile.js";
    SourceFile.fromFile(testFilePath);
  }
}