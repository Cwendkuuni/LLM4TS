import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {
  
  @Test
  public void testConstructor_ValidFileName() {
    SourceFile sourceFile = new SourceFile("example.js");
    assertEquals("example.js", sourceFile.getName());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_NullFileName() {
    SourceFile sourceFile = new SourceFile(null);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_EmptyFileName() {
    SourceFile sourceFile = new SourceFile("");
  }
  
  @Test
  public void testGetLineOffset_ValidLineNumber() {
    SourceFile sourceFile = new SourceFile("example.js");
    int offset = sourceFile.getLineOffset(2);
    assertEquals(0, offset);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffset_OutOfRangeLineNumber() {
    SourceFile sourceFile = new SourceFile("example.js");
    int offset = sourceFile.getLineOffset(-1);
  }
  
  @Test
  public void testGetNumLines() {
    SourceFile sourceFile = new SourceFile("example.js");
    int numLines = sourceFile.getNumLines();
    assertEquals(1, numLines);
  }
  
  @Test
  public void testGetLine_ValidLineNumber() {
    SourceFile sourceFile = new SourceFile("example.js");
    String line = sourceFile.getLine(1);
    assertEquals("", line);
  }
  
  @Test
  public void testGetLine_OutOfRangeLineNumber() {
    SourceFile sourceFile = new SourceFile("example.js");
    String line = sourceFile.getLine(2);
    assertNull(line);
  }
  
  @Test
  public void testGetRegion_ValidLineNumber() {
    SourceFile sourceFile = new SourceFile("example.js");
    Region region = sourceFile.getRegion(1);
    assertNull(region);
  }
  
  @Test
  public void testGetRegion_OutOfRangeLineNumber() {
    SourceFile sourceFile = new SourceFile("example.js");
    Region region = sourceFile.getRegion(2);
    assertNull(region);
  }
}