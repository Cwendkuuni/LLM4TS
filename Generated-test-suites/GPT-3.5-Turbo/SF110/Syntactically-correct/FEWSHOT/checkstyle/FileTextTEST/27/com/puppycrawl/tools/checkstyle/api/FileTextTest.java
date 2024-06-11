
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

    private static final String TEST_FILE_PATH = "testFile.txt";

    @Test
    public void testFileText() throws IOException {
        FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
        Assert.assertEquals(TEST_FILE_PATH, fileText.getFile().getPath());
        Assert.assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTextWithUnsupportedCharset() throws IOException {
        new FileText(new File(TEST_FILE_PATH), "unsupportedCharset");
    }

    @Test
    public void testFileTextCopy() throws IOException {
        FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
        FileText copy = new FileText(fileText);
        Assert.assertEquals(fileText.getFile(), copy.getFile());
        Assert.assertEquals(fileText.getCharset(), copy.getCharset());
    }

    @Test
    public void testFromLines() {
        File file = new File(TEST_FILE_PATH);
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        FileText fileText = FileText.fromLines(file, lines);
        Assert.assertEquals(file, fileText.getFile());
        Assert.assertNull(fileText.getCharset());
        Assert.assertEquals(lines.size(), fileText.size());
        Assert.assertEquals(lines.get(0), fileText.get(0));
        Assert.assertEquals(lines.get(1), fileText.get(1));
        Assert.assertEquals(lines.get(2), fileText.get(2));
    }

    @Test
    public void testGetFullText() throws IOException {
        String expectedText = "line1\nline2\nline3\n";
        FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
        Assert.assertEquals(expectedText, fileText.getFullText().toString());
    }

    @Test
    public void testToLinesArray() throws IOException {
        String[] expectedLines = {"line1", "line2", "line3"};
        FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
        Assert.assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() throws IOException {
        FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
        FileText.LineColumn lineColumn = fileText.lineColumn(5);
        Assert.assertEquals(1, lineColumn.getLineNo());
        Assert.assertEquals(4, lineColumn.getColumnNo());
    }

    @Test
    public void testGet() throws IOException {
        FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
        Assert.assertEquals("line1", fileText.get(0));
        Assert.assertEquals("line2", fileText.get(1));
        Assert.assertEquals("line3", fileText.get(2));
    }

    @Test
    public void testSize() throws IOException {
        FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
        Assert.assertEquals(3, fileText.size());
    }
}
