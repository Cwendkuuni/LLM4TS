import static org.junit.Assert.*;
import java.io.*;
import java.util.*;
import org.junit.*;

public class FileTextTest {

    private static FileText fileText;

    @BeforeClass
    public static void setUp() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");
        lines.add("Line 4");
        fileText = FileText.fromLines(file, lines);
    }

    @AfterClass
    public static void tearDown() {
        File file = new File("testFile.txt");
        file.delete();
    }
    
    @Test
    public void testGetFile() {
        File expectedFile = new File("testFile.txt");
        File actualFile = fileText.getFile();
        assertEquals(expectedFile, actualFile);
    }
    
    @Test
    public void testGetCharset() {
        Charset expectedCharset = null;
        Charset actualCharset = fileText.getCharset();
        assertEquals(expectedCharset, actualCharset);
    }
    
    @Test
    public void testGetFullText() {
        CharSequence expectedFullText = "Line 1\nLine 2\nLine 3\nLine 4\n";
        CharSequence actualFullText = fileText.getFullText();
        assertEquals(expectedFullText, actualFullText);
    }
    
    @Test
    public void testToLinesArray() {
        String[] expectedLines = {"Line 1", "Line 2", "Line 3", "Line 4"};
        String[] actualLines = fileText.toLinesArray();
        assertArrayEquals(expectedLines, actualLines);
    }
    
    @Test
    public void testLineColumn() {
        LineColumn expectedLineColumn = new LineColumn(3, 6);
        LineColumn actualLineColumn = fileText.lineColumn(15);
        assertEquals(expectedLineColumn, actualLineColumn);
    }
    
    @Test
    public void testGet() {
        String expectedLine = "Line 2";
        String actualLine = fileText.get(1);
        assertEquals(expectedLine, actualLine);
    }
    
    @Test
    public void testSize() {
        int expectedSize = 4;
        int actualSize = fileText.size();
        assertEquals(expectedSize, actualSize);
    }
}