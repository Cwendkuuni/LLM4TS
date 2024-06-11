import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceMap.Format.*;
import static com.google.javascript.jscomp.SourceMap.DetailLevel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.SourceMap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;

public class SourceMapTest {

  @Test
  public void testSourceMapGeneration() throws IOException {
    // Create a SourceMap instance with the V1 format
    SourceMap sourceMap = FORMAT_V1.getInstance().getInstance();

    // Set prefix mappings for testing
    List<SourceMap.LocationMapping> prefixMappings = ImmutableList.of(
        new SourceMap.LocationMapping("test/", "output/"),
        new SourceMap.LocationMapping("src/main/", "dist/")
    );
    sourceMap.setPrefixMappings(prefixMappings);

    // Create test Node objects for testing
    Node testNode1 = Node.newNode("testmethod1(x, y)");
    Node testNode2 = Node.newNode("testmethod2()");

    // Set source file names for test Node objects
    testNode1.setSourceFileName("test/TestSourceMap.java");
    testNode2.setSourceFileName("src/main/JavaSourceMap.java");

    // Add mappings for test Node objects
    sourceMap.addMapping(testNode1, new FilePosition(1, 1), new FilePosition(1, 1));
    sourceMap.addMapping(testNode2, new FilePosition(2, 2), new FilePosition(3, 3));

    // Create output stream for testing
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    // Append SourceMap to output stream
    sourceMap.appendTo(outputStream, "TestSourceMap.js.map");

    // Test SourceMap generation
    String sourceMapString = outputStream.toString();
    assertTrue(sourceMapString.contains("{\\n  \"version\": 1,\\n  \"file\": \"TestSourceMap.js.map\""));
    assertTrue(sourceMapString.contains("\"test/TestSourceMap.java\":["));
    assertTrue(sourceMapString.contains("\"src/main/JavaSourceMap.java\":["));

    // Test mapping for testNode1
    String testNode1Source = "test/TestSourceMap.java";
    String testNode1Mapping = sourceMapString.substring(
        sourceMapString.indexOf(testNode1Source) + testNode1Source.length() + 1,
        sourceMapString.indexOf("],")
    );
    List<Map.Entry<String, Map<String, List<FilePosition>>>> testNode1Mappings = parseSourceMapMappings(testNode1Mapping);
    assertEquals(1, testNode1Mappings.size());
    Map<String, List<FilePosition>> testNode1Map = testNode1Mappings.get(0).getValue();
    List<FilePosition> testNode1FilePositions = testNode1Map.get("testmethod1");
    assertEquals(1, testNode1FilePositions.size());
    assertEquals(new FilePosition(1, 1), testNode1FilePositions.get(0));

    // Test mapping for testNode2
    String testNode2Source = "src/main/JavaSourceMap.java";
    String testNode2Mapping = sourceMapString.substring(
        sourceMapString.indexOf(testNode2Source) + testNode2Source.length() + 1,
        sourceMapString.indexOf("]}")
    );
    List<Map.Entry<String, Map<String, List<FilePosition>>>> testNode2Mappings = parseSourceMapMappings(testNode2Mapping);
    assertEquals(1, testNode2Mappings.size());
    Map<String, List<FilePosition>> testNode2Map = testNode2Mappings.get(0).getValue();
    List<FilePosition> testNode2FilePositions = testNode2Map.get("testmethod2");
    assertEquals(1, testNode2FilePositions.size());
    assertEquals(new FilePosition(3, 3), testNode2FilePositions.get(0));
  }

  @Test
  public void testSourceMapGenerationWithError() throws IOException {
    // Create a SourceMap instance with the DEFAULT format
    SourceMap sourceMap = FORMAT_DEFAULT.getInstance().getInstance();

    // Set prefix mappings for testing
    List<SourceMap.LocationMapping> prefixMappings = Arrays.asList(
        new SourceMap.LocationMapping("test/", "output/"),
        new SourceMap.LocationMapping("src/main/", "dist/")
    );
    sourceMap.setPrefixMappings(prefixMappings);

    // Create test Node objects for testing
    Node testNode = Node.newNode("testmethod()");

    // Set source file name for test Node object
    testNode.setSourceFileName("test/TestSourceMap.java");

    // Add mappings for test Node object with invalid FilePosition
    sourceMap.addMapping(testNode, new FilePosition(-1, -1), new FilePosition(1, 1));

    // Create output stream for testing
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    // Append SourceMap to output stream
    try {
      sourceMap.appendTo(outputStream, "TestSourceMap.js.map");
      fail("Expected IOException with invalid FilePosition");
    } catch (IOException e) {
      // Expected
    }
  }

  @Test
  public void testSourceMapGenerationWithoutPrefixMappings() throws IOException {
    // Create a SourceMap instance with the DEFAULT format
    SourceMap sourceMap = FORMAT_DEFAULT.getInstance().getInstance();

    // Create test Node objects for testing
    Node testNode1 = Node.newNode("testmethod1(x, y)");
    Node testNode2 = Node.newNode("testmethod2()");

    // Set source file names for test Node objects
    testNode1.setSourceFileName("test/TestSourceMap.java");
    testNode2.setSourceFileName("src/main/JavaSourceMap.java");

    // Add mappings for test Node objects
    sourceMap.addMapping(testNode1, new FilePosition(1, 1), new FilePosition(1, 1));
    sourceMap.addMapping(testNode2, new FilePosition(2, 2), new FilePosition(3, 3));

    // Create output stream for testing
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    // Append SourceMap to output stream
    String sourceMapString = sourceMap.appendTo(outputStream, "TestSourceMap.js.map");

    // Test SourceMap generation without prefix mappings
    assertTrue(sourceMapString.contains("{\\n  \"version\": 1,\\n  \"file\": \"TestSourceMap.js.map\""));
    assertTrue(sourceMapString.contains("\"test/TestSourceMap.java\":["));
    assertTrue(sourceMapString.contains("\"src/main/JavaSourceMap.java\":["));

    // Test mapping for testNode1
    String testNode1Source = "test/TestSourceMap.java";
    String testNode1Mapping = sourceMapString.substring(
        sourceMapString.indexOf(testNode1Source) + testNode1Source.length() + 1,
        sourceMapString.indexOf("],")
    );
    List<Map.Entry<String, Map<String, List<FilePosition>>>> testNode1Mappings = parseSourceMapMappings(testNode1Mapping);
    assertEquals(1, testNode1Mappings.size());
    Map<String, List<FilePosition>> testNode1Map = testNode1Mappings.get(0).getValue();
    List<FilePosition> testNode1FilePositions = testNode1Map.get("testmethod1");
    assertEquals(1, testNode1FilePositions.size());
    assertEquals(new FilePosition(1, 1), testNode1FilePositions.get(0));

    // Test mapping for testNode2
    String testNode2Source = "src/main/JavaSourceMap.java";
    String testNode2Mapping = sourceMapString.substring(
        sourceMapString.indexOf(testNode2Source) + testNode2Source.length() + 1,
        sourceMapString.indexOf("]}")
    );
    List<Map.Entry<String, Map<String, List<FilePosition>>>> testNode2Mappings = parseSourceMapMappings(testNode2Mapping);
    assertEquals(1, testNode2Mappings.size());
    Map<String, List<FilePosition>> testNode2Map = testNode2Mappings.get(0).getValue();
    List<FilePosition> testNode2FilePositions = testNode2Map.get("testmethod2");
    assertEquals(1, testNode2FilePositions.size());
    assertEquals(new FilePosition(3, 3), testNode2FilePositions.get(0));
  }

  private List<Map.Entry<String, Map<String, List<FilePosition>>>> parseSourceMapMappings(String sourceMapMapping) {
    String jsonString = sourceMapMapping.substring(sourceMapMapping.indexOf("{")).replaceAll("\\\\/", "/");
    return Maps.parseMap(jsonString, Map.Entry.class);
  }
}