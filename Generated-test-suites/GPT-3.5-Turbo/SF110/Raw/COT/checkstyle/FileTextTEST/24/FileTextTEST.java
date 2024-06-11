import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class FileTextTest {

    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        assertEquals("", fileText.getFullText().toString());
        assertEquals(new String[]{}, fileText.toLinesArray());
        assertEquals(0, fileText.size());
    }

    @Test
    public void testConstructorWithFileText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText originalFileText = new FileText(file, charsetName);
        FileText fileText = new FileText(originalFileText);
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        assertEquals("", fileText.getFullText().toString());
        assertEquals(new String[]{}, fileText.toLinesArray());
        assertEquals(0, fileText.size());
    }

    @Test
    public void testFromFileText() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("line 1");
        lines.add("line 2");
        FileText fileText = FileText.fromLines(file, lines);
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line 1\nline 2\n", fileText.getFullText().toString());
        assertArrayEquals(new String[]{"line 1", "line 2"}, fileText.toLinesArray());
        assertEquals(2, fileText.size());
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
    public void testGetFullText() throws IOException {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line 1", "line 2");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals("line 1\nline 2\n", fileText.getFullText().toString());
    }

    @Test
    public void testToLinesArray() throws IOException {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line 1", "line 2");
        FileText fileText = FileText.fromLines(file, lines);
        assertArrayEquals(new String[]{"line 1", "line 2"}, fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() throws IOException {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line 1", "line 2");
        FileText fileText = FileText.fromLines(file, lines);

        assertEquals(new LineColumn(1, 0), fileText.lineColumn(0));
        assertEquals(new LineColumn(1, 1), fileText.lineColumn(1));
        assertEquals(new LineColumn(1, 2), fileText.lineColumn(2));
        assertEquals(new LineColumn(2, 0), fileText.lineColumn(6));
        assertEquals(new LineColumn(2, 1), fileText.lineColumn(7));
        assertEquals(new LineColumn(2, 2), fileText.lineColumn(8));
    }

    @Test
    public void testGet() throws IOException {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line 1", "line 2");
        FileText fileText = FileText.fromLines(file, lines);

        assertEquals("line 1", fileText.get(0));
        assertEquals("line 2", fileText.get(1));
    }

    @Test
    public void testSize() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line 1", "line 2");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(2, fileText.size());
    }
}

