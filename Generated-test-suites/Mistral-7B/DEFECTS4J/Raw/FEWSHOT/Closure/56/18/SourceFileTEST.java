package com.google.javascript.jscomp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testCreateSourceFile() {
    String fileName = "test.js";
    SourceFile sourceFile = new SourceFile(fileName);
    assertEquals(fileName, sourceFile.getName());
    assertFalse(sourceFile.isExtern());
    assertNull(sourceFile.getCode());
  }

  @Test
  public void testGetLineOffset() throws IOException {
    String code = "function foo() {}\nfunction bar() {}\n";
    SourceFile sourceFile = new Preloaded("test.js", code);
    int[] expectedLineOffsets = {1, 3, 5};

    int[] lineOffsets = new int[3];
    for (int i = 0; i < 3; i++) {
      lineOffsets[i] = sourceFile.getLineOffset(i + 1);
    }
    assertArrayEquals(expectedLineOffsets, lineOffsets);
  }

  @Test
  public void testGetLineOffsetInvalidLineNumber() throws IOException {
    String code = "function foo() {}\nfunction bar() {}\n";
    SourceFile sourceFile = new Preloaded("test.js", code);

    assertThrows(IllegalArgumentException.class, () -> sourceFile.getLineOffset(0));
    assertThrows(IllegalArgumentException.class, () -> sourceFile.getLineOffset(5));
  }

  @Test
  public void testGetCode() throws IOException {
    String fileName = "test.js";
    String code = "function foo() {}\nfunction bar() {}\n";

    SourceFile sourceFileFromFile = new OnDisk(new File(fileName));
    String sourceCode = sourceFileFromFile.getCode();
    assertEquals(code, sourceCode);

    SourceFile sourceFileFromPreloadedCode = new Preloaded(fileName, code);
    sourceCode = sourceFileFromPreloadedCode.getCode();
    assertEquals(code, sourceCode);
  }

  @Test
  public void testGetCodeThrowIOException() {
    String fileName = "test.js";
    SourceFile sourceFile = new SourceFile(fileName);

    assertThrows(IOException.class, () -> sourceFile.getCode());
  }

  @Test
  public void testGetCodeReader() throws IOException {
    String fileName = "test.js";
    String code = "function foo() {}\nfunction bar() {}\n";

    SourceFile sourceFileFromFile = new OnDisk(new File(fileName));
    Reader codeReader = sourceFileFromFile.getCodeReader();
    char[] buffer = new char[code.length()];
    CharStreams.read(codeReader, buffer);
    assertEquals(code, new String(buffer));

    SourceFile sourceFileFromPreloadedCode = new Preloaded(fileName, code);
    codeReader = sourceFileFromPreloadedCode.getCodeReader();
    CharStreams.read(codeReader, buffer);
    assertEquals(code, new String(buffer));
  }

  @Test
  public void testGetRegion() {
    String code = "function foo() {}\nfunction bar() {}\n";
    SourceFile sourceFile = new Preloaded("test.js", code);
    Region region = sourceFile.getRegion(2);

    assertSame(2, region.getStartLineNumber());
    assertSame(2, region.getEndLineNumber());
    assertEquals("function bar() {}\n", region.getCode());
  }

  @Test
  public void testGetRegionInvalidLineNumber() {
    String code = "function foo() {}\nfunction bar() {}\n";
    SourceFile sourceFile = new Preloaded("test.js", code);

    assertNull(sourceFile.getRegion(0));
    assertNull(sourceFile.getRegion(5));
  }
}