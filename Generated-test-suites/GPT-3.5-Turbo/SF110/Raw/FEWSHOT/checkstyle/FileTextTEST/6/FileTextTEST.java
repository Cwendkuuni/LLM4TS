
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FileTextTest {

    @Test
    public void testConstructor() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        FileText fileText = FileText.fromLines(file, lines);

        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line 1\nline 2\nline 3\n", fileText.getFullText().toString());
        assertArrayEquals(new String[]{"line 1", "line 2", "line 3"}, fileText.toLinesArray());
        assertEquals(3, fileText.lineColumn(10).getLine());
        assertEquals(1, fileText.lineColumn(10).getColumn());
        assertEquals("line 2", fileText.get(1));
        assertEquals(3, fileText.size());
    }

    @Test
    public void testConstructor_withCharset() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        FileText fileText = new FileText(file, "UTF-8");

        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
        assertEquals("line 1\nline 2\nline 3\n", fileText.getFullText().toString());
        assertArrayEquals(new String[]{"line 1", "line 2", "line 3"}, fileText.toLinesArray());
        assertEquals(3, fileText.lineColumn(10).getLine());
        assertEquals(1, fileText.lineColumn(10).getColumn());
        assertEquals("line 2", fileText.get(1));
        assertEquals(3, fileText.size());
    }

    @Test
    public void testConstructor_fileNotFound() {
        File file = new File("nonExistentFile.txt");
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");

        try {
            FileText fileText = new FileText(file, "UTF-8");
            fail("Expected FileNotFoundException");
        } catch (IOException e) {
            assertEquals("nonExistentFile.txt (No such file or directory)", e.getMessage());
        }
    }
}
