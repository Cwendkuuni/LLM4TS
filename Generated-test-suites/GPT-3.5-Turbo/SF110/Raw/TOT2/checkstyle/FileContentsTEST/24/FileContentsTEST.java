import static org.junit.Assert.*;
import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testReportCppComment() {
        FileContents file = new FileContents("testFile.java", "line1", "line2", "line3");
        file.reportCppComment(1, 5);
        assertEquals(1, file.getCppComments().size());
    }
    
    // Add more test cases for other methods
    
}