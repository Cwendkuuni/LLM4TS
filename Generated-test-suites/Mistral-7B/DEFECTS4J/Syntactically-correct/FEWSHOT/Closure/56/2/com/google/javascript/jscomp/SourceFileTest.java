package com.google.javascript.jscomp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SourceFileTest {

  @Test
  public void testSimpleConstructor() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertEquals("test.js", sourceFile.getName());
    assertEquals(1, sourceFile.getNumLines());
    assertNull(sourceFile.getCode());
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js");
    String code = "line 1\nline 2\nline 3";
    sourceFile.setCode(code);

    int[] lineOffsets = sourceFile.getLineOffsets();
    assertEquals(3, lineOffsets.length);
    assertEquals(1, lineOffsets[0]);
    assertEquals(5, lineOffsets[1]);
    assertEquals(11, lineOffsets[2]);
  }

  @Test
  public void testGetLine() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js");
    String code = "line 1\nline 2\nline 3";
    sourceFile.setCode(code);

    String line1 = sourceFile.getLine(1);
    assertEquals("line 1", line1);

    String line3 = sourceFile.getLine(3);
    assertEquals("line 3", line3);

    String line4 = sourceFile.getLine(4);
    assertNull(line4);
  }

  @Test
  public void testGetRegion() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js");
    String code = "line 1\nline 2\nline 3";
    sourceFile.setCode(code);

    Region region = sourceFile.getRegion(2);
    assertEquals(2, region.getStartLineNumber());
    assertEquals(2, region.getEndLineNumber());
    assertEquals("line 2", region.getSource());
  }

  @Test
  public void testOnDisk() throws IOException {
    String testFilePath = "test_on_disk.js";
    File testFile = new File(testFilePath);
    Charset charset = Charsets.UTF_8;

    SourceFile sourceFile = SourceFile.fromFile(testFilePath, charset);
    assertEquals(testFilePath, sourceFile.getOriginalPath());
    assertEquals(testFilePath, sourceFile.getName());

    String code = sourceFile.getCode();
    assertEquals("", code);

    String expectedCode = Files.toString(testFile, charset);
    sourceFile.setCode(expectedCode);
    code = sourceFile.getCode();
    assertEquals(expectedCode, code);

    Region region = sourceFile.getRegion(2);
    assertEquals(2, region.getStartLineNumber());
    assertEquals(2, region.getEndLineNumber());
    assertEquals(Files.readAllLines(testFile.toPath()).get(1), region.getSource());
  }

  @Test
  public void testPreloaded() throws IOException {
    String fileName = "test_preloaded.js";
    String code = "const a = 1;";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    assertEquals(fileName, sourceFile.getOriginalPath());
    assertEquals(fileName, sourceFile.getName());

    String expectedCode = code;
    sourceFile.setCode(expectedCode);
    String codeFromSourceFile = sourceFile.getCode();
    assertEquals(expectedCode, codeFromSourceFile);

    Region region = sourceFile.getRegion(1);
    assertEquals(1, region.getStartLineNumber());
    assertEquals(1, region.getEndLineNumber());
    assertEquals(code.substring(0, code.indexOf('\n')), region.getSource());
  }

  @Test
  public void testGenerated() {
    String fileName = "test_generated.js";
    String expectedCode = "const a = 1;";

    SourceFile sourceFile = SourceFile.fromGenerator(fileName, () -> expectedCode);
    assertEquals(fileName, sourceFile.getOriginalPath());
    assertEquals(fileName, sourceFile.getName());

    String codeFromSourceFile = sourceFile.getCode();
    assertEquals(expectedCode, codeFromSourceFile);
  }

  @Test
  public void testGetLineOffsetsWithNullCode() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertThrows(IOException.class, () -> sourceFile.getLineOffsets());
  }

  @Test
  public void testGetLineWithNullCode() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertThrows(IOException.class, () -> sourceFile.getLine(1));
  }

  @Test
  public void testGetRegionWithNullCode() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertThrows(IOException.class, () -> sourceFile.getRegion(1));
  }

  @Test
  public void testSetCodeWithNullCode() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertThrows(NullPointerException.class, () -> sourceFile.setCode(null));
  }

  @Test
  public void testSetOriginalPathWithNullPath() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertThrows(NullPointerException.class, () -> sourceFile.setOriginalPath(null));
  }
}

class Region implements Serializable {
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