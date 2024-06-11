package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;
import com.google.javascript.jscomp.SourceFile.OnDisk;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testSourceFileCreation() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertNotNull(sourceFile);
    assertEquals("test.js", sourceFile.getName());
  }

  @Test
  public void testGetLine() throws IOException {
    String testCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", testCode);

    String line1 = sourceFile.getLine(1);
    assertEquals("function test() {\n", line1);

    String line2 = sourceFile.getLine(2);
    assertEquals("  var x = 1;\n", line2);

    String line3 = sourceFile.getLine(3);
    assertEquals("  var y = 2;\n", line3);

    String line4 = sourceFile.getLine(4);
    assertEquals("  return x + y;\n", line4);

    String line5 = sourceFile.getLine(5);
    assertNull(line5);
  }

  @Test
  public void testGetLineOffset() {
    String testCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", testCode);

    int line1Offset = sourceFile.getLineOffset(1);
    assertEquals(1, line1Offset);

    int line2Offset = sourceFile.getLineOffset(2);
    assertEquals(2, line2Offset);

    int line3Offset = sourceFile.getLineOffset(3);
    assertEquals(4, line3Offset);

    int line4Offset = sourceFile.getLineOffset(4);
    assertEquals(7, line4Offset);

    int line5Offset = sourceFile.getLineOffset(5);
    assertNull(line5Offset);
  }

  @Test
  public void testGetRegion() throws IOException {
    String testCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", testCode);

    String region1 = sourceFile.getRegion(1).toString();
    assertEquals("function test() {\n", region1);

    String region2 = sourceFile.getRegion(2).toString();
    assertEquals("  var x = 1;\n", region2);

    String region3 = sourceFile.getRegion(3).toString();
    assertEquals("  var y = 2;\n", region3);

    String region4 = sourceFile.getRegion(4).toString();
    assertEquals("  return x + y;\n}", region4);

    String region5 = sourceFile.getRegion(5).toString();
    assertNull(region5);
  }

  @Test
  public void testFromFile() throws IOException {
    String testFilePath = "/test/test.js";
    String testCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    Files.write(new File(testFilePath), testCode.getBytes("UTF-8"));

    SourceFile sourceFile = SourceFile.fromFile(testFilePath);
    String line1 = sourceFile.getLine(1);
    assertEquals("function test() {\n", line1);

    String line2 = sourceFile.getLine(2);
    assertEquals("  var x = 1;\n", line2);

    String line3 = sourceFile.getLine(3);
    assertEquals("  var y = 2;\n", line3);

    String line4 = sourceFile.getLine(4);
    assertEquals("  return x + y;\n", line4);

    Files.delete(new File(testFilePath));
  }

  @Test
  public void testFromInputStream() throws IOException {
    String testCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    InputStream testInputStream = Resources.getResource("test/test.js").openStream();

    SourceFile sourceFile = SourceFile.fromInputStream("test.js", testInputStream);
    String line1 = sourceFile.getLine(1);
    assertEquals("function test() {\n", line1);

    String line2 = sourceFile.getLine(2);
    assertEquals("  var x = 1;\n", line2);

    String line3 = sourceFile.getLine(3);
    assertEquals("  var y = 2;\n", line3);

    String line4 = sourceFile.getLine(4);
    assertEquals("  return x + y;\n", line4);

    testInputStream.close();
  }

  @Test
  public void testFromGenerator() {
    String testCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    Generator testGenerator = mock(Generator.class);
    when(testGenerator.getCode()).thenReturn(testCode);

    SourceFile sourceFile = new SourceFile.Generated("test.js", testGenerator);
    String line1 = sourceFile.getLine(1);
    assertEquals("function test() {\n", line1);

    String line2 = sourceFile.getLine(2);
    assertEquals("  var x = 1;\n", line2);

    String line3 = sourceFile.getLine(3);
    assertEquals("  var y = 2;\n", line3);

    String line4 = sourceFile.getLine(4);
    assertEquals("  return x + y;\n", line4);
  }

  @Test
  public void testOnDisk() throws IOException {
    String testCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";
    File testFile = new File("/test/test.js");
    Files.write(testFile, testCode.getBytes("UTF-8"));

    SourceFile sourceFile = SourceFile.fromFile("/test/test.js");
    String line1 = sourceFile.getLine(1);
    assertEquals("function test() {\n", line1);

    String line2 = sourceFile.getLine(2);
    assertEquals("  var x = 1;\n", line2);

    String line3 = sourceFile.getLine(3);
    assertEquals("  var y = 2;\n", line3);

    String line4 = sourceFile.getLine(4);
    assertEquals("  return x + y;\n", line4);

    Files.delete(testFile);
  }
}