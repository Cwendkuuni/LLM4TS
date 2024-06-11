package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMap;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.rhino.Node;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

    @Test
    public void testNewInstanceV1() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance().getInstance();
        assertNotNull(sourceMap);
    }

    @Test
    public void testNewInstanceDefault() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().getInstance();
        assertNotNull(sourceMap);
    }

    @Test
    public void testNewInstanceV2() {
        SourceMap sourceMap = SourceMap.Format.V2.getInstance().getInstance();
        assertNotNull(sourceMap);
    }

    @Test
    public void testNewInstanceV3() {
        SourceMap sourceMap = SourceMap.Format.V3.getInstance().getInstance();
        assertNotNull(sourceMap);
    }

    @Test
    public void testAddMapping() {
        SourceMap sourceMap = new SourceMap(
                SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
        Node node = Node.newNode("foo");
        FilePosition outputStartPosition = new FilePosition(1, 1);
        FilePosition outputEndPosition = new FilePosition(1, 3);
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
        FilePosition mappedPosition = sourceMap.getMappingForPosition(
                node.getSourceFileName(), node.getLineno(), node.getCharno());
        assertEquals(outputStartPosition, mappedPosition);
    }

    @Test
    public void testAddMappingNullNode() {
        SourceMap sourceMap = new SourceMap(
                SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
        assertNull(sourceMap.getMappingForPosition(null, 0, 0));
    }

    @Test
    public void testAddMappingNullSourceFile() {
        SourceMap sourceMap = new SourceMap(
                SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
        Node node = Node.newNode("foo");
        FilePosition outputStartPosition = new FilePosition(1, 1);
        FilePosition outputEndPosition = new FilePosition(1, 3);
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
        assertNull(sourceMap.getMappingForPosition(null, node.getLineno(), node.getCharno()));
    }

    @Test
    public void testGetMappingForPositionNullSourceMap() {
        assertNull(SourceMap.getInstance(null).getMappingForPosition(null, 0, 0));
    }

    @Test
    public void testAddMappingNullOutputStartPosition() {
        SourceMap sourceMap = new SourceMap(
                SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
        Node node = Node.newNode("foo");
        FilePosition outputEndPosition = new FilePosition(1, 3);
        sourceMap.addMapping(node, null, outputEndPosition);
        assertNull(sourceMap.getMappingForPosition(node.getSourceFileName(), node.getLineno(), node.getCharno()));
    }

    @Test
    public void testAddMappingNullOutputEndPosition() {
        SourceMap sourceMap = new SourceMap(
                SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
        Node node = Node.newNode("foo");
        FilePosition outputStartPosition = new FilePosition(1, 1);
        sourceMap.addMapping(node, outputStartPosition, null);
        assertNull(sourceMap.getMappingForPosition(node.getSourceFileName(), node.getLineno(), node.getCharno()));
    }

    @Test
    public void testAddMappingDuplicateSourceFileAndLine() {
        SourceMap sourceMap = new SourceMap(
                SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
        Node node1 = Node.newNode("foo1");
        Node node2 = Node.newNode("foo2");
        FilePosition outputStartPosition1 = new FilePosition(1, 1);
        FilePosition outputEndPosition1 = new FilePosition(1, 3);
        FilePosition outputStartPosition2 = new FilePosition(1, 1);
        FilePosition outputEndPosition2 = new FilePosition(1, 3);
        sourceMap.addMapping(node1, outputStartPosition1, outputEndPosition1);
        sourceMap.addMapping(node2, outputStartPosition2, outputEndPosition2);
        FilePosition mappedPosition1 = sourceMap.getMappingForPosition(
                node1.getSourceFileName(), node1.getLineno(), node1.getCharno());
        FilePosition mappedPosition2 = sourceMap.getMappingForPosition(
                node2.getSourceFileName(), node2.getLineno(), node2.getCharno());
        assertEquals(mappedPosition1, mappedPosition2);
    }

    @Test
    public void testGetMappingForPositionNullSourceFileOrLine() {
        SourceMap sourceMap = new SourceMap(
                SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
        assertNull(sourceMap.getMappingForPosition(null, 0, 0));
        assertNull(sourceMap.getMappingForPosition("", 0, 0));
        assertNull(sourceMap.getMappingForPosition("", -1, 0));
        assertNull(sourceMap.getMappingForPosition("", 0, -1));
        assertNull(sourceMap.getMappingForPosition("", 0, 0));
    }

    @Test
    public void testGetMappingForPositionDifferentLine() {
        SourceMap sourceMap = new SourceMap(
                SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
        Node node = Node.newNode("foo");
        FilePosition outputStartPosition1 = new FilePosition(1, 1);
        FilePosition outputEndPosition1 = new FilePosition(1, 3);
        FilePosition outputStartPosition2 = new FilePosition(2, 1);
        FilePosition outputEndPosition2 = new FilePosition(2, 3);
        sourceMap.addMapping(node, outputStartPosition1, outputEndPosition1);
        sourceMap.addMapping(node, outputStartPosition2, outputEndPosition2);
        assertNull(sourceMap.getMappingForPosition(node.getSourceFileName(), node.getLineno(), node.getCharno()));
    }

    @Test
    public void testGetMappingForPositionDifferentFile() {
        SourceMap sourceMap = new SourceMap(
                SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
        Node node1 = Node.newNode("foo1");
        Node node2 = Node.newNode("foo2");
        FilePosition outputStartPosition1 = new FilePosition(1, 1);
        FilePosition outputEndPosition1 = new FilePosition(1, 3);
        FilePosition outputStartPosition2 = new FilePosition(2, 1);
        FilePosition outputEndPosition2 = new FilePosition(2, 3);
        sourceMap.addMapping(node1, outputStartPosition1, outputEndPosition1);
        sourceMap.addMapping(node2, outputStartPosition2, outputEndPosition2);
        assertNull(sourceMap.getMappingForPosition(node2.getSourceFileName(), node1.getLineno(), node1.getCharno()));
    }

    @Test
    public void testAppendTo() throws IOException {
        SourceMap sourceMap = new SourceMap(
                SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        sourceMap.appendTo(out, "test.js");
        String expected = "// SourceMap:\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testValidate() {
        SourceMap sourceMap = new SourceMap(
                SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
        assertThrows(IOException.class, () -> sourceMap.validate(true));
    }

    @Test
    public void testSetPrefixMappings() {
        SourceMap sourceMap = new SourceMap(
                SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
        List<LocationMapping> prefixMappings = Lists.newArrayList(
                new LocationMapping("oldPath", "newPath1"),
                new LocationMapping("oldPath2", "newPath2"));
        sourceMap.setPrefixMappings(prefixMappings);
        String sourceFile = "oldPath/foo.js";
        String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
        assertEquals("newPath1/foo.js", fixedSourceFile);

        sourceFile = "oldPath2/bar.js";
        fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
        assertEquals("newPath2/bar.js", fixedSourceFile);

        sourceFile = "otherPath/baz.js";
        fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
        assertEquals("otherPath/baz.js", fixedSourceFile);
    }
}