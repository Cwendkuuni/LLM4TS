package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testNewSourceFile() {
    SourceFile sf = new SourceFile("test.js");
    assertEquals("test.js", sf.getName());
  }

  @Test
  public void testGetLineOffset() {
    String testCode = "function foo() {\n" +
                      "  var a = 1;\n" +
                      "  var b = 2;\n" +
                      "}";

    SourceFile sf = new Preloaded("test.js", testCode);

    for (int i = 1; i <= 4; i++) {
      int lineNumber = i;
      int lineOffset = sf.getLineOffset(lineNumber);
      assertEquals(i * 10 + 1, lineOffset);
    }

    assertThrows(IllegalArgumentException.class, () -> sf.getLineOffset(0));
    assertThrows(IllegalArgumentException.class, () -> sf.getLineOffset(5));
  }

  @Test
  public void testGetNumLines() throws IOException {
    String testCode = "function foo() {\n" +
                      "  var a = 1;\n" +
                      "  var b = 2;\n" +
                      "}";

    SourceFile sf = new Preloaded("test.js", testCode);
    assertEquals(3, sf.getNumLines());
  }

  @Test
  public void testGetCode() throws IOException {
    String testCode = "function foo() {\n" +
                      "  var a = 1;\n" +
                      "  var b = 2;\n" +
                      "}";

    SourceFile sf = new Preloaded("test.js", testCode);
    assertEquals(testCode, sf.getCode());
  }

  @Test
  public void testGetCodeReader() throws IOException {
    String testCode = "function foo() {\n" +
                      "  var a = 1;\n" +
                      "  var b = 2;\n" +
                      "}";

    SourceFile sf = new Preloaded("test.js", testCode);
    Reader reader = sf.getCodeReader();
    StringBuilder expectedCode = new StringBuilder();
    int c;

    while ((c = reader.read()) != -1) {
      expectedCode.append((char) c);
    }

    assertEquals(testCode, expectedCode.toString());
  }

  @Test
  public void testGetOriginalPath() throws IOException {
    String testCode = "function foo() {\n" +
                      "  var a = 1;\n" +
                      "  var b = 2;\n" +
                      "}";

    SourceFile sf = new Preloaded("test.js", testCode);
    assertEquals("test.js", sf.getOriginalPath());

    SourceFile sfWithOriginalPath = new Preloaded("test.js", testCode, "path/to/test.js");
    assertEquals("path/to/test.js", sfWithOriginalPath.getOriginalPath());
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String testCode = "function foo() {\n" +
                      "  var a = 1;\n" +
                      "  var b = 2;\n" +
                      "}";

    SourceFile sf = new Preloaded("test.js", testCode);
    assertEquals(testCode, sf.getCode());

    sf.clearCachedSource();
    assertNull(sf.getCode());
  }

  @Test
  public void testHasSourceInMemory() throws IOException {
    String testCode = "function foo() {\n" +
                      "  var a = 1;\n" +
                      "  var b = 2;\n" +
                      "}";

    SourceFile sf = new Preloaded("test.js", testCode);
    assertTrue(sf.hasSourceInMemory());

    sf.clearCachedSource();
    assertFalse(sf.hasSourceInMemory());
  }

  @Test
  public void testGetRegion() throws IOException {
    String testCode = "function foo() {\n" +
                      "  var a = 1;\n" +
                      "  var b = 2;\n" +
                      "}";

    SourceFile sf = new Preloaded("test.js", testCode);

    Region testRegion = sf.getRegion(3);
    assertEquals("var b = 2;\n", testRegion.getCode());
    assertEquals(3, testRegion.getStartLineNumber());
    assertEquals(3, testRegion.getEndLineNumber());

    testRegion = sf.getRegion(1);
    assertNull(testRegion);
  }

  @Test
  public void testFromFile() throws IOException {
    String testCode = "function foo() {\n" +
                      "  var a = 1;\n" +
                      "  var b = 2;\n" +
                      "}";

    File testFile = new File("test.js");
    Files.write(testFile, testCode.getBytes(Charsets.UTF_8));

    SourceFile sf = SourceFile.fromFile("test.js");
    assertEquals("test.js", sf.getName());

    Files.delete(testFile);
  }

  @Test(expected = IOException.class)
  public void testFromFileWithInvalidFile() throws IOException {
    SourceFile.fromFile("non-existent-file.js");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineWithInvalidLineNumber() throws IOException {
    String testCode = "function foo() {\n" +
                      "  var a = 1;\n" +
                      "  var b = 2;\n" +
                      "}";

    SourceFile sf = new Preloaded("test.js", testCode);
    sf.getLine(0);
  }

  @Test
  public void testGetRegionWithValidLineNumber() throws IOException {
    String testCode = "function foo() {\n" +
                      "  var a = 1;\n" +
                      "  var b = 2;\n" +
                      "}";

    SourceFile sf = new Preloaded("test.js", testCode);

    Region testRegion = sf.getRegion(3);
    assertEquals("var b = 2;\n", testRegion.getCode());
    assertEquals(3, testRegion.getStartLineNumber());
    assertEquals(3, testRegion.getEndLineNumber());
  }

  @Test
  public void testGetRegionWithInvalidLineNumber() throws IOException {
    String testCode = "function foo() {\n" +
                      "  var a = 1;\n" +
                      "  var b = 2;\n" +
                      "}";

    SourceFile sf = new Preloaded("test.js", testCode);
    assertNull(sf.getRegion(0));
  }

  @Test
  public void testGetRegionWithInvalidLineNumberAndLength() throws IOException {
    String testCode = "function foo() {\n" +
                      "  var a = 1;\n" +
                      "  var b = 2;\n" +
                      "}";

    SourceFile sf = new Preloaded("test.js", testCode);
    assertNull(sf.getRegion(0, 0));
  }
}