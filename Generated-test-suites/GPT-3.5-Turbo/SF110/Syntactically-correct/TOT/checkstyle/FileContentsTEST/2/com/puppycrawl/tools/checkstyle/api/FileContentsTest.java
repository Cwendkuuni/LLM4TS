import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructor1() {
        String[] lines = {"line 1", "line 2", "line 3"};
        FileContents fileContents = new FileContents("test.txt", lines);
        assertNotNull(fileContents);
        assertEquals("test.txt", fileContents.getFileName());
        assertArrayEquals(lines, fileContents.getLines());
    }

    @Test
    public void testFileContentsConstructor2() {
        FileText fileText = new FileText("test.txt", "line 1", "line 2", "line 3");
        FileContents fileContents = new FileContents(fileText);
        assertNotNull(fileContents);
        assertEquals("test.txt", fileContents.getFileName());
        assertArrayEquals(fileText.toLinesArray(), fileContents.getLines());
    }

    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("test.txt");
        fileContents.reportSingleLineComment("type", 1, 1);
        assertFalse(fileContents.getCppComments().isEmpty());
        assertEquals(1, fileContents.getCppComments().size());
    }

    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("test.txt");
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        assertFalse(fileContents.getCComments().isEmpty());
        assertEquals(1, fileContents.getCComments().size());
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("test.txt");
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        TextBlock textBlock = fileContents.getJavadocBefore(2);
        assertNotNull(textBlock);
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("test.txt", "", "line 2", "");
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertTrue(fileContents.lineIsBlank(2));
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("test.txt", "// comment", "line 2", "");
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("test.txt");
        fileContents.reportSingleLineComment("type", 1, 1);
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 1, 5));
        assertFalse(fileContents.hasIntersectionWithComment(3, 1, 3, 5));
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents1 = new FileContents("package-info.java");
        assertTrue(fileContents1.inPackageInfo());

        FileContents fileContents2 = new FileContents("test.txt");
        assertFalse(fileContents2.inPackageInfo());
    }

}