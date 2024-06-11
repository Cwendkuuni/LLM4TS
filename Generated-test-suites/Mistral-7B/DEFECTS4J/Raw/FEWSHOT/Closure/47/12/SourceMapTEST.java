package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.rhino.NodeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class SourceMapTest {

  private SourceMapGenerator generator;
  private SourceMap sourceMap;
  private Node node;

  @Before
  public void setUp() {
    SourceMapFormat format = mock(SourceMapFormat.class);
    SourceMapGeneratorFactory generatorFactory = mock(SourceMapGeneratorFactory.class);
    when(generatorFactory.getInstance(SourceMapFormat.V1)).thenReturn(format);
    generator = new SourceMapGeneratorV1();
    sourceMap = new SourceMap(generatorFactory);
    node = mock(Node.class);
  }

  @Test
  public void testAddMapping() {
    String sourceFile = "src/TestSourceFile.js";
    String originalName = "testFunction";
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ArgumentCaptor<String[]> fileArgsCaptor = ArgumentCaptor.forClass(String[].class);
    ArgumentCaptor<String> originalNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<FilePosition> inputStartPositionCaptor = ArgumentCaptor.forClass(FilePosition.class);
    ArgumentCaptor<FilePosition> inputEndPositionCaptor = ArgumentCaptor.forClass(FilePosition.class);

    when(generator.addMapping(sourceFile, originalNameCaptor.capture(), fileArgsCaptor.capture(), inputStartPositionCaptor.capture(), inputEndPositionCaptor.capture()))
        .thenReturn(true);

    assertNotNull(sourceMap.generator);
    assertNotNull(generator);
    assertEquals(1, generator.getSize());
    assertEquals(sourceFile, generator.getSourceFile(0));
    assertEquals(originalName, generator.getOriginalName(0));
    assertEquals(outputStartPosition.getLine(), generator.getInputStartColumn(0));
    assertEquals(outputStartPosition.getCharacter(), generator.getInputStartLine(0));
    assertEquals(outputEndPosition.getLine(), generator.getInputEndColumn(0));
    assertEquals(outputEndPosition.getCharacter(), generator.getInputEndLine(0));
  }

  @Test
  public void testAddMappingWithDetailLevel() {
    String sourceFile = "src/TestSourceFile.js";
    String originalName = "testFunction";
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    DetailLevel detailLevel = DetailLevel.SYMBOLS;

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition, detailLevel);

    ArgumentCaptor<String[]> fileArgsCaptor = ArgumentCaptor.forClass(String[].class);
    ArgumentCaptor<String> originalNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<FilePosition> inputStartPositionCaptor = ArgumentCaptor.forClass(FilePosition.class);
    ArgumentCaptor<FilePosition> inputEndPositionCaptor = ArgumentCaptor.forClass(FilePosition.class);

    when(generator.addMapping(eq(sourceFile), originalNameCaptor.capture(), fileArgsCaptor.capture(), inputStartPositionCaptor.capture(), inputEndPositionCaptor.capture()))
        .thenReturn(true);

    assertNotNull(sourceMap.generator);
    assertNotNull(generator);
    assertEquals(1, generator.getSize());
    assertEquals(sourceFile, generator.getSourceFile(0));
    assertEquals(originalName, generator.getOriginalName(0));
    assertEquals(outputStartPosition.getLine(), generator.getInputStartColumn(0));
    assertEquals(outputStartPosition.getCharacter(), generator.getInputStartLine(0));
    assertEquals(outputEndPosition.getLine(), generator.getInputEndColumn(0));
    assertEquals(outputEndPosition.getCharacter(), generator.getInputEndLine(0));
  }

  @Test
  public void testFixupSourceLocation() {
    String sourceFile = "src/TestSourceFile.js";
    String fixedSourceFile = "test_src/TestSourceFile.js";

    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new SourceMap.LocationMapping("src/", "test_src/"));

    sourceMap.setPrefixMappings(prefixMappings);

    assertEquals(fixedSourceFile, sourceMap.fixupSourceLocation(sourceFile));
  }

  @Test
  public void testAddMappingWithNoSourceFile() {
    String originalName = "testFunction";
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ArgumentCaptor<String[]> fileArgsCaptor = ArgumentCaptor.forClass(String[].class);
    ArgumentCaptor<String> originalNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<FilePosition> inputStartPositionCaptor = ArgumentCaptor.forClass(FilePosition.class);
    ArgumentCaptor<FilePosition> inputEndPositionCaptor = ArgumentCaptor.forClass(FilePosition.class);

    when(generator.addMapping(sourceFileCaptor.capture(), originalNameCaptor.capture(), fileArgsCaptor.capture(), inputStartPositionCaptor.capture(), inputEndPositionCaptor.capture()))
        .thenReturn(true);

    assertNull(sourceFileCaptor.getValue());
  }

  @Test
  public void testAddMappingWithEmptySourceFile() {
    String originalName = "testFunction";
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ArgumentCaptor<String[]> fileArgsCaptor = ArgumentCaptor.forClass(String[].class);
    ArgumentCaptor<String> originalNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<FilePosition> inputStartPositionCaptor = ArgumentCaptor.forClass(FilePosition.class);
    ArgumentCaptor<FilePosition> inputEndPositionCaptor = ArgumentCaptor.forClass(FilePosition.class);

    when(generator.addMapping(eq(""), originalNameCaptor.capture(), fileArgsCaptor.capture(), inputStartPositionCaptor.capture(), inputEndPositionCaptor.capture()))
        .thenReturn(true);

    assertNull(originalNameCaptor.getValue());
  }

  @Test
  public void testAppendTo() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js");

    ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);

    when(generator.appendTo(out, nameCaptor.capture())).thenReturn(true);

    assertEquals("test.js", nameCaptor.getValue());
    assertEquals(out.toString(), sourceMap.toString());
  }

  @Test
  public void testReset() {
    ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);

    sourceMap.appendTo(new ByteArrayOutputStream(), "test.js");
    sourceMap.reset();

    when(generator.appendTo(nameCaptor.capture(), anyString())).thenReturn(true);

    assertNull(nameCaptor.getValue());
    assertEquals(0, sourceMap.generator.getSize());
  }

  @Test
  public void testSetStartingPosition() {
    int offsetLine = 1;
    int offsetIndex = 10;

    sourceMap.setStartingPosition(offsetLine, offsetIndex);

    ArgumentCaptor<Integer> lineCaptor = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> indexCaptor = ArgumentCaptor.forClass(Integer.class);

    when(generator.setStartingPosition(lineCaptor.capture(), indexCaptor.capture()))
        .thenReturn(true);

    assertEquals(offsetLine, lineCaptor.getValue());
    assertEquals(offsetIndex, indexCaptor.getValue());
  }

  @Test
  public void testSetWrapperPrefix() {
    String prefix = "test_";

    sourceMap.setWrapperPrefix(prefix);

    ArgumentCaptor<String> prefixCaptor = ArgumentCaptor.forClass(String.class);

    when(generator.setWrapperPrefix(prefixCaptor.capture()))
        .thenReturn(true);

    assertEquals(prefix, prefixCaptor.getValue());
  }

  @Test
  public void testValidate() {
    sourceMap.validate(true);

    when(generator.validate(anyBoolean())).thenReturn(true);

    assertTrue(sourceMap.isValid());
  }

  @Test
  public void testValidateInvalid() {
    sourceMap.validate(true);

    when(generator.validate(anyBoolean())).thenReturn(false);

    assertFalse(sourceMap.isValid());
  }
}