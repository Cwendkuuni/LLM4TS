import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import com.google.common.collect.*;

public class FileContentsTest {

    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents("testFile.java");
        fileContents.reportCppComment(1, 1);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertTrue(cppComments.containsKey(1));
        assertEquals("comment", cppComments.get(1).getText()[0]);
    }

    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents("testFile.java");
        fileContents.reportCComment(1, 1, 2, 5);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertTrue(cComments.containsKey(1));
        assertEquals("comment1", cComments.get(1).get(0).getText()[0]);
        assertEquals("comment2", cComments.get(1).get(1).getText()[0]);
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("testFile.java");
        fileContents.reportCComment(1, 1, 2, 5);
        fileContents.reportCComment(5, 1, 6, 5);
        fileContents.reportCComment(10, 1, 11, 5);
        fileContents.reportCComment(15, 1, 16, 5);

        TextBlock javadocBefore1 = fileContents.getJavadocBefore(2);
        assertNotNull(javadocBefore1);
        assertEquals("comment2", javadocBefore1.getText()[0]);

        TextBlock javadocBefore2 = fileContents.getJavadocBefore(8);
        assertNotNull(javadocBefore2);
        assertEquals("comment6", javadocBefore2.getText()[0]);
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("testFile.java");
        assertTrue(fileContents.lineIsBlank(2));
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("testFile.java");
        assertTrue(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("testFile.java");
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 2, 5));
        assertFalse(fileContents.hasIntersectionWithComment(3, 1, 4, 5));
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("testFile(package-info).java");
        assertTrue(fileContents.inPackageInfo());
    }
}