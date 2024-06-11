package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class SourceFileTest {

  private SourceFile sourceFile;
  private String fileName;

  @Before
  public void setup() {
    fileName = "test.js";
    sourceFile = new SourceFile(fileName);
  }

  // Basic functionality tests

  @Test
  public void testGetLineOffset() {
    assertEquals(1, sourceFile.getLineOffset(1));
  }

  @Test
  public void testGetNumLines() {
    assertEquals(5, sourceFile.getNumLines());
  }

  @Test
  public void testGetCode() throws IOException {
    String expectedCode = "function test() {}";
    when(mock(Generator.class).getCode()).thenReturn(expectedCode);
    sourceFile.setGenerator(mock(Generator.class));
    assertEquals(expectedCode, sourceFile.getCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetWithInvalidLineNumber() {
    sourceFile.getLineOffset(10);
  }

  @Test
  public void testGetCodeReader() throws IOException {
    String expectedCode = "function test() {}";
    when(mock(Generator.class).getCode()).thenReturn(expectedCode);
    sourceFile.setGenerator(mock(Generator.class));
    Reader reader = sourceFile.getCodeReader();
    assertEquals(expectedCode, CharStreams.toString(reader));
  }

  @Test(expected = IOException.class)
  public void testGetCodeWithIOException() throws IOException {
    when(mock(Generator.class).getCode()).thenThrow(new IOException());
    sourceFile.setGenerator(mock(Generator.class));
    sourceFile.getCode();
  }

  // Edge cases and exception handling tests

  @Test
  public void testGetLineNullResult() {
    String code = "function test() {\n" +
        "  function innerTest() {\n" +
        "  }\n" +
        "}";
    sourceFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    assertNull(sourceFile.getLine(11));
  }

  @Test
  public void testGetRegionNullResult() {
    String code = "function test() {\n" +
        "  function innerTest() {\n" +
        "  }\n" +
        "}";
    sourceFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    assertNull(sourceFile.getRegion(11));
  }

  @Test
  public void testGetRegionWithIOException() {
    String code = "function test() {\n" +
        "  function innerTest() {\n" +
        "  }\n" +
        "}";
    sourceFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        throw new IOException();
      }
    };
    assertNull(sourceFile.getRegion(1));
  }

  @Test
  public void testFindLineOffsetsWithEmptyCode() {
    sourceFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return "";
      }
    };
    sourceFile.findLineOffsets();
    int[] lineOffsets = sourceFile.lineOffsets;
    assertSame(new int[0], lineOffsets);
  }
}