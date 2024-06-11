package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileTextTest {

    @Test
    public void testConstructorWithValidFileAndCharsetName() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertNotNull(fileText.findLineBreaks());
    }

    @Test(expected = IOException.class)
    public void testConstructorWithInvalidFile() throws IOException {
        File file = new File("invalidFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
    }

    @Test
    public void testConstructorWithFileTextObject() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText1 = new FileText(file, charsetName);
        FileText fileText2 = new FileText(fileText1);

        assertNotNull(fileText2);
        assertEquals(fileText1.getFile(), fileText2.getFile());
        assertEquals(fileText1.getCharset(), fileText2.getCharset());
        assertEquals(fileText1.getFullText(), fileText2.getFullText());
        assertArrayEquals(fileText1.toLinesArray(), fileText2.toLinesArray());
        assertArrayEquals(fileText1.findLineBreaks(), fileText2.findLineBreaks());
    }

    @Test
    public void testConstructorWithFileAndLinesList() {
        File file = new File("testFile.txt");
        List<String> lines = new ArrayList<>();
        lines.add("line 1");
        lines.add("line 2");
        FileText fileText = FileText.fromLines(file, lines);

        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertNotNull(fileText.findLineBreaks());
    }

    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        assertEquals(Charset.forName(charsetName), fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        assertNotNull(fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        assertNotNull(fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        LineColumn lineColumn = fileText.lineColumn(10);

        assertNotNull(lineColumn);
        assertEquals(1, lineColumn.getLineNo());
        assertEquals(10, lineColumn.getColumnNo());
    }

    @Test
    public void testGet() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        String line = fileText.get(0);

        assertEquals("line 1", line);
    }

    @Test
    public void testSize() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        assertEquals(2, fileText.size());
    }
}