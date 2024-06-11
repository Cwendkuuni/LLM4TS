import org.junit.Test;
import static org.junit.Assert.*;

public class FileTextTest {

    // Test constructor with valid file and charsetName
    @Test
    public void testConstructor() {
        try {
            FileText fileText = new FileText(new File("test.txt"), "UTF-8");
            assertNotNull(fileText);
            assertEquals("test.txt", fileText.getFile().getName());
            assertEquals("UTF-8", fileText.getCharset().name());
            assertNotNull(fileText.getFullText());
            assertNotNull(fileText.toLinesArray());
            assertEquals(0, fileText.size());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    // Test constructor with invalid charsetName
    @Test(expected = IllegalStateException.class)
    public void testConstructor_InvalidCharsetName() throws IOException {
        FileText fileText = new FileText(new File("test.txt"), "InvalidCharset");
    }

    // Test constructor with non-existent file
    @Test(expected = FileNotFoundException.class)
    public void testConstructor_NonExistentFile() throws IOException {
        FileText fileText = new FileText(new File("non_existent.txt"), "UTF-8");
    }

    // Test fromLines method
    @Test
    public void testFromLines() {
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");
        FileText fileText = FileText.fromLines(new File("test.txt"), lines);
        assertNotNull(fileText);
        assertEquals("test.txt", fileText.getFile().getName());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertEquals(3, fileText.size());
    }

    // Test getFile method
    @Test
    public void testGetFile() throws IOException {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        File file = fileText.getFile();
        assertNotNull(file);
        assertEquals("test.txt", file.getName());
    }

    // Test getCharset method
    @Test
    public void testGetCharset() throws IOException {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        Charset charset = fileText.getCharset();
        assertNotNull(charset);
        assertEquals("UTF-8", charset.name());
    }

    // Test getFullText method
    @Test
    public void testGetFullText() throws IOException {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        CharSequence fullText = fileText.getFullText();
        assertNotNull(fullText);
        assertTrue(fullText.toString().startsWith("Line 1"));
        assertTrue(fullText.toString().endsWith("Line 3\n"));
    }

    // Test toLinesArray method
    @Test
    public void testToLinesArray() throws IOException {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        String[] linesArray = fileText.toLinesArray();
        assertNotNull(linesArray);
        assertEquals(0, linesArray.length);
    }

    // Test lineColumn method
    @Test
    public void testLineColumn() throws IOException {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        LineColumn lineColumn = fileText.lineColumn(10);
        assertNotNull(lineColumn);
        assertEquals(1, lineColumn.getLine());
        assertEquals(10, lineColumn.getColumn());
    }

    // Test get method
    @Test
    public void testGet() throws IOException {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        String line = fileText.get(1);
        assertNotNull(line);
        assertEquals("Line 2", line);
    }

    // Test size method
    @Test
    public void testSize() throws IOException {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        int size = fileText.size();
        assertEquals(0, size);
    }
}