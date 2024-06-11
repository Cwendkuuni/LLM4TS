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
import com.google.javascript.rhino.Node;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  private static final String SOURCE_FILE = "TestSourceFile.java";
  private static final String PREFIX = "test_prefix_";
  private static final String REPLACEMENT = "replaced_prefix_";

  @Test
  public void testAddMapping() {
    SourceMap sourceMap = SourceMap.getDefault().getInstance();
    Node node = mock(Node.class);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(2, 2);

    when(node.getSourceFileName()).thenReturn(SOURCE_FILE);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Verify that the mapping was added to the generator
    SourceMapGenerator sourceMapGenerator = sourceMap.generator;
    assertThat(sourceMapGenerator).isNotNull();
    List<Map<String, Object>> sourceEntries = sourceMapGenerator.getSourceEntries();
    Map<String, Object> sourceEntry = sourceEntries.get(0);
    Map<String, String> sources = (Map<String, String>) sourceEntry.get("sources");
    Map<String, Object> source = sources.get(0);
    List<Map<String, Object>> sourcesContent = (List<Map<String, Object>>) source.get("content");
    Map<String, Object> content = sourcesContent.get(0);
    List<Map<String, Object>> mappings = (List<Map<String, Object>>) content.get("mappings");
    Map<String, Object> mapping = mappings.get(0);
    String sourceFile = (String) mapping.get("source");
    List<Map<String, Object>> sourceMapping = (List<Map<String, Object>>) mapping.get("mappings");
    Map<String, Object> sourceMappingEntry = sourceMapping.get(0);
    int sourceLine = ((Long) sourceMappingEntry.get("line")).intValue();
    int sourceColumn = ((Long) sourceMappingEntry.get("column")).intValue();

    // Verify that the mapping was added with the correct source file and position
    assertThat(sourceFile).isEqualTo(SOURCE_FILE);
    assertThat(sourceLine).isEqualTo(1);
    assertThat(sourceColumn).isEqualTo(1);
  }

  @Test
  public void testFixupSourceLocation() {
    SourceMap sourceMap = SourceMap.getDefault().getInstance();

    String sourceFile = SOURCE_FILE;
    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    // Verify that the source file was not modified if no prefix mappings exist
    assertThat(fixed).isEqualTo(sourceFile);

    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping(PREFIX, REPLACEMENT));
    sourceMap.setPrefixMappings(prefixMappings);

    fixed = sourceMap.fixupSourceLocation(sourceFile);

    // Verify that the source file was modified with the first prefix mapping
    assertThat(fixed).isEqualTo(REPLACEMENT + sourceFile.substring(PREFIX.length()));
  }

  @Test
  public void testInstanceCreation() {
    SourceMap sourceMapV1 = SourceMap.V1.getInstance();
    SourceMap sourceMapDefault = SourceMap.DEFAULT.getInstance();
    SourceMap sourceMapV2 = SourceMap.V2.getInstance();
    SourceMap sourceMapV3 = SourceMap.V3.getInstance();

    // Verify that each instance was created with the correct source map format
    assertThat(sourceMapV1.generator.getFormat()).isEqualTo(SourceMapFormat.V1);
    assertThat(sourceMapDefault.generator.getFormat()).isEqualTo(SourceMapFormat.DEFAULT);
    assertThat(sourceMapV2.generator.getFormat()).isEqualTo(SourceMapFormat.V2);
    assertThat(sourceMapV3.generator.getFormat()).isEqualTo(SourceMapFormat.V3);
  }

  @Test
  public void testSetPrefixMappingsNull() {
    SourceMap sourceMap = SourceMap.getDefault().getInstance();
    sourceMap.setPrefixMappings(null);
  }

  @Test(expected = NullPointerException.class)
  public void testSetPrefixMappingsEmptyList() {
    SourceMap sourceMap = SourceMap.getDefault().getInstance();   
    sourceMap.setPrefixMappings(Collections.emptyList());
  }

  @Test
  public void testGetMappingsForAllNodes() {
    SourceMap sourceMap = SourceMap.getDefault().getInstance();

    Node node1 = mock(Node.class);
    Node node2 = mock(Node.class);
    when(node1.getSourceFileName()).thenReturn(SOURCE_FILE);
    when(node2.getSourceFileName()).thenReturn(SOURCE_FILE);

    FilePosition outputStartPosition1 = new FilePosition(1, 1);
    FilePosition outputEndPosition1 = new FilePosition(2, 2);
    FilePosition outputStartPosition2 = new FilePosition(3, 3);
    FilePosition outputEndPosition2 = new FilePosition(4, 4);

    sourceMap.addMapping(node1, outputStartPosition1, outputEndPosition1);
    sourceMap.addMapping(node2, outputStartPosition2, outputEndPosition2);

    // Verify that all nodes with non-null source files have mappings
    List<Map<String, Object>> mappings = sourceMap.generator.getSourceEntries().get(0).get("sources").get(0).get("mappings");
    for (Map<String, Object> mapping : mappings) {
      String sourceFile = (String) mapping.get("source");
      List<Map<String, Object>> sourceMapping = (List<Map<String, Object>>) mapping.get("mappings");
      for (Map<String, Object> sourceMappingEntry : sourceMapping) {
        Node node = (Node) sourceMappingEntry.get("original");
        assertNull(node); // original nodes should not be stored in the source map
        int sourceLine = ((Long) sourceMappingEntry.get("line")).intValue();
        int sourceColumn = ((Long) sourceMappingEntry.get("column")).intValue();
        String nodeSourceFileName = node.getSourceFileName();
        assertThat(nodeSourceFileName).isNotNull();
        assertThat(nodeSourceFileName).isEqualTo(sourceFile);
      }
    }
  }
}