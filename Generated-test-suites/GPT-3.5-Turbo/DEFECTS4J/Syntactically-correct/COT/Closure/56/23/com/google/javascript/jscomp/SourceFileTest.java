import org.junit.*;
import static org.junit.Assert.*;

public class SourceFileTest {

  @Test
  public void testSourceFileConstructor() {
    SourceFile sourceFile = new SourceFile("testfile");
    assertEquals("testfile", sourceFile.getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileConstructorWithNoName() {
    SourceFile sourceFile = new SourceFile("");
  }

  @Test
  public void testGetLineOffset() {
    SourceFile sourceFile = new SourceFile("testfile");
    int lineOffset = sourceFile.getLineOffset(1);
    assertEquals(0, lineOffset);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetWithInvalidLineNumber() {
    SourceFile sourceFile = new SourceFile("testfile");
    int lineOffset = sourceFile.getLineOffset(0);
  }

  @Test
  public void testGetNumLines() {
    SourceFile sourceFile = new SourceFile("testfile");
    int numLines = sourceFile.getNumLines();
    assertEquals(1, numLines);
  }

  @Test
  public void testGetCode() throws IOException {
    SourceFile sourceFile = new SourceFile("testfile");
    String code = sourceFile.getCode();
    assertNull(code);
  }

  @Test
  public void testGetCodeReader() throws IOException {
    SourceFile sourceFile = new SourceFile("testfile");
    try {
      Reader codeReader = sourceFile.getCodeReader();
      assertNotNull(codeReader);
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }

  @Test
  public void testGetOriginalPath() {
    SourceFile sourceFile = new SourceFile("testfile");
    String originalPath = sourceFile.getOriginalPath();
    assertEquals("testfile", originalPath);
  }

  @Test
  public void testSetOriginalPath() {
    SourceFile sourceFile = new SourceFile("testfile");
    sourceFile.setOriginalPath("original");
    String originalPath = sourceFile.getOriginalPath();
    assertEquals("original", originalPath);
  }

  @Test
  public void testClearCachedSource() {
    SourceFile sourceFile = new SourceFile("testfile");
    sourceFile.clearCachedSource();
    String code = sourceFile.getCodeNoCache();
    assertNull(code);
  }

  @Test
  public void testIsExtern() {
    SourceFile sourceFile = new SourceFile("testfile");
    assertFalse(sourceFile.isExtern());
  }

  @Test
  public void testSetIsExtern() {
    SourceFile sourceFile = new SourceFile("testfile");
    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testToString() {
    SourceFile sourceFile = new SourceFile("testfile");
    String toString = sourceFile.toString();
    assertEquals("testfile", toString);
  }
}