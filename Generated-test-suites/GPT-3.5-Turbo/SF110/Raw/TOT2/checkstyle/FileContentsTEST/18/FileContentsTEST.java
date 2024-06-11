
import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructorWithStringLines() {
        // Test typical use case
        FileContents fileContents = new FileContents("test.java", "line1", "line2");
        assertEquals("test.java", fileContents.getFileName());
        assertEquals(2, fileContents.getLines().length);
    }

    @Test
    public void testReportSingleLineComment() {
        // Test with valid parameters
        FileContents fileContents = new FileContents("test.java", "line1", "line2");
        fileContents.reportSingleLineComment("Type", 2, 3);
        assertEquals(1, fileContents.getCppComments().size());
    }

    @Test
    public void testReportCppComment() {
        // Test with valid parameters
        FileContents fileContents = new FileContents("test.java", "line1", "line2");
        fileContents.reportCppComment(1, 1);
        assertEquals(1, fileContents.getCppComments().size());
    }

    // Repeat similar tests for other methods

}

