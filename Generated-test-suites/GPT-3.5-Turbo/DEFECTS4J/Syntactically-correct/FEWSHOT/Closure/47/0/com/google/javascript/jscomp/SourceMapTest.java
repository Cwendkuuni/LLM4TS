package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class SourceMapTest {
    @Test
    public void testAddMapping() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        FilePosition outputStartPosition = new FilePosition(1, 0);
        FilePosition outputEndPosition = new FilePosition(2, 0);
        Node node = new Node(
            Token.CALL,
            new Node(Token.NAME, "alert"),
            new Node(Token.STRING, "Hello, world!"));

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
        
        // Test that the mapping was added correctly
        // (source file, name, original position, output start position, output end position)
        assertEquals("test.js", sourceMap.generator.getMappings().get(0)[0]);
        assertEquals("alert", sourceMap.generator.getMappings().get(0)[1]);
        assertEquals(0, sourceMap.generator.getMappings().get(0)[2]);
        assertEquals(0, sourceMap.generator.getMappings().get(0)[3]);
        assertEquals(1, sourceMap.generator.getMappings().get(0)[4]);
        assertEquals(0, sourceMap.generator.getMappings().get(0)[5]);
    }

    @Test
    public void testFixupSourceLocation() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        sourceMap.setPrefixMappings(Collections.singletonList(
            new LocationMapping("C:/original/path", "D:/new/path")));

        // Test a source file with the prefix to be fixed
        String fixedLocation = sourceMap.fixupSourceLocation("C:/original/path/file.js");
        assertEquals("D:/new/path/file.js", fixedLocation);

        // Test a source file with no matching prefix
        String mismatchLocation = sourceMap.fixupSourceLocation("C:/other/path/file.js");
        assertEquals("C:/other/path/file.js", mismatchLocation);
    }

    @Test
    public void testSetStartingPosition() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        sourceMap.setStartingPosition(2, 3);

        // Test that the starting position was set correctly
        assertEquals(2, sourceMap.generator.getStartingLine());
        assertEquals(3, sourceMap.generator.getStartingColumn());
    }

    @Test
    public void testSetWrapperPrefix() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        sourceMap.setWrapperPrefix("function() {");

        // Test that the wrapper prefix was set correctly
        assertEquals("function() {", sourceMap.generator.getWrapperPrefix());
    }

    @Test
    public void testValidate() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        sourceMap.validate(true);

        // Test that validation was enabled
        assertTrue(sourceMap.generator.isValidating());

        sourceMap.validate(false);

        // Test that validation was disabled
        assertFalse(sourceMap.generator.isValidating());
    }

    @Test
    public void testSetPrefixMappings() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        List<LocationMapping> mappings = Collections.singletonList(
            new LocationMapping("C:/original/path", "D:/new/path"));

        sourceMap.setPrefixMappings(mappings);

        // Test that the prefix mappings were set correctly
        assertEquals(mappings, sourceMap.prefixMappings);
    }

    @Test
    public void testReset() {
        SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
        sourceMap.generator.setWrapperPrefix("function() {");
        sourceMap.sourceLocationFixupCache.put("C:/original/path/file.js", "D:/new/path/file.js");

        sourceMap.reset();

        // Test that the source map was reset
        assertEquals(SourceMap.Format.DEFAULT.getInstance().generator, sourceMap.generator);
        assertEquals(Collections.emptyList(), sourceMap.prefixMappings);
        assertTrue(sourceMap.sourceLocationFixupCache.isEmpty());
    }
}