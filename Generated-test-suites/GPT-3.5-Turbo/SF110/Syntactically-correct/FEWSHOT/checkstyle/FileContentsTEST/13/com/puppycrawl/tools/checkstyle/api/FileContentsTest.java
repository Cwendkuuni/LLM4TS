package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class FileContentsTest {
    
    private FileContents fileContents;
    
    @Before
    public void setup() {
        fileContents = new FileContents("filename", "line1", "line2", "line3");
    }
    
    @Test
    public void testConstructorWithFileNameAndLines() {
        assertEquals("filename", fileContents.getFileName());
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
        assertEquals("line3", fileContents.getLine(2));
    }
    
    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText(new File(""));
        FileContents fileContents = new FileContents(fileText);
        
        assertEquals(fileText, fileContents.getText());
    }
    
    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", 1, 1);
        
        assertTrue(fileContents.getCppComments().containsKey(1));
        assertNotNull(fileContents.getCppComments().get(1));
        assertEquals("type", fileContents.getCppComments().get(1).getType());
        assertEquals(1, fileContents.getCppComments().get(1).getStartLineNo());
        assertEquals(1, fileContents.getCppComments().get(1).getStartColNo());
        assertEquals("line1", fileContents.getCppComments().get(1).getText()[0]);
    }
    
    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        
        assertTrue(fileContents.getCComments().containsKey(1));
        assertNotNull(fileContents.getCComments().get(1));
        assertEquals("type", fileContents.getCComments().get(1).get(0).getType());
        assertEquals(1, fileContents.getCComments().get(1).get(0).getStartLineNo());
        assertEquals(1, fileContents.getCComments().get(1).get(0).getStartColNo());
        assertEquals(2, fileContents.getCComments().get(1).get(0).getEndLineNo());
        assertEquals(2, fileContents.getCComments().get(1).get(0).getEndColNo());
        assertEquals("line1", fileContents.getCComments().get(1).get(0).getText()[0]);
        assertEquals("line2", fileContents.getCComments().get(1).get(0).getText()[1]);
    }
    
    @Test
    public void testGetJavadocBefore() {
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        TextBlock comment = fileContents.getJavadocBefore(2);
        
        assertNotNull(comment);
        assertEquals("type", comment.getType());
        assertEquals(1, comment.getStartLineNo());
        assertEquals(1, comment.getStartColNo());
        assertEquals(2, comment.getEndLineNo());
        assertEquals(2, comment.getEndColNo());
        assertEquals("line1", comment.getText()[0]);
        assertEquals("line2", comment.getText()[1]);
    }
    
    @Test
    public void testGetText() {
        FileText fileText = fileContents.getText();
        
        assertEquals(fileText, fileContents.getText());
    }
    
    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        
        assertEquals("line1", lines[0]);
        assertEquals("line2", lines[1]);
        assertEquals("line3", lines[2]);
    }
    
    @Test
    public void testGetLine() {
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
        assertEquals("line3", fileContents.getLine(2));
    }
    
    @Test
    public void testGetFileName() {
        assertEquals("filename", fileContents.getFileName());
    }
    
    @Test
    public void testGetFilename() {
        assertEquals("filename", fileContents.getFilename());
    }
    
    @Test
    public void testLineIsBlank() {
        assertFalse(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
    }
    
    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 2, 2));
    }
    
    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }
}