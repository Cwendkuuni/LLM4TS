
package com.puppycrawl.tools.checkstyle.api;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class FileContentsTest {
    private FileContents fileContents;

    @Before
    public void setUp() {
        String[] lines = {"line 1", "line 2", "line 3"};
        fileContents = new FileContents("testFile.java", lines);
    }

    @Test
    public void testConstructorWithFileNameAndLines() {
        assertEquals("testFile.java", fileContents.getFileName());
        assertArrayEquals(new String[]{"line 1", "line 2", "line 3"}, fileContents.getLines());
    }

    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText(new File("testFile.java"));
        FileContents fileContents = new FileContents(fileText);

        assertEquals("testFile.java", fileContents.getFileName());
        assertArrayEquals(new String[]{}, fileContents.getLines());
    }

    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", 1, 0);

        assertEquals(1, fileContents.getCppComments().size());
    }

    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", 1, 0, 2, 0);

        assertEquals(1, fileContents.getCComments().size());
    }

    @Test
    public void testGetCppComments() {
        assertEquals(0, fileContents.getCppComments().size());
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(1, 0);

        assertEquals(1, fileContents.getCppComments().size());
    }

    @Test
    public void testGetCComments() {
        assertEquals(0, fileContents.getCComments().size());
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(1, 0, 2, 0);

        assertEquals(1, fileContents.getCComments().size());
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportCComment(1, 0, 2, 0);

        assertNull(fileContents.getJavadocBefore(1));
        assertNotNull(fileContents.getJavadocBefore(2));
    }

    @Test
    public void testGetText() {
        assertNotNull(fileContents.getText());
    }

    @Test
    public void testGetLines() {
        assertArrayEquals(new String[]{"line 1", "line 2", "line 3"}, fileContents.getLines());
    }

    @Test
    public void testGetLine() {
        assertEquals("line 1", fileContents.getLine(0));
    }

    @Test
    public void testGetFileName() {
        assertEquals("testFile.java", fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        assertEquals("testFile.java", fileContents.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        assertFalse(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
    }

    @Test
    public void testLineIsComment() {
        assertFalse(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 0));
        assertFalse(fileContents.hasIntersectionWithComment(1, 0, 2, 0));
        assertFalse(fileContents.hasIntersectionWithComment(2, 0, 3, 0));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }

}
