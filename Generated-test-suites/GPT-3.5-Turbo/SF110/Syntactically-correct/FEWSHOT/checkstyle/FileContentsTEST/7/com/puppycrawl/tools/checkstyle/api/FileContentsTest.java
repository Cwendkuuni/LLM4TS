package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import java.util.*;

public class FileContentsTest {
    @Test
    public void testConstructorWithLines() {
        FileContents fileContents = new FileContents("test.txt", "line1", "line2");
        assertEquals("test.txt", fileContents.getFileName());
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
        assertNull(fileContents.getLine(2));
    }
    
    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText(new File("test.txt"));
        FileContents fileContents = new FileContents(fileText);
        assertEquals("test.txt", fileContents.getFileName());
        assertEquals(fileText, fileContents.getText());
    }
    
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("test.txt", "line1", "line2");
        fileContents.reportSingleLineComment("", 1, 0);
        fileContents.reportSingleLineComment("", 2, 0);
        Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(2, cppComments.size());
        TextBlock comment1 = cppComments.get(1);
        TextBlock comment2 = cppComments.get(2);
        assertEquals("line1", comment1.getLines()[0]);
        assertEquals("line2", comment2.getLines()[0]);
    }
    
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("test.txt", "line1", "line2");
        fileContents.reportBlockComment("", 1, 0, 1, 4);
        fileContents.reportBlockComment("", 2, 0, 2, 4);
        Map<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(2, cComments.size());
        List<TextBlock> comments1 = cComments.get(1);
        List<TextBlock> comments2 = cComments.get(2);
        assertEquals(1, comments1.size());
        assertEquals(1, comments2.size());
        TextBlock comment1 = comments1.get(0);
        TextBlock comment2 = comments2.get(0);
        assertEquals("line1", comment1.getLines()[0]);
        assertEquals("line2", comment2.getLines()[0]);
    }
    
    @Test
    public void testExtractCComment() {
        FileContents fileContents = new FileContents("test.txt", "line1", "line2");
        String[] cComment = fileContents.extractCComment(1, 1, 2, 2);
        assertEquals(2, cComment.length);
        assertEquals("line1", cComment[0]);
        assertEquals("line2", cComment[1]);
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("test.txt", "line1", "line2");
        fileContents.reportBlockComment("", 1, 0, 1, 4);
        fileContents.reportBlockComment("", 2, 0, 2, 4);
        TextBlock javadocComment = fileContents.getJavadocBefore(2);
        assertNull(javadocComment);
    }
    
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("test.txt", "line1", "", "line3");
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
        assertFalse(fileContents.lineIsBlank(3));
    }
    
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("test.txt", "// comment", "line2");
        assertTrue(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("test.txt", "line1", "line2");
        fileContents.reportBlockComment("", 1, 0, 1, 4);
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 1, 3));
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 1, 5));
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", "line1");
        assertTrue(fileContents.inPackageInfo());
        fileContents = new FileContents("file.txt", "line1");
        assertFalse(fileContents.inPackageInfo());
    }
    
    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("test.txt", "line1", "line2");
        String[] lines = fileContents.getLines();
        assertEquals(2, lines.length);
        assertArrayEquals(new String[] {"line1", "line2"}, lines);
    }
}