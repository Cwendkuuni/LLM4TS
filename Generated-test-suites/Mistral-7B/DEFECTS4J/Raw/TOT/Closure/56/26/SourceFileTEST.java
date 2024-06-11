package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;

public class SourceFileTest {

  @Test
  public void testGetLine() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js");
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "}\n";
    sourceFile.setCode(code);

    assertEquals("function test() {\n", sourceFile.getLine(1));
    assertEquals("  var x = 1;\n", sourceFile.getLine(2));
    assertEquals("  var y = 2;\n", sourceFile.getLine(3));
    assertNull(sourceFile.getLine(5));
  }

  @Test
  public void testGetRegion() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js");
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  var z = 3;\n" +
        "}\n";
    sourceFile.setCode(code);

    Region region = sourceFile.getRegion(3);
    assertEquals(3, region.getStartLineNumber());
    assertEquals(3, region.getEndLineNumber());
    assertEquals("  var z = 3;\n", region.getSource());
  }

  @Test
  public void testGetLineAndRegionFromFile() throws IOException {
    String testFilePath = "/path/to/test.js";
    File testFile = new File(testFilePath);
    Charset charset = Charsets.UTF_8;

    SourceFile sourceFile = new SourceFile(testFile, charset);

    String expectedLine1 = "function test() {\n";
    String expectedLine3 = "  var x = 1;\n";
    String expectedRegion = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  var z = 3;\n" +
        "}\n";

    assertEquals(expectedLine1, sourceFile.getLine(1));
    Region region = sourceFile.getRegion(3);
    assertEquals(expectedRegion, region.getSource());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeLineNumber() {
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.getLine(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLineNumberGreaterThanNumberOfLines() {
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.setCode("function test() {\n}");
    sourceFile.getLine(3);
  }
}