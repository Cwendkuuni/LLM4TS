import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.*;

public class FileTextTest {

    private FileText fileText;
    private File testFile;
    
    @Before
    public void setUp() {
        testFile = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Hello");
        lines.add("World");
        fileText = FileText.fromLines(testFile, lines);
    }
    
    @Test
    public void testConstructor_FromLines() {
        assertEquals(testFile, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("Hello\nWorld\n", fileText.getFullText());
        assertArrayEquals(new String[]{"Hello", "World"}, fileText.toLinesArray());
    }
    
    @Test
    public void testConstructor_FileCharset() throws IOException {
        FileText file = new FileText(testFile, "UTF-8");
        assertEquals(testFile, file.getFile());
        assertEquals(Charset.forName("UTF-8"), file.getCharset());
        assertEquals("Hello\nWorld\n", file.getFullText());
        assertArrayEquals(new String[]{"Hello", "World"}, file.toLinesArray());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testConstructor_UnsupportedCharset() throws IOException {
        new FileText(testFile, "FakeCharset");
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testConstructor_FileNotFound() throws IOException {
        File nonExistentFile = new File("nonexistent.txt");
        new FileText(nonExistentFile, "UTF-8");
    }
    
    @Test
    public void testConstructor_Copy() {
        FileText copy = new FileText(fileText);
        assertNotSame(fileText, copy);
        assertEquals(fileText.getFile(), copy.getFile());
        assertEquals(fileText.getCharset(), copy.getCharset());
        assertEquals(fileText.getFullText(), copy.getFullText());
        assertArrayEquals(fileText.toLinesArray(), copy.toLinesArray());
    }
    
    @Test
    public void testLineColumn() {
        assertEquals(new LineColumn(1, 0), fileText.lineColumn(0));
        assertEquals(new LineColumn(1, 1), fileText.lineColumn(1));
        assertEquals(new LineColumn(2, 0), fileText.lineColumn(6));
        assertEquals(new LineColumn(2, 1), fileText.lineColumn(7));
    }
    
    @Test
    public void testGet() {
        assertEquals("Hello", fileText.get(1));
        assertEquals("World", fileText.get(2));
        assertNull(fileText.get(0));
        assertNull(fileText.get(3));
    }
    
    @Test
    public void testSize() {
        assertEquals(2, fileText.size());
    }
}