
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

    private static final File TEST_FILE = new File("test.txt");
    private static final String CHARSET_NAME = "UTF-8";

    @Test
    public void testConstructorWithFileAndCharsetName() throws Exception {
        FileText fileText = new FileText(TEST_FILE, CHARSET_NAME);
        assertEquals(TEST_FILE, fileText.getFile());
        assertEquals(CHARSET_NAME, fileText.getCharset().name());
        assertEquals("This is a test file.", fileText.getFullText().toString());
        assertArrayEquals(new String[]{"This is a test file."}, fileText.toLinesArray());
        assertEquals("This is a test file.", fileText.get(0));
        assertEquals(1, fileText.size());
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructorWithNonexistentFile() throws Exception {
        FileText fileText = new FileText(new File("invalid.txt"), CHARSET_NAME);
    }

    @Test
    public void testConstructorWithFileText() {
        FileText originalFileText = new FileText(TEST_FILE, CHARSET_NAME);
        FileText copyFileText = new FileText(originalFileText);
        assertEquals(originalFileText.getFile(), copyFileText.getFile());
        assertEquals(originalFileText.getCharset(), copyFileText.getCharset());
        assertEquals(originalFileText.getFullText(), copyFileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), copyFileText.toLinesArray());
        assertEquals(originalFileText.get(0), copyFileText.get(0));
        assertEquals(originalFileText.size(), copyFileText.size());
    }

    @Test
    public void testFromLines() {
        List<String> lines = new ArrayList<>();
        lines.add("First line");
        lines.add("Second line");
        lines.add("Third line");
        FileText fileText = FileText.fromLines(TEST_FILE, lines);
        assertEquals(TEST_FILE, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("First line\nSecond line\nThird line\n", fileText.getFullText().toString());
        assertArrayEquals(new String[]{"First line", "Second line", "Third line"}, fileText.toLinesArray());
        assertEquals("First line", fileText.get(0));
        assertEquals(3, fileText.size());
    }

    @Test
    public void testLineColumn() throws Exception {
        FileText fileText = new FileText(TEST_FILE, CHARSET_NAME);
        LineColumn lineColumn = fileText.lineColumn(0);
        assertEquals(1, lineColumn.getLine());
        assertEquals(0, lineColumn.getColumn());
    }

    @Test
    public void testGet() throws Exception {
        FileText fileText = new FileText(TEST_FILE, CHARSET_NAME);
        assertEquals("This is a test file.", fileText.get(0));
    }

    @Test
    public void testSize() throws Exception {
        FileText fileText = new FileText(TEST_FILE, CHARSET_NAME);
        assertEquals(1, fileText.size());
    }
}
