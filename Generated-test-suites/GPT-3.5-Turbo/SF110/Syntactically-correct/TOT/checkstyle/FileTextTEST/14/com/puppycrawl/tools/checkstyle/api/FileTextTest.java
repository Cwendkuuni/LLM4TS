package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class FileTextTest {

    private FileText fileText;

    @Before
    public void setUp() throws IOException {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        fileText = FileText.fromLines(file, lines);
    }

    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        assertEquals("line 1\nline 2\nline 3\n", fileText.getFullText());
        assertArrayEquals(new String[]{"line 1", "line 2", "line 3"}, fileText.toLinesArray());
    }

    @Test
    public void testConstructorWithFileText() {
        FileText newFileText = new FileText(fileText);
        assertEquals(fileText.getFile(), newFileText.getFile());
        assertEquals(fileText.getCharset(), newFileText.getCharset());
        assertEquals(fileText.getFullText(), newFileText.getFullText());
        assertArrayEquals(fileText.toLinesArray(), newFileText.toLinesArray());
    }

    @Test
    public void testFromLines() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line 1\nline 2\nline 3\n", fileText.getFullText());
        assertArrayEquals(new String[]{"line 1", "line 2", "line 3"}, fileText.toLinesArray());
    }

    @Test
    public void testGetFile() {
        assertEquals(new File("test.txt"), fileText.getFile());
    }

    @Test
    public void testGetCharset() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        assertEquals("line 1\nline 2\nline 3\n", fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        assertArrayEquals(new String[]{"line 1", "line 2", "line 3"}, fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks() {
        int[] lineBreaks = fileText.findLineBreaks();
        assertArrayEquals(new int[]{0, 7, 14, 21}, lineBreaks);
    }

    @Test
    public void testLineColumn() {
        LineColumn lineColumn = fileText.lineColumn(10);
        assertEquals(2, lineColumn.getLine());
        assertEquals(2, lineColumn.getColumn());
    }

    @Test
    public void testGet() {
        assertEquals("line 1", fileText.get(0));
        assertEquals("line 2", fileText.get(1));
        assertEquals("line 3", fileText.get(2));
    }

    @Test
    public void testSize() {
        assertEquals(3, fileText.size());
    }
}