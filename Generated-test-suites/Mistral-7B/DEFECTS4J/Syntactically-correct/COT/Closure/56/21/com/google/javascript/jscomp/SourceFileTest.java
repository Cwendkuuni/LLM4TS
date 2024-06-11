package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SourceFileTest {
  // Given
  private SourceFile sourceFile;

  @Before
  public void setUp() throws Exception {
    sourceFile = new SourceFile("testFile.js");
  }

  @After
  public void tearDown() throws Exception {
    sourceFile = null;
  }

  // Testing getName() method
  @Test
  public void testGetName() {
    assertEquals("testFile.js", sourceFile.getName());
  }

  // Testing isExtern() method with default value
  @Test
  public void testIsExternDefaultValue() {
    assertFalse(sourceFile.isExtern());
  }

  // Testing isExtern() method with set value
  @Test
  public void testSetIsExtern() {
    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  // Testing getLineOffset() method
  @Test
  public void testGetLineOffset() {
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    sourceFile.setCode(jsCode);

    int line1 = sourceFile.getLineOffset(1);
    int line3 = sourceFile.getLineOffset(3);

    assertEquals(1, line1);
    assertEquals(5, line3);
  }

  // Testing getNumLines() method
  @Test
  public void testGetNumLines() {
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    sourceFile.setCode(jsCode);

    int numLines = sourceFile.getNumLines();

    assertEquals(4, numLines);
  }

  // Testing getLine() method with valid input
  @Test
  public void testGetLineValidInput() {
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    sourceFile.setCode(jsCode);

    String line1 = sourceFile.getLine(1);
    String line3 = sourceFile.getLine(3);

    assertEquals("function test() {", line1);
    assertEquals("  var y = 2;\n  return x + y;\n", line3);
  }

  // Testing getLine() method with invalid input
  @Test(expected = IllegalArgumentException.class)
  public void testGetLineInvalidInput() {
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    sourceFile.setCode(jsCode);

    String line0 = sourceFile.getLine(0);
  }

  // Testing getRegion() method with valid input
  @Test
  public void testGetRegionValidInput() {
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    sourceFile.setCode(jsCode);

    Region region = sourceFile.getRegion(4);

    assertNull(region);

    region = sourceFile.getRegion(2);

    assertEquals(2, region.getStartLineNumber());
    assertEquals(2, region.getEndLineNumber());
    assertEquals("  var y = 2;\n  return x + y;\n", region.getSource());
  }

  // Testing getRegion() method with invalid input
  @Test(expected = IllegalArgumentException.class)
  public void testGetRegionInvalidInput() {
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    sourceFile.setCode(jsCode);

    Region region = sourceFile.getRegion(0);
  }

  // Testing SourceFile.fromFile() method
  @Test
  public void testFromFile() throws IOException {
    String filePath = "testFromFile.js";
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";

    File file = new File(filePath);
    Files.write(file, jsCode.getBytes(Charsets.UTF_8), Charsets.UTF_8);

    SourceFile fromFileSource = SourceFile.fromFile(filePath);

    assertEquals("testFromFile.js", fromFileSource.getName());
    assertEquals(jsCode, fromFileSource.getCode());

    Files.delete(file.toPath());
  }

  // Testing SourceFile.fromCode() method
  @Test
  public void testFromCode() {
    String fileName = "testFromCode.js";
    String originalPath = "testFromCodeOriginalPath.js";
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";

    SourceFile fromCodeSource = SourceFile.fromCode(fileName, jsCode);

    assertEquals(fileName, fromCodeSource.getName());
    assertEquals(jsCode, fromCodeSource.getCode());
    assertEquals(originalPath, fromCodeSource.getOriginalPath());
  }

  // Testing SourceFile.fromCode() method with set originalPath
  @Test
  public void testFromCodeWithOriginalPath() {
    String fileName = "testFromCodeWithOriginalPath.js";
    String originalPath = "testFromCodeWithOriginalPathOriginalPath.js";
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";

    SourceFile fromCodeSource = SourceFile.fromCode(fileName, originalPath, jsCode);

    assertEquals(fileName, fromCodeSource.getName());
    assertEquals(jsCode, fromCodeSource.getCode());
    assertEquals(originalPath, fromCodeSource.getOriginalPath());
  }

  // Testing SourceFile.fromInputStream() method
  @Test
  public void testFromInputStream() throws IOException {
    String fileName = "testFromInputStream.js";
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";

    File file = new File(fileName);
    Files.write(file.toPath(), jsCode.getBytes(Charsets.UTF_8), Charsets.UTF_8);

    InputStream inputStream = new FileInputStream(file);
    SourceFile fromInputStreamSource = SourceFile.fromInputStream(fileName, inputStream);

    assertEquals("testFromInputStream.js", fromInputStreamSource.getName());
    assertEquals(jsCode, fromInputStreamSource.getCode());

    Files.delete(file.toPath());
  }

  // Testing SourceFile.fromInputStream() method with set originalPath
  @Test
  public void testFromInputStreamWithOriginalPath() throws IOException {
    String fileName = "testFromInputStreamWithOriginalPath.js";
    String originalPath = "testFromInputStreamWithOriginalPathOriginalPath.js";
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";

    File file = new File(fileName);
    Files.write(file.toPath(), jsCode.getBytes(Charsets.UTF_8), Charsets.UTF_8);

    InputStream inputStream = new FileInputStream(file);
    SourceFile fromInputStreamSource = SourceFile.fromInputStream(fileName, originalPath, inputStream);

    assertEquals("testFromInputStreamWithOriginalPath.js", fromInputStreamSource.getName());
    assertEquals(jsCode, fromInputStreamSource.getCode());
    assertEquals(originalPath, fromInputStreamSource.getOriginalPath());

    Files.delete(file.toPath());
  }

  // Testing SourceFile.fromReader() method
  @Test
  public void testFromReader() throws IOException {
    String fileName = "testFromReader.js";
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";

    File file = new File(fileName);
    Files.write(file.toPath(), jsCode.getBytes(Charsets.UTF_8), Charsets.UTF_8);

    Reader reader = new FileReader(file);
    SourceFile fromReaderSource = SourceFile.fromReader(fileName, reader);

    assertEquals("testFromReader.js", fromReaderSource.getName());
    assertEquals(jsCode, fromReaderSource.getCode());

    Files.delete(file.toPath());
  }

  // Testing SourceFile.fromGenerator() method
  @Test
  public void testFromGenerator() {
    String fileName = "testFromGenerator.js";
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";

    Generator generator = () -> jsCode;
    SourceFile fromGeneratorSource = SourceFile.fromGenerator(fileName, generator);

    assertEquals("testFromGenerator.js", fromGeneratorSource.getName());
    assertEquals(jsCode, fromGeneratorSource.getCode());
  }
}