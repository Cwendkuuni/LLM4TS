import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.*;

public class FileTextTest {

    @Test
    public void testFileTextConstructor() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertNotNull(fileText);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testFileTextCopyConstructor() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText originalFileText = new FileText(file, charsetName);
            FileText copiedFileText = new FileText(originalFileText);
            assertEquals(originalFileText.getFile(), copiedFileText.getFile());
            assertEquals(originalFileText.getCharset(), copiedFileText.getCharset());
            assertEquals(originalFileText.getFullText(), copiedFileText.getFullText());
            assertArrayEquals(originalFileText.toLinesArray(), copiedFileText.toLinesArray());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testReadFile() {
        File file = new File("test.txt");
        CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
        try {
            String fileContent = FileText.readFile(file, decoder);
            assertNotNull(fileContent);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    // Add more test cases for other methods
    
}