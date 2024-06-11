import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SourceMapTest {

  private SourceMap sourceMap;

  @Before
  public void setup() {
    sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
  }

  @Test
  public void testAddMapping_whenSourceFileAndLineAreValid_shouldAddMapping() {
    Node node = new Node(null);
    node.setSourceFileName("test.js");
    node.setLineno(5);

    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(1, 1);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Assert the mapping is added correctly
    // ...
  }

  @Test
  public void testAddMapping_whenSourceFileIsNull_shouldNotAddMapping() {
    Node node = new Node(null);
    node.setLineno(5);

    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(1, 1);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Assert that no mapping is added
    // ...
  }

  // Add more test cases for each method...

  @Test
  public void testFixupSourceLocation_withPrefixMappings_shouldFixupSourceFile() {
    LocationMapping mapping = new LocationMapping("src/", "test/");
    sourceMap.setPrefixMappings(Collections.singletonList(mapping));

    String sourceFile = "src/example.js";
    String fixedFile = sourceMap.fixupSourceLocation(sourceFile);

    // Assert that the source file is fixed correctly
    // ...
  }

  @Test
  public void testFixupSourceLocation_withNoPrefixMappings_shouldReturnOriginalSourceFile() {
    String sourceFile = "src/example.js";
    String fixedFile = sourceMap.fixupSourceLocation(sourceFile);

    // Assert that the source file remains unchanged
    // ...
  }

  // Add more test cases for each method...

}