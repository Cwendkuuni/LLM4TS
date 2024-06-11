package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class SourceMapTest {
  
  private SourceMap sourceMap;
  
  @Before
  public void setUp() {
    sourceMap = SourceMap.Format.DEFAULT.getInstance();
  }
  
  @Test
  public void testAddMapping_withValidNodeAndPositions_shouldAddMapping() {
    Node node = new Node();
    node.setSourceFileName("filename.js");
    node.setLineno(1);
    node.setCharno(0);
    
    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(0, 1);
    
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    
    // Assert that the mapping was added correctly
    // ...
  }
  
  @Test
  public void testAddMapping_withNullSourceFile_shouldNotAddMapping() {
    Node node = new Node();
    node.setSourceFileName(null);
    node.setLineno(1);
    node.setCharno(0);
    
    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(0, 1);
    
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    
    // Assert that no mapping was added
    // ...
  }
  
  @Test
  public void testAddMapping_withNegativeLineNumber_shouldNotAddMapping() {
    Node node = new Node();
    node.setSourceFileName("filename.js");
    node.setLineno(-1);
    node.setCharno(0);
    
    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(0, 1);
    
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    
    // Assert that no mapping was added
    // ...
  }
  
  @Test
  public void testAddMapping_withNullNode_shouldNotAddMapping() {
    Node node = null;
    
    FilePosition outputStartPosition = new FilePosition(0, 0);
    FilePosition outputEndPosition = new FilePosition(0, 1);
    
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    
    // Assert that no mapping was added
    // ...
  }
  
  @Test
  public void testFixupSourceLocation_withNoPrefixMappings_shouldReturnOriginalSourceFile() {
    String sourceFile = "filename.js";
    
    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    
    assertEquals(sourceFile, fixed);
  }
  
  @Test
  public void testFixupSourceLocation_withPrefixMappings_shouldReturnFixedSourceFile() {
    sourceMap.setPrefixMappings(createPrefixMappings());
    
    String sourceFile = "path/to/filename.js";
    
    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    
    assertEquals("fixed/" + sourceFile, fixed);
  }
  
  // Helper method to create prefix mappings
  private List<LocationMapping> createPrefixMappings() {
    List<LocationMapping> prefixMappings = new ArrayList<>();
    prefixMappings.add(new LocationMapping("path/to/", "fixed/"));
    return prefixMappings;
  }
}