
import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {
  
  @Test
  public void testConstructorWithLines() {
    FileContents fileContents = new FileContents("test.java", "line 1", "line 2", "line 3");
    
    assertEquals("test.java", fileContents.getFileName());
    assertEquals("line 1", fileContents.getLine(0));
    assertEquals("line 2", fileContents.getLine(1));
    assertEquals("line 3", fileContents.getLine(2));
  }
  
  @Test
  public void testConstructorWithFileText() {
    FileText fileText = new FileText(new File("test.java"));
    FileContents fileContents = new FileContents(fileText);
    
    assertEquals("test.java", fileContents.getFileName());
    assertEquals(fileText, fileContents.getText());
  }
  
  @Test
  public void testReportSingleLineComment() {
    FileContents fileContents = new FileContents("test.java", "line 1", "line 2", "line 3");
    fileContents.reportSingleLineComment("type", 2, 3);
    
    assertEquals(new Comment(new String[]{"3"}, 3, 2, 6), fileContents.getCppComments().get(2));
  }
  
  @Test
  public void testReportBlockComment() {
    FileContents fileContents = new FileContents("test.java", "line 1", "line 2", "line 3");
    fileContents.reportBlockComment("type", 2, 3, 3, 6);
    
    assertEquals(new Comment(new String[]{"line 2", "line 3"}, 3, 2, 6), fileContents.getJavadocComments().get(3));
    assertEquals(new Comment(new String[]{"line 2", "line 3"}, 3, 2, 6), fileContents.getCComments().get(2).get(0));
  }
  
  @Test
  public void testGetJavadocBefore() {
    FileContents fileContents = new FileContents("test.java", "line 1", "line 2", "line 3");
    Comment comment = fileContents.getJavadocBefore(3);
    
    assertEquals(new Comment(new String[]{"line 1", "line 2"}, 3, 1, 6), comment);
  }
  
  @Test
  public void testLineIsBlank() {
    FileContents fileContents = new FileContents("test.java", "", "line 2", "line 3");
    
    assertFalse(fileContents.lineIsBlank(0));
    assertTrue(fileContents.lineIsBlank(1));
    assertFalse(fileContents.lineIsBlank(2));
  }
  
  @Test
  public void testLineIsComment() {
    FileContents fileContents = new FileContents("test.java", "// line", "line 2", "line 3");
    
    assertTrue(fileContents.lineIsComment(0));
    assertFalse(fileContents.lineIsComment(1));
    assertFalse(fileContents.lineIsComment(2));
  }
  
  @Test
  public void testHasIntersectionWithComment() {
    FileContents fileContents = new FileContents("test.java", "line 1", "/* comment 1", "comment 2", "*/", "line 5");
    
    assertTrue(fileContents.hasIntersectionWithComment(1, 2, 4, 3));
    assertFalse(fileContents.hasIntersectionWithComment(1, 2, 2, 10));
  }
  
  @Test
  public void testInPackageInfo() {
    FileContents fileContents = new FileContents("test.java", "package-info.java");
    
    assertTrue(fileContents.inPackageInfo());
    
    FileContents otherFileContents = new FileContents("test.java", "other file.java");
    
    assertFalse(otherFileContents.inPackageInfo());
  }
  
}
