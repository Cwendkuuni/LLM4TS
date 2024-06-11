
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FileTextTest {

    private static final String FILE_NAME = "testFile.txt";
    private static final String CHARSET_NAME = "UTF-8";

    @Test
    public void testConstructorAndAccessors() throws IOException {
        FileText fileText = new FileText(createTestFile(FILE_NAME), CHARSET_NAME);
        assertEquals(createTestFile(FILE_NAME), fileText.getFile());
        assertEquals(Charset.forName(CHARSET_NAME), fileText.getCharset());
        assertEquals("Test line 1\nTest line 2\nTest line 3\n", fileText.getFullText().toString());
        assertArrayEquals(new String[] {"Test line 1", "Test line 2", "Test line 3"}, fileText.toLinesArray());
        assertEquals("Test line 1", fileText.get(0));
        assertEquals(3, fileText.size());
    }

    @Test
    public void testConstructorWithUnsupportedCharset() {
        try {
            new FileText(createTestFile(FILE_NAME), "InvalidCharset");
        } catch (IllegalStateException e) {
            assertEquals("Unsupported charset: InvalidCharset", e.getMessage());
        } catch (IOException e) {
            // Not expected to reach here
        }
    }

    @Test
    public void testConstructorWithNonExistingFile() {
        try {
            FileText fileText = new FileText(new File("nonExistingFile.txt"), CHARSET_NAME);
        } catch (FileNotFoundException e) {
            assertEquals("nonExistingFile.txt (No such file or directory)", e.getMessage());
        } catch (IOException e) {
            // Not expected to reach here
        }
    }

    @Test
    public void testConstructorWithFileText() {
        FileText originalFileText = new FileText(createTestFile(FILE_NAME), CHARSET_NAME);

        FileText fileText = new FileText(originalFileText);
        assertEquals(createTestFile(FILE_NAME), fileText.getFile());
        assertEquals(Charset.forName(CHARSET_NAME), fileText.getCharset());
        assertEquals("Test line 1\nTest line 2\nTest line 3\n", fileText.getFullText().toString());
        assertArrayEquals(new String[] {"Test line 1", "Test line 2", "Test line 3"}, fileText.toLinesArray());
        assertEquals("Test line 1", fileText.get(0));
        assertEquals(3, fileText.size());
    }

    @Test
    public void testConstructorWithLineList() {
        List<String> lines = new ArrayList<>();
        lines.add("Test line 1");
        lines.add("Test line 2");
        lines.add("Test line 3");

        FileText fileText = FileText.fromLines(createTestFile(FILE_NAME), lines);
        assertEquals(createTestFile(FILE_NAME), fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("Test line 1\nTest line 2\nTest line 3\n", fileText.getFullText().toString());
        assertArrayEquals(new String[] {"Test line 1", "Test line 2", "Test line 3"}, fileText.toLinesArray());
        assertEquals("Test line 1", fileText.get(0));
        assertEquals(3, fileText.size());
    }

    @Test
    public void testLineColumn() throws IOException {
        FileText fileText = new FileText(createTestFile(FILE_NAME), CHARSET_NAME);

        assertEquals(new LineColumn(1, 0), fileText.lineColumn(0));
        assertEquals(new LineColumn(1, 4), fileText.lineColumn(4));
        assertEquals(new LineColumn(2, 0), fileText.lineColumn(14));
        assertEquals(new LineColumn(2, 4), fileText.lineColumn(18));
        assertEquals(new LineColumn(3, 0), fileText.lineColumn(28));
    }

    private File createTestFile(String fileName) throws IOException {
        File file = new File(fileName);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write("Test line 1\nTest line 2\nTest line 3\n");
        writer.close();
        return file;
    }

    @After
    public void cleanup() {
        File testFile = new File(FILE_NAME);
        testFile.delete();
    }
}
