package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testSourceFileConstruction() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertEquals("test.js", sourceFile.getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileConstructionWithNullFileName() {
    new SourceFile(null);
  }

  @Test
  public void testGetLineOffset() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "test.js", "function test() {}");
    assertEquals(1, sourceFile.getLineOffset(1));
    assertEquals(11, sourceFile.getLineOffset(11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetWithNegativeLineNumber() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "test.js", "function test() {}");
    sourceFile.getLineOffset(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetWithLineNumberGreaterThanNumberOfLines() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "test.js", "function test() {}\n");
    sourceFile.getLineOffset(2);
  }

  @Test
  public void testGetNumLines() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "test.js", "function test() {\nfunction innerTest() {}\n}");
    assertEquals(3, sourceFile.getNumLines());
  }

  @Test
  public void testGetCode() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "test.js", "function test() {}");
    assertEquals("function test() {}\n", sourceFile.getCode());
  }

  @Test(expected = IOException.class)
  public void testGetCodeThrowsIOException() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    sourceFile.getCode();
  }

  @Test
  public void testGetCodeReader() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "test.js", "function test() {}");
    Reader reader = sourceFile.getCodeReader();
    assertEquals("function test() {}\n", CharStreams.toString(reader));
  }

  @Test
  public void testGetOriginalPath() {
    SourceFile sourceFile = new Preloaded("test.js", "test.js", "function test() {}");
    assertEquals("test.js", sourceFile.getOriginalPath());
  }

  @Test
  public void testClearCachedSource() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "test.js", "function test() {}");
    assertEquals("function test() {}\n", sourceFile.getCode());
    sourceFile.clearCachedSource();
    assertEquals(null, sourceFile.getCode());
  }

  @Test(expected = IOException.class)
  public void testClearCachedSourceThrowsIOException() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    sourceFile.clearCachedSource();
  }

  @Test
  public void testHasSourceInMemory() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "test.js", "function test() {}");
    assertTrue(sourceFile.hasSourceInMemory());
    sourceFile.clearCachedSource();
    assertFalse(sourceFile.hasSourceInMemory());
  }

  @Test(expected = IOException.class)
  public void testSetCodeThrowsIOException() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    sourceFile.setCode("function test() {}");
  }

  @Test
  public void testGetLine() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "test.js",
        "function test() {\n  function innerTest() {}\n}");
    assertEquals("function innerTest() {}\n", sourceFile.getLine(3));
  }

  @Test(expected = NullPointerException.class)
  public void testGetLineWithNullSourceFile() {
    SourceFile sourceFile = null;
    sourceFile.getLine(1);
  }

  @Test(expected = IOException.class)
  public void testGetLineThrowsIOException() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    sourceFile.getLine(1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineWithNegativeLineNumber() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "test.js",
        "function test() {\n  function innerTest() {}\n}");
    sourceFile.getLine(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineWithLineNumberGreaterThanNumberOfLines() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "test.js", "function test() {}\n");
    sourceFile.getLine(2);
  }

  @Test
  public void testGetRegion() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "test.js",
        "function test() {\n  function innerTest() {}\n}");
    Region region = sourceFile.getRegion(4);
    assertTrue(region instanceof SimpleRegion);
    assertEquals(2, region.getStartLine());
    assertEquals(2, region.getEndLine());
    assertEquals("  function innerTest() {}\n", region.toString());
  }

  @Test(expected = NullPointerException.class)
  public void testGetRegionWithNullSourceFile() {
    SourceFile sourceFile = null;
    sourceFile.getRegion(1);
  }

  @Test(expected = IOException.class)
  public void testGetRegionThrowsIOException() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    sourceFile.getRegion(1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRegionWithNegativeLineNumber() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "test.js",
        "function test() {\n  function innerTest() {}\n}");
    sourceFile.getRegion(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRegionWithLineNumberGreaterThanNumberOfLines() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "test.js", "function test() {}\n");
    sourceFile.getRegion(2);
  }
}