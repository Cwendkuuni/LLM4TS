package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FileContentsTest {
    @Test
    public void testReportSingleLineComment() {
        FileContents file = new FileContents("test.java");
        file.reportSingleLineComment("", 1, 1);
        assertFalse(file.getCppComments().isEmpty());
        assertTrue(file.getCComments().isEmpty());
    }
    
    @Test
    public void testReportBlockComment() {
        FileContents file = new FileContents("test.java");
        file.reportBlockComment("", 1, 1, 2, 2);
        assertFalse(file.getCComments().isEmpty());
        assertTrue(file.getCppComments().isEmpty());
    }
    
    @Test
    public void testReportCppComment() {
        FileContents file = new FileContents("test.java");
        file.reportCppComment(1, 1);
        assertFalse(file.getCppComments().isEmpty());
        assertTrue(file.getCComments().isEmpty());
    }
    
    @Test
    public void testGetCppComments() {
        FileContents file = new FileContents("test.java");
        assertTrue(file.getCppComments().isEmpty());
    }
    
    @Test
    public void testReportCComment() {
        FileContents file = new FileContents("test.java");
        file.reportCComment(1, 1, 2, 2);
        assertFalse(file.getCComments().isEmpty());
        assertTrue(file.getCppComments().isEmpty());
    }
    
    @Test
    public void testGetCComments() {
        FileContents file = new FileContents("test.java");
        assertTrue(file.getCComments().isEmpty());
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents file = new FileContents("test.java");
        assertNull(file.getJavadocBefore(1));
    }
    
    @Test
    public void testGetText() {
        FileContents file = new FileContents("test.java");
        assertEquals(new FileText(new File("test.java"), Collections.emptyList()), file.getText());
    }
    
    @Test
    public void testGetLines() {
        FileContents file = new FileContents("test.java");
        assertEquals(new String[0], file.getLines());
    }
    
    @Test
    public void testGetLine() {
        FileContents file = new FileContents("test.java");
        assertNull(file.getLine(1));
    }
    
    @Test
    public void testGetFileName() {
        FileContents file = new FileContents("test.java");
        assertEquals("test.java", file.getFileName());
    }
    
    @Test
    public void testGetFilename() {
        FileContents file = new FileContents("test.java");
        assertEquals("test.java", file.getFilename());
    }
    
    @Test
    public void testLineIsBlank() {
        FileContents file = new FileContents("test.java");
        assertTrue(file.lineIsBlank(1));
    }
    
    @Test
    public void testLineIsComment() {
        FileContents file = new FileContents("test.java");
        assertFalse(file.lineIsComment(1));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        FileContents file = new FileContents("test.java");
        assertFalse(file.hasIntersectionWithComment(1, 1, 2, 2));
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents file = new FileContents("test.java");
        assertFalse(file.inPackageInfo());
    }
}