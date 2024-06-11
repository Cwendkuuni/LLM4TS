package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testutils.TestSourceFiles;

/**
 * Unit tests for {@link SourceFile} class.
 */
public class SourceFileTest {

  @Test
  public void testFromFile() throws IOException {
    String fileName = "test.js";
    Charset charset = Charsets.UTF_8;

    // Create source file from file
    SourceFile sourceFile = SourceFile.fromFile(fileName, charset);

    // Check file name
    assertEquals("test.js", sourceFile.getName());

    // Check file code
    String code = sourceFile.getCode();
    assertEquals(TestSourceFiles.TEST_SOURCE_CODE, code);

    // Check file line offsets
    for (int i = 1; i <= 5; i++) {
      int lineOffset = sourceFile.getLineOffset(i);
      int expectedOffset = 4 * i + 1; // Assuming source code starts at line 1
      assertEquals(expectedOffset, lineOffset);
    }

    // Check file region
    String expectedRegion = "function test() {\n" // Line 1
        + "  var x = 1;\n" // Line 2
        + "  var y = 2;\n" // Line 3
        + "  return x + y;\n" // Line 4
        + "}\n";
    Region region = sourceFile.getRegion(3);
    assertEquals(expectedRegion, region.getCode());

    // Check file line
    String expectedLine = "var y = 2;\n";
    String line = sourceFile.getLine(3);
    assertEquals(expectedLine, line);
  }

  @Test
  public void testFromCode() {
    String fileName = "test.js";
    String code = "function test() {\n"
        + "  var x = 1;\n"
        + "  var y = 2;\n"
        + "  return x + y;\n"
        + "}";

    // Create source file from code
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);

    // Check file name
    assertEquals("test.js", sourceFile.getName());

    // Check file code
    String sourceCode = sourceFile.getCode();
    assertEquals(code, sourceCode);

    // Check file line offsets
    for (int i = 1; i <= 5; i++) {
      int lineOffset = sourceFile.getLineOffset(i);
      int expectedOffset = 3 * i + 1; // Assuming source code starts at line 1
      assertEquals(expectedOffset, lineOffset);
    }

    // Check file region
    String expectedRegion = "function test() {\n" // Line 1
        + "  var x = 1;\n" // Line 2
        + "  var y = 2;\n" // Line 3
        + "  return x + y;\n" // Line 4
        + "}\n";
    Region region = sourceFile.getRegion(3);
    assertEquals(expectedRegion, region.getCode());

    // Check file line
    String expectedLine = "var y = 2;\n";
    String line = sourceFile.getLine(3);
    assertEquals(expectedLine, line);
  }

  @Test
  public void testFromGenerator() {
    String fileName = "test.js";
    String code = "function test() {\n"
        + "  var x = 1;\n"
        + "  var y = 2;\n"
        + "  return x + y;\n"
        + "}";

    // Create source file generator
    SourceFile.Generator generator = new SourceFile.Generator() {
      @Override
      public String getCode() {
        return code;
      }
    };

    // Create source file from generator
    SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);

    // Check file name
    assertEquals("test.js", sourceFile.getName());

    // Check file code
    String sourceCode = sourceFile.getCode();
    assertEquals(code, sourceCode);

    // Check file line offsets
    for (int i = 1; i <= 5; i++) {
      int lineOffset = sourceFile.getLineOffset(i);
      int expectedOffset = 3 * i + 1; // Assuming source code starts at line 1
      assertEquals(expectedOffset, lineOffset);
    }

    // Check file region
    String expectedRegion = "function test() {\n" // Line 1
        + "  var x = 1;\n" // Line 2
        + "  var y = 2;\n" // Line 3
        + "  return x + y;\n" // Line 4
        + "}\n";
    Region region = sourceFile.getRegion(3);
    assertEquals(expectedRegion, region.getCode());

    // Check file line
    String expectedLine = "var y = 2;\n";
    String line = sourceFile.getLine(3);
    assertEquals(expectedLine, line);
  }

  @Test
  public void testNullFileName() {
    // Create source file with null file name
    try {
      SourceFile sourceFile = new SourceFile("");
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testEmptyFileName() {
    // Create source file with empty file name
    try {
      SourceFile sourceFile = new SourceFile(" ");
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testInvalidFileName() {
    // Create source file with invalid file name
    try {
      SourceFile sourceFile = new SourceFile("test$123.js");
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testFromFileNullCharset() {
    String fileName = "test.js";

    try {
      SourceFile sourceFile = SourceFile.fromFile(fileName, null);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testFromFileNonExistentFile() {
    String fileName = "test_non_existent.js";

    try {
      SourceFile sourceFile = SourceFile.fromFile(fileName, Charsets.UTF_8);
      assertNull(sourceFile);
    } catch (IOException e) {
      // Expected
    }
  }

  @Test
  public void testFromGeneratorNullGenerator() {
    String fileName = "test.js";

    try {
      SourceFile sourceFile = SourceFile.fromGenerator(fileName, null);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testFromGeneratorInvalidGenerator() {
    String fileName = "test.js";
    String generatorCode = "function test() {\n" + "  // Invalid generator";

    try {
      SourceFile sourceFile = SourceFile.fromGenerator(fileName, new SourceFile.Generator() {
        @Override
        public String getCode() {
          return generatorCode;
        }
      });
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }
}