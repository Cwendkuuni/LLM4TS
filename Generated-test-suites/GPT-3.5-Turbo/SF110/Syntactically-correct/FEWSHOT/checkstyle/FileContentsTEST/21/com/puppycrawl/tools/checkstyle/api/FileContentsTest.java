
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.TextBlock;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class FileContentsTest {

    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("filename", "//Single line comment");
        fileContents.reportSingleLineComment("", 1, 1);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertEquals("//Single line comment", cppComments.get(1));
    }

    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("filename", "/*Block comment*/");
        fileContents.reportBlockComment("", 1, 1, 1, 18);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertEquals("/*Block comment*/", cComments.get(1).get(0));
    }

    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents("filename", "//Cpp comment");
        fileContents.reportCppComment(1, 1);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertEquals("//Cpp comment", cppComments.get(1));
    }

    @Test
    public void testGetCppComments() {
        FileContents fileContents = new FileContents("filename", "//Cpp comment");
        fileContents.reportCppComment(1, 1);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertEquals("//Cpp comment", cppComments.get(1));
    }

    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents("filename", "/*C comment*/");
        fileContents.reportCComment(1, 1, 1, 13);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertEquals("/*C comment*/", cComments.get(1).get(0));
    }

    @Test
    public void testGetCComments() {
        FileContents fileContents = new FileContents("filename", "/*C comment*/");
        fileContents.reportCComment(1, 1, 1, 13);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertEquals("/*C comment*/", cComments.get(1).get(0));
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("filename", "/*C comment*/");
        fileContents.reportCComment(1, 1, 1, 13);
        TextBlock javadocComment = fileContents.getJavadocBefore(2);
        assertNull(javadocComment);
    }

    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("filename", "Line 1", "Line 2");
        assertEquals("Line 1\nLine 2", fileContents.getText().toString());
    }

    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("filename", "Line 1", "Line 2");
        assertEquals("Line 1", fileContents.getLines()[0]);
        assertEquals("Line 2", fileContents.getLines()[1]);
    }

    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("filename", "Line 1", "Line 2");
        assertEquals("Line 1", fileContents.getLine(0));
        assertEquals("Line 2", fileContents.getLine(1));
    }

    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("filename");
        assertEquals("filename", fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        FileContents fileContents = new FileContents("filename");
        assertEquals("filename", fileContents.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("filename", "", "Line 2");
        assertEquals(true, fileContents.lineIsBlank(0));
        assertEquals(false, fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("filename", "//Comment");
        assertEquals(true, fileContents.lineIsComment(0));
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("filename", "//Comment");
        assertEquals(true, fileContents.hasIntersectionWithComment(1, 1, 1, 5));
        assertEquals(false, fileContents.hasIntersectionWithComment(2, 1, 2, 5));
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java");
        assertEquals(true, fileContents.inPackageInfo());
    }
}
