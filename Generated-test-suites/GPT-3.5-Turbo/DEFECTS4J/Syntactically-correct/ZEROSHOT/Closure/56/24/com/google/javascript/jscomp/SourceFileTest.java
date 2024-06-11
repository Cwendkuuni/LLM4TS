import org.junit.*;
import static org.junit.Assert.*;

public class SourceFileTest {

  @Test
  public void testGetLineOffset() {
    SourceFile file = new SourceFile("test.js");
    int offset = file.getLineOffset(1);
    assertEquals(0, offset);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testGetLineOffsetWithInvalidLineNumber() {
    SourceFile file = new SourceFile("test.js");
    int offset = file.getLineOffset(0);
  }
  
  @Test
  public void testGetNumLines() {
    SourceFile file = new SourceFile("test.js");
    int numLines = file.getNumLines();
    assertEquals(1, numLines);
  }
  
  @Test
  public void testGetCode() throws IOException {
    SourceFile file = new SourceFile("test.js");
    String code = file.getCode();
    assertEquals("", code);
  }
  
  @Test
  public void testGetLine() {
    SourceFile file = new SourceFile("test.js");
    String line = file.getLine(1);
    assertNull(line);
  }
  
  @Test
  public void testGetRegion() {
    SourceFile file = new SourceFile("test.js");
    Region region = file.getRegion(1);
    assertNull(region);
  }
  
  @Test
  public void testGetName() {
    SourceFile file = new SourceFile("test.js");
    String name = file.getName();
    assertEquals("test.js", name);
  }
  
  @Test
  public void testIsExtern() {
    SourceFile file = new SourceFile("test.js");
    boolean isExtern = file.isExtern();
    assertFalse(isExtern);
  }
  
  @Test
  public void testSetOriginalPath() {
    SourceFile file = new SourceFile("test.js");
    file.setOriginalPath("original/path");
    String path = file.getOriginalPath();
    assertEquals("original/path", path);
  }
  
  @Test
  public void testClearCachedSource() {
    SourceFile file = new SourceFile("test.js");
    file.clearCachedSource();
    String code = file.getCodeNoCache();
    assertNull(code);
  }

}