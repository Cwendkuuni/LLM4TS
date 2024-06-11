package com.google.javascript.jscomp; // Assuming the test file is in the same package

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * Unit tests for the SourceFile class.
 */
public class SourceFileTest {

  @Test
  public void testFileName() throws IOException {
    SourceFile sourceFile = new SourceFile("testSourceFile.js");
    assertEquals("testSourceFile.js", sourceFile.getName());
  }

  @Test
  public void testSetAndGetCode() throws IOException {
    String fileContent = "function testFunction() {\n" +
        "  // Test code\n" +
        "}";

    SourceFile sourceFile = new SourceFile("testSourceFile.js");
    sourceFile.setCode(fileContent);

    String code = sourceFile.getCode();
    assertEquals(fileContent, code);
  }

  @Test
  public void testGetCodeFromFile() throws IOException {
    String filePath = "testSourceFile.js";
    String code = Files.toString(new File(filePath), Charsets.UTF_8);
    SourceFile sourceFile = new SourceFile(filePath);

    String testCode = sourceFile.getCode();
    assertEquals(code, testCode);
  }

  @Test
  public void testGetCodeFromInputStream() throws IOException {
    String filePath = "testSourceFile.js";
    File file = new File(filePath);
    InputStream inputStream = Files.newInputStream(file);

    SourceFile sourceFile = new SourceFile(filePath);
    String code = sourceFile.getCode();

    String testCode = Files.toString(inputStream, Charsets.UTF_8);
    assertEquals(testCode, code);
  }

  @Test
  public void testGetCodeFromReader() throws IOException {
    String filePath = "testSourceFile.js";
    File file = new File(filePath);
    Reader reader = new FileReader(file);

    SourceFile sourceFile = new SourceFile(filePath);
    String code = sourceFile.getCode();

    String testCode = Files.toString(reader, Charsets.UTF_8);
    assertEquals(testCode, code);
  }

  @Test
  public void testGetLineOffset() throws IOException {
    String fileContent = "function testFunction() {\n" +
        "  // Test code 1\n" +
        "}\n" +
        "\n" +
        "function testFunction2() {\n" +
        "  // Test code 2\n" +
        "}";

    SourceFile sourceFile = new SourceFile("testSourceFile.js");
    sourceFile.setCode(fileContent);

    int lineNumber = 2;
    int lineOffset = sourceFile.getLineOffset(lineNumber);

    int expectedLineOffset = 1 + 17 + 1; // 1 = line number, 17 = length of "function testFunction2() ", 1 = newline character
    assertEquals(expectedLineOffset, lineOffset);
  }

  @Test
  public void testGetRegion() throws IOException {
    String fileContent = "function testFunction() {\n" +
        "  // Test code 1\n" +
        "}\n" +
        "\n" +
        "function testFunction2() {\n" +
        "  // Test code 2\n" +
        "}";

    SourceFile sourceFile = new SourceFile("testSourceFile.js");
    sourceFile.setCode(fileContent);

    int lineNumber = 2;
    Region region = sourceFile.getRegion(lineNumber);

    String expectedRegion = "function testFunction2() {\n" +
        "  // Test code 2\n" +
        "}";
    assertEquals(expectedRegion, region.getCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetInvalidLineNumber() throws IOException {
    String fileContent = "function testFunction() {\n" +
        "  // Test code 1\n" +
        "}";

    SourceFile sourceFile = new SourceFile("testSourceFile.js");
    sourceFile.setCode(fileContent);

    int lineNumber = -1;
    sourceFile.getLineOffset(lineNumber);
  }

  @Test
  public void testToString() throws IOException {
    String fileContent = "function testFunction() {\n" +
        "  // Test code 1\n" +
        "}";

    SourceFile sourceFile = new SourceFile("testSourceFile.js");
    sourceFile.setCode(fileContent);

    assertEquals("testSourceFile.js", sourceFile.toString());
  }

  @Test
  public void testFromFile() throws IOException {
    String fileContent = "function testFunction() {\n" +
        "  // Test code 1\n" +
        "}";

    String fileName = "testSourceFile.js";
    SourceFile sourceFile = SourceFile.fromFile(fileName);

    String code = sourceFile.getCode();
    assertEquals(fileContent, code);
  }

  @Test
  public void testFromCode() throws IOException {
    String fileName = "testSourceFile.js";
    String code = "function testFunction() {\n" +
        "  // Test code 1\n" +
        "}";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    String testCode = sourceFile.getCode();

    assertEquals(code, testCode);
  }

  @Test
  public void testFromInputStream() throws IOException {
    String fileName = "testSourceFile.js";
    String fileContent = "function testFunction() {\n" +
        "  // Test code 1\n" +
        "}";
    File file = new File(fileName);

    InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
    SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream);

    String code = sourceFile.getCode();
    assertEquals(fileContent, code);
  }

  @Test
  public void testFromReader() throws IOException {
    String fileName = "testSourceFile.js";
    String fileContent = "function testFunction() {\n" +
        "  // Test code 1\n" +
        "}";

    Reader reader = new StringReader(fileContent);
    SourceFile sourceFile = SourceFile.fromReader(fileName, reader);

    String code = sourceFile.getCode();
    assertEquals(fileContent, code);
  }

  @Test
  public void testFromGenerator() throws IOException {
    String fileName = "testSourceFile.js";
    String code = "function testFunction() {\n" +
        "  // Test code 1\n" +
        "}";

    SourceFile sourceFile = SourceFile.fromGenerator(fileName, () -> code);
    String testCode = sourceFile.getCode();

    assertEquals(code, testCode);
  }

  @Test
  public void testIsExtern() {
    SourceFile sourceFile = new SourceFile("testSourceFile.js");
    assertFalse(sourceFile.isExtern());

    sourceFile = new SourceFile("testSourceFile.js", true);
    assertTrue(sourceFile.isExtern());
  }
}