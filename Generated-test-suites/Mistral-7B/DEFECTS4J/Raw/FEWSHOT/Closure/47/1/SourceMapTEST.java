package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
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

public class SourceMapTest {

  @Test
  public void testFormatV1() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance()).setPrefixMappings(ImmutableList.of());

    Node node = Node.newNode("test");
    node.setLine(1);
    node.setSourceFileName("test.js");

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.map");

    String sourceMapText = out.toString();
    SourceMapGeneratorV1 sourceMapGeneratorV1 = new SourceMapGeneratorV1();
    String expectedSourceMapText = sourceMapGeneratorV1.generate(null, ImmutableList.of("test.js"), ImmutableList.of(), null);

    assertThat(sourceMapText).isEqualTo(expectedSourceMapText);
  }

  @Test
  public void testFormatV2() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V2.getInstance()).setPrefixMappings(ImmutableList.of());

    Node node = Node.newNode("test");
    node.setLine(1);
    node.setSourceFileName("test.js");

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.map");

    String sourceMapText = out.toString();
    SourceMapGeneratorV2 sourceMapGeneratorV2 = new SourceMapGeneratorV2();
    String expectedSourceMapText = sourceMapGeneratorV2.generate(null, ImmutableList.of("test.js"), ImmutableList.of(), null);

    assertThat(sourceMapText).isEqualTo(expectedSourceMapText);
  }

  @Test
  public void testAddMappingWithDetailLevel() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance()).setPrefixMappings(Collections.emptyList())
        .setDetailLevel(SourceMap.DetailLevel.ALL);

    Node node = Node.newNode("test");
    node.setLine(1);
    node.setSourceFileName("test.js");

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.map");

    String sourceMapText = out.toString();

    // The generated source map text is not important here, since we're just
    // testing the addMapping method.

    Node node2 = Node.newNode("test2");
    node2.setLine(2);
    node2.setSourceFileName("test2.js");

    sourceMap.addMapping(node2, new FilePosition(2, 2), new FilePosition(2, 6));

    // The addMapping method should not add duplicate mappings.
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Test with a node that does not have a source file or line number.
    Node node3 = Node.newNode("test3");

    assertNull(sourceMap.addMapping(node3, outputStartPosition, outputEndPosition));
  }

  @Test
  public void testSetPrefixMappings() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance()).setDetailLevel(SourceMap.DetailLevel.ALL);

    List<LocationMapping> sourceMapLocationMappings = ImmutableList.of(
        new LocationMapping("old_project/", "new_project/"),
        new LocationMapping("src/", "dist/")
    );

    sourceMap.setPrefixMappings(sourceMapLocationMappings);

    Node node = Node.newNode("test");
    node.setLine(1);
    node.setSourceFileName("src/test.js");

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    String expectedSourceFile = "dist/test.js";

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String sourceFile = sourceMap.fixupSourceLocation(node.getSourceFileName());

    assertTrue(sourceFile.equals(expectedSourceFile));
  }

  @Test
  public void testSetPrefixMappingsWithCollidingPrefixes() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance()).setDetailLevel(SourceMap.DetailLevel.ALL);

    List<LocationMapping> sourceMapLocationMappings = ImmutableList.of(
        new LocationMapping("old_project/", "new_project/"),
        new LocationMapping("src/", "dist/src/")
    );

    sourceMap.setPrefixMappings(sourceMapLocationMappings);

    Node node = Node.newNode("test");
    node.setLine(1);
    node.setSourceFileName("src/test.js");

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    String expectedSourceFile = "dist/src/test.js";

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String sourceFile = sourceMap.fixupSourceLocation(node.getSourceFileName());

    assertTrue(sourceFile.equals(expectedSourceFile));
  }

  @Test
  public void testSetPrefixMappingsWithPrefixNotStartingWithSlash() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance()).setDetailLevel(SourceMap.DetailLevel.ALL);

    List<LocationMapping> sourceMapLocationMappings = ImmutableList.of(
        new LocationMapping("old_project", "new_project")
    );

    sourceMap.setPrefixMappings(sourceMapLocationMappings);

    Node node = Node.newNode("test");
    node.setLine(1);
    node.setSourceFileName("old_project/test.js");

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    String expectedSourceFile = "new_project/test.js";

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String sourceFile = sourceMap.fixupSourceLocation(node.getSourceFileName());

    assertTrue(sourceFile.equals(expectedSourceFile));
  }

  @Test
  public void testSetPrefixMappingsWithTrailingSlash() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance()).setDetailLevel(SourceMap.DetailLevel.ALL);

    List<LocationMapping> sourceMapLocationMappings = ImmutableList.of(
        new LocationMapping("old_project/", "new_project/")
    );

    sourceMap.setPrefixMappings(sourceMapLocationMappings);

    Node node = Node.newNode("test");
    node.setLine(1);
    node.setSourceFileName("old_project/test.js");

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    String expectedSourceFile = "new_project/test.js";

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String sourceFile = sourceMap.fixupSourceLocation(node.getSourceFileName());

    assertTrue(sourceFile.equals(expectedSourceFile));
  }

  @Test
  public void testNodeUtilIsGet() {
    Node node = Node.newNode("getter");
    Node parentNode = Node.newNode("object");
    parentNode.setProperty("foo", node);

    assertTrue(SourceMap.DetailLevel.SYMBOLS.apply(node));
    assertTrue(SourceMap.DetailLevel.SYMBOLS.apply(parentNode));
    assertFalse(SourceMap.DetailLevel.SYMBOLS.apply(Node.newNode("number")));
    assertFalse(SourceMap.DetailLevel.SYMBOLS.apply(Node.newNode("function")));
  }

  @Test
  public void testNodeUtilIsObjectLitKey() {
    Node parentNode = Node.newNode("object");
    Node node = Node.newNode("key");
    Node objectLitKey = Node.newNode("{key: node}");
    objectLitKey.setLine(1);
    objectLitKey.setSourceFileName("keyMap.js");

    parentNode.setProperty("foo", objectLitKey);

    assertTrue(SourceMap.DetailLevel.SYMBOLS.apply(parentNode));
    assertTrue(SourceMap.DetailLevel.SYMBOLS.apply(node));
    assertFalse(SourceMap.DetailLevel.SYMBOLS.apply(Node.newNode("number")));
  }
}