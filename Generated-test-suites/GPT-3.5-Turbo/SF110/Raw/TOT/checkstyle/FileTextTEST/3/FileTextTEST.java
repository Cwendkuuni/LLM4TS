package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FileTextTest {

    private FileText fileText;
    private File file;
    private String charsetName;

    @Before
    public void setUp() {
        file = new File("test_file.txt");
        charsetName = "UTF-8";
        try {
            fileText = new FileText(file, charsetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor_withFileAndCharsetName_shouldCreateFileTextObject() {
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertEquals(charsetName, fileText.getCharset().name());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertEquals(0, fileText.toLinesArray().length);
        assertEquals(0, fileText.size());
    }

    @Test
    public void testConstructor_withUnsupportedCharsetName_shouldThrowIllegalStateException() {
        try {
            fileText = new FileText(file, "invalid_charset");
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Unsupported charset: invalid_charset", e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }

    @Test
    public void testConstructor_withNonExistentFile_shouldThrowFileNotFoundException() {
        File nonExistentFile = new File("non_existent_file.txt");
        try {
            fileText = new FileText(nonExistentFile, charsetName);
            fail("Expected FileNotFoundException to be thrown");
        } catch (FileNotFoundException e) {
            assertEquals("non_existent_file.txt (No such file or directory)", e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }

    @Test
    public void testConstructor_withFileTextObject_shouldCreateCopyOfFileText() {
        FileText copyFileText = new FileText(fileText);
        assertNotNull(copyFileText);
        assertNotSame(fileText, copyFileText);
        assertEquals(fileText.getFile(), copyFileText.getFile());
        assertEquals(fileText.getCharset(), copyFileText.getCharset());
        assertSame(fileText.getFullText(), copyFileText.getFullText());
        assertArrayEquals(fileText.toLinesArray(), copyFileText.toLinesArray());
        assertNotSame(fileText.toLinesArray(), copyFileText.toLinesArray());
        assertEquals(fileText.size(), copyFileText.size());
    }

    @Test
    public void testFromFile_withFileTextObject_shouldReturnSameFileTextObject() {
        FileText fromLinesFileText = FileText.fromLines(file, Arrays.asList(fileText.toLinesArray()));
        assertNotNull(fromLinesFileText);
        assertSame(fileText, fromLinesFileText);
    }

    @Test
    public void testFromFile_withFileAndLines_shouldCreateFileTextObject() {
        List<String> lines = new ArrayList<>();
        lines.add("line1");
        lines.add("line2");
        lines.add("line3");
        FileText fromLinesFileText = FileText.fromLines(file, lines);
        assertNotNull(fromLinesFileText);
        assertNotSame(fileText, fromLinesFileText);
        assertEquals(file, fromLinesFileText.getFile());
        assertNull(fromLinesFileText.getCharset());
        assertEquals("line1\nline2\nline3\n", fromLinesFileText.getFullText());
        assertArrayEquals(new String[]{"line1", "line2", "line3"}, fromLinesFileText.toLinesArray());
        assertEquals(3, fromLinesFileText.size());
    }

    @Test
    public void testLineColumn_withPositionOnLine1_shouldReturnLineColumn1() {
        assertEquals(new LineColumn(1, 0), fileText.lineColumn(0));
        assertEquals(new LineColumn(1, 1), fileText.lineColumn(1));
        assertEquals(new LineColumn(1, 2), fileText.lineColumn(2));
        assertEquals(new LineColumn(1, 3), fileText.lineColumn(3));
    }

    @Test
    public void testLineColumn_withPositionOnLine2_shouldReturnLineColumn2() {
        assertEquals(new LineColumn(2, 0), fileText.lineColumn(4));
        assertEquals(new LineColumn(2, 1), fileText.lineColumn(5));
        assertEquals(new LineColumn(2, 2), fileText.lineColumn(6));
    }

    @Test
    public void testLineColumn_withPositionOnLine3_shouldReturnLineColumn3() {
        assertEquals(new LineColumn(3, 0), fileText.lineColumn(7));
        assertEquals(new LineColumn(3, 1), fileText.lineColumn(8));
        assertEquals(new LineColumn(3, 2), fileText.lineColumn(9));
    }

    @Test
    public void testGet_withLineNoInRange_shouldReturnCorrectLine() {
        assertEquals("line1", fileText.get(0));
        assertEquals("line2", fileText.get(1));
        assertEquals("line3", fileText.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_withLineNoOutOfRange_shouldThrowIndexOutOfBoundsException() {
        fileText.get(3);
    }

    @Test
    public void testSize_withNonEmptyFile_shouldReturnNumberOfLinesInFile() {
        assertEquals(3, fileText.size());
    }

    @Test
    public void testSize_withEmptyFile_shouldReturnZero() {
        File emptyFile = new File("empty_file.txt");
        try {
            fileText = new FileText(emptyFile, charsetName);
            assertEquals(0, fileText.size());
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }
}