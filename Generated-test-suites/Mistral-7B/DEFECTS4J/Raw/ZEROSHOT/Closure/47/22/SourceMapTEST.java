import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceMap.DetailLevel.*;
import static com.google.javascript.jscomp.SourceMap.Format.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

/**
 * Test class for {@link SourceMap} class
 */
public class SourceMapTest {

  @Test
  public void testCreateSourceMap() throws IOException {
    // Create a new source map generator for format V1
    SourceMap sourceMap = FORMAT_V1.getInstance().getInstance();

    // Test adding mappings
    addMappings(sourceMap);

    // Test appending to a file
    appendToFile(sourceMap);

    // Test resetting the source map
    reset(sourceMap);

    // Test setting starting position
    setStartingPosition(sourceMap);

    // Test validation
    validate(sourceMap);
  }

  @Test
  public void testCreateSourceMapWithPrefixMappings() throws IOException {
    // Create a new source map generator for format V2 with prefix mappings
    SourceMap sourceMap = FORMAT_V2.getInstance().getInstance();
    List<LocationMapping> prefixMappings = ImmutableList.of(
        new LocationMapping("old_prefix", "new_prefix"),
        new LocationMapping("another_old_prefix", "another_new_prefix"));
    sourceMap.setPrefixMappings(prefixMappings);

    // Test adding mappings with prefix mappings
    addMappingsWithPrefixMappings(sourceMap);

    // Test appending to a file with prefix mappings
    appendToFileWithPrefixMappings(sourceMap);

    // Test resetting the source map with prefix mappings
    resetWithPrefixMappings(sourceMap);

    // Test setting starting position with prefix mappings
    setStartingPositionWithPrefixMappings(sourceMap);

    // Test validation with prefix mappings
    validateWithPrefixMappings(sourceMap);
  }

  /**
   * Add mappings to a source map
   *
   * @param sourceMap the source map to add mappings to
   */
  private void addMappings(SourceMap sourceMap) {
    Node node = new Node(Token.NEW);
    String sourceFile = "test_file.js";
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition, sourceFile, null);
  }

  /**
   * Add mappings to a source map with prefix mappings
   *
   * @param sourceMap the source map to add mappings to
   */
  private void addMappingsWithPrefixMappings(SourceMap sourceMap) {
    Node node = new Node(Token.NEW);
    String sourceFile = "test_file_old_prefix.js";
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition, sourceFile, null);
  }

  /**
   * Append source map to a file
   *
   * @param sourceMap the source map to append to a file
   * @throws IOException if an error occurs while appending to the file
   */
  private void appendToFile(SourceMap sourceMap) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test_file.js.map");
    String expected = "sourceMappingURL=test_file.js.map";
    String actual = new String(out.toByteArray());
    assertEquals(expected, actual);
  }

  /**
   * Append source map to a file with prefix mappings
   *
   * @param sourceMap the source map to append to a file
   * @throws IOException if an error occurs while appending to the file
   */
  private void appendToFileWithPrefixMappings(SourceMap sourceMap) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test_file_old_prefix.js.map");
    String expected = "sourceMappingURL=test_file_new_prefix.js.map";
    String actual = new String(out.toByteArray());
    assertEquals(expected, actual);
  }

  /**
   * Reset the source map
   *
   * @param sourceMap the source map to reset
   */
  private void reset(SourceMap sourceMap) {
    sourceMap.reset();
  }

  /**
   * Reset the source map with prefix mappings
   *
   * @param sourceMap the source map to reset
   */
  private void resetWithPrefixMappings(SourceMap sourceMap) {
    sourceMap.reset();
    assertEquals(Collections.emptyList(), sourceMap.getPrefixMappings());
  }

  /**
   * Set starting position for the source map
   *
   * @param sourceMap the source map to set starting position for
   */
  private void setStartingPosition(SourceMap sourceMap) {
    sourceMap.setStartingPosition(1, 1);
  }

  /**
   * Set starting position for the source map with prefix mappings
   *
   * @param sourceMap the source map to set starting position for
   */
  private void setStartingPositionWithPrefixMappings(SourceMap sourceMap) {
    sourceMap.setStartingPosition(1, 1);
    assertEquals(Collections.emptyList(), sourceMap.getPrefixMappings());
  }

  /**
   * Validate the source map
   *
   * @param sourceMap the source map to validate
   */
  private void validate(SourceMap sourceMap) {
    sourceMap.validate(true);
  }

  /**
   * Validate the source map with prefix mappings
   *
   * @param sourceMap the source map to validate
   */
  private void validateWithPrefixMappings(SourceMap sourceMap) {
    sourceMap.validate(true);
    assertEquals(Collections.emptyList(), sourceMap.getPrefixMappings());
  }
}