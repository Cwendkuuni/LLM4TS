package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.common.truth.Truth;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMap;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {
    private static final String TEST_FILE = "TestSourceMap.js";
    private static final String TEST_SOURCE_MAP_FILE = "TestSourceMap.js.map";

    @Test
    public void testSourceMapInstance() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        Truth.assertThat(sourceMap).isInstanceOf(SourceMap.class);
    }

    @Test
    public void testSourceMapV1Instance() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance();
        Truth.assertThat(sourceMap).isInstanceOf(SourceMap.class);
        Truth.assertThat(sourceMap).isInstanceOf(SourceMapV1.class);
    }

    @Test
    public void testSourceMapV2Instance() {
        SourceMap sourceMap = SourceMap.Format.V2.getInstance();
        Truth.assertThat(sourceMap).isInstanceOf(SourceMap.class);
        Truth.assertThat(sourceMap).isInstanceOf(SourceMapV2.class);
    }

    @Test
    public void testAddMappingWithNullNodeAndPosition() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        Node nullNode = null;
        FilePosition nullPosition = null;
        sourceMap.addMapping(nullNode, nullPosition);

        // Method should not throw any exception
        Truth.assertThat(sourceMap.getClass()).doesNotThrowExceptionCalling("addMapping");
    }

    @Test
    public void testAddMappingWithValidNodeAndPosition() throws IOException {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        Node node = NodeFactory.createText(TEST_FILE, "test");
        FilePosition filePosition = new FilePosition(1, 1);

        sourceMap.addMapping(node, filePosition);

        // Testing for appendTo method
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        sourceMap.appendTo(outputStream, TEST_FILE);

        String expectedSourceMapContent = readSourceMapContentFromFile(TEST_SOURCE_MAP_FILE);
        String actualSourceMapContent = new String(outputStream.toByteArray());
        Truth.assertThat(actualSourceMapContent).isEqualTo(expectedSourceMapContent);
    }

    @Test
    public void testAddMappingWithValidNodeAndPositionWithPrefixMappings() throws IOException {
        String prefix = "/test/prefix/";
        String replacement = "/test/replacement/";

        List<LocationMapping> prefixMappings = Lists.newArrayList(
            new LocationMapping(prefix, replacement)
        );

        SourceMap sourceMap = new SourceMap(
                SourceMapGeneratorFactory.getInstance(
                        SourceMapFormat.V2), prefixMappings);

        Node node = NodeFactory.createText(TEST_FILE, "test");
        FilePosition filePosition = new FilePosition(1, 1);

        sourceMap.addMapping(node, filePosition);

        // Testing for appendTo method
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        sourceMap.appendTo(outputStream, TEST_FILE);

        String expectedSourceMapContent = readSourceMapContentFromFile(TEST_SOURCE_MAP_FILE);
        String actualSourceMapContent = new String(outputStream.toByteArray());
        Truth.assertThat(actualSourceMapContent).contains(replacement);
    }

    @Test
    public void testFixupSourceLocation() throws IOException {
        String inputSourceFile = "/test/input/TestSourceMap.js";
        String expectedPrefix = "/test/prefix/";
        String expectedReplacement = "/test/replacement/";

        List<LocationMapping> prefixMappings = Lists.newArrayList(
            new LocationMapping(expectedPrefix, expectedReplacement)
        );

        SourceMap sourceMap = new SourceMap(
                SourceMapGeneratorFactory.getInstance(
                        SourceMapFormat.V2), prefixMappings);

        String fixedSourceFile = sourceMap.fixupSourceLocation(inputSourceFile);

        Truth.assertThat(fixedSourceFile).isEqualTo(expectedReplacement + inputSourceFile.substring(expectedPrefix.length()));
    }

    @Test
    public void testReset() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        sourceMap.reset();

        Truth.assertThat(sourceMap.getClass()).doesNotThrowExceptionCalling("addMapping");
    }

    @Test
    public void testSetStartingPosition() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        int offsetLine = 1;
        int offsetIndex = 1;

        sourceMap.setStartingPosition(offsetLine, offsetIndex);

        Truth.assertThat(sourceMap.getClass()).doesNotThrowExceptionCalling("addMapping");
    }

    @Test
    public void testValidate() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        boolean validate = true;

        sourceMap.validate(validate);

        Truth.assertThat(sourceMap.getClass()).doesNotThrowExceptionCalling("addMapping");
    }

    private String readSourceMapContentFromFile(String fileName) throws IOException {
        return new String(Files.toByteArray(Resources.getResource(fileName)));
    }
}