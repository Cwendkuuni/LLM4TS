
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FileTextTest {

    private static final String TEST_FILE_PATH = "testFile.txt";
    private static final String CHARSET_NAME = "UTF-8";

    // Helper method to create test file with given lines
    private void createTestFile(List<String> lines) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE_PATH));
        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }
        writer.close();
    }

    @Test
    public void testFileTextWithExistingFileAndValidCharsetName() throws IOException {
        // Create a test file with some lines
        List<String> lines = new ArrayList<>();
        lines.add("This is line 1");
        lines.add("This is line 2");
        lines.add("This is line 3");
        createTestFile(lines);

        // Create FileText instance with the test file and charset name
        File file = new File(TEST_FILE_PATH);
        FileText fileText = new FileText(file, CHARSET_NAME);

        // Check if the FileText instance is created correctly
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(CHARSET_NAME), fileText.getCharset());
        assertEquals(String.join("\n", lines), fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
        assertEquals(lines.size(), fileText.size());

        // Check individual line retrieval
        for (int i = 0; i < lines.size(); i++) {
            assertEquals(lines.get(i), fileText.get(i));
        }

        // Check line and column calculation for different positions in the text
        assertEquals(new FileText.LineColumn(1, 0), fileText.lineColumn(0));
        assertEquals(new FileText.LineColumn(1, 5), fileText.lineColumn(5));
        assertEquals(new FileText.LineColumn(2, 0), fileText.lineColumn(lines.get(0).length() + 1));
        assertEquals(new FileText.LineColumn(3, 3), fileText.lineColumn(lines.get(0).length() + lines.get(1).length() + 3));

        // Clean up the test file
        boolean deleted = file.delete();
        assertTrue(deleted);
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextWithNonExistingFile() throws IOException {
        // Create FileText with a non-existing file
        File file = new File("nonExistingFile.txt");
        new FileText(file, CHARSET_NAME);
    }

    @Test
    public void testFileTextWithUnsupportedCharsetName() {
        // Create FileText with an unsupported charset name
        File file = new File(TEST_FILE_PATH);
        try {
            new FileText(file, "invalidCharset");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Unsupported charset: invalidCharset", e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testFileTextCopyConstructor() {
        // Create a FileText instance
        File file = new File(TEST_FILE_PATH);
        FileText fileText1 = null;
        try {
            fileText1 = new FileText(file, CHARSET_NAME);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        // Create a new FileText instance using the copy constructor
        FileText fileText2 = new FileText(fileText1);

        // Check if the new instance is created correctly
        assertEquals(fileText1.getFile(), fileText2.getFile());
        assertEquals(fileText1.getCharset(), fileText2.getCharset());
        assertEquals(fileText1.getFullText(), fileText2.getFullText());
        assertArrayEquals(fileText1.toLinesArray(), fileText2.toLinesArray());
        assertEquals(fileText1.size(), fileText2.size());
    }

    @Test
    public void testFileTextFromLines() {
        // Create a test file with some lines
        List<String> lines = new ArrayList<>();
        lines.add("This is line 1");
        lines.add("This is line 2");
        lines.add("This is line 3");

        // Create FileText from the lines
        File file = new File(TEST_FILE_PATH);
        FileText fileText = FileText.fromLines(file, lines);

        // Check if the FileText instance is created correctly
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals(String.join("\n", lines), fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
        assertEquals(lines.size(), fileText.size());
    }

    @Test
    public void testFileTextGetWithInvalidLineNo() throws IOException {
        // Create a test file with some lines
        List<String> lines = new ArrayList<>();
        lines.add("This is line 1");
        lines.add("This is line 2");
        lines.add("This is line 3");
        createTestFile(lines);

        // Create FileText instance with the test file and charset name
        File file = new File(TEST_FILE_PATH);
        FileText fileText = new FileText(file, CHARSET_NAME);

        // Check invalid line number retrieval
        try {
            fileText.get(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Size: 3", e.getMessage());
        }
        try {
            fileText.get(lines.size());
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index: 3, Size: 3", e.getMessage());
        }

        // Clean up the test file
        boolean deleted = file.delete();
        assertTrue(deleted);
    }

    @Test
    public void testFileTextLineColumnWithInvalidPosition() throws IOException {
        // Create a test file with some lines
        List<String> lines = new ArrayList<>();
        lines.add("This is line 1");
        lines.add("This is line 2");
        lines.add("This is line 3");
        createTestFile(lines);

        // Create FileText instance with the test file and charset name
        File file = new File(TEST_FILE_PATH);
        FileText fileText = new FileText(file, CHARSET_NAME);

        // Check invalid position for line and column calculation
        try {
            fileText.lineColumn(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index: -1", e.getMessage());
        }
        try {
            fileText.lineColumn(fileText.getFullText().length() + 1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index: " + (fileText.getFullText().length() + 1), e.getMessage());
        }

        // Clean up the test file
        boolean deleted = file.delete();
        assertTrue(deleted);
    }
}
