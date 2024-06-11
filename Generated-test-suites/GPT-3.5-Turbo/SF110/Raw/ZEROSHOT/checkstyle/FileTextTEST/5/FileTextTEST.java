
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

    private FileText fileText;

    @Before
    public void setUp() throws IOException {
        File file = new File("test.txt");
        fileText = new FileText(file, "UTF-8");
    }

    @Test
    public void testGetFile() {
        File file = fileText.getFile();
        assertNotNull(file);
        assertEquals("test.txt", file.getName());
    }

    @Test
    public void testGetCharset() {
        Charset charset = fileText.getCharset();
        assertNotNull(charset);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testGetFullText() {
        CharSequence fullText = fileText.getFullText();
        assertNotNull(fullText);
        assertEquals("This is a test file.\n", fullText.toString());
    }

    @Test
    public void testToLinesArray() {
        String[] linesArray = fileText.toLinesArray();
        assertNotNull(linesArray);
        assertEquals(1, linesArray.length);
        assertEquals("This is a test file.", linesArray[0]);
    }

    @Test
    public void testLineColumn() {
        LineColumn lineColumn = fileText.lineColumn(5);
        assertNotNull(lineColumn);
        assertEquals(1, lineColumn.getLine());
        assertEquals(5, lineColumn.getColumn());
    }

    @Test
    public void testGet() {
        String line = fileText.get(0);
        assertNotNull(line);
        assertEquals("This is a test file.", line);
    }

    @Test
    public void testSize() {
        int size = fileText.size();
        assertEquals(1, size);
    }
    
    @Test
    public void testConstructor_withFileAndLines() {
        List<String> lines = new ArrayList<>();
        lines.add("This is a test file.");
        FileText fileText = FileText.fromLines(new File("test.txt"), lines);
        assertNotNull(fileText);
        assertEquals("test.txt", fileText.getFile().getName());
        assertNull(fileText.getCharset());
        assertEquals("This is a test file.\n", fileText.getFullText().toString());
        assertEquals(1, fileText.size());
        assertEquals("This is a test file.", fileText.get(0));
    }

}
