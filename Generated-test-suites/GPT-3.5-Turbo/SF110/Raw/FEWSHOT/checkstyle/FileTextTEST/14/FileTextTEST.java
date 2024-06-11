
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FileTextTest {
    private final FileText fileText;
    private final File testFile;
    private final String charsetName;

    public FileTextTest() throws Exception {
        charsetName = "UTF-8";
        testFile = new File("test.txt");
        List<String> lines = new ArrayList<String>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");
        fileText = FileText.fromLines(testFile, lines);
    }

    @Test
    public void testGetFile() {
        assertEquals(testFile, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        Charset expectedCharset = Charset.forName(charsetName);
        assertEquals(expectedCharset, fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        String expectedText = "Line 1\nLine 2\nLine 3\n";
        assertEquals(expectedText, fileText.getFullText().toString());
    }

    @Test
    public void testToLinesArray() {
        String[] expectedLines = { "Line 1", "Line 2", "Line 3" };
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() {
        LineColumn expectedLineColumn1 = new LineColumn(2, 3);
        LineColumn expectedLineColumn2 = new LineColumn(3, 5);
        assertEquals(expectedLineColumn1, fileText.lineColumn(8));
        assertEquals(expectedLineColumn2, fileText.lineColumn(15));
    }

    @Test
    public void testGet() {
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));
    }

    @Test
    public void testSize() {
        assertEquals(3, fileText.size());
    }
}
