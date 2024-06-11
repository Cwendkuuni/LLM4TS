
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.debugging.sourcemap.FilePosition;

public class SourceMapTest {

    private SourceMap sourceMap;
    private FilePosition outputStartPosition;
    private FilePosition outputEndPosition;

    @Before
    public void setup() {
        sourceMap = new SourceMap(SourceMap.Format.V1.getInstance());
        outputStartPosition = new FilePosition(1, 0);
        outputEndPosition = new FilePosition(1, 5);
    }

    @Test
    public void testAddMapping() {
        Node node = new Node();
        node.setSourceFileName("test.js");
        node.setLineno(1);
        node.setCharno(0);
        node.putProp(Node.ORIGINALNAME_PROP, "originalName");

        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

        // Verify the mapping was added correctly
        // (sourceFile, originalName, inputStartPosition, outputStartPosition, outputEndPosition)
        assertEquals("test.js", sourceMap.getGenerator().getFile(0));
        assertEquals("originalName", sourceMap.getGenerator().getName(0));
        assertEquals(1, sourceMap.getGenerator().getStartLine(0));
        assertEquals(0, sourceMap.getGenerator().getStartColumn(0));
        assertEquals(1, sourceMap.getGenerator().getEndLine(0));
        assertEquals(5, sourceMap.getGenerator().getEndColumn(0));
    }

    @Test
    public void testFixupSourceLocationWithPrefixMappings() {
        SourceMap.LocationMapping mapping1 = new SourceMap.LocationMapping("src", "src/main/");
        SourceMap.LocationMapping mapping2 = new SourceMap.LocationMapping("test", "test/main/");
        sourceMap.setPrefixMappings(Arrays.asList(mapping1, mapping2));

        String fixedSourceLocation1 = sourceMap.fixupSourceLocation("src/source.js");
        assertEquals("src/main/source.js", fixedSourceLocation1);

        String fixedSourceLocation2 = sourceMap.fixupSourceLocation("test/test.js");
        assertEquals("test/main/test.js", fixedSourceLocation2);
    }

    @Test
    public void testFixupSourceLocationWithoutPrefixMappings() {
        String fixedSourceLocation = sourceMap.fixupSourceLocation("source.js");
        assertEquals("source.js", fixedSourceLocation);
    }
}
