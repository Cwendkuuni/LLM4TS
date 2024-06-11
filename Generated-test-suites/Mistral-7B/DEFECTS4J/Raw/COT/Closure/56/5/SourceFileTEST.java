package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;

public class SourceFileTest {

  private static final String TEST_FILE_PATH = "test_source_file.js";

  @Test
  public void testSourceFileBasic() {
    String fileName = "test_source_file.js";
    Generator generator = () -> "function test() {}";
    SourceFile sourceFile = new SourceFile(fileName);

    assertEquals("test_source_file.js", sourceFile.getName());
    assertEquals(true, sourceFile.hasSourceInMemory());
    assertEquals("function test() {}\n", sourceFile.getCode());
  }

  @Test
  public void testSourceFileFromDisk() throws IOException {
    String fileName = TEST_FILE_PATH;
    File file = new File(fileName);
    Charset charset = StandardCharsets.UTF_8;
    SourceFile sourceFile = new SourceFile(file, charset);

    assertEquals(fileName, sourceFile.getName());
    assertEquals(false, sourceFile.hasSourceInMemory());
    assertEquals(Files.toString(file, charset), sourceFile.getCode());
  }

  @Test
  public void testGetLine() {
    String fileName = "test_source_file.js";
    String code = "function test() {\n  var a = 1;\n  var b = 2;\n}";
    Generator generator = () -> code;
    SourceFile sourceFile = new SourceFile(fileName, generator);

    assertEquals("function test() {\n  var a = 1;\n", sourceFile.getLine(1));
    assertEquals("var b = 2;\n}", sourceFile.getLine(3));
    assertNull(sourceFile.getLine(5));
  }

  @Test
  public void testGetRegion() {
    String fileName = "test_source_file.js";
    String code = "function test() {\n  var a = 1;\n  var b = 2;\n}";
    Generator generator = () -> code;
    SourceFile sourceFile = new SourceFile(fileName, generator);

    String expectedRegion = "function test() {\n  var a = 1;\n  var b = 2;\n}";
    Region region = sourceFile.getRegion(3);
    assertEquals(3, region.getStartLineNumber());
    assertEquals(3, region.getEndLineNumber());
    assertEquals(expectedRegion, region.getSourceCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineInvalidLineNumber() {
    String fileName = "test_source_file.js";
    String code = "function test() {}";
    Generator generator = () -> code;
    SourceFile sourceFile = new SourceFile(fileName, generator);

    sourceFile.getLine(10);
  }

  @Test
  public void testSetOriginalPath() {
    String fileName = "test_source_file.js";
    String originalPath = "test_source_file_original.js";
    String code = "function test() {}";
    Generator generator = () -> code;
    SourceFile sourceFile = new SourceFile(fileName);

    sourceFile.setOriginalPath(originalPath);
    assertEquals(originalPath, sourceFile.getOriginalPath());
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    String fileName = "test_source_file.js";
    String code = "function test() {\n  var a = 1;\n  var b = 2;\n}";
    Generator generator = () -> code;
    SourceFile sourceFile = new SourceFile(fileName, generator);

    int[] lineOffsets = sourceFile.getLineOffsets();
    assertEquals(3, lineOffsets.length);
    assertEquals(1, lineOffsets[1]);
    assertEquals(14, lineOffsets[2]);
  }

  @Test
  public void testFromInputStream() throws IOException {
    String fileName = "test_source_file.js";
    String inputCode = "function test() {}";
    InputStream inputStream = new ByteArrayInputStream(inputCode.getBytes());

    SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream);

    assertEquals(fileName, sourceFile.getName());
    assertEquals(true, sourceFile.hasSourceInMemory());
    assertEquals(inputCode, sourceFile.getCode());
  }

  @Test(expected = IOException.class)
  public void testFromInputStreamInvalidFileName() throws IOException {
    String fileName = null;
    InputStream inputStream = new ByteArrayInputStream("function test() {}".getBytes());

    SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream);
  }

  @Test
  public void testFromGenerator() {
    String fileName = "test_source_file.js";
    Generator generator = () -> "function test() {}";
    SourceFile sourceFile = new SourceFile(fileName, generator);

    assertEquals(fileName, sourceFile.getName());
    assertEquals(true, sourceFile.hasSourceInMemory());
    assertEquals("function test() {}", sourceFile.getCode());
  }

  @Test
  public void testSetCode() {
    String fileName = "test_source_file.js";
    String newCode = "function test() {return 'Hello World';}";
    SourceFile sourceFile = new SourceFile(fileName);

    sourceFile.setCode(newCode);

    assertEquals(newCode, sourceFile.getCode());
  }
}