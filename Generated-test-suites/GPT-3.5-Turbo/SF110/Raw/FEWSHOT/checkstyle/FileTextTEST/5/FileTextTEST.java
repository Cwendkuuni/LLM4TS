
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {
    private FileText fileText;

    @Before
    public void setup() throws Exception {
        File inputFile = new File("test_file.txt");
        List<String> lines = new ArrayList<String>(Arrays.asList("line 1", "line 2", "line 3"));
        fileText = FileText.fromLines(inputFile, lines);
    }

    @Test
    public void testGetFile() {
        File expectedFile = new File("test_file.txt");
        assertEquals(expectedFile, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        assertNull(fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        String expectedText = "line 1\nline 2\nline 3\n";
        assertEquals(expectedText, fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        String[] expectedLines = {"line 1", "line 2", "line 3"};
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() {
        LineColumn lineColumn = fileText.lineColumn(8);
        assertEquals(2, lineColumn.getLineNumber());
        assertEquals(5, lineColumn.getColumnNumber());
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
