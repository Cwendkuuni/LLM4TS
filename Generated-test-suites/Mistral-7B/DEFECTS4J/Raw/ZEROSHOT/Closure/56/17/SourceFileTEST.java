package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.OnDisk;

public class SourceFileTest {

  @Test
  public void testGetCode() throws IOException {
    String testFilePath = "testFile.js";
    File testFile = new File(testFilePath);

    String testCode = "function testFunction() {\n" +
      "  return 42;\n" +
      "}";

    Files.write(testFile, testCode.getBytes(StandardCharsets.UTF_8));

    SourceFile sourceFile = new OnDisk(testFile);

    String code = sourceFile.getCode();

    assertEquals(testCode, code);

    Files.delete(testFile);
  }

  @Test
  public void testGetCode_EmptyFile() throws IOException {
    String testFilePath = "emptyFile.js";
    File testFile = new File(testFilePath);

    SourceFile sourceFile = new OnDisk(testFile);

    String code = sourceFile.getCode();

    assertNull(code);

    Files.delete(testFile);
  }

  @Test
  public void testGetCode_NullFile() {
    SourceFile sourceFile = new OnDisk(null);

    String code = sourceFile.getCode();

    assertNull(code);
  }

  @Test
  public void testGetLine() {
    String testCode = "function testFunction() {\n" +
      "  return 42;\n" +
      "}";

    SourceFile sourceFile = new Preloaded("testFile.js", testCode);

    assertEquals("function testFunction() {\n", sourceFile.getLine(1));
    assertEquals("  return 42;\n", sourceFile.getLine(2));
    assertNull(sourceFile.getLine(3));
  }

  @Test
  public void testGetLine_NullFile() {
    SourceFile sourceFile = new Preloaded(null, "");

    assertNull(sourceFile.getLine(1));
  }

  @Test
  public void testGetRegion() {
    String testCode = "function testFunction1() {\n" +
      "  return 42;\n" +
      "}\n" +
      "\n" +
      "function testFunction2() {\n" +
      "  return 43;\n" +
      "}";

    SourceFile sourceFile = new Preloaded("testFile.js", testCode);

    Region region = sourceFile.getRegion(3);

    assertSame(3, region.getStartLineNumber());
    assertSame(4, region.getEndLineNumber());
    assertEquals("function testFunction2() {\n  return 43;\n}", region.getText());
  }

  @Test
  public void testGetRegion_NullFile() {
    SourceFile sourceFile = new Preloaded(null, "");

    Region region = sourceFile.getRegion(1);

    assertNull(region);
  }
}