package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FileContentsTest {
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("testFile.java", "This is a test", "// Single line comment");

        fileContents.reportSingleLineComment("SINGLE_LINE", 2, 0);

        assertEquals(1, fileContents.getCppComments().size());
        assertEquals("Single line comment", fileContents.getCppComments().get(2).getText()[0]);
    }

    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("testFile.java", "This is a test", "/*", "Block", "Comment", "*/");

        fileContents.reportBlockComment("BLOCK", 2, 0, 5, 2);

        assertEquals(1, fileContents.getCComments().size());
        assertEquals(1, fileContents.getCComments().get(2).size());
        assertEquals("/*\nBlock\nComment\n*/", fileContents.getCComments().get(2).get(0).getText()[0]);
    }

    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents("testFile.java", "This is a test", "// Single line comment");

        fileContents.reportCppComment(2, 0);

        assertEquals(1, fileContents.getCppComments().size());
        assertEquals("Single line comment", fileContents.getCppComments().get(2).getText()[0]);
    }

    @Test
    public void testGetCppComments() {
        FileContents fileContents = new FileContents("testFile.java", "This is a test", "// Single line comment");

        fileContents.reportCppComment(2, 0);

        assertEquals(1, fileContents.getCppComments().size());
        assertEquals("Single line comment", fileContents.getCppComments().get(2).getText()[0]);
    }

    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents("testFile.java", "This is a test", "/*", "Block", "Comment", "*/");

        fileContents.reportCComment(2, 0, 5, 2);

        assertEquals(1, fileContents.getCComments().size());
        assertEquals(1, fileContents.getCComments().get(2).size());
        assertEquals("/*\nBlock\nComment\n*/", fileContents.getCComments().get(2).get(0).getText()[0]);
    }

    @Test
    public void testGetCComments() {
        FileContents fileContents = new FileContents("testFile.java", "This is a test", "/*", "Block", "Comment", "*/");

        fileContents.reportCComment(2, 0, 5, 2);

        assertEquals(1, fileContents.getCComments().size());
        assertEquals(1, fileContents.getCComments().get(2).size());
        assertEquals("/*\nBlock\nComment\n*/", fileContents.getCComments().get(2).get(0).getText()[0]);
    }

    @Test
    public void testExtractCComment() {
        FileContents fileContents = new FileContents("testFile.java", "This is a test", "/*", "Block", "Comment", "*/");

        String[] cComment = fileContents.extractCComment(2, 0, 5, 2);

        assertEquals(5, cComment.length);
        assertEquals("/*", cComment[0]);
        assertEquals("Block", cComment[1]);
        assertEquals("Comment", cComment[2]);
        assertEquals("*/", cComment[3]);
        assertNull(cComment[4]);
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("testFile.java", "This is a test", "/**", "Javadoc", "Comment", "*/");

        TextBlock javadocComment = fileContents.getJavadocBefore(5);

        assertEquals("/**\nJavadoc\nComment\n*/", javadocComment.getText()[0]);
    }

    @Test
    public void testLine() {
        FileContents fileContents = new FileContents("testFile.java", "This is a test");

        assertEquals("This is a test", fileContents.line(0));
    }

    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("testFile.java", "This is a test");

        FileText fileText = fileContents.getText();

        assertEquals("This is a test", fileText.get(0));
    }

    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("testFile.java", "This is", "a test");

        String[] lines = fileContents.getLines();

        assertEquals(2, lines.length);
        assertEquals("This is", lines[0]);
        assertEquals("a test", lines[1]);
    }

    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("testFile.java", "This is a test");

        assertEquals("This is a test", fileContents.getLine(0));
    }

    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("testFile.java", "This is a test");

        assertEquals("testFile.java", fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        FileContents fileContents = new FileContents("testFile.java", "This is a test");

        assertEquals("testFile.java", fileContents.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("testFile.java", "", "This is a test");

        boolean isBlank = fileContents.lineIsBlank(0);

        assertTrue(isBlank);
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("testFile.java", "// This is a comment", "This is not a comment");

        boolean isComment = fileContents.lineIsComment(0);
        boolean isNotComment = fileContents.lineIsComment(1);

        assertTrue(isComment);
        assertFalse(isNotComment);
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("testFile.java", "This is a test", "// Single line comment");

        boolean intersection = fileContents.hasIntersectionWithComment(2, 0, 2, 5);
        boolean noIntersection = fileContents.hasIntersectionWithComment(1, 0, 1, 5);

        assertTrue(intersection);
        assertFalse(noIntersection);
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", "This is a test");

        boolean inPackageInfo = fileContents.inPackageInfo();

        assertTrue(inPackageInfo);
    }
}