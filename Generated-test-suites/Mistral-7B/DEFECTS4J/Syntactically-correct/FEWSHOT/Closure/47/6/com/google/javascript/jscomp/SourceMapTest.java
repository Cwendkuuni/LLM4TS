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
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().newSourceMap();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        sourceMap.setWrapperPrefix("wrapper");
        sourceMap.appendTo(output, "test.js");
        sourceMap.validate(true);

        assertNotNull(output.toString());

        SourceMapGenerator generator = sourceMap.generator;
        assertTrue(generator instanceof SourceMapGeneratorV1);

        SourceMapGeneratorV1 sourceMapGeneratorV1 = (SourceMapGeneratorV1) generator;
        List<Map<String, Object>> sources = sourceMapGeneratorV1.getSources();
        assertTrue(sources.size() > 0);
        Map<String, Object> source = sources.get(0);
        assertTrue(source.containsKey("file"));
        assertTrue(source.containsKey("sourceRoot"));
        String sourceFile = (String) source.get("file");
        assertTrue(sourceFile.endsWith(".js"));

        sourceMap.reset();
        sourceMap.setPrefixMappings(getPrefixMappings());
        sourceMap.appendTo(output, "test.js");
        sourceMap.validate(true);

        assertNotNull(output.toString());
    }

    @Test
    public void testAddMapping() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().newSourceMap();

        Node node = new Node_(); //assuming Node is a valid class with Node_ as constructor
        String sourceFile = "test.js";
        FilePosition outputStartPosition = new FilePosition(1, 1);
        FilePosition outputEndPosition = new FilePosition(1, 10);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
        String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
        assertTrue(fixedSourceFile.equals(sourceFile));

        sourceMap.setPrefixMappings(getPrefixMappings());
        fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
        assertTrue(fixedSourceFile.equals("new_source.js"));
    }

    private List<LocationMapping> getPrefixMappings() {
        return Lists.newArrayList(new LocationMapping("src/", "new_source/"),
                new LocationMapping("test/", "new_test/"));
    }
}