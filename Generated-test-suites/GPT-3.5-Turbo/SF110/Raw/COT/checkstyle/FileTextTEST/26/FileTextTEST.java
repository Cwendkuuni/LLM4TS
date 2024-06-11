import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileTextTest {
    private static final File FILE = new File("test.txt");
    private static final String CHARSET = "UTF-8";

    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        FileText fileText = new FileText(FILE, CHARSET);
        assertNotNull(fileText);
        assertEquals(FILE, fileText.getFile());
        assertEquals(Charset.forName(CHARSET), fileText.getCharset());
    }

    @Test(expected = UnsupportedCharsetException.class)
    public void testConstructorWithUnsupportedCharsetName() throws IOException {
        new FileText(FILE, "invalidCharset");
    }

    @Test
    public void testConstructorWithFileText() {
        FileText originalFileText = new FileText(FILE, CHARSET);
        FileText fileText = new FileText(originalFileText);
        assertNotNull(fileText);
        assertEquals(originalFileText.getFile(), fileText.getFile());
        assertEquals(originalFileText.getCharset(), fileText.getCharset());
        assertEquals(originalFileText.getFullText(), fileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), fileText.toLinesArray());
    }

    @Test
    public void testFromFileLines() {
        List<String> lines = Arrays.asList("This is line 1", "This is line 2");
        FileText fileText = FileText.fromLines(FILE, lines);
        assertNotNull(fileText);
        assertEquals(FILE, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("This is line 1\nThis is line 2\n", fileText.getFullText());
        assertArrayEquals(new String[]{"This is line 1", "This is line 2" }, fileText.toLinesArray());
    }

    @Test
    public void testGetFile() {
        FileText fileText = new FileText(FILE, CHARSET);
        assertEquals(FILE, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        FileText fileText = new FileText(FILE, CHARSET);
        assertEquals(Charset.forName(CHARSET), fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        FileText fileText = new FileText(FILE, CHARSET);
        assertEquals("This is line 1\nThis is line 2\n", fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        FileText fileText = new FileText(FILE, CHARSET);
        assertArrayEquals(new String[]{"This is line 1", "This is line 2" }, fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(FILE, CHARSET);
        LineColumn lineColumn = fileText.lineColumn(15);
        assertEquals(1, lineColumn.getLineNo());
        assertEquals(14, lineColumn.getColumnNo());
    }

    @Test
    public void testGet() {
        FileText fileText = new FileText(FILE, CHARSET);
        assertEquals("This is line 1", fileText.get(0));
        assertEquals("This is line 2", fileText.get(1));
    }

    @Test
    public void testSize() {
        FileText fileText = new FileText(FILE, CHARSET);
        assertEquals(2, fileText.size());
    }
}