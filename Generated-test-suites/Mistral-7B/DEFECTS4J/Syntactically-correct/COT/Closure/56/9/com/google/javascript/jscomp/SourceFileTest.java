package com.google.javascript.jscomp; // Make sure to import all necessary classes

import static org.junit.Assert.*;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testSourceFileConstruction() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    assertEquals("testFile.js", sourceFile.getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileNullName() {
    SourceFile sourceFile = new SourceFile(null);
  }

  @Test
  public void testGetLineOffset() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    sourceFile.setCode("line1\nline2\nline3");

    assertEquals(1, sourceFile.getLineOffset(1));
    assertEquals(6, sourceFile.getLineOffset(3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetInvalidLineNumber() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    sourceFile.setCode("line1\nline2\nline3");

    sourceFile.getLineOffset(5);
  }

  @Test
  public void testGetNumLines() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    sourceFile.setCode("line1\nline2\nline3");

    assertEquals(3, sourceFile.getNumLines());
  }

  @Test
  public void testGetCode() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    sourceFile.setCode("function test() {}\n");

    assertEquals("function test() {}\n", sourceFile.getCode());
  }

  @Test(expected = IOException.class)
  public void testGetCodeIOException() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    throw new IOException();
  }

  @Test
  public void testGetCodeReader() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    sourceFile.setCode("function test() {}\n");

    Reader reader = sourceFile.getCodeReader();
    StringBuilder stringBuilder = new StringBuilder();
    int c;
    while ((c = reader.read()) != -1) {
      stringBuilder.append((char) c);
    }

    assertEquals("function test() {}\n", stringBuilder.toString());
  }

  @Test(expected = IOException.class)
  public void testGetCodeReaderIOException() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    throw new IOException();
  }
}