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

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  private SourceMapGenerator generatorV1;
  private SourceMapGenerator generatorV2;
  private SourceMap sourceMap;

  @Before
  public void setup() {
    generatorV1 = mock(SourceMapGeneratorV1.class);
    generatorV2 = mock(SourceMapGeneratorV2.class);

    when(generatorV1.getInstance()).thenReturn(generatorV1);
    when(generatorV2.getInstance()).thenReturn(generatorV2);

    sourceMap = new SourceMap(
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
  }

  @Test
  public void testInstance() {
    assertEquals(Format.V1, sourceMap.getFormat());
  }

  @Test
  public void testNewInstanceV2() {
    SourceMap sourceMapV2 = new SourceMap(
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.V2));
    assertEquals(Format.V2, sourceMapV2.getFormat());
  }

  @Test
  public void testAddMapping() {
    String sourceFile = "test.js";
    Node node = new Node();
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(2, 2);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // If the node does not have an associated source file or
    // its line number is -1, then the node does not have sufficient
    // information for a mapping to be useful.
    Node nodeWithoutSourceFile = new Node();
    assertNull(sourceMap.addMapping(nodeWithoutSourceFile, outputStartPosition, outputEndPosition));

    node.setSourceFileName("test.js");
    FilePosition invalidPosition = new FilePosition(-1, -1);
    assertNull(sourceMap.addMapping(node, invalidPosition, outputEndPosition));
  }

  @Test
  public void testFixupSourceLocation() {
    String sourceFile = "/src/main/java/com/google/javascript/jscomp/SourceMapTest.java";
    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping("/", ""),
        new LocationMapping("/src/main/java/", "src/")
    );

    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
    sourceMap.setPrefixMappings(prefixMappings);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("src/main/java/com/google/javascript/jscomp/SourceMapTest.java", fixedSourceFile);
  }

  @Test
  public void testAppendTo() throws IOException {
    StringWriter out = new StringWriter();
    sourceMap.appendTo(out, "test.js");

    assertNotNull(out.toString());
  }

  @Test
  public void testReset() {
    sourceMap.reset();
    assertNull(sourceMap.generator);
    assertNull(sourceMap.prefixMappings);
    assertNull(sourceMap.sourceLocationFixupCache);
  }

  @Test
  public void testValidate() {
    sourceMap.validate(true);
  }
}