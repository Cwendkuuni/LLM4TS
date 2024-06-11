package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class SourceFileTest {

  @Test
  public void testGetLine() {
    SourceFile file = new SourceFile("test.js");
    file.setCode("line 1\nline 2\nline 3");

    assertEquals("line 1", file.getLine(1));
    assertEquals("line 2", file.getLine(2));
    assertEquals("line 3", file.getLine(3));
    assertNull(file.getLine(4));
  }

  @Test
  public void testGetRegion() {
    SourceFile file = new SourceFile("test.js");
    file.setCode("line 1\nline 2\nline 3");

    Region region = file.getRegion(2);
    assertEquals(2, region.getStartLineNumber());
    assertEquals(2, region.getEndLineNumber());
    assertEquals("line 2", region.getSource());
  }

  @Test
  public void testGetLineAndRegionFromFile() throws IOException {
    String fileContent = "function testFunction() {\n" +
        "  // some code\n" +
        "}";
    File testFile = new File("test.js");
    Files.write(testFile, fileContent.getBytes(Charsets.UTF_8));

    SourceFile file = new SourceFile("test.js");
    assertEquals("function testFunction() {\n" +
        "  // some code\n" +
        "}", file.getLine(1));

    Region region = file.getRegion(2);
    assertEquals(2, region.getStartLineNumber());
    assertEquals(2, region.getEndLineNumber());
    assertEquals("  // some code\n", region.getSource());

    Files.delete(testFile);
  }

  @Test
  public void testGetCodeFromInputStream() throws IOException {
    String fileContent = "function testFunction() {\n" +
        "  // some code\n" +
        "}";
    File testFile = new File("test.js");
    Files.write(testFile, fileContent.getBytes(Charsets.UTF_8));

    try (InputStream inputStream = new FileInputStream(testFile)) {
      SourceFile file = new SourceFile("test.js");
      file.setCodeReader(new InputStreamReader(inputStream, Charsets.UTF_8));
      assertEquals("function testFunction() {\n" +
          "  // some code\n" +
          "}", file.getLine(1));

      Region region = file.getRegion(2);
      assertEquals(2, region.getStartLineNumber());
      assertEquals(2, region.getEndLineNumber());
      assertEquals("  // some code\n", region.getSource());
    }

    Files.delete(testFile);
  }

  @Test
  public void testGetCodeFromGenerator() {
    SourceFile file = new SourceFile("test.js");
    file.setGenerator(() -> "function testFunction() {\n" +
        "  // some code\n" +
        "}");

    assertEquals("function testFunction() {\n" +
        "  // some code\n" +
        "}", file.getLine(1));

    Region region = file.getRegion(2);
    assertEquals(2, region.getStartLineNumber());
    assertEquals(2, region.getEndLineNumber());
    assertEquals("  // some code\n", region.getSource());
  }
}

class Region {
  private final int startLineNumber;
  private final int endLineNumber;
  private final String source;

  public Region(int startLineNumber, int endLineNumber, String source) {
    this.startLineNumber = startLineNumber;
    this.endLineNumber = endLineNumber;
    this.source = source;
  }

  public int getStartLineNumber() {
    return startLineNumber;
  }

  public int getEndLineNumber() {
    return endLineNumber;
  }

  public String getSource() {
    return source;
  }
}