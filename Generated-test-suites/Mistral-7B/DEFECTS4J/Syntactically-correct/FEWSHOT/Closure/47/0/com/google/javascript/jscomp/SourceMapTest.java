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
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  private SourceMapGenerator generator;
  private SourceMap sourceMap;

  @Before
  public void setUp() {
    SourceMapGeneratorFactory factory = mock(SourceMapGeneratorFactory.class);
    generator = mock(SourceMapGenerator.class);

    when(factory.getInstance(SourceMapFormat.V1)).thenReturn(new SourceMapGeneratorV1());
    when(factory.getInstance(SourceMapFormat.DEFAULT)).thenReturn(new SourceMapGeneratorV2());
    when(factory.getInstance(SourceMapFormat.V2)).thenReturn(new SourceMapGeneratorV2());
    when(factory.getInstance(SourceMapFormat.V3)).thenReturn(new SourceMapGeneratorV3()); // Assuming V3 is a valid format

    sourceMap = new SourceMap(factory);
  }

  @Test
  public void testAddMapping() {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(2, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Verify that the mapping was added to the generator
    SourceMapGenerator generatorMock = mock(SourceMapGenerator.class);
    when(generator.addMapping(anyString(), anyString(), any(FilePosition.class), any(FilePosition.class))).then(invocation -> {
      generatorMock.addMapping(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2), invocation.getArgument(3));
      return null;
    });

    sourceMap = new SourceMap(generatorMock);

    String sourceFile = "test.js";
    String originalName = "TestClass";
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Verify that the generator received the correct arguments
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    generatorMock.addMapping(
        equals(fixedSourceFile), equals(originalName),
        eq(outputStartPosition), eq(outputEndPosition)
    );
  }

  @Test
  public void testFixupSourceLocation() {
    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping("test/", "src/")
    );

    sourceMap.setPrefixMappings(prefixMappings);

    String sourceFile = "test/test.js";

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

    assertNotNull(fixedSourceFile);
    assertEquals("src/test.js", fixedSourceFile);
  }

  @Test
  public void testFixupSourceLocationNoMappingFound() {
    String sourceFile = "test.js";

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

    assertNull(fixedSourceFile);
  }

  @Test
  public void testValidate() {
    sourceMap.validate(true);

    // Verify that the generator received the validate call
    SourceMapGenerator generatorMock = mock(SourceMapGenerator.class);
    when(generator.validate(anyBoolean())).thenReturn(null);

    sourceMap = new SourceMap(generatorMock);

    sourceMap.validate(true);

    generatorMock.validate(eq(true));
  }

  @Test(expected = IOException.class)
  public void testAppendToThrowsIOException() throws IOException {
    Appendable out = mock(Appendable.class);
    String name = "test.js.map";

    sourceMap.appendTo(out, name);

    SourceMapGenerator generatorMock = mock(SourceMapGenerator.class);
    when(generator.appendTo(any(Appendable.class), anyString())).thenThrow(new IOException());

    sourceMap = new SourceMap(generatorMock);

    sourceMap.appendTo(out, name);
  }

  @Test
  public void testReset() {
    sourceMap.reset();

    // Verify that the generator received the reset call
    SourceMapGenerator generatorMock = mock(SourceMapGenerator.class);
    when(generator.reset()).thenReturn(null);

    sourceMap = new SourceMap(generatorMock);

    sourceMap.reset();

    generatorMock.reset();
  }

  @Test
  public void testSetStartingPosition() {
    sourceMap.setStartingPosition(1, 1);

    // Verify that the generator received the setStartingPosition call
    SourceMapGenerator generatorMock = mock(SourceMapGenerator.class);
    when(generator.setStartingPosition(anyInt(), anyInt())).thenReturn(null);

    sourceMap = new SourceMap(generatorMock);

    sourceMap.setStartingPosition(1, 1);

    generatorMock.setStartingPosition(eq(1), eq(1));
  }

  @Test
  public void testSetWrapperPrefix() {
    sourceMap.setWrapperPrefix("test_");

    // Verify that the generator received the setWrapperPrefix call
    SourceMapGenerator generatorMock = mock(SourceMapGenerator.class);
    when(generator.setWrapperPrefix(anyString())).thenReturn(null);

    sourceMap = new SourceMap(generatorMock);

    sourceMap.setWrapperPrefix("test_");

    generatorMock.setWrapperPrefix(eq("test_"));
  }
}