import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SourceMapTest {
  
  private SourceMap sourceMap;
  
  @Before
  public void setUp() {
    sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
  }
  
  @Test
  public void testAddMapping_validNodeAndPositions_mappingAddedSuccessfully() {
    Node node = new Node("test");
    node.setSourceFileName("file.js");
    node.setLineno(1);
    node.setCharno(0);
    
    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(1, 1);
    
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    
    // Check if the mapping is added correctly
    // TODO: Assert mapping
  }
  
  @Test
  public void testAddMapping_invalidNodeNodeWithNoSourceFileName_mappingNotAdded() {
    Node node = new Node("test");
    node.setLineno(1);
    node.setCharno(0);
    
    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(1, 1);
    
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    
    // Check if the mapping is not added
    // TODO: Assert mapping is not added
  }
  
  @Test
  public void testAddMapping_invalidNodeNodeWithNegativeLineNumber_mappingNotAdded() {
    Node node = new Node("test");
    node.setSourceFileName("file.js");
    node.setLineno(-1);
    node.setCharno(0);
    
    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(1, 1);
    
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    
    // Check if the mapping is not added
    // TODO: Assert mapping is not added
  }
  
  @Test
  public void testFixupSourceLocation_noMappings_sourceFileRemainsTheSame() {
    String sourceFile = "file.js";
    
    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    
    assertEquals(sourceFile, fixed);
  }
  
  @Test
  public void testFixupSourceLocation_matchingMapping_sourceFileIsFixedUp() {
    String sourceFile = "path/file.js";
    String prefix = "path/";
    String replacement = "src/";
    
    sourceMap.setPrefixMappings(List.of(new SourceMap.LocationMapping(prefix, replacement)));
    
    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    
    assertEquals("src/file.js", fixed);
  }
  
  @Test
  public void testFixupSourceLocation_multipleMappingsFirstMatching_sourceFileIsFixedUp() {
    String sourceFile = "path/file.js";
    String prefix1 = "path/";
    String replacement1 = "src/";
    String prefix2 = "file.";
    String replacement2 = "resource.";
    
    sourceMap.setPrefixMappings(List.of(
        new SourceMap.LocationMapping(prefix1, replacement1),
        new SourceMap.LocationMapping(prefix2, replacement2)
    ));
    
    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    
    assertEquals("src/file.js", fixed);
  }
  
  @Test
  public void testFixupSourceLocation_multipleMappingsNoMatching_sourceFileRemainsTheSame() {
    String sourceFile = "path/file.js";
    String prefix1 = "folder/";
    String replacement1 = "src/";
    String prefix2 = "dir.";
    String replacement2 = "resource.";
    
    sourceMap.setPrefixMappings(List.of(
        new SourceMap.LocationMapping(prefix1, replacement1),
        new SourceMap.LocationMapping(prefix2, replacement2)
    ));
    
    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    
    assertEquals(sourceFile, fixed);
  }
  
  @Test
  public void testAppendTo_validAppendableAndName_appendedSuccessfully() throws IOException {
    StringBuilder out = new StringBuilder();
    String name = "test";
    
    sourceMap.appendTo(out, name);
    
    // TODO: Assert that the source map is appended to out with the given name
  }
  
  @Test
  public void testReset_sourceMapReset_successfullyReset() {
    sourceMap.reset();
    
    // TODO: Assert that the source map is reset
  }
  
  @Test
  public void testSetStartingPosition_validOffsetLineAndIndex_startingPositionSetSuccessfully() {
    int offsetLine = 1;
    int offsetIndex = 0;
    
    sourceMap.setStartingPosition(offsetLine, offsetIndex);
    
    // TODO: Assert that the starting position is set correctly
  }
  
  @Test
  public void testSetWrapperPrefix_validPrefix_prefixSetSuccessfully() {
    String prefix = "test";
    
    sourceMap.setWrapperPrefix(prefix);
    
    // TODO: Assert that the wrapper prefix is set correctly
  }
  
  @Test
  public void testValidate_true_validateSetSuccessfully() {
    boolean validate = true;
    
    sourceMap.validate(validate);
    
    // TODO: Assert that the validate flag is set correctly
  }
}