import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SourceFileTest {

  @Test
  public void testGetLine() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test() {\n" +
        "  var x = 5;\n" +
        "  return x;\n" +
        "}";
    try {
      sourceFile.setCode(code);
    } catch (IOException e) {
      e.printStackTrace();
    }

    assertEquals("function test() {\n  var x = 5;\n  return x;\n}", sourceFile.getCode());
    assertEquals("function test() {\n  var x = 5;\n  return x;\n}", sourceFile.getLine(1));
    assertEquals("  var x = 5;\n  return x;\n", sourceFile.getLine(2));
    assertNull(sourceFile.getLine(10));
  }

  @Test
  public void testGetRegion() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test() {\n" +
        "  var x = 5;\n" +
        "  return x;\n" +
        "}";
    try {
      sourceFile.setCode(code);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Region region = sourceFile.getRegion(3);
    assertEquals("  var x = 5;\n", region.getCode());
    assertEquals(2, region.getStartLineNumber());
    assertEquals(3, region.getEndLineNumber());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineInvalidLineNumber() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    sourceFile.getLine(10);
  }

  @Test
  public void testGetCode() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test() {\n" +
        "  var x = 5;\n" +
        "  return x;\n" +
        "}";
    sourceFile.setCode(code);
    assertEquals(code, sourceFile.getCode());
  }

  @Test
  public void testGetCodeReader() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test() {\n" +
        "  var x = 5;\n" +
        "  return x;\n" +
        "}";
    try {
      sourceFile.setCode(code);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Reader reader = sourceFile.getCodeReader();
    char[] buffer = new char[code.length()];
    reader.read(buffer);
    assertEquals(code, new String(buffer));
  }

  @Test
  public void testToString() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    assertEquals("testFile.js", sourceFile.toString());
  }
}