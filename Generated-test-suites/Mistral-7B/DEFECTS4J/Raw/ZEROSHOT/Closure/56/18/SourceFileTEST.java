import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceFile.*;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testGetLine() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sourceFile = new Preloaded("test.js", code);

    String expectedLine1 = "line1";
    String expectedLine2 = "line2";
    String expectedLine3 = "line3";

    assertEquals(expectedLine1, sourceFile.getLine(1));
    assertEquals(expectedLine2, sourceFile.getLine(2));
    assertEquals(expectedLine3, sourceFile.getLine(3));
    assertNull(sourceFile.getLine(4));
  }

  @Test
  public void testGetRegion() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sourceFile = new Preloaded("test.js", code);

    Region regionForLine2 = sourceFile.getRegion(2);

    String expectedRegionForLine2 = "line2\nline3";

    assertEquals(2, regionForLine2.getStartLineNumber());
    assertEquals(3, regionForLine2.getEndLineNumber());
    assertEquals(expectedRegionForLine2, regionForLine2.getCode());
  }

  @Test
  public void testGetLineOffset() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sourceFile = new Preloaded("test.js", code);

    int lineNumber = 2;
    int expectedLineOffset = 8; // "line2\n".length() + 1

    int lineOffset = sourceFile.getLineOffset(lineNumber);

    assertEquals(expectedLineOffset, lineOffset);
  }

  @Test
  public void testNumLines() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sourceFile = new Preloaded("test.js", code);

    int expectedNumLines = 3;

    int numLines = sourceFile.getNumLines();

    assertEquals(expectedNumLines, numLines);
  }

  @Test
  public void testGetOriginalPath() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sourceFile = new Preloaded("test.js", code);

    String expectedOriginalPath = "test.js";

    String originalPath = sourceFile.getOriginalPath();

    assertEquals(expectedOriginalPath, originalPath);
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sourceFile = new Preloaded("test.js", code);

    String cachedCode = sourceFile.getCode();

    sourceFile.clearCachedSource();

    String newCachedCode = sourceFile.getCode();

    assertNotEquals(cachedCode, newCachedCode);
  }

  @Test
  public void testHasSourceInMemory() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sourceFile = new Preloaded("test.js", code);

    assertTrue(sourceFile.hasSourceInMemory());
  }

  @Test
  public void testGivenFileNameWithNoExtensionReturnsFileWithDefaultExtension() {
    SourceFile sourceFile = new OnDisk("test");

    String expectedFileName = "test.js";

    assertEquals(expectedFileName, sourceFile.getName());
  }

  @Test
  public void testGivenFileNameWithExtensionReturnsFileWithGivenExtension() {
    SourceFile sourceFile = new OnDisk("test.html");

    String expectedFileName = "test.html";

    assertEquals(expectedFileName, sourceFile.getName());
  }

  @Test
  public void testIsExternReturnsFalseByDefault() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "function test() {}");

    assertFalse(sourceFile.isExtern());
  }

  @Test
  public void testIsExternReturnsTrueWhenSet() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "function test() {}", true);

    assertTrue(sourceFile.isExtern());
  }
}