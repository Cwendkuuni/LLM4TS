
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FileTextTest {

    private FileText fileText;

    @Before
    public void setup() {
        // Create temporary file for testing
        File tempFile = new File("temp.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        fileText = FileText.fromLines(tempFile, lines);
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructor_fileNotExists() throws IOException {
        File nonExistingFile = new File("nonexisting.txt");
        new FileText(nonExistingFile, "UTF-8");
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_unsupportedCharset() throws IOException {
        File existingFile = new File("existing.txt");
        new FileText(existingFile, "unsupportedCharset");
    }

    @Test
    public void testConstructor_validFileAndEncoding() throws IOException {
        File existingFile = new File("existing.txt");
        FileText fileText = new FileText(existingFile, "UTF-8");
        assertEquals(existingFile, fileText.getFile());
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        assertEquals("Line 1\nLine 2\nLine 3\n", fileText.getFullText().toString());
    }

    @Test
    public void testToLinesArray() {
        String[] expectedArray = {"Line 1", "Line 2", "Line 3"};
        assertArrayEquals(expectedArray, fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() {
        LineColumn lineColumn = fileText.lineColumn(7);
        assertEquals(2, lineColumn.getLine());
        assertEquals(1, lineColumn.getColumn());
    }

    @Test
    public void testGet() {
        assertEquals("Line 2", fileText.get(1));
    }

    @Test
    public void testSize() {
        assertEquals(3, fileText.size());
    }
}
