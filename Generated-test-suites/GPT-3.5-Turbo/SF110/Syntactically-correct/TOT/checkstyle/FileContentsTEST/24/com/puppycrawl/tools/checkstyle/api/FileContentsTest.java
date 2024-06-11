
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        fileContents = new FileContents("testFile.java");
    }

    @Test
    public void testReportSingleLineComment() {
        // typical use case
        fileContents.reportSingleLineComment("type", 1, 1);

        // edge case: startLineNo and startColNo are 0
        fileContents.reportSingleLineComment("type", 0, 0);

        // edge case: startLineNo is negative
        fileContents.reportSingleLineComment("type", -1, 1);

        // edge case: startColNo is negative
        fileContents.reportSingleLineComment("type", 1, -1);
    }

    @Test
    public void testReportBlockComment() {
        // typical use case
        fileContents.reportBlockComment("type", 1, 1, 2, 2);

        // edge case: startLineNo, startColNo, endLineNo, and endColNo are 0
        fileContents.reportBlockComment("type", 0, 0, 0, 0);

        // edge case: startLineNo, startColNo, endLineNo, and endColNo are negative
        fileContents.reportBlockComment("type", -1, -1, -2, -2);

        // edge case: startLineNo is greater than endLineNo
        fileContents.reportBlockComment("type", 2, 2, 1, 1);

        // edge case: startLineNo is equal to endLineNo but startColNo is greater than endColNo
        fileContents.reportBlockComment("type", 1, 2, 1, 1);
    }

    @Test
    public void testReportCppComment() {
        // typical use case
        fileContents.reportCppComment(1, 1);

        // edge case: startLineNo and startColNo are 0
        fileContents.reportCppComment(0, 0);

        // edge case: startLineNo is negative
        fileContents.reportCppComment(-1, 1);

        // edge case: startColNo is negative
        fileContents.reportCppComment(1, -1);
    }

    @Test
    public void testGetCppComments() {
        // typical use case
        fileContents.reportCppComment(1, 1);
        assertEquals(1, fileContents.getCppComments().size());

        // edge case: no C++ comments
        assertEquals(0, fileContents.getCppComments().size());
    }

    @Test
    public void testReportCComment() {
        // typical use case
        fileContents.reportCppComment(1, 1);

        // edge case: startLineNo, startColNo, endLineNo, and endColNo are 0
        fileContents.reportBlockComment("type", 0, 0, 0, 0);

        // edge case: startLineNo, startColNo, endLineNo, and endColNo are negative
        fileContents.reportBlockComment("type", -1, -1, -2, -2);

        // edge case: startLineNo is greater than endLineNo
        fileContents.reportBlockComment("type", 2, 2, 1, 1);

        // edge case: startLineNo is equal to endLineNo but startColNo is greater than endColNo
        fileContents.reportBlockComment("type", 1, 2, 1, 1);
    }

    @Test
    public void testGetCComments() {
        // typical use case
        fileContents.reportCComment(1, 1, 2, 2);

        // edge case: no C comments
        assertEquals(0, fileContents.getCComments().size());
    }

    @Test
    public void testGetJavadocBefore() {
        // typical use case
        fileContents.reportCComment(1, 1, 2, 2);
        assertNotNull(fileContents.getJavadocBefore(1));

        // edge case: no javadoc comments before the line number
        assertNull(fileContents.getJavadocBefore(0));
    }

    @Test
    public void testGetText() {
        assertNotNull(fileContents.getText());
    }

    @Test
    public void testGetLines() {
        assertNotNull(fileContents.getLines());
    }

    @Test
    public void testGetLine() {
        fileContents = new FileContents("testFile.java", "Line 1", "Line 2");
        assertEquals("Line 1", fileContents.getLine(0));
        assertEquals("Line 2", fileContents.getLine(1));

        // edge case: index is out of bounds
        assertNull(fileContents.getLine(2));
    }

    @Test
    public void testGetFileName() {
        assertEquals("testFile.java", fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        assertEquals("testFile.java", fileContents.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        // typical use case
        fileContents = new FileContents("testFile.java", "Line 1", " ", "");
        assertFalse(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(1));
        assertTrue(fileContents.lineIsBlank(2));

        // edge case: index is out of bounds
        assertFalse(fileContents.lineIsBlank(3));
        assertFalse(fileContents.lineIsBlank(-1));
    }

    @Test
    public void testLineIsComment() {
        // typical use case
        fileContents = new FileContents("testFile.java", "// Comment", "Not a comment");
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));

        // edge case: index is out of bounds
        assertFalse(fileContents.lineIsComment(2));
        assertFalse(fileContents.lineIsComment(-1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        // typical use case
        fileContents.reportCComment(1, 1, 2, 2);
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 2, 2));

        // edge case: no intersection with any comments
        assertFalse(fileContents.hasIntersectionWithComment(3, 3, 4, 4));
    }

    @Test
    public void testInPackageInfo() {
        // typical use case
        fileContents = new FileContents("package-info.java");
        assertTrue(fileContents.inPackageInfo());

        // edge case: not in package-info
        fileContents = new FileContents("testFile.java");
        assertFalse(fileContents.inPackageInfo());
    }
}
