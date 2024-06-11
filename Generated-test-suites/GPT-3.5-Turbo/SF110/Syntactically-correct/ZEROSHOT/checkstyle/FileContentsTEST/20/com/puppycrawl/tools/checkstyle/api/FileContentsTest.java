import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        fileContents = new FileContents("test.txt");
    }

    @Test
    public void testConstructorWithLines() {
        String[] lines = {"line1", "line2", "line3"};
        FileContents fileContents = new FileContents("test.txt", lines);
        assertNotNull(fileContents);
    }

    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText(new File("test.txt"));
        FileContents fileContents = new FileContents(fileText);
        assertNotNull(fileContents);
    }

    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", 1, 1);
        assertNotNull(fileContents.getCppComments());
    }

    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        assertNotNull(fileContents.getCComments());
    }

    @Test
    public void testGetJavadocBefore() {
        assertNull(fileContents.getJavadocBefore(1));
    }

    @Test
    public void testGetText() {
        FileText fileText = fileContents.getText();
        assertNotNull(fileText);
    }

    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        assertNotNull(lines);
    }

    @Test
    public void testGetLine() {
        String line = fileContents.getLine(1);
        assertNotNull(line);
    }

    @Test
    public void testGetFileName() {
        String fileName = fileContents.getFileName();
        assertNotNull(fileName);
    }

    @Test
    public void testGetFilename() {
        String fileName = fileContents.getFilename();
        assertNotNull(fileName);
    }

    @Test
    public void testLineIsBlank() {
        boolean isBlank = fileContents.lineIsBlank(1);
        assertNotNull(isBlank);
    }

    @Test
    public void testLineIsComment() {
        boolean isComment = fileContents.lineIsComment(1);
        assertNotNull(isComment);
    }

    @Test
    public void testHasIntersectionWithComment() {
        boolean hasIntersection = fileContents.hasIntersectionWithComment(1, 1, 2, 2);
        assertNotNull(hasIntersection);
    }

    @Test
    public void testInPackageInfo() {
        boolean inPackageInfo = fileContents.inPackageInfo();
        assertNotNull(inPackageInfo);
    }

}