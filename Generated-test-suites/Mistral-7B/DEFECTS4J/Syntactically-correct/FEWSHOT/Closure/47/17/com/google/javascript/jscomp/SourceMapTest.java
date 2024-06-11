package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMap;
import com.google.debugging.sourcemap.SourceMapEntry;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for the {@link SourceMap} class.
 */
public class SourceMapTest {

  private static final String SOURCE_FILE = "TestSource.java";
  private static final Node NODE = Node.newInstance("identifier");
  private static final int LINE_NUMBER = 10;
  private static final int COLUMN_NUMBER = 20;

  @Test
  public void testInstance() {
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
    assertThat(sourceMap).isInstanceOf(SourceMap.class);
  }

  @Test
  public void testAddMapping() throws IOException {
    SourceMap sourceMap = new SourceMap(mockSourceMapGenerator());

    String sourceFile = "TestSource.java";
    String originalName = "TestMethod";
    FilePosition outputStartPosition = new FilePosition(LINE_NUMBER, COLUMN_NUMBER);

    sourceMap.addMapping(NODE, outputStartPosition, outputStartPosition);

    List<SourceMapEntry> sourceMapEntries = sourceMap.getSourceMapEntries();
    SourceMapEntry sourceMapEntry = sourceMapEntries.get(0);

    String expectedSourceFile = fixupSourceLocation(sourceFile);
    assertThat(sourceMapEntry.getSource()).isEqualTo(expectedSourceFile);
    assertThat(sourceMapEntry.getName()).isEqualTo(originalName);
    assertThat(sourceMapEntry.getLine()).isEqualTo(LINE_NUMBER);
    assertThat(sourceMapEntry.getColumn()).isEqualTo(COLUMN_NUMBER);
  }

  @Test
  public void testAddMappingWithPrefixMapping() throws IOException {
    SourceMap sourceMap = new SourceMap(mockSourceMapGenerator());
    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping("test/", "src/"));

    sourceMap.setPrefixMappings(prefixMappings);

    Node node = Node.newInstance("identifier");
    node.setSourceFileName("test/TestSource.java");
    node.setLineno(LINE_NUMBER);

    String sourceFile = "test/TestSource.java";
    String originalName = "TestMethod";
    FilePosition outputStartPosition = new FilePosition(LINE_NUMBER, COLUMN_NUMBER);

    sourceMap.addMapping(node, outputStartPosition, outputStartPosition);

    List<SourceMapEntry> sourceMapEntries = sourceMap.getSourceMapEntries();
    SourceMapEntry sourceMapEntry = sourceMapEntries.get(0);

    String expectedSourceFile = "src/TestSource.java";
    assertThat(sourceMapEntry.getSource()).isEqualTo(expectedSourceFile);
    assertThat(sourceMapEntry.getName()).isEqualTo(originalName);
    assertThat(sourceMapEntry.getLine()).isEqualTo(LINE_NUMBER);
    assertThat(sourceMapEntry.getColumn()).isEqualTo(COLUMN_NUMBER);
  }

  @Test
  public void testFixupSourceLocation() {
    SourceMap sourceMap = new SourceMap(mockSourceMapGenerator());

    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping("test/", "src/"));

    sourceMap.setPrefixMappings(prefixMappings);

    String sourceFile = "test/TestSource.java";

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertThat(fixedSourceFile).isEqualTo("src/TestSource.java");
  }

  @Test
  public void testFixupSourceLocationWithNoPrefixMapping() {
    SourceMap sourceMap = new SourceMap(mockSourceMapGenerator());

    String sourceFile = "TestSource.java";

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertThat(fixedSourceFile).isEqualTo(sourceFile);
  }

  @Test
  public void testValidate() throws IOException {
    SourceMap sourceMap = new SourceMap(mockSourceMapGenerator());

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    sourceMap.appendTo(outputStream, SOURCE_FILE);

    sourceMap.validate(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateWithNullOutputStream() throws IOException {
    SourceMap sourceMap = new SourceMap(mockSourceMapGenerator());

    sourceMap.validate(null);
  }

  @Test
  public void testReset() throws IOException {
    SourceMap sourceMap = new SourceMap(mockSourceMapGenerator());

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    sourceMap.appendTo(outputStream, SOURCE_FILE);

    sourceMap.reset();

    byte[] output = outputStream.toByteArray();
    String sourceMapText = new String(output);

    assertThat(sourceMapText).isEmpty();
  }

  @Test
  public void testSetWrapperPrefix() throws IOException {
    SourceMap sourceMap = new SourceMap(mockSourceMapGenerator());

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    sourceMap.appendTo(outputStream, SOURCE_FILE);

    sourceMap.setWrapperPrefix("wrapper_");

    sourceMap.appendTo(outputStream, SOURCE_FILE);

    byte[] output = outputStream.toByteArray();
    String sourceMapText = new String(output);

    String expectedSourceMapText =
        "{\"version\":1,\"file\":\"TestSource.java\",\"sources\":[\"wrapper_TestSource.java\"],\"sourcesContent\":{},\"fileContents\":[\"wrapper_TestSource.java\\n\",\"%s\"],\"lineMappings\":[";

    assertThat(sourceMapText).matches(expectedSourceMapText);
  }

  @Test
  public void testSetStartingPosition() throws IOException {
    SourceMap sourceMap = new SourceMap(mockSourceMapGenerator());

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    sourceMap.appendTo(outputStream, SOURCE_FILE);

    sourceMap.setStartingPosition(LINE_NUMBER, COLUMN_NUMBER);

    sourceMap.appendTo(outputStream, SOURCE_FILE);

    byte[] output = outputStream.toByteArray();
    String sourceMapText = new String(output);

    String expectedSourceMapText =
        "{\"version\":1,\"file\":\"TestSource.java\",\"sources\":[],\"sourcesContent\":{},\"fileContents\":[\"%s\"],\"lineMappings\":[";

    assertThat(sourceMapText).matches(expectedSourceMapText);

    int sourceMapIndex = sourceMapText.indexOf("\"lineMappings\":[");
    String sourceMapSubstring = sourceMapText.substring(sourceMapIndex);

    int mappingIndex = sourceMapSubstring.indexOf("[");

    String mappingString = sourceMapSubstring.substring(mappingIndex);

    String expectedMappingString = "  { \"source\": \"%s\", \"name\": \"\", \"original\": { \"line\": %d, \"column\": %d }, \"generated\": { \"line\": %d, \"column\": %d } }";

    assertThat(mappingString).matches(expectedMappingString);
  }

  private SourceMapGenerator mockSourceMapGenerator() {
    SourceMapGenerator sourceMapGenerator = mock(SourceMapGenerator.class);

    when(sourceMapGenerator.getVersion()).thenReturn(1);

    return sourceMapGenerator;
  }

  private String fixupSourceLocation(String sourceFile) {
    return SOURCE_FILE.equals(sourceFile) ? SOURCE_FILE : SOURCE_FILE + ":test";
  }
}