package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.regex.Pattern;
import java.io.File;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public class FileContentsTest {
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("filename", "line 1", "// comment", "line 3");
        fileContents.reportSingleLineComment("type", 2, 1);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertFalse(cppComments.isEmpty());
        assertEquals(1, cppComments.size());
        TextBlock comment = cppComments.get(2);
        assertNotNull(comment);
    }
    
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("filename", "line 1", "/* block comment */", "line 3");
        fileContents.reportBlockComment("type", 2, 1, 2, 19);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertFalse(cComments.isEmpty());
        assertEquals(1, cComments.size());
        List<TextBlock> comments = cComments.get(2);
        assertNotNull(comments);
        assertEquals(1, comments.size());
    }
    
    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents("filename", "line 1", "// cpp comment", "line 3");
        fileContents.reportCppComment(2, 1);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertFalse(cppComments.isEmpty());
        assertEquals(1, cppComments.size());
        TextBlock comment = cppComments.get(2);
        assertNotNull(comment);
    }
    
    @Test
    public void testGetCppComments() {
        FileContents fileContents = new FileContents("filename", "line 1", "// cpp comment", "line 3");
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertNotNull(cppComments);
        assertTrue(cppComments.isEmpty());
    }
    
    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents("filename", "line 1", "/* block comment */", "line 3");
        fileContents.reportCComment(2, 1, 2, 19);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertFalse(cComments.isEmpty());
        assertEquals(1, cComments.size());
        List<TextBlock> comments = cComments.get(2);
        assertNotNull(comments);
        assertEquals(1, comments.size());
    }
    
    @Test
    public void testGetCComments() {
        FileContents fileContents = new FileContents("filename", "line 1", "/* block comment */", "line 3");
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertNotNull(cComments);
        assertTrue(cComments.isEmpty());
    }
    
    @Test
    public void testExtractCComment() {
        FileContents fileContents = new FileContents("filename", "line 1", "/* block comment */", "line 3");
        String[] cComment = fileContents.extractCComment(2, 1, 2, 19);
        assertNotNull(cComment);
        assertEquals(1, cComment.length);
        assertEquals("/* block comment */", cComment[0]);
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("filename", "line 1", "/** javadoc comment */", "line 3");
        TextBlock javadoc = fileContents.getJavadocBefore(3);
        assertNotNull(javadoc);
    }
    
    @Test
    public void testLine() {
        FileContents fileContents = new FileContents("filename", "line 1", "line 2", "line 3");
        String line = fileContents.line(1);
        assertEquals("line 1", line);
    }
    
    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("filename", "line 1", "line 2", "line 3");
        FileText text = fileContents.getText();
        assertNotNull(text);
        assertEquals(3, text.size());
    }
    
    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("filename", "line 1", "line 2", "line 3");
        String[] lines = fileContents.getLines();
        assertNotNull(lines);
        assertEquals(3, lines.length);
    }
    
    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("filename", "line 1", "line 2", "line 3");
        String line = fileContents.getLine(1);
        assertEquals("line 2", line);
    }
    
    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("filename", "line 1", "line 2", "line 3");
        String fileName = fileContents.getFileName();
        assertEquals("filename", fileName);
    }
    
    @Test
    public void testGetFilename() {
        FileContents fileContents = new FileContents("filename", "line 1", "line 2", "line 3");
        String fileName = fileContents.getFilename();
        assertEquals("filename", fileName);
    }
    
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("filename", "line 1", " ", "line 3");
        boolean isBlank = fileContents.lineIsBlank(2);
        assertTrue(isBlank);
    }
    
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("filename", "line 1", "// comment", "line 3");
        boolean isComment = fileContents.lineIsComment(2);
        assertTrue(isComment);
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("filename", "line 1", "/* block comment */", "line 3");
        boolean hasIntersection = fileContents.hasIntersectionWithComment(2, 1, 2, 19);
        assertTrue(hasIntersection);
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("filename", "line 1", "line 2", "line 3");
        boolean inPackageInfo = fileContents.inPackageInfo();
        assertFalse(inPackageInfo);
    }
}