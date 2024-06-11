import org.junit.*;
import static org.junit.Assert.*;

public class FileContentsTest {

    @Test
    public void testConstructor_lines() {
        FileContents fc = new FileContents("filename", "line1", "line2");

        assertEquals("filename", fc.getFileName());
        assertTrue(fc.getLine(0).equals("line1"));
        assertTrue(fc.getLine(1).equals("line2"));
    }

    @Test
    public void testConstructor_text() {
        FileText text = new FileText("filename", new String[]{"line1", "line2"});
        FileContents fc = new FileContents(text);

        assertEquals("filename", fc.getFileName());
        assertTrue(fc.getLine(0).equals("line1"));
        assertTrue(fc.getLine(1).equals("line2"));
    }

    @Test
    public void testReportSingleLineComment() {
        FileContents fc = new FileContents("filename", "// single line comment");

        fc.reportSingleLineComment("type", 1, 3);

        ImmutableMap<Integer, TextBlock> cppComments = fc.getCppComments();
        assertEquals(1, cppComments.size());
        assertTrue(cppComments.get(1).getText().equals("single line comment"));
    }

    @Test
    public void testReportBlockComment() {
        FileContents fc = new FileContents("filename", "/* block comment */");

        fc.reportBlockComment("type", 1, 1, 1, 20);

        ImmutableMap<Integer, List<TextBlock>> cComments = fc.getCComments();
        assertEquals(1, cComments.size());
        assertTrue(cComments.get(1).get(0).getText().equals("block comment"));
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fc = new FileContents("filename", "/* block comment */", "/** javadoc comment */", "line");

        fc.reportBlockComment("type", 1, 1, 1, 20);
        fc.reportBlockComment("type", 2, 1, 2, 20);

        TextBlock javadocBefore = fc.getJavadocBefore(3);
        assertNotNull(javadocBefore);
        assertTrue(javadocBefore.getText().equals("javadoc comment"));
    }

    @Test
    public void testLineIsBlank() {
        FileContents fc = new FileContents("filename", "line1", "", "line3");

        assertTrue(fc.lineIsBlank(1));
        assertFalse(fc.lineIsBlank(2));
    }

    @Test
    public void testLineIsComment() {
        FileContents fc = new FileContents("filename", "// single line comment", "/* block comment */");

        assertTrue(fc.lineIsComment(1));
        assertTrue(fc.lineIsComment(2));
        assertFalse(fc.lineIsComment(3));
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fc = new FileContents("filename", "// single line comment", "/* block comment */");

        assertTrue(fc.hasIntersectionWithComment(1, 1, 1, 10));
        assertTrue(fc.hasIntersectionWithComment(2, 3, 2, 16));
        assertFalse(fc.hasIntersectionWithComment(2, 3, 3, 10));
    }

    @Test
    public void testInPackageInfo() {
        FileContents fc = new FileContents("package-info.java");

        assertTrue(fc.inPackageInfo());
    }

}