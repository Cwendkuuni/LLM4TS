
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class FileTextTest {

    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";

        FileText fileText = new FileText(file, charsetName);

        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertEquals("This is a test file.", fileText.getFullText().toString());
    }

    @Test
    public void testConstructorWithFileText() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText originalFileText = new FileText(file, charsetName);

        FileText copiedFileText = new FileText(originalFileText);

        assertNotNull(copiedFileText);
        assertEquals(file, copiedFileText.getFile());
        assertEquals(StandardCharsets.UTF_8, copiedFileText.getCharset());
        assertNotNull(copiedFileText.getFullText());
        assertEquals(originalFileText.getFullText().toString(), copiedFileText.getFullText().toString());
    }

    @Test
    public void testFromFileLines() throws IOException {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("This", "is", "a", "test", "file.");

        FileText fileText = FileText.fromLines(file, lines);

        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertEquals("This\nis\na\ntest\nfile.\n", fileText.getFullText().toString());
        assertArrayEquals(new String[]{"This", "is", "a", "test", "file."}, fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        LineColumn lineColumn = fileText.lineColumn(12);

        assertNotNull(lineColumn);
        assertEquals(2, lineColumn.getLineNo());
        assertEquals(3, lineColumn.getColumnNo());
    }

    @Test
    public void testGet() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        assertEquals("This", fileText.get(0));
        assertEquals("is", fileText.get(1));
        assertEquals("a", fileText.get(2));
        assertEquals("test", fileText.get(3));
        assertEquals("file.", fileText.get(4));
    }

    @Test
    public void testSize() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        assertEquals(5, fileText.size());
    }
}
