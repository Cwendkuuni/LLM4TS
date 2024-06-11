package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        String[] lines = {
            "public class Example {",
            "    // This is a single-line comment",
            "    /*",
            "     * This is a block comment",
            "     */",
            "    /**",
            "     * This is a javadoc comment",
            "     */",
            "    /* This is another block comment */",
            "}"
        };
        fileContents = new FileContents("Example.java", lines);
    }

    @Test
    public void testConstructorWithFileNameAndLines() {
        String[] lines = fileContents.getLines();
        assertEquals("Example.java", fileContents.getFileName());
        assertEquals("public class Example {", lines[0]);
        assertEquals("}", lines[9]);
    }

    @Test
    public void testConstructorWithFileText() {
        FileText fileText = fileContents.getText();
        assertEquals("Example.java", fileContents.getFileName());
        assertEquals("public class Example {", fileText.get(0));
        assertEquals("}", fileText.get(9));
    }

    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", 2, 4);
        assertTrue(fileContents.getCppComments().containsKey(2));
        assertEquals("// This is a single-line comment", fileContents.getCppComments().get(2).getText()[0]);
    }

    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", 4, 8, 8, 4);
        assertTrue(fileContents.getCComments().containsKey(4));
        assertEquals("    /*", fileContents.getCComments().get(4).get(0).getText()[0]);
        assertEquals("     * This is a block comment", fileContents.getCComments().get(4).get(0).getText()[1]);
        assertEquals("     */", fileContents.getCComments().get(4).get(0).getText()[2]);
    }

    @Test
    public void testGetJavadocBefore() {
        TextBlock javadoc = fileContents.getJavadocBefore(9);
        assertNull(javadoc);

        fileContents.reportBlockComment("type", 6, 4, 8, 4);
        javadoc = fileContents.getJavadocBefore(9);
        assertEquals(6, javadoc.getStartLineNo());
        assertEquals("    /**\n     * This is a javadoc comment\n     */", javadoc.getText()[0]);
    }

    @Test
    public void testGetText() {
        FileText fileText = fileContents.getText();
        assertEquals("Example.java", fileText.getFileName());
        assertEquals("public class Example {", fileText.get(0));
        assertEquals("}", fileText.get(9));
    }

    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        assertEquals("Example.java", fileContents.getFileName());
        assertEquals("public class Example {", lines[0]);
        assertEquals("}", lines[9]);
    }

    @Test
    public void testGetLine() {
        assertEquals("public class Example {", fileContents.getLine(0));
        assertEquals("}", fileContents.getLine(9));
    }

    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 0, 0));
        assertTrue(fileContents.hasIntersectionWithComment(2, 4, 2, 15));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
        FileContents packageInfoFileContents = new FileContents("package-info.java");
        assertTrue(packageInfoFileContents.inPackageInfo());
    }
}