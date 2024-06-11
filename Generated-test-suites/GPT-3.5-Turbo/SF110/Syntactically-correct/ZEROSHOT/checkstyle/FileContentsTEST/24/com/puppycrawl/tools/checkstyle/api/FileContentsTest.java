package com.puppycrawl.tools.checkstyle.api;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FileContentsTest {

    @Test
    public void testConstructorWithLines() {
        String[] lines = {"line 1", "line 2", "line 3"};

        FileContents fileContents = new FileContents("filename", lines);

        Assert.assertNotNull(fileContents);
    }

    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText(new File("filename"), Arrays.asList("line 1", "line 2", "line 3"));

        FileContents fileContents = new FileContents(fileText);

        Assert.assertNotNull(fileContents);
    }

    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("filename");
        fileContents.reportSingleLineComment("", 1, 1);

        Assert.assertNotNull(fileContents);
    }

    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("filename");
        fileContents.reportBlockComment("", 1, 1, 2, 2);

        Assert.assertNotNull(fileContents);
    }

    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents("filename");
        fileContents.reportCppComment(1, 1);

        Assert.assertNotNull(fileContents.getCppComments());
    }

    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents("filename");
        fileContents.reportCComment(1, 1, 2, 2);

        Assert.assertNotNull(fileContents.getCComments());
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("filename");

        TextBlock textBlock = fileContents.getJavadocBefore(1);

        Assert.assertNull(textBlock);
    }

    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("filename");

        FileText fileText = fileContents.getText();

        Assert.assertNotNull(fileText);
    }

    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("filename");

        String[] lines = fileContents.getLines();

        Assert.assertNotNull(lines);
    }

    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("filename");

        String line = fileContents.getLine(0);

        Assert.assertNotNull(line);
    }

    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("filename");

        String fileName = fileContents.getFileName();

        Assert.assertNotNull(fileName);
    }

    @Deprecated
    @Test
    public void testGetFilename() {
        FileContents fileContents = new FileContents("filename");

        String fileName = fileContents.getFilename();

        Assert.assertNotNull(fileName);
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("filename");

        boolean isBlank = fileContents.lineIsBlank(1);

        Assert.assertFalse(isBlank);
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("filename");

        boolean isComment = fileContents.lineIsComment(1);

        Assert.assertFalse(isComment);
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("filename");

        boolean hasIntersection = fileContents.hasIntersectionWithComment(1, 1, 2, 2);

        Assert.assertFalse(hasIntersection);
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("filename");

        boolean inPackageInfo = fileContents.inPackageInfo();

        Assert.assertFalse(inPackageInfo);
    }
}