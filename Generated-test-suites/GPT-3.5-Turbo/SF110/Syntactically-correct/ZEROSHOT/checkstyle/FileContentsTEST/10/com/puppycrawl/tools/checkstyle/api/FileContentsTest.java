import static org.junit.Assert.*;
import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("test.txt");
        fileContents.reportSingleLineComment("type", 1, 1);
        // Assertion here
    }

    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("test.txt");
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        // Assertion here
    }

    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents("test.txt");
        fileContents.reportCppComment(1, 1);
        // Assertion here
    }

    @Test
    public void testGetCppComments() {
        FileContents fileContents = new FileContents("test.txt");
        // Assertion here
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        // Assertion here
    }

    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents("test.txt");
        fileContents.reportCComment(1, 1, 2, 2);
        // Assertion here
    }

    @Test
    public void testGetCComments() {
        FileContents fileContents = new FileContents("test.txt");
        // Assertion here
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        // Assertion here
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("test.txt");
        // Assertion here
        TextBlock javadocBefore = fileContents.getJavadocBefore(1);
        // Assertion here
    }

    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("test.txt");
        FileText text = fileContents.getText();
        // Assertion here
    }

    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("test.txt");
        String[] lines = fileContents.getLines();
        // Assertion here
    }

    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("test.txt");
        String line = fileContents.getLine(1);
        // Assertion here
    }

    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("test.txt");
        String fileName = fileContents.getFileName();
        // Assertion here
    }

    @Test
    public void testGetFilename() {
        FileContents fileContents = new FileContents("test.txt");
        String fileName = fileContents.getFilename();
        // Assertion here
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("test.txt");
        boolean isBlank = fileContents.lineIsBlank(1);
        // Assertion here
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("test.txt");
        boolean isComment = fileContents.lineIsComment(1);
        // Assertion here
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("test.txt");
        boolean hasIntersection = fileContents.hasIntersectionWithComment(1, 1, 2, 2);
        // Assertion here
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java");
        boolean inPackageInfo = fileContents.inPackageInfo();
        // Assertion here
    }
}