
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FileTextTest {

    private FileText fileText;

    @Before
    public void setUp() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        fileText = FileText.fromLines(file, lines);
    }

    @Test
    public void testGetFile() {
        File expected = new File("testFile.txt");
        File actual = fileText.getFile();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCharset() {
        assertNull(fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        String expected = "line 1\nline 2\nline 3\n";
        CharSequence actual = fileText.getFullText();
        assertEquals(expected, actual);
    }

    @Test
    public void testToLinesArray() {
        String[] expected = {"line 1", "line 2", "line 3"};
        String[] actual = fileText.toLinesArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testLineColumn() {
        LineColumn expected = new LineColumn(2, 6);
        LineColumn actual = fileText.lineColumn(11);
        assertEquals(expected, actual);
    }

    @Test
    public void testGet() {
        String expected = "line 2";
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
