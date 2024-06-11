package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FileContentsTest {
    @Test
    public void testConstructor1() {
        FileContents fileContents = new FileContents("filename", "line1", "line2", "line3");
        assertEquals("filename", fileContents.getFileName());
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
        assertEquals("line3", fileContents.getLine(2));
    }

    @Test
    public void testConstructor2() {
        FileText fileText = new FileText("filename", "line1", "line2", "line3");
        FileContents fileContents = new FileContents(fileText);
        assertEquals("filename", fileContents.getFileName());
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
        assertEquals("line3", fileContents.getLine(2));
    }

    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("filename", "line1", "line2", "line3");
        fileContents.reportSingleLineComment("type", 2, 2);
        assertEquals(1, fileContents.getCppComments().size());
        assertEquals("line2", fileContents.getCppComments().get(2).getTextLines()[0]);
    }

    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("filename", "line1", "line2", "line3");
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        assertEquals(1, fileContents.getCComments().size());
        assertEquals("line1", fileContents.getCComments().get(1).get(0).getTextLines()[0]);
        assertEquals("line2", fileContents.getCComments().get(2).get(0).getTextLines()[0]);
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("filename", "line1", "/**", "line2", "line3");
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        TextBlock javadocBefore = fileContents.getJavadocBefore(4);
        assertEquals("line1", javadocBefore.getTextLines()[0]);
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("filename", "line1", "", "line3");
        assertEquals(false, fileContents.lineIsBlank(0));
        assertEquals(true, fileContents.lineIsBlank(1));
        assertEquals(false, fileContents.lineIsBlank(2));
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("filename", "// comment", "line2", "line3");
        assertEquals(true, fileContents.lineIsComment(0));
        assertEquals(false, fileContents.lineIsComment(1));
        assertEquals(false, fileContents.lineIsComment(2));
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("filename", "line1", "/* comment */", "line3");
        assertEquals(true, fileContents.hasIntersectionWithComment(1, 2, 2, 12));
        assertEquals(true, fileContents.hasIntersectionWithComment(2, 1, 2, 12));
        assertEquals(false, fileContents.hasIntersectionWithComment(0, 0, 0, 5));
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("filename", "line1", "line2", "package-info.java");
        assertEquals(true, fileContents.inPackageInfo());
        FileContents fileContents2 = new FileContents("filename", "line1", "line2", "line3");
        assertEquals(false, fileContents2.inPackageInfo());
    }
}