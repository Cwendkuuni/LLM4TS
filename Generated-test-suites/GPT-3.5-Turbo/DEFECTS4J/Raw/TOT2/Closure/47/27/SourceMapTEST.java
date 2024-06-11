package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.*;

public class SourceMapTest {

    @Test
    public void testAddMapping() {
        SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
        
        FilePosition outputStartPosition = new FilePosition(1, 0);
        FilePosition outputEndPosition = new FilePosition(1, 5);
        
        Node node = new Node("test", 1, 0);
        
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
        
        // Check if the mapping has been added correctly
        // Add more assertions if needed
        assertEquals(1, sourceMap.getGenerator().getMappings().size());
    }

    @Test
    public void testFixupSourceLocation() {
        SourceMap sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
        
        sourceMap.setPrefixMappings(Collections.singletonList(new SourceMap.LocationMapping("test/", "example/")));
        
        // Test with a source file that matches the prefix
        String fixedSourceFile1 = sourceMap.fixupSourceLocation("test/testFile.js");
        assertEquals("example/testFile.js", fixedSourceFile1);
        
        // Test with a source file that does not match any prefix
        String fixedSourceFile2 = sourceMap.fixupSourceLocation("anotherFile.js");
        assertEquals("anotherFile.js", fixedSourceFile2);
    }

    // Add more test cases for other methods as needed

}