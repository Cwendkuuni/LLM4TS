import org.junit.Assert;
import org.junit.Test;

public class SourceMapTest {

  @Test
  public void testAddMapping() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

    Node node = new Node();
    node.setSourceFileName("test.js");
    node.setLineno(10);
    node.setCharno(5);

    FilePosition outputStartPosition = new FilePosition(20, 7);
    FilePosition outputEndPosition = new FilePosition(25, 12);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Verify that the mapping is added correctly
    // Get the mapping entry from the source map and assert its values
    MappingEntry entry = sourceMap.generator.getMappingEntries().get(0);
    Assert.assertEquals("test.js", entry.getSourceFile());
    Assert.assertEquals(10, entry.getLine());
    Assert.assertEquals(5, entry.getColumn());
    Assert.assertEquals(20, entry.getStartPosition().getLine());
    Assert.assertEquals(7, entry.getStartPosition().getColumn());
    Assert.assertEquals(25, entry.getEndPosition().getLine());
    Assert.assertEquals(12, entry.getEndPosition().getColumn());
  }

  @Test
  public void testFixupSourceLocationWithPrefixMapping() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

    Node node = new Node();
    node.setSourceFileName("src/app/test.js");
    node.setLineno(10);
    node.setCharno(5);

    FilePosition outputStartPosition = new FilePosition(20, 7);
    FilePosition outputEndPosition = new FilePosition(25, 12);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Set prefix mappings for source location fixup
    LocationMapping mapping = new LocationMapping("src/app", "src/main");
    sourceMap.setPrefixMappings(Collections.singletonList(mapping));

    // Verify that the source location is fixed up correctly
    // Get the mapping entry from the source map and assert the fixed source file
    MappingEntry entry = sourceMap.generator.getMappingEntries().get(0);
    Assert.assertEquals("src/main/test.js", entry.getSourceFile());
  }

  @Test
  public void testFixupSourceLocationWithoutPrefixMapping() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

    Node node = new Node();
    node.setSourceFileName("src/app/test.js");
    node.setLineno(10);
    node.setCharno(5);

    FilePosition outputStartPosition = new FilePosition(20, 7);
    FilePosition outputEndPosition = new FilePosition(25, 12);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Verify that the source location remains unchanged
    // Get the mapping entry from the source map and assert the original source file
    MappingEntry entry = sourceMap.generator.getMappingEntries().get(0);
    Assert.assertEquals("src/app/test.js", entry.getSourceFile());
  }

  @Test
  public void testSetStartingPosition() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

    sourceMap.setStartingPosition(5, 10);

    // Verify that the starting position is set correctly
    Assert.assertEquals(5, sourceMap.generator.getStartingPosition().getLine());
    Assert.assertEquals(10, sourceMap.generator.getStartingPosition().getColumn());
  }

  @Test
  public void testSetWrapperPrefix() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

    String prefix = "function() {";
    sourceMap.setWrapperPrefix(prefix);

    // Verify that the wrapper prefix is set correctly
    Assert.assertEquals(prefix, sourceMap.generator.getWrapperPrefix());
  }

  @Test
  public void testValidate() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

    boolean validate = true;
    sourceMap.validate(validate);

    // Verify that the validation flag is set correctly
    Assert.assertEquals(validate, sourceMap.generator.getValidate());
  }

  @Test
  public void testReset() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

    // Add a mapping entry
    Node node = new Node();
    node.setSourceFileName("test.js");
    node.setLineno(10);
    node.setCharno(5);

    FilePosition outputStartPosition = new FilePosition(20, 7);
    FilePosition outputEndPosition = new FilePosition(25, 12);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Reset the source map
    sourceMap.reset();

    // Verify that the source map is reset
    Assert.assertEquals(0, sourceMap.generator.getMappingEntries().size());
    Assert.assertEquals(0, sourceMap.sourceLocationFixupCache.size());
  }

  @Test
  public void testAppendTo() throws IOException {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

    // Add a mapping entry
    Node node = new Node();
    node.setSourceFileName("test.js");
    node.setLineno(10);
    node.setCharno(5);

    FilePosition outputStartPosition = new FilePosition(20, 7);
    FilePosition outputEndPosition = new FilePosition(25, 12);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    StringBuilder out = new StringBuilder();
    String name = "sourceMap.js";
    sourceMap.appendTo(out, name);

    // Verify that the source map is appended correctly
    // Assert the expected output string
    String expectedOutput = "//# sourceMappingURL=sourceMap.js";
    Assert.assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testSetPrefixMappings() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

    LocationMapping mapping = new LocationMapping("src/app", "src/main");
    sourceMap.setPrefixMappings(Collections.singletonList(mapping));

    // Verify that the prefix mappings are set correctly
    Assert.assertEquals(1, sourceMap.prefixMappings.size());
    Assert.assertEquals("src/app", sourceMap.prefixMappings.get(0).prefix);
    Assert.assertEquals("src/main", sourceMap.prefixMappings.get(0).replacement);
  }
}