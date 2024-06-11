package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FileTextTest {

    @Test
    public void testFileTextConstructorWithValidFileAndValidCharsetName() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");

        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
        assertEquals(3, fileText.size());
        assertEquals("line 1", fileText.get(0));
        assertEquals("line 2", fileText.get(1));
        assertEquals("line 3", fileText.get(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTextConstructorWithInvalidCharsetName() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "InvalidCharset");
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructorWithNonExistingFile() throws IOException {
        File file = new File("nonexisting.txt");
        FileText fileText = new FileText(file, "UTF-8");
    }

    @Test
    public void testFileTextCopyConstructor() throws IOException {
        File file = new File("test.txt");
        FileText original = new FileText(file, "UTF-8");

        FileText copy = new FileText(original);

        assertEquals(original.getFile(), copy.getFile());
        assertEquals(original.getCharset(), copy.getCharset());
        assertEquals(original.getFullText(), copy.getFullText());
        assertEquals(original.toLinesArray(), copy.toLinesArray());
        assertNull(copy.lineBreaks);
    }

    @Test
    public void testFileTextFromLines() throws IOException {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");

        FileText fileText = FileText.fromLines(file, lines);

        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line 1\nline 2\nline 3\n", fileText.getFullText());
        assertEquals(lines.size(), fileText.size());
        assertEquals(lines.get(0), fileText.get(0));
        assertEquals(lines.get(1), fileText.get(1));
        assertEquals(lines.get(2), fileText.get(2));
    }

    @Test
    public void testGetLineColumn() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");

        LineColumn lineColumn1 = fileText.lineColumn(0);
        assertEquals(1, lineColumn1.getLine());
        assertEquals(0, lineColumn1.getColumn());

        LineColumn lineColumn2 = fileText.lineColumn(6);
        assertEquals(2, lineColumn2.getLine());
        assertEquals(0, lineColumn2.getColumn());

        LineColumn lineColumn3 = fileText.lineColumn(13);
        assertEquals(2, lineColumn3.getLine());
        assertEquals(7, lineColumn3.getColumn());

        LineColumn lineColumn4 = fileText.lineColumn(21);
        assertEquals(3, lineColumn4.getLine());
        assertEquals(3, lineColumn4.getColumn());
    }
}