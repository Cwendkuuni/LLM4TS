import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {

    @Test
    public void testConstructorWithLines() {
        FileContents file = new FileContents("test.txt", "line 1", "line 2");
        assertEquals("test.txt", file.getFileName());
        assertEquals("line 1", file.getLine(0));
        assertEquals("line 2", file.getLine(1));
    }

    @Test
    public void testConstructorWithFileText() {
        FileText text = new FileText(new File("test.txt"));
        FileContents file = new FileContents(text);
        assertEquals("test.txt", file.getFileName());
        assertEquals(text, file.getText());
    }

    @Test
    public void testReportSingleLineComment() {
        FileContents file = new FileContents("test.txt");
        file.reportSingleLineComment("type", 1, 1);
        assertNotNull(file.getCppComments().get(1));
    }

    @Test
    public void testReportBlockComment() {
        FileContents file = new FileContents("test.txt");
        file.reportBlockComment("type", 1, 1, 2, 2);
        assertNotNull(file.getCComments().get(1));
        assertEquals(1, file.getJavadocBefore(3));
    }

    @Test
    public void testGetCppComments() {
        FileContents file = new FileContents("test.txt");
        file.reportCppComment(1, 1);
        assertEquals(1, file.getCppComments().size());
    }

    @Test
    public void testGetCComments() {
        FileContents file = new FileContents("test.txt");
        file.reportCComment(1, 1, 2, 2);
        assertEquals(1, file.getCComments().size());
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents file = new FileContents("test.txt");
        file.reportCComment(1, 1, 2, 2);
        assertEquals(2, file.getJavadocBefore(3));
    }

    @Test
    public void testGetText() {
        FileContents file = new FileContents("test.txt");
        assertEquals(new FileText(new File("test.txt")), file.getText());
    }

    @Test
    public void testGetLines() {
        FileContents file = new FileContents("test.txt", "line 1", "line 2");
        assertArrayEquals(new String[]{"line 1", "line 2"}, file.getLines());
    }

    @Test
    public void testGetLine() {
        FileContents file = new FileContents("test.txt", "line 1");
        assertEquals("line 1", file.getLine(0));
    }

    @Test
    public void testGetFileName() {
        FileContents file = new FileContents("test.txt");
        assertEquals("test.txt", file.getFileName());
    }

    @Test
    public void testGetFilename() {
        FileContents file = new FileContents("test.txt");
        assertEquals("test.txt", file.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        FileContents file = new FileContents("test.txt", "", "line 2");
        assertTrue(file.lineIsBlank(0));
        assertFalse(file.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        FileContents file = new FileContents("test.txt", "// comment");
        assertTrue(file.lineIsComment(0));
        assertFalse(file.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents file = new FileContents("test.txt");
        assertFalse(file.hasIntersectionWithComment(1, 1, 2, 2));
    }

    @Test
    public void testInPackageInfo() {
        FileContents file = new FileContents("package-info.java");
        assertTrue(file.inPackageInfo());
        file = new FileContents("test.txt");
        assertFalse(file.inPackageInfo());
    }
}