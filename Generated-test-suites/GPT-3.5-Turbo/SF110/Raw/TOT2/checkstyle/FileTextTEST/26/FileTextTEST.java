import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FileTextTest {

    @Test
    public void testFileTextConstructorWithFileAndCharsetName() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
    }

    @Test
    public void testFileTextConstructorWithFileText() {
        File file = new File("test.txt");
        FileText originalFileText = new FileText(file, "UTF-8");
        FileText copiedFileText = new FileText(originalFileText);
        assertEquals(originalFileText.getFile(), copiedFileText.getFile());
        assertEquals(originalFileText.getCharset(), copiedFileText.getCharset());
        assertEquals(originalFileText.getFullText(), copiedFileText.getFullText());
    }

    @Test
    public void testFileTextFromLines() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        FileText fileText = FileText.fromLines(file, lines);
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertArrayEquals(lines.toArray(), fileText.toLinesArray());
    }

    // Additional test cases for edge cases and exception handling can be added here

}