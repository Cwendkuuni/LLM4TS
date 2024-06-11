
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FileContentsTest {
    private FileContents fileContents;

    @Before
    public void setUp() {
        String[] lines = {"// This is a single line comment",
                          "/* This is a block comment */",
                          "/* This is a multiline",
                          "   block comment */",
                          "/** This is a javadoc comment */",
                          "/* This is a clang comment",
                          "   on line 7 */",
                          "/*",
                          " * This is a clang comment",
                          " * on line 9",
                          " */"};
        fileContents = new FileContents("testfile.java", lines);
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(1, 3);
        fileContents.reportCppComment(7, 3);
        fileContents.reportCppComment(9, 3);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();

        assertEquals(3, cppComments.size());
        assertNotNull(cppComments.get(1));
        assertNotNull(cppComments.get(7));
        assertNotNull(cppComments.get(9));

        assertEquals("// This is a single line comment", cppComments.get(1).getLines()[0]);
        assertEquals("// This is a clang comment", cppComments.get(7).getLines()[0]);
        assertEquals(" * This is a clang comment\n * on line 9", cppComments.get(9).getLines()[0]);
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(3, 3, 5, 22);
        fileContents.reportCComment(6, 3, 10, 3);

        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();

        assertEquals(2, cComments.size());
        assertNotNull(cComments.get(3));
        assertNotNull(cComments.get(6));

        assertEquals(1, cComments.get(3).size());
        assertEquals(2, cComments.get(6).size());

        assertEquals("/* This is a block comment */", cComments.get(3).get(0).getLines()[0]);

        assertEquals("/* This is a clang comment\n * on line 7\n */", cComments.get(6).get(0).getLines()[0]);
        assertEquals("/*\n * This is a clang comment\n * on line 9\n */", cComments.get(6).get(1).getLines()[0]);
    }

    @Test
    public void testGetJavadocBefore() {
        TextBlock javadocBefore = fileContents.getJavadocBefore(6);
        assertNull(javadocBefore);

        javadocBefore = fileContents.getJavadocBefore(8);
        assertNotNull(javadocBefore);
        assertEquals("/** This is a javadoc comment */", javadocBefore.getLines()[0]);
    }

    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(0));
        assertTrue(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertTrue(fileContents.hasIntersectionWithComment(0, 0, 2, 23));
        assertTrue(fileContents.hasIntersectionWithComment(3, 4, 5, 22));
        assertTrue(fileContents.hasIntersectionWithComment(6, 4, 10, 5));
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 2));
    }

    @Test
    public void testInPackageInfo() {
        FileContents packageInfoFileContents = new FileContents("package-info.java",
                                                                "/**",
                                                                " * This is a package-info file",
                                                                " */");

        assertTrue(packageInfoFileContents.inPackageInfo());
        assertFalse(fileContents.inPackageInfo());
    }
}
