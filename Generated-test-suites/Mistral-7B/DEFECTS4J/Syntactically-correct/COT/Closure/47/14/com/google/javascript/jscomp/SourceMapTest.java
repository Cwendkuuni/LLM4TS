import static org.junit.Assert.*;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.LocationMapping;
import com.google.javascript.jscomp.SourceMapGeneratorFactory;
import com.google.javascript.rhino.Node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  @org.junit.Test
  public void testGetInstance() {
    for (Format format : Format.values()) {
      SourceMap sourceMap = format.getInstance();
      assertNotNull(sourceMap);
    }
  }

  @org.junit.Test
  public void testAddMapping() throws IOException {
    SourceMapGeneratorV1 generator =
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1);
    SourceMap sourceMap = new SourceMap(generator);

    Node node = new Node();
    node.setSourceFileName("test.js");
    node.setLineno(1);

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js.map");
    String result = out.toString();

    String expected = "{\n" +
        "  \"version\": 1,\n" +
        "  \"file\": \"test.js\",\n" +
        "  \"sources\": [\n" +
        "    \"test.js\"\n" +
        "  ],\n" +
        "  \"sourcesContent\": [\n" +
        "    \"\"\n" +
        "  ],\n" +
        "  \"name\": \"test.js\",\n" +
        "  \"mappings\": \"\".split(\"\\n\").slice(1)\n" +
        "}\n";

    assertEquals(expected, result);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullNode() {
    SourceMap sourceMap = new SourceMap(
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));

    Node node = null;
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullOutputStartPosition() {
    SourceMap sourceMap = new SourceMap(
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));

    Node node = new Node();
    FilePosition outputStartPosition = null;
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullOutputEndPosition() {
    SourceMap sourceMap = new SourceMap(
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));

    Node node = new Node();
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = null;

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @org.junit.Test
  public void testFixupSourceLocationWithNoMappings() {
    SourceMap sourceMap = new SourceMap(
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));

    String sourceFile = "test.js";

    String result = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals(sourceFile, result);
  }

  @org.junit.Test
  public void testFixupSourceLocationWithPrefixMappings() {
    SourceMapGeneratorFactory factory =
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1);
    SourceMapGenerator generator = factory.getInstance(SourceMapFormat.V1);
    SourceMap sourceMap = new SourceMap(generator);

    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping("test/", "src/"));

    sourceMap.setPrefixMappings(prefixMappings);

    String sourceFile = "test/test.js";

    String result = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals("src/test.js", result);
  }

  @org.junit.Test
  public void testAppendTo() throws IOException {
    SourceMapGeneratorFactory factory =
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1);
    SourceMapGenerator generator = factory.getInstance(SourceMapFormat.V1);
    SourceMap sourceMap = new SourceMap(generator);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js.map");

    String result = out.toString();

    String expected = "{\n" +
        "  \"version\": 1,\n" +
        "  \"file\": \"test.js\",\n" +
        "  \"sources\": [\n" +
        "    \"test.js\"\n" +
        "  ],\n" +
        "  \"sourcesContent\": [\n" +
        "    \"\"\n" +
        "  ],\n" +
        "  \"name\": \"test.js\",\n" +
        "  \"mappings\": \"\"\n" +
        "}\n";

    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testValidate() {
    SourceMapGeneratorFactory factory =
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1);
    SourceMapGenerator generator = factory.getInstance(SourceMapFormat.V1);
    SourceMap sourceMap = new SourceMap(generator);

    sourceMap.validate(true);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testValidateWithNullGenerator() {
    SourceMap sourceMap = new SourceMap(null);

    sourceMap.validate(true);
  }

  @org.junit.Test
  public void testReset() {
    SourceMapGeneratorFactory factory =
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1);
    SourceMapGenerator generator = factory.getInstance(SourceMapFormat.V1);
    SourceMap sourceMap = new SourceMap(generator);

    sourceMap.reset();
  }

  @org.junit.Test
  public void testSetStartingPosition() {
    SourceMapGeneratorFactory factory =
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1);
    SourceMapGenerator generator = factory.getInstance(SourceMapFormat.V1);
    SourceMap sourceMap = new SourceMap(generator);

    sourceMap.setStartingPosition(1, 1);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testSetStartingPositionWithNegativeLineNumber() {
    SourceMapGeneratorFactory factory =
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1);
    SourceMapGenerator generator = factory.getInstance(SourceMapFormat.V1);
    SourceMap sourceMap = new SourceMap(generator);

    sourceMap.setStartingPosition(-1, 1);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testSetStartingPositionWithNegativeIndex() {
    SourceMapGeneratorFactory factory =
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1);
    SourceMapGenerator generator = factory.getInstance(SourceMapFormat.V1);
    SourceMap sourceMap = new SourceMap(generator);

    sourceMap.setStartingPosition(1, -1);
  }

  @org.junit.Test
  public void testSetWrapperPrefix() {
    SourceMapGeneratorFactory factory =
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1);
    SourceMapGenerator generator = factory.getInstance(SourceMapFormat.V1);
    SourceMap sourceMap = new SourceMap(generator);

    sourceMap.setWrapperPrefix("wrapper");
  }
}