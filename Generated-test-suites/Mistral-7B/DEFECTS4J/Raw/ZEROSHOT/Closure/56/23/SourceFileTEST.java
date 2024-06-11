package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.jstype.StaticSourceFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testBasic() throws IOException {
    String filePath = "test.js";
    String code = "function test() {}",
        expectedCode = "function test() {\n  // function body\n}",
        fileName = "test.js";

    SourceFile sourceFile = SourceFile.fromFile(filePath, StandardCharsets.UTF_8);

    assertEquals(fileName, sourceFile.getName());
    assertEquals(expectedCode, sourceFile.getCode());
    assertEquals(expectedCode, sourceFile.getCodeNoCache());
    assertEquals(2, sourceFile.getNumLines());

    // Test getLine method
    String line1 = sourceFile.getLine(1);
    assertEquals("function test() {", line1);

    String line2 = sourceFile.getLine(2);
    assertNull(line2);

    // Test getRegion method
    Region region = sourceFile.getRegion(2);
    assertEquals(1, region.getStartLineNumber());
    assertEquals(2, region.getEndLineNumber());
    assertEquals(expectedCode, region.getSourceCode());
  }

  @Test
  public void testFromString() throws IOException {
    String code = "function test() {}",
        fileName = "test.js";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);

    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
    assertEquals(code, sourceFile.getCodeNoCache());
    assertEquals(1, sourceFile.getNumLines());

    String line1 = sourceFile.getLine(1);
    assertEquals("function test() {", line1);

    Region region = sourceFile.getRegion(1);
    assertEquals(1, region.getStartLineNumber());
    assertEquals(1, region.getEndLineNumber());
    assertEquals(code, region.getSourceCode());
  }

  @Test
  public void testFromInputStream() throws IOException {
    String filePath = "test.js";
    String code = "function test() {}",
        expectedCode = "function test() {\n  // function body\n}",
        fileName = "test.js";

    File file = new File(filePath);
    InputStream inputStream = Files.newInputStream(file);

    SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream);

    assertEquals(fileName, sourceFile.getName());
    assertEquals(expectedCode, sourceFile.getCode());
    assertEquals(expectedCode, sourceFile.getCodeNoCache());
    assertEquals(2, sourceFile.getNumLines());

    String line1 = sourceFile.getLine(1);
    assertEquals("function test() {", line1);

    String line2 = sourceFile.getLine(2);
    assertNull(line2);

    Region region = sourceFile.getRegion(2);
    assertEquals(1, region.getStartLineNumber());
    assertEquals(2, region.getEndLineNumber());
    assertEquals(expectedCode, region.getSourceCode());

    // Test clearing cached source
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
  }
}