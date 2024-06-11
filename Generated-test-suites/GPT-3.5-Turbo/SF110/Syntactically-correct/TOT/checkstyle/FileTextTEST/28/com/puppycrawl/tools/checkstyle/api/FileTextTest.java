import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class FileTextTest {

    private static final String CHARSET_NAME = "UTF-8";
    private static final File TEST_FILE = new File("testfile.txt");
    private static final List<String> TEST_LINES = Arrays.asList("Line 1", "Line 2", "Line 3");
    
    @Test
    public void testFileTextConstructor() throws IOException {
        FileText fileText = new FileText(TEST_FILE, CHARSET_NAME);
        
        assertEquals(TEST_FILE, fileText.getFile());
        assertEquals(Charset.forName(CHARSET_NAME), fileText.getCharset());
        assertEquals("", fileText.getFullText().toString());
        assertArrayEquals(new String[]{}, fileText.toLinesArray());
    }
    
    @Test
    public void testFileTextConstructor_InvalidCharset() {
        assertThrows(IllegalStateException.class, () -> {
            new FileText(TEST_FILE, "InvalidCharset");
        });
    }
    
    @Test
    public void testFileTextConstructor_FileDoesNotExist() {
        assertThrows(FileNotFoundException.class, () -> {
            new FileText(new File("nonexistent.txt"), CHARSET_NAME);
        });
    }
    
    @Test
    public void testFileTextCopyConstructor() {
        FileText originalFileText = new FileText(TEST_FILE, CHARSET_NAME);
        FileText copiedFileText = new FileText(originalFileText);
        
        assertEquals(originalFileText.getFile(), copiedFileText.getFile());
        assertEquals(originalFileText.getCharset(), copiedFileText.getCharset());
        assertEquals(originalFileText.getFullText(), copiedFileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), copiedFileText.toLinesArray());
        assertNotSame(originalFileText.toLinesArray(), copiedFileText.toLinesArray());
    }
    
    @Test
    public void testFromFileLines() {
        FileText fileText = FileText.fromLines(TEST_FILE, TEST_LINES);
        
        assertEquals(TEST_FILE, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("Line 1\nLine 2\nLine 3\n", fileText.getFullText().toString());
        assertArrayEquals(new String[]{"Line 1", "Line 2", "Line 3"}, fileText.toLinesArray());
    }
    
    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(TEST_FILE, CHARSET_NAME);
        
        LineColumn lineColumn = fileText.lineColumn(10);
        
        assertEquals(1, lineColumn.getLine());
        assertEquals(10, lineColumn.getColumn());
    }
    
    @Test
    public void testGet() {
        FileText fileText = new FileText(TEST_FILE, CHARSET_NAME);
        
        assertEquals("", fileText.get(0));
    }
    
    @Test
    public void testSize() {
        FileText fileText = new FileText(TEST_FILE, CHARSET_NAME);
        
        assertEquals(0, fileText.size());
    }
  
    @After
    public void tearDown() {
        TEST_FILE.delete();
    }
}