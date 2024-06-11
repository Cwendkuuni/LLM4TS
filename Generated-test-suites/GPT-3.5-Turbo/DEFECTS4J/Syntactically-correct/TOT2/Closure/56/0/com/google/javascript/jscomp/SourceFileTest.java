import static org.junit.Assert.*;
import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testConstructor() {
    SourceFile sourceFile = new SourceFile("testFile.java");
    assertEquals("testFile.java", sourceFile.getName());
  }

  @Test
  public void testGetLineOffset() {
    SourceFile sourceFile = new SourceFile("testFile.java");
    int offset = sourceFile.getLineOffset(1);
    assertEquals(0, offset);
  }

  @Test
  public void testGetNumLines() {
    SourceFile sourceFile = new SourceFile("testFile.java");
    int numLines = sourceFile.getNumLines();
    assertEquals(0, numLines);
  }
  
  @Test
  public void testGetLine() {
    SourceFile sourceFile = new SourceFile("testFile.java");
    String line = sourceFile.getLine(1);
    assertNull(line);
  }
  
  @Test
  public void testGetRegion() {
    SourceFile sourceFile = new SourceFile("testFile.java");
    Region region = sourceFile.getRegion(1);
    assertNull(region);
  }

  @Test
  public void testFromFilePath() {
    SourceFile sourceFile = SourceFile.fromFile("testFile.java");
    assertNotNull(sourceFile);
  }

  @Test
  public void testFromCode() {
    SourceFile sourceFile = SourceFile.fromCode("testFile.java", "public class Test {}");
    assertNotNull(sourceFile);
  }

  @Test
  public void testFromInputStream() {
    SourceFile sourceFile = SourceFile.fromInputStream("testFile.java", getClass().getResourceAsStream("Test.java"));
    assertNotNull(sourceFile);
  }

  @Test
  public void testFromReader() {
    SourceFile sourceFile = SourceFile.fromReader("testFile.java", new StringReader("public class Test {}"));
    assertNotNull(sourceFile);
  }

  @Test
  public void testGetCode() {
    SourceFile sourceFile = new SourceFile("testFile.java");
    String code = null;
    try {
      code = sourceFile.getCode();
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertNull(code);
  }

}