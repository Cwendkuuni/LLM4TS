package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  @Test
  public void testFormatV1() {
    SourceMap sourceMap = SourceMap.newV1();
    testSourceMap(sourceMap, SourceMapFormat.V1);
  }

  @Test
  public void testFormatV2() {
    SourceMap sourceMap = SourceMap.newV2();
    testSourceMap(sourceMap, SourceMapFormat.V2);
  }

  @Test
  public void testFormatDefault() {
    SourceMap sourceMap = SourceMap.newDefault();
    testSourceMap(sourceMap, SourceMapFormat.DEFAULT);
  }

  @Test
  public void testNewInstance() {
    SourceMap sourceMap = SourceMap.newInstance(SourceMapFormat.V1);
    assertNotNull(sourceMap);
  }

  @Test
  public void testAddMapping() {
    SourceMap sourceMap = SourceMap.newInstance(SourceMapFormat.V1);

    Node node = new Node();
    node.setSourceFileName("testFile.js");
    node.setLineno(1);
    node.setCharno(0);

    FilePosition outputStartPosition = new FilePosition(2, 3);
    FilePosition outputEndPosition = new FilePosition(4, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    SourceMapGeneratorV1 generator = (SourceMapGeneratorV1) sourceMap.getGenerator();
    List<String> sourceMapData = generator.toSourceMapData();
    String sourceMapString = String.join("\n", sourceMapData);

    String expected = "{\"version\":1,\"file\":\"testFile.js\",\"sources\":{},\"sourcesContent\":{},\"names\":[],\"mappings\":[";
    String actual = sourceMapString.substring(0, expected.length());
    assertTrue(actual.equals(expected) || actual.startsWith(expected));
  }

  @Test
  public void testAddMappingWithPrefixMappings() {
    SourceMap sourceMap = SourceMap.newInstance(SourceMapFormat.V1);
    sourceMap.setPrefixMappings(ImmutableList.of(
        new SourceMap.LocationMapping("/prefix1/", "/replacement1/"),
        new SourceMap.LocationMapping("/prefix2/", "/replacement2/")));

    Node node = new Node();
    node.setSourceFileName("/prefix1/testFile.js");
    node.setLineno(1);
    node.setCharno(0);

    FilePosition outputStartPosition = new FilePosition(2, 3);
    FilePosition outputEndPosition = new FilePosition(4, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    SourceMapGeneratorV1 generator = (SourceMapGeneratorV1) sourceMap.getGenerator();
    List<String> sourceMapData = generator.toSourceMapData();
    String sourceMapString = String.join("\n", sourceMapData);

    String expected = "{\"version\":1,\"file\":\"/replacement1/testFile.js\",\"sources\":{},\"sourcesContent\":{},\"names\":[],\"mappings\":[";
    String actual = sourceMapString.substring(0, expected.length());
    assertTrue(actual.equals(expected) || actual.startsWith(expected));
  }

  @Test
  public void testReset() {
    SourceMap sourceMap = SourceMap.newInstance(SourceMapFormat.V1);
    sourceMap.addMapping(new Node(), new FilePosition(1, 1), new FilePosition(1, 1));
    sourceMap.reset();
    assertFalse(sourceMap.hasMappings());
  }

  @Test
  public void testValidate() throws IOException {
    SourceMap sourceMap = SourceMap.newInstance(SourceMapFormat.V1);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sourceMap.setWrapperPrefix("testWrapperPrefix");
    sourceMap.validate(true);
    sourceMap.appendTo(outputStream, "testFile.js");
    String sourceMapString = outputStream.toString();

    String expected = "testWrapperPrefix%2FtestFile.js\n";
    String actual = sourceMapString.substring(0, expected.length());
    assertTrue(actual.equals(expected) || actual.startsWith(expected));
  }

  @Test
  public void testSetStartingPosition() {
    SourceMap sourceMap = SourceMap.newInstance(SourceMapFormat.V1);
    sourceMap.setStartingPosition(1, 1);
    SourceMapGenerator generator = sourceMap.getGenerator();
    FilePosition position = generator.getStartingPosition();
    assertEquals(1, position.getLineNumber());
    assertEquals(1, position.getColumnNumber());
  }

  @Test
  public void testDetailLevelAll() {
    SourceMap sourceMap = SourceMap.newInstance(SourceMapFormat.DEFAULT);
    sourceMap.setDetailLevel(SourceMap.DetailLevel.ALL);
    Node node = new Node();
    node.setSourceFileName("testFile.js");
    node.setLineno(1);
    node.setCharno(0);
    sourceMap.addMapping(node, new FilePosition(2, 3), new FilePosition(
        2, 3));
    assertTrue(sourceMap.hasMappingFor(node));
  }

  @Test
  public void testDetailLevelSymbols() {
    SourceMap sourceMap = SourceMap.newInstance(SourceMapFormat.DEFAULT);
    sourceMap.setDetailLevel(SourceMap.DetailLevel.SYMBOLS);
    Node node = new Node();
    node.setName("testName");
    node.setLineno(1);
    node.setCharno(0);
    sourceMap.addMapping(node, new FilePosition(2, 3), new FilePosition(
        2, 3));
    assertTrue(sourceMap.hasMappingFor(node));
  }

  @Test
  public void testDetailLevelNone() {
    SourceMap sourceMap = SourceMap.newInstance(SourceMapFormat.DEFAULT);
    sourceMap.setDetailLevel(SourceMap.DetailLevel.NONE);
    Node node = new Node();
    node.setLineno(1);
    node.setCharno(0);
    assertFalse(sourceMap.hasMappingFor(node));
  }

  @Test
  public void testHasMappings() {
    SourceMap sourceMap = SourceMap.newInstance(SourceMapFormat.DEFAULT);
    Node node = new Node();
    assertFalse(sourceMap.hasMappings());
    sourceMap.addMapping(node, new FilePosition(1, 1), new FilePosition(1, 1));
    assertTrue(sourceMap.hasMappings());
  }

  private void testSourceMap(SourceMap sourceMap, SourceMapFormat format) {
    assertNotNull(sourceMap);
    assertEquals(format, sourceMap.getFormat());

    SourceMapGenerator generator = sourceMap.getGenerator();
    SourceMapFormat expectedFormat = checkNotNull(generator.getFormat());
    assertEquals(format, expectedFormat);
  }
}