
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorAdapterMock;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

public class SourceMapTest {
    private SourceMap sourceMap;

    @Before
    public void setUp() {
        sourceMap = SourceMap.Format.V1.getInstance();
    }

    @Test
    public void testAddMapping_noPrefixMappings() {
        Node node = new Node(Token.BLOCK);
        node.setOriginalName("originalName");
        node.setLineno(1);
        node.setCharno(2);

        sourceMap.addMapping(node, new FilePosition(3, 4), new FilePosition(5, 6));

        SourceMapGeneratorAdapterMock generator = (SourceMapGeneratorAdapterMock) sourceMap.generator;
        SourceMapGenerator.LineEntry lineEntry = generator.getLineEntry(node.getLineno());

        assertEquals("originalName", lineEntry.originalName);
        assertEquals("test1.js", lineEntry.getOriginalFile());
        assertEquals(3, lineEntry.getOriginalLine());
        assertEquals(4, lineEntry.getOriginalColumn());
        assertEquals(5, lineEntry.getStartOffset());
        assertEquals(6, lineEntry.getEndOffset());
    }

    @Test
    public void testAddMapping_withPrefixMappings() {
        Node node = new Node(Token.BLOCK);
        node.setOriginalName("originalName");
        node.setLineno(1);
        node.setCharno(2);

        List<SourceMap.LocationMapping> prefixMappings = new ArrayList<>();
        prefixMappings.add(new SourceMap.LocationMapping("prefix1/", "replacement1/"));
        prefixMappings.add(new SourceMap.LocationMapping("prefix2/", "replacement2/"));
        sourceMap.setPrefixMappings(prefixMappings);

        sourceMap.addMapping(node, new FilePosition(3, 4), new FilePosition(5, 6));

        SourceMapGeneratorAdapterMock generator = (SourceMapGeneratorAdapterMock) sourceMap.generator;
        SourceMapGenerator.LineEntry lineEntry = generator.getLineEntry(node.getLineno());

        assertEquals("originalName", lineEntry.originalName);
        assertEquals("replacement1/test1.js", lineEntry.getOriginalFile());
        assertEquals(3, lineEntry.getOriginalLine());
        assertEquals(4, lineEntry.getOriginalColumn());
        assertEquals(5, lineEntry.getStartOffset());
        assertEquals(6, lineEntry.getEndOffset());
    }

    @Test
    public void testAppendTo() throws IOException {
        sourceMap.setStartingPosition(1, 2);
        sourceMap.setWrapperPrefix("PREFIX");
        sourceMap.validate(true);

        Appendable out = new StringBuilder();
        String name = "test.js";
        sourceMap.appendTo(out, name);

        String expectedOutput = "SOURCEMAP\nWrapperPrefix: PREFIX\nStartingPos: 1,2\nOutputFile: " + name + "\n";

        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testReset() {
        sourceMap.generator.setStartingPosition(1, 2);
        sourceMap.generator.setWrapperPrefix("PREFIX");

        sourceMap.reset();

        assertEquals(0, sourceMap.generator.getStartingLine());
        assertEquals(0, sourceMap.generator.getStartingColumn());
        assertNull(sourceMap.generator.getWrapperPrefix());
        assertEquals(0, sourceMap.sourceLocationFixupCache.size());
    }

    @Test
    public void testSetWrapperPrefix() {
        String prefix = "PREFIX";

        sourceMap.setWrapperPrefix(prefix);

        assertEquals(prefix, sourceMap.generator.getWrapperPrefix());
    }

    @Test
    public void testValidate() {
        boolean validate = true;

        sourceMap.validate(validate);

        assertEquals(validate, sourceMap.generator.isValidating());
    }
}
