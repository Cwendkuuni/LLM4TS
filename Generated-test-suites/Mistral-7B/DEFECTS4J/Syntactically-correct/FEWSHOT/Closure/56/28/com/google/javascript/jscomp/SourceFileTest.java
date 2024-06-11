package com.google.javascript.jscomp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class SourceFileTest {

  private static final String fileContent = "line1\nline2\nline3";
  private static final String fileName = "testFile.js";
  private static final Charset charset = Charsets.UTF_8;
  private static final int SOURCE_EXCERPT_REGION_LENGTH = 5;

  private File testFile;
  private SourceFile sourceFile;
  private Generator mockGenerator;

  @Before
  public void setup() throws IOException {
    testFile = new File(System.getProperty("java.io.tmpdir"), fileName);
    Files.write(testFile, fileContent.getBytes(charset));

    mockGenerator = mock(Generator.class);
    when(mockGenerator.getCode()).thenReturn(fileContent);

    sourceFile = new SourceFile(fileName);
  }

  @Test
  public void testGetName() {
    assertEquals(fileName, sourceFile.getName());
  }

  @Test
  public void testIsExtern() {
    assertFalse(sourceFile.isExtern());
  }

  @Test
  public void testSetIsExtern() {
    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testGetCode() throws IOException {
    String code = sourceFile.getCode();
    assertEquals(fileContent, code);
  }

  @Test
  public void testGetCodeReader() throws IOException {
    Reader reader = sourceFile.getCodeReader();
    assertSame(new StringReader(sourceFile.getCode()), reader);
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String originalCode = sourceFile.getCode();
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
    assertEquals(originalCode, Files.toString(testFile, charset));
  }

  @Test
  public void testHasSourceInMemory() throws IOException {
    assertTrue(sourceFile.hasSourceInMemory());

    Files.delete(testFile);
    assertFalse(sourceFile.hasSourceInMemory());
  }

  @Test
  public void testGetLine() {
    assertEquals("line1", sourceFile.getLine(1));
    assertEquals("line2", sourceFile.getLine(2));
    assertEquals("line3", sourceFile.getLine(3));
    assertNull(sourceFile.getLine(4));
    assertNull(sourceFile.getLine(-1));
  }

  @Test
  public void testGetRegion() {
    String expectedRegion = "line1\nline2\nline3";

    Region region = sourceFile.getRegion(3);
    assertEquals(3, region.getStartLineNumber());
    assertEquals(3, region.getEndLineNumber());
    assertEquals(expectedRegion, region.getText());
  }

  @Test
  public void testFromFile() throws IOException {
    SourceFile fileSource = SourceFile.fromFile(fileName, charset);
    assertEquals(fileName, fileSource.getName());
  }

  @Test
  public void testFromCode() throws IOException {
    SourceFile codeSource = SourceFile.fromCode(fileName, fileContent);
    assertEquals(fileName, codeSource.getName());
  }

  @Test
  public void testFromGenerator() throws IOException {
    SourceFile generatorSource = SourceFile.fromGenerator(fileName, mockGenerator);
    assertEquals(fileName, generatorSource.getName());
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    String code = sourceFile.getCode();
    int lineCount = code.split("\n").length;

    int[] lineOffsets = sourceFile.getLineOffsets();
    assertArrayEquals(new int[] {0, 1, 1 + fileContent.length()}, lineOffsets);
  }

  @Test
  public void testFindLineOffsetsWithEmptyFile() throws IOException {
    Files.write(testFile, new byte[0][]);
    int[] lineOffsets = sourceFile.getLineOffsets();
    assertArrayEquals(new int[] {0}, lineOffsets);
  }

  @Test
  public void testGetNumLines() throws IOException {
    int numLines = sourceFile.getNumLines();
    assertEquals(3, numLines);
  }

  @Test
  public void testToString() {
    assertEquals(fileName, sourceFile.toString());
  }

  @Test
  public void testFromFileWithReader() throws IOException {
    Reader reader = new FileReader(testFile);
    SourceFile fileSourceWithReader = SourceFile.fromReader(fileName, reader);
    assertEquals(fileName, fileSourceWithReader.getName());
  }

  @Test
  public void testFromFileWithCharset() throws IOException {
    SourceFile fileSourceWithCharset = SourceFile.fromFile(fileName, charset);
    assertEquals(fileName, fileSourceWithCharset.getName());
  }

  @Test
  public void testFromInputStream() throws IOException {
    InputStream inputStream = new FileInputStream(testFile);
    SourceFile inputStreamSource = SourceFile.fromInputStream(fileName, inputStream);
    assertEquals(fileName, inputFileSource.getName());
  }

  @Test
  public void testFromInputStreamWithCharset() throws IOException {
    InputStream inputStream = new FileInputStream(testFile);
    SourceFile inputStreamSourceWithCharset =
        SourceFile.fromInputStream(fileName, inputStream, charset);
    assertEquals(fileName, inputStreamSourceWithCharset.getName());
  }

  @Test
  public void testFromReaderWithCharset() throws IOException {
    Reader reader = new InputStreamReader(new FileInputStream(testFile), charset);
    SourceFile readerSource = SourceFile.fromReader(fileName, reader);
    assertEquals(fileName, readerSource.getName());
  }
}

class Region {
  private final int startLineNumber;
  private final int endLineNumber;
  private final String text;

  public Region(int startLineNumber, int endLineNumber, String text) {
    this.startLineNumber = startLineNumber;
    this.endLineNumber = endLineNumber;
    this.text = text;
  }

  public int getStartLineNumber() {
    return startLineNumber;
  }

  public int getEndLineNumber() {
    return endLineNumber;
  }

  public String getText() {
    return text;
  }
}