package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testBasicConstruction() {
    SourceFile sf = new SourceFile("test.js");
    assertEquals("test.js", sf.getName());
  }

  @Test
  public void testFromFileConstruction() throws IOException {
    File file = new File("test.txt");
    SourceFile sf = new SourceFile(file);
    assertEquals("test.txt", sf.getName());
  }

  @Test
  public void testFromFileWithCharsetConstruction() throws IOException {
    File file = new File("test.txt");
    SourceFile sf = new SourceFile(file, Charsets.UTF_8);
    assertEquals("test.txt", sf.getName());
  }

  @Test
  public void testFromCodeConstruction() {
    String code = "function test() {\n" +
        "  return 42;\n" +
        "}";
    SourceFile sf = new SourceFile("test.js", code);
    assertEquals("test.js", sf.getName());
    assertEquals(code, sf.getCode());
  }

  @Test
  public void testFromCodeWithOriginalPathConstruction() {
    String code = "function test() {\n" +
        "  return 42;\n" +
        "}";
    String originalPath = "/path/to/original/test.js";
    SourceFile sf = new SourceFile("test.js", code, originalPath);
    assertEquals("test.js", sf.getName());
    assertEquals(code, sf.getCode());
    assertEquals(originalPath, sf.getOriginalPath());
  }

  @Test
  public void testFromInputStreamConstruction() throws IOException {
    String code = "function test() {\n" +
        "  return 42;\n" +
        "}";
    InputStream is = new FileInputStream(new File("test.txt"));
    SourceFile sf = new SourceFile("test.txt", is);
    assertEquals("test.txt", sf.getName());
    assertEquals(code, sf.getCode());
  }

  @Test
  public void testFromInputStreamWithOriginalPathConstruction() throws IOException {
    String code = "function test() {\n" +
        "  return 42;\n" +
        "}";
    String originalPath = "/path/to/original/test.txt";
    InputStream is = new FileInputStream(new File("test.txt"));
    SourceFile sf = new SourceFile("test.txt", is, originalPath);
    assertEquals("test.txt", sf.getName());
    assertEquals(code, sf.getCode());
    assertEquals(originalPath, sf.getOriginalPath());
  }

  @Test
  public void testFromReaderConstruction() throws IOException {
    String code = "function test() {\n" +
        "  return 42;\n" +
        "}";
    Reader reader = new StringReader(code);
    SourceFile sf = new SourceFile("test.js", reader);
    assertEquals("test.js", sf.getName());
    assertEquals(code, sf.getCode());
  }

  @Test
  public void testFromGeneratorConstruction() {
    String fileName = "test.js";
    Generator generator = () -> "function test() {\n" +
        "  return 42;\n" +
        "}";
    SourceFile sf = new SourceFile(fileName, generator);
    assertEquals(fileName, sf.getName());
  }

  @Test
  public void testGetLine() throws IOException {
    String code = "function test() {\n" +
        "  return 42;\n" +
        "}";
    SourceFile sf = new SourceFile("test.js", code);
    assertEquals("function test() {\n" +
        "  return 42;\n" +
        "}", sf.getLine(1));
    assertNull(sf.getLine(5));
  }

  @Test
  public void testGetRegion() throws IOException {
    String code = "function test() {\n" +
        "  return 42;\n" +
        "}";
    SourceFile sf = new SourceFile("test.js", code);
    Region region = sf.getRegion(3);
    assertEquals(3, region.getLineNumberStart());
    assertEquals(3, region.getLineNumberEnd());
    assertEquals("  return 42;\n", region.getCode());
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    String code = "function test1() {\n" +
        "  return 42;\n" +
        "}";
    String code2 = "function test2() {\n" +
        "  return 42;\n" +
        "  function inner() {\n" +
        "    return 43;\n" +
        "  }\n" +
        "}";
    SourceFile sf = new SourceFile("test.js", code);
    int[] lineOffsets = sf.getLineOffsets();
    assertEquals(2, lineOffsets.length);
    assertEquals(5, lineOffsets[0]);
    assertEquals(11, lineOffsets[1]);
    SourceFile sf2 = new SourceFile("test2.js", code2);
    lineOffsets = sf2.getLineOffsets();
    assertEquals(4, lineOffsets.length);
    assertEquals(10, lineOffsets[0]);
    assertEquals(26, lineOffsets[1]);
    assertEquals(48, lineOffsets[2]);
    assertEquals(61, lineOffsets[3]);
  }

  @Test
  public void testSetOriginalPath() {
    String fileName = "test.js";
    SourceFile sf = new SourceFile(fileName);
    sf.setOriginalPath("/path/to/original/test.js");
    assertEquals("/path/to/original/test.js", sf.getOriginalPath());
  }

  @Test
  public void testHasSourceInMemory() {
    String code = "function test() {\n" +
        "  return 42;\n" +
        "}";
    SourceFile sf = new SourceFile("test.js", code);
    assertTrue(sf.hasSourceInMemory());
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String code = "function test() {\n" +
        "  return 42;\n" +
        "}";
    SourceFile sf = new SourceFile("test.js", code);
    assertEquals(code, sf.getCode());
    sf.clearCachedSource();
    assertNull(sf.getCode());
  }

  @Test
  public void testToString() {
    String fileName = "test.js";
    SourceFile sf = new SourceFile(fileName);
    assertEquals(fileName, sf.toString());
  }

  @Test
  public void testIsExtern() {
    SourceFile sf = new SourceFile("test.js");
    assertFalse(sf.isExtern());
    sf.setIsExtern(true);
    assertTrue(sf.isExtern());
  }

  @Test
  public void testGetName() {
    SourceFile sf = new SourceFile("test.js");
    assertEquals("test.js", sf.getName());
  }

  @Test
  public void testGetCode() throws IOException {
    String code = "function test() {\n" +
        "  return 42;\n" +
        "}";
    SourceFile sf = new SourceFile("test.js", code);
    assertEquals(code, sf.getCode());
  }

  @Test
  public void testGetCodeReader() throws IOException {
    String code = "function test() {\n" +
        "  return 42;\n" +
        "}";
    SourceFile sf = new SourceFile("test.js", code);
    Reader reader = sf.getCodeReader();
    assertSame(new StringReader(code), reader);
  }

  @Test
  public void testGetCodeNoCache() throws IOException {
    String code = "function test() {\n" +
        "  return 42;\n" +
        "}";
    SourceFile sf = new SourceFile("test.js", code);
    assertEquals(code, sf.getCodeNoCache());
  }

  @Test
  public void testSetCode() throws IOException {
    String code = "function test() {\n" +
        "  return 42;\n" +
        "}";
    SourceFile sf = new SourceFile("test.js");
    sf.setCode(code);
    assertEquals(code, sf.getCode());
  }

  @Test
  public void testGetLineOffset() {
    String code = "function test1() {\n" +
        "  return 42;\n" +
        "}";
    String code2 = "function test2() {\n" +
        "  return 42;\n" +
        "  function inner() {\n" +
        "    return 43;\n" +
        "  }\n" +
        "}";
    SourceFile sf = new SourceFile("test.js", code);
    assertEquals(5, sf.getLineOffset(1));
    assertEquals(11, sf.getLineOffset(2));
    SourceFile sf2 = new SourceFile("test2.js", code2);
    assertEquals(10, sf2.getLineOffset(1));
    assertEquals(26, sf2.getLineOffset(2));
    assertEquals(48, sf2.getLineOffset(3));
    assertEquals(61, sf2.getLineOffset(4));
  }

  @Test
  public void testGetNumLines() throws IOException {
    String code = "function test1() {\n" +
        "  return 42;\n" +
        "}";
    String code2 = "function test2() {\n" +
        "  return 42;\n" +
        "  function inner() {\n" +
        "    return 43;\n" +
        "  }\n" +
        "}";
    SourceFile sf = new SourceFile("test.js", code);
    assertEquals(1, sf.getNumLines());
    SourceFile sf2 = new SourceFile("test2.js", code2);
    assertEquals(4, sf2.getNumLines());
  }
}

class Region {
  private final int lineNumberStart;
  private final int lineNumberEnd;
  private final String code;

  public Region(int lineNumberStart, int lineNumberEnd, String code) {
    this.lineNumberStart = lineNumberStart;
    this.lineNumberEnd = lineNumberEnd;
    this.code = code;
  }

  public int getLineNumberStart() {
    return lineNumberStart;
  }

  public int getLineNumberEnd() {
    return lineNumberEnd;
  }

  public String getCode() {
    return code;
  }
}