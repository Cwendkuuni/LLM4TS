package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FileTextTest {

    private static final String FILE_PATH = "path/to/test/file.txt";
    private static final String CHARSET_NAME = "UTF-8";

    private FileText fileText;

    @Before
    public void setUp() throws IOException {
        File testFile = new File(FILE_PATH);
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");
        this.fileText = FileText.fromLines(testFile, lines);
    }

    @Test
    public void testConstructorFromFile_validFile_success() throws IOException {
        File validFile = new File(FILE_PATH);
        FileText ft = new FileText(validFile, CHARSET_NAME);
        assertNotNull(ft);
        assertEquals(validFile, ft.getFile());
        assertEquals(CHARSET_NAME, ft.getCharset().name());
        assertTrue(ft.size() > 0);
        assertFalse(ft.getFullText().toString().isEmpty());
        assertFalse(ft.toLinesArray()[0].isEmpty());
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructorFromFile_invalidFile_exception() throws IOException {
        File invalidFile = new File("invalid_file.txt");
        new FileText(invalidFile, CHARSET_NAME);
    }

    @Test
    public void testConstructorFromLines_validLines_success() {
        File testFile = new File(FILE_PATH);
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");
        FileText ft = FileText.fromLines(testFile, lines);
        assertNotNull(ft);
        assertEquals(testFile, ft.getFile());
        assertNull(ft.getCharset());
        assertTrue(ft.size() > 0);
        assertFalse(ft.getFullText().toString().isEmpty());
        assertFalse(ft.toLinesArray()[0].isEmpty());
    }

    @Test
    public void testFileTextCopyConstructor_validFileText_success() {
        FileText copy = new FileText(fileText);
        assertNotSame(copy, fileText);
        assertEquals(fileText.getFile(), copy.getFile());
        assertEquals(fileText.getCharset(), copy.getCharset());
        assertEquals(fileText.getFullText(), copy.getFullText());
        assertArrayEquals(fileText.toLinesArray(), copy.toLinesArray());
    }

    @Test
    public void testLineColumn_validPosition_success() {
        int position = 6; // pos 6 belongs to line 1
        LineColumn lineColumn = fileText.lineColumn(position);
        assertEquals(1, lineColumn.getLineNo());
        assertEquals(6, lineColumn.getColumnNo());
    }

    @Test
    public void testLineColumn_positionAtLineBreak_success() {
        int position = 7; // pos 7 is a line break, should return line 1 and column 6
        LineColumn lineColumn = fileText.lineColumn(position);
        assertEquals(1, lineColumn.getLineNo());
        assertEquals(6, lineColumn.getColumnNo());
    }

    @Test
    public void testLineColumn_positionGreaterThanLastLine_success() {
        int position = 30; // pos 30 is greater than last line, should return last line and column
        LineColumn lineColumn = fileText.lineColumn(position);
        assertEquals(fileText.size(), lineColumn.getLineNo());
        assertEquals(0, lineColumn.getColumnNo());
    }

    @Test
    public void testGet_validLineNo_success() {
        String line = fileText.get(1); // line index starts from 0, so line 2 is at index 1
        assertEquals("Line 2", line);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_invalidLineNo_exception() {
        fileText.get(fileText.size()); // expected size is 3, so line 4 should throw an exception
    }

    @Test
    public void testSize_validFileText_success() {
        int size = fileText.size();
        assertEquals(3, size);
    }

    @Test
    public void testSize_validEmptyFileText_success() throws IOException {
        File emptyFile = new File("path/to/empty_file.txt");
        FileText emptyFileText = new FileText(emptyFile, CHARSET_NAME);
        int size = emptyFileText.size();
        assertEquals(0, size);
    }

    @Test
    public void testLineBreaks_validFileText_success() {
        int[] lineBreaks = fileText.findLineBreaks();
        assertNotNull(lineBreaks);
        assertEquals(fileText.size() + 1, lineBreaks.length);
        assertEquals(0, lineBreaks[0]);
        assertEquals(7, lineBreaks[1]);
        assertEquals(14, lineBreaks[2]);
        assertEquals(21, lineBreaks[3]);
    }

    @Test
    public void testLineBreaks_validEmptyFileText_success() throws IOException {
        File emptyFile = new File("path/to/empty_file.txt");
        FileText emptyFileText = new FileText(emptyFile, CHARSET_NAME);
        int[] lineBreaks = emptyFileText.findLineBreaks();
        assertNotNull(lineBreaks);
        assertEquals(1, lineBreaks.length);
        assertEquals(0, lineBreaks[0]);
    }

    @Test
    public void testLineBreaks_cachedData_success() {
        int[] lineBreaks1 = fileText.findLineBreaks();
        int[] lineBreaks2 = fileText.findLineBreaks();
        assertSame(lineBreaks1, lineBreaks2);
    }
    
    // Additional test cases can be added for each method to cover more scenarios and edge cases

}