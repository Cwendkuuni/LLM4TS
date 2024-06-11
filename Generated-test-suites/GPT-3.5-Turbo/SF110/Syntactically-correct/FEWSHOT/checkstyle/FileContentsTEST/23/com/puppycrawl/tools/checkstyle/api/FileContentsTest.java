package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class FileContentsTest {
    // Create a FileContents object for testing
    private FileContents fileContents;
    private String[] lines = {"// this is a comment", "public class MyClass {", "    // another comment", "    private int myVariable;", "}"};
    
    @Before
    public void setUp() {
        fileContents = new FileContents("TestFile.java", lines);
    }
    
    @Test
    public void testGetFileName() {
        assertEquals("TestFile.java", fileContents.getFileName());
    }
    
    @Test
    public void testGetLines() {
        assertEquals(lines, fileContents.getLines());
    }
    
    @Test
    public void testGetLine() {
        assertEquals("// this is a comment", fileContents.getLine(0));
        assertEquals("public class MyClass {", fileContents.getLine(1));
        assertEquals("// another comment", fileContents.getLine(2));
        assertEquals("private int myVariable;", fileContents.getLine(3));
        assertEquals("}", fileContents.getLine(4));
    }
    
    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertTrue(fileContents.lineIsBlank(2));
        assertFalse(fileContents.lineIsBlank(3));
        assertFalse(fileContents.lineIsBlank(4));
    }

    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertTrue(fileContents.lineIsComment(2));
        assertFalse(fileContents.lineIsComment(3));
        assertFalse(fileContents.lineIsComment(4));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 0));
        assertTrue(fileContents.hasIntersectionWithComment(1, 0, 4, 1));

    }
    
    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }
  
}