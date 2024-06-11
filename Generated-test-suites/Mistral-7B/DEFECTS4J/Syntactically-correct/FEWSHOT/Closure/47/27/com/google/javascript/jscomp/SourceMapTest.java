package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

    @Test
    public void testSourceMap() throws IOException {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        sourceMap.appendTo(out, "test.js");
        String sourceMapContent = out.toString();
        assertTrue(sourceMapContent.length() > 0);

        assertNotNull(sourceMap.getGenerator());
        sourceMap.reset();

        SourceMap withPrefixMappings = new SourceMap(
                SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT));
        withPrefixMappings.setPrefixMappings(getPrefixMappings());

        Node node = Node.newJsonObjectNode();
        node.setProperty("lineno", 1);
        node.setProperty("sourceFileName", "test.js");
        node.setProperty("originalName", "testFunction");

        withPrefixMappings.addMapping(node, null, null, null, null);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        withPrefixMappings.appendTo(output, "test.js");
        String sourceMapWithPrefixMappingsContent = output.toString();
        assertTrue(sourceMapWithPrefixMappingsContent.length() > 0);
    }

    @Test
    public void testV1SourceMap() throws IOException {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance().getInstance();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        sourceMap.appendTo(out, "test.js");
        String sourceMapContent = out.toString();
        assertTrue(sourceMapContent.length() > 0);

        assertNotNull(sourceMap.getGenerator());
        sourceMap.reset();

        Node node = Node.newJsonObjectNode();
        node.setProperty("lineno", 1);
        node.setProperty("sourceFileName", "test.js");
        node.setProperty("originalName", "testFunction");

        sourceMap.addMapping(node, null, null, null, null);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        sourceMap.appendTo(output, "test.js");
        String sourceMapV1Content = output.toString();
        assertTrue(sourceMapV1Content.length() > 0);
    }

    @Test
    public void testV2SourceMap() throws IOException {
        SourceMap sourceMap = SourceMap.Format.V2.getInstance().getInstance();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        sourceMap.appendTo(out, "test.js");
        String sourceMapContent = out.toString();
        assertTrue(sourceMapContent.length() > 0);

        assertNotNull(sourceMap.getGenerator());
        sourceMap.reset();

        Node node = Node.newJsonobjectNode();
        node.setProperty("lineno", 1);
        node.setProperty("sourceFileName", "test.js");
        node.setProperty("originalName", "testFunction");

        sourceMap.addMapping(node, null, null, null, null);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        sourceMap.appendTo(output, "test.js");
        String sourceMapV2Content = output.toString();
        assertTrue(sourceMapV2Content.length() > 0);
    }

    private List<LocationMapping> getPrefixMappings() {
        return Lists.newArrayList(
                new LocationMapping("old/", "new/"),
                new LocationMapping("src/test/", "test/"));
    }
}