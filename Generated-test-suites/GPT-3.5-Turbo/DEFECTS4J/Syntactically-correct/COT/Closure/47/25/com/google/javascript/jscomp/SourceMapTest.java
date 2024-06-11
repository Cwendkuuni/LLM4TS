
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.debugging.sourcemap.FilePosition;

public class SourceMapTest {
  private SourceMap sourceMap;

  @Before
  public void setUp() {
    sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
  }

  @Test
  public void testAddMapping() {
    Node node = new Node();
    node.setSourceFileName("test.js");
    node.setLineno(1);
    node.setCharno(0);
    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(10, 10);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    // Assert something
  }

  @Test
  public void testFixupSourceLocation_NoPrefixMappings() {
    String sourceFile = "File.js";
    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals(sourceFile, fixed);
  }

  @Test
  public void testFixupSourceLocation_WithPrefixMappings() {
    sourceMap.setPrefixMappings(Arrays.asList(
      new SourceMap.LocationMapping("old_prefix/", "new_prefix/"),
      new SourceMap.LocationMapping("old_prefix2/", "new_prefix2/")
    ));
    String sourceFile = "old_prefix/test.js";
    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("new_prefix/test.js", fixed);
  }

  // Add additional test cases for other public methods
}

