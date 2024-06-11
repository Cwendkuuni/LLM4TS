
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class SourceMapTest {

    private SourceMap sourceMap;

    @Before
    public void setUp() {
        sourceMap = SourceMap.Format.V1.getInstance();
    }

    @Test
    public void testAddMapping() {
        // Test a valid node with sufficient information
        Node node1 = new Node("file1.js");
        node1.setLineno(10);
        node1.setCharno(5);
        FilePosition outputStartPosition1 = new FilePosition(1, 1);
        FilePosition outputEndPosition1 = new FilePosition(2, 2);
        sourceMap.addMapping(node1, outputStartPosition1, outputEndPosition1);

        // Test a valid node with null source file
        Node node2 = new Node(null);
        node2.setLineno(2);
        node2.setCharno(0);
        FilePosition outputStartPosition2 = new FilePosition(0, 0);
        FilePosition outputEndPosition2 = new FilePosition(0, 0);
        sourceMap.addMapping(node2, outputStartPosition2, outputEndPosition2);

        // Test a valid node with negative line number
        Node node3 = new Node("file3.js");
        node3.setLineno(-1);
        node3.setCharno(0);
        FilePosition outputStartPosition3 = new FilePosition(0, 0);
        FilePosition outputEndPosition3 = new FilePosition(0, 0);
        sourceMap.addMapping(node3, outputStartPosition3, outputEndPosition3);

        // Test a valid node with negative char number
        Node node4 = new Node("file4.js");
        node4.setLineno(1);
        node4.setCharno(-1);
        FilePosition outputStartPosition4 = new FilePosition(0, 0);
        FilePosition outputEndPosition4 = new FilePosition(0, 0);
        sourceMap.addMapping(node4, outputStartPosition4, outputEndPosition4);
    }

    @Test
    public void testFixupSourceLocation() {
        sourceMap.setPrefixMappings(new ArrayList<LocationMapping>() {
            {
                add(new LocationMapping("path1/", "replacement1/"));
                add(new LocationMapping("path2/", "replacement2/"));
            }
        });

        // Test a source file with matching prefix
        String fixedFile1 = sourceMap.fixupSourceLocation("path1/file1.js");
        assertEquals("replacement1/file1.js", fixedFile1);

        // Test a source file with no matching prefix
        String fixedFile2 = sourceMap.fixupSourceLocation("path3/file2.js");
        assertEquals("path3/file2.js", fixedFile2);

        // Test a source file with multiple matching prefixes
        String fixedFile3 = sourceMap.fixupSourceLocation("path1/path2/file3.js");
        assertEquals("replacement1/path2/file3.js", fixedFile3);
    }
  
    @Test
    public void testAppendTo() {
        // Test appending to an Appendable object
        StringBuilder builder = new StringBuilder();
        String name = "output.js";
        try {
            sourceMap.appendTo(builder, name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test appending to a null Appendable object
        Appendable nullAppendable = null;
        try {
            sourceMap.appendTo(nullAppendable, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReset() {
        sourceMap.reset();
    }

    @Test
    public void testSetStartingPosition() {
        int offsetLine = 1;
        int offsetIndex = 2;
        sourceMap.setStartingPosition(offsetLine, offsetIndex);
    }

    @Test
    public void testSetWrapperPrefix() {
        String prefix = "wrapper";
        sourceMap.setWrapperPrefix(prefix);
    }

    @Test
    public void testValidate() {
        boolean validate = true;
        sourceMap.validate(validate);
    }

}
