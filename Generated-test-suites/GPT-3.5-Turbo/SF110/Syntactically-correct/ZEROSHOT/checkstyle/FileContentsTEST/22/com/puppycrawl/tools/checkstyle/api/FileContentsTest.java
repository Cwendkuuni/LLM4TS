import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.List;
import static org.junit.Assert.*;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        fileContents = new FileContents("testFile.java",
                "public class Test {",
                "    // This is a single-line comment",
                "    /**",
                "     * This is a javadoc comment",
                "     */",
                "    /*",
                "     * This is a block comment",
                "     */",
                "    // This is another single-line comment",
                "}");
    }

    @Test
    public void testReportSingleLineComment() {
        int expectedSize = 2;
        fileContents.reportSingleLineComment("type", 3, 4);
        fileContents.reportSingleLineComment("type", 10, 4);
        Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(expectedSize, cppComments.size());
    }

    @Test
    public void testReportBlockComment() {
        int expectedSize = 1;
        fileContents.reportBlockComment("type", 7, 4, 8, 4);
        Map<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(expectedSize, cComments.size());
    }

    @Test
    public void testGetJavadocBefore() {
        TextBlock expectedComment = new Comment(new String[]{"    * This is a javadoc comment"}, 4, 6, 29);
        TextBlock actualComment = fileContents.getJavadocBefore(7);
        assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(2));
        assertFalse(fileContents.lineIsComment(3));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertTrue(fileContents.hasIntersectionWithComment(6, 1, 8, 3));
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 1));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }

    @Test
    public void testGetText() {
        FileText expectedText = FileText.fromLines(new File("testFile.java"), 
                "public class Test {",
                "    // This is a single-line comment",
                "    /**",
                "     * This is a javadoc comment",
                "     */",
                "    /*",
                "     * This is a block comment",
                "     */",
                "    // This is another single-line comment",
                "}"
        );
        FileText actualText = fileContents.getText();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testGetLines() {
        String[] expectedLines = {
                "public class Test {",
                "    // This is a single-line comment",
                "    /**",
                "     * This is a javadoc comment",
                "     */",
                "    /*",
                "     * This is a block comment",
                "     */",
                "    // This is another single-line comment",
                "}"
        };
        String[] actualLines = fileContents.getLines();
        assertArrayEquals(expectedLines, actualLines);
    }

    @Test
    public void testGetLine() {
        String expectedLine1 = "public class Test {";
        String expectedLine2 = "    // This is a single-line comment";
        assertEquals(expectedLine1, fileContents.getLine(0));
        assertEquals(expectedLine2, fileContents.getLine(1));
    }

    @Test
    public void testGetFileName() {
        String expectedFileName = "testFile.java";
        assertEquals(expectedFileName, fileContents.getFileName());
    }

    @Test
    @Deprecated
    public void testGetFilename() {
        String expectedFileName = "testFile.java";
        assertEquals(expectedFileName, fileContents.getFileName());
    }
}