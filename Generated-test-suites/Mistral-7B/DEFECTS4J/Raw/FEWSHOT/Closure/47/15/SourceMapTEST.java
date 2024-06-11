package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.rhino.Node;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

    @Test
    public void testAddMapping() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance().newInstance();
        Node node = new Node();
        node.setSourceFileName("input.js");
        node.setLineno(1);
        node.setCharno(10);
        String originalName = "myFunction";
        sourceMap.addMapping(node, new FilePosition(1, 10), new FilePosition(1, 10));
        FilePosition outputStartPosition = new FilePosition(1, 11);
        FilePosition outputEndPosition = new FilePosition(1, 20);
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        assertEquals("input.js", sourceMap.fixupSourceLocation("input.js"));
        assertEquals("input.js", sourceMap.fixupSourceLocation("input.js/somePath"));
        assertEquals("input.js", sourceMap.fixupSourceLocation("input.js\\somePath"));
        assertEquals("input.js", sourceMap.fixupSourceLocation("input.js"));
        assertEquals("input.js", sourceMap.fixupSourceLocation("somePath/input.js"));
        assertEquals("input.js", sourceMap.fixupSourceLocation("somePath/input.js/"));

        sourceMap.addMapping(node, new FilePosition(2, 11), new FilePosition(2, 20));
        FilePosition expectedOutputStartPosition = new FilePosition(1, 11);
        FilePosition expectedOutputEndPosition = new FilePosition(2, 20);
        assertEquals(expectedOutputStartPosition, sourceMap.getMapping(node).getOutputStartPosition());
        assertEquals(expectedOutputEndPosition, sourceMap.getMapping(node).getOutputEndPosition());
    }

    @Test
    public void testAppendTo() throws IOException {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance().newInstance();
        Appendable out = new StringBuffer();
        sourceMap.appendTo(out, "sourceMap.js");
        assertEquals("sourcemap{\n  versions: [1],\n  file: \"sourceMap.js\"\n}", out.toString());
    }

    @Test
    public void testReset() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance().newInstance();
        sourceMap.addMapping(new Node(), new FilePosition(1, 1), new FilePosition(1, 1));
        sourceMap.reset();
        assertNull(sourceMap.getMapping(new Node()));
    }

    @Test
    public void testValidate() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance().newInstance();
        sourceMap.validate(true);
    }

    @Test
    public void testSetWrapperPrefix() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance().newInstance();
        sourceMap.setWrapperPrefix("prefix");
        String inputFile = "input.js";
        String expectedOutputFile = "prefix__input.js";
        assertEquals(expectedOutputFile, sourceMap.fixupSourceLocation(inputFile));
    }

    @Test
    public void testSetPrefixMappings() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance().newInstance();
        List<LocationMapping> sourceMapLocationMappings = Lists.newArrayList(
            new LocationMapping("input.js", "output.js"),
            new LocationMapping("input2.js", "output2.js")
        );
        sourceMap.setPrefixMappings(sourceMapLocationMappings);
        String inputFile = "input.js";
        String expectedOutputFile = "output.js";
        assertEquals(expectedOutputFile, sourceMap.fixupSourceLocation(inputFile));
        assertEquals("input2.js", sourceMap.fixupSourceLocation("input2.js"));
    }
}