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
  public void testGetLineOffset() {
    int lineOffset = sourceFile.getLineOffset(3);
    assertEquals(20, lineOffset);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetWithInvalidLineNumber() {
    sourceFile.getLineOffset(0);
  }
  
  @Test
  public void testGetNumLines() {
    int numLines = sourceFile.getNumLines();
    assertEquals(0, numLines);
  }

  @Test
  public void testGetCode() throws IOException {
    String code = sourceFile.getCode();
    assertEquals("", code);
  }

  @Test
  public void testGetCodeReader() throws IOException {
    Reader codeReader = sourceFile.getCodeReader();
    assertNull(codeReader);
  }

  @Test
  public void testGetOriginalPath() {
    String originalPath = sourceFile.getOriginalPath();
    assertEquals("testFile", originalPath);
  }

  @Test
  public void testSetOriginalPath() {
    sourceFile.setOriginalPath("originalFilePath");
    String originalPath = sourceFile.getOriginalPath();
    assertEquals("originalFilePath", originalPath);
  }

  @Test
  public void testClearCachedSource() {
    sourceFile.clearCachedSource();
    String code = sourceFile.getCode();
    assertEquals("", code);
  }

  @Test
  public void testIsExtern() {
    boolean isExtern = sourceFile.isExtern();
    assertFalse(isExtern);
  }

  @Test
  public void testSetIsExtern() {
    sourceFile.setIsExtern(true);
    boolean isExtern = sourceFile.isExtern();
    assertTrue(isExtern);
  }

  @Test
  public void testGetLine() {
    String line = sourceFile.getLine(1);
    assertNull(line);
  }

  @Test
  public void testGetRegion() {
    Region region = sourceFile.getRegion(1);
    assertNull(region);
  }

}