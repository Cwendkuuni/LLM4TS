import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SourceMapTest {

    private SourceMap sourceMap;
    private SourceMapGenerator mockGenerator;

    @Before
    public void setUp() {
        mockGenerator = mock(SourceMapGenerator.class);
        sourceMap = new SourceMap(mockGenerator);
    }

    @Test
    public void addMapping_withValidNode_shouldCallGeneratorAddMapping() {
        Node mockNode = mock(Node.class);
        FilePosition mockStartPosition = mock(FilePosition.class);
        FilePosition mockEndPosition = mock(FilePosition.class);

        sourceMap.addMapping(mockNode, mockStartPosition, mockEndPosition);

        verify(mockGenerator).addMapping(anyString(), anyString(), any(FilePosition.class), eq(mockStartPosition), eq(mockEndPosition));
    }

    @Test
    public void addMapping_withInvalidNode_shouldNotCallGeneratorAddMapping() {
        Node mockNode = mock(Node.class);
        FilePosition mockStartPosition = mock(FilePosition.class);
        FilePosition mockEndPosition = mock(FilePosition.class);

        when(mockNode.getSourceFileName()).thenReturn(null);
        when(mockNode.getLineno()).thenReturn(-1);

        sourceMap.addMapping(mockNode, mockStartPosition, mockEndPosition);

        verifyNoInteractions(mockGenerator);
    }

    @Test
    public void appendTo_withValidOutAndName_shouldCallGeneratorAppendTo() throws IOException {
        Appendable mockOut = mock(Appendable.class);
        String mockName = "testName";

        sourceMap.appendTo(mockOut, mockName);

        verify(mockGenerator).appendTo(mockOut, mockName);
    }

    @Test
    public void reset_shouldCallGeneratorReset() {
        sourceMap.reset();

        verify(mockGenerator).reset();
    }

    @Test
    public void setStartingPosition_withValidOffsetLineAndOffsetIndex_shouldCallGeneratorSetStartingPosition() {
        int mockOffsetLine = 5;
        int mockOffsetIndex = 10;

        sourceMap.setStartingPosition(mockOffsetLine, mockOffsetIndex);

        verify(mockGenerator).setStartingPosition(mockOffsetLine, mockOffsetIndex);
    }

    @Test
    public void setWrapperPrefix_withValidPrefix_shouldCallGeneratorSetWrapperPrefix() {
        String mockPrefix = "testPrefix";

        sourceMap.setWrapperPrefix(mockPrefix);

        verify(mockGenerator).setWrapperPrefix(mockPrefix);
    }

    @Test
    public void validate_withTrue_shouldCallGeneratorValidate() {
        boolean mockValidate = true;

        sourceMap.validate(mockValidate);

        verify(mockGenerator).validate(mockValidate);
    }

    @Test
    public void setPrefixMappings_withValidLocationMappings_shouldSetPrefixMappings() {
        List<LocationMapping> mockMappings = new ArrayList<>();
        mockMappings.add(new LocationMapping("mockPrefix1", "mockReplacement1"));
        mockMappings.add(new LocationMapping("mockPrefix2", "mockReplacement2"));

        sourceMap.setPrefixMappings(mockMappings);

        List<LocationMapping> actualMappings = sourceMap.getPrefixMappings();
        assertEquals(mockMappings, actualMappings);
    }

    @Test
    public void fixupSourceLocation_withNoPrefixMappings_shouldReturnUnchangedSourceFile() {
        String mockSourceFile = "mockSourceFile";

        String actualFixedSourceFile = sourceMap.fixupSourceLocation(mockSourceFile);

        assertEquals(mockSourceFile, actualFixedSourceFile);
    }

    @Test
    public void fixupSourceLocation_withMatchingPrefixMapping_shouldReturnFixedSourceFile() {
        String mockSourceFile = "mockPrefix1/mockSuffix";
        List<LocationMapping> mockMappings = new ArrayList<>();
        LocationMapping mockMapping = new LocationMapping("mockPrefix1", "mockReplacement1");
        mockMappings.add(mockMapping);

        sourceMap.setPrefixMappings(mockMappings);

        String actualFixedSourceFile = sourceMap.fixupSourceLocation(mockSourceFile);

        assertEquals("mockReplacement1/mockSuffix", actualFixedSourceFile);
    }

    @Test
    public void fixupSourceLocation_withNoMatchingPrefixMapping_shouldReturnUnchangedSourceFile() {
        String mockSourceFile = "mockPrefix3/mockSuffix";
        List<LocationMapping> mockMappings = new ArrayList<>();
        LocationMapping mockMapping = new LocationMapping("mockPrefix1", "mockReplacement1");
        mockMappings.add(mockMapping);

        sourceMap.setPrefixMappings(mockMappings);

        String actualFixedSourceFile = sourceMap.fixupSourceLocation(mockSourceFile);

        assertEquals(mockSourceFile, actualFixedSourceFile);
    }

    @Test
    public void fixupSourceLocation_withFixedSourceFileInCache_shouldReturnFixedSourceFileFromCache() {
        String mockSourceFile = "mockPrefix1/mockSuffix";
        String mockFixedSourceFile = "mockReplacement1/mockSuffix";
        List<LocationMapping> mockMappings = new ArrayList<>();
        LocationMapping mockMapping = new LocationMapping("mockPrefix1", "mockReplacement1");
        mockMappings.add(mockMapping);

        sourceMap.setPrefixMappings(mockMappings);

        // Add fixed source file to cache
        sourceMap.fixupSourceLocation(mockSourceFile);

        // Mock generator should not be called since fixed source file is already in cache
        String actualFixedSourceFile = sourceMap.fixupSourceLocation(mockSourceFile);

        assertEquals(mockFixedSourceFile, actualFixedSourceFile);
        verifyNoInteractions(mockGenerator);
    }

}