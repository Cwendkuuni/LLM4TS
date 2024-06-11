package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

public class SourceFileTest {

  private SourceFile sourceFile;

  @Before
  public void setUp() {
    sourceFile = new SourceFile("test SourceFile.java");
  }

  // Test getLineOffset method

  @Test
  public void testGetLineOffset_validLineNumber() throws IOException {
    String code = "line1\nline2\nline3";
    sourceFile.setCode(code);
    int expectedLineOffset = 12; // line1 has 6 chars, so 6+1
    int actualLineOffset = sourceFile.getLineOffset(1);
    assertEquals(expectedLineOffset, actualLineOffset);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffset_invalidLineNumber() throws IOException {
    String code = "line1\nline2\nline3";
    sourceFile.setCode(code);
    sourceFile.getLineOffset(10);
  }

  // Test getCode method

  @Test(expected = IOException.class)
  public void testGetCode_noCode() throws IOException {
    sourceFile.getCode();
  }

  @Test
  public void testGetCode_hasCode() throws IOException {
    String code = "test code";
    sourceFile.setCode(code);
    String actualCode = sourceFile.getCode();
    assertEquals(code, actualCode);
  }

  // Test getCodeReader method

  @Test
  public void testGetCodeReader_hasCode() throws IOException {
    String code = "test code";
    sourceFile.setCode(code);
    Reader expectedCodeReader = new StringReader(code);
    Reader actualCodeReader = sourceFile.getCodeReader();
    assertEquals(expectedCodeReader, actualCodeReader);
  }

  @Test(expected = IOException.class)
  public void testGetCodeReader_noCode() throws IOException {
    sourceFile.getCodeReader();
  }

  // Test getRegion method

  @Test
  public void testGetRegion_validLineNumber() throws IOException {
    String code = "line1\nline2\nline3";
    sourceFile.setCode(code);
    int lineNumber = 2;
    Region expectedRegion = new Region(1, 2, "line2");
    Region actualRegion = sourceFile.getRegion(lineNumber);
    assertEquals(expectedRegion.getStartLine(), actualRegion.getStartLine());
    assertEquals(expectedRegion.getEndLine(), actualRegion.getEndLine());
    assertEquals(expectedRegion.getCode(), actualRegion.getCode());
  }

  @Test
  public void testGetRegion_lineNumberAtBeginning() throws IOException {
    String code = "line1\nline2\nline3";
    sourceFile.setCode(code);
    int lineNumber = 1;
    Region expectedRegion = new Region(1, 1, "line1");
    Region actualRegion = sourceFile.getRegion(lineNumber);
    assertEquals(expectedRegion.getStartLine(), actualRegion.getStartLine());
    assertEquals(expectedRegion.getEndLine(), actualRegion.getEndLine());
    assertEquals(expectedRegion.getCode(), actualRegion.getCode());
  }

  @Test
  public void testGetRegion_lineNumberAtEnd() throws IOException {
    String code = "line1\nline2\nline3";
    sourceFile.setCode(code);
    int lineNumber = 4;
    Region expectedRegion = null;
    Region actualRegion = sourceFile.getRegion(lineNumber);
    assertNull(actualRegion);
    assertEquals(expectedRegion, actualRegion);
  }

  @Test
  public void testGetRegion_lineNumberInMiddle() throws IOException {
    String code = "line1\nline2\nline3";
    sourceFile.setCode(code);
    int lineNumber = 3;
    Region expectedRegion = new Region(1, 3, "line3");
    Region actualRegion = sourceFile.getRegion(lineNumber);
    assertEquals(expectedRegion.getStartLine(), actualRegion.getStartLine());
    assertEquals(expectedRegion.getEndLine(), actualRegion.getEndLine());
    assertEquals(expectedRegion.getCode(), actualRegion.getCode());
  }

  // Test toString method

  @Test
  public void testToString() {
    String expectedString = "test SourceFile.java";
    String actualString = sourceFile.toString();
    assertEquals(expectedString, actualString);
  }

  // Test fromFile methods

  @Test
  public void testFromFile_validFileName() throws IOException {
    String fileName = "testSourceFile.java";
    File file = new File(fileName);
    SourceFile expectedSourceFile = new OnDisk(file);
    SourceFile actualSourceFile =
        SourceFile.fromFile(fileName, Charsets.UTF_8);
    assertEquals(expectedSourceFile.getClass(), actualSourceFile.getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromFile_invalidFileName() {
    SourceFile.fromFile(null);
  }

  // Test fromCode methods

  @Test
  public void testFromCode_validFileNameAndCode() throws IOException {
    String fileName = "testSourceFile.java";
    String code = "test code";
    SourceFile expectedSourceFile = new Preloaded(fileName, code);
    SourceFile actualSourceFile =
        SourceFile.fromCode(fileName, code);
    assertEquals(expectedSourceFile.getClass(), actualSourceFile.getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromCode_nullFileName() {
    String code = "test code";
    SourceFile.fromCode(null, code);
  }

  @Test(expected = NullPointerException.class)
  public void testFromCode_nullCode() {
    String fileName = "testSourceFile.java";
    SourceFile.fromCode(fileName, null);
  }

  // Test fromInputStream methods

  @Test
  public void testFromInputStream_validFileNameAndInputStream() throws IOException {
    String fileName = "testSourceFile.java";
    String code = "test code";
    File tempFile = File.createTempFile("test", null);
    Files.write(tempFile, code.getBytes(Charsets.UTF_8));
    InputStream inputStream = Files.newInputStream(tempFile.toPath());
    SourceFile expectedSourceFile = new Preloaded(fileName, code);
    SourceFile actualSourceFile =
        SourceFile.fromInputStream(fileName, inputStream);
    assertEquals(expectedSourceFile.getClass(), actualSourceFile.getClass());
    Files.delete(tempFile.toPath());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromInputStream_nullFileName() {
    InputStream inputStream = new ByteArrayInputStream("test code".getBytes());
    SourceFile.fromInputStream(null, inputStream);
  }

  @Test(expected = NullPointerException.class)
  public void testFromInputStream_nullInputStream() {
    String fileName = "testSourceFile.java";
    SourceFile.fromInputStream(fileName, null);
  }

  // Test fromReader methods

  @Test
  public void testFromReader_validFileNameAndReader() throws IOException {
    String fileName = "testSourceFile.java";
    String code = "test code";
    File tempFile = File.createTempFile("test", null);
    Files.write(tempFile, code.getBytes(Charsets.UTF_8));
    Reader reader = new FileReader(tempFile);
    SourceFile expectedSourceFile = new Preloaded(fileName, code);
    SourceFile actualSourceFile =
        SourceFile.fromReader(fileName, reader);
    assertEquals(expectedSourceFile.getClass(), actualSourceFile.getClass());
    reader.close();
    Files.delete(tempFile.toPath());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromReader_nullFileName() {
    String code = "test code";
    Reader reader = new StringReader(code);
    SourceFile.fromReader(null, reader);
  }

  @Test(expected = NullPointerException.class)
  public void testFromReader_nullReader() {
    String fileName = "testSourceFile.java";
    SourceFile.fromReader(fileName, null);
  }

  // Test fromGenerator methods

  @Test
  public void testFromGenerator_validFileNameAndGenerator() {
    String fileName = "testSourceFile.java";
    String code = "test code";
    Generator generator = () -> code;
    SourceFile expectedSourceFile = new Generated(fileName, generator);
    SourceFile actualSourceFile = SourceFile.fromGenerator(fileName, generator);
    assertEquals(expectedSourceFile.getClass(), actualSourceFile.getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromGenerator_nullFileName() {
    Generator generator = () -> "test code";
    SourceFile.fromGenerator(null, generator);
  }

  @Test(expected = NullPointerException.class)
  public void testFromGenerator_nullGenerator() {
    String fileName = "testSourceFile.java";
    SourceFile.fromGenerator(fileName, null);
  }
}