package com.puppycrawl.tools.checkstyle.api;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setup() {
        String[] lines = {
                "// line 1",
                "// line 2",
                "// line 3"
        };
        fileContents = new FileContents("filename.java", lines);
    }

    @Test
    public void testReportCppComment() {
        // Test for a single line comment
        fileContents.reportCppComment(1, 3);
        Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
        Assert.assertEquals(1, cppComments.size());
        Assert.assertTrue(cppComments.containsKey(1));
        TextBlock comment = cppComments.get(1);
        Assert.assertEquals("// line 1", comment.getText()[0]);
        Assert.assertEquals(3, comment.getStartColumnNumber());
        Assert.assertEquals(1, comment.getStartLineNumber());
        Assert.assertEquals(10, comment.getEndColumnNumber());
        
        // Test for a comment that spans multiple lines
        fileContents.reportCppComment(2, 3);
        cppComments = fileContents.getCppComments();
        Assert.assertEquals(2, cppComments.size());
        Assert.assertTrue(cppComments.containsKey(1));
        Assert.assertTrue(cppComments.containsKey(2));
        comment = cppComments.get(2);
        Assert.assertEquals("// line 2", comment.getText()[0]);
        Assert.assertEquals(3, comment.getStartColumnNumber());
        Assert.assertEquals(2, comment.getStartLineNumber());
        Assert.assertEquals(10, comment.getEndColumnNumber());
    }

    @Test
    public void testReportCComment() {
        // Test for a single line comment
        fileContents.reportCComment(1, 3, 1, 10);
        Map<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        Assert.assertEquals(1, cComments.size());
        Assert.assertTrue(cComments.containsKey(1));
        List<TextBlock> comments = cComments.get(1);
        Assert.assertEquals(1, comments.size());
        TextBlock comment = comments.get(0);
        Assert.assertEquals("// line 1", comment.getText()[0]);
        Assert.assertEquals(3, comment.getStartColumnNumber());
        Assert.assertEquals(1, comment.getStartLineNumber());
        Assert.assertEquals(10, comment.getEndColumnNumber());

        // Test for a comment that spans multiple lines
        fileContents.reportCComment(2, 3, 3, 10);
        cComments = fileContents.getCComments();
        Assert.assertEquals(2, cComments.size());
        Assert.assertTrue(cComments.containsKey(1));
        Assert.assertTrue(cComments.containsKey(2));
        comments = cComments.get(2);
        Assert.assertEquals(2, comments.size());
        comment = comments.get(1);
        Assert.assertEquals("// line 3", comment.getText()[0]);
        Assert.assertEquals(3, comment.getStartColumnNumber());
        Assert.assertEquals(2, comment.getStartLineNumber());
        Assert.assertEquals(10, comment.getEndColumnNumber());
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportCComment(1, 3, 1, 10);
        fileContents.reportCComment(2, 3, 2, 10);
        TextBlock javadocComment = fileContents.getJavadocBefore(2);
        Assert.assertNotNull(javadocComment);
        Assert.assertEquals("// line 1", javadocComment.getText()[0]);
        Assert.assertEquals(3, javadocComment.getStartColumnNumber());
        Assert.assertEquals(1, javadocComment.getStartLineNumber());
        Assert.assertEquals(10, javadocComment.getEndColumnNumber());
    }

    @Test
    public void testLineIsBlank() {
        Assert.assertFalse(fileContents.lineIsBlank(0));
        Assert.assertFalse(fileContents.lineIsBlank(1));
        Assert.assertFalse(fileContents.lineIsBlank(2));
        Assert.assertFalse(fileContents.lineIsBlank(3));
    }

    @Test
    public void testLineIsComment() {
        Assert.assertTrue(fileContents.lineIsComment(1));
        Assert.assertTrue(fileContents.lineIsComment(2));
        Assert.assertTrue(fileContents.lineIsComment(3));
    }

    @Test
    public void testHasIntersectionWithComment() {
        boolean hasIntersection = fileContents.hasIntersectionWithComment(1, 1, 2, 5);
        Assert.assertTrue(hasIntersection);
        hasIntersection = fileContents.hasIntersectionWithComment(2, 1, 3, 5);
        Assert.assertTrue(hasIntersection);
        hasIntersection = fileContents.hasIntersectionWithComment(1, 1, 1, 10);
        Assert.assertFalse(hasIntersection);
    }

    @Test
    public void testInPackageInfo() {
        Assert.assertFalse(fileContents.inPackageInfo());
    }

    @Test
    public void testGetText() {
        Assert.assertEquals("// line 1\n// line 2\n// line 3", fileContents.getText().toString());
    }

    @Test
    public void testGetLines() {
        String[] lines = {
                "// line 1",
                "// line 2",
                "// line 3"
        };
        Assert.assertArrayEquals(lines, fileContents.getLines());
    }

    @Test
    public void testGetLine() {
        Assert.assertEquals("// line 1", fileContents.getLine(0));
        Assert.assertEquals("// line 2", fileContents.getLine(1));
        Assert.assertEquals("// line 3", fileContents.getLine(2));
    }

    @Test
    public void testGetFileName() {
        Assert.assertEquals("filename.java", fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        Assert.assertEquals("filename.java", fileContents.getFilename());
    }
}