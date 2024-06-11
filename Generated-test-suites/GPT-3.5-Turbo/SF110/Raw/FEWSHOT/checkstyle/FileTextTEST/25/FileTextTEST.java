
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileTextTest {

    private FileText fileText;

    @Before
    public void setUp() throws IOException {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("First line", "Second line", "Third line");

        fileText = FileText.fromLines(file, lines);
    }

    @After
    public void tearDown() {
        fileText = null;
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
        String expected = "First line\nSecond line\nThird line";
        CharSequence actual = fileText.getFullText();
        assertEquals(expected, actual);
    }

    @Test
    public void testToLinesArray() {
        String[] expected = { "First line", "Second line", "Third line" };
        String[] actual = fileText.toLinesArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testLineColumn() {
        LineColumn expected = new LineColumn(2, 5);
        LineColumn actual = fileText.lineColumn(12);
        assertEquals(expected, actual);
    }

    @Test
    public void testGet() {
        String expected = "Second line";
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
