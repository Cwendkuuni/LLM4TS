package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMap;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.jscomp.SourceMap.LocationMapping;
import com.google.javascript.rhino.NodeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SourceMapTest {
  private SourceMapGenerator generatorV1;
  private SourceMapGenerator generatorV2;
  private SourceMap generator;

  @Before
  public void setUp() {
    generatorV1 = SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1);
    generatorV2 = SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT);
  }

  @Test
  public void testNewSourceMap() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();
    assertNull(sourceMap.getGenerator());
    assertNull(sourceMap.getPrefixMappings());
  }

  @Test
  public void testNewSourceMapV1() {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance().getInstance();
    assertNull(sourceMap.getGenerator());
    assertNull(sourceMap.getPrefixMappings());
  }

  @Test
  public void testNewSourceMapV2() {
    SourceMap sourceMap = SourceMap.Format.V2.getInstance().getInstance();
    assertNull(sourceMap.getGenerator());
    assertNull(sourceMap.getPrefixMappings());
  }

  @Test
  public void testAddMapping() {
    // Create a new source map with no prefix mappings
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();

    Node node = Node.newLiteral(123);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 3);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String expectedSourceFile = "SomeSourceFile.js";
    String expectedSourceLocation = "SomeSourceFile.js:1:1-3";
    SourceMapGenerator generator = sourceMap.getGenerator();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      sourceMap.appendTo(out, "SomeFileName.js");
    } catch (IOException e) {
      // Should not happen since appendTo is not supposed to throw an exception
    }

    String sourceMapString = out.toString();
    int index = sourceMapString.indexOf(expectedSourceLocation);

    String actualSourceLocation = sourceMapString.substring(index - 10, index + expectedSourceLocation.length());
    assertThat(actualSourceLocation).isEqualTo(expectedSourceLocation);
  }

  @Test
  public void testAddMappingWithPrefixMappings() {
    // Create a new source map with prefix mappings
    List<LocationMapping> sourceMapLocationMappings = Lists.newArrayList(
        new LocationMapping("test/", "src/test/"),
        new LocationMapping("test/src/", "src/test/src/")
    );
    SourceMap sourceMap = new SourceMap(
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT),
        sourceMapLocationMappings);

    Node node = Node.newLiteral(123);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 3);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String expectedSourceFile = "test/SomeSourceFile.js";
    String expectedSourceLocation = "src/test/SomeSourceFile.js:1:1-3";
    SourceMapGenerator generator = sourceMap.getGenerator();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      sourceMap.appendTo(out, "test/SomeFileName.js");
    } catch (IOException e) {
      // Should not happen since appendTo is not supposed to throw an exception
    }

    String sourceMapString = out.toString();
    int index = sourceMapString.indexOf(expectedSourceLocation);

    String actualSourceLocation = sourceMapString.substring(index - 10, index + expectedSourceLocation.length());
    assertThat(actualSourceLocation).isEqualTo(expectedSourceLocation);
  }

  @Test
  public void testSetPrefixMappings() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();

    List<LocationMapping> sourceMapLocationMappings = Lists.newArrayList(
        new LocationMapping("test/", "src/test/"),
        new LocationMapping("test/src/", "src/test/src/")
    );
    sourceMap.setPrefixMappings(sourceMapLocationMappings);

    assertThat(sourceMap.getPrefixMappings()).isEqualTo(sourceMapLocationMappings);
  }

  @Test
  public void testFixupSourceLocationWithNoPrefixMappings() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();

    String sourceFile = "SomeSourceFile.js";
    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    assertThat(fixed).isEqualTo(sourceFile);
  }

  @Test
  public void testFixupSourceLocationWithPrefixMappings() {
    List<LocationMapping> sourceMapLocationMappings = Lists.newArrayList(
        new LocationMapping("test/", "src/test/"),
        new LocationMapping("test/src/", "src/test/src/")
    );
    SourceMap sourceMap = new SourceMap(
        generatorV1,
        sourceMapLocationMappings);

    String sourceFile = "test/SomeSourceFile.js";
    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    assertThat(fixed).isEqualTo("src/test/SomeSourceFile.js");
  }

  @Test
  public void testValidate() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();

    sourceMap.validate(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateWithNullGenerator() {
    SourceMap sourceMap = new SourceMap(null, Collections.emptyList());
    sourceMap.validate(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullNode() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();

    Node node = null;
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 3);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullOutputStartPosition() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();

    Node node = Node.newLiteral(123);
    FilePosition outputStartPosition = null;
    FilePosition outputEndPosition = new FilePosition(1, 3);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullOutputEndPosition() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();

    Node node = Node.newLiteral(123);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = null;

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNonStringSourceFile() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();

    Node node = Node.newLiteral(123);
    int sourceFile = 123;
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 3);

    sourceMap.addMapping(node, sourceFile, outputStartPosition, outputEndPosition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNegativeLineno() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();

    Node node = Node.newLiteral(123);
    String sourceFile = "SomeSourceFile.js";
    FilePosition outputStartPosition = new FilePosition(-1, 1);
    FilePosition outputEndPosition = new FilePosition(-1, 3);

    sourceMap.addMapping(node, sourceFile, outputStartPosition, outputEndPosition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNegativeCharno() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();

    Node node = Node.newLiteral(123);
    String sourceFile = "SomeSourceFile.js";
    FilePosition outputStartPosition = new FilePosition(1, -1);
    FilePosition outputEndPosition = new FilePosition(1, 3);

    sourceMap.addMapping(node, sourceFile, outputStartPosition, outputEndPosition);
  }

  @Test
  public void testAddMappingWithDetailLevel() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();

    Node node = Node.newLiteral(123);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 3);

    DetailLevel detailLevel = SourceMap.DetailLevel.ALL;
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition, detailLevel);
  }

  @Test
  public void testAddMappingWithNullDetailLevel() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();

    Node node = Node.newLiteral(123);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 3);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition, null);
  }

  @Test
  public void testReset() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();

    Node node = Node.newLiteral(123);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 3);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMap.reset();

    int size = sourceMap.getGenerator().getMappingCount();
    assertThat(size).isZero();
  }

  @Test
  public void testSetStartingPosition() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();

    int expectedStartingLine = 1;
    int expectedStartingIndex = 1;

    sourceMap.setStartingPosition(expectedStartingLine, expectedStartingIndex);

    SourceMapGenerator generator = sourceMap.getGenerator();

    FilePosition startingPosition = generator.getStartingPosition();

    int actualStartingLine = startingPosition.getLine();
    int actualStartingIndex = startingPosition.getCharacter();

    assertThat(actualStartingLine).isEqualTo(expectedStartingLine);
    assertThat(actualStartingIndex).isEqualTo(expectedStartingIndex);
  }

  @Test
  public void testSetWrapperPrefix() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();

    String expectedWrapperPrefix = "wrapper";

    sourceMap.setWrapperPrefix(expectedWrapperPrefix);

    String actualWrapperPrefix = sourceMap.getGenerator().getWrapperPrefix();

    assertThat(actualWrapperPrefix).isEqualTo(expectedWrapperPrefix);
  }

  @Test
  public void testAppendToWithNullName() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    sourceMap.appendTo(out, null);

    String expectedSourceMapString = "";
    String actualSourceMapString = out.toString();

    assertThat(actualSourceMapString).isEqualTo(expectedSourceMapString);
  }

  @Test
  public void testAppendTo() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();

    String name = "SomeFileName.js";
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    sourceMap.appendTo(out, name);

    String expectedSourceMapString = "sourceMappingURL=data:application/json;base64,";
    String actualSourceMapString = out.toString();

    assertThat(actualSourceMapString.startsWith(expectedSourceMapString)).isTrue();
  }

  @Test
  public void testInstanceOfSourceMapGeneratorV1() {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance().getInstance();

    SourceMapGenerator generator = sourceMap.getGenerator();

    assertThat(generator).isInstanceOf(SourceMapGeneratorV1.class);
  }

  @Test
  public void testInstanceOfSourceMapGeneratorV2() {
    SourceMap sourceMap = SourceMap.Format.V2.getInstance().getInstance();

    SourceMapGenerator generator = sourceMap.getGenerator();

    assertThat(generator).isInstanceOf(SourceMapGeneratorV2.class);
  }
}