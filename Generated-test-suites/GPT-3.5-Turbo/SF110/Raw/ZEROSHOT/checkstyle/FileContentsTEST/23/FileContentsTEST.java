package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setup() {
        String[] lines = {"line 1", "line 2", "line 3"};
        fileContents = new FileContents("testFile.txt", lines);
    }

    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("test", 2, 3);
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 2", fileContents.getLine(1));
        assertEquals("line 2", fileContents.getCppComments().get(2).getText()[0]);
    }

    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("test", 1, 2, 3, 4);
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 2", fileContents.getLine(1));
        assertEquals("line 2", fileContents.getCppComments().get(1).getText()[0]);
    }

    @Test
    public void testGetCppComments() {
        fileContents.reportSingleLineComment("test", 1, 2);
        fileContents.reportSingleLineComment("test", 2, 3);
        assertEquals(2, fileContents.getCppComments().size());
        assertEquals("line 1", fileContents.getCppComments().get(1).getText()[0]);
        assertEquals("line 2", fileContents.getCppComments().get(2).getText()[0]);
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(1, 2, 3, 4);
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 2", fileContents.getLine(1));
        assertEquals("line 2", fileContents.getCComments().get(1).get(0).getText()[0]);
    }

    @Test
    public void testGetCComments() {
        fileContents.reportCComment(1, 2, 3, 4);
        assertEquals(1, fileContents.getCComments().size());
        assertEquals("line 1", fileContents.getCComments().get(1).get(0).getText()[0]);
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportCComment(1, 2, 3, 4);
        assertNull(fileContents.getJavadocBefore(2));
        assertNotNull(fileContents.getJavadocBefore(3));
        assertEquals("line 1", fileContents.getJavadocBefore(3).getText()[0]);
    }

    @Test
    public void testGetText() {
        assertEquals("line 2", fileContents.getText().get(1));
    }

    @Test
    public void testGetLines() {
        assertEquals(3, fileContents.getLines().length);
        assertEquals("line 2", fileContents.getLines()[1]);
    }

    @Test
    public void testGetLine() {
        assertEquals("line 2", fileContents.getLine(1));
    }

    @Test
    public void testGetFileName() {
        assertEquals("testFile.txt", fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        assertEquals("testFile.txt", fileContents.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        assertFalse(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        assertFalse(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 1));
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 2, 2));
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 2, 2));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }

    @Test
    public void testHasIntersectionWithCComment() {
        assertFalse(fileContents.hasIntersectionWithCComment(0, 0, 1, 1));
        assertFalse(fileContents.hasIntersectionWithCComment(0, 0, 2, 2));
        assertFalse(fileContents.hasIntersectionWithCComment(1, 1, 2, 2));
    }

    @Test
    public void testHasIntersectionWithCppComment() {
        assertFalse(fileContents.hasIntersectionWithCppComment(0, 0, 1, 1));
        assertFalse(fileContents.hasIntersectionWithCppComment(0, 0, 2, 2));
        assertFalse(fileContents.hasIntersectionWithCppComment(1, 1, 2, 2));
    }
}