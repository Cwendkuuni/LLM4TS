import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class SourceMapTest {

  @Test
  public void testAddMapping() {
    // Create a mock Node object
    Node node = Mockito.mock(Node.class);

    // Create mock FilePosition objects
    FilePosition outputStartPosition = Mockito.mock(FilePosition.class);
    FilePosition outputEndPosition = Mockito.mock(FilePosition.class);

    // Create a mock SourceMapGenerator object
    SourceMapGenerator generator = Mockito.mock(SourceMapGenerator.class);

    // Create a mock LocationMapping object
    LocationMapping locationMapping = Mockito.mock(LocationMapping.class);

    // Set up the expected values
    String sourceFile = "sourceFile";
    int lineNumber = 5;
    int charNumber = 10;
    String originalName = "originalName";

    // Set up the method calls and return values
    Mockito.when(node.getSourceFileName()).thenReturn(sourceFile);
    Mockito.when(node.getLineno()).thenReturn(lineNumber);
    Mockito.when(node.getCharno()).thenReturn(charNumber);
    Mockito.when(node.getProp(Node.ORIGINALNAME_PROP)).thenReturn(originalName);
    Mockito.when(generator.addMapping(
      Mockito.eq(sourceFile), Mockito.eq(originalName),
      Mockito.eq(new FilePosition(lineNumber, charNumber)),
      Mockito.eq(outputStartPosition), Mockito.eq(outputEndPosition))).thenReturn(true);

    // Create a new SourceMap object and add the mapping
    SourceMap sourceMap = new SourceMap(generator);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Verify that the generator's addMapping method was called with the expected arguments
    Mockito.verify(generator).addMapping(
      Mockito.eq(sourceFile), Mockito.eq(originalName),
      Mockito.eq(new FilePosition(lineNumber, charNumber)),
      Mockito.eq(outputStartPosition), Mockito.eq(outputEndPosition));
  }

  @Test
  public void testFixupSourceLocationWithPrefixMappings() {
    // Create a new SourceMap object
    SourceMap sourceMap = new SourceMap(null);

    // Create a list of LocationMapping objects
    List<LocationMapping> prefixMappings = new ArrayList<>();
    prefixMappings.add(new LocationMapping("prefix1", "replacement1"));
    prefixMappings.add(new LocationMapping("prefix2", "replacement2"));

    // Set the prefixMappings field in the SourceMap object
    sourceMap.setPrefixMappings(prefixMappings);

    // Set up the expected values
    String sourceFile = "prefix1/file.js";
    String fixed = "replacement1/file.js";

    // Call the fixupSourceLocation method
    String result = sourceMap.fixupSourceLocation(sourceFile);

    // Verify that the result is equal to the expected fixed value
    assertEquals(fixed, result);
  }

  @Test
  public void testFixupSourceLocationWithNoPrefixMappings() {
    // Create a new SourceMap object
    SourceMap sourceMap = new SourceMap(null);

    // Create an empty list of LocationMapping objects
    List<LocationMapping> prefixMappings = new ArrayList<>();

    // Set the prefixMappings field in the SourceMap object
    sourceMap.setPrefixMappings(prefixMappings);

    // Set up the expected values
    String sourceFile = "file.js";

    // Call the fixupSourceLocation method
    String result = sourceMap.fixupSourceLocation(sourceFile);

    // Verify that the result is equal to the expected sourceFile value
    assertEquals(sourceFile, result);
  }

  @Test
  public void testAppendTo() {
    // Create a mock Appendable object
    Appendable out = Mockito.mock(Appendable.class);

    // Create a mock SourceMapGenerator object
    SourceMapGenerator generator = Mockito.mock(SourceMapGenerator.class);

    // Set up the expected values
    String name = "name";

    // Set up the method calls and return values
    Mockito.when(generator.appendTo(Mockito.eq(out), Mockito.eq(name))).thenReturn(true);

    // Create a new SourceMap object and call the appendTo method
    SourceMap sourceMap = new SourceMap(generator);
    try {
      sourceMap.appendTo(out, name);
    } catch (IOException e) {
      fail("IOException occurred");
    }

    // Verify that the generator's appendTo method was called with the expected arguments
    Mockito.verify(generator).appendTo(Mockito.eq(out), Mockito.eq(name));
  }

  @Test
  public void testReset() {
    // Create a mock SourceMapGenerator object
    SourceMapGenerator generator = Mockito.mock(SourceMapGenerator.class);

    // Create a new SourceMap object and call the reset method
    SourceMap sourceMap = new SourceMap(generator);
    sourceMap.reset();

    // Verify that the generator's reset method was called
    Mockito.verify(generator).reset();
  }

  @Test
  public void testSetStartingPosition() {
    // Create a mock SourceMapGenerator object
    SourceMapGenerator generator = Mockito.mock(SourceMapGenerator.class);

    // Set up the expected values
    int offsetLine = 5;
    int offsetIndex = 10;

    // Create a new SourceMap object and call the setStartingPosition method
    SourceMap sourceMap = new SourceMap(generator);
    sourceMap.setStartingPosition(offsetLine, offsetIndex);

    // Verify that the generator's setStartingPosition method was called with the expected arguments
    Mockito.verify(generator).setStartingPosition(offsetLine, offsetIndex);
  }

  @Test
  public void testSetWrapperPrefix() {
    // Create a mock SourceMapGenerator object
    SourceMapGenerator generator = Mockito.mock(SourceMapGenerator.class);

    // Set up the expected value
    String prefix = "prefix";

    // Create a new SourceMap object and call the setWrapperPrefix method
    SourceMap sourceMap = new SourceMap(generator);
    sourceMap.setWrapperPrefix(prefix);

    // Verify that the generator's setWrapperPrefix method was called with the expected prefix argument
    Mockito.verify(generator).setWrapperPrefix(prefix);
  }

  @Test
  public void testValidate() {
    // Create a mock SourceMapGenerator object
    SourceMapGenerator generator = Mockito.mock(SourceMapGenerator.class);

    // Create a new SourceMap object and call the validate method
    SourceMap sourceMap = new SourceMap(generator);
    sourceMap.validate(true);

    // Verify that the generator's validate method was called with the expected validate argument
    Mockito.verify(generator).validate(true);
  }
}