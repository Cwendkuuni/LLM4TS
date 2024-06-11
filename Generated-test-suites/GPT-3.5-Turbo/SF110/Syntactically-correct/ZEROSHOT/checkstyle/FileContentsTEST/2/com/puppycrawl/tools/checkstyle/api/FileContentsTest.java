import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        fileContents = new FileContents("testFile.java",
                "public class Test {",
                "// This is a single line comment",
                "/* This is a block comment",
                "   that spans multiple",
                "   lines */",
                "}");

    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(2, 3);
        assertTrue(fileContents.getCppComments().containsKey(2));
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(3, 3, 5, 5);
        assertTrue(fileContents.getCComments().containsKey(3));
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportCComment(2, 3, 4, 5);
        assertEquals("/* This is a block comment\n   that spans multiple\n   lines */", fileContents.getJavadocBefore(3));
    }

    @Test
    public void testGetText() {
        assertEquals("public class Test {", fileContents.getText().get(0));
    }

    @Test
    public void testGetLines() {
        assertEquals("}", fileContents.getLines()[4]);
    }

    @Test
    public void testGetLine() {
        assertEquals("// This is a single line comment", fileContents.getLine(1));
    }

    @Test
    public void testGetFileName() {
        assertEquals("testFile.java", fileContents.getFileName());
    }

    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(4));
    }

    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertTrue(fileContents.hasIntersectionWithComment(2, 3, 3, 8));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }

}