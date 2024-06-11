
import static org.junit.Assert.*;
import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testConstructorWithFilename() {
        FileContents fileContents = new FileContents("example.txt", "line 1", "line 2");
        assertEquals("example.txt", fileContents.getFileName());
        assertArrayEquals(new String[]{"line 1", "line 2"}, fileContents.getLines());
    }

    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText("example.txt", new String[]{"line 1", "line 2"});
        FileContents fileContents = new FileContents(fileText);
        assertEquals("example.txt", fileContents.getFileName());
        assertArrayEquals(new String[]{"line 1", "line 2"}, fileContents.getLines());
    }

    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("example.txt", "line 1", "line 2");
        fileContents.reportSingleLineComment("cpp", 1, 0);
        assertEquals(1, fileContents.getCppComments().size());
        assertNotNull(fileContents.getCppComments().get(1));
    }

    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("example.txt", "line 1", "line 2");
        fileContents.reportBlockComment("c", 1, 0, 1, 5);
        assertEquals(1, fileContents.getCComments().size());
        assertNotNull(fileContents.getCComments().get(1));
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("example.txt", "line 1", "line 2");
        fileContents.reportBlockComment("c", 1, 0, 1, 5);
        fileContents.reportBlockComment("c", 2, 0, 2, 5);
        TextBlock javadoc = new TextBlock(new String[]{"line 1"}, 0, 0, 0);
        fileContents.getJavadocComments().put(1, javadoc);
        assertEquals(javadoc, fileContents.getJavadocBefore(2));
    }

    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("example.txt", "line 1", "line 2");
        assertEquals("line 1", fileContents.getText().get(0));
        assertEquals("line 2", fileContents.getText().get(1));
    }

    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("example.txt", "line 1", "line 2");
        assertArrayEquals(new String[]{"line 1", "line 2"}, fileContents.getLines());
    }

    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("example.txt", "line 1", "line 2");
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 2", fileContents.getLine(1));
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("example.txt", "", "line 2");
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("example.txt", "// line 1", "line 2");
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("example.txt", "line 1", "line 2");
        fileContents.reportCppComment(1, 0);
        assertTrue(fileContents.hasIntersectionWithComment(1, 0, 1, 4));
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 0, 4));
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", "line 1", "line 2");
        assertTrue(fileContents.inPackageInfo());
        FileContents otherFileContents = new FileContents("other.java", "line 1", "line 2");
        assertFalse(otherFileContents.inPackageInfo());
    }
}

