
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        fileContents = new FileContents("TestFile.java",
                "package com.example;",
                "",
                "// This is a single line comment",
                "public class TestFile {",
                "    /* This is a block comment",
                "       spanning multiple lines */",
                "    private int number;",
                "    /**",
                "     * This is a javadoc comment.",
                "     */",
                "    public void printNumber() {",
                "        System.out.println(number);",
                "    }",
                "}");
    }

    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", 3, 1);
        assertTrue(fileContents.getLine(2).startsWith("//"));
        assertFalse(fileContents.getLine(4).startsWith("//"));
    }

    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", 7, 5, 8, 15);
        assertTrue(fileContents.getLine(6).startsWith("    /*"));
        assertTrue(fileContents.getLine(7).contains("multiple lines"));
        assertTrue(fileContents.getLine(8).endsWith("      */"));
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(3, 5);
        assertTrue(fileContents.getCppComments().containsKey(3));
        assertEquals("// This is a single line comment", fileContents.getCppComments().get(3).getText()[0]);
    }

    @Test
    public void testGetCppComments() {
        fileContents.reportCppComment(3, 5);
        assertEquals(1, fileContents.getCppComments().size());
        assertTrue(fileContents.getCppComments().containsKey(3));
        assertEquals("// This is a single line comment", fileContents.getCppComments().get(3).getText()[0]);
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(7, 5, 8, 15);
        assertTrue(fileContents.getCComments().containsKey(7));
        assertEquals(1, fileContents.getCComments().get(7).size());
        assertEquals("    /* This is a block comment\n       spanning multiple lines */", fileContents.getCComments().get(7).get(0).getText()[0]);
    }

    @Test
    public void testGetCComments() {
        fileContents.reportCComment(7, 5, 8, 15);
        assertEquals(1, fileContents.getCComments().size());
        assertTrue(fileContents.getCComments().containsKey(7));
        assertEquals(1, fileContents.getCComments().get(7).size());
        assertEquals("    /* This is a block comment\n       spanning multiple lines */", fileContents.getCComments().get(7).get(0).getText()[0]);
    }

    @Test
    public void testGetJavadocBefore() {
        assertEquals("    /**\n     * This is a javadoc comment.\n     */",
                fileContents.getJavadocBefore(10).getText()[0]);
    }

    @Test
    public void testGetText() {
        assertEquals("package com.example;", fileContents.getText().get(0));
        assertEquals("    private int number;", fileContents.getText().get(7));
    }

    @Test
    public void testGetLines() {
        assertArrayEquals(new String[]{"package com.example;", "", "// This is a single line comment", "public class TestFile {",
                "    /* This is a block comment", "       spanning multiple lines */", "    private int number;",
                "    /**", "     * This is a javadoc comment.", "     */", "    public void printNumber() {",
                "        System.out.println(number);", "    }", "}"}, fileContents.getLines());
    }

    @Test
    public void testGetLine() {
        assertEquals("package com.example;", fileContents.getLine(0));
        assertEquals("    private int number;", fileContents.getLine(7));
    }

    @Test
    public void testGetFileName() {
        assertEquals("TestFile.java", fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        assertEquals("TestFile.java", fileContents.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(3));
    }

    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(3));
        assertFalse(fileContents.lineIsComment(4));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertTrue(fileContents.hasIntersectionWithComment(7, 20, 8, 10));
        assertFalse(fileContents.hasIntersectionWithComment(3, 5, 7, 15));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
        fileContents = new FileContents("package-info.java", "package com.example;");
        assertTrue(fileContents.inPackageInfo());
    }
}

