import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import com.puppycrawl.tools.checkstyle.api.*;

public class FileContentsTest {
  
  @Test
  public void testReportCppComment() {
    FileContents fileContents = new FileContents("TestFile.java");
    fileContents.reportCppComment(1, 2);
    ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
    assertEquals(1, cppComments.size());
    assertTrue(cppComments.containsKey(1));
    assertEquals(" line", cppComments.get(1).getText()[0]);
  }
  
  @Test
  public void testReportCComment() {
    FileContents fileContents = new FileContents("TestFile.java");
    fileContents.reportCComment(1, 1, 2, 5);
    ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
    assertEquals(1, cComments.size());
    assertTrue(cComments.containsKey(1));
    List<TextBlock> comments = cComments.get(1);
    assertEquals(1, comments.size());
    TextBlock comment = comments.get(0);
    assertEquals(" line1", comment.getText()[0]);
    assertEquals("line2", comment.getText()[1]);
  }
  
  @Test
  public void testGetJavadocBefore() {
    FileContents fileContents = new FileContents("TestFile.java");
    fileContents.reportCComment(1, 2, 3, 4);
    TextBlock javadocBefore = fileContents.getJavadocBefore(3);
    assertNull(javadocBefore);
    fileContents.reportCComment(1, 2, 2, 10);
    javadocBefore = fileContents.getJavadocBefore(3);
    assertNotNull(javadocBefore);
    assertEquals(" line1", javadocBefore.getText()[0]);
    assertEquals("line2", javadocBefore.getText()[1]);
  }
  
  @Test
  public void testLineIsBlank() {
    FileContents fileContents = new FileContents("TestFile.java", "", " ", "line");
    assertTrue(fileContents.lineIsBlank(0));
    assertFalse(fileContents.lineIsBlank(1));
    assertFalse(fileContents.lineIsBlank(2));
  }
  
  @Test
  public void testLineIsComment() {
    FileContents fileContents = new FileContents("TestFile.java", "// comment", "line");
    assertTrue(fileContents.lineIsComment(0));
    assertFalse(fileContents.lineIsComment(1));
  }
  
  @Test
  public void testHasIntersectionWithComment() {
    FileContents fileContents = new FileContents("TestFile.java");
    assertFalse(fileContents.hasIntersectionWithComment(1, 1, 3, 5));
    fileContents.reportCComment(1, 1, 2, 5);
    assertTrue(fileContents.hasIntersectionWithComment(1, 2, 1, 6));
    assertFalse(fileContents.hasIntersectionWithComment(3, 1, 4, 5));
  }
  
  @Test
  public void testInPackageInfo() {
    FileContents fileContents = new FileContents("TestFile.java");
    assertFalse(fileContents.inPackageInfo());
    fileContents = new FileContents("package-info.java");
    assertTrue(fileContents.inPackageInfo());
  }

}