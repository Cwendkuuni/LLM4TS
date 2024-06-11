package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FileContentsTest {
    private static final String[] LINES = {
            "/*",
            " * This is a multi-line comment",
            " */",
            "", // empty line
            "// This is a single-line comment",
            "class MyClass {",
            "    private int myVariable;",
            "}" };

    private static final String FILENAME = "TestFile.java";
    
    @Test
    public void testConstructorWithLines() {
        FileContents fileContents = new FileContents(FILENAME, LINES);
        assertEquals(FILENAME, fileContents.getFileName());
        assertEquals(LINES.length, fileContents.getLines().length);
        assertEquals(LINES[0], fileContents.getLine(0));
        assertEquals(LINES[1], fileContents.getLine(1));
        assertEquals(LINES[2], fileContents.getLine(2));
        assertEquals(LINES[3], fileContents.getLine(3));
        assertEquals(LINES[4], fileContents.getLine(4));
        assertEquals(LINES[5], fileContents.getLine(5));
        assertEquals(LINES[6], fileContents.getLine(6));
        assertEquals(LINES[7], fileContents.getLine(7));
    }
    
    @Test
    public void testConstructorWithFileText() {
        FileText fileText = FileText.fromLines(new File(FILENAME), Arrays.asList(LINES));
        FileContents fileContents = new FileContents(fileText);
        assertEquals(FILENAME, fileContents.getFileName());
        assertEquals(LINES.length, fileContents.getLines().length);
        assertEquals(LINES[0], fileContents.getLine(0));
        assertEquals(LINES[1], fileContents.getLine(1));
        assertEquals(LINES[2], fileContents.getLine(2));
        assertEquals(LINES[3], fileContents.getLine(3));
        assertEquals(LINES[4], fileContents.getLine(4));
        assertEquals(LINES[5], fileContents.getLine(5));
        assertEquals(LINES[6], fileContents.getLine(6));
        assertEquals(LINES[7], fileContents.getLine(7));
    }
    
    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents(FILENAME, LINES);
        assertEquals(FILENAME, fileContents.getFileName());
    }
    
    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents(FILENAME, LINES);
        String[] lines = fileContents.getLines();
        assertEquals(LINES.length, lines.length);
        assertEquals(LINES[0], lines[0]);
        assertEquals(LINES[1], lines[1]);
        assertEquals(LINES[2], lines[2]);
        assertEquals(LINES[3], lines[3]);
        assertEquals(LINES[4], lines[4]);
        assertEquals(LINES[5], lines[5]);
        assertEquals(LINES[6], lines[6]);
        assertEquals(LINES[7], lines[7]);
    }
    
    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents(FILENAME, LINES);
        assertEquals(LINES[0], fileContents.getLine(0));
        assertEquals(LINES[1], fileContents.getLine(1));
        assertEquals(LINES[2], fileContents.getLine(2));
        assertEquals(LINES[3], fileContents.getLine(3));
        assertEquals(LINES[4], fileContents.getLine(4));
        assertEquals(LINES[5], fileContents.getLine(5));
        assertEquals(LINES[6], fileContents.getLine(6));
        assertEquals(LINES[7], fileContents.getLine(7));
    }
    
    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents(FILENAME, LINES);
        FileText fileText = fileContents.getText();
        assertEquals(FILENAME, fileText.getFile().toString());
        assertEquals(LINES.length, fileText.toLinesArray().length);
        assertEquals(LINES[0], fileText.get(0));
        assertEquals(LINES[1], fileText.get(1));
        assertEquals(LINES[2], fileText.get(2));
        assertEquals(LINES[3], fileText.get(3));
        assertEquals(LINES[4], fileText.get(4));
        assertEquals(LINES[5], fileText.get(5));
        assertEquals(LINES[6], fileText.get(6));
        assertEquals(LINES[7], fileText.get(7));
    }
    
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents(FILENAME, LINES);
        assertFalse(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
        assertTrue(fileContents.lineIsBlank(3));
        assertFalse(fileContents.lineIsBlank(4));
    }
    
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents(FILENAME, LINES);
        assertFalse(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertTrue(fileContents.lineIsComment(4));
        assertFalse(fileContents.lineIsComment(5));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents(FILENAME, LINES);
        assertTrue(fileContents.hasIntersectionWithComment(0, 0, 2, 2));
        assertFalse(fileContents.hasIntersectionWithComment(2, 2, 4, 4));
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents(FILENAME, LINES);
        assertFalse(fileContents.inPackageInfo());
    }
    
    @Test
    public void testGetCppComments() {
        FileContents fileContents = new FileContents(FILENAME, LINES);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertTrue(cppComments.isEmpty());
    }
    
    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents(FILENAME, LINES);
        fileContents.reportCppComment(4, 2);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertEquals("This is a single-line comment", cppComments.get(4).getText()[0]);
    }
    
    @Test
    public void testGetCComments() {
        FileContents fileContents = new FileContents(FILENAME, LINES);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertTrue(cComments.isEmpty());
    }
    
    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents(FILENAME, LINES);
        fileContents.reportCComment(1, 2, 2, 3);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertEquals(1, cComments.get(1).size());
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents(FILENAME, LINES);
        assertNull(fileContents.getJavadocBefore(0));
        assertNull(fileContents.getJavadocBefore(1));
        assertNull(fileContents.getJavadocBefore(2));
    }
    
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents(FILENAME, LINES);
        fileContents.reportBlockComment("", 1, 2, 2, 3);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertEquals(1, cComments.get(1).size());
        ImmutableMap<Integer, TextBlock> javadocComments = fileContents.getJavadocComments();
        assertEquals(1, javadocComments.size());
    }
    
    @Test
    public void testGetJavadocComments() {
        FileContents fileContents = new FileContents(FILENAME, LINES);
        ImmutableMap<Integer, TextBlock> javadocComments = fileContents.getJavadocComments();
        assertTrue(javadocComments.isEmpty());
    }
}