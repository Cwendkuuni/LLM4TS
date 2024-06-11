
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class FileTextTest {

    private Charset charset = Charset.forName("UTF-8");
    private String filePath = "test.txt";
    
    @Test
    public void testConstructor() throws IOException {
        File file = new File(filePath);
        FileText fileText = new FileText(file, "UTF-8");
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(charset, fileText.getCharset());
        assertEquals("This is the first line\nThis is the second line\nThis is the third line\n", fileText.getFullText().toString());
        assertArrayEquals(new String[]{"This is the first line", "This is the second line", "This is the third line"}, fileText.toLinesArray());
        assertEquals(3, fileText.size());
    }
    
    @Test
    public void testCopyConstructor() throws IOException {
        File file = new File(filePath);
        FileText fileText = new FileText(file, "UTF-8");
        FileText copiedFileText = new FileText(fileText);
        assertNotNull(copiedFileText);
        assertEquals(file, copiedFileText.getFile());
        assertEquals(charset, copiedFileText.getCharset());
        assertEquals("This is the first line\nThis is the second line\nThis is the third line\n", copiedFileText.getFullText().toString());
        assertArrayEquals(new String[]{"This is the first line", "This is the second line", "This is the third line"}, copiedFileText.toLinesArray());
        assertEquals(3, copiedFileText.size());
    }
    
    @Test
    public void testFromLines() {
        File file = new File(filePath);
        List<String> lines = new ArrayList<>();
        lines.add("This is the first line");
        lines.add("This is the second line");
        lines.add("This is the third line");
        
        FileText fileText = FileText.fromLines(file, lines);
        
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("This is the first line\nThis is the second line\nThis is the third line\n", fileText.getFullText().toString());
        assertArrayEquals(new String[]{"This is the first line", "This is the second line", "This is the third line"}, fileText.toLinesArray());
        assertEquals(3, fileText.size());
    }
    
    @Test
    public void testGet() throws IOException {
        File file = new File(filePath);
        FileText fileText = new FileText(file, "UTF-8");
        
        assertEquals("This is the first line", fileText.get(0));
        assertEquals("This is the second line", fileText.get(1));
        assertEquals("This is the third line", fileText.get(2));
    }
    
    @Test
    public void testSize() throws IOException {
        File file = new File(filePath);
        FileText fileText = new FileText(file, "UTF-8");
        
        assertEquals(3, fileText.size());
    }
    
    @Test
    public void testLineColumn() throws IOException {
        File file = new File(filePath);
        FileText fileText = new FileText(file, "UTF-8");
        
        assertEquals(new LineColumn(1, 1), fileText.lineColumn(0));
        assertEquals(new LineColumn(1, 5), fileText.lineColumn(4));
        assertEquals(new LineColumn(2, 1), fileText.lineColumn(20));
        assertEquals(new LineColumn(2, 13), fileText.lineColumn(32));
        assertEquals(new LineColumn(3, 1), fileText.lineColumn(44));
        assertEquals(new LineColumn(3, 16), fileText.lineColumn(59));
    }
}
