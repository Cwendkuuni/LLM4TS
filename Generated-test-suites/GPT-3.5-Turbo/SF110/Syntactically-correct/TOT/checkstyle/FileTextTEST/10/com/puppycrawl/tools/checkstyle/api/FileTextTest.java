import static org.junit.Assert.*;
import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextConstructor() {
        try {
            File file = new File("test.txt");
            String charsetName = "UTF-8";
            
            FileText fileText = new FileText(file, charsetName);
            
            assertEquals(file, fileText.getFile());
            assertEquals(Charset.forName(charsetName), fileText.getCharset());
            assertNotNull(fileText.getFullText());
            assertNotNull(fileText.toLinesArray());
            assertEquals(0, fileText.size());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testFileTextConstructorUnsupportedCharset() {
        try {
            File file = new File("test.txt");
            String invalidCharsetName = "invalidcharset";
            
            FileText fileText = new FileText(file, invalidCharsetName);
            
            fail("UnsupportedCharsetException not thrown");
        } catch (IllegalStateException e) {
            // UnsupportedCharsetException caught, test passed
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testFromLines() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("line 1");
        lines.add("line 2");
        
        FileText fileText = FileText.fromLines(file, lines);
        
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line 1\nline 2\n", fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertEquals(2, fileText.size());
        assertEquals("line 1", fileText.get(0));
        assertEquals("line 2", fileText.get(1));
    }
    
    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("line 1");
        lines.add("line 2");
        lines.add("line 3");
        FileText fileText = FileText.fromLines(file, lines);
        
        LineColumn lineColumn1 = fileText.lineColumn(5);
        LineColumn lineColumn2 = fileText.lineColumn(10);
        LineColumn lineColumn3 = fileText.lineColumn(15);
        
        assertEquals(1, lineColumn1.getLine());
        assertEquals(5, lineColumn1.getColumn());
        assertEquals(2, lineColumn2.getLine());
        assertEquals(1, lineColumn2.getColumn());
        assertEquals(2, lineColumn3.getLine());
        assertEquals(6, lineColumn3.getColumn());
    }
}