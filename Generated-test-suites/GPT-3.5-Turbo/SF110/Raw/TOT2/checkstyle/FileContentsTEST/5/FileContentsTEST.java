
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import com.puppycrawl.tools.checkstyle.api.*;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        fileContents = new FileContents("testFile.java", "Line 1", "Line 2", "Line 3");
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(1, 0);
        assertEquals(1, fileContents.getCppComments().size());
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(1, 0, 2, 5);
        assertEquals(1, fileContents.getCComments().size());
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportCComment(1, 0, 2, 5);
        TextBlock javadocBefore = fileContents.getJavadocBefore(1);
        assertNotNull(javadocBefore);
    }

    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(0));
    }

    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(0));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertTrue(fileContents.hasIntersectionWithComment(1, 0, 2, 5));
    }

    @Test
    public void testInPackageInfo() {
        assertTrue(fileContents.inPackageInfo());
    }
}
