
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.rhino.Node;

public class SourceMapTest {
    private SourceMap sourceMap;

    @Before
    public void setUp() {
        sourceMap = new SourceMap(
            SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
    }

    @Test
    public void testAddMapping() {
        Node node1 = new Node();
        node1.putProp(Node.ORIGINALNAME_PROP, "originalName1");
        node1.setSourceFileName("sourceFile1");
        node1.setLineno(1);
        node1.setCharno(0);
        FilePosition outputStartPosition1 = new FilePosition(0, 0);
        FilePosition outputEndPosition1 = new FilePosition(0, 0);
        sourceMap.addMapping(node1, outputStartPosition1, outputEndPosition1);

        Node node2 = new Node();
        node2.putProp(Node.ORIGINALNAME_PROP, "originalName2");
        node2.setSourceFileName("sourceFile2");
        node2.setLineno(2);
        node2.setCharno(1);
        FilePosition outputStartPosition2 = new FilePosition(1, 1);
        FilePosition outputEndPosition2 = new FilePosition(1, 1);
        sourceMap.addMapping(node2, outputStartPosition2, outputEndPosition2);

        List<LocationMapping> prefixMappings = new ArrayList<>();
        prefixMappings.add(new LocationMapping("sourceFile1", "fixedFile1"));
        prefixMappings.add(new LocationMapping("sourceFile2", "fixedFile2"));
        sourceMap.setPrefixMappings(prefixMappings);

        StringBuilder out = new StringBuilder();
        try {
            sourceMap.appendTo(out, "name");
        } catch (IOException e) {
            // Ignore
        }

        assertEquals("fixedFile1", sourceMap.fixupSourceLocation("sourceFile1"));
        assertEquals("fixedFile2", sourceMap.fixupSourceLocation("sourceFile2"));

        sourceMap.reset();

        Node node3 = new Node();
        node3.putProp(Node.ORIGINALNAME_PROP, null);
        node3.setSourceFileName(null);
        node3.setLineno(-1);
        node3.setCharno(0);
        FilePosition outputStartPosition3 = new FilePosition(0, 0);
        FilePosition outputEndPosition3 = new FilePosition(0, 0);
        sourceMap.addMapping(node3, outputStartPosition3, outputEndPosition3);

        StringBuilder out2 = new StringBuilder();
        try {
            sourceMap.appendTo(out2, "name");
        } catch (IOException e) {
            // Ignore
        }

        assertNull(sourceMap.fixupSourceLocation(null));
        assertNull(sourceMap.fixupSourceLocation("sourceFile3"));
    }

    @Test
    public void testSetStartingPosition() {
        sourceMap.setStartingPosition(1, 1);
    }

    @Test
    public void testSetWrapperPrefix() {
        sourceMap.setWrapperPrefix("prefix");
    }

    @Test
    public void testValidate() {
        sourceMap.validate(true);
        sourceMap.validate(false);
    }
}
