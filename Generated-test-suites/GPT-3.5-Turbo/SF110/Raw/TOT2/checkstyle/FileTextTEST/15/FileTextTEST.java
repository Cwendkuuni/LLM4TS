import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class FileTextTest {

    @Test
    public void testFileTextConstructor() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertEquals(0, fileText.lineColumn(0).getColumn());
    }

    @Test
    public void testFileTextCopyConstructor() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        FileText copiedFileText = new FileText(fileText);
        assertNotNull(copiedFileText);
        assertEquals(fileText.getFile(), copiedFileText.getFile());
        assertEquals(fileText.getCharset(), copiedFileText.getCharset());
        assertEquals(fileText.getFullText(), copiedFileText.getFullText());
    }

    @Test
    public void testFileTextFromLines() {
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        File file = new File("test.txt");
        FileText fileText = FileText.fromLines(file, lines);
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line 1\nline 2\nline 3\n", fileText.getFullText());
    }

    @Test
    public void testLineColumn() {
        List<String> lines = Collections.singletonList("test line");
        File file = new File("test.txt");
        FileText fileText = FileText.fromLines(file, lines);
        LineColumn lineColumn = fileText.lineColumn(5);
        assertEquals(1, lineColumn.getLine());
        assertEquals(5, lineColumn.getColumn());
    }

    @Test(expected = IOException.class)
    public void testFileTextConstructorWithInvalidCharset() throws IOException {
        File file = new File("test.txt");
        new FileText(file, "InvalidCharset");
    }

    @Test
    public void testGet() {
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        File file = new File("test.txt");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals("line 2", fileText.get(1));
    }

    @Test
    public void testSize() {
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        File file = new File("test.txt");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(3, fileText.size());
    }
}