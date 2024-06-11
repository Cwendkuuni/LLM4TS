package com.puppycrawl.tools.checkstyle.api;

import org.junit.*;
import junit.framework.*;

import java.nio.charset.UnsupportedCharsetException;
import java.util.*;
import java.io.*;

public class FileTextTest {

    private FileText fileText;

    @Before
    public void setUp() throws IOException {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");
        this.fileText = FileText.fromLines(file, lines);
    }

    @Test
    public void testGetFile() {
        File expected = new File("test.txt");
        File actual = fileText.getFile();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetCharset() {
        Charset expected = null;
        Charset actual = fileText.getCharset();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetFullText() {
        CharSequence expected = "Line 1\nLine 2\nLine 3\n";
        CharSequence actual = fileText.getFullText();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToLinesArray() {
        String[] expected = {"Line 1", "Line 2", "Line 3"};
        String[] actual = fileText.toLinesArray();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testLineColumn() {
        LineColumn expected = new LineColumn(2, 3);
        LineColumn actual = fileText.lineColumn(9);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGet() {
        String expected = "Line 2";
        String actual = fileText.get(1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSize() {
        int expected = 3;
        int actual = fileText.size();
        Assert.assertEquals(expected, actual);
    }
}