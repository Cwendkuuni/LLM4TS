package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class SourceFileTest {
  @Test
  public void testSourceFileConstructor() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertEquals("test.js", sourceFile.getName());
    assertFalse(sourceFile.isExtern());
  }

  @Test
  public void testGetLineOffset() {
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.setCode("line 1\nline 2\nline 3");

    assertEquals(1, sourceFile.getLineOffset(1));
    assertEquals(5, sourceFile.getLineOffset(3));
  }

  @Test
  public void testGetCode() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js");
    String expectedCode = "function test() {\n  // test function\n}";
    byte[] fileContent = expectedCode.getBytes();

    File tempFile = File.createTempFile("sourceFileTest", null);
    Files.write(tempFile, fileContent, Charsets.UTF_8);
    sourceFile = new SourceFile(tempFile.getAbsolutePath());

    assertEquals(expectedCode, sourceFile.getCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileNullFileName() {
    new SourceFile(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileEmptyFileName() {
    new SourceFile("");
  }

  @Test
  public void testGetRegion() {
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.setCode("line 1\nline 2\nline 3\nline 4");

    Region region = sourceFile.getRegion(3);
    assertNotNull(region);
    assertEquals(2, region.getStartLineNumber());
    assertEquals(4, region.getEndLineNumber());
    assertEquals("line 2\nline 3", region.getSource());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRegionInvalidLineNumber() {
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.setCode("line 1\nline 2\nline 3\nline 4");
    sourceFile.getRegion(5);
  }

  @Test
  public void testGetRegionWithEmptyCode() {
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.setCode("");

    Region region = sourceFile.getRegion(1);
    assertNull(region);
  }

  @Test
  public void testFromFile() throws IOException {
    String expectedCode = "function test() {\n  // test function\n}";
    File tempFile = File.createTempFile("sourceFileTest", null);
    Files.write(tempFile, expectedCode.getBytes(), Charsets.UTF_8);

    SourceFile sourceFile = SourceFile.fromFile(tempFile.getAbsolutePath());
    assertEquals(expectedCode, sourceFile.getCode());
  }
}