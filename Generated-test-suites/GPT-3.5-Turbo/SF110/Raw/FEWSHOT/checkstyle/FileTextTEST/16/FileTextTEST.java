
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileTextTest {

    private final FileText fileText;

    public FileTextTest() throws IOException {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("This is a test file.");
        lines.add("It contains multiple lines.");
        lines.add("Testing the FileText class.");
        this.fileText = new FileText(file, lines);
    }

    @Test
    public void testGetFile() {
        File expectedFile = new File("test.txt");
        assertEquals(expectedFile, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        String expectedText = "This is a test file.\nIt contains multiple lines.\nTesting the FileText class.\n";
        assertEquals(expectedText, fileText.getFullText().toString());
    }

    @Test
    public void testToLinesArray() {
        String[] expectedLines = {"This is a test file.", "It contains multiple lines.", "Testing the FileText class."};
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() {
        LineColumn lineColumn1 = fileText.lineColumn(10);
        assertEquals(1, lineColumn1.getLine());
        assertEquals(10, lineColumn1.getColumn());

        LineColumn lineColumn2 = fileText.lineColumn(25);
        assertEquals(2, lineColumn2.getLine());
        assertEquals(5, lineColumn2.getColumn());

        LineColumn lineColumn3 = fileText.lineColumn(40);
        assertEquals(3, lineColumn3.getLine());
        assertEquals(3, lineColumn3.getColumn());
    }

    @Test
    public void testGet() {
        assertEquals("This is a test file.", fileText.get(0));
        assertEquals("It contains multiple lines.", fileText.get(1));
        assertEquals("Testing the FileText class.", fileText.get(2));
    }

    @Test
    public void testSize() {
        assertEquals(3, fileText.size());
    }
}
