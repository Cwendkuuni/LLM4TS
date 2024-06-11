package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.google.common.collect.ImmutableMap;

public class FileContentsTest {

    @Test
    public void testConstructor_StringArray() {
        String[] lines = {"line 1", "line 2", "line 3"};
        FileContents fileContents = new FileContents("test.java", lines);

        assertEquals("test.java", fileContents.getFileName());
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 2", fileContents.getLine(1));
        assertEquals("line 3", fileContents.getLine(2));
    }

    @Test
    public void testConstructor_FileText() {
        FileText fileText = new FileText();
        FileContents fileContents = new FileContents(fileText);

        assertNotNull(fileContents.getText());
        assertTrue(fileContents.getLines().length == 0);
    }

    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("test.java");
        fileContents.reportSingleLineComment("type", 1, 2);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();

        assertTrue(cppComments.size() == 1);
        assertNotNull(cppComments.get(1));
    }

    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("test.java");
        fileContents.reportBlockComment("type", 1, 2, 3, 4);

        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();

        assertTrue(cComments.size() == 1);
        assertNotNull(cComments.get(1));
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("test.java");
        fileContents.reportBlockComment("/**", 1, 2, 3, 4);
        fileContents.reportBlockComment("/**", 5, 6, 7, 8);

        TextBlock javadocComment = fileContents.getJavadocBefore(6);

        assertNotNull(javadocComment);
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("test.java");
        fileContents.reportBlockComment("/**", 1, 2, 3, 4);

        assertTrue(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("test.java");
        fileContents.reportBlockComment("/**", 1, 2, 3, 4);

        assertTrue(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("test.java");
        fileContents.reportBlockComment("/**", 1, 2, 3, 4);

        assertTrue(fileContents.hasIntersectionWithComment(1, 2, 3, 4));
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java");

        assertTrue(fileContents.inPackageInfo());
    }

}