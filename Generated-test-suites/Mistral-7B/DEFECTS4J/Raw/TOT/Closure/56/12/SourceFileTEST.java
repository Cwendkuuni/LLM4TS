package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testGetLine() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js");
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    sourceFile.setCode(code);

    String expectedLine1 = "function test() {\n";
    String expectedLine2 = "  var x = 1;\n";
    String expectedLine3 = "  var y = 2;\n";
    String expectedLine4 = "  return x + y;\n";

    assertEquals(expectedLine1, sourceFile.getLine(1));
    assertEquals(expectedLine2, sourceFile.getLine(2));
    assertEquals(expectedLine3, sourceFile.getLine(3));
    assertEquals(expectedLine4, sourceFile.getLine(4));
  }

  @Test
  public void testGetLineNull() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js");
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    sourceFile.setCode(code);

    assertNull(sourceFile.getLine(5));
  }

  @Test
  public void testGetLineIOException() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js");
    assertNull(sourceFile.getLine(1));
  }

  @Test
  public void testGetRegion() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js");
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    sourceFile.setCode(code);

    Region expectedRegion = new Region(1, 1, "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n");

    assertEquals(expectedRegion, sourceFile.getRegion(1));
  }

  @Test
  public void testGetRegionNull() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js");
    assertNull(sourceFile.getRegion(1));
  }

  @Test
  public void testGetRegionIOException() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js");
    assertNull(sourceFile.getRegion(1));
  }
}