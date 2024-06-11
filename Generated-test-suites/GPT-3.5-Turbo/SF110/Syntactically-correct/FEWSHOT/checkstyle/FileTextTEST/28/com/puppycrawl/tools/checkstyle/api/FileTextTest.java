
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FileTextTest {

    private static final String TEST_FILE_PATH = "path/to/test/file.txt";
    private static final String TEST_CHARSET = "UTF-8";

    private FileText fileText;

    @Before
    public void setup() throws IOException {
        File testFile = new File(TEST_FILE_PATH);
        testFile.createNewFile();
        FileWriter fileWriter = new FileWriter(testFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write("Line 1");
        bufferedWriter.newLine();
        bufferedWriter.write("Line 2");
        bufferedWriter.newLine();
        bufferedWriter.write("Line 3");
        bufferedWriter.newLine();
        bufferedWriter.close();

        fileText = new FileText(testFile, TEST_CHARSET);
    }

    @Test
    public void testConstructor() {
        assertEquals(TEST_FILE_PATH, fileText.getFile().getPath());
        assertEquals(Charset.forName(TEST_CHARSET), fileText.getCharset());
        assertEquals("Line 1\nLine 2\nLine 3\n", fileText.getFullText());
        assertEquals(3, fileText.size());
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));
    }

    @Test
    public void testConstructorWithInvalidCharset() {
        try {
            new FileText(new File(TEST_FILE_PATH), "invalidCharset");
            // If the constructor does not throw an exception, the test should fail
            // because an invalid charset should throw an IllegalStateException
            assertNull(null);
        } catch (IOException ex) {
            // Exception expected
        }
    }

    @Test
    public void testFromLines() {
        List<String> lines = new ArrayList<>();
        lines.add("Line 4");
        lines.add("Line 5");
        lines.add("Line 6");

        FileText fileTextFromLines = FileText.fromLines(new File(TEST_FILE_PATH), lines);
        assertEquals(TEST_FILE_PATH, fileTextFromLines.getFile().getPath());
        assertNull(fileTextFromLines.getCharset());
        assertEquals("Line 4\nLine 5\nLine 6\n", fileTextFromLines.getFullText());
        assertEquals(3, fileTextFromLines.size());
        assertEquals("Line 4", fileTextFromLines.get(0));
        assertEquals("Line 5", fileTextFromLines.get(1));
        assertEquals("Line 6", fileTextFromLines.get(2));
    }

    @Test
    public void testLineColumn() {
        assertEquals(new LineColumn(1, 0), fileText.lineColumn(0));
        assertEquals(new LineColumn(1, 5), fileText.lineColumn(5));
        assertEquals(new LineColumn(2, 0), fileText.lineColumn(6));
        assertEquals(new LineColumn(2, 5), fileText.lineColumn(11));
        assertEquals(new LineColumn(3, 0), fileText.lineColumn(12));
        assertEquals(new LineColumn(3, 5), fileText.lineColumn(17));
    }
}
