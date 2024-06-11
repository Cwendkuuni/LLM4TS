package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test class for {@link SourceFile} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class SourceFileTest {

  @Mock
  private Generator generatorMock;

  @InjectMocks
  private SourceFile sourceFile;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSourceFileConstructor() {
    sourceFile = new SourceFile("testFileName");
    assertEquals("testFileName", sourceFile.getName());
  }

  @Test
  public void testGetLineOffset() throws IOException {
    String code = "line1\nline2\nline3";
    when(sourceFile.getCode()).thenReturn(code);

    assertEquals(1, sourceFile.getLineOffset(1));
    assertEquals(3, sourceFile.getLineOffset(3));
    assertNull(sourceFile.getLineOffset(-1));
    assertNull(sourceFile.getLineOffset(4));
  }

  @Test
  public void testGetNumLines() throws IOException {
    String code = "line1\nline2\nline3";
    when(sourceFile.getCode()).thenReturn(code);

    assertEquals(3, sourceFile.getNumLines());
  }

  @Test
  public void testGetCode() throws IOException {
    String code = "testCode";
    when(generatorMock.getCode()).thenReturn(code);
    sourceFile = new Generated("testFileName", generatorMock);

    assertEquals(code, sourceFile.getCode());
  }

  @Test
  public void testGetCodeReader() throws IOException {
    String code = "testCode";
    StringReader stringReader = mock(StringReader.class);
    when(stringReader.readLine()).thenAnswer(invocation -> code);
    when(sourceFile.getCodeReader()).thenReturn(stringReader);

    String result = sourceFile.getCode().trim();
    assertEquals(code.trim(), result);
  }

  @Test
  public void testSetCode() {
    sourceFile = new SourceFile("testFileName");
    sourceFile.setCode("testCode");

    assertEquals("testCode", sourceFile.getCode());
  }

  @Test
  public void testGetOriginalPath() {
    sourceFile = new SourceFile("testFileName");
    sourceFile.setOriginalPath("testOriginalPath");

    assertEquals("testOriginalPath", sourceFile.getOriginalPath());
  }

  @Test
  public void testClearCachedSource() {
    sourceFile = new Preloaded("testFileName", "testCode");
    sourceFile.clearCachedSource();

    assertNull(sourceFile.getCode());
  }

  @Test
  public void testHasSourceInMemory() {
    sourceFile = new Preloaded("testFileName", "testCode");

    assertTrue(sourceFile.hasSourceInMemory());

    sourceFile.clearCachedSource();

    assertFalse(sourceFile.hasSourceInMemory());
  }

  @Test
  public void testGetName() {
    sourceFile = new SourceFile("testFileName");

    assertEquals("testFileName", sourceFile.getName());
  }

  @Test
  public void testIsExtern() {
    sourceFile = new SourceFile("testFileName");

    assertFalse(sourceFile.isExtern());

    sourceFile.setIsExtern(true);

    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testGetLine() throws IOException {
    String code = "line1\nline2\nline3\nline4";
    when(sourceFile.getCode()).thenReturn(code);

    assertEquals("line2", sourceFile.getLine(2));

    assertNull(sourceFile.getLine(0));
    assertNull(sourceFile.getLine(5));
  }

  @Test
  public void testGetRegion() throws IOException {
    String code = "line1\nline2\nline3";
    when(sourceFile.getCode()).thenReturn(code);

    Region region = sourceFile.getRegion(3);
    assertEquals(1, region.getStartLineNumber());
    assertEquals(3, region.getEndLineNumber());
    assertEquals("line1\nline2\nline3", region.getSourceCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetWithInvalidLineNumber() throws IOException {
    sourceFile = new SourceFile("testFileName");

    sourceFile.getLineOffset(-1);
  }
}

class SimpleRegion {
  private final int startLineNumber;
  private final int endLineNumber;
  private final String sourceCode;

  SimpleRegion(int startLineNumber, int endLineNumber, String sourceCode) {
    this.startLineNumber = startLineNumber;
    this.endLineNumber = endLineNumber;
    this.sourceCode = sourceCode;
  }

  public int getStartLineNumber() {
    return startLineNumber;
  }

  public int getEndLineNumber() {
    return endLineNumber;
  }

  public String getSourceCode() {
    return sourceCode;
  }
}