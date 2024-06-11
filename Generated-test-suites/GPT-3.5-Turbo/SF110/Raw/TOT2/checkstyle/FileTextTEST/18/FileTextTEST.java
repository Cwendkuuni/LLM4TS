// JUnit test cases for FileText class

import static org.junit.Assert.*;
import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileText_constructor_withExistingFile_successful() throws IOException {
        // Test the constructor with an existing file
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testFileText_fromLines_withEmptyLinesList_successful() {
        // Test fromLines method with an empty list of lines
        File file = new File("testFile.txt");
        List<String> lines = new ArrayList<>();
        FileText fileText = FileText.fromLines(file, lines);
        
        assertNotNull(fileText);
        assertEquals(0, fileText.size());
    }

    @Test
    public void testGetCharset_withUnsupportedCharset_exceptionThrown() {
        // Test getCharset method with an unsupported charset
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "InvalidCharset");
        
        try {
            fileText.getCharset();
            fail("Expected exception not thrown");
        } catch (IllegalStateException e) {
            assertEquals("Unsupported charset: InvalidCharset", e.getMessage());
        }
    }

    // More test cases for other methods can be added here

}