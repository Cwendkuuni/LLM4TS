package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

public class SourceFileTest {
  private SourceFile sourceFile;

  @Before
  public void setup() {
    sourceFile = new SourceFile("testFile.js");
  }

  @Test
  public void testGetLineOffset() {
    assertEquals(0, sourceFile.getLineOffset(1));
    assertEquals(1, sourceFile.getLineOffset(2));
    assertNull(sourceFile.getLineOffset(0));
    assertNull(sourceFile.getLineOffset(100));
  }

  @Test
  public void testGetNumLines() {
    int numLines = sourceFile.getNumLines();
    String code = sourceFile.getCode();
    String[] lines = code.split("\n");
    assertEquals(lines.length, numLines);
  }

  @Test
  public void testGetCode() throws IOException {
    String expectedCode = "function test() {\n" +
            "  // some code here\n" +
            "}";
    sourceFile.setCode(expectedCode);
    String actualCode = sourceFile.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetWithInvalidLineNumber() {
    sourceFile.getLineOffset(0);
  }

  @Test
  public void testGetRegion() {
    String expectedRegion = "function test() {\n" +
            "  // some code here\n" +
            "}\n" +
            "\n" +
            "function main() {\n" +
            "  test();\n" +
            "}";
    sourceFile.setCode(expectedRegion);
    Region region = sourceFile.getRegion(3);
    String actualRegion = region.getCode();
    assertEquals(expectedRegion.substring(0, 62), actualRegion);
  }

  @Test
  public void testGetRegionWithInvalidLineNumber() {
    String expectedRegion = "function test() {\n" +
            "  // some code here\n" +
            "}\n" +
            "\n" +
            "function main() {\n" +
            "  test();\n" +
            "}";
    sourceFile.setCode(expectedRegion);
    Region region = sourceFile.getRegion(5);
    assertNull(region);
  }

  @Test
  public void testGetOriginalPath() {
    sourceFile.setOriginalPath("test/testFile.js");
    assertEquals("test/testFile.js", sourceFile.getOriginalPath());
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    String code = "function test() {\n" +
            "  // some code here\n" +
            "}";
    sourceFile.setCode(code);

    int[] lineOffsets = sourceFile.getLineOffsets();
    assertEquals(12, lineOffsets.length);
    assertEquals(0, lineOffsets[0]);
    assertEquals(12, lineOffsets[11]);
  }
}