
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FileContentsTest {
  
  private FileContents fileContents;
  
  @Before
  public void setUp() {
    fileContents = new FileContents("test.txt", "Line 1", "Line 2", "Line 3");
  }
  
  // Test for the constructor that takes a filename and vararg lines
  @Test
  public void testConstructorFileNameVararg() {
    assertEquals("test.txt", fileContents.getFileName());
    assertEquals("Line 1", fileContents.getLine(0));
    assertEquals("Line 2", fileContents.getLine(1));
    assertEquals("Line 3", fileContents.getLine(2));
  }
  
  // Test for the constructor that takes a FileText
  @Test
  public void testConstructorFileText() {
    FileText fileText = new FileText(new File("test.txt"), Arrays.asList("Line 1", "Line 2", "Line 3"));
    fileContents = new FileContents(fileText);
    assertEquals("test.txt", fileContents.getFileName());
    assertEquals("Line 1", fileContents.getLine(0));
    assertEquals("Line 2", fileContents.getLine(1));
    assertEquals("Line 3", fileContents.getLine(2));
  }
  
  // Test for reportSingleLineComment method
  @Test
  public void testReportSingleLineComment() {
    fileContents.reportSingleLineComment("type", 1, 2);
    assertEquals(1, fileContents.getCppComments().size());
    assertNotNull(fileContents.getCppComments().get(1));
  }
  
  // Test for reportBlockComment method
  @Test
  public void testReportBlockComment() {
    fileContents.reportBlockComment("type", 1, 2, 3, 4);
    assertEquals(1, fileContents.getCComments().size());
    assertNotNull(fileContents.getCComments().get(1));
    assertEquals(1, fileContents.getJavadocBefore(2).size());
    assertNotNull(fileContents.getJavadocBefore(2));
  }
  
  // Test for extractCComment method - startLineNo == endLineNo
  @Test
  public void testExtractCCommentSingleLine() {
    String[] cComment = fileContents.extractCComment(1, 2, 1, 4);
    assertEquals(1, cComment.length);
    assertEquals("ne 1", cComment[0]);
  }
  
  // Test for extractCComment method - startLineNo != endLineNo
  @Test
  public void testExtractCCommentMultiLine() {
    String[] cComment = fileContents.extractCComment(1, 2, 2, 4);
    assertEquals(2, cComment.length);
    assertEquals("ne 1", cComment[0]);
    assertEquals("ne 2", cComment[1]);
  }
  
  // Test for getText method
  @Test
  public void testGetText() {
    FileText fileText = fileContents.getText();
    assertEquals("Line 1", fileText.get(0));
    assertEquals("Line 2", fileText.get(1));
    assertEquals("Line 3", fileText.get(2));
  }
  
  // Test for getLines method
  @Test
  public void testGetLines() {
    String[] lines = fileContents.getLines();
    assertEquals(3, lines.length);
    assertEquals("Line 1", lines[0]);
    assertEquals("Line 2", lines[1]);
    assertEquals("Line 3", lines[2]);
  }
  
  // Test for getLine method
  @Test
  public void testGetLine() {
    assertEquals("Line 1", fileContents.getLine(0));
    assertEquals("Line 2", fileContents.getLine(1));
    assertEquals("Line 3", fileContents.getLine(2));
  }
  
  // Test for lineIsBlank method - line is blank
  @Test
  public void testLineIsBlankBlank() {
    assertTrue(fileContents.lineIsBlank(0));
  }
  
  // Test for lineIsBlank method - line is not blank
  @Test
  public void testLineIsNotBlank() {
    assertFalse(fileContents.lineIsBlank(1));
  }
  
  // Test for lineIsComment method - line is a single line comment
  @Test
  public void testLineIsCommentSingleLine() {
    assertTrue(fileContents.lineIsComment(0));
  }
  
  // Test for lineIsComment method - line is not a single line comment
  @Test
  public void testLineIsNotComment() {
    assertFalse(fileContents.lineIsComment(1));
  }
  
  // Test for hasIntersectionWithComment method - has intersection with C++ comment
  @Test
  public void testHasIntersectionWithCommentCppComment() {
    fileContents.reportCppComment(1, 2);
    assertTrue(fileContents.hasIntersectionWithComment(1, 3, 1, 4));
  }
  
  // Test for hasIntersectionWithComment method - has intersection with C comment
  @Test
  public void testHasIntersectionWithCommentCComment() {
    fileContents.reportCComment(1, 2, 1, 4);
    assertTrue(fileContents.hasIntersectionWithComment(1, 3, 1, 5));
  }
  
  // Test for hasIntersectionWithComment method - does not have intersection with any comment
  @Test
  public void testHasIntersectionWithCommentNoIntersection() {
    assertFalse(fileContents.hasIntersectionWithComment(1, 3, 1, 4));
  }
  
  // Test for inPackageInfo method - file name ends with package-info.java
  @Test
  public void testInPackageInfoTrue() {
    FileContents fileContents = new FileContents("test/package-info.java");
    assertTrue(fileContents.inPackageInfo());
  }
  
  // Test for inPackageInfo method - file name does not end with package-info.java
  @Test
  public void testInPackageInfoFalse() {
    FileContents fileContents = new FileContents("test/Foo.java");
    assertFalse(fileContents.inPackageInfo());
  }
}
