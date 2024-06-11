import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {
  
  @Test
  public void testConstructor() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    assertEquals("testFile.js", sourceFile.getName());
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testConstructorWithEmptyFileName() {
    SourceFile sourceFile = new SourceFile("");
  }
  
  @Test
  public void testGetLineOffset() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    int lineOffset = sourceFile.getLineOffset(1);
    assertEquals(0, lineOffset);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testGetLineOffsetWithInvalidLineNumber() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    int lineOffset = sourceFile.getLineOffset(0);
  }
  
  @Test
  public void testGetNumLines() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    int numLines = sourceFile.getNumLines();
    assertEquals(1, numLines);
  }
  
  @Test
  public void testGetCode() throws Exception {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = sourceFile.getCode();
    assertNull(code);
  }
  
  @Test
  public void testGetLine() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String line = sourceFile.getLine(1);
    assertNull(line);
  }
  
  @Test
  public void testGetRegion() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    Region region = sourceFile.getRegion(1);
    assertNull(region);
  }
  
  @Test
  public void testToString() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String string = sourceFile.toString();
    assertEquals("testFile.js", string);
  }
  
  @Test
  public void testFromFile() {
    SourceFile sourceFile = SourceFile.fromFile("testFile.js");
    assertEquals("testFile.js", sourceFile.getName());
  }
  
  @Test
  public void testFromCode() {
    SourceFile sourceFile = SourceFile.fromCode("testFile.js", "var x = 10;");
    assertEquals("testFile.js", sourceFile.getName());
  }
  
  @Test
  public void testFromInputStream() throws Exception {
    InputStream inputStream = new ByteArrayInputStream("var x = 10;".getBytes());
    SourceFile sourceFile = SourceFile.fromInputStream("testFile.js", inputStream);
    assertEquals("testFile.js", sourceFile.getName());
  }
  
  @Test
  public void testFromReader() throws Exception {
    Reader reader = new StringReader("var x = 10;");
    SourceFile sourceFile = SourceFile.fromReader("testFile.js", reader);
    assertEquals("testFile.js", sourceFile.getName());
  }
  
  @Test
  public void testFromGenerator() {
    SourceFile.Generator generator = new SourceFile.Generator() {
      @Override
      public String getCode() {
        return "var x = 10;";
      }
    };
    SourceFile sourceFile = SourceFile.fromGenerator("testFile.js", generator);
    assertEquals("testFile.js", sourceFile.getName());
  }
}