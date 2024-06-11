import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class SourceFileTest {

  @Test
  public void testConstructor() {
    SourceFile sourceFile = new SourceFile("TestFile");
    assertEquals("TestFile", sourceFile.getName());
  }

  @Test
  public void testGetLine() throws IOException {
    SourceFile sourceFile = SourceFile.fromCode("TestFile", "Line 1\nLine 2\nLine 3\n");
    
    assertEquals("Line 1", sourceFile.getLine(1));
    assertEquals("Line 2", sourceFile.getLine(2));
    assertEquals(null, sourceFile.getLine(4));
  }

  @Test
  public void testGetRegion() throws IOException {
    SourceFile sourceFile = SourceFile.fromCode("TestFile", "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\n");

    Region region = sourceFile.getRegion(3);
    assertEquals(3, region.getStartLine());
    assertEquals(7, region.getEndLine());
    assertEquals("Line 2\nLine 3\nLine 4\nLine 5\nLine 6", region.getContent());
    
    Region regionError = sourceFile.getRegion(7);
    assertNull(regionError);
  }

  @Test
  public void testGetCode() throws IOException {
    SourceFile sourceFile = SourceFile.fromCode("TestFile", "Test code");
    assertEquals("Test code", sourceFile.getCode());
  }

  @Test
  public void testGetCodeReader() throws IOException {
    SourceFile sourceFile = SourceFile.fromCode("TestFile", "Test code");
    assertNotNull(sourceFile.getCodeReader());
  }
}