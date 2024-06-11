package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMap;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.rhino.Node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Test cases for SourceMap class.
 */
public class SourceMapTest {

  @Test
  public void testInstanceCreation() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
    assertNotNull(sourceMap);
  }

  @Test
  public void testAddMapping() throws IOException {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
    Node node = new Node();
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 2);
    String sourceFile = "test.js";
    String originalName = "testFunction";

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals(sourceFile, fixedSourceFile);

    sourceMap.appendTo(new ByteArrayOutputStream(), "test.jsmap");
    sourceMap.reset();
  }

  @Test
  public void testFixupSourceLocation() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();

    // No prefixMappings set
    String sourceFile = "test.js";
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals(sourceFile, fixedSourceFile);

    // With prefixMappings set
    List<LocationMapping> sourceMapLocationMappings = Lists.newArrayList(
        new LocationMapping("test", "fix_test.js"),
        new LocationMapping("lib/", "src/")
    );
    sourceMap.setPrefixMappings(sourceMapLocationMappings);
    sourceFile = "lib/test.js";
    fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("src/test.js", fixedSourceFile);
  }

  @Test
  public void testGetInstance() {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();
    assertNotNull(sourceMapV1);
    SourceMap sourceMapDefault = SourceMap.Format.DEFAULT.getInstance();
    assertNotNull(sourceMapDefault);
    SourceMap sourceMapV2 = SourceMap.Format.V2.getInstance();
    assertNotNull(sourceMapV2);
    SourceMap sourceMapV3 = SourceMap.Format.V3.getInstance();
    assertNotNull(sourceMapV3);
  }

  @Test
  public void testSetPrefixMappings() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();

    List<LocationMapping> sourceMapLocationMappings = Lists.newArrayList(
        new LocationMapping("test", "fix_test.js"),
        new LocationMapping("lib/", "src/")
    );
    sourceMap.setPrefixMappings(sourceMapLocationMappings);
    List<LocationMapping> prefixMappings = sourceMap.getPrefixMappings();
    assertEquals(2, prefixMappings.size());
    assertEquals(new LocationMapping("test", "fix_test.js"), prefixMappings.get(0));
    assertEquals(new LocationMapping("lib/", "src/"), prefixMappings.get(1));
  }

  @Test
  public void testApplyDetailLevelPredicate() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();

    DetailLevel detailLevel = DetailLevel.SYMBOLS;
    Node node1 = new Node();
    Node node2 = new Node();
    node1.setType(Node.CALL_EXPR);
    node2.setType(Node.FUNCTION);

    boolean result1 = detailLevel.apply(node1);
    assertTrue(result1);

    boolean result2 = detailLevel.apply(node2);
    assertFalse(result2);

    detailLevel = DetailLevel.ALL;
    result1 = detailLevel.apply(node1);
    assertTrue(result1);

    result2 = detailLevel.apply(node2);
    assertTrue(result2);
  }
}