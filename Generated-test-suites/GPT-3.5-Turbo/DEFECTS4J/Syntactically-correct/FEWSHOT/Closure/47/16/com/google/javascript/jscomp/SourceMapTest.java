package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMap;
import com.google.debugging.sourcemap.SourceMap.Format;
import com.google.debugging.sourcemap.SourceMap.LocationMapping;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SourceMapTest {
    private SourceMap sourceMap;

    @Before
    public void setUp() {
        sourceMap = Format.V1.getInstance();
    }

    @Test
    public void testAddMapping() {
        Node node = new Node();
        node.setSourceFileName("test.js");
        node.setLineno(1);
        node.setCharno(0);

        FilePosition startPos = new FilePosition(0, 0);
        FilePosition endPos = new FilePosition(0, 1);

        sourceMap.addMapping(node, startPos, endPos);

        // TODO: Assert actual mapping data
    }

    @Test
    public void testFixupSourceLocation_NoMappings() {
        String sourceFile = "test.js";
        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        assertEquals(sourceFile, fixed);
    }

    @Test
    public void testFixupSourceLocation_WithMappings() {
        String sourceFile = "path/to/test.js";
        String prefix = "path/to/";
        String replacement = "replaced/";

        List<LocationMapping> mappings = new ArrayList<>();
        LocationMapping mapping = new LocationMapping(prefix, replacement);
        mappings.add(mapping);
        sourceMap.setPrefixMappings(mappings);

        String fixed = sourceMap.fixupSourceLocation(sourceFile);

        assertEquals("replaced/test.js", fixed);
    }

    @Test
    public void testAppendTo() throws IOException {
        StringBuilder sb = new StringBuilder();

        sourceMap.appendTo(sb, "test.js");

        // TODO: Assert actual output
    }

    @Test
    public void testReset() {
        Node node = new Node();
        node.setSourceFileName("test.js");
        node.setLineno(1);
        node.setCharno(0);

        FilePosition startPos = new FilePosition(0, 0);
        FilePosition endPos = new FilePosition(0, 1);

        sourceMap.addMapping(node, startPos, endPos);

        sourceMap.reset();

        // TODO: Assert that the mappings have been cleared
    }

    @Test
    public void testSetStartingPosition() {
        int offsetLine = 1;
        int offsetIndex = 2;

        sourceMap.setStartingPosition(offsetLine, offsetIndex);

        // TODO: Assert actual starting position
    }

    @Test
    public void testSetWrapperPrefix() {
        String prefix = "prefix/";

        sourceMap.setWrapperPrefix(prefix);

        // TODO: Assert actual wrapper prefix
    }

    @Test
    public void testValidate() {
        boolean validate = true;

        sourceMap.validate(validate);

        // TODO: Assert actual validation setting
    }
}