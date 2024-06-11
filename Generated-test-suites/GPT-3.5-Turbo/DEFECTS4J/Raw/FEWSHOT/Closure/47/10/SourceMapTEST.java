package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SourceMapTest {
    private SourceMap sourceMap;

    @Before
    public void setUp() {
        sourceMap = new SourceMap(SourceMap.Format.DEFAULT.getInstance());
    }

    @After
    public void tearDown() {
        sourceMap.reset();
    }

    @Test
    public void testAddMapping() {
        Node node = new Node(Token.GETPROP);
        node.setSourceFileName("filename.js");
        node.setLineno(2);
        node.setCharno(5);

        FilePosition outputStartPosition = new FilePosition(3, 6);
        FilePosition outputEndPosition = new FilePosition(4, 7);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Read the generated source map and verify its content
        try (BufferedReader reader = new BufferedReader(new FileReader("output.map"))) {
            String line;
            List<String> lines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            assertEquals("Generated source map content is correct", lines.get(0), "filename.js:2:6 => (3:6, 4:7)");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAppendTo() {
        Node node = new Node(Token.GETPROP);
        node.setSourceFileName("filename.js");
        node.setLineno(2);
        node.setCharno(5);

        FilePosition outputStartPosition = new FilePosition(3, 6);
        FilePosition outputEndPosition = new FilePosition(4, 7);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        StringBuilder sb = new StringBuilder();
        try {
            sourceMap.appendTo(sb, "output.js");
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("Generated source map content is correct", sb.toString(), "filename.js:2:6 => (3:6, 4:7)");
    }

    @Test
    public void testReset() {
        Node node = new Node(Token.GETPROP);
        node.setSourceFileName("filename.js");
        node.setLineno(2);
        node.setCharno(5);

        FilePosition outputStartPosition = new FilePosition(3, 6);
        FilePosition outputEndPosition = new FilePosition(4, 7);

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        sourceMap.reset();
        assertEquals("Source map is reset", sourceMap.sourceLocationFixupCache.size(), 0);
    }

    @Test
    public void testSetStartingPosition() {
        int offsetLine = 5;
        int offsetIndex = 10;

        sourceMap.setStartingPosition(offsetLine, offsetIndex);

        assertEquals("Starting position is set", sourceMap.generator.getStartingLine(), offsetLine);
        assertEquals("Starting position is set", sourceMap.generator.getStartingIndex(), offsetIndex);
    }

    @Test
    public void testSetWrapperPrefix() {
        String prefix = "prefix_";

        sourceMap.setWrapperPrefix(prefix);

        assertEquals("Wrapper prefix is set", sourceMap.generator.getWrapperPrefix(), prefix);
    }

    @Test
    public void testValidate() {
        boolean validate = true;

        sourceMap.validate(validate);

        assertEquals("Validation is set", sourceMap.generator.getValidate(), validate);
    }

    @Test
    public void testSetPrefixMappings() {
        List<SourceMap.LocationMapping> mappings = new ArrayList<>();
        mappings.add(new SourceMap.LocationMapping("src/", "src/main/"));

        sourceMap.setPrefixMappings(mappings);

        assertEquals("Prefix mappings are set", sourceMap.prefixMappings.size(), 1);
    }
}