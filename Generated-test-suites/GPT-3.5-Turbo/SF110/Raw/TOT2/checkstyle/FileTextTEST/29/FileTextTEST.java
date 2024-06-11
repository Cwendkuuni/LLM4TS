import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileTextTest {
    
    @Test
    public void testFileTextConstructor() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertNotNull(fileText);
        } catch (IOException e) {
            fail("Failed to create FileText object");
        }
    }
    
    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertEquals(file, fileText.getFile());
        } catch (IOException e) {
            fail("Failed to create FileText object");
        }
    }
    
    @Test
    public void testGetCharset() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertEquals(Charset.forName(charsetName), fileText.getCharset());
        } catch (IOException e) {
            fail("Failed to create FileText object");
        }
    }

    // Add more test cases for other methods as needed

}