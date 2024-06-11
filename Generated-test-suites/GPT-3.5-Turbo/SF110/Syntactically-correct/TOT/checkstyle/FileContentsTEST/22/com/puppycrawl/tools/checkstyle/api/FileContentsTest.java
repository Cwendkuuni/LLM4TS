import org.junit.Before;
    import org.junit.Test;

    import com.puppycrawl.tools.checkstyle.api.FileContents;

    public class FileContentsTest {
    
        private FileContents fileContents;
    
        @Before
        public void setUp() {
            fileContents = new FileContents("test.java", 
                    "package com.example;",
                    "",
                    "// single line comment",
                    "/*",
                    " * block comment",
                    " */",
                    "public class Test {",
                    "",
                    "}"
            );
        }
    
        @Test
        public void testReportSingleLineComment() {
            fileContents.reportSingleLineComment("type", 3, 1);
    
            // Check if cpp comment is added at the correct line
            assertEquals("single line comment\n", fileContents.getCppComments().get(3).getText());
        }
    
        @Test
        public void testReportBlockComment() {
            fileContents.reportBlockComment("type", 5, 1, 6, 2);
    
            // Check if c comment is added at the correct lines
            assertEquals("block comment\n", fileContents.getCComments().get(5).get(0).getText());
        }
    
        @Test
        public void testReportCppComment() {
            fileContents.reportCppComment(3, 1);
    
            // Check if cpp comment is added at the correct line
            assertEquals(" single line comment\n", fileContents.getCppComments().get(3).getText());
        }
    
        @Test
        public void testGetCppComments() {
            fileContents.reportCppComment(3, 1);
    
            // Check if cpp comments map is returned correctly
            assertEquals(" single line comment\n", fileContents.getCppComments().get(3).getText());
        }
    
        @Test
        public void testReportCComment() {
            fileContents.reportCComment(5, 5, 6, 10);
    
            // Check if c comment is added at the correct lines
            assertEquals(" block comment\n", fileContents.getCComments().get(5).get(0).getText());
        }
    
        @Test
        public void testGetCComments() {
            fileContents.reportCComment(5, 5, 6, 10);
    
            // Check if c comments map is returned correctly
            assertEquals(" block comment\n", fileContents.getCComments().get(5).get(0).getText());
        }
    
        @Test
        public void testGetJavadocBefore() {
            fileContents.reportCComment(5, 5, 6, 10);
            fileContents.reportCppComment(8, 5);
    
            // Check if correct javadoc comment is returned
            assertEquals(" block comment\n", fileContents.getJavadocBefore(8).getText());
        }
    
        @Test
        public void testGetText() {
            // Check if the correct file text object is returned
            assertNotNull(fileContents.getText());
        }
    
        @Test
        public void testGetLines() {
            // Check if the correct lines array is returned
            assertArrayEquals(new String[] {"package com.example;", "", "// single line comment", "/*", " * block comment", " */", "public class Test {", "", "}"}, fileContents.getLines());
        }
    
        @Test
        public void testGetLine() {
            // Check if the correct line is returned
            assertEquals("public class Test {", fileContents.getLine(7));
        }
    
        @Test
        public void testGetFileName() {
            // Check if the correct file name is returned
            assertEquals("test.java", fileContents.getFileName());
        }
    
        @Test
        public void testGetFilename() {
            // Check if the correct file name is returned
            assertEquals("test.java", fileContents.getFilename());
        }
    
        @Test
        public void testLineIsBlank() {
            // Check if the correct result is returned for a blank line
            assertTrue(fileContents.lineIsBlank(1));
        }
    
        @Test
        public void testLineIsComment() {
            // Check if the correct result is returned for a comment line
            assertTrue(fileContents.lineIsComment(2));
        }
    
        @Test
        public void testHasIntersectionWithComment() {
            // Check if the correct result is returned for intersection with a comment block
            assertFalse(fileContents.hasIntersectionWithComment(5, 1, 6, 2));
        }
    
        @Test
        public void testInPackageInfo() {
            // Check if the correct result is returned for a package-info file
            assertFalse(fileContents.inPackageInfo());
        }
    
    }