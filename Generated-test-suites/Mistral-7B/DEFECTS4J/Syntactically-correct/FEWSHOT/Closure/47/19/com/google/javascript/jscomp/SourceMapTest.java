package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Test cases for the SourceMap class.
 */
public class SourceMapTest {

  private SourceMapGenerator generatorV1;
  private SourceMapGenerator generatorV2;
  private SourceMap sourceMapV1;
  private SourceMap sourceMapV2;

  @Before
  public void setup() {
    generatorV1 = mock(SourceMapGeneratorV1.class);
    generatorV2 = mock(SourceMapGeneratorV2.class);
    sourceMapV1 = new SourceMap(SourceMap.Format.V1.getInstance());
    sourceMapV2 = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
  }

  @Test
  public void testAddMapping() {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);

    String sourceFile = "test.js";
    sourceFile = sourceMapV1.fixupSourceLocation(sourceFile);
    assertTrue(sourceFile.equals("test.js"));

    sourceMapV1.addMapping(node, outputStartPosition, outputEndPosition);

    String fixedSourceFile = sourceMapV1.fixupSourceLocation(sourceFile);
    assertTrue(fixedSourceFile.equals("test.js"));

    sourceFile = "test/subdir/test.js";
    sourceFile = sourceMapV1.fixupSourceLocation(sourceFile);
    assertTrue(sourceFile.equals("test/subdir/test.js"));

    sourceMapV2.setPrefixMappings(Lists.newArrayList(
        new SourceMap.LocationMapping("test", "test_out")
    ));

    sourceFile = "test.js";
    sourceFile = sourceMapV2.fixupSourceLocation(sourceFile);
    assertTrue(sourceFile.equals("test_out/test.js"));

    sourceMapV2.addMapping(node, outputStartPosition, outputEndPosition);

    String fixedSourceFile2 = sourceMapV2.fixupSourceLocation(sourceFile);
    assertTrue(fixedSourceFile2.equals("test_out/test.js"));
  }

  @Test
  public void testFixupSourceLocationWithNoPrefixMappings() {
    String sourceFile = "test.js";
    sourceFile = sourceMapV1.fixupSourceLocation(sourceFile);
    assertTrue(sourceFile.equals("test.js"));

    sourceMapV2.setPrefixMappings(Collections.emptyList());

    sourceFile = "test.js";
    sourceFile = sourceMapV2.fixupSourceLocation(sourceFile);
    assertTrue(sourceFile.equals("test.js"));
  }

  @Test
  public void testFixupSourceLocationWithPrefixMappings() {
    String sourceFile = "test/subdir/test.js";
    sourceFile = sourceMapV1.fixupSourceLocation(sourceFile);
    assertTrue(sourceFile.equals("test/subdir/test.js"));

    sourceMapV2.setPrefixMappings(Lists.newArrayList(
        new SourceMap.LocationMapping("test/subdir", "test_out/subdir")
    ));

    sourceFile = "test/subdir/test.js";
    sourceFile = sourceMapV2.fixupSourceLocation(sourceFile);
    assertTrue(sourceFile.equals("test_out/subdir/test.js"));
  }

  @Test
  public void testValidate() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    sourceMapV1.validate(true);
    sourceMapV1.appendTo(output, "test.js");

    assertTrue(output.toString().isEmpty());

    sourceMapV2.setPrefixMappings(Lists.newArrayList(
        new SourceMap.LocationMapping("test", "test_out")
    ));

    sourceMapV2.appendTo(output, "test.js");

    String expectedSourceMap =
        "{\n" +
        "  \"version\": 3,\n" +
        "  \"file\": \"test_out/test.js\",\n" +
        "  \"sources\": [\n" +
        "    \"test.js\"\n" +
        "  ],\n" +
        "  \"sourceRoot\": \"test_out/\",\n" +
        "  \"mappings\": [\n" +
        "  ]\n" +
        "}\n";

    assertTrue(output.toString().equals(expectedSourceMap));
  }

  @Test
  public void testReset() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    sourceMapV1.appendTo(output, "test.js");
    sourceMapV1.reset();

    assertTrue(output.toString().isEmpty());
  }

  @Test
  public void testSetStartingPosition() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    sourceMapV1.appendTo(output, "test.js");
    sourceMapV1.setStartingPosition(1, 1);

    String expectedSourceMap =
        "{\n" +
        "  \"version\": 3,\n" +
        "  \"file\": \"test.js\",\n" +
        "  \"sources\": [\n" +
        "    \"test.js\"\n" +
        "  ],\n" +
        "  \"sourceRoot\": \"\",\n" +
        "  \"mappings\": [\n" +
        "    {\n" +
        "      \"original\": {\n" +
        "        \"line\": 1,\n" +
        "        \"column\": 1\n" +
        "      },\n" +
        "      \"generated\": {\n" +
        "        \"line\": 1,\n" +
        "        \"column\": 1\n" +
        "      }\n" +
        "    }\n" +
        "  ]\n" +
        "}\n";

    assertTrue(output.toString().equals(expectedSourceMap));
  }

  @Test
  public void testSetWrapperPrefix() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    sourceMapV1.appendTo(output, "test.js");
    sourceMapV1.setWrapperPrefix("test_out_");

    String expectedSourceMap =
        "{\n" +
        "  \"version\": 3,\n" +
        "  \"file\": \"test_out_test.js\",\n" +
        "  \"sources\": [\n" +
        "    \"test.js\"\n" +
        "  ],\n" +
        "  \"sourceRoot\": \"\",\n" +
        "  \"mappings\": [\n" +
        "  ]\n" +
        "}\n";

    assertTrue(output.toString().equals(expectedSourceMap));
  }

  @Test
  public void testApplyPredicate() {
    Node node1 = mock(Node.class);
    Node node2 = mock(Node.class);
    Node node3 = mock(Node.class);

    when(node1.isFunction()).thenReturn(true);
    when(node2.isName()).thenReturn(true);
    when(node3.isCall()).thenReturn(true);

    Predicate<Node> symbolPredicate = SourceMap.DetailLevel.SYMBOLS;

    assertTrue(symbolPredicate.apply(node1));
    assertTrue(symbolPredicate.apply(node2));
    assertFalse(symbolPredicate.apply(node3));
  }
}