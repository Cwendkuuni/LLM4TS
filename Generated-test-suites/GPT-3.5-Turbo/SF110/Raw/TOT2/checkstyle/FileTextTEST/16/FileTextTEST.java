import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.*;
import java.util.ArrayList;
import java.util.List;

public class FileTextTest {
    
    @Test
    public void testFileTextConstructor() throws IOException {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertNotNull(fileText);
    }
    
    @Test
    public void testFileTextCopyConstructor() {
        File file = new File("testFile.txt");
        FileText originalFileText = new FileText(file, "UTF-8");
        FileText copiedFileText = new FileText(originalFileText);
        assertNotNull(copiedFileText);
    }
    
    @Test
    public void testFromFileLinesMethod() {
        List<String> lines = new ArrayList<>();
        lines.add("This is a test line");
        File file = new File("testFile.txt");
        FileText fileText = FileText.fromLines(file, lines);
        assertNotNull(fileText);
    }
    
    // Add more test cases for other methods
    
}