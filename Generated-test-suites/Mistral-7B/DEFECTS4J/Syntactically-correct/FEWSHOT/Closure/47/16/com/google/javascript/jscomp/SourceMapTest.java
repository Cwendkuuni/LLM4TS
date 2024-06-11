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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  private SourceMapGenerator generator;
  private SourceMap sourceMap;

  @Before
  public void setup() {
    SourceMapFormat format = SourceMapFormat.V1;
    generator = mock(SourceMapGenerator.class);
    SourceMapGeneratorFactory factory = mock(SourceMapGeneratorFactory.class);
    when(factory.getInstance(format)).thenReturn(generator);
    sourceMap = new SourceMap(factory.getInstance(format));
  }

  @Test
  public void testAddMapping() {
    Node testNode = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);

    String sourceFile = "test.js";
    String originalName = "originalName";

    sourceMap.addMapping(testNode, outputStartPosition, outputEndPosition);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertNotNull(fixedSourceFile);
    assertEquals(sourceFile, fixedSourceFile);

    sourceMap.addMapping(testNode, outputStartPosition, outputEndPosition);
    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertNotNull(fixedSourceFile);
    assertEquals(sourceFile, fixedSourceFile);
  }

  @Test
  public void testFixupSourceLocationWithPrefixMappings() {
    Node testNode = mock(Node.class);

    String sourceFile = "/test/prefix.js";
    String prefix = "/test/prefix/";

    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping(prefix, "test.js")
    );

    sourceMap.setPrefixMappings(prefixMappings);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertNotNull(fixedSourceFile);
    assertEquals("test.js", fixedSourceFile);
  }

  @Test
  public void testFixupSourceLocationWithoutPrefixMappings() {
    Node testNode = mock(Node.class);

    String sourceFile = "test.js";

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertNotNull(fixedSourceFile);
    assertEquals(sourceFile, fixedSourceFile);
  }

  @Test
  public void testFixupSourceLocationWithNullSourceFile() {
    Node testNode = mock(Node.class);

    sourceMap.fixupSourceLocation(null);
    assertNull(sourceMap.sourceLocationFixupCache.get(null));
  }

  @Test
  public void testValidate() {
    sourceMap.validate(true);
  }

  @Test(expected = IOException.class)
  public void testValidateWithIOException() {
    sourceMap.validate(false);
  }

  @Test
  public void testAppendTo() throws IOException {
    Appendable testOutput = mock(Appendable.class);
    String testName = "testName";

    sourceMap.appendTo(testOutput, testName);
  }

  @Test
  public void testReset() {
    sourceMap.reset();
    assertNull(sourceMap.generator);
    sourceMap.sourceLocationFixupCache.clear();
  }

  @Test
  public void testSetStartingPosition() {
    int offsetLine = 1;
    int offsetIndex = 1;

    sourceMap.setStartingPosition(offsetLine, offsetIndex);
  }

  @Test
  public void testSetWrapperPrefix() {
    String prefix = "testPrefix";

    sourceMap.setWrapperPrefix(prefix);
  }
}