package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SourceFileTest {

  @Test
  public void testSourceFileConstructor() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    assertEquals("testFile.js", sourceFile.getName());
    assertEquals("testFile.js", sourceFile.getOriginalPath());
  }

  @Test
  public void testGetLineOffset() throws IOException {
    String code = "function test() {\n" +
                  "  var a = 1;\n" +
                  "  var b = 2;\n" +
                  "  var c = 3;\n" +
                  "}";

    SourceFile sourceFile = new Preloaded("testFile.js", code);

    assertEquals(1, sourceFile.getLineOffset(1));
    assertEquals(9, sourceFile.getLineOffset(5));
  }

  @Test
  public void testGetNumLines() throws IOException {
    String code = "function test() {\n" +
                  "  var a = 1;\n" +
                  "  var b = 2;\n" +
                  "  var c = 3;\n" +
                  "}";

    SourceFile sourceFile = new Preloaded("testFile.js", code);

    assertEquals(4, sourceFile.getNumLines());
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    String code = "function test() {\n" +
                  "  var a = 1;\n" +
                  "  var b = 2;\n" +
                  "  var c = 3;\n" +
                  "}";

    SourceFile sourceFile = new Preloaded("testFile.js", code);
    sourceFile.findLineOffsets();

    assertEquals(1, sourceFile.lineOffsets[0]);
    assertEquals(10, sourceFile.lineOffsets[1]);
    assertEquals(22, sourceFile.lineOffsets[2]);
    assertEquals(33, sourceFile.lineOffsets[3]);
  }

  @Test
  public void testGetCode() throws IOException {
    String code = "function test() {\n" +
                  "  var a = 1;\n" +
                  "  var b = 2;\n" +
                  "  var c = 3;\n" +
                  "}";

    SourceFile sourceFile = new Preloaded("testFile.js", code);
    assertEquals(code, sourceFile.getCode());
  }

  @Test
  public void testGetCodeReader() throws IOException {
    String code = "function test() {\n" +
                  "  var a = 1;\n" +
                  "  var b = 2;\n" +
                  "  var c = 3;\n" +
                  "}";

    SourceFile sourceFile = new Preloaded("testFile.js", code);

    StringReader reader = new StringReader(sourceFile.getCode());
    assertEquals(reader.read(), sourceFile.getCode().charAt(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentExceptionLineNumber() throws IOException {
    SourceFile sourceFile = new Preloaded("testFile.js", "");
    sourceFile.getLineOffset(-1);
  }

  @Test
  public void testGetRegion() throws IOException {
    String code = "function test() {\n" +
                  "  var a = 1;\n" +
                  "  var b = 2;\n" +
                  "  var c = 3;\n" +
                  "}";

    SourceFile sourceFile = new Preloaded("testFile.js", code);
    Region region = sourceFile.getRegion(5);

    assertEquals("  var b = 2;\n" +
                 "  var c = 3;\n", region.getCode());
    assertEquals(3, region.getLineNumber());
    assertEquals(3, region.getEndLineNumber());
  }

  @Test
  public void testGetRegionNull() throws IOException {
    String code = "function test() {\n" +
                  "  var a = 1;\n" +
                  "}";

    SourceFile sourceFile = new Preloaded("testFile.js", code);
    assertNull(sourceFile.getRegion(10));
  }

  @Test
  public void testToString() throws IOException {
    String code = "function test() {\n" +
                  "  var a = 1;\n" +
                  "  var b = 2;\n" +
                  "  var c = 3;\n" +
                  "}";

    SourceFile sourceFile = new Preloaded("testFile.js", code);
    assertEquals("testFile.js", sourceFile.toString());
  }

  @Test
  public void testFromFile() throws IOException {
    String filePath = "testFile.txt";
    File file = new File(filePath);
    String code = "function test() {\n" +
                  "  var a = 1;\n" +
                  "  var b = 2;\n" +
                  "  var c = 3;\n" +
                  "}";

    Files.write(file.toPath(), code.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);

    SourceFile sourceFile = new OnDisk(file, StandardCharsets.UTF_8);
    assertEquals(code, sourceFile.getCode());

    Files.delete(file.toPath());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentExceptionNullFileName() {
    assertThrows(IllegalArgumentException.class, () -> {
      new SourceFile(null);
    });
  }

  @Test
  public void testIsExtern() throws IOException {
    SourceFile sourceFile1 = new Preloaded("testFile1.js", "");
    assertFalse(sourceFile1.isExtern());

    SourceFile sourceFile2 = new OnDisk(new File("testFile2.js"), StandardCharsets.UTF_8);
    sourceFile2.setIsExtern(true);
    assertTrue(sourceFile2.isExtern());
  }

  @Test
  public void testSetOriginalPath() throws IOException {
    String code = "function test() {\n" +
                  "  var a = 1;\n" +
                  "  var b = 2;\n" +
                  "  var c = 3;\n" +
                  "}";

    SourceFile sourceFile = new Preloaded("testFile.js", code);

    sourceFile.setOriginalPath("testFileOriginalPath.js");
    assertEquals("testFileOriginalPath.js", sourceFile.getOriginalPath());
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String code = "function test() {\n" +
                  "  var a = 1;\n" +
                  "  var b = 2;\n" +
                  "  var c = 3;\n" +
                  "}";

    SourceFile sourceFile = new Preloaded("testFile.js", code);

    assertEquals(code, sourceFile.getCode());
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
  }

  @Test
  public void testHasSourceInMemory() throws IOException {
    String code = "function test() {\n" +
                  "  var a = 1;\n" +
                  "  var b = 2;\n" +
                  "  var c = 3;\n" +
                  "}";

    SourceFile sourceFile = new Preloaded("testFile.js", code);

    assertTrue(sourceFile.hasSourceInMemory());

    sourceFile.clearCachedSource();

    assertFalse(sourceFile.hasSourceInMemory());
  }
}