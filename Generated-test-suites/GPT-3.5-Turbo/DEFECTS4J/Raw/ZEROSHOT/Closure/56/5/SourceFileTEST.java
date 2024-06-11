import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class SourceFileTest {
  
  private SourceFile sourceFile;
  
  @Before
  public void setUp() {
    sourceFile = new SourceFile("testFile");
  }
  
  @Test
  public void testGetName() {
    assertEquals("testFile", sourceFile.getName());
  }
  
  @Test
  public void testIsExtern() {
    assertFalse(sourceFile.isExtern());
  }
  
  @Test
  public void testGetLineOffset() {
    assertEquals(0, sourceFile.getLineOffset(1));
  }
  
  @Test
  public void testGetCodeReader() throws IOException {
    assertNotNull(sourceFile.getCodeReader());
  }
  
  @Test
  public void testSetOriginalPath() {
    sourceFile.setOriginalPath("originalPath");
    assertEquals("originalPath", sourceFile.getOriginalPath());
  }
  
  @Test
  public void testClearCachedSource() {
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCodeNoCache());
  }
  
  @Test
  public void testGetLine() {
    assertNull(sourceFile.getLine(1));
  }
  
  @Test
  public void testGetRegion() {
    assertNull(sourceFile.getRegion(1));
  }
  
  @Test
  public void testToString() {
    assertEquals("testFile", sourceFile.toString());
  }
}