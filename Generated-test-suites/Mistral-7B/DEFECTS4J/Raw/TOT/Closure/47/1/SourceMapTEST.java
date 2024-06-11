import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.rhino.Node;
import com.google.debugging.sourcemap.FilePosition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Tests for the SourceMap class.
 */
public class SourceMapTest {

  private SourceMap sourceMap;
  private Node node;
  private FilePosition outputStartPosition;
  private FilePosition outputEndPosition;

  @Before
  public void setUp() {
    // Create a new SourceMap generator for the V1 format with no prefix mappings.
    sourceMap = SourceMap.Format.V1.getInstance().getInstance();

    node = Node.newIntLiteral(1);
    outputStartPosition = new FilePosition(1, 1);
    outputEndPosition = new FilePosition(1, 1);
  }

  @Test
  public void testAddMapping() {
    // Add a mapping for the node with the given output start and end positions.
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // The source map generator should have added the mapping.
    assertEquals(1, sourceMap.getMappingCount());

    // Get the mapping and check that it matches the expected input and output positions.
    Map.Entry<String, Map<String, List<Map.Entry<String, FilePosition>>>> mapping =
        sourceMap.getMappingForSourceFile("test.js");
    assertNotNull(mapping);

    List<Map.Entry<String, FilePosition>> entries = mapping.getValue().get(node.getSourceFileName());
    assertNotNull(entries);
    assertEquals(1, entries.size());
    Map.Entry<String, FilePosition> mappingEntry = entries.get(0);
    assertEquals("test.js", mappingEntry.getKey());
    assertEquals(outputStartPosition, mappingEntry.getValue().getStartPosition());
    assertEquals(outputEndPosition, mappingEntry.getValue().getEndPosition());
  }

  @Test
  public void testAddMappingWithInvalidNode() {
    // Add a mapping for a null node.
    sourceMap.addMapping(null, outputStartPosition, outputEndPosition);

    // The source map should not have added the mapping.
    assertEquals(0, sourceMap.getMappingCount());
  }

  @Test
  public void testAddMappingWithInvalidOutputPositions() {
    // Add a mapping with invalid output positions.
    sourceMap.addMapping(node, null, null);

    // The source map should not have added the mapping.
    assertEquals(0, sourceMap.getMappingCount());
  }

  @Test
  public void testAppendTo() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    // Append the source map to the output stream.
    sourceMap.appendTo(out, "test.jsmap");

    // The output should be a valid source map.
    String sourceMapString = out.toString();
    assertNotNull(sourceMapString);
  }

  @Test
  public void testReset() {
    // Add a mapping and then reset the source map.
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMap.reset();

    // The source map should be empty after reset.
    assertEquals(0, sourceMap.getMappingCount());
  }

  @Test
  public void testSetStartingPosition() {
    // Set the starting position for the source map.
    sourceMap.setStartingPosition(1, 1);

    // The source map should have updated its starting position.
    assertEquals(new FilePosition(1, 1), sourceMap.getStartingPosition());
  }

  @Test
  public void testSetWrapperPrefix() {
    // Set the wrapper prefix for the source map.
    sourceMap.setWrapperPrefix("test:");

    // The source map should have updated its wrapper prefix.
    assertEquals("test:", sourceMap.getWrapperPrefix());
  }

  @Test
  public void testValidate() {
    // Validate the source map.
    sourceMap.validate(true);

    // The source map should not throw an exception.
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateWithInvalidFormat() {
    // Create a new SourceMap generator for an invalid format.
    SourceMapGenerator generator = SourceMapGeneratorFactory.getInstance(new String("INVALID"));
    SourceMap sourceMap = new SourceMap(generator);

    // Validate the source map.
    sourceMap.validate(true);
  }
}