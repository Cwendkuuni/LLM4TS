import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

  @Test
  public void testConstructor() {
    SourceFile sourceFile = new SourceFile("testFile");
    assertNotNull(sourceFile);
  }

  @Test
  public void testGetCode() {
    SourceFile sourceFile = new SourceFile("testFile");
    try {
      String code = sourceFile.getCode();
      assertNotNull(code);
    } catch (IOException e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testGetLineOffset() {
    SourceFile sourceFile = new SourceFile("testFile");
    try {
      int offset = sourceFile.getLineOffset(1);
      assertEquals(0, offset);
    } catch (IOException e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testGetLine() {
    SourceFile sourceFile = new SourceFile("testFile");
    String line = sourceFile.getLine(1);
    assertNotNull(line);
  }

  @Test
  public void testGetRegion() {
    SourceFile sourceFile = new SourceFile("testFile");
    Region region = sourceFile.getRegion(1);
    assertNotNull(region);
  }

  @Test
  public void testIsExtern() {
    SourceFile sourceFile = new SourceFile("testFile");
    assertFalse(sourceFile.isExtern());
  }

  @Test
  public void testSetIsExtern() {
    SourceFile sourceFile = new SourceFile("testFile");
    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testGetName() {
    SourceFile sourceFile = new SourceFile("testFile");
    assertEquals("testFile", sourceFile.getName());
  }

  @Test
  public void testFromCode() {
    SourceFile sourceFile = SourceFile.fromCode("testFile", "var x = 1;");
    assertEquals("testFile", sourceFile.getName());
  }

}