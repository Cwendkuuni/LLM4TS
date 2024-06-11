package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.jstype.StaticSourceFile;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Unit tests for {@link SourceFile}.
 */
public class SourceFileTest {

  @Test
  public void testFromDisk() throws IOException {
    String filePath = "test.js";
    String expectedCode = "function foo() {}";

    File testFile = new File(filePath);
    Files.write(testFile, expectedCode.getBytes(StandardCharsets.UTF_8));

    SourceFile sourceFile = SourceFile.fromFile(filePath);
    assertEquals("foo.js", sourceFile.getName());
    assertEquals(expectedCode, sourceFile.getCode());
    assertNull(sourceFile.getOriginalPath());

    Files.delete(testFile);
  }

  @Test
  public void testFromCode() {
    String fileName = "bar.js";
    String code = "function bar() {}";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
    assertNull(sourceFile.getOriginalPath());
  }

  @Test
  public void testFromCodeWithOriginalPath() {
    String fileName = "baz.js";
    String originalPath = "path/to/original/file.js";
    String code = "function baz() {}";

    SourceFile sourceFile = SourceFile.fromCode(fileName, originalPath, code);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
    assertEquals(originalPath, sourceFile.getOriginalPath());
  }

  @Test
  public void testGetLine() throws IOException {
    String fileName = "qux.js";
    String code = "function qux() {\n" +
                  "  var x = 1;\n" +
                  "  var y = 2;\n" +
                  "}";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);

    assertEquals("function qux() {\n  var x = 1;\n", sourceFile.getLine(1));
    assertEquals("  var y = 2;\n}", sourceFile.getLine(3));
    assertNull(sourceFile.getLine(5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineNegativeNumber() {
    SourceFile sourceFile = new SourceFile("file.js");
    sourceFile.getLine(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineNumberGreaterThanNumberOfLines() {
    String fileName = "file.js";
    String code = "function foo() {}";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    sourceFile.getLine(sourceFile.getNumLines() + 1);
  }
}