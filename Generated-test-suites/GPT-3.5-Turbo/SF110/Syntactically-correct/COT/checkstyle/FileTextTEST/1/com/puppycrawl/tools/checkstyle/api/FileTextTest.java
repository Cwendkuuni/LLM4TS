import static org.junit.Assert.*;
import org.junit.*;

public class FileTextTest {

    @Test
    public void testConstructor() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText ft = new FileText(file, charsetName);
            assertNotNull(ft);
            assertEquals(file, ft.getFile());
            assertEquals(Charset.forName(charsetName), ft.getCharset());
        } catch(IOException e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testCopyConstructor() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText ft1 = new FileText(file, charsetName);
            FileText ft2 = new FileText(ft1);
            assertNotNull(ft2);
            assertEquals(file, ft2.getFile());
            assertEquals(Charset.forName(charsetName), ft2.getCharset());
            assertEquals(ft1.getFullText(), ft2.getFullText());
            assertArrayEquals(ft1.toLinesArray(), ft2.toLinesArray());
        } catch(IOException e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testFromLines() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        
        FileText ft = FileText.fromLines(file, lines);
        assertNotNull(ft);
        assertEquals(file, ft.getFile());
        assertNull(ft.getCharset());
        assertEquals("line 1\nline 2\nline 3\n", ft.getFullText());
        assertArrayEquals(new String[]{"line 1", "line 2", "line 3"}, ft.toLinesArray());
    }

    @Test
    public void testGet() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText ft = new FileText(file, charsetName);
            assertEquals("First line", ft.get(0));
            assertEquals("Second line", ft.get(1));
            assertEquals("Third line", ft.get(2));
        } catch(IOException e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testSize() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText ft = new FileText(file, charsetName);
            assertEquals(3, ft.size());
        } catch(IOException e) {
            fail("Exception should not be thrown.");
        }
    }
    
    // Edge cases and exception handling scenarios
    
    @Test(expected = IOException.class)
    public void testConstructorFileNotFound() throws IOException {
        File file = new File("nonexistent.txt");
        String charsetName = "UTF-8";
        FileText ft = new FileText(file, charsetName);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorUnsupportedCharset() throws IOException {
        File file = new File("test.txt");
        String charsetName = "invalidCharset";
        FileText ft = new FileText(file, charsetName);
    }

    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText ft = new FileText(file, charsetName);
            LineColumn lc = ft.lineColumn(10);
            assertEquals(1, lc.getLine());
            assertEquals(9, lc.getColumn());
        } catch(IOException e) {
            fail("Exception should not be thrown.");
        }
    }
}