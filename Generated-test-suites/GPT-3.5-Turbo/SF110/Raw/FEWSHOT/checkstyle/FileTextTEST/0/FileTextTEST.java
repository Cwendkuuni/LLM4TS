package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import com.google.common.io.Closeables;

public class FileTextTest {

    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());

        // Assert that the file was read correctly
        String expectedText = "This is the content of the test file.";
        assertEquals(expectedText, fileText.getFullText().toString());

        // Assert that the lines were split correctly
        String[] expectedLines = {"This is the content of the test file."};
        assertArrayEquals(expectedLines, fileText.toLinesArray());
        assertEquals(1, fileText.size());
    }

    @Test
    public void testConstructorWithFileText() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText originalFileText = new FileText(file, charsetName);
        FileText fileText = new FileText(originalFileText);

        assertEquals(originalFileText.getFile(), fileText.getFile());
        assertEquals(originalFileText.getCharset(), fileText.getCharset());
        assertEquals(originalFileText.getFullText(), fileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), fileText.toLinesArray());
        assertNotSame(originalFileText.toLinesArray(), fileText.toLinesArray());
        assertEquals(originalFileText.size(), fileText.size());
    }

    @Test
    public void testConstructorWithFileAndLines() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("This is line 1");
        lines.add("This is line 2");
        FileText fileText = FileText.fromLines(file, lines);

        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("This is line 1\nThis is line 2\n", fileText.getFullText().toString());
        assertArrayEquals(new String[]{"This is line 1", "This is line 2"}, fileText.toLinesArray());
        assertEquals(2, fileText.size());
    }

    @Test
    public void testReadFile() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        CharsetDecoder decoder = Charset.forName(charsetName).newDecoder();
        decoder.onMalformedInput(CodingErrorAction.REPLACE);
        decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);

        String expectedText = "This is the content of the test file.";
        String actualText = FileText.readFile(file, decoder);

        assertEquals(expectedText, actualText);
    }

    @Test(expected = FileNotFoundException.class)
    public void testReadFileThrowsFileNotFoundException() throws IOException {
        File file = new File("nonexistent.txt");
        String charsetName = "UTF-8";
        CharsetDecoder decoder = Charset.forName(charsetName).newDecoder();
        decoder.onMalformedInput(CodingErrorAction.REPLACE);
        decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);

        FileText.readFile(file, decoder);
    }

    @Test
    public void testFindLineBreaks() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        int[] expectedLineBreaks = {0, 34};
        assertArrayEquals(expectedLineBreaks, fileText.findLineBreaks());
    }

    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        int pos1 = 5;
        LineColumn lineColumn1 = fileText.lineColumn(pos1);
        assertEquals(1, lineColumn1.getLine());
        assertEquals(5, lineColumn1.getColumn());

        int pos2 = 20;
        LineColumn lineColumn2 = fileText.lineColumn(pos2);
        assertEquals(1, lineColumn2.getLine());
        assertEquals(20, lineColumn2.getColumn());
    }

    @Test
    public void testGet() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        String expectedLine1 = "This is the content of the test file.";
        assertEquals(expectedLine1, fileText.get(0));
    }

    @Test
    public void testSize() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        assertEquals(1, fileText.size());
    }
}