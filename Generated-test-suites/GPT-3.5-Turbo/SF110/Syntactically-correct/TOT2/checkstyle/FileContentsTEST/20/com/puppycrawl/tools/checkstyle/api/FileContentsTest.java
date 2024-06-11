package com.puppycrawl.tools.checkstyle.api;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        fileContents = new FileContents("testfile.java", "line1", "line2", "line3");
    }

    @Test
    public void testGetFileName() {
        assertEquals("testfile.java", fileContents.getFileName());
    }

    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        assertNotNull(lines);
        assertEquals(3, lines.length);
        assertEquals("line1", lines[0]);
        assertEquals("line2", lines[1]);
        assertEquals("line3", lines[2]);
    }

    // Add more test cases for other methods

}