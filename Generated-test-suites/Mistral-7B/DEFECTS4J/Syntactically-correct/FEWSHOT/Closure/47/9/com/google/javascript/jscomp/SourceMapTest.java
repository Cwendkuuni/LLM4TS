package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMap;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {
    private SourceMapGenerator generatorV1;
    private SourceMapGenerator generatorV2;
    private SourceMap sourceMapV1;
    private SourceMap sourceMapV2;

    @Before
    public void setup() {
        generatorV1 = mock(SourceMapGeneratorV1.class);
        generatorV2 = mock(SourceMapGeneratorV2.class);

        when(generatorV1.getInstance()).thenReturn(new SourceMap(generatorV1));
        when(generatorV2.getInstance()).thenReturn(new SourceMap(generatorV2));

        sourceMapV1 = SourceMap.Format.V1.getInstance();
        sourceMapV2 = SourceMap.Format.DEFAULT.getInstance();
    }

    @Test
    public void testAddMapping() {
        SourceMap sourceMap = sourceMapV1;
        Node node = mock(Node.class);

        FilePosition outputStartPosition = new FilePosition(1, 1);
        FilePosition outputEndPosition = new FilePosition(1, 2);

        String sourceFile = "input.js";
        sourceFile = sourceMap.fixupSourceLocation(sourceFile);
        assertNotNull(sourceFile);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            sourceMap.appendTo(output, "output.js");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expectedSourceMap = "" +
                "version:1\n" +
                "sources:input.js\n" +
                "mappings:";

        String actualSourceMap = output.toString();
        assertNotNull(actualSourceMap);
        assertTrue(expectedSourceMap.equals(actualSourceMap));
    }

    @Test
    public void testFixupSourceLocation() {
        String sourceFile = "input.js/foo/bar.js";
        List<LocationMapping> prefixMappings = Collections.singletonList(
                new LocationMapping("input.js/", "input:")
        );

        SourceMap sourceMap = sourceMapV1;
        sourceMap.setPrefixMappings(prefixMappings);

        String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
        assertEquals("input:foo/bar.js", fixedSourceFile);
    }

    @Test
    public void testFixupSourceLocation_noPrefixMapping() {
        String sourceFile = "input.js";
        SourceMap sourceMap = sourceMapV1;

        String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
        assertEquals(sourceFile, fixedSourceFile);
    }

    @Test
    public void testValidate() {
        SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();
        sourceMapV1.validate(true);

        SourceMap sourceMapV2 = SourceMap.Format.DEFAULT.getInstance();
        sourceMapV2.validate(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidate_validateFalse() {
        SourceMap sourceMap = sourceMapV1;
        sourceMap.validate(false);
    }
}