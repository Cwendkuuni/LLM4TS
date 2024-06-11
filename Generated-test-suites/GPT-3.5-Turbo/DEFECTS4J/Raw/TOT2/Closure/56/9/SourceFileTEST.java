import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SourceFileTest {

  @Test
  public void testGetName() {
    SourceFile sourceFile = new SourceFile("TestFile");
    assertEquals("TestFile", sourceFile.getName());
  }

  @Test
  public void testIsExtern() {
    SourceFile sourceFile = new SourceFile("TestFile");
    assertFalse(sourceFile.isExtern());
    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testGetLineOffset() {
    SourceFile sourceFile = new SourceFile("TestFile");
    try {
      sourceFile.getCode(); // Load code to initialize line offsets
      assertEquals(0, sourceFile.getLineOffset(1));
    } catch (IOException e) {
      fail("IOException thrown unexpectedly");
    }
  }

  @Test
  public void testGetLine() {
    SourceFile sourceFile = new SourceFile("TestFile");
    try {
      sourceFile.setCode("Line 1\nLine 2\nLine 3\n");
      assertEquals("Line 2", sourceFile.getLine(2));
      assertNull(sourceFile.getLine(4));
    } catch (IOException e) {
      fail("IOException thrown unexpectedly");
    }
  }

  @Test
  public void testGetRegion() {
    SourceFile sourceFile = new SourceFile("TestFile");
    try {
      sourceFile.setCode("Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\n");
      Region region = sourceFile.getRegion(3);
      assertNotNull(region);
      assertEquals("Line 1\nLine 2\nLine 3\nLine 4\nLine 5\n", region.getContent());
      assertNull(sourceFile.getRegion(6));
    } catch (IOException e) {
      fail("IOException thrown unexpectedly");
    }
  }

  @Test
  public void testInvalidLineOffset() {
    SourceFile sourceFile = new SourceFile("TestFile");
    try {
      sourceFile.getLineOffset(0);
      fail("Expected IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }

  @Test
  public void testInvalidLine() {
    SourceFile sourceFile = new SourceFile("TestFile");
    try {
      sourceFile.setCode("Line 1\nLine 2\nLine 3\n");
      assertNull(sourceFile.getLine(0));
      assertNull(sourceFile.getLine(4));
    } catch (IOException e) {
      fail("IOException thrown unexpectedly");
    }
  }

  @Test
  public void testClearCachedSource() {
    SourceFile sourceFile = new SourceFile("TestFile");
    try {
      sourceFile.getCode(); // Load code to cache it
      sourceFile.clearCachedSource();
      assertNull(sourceFile.getCodeNoCache());
    } catch (IOException e) {
      fail("IOException thrown unexpectedly");
    }
  }
}