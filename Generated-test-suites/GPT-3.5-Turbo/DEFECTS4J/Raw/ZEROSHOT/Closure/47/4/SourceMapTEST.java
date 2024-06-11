import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import com.google.javascript.rhino.Node;

public class SourceMapTest {

  @Test
  public void testAddMapping() {
    // create a dummy SourceMap and test adding a mapping
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
    Node node = new Node();
    node.setSourceFileName("testFile.js");
    node.setLineno(1);
    node.setCharno(0);
    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(0, 10);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    // Assert that the mapping was added successfully
    assertEquals(1, sourceMap.generator.getMappings().size());
  }

  @Test
  public void testFixupSourceLocation() {
    // create a dummy SourceMap and test fixing up a source location
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
    sourceMap.setPrefixMappings(new ArrayList<>());
    String sourceFile = "testFile.js";
    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    // Assert that the fixed source location is the same as the original source location
    assertEquals(sourceFile, fixed);
  }

  @Test
  public void testAppendTo() {
    // create a dummy SourceMap and test appending to an Appendable object
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
    Node node = new Node();
    node.setSourceFileName("testFile.js");
    node.setLineno(1);
    node.setCharno(0);
    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(0, 10);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    StringBuilder sb = new StringBuilder();
    try {
      sourceMap.appendTo(sb, "testSourceMap");
    } catch (Exception e) {
      fail("Exception thrown while appending to Appendable object: " + e.getMessage());
    }
    // Assert that the Appendable object is not empty
    assertNotEquals(0, sb.length());
  }

  @Test
  public void testReset() {
    // create a dummy SourceMap and test resetting it
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
    Node node = new Node();
    node.setSourceFileName("testFile.js");
    node.setLineno(1);
    node.setCharno(0);
    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(0, 10);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMap.reset();
    // Assert that the SourceMap has been reset and is empty
    assertEquals(0, sourceMap.generator.getMappings().size());
    assertEquals(0, sourceMap.sourceLocationFixupCache.size());
  }

  @Test
  public void testSetStartingPosition() {
    // create a dummy SourceMap and test setting the starting position
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
    int offsetLine = 10;
    int offsetIndex = 5;
    sourceMap.setStartingPosition(offsetLine, offsetIndex);
    // Assert that the starting position of the SourceMap has been set correctly
    assertEquals(offsetLine, sourceMap.generator.getStartingPosition().getLine());
    assertEquals(offsetIndex, sourceMap.generator.getStartingPosition().getColumn());
  }

  @Test
  public void testSetWrapperPrefix() {
    // create a dummy SourceMap and test setting the wrapper prefix
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
    String prefix = "wrapperPrefix";
    sourceMap.setWrapperPrefix(prefix);
    // Assert that the wrapper prefix has been set correctly
    assertEquals(prefix, sourceMap.generator.getWrapperPrefix());
  }

  @Test
  public void testValidate() {
    // create a dummy SourceMap and test setting the validate flag
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
    boolean validate = true;
    sourceMap.validate(validate);
    // Assert that the validate flag has been set correctly
    assertEquals(validate, sourceMap.generator.hasValidation());
  }

  @Test
  public void testSetPrefixMappings() {
    // create a dummy SourceMap and test setting the prefix mappings
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
    List<SourceMap.LocationMapping> prefixMappings = new ArrayList<>();
    sourceMap.setPrefixMappings(prefixMappings);
    // Assert that the prefix mappings have been set correctly
    assertEquals(prefixMappings, sourceMap.prefixMappings);
  }
}