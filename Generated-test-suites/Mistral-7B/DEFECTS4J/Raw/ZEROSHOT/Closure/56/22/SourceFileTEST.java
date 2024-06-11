import static org.junit.Assert.*;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SourceFileTest {

  @SuppressWarnings("result-of-method-call-ignored")
  @org.junit.Test
  public void testSourceFileCreation() throws IOException {
    String fileName = "testFile.js";
    SourceFile sourceFile = new SourceFile(fileName);
    assertEquals(fileName, sourceFile.getName());
    assertFalse(sourceFile.isExtern());
    assertNull(sourceFile.getCode());
    assertNull(sourceFile.getOriginalPath());
    assertNull(sourceFile.lineOffsets);
    assertEquals(0, sourceFile.getLineOffset(1));
    assertEquals(1, sourceFile.getNumLines());
  }

  @org.junit.Test
  public void testGetLine() throws IOException {
    String fileName = "testFile.js";
    String code = "function test(){\n" +
                  "  var x = 5;\n" +
                  "  return x;\n" +
                  "}";
    SourceFile sourceFile = new SourceFile.Preloaded(fileName, code);
    assertEquals("function test(){\n", sourceFile.getLine(1));
    assertEquals("  var x = 5;\n", sourceFile.getLine(2));
    assertEquals("return x;\n", sourceFile.getLine(3));
  }

  @org.junit.Test
  public void testGetRegion() throws IOException {
    String fileName = "testFile.js";
    String code = "function test(){\n" +
                  "  var x = 5;\n" +
                  "  return x;\n" +
                  "}";
    SourceFile sourceFile = new SourceFile.Preloaded(fileName, code);
    Region region = sourceFile.getRegion(3);
    assertEquals(1, region.getStartLineNumber());
    assertEquals(3, region.getEndLineNumber());
    assertEquals("return x;\n", region.getSource());
  }

  @org.junit.Test
  public void testLineOffsets() throws IOException {
    String fileName = "testFile.js";
    String code = "function test(){\n" +
                  "  var x = 5;\n" +
                  "  return x;\n" +
                  "}";
    SourceFile sourceFile = new SourceFile.Preloaded(fileName, code);
    int[] lineOffsets = sourceFile.lineOffsets;
    assertEquals(3, lineOffsets.length);
    assertEquals(1, lineOffsets[1]);
    assertEquals(15, lineOffsets[3]);
  }

  @org.junit.Test
  public void testGetCode() throws IOException {
    String fileName = "testFile.js";
    String code = "function test(){\n" +
                  "  var x = 5;\n" +
                  "  return x;\n" +
                  "}";
    SourceFile sourceFile = new SourceFile.Preloaded(fileName, code);
    String sourceCode = sourceFile.getCode();
    assertEquals(code, sourceCode);
  }

  @org.junit.Test
  public void testGetCodeReader() throws IOException {
    String fileName = "testFile.js";
    String code = "function test(){\n" +
                  "  var x = 5;\n" +
                  "  return x;\n" +
                  "}";
    SourceFile sourceFile = new SourceFile.Preloaded(fileName, code);
    Reader reader = sourceFile.getCodeReader();
    char[] buffer = new char[code.length()];
    int length = reader.read(buffer);
    assertEquals(code.length(), length);
    String readSource = new String(buffer, 0, length);
    assertEquals(code, readSource);
  }

  @org.junit.Test
  public void testFromFile() throws IOException {
    String fileName = "testFile.js";
    File file = new File(fileName);
    String code = Files.toString(file, StandardCharsets.UTF_8);
    SourceFile sourceFile = SourceFile.fromFile(fileName, StandardCharsets.UTF_8);
    assertEquals(fileName, sourceFile.getName());
    assertTrue(sourceFile.hasSourceInMemory());
    assertEquals(code, sourceFile.getCode());
  }

  @org.junit.Test
  public void testFromInputStream() throws IOException {
    String fileName = "testFile.js";
    File file = new File(fileName);
    InputStream inputStream = new FileInputStream(file);
    String code = Files.toString(file, StandardCharsets.UTF_8);
    SourceFile sourceFile = SourceFile.fromInputStream(fileName, code, inputStream);
    assertEquals(fileName, sourceFile.getName());
    assertTrue(sourceFile.hasSourceInMemory());
    assertEquals(code, sourceFile.getCode());
  }

  @org.junit.Test
  public void testFromGenerator() throws IOException {
    String fileName = "testFile.js";
    String code = "function test(){\n" +
                  "  var x = 5;\n" +
                  "  return x;\n" +
                  "}";
    SourceFile.Generator generator = new SourceFile.Generator() {
      @Override
      public String getCode() {
        return code;
      }
    };
    SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);
    assertEquals(fileName, sourceFile.getName());
    assertTrue(sourceFile.hasSourceInMemory());
    assertEquals(code, sourceFile.getCode());
  }

  @org.junit.Test
  public void testReadFileWithInvalidCharset() throws IOException {
    String fileName = "testFile.js";
    SourceFile sourceFile = SourceFile.fromFile(fileName, null);
    assertNull(sourceFile.getCode());
  }

  @org.junit.Test
  public void testReadFileWithNonExistentFile() throws IOException {
    String fileName = "nonexistentFile.js";
    SourceFile sourceFile = SourceFile.fromFile(fileName, StandardCharsets.UTF_8);
    assertNull(sourceFile.getCode());
  }

  @org.junit.Test
  public void testSetOriginalPath() throws IOException {
    String fileName = "testFile.js";
    String originalPath = "path/to/testFile.js";
    SourceFile sourceFile = new SourceFile.Preloaded(fileName, "test code");
    sourceFile.setOriginalPath(originalPath);
    assertEquals(originalPath, sourceFile.getOriginalPath());
  }

  @org.junit.Test
  public void testClearCache() throws IOException {
    String fileName = "testFile.js";
    String code = "function test(){\n" +
                  "  var x = 5;\n" +
                  "  return x;\n" +
                  "}";
    SourceFile sourceFile = new SourceFile.Preloaded(fileName, code);
    String originalCode = sourceFile.getCode();
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
    assertEquals(originalCode, Files.toString(new File(fileName), StandardCharsets.UTF_8));
  }
}

class Region {
  int startLineNumber;
  int endLineNumber;
  String source;

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