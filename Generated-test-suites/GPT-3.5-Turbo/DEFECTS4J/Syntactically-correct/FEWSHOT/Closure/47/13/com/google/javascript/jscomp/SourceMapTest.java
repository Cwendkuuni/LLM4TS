package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.javascript.rhino.Node;

public class SourceMapTest {
    private SourceMap sourceMap;
    private SourceMapGenerator generator;

    @Before
    public void setUp() {
        generator = SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT);
        sourceMap = new SourceMap(generator);
    }

    @Test
    public void testAddMapping_withValidNode_shouldAddMapping() {
        Node node = new Node(10);
        node.setSourceFileName("test.js");

        FilePosition outputStartPosition = new FilePosition(1, 0);
        FilePosition outputEndPosition = new FilePosition(2, 0);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        assertEquals("test.js", generator.getSourceFile(0));
        assertNull(generator.getOriginalName(0));
        assertEquals(outputStartPosition, generator.getStartPosition(0));
        assertEquals(outputEndPosition, generator.getEndPosition(0));
    }

    @Test
    public void testAddMapping_withNullSourceFile_shouldNotAddMapping() {
        Node node = new Node(10);
        node.setSourceFileName(null);

        FilePosition outputStartPosition = new FilePosition(1, 0);
        FilePosition outputEndPosition = new FilePosition(2, 0);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        assertEquals(0, generator.getSourceFileCount());
        assertNull(generator.getOriginalName(0));
        assertNull(generator.getStartPosition(0));
        assertNull(generator.getEndPosition(0));
    }

    @Test
    public void testAddMapping_withNegativeLineNo_shouldNotAddMapping() {
        Node node = new Node(10);
        node.setSourceFileName("test.js");
        node.setLineno(-1);

        FilePosition outputStartPosition = new FilePosition(1, 0);
        FilePosition outputEndPosition = new FilePosition(2, 0);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        assertEquals(0, generator.getSourceFileCount());
        assertNull(generator.getOriginalName(0));
        assertNull(generator.getStartPosition(0));
        assertNull(generator.getEndPosition(0));
    }

    @Test
    public void testFixupSourceLocation_withNoPrefixMappings_shouldReturnOriginalSourceFile() {
        String sourceFile = "test.js";
        assertEquals(sourceFile, sourceMap.fixupSourceLocation(sourceFile));
    }

    @Test
    public void testFixupSourceLocation_withMatchingPrefixMapping_shouldReturnMappedSourceFile() {
        String sourceFile = "com/google/javascript/test.js";
        String expected = "test.js";

        sourceMap.setPrefixMappings(Collections.singletonList(new LocationMapping(
                "com/google/javascript/", "")));

        assertEquals(expected, sourceMap.fixupSourceLocation(sourceFile));
    }

    @Test
    public void testFixupSourceLocation_withMultiplePrefixMappings_shouldReturnMappedSourceFile() {
        String sourceFile = "com/google/javascript/test.js";
        String expected = "js/test.js";

        sourceMap.setPrefixMappings(Arrays.asList(
                new LocationMapping("com/google/javascript/", ""),
                new LocationMapping("com/google/", ""),
                new LocationMapping("com/", "js/")));

        assertEquals(expected, sourceMap.fixupSourceLocation(sourceFile));
    }

    @Test
    public void testFixupSourceLocation_withNoMatchingPrefixMapping_shouldReturnOriginalSourceFile() {
        String sourceFile = "com/google/javascript/test.js";
        assertEquals(sourceFile, sourceMap.fixupSourceLocation(sourceFile));
    }
}