
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class FileTextTest {

    @Test
    public void testFileTextConstructor_withExistingFileAndSupportedCharset() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        
        FileText fileText = new FileText(file, charsetName);
        
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertEquals(charsetName, fileText.getCharset().name());
        assertNotNull(fileText.getFullText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileTextConstructor_withUnsupportedCharset() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "unsupportedCharset";

        FileText fileText = new FileText(file, charsetName);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructor_withNonExistingFile() throws IOException {
        File file = new File("nonExistingFile.txt");
        String charsetName = "UTF-8";
        
        FileText fileText = new FileText(file, charsetName);
    }
    
    @Test
    public void testFileTextConstructor_copyConstructor() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText originalFileText = new FileText(file, charsetName);
        
        FileText copiedFileText = new FileText(originalFileText);
        
        assertNotNull(copiedFileText);
        assertEquals(originalFileText.getFile(), copiedFileText.getFile());
        assertEquals(originalFileText.getCharset(), copiedFileText.getCharset());
        assertEquals(originalFileText.getFullText(), copiedFileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), copiedFileText.toLinesArray());
    }
    
    @Test
    public void testFromFileTextMethod_withFileTextInstance() {
        File file = new File("testFile.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        FileText originalFileText = new FileText(file, lines);
        
        FileText convertedFileText = FileText.fromLines(file, originalFileText);
        
        assertNotNull(convertedFileText);
        assertEquals(originalFileText.getFile(), convertedFileText.getFile());
        assertEquals(originalFileText.getFullText(), convertedFileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), convertedFileText.toLinesArray());
    }
    
    @Test
    public void testFromFileTextMethod_withListOfStrings() {
        File file = new File("testFile.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        
        FileText fileText = FileText.fromLines(file, lines);
        
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertArrayEquals(lines.toArray(), fileText.toLinesArray());
    }
    
    // Additional test cases for other methods can be added
    
}

