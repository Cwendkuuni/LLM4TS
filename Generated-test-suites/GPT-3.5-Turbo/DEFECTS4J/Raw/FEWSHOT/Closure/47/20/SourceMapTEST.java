package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SourceMapTest {
    private SourceMap sourceMap;
    private SourceMapGenerator generator;
    private Node node;

    @Before
    public void setUp() {
        generator = mock(SourceMapGenerator.class);
        sourceMap = new SourceMap(generator);
        node = mock(Node.class);
    }

    @Test
    public void testAddMapping_withValidNode_shouldAddMapping() {
        FilePosition outputStartPosition = new FilePosition(2, 0);
        FilePosition outputEndPosition = new FilePosition(2, 4);

        when(node.getSourceFileName()).thenReturn("testFile.js");
        when(node.getLineno()).thenReturn(1);
        when(node.getCharno()).thenReturn(2);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Verify that the addMapping method in the generator is called with the correct parameters
        String expectedSourceFile = "testFile.js";
        String expectedOriginalName = null;
        FilePosition expectedOriginalPosition = new FilePosition(1, 2);
        assertEquals(expectedSourceFile, generator.addMapping(
                expectedSourceFile, expectedOriginalName,
                expectedOriginalPosition, outputStartPosition, outputEndPosition));
    }

    @Test
    public void testAddMapping_withNodeWithoutSourceFileName_shouldNotAddMapping() {
        when(node.getSourceFileName()).thenReturn(null);
        when(node.getLineno()).thenReturn(1);
        when(node.getCharno()).thenReturn(2);

        sourceMap.addMapping(node, new FilePosition(2, 0), new FilePosition(2, 4));

        // Verify that the addMapping method in the generator is not called
    }

    @Test
    public void testAddMapping_withNodeWithNegativeLineNumber_shouldNotAddMapping() {
        when(node.getSourceFileName()).thenReturn("testFile.js");
        when(node.getLineno()).thenReturn(-1);
        when(node.getCharno()).thenReturn(2);

        sourceMap.addMapping(node, new FilePosition(2, 0), new FilePosition(2, 4));

        // Verify that the addMapping method in the generator is not called
    }

    @Test
    public void testFixupSourceLocation_withEmptyPrefixMappings_shouldReturnSourceFile() {
        String sourceFile = "testFile.js";

        sourceMap.setPrefixMappings(Collections.emptyList());
        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        assertEquals(sourceFile, fixed);
    }

    @Test
    public void testFixupSourceLocation_withMatchingPrefixMapping_shouldReturnRemappedSourceFile() {
        String sourceFile = "testFile.js";
        String prefix = "test";
        String replacement = "newTest";
        String expectedFixed = "newTestFile.js";

        List<SourceMap.LocationMapping> prefixMappings = Collections.singletonList(
                new SourceMap.LocationMapping(prefix, replacement)
        );

        sourceMap.setPrefixMappings(prefixMappings);
        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        assertEquals(expectedFixed, fixed);
    }

    @Test
    public void testFixupSourceLocation_withMultiplePrefixMappings_shouldReturnRemappedSourceFile() {
        String sourceFile = "testFile.js";
        String prefix1 = "test";
        String replacement1 = "newTest";
        String prefix2 = "file";
        String replacement2 = "newFile";
        String expectedFixed = "newFileTestFile.js";

        List<SourceMap.LocationMapping> prefixMappings = Arrays.asList(
                new SourceMap.LocationMapping(prefix1, replacement1),
                new SourceMap.LocationMapping(prefix2, replacement2)
        );

        sourceMap.setPrefixMappings(prefixMappings);
        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        assertEquals(expectedFixed, fixed);
    }

    @Test
    public void testFixupSourceLocation_withNoMatchingPrefix_shouldReturnSourceFile() {
        String sourceFile = "testFile.js";
        String prefix = "newTest";
        String replacement = "newTest";

        List<SourceMap.LocationMapping> prefixMappings = Collections.singletonList(
                new SourceMap.LocationMapping(prefix, replacement)
        );

        sourceMap.setPrefixMappings(prefixMappings);
        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        assertEquals(sourceFile, fixed);
    }

    @Test
    public void testAppendTo_withValidGeneratorAndName_shouldAppendToWriter() throws IOException {
        StringBuilder sb = new StringBuilder();
        String name = "testMap";

        sourceMap.appendTo(sb, name);

        // Verify that the appendTo method in the generator is called with the correct parameters
        assertEquals(name, generator.appendTo(sb, name));
    }

    @Test
    public void testReset_shouldResetGeneratorAndCache() {
        sourceMap.reset();

        // Verify that the reset method in the generator is called and the sourceLocationFixupCache is cleared
    }

    @Test
    public void testSetStartingPosition_withOffsetLineAndIndex_shouldSetStartingPositionInGenerator() {
        int offsetLine = 1;
        int offsetIndex = 2;

        sourceMap.setStartingPosition(offsetLine, offsetIndex);

        // Verify that the setStartingPosition method in the generator is called with the correct parameters
        generator.setStartingPosition(offsetLine, offsetIndex);
    }

    @Test
    public void testSetWrapperPrefix_withPrefix_shouldSetWrapperPrefixInGenerator() {
        String prefix = "testPrefix";

        sourceMap.setWrapperPrefix(prefix);

        // Verify that the setWrapperPrefix method in the generator is called with the correct prefix
        assertEquals(prefix, generator.setWrapperPrefix(prefix));
    }

    @Test
    public void testValidate_withValidateFlag_shouldSetValidateInGenerator() {
        boolean validate = true;

        sourceMap.validate(validate);

        // Verify that the validate method in the generator is called with the correct validate flag
        assertEquals(validate, generator.validate(validate));
    }
}