package com.google.javascript.jscomp; // Make sure to import all necessary classes

import static org.junit.Assert.*;

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
    assertFalse(sourceFile.isExtern());
  }

  @Test
  public void testGetLineOffset() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("testFile.js"));
    String code = sourceFile.getCode();
    String testCode = "function testFunction() {\n" +
        "  return 42;\n" +
        "}";
    sourceFile.setCode(testCode);

    int lineNumber = 2;
    int expectedLineOffset = testCode.indexOf("return", 0) + testCode.indexOf("\n", testCode.indexOf("return", 0));

    int lineOffset = sourceFile.getLineOffset(lineNumber);
    assertEquals(expectedLineOffset, lineOffset);
  }

  @Test
  public void testGetLine() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("testFile.js"));
    String code = sourceFile.getCode();
    String testCode = "function testFunction() {\n" +
        "  return 42;\n" +
        "}";
    sourceFile.setCode(testCode);

    int lineNumber = 2;
    String expectedLine = "  return 42;\n";

    String line = sourceFile.getLine(lineNumber);
    assertEquals(expectedLine, line);
  }

  @Test
  public void testGetRegion() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("testFile.js"));
    String code = sourceFile.getCode();
    String testCode = "function testFunction() {\n" +
        "  return 42;\n" +
        "}\n" +
        "function anotherFunction() {\n" +
        "  return 3;\n" +
        "}\n";
    sourceFile.setCode(testCode);

    int lineNumber = 3;
    Region expectedRegion = new Region(1, 3, "function anotherFunction() {\n" +
        "  return 3;\n" +
        "}");

    Region region = sourceFile.getRegion(lineNumber);
    assertEquals(expectedRegion.getStartLine(), region.getStartLine());
    assertEquals(expectedRegion.getEndLine(), region.getEndLine());
    assertEquals(expectedRegion.getCode(), region.getCode());
  }

  @Test
  public void testGetCode() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("testFile.js"));
    String expectedCode = "function testFunction() {\n" +
        "  return 42;\n" +
        "}";

    String code = sourceFile.getCode();
    assertEquals(expectedCode, code);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileConstructorWithEmptyFileName() {
    new SourceFile("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetWithInvalidLineNumber() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("testFile.js"));
    String code = sourceFile.getCode();
    sourceFile.setCode(code);

    int lineNumber = 10;
    sourceFile.getLineOffset(lineNumber);
  }

  @Test(expected = IOException.class)
  public void testGetCodeWithInvalidFile() throws IOException {
    new SourceFile("nonExistentFile.js").getCode();
  }
}