package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {

    @Test
    public void testReportSingleLineComment() {
        // Test case with valid input
        FileContents fileContents = new FileContents("testFile");
        fileContents.reportSingleLineComment("type", 1, 2);
        // Assert that the cppComments map is updated correctly
        assertTrue(fileContents.getCppComments().containsKey(1));
        assertNotNull(fileContents.getCppComments().get(1));
    }
    
    @Test
    public void testReportBlockComment() {
        // Test case with valid input
        FileContents fileContents = new FileContents("testFile");
        fileContents.reportBlockComment("type", 1, 2, 3, 4);
        // Assert that the clangComments map is updated correctly
        assertTrue(fileContents.getCComments().containsKey(1));
        assertNotNull(fileContents.getCComments().get(1));
    }
    
    @Test
    public void testReportCppComment() {
        // Test case with valid input
        FileContents fileContents = new FileContents("testFile");
        fileContents.reportCppComment(1, 2);
        // Assert that the cppComments map is updated correctly
        assertTrue(fileContents.getCppComments().containsKey(1));
        assertNotNull(fileContents.getCppComments().get(1));
    }
    
    @Test
    public void testGetCppComments() {
        // Test case with valid input
        FileContents fileContents = new FileContents("testFile");
        // Add a cpp comment
        fileContents.reportCppComment(1, 2);
        // Get the cpp comments map
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        // Assert that the cppComments map is correctly returned
        assertNotNull(cppComments);
        assertTrue(cppComments.containsKey(1));
        assertNotNull(cppComments.get(1));
    }
    
    @Test
    public void testReportCComment() {
        // Test case with valid input
        FileContents fileContents = new FileContents("testFile");
        fileContents.reportCComment(1, 2, 3, 4);
        // Assert that the clangComments map is updated correctly
        assertTrue(fileContents.getCComments().containsKey(1));
        assertNotNull(fileContents.getCComments().get(1));
    }
    
    @Test
    public void testGetCComments() {
        // Test case with valid input
        FileContents fileContents = new FileContents("testFile");
        // Add a clang comment
        fileContents.reportCComment(1, 2, 3, 4);
        // Get the clang comments map
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        // Assert that the cComments map is correctly returned
        assertNotNull(cComments);
        assertTrue(cComments.containsKey(1));
        assertNotNull(cComments.get(1));
    }
    
    @Test
    public void testExtractCComment() {
        // Test case with valid input
        FileContents fileContents = new FileContents("testFile");
        String[] cComment = fileContents.extractCComment(1, 2, 3, 4);
        // Assert that the correct cComment is returned
        assertNotNull(cComment);
        assertEquals("line1", cComment[0]);
        assertEquals("line2", cComment[1]);
    }
    
    @Test
    public void testGetJavadocBefore() {
        // Test case with valid input
        FileContents fileContents = new FileContents("testFile");
        // Add a javadoc comment
        fileContents.reportCComment(1, 2, 3, 4);
        // Get the javadoc comment before a specific line
        TextBlock javadocBefore = fileContents.getJavadocBefore(2);
        // Assert that the correct javadoc comment is returned
        assertNotNull(javadocBefore);
    }
    
    @Test
    public void testGetText() {
        // Test case with valid input
        FileContents fileContents = new FileContents("testFile");
        FileText text = fileContents.getText();
        // Assert that the correct FileText object is returned
        assertNotNull(text);
    }
    
    @Test
    public void testGetLines() {
        // Test case with valid input
        FileContents fileContents = new FileContents("testFile", "line1", "line2");
        String[] lines = fileContents.getLines();
        // Assert that the correct lines array is returned
        assertNotNull(lines);
        assertEquals("line1", lines[0]);
        assertEquals("line2", lines[1]);
    }
    
    @Test
    public void testGetLine() {
        // Test case with valid input
        FileContents fileContents = new FileContents("testFile", "line1", "line2");
        String line = fileContents.getLine(1);
        // Assert that the correct line is returned
        assertNotNull(line);
        assertEquals("line2", line);
    }
    
    @Test
    public void testGetFileName() {
        // Test case with valid input
        FileContents fileContents = new FileContents("testFile");
        String fileName = fileContents.getFileName();
        // Assert that the correct file name is returned
        assertNotNull(fileName);
        assertEquals("testFile", fileName);
    }
    
    @Test
    public void testGetFilename() {
        // Test case with valid input
        FileContents fileContents = new FileContents("testFile");
        String filename = fileContents.getFilename();
        // Assert that the correct filename is returned
        assertNotNull(filename);
        assertEquals("testFile", filename);
    }
    
    @Test
    public void testLineIsBlank() {
        // Test case with valid input
        FileContents fileContents = new FileContents("testFile", "line1", "");
        boolean isBlank = fileContents.lineIsBlank(1);
        // Assert that the correct result is returned
        assertFalse(isBlank);
        isBlank = fileContents.lineIsBlank(2);
        assertTrue(isBlank);
    }
    
    @Test
    public void testLineIsComment() {
        // Test case with valid input
        FileContents fileContents = new FileContents("testFile", "//comment", "line");
        boolean isComment = fileContents.lineIsComment(1);
        // Assert that the correct result is returned
        assertTrue(isComment);
        isComment = fileContents.lineIsComment(2);
        assertFalse(isComment);
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        // Test case with valid input
        FileContents fileContents = new FileContents("testFile");
        // Add a clang comment
        fileContents.reportCComment(1, 2, 3, 4);
        // Check if there is an intersection with the comment
        boolean hasIntersection = fileContents.hasIntersectionWithComment(2, 3, 4, 5);
        // Assert that the correct result is returned
        assertTrue(hasIntersection);
    }
    
    @Test
    public void testInPackageInfo() {
        // Test case with valid input
        FileContents fileContents = new FileContents("package-info.java");
        // Check if the file is a package-info file
        boolean isPackageInfo = fileContents.inPackageInfo();
        // Assert that the correct result is returned
        assertTrue(isPackageInfo);
    }
}