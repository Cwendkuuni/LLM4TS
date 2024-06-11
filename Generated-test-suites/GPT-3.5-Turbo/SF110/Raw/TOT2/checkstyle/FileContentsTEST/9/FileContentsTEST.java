
import static org.junit.Assert.*;
import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText(new File("testFile.txt"), Arrays.asList("Line 1", "Line 2"));
        FileContents fileContents = new FileContents(fileText);
        
        assertEquals("testFile.txt", fileContents.getFileName());
        assertEquals(2, fileContents.getLines().length);
        assertEquals("Line 1", fileContents.getLines()[0]);
        assertEquals("Line 2", fileContents.getLine(1));
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", "// this is a package-info file");
        
        assertTrue(fileContents.inPackageInfo());
    }
}
