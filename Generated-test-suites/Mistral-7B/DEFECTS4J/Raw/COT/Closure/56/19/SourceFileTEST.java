package com.google.javascript.jscomp; // adjust the package name if necessary

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class SourceFileTest {
  private static final String TEST_FILE_NAME = "testFile.js";
  private static final String TEST_ORIGINAL_PATH = "testPath";
  private static final String TEST_CODE = "testCode";

  private SourceFile sourceFile;
  private Generator mockGenerator;

  @Before
  public void setup() {
    mockGenerator = mock(Generator.class);
    sourceFile = new Generated(TEST_FILE_NAME, mockGenerator);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testConstructor() {
    assertEquals(TEST_FILE_NAME, sourceFile.getName());
    assertNull(sourceFile.getOriginalPath());
    assertNull(sourceFile.getCode());
    assertFalse(sourceFile.isExtern());
  }

  @Test
  public void testGetLineOffset() {
    String testCode = "line1\nline2\nline3";
    when(mockGenerator.getCode()).thenReturn(testCode);
    sourceFile.setCode(testCode);

    int lineNumber = 2;
    int expectedLineOffset = 10; // calculate the expected line offset

    int actualLineOffset = sourceFile.getLineOffset(lineNumber);
    assertEquals(expectedLineOffset, actualLineOffset);
  }

  @Test
  public void testGetNumLines() {
    String testCode = "line1\nline2\nline3";
    when(mockGenerator.getCode()).thenReturn(testCode);
    sourceFile.setCode(testCode);

    int expectedNumLines = 3;
    int actualNumLines = sourceFile.getNumLines();

    assertEquals(expectedNumLines, actualNumLines);
  }

  @Test
  public void testGetRegion() {
    String testCode = "line1\nline2\nline3";
    when(mockGenerator.getCode()).thenReturn(testCode);
    sourceFile.setCode(testCode);

    int lineNumber = 2;
    Region expectedRegion = new Region(1, 2, "line2\nline3");

    Region actualRegion = sourceFile.getRegion(lineNumber);
    assertEquals(expectedRegion.getStartLine(), actualRegion.getStartLine());
    assertEquals(expectedRegion.getEndLine(), actualRegion.getEndLine());
    assertEquals(expectedRegion.getText(), actualRegion.getText());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetWithInvalidLineNumber() {
    String testCode = "line1";
    when(mockGenerator.getCode()).thenReturn(testCode);
    sourceFile.setCode(testCode);

    int lineNumber = 0;
    sourceFile.getLineOffset(lineNumber);
  }

  @Test
  public void testIsExternWithExternSourceFile() {
    sourceFile = new SourceFile(TEST_FILE_NAME) {
      @Override
      public boolean isExtern() {
        return true;
      }
    };

    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testIsExternWithNonExternSourceFile() {
    sourceFile = new SourceFile(TEST_FILE_NAME);

    assertFalse(sourceFile.isExtern());
  }

  @Test
  public void testGetCode() throws IOException {
    String testCode = "testCode";
    when(mockGenerator.getCode()).thenReturn(testCode);

    sourceFile.setCode(testCode);

    String actualCode = sourceFile.getCode();
    assertEquals(testCode, actualCode);
  }

  @Test
  public void testGetCodeReader() throws IOException {
    String testCode = "testCode";
    when(mockGenerator.getCode()).thenReturn(testCode);

    sourceFile.setCode(testCode);

    Reader actualCodeReader = sourceFile.getCodeReader();
    ArgumentCaptor<StringReader> stringReaderCaptor =
        ArgumentCaptor.forClass(StringReader.class);
    verify(mockGenerator).getCodeReader();
    verify(stringReaderCaptor.capture()).setInput(testCode);
    assertEquals(stringReaderCaptor.getValue(), actualCodeReader);
  }

  @Test
  public void testGetOriginalPath() {
    sourceFile.setOriginalPath(TEST_ORIGINAL_PATH);

    String actualOriginalPath = sourceFile.getOriginalPath();
    assertEquals(TEST_ORIGINAL_PATH, actualOriginalPath);
  }

  @Test
  public void testSetOriginalPath() {
    sourceFile.setOriginalPath(TEST_ORIGINAL_PATH);

    String newOriginalPath = "newOriginalPath";
    sourceFile.setOriginalPath(newOriginalPath);

    String actualOriginalPath = sourceFile.getOriginalPath();
    assertEquals(newOriginalPath, actualOriginalPath);
  }

  @Test
  public void testHasSourceInMemory() {
    String testCode = "testCode";
    when(mockGenerator.getCode()).thenReturn(testCode);
    sourceFile.setCode(testCode);

    assertTrue(sourceFile.hasSourceInMemory());
  }

  @Test
  public void testHasSourceInMemoryWithNullCode() {
    sourceFile.setCode(null);

    assertFalse(sourceFile.hasSourceInMemory());
  }

  @Test
  public void testToString() {
    assertEquals(TEST_FILE_NAME, sourceFile.toString());
  }

  @Test
  public void testFromFile() throws IOException {
    File testFile = new File("testFile.js");
    SourceFile expectedSourceFile = new OnDisk(testFile);

    SourceFile actualSourceFile = SourceFile.fromFile(testFile.getPath());

    assertEquals(expectedSourceFile.getName(), actualSourceFile.getName());
    assertEquals(expectedSourceFile.getOriginalPath(), actualSourceFile.getOriginalPath());
    assertEquals(expectedSourceFile.getCode(), actualSourceFile.getCode());
    assertSame(expectedSourceFile.getClass(), actualSourceFile.getClass());
  }

  @Test
  public void testFromFileNullFilePath() {
    SourceFile actualSourceFile = SourceFile.fromFile(null);

    assertNull(actualSourceFile);
  }

  @Test
  public void testFromCode() {
    String testFileName = "testFile.js";
    String testCode = "testCode";

    SourceFile expectedSourceFile = new Preloaded(testFileName, testCode);

    SourceFile actualSourceFile = SourceFile.fromCode(testFileName, testCode);

    assertEquals(expectedSourceFile.getName(), actualSourceFile.getName());
    assertEquals(testFileName, actualSourceFile.getOriginalPath());
    assertEquals(testCode, actualSourceFile.getCode());
    assertSame(expectedSourceFile.getClass(), actualSourceFile.getClass());
  }

  @Test
  public void testFromCodeNullFileName() {
    String testCode = "testCode";

    SourceFile actualSourceFile = SourceFile.fromCode(null, testCode);

    assertNull(actualSourceFile);
  }

  @Test
  public void testFromInputStream() throws IOException {
    String testFileName = "testFile.js";
    InputStream testInputStream = mock(InputStream.class);
    when(testInputStream.available()).thenReturn(testCode.length());
    when(testInputStream.readAllBytes()).thenReturn(testCode.getBytes());

    SourceFile expectedSourceFile = new Preloaded(testFileName, testCode);

    SourceFile actualSourceFile = SourceFile.fromInputStream(testFileName, testInputStream);

    assertEquals(expectedSourceFile.getName(), actualSourceFile.getName());
    assertEquals(testFileName, actualSourceFile.getOriginalPath());
    assertEquals(testCode, actualSourceFile.getCode());
    assertSame(expectedSourceFile.getClass(), actualSourceFile.getClass());
  }

  @Test
  public void testFromInputStreamNullFileName() {
    InputStream testInputStream = mock(InputStream.class);

    SourceFile actualSourceFile = SourceFile.fromInputStream(null, testInputStream);

    assertNull(actualSourceFile);
  }

  @Test
  public void testFromGenerator() {
    String testFileName = "testFile.js";
    String testCode = "testCode";
    when(mockGenerator.getCode()).thenReturn(testCode);

    SourceFile expectedSourceFile = new Generated(testFileName, mockGenerator);

    SourceFile actualSourceFile = SourceFile.fromGenerator(testFileName, mockGenerator);

    assertEquals(expectedSourceFile.getName(), actualSourceFile.getName());
    assertEquals(testFileName, actualSourceFile.getOriginalPath());
    assertEquals(testCode, actualSourceFile.getCode());
    assertSame(expectedSourceFile.getClass(), actualSourceFile.getClass());
  }

  @Test
  public void testFromGeneratorNullFileName() {
    Generator mockGenerator = mock(Generator.class);

    SourceFile actualSourceFile = SourceFile.fromGenerator(null, mockGenerator);

    assertNull(actualSourceFile);
  }
}