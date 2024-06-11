import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.rhino.Node;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Unit tests for the SourceMap class.
 */
public class SourceMapTest {

  private SourceMap sourceMap;
  private List<SourceMap.LocationMapping> prefixMappings;

  @Before
  public void setup() {
    // Create a new SourceMap instance with default settings.
    Format format = SourceMap.Format.DEFAULT;
    SourceMapGenerator generator = format.getInstance();
    sourceMap = new SourceMap(generator);

    // Set up some prefix mappings for testing.
    prefixMappings = Lists.newArrayList(
        new SourceMap.LocationMapping("/test/oldPath/A.js", "/test/newPath/A.js"),
        new SourceMap.LocationMapping("/test/oldPath/B.js", "/test/newPath/B.js")
    );
  }

  /**
   * Test that adding a mapping for a Node with a valid source file and line number
   * results in the mapping being added to the generator.
   */
  @Test
  public void testAddMapping() {
    String sourceFile = "test/test.js";
    Node node = new Node();
    node.setSourceFileName(sourceFile);
    node.setLineno(15);

    // Add a mapping for the node.
    FilePosition outputStartPosition = new FilePosition(10, 20);
    FilePosition outputEndPosition = new FilePosition(20, 30);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Verify that the mapping was added to the generator.
    String expectedMapping = String.format(
        "%s:%d:%d-%d:%d\t%s\t%s\n", sourceFile, node.getLineno(), node.getCharno(),
        outputStartPosition.getLine(), outputStartPosition.getColumn(),
        node.getProp(Node.ORIGINALNAME_PROP), outputEndPosition.getLine(),
        outputEndPosition.getColumn());
    String actualMapping = sourceMap.generator.getSourceContent().toString();
    assertTrue("Expected mapping not found in generator output.", actualMapping.contains(expectedMapping));
  }

  /**
   * Test that adding a mapping for a Node with an invalid source file or line number
   * results in the mapping not being added to the generator.
   */
  @Test
  public void testInvalidMapping() {
    Node node = new Node();

    // Add a mapping for the node with an invalid source file.
    String invalidSourceFile = null;
    FilePosition outputStartPosition = new FilePosition(10, 20);
    FilePosition outputEndPosition = new FilePosition(20, 30);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Verify that the mapping was not added to the generator.
    String actualMapping = sourceMap.generator.getSourceContent().toString();
    assertFalse("Expected mapping not to be found in generator output.", actualMapping.contains(
        String.format("%s:%d:%d-%d:%d\t%s\t%s\n", invalidSourceFile, node.getLineno(), node.getCharno(),
            outputStartPosition.getLine(), outputStartPosition.getColumn(),
            node.getProp(Node.ORIGINALNAME_PROP), outputEndPosition.getLine(),
            outputEndPosition.getColumn())));

    // Add a mapping for the node with an invalid line number.
    sourceMap.reset();
    node.setSourceFileName("test.js");
    node.setLineno(-1);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Verify that the mapping was not added to the generator.
    actualMapping = sourceMap.generator.getSourceContent().toString();
    assertFalse("Expected mapping not to be found in generator output.", actualMapping.contains(
        String.format("%s:%d:%d-%d:%d\t%s\t%s\n", "test.js", node.getLineno(), node.getCharno(),
            outputStartPosition.getLine(), outputStartPosition.getColumn(),
            node.getProp(Node.ORIGINALNAME_PROP), outputEndPosition.getLine(),
            outputEndPosition.getColumn())));
  }

  /**
   * Test that setting prefix mappings allows the source file location to be
   * remapped during the generation of source map data.
   */
  @Test
  public void testPrefixMappings() {
    // Set up the source map with prefix mappings.
    sourceMap.setPrefixMappings(prefixMappings);

    // Add a mapping for a Node with a source file that matches one of the prefix mappings.
    String sourceFileWithPrefix = "/test/oldPath/A.js";
    Node nodeWithPrefix = new Node();
    nodeWithPrefix.setSourceFileName(sourceFileWithPrefix);
    nodeWithPrefix.setLineno(15);

    FilePosition outputStartPosition = new FilePosition(10, 20);
    FilePosition outputEndPosition = new FilePosition(20, 30);
    sourceMap.addMapping(nodeWithPrefix, outputStartPosition, outputEndPosition);

    // Verify that the remapped source file location is used in the mapping.
    String expectedMapping = String.format(
        "%s:%d:%d-%d:%d\t%s\t%s\n", "/test/newPath/A.js", nodeWithPrefix.getLineno(), nodeWithPrefix.getCharno(),
        outputStartPosition.getLine(), outputStartPosition.getColumn(),
        nodeWithPrefix.getProp(Node.ORIGINALNAME_PROP), outputEndPosition.getLine(),
        outputEndPosition.getColumn());
    String actualMapping = sourceMap.generator.getSourceContent().toString();
    assertTrue("Expected mapping not found in generator output.", actualMapping.contains(expectedMapping));
  }

  /**
   * Test that appending the SourceMap to an output stream generates valid source map data.
   */
  @Test
  public void testAppendTo() throws IOException {
    String name = "test.js.map";
    sourceMap.appendTo(System.out, name);

    // Verify that the source map data was written to the output stream.
    String expectedContent = "..."; // The actual expected content would depend on the prefix mappings and test cases.
    String actualContent = new String(System.out.array());
    assertEquals("Expected source map content not equal to actual content.", expectedContent, actualContent);
  }
}