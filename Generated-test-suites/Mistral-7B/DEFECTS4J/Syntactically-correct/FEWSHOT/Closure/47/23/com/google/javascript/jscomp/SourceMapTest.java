package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SourceMapTest {
    private SourceMap sourceMap;
    private List<LocationMapping> prefixMappings;

    @Before
    public void setUp() {
        SourceMapGeneratorFactory factory = SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT);
        SourceMapGenerator generator = factory.getGenerator();
        sourceMap = new SourceMap(generator);
        prefixMappings = Lists.newArrayList(
            new LocationMapping("test/", "src/"),
            new LocationMapping("test/sub/", "src/sub/")
        );
    }

    @Test
    public void testFixupSourceLocation() {
        String sourceFile = "test/Test.java";
        String fixed = sourceMap.fixupSourceLocation(sourceFile);
        assertTrue(fixed.startsWith("src/"));
        assertEquals("src/Test.java", fixed);

        sourceFile = "test/sub/SubTest.java";
        fixed = sourceMap.fixupSourceLocation(sourceFile);
        assertTrue(fixed.startsWith("src/sub/"));
        assertEquals("src/sub/SubTest.java", fixed);

        sourceFile = "test/Test.java";
        fixed = sourceMap.fixupSourceLocation(sourceFile);
        assertEquals("src/Test.java", fixed);

        sourceFile = "test/sub/SubTest.java";
        fixed = sourceMap.fixupSourceLocation(sourceFile);
        assertEquals("src/sub/SubTest.java", fixed);

        sourceFile = "test/SubTest.java";
        fixed = sourceMap.fixupSourceLocation(sourceFile);
        assertTrue(fixed.startsWith("src/sub/"));
        assertEquals("src/sub/SubTest.java", fixed);

        sourceFile = "Test.java";
        fixed = sourceMap.fixupSourceLocation(sourceFile);
        assertFalse(sourceFile.equals(fixed));
        assertTrue(fixed.startsWith("src/"));
    }

    @Test
    public void testAddMapping() {
        String sourceFile = "test/Test.java";
        Node node = Node.newNode(Node.SOURCE_FILE_PROPERTY, sourceFile);
        FilePosition outputStartPosition = new FilePosition(1, 1);
        FilePosition outputEndPosition = new FilePosition(1, 5);
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
        String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
        assertEquals("src/Test.java", fixedSourceFile);
        String expectedMapping = "-- sourceFile: " + fixedSourceFile + "\n" +
                "-- source:Test.java\n" +
                "1:1-1:5";
        try {
            String actualMapping = sourceMap.generateSourceMapString();
            assertEquals(expectedMapping, actualMapping);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSetPrefixMappings() {
        sourceMap.setPrefixMappings(prefixMappings);
        String sourceFile = "test/Test.java";
        String fixed = sourceMap.fixupSourceLocation(sourceFile);
        assertTrue(fixed.startsWith("src/"));
        assertEquals("src/Test.java", fixed);

        sourceFile = "test/sub/SubTest.java";
        fixed = sourceMap.fixupSourceLocation(sourceFile);
        assertTrue(fixed.startsWith("src/sub/"));
        assertEquals("src/sub/SubTest.java", fixed);
    }

    @Test
    public void testSetPrefixMappingsEmpty() {
        SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT).getGenerator());
        sourceMap.setPrefixMappings(Collections.emptyList());
        String sourceFile = "test/Test.java";
        String fixed = sourceMap.fixupSourceLocation(sourceFile);
        assertEquals("test/Test.java", fixed);
    }
}