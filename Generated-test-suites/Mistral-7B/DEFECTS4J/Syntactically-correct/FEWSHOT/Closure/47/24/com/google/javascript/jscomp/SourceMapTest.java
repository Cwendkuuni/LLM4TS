package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
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
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.jscomp.SourceMap.LocationMapping;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  private SourceMapGenerator generator;
  private SourceMap sourceMap;

  @Before
  public void setUp() throws IOException {
    SourceMapGeneratorFactory factory = mock(SourceMapGeneratorFactory.class);
    when(factory.getInstance(SourceMapFormat.V1)).thenReturn(new SourceMapGeneratorV1());
    generator = factory.getInstance(SourceMapFormat.DEFAULT);
    sourceMap = new SourceMap(generator);
  }

  @Test
  public void testAddMapping() {
    Node node = mock(Node.class);
    String sourceFile = "test.js";
    String originalName = "testFunction";
    int lineno = 1;
    int charno = 5;

    sourceMap.addMapping(node, new FilePosition(lineno, charno),
        new FilePosition(lineno, charno));
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertThat(fixedSourceFile).isEqualTo(sourceFile);

    Node nodeWithSourceFile = new Node(Node.SCRIPT, null);
    nodeWithSourceFile.setSourceFileName(sourceFile);
    sourceMap.addMapping(nodeWithSourceFile, new FilePosition(lineno, charno),
        new FilePosition(lineno, charno));
    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertThat(fixedSourceFile).isEqualTo(sourceFile);

    sourceMap.setPrefixMappings(Lists.newArrayList(new LocationMapping("ts", "js")));
    sourceMap.addMapping(nodeWithSourceFile, new FilePosition(lineno, charno),
        new FilePosition(lineno, charno));
    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertThat(fixedSourceFile).isEqualTo("js" + sourceFile.substring(3));
  }

  @Test
  public void testAddMappingNullSourceFile() {
    Node node = mock(Node.class);
    int lineno = 1;
    int charno = 5;

    sourceMap.addMapping(node, null, new FilePosition(lineno, charno),
        new FilePosition(lineno, charno));
    assertNull(sourceMap.fixupSourceLocation(null));
  }

  @Test
  public void testAddMappingNegativeLineno() {
    Node node = mock(Node.class);
    String sourceFile = "test.js";
    int lineno = -1;
    int charno = 5;

    sourceMap.addMapping(node, new FilePosition(lineno, charno),
        new FilePosition(lineno, charno));
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertThat(fixedSourceFile).isEqualTo(sourceFile);
  }

  @Test
  public void testAppendTo() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js");
    String sourceMapContent = out.toString();
    // Verify that the source map is empty
    // The output format may change, so we cannot directly check the content
    // instead, we will check the length of the content
    // The content length should be zero for an empty source map
    assertThat(sourceMapContent.length()).isZero();
  }

  @Test
  public void testReset() {
    generator.addMapping("test.js", "testFunction",
        new FilePosition(1, 1), new FilePosition(1, 1));
    String fixedSourceFile = sourceMap.fixupSourceLocation("test.js");
    assertThat(fixedSourceFile).isEqualTo("test.js");
    sourceMap.reset();
    fixedSourceFile = sourceMap.fixupSourceLocation("test.js");
    assertThat(fixedSourceFile).isNull();
  }
}