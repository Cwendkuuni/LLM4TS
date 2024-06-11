
import org.junit.*;
import static org.junit.Assert.*;

public class FileTextTest {

    @Test
    public void testFileTextConstructor() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            // Assert file, charset, fullText, and lines are initialized correctly
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
    public void testFileTextCopyConstructor() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText originalFileText = new FileText(file, charsetName);
            FileText copiedFileText = new FileText(originalFileText);
            // Assert file, charset, fullText, and lines are copied correctly
            assertEquals(originalFileText.getFile(), copiedFileText.getFile());
            assertEquals(originalFileText.getCharset(), copiedFileText.getCharset());
            assertEquals(originalFileText.getFullText(), copiedFileText.getFullText());
            assertArrayEquals(originalFileText.toLinesArray(), copiedFileText.toLinesArray());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testFileTextFromLines() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line1\nline2\nline3\n", fileText.getFullText().toString());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
        assertEquals(lines.size(), fileText.size());
        assertEquals("line2", fileText.get(1));
    }
    
    @Test
    public void testGetLineColumn() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            // Test lineColumn method with different positions
            LineColumn lineColumn1 = fileText.lineColumn(5);
            assertEquals(1, lineColumn1.getLine());
            assertEquals(5, lineColumn1.getColumn());
            LineColumn lineColumn2 = fileText.lineColumn(20);
            assertEquals(2, lineColumn2.getLine());
            assertEquals(1, lineColumn2.getColumn());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Additional test cases for edge cases and exception handling can be added here
    
}

