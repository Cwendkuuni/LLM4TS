package com.google.javascript.jscomp;

import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.javascript.rhino.Node;
import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class SourceMapTest {
    private SourceMap sourceMap;

    @Before
    public void setUp() {
        sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(SourceMapFormat.V2));
    }

    @After
    public void tearDown() {
        sourceMap.reset();
    }

    @Test
    public void testAddMapping_validNodeAndOutputPosition_correctMappingAdded() {
        // Arrange
        Node node = mock(Node.class);
        FilePosition outputStart = mock(FilePosition.class);
        FilePosition outputEnd = mock(FilePosition.class);

        // Act
        sourceMap.addMapping(node, outputStart, outputEnd);

        // Assert
        // Verify that the mapping was added using the correct parameters
        // Use appropriate Mockito matchers to check the equality of the parameters
        verify(node).getSourceFileName();
        verify(node).getLineno();
        verify(node, times(1)).getCharno();
        verify(outputStart, times(1)).getLine();
        verify(outputStart, times(1)).getColumn();
        verify(outputEnd, times(1)).getLine();
        verify(outputEnd, times(1)).getColumn();
        verify(outputStart, times(1)).getLine();
        verify(outputStart, times(1)).getColumn();

        // Verify that the mapping was added to the generator
        // Use Mockito's verify method with the generator.addMapping parameters to check the equality of the parameters
        verify(generator).addMapping(anyString(), anyString(), eq(outputStart), eq(outputStart), eq(outputEnd));
    }

    @Test
    public void testAddMapping_nodeWithoutSourceFileNameNoMappingAdded() {
        // Arrange
        Node node = mock(Node.class);
        FilePosition outputStart = mock(FilePosition.class);
        FilePosition outputEnd = mock(FilePosition.class);
        when(node.getSourceFileName()).thenReturn(null);

        // Act
        sourceMap.addMapping(node, outputStart, outputEnd);

        // Assert
        // Verify that the getSourceFileName method was called
        verify(node).getSourceFileName();

        // Verify that the mapping was not added to the generator
        verify(generator, times(0)).addMapping(anyString(), anyString(), any(FilePosition.class), any(FilePosition.class), any(FilePosition.class));
    }

    @Test
        public void testAddMapping_nodeWithNegativeLineNumberNoMappingAdded() {
        // Arrange
        Node node = mock(Node.class);
        FilePosition outputStart = mock(FilePosition.class);
        FilePosition outputEnd = mock(FilePosition.class);
        when(node.getLineno()).thenReturn(-1);

        // Act
        sourceMap.addMapping(node, outputStart, outputEnd);

        // Assert
        // Verify that the getLineno method was called
        verify(node).getLineno();

        // Verify that the mapping was not added to the generator
        verify(generator, times(0)).addMapping(anyString(), anyString(), any(FilePosition.class), any(FilePosition.class), any(FilePosition.class));
    }

    @Test
    public void testAddMapping_nodeWithNullOutputStartPositionOrOutputEndPositionNoMappingAdded() {
        // Arrange
        Node node = mock(Node.class);
        FilePosition outputStart = null;
        FilePosition outputEnd = mock(FilePosition.class);

        // Act
        sourceMap.addMapping(node, outputStart, outputEnd);

        // Assert
        // Verify that the getSourceFileName method was called
        verify(node).getSourceFileName();

        // Verify that the mapping was not added to the generator
        verify(generator, times(0)).addMapping(anyString(), anyString(), any(FilePosition.class), any(FilePosition.class), any(FilePosition.class));

        // Repeat the test with a null outputEnd
        // Arrange
        outputStart = mock(FilePosition.class);
        outputEnd = null;

        // Act
        sourceMap.addMapping(node, outputStart, outputEnd);

        // Assert
        // Verify that the getSourceFileName method was called again
        verify(node, times(2)).getSourceFileName();

        // Verify that the mapping was not added to the generator
        verify(generator, times(0)).addMapping(anyString(), anyString(), any(FilePosition.class), any(FilePosition.class), any(FilePosition.class));
    }

    @Test
    public void testFixupSourceLocation_noPrefixMappings_returnsOriginalSourceFile() {
        // Arrange
        String sourceFile = "source.js";

        // Act
        String result = sourceMap.fixupSourceLocation(sourceFile);

        // Assert
        assertEquals(sourceFile, result);
    }

    @Test
    public void testFixupSourceLocation_withMatchingPrefixMappings_returnsFixedSourceFile() {
        // Arrange
        String sourceFile = "source.js";
        List<SourceMap.LocationMapping> prefixMappings = Lists.newArrayList(
                new SourceMap.LocationMapping("", "fixed/"),
                new SourceMap.LocationMapping("s", "f"),
                new SourceMap.LocationMapping("source", "fixed_source"),
                new SourceMap.LocationMapping("source.js", "fixed_source.js"));

        sourceMap.setPrefixMappings(prefixMappings);

        // Act
        String result = sourceMap.fixupSourceLocation(sourceFile);

        // Assert
        assertEquals("fixed/source.js", result);
    }

    @Test
    public void testFixupSourceLocation_withNoMatchingPrefixMappings_returnsOriginalSourceFile() {
        // Arrange
        String sourceFile = "source.js";
        List<SourceMap.LocationMapping> prefixMappings = Lists.newArrayList(
                new SourceMap.LocationMapping("src", "fixed/"),
                new SourceMap.LocationMapping("folder", "f"));

        sourceMap.setPrefixMappings(prefixMappings);

        // Act
        String result = sourceMap.fixupSourceLocation(sourceFile);

        // Assert
        assertEquals(sourceFile, result);
    }

    @Test
    public void testAppendTo_validAppendableAndName_correctOutput() throws IOException {
        // Arrange
        Appendable out = mock(Appendable.class);
        String name = "output.js";

        // Act
        sourceMap.appendTo(out, name);

        // Assert
        // Verify that the appendTo method was called with the correct parameters
        verify(generator).appendTo(eq(out), eq(name));
    }

    @Test
    public void testReset_resetCalled_generatesEmptySourceMap() {
        // Arrange
        Node node = mock(Node.class);
        FilePosition outputStart = mock(FilePosition.class);
        FilePosition outputEnd = mock(FilePosition.class);
        sourceMap.addMapping(node, outputStart, outputEnd);

        // Act
        sourceMap.reset();

        // Assert
        // Verify that the reset method was called on the generator
        verify(generator).reset();

        // Try to add another mapping after resetting
        // Act
        sourceMap.addMapping(node, outputStart, outputEnd);

        // Assert
        // Verify that the mapping was not added to the generator
        verify(generator, times(0)).addMapping(anyString(), anyString(), any(FilePosition.class), any(FilePosition.class), any(FilePosition.class));
    }

    @Test
    public void testSetStartingPosition_validOffsetLineAndOffsetIndex_setsStartingPositionOnGenerator() {
        // Arrange
        int offsetLine = 5;
        int offsetIndex = 10;

        // Act
        sourceMap.setStartingPosition(offsetLine, offsetIndex);

        // Assert
        // Verify that the setStartingPosition method was called on the generator with the correct parameters
        verify(generator).setStartingPosition(eq(offsetLine), eq(offsetIndex));
    }

    @Test
    public void testSetWrapperPrefix_validPrefix_setsWrapperPrefixOnGenerator() {
        // Arrange
        String prefix = "wrapper";

        // Act
        sourceMap.setWrapperPrefix(prefix);

        // Assert
        // Verify that the setWrapperPrefix method was called on the generator with the correct prefix
        verify(generator).setWrapperPrefix(eq(prefix));
    }

    @Test
    public void testValidate_validValidate_callsValidateOnGenerator() {
        // Arrange
        boolean validate = true;

        // Act
        sourceMap.validate(validate);

        // Assert
        // Verify that the validate method was called on the generator with the correct validate parameter
        verify(generator).validate(eq(validate));
    }

    @Test
    public void testSetPrefixMappings_validMappings_setsPrefixMappingsOnSourceMap() {
        // Arrange
        List<SourceMap.LocationMapping> mappings = Lists.newArrayList(
                new SourceMap.LocationMapping("src", "fixed/"),
                new SourceMap.LocationMapping("folder", "f"));

        // Act
        sourceMap.setPrefixMappings(mappings);

        // Assert
        // Verify that the prefixMappings member variable was set on the sourceMap object
        assertEquals(mappings, sourceMap.prefixMappings);
    }
}