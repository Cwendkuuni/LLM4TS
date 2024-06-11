import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileTextTest {

    @Test
    public void testFileTextConstructorWithFileAndCharsetName() {
        // Test typical use case
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertNotNull(fileText.getFullText());
            assertEquals(0, fileText.lineColumn(0).getLine());
            assertEquals(0, fileText.lineColumn(0).getColumn());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFileTextConstructorWithFileText() {
        // Test typical use case
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText originalFileText = new FileText(file, charsetName);
            FileText fileText = new FileText(originalFileText);
            assertEquals(originalFileText.getFullText(), fileText.getFullText());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadFile() {
        // Test typical use case
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            String result = FileText.readFile(file, Charset.forName(charsetName).newDecoder());
            assertNotNull(result);
            assertTrue(result.length() > 0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    // Add more test cases for other methods and edge cases

}