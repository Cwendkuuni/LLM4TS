
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testConstructorWithFileAndCharsetName() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertEquals(file, fileText.getFile());
            assertEquals(charsetName, fileText.getCharset().name());
            assertEquals("", fileText.getFullText().toString());
            assertEquals(0, fileText.size());
        } catch (Exception e) {
            // Handle exception
        }
    }

    @Test
    public void testConstructorWithFileText() {
        FileText originalFileText = createDummyFileText();
        FileText fileText = new FileText(originalFileText);
        assertEquals(originalFileText.getFile(), fileText.getFile());
        assertEquals(originalFileText.getCharset(), fileText.getCharset());
        assertEquals(originalFileText.getFullText(), fileText.getFullText());
        assertEquals(originalFileText.size(), fileText.size());
        assertEquals(originalFileText.get(0), fileText.get(0));
        assertEquals(originalFileText.get(1), fileText.get(1));
    }

    @Test
    public void testFromFileText() {
        FileText originalFileText = createDummyFileText();
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        FileText fileText = FileText.fromLines(originalFileText.getFile(), lines);
        assertEquals(lines, Arrays.asList(fileText.toLinesArray()));
    }

    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        assertEquals("UTF-8", fileText.getCharset().name());
    }

    @Test
    public void testGetFullText() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        assertEquals("", fileText.getFullText().toString());
    }

    @Test
    public void testToLinesArray() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        assertEquals(0, fileText.toLinesArray().length);
    }

    @Test
    public void testLineColumn() {
        FileText fileText = createDummyFileText();
        assertEquals(new LineColumn(1, 1), fileText.lineColumn(0));
        assertEquals(new LineColumn(2, 5), fileText.lineColumn(12));
    }

    @Test
    public void testGet() {
        FileText fileText = createDummyFileText();
        assertEquals("line1", fileText.get(0));
    }

    @Test
    public void testSize() {
        FileText fileText = createDummyFileText();
        assertEquals(2, fileText.size());
    }

    private FileText createDummyFileText() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("line1");
        lines.add("line2");
        return FileText.fromLines(file, lines);
    }
}
