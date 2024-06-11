package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.junit.Before;
import org.junit.Test;

import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.rhino.Node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Test cases for the SourceMap class.
 */
public class SourceMapTest {

  private SourceMap generatorV1;
  private SourceMap generatorV2;

  @Before
  public void setUp() {
    generatorV1 = SourceMap.FORMAT.V1.getInstance();
    generatorV2 = SourceMap.FORMAT.DEFAULT.getInstance();
  }

  @Test
  public void testAddMapping() {
    // Test adding a mapping for a Node with a source location.
    Node node = new Node();
    String sourceFile = "testSourceFile.js";

    // Set the source file and line number for the Node.
    node.setSourceFileName(sourceFile);
    node.setLineno(1);

    // Create a SourceMap instance and add a mapping for the Node.
    SourceMap sourceMap = new SourceMap(generatorV1);
    sourceMap.addMapping(node, new FilePosition(1, 0), new FilePosition(1, 0));

    // Verify that the mapping was added correctly.
    String expectedSourceMap = "version 1\n" +
        "Sources: testSourceFile.js\n" +
        "s testSourceFile.js:1:0 # 0:1\n";
    String actualSourceMap = new String(sourceMap.toByteArray());
    assertThat(actualSourceMap).isEqualTo(expectedSourceMap);
  }

  @Test
  public void testAddMappingWithNullSourceFile() {
    // Test adding a mapping for a Node with a null source file.
    Node node = new Node();

    // Create a SourceMap instance and add a mapping for the Node.
    SourceMap sourceMap = new SourceMap(generatorV1);
    sourceMap.addMapping(node, null, null);

    // Verify that no mapping was added.
    String actualSourceMap = new String(sourceMap.toByteArray());
    assertTrue(actualSourceMap.isEmpty());
  }

  @Test
  public void testAddMappingWithNegativeLineNumber() {
    // Test adding a mapping for a Node with a negative line number.
    Node node = new Node();
    String sourceFile = "testSourceFile.js";

    // Set the source file and line number for the Node.
    node.setSourceFileName(sourceFile);
    node.setLineno(-1);

    // Create a SourceMap instance and add a mapping for the Node.
    SourceMap sourceMap = new SourceMap(generatorV1);
    sourceMap.addMapping(node, new FilePosition(-1, 0), new FilePosition(-1, 0));

    // Verify that no mapping was added.
    String actualSourceMap = new String(sourceMap.toByteArray());
    assertTrue(actualSourceMap.isEmpty());
  }

  @Test
  public void testFixupSourceLocation() {
    // Test fixing up a source location with no prefix mappings.
    String sourceFile = "testPrefixFreeSourceFile.js";
    String fixedSourceFile = SourceMapTest.fixupSourceLocation(sourceFile);
    assertThat(fixedSourceFile).isEqualTo(sourceFile);

    // Test fixing up a source location with prefix mappings.
    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping("testPrefix/", "testPrefixedSourceFile/")
    );
    SourceMap sourceMap = new SourceMap(generatorV1);
    sourceMap.setPrefixMappings(prefixMappings);
    String fixedSourceFileWithPrefix = SourceMapTest.fixupSourceLocation(sourceFile);
    assertThat(fixedSourceFileWithPrefix).isEqualTo("testPrefixedSourceFile/testPrefixFreeSourceFile.js");
  }

  @Test
  public void testAppendTo() throws IOException {
    // Test appending the SourceMap to an output stream.
    String name = "testOutputFile.js.map";
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    SourceMap sourceMap = generatorV1.getGenerator();
    sourceMap.appendTo(outputStream, name);

    // Verify that the SourceMap was appended to the output stream.
    String expectedSourceMap = "version 1\n" +
        "Sources: testSourceFile.js\n";
    String actualSourceMap = new String(outputStream.toByteArray());
    assertThat(actualSourceMap).isEqualTo(expectedSourceMap);
  }

  @Test
  public void testReset() {
    // Test resetting the SourceMap.
    SourceMap sourceMap = generatorV1.getGenerator();
    sourceMap.reset();

    // Verify that the generator is reset.
    String actualSourceMap = new String(sourceMap.toByteArray());
    assertTrue(actualSourceMap.isEmpty());
  }

  @Test
  public void testSetStartingPosition() {
    // Test setting the starting position for the SourceMap.
    SourceMap sourceMap = generatorV1.getGenerator();
    sourceMap.setStartingPosition(1, 0);

    // Verify that the starting position was set.
    String expectedSourceMap = "version 1\n" +
        "Sources: testSourceFile.js\n" +
        "s testSourceFile.js:1:0 # 0:1\n";
    String actualSourceMap = new String(sourceMap.toByteArray());
    String expectedStartingPosition = "s testSourceFile.js:1:0 ";
    String actualStartingPosition = new String(sourceMap.toByteArray(0, expectedStartingPosition.length()));
    assertThat(actualSourceMap).contains(expectedStartingPosition);
  }

  @Test
  public void testSetWrapperPrefix() {
    // Test setting the wrapper prefix for the SourceMap.
    String prefix = "testWrapperPrefix_";
    SourceMap sourceMap = generatorV1.getGenerator();
    sourceMap.setWrapperPrefix(prefix);

    // Verify that the wrapper prefix was set.
    String expectedSourceMap = "version 1\n" +
        "Sources: testSourceFile.js\n" +
        "mappings: " + prefix + "testSourceFile.js.map\n";
    String actualSourceMap = new String(sourceMap.toByteArray());
    String expectedWrapperPrefix = "mappings: " + prefix;
    String actualWrapperPrefix = new String(sourceMap.toByteArray(0, expectedWrapperPrefix.length()));
    assertThat(actualSourceMap).contains(expectedWrapperPrefix);
  }

  @Test
  public void testValidate() {
    // Test validating the SourceMap.
    SourceMap sourceMap = generatorV1.getGenerator();
    sourceMap.validate(true);

    // No exception should be thrown.
  }

  /**
   * Helper method to fix up a source location with the provided SourceMap instance.
   *
   * @param sourceFile The source file location to fixup.
   * @return a remapped source file.
   */
  private static String fixupSourceLocation(String sourceFile) {
    SourceMap sourceMap = new SourceMap(generatorV1);
    return sourceMap.fixupSourceLocation(sourceFile);
  }
}