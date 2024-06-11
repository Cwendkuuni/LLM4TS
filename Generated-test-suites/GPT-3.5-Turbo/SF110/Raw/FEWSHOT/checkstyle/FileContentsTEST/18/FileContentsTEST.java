package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        FileText text = new FileText(new File("testfile.java"), Arrays.asList("line 1", "line 2", "line 3"));
        fileContents = new FileContents(text);
    }

    @Test
    public void testConstructor() {
        FileText text = new FileText(new File("testfile.java"), Arrays.asList("line 1", "line 2", "line 3"));
        FileContents fileContents = new FileContents(text);
        assertEquals("testfile.java", fileContents.getFileName());
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 2", fileContents.getLine(1));
        assertEquals("line 3", fileContents.getLine(2));
    }

    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", 1, 1);
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
    }

    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        assertFalse(fileContents.lineIsComment(0));
        assertTrue(fileContents.lineIsComment(1));
        assertTrue(fileContents.lineIsComment(2));
    }

    @Test
    public void testGetCppComments() {
        fileContents.reportCppComment(1, 1);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertEquals(1, cppComments.keySet().iterator().next().intValue());
        assertEquals("line 1", cppComments.values().iterator().next().getText()[0]);
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(1, 1, 2, 2);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertEquals(1, cComments.keySet().iterator().next().intValue());
        assertEquals(1, cComments.values().iterator().next().size());
        assertEquals("line 1", cComments.values().iterator().next().get(0).getText()[0]);
        assertEquals("line 2", cComments.values().iterator().next().get(0).getText()[1]);
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportCComment(1, 1, 2, 2);
        fileContents.reportCppComment(3, 1);
        TextBlock javadocComment = new TextBlock(new String[]{"line 1", "line 2"}, 0, 1, 2);
        assertEquals(javadocComment, fileContents.getJavadocBefore(2));
    }

    @Test
    public void testGetText() {
        FileText text = fileContents.getText();
        assertEquals(new File("testfile.java"), text.getFile());
        assertEquals("line 1", text.get(0));
        assertEquals("line 2", text.get(1));
        assertEquals("line 3", text.get(2));
    }

    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        assertEquals("line 1", lines[0]);
        assertEquals("line 2", lines[1]);
        assertEquals("line 3", lines[2]);
    }

    @Test
    public void testGetLine() {
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 2", fileContents.getLine(1));
        assertEquals("line 3", fileContents.getLine(2));
    }

    @Test
    public void testGetFileName() {
        assertEquals("testfile.java", fileContents.getFileName());
    }

    @Test
    public void testDeprecatedGetFilename() {
        assertEquals("testfile.java", fileContents.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        assertFalse(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
    }

    @Test
    public void testLineIsComment() {
        assertFalse(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 2, 2));
    }
}