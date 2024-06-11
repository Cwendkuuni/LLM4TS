import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {

    @Test
    public void testConstructorWithNameAndLines() {
        FileContents fileContents = new FileContents("example.txt", "line1", "line2", "line3");
        assertEquals("example.txt", fileContents.getFileName());
        assertArrayEquals(new String[]{"line1", "line2", "line3"}, fileContents.getLines());
    }

    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText(new File("example.txt"), Arrays.asList("line1", "line2", "line3"));
        FileContents fileContents = new FileContents(fileText);
        assertEquals("example.txt", fileContents.getFileName());
        assertArrayEquals(new String[]{"line1", "line2", "line3"}, fileContents.getLines());
    }

    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("example.txt", "// Single line comment");
        fileContents.reportSingleLineComment("type", 1, 3);
        assertEquals("Single line comment", fileContents.getCppComments().get(1).getText()[0]);
    }

    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("example.txt", "/* Block comment */");
        fileContents.reportBlockComment("type", 1, 3, 1, 20);
        assertEquals("Block comment", fileContents.getCComments().get(1).get(0).getText()[0]);
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("example.txt", "/** Javadoc comment */", "public class Example {}");
        fileContents.reportCComment(1, 3, 1, 22);
        TextBlock javadocComment = fileContents.getJavadocBefore(2);
        assertEquals("Javadoc comment", javadocComment.getText()[0]);
    }

    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("example.txt", "line1", "line2", "line3");
        FileText text = fileContents.getText();
        assertEquals("line1", text.get(0));
        assertEquals("line2", text.get(1));
        assertEquals("line3", text.get(2));
    }

    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("example.txt", "line1", "line2", "line3");
        assertEquals("line2", fileContents.getLine(1));
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("example.txt", "line1", "", "line3");
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("example.txt", "// Comment", "line2");
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("example.txt", "/* Comment */");
        assertTrue(fileContents.hasIntersectionWithComment(1, 3, 1, 10));
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 0, 5));
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", "");
        assertTrue(fileContents.inPackageInfo());
        FileContents otherFileContents = new FileContents("Example.java", "");
        assertFalse(otherFileContents.inPackageInfo());
    }
}

