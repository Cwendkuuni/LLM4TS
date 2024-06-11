import org.junit.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.nio.charset.Charset;
import static org.junit.Assert.*;

public class FileTextTest {

    private final File file = new File("src/test/resources/testfile.txt");

    @Test
    public void testConstructorWithCharsetName() throws IOException {
        FileText fileText = new FileText(file, "UTF-8");
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertEquals("This is a test file.\nIt has multiple lines.", fileText.getFullText().toString());
        assertArrayEquals(new String[]{"This is a test file.", "It has multiple lines."}, fileText.toLinesArray());
        assertEquals(2, fileText.size());
        assertEquals("This is a test file.", fileText.get(0));
        assertEquals("It has multiple lines.", fileText.get(1));
    }

    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText(file, "UTF-8");
        FileText newFileText = new FileText(fileText);
        assertNotNull(newFileText);
        assertEquals(file, newFileText.getFile());
        assertEquals(Charset.forName("UTF-8"), newFileText.getCharset());
        assertNotNull(newFileText.getFullText());
        assertEquals("This is a test file.\nIt has multiple lines.", newFileText.getFullText().toString());
        assertArrayEquals(new String[]{"This is a test file.", "It has multiple lines."}, newFileText.toLinesArray());
        assertEquals(2, newFileText.size());
        assertEquals("This is a test file.", newFileText.get(0));
        assertEquals("It has multiple lines.", newFileText.get(1));
    }

    @Test
    public void testFromStringLines() {
        List<String> lines = Arrays.asList("This is a test file.", "It has multiple lines.");
        FileText fileText = FileText.fromLines(file, lines);
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertEquals("This is a test file.\nIt has multiple lines.", fileText.getFullText().toString());
        assertArrayEquals(new String[]{"This is a test file.", "It has multiple lines."}, fileText.toLinesArray());
        assertEquals(2, fileText.size());
        assertEquals("This is a test file.", fileText.get(0));
        assertEquals("It has multiple lines.", fileText.get(1));
    }

    @Test
    public void testLineColumn() throws IOException {
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(new LineColumn(1, 0), fileText.lineColumn(0));
        assertEquals(new LineColumn(1, 1), fileText.lineColumn(1));
        assertEquals(new LineColumn(1, 2), fileText.lineColumn(2));
        assertEquals(new LineColumn(1, 3), fileText.lineColumn(3));
        assertEquals(new LineColumn(1, 4), fileText.lineColumn(4));
        assertEquals(new LineColumn(1, 5), fileText.lineColumn(5));
        assertEquals(new LineColumn(2, 0), fileText.lineColumn(18));
        assertEquals(new LineColumn(2, 1), fileText.lineColumn(19));
        assertEquals(new LineColumn(2, 2), fileText.lineColumn(20));
        assertEquals(new LineColumn(2, 3), fileText.lineColumn(21));
        assertEquals(new LineColumn(2, 4), fileText.lineColumn(22));
        assertEquals(new LineColumn(2, 5), fileText.lineColumn(23));
    }

    @Test
    public void testGetLine() throws IOException {
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("This is a test file.", fileText.get(0));
        assertEquals("It has multiple lines.", fileText.get(1));
    }

    @Test
    public void testGetSize() throws IOException {
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(2, fileText.size());
    }
}