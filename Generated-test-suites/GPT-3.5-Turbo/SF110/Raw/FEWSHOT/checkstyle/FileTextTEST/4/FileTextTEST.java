
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

    @Test
    public void testConstructorWithCharset() throws Exception {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        assertEquals(file, fileText.getFile());
        assertEquals(charsetName, fileText.getCharset().name());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorWithUnsupportedCharset() throws Exception {
        File file = new File("test.txt");
        String charsetName = "InvalidCharset";
        new FileText(file, charsetName);
    }

    @Test
    public void testConstructorWithFileText() throws Exception {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText originalFileText = new FileText(file, charsetName);
        FileText copyFileText = new FileText(originalFileText);

        assertEquals(originalFileText.getFile(), copyFileText.getFile());
        assertEquals(originalFileText.getCharset().name(), copyFileText.getCharset().name());
        assertEquals(originalFileText.getFullText(), copyFileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), copyFileText.toLinesArray());
        assertNull(copyFileText.lineBreaks);
    }

    @Test
    public void testConstructorWithLinesList() throws Exception {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);

        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("Line 1\nLine 2\nLine 3\n", fileText.getFullText());
        assertArrayEquals(lines.toArray(), fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() throws Exception {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        assertEquals(new LineColumn(1, 0), fileText.lineColumn(0));
        assertEquals(new LineColumn(1, 4), fileText.lineColumn(4));
        assertEquals(new LineColumn(2, 0), fileText.lineColumn(6));
    }

    @Test
    public void testSize() throws Exception {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        assertEquals(3, fileText.size());
    }

    @Test
    public void testGet() throws Exception {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));
    }
}
