
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileContentsTest {
    private FileContents fileContents;

    @Before
    public void setUp() {
        fileContents = new FileContents("test.java", "package com.puppycrawl.tools.checkstyle.api;", "", "public class Test {", "    // single-line comment", "", "    /*", "     * block comment", "     */", "", "    public void method() {", "        // single-line comment", "        /*", "         * block comment", "         */", "    }", "}");
    }

    @Test
    public void testGetCppComments() {
        assertEquals(0, fileContents.getCppComments().size());
    }

    @Test
    public void testGetCComments() {
        assertEquals(1, fileContents.getCComments().size());
    }

    @Test
    public void testGetJavadocBefore() {
        assertNull(fileContents.getJavadocBefore(4));
        assertNotNull(fileContents.getJavadocBefore(12));
    }

    @Test
    public void testGetText() {
        assertNotNull(fileContents.getText());
    }

    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        assertEquals(17, lines.length);
        assertEquals("package com.puppycrawl.tools.checkstyle.api;", lines[0]);
        assertEquals("", lines[1]);
        assertEquals("public class Test {", lines[2]);
        assertEquals("    // single-line comment", lines[3]);
        assertEquals("", lines[4]);
        assertEquals("    /*", lines[5]);
        assertEquals("     * block comment", lines[6]);
        assertEquals("     */", lines[7]);
        assertEquals("", lines[8]);
        assertEquals("    public void method() {", lines[9]);
        assertEquals("        // single-line comment", lines[10]);
        assertEquals("        /*", lines[11]);
        assertEquals("         * block comment", lines[12]);
        assertEquals("         */", lines[13]);
        assertEquals("    }", lines[14]);
        assertEquals("}", lines[15]);
        assertNull(lines[16]);
    }

    @Test
    public void testGetLine() {
        assertEquals("package com.puppycrawl.tools.checkstyle.api;", fileContents.getLine(0));
        assertEquals("", fileContents.getLine(1));
        assertEquals("public class Test {", fileContents.getLine(2));
        assertEquals("    // single-line comment", fileContents.getLine(3));
        assertEquals("", fileContents.getLine(4));
        assertEquals("    /*", fileContents.getLine(5));
        assertEquals("     * block comment", fileContents.getLine(6));
        assertEquals("     */", fileContents.getLine(7));
        assertEquals("", fileContents.getLine(8));
        assertEquals("    public void method() {", fileContents.getLine(9));
        assertEquals("        // single-line comment", fileContents.getLine(10));
        assertEquals("        /*", fileContents.getLine(11));
        assertEquals("         * block comment", fileContents.getLine(12));
        assertEquals("         */", fileContents.getLine(13));
        assertEquals("    }", fileContents.getLine(14));
        assertEquals("}", fileContents.getLine(15));
    }

    @Test
    public void testGetFileName() {
        assertEquals("test.java", fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        assertEquals("test.java", fileContents.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(3));
        assertTrue(fileContents.lineIsBlank(5));
        assertFalse(fileContents.lineIsBlank(9));
    }

    @Test
    public void testLineIsComment() {
        assertFalse(fileContents.lineIsComment(1));
        assertTrue(fileContents.lineIsComment(3));
        assertFalse(fileContents.lineIsComment(5));
        assertTrue(fileContents.lineIsComment(9));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(4, 0, 4, 5));
        assertTrue(fileContents.hasIntersectionWithComment(12, 0, 13, 15));
        assertFalse(fileContents.hasIntersectionWithComment(2, 0, 2, 20));
        assertTrue(fileContents.hasIntersectionWithComment(10, 0, 14, 5));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }
}
