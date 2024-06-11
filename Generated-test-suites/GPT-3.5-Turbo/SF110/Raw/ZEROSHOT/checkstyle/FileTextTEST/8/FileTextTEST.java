import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class FileTextTest {

    @Test
    public void testFileTextWithValidCharset() {
        File file = new File("test.txt");
        try {
            FileText fileText = new FileText(file, "UTF-8");
            assertEquals(file, fileText.getFile());
            assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTextWithUnsupportedCharset() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "InvalidCharset");
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextWithNonExistingFile() throws IOException {
        File file = new File("non_existing.txt");
        FileText fileText = new FileText(file, "UTF-8");
    }

    @Test
    public void testFileTextCopyConstructor() throws IOException {
        File file = new File("test.txt");
        FileText originalFileText = new FileText(file, "UTF-8");
        FileText copiedFileText = new FileText(originalFileText);

        assertEquals(originalFileText.getFile(), copiedFileText.getFile());
        assertEquals(originalFileText.getCharset(), copiedFileText.getCharset());
        assertEquals(originalFileText.getFullText(), copiedFileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), copiedFileText.toLinesArray());
    }

    @Test
    public void testFileTextFromLines() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("line 1");
        lines.add("line 2");
        FileText fileText = FileText.fromLines(file, lines);

        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line 1\nline 2\n", fileText.getFullText());
        assertArrayEquals(new String[]{"line 1", "line 2"}, fileText.toLinesArray());
    }

    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("This is a test file.", fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertArrayEquals(new String[]{"This is line 1", "This is line 2"}, fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        int[] lineBreaks = fileText.findLineBreaks();
        assertArrayEquals(new int[]{0, 21, 34}, lineBreaks);
    }

    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        LineColumn lineColumn = fileText.lineColumn(10);
        assertEquals(2, lineColumn.getLine());
        assertEquals(4, lineColumn.getColumn());
    }

    @Test
    public void testGet() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("This is line 1", fileText.get(0));
        assertEquals("This is line 2", fileText.get(1));
    }

    @Test
    public void testSize() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(2, fileText.size());
    }
}