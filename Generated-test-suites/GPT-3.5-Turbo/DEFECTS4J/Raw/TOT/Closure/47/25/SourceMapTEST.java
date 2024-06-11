import org.junit.Assert;
import org.junit.Test;

public class SourceMapTest {

  @Test
  public void testAddMapping() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
    Node node = new Node(NodeType.SCRIPT);
    node.setSourceFileName("test.js");
    node.setLineno(10);
    node.setCharno(5);

    FilePosition outputStartPosition = new FilePosition(2, 3);
    FilePosition outputEndPosition = new FilePosition(4, 6);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Verify that the mapping is added correctly
    // Use typical use case
    Assert.assertEquals(1, sourceMap.getMappingCount());

    // Use edge case with invalid node line number
    Node invalidNode = new Node(NodeType.SCRIPT);
    invalidNode.setSourceFileName("test.js");
    invalidNode.setLineno(-1);
    invalidNode.setCharno(5);

    sourceMap.addMapping(invalidNode, outputStartPosition, outputEndPosition);

    // Verify that the invalid node mapping is not added
    Assert.assertEquals(1, sourceMap.getMappingCount());
  }

  @Test
  public void testFixupSourceLocation() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());

    // Add prefix mapping
    LocationMapping mapping = new LocationMapping("src/main/js", "src");
    sourceMap.setPrefixMappings(Collections.singletonList(mapping));

    // Test typical use case with matching prefix mapping
    String sourceFile = "src/main/js/test.js";
    String expectedFixedSourceFile = "src/test.js";

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

    Assert.assertEquals(expectedFixedSourceFile, fixedSourceFile);

    // Test edge case with no matching prefix mapping
    String noMappingSourceFile = "src/com/example/test.js";

    String sourceFileAfterNoMapping = sourceMap.fixupSourceLocation(noMappingSourceFile);

    Assert.assertEquals(noMappingSourceFile, sourceFileAfterNoMapping);
  }

  @Test
  public void testAppendTo() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V2.getInstance());

    FilePosition outputStartPosition = new FilePosition(2, 3);
    FilePosition outputEndPosition = new FilePosition(4, 6);
    Node node = new Node(NodeType.SCRIPT);
    node.setSourceFileName("test.js");
    node.setLineno(10);
    node.setCharno(5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    StringBuilder stringBuilder = new StringBuilder();
    String expectedOutput = "output";

    try {
      sourceMap.appendTo(stringBuilder, expectedOutput);
    } catch (IOException e) {
      Assert.fail("IOException occured while appending source map.");
    }

    // Verify that the appended output is correct
    Assert.assertTrue(stringBuilder.toString().contains(expectedOutput));
  }

  @Test
  public void testReset() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());

    FilePosition outputStartPosition = new FilePosition(2, 3);
    FilePosition outputEndPosition = new FilePosition(4, 6);
    Node node = new Node(NodeType.SCRIPT);
    node.setSourceFileName("test.js");
    node.setLineno(10);
    node.setCharno(5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Verify that a mapping is added
    Assert.assertEquals(1, sourceMap.getMappingCount());

    sourceMap.reset();

    // Verify that the mapping is reset
    Assert.assertEquals(0, sourceMap.getMappingCount());
  }

  @Test
  public void testSetStartingPosition() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
    int offsetLine = 5;
    int offsetIndex = 3;

    sourceMap.setStartingPosition(offsetLine, offsetIndex);

    // Verify that the starting position is set correctly
    Assert.assertEquals(offsetLine, sourceMap.getGenerator().getStartingPosition().getLine());
    Assert.assertEquals(offsetIndex, sourceMap.getGenerator().getStartingPosition().getColumn());
  }

  @Test
  public void testSetWrapperPrefix() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
    String prefix = "wrapper_";

    sourceMap.setWrapperPrefix(prefix);

    // Verify that the wrapper prefix is set correctly
    Assert.assertEquals(prefix, sourceMap.getGenerator().getWrapperPrefix());
  }

  @Test
  public void testValidate() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
    boolean validate = true;

    sourceMap.validate(validate);

    // Verify that the validate flag is set correctly
    Assert.assertEquals(validate, sourceMap.getGenerator().shouldValidate());
  }

}