import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import static org.junit.Assert.*;

public class FileTextTest {

    @Test
    public void testConstructor1() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        assertNotNull(fileText);
    }

    @Test
    public void testConstructor2() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        FileText copy = new FileText(fileText);
        assertEquals(fileText.getCharset(), copy.getCharset());
    }

    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetFullText() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        assertNotNull(fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        assertNotNull(fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        LineColumn lineColumn = fileText.lineColumn(0);
        assertNotNull(lineColumn);
    }

    // Additional test cases for edge cases and exception handling can be added here

}