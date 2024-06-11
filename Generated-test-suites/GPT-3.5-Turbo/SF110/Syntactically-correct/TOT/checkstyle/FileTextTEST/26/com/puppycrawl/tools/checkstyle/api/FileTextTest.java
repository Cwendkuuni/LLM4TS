package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class FileTextTest {

    // Test case for the constructor with a valid Charset name
    @Test
    public void testConstructorWithValidCharset() throws Exception {
        File file = new File("test-file.txt");
        String charsetName = "UTF-8";

        FileText fileText = new FileText(file, charsetName);

        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
    }

    // Test case for the constructor with an unsupported Charset name
    @Test(expected = IllegalStateException.class)
    public void testConstructorWithUnsupportedCharset() throws Exception {
        File file = new File("test-file.txt");
        String charsetName = "INVALID_CHARSET";

        new FileText(file, charsetName);
    }

    // Test case for the constructor with a non-existent file
    @Test(expected = FileNotFoundException.class)
    public void testConstructorWithNonExistentFile() throws Exception {
        File file = new File("non-existent-file.txt");
        String charsetName = "UTF-8";

        new FileText(file, charsetName);
    }

    // Test case for the constructor with an empty file
    @Test
    public void testConstructorWithEmptyFile() throws Exception {
        File file = new File("empty-file.txt");
        String charsetName = "UTF-8";

        FileText fileText = new FileText(file, charsetName);

        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertEquals(0, fileText.size());
    }

    // Test case for the constructor with a file containing one line
    @Test
    public void testConstructorWithSingleLineFile() throws Exception {
        File file = new File("single-line-file.txt");
        String charsetName = "UTF-8";
        List<String> lines = Arrays.asList("This is a single line in the file");

        FileText fileText = FileText.fromLines(file, lines);

        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertEquals(1, fileText.size());
        assertEquals("This is a single line in the file", fileText.get(0));
    }

    // Test case for the constructor with a file containing multiple lines
    @Test
    public void testConstructorWithMultipleLinesFile() throws Exception {
        File file = new File("multiple-lines-file.txt");
        String charsetName = "UTF-8";
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");

        FileText fileText = FileText.fromLines(file, lines);

        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertEquals(3, fileText.size());
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));
    }

    // Test case for getting the line and column of a position within the text
    @Test
    public void testLineColumn() throws Exception {
        File file = new File("test-file.txt");
        String charsetName = "UTF-8";
        List<String> lines = Arrays.asList("This is a test line");

        FileText fileText = FileText.fromLines(file, lines);

        LineColumn lineColumn = fileText.lineColumn(10);

        assertEquals(1, lineColumn.getLine());
        assertEquals(10, lineColumn.getColumn());
    }
}