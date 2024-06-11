
package com.puppycrawl.tools.checkstyle.api;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setup() {
        String[] lines = {"line 1", "line 2", "line 3"};
        fileContents = new FileContents("test.txt", lines);
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(2, 2);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        Assert.assertNotNull(cppComments);
        Assert.assertTrue(cppComments.containsKey(2));
        Assert.assertEquals("line 2", cppComments.get(2).getText());
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(1, 0, 2, 5);

        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        Assert.assertNotNull(cComments);
        Assert.assertTrue(cComments.containsKey(1));
        List<TextBlock> comments = cComments.get(1);
        Assert.assertEquals(1, comments.size());
        Assert.assertEquals("line 1", comments.get(0).getText());
        Assert.assertEquals(0, comments.get(0).getStartColumn());
        Assert.assertEquals(2, comments.get(0).getEndLine());
        Assert.assertEquals(5, comments.get(0).getEndColumn());
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportCComment(1, 0, 2, 5);
        fileContents.reportCComment(3, 0, 4, 10);
        fileContents.reportCComment(5, 0, 6, 8);

        TextBlock javadocBefore = fileContents.getJavadocBefore(4);
        Assert.assertNotNull(javadocBefore);
        Assert.assertEquals("line 1", javadocBefore.getText());
        Assert.assertEquals(0, javadocBefore.getStartColumn());
        Assert.assertEquals(2, javadocBefore.getEndLine());
        Assert.assertEquals(5, javadocBefore.getEndColumn());
    }

    @Test
    public void testGetText() {
        FileText text = fileContents.getText();
        Assert.assertNotNull(text);
        Assert.assertEquals("line 1", text.get(0));
    }

    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        Assert.assertNotNull(lines);
        Assert.assertEquals(3, lines.length);
        Assert.assertEquals("line 2", lines[1]);
    }

    @Test
    public void testGetLine() {
        String line = fileContents.getLine(0);
        Assert.assertNotNull(line);
        Assert.assertEquals("line 1", line);
    }

    @Test
    public void testGetFileName() {
        String fileName = fileContents.getFileName();
        Assert.assertNotNull(fileName);
        Assert.assertEquals("test.txt", fileName);
    }

    @Test
    public void testLineIsBlank() {
        boolean isBlank = fileContents.lineIsBlank(0);
        Assert.assertFalse(isBlank);
    }

    @Test
    public void testLineIsComment() {
        boolean isComment = fileContents.lineIsComment(0);
        Assert.assertFalse(isComment);
    }

    @Test
    public void testHasIntersectionWithComment() {
        boolean hasIntersection = fileContents.hasIntersectionWithComment(0, 0, 1, 5);
        Assert.assertFalse(hasIntersection);
    }

    @Test
    public void testInPackageInfo() {
        boolean inPackageInfo = fileContents.inPackageInfo();
        Assert.assertFalse(inPackageInfo);
    }
}
