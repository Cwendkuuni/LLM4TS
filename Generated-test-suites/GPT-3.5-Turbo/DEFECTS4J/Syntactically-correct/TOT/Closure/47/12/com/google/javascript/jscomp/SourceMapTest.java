import org.junit.Test;
import static org.junit.Assert.*;

public class SourceMapTest {

  @Test
  public void testAddMapping_typicalCase() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    Node node = new Node.Builder()
                  .setSourceFileName("test.js")
                  .setLineno(10)
                  .setCharno(5)
                  .build();
    FilePosition outputStartPosition = new FilePosition(20, 5);
    FilePosition outputEndPosition = new FilePosition(20, 12);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Assert that the mapping is added correctly
    // Check if the source file, original name, and positions are correct
    assertEquals("test.js", sourceMap.generator.sources.get(0));
    assertNull(node.getProp(Node.ORIGINALNAME_PROP));
    assertEquals(10, sourceMap.generator.mappings.get(0).getOriginalLine());
    assertEquals(5, sourceMap.generator.mappings.get(0).getOriginalColumn());
    assertEquals(20, sourceMap.generator.mappings.get(0).getGeneratedLine());
    assertEquals(5, sourceMap.generator.mappings.get(0).getGeneratedColumn());
    assertEquals(20, sourceMap.generator.mappings.get(0).getEndGeneratedLine());
    assertEquals(12, sourceMap.generator.mappings.get(0).getEndGeneratedColumn());
  }

  @Test
  public void testAddMapping_missingSourceFile() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    Node node = new Node.Builder()
                  .setSourceFileName(null)
                  .setLineno(10)
                  .setCharno(5)
                  .build();
    FilePosition outputStartPosition = new FilePosition(20, 5);
    FilePosition outputEndPosition = new FilePosition(20, 12);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Assert that no mapping is added when the source file is missing
    assertTrue(sourceMap.generator.mappings.isEmpty());
  }

  @Test
  public void testAddMapping_negativeLineNo() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    Node node = new Node.Builder()
                  .setSourceFileName("test.js")
                  .setLineno(-1)
                  .setCharno(5)
                  .build();
    FilePosition outputStartPosition = new FilePosition(20, 5);
    FilePosition outputEndPosition = new FilePosition(20, 12);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Assert that no mapping is added when the line number is negative
    assertTrue(sourceMap.generator.mappings.isEmpty());
  }

  @Test
  public void testAddMapping_withoutOriginalName() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    Node node = new Node.Builder()
                  .setSourceFileName("test.js")
                  .setLineno(10)
                  .setCharno(5)
                  .build();
    FilePosition outputStartPosition = new FilePosition(20, 5);
    FilePosition outputEndPosition = new FilePosition(20, 12);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Assert that the original name is null
    assertNull(node.getProp(Node.ORIGINALNAME_PROP));
  }

  @Test
  public void testAddMapping_withOriginalName() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    Node node = new Node.Builder()
                  .setSourceFileName("test.js")
                  .setLineno(10)
                  .setCharno(5)
                  .build();
    node.putProp(Node.ORIGINALNAME_PROP, "originalName");
    FilePosition outputStartPosition = new FilePosition(20, 5);
    FilePosition outputEndPosition = new FilePosition(20, 12);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Assert that the original name is set correctly
    assertEquals("originalName", node.getProp(Node.ORIGINALNAME_PROP));
  }

  @Test
  public void testFixupSourceLocation_noMappings() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    String sourceFile = "test.js";

    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    // Assert that the source file is not modified when there are no mappings
    assertEquals("test.js", fixed);
  }

  @Test
  public void testFixupSourceLocation_withMappings() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    String sourceFile = "/original/test.js";
    List<LocationMapping> prefixMappings = new ArrayList<>();
    prefixMappings.add(new LocationMapping("/original/", "/remapped/"));
    sourceMap.setPrefixMappings(prefixMappings);

    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    // Assert that the source file is remapped correctly based on the prefix mappings
    assertEquals("/remapped/test.js", fixed);
  }

  @Test
  public void testFixupSourceLocation_noMatchingMapping() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    String sourceFile = "test.js";
    List<LocationMapping> prefixMappings = new ArrayList<>();
    prefixMappings.add(new LocationMapping("/original/", "/remapped/"));
    sourceMap.setPrefixMappings(prefixMappings);

    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    // Assert that the source file is not modified when there is no matching mapping
    assertEquals("test.js", fixed);
  }

  @Test
  public void testAppendTo_typicalCase() throws IOException {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    sourceMap.generator.sources.add("test.js");
    sourceMap.generator.mappings.add(new Mapping("test.js", null, 10, 5, 20, 5, 20, 12));
    StringBuilder out = new StringBuilder();

    sourceMap.appendTo(out, "test.js.map");

    // Assert that the source map is appended to the string builder correctly
    assertEquals("{\"version\": 3,\"file\":\"test.js\",\"lineCount\":0,\"mappings\":[[0,10,5,20,5,20]]}", out.toString());
  }

  @Test
  public void testReset() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    sourceMap.generator.sources.add("test.js");
    sourceMap.generator.mappings.add(new Mapping("test.js", null, 10, 5, 20, 5, 20, 12));
    sourceMap.sourceLocationFixupCache.put("test.js", "modified.js");

    sourceMap.reset();

    // Assert that the source map and fixup cache are cleared
    assertTrue(sourceMap.generator.sources.isEmpty());
    assertTrue(sourceMap.generator.mappings.isEmpty());
    assertTrue(sourceMap.sourceLocationFixupCache.isEmpty());
  }

  @Test
  public void testSetStartingPosition() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());

    sourceMap.setStartingPosition(5, 2);

    // Assert that the starting position is set correctly
    assertEquals(5, sourceMap.generator.startingLine);
    assertEquals(2, sourceMap.generator.startingColumn);
  }

  @Test
  public void testSetWrapperPrefix() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    String prefix = "myprefix/";

    sourceMap.setWrapperPrefix(prefix);

    // Assert that the wrapper prefix is set correctly
    assertEquals(prefix, sourceMap.generator.wrapperPrefix);
  }

  @Test
  public void testValidate() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    boolean validate = true;

    sourceMap.validate(validate);

    // Assert that the validate flag is set correctly
    assertEquals(validate, sourceMap.generator.validate);
  }

  @Test
  public void testSetPrefixMappings_emptyList() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    List<LocationMapping> prefixMappings = new ArrayList<>();

    sourceMap.setPrefixMappings(prefixMappings);

    // Assert that the prefix mappings are set correctly
    assertTrue(sourceMap.prefixMappings.isEmpty());
  }

  @Test
  public void testSetPrefixMappings_nonEmptyList() {
    SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
    List<LocationMapping> prefixMappings = new ArrayList<>();
    prefixMappings.add(new LocationMapping("/original/", "/remapped/"));

    sourceMap.setPrefixMappings(prefixMappings);

    // Assert that the prefix mappings are set correctly
    assertEquals(prefixMappings, sourceMap.prefixMappings);
  }
}