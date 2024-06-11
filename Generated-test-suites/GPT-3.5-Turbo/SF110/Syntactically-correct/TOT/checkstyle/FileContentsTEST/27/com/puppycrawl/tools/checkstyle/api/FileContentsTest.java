
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class FileContentsTest {

    private FileContents fileContents;
    private FileText fileText;

    @Before
    public void setUp() {
        String[] lines = { "line 1", "line 2", "// single line comment", "/* block comment */" };
        fileText = FileText.fromLines(new File("test.txt"), lines);
        fileContents = new FileContents(fileText);
        fileContents.reportCppComment(3, 4);
        fileContents.reportCComment(4, 5, 4, 21);
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(2, 3);
        Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertNotNull(cppComments);
        assertEquals(2, cppComments.size());
        assertNotNull(cppComments.get(2));
        assertNotNull(cppComments.get(3));
    }

    @Test
    public void testGetCppComments() {
        Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertNotNull(cppComments);
        assertEquals(1, cppComments.size());
        assertNotNull(cppComments.get(3));
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(4, 14, 4, 16);
        Map<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertNotNull(cComments);
        assertEquals(1, cComments.size());
        assertEquals(2, cComments.get(4).size());
    }

    @Test
    public void testGetCComments() {
        Map<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertNotNull(cComments);
        assertEquals(1, cComments.size());
        assertEquals(1, cComments.get(4).size());
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportCComment(2, 5, 2, 18);
        TextBlock javadocComment = new Comment(new String[] { " line 2" }, 5, 2, 18);
        assertEquals(javadocComment, fileContents.getJavadocBefore(3));
    }

    @Test
    public void testGetText() {
        assertEquals(fileText, fileContents.getText());
    }

    @Test
    public void testGetLines() {
        String[] lines = { "line 1", "line 2", "// single line comment", "/* block comment */" };
        assertArrayEquals(lines, fileContents.getLines());
    }

    @Test
    public void testGetLine() {
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("// single line comment", fileContents.getLine(2));
    }

    @Test
    public void testGetFileName() {
        assertEquals("test.txt", fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        assertEquals("test.txt", fileContents.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(2));
        assertFalse(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertTrue(fileContents.hasIntersectionWithComment(3, 4, 4, 20));
        assertFalse(fileContents.hasIntersectionWithComment(2, 2, 2, 6));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }

    @Test
    public void testExtractCComment() {
        String[] cComment = new String[] { "line 2", "/* block comment ", "*/" };
        assertArrayEquals(cComment, fileContents.extractCComment(4, 5, 4, 21));
    }
}

