import static org.junit.Assert.*;
import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testGetCode() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js");
    String code = "var x = 10;";
    sourceFile.setCode(code);
    
    try {
      assertEquals(code, sourceFile.getCode());
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }

  @Test
  public void testGetCodeReader() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js");
    String code = "var x = 10;";
    sourceFile.setCode(code);
    
    try {
      assertNotNull(sourceFile.getCodeReader());
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }

  @Test
  public void testGetLine() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js");
    String code = "var x = 10;\nvar y = 20;";
    sourceFile.setCode(code);
    
    assertEquals("var x = 10;", sourceFile.getLine(1));
    assertEquals("var y = 20;", sourceFile.getLine(2));
    
    // Edge case: Line number out of bound
    assertNull(sourceFile.getLine(3));
  }

  @Test
  public void testGetRegion() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js");
    String code = "var x = 10;\nvar y = 20;\nvar z = 30;\nvar a = 40;\nvar b = 50;\nvar c = 60;";
    sourceFile.setCode(code);
    
    Region region = sourceFile.getRegion(3);
    assertNotNull(region);
    assertEquals("var y = 20;\nvar z = 30;\nvar a = 40;", region.getContent());
  }

  @Test
  public void testGetLineOffset() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js");
    String code = "var x = 10;\nvar y = 20;\nvar z = 30;\nvar a = 40;\nvar b = 50;\nvar c = 60;";
    sourceFile.setCode(code);
    
    try {
      assertEquals(0, sourceFile.getLineOffset(1));
      assertEquals(12, sourceFile.getLineOffset(2));
      assertEquals(26, sourceFile.getLineOffset(3));
      assertEquals(40, sourceFile.getLineOffset(4));
      assertEquals(54, sourceFile.getLineOffset(5));
      assertEquals(68, sourceFile.getLineOffset(6));
      
      // Edge case: Line number out of bound
      sourceFile.getLineOffset(7);
      fail("IllegalArgumentException should be thrown");
    } catch (IllegalArgumentException e) {
      // Test passed
    }
  }

  @Test
  public void testGetNumLines() {
    // Typical use case
    SourceFile sourceFile = new SourceFile("test.js");
    String code = "var x = 10;\nvar y = 20;\nvar z = 30;\nvar a = 40;\nvar b = 50;\nvar c = 60;";
    sourceFile.setCode(code);
    
    assertEquals(6, sourceFile.getNumLines());
  }

  @Test
  public void testIsExtern() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertFalse(sourceFile.isExtern());
    
    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testGetName() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertEquals("test.js", sourceFile.getName());
  }

  @Test
  public void testGetOriginalPath() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertNull(sourceFile.getOriginalPath());
    
    sourceFile.setOriginalPath("path/to/original.js");
    assertEquals("path/to/original.js", sourceFile.getOriginalPath());
  }

}