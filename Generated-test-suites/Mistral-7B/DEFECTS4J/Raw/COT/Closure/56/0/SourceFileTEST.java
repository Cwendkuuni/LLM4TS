package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;

public class SourceFileTest {

  private SourceFile sourceFile;
  private Generator mockGenerator;

  @Before
  public void setUp() {
    mockGenerator = mock(Generator.class);
    when(mockGenerator.getCode()).thenReturn("mockCode");
    sourceFile = new Generated("testFile.js", mockGenerator);
  }

  @Test
  public void testGetFileName() {
    assertEquals("testFile.js", sourceFile.getName());
  }

  @Test
  public void testIsExtern() {
    assertFalse(sourceFile.isExtern());
  }

  @Test
  public void testGetCode() throws IOException {
    assertEquals("mockCode", sourceFile.getCode());
  }

  @Test
  public void testGetCodeReader() throws IOException {
    Reader reader = sourceFile.getCodeReader();
    assertEquals("mockCode", CharStreams.toString(reader));
  }

  @Test
  public void testGetLine() {
    assertEquals("mockCode", sourceFile.getCode());
    assertNull(sourceFile.getLine(0));
    assertNull(sourceFile.getLine( Integer.MAX_VALUE));
    assertEquals("mockCode", sourceFile.getLine(1));
  }

  @Test
  public void testGetRegion() {
    assertEquals("mockCode", sourceFile.getCode());
    assertNull(sourceFile.getRegion(0));
    assertNull(sourceFile.getRegion( Integer.MAX_VALUE));

    int lineNumber = 1;
    String expectedRegion = "mockCode";
    Region region = sourceFile.getRegion(lineNumber);
    assertEquals(lineNumber, region.getStartLineNumber());
    assertEquals(lineNumber, region.getEndLineNumber());
    assertEquals(expectedRegion, region.getSource());
  }

  @Test
  public void testFromFile() throws IOException {
    String filePath = "/path/to/testFile.js";
    SourceFile sourceFile = SourceFile.fromFile(filePath, StandardCharsets.UTF_8);
    assertEquals(filePath, sourceFile.getName());

    String code = Files.toString(new File(filePath), StandardCharsets.UTF_8);
    assertEquals(code, sourceFile.getCode());
  }

  @Test
  public void testFromCode() throws IOException {
    String fileName = "testFile.js";
    String code = "mockCode";
    String originalPath = "/path/to/originalPath.js";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code, originalPath);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
    assertEquals(originalPath, sourceFile.getOriginalPath());
  }

  @Test
  public void testFromInputStream() throws IOException {
    String fileName = "testFile.js";
    String inputCode = "mockCode";
    InputStream inputStream = new FileInputStream("/path/to/testFile.js");
    String charsetName = StandardCharsets.UTF_8.name();

    SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream, charsetName);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(inputCode, sourceFile.getCode());
  }

  @Test
  public void testFromReader() throws IOException {
    String fileName = "testFile.js";
    String readerCode = "mockCode";
    Reader reader = new StringReader(readerCode);

    SourceFile sourceFile = SourceFile.fromReader(fileName, reader);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(readerCode, sourceFile.getCode());
  }

  @Test
  public void testFromGenerator() throws IOException {
    String fileName = "testFile.js";
    String generatorCode = "mockCode";

    SourceFile sourceFile = SourceFile.fromGenerator(fileName, mockGenerator);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(generatorCode, sourceFile.getCode());
  }
}