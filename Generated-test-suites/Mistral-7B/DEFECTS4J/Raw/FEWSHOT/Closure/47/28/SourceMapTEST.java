package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMap;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  private SourceMapGenerator mockSourceMapGenerator;
  private SourceMap sourceMap;

  @Before
  public void setUp() {
    mockSourceMapGenerator = mock(SourceMapGenerator.class);
    sourceMap = new SourceMap(mockSourceMapGenerator);
  }

  @Test
  public void testInstanceCreation() {
    assertEquals(SourceMapFormat.DEFAULT, sourceMap.getFormat());
  }

  @Test
  public void testSetFormat() {
    Format format = Format.V1;
    sourceMap.setFormat(format);
    assertEquals(format, sourceMap.getFormat());
  }

  @Test
  public void testAddMapping() {
    String sourceFile = "input.js";
    int lineNumber = 1;
    int columnNumber = 2;

    FilePosition outputStartPosition = new FilePosition(lineNumber, columnNumber);

    Node node = mock(Node.class);
    when(node.getSourceFileName()).thenReturn(sourceFile);
    when(node.getLineno()).thenReturn(lineNumber);
    when(node.getCharno()).thenReturn(columnNumber);

    sourceMap.addMapping(node, outputStartPosition, outputStartPosition);

    SourceMapGenerator generator = sourceMap.getInstance();
    List<Map<String, Object>> sourceEntries = generator.getSourceEntries();
    assertEquals(1, sourceEntries.size());
    Map<String, Object> sourceEntry = sourceEntries.get(0);
    assertEquals(sourceFile, sourceEntry.get("source"));
    Map<String, Object> positions = (Map<String, Object>) sourceEntry.get("sourcesContent");
    assertEquals(Collections.singletonMap("", ""), positions);
    List<Map<String, Object>> mappings = (List<Map<String, Object>>) sourceEntry.get("mappings");
    assertEquals(1, mappings.size());
    Map<String, Object> mapping = mappings.get(0);
    assertEquals(lineNumber, mapping.get("original"));
    assertEquals(lineNumber, mapping.get("generated"));
  }

  @Test
  public void testFixupSourceLocation() {
    String sourceFile = "input.js";
    String prefix = "test/";
    List<LocationMapping> prefixMappings = Lists.newArrayList(new LocationMapping(prefix, "output/"));

    sourceMap.setPrefixMappings(prefixMappings);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("output/input.js", fixedSourceFile);
  }

  @Test
  public void testFixupSourceLocationNoPrefixMapping() {
    String sourceFile = "input.js";

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals(sourceFile, fixedSourceFile);
  }

  @Test
  public void testAppendTo() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    sourceMap.appendTo(outputStream, "test.js");

    String expectedSourceMapContent = "{\n" +
        "  \"version\": 3,\n" +
        "  \"file\": \"test.js\",\n" +
        "  \"sources\": [\n" +
        "    \"input.js\"\n" +
        "  ],\n" +
        "  \"sourcesContent\": {\n" +
        "    \"input.js\": \"\"\n" +
        "  },\n" +
        "  \"mappings\": []\n" +
        "}";

    String actualSourceMapContent = outputStream.toString();
    assertEquals(expectedSourceMapContent, actualSourceMapContent);
  }

  @Test
  public void testReset() {
    sourceMap.reset();

    assertNull(sourceMap.getInstance());
  }

  @Test
  public void testValidate() {
    when(mockSourceMapGenerator.validate(true)).thenReturn(true);

    sourceMap.validate(true);
  }
}