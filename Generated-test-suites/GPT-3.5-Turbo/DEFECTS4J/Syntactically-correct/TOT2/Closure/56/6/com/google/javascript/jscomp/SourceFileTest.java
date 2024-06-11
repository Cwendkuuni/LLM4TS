import static org.junit.Assert.*;
import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testConstructor() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertEquals("test.js", sourceFile.getName());
    assertFalse(sourceFile.isExtern());
    assertEquals(1, sourceFile.getNumLines());
  }

  @Test
  public void testGetLineOffset() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertEquals(0, sourceFile.getLineOffset(1));
  }
  
  @Test
  public void testGetLineOffsetInvalid() {
    SourceFile sourceFile = new SourceFile("test.js");
    try {
      sourceFile.getLineOffset(0);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testGetLine() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertEquals("", sourceFile.getLine(1));
  }

  @Test
  public void testGetLineNonExisting() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertNull(sourceFile.getLine(2));
  }

  @Test
  public void testGetRegion() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertEquals("",
        sourceFile.getRegion(1).getContent());
  }

  @Test
  public void testGetRegionNonExisting() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertNull(sourceFile.getRegion(2));
  }

  @Test
  public void testFromCode() {
    SourceFile sourceFile = SourceFile.fromCode("test.js", "console.log('Hello');");
    assertEquals("test.js", sourceFile.getName());
    assertFalse(sourceFile.isExtern());
    assertEquals(1, sourceFile.getNumLines());
  }

  @Test
  public void testFromCodeWithOriginalPath() {
    SourceFile sourceFile = SourceFile.fromCode("test.js", "original/test.js", "console.log('Hello');");
    assertEquals("test.js", sourceFile.getName());
    assertEquals("original/test.js", sourceFile.getOriginalPath());
  }
}