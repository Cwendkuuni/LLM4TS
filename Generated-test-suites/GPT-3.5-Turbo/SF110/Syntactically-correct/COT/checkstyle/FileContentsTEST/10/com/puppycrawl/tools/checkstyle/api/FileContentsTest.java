
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileContentsTest {
    private FileContents fileContents;
    
    @Before
    public void setUp() {
        String[] lines = {
            "// Comment 1",
            "public class TestClass {",
            "   // Comment 2",
            "   private int number;",
            "   /**",
            "    * Method description",
            "    */",
            "   public void method() {",
            "       // Comment 3",
            "       // Comment 4",
            "       System.out.println(\"Hello, World!\");",
            "   }",
            "}"
        };
        fileContents = new FileContents("TestClass.java", lines);
    }
    
    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("Type", 1, 1);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertTrue(cppComments.containsKey(1));
        TextBlock comment = cppComments.get(1);
        assertEquals("// Comment 1", comment.getText()[0]);
        assertEquals(2, comment.getStartColNo());
        assertEquals(1, comment.getStartLineNo());
        assertEquals(13, comment.getEndColNo());
    }
    
    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("Type", 5, 4, 7, 5);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertTrue(cComments.containsKey(5));
        List<TextBlock> comments = cComments.get(5);
        assertEquals(1, comments.size());
        TextBlock comment = comments.get(0);
        assertEquals("   /**\n    * Method description\n    */", comment.getText()[0]);
        assertEquals(4, comment.getStartColNo());
        assertEquals(5, comment.getStartLineNo());
        assertEquals(4, comment.getEndColNo());
        assertEquals(7, comment.getEndLineNo());
    }
    
    @Test
    public void testGetJavadocBefore() {
        TextBlock javadoc = fileContents.getJavadocBefore(9);
        assertNotNull(javadoc);
        assertEquals("   /**\n    * Method description\n    */", javadoc.getText()[0]);
        assertEquals(4, javadoc.getStartColNo());
        assertEquals(5, javadoc.getStartLineNo());
        assertEquals(4, javadoc.getEndColNo());
        assertEquals(7, javadoc.getEndLineNo());
    }
    
    @Test
    public void testGetText() {
        FileText text = fileContents.getText();
        assertNotNull(text);
        String[] lines = text.toLinesArray();
        assertEquals("// Comment 1", lines[0]);
        assertEquals("public class TestClass {", lines[1]);
        assertEquals("   // Comment 2", lines[2]);
        assertEquals("   private int number;", lines[3]);
        assertEquals("   /**", lines[4]);
        assertEquals("    * Method description", lines[5]);
        assertEquals("    */", lines[6]);
        assertEquals("   public void method() {", lines[7]);
        assertEquals("       // Comment 3", lines[8]);
        assertEquals("       // Comment 4", lines[9]);
        assertEquals("       System.out.println(\"Hello, World!\");", lines[10]);
        assertEquals("   }", lines[11]);
        assertEquals("}", lines[12]);
    }
    
    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        assertEquals("// Comment 1", lines[0]);
        assertEquals("public class TestClass {", lines[1]);
        assertEquals("   // Comment 2", lines[2]);
        assertEquals("   private int number;", lines[3]);
        assertEquals("   /**", lines[4]);
        assertEquals("    * Method description", lines[5]);
        assertEquals("    */", lines[6]);
        assertEquals("   public void method() {", lines[7]);
        assertEquals("       // Comment 3", lines[8]);
        assertEquals("       // Comment 4", lines[9]);
        assertEquals("       System.out.println(\"Hello, World!\");", lines[10]);
        assertEquals("   }", lines[11]);
        assertEquals("}", lines[12]);
    }
    
    @Test
    public void testGetLine() {
        String line = fileContents.getLine(9);
        assertEquals("       // Comment 4", line);
    }
    
    @Test
    public void testGetFileName() {
        assertEquals("TestClass.java", fileContents.getFileName());
    }
    
    @Test
    public void testGetFilename() {
        assertEquals("TestClass.java", fileContents.getFilename());
    }
    
    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
    }
    
    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        assertTrue(fileContents.hasIntersectionWithComment(2, 0, 4, 6));
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 0));
    }
    
    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
        String[] packageInfoLines = {
            "// Package Info",
            "package com.example;"
        };
        FileContents packageFileContents = new FileContents("package-info.java", packageInfoLines);
        assertTrue(packageFileContents.inPackageInfo());
    }
}
