import static org.junit.Assert.*;
import org.junit.Test;

public class FileContentsTest {
    
    @Test
    public void testConstructor_withFileNameAndLines_shouldSetProperValues() {
        String filename = "test.txt";
        String[] lines = {"line1", "line2", "line3"};
        
        FileContents fileContents = new FileContents(filename, lines);
        
        assertEquals(filename, fileContents.getFileName());
        assertArrayEquals(lines, fileContents.getLines());
    }
    
    @Test
    public void testConstructor_withFileText_shouldSetProperValues() {
        FileText fileText = new FileText("test.txt", "line1", "line2", "line3");
        
        FileContents fileContents = new FileContents(fileText);
        
        assertEquals("test.txt", fileContents.getFileName());
        assertArrayEquals(new String[] {"line1", "line2", "line3"}, fileContents.getLines());
    }
    
    @Test
    public void testReportSingleLineComment_shouldAddCppComment() {
        FileContents fileContents = new FileContents("test.txt");
        
        fileContents.reportSingleLineComment("type", 3, 5);
        
        assertEquals(1, fileContents.getCppComments().size());
        assertNotNull(fileContents.getCppComments().get(3));
    }
    
    @Test
    public void testReportBlockComment_shouldAddCComment() {
        FileContents fileContents = new FileContents("test.txt");
        
        fileContents.reportBlockComment("type", 2, 3, 4, 6);
        
        assertEquals(1, fileContents.getCComments().size());
        assertNotNull(fileContents.getCComments().get(2));
        assertEquals(1, fileContents.getCComments().get(2).size());
    }
    
    @Test
    public void testReportCComment_withExistingStartLine_shouldAddNewCComment() {
        FileContents fileContents = new FileContents("test.txt");
        
        fileContents.reportCComment(2, 3, 4, 6);
        fileContents.reportCComment(2, 7, 5, 9);
        
        assertEquals(1, fileContents.getCComments().size());
        assertNotNull(fileContents.getCComments().get(2));
        assertEquals(2, fileContents.getCComments().get(2).size());
    }
    
    @Test
    public void testGetJavadocBefore_shouldReturnJavadocComment() {
        FileContents fileContents = new FileContents("test.txt");
        
        fileContents.reportCComment(2, 3, 4, 6);
        fileContents.reportCComment(5, 2, 7, 5);
        fileContents.reportCComment(8, 1, 8, 4);
        fileContents.reportCComment(9, 1, 9, 4);
        fileContents.reportSingleLineComment("type", 11, 6);
        
        TextBlock javadocComment = fileContents.getJavadocBefore(11);
        
        assertNotNull(javadocComment);
    }
    
    @Test
    public void testGetText_shouldReturnFileText() {
        FileText fileText = new FileText("test.txt", "line1", "line2", "line3");
        FileContents fileContents = new FileContents(fileText);
        
        FileText result = fileContents.getText();
        
        assertEquals(fileText, result);
    }
    
    @Test
    public void testGetLine_shouldReturnLineAtIndex() {
        FileContents fileContents = new FileContents("test.txt", "line1", "line2", "line3");
        
        String line = fileContents.getLine(1);
        
        assertEquals("line2", line);
    }
    
    @Test
    public void testLineIsBlank_withBlankLine_shouldReturnTrue() {
        FileContents fileContents = new FileContents("test.txt", "line1", "", "line3");
        
        boolean isBlank = fileContents.lineIsBlank(1);
        
        assertTrue(isBlank);
    }
    
    @Test
    public void testLineIsBlank_withNonBlankLine_shouldReturnFalse() {
        FileContents fileContents = new FileContents("test.txt", "line1", "line2", "line3");
        
        boolean isBlank = fileContents.lineIsBlank(1);
        
        assertFalse(isBlank);
    }
    
    @Test
    public void testLineIsComment_withCommentLine_shouldReturnTrue() {
        FileContents fileContents = new FileContents("test.txt", "// This is a comment");
        
        boolean isComment = fileContents.lineIsComment(1);
        
        assertTrue(isComment);
    }
    
    @Test
    public void testLineIsComment_withNonCommentLine_shouldReturnFalse() {
        FileContents fileContents = new FileContents("test.txt", "line1");
        
        boolean isComment = fileContents.lineIsComment(1);
        
        assertFalse(isComment);
    }
    
    @Test
    public void testHasIntersectionWithComment_withIntersection_shouldReturnTrue() {
        FileContents fileContents = new FileContents("test.txt");
        
        boolean hasIntersection = fileContents.hasIntersectionWithComment(2, 3, 4, 5);
        
        assertTrue(hasIntersection);
    }
    
    @Test
    public void testHasIntersectionWithComment_withoutIntersection_shouldReturnFalse() {
        FileContents fileContents = new FileContents("test.txt");
        
        boolean hasIntersection = fileContents.hasIntersectionWithComment(1, 1, 1, 2);
        
        assertFalse(hasIntersection);
    }
    
    @Test
    public void testInPackageInfo_withPackageInfoFile_shouldReturnTrue() {
        FileContents fileContents = new FileContents("package-info.java");
        
        boolean isInPackageInfo = fileContents.inPackageInfo();
        
        assertTrue(isInPackageInfo);
    }
    
    @Test
    public void testInPackageInfo_withoutPackageInfoFile_shouldReturnFalse() {
        FileContents fileContents = new FileContents("test.txt");
        
        boolean isInPackageInfo = fileContents.inPackageInfo();
        
        assertFalse(isInPackageInfo);
    }
    
    @Test
    public void testMatchSingleLineCommentPattern_withSingleLineComment_shouldReturnTrue() {
        String comment = "// This is a comment";
        
        boolean isComment = FileContents.MATCH_SINGLELINE_COMMENT.matcher(comment).matches();
        
        assertTrue(isComment);
    }
    
    @Test
    public void testMatchSingleLineCommentPattern_withoutSingleLineComment_shouldReturnFalse() {
        String nonComment = "This is not a comment";
        
        boolean isComment = FileContents.MATCH_SINGLELINE_COMMENT.matcher(nonComment).matches();
        
        assertFalse(isComment);
    }
}