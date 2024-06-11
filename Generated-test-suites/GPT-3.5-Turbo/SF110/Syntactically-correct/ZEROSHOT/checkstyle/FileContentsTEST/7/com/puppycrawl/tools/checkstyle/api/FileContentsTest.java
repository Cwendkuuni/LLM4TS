import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        fileContents = new FileContents("testFile.java", "Line 1", "// Single line comment", "Line 3", "/* Block comment */");
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(2, 2);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();

        assertNotNull(cppComments);
        assertEquals(1, cppComments.size());
        assertEquals(" Single line comment", cppComments.get(2).getText()[0]);
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(4, 0, 4, 18);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();

        assertNotNull(cComments);
        assertEquals(1, cComments.size());
        assertEquals(1, cComments.get(4).size());
        assertEquals("/* Block comment */", cComments.get(4).get(0).getText()[0]);
    }

    @Test
    public void testGetJavadocBefore() {
        TextBlock javadocComment = new Comment(new String[]{" Javadoc comment "}, 0, 0, 18);
        fileContents.getJavadocComments().put(0, javadocComment);

        TextBlock result = fileContents.getJavadocBefore(2);

        assertEquals(javadocComment, result);
    }

    @Test
    public void testLineIsBlank() {
        boolean result = fileContents.lineIsBlank(0);
        assertFalse(result);

        result = fileContents.lineIsBlank(1);
        assertTrue(result);
    }

    @Test
    public void testLineIsComment() {
        boolean result = fileContents.lineIsComment(0);
        assertFalse(result);

        result = fileContents.lineIsComment(1);
        assertTrue(result);
    }

    @Test
    public void testHasIntersectionWithComment() {
        boolean result = fileContents.hasIntersectionWithComment(2, 0, 4, 18);
        assertTrue(result);

        result = fileContents.hasIntersectionWithComment(0, 0, 1, 0);
        assertFalse(result);
    }

    @Test
    public void testInPackageInfo() {
        boolean result = fileContents.inPackageInfo();
        assertFalse(result);

        fileContents = new FileContents("package-info.java", "Line 1");
        result = fileContents.inPackageInfo();
        assertTrue(result);
    }

}