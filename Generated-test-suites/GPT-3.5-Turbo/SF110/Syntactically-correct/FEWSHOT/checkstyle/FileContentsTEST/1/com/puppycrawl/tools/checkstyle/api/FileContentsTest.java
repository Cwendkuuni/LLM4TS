package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class FileContentsTest {
    private FileContents file;
    
    @Before
    public void setUp() {
        String[] lines = {"// line 1", "// line 2", "// line 3"};
        file = new FileContents("test.java", lines);
    }
    
    @Test
    public void testConstructor() {
        assertEquals("test.java", file.getFileName());
        assertEquals("// line 1", file.getLine(0));
        assertEquals("// line 2", file.getLine(1));
        assertEquals("// line 3", file.getLine(2));
    }
    
    @Test
    public void testReportSingleLineComment() {
        file.reportSingleLineComment("type", 1, 0);
        assertTrue(file.lineIsComment(0));
        assertFalse(file.lineIsComment(1));
        assertFalse(file.lineIsComment(2));
    }
    
    @Test
    public void testReportBlockComment() {
        file.reportBlockComment("type", 1, 0, 2, 10);
        assertTrue(file.lineIsComment(0));
        assertTrue(file.lineIsComment(1));
        assertTrue(file.lineIsComment(2));
    }
    
    @Test
    public void testGetCppComments() {
        file.reportCppComment(1, 0);
        assertEquals(1, file.getCppComments().size());
        assertNull(file.getCppComments().get(0));
        assertEquals("// line 1", file.getCppComments().get(1).getText());
    }
    
    @Test
    public void testReportCComment() {
        file.reportCComment(1, 0, 2, 10);
        assertEquals(1, file.getCComments().size());
        assertNull(file.getCComments().get(0));
        assertEquals("// line 1", file.getCComments().get(1).get(0).getText());
        assertEquals("// line 2", file.getCComments().get(1).get(1).getText());
    }
    
    @Test
    public void testExtractCComment() {
        String[] cComment = file.extractCComment(1, 0, 2, 10);
        assertEquals(2, cComment.length);
        assertEquals("// line 1", cComment[0]);
        assertEquals("// line 2", cComment[1]);
    }
    
    @Test
    public void testGetJavadocBefore() {
        file.reportCComment(1, 0, 2, 10);
        assertNull(file.getJavadocBefore(0));
        assertNull(file.getJavadocBefore(1));
    }
    
    @Test
    public void testGetText() {
        assertEquals("// line 1", file.getText().get(0));
        assertEquals("// line 2", file.getText().get(1));
        assertEquals("// line 3", file.getText().get(2));
    }
    
    @Test
    public void testGetLines() {
        String[] lines = file.getLines();
        assertEquals(3, lines.length);
        assertEquals("// line 1", lines[0]);
        assertEquals("// line 2", lines[1]);
        assertEquals("// line 3", lines[2]);
    }
    
    @Test
    public void testGetLine() {
        assertEquals("// line 1", file.getLine(0));
        assertEquals("// line 2", file.getLine(1));
        assertEquals("// line 3", file.getLine(2));
    }
    
    @Test
    public void testGetFileName() {
        assertEquals("test.java", file.getFileName());
        assertEquals("test.java", file.getFilename());
    }
    
    @Test
    public void testLineIsBlank() {
        assertFalse(file.lineIsBlank(0));
        assertFalse(file.lineIsBlank(1));
        assertFalse(file.lineIsBlank(2));
    }
    
    @Test
    public void testLineIsComment() {
        assertTrue(file.lineIsComment(0));
        assertTrue(file.lineIsComment(1));
        assertTrue(file.lineIsComment(2));
        assertFalse(file.lineIsComment(3));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(file.hasIntersectionWithComment(0, 0, 1, 10));
        assertFalse(file.hasIntersectionWithComment(1, 0, 2, 10));
        assertFalse(file.hasIntersectionWithComment(0, 0, 2, 10));
    }
    
    @Test
    public void testInPackageInfo() {
        assertFalse(file.inPackageInfo());
    }
}