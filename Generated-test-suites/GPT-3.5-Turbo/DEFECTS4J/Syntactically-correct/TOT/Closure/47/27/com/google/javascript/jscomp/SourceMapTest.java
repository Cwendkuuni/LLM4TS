import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SourceMapTest {

    private SourceMapFormat sourceMapFormat;
    private SourceMapGeneratorFactory sourceMapGeneratorFactory;
    private SourceMapGenerator sourceMapGenerator;
    private SourceMap sourceMap;

    @Before
    public void setUp() {
        sourceMapFormat = mock(SourceMapFormat.class);
        sourceMapGeneratorFactory = mock(SourceMapGeneratorFactory.class);
        sourceMapGenerator = mock(SourceMapGenerator.class);
        sourceMap = new SourceMap(sourceMapGenerator);
    }

    @Test
    public void addMapping_validNode_addsMappingToGenerator() {
        // Arrange
        Node node = mock(Node.class);
        FilePosition outputStartPosition = mock(FilePosition.class);
        FilePosition outputEndPosition = mock(FilePosition.class);

        // Act
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Assert
        verify(sourceMapGenerator, times(1)).addMapping(any(String.class), any(String.class), any(FilePosition.class), any(FilePosition.class), any(FilePosition.class));
    }

    @Test
    public void addMapping_nullSourceFile_noMappingAdded() {
        // Arrange
        Node node = mock(Node.class);
        FilePosition outputStartPosition = mock(FilePosition.class);
        FilePosition outputEndPosition = mock(FilePosition.class);
        when(node.getSourceFileName()).thenReturn(null);

        // Act
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Assert
        verifyZeroInteractions(sourceMapGenerator);
    }

    @Test
    public void addMapping_negativeLineno_noMappingAdded() {
        // Arrange
        Node node = mock(Node.class);
        FilePosition outputStartPosition = mock(FilePosition.class);
        FilePosition outputEndPosition = mock(FilePosition.class);
        when(node.getSourceFileName()).thenReturn("filename");
        when(node.getLineno()).thenReturn(-1);

        // Act
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Assert
        verifyZeroInteractions(sourceMapGenerator);
    }

    @Test
    public void fixupSourceLocation_noPrefixMappings_returnOriginalSourceFile() {
        // Arrange
        String sourceFile = "original/source/file";

        // Act
        String result = sourceMap.fixupSourceLocation(sourceFile);

        // Assert
        assertEquals(sourceFile, result);
    }

    @Test
    public void fixupSourceLocation_matchingPrefixMapping_returnReplacedSourceFile() {
        // Arrange
        String prefix = "original/source";
        String replacement = "modified/source";
        String sourceFile = "original/source/file";
        sourceMap.setPrefixMappings(Collections.singletonList(new LocationMapping(prefix, replacement)));

        // Act
        String result = sourceMap.fixupSourceLocation(sourceFile);

        // Assert
        assertEquals("modified/source/file", result);
    }

    @Test
    public void fixupSourceLocation_matchingPrefixMapping_multipleMappings_returnReplacedSourceFile() {
        // Arrange
        String prefix1 = "original/source";
        String replacement1 = "modified1/source";
        String prefix2 = "original/source/file";
        String replacement2 = "modified2/source";
        String sourceFile = "original/source/file";
        sourceMap.setPrefixMappings(Arrays.asList(new LocationMapping(prefix1, replacement1), new LocationMapping(prefix2, replacement2)));

        // Act
        String result = sourceMap.fixupSourceLocation(sourceFile);

        // Assert
        assertEquals("modified2/source/file", result);
    }

    @Test
    public void fixupSourceLocation_noMatchingPrefixMappings_returnOriginalSourceFile() {
        // Arrange
        String prefix = "original/source";
        String replacement = "modified/source";
        String sourceFile = "other/source/file";
        sourceMap.setPrefixMappings(Collections.singletonList(new LocationMapping(prefix, replacement)));

        // Act
        String result = sourceMap.fixupSourceLocation(sourceFile);

        // Assert
        assertEquals(sourceFile, result);
    }

    @Test
    public void appendTo_validAppendableAndName_callsGeneratorAppendTo() throws IOException {
        // Arrange
        Appendable out = mock(Appendable.class);
        String name = "sourceMap";

        // Act
        sourceMap.appendTo(out, name);

        // Assert
        verify(sourceMapGenerator, times(1)).appendTo(out, name);
    }

    @Test
    public void reset_callsGeneratorResetAndClearsSourceLocationFixupCache() {
        // Act
        sourceMap.reset();

        // Assert
        verify(sourceMapGenerator, times(1)).reset();
        assertTrue(sourceMap.sourceLocationFixupCache.isEmpty());
    }

    @Test
    public void setStartingPosition_validOffsetValues_callsGeneratorSetStartingPosition() {
        // Arrange
        int offsetLine = 5;
        int offsetIndex = 10;

        // Act
        sourceMap.setStartingPosition(offsetLine, offsetIndex);

        // Assert
        verify(sourceMapGenerator, times(1)).setStartingPosition(offsetLine, offsetIndex);
    }

    @Test
    public void setWrapperPrefix_validPrefix_callsGeneratorSetWrapperPrefix() {
        // Arrange
        String prefix = "wrapper/";

        // Act
        sourceMap.setWrapperPrefix(prefix);

        // Assert
        verify(sourceMapGenerator, times(1)).setWrapperPrefix(prefix);
    }

    @Test
    public void validate_validValue_callsGeneratorValidate() {
        // Arrange
        boolean validate = true;

        // Act
        sourceMap.validate(validate);

        // Assert
        verify(sourceMapGenerator, times(1)).validate(validate);
    }

    @Test
    public void setPrefixMappings_validMappings_setsPrefixMappings() {
        // Arrange
        List<LocationMapping> mappings = Collections.singletonList(new LocationMapping("prefix", "replacement"));

        // Act
        sourceMap.setPrefixMappings(mappings);

        // Assert
        assertEquals(mappings, sourceMap.prefixMappings);
    }
}