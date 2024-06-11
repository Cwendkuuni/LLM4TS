
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        fileContents = new FileContents("testFile.java",
                "public class testFile {",
                "    // Single line comment",
                "    /* Block comment */",
                "    // Another single line comment",
                "}");
    }

    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("single line", 2, 5);
        assertNotNull(fileContents.getCppComments().get(2));
    }

    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("block", 4, 5, 4, 19);
        assertNotNull(fileContents.getCComments().get(4));
    }

    @Test
    public void testGetCppComments() {
        assertTrue(fileContents.getCppComments().isEmpty());
        fileContents.reportSingleLineComment("single line", 2, 5);
        assertFalse(fileContents.getCppComments().isEmpty());
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(2, 5);
        assertNotNull(fileContents.getCppComments().get(2));
    }

    @Test
    public void testGetCComments() {
        assertTrue(fileContents.getCComments().isEmpty());
        fileContents.reportBlockComment("block", 4, 5, 4, 19);
        assertFalse(fileContents.getCComments().isEmpty());
    }

    @Test
    public void testExtractCComment() {
        String[] cComment = fileContents.extractCComment(4, 5, 4, 19);
        assertNotNull(cComment);
        assertEquals("    /* Block comment */", cComment[0]);
    }

    @Test
    public void testGetJavadocBefore() {
        assertNull(fileContents.getJavadocBefore(1));
        fileContents.reportBlockComment("block", 4, 5, 4, 19);
        assertNotNull(fileContents.getJavadocBefore(3));
    }

    @Test
    public void testGetText() {
        assertNotNull(fileContents.getText());
        assertEquals("public class testFile {", fileContents.getText().get(0));
    }

    @Test
    public void testGetLines() {
        assertNotNull(fileContents.getLines());
        assertEquals(5, fileContents.getLines().length);
    }

    @Test
    public void testGetLine() {
        assertEquals("public class testFile {", fileContents.getLine(0));
        assertEquals("    // Another single line comment", fileContents.getLine(4));
    }

    @Test
    public void testGetFileName() {
        assertEquals("testFile.java", fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        assertEquals("testFile.java", fileContents.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        assertFalse(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(0));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(1, 0, 3, 0));
        fileContents.reportBlockComment("block", 2, 5, 3, 0);
        assertTrue(fileContents.hasIntersectionWithComment(1, 0, 3, 0));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
        FileContents packageInfoFile = new FileContents("package-info.java",
                "// Package level comment");
        assertTrue(packageInfoFile.inPackageInfo());
    }

    @Test
    public void testHasIntersectionWithCComment() {
        assertFalse(fileContents.hasIntersectionWithCComment(1, 0, 3, 0));
        fileContents.reportBlockComment("block", 2, 5, 3, 0);
        assertTrue(fileContents.hasIntersectionWithCComment(1, 0, 3, 0));
    }

    @Test
    public void testHasIntersectionWithCppComment() {
        assertFalse(fileContents.hasIntersectionWithCppComment(1, 0, 3, 0));
        fileContents.reportCppComment(2, 5);
        assertTrue(fileContents.hasIntersectionWithCppComment(1, 0, 3, 0));
    }
}
