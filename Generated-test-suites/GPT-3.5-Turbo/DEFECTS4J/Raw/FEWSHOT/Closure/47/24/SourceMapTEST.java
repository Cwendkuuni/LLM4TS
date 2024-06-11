
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.rhino.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SourceMapTest {

    private SourceMap sourceMap;
    private SourceMapGenerator generator;

    @Before
    public void setUp() {
        generator = SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT);
        sourceMap = new SourceMap(generator);
    }

    @Test
    public void testAddMapping() {
        Node node = new Node();
        node.setSourceFileName("test.js");
        node.setLineno(1);
        node.setCharno(5);

        FilePosition outputStartPosition = new FilePosition(2, 1);
        FilePosition outputEndPosition = new FilePosition(2, 5);
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    }

    @Test
    public void testSourceMapAppendTo() throws IOException {
        StringBuilder sb = new StringBuilder();

        FilePosition outputStartPosition = new FilePosition(1, 1);
        FilePosition outputEndPosition = new FilePosition(1, 5);
        sourceMap.appendTo(sb, "test.js");

        String expected = "expected output";

        assertEquals(expected, sb.toString());
    }

    @Test
    public void testSourceMapReset() {
        FilePosition outputStartPosition = new FilePosition(1, 1);
        FilePosition outputEndPosition = new FilePosition(1, 5);
        sourceMap.addMapping(new Node(), outputStartPosition, outputEndPosition);

        assertNotNull(sourceMap);
        sourceMap.reset();
        assertNotNull(sourceMap);
    }

    @Test
    public void testSourceMapSetStartingPosition() {
        sourceMap.setStartingPosition(2, 1);
    }

    @Test
    public void testSourceMapSetWrapperPrefix() {
        sourceMap.setWrapperPrefix("prefix");
    }

    @Test
    public void testSourceMapValidate() {
        sourceMap.validate(true);
    }

    @Test
    public void testSourceMapSetPrefixMappings() {
        List<SourceMap.LocationMapping> mappings = new ArrayList<>();
        mappings.add(new SourceMap.LocationMapping("old/", "new/"));
        sourceMap.setPrefixMappings(mappings);
    }
}
