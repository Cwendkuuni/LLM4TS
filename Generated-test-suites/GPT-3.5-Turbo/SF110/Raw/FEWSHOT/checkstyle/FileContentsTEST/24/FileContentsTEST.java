package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.util.regex.*;
import java.io.*;
import com.google.common.collect.*;
import java.util.*;

public class FileContentsTest {
    @Test
    public void testConstructorLines() {
        FileContents fileContents = new FileContents("filename", "line1", "line2");
        assertEquals("filename", fileContents.getFileName());
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
    }

    @Test
    public void testConstructorFileText() {
        FileText fileText = new FileText(new File("filename"), Arrays.asList("line1", "line2"));
        FileContents fileContents = new FileContents(fileText);
        assertEquals("filename", fileContents.getFileName());
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
    }

    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("filename", "line1", "line2");
        fileContents.reportSingleLineComment("type", 1, 1);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertEquals(1, cppComments.keySet().iterator().next().intValue());
        assertEquals("type", cppComments.values().iterator().next().getType());
        assertEquals(1, cppComments.values().iterator().next().getStartLine());
        assertEquals(1, cppComments.values().iterator().next().getStartColumn());
        assertEquals(4, cppComments.values().iterator().next().getEndColumn());
    }

    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("filename", "line1", "line2");
        fileContents.reportBlockComment("type", 1, 1, 2, 3);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertEquals(1, cComments.keySet().iterator().next().intValue());
        assertEquals("type", cComments.get(1).get(0).getType());
        assertEquals(1, cComments.get(1).get(0).getStartLine());
        assertEquals(1, cComments.get(1).get(0).getStartColumn());
        assertEquals(2, cComments.get(1).get(0).getEndLine());
        assertEquals(3, cComments.get(1).get(0).getEndColumn());
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("filename", "line1", "line2");
        fileContents.reportBlockComment("type", 1, 1, 2, 3);
        fileContents.reportBlockComment("type", 3, 1, 4, 3);
        TextBlock javadocBefore = fileContents.getJavadocBefore(2);
        assertEquals("type", javadocBefore.getType());
        assertEquals(1, javadocBefore.getStartLine());
        assertEquals(1, javadocBefore.getStartColumn());
        assertEquals(2, javadocBefore.getEndLine());
        assertEquals(3, javadocBefore.getEndColumn());
    }

    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("filename", "line1", "line2");
        FileText text = fileContents.getText();
        assertEquals("filename", text.getFile().toString());
        assertEquals("line1", text.get(0));
        assertEquals("line2", text.get(1));
    }

    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("filename", "line1", "line2");
        String[] lines = fileContents.getLines();
        assertEquals(2, lines.length);
        assertEquals("line1", lines[0]);
        assertEquals("line2", lines[1]);
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("filename", "line1", "line2");
        fileContents.reportSingleLineComment("type", 1, 1);
        boolean hasIntersection = fileContents.hasIntersectionWithComment(1, 1, 1, 1);
        assertEquals(true, hasIntersection);
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("filename", "line1", "line2", "package-info.java");
        boolean inPackageInfo = fileContents.inPackageInfo();
        assertEquals(true, inPackageInfo);
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("filename", "line1", "", "line3");
        boolean isBlank = fileContents.lineIsBlank(1);
        assertEquals(false, isBlank);
        isBlank = fileContents.lineIsBlank(2);
        assertEquals(true, isBlank);
        isBlank = fileContents.lineIsBlank(3);
        assertEquals(false, isBlank);
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("filename", "//comment", "line2");
        boolean isComment = fileContents.lineIsComment(1);
        assertEquals(true, isComment);
        isComment = fileContents.lineIsComment(2);
        assertEquals(false, isComment);
    }

    @Test
    public void testExtractCComment() {
        FileContents fileContents = new FileContents("filename", "line1", "line2");
        String[] cComment = fileContents.extractCComment(1, 1, 2, 5);
        assertEquals(2, cComment.length);
        assertEquals("line1", cComment[0]);
        assertEquals("line2", cComment[1]);
    }
}