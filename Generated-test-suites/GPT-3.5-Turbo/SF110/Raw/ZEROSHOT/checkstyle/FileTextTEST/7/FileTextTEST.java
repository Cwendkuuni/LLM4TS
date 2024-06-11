import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {
    
    private FileText fileText;
    
    @Before
    public void setUp() throws IOException {
        File file = new File("testfile.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");
        this.fileText = FileText.fromLines(file, lines);
    }
    
    @Test
    public void testGetFile() {
        File expected = new File("testfile.txt");
        File actual = fileText.getFile();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetCharset() {
        Charset expected = null;
        Charset actual = fileText.getCharset();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetFullText() {
        CharSequence expected = "Line 1\nLine 2\nLine 3\n";
        CharSequence actual = fileText.getFullText();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToLinesArray() {
        String[] expected = {"Line 1", "Line 2", "Line 3"};
        String[] actual = fileText.toLinesArray();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLineColumn() {
        LineColumn expected = new LineColumn(2, 4);
        LineColumn actual = fileText.lineColumn(9);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGet() {
        String expected = "Line 2";
        String actual = fileText.get(1);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSize() {
        int expected = 3;
        int actual = fileText.size();
        assertEquals(expected, actual);
    }
}