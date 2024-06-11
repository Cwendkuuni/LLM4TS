import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class SourceFileTest {
  
  @Test
  public void testGetCode() {
    SourceFile sourceFile = new SourceFile("TestFile");
    try {
      String code = sourceFile.getCode();
      assertNotNull(code);
    } catch(IOException e) {
      fail("IOException should not be thrown.");
    }
  }
  
  @Test
  public void testGetLineOffset() {
    SourceFile sourceFile = new SourceFile("TestFile");
    int offset = sourceFile.getLineOffset(1);
    assertEquals(0, offset);
  }
  
  @Test
  public void testGetNumLines() {
    SourceFile sourceFile = new SourceFile("TestFile");
    int numLines = sourceFile.getNumLines();
    assertEquals(1, numLines);
  }
  
  @Test
  public void testGetLine() {
    SourceFile sourceFile = new SourceFile("TestFile");
    String line = sourceFile.getLine(1);
    assertNull(line);
  }
  
  @Test
  public void testGetRegion() {
    SourceFile sourceFile = new SourceFile("TestFile");
    Region region = sourceFile.getRegion(1);
    assertNull(region);
  }
  
  @Test
  public void testGetOriginalPath() {
    SourceFile sourceFile = new SourceFile("TestFile");
    String originalPath = sourceFile.getOriginalPath();
    assertEquals("TestFile", originalPath);
  }
  
  @Test
  public void testIsExtern() {
    SourceFile sourceFile = new SourceFile("TestFile");
    assertFalse(sourceFile.isExtern());
  }
  
  @Test
  public void testSetName() {
    SourceFile sourceFile = new SourceFile("TestFile");
    String name = sourceFile.getName();
    assertEquals("TestFile", name);
  }
  
}