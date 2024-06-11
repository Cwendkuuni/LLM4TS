package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  @Test
  public void testAddMapping() throws IOException {
    SourceMapGenerator generatorMock = mock(SourceMapGenerator.class);
    SourceMap sourceMap = new SourceMap(generatorMock);

    Node node = new Node();
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);

    String sourceFile = "test.js";
    String originalName = "testFunction";

    when(generatorMock.addMapping(eq(sourceFile), eq(originalName), any(FilePosition.class), any(FilePosition.class)))
        .thenReturn(null);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    assertNull(sourceMap.sourceLocationFixupCache.get(sourceFile));
  }

  @Test
  public void testFixupSourceLocation() {
    SourceMap sourceMap = new SourceMap(null);

    String sourceFile = "test.js";
    String fixedSourceFile = "test_fixed.js";

    sourceMap.prefixMappings = Lists.newArrayList(new SourceMap.LocationMapping("test.", "test_fixed."));

    String result = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals(fixedSourceFile, result);
  }

  @Test
  public void testFixupSourceLocationNoMapping() {
    SourceMap sourceMap = new SourceMap(null);

    String sourceFile = "test.js";

    String result = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals(sourceFile, result);
  }

  @Test
  public void testAppendTo() throws IOException {
    SourceMapGenerator generatorMock = mock(SourceMapGenerator.class);
    SourceMap sourceMap = new SourceMap(generatorMock);

    String name = "test.js.map";

    Appendable out = mock(Appendable.class);

    when(generatorMock.appendTo(eq(out), eq("sourceFile: \"test.js\";")))
        .thenReturn(out);

    sourceMap.appendTo(out, name);

    verify(generatorMock).appendTo(eq(out), eq("sourceFile: \"test.js\";"));
  }

  @Test
  public void testReset() {
    SourceMap sourceMap = new SourceMap(null);

    sourceMap.generator = mock(SourceMapGenerator.class);
    when(sourceMap.generator.reset()).thenReturn(null);

    sourceMap.reset();

    verify(sourceMap.generator).reset();
  }

  @Test
  public void testValidate() {
    SourceMap sourceMap = new SourceMap(null);

    sourceMap.generator = mock(SourceMapGenerator.class);
    when(sourceMap.generator.validate(eq(true))).thenReturn(null);

    sourceMap.validate(true);

    verify(sourceMap.generator).validate(eq(true));
  }

  @Test
  public void testSetStartingPosition() {
    SourceMap sourceMap = new SourceMap(null);

    sourceMap.generator = mock(SourceMapGenerator.class);
    when(sourceMap.generator.setStartingPosition(anyInt(), anyInt())).thenReturn(null);

    sourceMap.setStartingPosition(1, 1);

    verify(sourceMap.generator).setStartingPosition(anyInt(), anyInt());
  }

  @Test
  public void testSetWrapperPrefix() {
    SourceMap sourceMap = new SourceMap(null);

    sourceMap.generator = mock(SourceMapGenerator.class);
    when(sourceMap.generator.setWrapperPrefix(eq("test"))).thenReturn(null);

    sourceMap.setWrapperPrefix("test");

    verify(sourceMap.generator).setWrapperPrefix(eq("test"));
  }

  @Test
  public void testSetPrefixMappings() {
    SourceMap sourceMap = new SourceMap(null);

    List<SourceMap.LocationMapping> sourceMapLocationMappings = Lists.newArrayList(
        new SourceMap.LocationMapping("test.", "test_fixed."),
        new SourceMap.LocationMapping("src/test.", "src/test_fixed."));

    sourceMap.setPrefixMappings(sourceMapLocationMappings);

    assertEquals(sourceMapLocationMappings, sourceMap.prefixMappings);
  }
}