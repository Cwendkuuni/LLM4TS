package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {
    
    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents("test.java", "int a = 5; // comment", "int b = 10; /* block comment */");
        fileContents.reportCppComment(1, 12);
        
        assertEquals(1, fileContents.getCppComments().size());
        assertTrue(fileContents.getCppComments().containsKey(1));
        assertNotNull(fileContents.getCppComments().get(1));
    }
    
    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents("test.java", "/*", " * This is a comment", " */");
        fileContents.reportCComment(1, 0, 3, 2);
        
        assertEquals(1, fileContents.getCComments().size());
        assertTrue(fileContents.getCComments().containsKey(1));
        assertNotNull(fileContents.getCComments().get(1));
    }
    
    @Test
    public void testExtractCComment() {
        FileContents fileContents = new FileContents("test.java", "/* This is a comment", "on multiple lines", " */");
        
        String[] expected = {"/* This is a comment", "on multiple lines", " */"};
        String[] actual = fileContents.extractCComment(1, 0, 3, 1);
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("test.java", "/**", " * Javadoc comment", " */");
        
        assertNull(fileContents.getJavadocBefore(1));
        assertNotNull(fileContents.getJavadocBefore(2));
    }
    
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("test.java", "int a = 5;", "");
        
        assertFalse(fileContents.lineIsBlank(1));
        assertTrue(fileContents.lineIsBlank(2));
    }
    
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("test.java", "// Single line comment", "int a = 5;");
        
        assertTrue(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("test.java", "/* This is a comment */", "int a = 5;");
        
        assertTrue(fileContents.hasIntersectionWithComment(1, 0, 1, 3));
        assertFalse(fileContents.hasIntersectionWithComment(2, 0, 2, 3));
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", "");
        
        assertTrue(fileContents.inPackageInfo());
    }
}