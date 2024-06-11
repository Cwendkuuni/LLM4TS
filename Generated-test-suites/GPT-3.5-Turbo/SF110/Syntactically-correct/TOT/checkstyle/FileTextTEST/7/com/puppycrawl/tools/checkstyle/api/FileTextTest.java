import org.junit.Test;
import static org.junit.Assert.*;

public class FileTextTest {

    private final String testFilePath = "testFilePath";
    private final String charsetName = "UTF-8";
    
    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        File file = new File(testFilePath);
        FileText fileText = new FileText(file, charsetName);
        
        // Test typical use case
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        
        // Test edge case
        assertEquals("", fileText.getFullText().toString());
        
        // Test error scenarios
        try {
            File nonExistentFile = new File("nonExistentPath");
            FileText fileText = new FileText(nonExistentFile, charsetName);
            fail("Expected FileNotFoundException to be thrown");
        } catch (FileNotFoundException ex) {
            // Exception should be thrown
        }
        
        try {
            FileText fileText = new FileText(file, "unsupportedCharset");
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            // Exception should be thrown
            assertEquals("Unsupported charset: unsupportedCharset", ex.getMessage());
        }
    }
    
    @Test
    public void testConstructorWithFileText() throws IOException {
        File file = new File(testFilePath);
        FileText originalFileText = new FileText(file, charsetName);
        
        FileText copiedFileText = new FileText(originalFileText);
        
        // Test typical use case
        assertEquals(file, copiedFileText.getFile());
        assertEquals(originalFileText.getCharset(), copiedFileText.getCharset());
        assertEquals(originalFileText.getFullText().toString(), copiedFileText.getFullText().toString());
        assertArrayEquals(originalFileText.toLinesArray(), copiedFileText.toLinesArray());
        
        // Test edge case
        assertNull(copiedFileText.lineBreaks);
        
    }
    
    @Test
    public void testConstructorWithFileAndLines() throws IOException {
        File file = new File(testFilePath);
        ArrayList<String> lines = new ArrayList<>();
        lines.add("line1");
        lines.add("line2");
        
        FileText fileText = new FileText(file, lines);
        
        // Test typical use case
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line1\nline2\n", fileText.getFullText().toString());
        assertArrayEquals(new String[]{"line1", "line2"}, fileText.toLinesArray());
        
    }
    
    @Test
    public void testFromFileLines() {
        File file = new File(testFilePath);
        ArrayList<String> lines = new ArrayList<>();
        lines.add("line1");
        lines.add("line2");
        
        FileText fileText = FileText.fromLines(file, lines);
        
        // Test typical use case
        assertSame(fileText, lines);
    }
    
    @Test
    public void testGetters() throws IOException {
        File file = new File(testFilePath);
        FileText fileText = new FileText(file, charsetName);
        
        // Test typical use case
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        assertEquals("", fileText.getFullText().toString());
        assertArrayEquals(new String[]{}, fileText.toLinesArray());
        
    }
    
    @Test
    public void testFindLineBreaks() throws IOException {
        File file = new File(testFilePath);
        ArrayList<String> lines = new ArrayList<>();
        lines.add("line1");
        lines.add("line2");
        FileText fileText = new FileText(file, lines);
        
        int[] lineBreaks = fileText.findLineBreaks();
        
        // Test typical use case
        assertArrayEquals(new int[]{0, 6, 12}, lineBreaks);
    }
    
    @Test
    public void testLineColumn() throws IOException {
        File file = new File(testFilePath);
        ArrayList<String> lines = new ArrayList<>();
        lines.add("line1");
        lines.add("line2");
        FileText fileText = new FileText(file, lines);
        
        LineColumn lineColumn = fileText.lineColumn(8);
        
        // Test typical use case
        assertEquals(2, lineColumn.line);
        assertEquals(2, lineColumn.column);
    }
    
    @Test
    public void testGetLine() throws IOException {
        File file = new File(testFilePath);
        ArrayList<String> lines = new ArrayList<>();
        lines.add("line1");
        lines.add("line2");
        FileText fileText = new FileText(file, lines);
        
        // Test typical use case
        assertEquals("line2", fileText.get(1));
        
        // Test edge case
        try {
            fileText.get(2);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException ex) {
            // Exception should be thrown
        }
    }
    
    @Test
    public void testGetSize() throws IOException {
        File file = new File(testFilePath);
        ArrayList<String> lines = new ArrayList<>();
        lines.add("line1");
        lines.add("line2");
        FileText fileText = new FileText(file, lines);
        
        // Test typical use case
        assertEquals(2, fileText.size());
        
        // Test edge case
        lines.clear();
        FileText emptyFileText = new FileText(file, lines);
        assertEquals(0, emptyFileText.size());
    }
    
}