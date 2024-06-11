import static org.junit.Assert.*;
import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructorWithStringArray() {
        // Test typical usage
        FileContents fileContents = new FileContents("test.java", "line1", "line2");
        assertEquals("test.java", fileContents.getFileName());
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));

        // Test edge case with empty lines array
        fileContents = new FileContents("test.java");
        assertEquals("test.java", fileContents.getFileName());
        assertEquals(0, fileContents.getLines().length);
    }

    @Test
    public void testFileContentsConstructorWithFileText() {
        // Test typical usage
        FileText fileText = new FileText(new File("test.java"), Arrays.asList("line1", "line2"));
        FileContents fileContents = new FileContents(fileText);
        assertEquals("test.java", fileContents.getFileName());
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
    }

    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents("test.java");

        // Test typical usage
        fileContents.reportCppComment(1, 1);
        assertEquals("line1", fileContents.getCppComments().get(1).getText()[0]);

        // Test edge case with empty line
        fileContents.reportCppComment(2, 1);
        assertNull(fileContents.getCppComments().get(2));
    }

    // Add more test methods for other public methods in FileContents class

}