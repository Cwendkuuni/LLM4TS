
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testConstructor_withValidCharset() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";

        FileText fileText = new FileText(file, charsetName);

        // Check if the file, charset, and lines are set correctly
        assertEquals(file, fileText.getFile());
        assertEquals(charsetName, fileText.getCharset().name());
        List<String> lines = new ArrayList<String>(Arrays.asList(fileText.toLinesArray()));
        assertEquals("Line1", lines.get(0));
        assertEquals("Line2", lines.get(1));
        assertEquals("Line3", lines.get(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_withInvalidCharset() throws IOException {
        File file = new File("test.txt");
        String charsetName = "InvalidCharset";

        new FileText(file, charsetName);
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructor_withNonExistingFile() throws IOException {
        File file = new File("nonexisting.txt");
        String charsetName = "UTF-8";

        new FileText(file, charsetName);
    }

    @Test
    public void testConstructor_withFileText() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";

        FileText fileText = new FileText(file, charsetName);
        FileText copy = new FileText(fileText);

        // Check if the file, charset, and lines are copied correctly
        assertEquals(fileText.getFile(), copy.getFile());
        assertEquals(fileText.getCharset(), copy.getCharset());
        assertEquals(fileText.getFullText(), copy.getFullText());
        List<String> originalLines = new ArrayList<String>(Arrays.asList(fileText.toLinesArray()));
        List<String> copyLines = new ArrayList<String>(Arrays.asList(copy.toLinesArray()));
        assertEquals(originalLines, copyLines);
    }

    @Test
    public void testFromLines() throws IOException {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<String>(Arrays.asList("Line1", "Line2", "Line3"));

        FileText fileText = FileText.fromLines(file, lines);

        // Check if the file, charset, and lines are set correctly
        assertEquals(file, fileText.getFile());
        assertEquals(null, fileText.getCharset());
        List<String> fileLines = new ArrayList<String>(Arrays.asList(fileText.toLinesArray()));
        assertEquals(lines, fileLines);
    }

    @Test
    public void testLineColumn() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";

        FileText fileText = new FileText(file, charsetName);

        // Check lineColumn method for different positions
        assertEquals(new LineColumn(1, 1), fileText.lineColumn(0));
        assertEquals(new LineColumn(1, 5), fileText.lineColumn(4));
        assertEquals(new LineColumn(2, 1), fileText.lineColumn(5));
        assertEquals(new LineColumn(3, 5), fileText.lineColumn(13));
        assertEquals(new LineColumn(3, 9), fileText.lineColumn(17));
    }

    @Test
    public void testGet() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";

        FileText fileText = new FileText(file, charsetName);

        // Check if the get method returns the correct lines
        assertEquals("Line1", fileText.get(0));
        assertEquals("Line2", fileText.get(1));
        assertEquals("Line3", fileText.get(2));
    }

    @Test
    public void testSize() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";

        FileText fileText = new FileText(file, charsetName);

        // Check if the size method returns the correct number of lines
        assertEquals(3, fileText.size());
    }

}
