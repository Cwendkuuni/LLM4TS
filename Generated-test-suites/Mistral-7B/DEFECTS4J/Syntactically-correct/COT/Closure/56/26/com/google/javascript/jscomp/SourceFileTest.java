package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

public class SourceFileTest {
  private SourceFile sourceFile;

  @Before
  public void setUp() throws Exception {
    sourceFile = new SourceFile("test.js");
  }

  @Test
  public void testGetLineOffset() throws IOException {
    sourceFile.setCode("line1\nline2\nline3");
    assertEquals(1, sourceFile.getLineOffset(1));
    assertEquals(4, sourceFile.getLineOffset(3));
  }

  @Test
  public void testGetNumLines() throws IOException {
    sourceFile.setCode("line1\nline2\nline3");
    assertEquals(3, sourceFile.getNumLines());
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    sourceFile.setCode("line1\nline2\nline3");
    sourceFile.findLineOffsets();
    assertEquals(3, sourceFile.lineOffsets.length);
    assertEquals(1, sourceFile.lineOffsets[0]);
    assertEquals(4, sourceFile.lineOffsets[1]);
    assertEquals(7, sourceFile.lineOffsets[2]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetWithInvalidLineNumber() throws IOException {
    sourceFile.setCode("line1\nline2\nline3");
    sourceFile.findLineOffsets();
    sourceFile.getLineOffset(-1);
  }

  @Test
  public void testGetCode() throws IOException {
    sourceFile.setCode("test code");
    assertEquals("test code", sourceFile.getCode());
  }

  @Test(expected = IOException.class)
  public void testGetCodeWithIOException() throws IOException, InterruptedException {
    sourceFile = new SourceFile("nonExistentFile.js");
    sourceFile.getCode();
  }

  @Test
  public void testGetCodeReader() throws IOException {
    sourceFile.setCode("test code");
    StringReader reader = new StringReader(sourceFile.getCode());
    assertSame(reader, sourceFile.getCodeReader());
  }

  @Test
  public void testGetOriginalPath() {
    sourceFile.setOriginalPath("originalPath");
    assertEquals("originalPath", sourceFile.getOriginalPath());
  }

  @Test
  public void testClearCachedSource() throws IOException {
    sourceFile.setCode("test code");
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
  }

  @Test
  public void testHasSourceInMemory() throws IOException {
    sourceFile.setCode("test code");
    assertTrue(sourceFile.hasSourceInMemory());
    sourceFile.clearCachedSource();
    assertFalse(sourceFile.hasSourceInMemory());
  }

  @Test
  public void testGetName() {
    assertEquals("test.js", sourceFile.getName());
  }

  @Test
  public void testIsExtern() {
    assertFalse(sourceFile.isExtern());
    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testGetLine() throws IOException {
    sourceFile.setCode("line1\nline2\nline3");
    assertEquals("line1", sourceFile.getLine(1));
    assertEquals("line3", sourceFile.getLine(3));
    assertNull(sourceFile.getLine(5));
  }

  @Test
  public void testGetRegion() throws IOException {
    sourceFile.setCode("line1\nline2\nline3");
    Region region = sourceFile.getRegion(3);
    assertEquals(2, region.getEndLineNumber());
    assertEquals("line2\nline3", region.getSourceCode());
    region = sourceFile.getRegion(5);
    assertNull(region);
  }
}