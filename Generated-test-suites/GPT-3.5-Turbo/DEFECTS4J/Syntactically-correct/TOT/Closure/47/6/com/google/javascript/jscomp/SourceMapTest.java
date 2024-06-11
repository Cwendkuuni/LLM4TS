import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceMapTest {

  private SourceMap sourceMap;

  @Before
  public void setUp() {
    sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
  }

  @Test
  public void testAddMapping() {
    // typical use case
    Node node = new Node();
    node.setLineno(10);
    node.setCharno(5);
    node.setSourceFileName("file1.js");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    // verify that mapping was added
    // ...
  }

  @Test
  public void testAddMapping_nodeWithoutSourceFile() {
    // edge case: node without source file
    Node node = new Node();
    node.setLineno(10);
    node.setCharno(5);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    // verify that mapping was not added
    // ...
  }

  @Test
  public void testAddMapping_nodeWithNegativeLineNumber() {
    // edge case: node with negative line number
    Node node = new Node();
    node.setLineno(-1);
    node.setCharno(5);
    node.setSourceFileName("file1.js");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    // verify that mapping was not added
    // ...
  }

  @Test
  public void testAddMapping_nodeWithOriginalName() {
    // typical use case: node with original name
    Node node = new Node();
    node.setLineno(10);
    node.setCharno(5);
    node.setSourceFileName("file1.js");
    node.putProp(Node.ORIGINALNAME_PROP, "original_name");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    // verify that mapping was added with original name
    // ...
  }

  @Test
  public void testFixupSourceLocation_noPrefixMappings() {
    // edge case: no prefix mappings
    String sourceFile = "file1.js";
    String expectedFixed = "file1.js";

    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals(expectedFixed, fixed);
  }

  @Test
  public void testFixupSourceLocation_matchingPrefix() {
    // typical use case: matching prefix
    sourceMap.setPrefixMappings(new ArrayList<LocationMapping>() {{
      add(new LocationMapping("path/to/old/", "path/to/new/"));
    }});

    String sourceFile = "path/to/old/file1.js";
    String expectedFixed = "path/to/new/file1.js";

    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals(expectedFixed, fixed);
  }

  @Test
  public void testFixupSourceLocation_noMatchingPrefix() {
    // edge case: no matching prefix
    sourceMap.setPrefixMappings(new ArrayList<LocationMapping>() {{
      add(new LocationMapping("path/to/old/", "path/to/new/"));
    }});

    String sourceFile = "path/to/other/file.js";
    String expectedFixed = "path/to/other/file.js";

    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals(expectedFixed, fixed);
  }

  @Test
  public void testSetWrapperPrefix() {
    // typical use case
    String prefix = "/** This is a wrapper prefix */";

    sourceMap.setWrapperPrefix(prefix);
    // verify that the wrapper prefix is set correctly
    // ...
  }

  @Test
  public void testSetStartingPosition() {
    // typical use case
    int offsetLine = 5;
    int offsetIndex = 2;

    sourceMap.setStartingPosition(offsetLine, offsetIndex);
    // verify that the starting position is set correctly
    // ...
  }

  @Test
  public void testValidate() {
    // typical use case
    boolean validate = true;

    sourceMap.validate(validate);
    // verify that the validation flag is set correctly
    // ...
  }

  @Test
  public void testSetPrefixMappings() {
    // typical use case
    List<LocationMapping> prefixMappings = new ArrayList<LocationMapping>() {{
      add(new LocationMapping("path/to/old/", "path/to/new/"));
    }};

    sourceMap.setPrefixMappings(prefixMappings);
    // verify that the prefix mappings are set correctly
    // ...
  }

}