import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        fileContents = new FileContents("test.java", "line 1", "line 2", "line 3");
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(2, 3);
        assertEquals("line 3", fileContents.getCppComments().get(2).getText()[0]);
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(1, 2, 2, 4);
        assertEquals(1, fileContents.getCComments().size());
    }

    @Test
    public void testGetJavadocBefore() {
        TextBlock javadoc = new TextBlock("javadoc", 1);
        fileContents.getJavadocComments().put(1, javadoc);
        assertEquals(javadoc, fileContents.getJavadocBefore(2));
    }

    // Additional test cases for other methods can be added here

}