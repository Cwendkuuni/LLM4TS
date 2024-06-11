import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {

    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents("test.txt", "line 1", "line 2");
        fileContents.reportCppComment(1, 5);
        assertNotNull(fileContents.getCppComments().get(1));
    }

    @Test
    public void testExtractCComment() {
        FileContents fileContents = new FileContents("test.txt", "line 1", "line 2");
        String[] cComment = fileContents.extractCComment(1, 4, 2, 6);
        assertEquals(2, cComment.length);
        assertEquals("ne 1", cComment[0]);
        assertEquals("line 2", cComment[1]);
    }

    // More test cases for other methods...

}