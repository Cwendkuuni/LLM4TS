package com.google.javascript.jscomp.test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SourceFileTest {

  @Test
  public void testFromFile() throws Exception {
    String fileName = "testFile.js";
    File file = new File(fileName);
    SourceFile sourceFile = SourceFile.fromFile(fileName);

    assertEquals(fileName, sourceFile.getName());
    assertNotNull(sourceFile.getCode());
    assertEquals(Charsets.UTF_8, sourceFile.getCharset());
    assertEquals(file.getAbsolutePath(), sourceFile.getOriginalPath());
  }

  @Test
  public void testFromFileWithCharset() throws Exception {
    String fileName = "testFile.js";
    File file = new File(fileName);
    SourceFile sourceFile = SourceFile.fromFile(fileName, Charsets.UTF_16);

    assertEquals(fileName, sourceFile.getName());
    assertNotNull(sourceFile.getCode());
    assertEquals(Charsets.UTF_16, sourceFile.getCharset());
    assertEquals(file.getAbsolutePath(), sourceFile.getOriginalPath());
  }

  @Test
  public void testFromCode() {
    String fileName = "testFile.js";
    String code = "function test() {\n" +
            "  return 1;\n" +
            "}";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);

    assertEquals(fileName, sourceFile.getName());
    assertNotNull(sourceFile.getCode());
    assertEquals(Charsets.UTF_8, sourceFile.getCharset());
    assertEquals(null, sourceFile.getOriginalPath());
  }

  @Test
  public void testGetLine() throws Exception {
    String fileName = "testFile.js";
    File file = new File(fileName);
    SourceFile sourceFile = SourceFile.fromFile(fileName);

    String line1 = "function test() {\n";
    String line2 = "  return 1;\n}";

    assertEquals(line1, sourceFile.getLine(1));
    assertEquals(line2, sourceFile.getLine(2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineWithNegativeNumber() throws Exception {
    String fileName = "testFile.js";
    SourceFile sourceFile = SourceFile.fromFile(fileName);
    sourceFile.getLine(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineWithNumberGreaterThanNumberOfLines() throws Exception {
    String fileName = "testFile.js";
    SourceFile sourceFile = SourceFile.fromFile(fileName);
    sourceFile.getLine(Integer.MAX_VALUE);
  }

  @Test
  public void testGetRegion() throws Exception {
    String fileName = "testFile.js";
    File file = new File(fileName);
    SourceFile sourceFile = SourceFile.fromFile(fileName);

    String expectedRegion = "function test() {\n" +
            "  return 1;\n" +
            "}\n" +
            "function anotherFunction() {\n" +
            "  return 2;\n" +
            "}";

    Region region = sourceFile.getRegion(5);
    assertEquals(expectedRegion, region.getCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRegionWithNegativeNumber() throws Exception {
    String fileName = "testFile.js";
    SourceFile sourceFile = SourceFile.fromFile(fileName);
    sourceFile.getRegion(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRegionWithNumberGreaterThanNumberOfLines() throws Exception {
    String fileName = "testFile.js";
    SourceFile sourceFile = SourceFile.fromFile(fileName);
    sourceFile.getRegion(Integer.MAX_VALUE);
  }

  @Test
  public void testClearCachedSource() throws Exception {
    String fileName = "testFile.js";
    File file = new File(fileName);
    SourceFile sourceFile = SourceFile.fromFile(fileName);

    String originalCode = sourceFile.getCode();
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
    assertEquals(originalCode, Files.toString(file, Charsets.UTF_8));
  }

  @Test
  public void testSetOriginalPath() throws Exception {
    String fileName = "testFile.js";
    File file = new File(fileName);
    SourceFile sourceFile = SourceFile.fromFile(fileName);

    sourceFile.setOriginalPath("/path/to/testFile.js");
    assertEquals("/path/to/testFile.js", sourceFile.getOriginalPath());
  }

  @Test
  public void testFromInputStream() throws Exception {
    String fileName = "testFile.js";
    String code = "function test() {\n" +
            "  return 1;\n}";
    InputStream inputStream = new ByteArrayInputStream(code.getBytes());
    SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream);

    assertEquals(fileName, sourceFile.getName());
    assertNotNull(sourceFile.getCode());
    assertEquals(Charsets.UTF_8, sourceFile.getCharset());
    assertEquals(null, sourceFile.getOriginalPath());
  }

  @Test
  public void testFromGenerator() throws Exception {
    String fileName = "testFile.js";
    String code = "function test() {\n" +
            "  return 1;\n}";
    Generator generator = () -> code;
    SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);

    assertEquals(fileName, sourceFile.getName());
    assertNotNull(sourceFile.getCode());
    assertEquals(Charsets.UTF_8, sourceFile.getCharset());
    assertEquals(null, sourceFile.getOriginalPath());
  }

  @Test
  public void testIsExtern() throws Exception {
    String fileName = "testFile.js";
    SourceFile sourceFile = SourceFile.fromFile(fileName);
    assertFalse(sourceFile.isExtern());

    fileName = "javaFile.java";
    SourceFile javaSourceFile = SourceFile.fromFile(fileName);
    assertTrue(javaSourceFile.isExtern());
  }

  @Test
  public void testSetIsExtern() throws Exception {
    String fileName = "testFile.js";
    SourceFile sourceFile = SourceFile.fromFile(fileName);
    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testGetNumLines() throws Exception {
    String fileName = "testFile.js";
    File file = new File(fileName);
    SourceFile sourceFile = SourceFile.fromFile(fileName);

    List<String> lines = Files.readAllLines(file.toPath(), Charsets.UTF_8);
    int numLines = lines.size();

    assertEquals(numLines, sourceFile.getNumLines());
  }

  @Test
  public void testGetLineOffset() throws Exception {
    String fileName = "testFile.js";
    File file = new File(fileName);
    SourceFile sourceFile = SourceFile.fromFile(fileName);

    List<String> lines = Files.readAllLines(file.toPath(), Charsets.UTF_8);
    int numLines = lines.size();

    for (int i = 0; i < numLines; i++) {
      String line = lines.get(i);
      int lineLength = line.length();
      int lineNumber = i + 1;

      int lineOffset = sourceFile.getLineOffset(lineNumber);
      assertEquals(lineOffset, lineLength + i);
    }
  }
}