package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMap;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.rhino.Node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Test class for SourceMap.
 */
public class SourceMapTest {

  @Test
  public void testNewInstance() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
    assertNotNull(sourceMap);
  }

  @Test
  public void testAddMapping() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();

    String sourceFile = "TestSourceFile.java";
    String originalName = "testMethod";
    int lineNumber = 5;
    int characterNumber = 10;

    Node node = new Node();
    node.setSourceFileName(sourceFile);
    node.setLineno(lineNumber);
    node.setCharno(characterNumber);

    FilePosition outputStartPosition = new FilePosition(lineNumber, characterNumber + 1);
    FilePosition outputEndPosition = new FilePosition(lineNumber, characterNumber + 10);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String expectedSourceFile = sourceFile;
    String expectedOriginalName = originalName;

    List<Map.Entry<String, String>> mappings = sourceMap.getMappings();

    assertEquals("Size of the mappings list should be 1", 1, mappings.size());
    Map.Entry<String, String> mapping = mappings.get(0);

    assertEquals("Expected source file to match", expectedSourceFile, mapping.getKey());
    assertEquals("Expected original name to match", expectedOriginalName, mapping.getValue());
  }

  @Test
  public void testFixupSourceLocation() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();

    String sourceFile = "TestSourceFile/prefix1.java";
    String expectedFixedSourceFile = "TestSourceFile/fixed.java";

    sourceMap.setPrefixMappings(Lists.newArrayList(
        new SourceMap.LocationMapping("prefix1", "fixed")
    ));

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals("Expected fixed source file to match", expectedFixedSourceFile, fixedSourceFile);
  }

  @Test
  public void testAppendTo() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();

    String name = "TestSourceMap.js";

    sourceMap.appendTo(outputStream, name);

    String expectedSourceMapContents = "SourceMap:\n" +
        "version: 3;\n" +
        "file: TestSourceMap.js";

    String actualSourceMapContents = outputStream.toString();

    assertEquals("Expected SourceMap contents to match", expectedSourceMapContents, actualSourceMapContents);
  }

  @Test
  public void testReset() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();

    sourceMap.addMapping(new Node(), new FilePosition(1, 1), new FilePosition(1, 1));

    sourceMap.reset();

    List<Map.Entry<String, String>> mappings = sourceMap.getMappings();

    assertEquals("Size of the mappings list should be 0", 0, mappings.size());
  }

  @Test
  public void testSetStartingPosition() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();

    sourceMap.setStartingPosition(1, 1);

    FilePosition expectedStartingPosition = new FilePosition(1, 1);

    FilePosition actualStartingPosition = sourceMap.getStartingPosition();

    assertEquals("Expected starting position to match", expectedStartingPosition, actualStartingPosition);
  }

  @Test
  public void testSetWrapperPrefix() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();

    String prefix = "wrapper";

    sourceMap.setWrapperPrefix(prefix);

    String expectedWrapperPrefix = prefix;

    String actualWrapperPrefix = sourceMap.getWrapperPrefix();

    assertEquals("Expected wrapper prefix to match", expectedWrapperPrefix, actualWrapperPrefix);
  }

  @Test
  public void testValidate() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();

    boolean validate = true;

    sourceMap.validate(validate);

    // Since no assertions are made in this test case, it just validates
    // the SourceMap internally.
  }
}