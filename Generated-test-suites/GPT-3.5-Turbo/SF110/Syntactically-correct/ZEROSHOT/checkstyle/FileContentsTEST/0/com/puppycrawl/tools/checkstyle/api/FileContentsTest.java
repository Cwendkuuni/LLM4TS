import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {
  
  private FileContents fileContents;
  
  @Before
  public void setUp() {
    fileContents = new FileContents("testFile.java");
  }
  
  @Test
  public void testReportSingleLineComment() {
    fileContents.reportSingleLineComment("type", 1, 5);
    assertNotNull(fileContents.getCppComments().get(1));
  }
  
  @Test
  public void testReportBlockComment() {
    fileContents.reportBlockComment("type", 2, 3, 5, 7);
    assertNotNull(fileContents.getCComments().get(2));
  }
  
  @Test
  public void testReportCppComment() {
    fileContents.reportCppComment(3, 4);
    assertNotNull(fileContents.getCppComments().get(3));
  }
  
  @Test
  public void testGetCppComments() {
    assertNotNull(fileContents.getCppComments());
  }
  
  @Test
  public void testReportCComment() {
    fileContents.reportCComment(4, 5, 6, 8);
    assertNotNull(fileContents.getCComments().get(4));
  }
  
  @Test
  public void testGetCComments() {
    assertNotNull(fileContents.getCComments());
  }
  
  @Test
  public void testGetJavadocBefore() {
    assertNull(fileContents.getJavadocBefore(1));
  }
  
  @Test
  public void testGetText() {
    assertNotNull(fileContents.getText());
  }
  
  @Test
  public void testGetLines() {
    assertNotNull(fileContents.getLines());
  }
  
  @Test
  public void testGetLine() {
    assertNotNull(fileContents.getLine(1));
  }
  
  @Test
  public void testGetFileName() {
    assertEquals("testFile.java", fileContents.getFileName());
  }
  
  @Test
  public void testGetFilename() {
    assertEquals("testFile.java", fileContents.getFilename());
  }
  
  @Test
  public void testLineIsBlank() {
    assertFalse(fileContents.lineIsBlank(1));
  }
  
  @Test
  public void testLineIsComment() {
    assertFalse(fileContents.lineIsComment(1));
  }
  
  @Test
  public void testHasIntersectionWithComment() {
    assertFalse(fileContents.hasIntersectionWithComment(1, 2, 3, 4));
  }
  
  @Test
  public void testInPackageInfo() {
    assertFalse(fileContents.inPackageInfo());
  }
}