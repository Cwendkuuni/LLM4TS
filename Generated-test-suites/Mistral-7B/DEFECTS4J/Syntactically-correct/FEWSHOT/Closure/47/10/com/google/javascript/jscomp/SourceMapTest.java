package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMap;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.rhino.Node;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

    @Test
    public void testCreation() {
        SourceMap sourceMap = SourceMap.V1.getInstance().newInstance();
        assertNotNull(sourceMap);
    }

    @Test
    public void testAddMapping() {
        SourceMap sourceMap = SourceMap.V1.getInstance().newInstance();
        Node node = mock(Node.class);
        FilePosition outputStartPosition = new FilePosition(1, 1);
        FilePosition outputEndPosition = new FilePosition(2, 2);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        String sourceFile = "test.js";
        String originalName = "testFunc";

        // Test case for null source file and node
        sourceMap.addMapping(null, outputStartPosition, outputEndPosition);
        sourceMap.addMapping(node, null, outputEndPosition);
        sourceMap.addMapping(node, outputStartPosition, null);

        // Test case for valid source file, original name, and positions
        String expectedSourceFile = "test.js";
        String expectedOriginalName = "testFunc";

        sourceMap.addMapping(node, new FilePosition(1, 1), new FilePosition(2, 2));
        assertEquals(expectedSourceFile, sourceMap.fixupSourceLocation(expectedSourceFile));
        assertNull(sourceMap.fixupSourceLocation(null));
    }

    @Test
    public void testAppendTo() throws IOException {
        SourceMap sourceMap = SourceMap.V1.getInstance().newInstance();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        sourceMap.appendTo(out, "test.js");
        assertNotNull(out.toString());
    }

    @Test
    public void testSetPrefixMappings() {
        SourceMap sourceMap = SourceMap.V1.getInstance().newInstance();
        List<LocationMapping> sourceMapLocationMappings = Lists.newArrayList(
                new LocationMapping("test", "testMapped"),
                new LocationMapping("test2", "testMapped2"));

        sourceMap.setPrefixMappings(sourceMapLocationMappings);

        String sourceFile = "test.js";
        String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
        assertEquals("testMapped", fixedSourceFile);
    }

    @Test
    public void testGetInstance() {
        SourceMap sourceMapV1 = SourceMap.V1.getInstance();
        SourceMap sourceMapDefault = SourceMap.DEFAULT.getInstance();
        SourceMap sourceMapV2 = SourceMap.V2.getInstance();
        SourceMap sourceMapV3 = SourceMap.V3.getInstance();

        assertNotNull(sourceMapV1);
        assertNotNull(sourceMapDefault);
        assertNotNull(sourceMapV2);
        assertNotNull(sourceMapV3);

        // Testing if the generated SourceMap instances are different
        assertNotSame(sourceMapV1.getClass(), sourceMapDefault.getClass());
        assertNotSame(sourceMapV1.getClass(), sourceMapV2.getClass());
        assertNotSame(sourceMapV1.getClass(), sourceMapV3.getClass());
    }
}