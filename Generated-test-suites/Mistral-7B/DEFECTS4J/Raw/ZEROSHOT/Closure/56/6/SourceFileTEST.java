package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;

public class SourceFileTest {

  @Test
  public void testSourceFileBasic() throws IOException {
    String fileName = "TestFile.js";
    String fileContent = "function testFunction() {\n" +
        "  return 1;\n" +
        "}";

    SourceFile sourceFile = SourceFile.fromCode(fileName, fileContent);
    assertEquals(fileContent, sourceFile.getCode());
    assertEquals(fileName, sourceFile.getName());
  }

  @Test
  public void testSourceFileFromFile() throws IOException {
    String fileName = "TestFile.js";
    String filePath = "src/test/resources/" + fileName;
    String fileContent = Files.toString(new File(filePath), StandardCharsets.UTF_8);

    SourceFile sourceFile = SourceFile.fromFile(fileName);
    assertEquals(fileContent, sourceFile.getCode());
    assertEquals(fileName, sourceFile.getName());
  }

  @Test
  public void testSourceFileGetLine() throws IOException {
    String fileName = "TestFile.js";
    String fileContent = "function testFunction() {\n" +
        "  return 1;\n" +
        "}";

    SourceFile sourceFile = SourceFile.fromCode(fileName, fileContent);
    String line1 = sourceFile.getLine(1);
    String line2 = sourceFile.getLine(2);

    assertEquals("function testFunction() {\n", line1);
    assertEquals("  return 1;\n", line2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileNegativeLineNumber() throws IOException {
    String fileName = "TestFile.js";
    SourceFile sourceFile = SourceFile.fromCode(fileName, "");
    sourceFile.getLine(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileOutOfRangeLineNumber() throws IOException {
    String fileName = "TestFile.js";
    String fileContent = "function testFunction() {\n" +
        "  return 1;\n" +
        "}";

    SourceFile sourceFile = SourceFile.fromCode(fileName, fileContent);
    sourceFile.getLine(1000);
  }

  @Test
  public void testSourceFileGetRegion() throws IOException {
    String fileName = "TestFile.js";
    String fileContent = "function testFunction1() {\n" +
        "  return 1;\n" +
        "}\n" +
        "\n" +
        "function testFunction2() {\n" +
        "  return 2;\n" +
        "}\n";

    SourceFile sourceFile = SourceFile.fromCode(fileName, fileContent);
    Region region = sourceFile.getRegion(2);

    assertEquals("function testFunction2() {\n  return 2;\n}", region.getCode());
    assertEquals(2, region.getStartLineNumber());
    assertEquals(2, region.getEndLineNumber());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileOutOfRangeRegionLineNumber() throws IOException {
    String fileName = "TestFile.js";
    String fileContent = "function testFunction1() {\n" +
        "  return 1;\n" +
        "}\n" +
        "\n" +
        "function testFunction2() {\n" +
        "  return 2;\n" +
        "}";

    SourceFile sourceFile = SourceFile.fromCode(fileName, fileContent);
    Region region = sourceFile.getRegion(1000);
  }
}

//////////////////////////////////////////////////////////////////////////////

class Region {
  private final int startLineNumber;
  private final int endLineNumber;
  private final String code;

  Region(int startLineNumber, int endLineNumber, String code) {
    this.startLineNumber = startLineNumber;
    this.endLineNumber = endLineNumber;
    this.code = code;
  }

  String getCode() {
    return code;
  }

  int getStartLineNumber() {
    return startLineNumber;
  }

  int getEndLineNumber() {
    return endLineNumber;
  }
}