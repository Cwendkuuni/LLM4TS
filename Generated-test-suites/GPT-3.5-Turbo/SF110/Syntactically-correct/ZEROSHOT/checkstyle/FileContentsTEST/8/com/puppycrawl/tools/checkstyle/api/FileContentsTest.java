import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {
  
  private FileContents fileContents;
  
  @Before
  public void setUp() {
    String[] lines = {"Line 1", "Line 2", "Line 3"};
    fileContents = new FileContents("filename.java", lines);
  }
  
  @Test
  public void testReportSingleLineComment() {
    fileContents.reportSingleLineComment("type", 1, 0);
    // Test assertions
  }
  
  @Test
  public void testReportBlockComment() {
    fileContents.reportBlockComment("type", 1, 0, 2, 5);
    // Test assertions
  }
  
  @Test
  public void testReportCppComment() {
    fileContents.reportCppComment(1, 0);
    // Test assertions
  }
  
  @Test
  public void testGetCppComments() {
    fileContents.reportCppComment(1, 0);
    ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
    // Test assertions
  }
  
  @Test
  public void testReportCComment() {
    fileContents.reportCComment(1, 0, 2, 5);
    // Test assertions
  }
  
  @Test
  public void testGetCComments() {
    fileContents.reportCComment(1, 0, 2, 5);
    ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
    // Test assertions
  }
  
  @Test
  public void testExtractCComment() {
    String[] cComment = fileContents.extractCComment(1, 0, 2, 5);
    // Test assertions
  }
  
  @Test
  public void testGetJavadocBefore() {
    fileContents.reportCComment(2, 0, 5, 2);
    TextBlock javadoc = fileContents.getJavadocBefore(5);
    // Test assertions
  }
  
  @Test
  public void testGetText() {
    FileText fileText = fileContents.getText();
    // Test assertions
  }
  
  @Test
  public void testGetLines() {
    String[] lines = fileContents.getLines();
    // Test assertions
  }
  
  @Test
  public void testGetLine() {
    String line = fileContents.getLine(0);
    // Test assertions
  }
  
  @Test
  public void testGetFileName() {
    String fileName = fileContents.getFileName();
    // Test assertions
  }
  
  @Test
  public void testGetFilename() {
    String fileName = fileContents.getFilename();
    // Test assertions
  }
  
  @Test
  public void testLineIsBlank() {
    boolean isBlank = fileContents.lineIsBlank(0);
    // Test assertions
  }
  
  @Test
  public void testLineIsComment() {
    boolean isComment = fileContents.lineIsComment(0);
    // Test assertions
  }
  
  @Test
  public void testHasIntersectionWithComment() {
    boolean hasIntersection = fileContents.hasIntersectionWithComment(1, 0, 2, 5);
    // Test assertions
  }
  
  @Test
  public void testInPackageInfo() {
    boolean isInPackageInfo = fileContents.inPackageInfo();
    // Test assertions
  }
}