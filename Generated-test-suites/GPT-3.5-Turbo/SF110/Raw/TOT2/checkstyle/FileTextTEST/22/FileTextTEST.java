import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ArrayList;

public class FileTextTest {

    @Test
    public void testConstructorFromFileAndCharsetName() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertNotNull(fileText);
            assertEquals(file, fileText.getFile());
            assertEquals(Charset.forName(charsetName), fileText.getCharset());
            // Add more assertions here for comprehensive testing
        } catch (IOException e) {
            fail("Exception thrown during test: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorFromFileText() {
        // Create a FileText object to use for testing
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText originalFileText;
        try {
            originalFileText = new FileText(file, charsetName);
        } catch (IOException e) {
            fail("Exception thrown during setup: " + e.getMessage());
            return;
        }

        FileText copiedFileText = new FileText(originalFileText);
        assertNotNull(copiedFileText);
        assertEquals(originalFileText.getFile(), copiedFileText.getFile());
        // Add more assertions here for comprehensive testing
    }

    // Add more test cases for other public methods

    @Test
    public void testGetLineColumn() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            LineColumn lineColumn = fileText.lineColumn(10);
            assertNotNull(lineColumn);
            // Add assertions for line number and column number
        } catch (IOException e) {
            fail("Exception thrown during test: " + e.getMessage());
        }

    }

    // Add more test cases for other methods

    @Test(expected = FileNotFoundException.class)
    public void testReadFileNonExistentFile() throws IOException {
        File nonExistentFile = new File("nonExistentFile.txt");
        CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
        FileText.readFile(nonExistentFile, decoder);
    }

}