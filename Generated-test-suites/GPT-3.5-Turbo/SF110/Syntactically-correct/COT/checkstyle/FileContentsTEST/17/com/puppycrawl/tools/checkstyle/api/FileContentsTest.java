
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testConstructor1() {
        FileContents fileContents = new FileContents("test.java", "line 1", "line 2");
        assertEquals("test.java", fileContents.getFileName());
        assertNotNull(fileContents.getText());
        assertEquals("line 1", fileContents.getLines()[0]);
        assertEquals("line 2", fileContents.getLines()[1]);
    }

    @Test
    public void testConstructor2() {
        FileText fileText = new FileText(new File("test.java"), Arrays.asList("line 1", "line 2"));
        FileContents fileContents = new FileContents(fileText);
        assertEquals("test.java", fileContents.getFileName());
        assertNotNull(fileContents.getText());
        assertEquals("line 1", fileContents.getLines()[0]);
        assertEquals("line 2", fileContents.getLines()[1]);
    }

    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("test.java", "this is a single line comment");
        fileContents.reportSingleLineComment("type", 1, 1);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertNotNull(cppComments.get(1));
        assertEquals("this is a single line comment", cppComments.get(1).getText()[0]);
    }

    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("test.java", "/** this is a block comment\n * line 1\n */");
        fileContents.reportBlockComment("type", 1, 1, 3, 2);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertNotNull(cComments.get(1));
        assertEquals(1, cComments.get(1).size());
        assertEquals("this is a block comment", cComments.get(1).get(0).getText()[0]);
        assertEquals("line 1", cComments.get(1).get(0).getText()[1]);
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("test.java", "/** Javadoc comment */", "class Test {}");
        assertNotNull(fileContents.getJavadocBefore(2));
        assertEquals("Javadoc comment", fileContents.getJavadocBefore(2).getText()[0]);
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("test.java", "line 1", "");
        assertFalse(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("test.java", "// this is a comment");
        assertTrue(fileContents.lineIsComment(0));
    }
}
