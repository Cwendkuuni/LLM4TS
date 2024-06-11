package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class FileTextTest {

    private FileText fileText;

    @Before
    public void setUp() throws Exception {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");

        fileText = new FileText(file, lines);
    }

    @Test
    public void testGetFile() {
        File expected = new File("test.txt");
        File actual = fileText.getFile();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCharset() {
        assertNull(fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        String expected = "Line 1\nLine 2\nLine 3\n";
        CharSequence actual = fileText.getFullText();
        assertEquals(expected, actual);
    }

    @Test
    public void testToLinesArray() {
        String[] expected = {"Line 1", "Line 2", "Line 3"};
        String[] actual = fileText.toLinesArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testLineColumn() {
        LineColumn expected = new LineColumn(1, 5);
        LineColumn actual = fileText.lineColumn(5);
        assertEquals(expected, actual);
    }

    @Test
    public void testGet() {
        String expected = "Line 2";
        String actual = fileText.get(1);
        assertEquals(expected, actual);
    }

    @Test
    public void testSize() {
        int expected = 3;
        int actual = fileText.size();
        assertEquals(expected, actual);
    }
}