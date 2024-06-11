import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class SourceMapTest {

  @Test
  public void testAddMapping() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    FilePosition start = new FilePosition(0, 0);
    FilePosition end = new FilePosition(1, 1);

    Node node = new Node();

    // Typical use case
    node.setSourceFileName("testFile.js");
    node.setLineno(10);
    node.setCharno(5);
    sourceMap.addMapping(node, start, end);
    // Assert that the mapping was added successfully
    assertEquals(1, sourceMap.getMappingCount());

    // Edge case: empty source file name
    node.setSourceFileName("");
    sourceMap.addMapping(node, start, end);
    // Assert that the mapping was not added
    assertEquals(1, sourceMap.getMappingCount());

    // Edge case: negative line number
    node.setSourceFileName("testFile.js");
    node.setLineno(-5);
    sourceMap.addMapping(node, start, end);
    // Assert that the mapping was not added
    assertEquals(1, sourceMap.getMappingCount());

    // Error scenario: null node
    node = null;
    sourceMap.addMapping(node, start, end);
    // Assert that the mapping was not added
    assertEquals(1, sourceMap.getMappingCount());
  }

  @Test
  public void testFixupSourceLocation() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

    // Typical use case: matching prefix
    sourceMap.setPrefixMappings(Arrays.asList(
      new SourceMap.LocationMapping("src/", "res/"),
      new SourceMap.LocationMapping("test/", "test/res/")
    ));
    String fixedLocation = sourceMap.fixupSourceLocation("src/file.js");
    // Assert that the source file location was fixed correctly
    assertEquals("res/file.js", fixedLocation);

    // Typical use case: no matching prefix
    sourceMap.setPrefixMappings(Arrays.asList(
      new SourceMap.LocationMapping("src/", "res/"),
      new SourceMap.LocationMapping("test/", "test/res/")
    ));
    fixedLocation = sourceMap.fixupSourceLocation("src/file2.js");
    // Assert that the source file location was not changed
    assertEquals("src/file2.js", fixedLocation);

    // Edge case: empty prefix mappings
    sourceMap.setPrefixMappings(Collections.emptyList());
    fixedLocation = sourceMap.fixupSourceLocation("src/file3.js");
    // Assert that the source file location was not changed
    assertEquals("src/file3.js", fixedLocation);

    // Error scenario: null source file location
    sourceMap.setPrefixMappings(Arrays.asList(
      new SourceMap.LocationMapping("src/", "res/"),
      new SourceMap.LocationMapping("test/", "test/res/")
    ));
    fixedLocation = sourceMap.fixupSourceLocation(null);
    // Assert that the source file location was not changed
    assertEquals(null, fixedLocation);
  }

  @Test
  public void testReset() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V3.getInstance());
    FilePosition start = new FilePosition(0, 0);
    FilePosition end = new FilePosition(1, 1);

    Node node = new Node();
    node.setSourceFileName("testFile.js");
    node.setLineno(10);
    node.setCharno(5);

    // Add a mapping
    sourceMap.addMapping(node, start, end);
    // Assert that the mapping count is not zero
    assertEquals(1, sourceMap.getMappingCount());

    // Reset the source map
    sourceMap.reset();
    // Assert that the mapping count is zero
    assertEquals(0, sourceMap.getMappingCount());
  }

  @Test
  public void testSetStartingPosition() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());

    // Typical use case
    int offsetLine = 5;
    int offsetIndex = 10;
    sourceMap.setStartingPosition(offsetLine, offsetIndex);
    // Assert that the starting position was set correctly
    assertEquals(5, sourceMap.getStartingPositionLine());
    assertEquals(10, sourceMap.getStartingPositionIndex());

    // Edge case: negative offset line
    offsetLine = -5;
    sourceMap.setStartingPosition(offsetLine, offsetIndex);
    // Assert that the starting position was not changed
    assertEquals(5, sourceMap.getStartingPositionLine());
    assertEquals(10, sourceMap.getStartingPositionIndex());
  }

  @Test
  public void testSetWrapperPrefix() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V2.getInstance());

    // Typical use case
    String prefix = "PREFIX_";
    sourceMap.setWrapperPrefix(prefix);
    // Assert that the wrapper prefix was set correctly
    assertEquals("PREFIX_", sourceMap.getWrapperPrefix());

    // Edge case: empty prefix
    prefix = "";
    sourceMap.setWrapperPrefix(prefix);
    // Assert that the wrapper prefix was not changed
    assertEquals("PREFIX_", sourceMap.getWrapperPrefix());

    // Error scenario: null prefix
    prefix = null;
    sourceMap.setWrapperPrefix(prefix);
    // Assert that the wrapper prefix was not changed
    assertEquals("PREFIX_", sourceMap.getWrapperPrefix());
  }

  @Test
  public void testValidate() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

    // Typical use case: validate enabled
    boolean validate = true;
    sourceMap.validate(validate);
    // Assert that validating was enabled
    assertTrue(sourceMap.isValidating());

    // Typical use case: validate disabled
    validate = false;
    sourceMap.validate(validate);
    // Assert that validating was disabled
    assertFalse(sourceMap.isValidating());
  }

  @Test
  public void testAppendTo() throws IOException {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V3.getInstance());
    FilePosition start = new FilePosition(0, 0);
    FilePosition end = new FilePosition(1, 1);

    Node node = new Node();
    node.setSourceFileName("testFile.js");
    node.setLineno(10);
    node.setCharno(5);

    // Add a mapping
    sourceMap.addMapping(node, start, end);

    // Typical use case
    StringBuilder sb = new StringBuilder();
    String name = "testSourceMap.js";
    sourceMap.appendTo(sb, name);
    // Assert that the source map was appended to the StringBuilder correctly
    assertTrue(sb.toString().contains("sourceMappingURL=" + name));

    // Error scenario: null Appendable
    Appendable out = null;
    assertThrows(IOException.class, () -> {
      sourceMap.appendTo(out, name);
    });
  }
}