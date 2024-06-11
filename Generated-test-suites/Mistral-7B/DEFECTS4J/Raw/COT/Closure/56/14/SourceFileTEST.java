package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SourceFileTest {

  @Test
  public void testSourceFileConstructor() {
    SourceFile sourceFile = new SourceFile("testSourceFile.js");
    assertEquals("testSourceFile.js", sourceFile.getName());
  }

  @Test
  public void testGetLineOffset() throws IOException {
    String code = "print(\"Hello World\");";
    SourceFile sourceFile = new Preloaded("testSourceFile.js", code);

    for (int i = 1; i <= 3; i++) {
      int lineOffset = sourceFile.getLineOffset(i);
      if (i == 1) {
        assertEquals(15, lineOffset);
      } else if (i == 2) {
        assertEquals(25, lineOffset);
      } else {
        assertEquals(36, lineOffset);
      }
    }

    // Test with invalid line number
    assertNull(sourceFile.getLineOffset(4));
  }

  @Test
  public void testGetLine() throws IOException {
    String code = "print(\"Line 1: Hello World\");\nprint(\"Line 2: Hello Closure Compiler\");";
    SourceFile sourceFile = new Preloaded("testSourceFile.js", code);

    assertEquals("Line 1: Hello World", sourceFile.getLine(1));
    assertEquals("Line 2: Hello Closure Compiler", sourceFile.getLine(2));
    assertNull(sourceFile.getLine(3));
  }

  @Test
  public void testGetRegion() throws IOException {
    String code = "print(\"Line 1: Hello World\");\nprint(\"Line 2: Hello Closure Compiler\");";
    SourceFile sourceFile = new Preloaded("testSourceFile.js", code);

    Region expectedRegion = new Region(1, 2, "Line 1: Hello World\nLine 2: Hello Closure Compiler");
    Region actualRegion = sourceFile.getRegion(2);

    assertSame(expectedRegion, actualRegion);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidFileName() {
    new SourceFile(null);
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    String code = "print(\"Line 1: Hello World\");\nprint(\"Line 2: Hello Closure Compiler\");";
    SourceFile sourceFile = new Preloaded("testSourceFile.js", code);

    sourceFile.findLineOffsets();
    int numLines = sourceFile.getNumLines();

    for (int i = 1; i <= numLines; i++) {
      int lineOffset = sourceFile.getLineOffset(i);
      if (i == 1) {
        assertEquals(15, lineOffset);
      } else if (i == 2) {
        assertEquals(42, lineOffset);
      }
    }
  }

  @Test
  public void testGetCodeFromFile() throws IOException {
    String fileName = "testSourceFile.js";
    String expectedCode = "print(\"Hello World\");";
    SourceFile sourceFile = SourceFile.fromFile(fileName);

    assertEquals(expectedCode, sourceFile.getCode());
  }

  @Test
  public void testGetCodeFromInputStream() throws IOException {
    String fileName = "testSourceFile.js";
    String expectedCode = "print(\"Hello World\");";

    File tempFile = File.createTempFile("test", null);
    Files.write(tempFile, expectedCode.getBytes());

    InputStream inputStream = Files.newInputStream(tempFile.toPath());
    SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream);

    assertEquals(expectedCode, sourceFile.getCode());
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String code = "print(\"Hello World\");";
    SourceFile sourceFile = new Preloaded("testSourceFile.js", code);

    String originalCode = sourceFile.getCode();
    sourceFile.clearCachedSource();

    assertNull(sourceFile.getCode());
    assertEquals(originalCode, sourceFile.getCodeNoCache());
  }
}