import com.puppycrawl.tools.checkstyle.api.FileText;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class FileTextTest {

    @Test
    public void testConstructor() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructor_FileNotFound() throws IOException {
        File file = new File("nonexistent.txt");
        FileText fileText = new FileText(file, "UTF-8");
    }

    @Test
    public void testConstructor_FromLines() {
        File file = new File("test.txt");
        ArrayList<String> lines = new ArrayList<>(Arrays.asList("line 1", "line 2", "line 3"));
        FileText fileText = FileText.fromLines(file, lines);
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line 1\nline 2\nline 3\n", fileText.getFullText());
        assertArrayEquals(new String[]{"line 1", "line 2", "line 3"}, fileText.toLinesArray());
    }

    @Test
    public void testConstructor_Copy() throws IOException {
        File file = new File("test.txt");
        FileText originalFileText = new FileText(file, "UTF-8");
        FileText copiedFileText = new FileText(originalFileText);
        assertEquals(originalFileText.getFile(), copiedFileText.getFile());
        assertEquals(originalFileText.getCharset(), copiedFileText.getCharset());
        assertEquals(originalFileText.getFullText(), copiedFileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), copiedFileText.toLinesArray());
    }

    @Test
    public void testLineColumn() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        FileText.LineColumn lineColumn = fileText.lineColumn(10);
        assertEquals(2, lineColumn.getLine());
        assertEquals(9, lineColumn.getColumn());
    }

    @Test
    public void testGet() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("line 2", fileText.get(1));
    }

    @Test
    public void testSize() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(3, fileText.size());
    }
}