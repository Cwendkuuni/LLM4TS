
import com.google.common.collect.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        fileContents = new FileContents("testFile.java",
                "package com.example;",
                "",
                "public class TestClass {",
                "   // This is a single-line comment",
                "   /*",
                "    * This is a",
                "    * multi-line comment",
                "    */",
                "   /**",
                "    * This is a javadoc comment",
                "    */",
                "   public void testMethod() {",
                "       // Do something",
                "   }",
                "}");
    }

    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", 5, 5);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());

        TextBlock comment = cppComments.get(5);
        assertNotNull(comment);
        assertEquals(1, comment.getLines().length);
        assertEquals("// This is a single-line comment", comment.getLines()[0]);
    }

    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", 7, 4, 9, 6);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());

        List<TextBlock> comments = cComments.get(7);
        assertNotNull(comments);
        assertEquals(1, comments.size());

        TextBlock comment = comments.get(0);
        assertEquals(4, comment.getStartLineNo());
        assertEquals(6, comment.getEndLineNo());
        assertEquals(4, comment.getStartColNo());
        assertEquals(6, comment.getEndColNo());
        assertEquals(6, comment.getLines().length);
        assertEquals("   /*", comment.getLines()[0]);
        assertEquals("    * This is a", comment.getLines()[1]);
        assertEquals("    * multi-line comment", comment.getLines()[2]);
        assertEquals("    */", comment.getLines()[3]);
        assertEquals("", comment.getLines()[4]);
    }

    @Test
    public void testGetJavadocBefore() {
        TextBlock javadoc = fileContents.getJavadocBefore(12);
        assertNotNull(javadoc);
        assertEquals(9, javadoc.getStartLineNo());
        assertEquals(9, javadoc.getEndLineNo());
        assertEquals(3, javadoc.getStartColNo());
        assertEquals(6, javadoc.getEndColNo());
        assertEquals(1, javadoc.getLines().length);
        assertEquals("   /**", javadoc.getLines()[0]);
    }

    @Test
    public void testGetText() {
        FileText text = fileContents.getText();
        assertNotNull(text);
        assertEquals(14, text.size());
        assertEquals("public class TestClass {", text.get(2));
    }

    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        assertNotNull(lines);
        assertEquals(14, lines.length);
        assertEquals("   public void testMethod() {", lines[11]);
    }

    @Test
    public void testGetLine() {
        String line = fileContents.getLine(9);
        assertNotNull(line);
        assertEquals("   /**", line);
    }

    @Test
    public void testGetFileName() {
        assertEquals("testFile.java", fileContents.getFileName());
    }

    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(3));
    }

    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(5));
        assertFalse(fileContents.lineIsComment(9));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertTrue(fileContents.hasIntersectionWithComment(4, 4, 6, 6));
        assertFalse(fileContents.hasIntersectionWithComment(2, 2, 3, 3));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }
}
