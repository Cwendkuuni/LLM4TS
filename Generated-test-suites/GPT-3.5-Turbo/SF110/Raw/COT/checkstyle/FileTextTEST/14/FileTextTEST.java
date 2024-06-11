
import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class FileTextTest {

    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorWithUnsupportedCharsetName() throws IOException {
        File file = new File("test.txt");
        String charsetName = "InvalidCharset";
        FileText fileText = new FileText(file, charsetName);
    }

    @Test
    public void testConstructorWithFileText() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        FileText newFileText = new FileText(fileText);
        assertEquals(fileText.getFile(), newFileText.getFile());
        assertEquals(fileText.getCharset(), newFileText.getCharset());
        assertEquals(fileText.getFullText(), newFileText.getFullText());
        assertArrayEquals(fileText.toLinesArray(), newFileText.toLinesArray());
    }

    @Test
    public void testFromLines() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line 1\nline 2\nline 3\n", fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
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
        FileText fileText = new FileText(file, "UTF-8");
        String fullText = fileText.getFullText().toString();
        assertEquals("Test file content", fullText);
    }

    @Test
    public void testToLinesArray() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        LineColumn lineColumn = fileText.lineColumn(10);
        assertEquals(3, lineColumn.getLine());
        assertEquals(2, lineColumn.getColumn());
    }

    @Test
    public void testGet() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("line 2", fileText.get(2));
    }

    @Test
    public void testSize() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(3, fileText.size());
    }
}
