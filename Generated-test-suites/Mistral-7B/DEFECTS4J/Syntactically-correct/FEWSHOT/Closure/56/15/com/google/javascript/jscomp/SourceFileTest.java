package com.google.javascript.jscomp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testFindLineOffsets() {
    SourceFile sf = new SourceFile("test.js");
    sf.setCode("line1\nline2\nline3");

    int[] expectedLineOffsets = {1, 2, 3};

    sf.findLineOffsets();
    assertArrayEquals(expectedLineOffsets, sf.lineOffsets);
  }

  @Test
  public void testGetLine() {
    SourceFile sf = new SourceFile("test.js");

    sf.setCode("line1\nline2\nline3");

    assertEquals("line1", sf.getLine(1));
    assertEquals("line2", sf.getLine(2));
    assertEquals("line3", sf.getLine(3));
    assertNull(sf.getLine(4));

    // Test error cases
    assertThrows(IllegalArgumentException.class, () -> sf.getLine(0));
    assertThrows(IllegalArgumentException.class, () -> sf.getLine(5));
  }

  @Test
  public void testGetRegion() {
    SourceFile sf = new SourceFile("test.js");

    sf.setCode("line1\nline2\nline3");

    Region expectedRegion = new SimpleRegion(2, 3, "line2 line3");

    Region region = sf.getRegion(3);
    assertSame(expectedRegion, region);
  }

  @Test
  public void testFromFile() throws IOException {
    String fileContent = "function test() {}";

    SourceFile sf = SourceFile.fromFile("test.js", Charsets.UTF_8);
    sf.setCodeNoCache(fileContent);

    assertEquals("test.js", sf.getName());
    assertEquals(fileContent, sf.getCode());
  }

  @Test
  public void testFromGeneratedCode() {
    String fileName = "test.js";
    String code = "function test() {}";

    SourceFile sf = SourceFile.fromGenerator(fileName, () -> code);

    assertEquals(fileName, sf.getName());
    assertEquals(code, sf.getCodeNoCache());
  }

  @Test
  public void testFromInputStream() throws IOException {
    String content = "function test() {}";

    InputStream inputStream = new ByteArrayInputStream(content.getBytes());
    SourceFile sf = SourceFile.fromInputStream(null, inputStream);

    assertEquals("test.js", sf.getName());
    assertEquals(content, sf.getCode());
  }

  @Test
  public void testToString() {
    SourceFile sf = new SourceFile("test.js");

    assertEquals("test.js", sf.toString());
  }

  @Test
  public void testSetOriginalPath() {
    SourceFile sf = new SourceFile("test.js");

    sf.setOriginalPath("path/to/test.js");

    assertEquals("path/to/test.js", sf.getOriginalPath());
  }

  @Test
  public void testClearCachedSource() {
    String fileName = "test.js";
    String code = "function test() {}";

    SourceFile sf = SourceFile.fromGenerator(fileName, () -> code);

    assertEquals(code, sf.getCodeNoCache());

    sf.clearCachedSource();

    assertNull(sf.getCodeNoCache());
  }

  @Test
  public void testHasSourceInMemory() {
    SourceFile sf = new SourceFile("test.js");
    String code = "function test() {}";

    sf.setCodeNoCache(code);

    assertTrue(sf.hasSourceInMemory());
  }

  @Test
  public void testIsExtern() {
    SourceFile sf = new SourceFile("test.js");

    assertFalse(sf.isExtern());

    sf.setIsExtern(true);

    assertTrue(sf.isExtern());
  }
}