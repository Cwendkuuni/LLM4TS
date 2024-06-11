package com.puppycrawl.tools.checkstyle.api;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        String[] lines = {
            "public class TestClass {",
            "    // Single line comment",
            "    /*",
            "     * Multiline comment",
            "     */",
            "    // Another single line comment",
            "    /**",
            "     * Javadoc comment",
            "     */",
            "    public static void main(String[] args) {",
            "        System.out.println(\"Hello, World!\");",
            "    }",
            "}"
        };
        fileContents = new FileContents("TestFile.java", lines);
    }

    @Test
    public void testGetCppComments() {
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertNotNull(cppComments);
        assertEquals(2, cppComments.size());
        assertTrue(cppComments.containsKey(2));
        assertTrue(cppComments.containsKey(6));
    }

    @Test
    public void testGetCComments() {
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertNotNull(cComments);
        assertEquals(1, cComments.size());
    }

    @Test
    public void testGetJavadocBefore() {
        TextBlock javadoc = fileContents.getJavadocBefore(10);
        assertNotNull(javadoc);
    }

    @Test
    public void testGetText() {
        FileText text = fileContents.getText();
        assertNotNull(text);
        assertEquals(13, text.size());
    }

    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        assertNotNull(lines);
        assertEquals(13, lines.length);
    }

    @Test
    public void testGetLine() {
        String line = fileContents.getLine(0);
        assertEquals("public class TestClass {", line);
    }

    @Test
    public void testGetFileName() {
        String fileName = fileContents.getFileName();
        assertEquals("TestFile.java", fileName);
    }

    @Test
    public void testGetFilename() {
        String fileName = fileContents.getFilename();
        assertEquals("TestFile.java", fileName);
    }

    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(1));
        assertTrue(fileContents.lineIsComment(2));
        assertFalse(fileContents.lineIsComment(0));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 0));
        assertTrue(fileContents.hasIntersectionWithComment(1, 0, 2, 0));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }
}