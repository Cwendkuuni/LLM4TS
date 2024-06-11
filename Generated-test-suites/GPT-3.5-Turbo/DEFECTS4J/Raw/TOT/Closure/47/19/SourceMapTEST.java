import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SourceMapTest {
  
  private SourceMap sourceMap;
  
  @Before
  public void setUp() {
    sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
  }
  
  @After
  public void tearDown() {
    sourceMap.reset();
  }
  
  @Test
  public void testAddMapping_NormalInput_Success() {
    Node node = new Node("TestNode", 1, 0);
    FilePosition outputStartPosition = new FilePosition(1, 0);
    FilePosition outputEndPosition = new FilePosition(1, 5);
    
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    
    // TODO: Assert the mapping is correctly added to the generator
  }
  
  @Test
  public void testAddMapping_NullSourceFile_InvalidMappingIgnored() {
    Node node = new Node("TestNode", 1, 0);
    FilePosition outputStartPosition = new FilePosition(1, 0);
    FilePosition outputEndPosition = new FilePosition(1, 5);
    
    // Set source file to null
    node.setSourceFileName(null);
    
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    
    // TODO: Assert the mapping is not added to the generator
  }
  
  @Test
  public void testAddMapping_NegativeLineNumber_InvalidMappingIgnored() {
    Node node = new Node("TestNode", -1, 0);
    FilePosition outputStartPosition = new FilePosition(1, 0);
    FilePosition outputEndPosition = new FilePosition(1, 5);
    
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    
    // TODO: Assert the mapping is not added to the generator
  }
  
  @Test
  public void testFixupSourceLocation_NoPrefixMappings_ReturnOriginalSourceFile() {
    String sourceFile = "test.js";
    
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    
    Assert.assertEquals(sourceFile, fixedSourceFile);
  }
  
  @Test
  public void testFixupSourceLocation_WithPrefixMappings_ReturnFixedSourceFile() {
    String sourceFile = "src/main/test.js";
    
    LocationMapping mapping = new LocationMapping("src/main/", "src/");
    sourceMap.setPrefixMappings(List.of(mapping));
    
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    
    String expectedFixedSourceFile = "src/test.js";
    Assert.assertEquals(expectedFixedSourceFile, fixedSourceFile);
  }
  
  @Test
  public void testFixupSourceLocation_WithMultiplePrefixMappings_ReturnFixedSourceFile() {
    String sourceFile = "src/main/test.js";
    
    LocationMapping mapping1 = new LocationMapping("src/main/", "src/");
    LocationMapping mapping2 = new LocationMapping("test.js", "changed/");
    sourceMap.setPrefixMappings(List.of(mapping1, mapping2));
    
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    
    String expectedFixedSourceFile = "changed/src/test.js";
    Assert.assertEquals(expectedFixedSourceFile, fixedSourceFile);
  }
  
  // TODO: Add more test cases for other methods
  
}