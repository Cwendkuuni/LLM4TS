import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.rhino.Node;

public class SourceMapTest {

  private SourceMap sourceMap;
  private SourceMapGenerator generator;

  @Before
  public void setUp() {
    generator = mock(SourceMapGenerator.class);
    sourceMap = new SourceMap(generator);
  }
  
  @Test
  public void testAddMapping() {
    Node node = new Node();
    node.setSourceFileName("testFile.js");
    node.setLineno(5);
    node.setCharno(10);
    
    FilePosition outputStartPosition = new FilePosition(1, 2);
    FilePosition outputEndPosition = new FilePosition(3, 4);
    
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    
    ArgumentCaptor<String> sourceFileCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> originalNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<FilePosition> nodePositionCaptor = ArgumentCaptor.forClass(FilePosition.class);
    
    verify(generator).addMapping(sourceFileCaptor.capture(), originalNameCaptor.capture(), 
                                 nodePositionCaptor.capture(), any(FilePosition.class), any(FilePosition.class));

    assertEquals("testFile.js", sourceFileCaptor.getValue());
    assertEquals(null, originalNameCaptor.getValue());
    assertEquals(new FilePosition(5, 10), nodePositionCaptor.getValue());
  }
  
  @Test
  public void testAddMappingWithNullSourceFile() {
    Node node = new Node();
    node.setSourceFileName(null);
    node.setLineno(5);
    node.setCharno(10);
    
    FilePosition outputStartPosition = new FilePosition(1, 2);
    FilePosition outputEndPosition = new FilePosition(3, 4);
    
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    
    verify(generator, never()).addMapping(any(String.class), any(String.class), 
        any(FilePosition.class), any(FilePosition.class), any(FilePosition.class));
  }
  
  @Test
  public void testAddMappingWithNegativeLineNumber() {
    Node node = new Node();
    node.setSourceFileName("testFile.js");
    node.setLineno(-1);
    node.setCharno(10);
    
    FilePosition outputStartPosition = new FilePosition(1, 2);
    FilePosition outputEndPosition = new FilePosition(3, 4);
    
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    
    verify(generator, never()).addMapping(any(String.class), any(String.class), 
        any(FilePosition.class), any(FilePosition.class), any(FilePosition.class));
  }
  
  @Test
  public void testFixupSourceLocationWithEmptyPrefixMappings() {
    String sourceFile = "path/to/sourceFile.js";
    
    String result = sourceMap.fixupSourceLocation(sourceFile);
    
    assertEquals(sourceFile, result);
  }
  
  @Test
  public void testFixupSourceLocationWithMatchingPrefixMappings() {
    String sourceFile = "path/to/sourceFile.js";
    List<LocationMapping> prefixMappings = new ArrayList<>();
    prefixMappings.add(new LocationMapping("path/", "fixed/"));
    sourceMap.setPrefixMappings(prefixMappings);
    
    String result = sourceMap.fixupSourceLocation(sourceFile);
    
    assertEquals("fixed/to/sourceFile.js", result);
  }
  
  @Test
  public void testFixupSourceLocationWithMultipleMatchingPrefixMappings() {
    String sourceFile = "path/to/sourceFile.js";
    List<LocationMapping> prefixMappings = new ArrayList<>();
    prefixMappings.add(new LocationMapping("path/", "fixed/"));
    prefixMappings.add(new LocationMapping("to/", "map/"));
    sourceMap.setPrefixMappings(prefixMappings);
    
    String result = sourceMap.fixupSourceLocation(sourceFile);
    
    assertEquals("fixed/map/sourceFile.js", result);
  }
  
  @Test
  public void testFixupSourceLocationWithNoMatchingPrefixMappings() {
    String sourceFile = "path/to/sourceFile.js";
    List<LocationMapping> prefixMappings = new ArrayList<>();
    prefixMappings.add(new LocationMapping("different/", "replacement/"));
    sourceMap.setPrefixMappings(prefixMappings);
    
    String result = sourceMap.fixupSourceLocation(sourceFile);
    
    assertEquals(sourceFile, result);
  }
  
  @Test
  public void testFixupSourceLocationWithCacheHit() {
    String sourceFile = "path/to/sourceFile.js";
    List<LocationMapping> prefixMappings = new ArrayList<>();
    prefixMappings.add(new LocationMapping("path/", "fixed/"));
    sourceMap.setPrefixMappings(prefixMappings);
    
    // First call to fixupSourceLocation()
    sourceMap.fixupSourceLocation(sourceFile);
    
    // Second call to fixupSourceLocation()
    String result = sourceMap.fixupSourceLocation(sourceFile);
    
    assertEquals("fixed/to/sourceFile.js", result);
  }
  
  @Test
  public void testAppendTo() throws IOException {
    Appendable out = mock(Appendable.class);
    String name = "testName";
    
    sourceMap.appendTo(out, name);
    
    verify(generator).appendTo(out, name);
  }
  
  @Test
  public void testReset() {
    sourceMap.reset();
    
    verify(generator).reset();
    assertEquals(0, sourceMap.getSourceLocationFixupCache().size());
  }
  
  @Test
  public void testSetWrapperPrefix() {
    String prefix = "// Wrapper:";
    
    sourceMap.setWrapperPrefix(prefix);
    
    verify(generator).setWrapperPrefix(prefix);
  }
  
  @Test
  public void testValidate() {
    boolean validate = true;
    
    sourceMap.validate(validate);
    
    verify(generator).validate(validate);
  }

}