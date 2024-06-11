
package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.rhino.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

        FilePosition outputStartPosition = new FilePosition();
        FilePosition outputEndPosition = new FilePosition();

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Verify that mapping is added correctly
        assertEquals(1, generator.getMappingCount());
    }

    @Test
    public void testAddMappingWithNullNode() {
        FilePosition outputStartPosition = new FilePosition();
        FilePosition outputEndPosition = new FilePosition();

        sourceMap.addMapping(null, outputStartPosition, outputEndPosition);

        // Verify that mapping is not added
        assertEquals(0, generator.getMappingCount());
    }

    @Test
    public void testFixupSourceLocationWithPrefixMappings() {
        String sourceFile = "src/test/java/com/example/test.js";
        List<SourceMap.LocationMapping> prefixMappings = new ArrayList<>();
        prefixMappings.add(new SourceMap.LocationMapping("src/test/java", "test"));

        sourceMap.setPrefixMappings(prefixMappings);

        String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

        // Verify that source file is fixed correctly
        assertEquals("test/com/example/test.js", fixedSourceFile);
    }

    @Test
    public void testFixupSourceLocationWithoutPrefixMappings() {
        String sourceFile = "src/test/java/com/example/test.js";

        String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

        // Verify that source file is not changed
        assertEquals(sourceFile, fixedSourceFile);
    }

    @Test
    public void testReset() {
        Node node = new Node();

        FilePosition outputStartPosition = new FilePosition();
        FilePosition outputEndPosition = new FilePosition();

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Verify that mapping is added correctly
        assertEquals(1, generator.getMappingCount());

        sourceMap.reset();

        // Verify that mapping is cleared after reset
        assertEquals(0, generator.getMappingCount());
    }

    @Test
    public void testSetStartingPosition() {
        int offsetLine = 1;
        int offsetIndex = 2;

        sourceMap.setStartingPosition(offsetLine, offsetIndex);

        // Verify that starting position is set correctly
        assertEquals(offsetLine, generator.getStartingPosition().line);
        assertEquals(offsetIndex, generator.getStartingPosition().index);
    }

    @Test
    public void testSetWrapperPrefix() {
        String prefix = "wrapper";

        sourceMap.setWrapperPrefix(prefix);

        // Verify that wrapper prefix is set correctly
        assertEquals(prefix, generator.getWrapperPrefix());
    }

    @Test
    public void testValidate() {
        boolean validate = true;

        sourceMap.validate(validate);

        // Verify that validation flag is set correctly
        assertEquals(validate, generator.isValidating());
    }

    @Test
    public void testAppendTo() throws IOException {
        StringBuilder out = new StringBuilder();
        String name = "sourceMap";

        sourceMap.appendTo(out, name);

        // Verify that source map is appended correctly
        assertTrue(out.toString().contains(name));
    }
}
