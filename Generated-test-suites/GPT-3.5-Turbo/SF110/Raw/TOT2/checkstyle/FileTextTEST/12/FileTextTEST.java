import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FileTextTest {

    @Test
    public void testFileTextConstructor() {
        // Test constructor with valid file and charset
        File file = new File("test.txt");
        try {
            FileText fileText = new FileText(file, "UTF-8");
            assertNotNull(fileText);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        // Test constructor with invalid file
        File invalidFile = new File("invalid.txt");
        try {
            new FileText(invalidFile, "UTF-8");
            fail("IOException should be thrown for invalid file");
        } catch (IOException e) {
            // Exception expected
        }

        // Test constructor with unsupported charset
        File file2 = new File("test2.txt");
        try {
            new FileText(file2, "INVALID_CHARSET");
            fail("IllegalStateException should be thrown for unsupported charset");
        } catch (IllegalStateException e) {
            // Exception expected
        }
    }

    @Test
    public void testFileTextCopyConstructor() {
        // Test copy constructor
        File file = new File("test.txt");
        try {
            FileText original = new FileText(file, "UTF-8");
            FileText copy = new FileText(original);
            assertEquals(original.getFullText(), copy.getFullText());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Additional test cases for other public methods can be added here
}