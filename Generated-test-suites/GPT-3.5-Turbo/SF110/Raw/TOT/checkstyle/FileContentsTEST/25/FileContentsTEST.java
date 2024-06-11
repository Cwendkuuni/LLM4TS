package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        fileContents = new FileContents("test.txt", "Hello", "World");
    }

    @Test
    public void testFileContentsConstructorWithFileName() {
        assertEquals("test.txt", fileContents.getFileName());
        assertEquals(2, fileContents.getLines().length);
        assertEquals("Hello", fileContents.getLine(0));
        assertEquals("World", fileContents.getLine(1));
    }

    @Test
    public void testFileContentsConstructorWithFileText() {
        FileText fileText = new FileText(new File("test.txt"));
        fileContents = new FileContents(fileText);

        assertEquals("test.txt", fileContents.getFileName());
        assertEquals(0, fileContents.getLines().length);
    }

    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type1", 1, 1);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertNotNull(cppComments.get(1));
    }

    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type1", 1, 1, 2, 2);

        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertNotNull(cComments.get(1));
        assertEquals(1, cComments.get(1).size());
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportBlockComment("type1", 1, 1, 2, 2);
        fileContents.reportCppComment(3, 1);

        TextBlock javadocComment = fileContents.getJavadocBefore(3);
        assertNull(javadocComment);

        javadocComment = fileContents.getJavadocBefore(2);
        assertNull(javadocComment);

        javadocComment = fileContents.getJavadocBefore(1);
        assertNotNull(javadocComment);
    }

    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        assertFalse(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 0, 0));
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 1, 1));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }

    @Test
    public void testGetText() {
        assertEquals("test.txt", fileContents.getText().getFile().toString());
    }

}