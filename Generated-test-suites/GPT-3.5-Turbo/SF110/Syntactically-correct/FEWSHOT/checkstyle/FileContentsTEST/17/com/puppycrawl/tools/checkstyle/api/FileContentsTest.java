package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import org.junit.Test;

public class FileContentsTest {
    
    @Test
    public void testConstructor_WithLines() {
        FileContents fileContents = new FileContents("testFile.java", "line 1", "line 2");
        
        assertEquals("testFile.java", fileContents.getFileName());
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 2", fileContents.getLine(1));
        assertNull(fileContents.getLine(2));
    }
    
    @Test
    public void testConstructor_WithFileText() {
        FileText fileText = new FileText(new File("testFile.java"), Arrays.asList("line 1", "line 2"));
        FileContents fileContents = new FileContents(fileText);
        
        assertEquals("testFile.java", fileContents.getFileName());
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 2", fileContents.getLine(1));
        assertNull(fileContents.getLine(2));
    }
    
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("testFile.java", "line 1", "line 2");
        fileContents.reportSingleLineComment("type", 2, 3);
        
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("//line 2", fileContents.getLine(1));
        assertNull(fileContents.getLine(2));
    }
    
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("testFile.java", "line 1", "line 2");
        fileContents.reportBlockComment("type", 2, 3, 2, 8);
        
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("/*line 2*/", fileContents.getLine(1));
        assertNull(fileContents.getLine(2));
    }
    
    @Test
    public void testGetCppComments() {
        FileContents fileContents = new FileContents("testFile.java", "line 1", "line 2");
        fileContents.reportCppComment(2, 3);
        
        assertNotNull(fileContents.getCppComments());
        assertEquals(1, fileContents.getCppComments().size());
        assertEquals("//line 2", fileContents.getCppComments().get(2).getText());
    }
    
    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents("testFile.java", "line 1", "line 2");
        fileContents.reportCComment(2, 3, 2, 8);
        
        assertNotNull(fileContents.getCComments());
        assertEquals(1, fileContents.getCComments().size());
        assertEquals("/*line 2*/", fileContents.getCComments().get(2).get(0).getText());
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("testFile.java", "line 1", "line 2");
        fileContents.reportCComment(1, 3, 2, 8);
        
        assertNotNull(fileContents.getJavadocBefore(2));
        assertEquals("/*line 2*/", fileContents.getJavadocBefore(2).getText());
    }
    
    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("testFile.java", "line 1", "line 2");
        
        assertNotNull(fileContents.getText());
        assertEquals("line 1", fileContents.getText().get(0));
        assertEquals("line 2", fileContents.getText().get(1));
    }
    
    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("testFile.java", "line 1", "line 2");
        
        assertNotNull(fileContents.getLines());
        assertArrayEquals(new String[]{"line 1", "line 2"}, fileContents.getLines());
    }
    
    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("testFile.java", "line 1", "line 2");
        
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 2", fileContents.getLine(1));
        assertNull(fileContents.getLine(2));
    }
    
    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("testFile.java", "line 1", "line 2");
        
        assertEquals("testFile.java", fileContents.getFileName());
    }
    
    @Test
    public void testGetFilename() {
        FileContents fileContents = new FileContents("testFile.java", "line 1", "line 2");
        
        assertEquals("testFile.java", fileContents.getFilename());
    }
    
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("testFile.java", "line 1", "", "line 3");
        
        assertFalse(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(1));
        assertTrue(fileContents.lineIsBlank(2));
    }
    
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("testFile.java", "//line 1", "line 2");
        
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("testFile.java", "line 1", "line 2");
        fileContents.reportCppComment(2, 3);
        
        assertTrue(fileContents.hasIntersectionWithComment(2, 3, 2, 4));
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 1));
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", "line 1", "line 2");
        
        assertTrue(fileContents.inPackageInfo());
    }
}