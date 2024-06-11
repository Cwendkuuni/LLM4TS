import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SourceMapTest {
  
  private SourceMap sourceMap;
  private SourceMapGenerator generator;
  
  @Before
  public void setUp() {
    generator = mock(SourceMapGenerator.class);
    sourceMap = new SourceMap(generator);
  }
  
  @Test
  public void addMapping_validNodeAndFile_addsMapping() {
    // Arrange
    Node node = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);
    
    // Act
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    
    // Assert
    verify(generator, times(1)).addMapping(anyString(), anyString(), any(FilePosition.class),
                                            eq(outputStartPosition), eq(outputEndPosition));
  }
  
  @Test
  public void addMapping_nullSourceFile_doesNotAddMapping() {
    // Arrange
    Node node = mock(Node.class);
    when(node.getSourceFileName()).thenReturn(null);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);
    
    // Act
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    
    // Assert
    verify(generator, never()).addMapping(anyString(), anyString(), any(FilePosition.class),
                                           any(FilePosition.class), any(FilePosition.class));
  }
  
  @Test
  public void addMapping_negativeLineNumber_doesNotAddMapping() {
    // Arrange
    Node node = mock(Node.class);
    when(node.getSourceFileName()).thenReturn("test.js");
    when(node.getLineno()).thenReturn(-1);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);
    
    // Act
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    
    // Assert
    verify(generator, never()).addMapping(anyString(), anyString(), any(FilePosition.class),
                                           any(FilePosition.class), any(FilePosition.class));
  }
  
  @Test
  public void fixupSourceLocation_noPrefixMappings_returnsOriginalSourceLocation() {
    // Arrange
    String sourceFile = "test.js";
    
    // Act
    String result = sourceMap.fixupSourceLocation(sourceFile);
    
    // Assert
    assertEquals(sourceFile, result);
  }
  
  @Test
  public void fixupSourceLocation_matchingPrefixMapping_returnsFixedSourceLocation() {
    // Arrange
    String sourceFile = "prefix/test.js";
    List<LocationMapping> prefixMappings = Collections.singletonList(
        new LocationMapping("prefix/", "remapped/"));
    sourceMap.setPrefixMappings(prefixMappings);
    
    // Act
    String result = sourceMap.fixupSourceLocation(sourceFile);
    
    // Assert
    assertEquals("remapped/test.js", result);
  }
  
  @Test
  public void fixupSourceLocation_multipleMatchingPrefixMappings_returnsFixedSourceLocation() {
    // Arrange
    String sourceFile = "prefix/test.js";
    List<LocationMapping> prefixMappings = Arrays.asList(
        new LocationMapping("prefix/", "remapped/"),
        new LocationMapping("prefix/", "replaced/")
    );
    sourceMap.setPrefixMappings(prefixMappings);
    
    // Act
    String result = sourceMap.fixupSourceLocation(sourceFile);
    
    // Assert
    assertEquals("remapped/test.js", result);
  }
  
  @Test
  public void fixupSourceLocation_noMatchingPrefixMappings_returnsOriginalSourceLocation() {
    // Arrange
    String sourceFile = "test.js";
    List<LocationMapping> prefixMappings = Collections.singletonList(
        new LocationMapping("prefix/", "remapped/"));
    sourceMap.setPrefixMappings(prefixMappings);
    
    // Act
    String result = sourceMap.fixupSourceLocation(sourceFile);
    
    // Assert
    assertEquals(sourceFile, result);
  }
  
  @Test
  public void appendTo_validOutAndName_callsGeneratorAppendTo() throws IOException {
    // Arrange
    Appendable out = mock(Appendable.class);
    String name = "sourceMap.js";
    
    // Act
    sourceMap.appendTo(out, name);
    
    // Assert
    verify(generator, times(1)).appendTo(out, name);
  }
  
  @Test
  public void reset_setsInitialState() {
    // Act
    sourceMap.reset();
    
    // Assert
    verify(generator, times(1)).reset();
    assertTrue(sourceMap.sourceLocationFixupCache.isEmpty());
  }
  
  @Test
  public void setStartingPosition_validOffset_setsStartingPositionInGenerator() {
    // Arrange
    int offsetLine = 2;
    int offsetIndex = 4;
    
    // Act
    sourceMap.setStartingPosition(offsetLine, offsetIndex);
    
    // Assert
    verify(generator, times(1)).setStartingPosition(offsetLine, offsetIndex);
  }
  
  @Test
  public void setWrapperPrefix_validPrefix_setsWrapperPrefixInGenerator() {
    // Arrange
    String prefix = "/* Wrapper */";
    
    // Act
    sourceMap.setWrapperPrefix(prefix);
    
    // Assert
    verify(generator, times(1)).setWrapperPrefix(prefix);
  }
  
  @Test
  public void validate_callsGeneratorValidate() {
    // Arrange
    boolean validate = true;
    
    // Act
    sourceMap.validate(validate);
    
    // Assert
    verify(generator, times(1)).validate(validate);
  }
  
  @Test
  public void setPrefixMappings_validMappings_setsPrefixMappingsInSourceMap() {
    // Arrange
    List<LocationMapping> mappings = Collections.singletonList(
        new LocationMapping("source/", "remapped/"));
    
    // Act
    sourceMap.setPrefixMappings(mappings);
    
    // Assert
    assertEquals(mappings.size(), sourceMap.prefixMappings.size());
  }
}